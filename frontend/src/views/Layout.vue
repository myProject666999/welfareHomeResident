<template>
  <el-container>
    <el-aside width="220px">
      <div class="logo">
        <h3>福利院管理系统</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical-demo"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/resident">
          <el-icon><User /></el-icon>
          <span>院民档案</span>
        </el-menu-item>
        <el-menu-item index="/activity-schedule">
          <el-icon><Calendar /></el-icon>
          <span>活动排班</span>
        </el-menu-item>
        <el-menu-item index="/activity">
          <el-icon><Tickets /></el-icon>
          <span>活动管理</span>
        </el-menu-item>
        <el-menu-item index="/appointment">
          <el-icon><ChatDotRound /></el-icon>
          <span>探访预约</span>
        </el-menu-item>
        <el-menu-item index="/emergency">
          <el-icon><Warning /></el-icon>
          <span>突发事件</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-left">
          <span>{{ currentTitle }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><User /></el-icon>
              {{ user?.realName || user?.username }}
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

const user = ref(null)
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '首页')

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  }
})

const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    localStorage.removeItem('user')
    ElMessage.success('退出成功')
    router.push('/login')
  }
}
</script>

<style scoped>
.el-header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  border-bottom: 1px solid #e6e6e6;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}

.logo h3 {
  margin: 0;
}

.header-left {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.header-right .user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #666;
}

.el-aside {
  height: 100vh;
  position: sticky;
  top: 0;
  overflow-y: auto;
}

.el-main {
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}
</style>
