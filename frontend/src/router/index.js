import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    meta: { requiresAuth: true },
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'resident',
        name: 'Resident',
        component: () => import('../views/Resident.vue'),
        meta: { title: '院民档案' }
      },
      {
        path: 'resident-detail/:id',
        name: 'ResidentDetail',
        component: () => import('../views/ResidentDetail.vue'),
        meta: { title: '院民详情' }
      },
      {
        path: 'activity',
        name: 'Activity',
        component: () => import('../views/Activity.vue'),
        meta: { title: '活动管理' }
      },
      {
        path: 'activity-schedule',
        name: 'ActivitySchedule',
        component: () => import('../views/ActivitySchedule.vue'),
        meta: { title: '活动排班' }
      },
      {
        path: 'appointment',
        name: 'Appointment',
        component: () => import('../views/Appointment.vue'),
        meta: { title: '探访预约' }
      },
      {
        path: 'emergency',
        name: 'Emergency',
        component: () => import('../views/Emergency.vue'),
        meta: { title: '突发事件' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('user')
  if (to.meta.requiresAuth && !user) {
    next('/login')
  } else if (to.path === '/login' && user) {
    next('/')
  } else {
    next()
  }
})

export default router
