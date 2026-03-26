<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>在线用户</h2>
      <span class="page-desc">实时查看当前登录系统的用户列表</span>
    </div>
    <div class="stats-row">
      <div class="stat-card" v-for="s in stats" :key="s.label">
        <div class="stat-icon" :style="{background: s.color}"><span>{{ s.icon }}</span></div>
        <div>
          <div class="stat-val">{{ s.val }}</div>
          <div class="stat-label">{{ s.label }}</div>
        </div>
      </div>
    </div>
    <div class="table-card">
      <div class="table-toolbar">
        <input v-model="search" class="search-input" placeholder="搜索用户名 / IP">
        <button class="btn-refresh" @click="loadData"><span>↻</span> 刷新</button>
      </div>
      <table class="data-table">
        <thead>
        <tr>
          <th>#</th>
          <th>用户名</th>
          <th>用户类型</th>
          <th>登录IP</th>
          <th>登录地点</th>
          <th>浏览器</th>
          <th>操作系统</th>
          <th>登录时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="filtered.length===0">
          <td colspan="10" class="empty">暂无在线用户</td>
        </tr>
        <tr v-for="(row,i) in filtered" :key="row.tokenId">
          <td>{{ i + 1 }}</td>
          <td><img v-if="row.avatar" :src="getFileUrl(row.avatar)" class="avatar avatar-img" alt=""><span v-else
                                                                                                          class="avatar">{{
              (row.userName || '?')[0]
            }}</span> {{ row.userName || '-' }}
          </td>
          <td><span class="tag" :class="typeClass(row.userType)">{{ typeLabel(row.userType) }}</span></td>
          <td>{{ row.ipaddr || '-' }}</td>
          <td>{{ row.loginLocation || '-' }}</td>
          <td>{{ row.browser || '-' }}</td>
          <td>{{ row.os || '-' }}</td>
          <td>{{ row.loginTime ? new Date(row.loginTime).toLocaleString('zh-CN') : '-' }}</td>
          <td><span class="dot online"></span> 在线</td>
          <td>
            <button class="btn-kick" @click="kick(row)">强退</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import {getOnlineUsers, forceLogout} from '@/api/monitor.js'

export default {
  name: 'OnlineUsers',
  data() {
    return {
      search: '',
      list: [],
      stats: [
        {label: '当前在线', val: 0, icon: '👤', color: 'rgba(16,185,129,0.2)'},
        {label: '今日登录', val: '-', icon: '📅', color: 'rgba(59,130,246,0.2)'},
        {label: '登录失败', val: '-', icon: '⚠️', color: 'rgba(239,68,68,0.2)'},
        {label: '注册用户', val: '-', icon: '🏥', color: 'rgba(167,243,208,0.2)'},
      ]
    }
  },
  computed: {
    filtered() {
      if (!this.search) return this.list
      const q = this.search.toLowerCase()
      return this.list.filter(r =>
          (r.userName || '').toLowerCase().includes(q) || (r.ipaddr || '').includes(q)
      )
    }
  },
  mounted() {
    this.loadData()
    // 每30秒自动刷新一次
    this._timer = setInterval(this.loadData, 30000)
  },
  activated() {
    // keep-alive 激活时也刷新
    this.loadData()
  },
  beforeUnmount() {
    if (this._timer) clearInterval(this._timer)
  },
  methods: {
    async loadData() {
      try {
        const res = await getOnlineUsers()
        // auth.js 响应拦截器已返回 response.data，即 R<T> 对象
        const data = (res && res.data) ? res.data : res
        this.list = Array.isArray(data) ? data : []
        this.stats[0].val = this.list.length
      } catch (e) {
        console.error('获取在线用户失败', e)
      }
    },
    typeLabel(t) {
      return {1: '患者', 2: '陪诊师', 3: '客服', 4: '管理员'}[t] || '用户'
    },
    typeClass(t) {
      return {1: 'tag-patient', 2: 'tag-accomp', 3: 'tag-service', 4: 'tag-admin'}[t] || 'tag-patient'
    },
    async kick(row) {
      if (!confirm(`确认强制退出用户 ${row.userName}？`)) return
      try {
        await forceLogout(row.tokenId)
        this.list = this.list.filter(r => r.tokenId !== row.tokenId)
        this.stats[0].val = this.list.length
      } catch (e) {
        alert('操作失败：' + (e?.message || e))
      }
    }
  }
}
</script>

<style scoped>
.page-wrap {
  padding: 28px 32px;
  min-height: 100%;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
  letter-spacing: 2px;
  margin: 0 0 4px;
}

.page-desc {
  font-size: 12px;
  color: var(--text-page-desc);
}

.stats-row {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 14px;
  background: var(--bg-collapse);
  border: 1px solid var(--border-nav);
  border-radius: 12px;
  padding: 16px 20px;
  min-width: 160px;
  flex: 1;
}

.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.stat-val {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: var(--text-page-desc);
  margin-top: 4px;
}

.table-card {
  background: var(--bg-sidebar);
  border: 1px solid var(--border-nav);
  border-radius: 12px;
  overflow: hidden;
}

.table-toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-logo);
}

.search-input {
  flex: 1;
  max-width: 280px;
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 8px;
  padding: 8px 14px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
}

.search-input::placeholder {
  color: var(--text-placeholder);
}

.btn-refresh {
  background: var(--tag-green-bg);
  border: 1px solid rgba(110, 231, 183, 0.3);
  border-radius: 8px;
  padding: 8px 16px;
  color: rgba(110, 231, 183, 0.9);
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}

.btn-refresh:hover {
  background: var(--bg-btn-add);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  padding: 12px 16px;
  text-align: left;
  font-size: 12px;
  color: var(--text-th);
  font-weight: 500;
  border-bottom: 1px solid var(--border-logo);
  white-space: nowrap;
}

.data-table td {
  padding: 12px 16px;
  font-size: 13px;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border-table-r);
}

.data-table tr:last-child td {
  border-bottom: none;
}

.data-table tr:hover td {
  background: var(--bg-card);
}

.empty {
  text-align: center;
  color: var(--text-empty);
  padding: 40px !important;
}

.avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: var(--bg-avatar);
  color: var(--text-accent);
  font-size: 11px;
  font-weight: 700;
  margin-right: 6px;
}

.tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
}

.tag-patient {
  background: var(--tag-blue-bg);
  color: var(--text-blue);
}

.tag-accomp {
  background: var(--tag-green-bg);
  color: var(--text-accent);
}

.tag-service {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
}

.tag-admin {
  background: var(--tag-red-bg);
  color: var(--text-red);
}

.dot {
  display: inline-block;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  margin-right: 5px;
}

.online {
  background: #10b981;
  box-shadow: 0 0 6px #10b981;
}

.btn-kick {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.25);
  border-radius: 6px;
  padding: 4px 12px;
  color: var(--text-red);
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.btn-kick:hover {
  background: rgba(239, 68, 68, 0.2);
}

.avatar-img {
  object-fit: cover;
  background: none;
}
</style>