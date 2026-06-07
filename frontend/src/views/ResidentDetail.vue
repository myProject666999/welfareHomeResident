<template>
  <div class="page-container">
    <el-button type="primary" link @click="$router.back()" style="margin-bottom: 20px;">
      <el-icon><ArrowLeft /></el-icon>返回
    </el-button>

    <el-card v-if="resident" class="resident-card">
      <template #header>
        <div class="card-header">
          <span>院民详情 - {{ resident.name }}</span>
          <el-tag :type="resident.status === '在院' ? 'success' : 'info'">{{ resident.status }}</el-tag>
        </div>
      </template>

      <el-descriptions :column="3" border>
        <el-descriptions-item label="姓名">{{ resident.name }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ resident.gender }}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{ calculateAge(resident.birthDate) }}岁</el-descriptions-item>
        <el-descriptions-item label="出生日期">{{ resident.birthDate }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ resident.idCard }}</el-descriptions-item>
        <el-descriptions-item label="院民类型">
          <el-tag>{{ resident.residentType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="入院日期">{{ resident.admissionDate }}</el-descriptions-item>
        <el-descriptions-item label="健康状况">{{ resident.healthStatus }}</el-descriptions-item>
        <el-descriptions-item label="户籍地址">{{ resident.address }}</el-descriptions-item>
        <el-descriptions-item label="慢性病" :span="3">{{ resident.chronicDiseases || '无' }}</el-descriptions-item>
        <el-descriptions-item label="过敏史" :span="3">{{ resident.allergies || '无' }}</el-descriptions-item>
        <el-descriptions-item label="家庭情况" :span="3">{{ resident.familySituation || '无' }}</el-descriptions-item>
        <el-descriptions-item label="入院原因" :span="3">{{ resident.admissionReason || '无' }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ resident.emergencyContactName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ resident.emergencyContactPhone }}</el-descriptions-item>
        <el-descriptions-item label="关系">{{ resident.emergencyContactRelation }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-tabs v-model="activeTab" style="margin-top: 20px;">
      <el-tab-pane label="用药记录" name="medication">
        <div class="tab-header">
          <el-button type="primary" size="small" @click="openMedicationDialog">
            <el-icon><Plus /></el-icon>添加用药记录
          </el-button>
        </div>
        <el-table :data="medicationList" style="width: 100%">
          <el-table-column prop="drugName" label="药品名称" />
          <el-table-column prop="dosage" label="剂量" />
          <el-table-column prop="frequency" label="服用频次" />
          <el-table-column prop="startDate" label="开始日期" />
          <el-table-column prop="endDate" label="结束日期" />
          <el-table-column prop="prescribingDoctor" label="开药医生" />
          <el-table-column prop="notes" label="备注" show-overflow-tooltip />
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-button type="danger" link @click="deleteMedication(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="指定亲属" name="relative">
        <div class="tab-header">
          <el-button type="primary" size="small" @click="openRelativeDialog">
            <el-icon><Plus /></el-icon>添加指定亲属
          </el-button>
        </div>
        <el-table :data="relativeList" style="width: 100%">
          <el-table-column prop="name" label="姓名" />
          <el-table-column prop="relation" label="与院民关系" />
          <el-table-column prop="phone" label="联系电话" />
          <el-table-column prop="idCard" label="身份证号" />
          <el-table-column label="是否授权">
            <template #default="{ row }">
              <el-tag :type="row.isAuthorized ? 'success' : 'info'">
                {{ row.isAuthorized ? '已授权' : '未授权' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="{ row }">
              <el-button type="primary" link @click="editRelative(row)">编辑</el-button>
              <el-button type="danger" link @click="deleteRelative(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="medicationDialogVisible" title="用药记录" width="500px">
      <el-form :model="medicationForm" label-width="100px">
        <el-form-item label="药品名称">
          <el-input v-model="medicationForm.drugName" />
        </el-form-item>
        <el-form-item label="剂量">
          <el-input v-model="medicationForm.dosage" />
        </el-form-item>
        <el-form-item label="服用频次">
          <el-input v-model="medicationForm.frequency" placeholder="如：每日3次" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期">
              <el-date-picker v-model="medicationForm.startDate" type="date" style="width: 100%;" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期">
              <el-date-picker v-model="medicationForm.endDate" type="date" style="width: 100%;" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="开药医生">
          <el-input v-model="medicationForm.prescribingDoctor" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="medicationForm.notes" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="medicationDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveMedication">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="relativeDialogVisible" title="指定亲属" width="500px">
      <el-form :model="relativeForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="relativeForm.name" />
        </el-form-item>
        <el-form-item label="与院民关系">
          <el-input v-model="relativeForm.relation" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="relativeForm.phone" />
        </el-form-item>
        <el-form-item label="身份证号">
          <el-input v-model="relativeForm.idCard" maxlength="18" />
        </el-form-item>
        <el-form-item label="是否授权">
          <el-switch v-model="relativeForm.isAuthorized" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="relativeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRelative">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '../utils/request'

const route = useRoute()
const residentId = route.params.id

const resident = ref(null)
const activeTab = ref('medication')
const medicationList = ref([])
const relativeList = ref([])

const medicationDialogVisible = ref(false)
const relativeDialogVisible = ref(false)
const editingRelativeId = ref(null)

const medicationForm = reactive({
  residentId: residentId,
  drugName: '',
  dosage: '',
  frequency: '',
  startDate: '',
  endDate: '',
  prescribingDoctor: '',
  notes: ''
})

const relativeForm = reactive({
  id: null,
  residentId: residentId,
  name: '',
  relation: '',
  phone: '',
  idCard: '',
  isAuthorized: true
})

const calculateAge = (birthDate) => {
  if (!birthDate) return 0
  const today = new Date()
  const birth = new Date(birthDate)
  let age = today.getFullYear() - birth.getFullYear()
  const m = today.getMonth() - birth.getMonth()
  if (m < 0 || (m === 0 && today.getDate() < birth.getDate())) {
    age--
  }
  return age
}

const loadResident = async () => {
  const res = await request.get(`/resident/${residentId}`)
  resident.value = res.data
}

const loadMedication = async () => {
  const res = await request.get(`/medication/list/${residentId}`)
  medicationList.value = res.data
}

const loadRelative = async () => {
  const res = await request.get(`/relative/list/${residentId}`)
  relativeList.value = res.data
}

const openMedicationDialog = () => {
  Object.keys(medicationForm).forEach(key => {
    if (key !== 'residentId') {
      medicationForm[key] = ''
    }
  })
  medicationDialogVisible.value = true
}

const saveMedication = async () => {
  await request.post('/medication', medicationForm)
  ElMessage.success('保存成功')
  medicationDialogVisible.value = false
  loadMedication()
}

const deleteMedication = async (row) => {
  await ElMessageBox.confirm('确定删除该用药记录吗？', '提示', { type: 'warning' })
  await request.delete(`/medication/${row.id}`)
  ElMessage.success('删除成功')
  loadMedication()
}

const openRelativeDialog = () => {
  editingRelativeId.value = null
  Object.keys(relativeForm).forEach(key => {
    if (key === 'residentId') {
      relativeForm[key] = residentId
    } else if (key === 'isAuthorized') {
      relativeForm[key] = true
    } else {
      relativeForm[key] = ''
    }
  })
  relativeDialogVisible.value = true
}

const editRelative = (row) => {
  editingRelativeId.value = row.id
  Object.assign(relativeForm, row)
  relativeDialogVisible.value = true
}

const saveRelative = async () => {
  if (editingRelativeId.value) {
    await request.put('/relative', relativeForm)
  } else {
    await request.post('/relative', relativeForm)
  }
  ElMessage.success('保存成功')
  relativeDialogVisible.value = false
  loadRelative()
}

const deleteRelative = async (row) => {
  await ElMessageBox.confirm('确定删除该亲属吗？', '提示', { type: 'warning' })
  await request.delete(`/relative/${row.id}`)
  ElMessage.success('删除成功')
  loadRelative()
}

onMounted(() => {
  loadResident()
  loadMedication()
  loadRelative()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.tab-header {
  margin-bottom: 15px;
}
</style>
