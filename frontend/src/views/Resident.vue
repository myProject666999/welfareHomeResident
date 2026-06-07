<template>
  <div class="page-container">
    <div class="page-header">
      <h3>院民档案管理</h3>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>新增院民
      </el-button>
    </div>

    <div class="search-bar">
      <el-input v-model="searchForm.name" placeholder="输入姓名搜索" style="width: 200px; margin-right: 10px;" clearable />
      <el-select v-model="searchForm.residentType" placeholder="选择院民类型" style="width: 180px; margin-right: 10px;" clearable>
        <el-option label="孤寡老人" value="孤寡老人" />
        <el-option label="孤儿" value="孤儿" />
        <el-option label="特殊困境人员" value="特殊困境人员" />
      </el-select>
      <el-button type="primary" @click="loadData">查询</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="residentType" label="类型" width="130">
        <template #default="{ row }">
          <el-tag>{{ row.residentType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="birthDate" label="出生日期" width="120" />
      <el-table-column prop="idCard" label="身份证号" width="180" />
      <el-table-column prop="healthStatus" label="健康状况" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '在院' ? 'success' : 'info'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="admissionDate" label="入院日期" width="120" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
          <el-button type="primary" link @click="editData(row)">编辑</el-button>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" destroy-on-close>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" style="width: 100%;">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker v-model="form.birthDate" type="date" style="width: 100%;" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input v-model="form.idCard" maxlength="18" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="院民类型" prop="residentType">
              <el-select v-model="form.residentType" style="width: 100%;">
                <el-option label="孤寡老人" value="孤寡老人" />
                <el-option label="孤儿" value="孤儿" />
                <el-option label="特殊困境人员" value="特殊困境人员" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入院日期" prop="admissionDate">
              <el-date-picker v-model="form.admissionDate" type="date" style="width: 100%;" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="健康状况" prop="healthStatus">
          <el-select v-model="form.healthStatus" style="width: 100%;">
            <el-option label="健康" value="健康" />
            <el-option label="良好" value="良好" />
            <el-option label="一般" value="一般" />
            <el-option label="较差" value="较差" />
            <el-option label="重病" value="重病" />
          </el-select>
        </el-form-item>
        <el-form-item label="慢性病">
          <el-input v-model="form.chronicDiseases" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="过敏史">
          <el-input v-model="form.allergies" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="家庭情况">
          <el-input v-model="form.familySituation" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="入院原因">
          <el-input v-model="form.admissionReason" type="textarea" :rows="2" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="紧急联系人">
              <el-input v-model="form.emergencyContactName" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="联系电话">
              <el-input v-model="form.emergencyContactPhone" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="关系">
              <el-input v-model="form.emergencyContactRelation" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="在院" value="在院" />
            <el-option label="离院" value="离院" />
            <el-option label="离世" value="离世" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const router = useRouter()

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增院民')
const formRef = ref(null)
const isEdit = ref(false)

const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchForm = reactive({
  name: '',
  residentType: ''
})

const form = reactive({
  id: null,
  name: '',
  gender: '',
  birthDate: '',
  idCard: '',
  residentType: '',
  healthStatus: '',
  chronicDiseases: '',
  allergies: '',
  familySituation: '',
  admissionReason: '',
  admissionDate: '',
  emergencyContactName: '',
  emergencyContactPhone: '',
  emergencyContactRelation: '',
  status: '在院'
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  residentType: [{ required: true, message: '请选择院民类型', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/resident/page', {
      params: {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        name: searchForm.name,
        residentType: searchForm.residentType
      }
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const openDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增院民'
  Object.keys(form).forEach(key => {
    if (key === 'status') {
      form[key] = '在院'
    } else {
      form[key] = typeof form[key] === 'number' ? null : ''
    }
  })
  dialogVisible.value = true
}

const editData = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑院民'
  Object.assign(form, row)
  dialogVisible.value = true
}

const viewDetail = (row) => {
  router.push(`/resident-detail/${row.id}`)
}

const submitForm = async () => {
  await formRef.value.validate()
  try {
    if (isEdit.value) {
      await request.put('/resident', form)
      ElMessage.success('修改成功')
    } else {
      await request.post('/resident', form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const deleteData = async (row) => {
  await ElMessageBox.confirm(`确定删除院民 "${row.name}" 吗？`, '提示', {
    type: 'warning'
  })
  await request.delete(`/resident/${row.id}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
