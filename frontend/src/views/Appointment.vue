<template>
  <div class="page-container">
    <div class="page-header">
      <h3>探访预约管理</h3>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>新增预约
      </el-button>
    </div>

    <div class="search-bar">
      <el-select v-model="searchForm.status" placeholder="选择状态" style="width: 150px; margin-right: 10px;" clearable>
        <el-option label="待审核" value="待审核" />
        <el-option label="已批准" value="已批准" />
        <el-option label="已拒绝" value="已拒绝" />
        <el-option label="已完成" value="已完成" />
        <el-option label="已取消" value="已取消" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="visitorName" label="探访人" width="100" />
      <el-table-column prop="visitorPhone" label="联系电话" width="130" />
      <el-table-column prop="residentName" label="被探访院民" width="120" />
      <el-table-column prop="relation" label="关系" width="100" />
      <el-table-column prop="visitDate" label="探访日期" width="120" />
      <el-table-column label="时间" width="180">
        <template #default="{ row }">
          {{ row.visitStartTime }} - {{ row.visitEndTime }}
        </template>
      </el-table-column>
      <el-table-column prop="visitReason" label="探访事由" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === '待审核'">
            <el-button type="success" link @click="approve(row.id, true)">批准</el-button>
            <el-button type="danger" link @click="approve(row.id, false)">拒绝</el-button>
          </template>
          <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
          <el-button type="danger" link @click="deleteData(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="pageNum"
      v-model:page-size="pageSize"
      :total="total"
      style="margin-top: 20px; justify-content: flex-end;"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="loadData"
      @current-change="loadData"
    />

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="选择院民" prop="residentId">
          <el-select v-model="form.residentId" style="width: 100%;" filterable @change="onResidentChange">
            <el-option v-for="r in residents" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="指定亲属" prop="relativeId">
          <el-select v-model="form.relativeId" style="width: 100%;">
            <el-option v-for="r in relatives" :key="r.id" :label="`${r.name}(${r.relation})`" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="探访人姓名" prop="visitorName">
              <el-input v-model="form.visitorName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="visitorPhone">
              <el-input v-model="form.visitorPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="探访日期" prop="visitDate">
          <el-date-picker v-model="form.visitDate" type="date" style="width: 100%;" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="visitStartTime">
              <el-time-picker v-model="form.visitStartTime" style="width: 100%;" format="HH:mm" value-format="HH:mm:ss" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="visitEndTime">
              <el-time-picker v-model="form.visitEndTime" style="width: 100%;" format="HH:mm" value-format="HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="探访事由">
          <el-input v-model="form.visitReason" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交预约</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="预约详情" width="500px">
      <el-descriptions :column="1" border v-if="currentDetail">
        <el-descriptions-item label="探访人">{{ currentDetail.visitorName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentDetail.visitorPhone }}</el-descriptions-item>
        <el-descriptions-item label="探访日期">{{ currentDetail.visitDate }}</el-descriptions-item>
        <el-descriptions-item label="探访时间">{{ currentDetail.visitStartTime }} - {{ currentDetail.visitEndTime }}</el-descriptions-item>
        <el-descriptions-item label="探访事由">{{ currentDetail.visitReason }}</el-descriptions-item>
        <el-descriptions-item label="审核备注">{{ currentDetail.approvalRemark || '无' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentDetail.status)">{{ currentDetail.status }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const dialogVisible = ref(false)
const detailDialogVisible = ref(false)
const dialogTitle = ref('新增预约')
const formRef = ref(null)

const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const residents = ref([])
const relatives = ref([])
const currentDetail = ref(null)

const searchForm = reactive({
  status: ''
})

const form = reactive({
  residentId: null,
  relativeId: null,
  visitorName: '',
  visitorPhone: '',
  visitDate: '',
  visitStartTime: '09:00:00',
  visitEndTime: '10:00:00',
  visitReason: ''
})

const rules = {
  residentId: [{ required: true, message: '请选择院民', trigger: 'change' }],
  relativeId: [{ required: true, message: '请选择指定亲属', trigger: 'change' }],
  visitorName: [{ required: true, message: '请输入探访人姓名', trigger: 'blur' }],
  visitDate: [{ required: true, message: '请选择探访日期', trigger: 'change' }]
}

const getStatusType = (status) => {
  const map = {
    '待审核': 'warning',
    '已批准': 'success',
    '已拒绝': 'danger',
    '已完成': 'info',
    '已取消': 'info'
  }
  return map[status] || ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/appointment/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        status: searchForm.status
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const loadResidents = async () => {
  const res = await request.get('/resident/list')
  residents.value = res.data.filter(r => r.status === '在院')
}

const onResidentChange = async (residentId) => {
  form.relativeId = null
  const res = await request.get(`/relative/list/${residentId}`)
  relatives.value = res.data
}

const openDialog = () => {
  Object.keys(form).forEach(key => {
    if (key === 'visitStartTime') form[key] = '09:00:00'
    else if (key === 'visitEndTime') form[key] = '10:00:00'
    else if (typeof form[key] === 'number') form[key] = null
    else form[key] = ''
  })
  relatives.value = []
  dialogVisible.value = true
}

const submitForm = async () => {
  await formRef.value.validate()
  try {
    await request.post('/appointment', form)
    ElMessage.success('预约提交成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const approve = async (id, approved) => {
  const { value: remark } = await ElMessageBox.prompt(
    approved ? '请输入批准意见（可选）' : '请输入拒绝原因',
    approved ? '批准预约' : '拒绝预约',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /.*/,
      inputErrorMessage: ''
    }
  ).catch(() => null)
  
  if (remark !== undefined) {
    await request.post(`/appointment/approve/${id}`, null, {
      params: { approved, remark }
    })
    ElMessage.success(approved ? '已批准' : '已拒绝')
    loadData()
  }
}

const viewDetail = (row) => {
  currentDetail.value = row
  detailDialogVisible.value = true
}

const deleteData = async (row) => {
  await ElMessageBox.confirm('确定删除该预约吗？', '提示', { type: 'warning' })
  await request.delete(`/appointment/${row.id}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
  loadResidents()
})
</script>
