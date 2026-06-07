<template>
  <div class="page-container">
    <div class="page-header">
      <h3>活动管理</h3>
      <el-button type="primary" @click="openDialog">
        <el-icon><Plus /></el-icon>新增活动
      </el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="活动名称" />
      <el-table-column prop="weekDay" label="星期" width="100">
        <template #default="{ row }">
          {{ weekDays[row.weekDay] }}
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="120" />
      <el-table-column prop="endTime" label="结束时间" width="120" />
      <el-table-column prop="location" label="地点" width="120" />
      <el-table-column prop="organizer" label="组织者" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === '启用' ? 'success' : 'info'">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="editData(row)">编辑</el-button>
          <el-button type="danger" link @click="deleteData(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="活动描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="星期" prop="weekDay">
          <el-select v-model="form.weekDay" style="width: 100%;">
            <el-option label="周一" :value="1" />
            <el-option label="周二" :value="2" />
            <el-option label="周三" :value="3" />
            <el-option label="周四" :value="4" />
            <el-option label="周五" :value="5" />
            <el-option label="周六" :value="6" />
            <el-option label="周日" :value="7" />
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-time-picker v-model="form.startTime" style="width: 100%;" format="HH:mm" value-format="HH:mm:ss" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-time-picker v-model="form.endTime" style="width: 100%;" format="HH:mm" value-format="HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动地点">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="组织者">
          <el-input v-model="form.organizer" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%;">
            <el-option label="启用" value="启用" />
            <el-option label="停用" value="停用" />
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
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const weekDays = {
  1: '周一', 2: '周二', 3: '周三', 4: '周四', 5: '周五', 6: '周六', 7: '周日'
}

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增活动')
const formRef = ref(null)
const isEdit = ref(false)

const tableData = ref([])

const form = reactive({
  id: null,
  name: '',
  description: '',
  weekDay: 1,
  startTime: '09:00:00',
  endTime: '10:00:00',
  location: '',
  organizer: '',
  status: '启用'
})

const rules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  weekDay: [{ required: true, message: '请选择星期', trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/activity/list')
    tableData.value = res.data
  } finally {
    loading.value = false
  }
}

const openDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增活动'
  Object.assign(form, {
    id: null,
    name: '',
    description: '',
    weekDay: 1,
    startTime: '09:00:00',
    endTime: '10:00:00',
    location: '',
    organizer: '',
    status: '启用'
  })
  dialogVisible.value = true
}

const editData = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑活动'
  Object.assign(form, row)
  dialogVisible.value = true
}

const submitForm = async () => {
  await formRef.value.validate()
  try {
    if (isEdit.value) {
      await request.put('/activity', form)
      ElMessage.success('修改成功')
    } else {
      await request.post('/activity', form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const deleteData = async (row) => {
  await ElMessageBox.confirm(`确定删除活动 "${row.name}" 吗？`, '提示', {
    type: 'warning'
  })
  await request.delete(`/activity/${row.id}`)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
