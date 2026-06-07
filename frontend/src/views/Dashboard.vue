<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-label">院民总数</p>
              <p class="stat-value">{{ stats.residentCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;">
              <el-icon><Tickets /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-label">活动数量</p>
              <p class="stat-value">{{ stats.activityCount }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C;">
              <el-icon><ChatDotRound /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-label">待审预约</p>
              <p class="stat-value">{{ stats.pendingAppointment }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <p class="stat-label">待处理事件</p>
              <p class="stat-value">{{ stats.pendingEmergency }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :span="12">
        <el-card title="今日活动">
          <el-table :data="todayActivities" style="width: 100%">
            <el-table-column prop="name" label="活动名称" />
            <el-table-column prop="startTime" label="开始时间" />
            <el-table-column prop="endTime" label="结束时间" />
            <el-table-column prop="location" label="地点" />
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button type="primary" link @click="goToActivity">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="近期突发事件">
          <el-table :data="recentEmergencies" style="width: 100%">
            <el-table-column prop="eventTitle" label="事件标题" show-overflow-tooltip />
            <el-table-column prop="eventType" label="类型" />
            <el-table-column prop="severityLevel" label="等级">
              <template #default="{ row }">
                <el-tag :type="getSeverityType(row.severityLevel)">{{ row.severityLevel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../utils/request'

const router = useRouter()

const stats = ref({
  residentCount: 0,
  activityCount: 0,
  pendingAppointment: 0,
  pendingEmergency: 0
})

const todayActivities = ref([])
const recentEmergencies = ref([])

const getSeverityType = (level) => {
  const map = {
    '一般': '',
    '较重': 'warning',
    '严重': 'danger',
    '特别严重': 'danger'
  }
  return map[level] || ''
}

const goToActivity = () => {
  router.push('/activity-schedule')
}

const loadData = async () => {
  try {
    const residentRes = await request.get('/resident/page?pageSize=1')
    stats.value.residentCount = residentRes.data.total

    const activityRes = await request.get('/activity/list')
    stats.value.activityCount = activityRes.data.length
    const today = new Date().getDay() || 7
    todayActivities.value = activityRes.data.filter(a => a.weekDay === today)

    const appointmentRes = await request.get('/appointment/page?status=待审核&pageSize=1')
    stats.value.pendingAppointment = appointmentRes.data.total

    const emergencyRes = await request.get('/emergency/page?status=待处理&pageSize=5')
    stats.value.pendingEmergency = emergencyRes.data.total
    recentEmergencies.value = emergencyRes.data.records || []
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.stat-cards {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 28px;
  margin-right: 16px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.content-row {
  margin-bottom: 20px;
}
</style>
