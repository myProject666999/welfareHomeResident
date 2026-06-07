<template>
  <div class="page-container">
    <div class="page-header">
      <h3>突发事件管理</h3>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>上报事件
      </el-button>
    </div>

    <div class="search-bar">
      <el-select v-model="searchForm.eventType" placeholder="事件类型" style="width: 150px; margin-right: 10px;" clearable>
        <el-option label="生病" value="生病" />
        <el-option label="走失" value="走失" />
        <el-option label="伤亡" value="伤亡" />
        <el-option label="其他" value="其他" />
      </el-select>
      <el-select v-model="searchForm.severityLevel" placeholder="严重等级" style="width: 150px; margin-right: 10px;" clearable>
        <el-option label="一般" value="一般" />
        <el-option label="较重" value="较重" />
        <el-option label="严重" value="严重" />
        <el-option label="特别严重" value="特别严重" />
      </el-select>
      <el-select v-model="searchForm.status" placeholder="处理状态" style="width: 150px; margin-right: 10px;" clearable>
        <el-option label="待处理" value="待处理" />
        <el-option label="处理中" value="处理中" />
        <el-option label="已处理" value="已处理" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="eventTitle" label="事件标题" width="200" show-overflow-tooltip />
      <el-table-column prop="eventType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag>{{ row.eventType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="severityLevel" label="严重等级" width="100">
        <template #default="{ row }">
          <el-tag :type="getSeverityType(row.severityLevel)">{{ row.severityLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="residentName" label="涉及院民" width="100" />
      <el-table-column prop="eventTime" label="发生时间" width="160" />
      <el-table-column prop="location" label="地点" width="120" />
      <el-table-column prop="reporter" label="上报人" width="100" />
      <el-table-column prop="reportLevel" label="上报级别" width="100">
        <template #default="{ row }">
          <el-tag>{{ row.reportLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '已处理' ? 'success' : row.status === '处理中' ? 'warning' : 'danger'">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
          <template v-if="row.status !== '已处理'">
            <el-button type="success" link @click="openHandleDialog(row)">处理</el-button>
          </template>
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

    <el-dialog v-model="dialogVisible" title="上报突发事件" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="事件类型" prop="eventType">
          <el-select v-model="form.eventType" style="width: 100%;">
            <el-option label="生病" value="生病" />
            <el-option label="走失" value="走失" />
            <el-option label="伤亡" value="伤亡" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="严重等级" prop="severityLevel">
          <el-select v-model="form.severityLevel" style="width: 100%;">
            <el-option label="一般" value="一般" />
            <el-option label="较重" value="较重" />
            <el-option label="严重" value="严重" />
            <el-option label="特别严重" value="特别严重" />
          </el-select>
        </el-form-item>
        <el-form-item label="涉及院民">
          <el-select v-model="form.residentId" style="width: 100%;" filterable clearable>
            <el-option v-for="r in residents" :key="r.id" :label="r.name" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="事件标题" prop="eventTitle">
          <el-input v-model="form.eventTitle" placeholder="简要描述事件" />
        </el-form-item>
        <el-form-item label="详细描述" prop="eventDescription">
          <el-input v-model="form.eventDescription" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="发生时间" prop="eventTime">
          <el-date-picker v-model="form.eventTime" type="datetime" style="width: 100%;" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="发生地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上报人" prop="reporter">
              <el-input v-model="form.reporter" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="form.reporterPhone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="上报级别" prop="reportLevel">
          <el-select v-model="form.reportLevel" style="width: 100%;">
            <el-option label="院内" value="院内" />
            <el-option label="区级" value="区级" />
            <el-option label="市级" value="市级" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交上报</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="handleDialogVisible" title="处理事件" width="500px">
      <el-form :model="handleForm" label-width="100px">
        <el-form-item label="处理人">
          <el-input v-model="handleForm.handler" />
        </el-form-item>
        <el-form-item label="处理过程">
          <el-input v-model="handleForm.handleProcess" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="handleForm.handleResult" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">完成处理</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialogVisible" title="事件详情" width="700px">
      <el-descriptions :column="2" border v-if="currentDetail">
        <el-descriptions-item label="事件类型">
          <el-tag>{{ currentDetail.eventType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="严重等级">
          <el-tag :type="getSeverityType(currentDetail.severityLevel)">{{ currentDetail.severityLevel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="事件标题" :span="2">{{ currentDetail.eventTitle }}</el-descriptions-item>
        <el-descriptions-item label="涉及院民">{{ currentDetail.residentName || '无' }}</el-descriptions-item>
        <el-descriptions-item label="发生时间">{{ currentDetail.eventTime }}</el-descriptions-item>
        <el-descriptions-item label="发生地点">{{ currentDetail.location || '无' }}</el-descriptions-item>
        <el-descriptions-item label="上报级别">
          <el-tag>{{ currentDetail.reportLevel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="详细描述" :span="2">{{ currentDetail.eventDescription }}</el-descriptions-item>
        <el-descriptions-item label="上报人">{{ currentDetail.reporter }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentDetail.reporterPhone || '无' }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ currentDetail.handler || '无' }}</el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="currentDetail.status === '已处理' ? 'success' : 'warning'">{{ currentDetail.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="currentDetail.handleProcess" label="处理过程" :span="2">
          {{ currentDetail.handleProcess }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentDetail.handleResult" label="处理结果" :span="2">
          {{ currentDetail.handleResult }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const dialogVisible = ref(false)
const handleDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const formRef = ref(null)

const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const residents = ref([])
const currentEventId = ref(null)
const currentDetail = ref(null)

const searchForm = reactive({
  eventType: '',
  severityLevel: '',
  status: ''
})

const form = reactive({
  eventType: '',
  severityLevel: '',
  residentId: null,
  eventTitle: '',
  eventDescription: '',
  eventTime: '',
  location: '',
  reporter: '',
  reporterPhone: '',
  reportLevel: '院内'
})

const handleForm = reactive({
  handler: '',
  handleProcess: '',
  handleResult: ''
})

const rules = {
  eventType: [{ required: true, message: '请选择事件类型', trigger: 'change' }],
  severityLevel: [{ required: true, message: '请选择严重等级', trigger: 'change' }],
  eventTitle: [{ required: true, message: '请输入事件标题', trigger: 'blur' }],
  eventDescription: [{ required: true, message: '请输入详细描述', trigger: 'blur' }],
  eventTime: [{ required: true, message: '请选择发生时间', trigger: 'change' }],
  reporter: [{ required: true, message: '请输入上报人', trigger: 'blur' }]
}

const getSeverityType = (level) => {
  const map = {
    '一般': '',
    '较重': 'warning',
    '严重': 'danger',
    '特别严重': 'danger'
  }
  return map[level] || ''
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/emergency/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        eventType: searchForm.eventType,
        severityLevel: searchForm.severityLevel,
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

const openDialog = () => {
  Object.keys(form).forEach(key => {
    if (key === 'reportLevel') form[key] = '院内'
    else if (typeof form[key] === 'number') form[key] = null
    else form[key] = ''
  })
  dialogVisible.value = true
}

const submitForm = async () => {
  await formRef.value.validate()
  form.status = '待处理'
  await request.post('/emergency', form)
  ElMessage.success('事件上报成功')
  dialogVisible.value = false
  loadData()
}

const openHandleDialog = (row) => {
  currentEventId.value = row.id
  Object.assign(handleForm, {
    handler: '',
    handleProcess: '',
    handleResult: ''
  })
  handleDialogVisible.value = true
}

const submitHandle = async () => {
  await request.post(`/emergency/handle/${currentEventId.value}`, null, {
    params: {
      handler: handleForm.handler,
      handleProcess: handleForm.handleProcess,
      handleResult: handleForm.handleResult
    }
  })
  ElMessage.success('处理完成')
  handleDialogVisible.value = false
  loadData()
}

const viewDetail = (row) => {
  currentDetail.value = row
  detailDialogVisible.value = true
}

onMounted(() => {
  loadData()
  loadResidents()
})
</script>
