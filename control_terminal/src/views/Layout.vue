<template>
  <div class="layout">
    <aside class="sidebar" :class="{collapsed:isCollapsed}">
      <div class="sidebar-logo">
        <div class="logo-icon">
          <svg viewBox="0 0 40 40" fill="none">
            <circle cx="20" cy="20" r="19" stroke="url(#lg1)" stroke-width="2"/>
            <path d="M20 10v20M10 20h20" stroke="url(#lg2)" stroke-width="2.5" stroke-linecap="round"/>
            <defs>
              <linearGradient id="lg1" x1="0" y1="0" x2="40" y2="40">
                <stop offset="0%" stop-color="#6ee7b7"/>
                <stop offset="100%" stop-color="#3b82f6"/>
              </linearGradient>
              <linearGradient id="lg2" x1="0" y1="0" x2="40" y2="40">
                <stop offset="0%" stop-color="#6ee7b7"/>
                <stop offset="100%" stop-color="#3b82f6"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <span v-if="!isCollapsed" class="logo-text">医路安心</span>
      </div>
      <nav class="sidebar-nav">
        <div v-for="menu in menus" :key="menu.key" class="nav-group">
          <div class="nav-item" :class="{active:activeMenu===menu.key, 'has-children':menu.children}"
               @click="toggleMenu(menu)">
            <span class="nav-icon">{{ menu.icon }}</span>
            <span v-if="!isCollapsed" class="nav-label">{{ menu.label }}</span>
            <span v-if="!isCollapsed && menu.children" class="nav-arrow"
                  :class="{open:openMenus.includes(menu.key)}">›</span>
          </div>
          <div v-if="menu.children && openMenus.includes(menu.key) && !isCollapsed" class="sub-menu">
            <div v-for="sub in menu.children" :key="sub.path"
                 class="sub-item" :class="{active:$route.path===sub.path}"
                 @click="navigate(sub.path)">
              <span class="sub-dot"></span>{{ sub.label }}
            </div>
          </div>
        </div>
      </nav>
      <div class="sidebar-footer">
        <button class="collapse-btn" @click="isCollapsed=!isCollapsed">{{ isCollapsed ? '›' : '‹' }}</button>
      </div>
    </aside>
    <div class="main-wrap">
      <header class="topbar">
        <div class="breadcrumb">
          <span class="bc-home">🏥</span>
          <span v-for="(bc,i) in breadcrumbs" :key="i">
            <span class="bc-sep">/</span><span :class="i===breadcrumbs.length-1?'bc-current':'bc-link'">{{ bc }}</span>
          </span>
        </div>
        <div class="topbar-right">
          <!-- Excel 导出下拉 -->
          <div class="export-wrap" ref="exportWrap">
            <button class="export-btn" @click="exportOpen=!exportOpen">⬇ 导出</button>
            <div class="export-dropdown" v-if="exportOpen">
              <div class="export-title">Excel 导出</div>
              <a v-for="e in exportItems" :key="e.path" class="export-item" :href="exportUrl(e.path)" target="_blank"
                 @click="exportOpen=false">{{ e.label }}</a>
            </div>
          </div>
          <!-- 主题切换按钮 -->
          <button class="theme-toggle" @click="toggleTheme" :title="isDark?'切换浅色模式':'切换深色模式'">
            <span class="theme-icon">{{ isDark ? '☀' : '🌙' }}</span>
            <span class="theme-label" v-if="!isMobile">{{ isDark ? '浅色' : '深色' }}</span>
          </button>
          <div class="user-info">
            <img v-if="userAvatar" :src="getFileUrl(userAvatar)" class="user-avatar user-avatar-img" alt="">
            <div v-else class="user-avatar">{{ userName[0] || 'A' }}</div>
            <span v-if="!isMobile" class="user-name">{{ userName }}</span>
          </div>
          <button class="logout-btn" @click="handleLogout">退出</button>
        </div>
      </header>
      <main class="page-main">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<script>
import {useTheme} from '@/theme.js'

export default {
  name: 'Layout',
  setup() {
    const {state, toggle} = useTheme()
    return {themeState: state, toggleTheme: toggle}
  },
  data() {
    return {
      isCollapsed: false,
      isMobile: false,
      exportOpen: false,
      openMenus: ['monitor'],
      currentUser: (() => {
        try {
          return JSON.parse(localStorage.getItem('userInfo') || '{}')
        } catch (_) {
          return {}
        }
      })(),
      exportItems: [
        {label: '📋 订单列表', path: '/export/order'},
        {label: '🏥 医院列表', path: '/export/hospital'},
        {label: '📅 预约列表', path: '/export/appointment'},
        {label: '🔍 分诊记录', path: '/export/triage'},
        {label: '📦 服务套餐', path: '/export/package'},
        {label: '🚗 派单记录', path: '/export/dispatch'},
      ],
      menus: [
        {key: 'dashboard', label: '首页', icon: '🏠', path: '/dashboard'},
        {
          key: 'user', label: '用户管理', icon: '👥', children: [
            {label: '用户列表', path: '/user/list'},
            {label: '陪诊师管理', path: '/user/accompanist'},
            {label: '实名认证', path: '/user/identity'},
          ]
        },
        {
          key: 'order', label: '订单管理', icon: '📋', children: [
            {label: '订单列表', path: '/order/list'},
            {label: '支付记录', path: '/order/payment'},
            {label: '订单评价', path: '/order/evaluation'},
          ]
        },
        {
          key: 'medical', label: '医疗管理', icon: '🏥', children: [
            {label: '医院信息', path: '/medical/hospital'},
            {label: '专家信息', path: '/medical/expert'},
            {label: '预约管理', path: '/medical/appointment'},
            {label: '智能分诊', path: '/medical/triage'},
          ]
        },
        {
          key: 'service', label: '服务管理', icon: '🛎', children: [
            {label: '服务套餐', path: '/service/package'},
            {label: '调度派单', path: '/service/dispatch'},
          ]
        },
        {
          key: 'patient', label: '患者管理', icon: '🧑‍⚕️', children: [
            {label: '就诊人管理', path: '/patient/list'},
            {label: '需求发布', path: '/patient/demand'},
          ]
        },
        {
          key: 'payment', label: '支付管理', icon: '💳', children: [
            {label: '支付记录', path: '/payment/list'},
          ]
        },
        {
          key: 'sysmsg', label: '消息管理', icon: '🔔', children: [
            {label: '系统消息', path: '/system/message'},
          ]
        },
        {
          key: 'monitor', label: '系统监控', icon: '📡', children: [
            {label: '在线用户', path: '/monitor/online'},
            {label: '缓存监控', path: '/monitor/cache'},
            {label: '服务监控', path: '/monitor/server'},
          ]
        },
      ]
    }
  },
  computed: {
    isDark() {
      return this.themeState.isDark
    },
    userName() {
      return this.currentUser.nickName || this.currentUser.username || 'admin'
    },
    userAvatar() {
      return this.currentUser.avatarUrl || ''
    },
    activeMenu() {
      const p = this.$route.path
      for (const m of this.menus) {
        if (m.path === p) return m.key
        if (m.children) for (const s of m.children) if (s.path === p) return m.key
      }
      return ''
    },
    breadcrumbs() {
      const p = this.$route.path
      for (const m of this.menus) {
        if (m.path === p) return [m.label]
        if (m.children) for (const s of m.children) if (s.path === p) return [m.label, s.label]
      }
      return []
    }
  },
  mounted() {
    this.checkMobile()
    window.addEventListener('resize', this.checkMobile)
    document.addEventListener('click', this.closeExport)
    try {
      this.currentUser = JSON.parse(localStorage.getItem('userInfo') || '{}')
    } catch (_) {
      this.currentUser = {}
    }
    const p = this.$route.path
    for (const m of this.menus) {
      if (m.children) for (const s of m.children) {
        if (s.path === p && !this.openMenus.includes(m.key)) this.openMenus.push(m.key)
      }
    }
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.checkMobile)
    document.removeEventListener('click', this.closeExport)
  },
  methods: {
    checkMobile() {
      this.isMobile = window.innerWidth < 768;
      if (this.isMobile) this.isCollapsed = true
    },
    toggleMenu(menu) {
      if (!menu.children) {
        this.navigate(menu.path);
        return
      }
      const i = this.openMenus.indexOf(menu.key)
      if (i > -1) this.openMenus.splice(i, 1)
      else this.openMenus.push(menu.key)
    },
    navigate(path) {
      if (this.$route.path !== path) this.$router.push(path)
    },
    handleLogout() {
      localStorage.removeItem('token');
      this.$router.push('/login')
    },
    exportUrl(path) {
      const base = 'http://localhost:8080'
      const token = localStorage.getItem('token') || ''
      return base + path + (token ? '?token=' + token : '')
    },
    closeExport(e) {
      if (this.$refs.exportWrap && !this.$refs.exportWrap.contains(e.target)) {
        this.exportOpen = false
      }
    },
    getFileUrl(filename) {
      if (!filename) return ''
      if (filename.startsWith('http')) return filename
      return 'http://localhost:8080/common/download?name=' + encodeURIComponent(filename)
    }
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
  background: var(--bg-root);
  font-family: 'Noto Serif SC', serif;
  overflow: hidden;
}

.sidebar {
  width: 220px;
  flex-shrink: 0;
  background: var(--bg-sidebar);
  border-right: 1px solid var(--border-sidebar);
  display: flex;
  flex-direction: column;
  transition: width 0.25s cubic-bezier(0.16, 1, 0.3, 1);
  overflow: hidden;
}

.sidebar.collapsed {
  width: 60px;
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 18px 14px;
  border-bottom: 1px solid var(--border-logo);
  min-height: 64px;
  flex-shrink: 0;
}

.logo-icon {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
}

.logo-text {
  font-size: 15px;
  font-weight: 700;
  color: var(--text-logo);
  letter-spacing: 3px;
  white-space: nowrap;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 10px 0;
}

.sidebar-nav::-webkit-scrollbar {
  width: 3px;
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb);
  border-radius: 2px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  cursor: pointer;
  transition: all 0.18s;
  white-space: nowrap;
}

.nav-item:hover {
  background: var(--bg-hover-nav);
}

.nav-item.active {
  background: var(--bg-active-nav);
  border-right: 2px solid var(--border-active-nav);
}

.nav-item.active .nav-icon, .nav-item.active .nav-label {
  color: var(--text-accent);
}

.nav-icon {
  font-size: 16px;
  flex-shrink: 0;
  width: 20px;
  text-align: center;
}

.nav-label {
  font-size: 13px;
  color: var(--text-nav);
  flex: 1;
}

.nav-arrow {
  font-size: 16px;
  color: var(--text-dim);
  transition: transform 0.2s;
  display: inline-block;
}

.nav-arrow.open {
  transform: rotate(90deg);
}

.sub-menu {
  background: var(--bg-sub-menu);
}

.sub-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px 8px 44px;
  cursor: pointer;
  font-size: 12px;
  color: var(--text-sub);
  transition: all 0.15s;
  white-space: nowrap;
}

.sub-item:hover {
  color: var(--text-accent);
  background: var(--bg-hover-nav);
}

.sub-item.active {
  color: var(--text-accent);
  font-weight: 600;
}

.sub-dot {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: currentColor;
  flex-shrink: 0;
}

.sidebar-footer {
  padding: 12px;
  border-top: 1px solid var(--border-nav);
  flex-shrink: 0;
}

.collapse-btn {
  width: 100%;
  background: var(--bg-collapse);
  border: 1px solid var(--border-nav);
  border-radius: 8px;
  padding: 7px;
  color: var(--text-collapse);
  cursor: pointer;
  font-size: 16px;
}

.collapse-btn:hover {
  background: var(--bg-hover-nav);
  color: var(--text-accent);
}

.main-wrap {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.topbar {
  height: 56px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: var(--bg-topbar);
  border-bottom: 1px solid var(--border-topbar);
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--text-breadcrumb);
}

.bc-home {
  font-size: 14px;
}

.bc-sep {
  margin: 0 4px;
  opacity: 0.4;
}

.bc-link {
  color: var(--text-bc-link);
}

.bc-current {
  color: var(--text-primary);
  font-weight: 500;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.export-wrap {
  position: relative;
}

.export-btn {
  background: var(--bg-btn-search);
  border: 1px solid var(--border-active-nav);
  border-radius: 7px;
  padding: 5px 12px;
  color: var(--text-accent);
  cursor: pointer;
  font-size: 12px;
  font-family: 'Noto Serif SC', serif;
  opacity: 0.9;
}

.export-btn:hover {
  opacity: 1;
}

.export-dropdown {
  position: absolute;
  top: calc(100% + 6px);
  right: 0;
  background: var(--bg-modal);
  border: 1px solid var(--border-modal);
  border-radius: 10px;
  min-width: 160px;
  padding: 6px 0;
  z-index: 200;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
}

.export-title {
  font-size: 11px;
  color: var(--text-muted);
  padding: 6px 14px 4px;
  letter-spacing: 1px;
}

.export-item {
  display: block;
  padding: 7px 14px;
  font-size: 13px;
  color: var(--text-secondary);
  text-decoration: none;
  cursor: pointer;
  white-space: nowrap;
}

.export-item:hover {
  background: var(--bg-hover-nav);
  color: var(--text-accent);
}

.theme-toggle {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--bg-collapse);
  border: 1px solid var(--border-nav);
  border-radius: 20px;
  padding: 5px 12px;
  cursor: pointer;
  font-size: 13px;
  color: var(--text-nav);
}

.theme-toggle:hover {
  background: var(--bg-hover-nav);
  color: var(--text-accent);
  border-color: var(--border-active-nav);
}

.theme-icon {
  font-size: 15px;
  line-height: 1;
}

.theme-label {
  font-size: 12px;
  font-family: 'Noto Serif SC', serif;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: var(--bg-avatar);
  color: var(--text-accent);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
}

.user-name {
  font-size: 13px;
  color: var(--text-user);
}

.logout-btn {
  background: transparent;
  border: 1px solid var(--border-modal);
  border-radius: 7px;
  padding: 5px 14px;
  color: var(--text-logout);
  cursor: pointer;
  font-size: 12px;
  font-family: 'Noto Serif SC', serif;
}

.logout-btn:hover {
  background: var(--bg-hover-nav);
  color: var(--text-accent);
}

.page-main {
  flex: 1;
  overflow-y: auto;
}

.page-main::-webkit-scrollbar {
  width: 4px;
}

.page-main::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb);
  border-radius: 2px;
}

.avatar-img {
  object-fit: cover;
  background: none;
}
</style>