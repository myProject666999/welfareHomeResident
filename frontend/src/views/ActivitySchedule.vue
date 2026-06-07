<template>
  <div class="page-container">
    <div class="page-header">
      <h3>活动排班表</h3>
    </div>

    <div class="week-selector">
      <el-radio-group v-model="selectedWeek" @change="loadData">
        <el-radio-button :label="1">周一</el-radio-button>
        <el-radio-button :label="2">周二</el-radio-button>
        <el-radio-button :label="3">周三</el-radio-button>
        <el-radio-button :label="4">周四</el-radio-button>
        <el-radio-button :label="5">周五</el-radio-button>
        <el-radio-button :label="6">周六</el-radio-button>
        <el-radio-button :label="7">周日</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="activities" style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="活动名称" width="150" />
      <el-table-column prop="description" label="活动描述" show-overflow-tooltip />
      <el-table-column prop="startTime" label="开始时间" width="120" />
      <el-table-column prop="endTime" label="结束时间" width="120" />
      <el-table-column prop="location" label="地点" width="120" />
      <el-table-column prop="organizer" label="组织者" width="120" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-button type="primary" link @click="openParticipantDialog(row)">参与设置</el-button>
          <el-button type="success" link @click="openCheckinDialog(row)">签到管理</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="participantDialogVisible" title="活动参与设置" width="800px">
      <div style="margin-bottom: 15px;">
        <el-button type="primary" size="small" @click="toggleAllParticipants">
          {{ allChecked ? '取消全选' : '全选可参与' }}
        </el-button>
      </div>
      <el-table :data="residents" style="width: 100%">
        <el-table-column type="selection" v-model="selectedResidents" :selectable="(row) => row.status === '在院'" />
        <el-table-column prop="name" label="姓名" />
        <el-table-column prop="residentType" label="类型" />
        <el-table-column prop="healthStatus" label="健康状况" />
        <el-table-column label="是否可参与">
          <template #default="{ row }">
            <el-switch v-model="row.canParticipate" @change="setParticipant(currentActivity.id, row.id, row.canParticipate)" />
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog v-model="checkinDialogVisible" title="活动签到管理" width="800px">
      <div style="margin-bottom: 15px;">
        <el-date-picker v-model="checkinDate" type="date" style="width: 200px;" value-format="YYYY-MM-DD" @change="loadCheckinData" />
        <el-button type="primary" size="small" style="margin-left: 10px;" @click="loadCheckinData">查询</el-button>
      </div>
      <el-table :data="checkinList" style="width: 100%">
        <el-table-column prop="residentName" label="姓名" />
        <el-table-column prop="checkinTime" label="签到时间" />
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.status === '已签到' ? 'success' : 'info'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row }">
            <el-button type="primary" link @click="doCheckin(row.residentId)" :disabled="row.status === '已签到'">
              签到
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loading = ref(false)
const selectedWeek = ref(new Date().getDay() || 7)
const activities = ref([])
const residents = ref([])
const checkinList = ref([])
const participantDialogVisible = ref(false)
const checkinDialogVisible = ref(false)
const currentActivity = ref(null)
const checkinDate = ref(new Date().toISOString().split('T')[0])
const selectedResidents = ref([])

const allChecked = computed(() => {
  return residents.value.filter(r => r.status === '在院').every(r => r.canParticipate)
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await request.get('/activity/list', { params: { weekDay: selectedWeek.value } })
    activities.value = res.data
  } finally {
    loading.value = false
  }
}

const openParticipantDialog = async (activity) => {
  currentActivity.value = activity
  const residentRes = await request.get('/resident/list')
  const participantRes = await request.get(`/activity-participant/list/${activity.id}`)
  
  residents.value = residentRes.data.map(r => ({
    ...r,
    canParticipate: participantRes.data.find(p => p.residentId === r.id)?.canParticipate === 1
  }))
  participantDialogVisible.value = true
}

const setParticipant = async (activityId, residentId, canParticipate) => {
  await request.post('/activity-participant/set', null, {
    params: { activityId, residentId, canParticipate }
  })
}

const toggleAllParticipants = async () => {
  const shouldCheck = !allChecked.value
  for (const r of residents.value) {
    if (r.status === '在院' && r.canParticipate !== shouldCheck) {
      r.canParticipate = shouldCheck
      await setParticipant(currentActivity.value.id, r.id, shouldCheck)
    }
  }
  ElMessage.success('操作成功')
}

const openCheckinDialog = async (activity) => {
  currentActivity.value = activity
  await loadCheckinData()
  checkinDialogVisible.value = true
}

const loadCheckinData = async () => {
  if (!currentActivity.value) return
  
  const checkinRes = await request.get('/activity-checkin/list', {
    params: {
      activityId: currentActivity.value.id,
      checkinDate: checkinDate.value
    }
  })
  
  const participantRes = await request.get(`/activity-participant/list/${currentActivity.value.id}`)
  const residentRes = await request.get('/resident/list')
  
  const participantIds = participantRes.data.filter(p => p.canParticipate === 1).map(p => p.residentId)
  
  checkinList.value = residentRes.data
    .filter(r => participantIds.includes(r.id) && r.status === '在院')
    .map(r => {
      const checkin = checkinRes.data.find(c => c.residentId === r.id)
      return {
        residentId: r.id,
        residentName: r.name,
        status: checkin?.status || '未签到',
        checkinTime: checkin?.checkinTime || '-'
      }
    })
}

const doCheckin = async (residentId) => {
  await request.post('/activity-checkin/checkin', null, {
    params: {
      activityId: currentActivity.value.id,
      residentId,
      checkinDate: checkinDate.value
    }
  })
  ElMessage.success('签到成功')
  loadCheckinData()
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.week-selector {
  margin-bottom: 20px;
}
</style>
