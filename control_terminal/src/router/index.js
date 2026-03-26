import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  {
    path: '/home',
    component: () => import('@/views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      { path: '/dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue') },
      // 用户管理
      { path: '/user/list', name: 'UserList', component: () => import('@/views/user/UserList.vue') },
      { path: '/user/accompanist', name: 'Accompanist', component: () => import('@/views/user/Accompanist.vue') },
      { path: '/user/identity', name: 'Identity', component: () => import('@/views/user/Identity.vue') },
      // 订单管理
      { path: '/order/list', name: 'OrderList', component: () => import('@/views/order/OrderList.vue') },
      { path: '/order/payment', name: 'Payment', component: () => import('@/views/order/Payment.vue') },
      { path: '/order/evaluation', name: 'Evaluation', component: () => import('@/views/order/Evaluation.vue') },
      // 医疗管理
      { path: '/medical/hospital', name: 'Hospital', component: () => import('@/views/medical/Hospital.vue') },
      { path: '/medical/expert', name: 'Expert', component: () => import('@/views/medical/Expert.vue') },
      { path: '/medical/appointment', name: 'Appointment', component: () => import('@/views/medical/Appointment.vue') },
      { path: '/medical/triage', name: 'Triage', component: () => import('@/views/medical/Triage.vue') },
      // 服务管理
      { path: '/service/package', name: 'Package', component: () => import('@/views/service/Package.vue') },
      { path: '/service/dispatch', name: 'Dispatch', component: () => import('@/views/service/Dispatch.vue') },
      // 患者管理
      { path: '/patient/list', name: 'PatientList', component: () => import('@/views/patient/PatientList.vue') },
      { path: '/patient/demand', name: 'DemandList', component: () => import('@/views/patient/DemandList.vue') },
      // 系统消息
      { path: '/system/message', name: 'MessageList', component: () => import('@/views/system/MessageList.vue') },
      // 支付管理
      { path: '/payment/list', name: 'PaymentList', component: () => import('@/views/payment/PaymentList.vue') },
      // 系统监控
      { path: '/monitor/online', name: 'OnlineUsers', component: () => import('@/views/monitor/OnlineUsers.vue') },
      { path: '/monitor/cache', name: 'CacheMonitor', component: () => import('@/views/monitor/CacheMonitor.vue') },
      { path: '/monitor/server', name: 'ServerMonitor', component: () => import('@/views/monitor/ServerMonitor.vue')},
    ]
  },
  // 404 兜底
  { path: '/:pathMatch(.*)*', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：未登录跳转到登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const publicPages = ['/login', '/register']
  if (!publicPages.includes(to.path) && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
