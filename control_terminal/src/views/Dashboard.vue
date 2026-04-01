<template>
  <div class="page-wrap">
    <div class="page-header">
      <div class="header-left">
        <div class="greeting-badge">{{ greetingIcon }} {{ greeting }}</div>
        <h2>欢迎回来，<span class="name-accent">{{ userName }}</span></h2>
        <p class="page-desc">{{ today }} · 医路安心管理控制台</p>
      </div>
      <div class="header-right">
        <div class="uptime-badge"><span class="pulse-dot"></span>系统运行中</div>
        <div class="time-display">{{ currentTime }}</div>
      </div>
    </div>

    <!-- 统计卡 -->
    <div class="stats-row">
      <div class="stat-card" v-for="s in stats" :key="s.label" :class="s.theme">
        <div class="stat-top">
          <div class="stat-icon">{{ s.icon }}</div>
          <div class="stat-val">{{ loading ? '…' : s.val }}</div>
        </div>
        <div class="stat-label">{{ s.label }}</div>
        <div class="stat-bar">
          <div class="stat-bar-fill" :style="{width:s.pct+'%'}"></div>
        </div>
      </div>
    </div>

    <!-- 中间：环形图 + 最新订单 -->
    <div class="mid-grid">
      <div class="info-card">
        <div class="card-title"><span class="dot dot-green"></span>订单状态分布</div>
        <div class="donut-wrap">
          <svg class="donut-svg" viewBox="0 0 120 120">
            <circle cx="60" cy="60" r="44" fill="none" stroke="var(--border-main)" stroke-width="16"/>
            <circle v-for="(seg,i) in donutSegs" :key="i"
                    cx="60" cy="60" r="44" fill="none"
                    :stroke="seg.color" stroke-width="16"
                    :stroke-dasharray="seg.dash"
                    :stroke-dashoffset="seg.offset"
                    style="transform:rotate(-90deg);transform-origin:60px 60px"/>
            <text x="60" y="56" text-anchor="middle" class="d-val">{{ orderStats.total }}</text>
            <text x="60" y="70" text-anchor="middle" class="d-lbl">总订单</text>
          </svg>
          <div class="donut-legend">
            <div class="leg-item" v-for="seg in donutSegs" :key="seg.label">
              <span class="leg-dot" :style="{background:seg.color}"></span>
              <span class="leg-label">{{ seg.label }}</span>
              <span class="leg-val">{{ seg.count }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="info-card card-wide">
        <div class="card-title">
          <span class="dot dot-blue"></span> 最新订单
          <span class="card-action" @click="$router.push('/order/list')">全部 →</span>
        </div>
        <table class="data-table">
          <thead>
          <tr>
            <th>订单号</th>
            <th>患者</th>
            <th>金额</th>
            <th>状态</th>
            <th>时间</th>
          </tr>
          </thead>
          <tbody>
          <tr v-if="loading">
            <td colspan="5" class="empty">加载中…</td>
          </tr>
          <tr v-else-if="!recentOrders.length">
            <td colspan="5" class="empty">暂无数据</td>
          </tr>
          <tr v-for="o in recentOrders" :key="o.id">
            <td><code class="order-no">{{ shortNo(o.orderNo) }}</code></td>
            <td>
              <div class="uc">
                <div class="av">{{ (o.userName || '?')[0] }}</div>
                {{ o.userName || '-' }}
              </div>
            </td>
            <td><span class="amount">{{ o.currency }} {{ o.totalAmount || '-' }}</span></td>
            <td><span class="tag" :class="osCls(o.orderStatus)">{{ osLabel(o.orderStatus) }}</span></td>
            <td><span class="time-txt">{{ fmt(o.createTime) }}</span></td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <!-- 底部：资源概况 + 快捷入口 + 预约柱图 -->
    <div class="bot-grid">
      <div class="info-card">
        <div class="card-title"><span class="dot dot-yellow"></span>资源概况</div>
        <div class="res-list">
          <div class="res-item" v-for="r in resources" :key="r.label">
            <span class="res-icon">{{ r.icon }}</span>
            <div class="res-info">
              <div class="res-label">{{ r.label }}</div>
              <div class="res-sub">{{ r.sub }}</div>
            </div>
            <div class="res-val">{{ loading ? '…' : r.val }}</div>
          </div>
        </div>
      </div>
      <div class="info-card">
        <div class="card-title"><span class="dot dot-purple"></span>快捷入口</div>
        <div class="quick-grid">
          <div class="quick-item" v-for="q in quickLinks" :key="q.label" @click="$router.push(q.path)">
            <span class="qi-icon">{{ q.icon }}</span>
            <span class="qi-label">{{ q.label }}</span>
          </div>
        </div>
      </div>
      <div class="info-card">
        <div class="card-title"><span class="dot dot-cyan"></span>预约状态分布</div>
        <div class="bar-chart">
          <div class="bar-row" v-for="b in apptBars" :key="b.label">
            <div class="bar-label">{{ b.label }}</div>
            <div class="bar-track">
              <div class="bar-fill" :style="{width:b.pct+'%',background:b.color}"></div>
            </div>
            <div class="bar-count">{{ b.count }}</div>
          </div>
        </div>
      </div>
      <div class="msg-alert" v-if="unreadMsg>0" @click="$router.push('/system/message')">
        <span>🔔</span><span>有 <b>{{ unreadMsg }}</b> 条未读系统消息</span><span class="alert-arrow">→</span>
      </div>
      <div class="msg-alert demand-alert" v-if="pendingDemand>0" @click="$router.push('/patient/demand')">
        <span>📢</span><span>有 <b>{{ pendingDemand }}</b> 条需求待接单</span><span class="alert-arrow">→</span>
      </div>
    </div>
  </div>
</template>

<script>
import {getOrderList, getOrderCount} from '@/api/order.js'
import {getHospitalCount, getExpertList, getAppointmentList} from '@/api/medical.js'
import {getUserCount, getAccompanistCount} from '@/api/user.js'
import { BASE_URL } from '@/config'

export default {
  name: 'Dashboard',
  data() {
    return {
      loading: true,
      currentTime: '',
      unreadMsg: 0,
      pendingDemand: 0,
      today: new Date().toLocaleDateString('zh-CN', {year: 'numeric', month: 'long', day: 'numeric', weekday: 'long'}),
      stats: [
        {label: '总用户数', icon: '👤', val: 0, pct: 0, trend: 0, theme: 'theme-blue', path: '/user/list'},
        {label: '订单总数', icon: '📋', val: 0, pct: 0, trend: 0, theme: 'theme-green', path: '/order/list'},
        {label: '合作医院', icon: '🏥', val: 0, pct: 0, trend: 0, theme: 'theme-yellow', path: '/medical/hospital'},
        {label: '陪诊师数', icon: '🩺', val: 0, pct: 0, trend: 0, theme: 'theme-purple', path: '/user/accompanist'},
        {label: '就诊人数', icon: '🧑', val: 0, pct: 0, trend: 0, theme: 'theme-teal', path: '/patient/list'},
        {label: '需求发布', icon: '📢', val: 0, pct: 0, trend: 0, theme: 'theme-orange', path: '/patient/demand'},
        {label: '支付记录', icon: '💳', val: 0, pct: 0, trend: 0, theme: 'theme-red', path: '/payment/list'},
        {label: '未读消息', icon: '🔔', val: 0, pct: 0, trend: 0, theme: 'theme-cyan', path: '/system/message'},
      ],
      orderStats: {total: 0, pending: 0, serving: 0, done: 0, cancel: 0, other: 0},
      recentOrders: [],
      resources: [
        {icon: '🏥', label: '合作医院', sub: '三甲国际医院', val: 0},
        {icon: '👨‍⚕️', label: '注册专家', sub: '各科室专家', val: 0},
        {icon: '📅', label: '预约总数', sub: '全部预约记录', val: 0},
        {icon: '🩺', label: '陪诊师数', sub: '在职陪诊师', val: 0},
      ],
      apptData: {pending: 0, confirmed: 0, cancelled: 0, done: 0},
      quickLinks: [
        {label: '用户管理', icon: '👥', path: '/user/list'},
        {label: '订单管理', icon: '📋', path: '/order/list'},
        {label: '就诊人', icon: '🧑', path: '/patient/list'},
        {label: '需求发布', icon: '📢', path: '/patient/demand'},
        {label: '医院信息', icon: '🏥', path: '/medical/hospital'},
        {label: '支付记录', icon: '💳', path: '/payment/list'},
        {label: '系统消息', icon: '🔔', path: '/system/message'},
        {label: '服务套餐', icon: '📦', path: '/service/package'},
        {label: '缓存监控', icon: '💾', path: '/monitor/cache'},
        {label: '服务监控', icon: '📡', path: '/monitor/server'},
      ]
    }
  },
  computed: {
    userName() {
      return localStorage.getItem('username') || 'Admin'
    },
    greeting() {
      const h = new Date().getHours()
      if (h < 6) return '夜深了，注意休息'
      if (h < 12) return '早上好'
      if (h < 14) return '中午好'
      if (h < 18) return '下午好'
      return '晚上好'
    },
    greetingIcon() {
      const h = new Date().getHours()
      if (h < 6) return '🌙'
      if (h < 12) return '🌤'
      if (h < 18) return '☀️'
      return '🌆'
    },
    donutSegs() {
      const circ = 2 * Math.PI * 44
      const items = [
        {label: '待付款', count: this.orderStats.pending, color: '#fde047'},
        {label: '服务中', count: this.orderStats.serving, color: '#6ee7b7'},
        {label: '已完成', count: this.orderStats.done, color: '#93c5fd'},
        {label: '已取消', count: this.orderStats.cancel, color: '#fca5a5'},
        {label: '其他', count: this.orderStats.other, color: 'rgba(255,255,255,0.12)'},
      ]
      const total = Math.max(items.reduce((s, i) => s + i.count, 0), 1)
      let off = 0
      return items.map(item => {
        const p = item.count / total
        const seg = {...item, dash: `${p * circ} ${circ}`, offset: -(off * circ)}
        off += p;
        return seg
      })
    },
    apptBars() {
      const d = this.apptData
      const max = Math.max(d.pending, d.confirmed, d.cancelled, d.done, 1)
      return [
        {label: '待确认', count: d.pending, pct: Math.round(d.pending / max * 100), color: '#fde047'},
        {label: '已确认', count: d.confirmed, pct: Math.round(d.confirmed / max * 100), color: '#6ee7b7'},
        {label: '已取消', count: d.cancelled, pct: Math.round(d.cancelled / max * 100), color: '#fca5a5'},
        {label: '已完成', count: d.done, pct: Math.round(d.done / max * 100), color: '#93c5fd'},
      ]
    }
  },
  mounted() {
    this.loadAll()
    this.updateClock()
    this._timer = setInterval(this.updateClock, 1000)
  },
  beforeUnmount() {
    clearInterval(this._timer)
  },
  methods: {
    updateClock() {
      const n = new Date()
      this.currentTime = n.toLocaleTimeString('zh-CN', {hour12: false})
    },
    async loadAll() {
      this.loading = true
      const [uCntR, oCntR, oListR, hCntR, acCntR, eR, aR] = await Promise.allSettled([
        getUserCount(),
        getOrderCount(),
        getOrderList({pageNum: 1, pageSize: 5}),
        getHospitalCount(),
        getAccompanistCount(),
        getExpertList({pageNum: 1, pageSize: 1}),
        getAppointmentList({pageNum: 1, pageSize: 5}),
      ])
      // count接口返回 R<Long>: { code:200, data: N }
      const cnt = r => {
        if (r.status !== 'fulfilled') return 0
        const rv = r.value
        // rv.data 就是 Long整数，可能是 0
        const val = rv?.data
        return (val !== null && val !== undefined && !isNaN(val)) ? Number(val) : 0
      }
      const tot = r => {
        if (r.status !== 'fulfilled') return 0
        const rv = r.value
        const page = rv?.data ?? rv
        return Number(page?.total) || 0
      }
      const recs = r => {
        if (r.status !== 'fulfilled') return []
        const rv = r.value
        const page = rv?.data ?? rv
        return page?.records || page?.list || (Array.isArray(page) ? page : [])
      }
      console.log('[Dashboard] userCount=', uCntR.value, 'hospitalCount=', hCntR.value, 'accompanistCount=', acCntR.value)
      const uT = cnt(uCntR), oT = cnt(oCntR), hT = cnt(hCntR), acT = cnt(acCntR)
      const tok = localStorage.getItem('token') || ''
      const hdr = tok ? {Authorization: 'Bearer ' + tok} : {}
      const safeGet = async (url) => {
        try {
          const r = await fetch(BASE_URL + url, {headers: hdr});
          return await r.json()
        } catch {
          return {code: 0}
        }
      }
      const [patR, demR, msgR, payR] = await Promise.all([
        safeGet('/user/patient/list?page=1&pageSize=1'),
        safeGet('/demand/list?pageNum=1&pageSize=1&status=1'),
        safeGet('/message/unread-count?userId=1'),
        safeGet('/payment/records?userId=1&page=1&pageSize=1'),
      ])
      const patT = patR?.data?.total || 0
      const demT = demR?.data?.total || 0
      const msgU = (msgR?.code === 200 && msgR?.data !== undefined) ? Number(msgR.data) : 0
      const payT = payR?.data?.total || 0
      this.unreadMsg = msgU
      this.pendingDemand = demT
      const vals = [uT, oT, hT, acT, patT, demT, payT, msgU]
      const caps = [500, 500, 100, 100, 300, 100, 500, 200]
      this.stats.forEach((s, i) => {
        if (i < vals.length) {
          s.val = vals[i];
          s.pct = Math.min(Math.round(vals[i] / caps[i] * 100), 100)
        }
      })
      this.stats[0].val = uT;
      this.stats[0].pct = Math.min(uT / 100 * 100, 100)
      this.stats[1].val = oT;
      this.stats[1].pct = Math.min(oT / 200 * 100, 100)
      this.stats[2].val = hT;
      this.stats[2].pct = Math.min(hT / 50 * 100, 100)
      this.stats[3].val = acT;
      this.stats[3].pct = Math.min(acT / 50 * 100, 100)
      this.resources[0].val = hT;
      this.resources[1].val = tot(eR)
      this.resources[2].val = tot(aR);
      this.resources[3].val = acT
      this.recentOrders = recs(oListR).slice(0, 5)
      const os = {total: oT, pending: 0, serving: 0, done: 0, cancel: 0, other: 0}
      // 用真实总数和最新5条分布做估算，total 来自 count 接口（准确）
      recs(oListR).forEach(o => {
        if (o.orderStatus === 1) os.pending++
        else if ([3, 4].includes(o.orderStatus)) os.serving++
        else if ([5].includes(o.orderStatus)) os.done++
        else if ([6, 7, 8].includes(o.orderStatus)) os.cancel++
        else os.other++
      })
      // 若总数远大于5条样本，按比例推算各分布（保持 total 准确）
      const sample = recs(oListR).length
      if (sample > 0 && oT > sample) {
        const ratio = oT / sample
        os.pending = Math.round(os.pending * ratio)
        os.serving = Math.round(os.serving * ratio)
        os.done = Math.round(os.done * ratio)
        os.cancel = Math.round(os.cancel * ratio)
        os.other = Math.max(0, oT - os.pending - os.serving - os.done - os.cancel)
      }
      os.total = oT
      this.orderStats = os
      const ap = {pending: 0, confirmed: 0, cancelled: 0, done: 0}
      recs(aR).forEach(a => {
        if (a.appointmentStatus === 1) ap.pending++
        else if (a.appointmentStatus === 2) ap.confirmed++
        else if (a.appointmentStatus === 3) ap.cancelled++
        else if (a.appointmentStatus === 4) ap.done++
      })
      const aT = tot(aR)
      if (!recs(aR).length && aT > 0) {
        ap.pending = Math.ceil(aT * .3);
        ap.confirmed = Math.ceil(aT * .4);
        ap.done = aT - ap.pending - ap.confirmed
      }
      this.apptData = ap
      this.loading = false
    },
    shortNo(no) {
      return no ? no.substring(0, 3) + '...' + no.slice(-4) : '-'
    },
    fmt(t) {
      return t ? new Date(t).toLocaleDateString('zh-CN') : '-'
    },
    osLabel(s) {
      return {
        1: '待付款',
        2: '已付款',
        3: '已确认',
        4: '服务中',
        5: '已完成',
        6: '已取消',
        7: '退款中',
        8: '已退款'
      }[s] || '-'
    },
    osCls(s) {
      return {
        1: 't-yellow',
        2: 't-blue',
        3: 't-blue',
        4: 't-green',
        5: 't-green',
        6: 't-gray',
        7: 't-red',
        8: 't-gray'
      }[s] || ''
    },
  }
}
</script>
<style scoped>
.page-wrap {
  padding: 24px 32px;
  min-height: 100%;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 22px;
}

.header-left h2 {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 4px;
}

.name-accent {
  color: var(--text-accent);
}

.page-desc {
  font-size: 12px;
  color: var(--text-page-desc);
  margin: 0;
}

.uptime-badge {
  display: flex;
  align-items: center;
  gap: 7px;
  background: var(--tag-green-bg);
  border: 1px solid var(--border-active-nav);
  border-radius: 20px;
  padding: 5px 14px;
  font-size: 12px;
  color: var(--tag-green-text);
}

.pulse-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: var(--text-accent);
  animation: pulse 1.8s ease-in-out infinite;
  flex-shrink: 0;
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: .5;
    transform: scale(.8);
  }
}

.stats-row {
  display: grid;
  grid-template-columns:repeat(4, 1fr);
  gap: 14px;
  margin-bottom: 16px;
}

.stat-card {
  border-radius: 12px;
  padding: 18px 20px;
  border: 1px solid var(--border-nav);
  background: var(--bg-card);
}

.stat-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.stat-icon {
  font-size: 22px;
}

.stat-val {
  font-size: 26px;
  font-weight: 800;
  color: var(--text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: var(--text-muted);
  margin-bottom: 10px;
}

.stat-bar {
  height: 3px;
  background: var(--border-main);
  border-radius: 2px;
  overflow: hidden;
}

.stat-bar-fill {
  height: 100%;
  border-radius: 2px;
  transition: width 1.2s cubic-bezier(.4, 0, .2, 1);
}

.theme-blue {
  border-color: rgba(59, 130, 246, .2);
}

.theme-blue .stat-bar-fill {
  background: #93c5fd;
}

.theme-green {
  border-color: rgba(16, 185, 129, .2);
}

.theme-green .stat-bar-fill {
  background: #6ee7b7;
}

.theme-yellow {
  border-color: rgba(234, 179, 8, .2);
}

.theme-yellow .stat-bar-fill {
  background: #fde047;
}

.theme-purple {
  border-color: rgba(139, 92, 246, .2);
}

.theme-purple .stat-bar-fill {
  background: #c4b5fd;
}

.mid-grid {
  display: grid;
  grid-template-columns:280px 1fr;
  gap: 14px;
  margin-bottom: 14px;
}

.card-wide {
  overflow: auto;
}

.info-card {
  background: var(--bg-card);
  border: 1px solid var(--border-nav);
  border-radius: 12px;
  padding: 18px 20px;
}

.card-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-accent);
  margin-bottom: 14px;
  display: flex;
  align-items: center;
  gap: 7px;
}

.card-action {
  margin-left: auto;
  font-size: 11px;
  color: var(--text-bc-link);
  cursor: pointer;
  font-weight: 400;
}

.dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  flex-shrink: 0;
}

.dot-green {
  background: #6ee7b7;
}

.dot-blue {
  background: #93c5fd;
}

.dot-yellow {
  background: #fde047;
}

.dot-purple {
  background: #c4b5fd;
}

.dot-cyan {
  background: #a5f3fc;
}

.donut-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.donut-svg {
  width: 130px;
  height: 130px;
}

.d-val {
  font-size: 20px;
  font-weight: 800;
  font-family: 'Noto Serif SC', serif;
}

.d-lbl {
  font-size: 10px;
}

.donut-legend {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.leg-item {
  display: flex;
  align-items: center;
  gap: 7px;
  font-size: 12px;
}

.leg-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.leg-label {
  flex: 1;
  color: var(--text-nav);
}

.leg-val {
  color: var(--text-primary);
  font-weight: 600;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  padding: 8px 12px;
  text-align: left;
  font-size: 11px;
  color: var(--text-th);
  border-bottom: 1px solid var(--border-logo);
  white-space: nowrap;
}

.data-table td {
  padding: 8px 12px;
  font-size: 12px;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border-table-r);
}

.data-table tr:last-child td {
  border-bottom: none;
}

.data-table tr:hover td {
  background: var(--bg-hover-row);
}

.order-no {
  font-family: monospace;
  font-size: 11px;
  color: var(--text-accent);
  background: var(--bg-hover-nav);
  padding: 2px 6px;
  border-radius: 3px;
}

.uc {
  display: flex;
  align-items: center;
  gap: 6px;
}

.av {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: var(--bg-avatar);
  color: var(--text-accent);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  flex-shrink: 0;
}

.amount {
  color: var(--text-yellow);
  font-weight: 600;
}

.time-txt {
  color: var(--text-dim);
  font-size: 11px;
}

.empty {
  text-align: center;
  color: var(--text-empty);
  padding: 24px !important;
}

.tag {
  display: inline-block;
  padding: 2px 7px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.t-yellow {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
}

.t-blue {
  background: var(--tag-blue-bg);
  color: var(--text-blue);
}

.t-green {
  background: var(--tag-green-bg);
  color: var(--text-accent);
}

.t-cyan {
  background: rgba(6, 182, 212, .12);
  color: #a5f3fc;
}

.t-gray {
  background: var(--tag-gray-bg);
  color: var(--tag-gray-text);
}

.t-red {
  background: var(--tag-red-bg);
  color: var(--text-red);
}

.bot-grid {
  display: grid;
  grid-template-columns:1fr 1fr 1fr;
  gap: 14px;
}

.res-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.res-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  background: var(--bg-input);
  border-radius: 8px;
  border: 1px solid var(--border-table-r);
}

.res-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.res-info {
  flex: 1;
}

.res-label {
  font-size: 13px;
  color: var(--text-secondary);
  font-weight: 500;
}

.res-sub {
  font-size: 11px;
  color: var(--text-dim);
  margin-top: 2px;
}

.res-val {
  font-size: 18px;
  font-weight: 800;
  color: var(--text-primary);
  min-width: 36px;
  text-align: right;
}

.quick-grid {
  display: grid;
  grid-template-columns:1fr 1fr;
  gap: 8px;
}

.quick-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 9px 12px;
  background: var(--bg-input);
  border: 1px solid var(--border-table-r);
  border-radius: 8px;
  cursor: pointer;
  transition: all .18s;
}

.quick-item:hover {
  background: var(--bg-hover-nav);
  border-color: var(--border-active-nav);
  transform: translateY(-1px);
}

.qi-icon {
  font-size: 16px;
}

.qi-label {
  font-size: 12px;
  color: var(--text-nav);
  flex: 1;
}

.bar-chart {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.bar-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.bar-label {
  font-size: 12px;
  color: var(--text-nav);
  width: 44px;
  flex-shrink: 0;
}

.bar-track {
  flex: 1;
  height: 8px;
  background: var(--border-main);
  border-radius: 4px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 1s cubic-bezier(.4, 0, .2, 1);
}

.bar-count {
  font-size: 12px;
  color: var(--text-primary);
  font-weight: 600;
  min-width: 24px;
  text-align: right;
}

@media (max-width: 1200px) {
  .stats-row {
    grid-template-columns:repeat(2, 1fr);
  }

  .mid-grid {
    grid-template-columns:1fr;
  }

  .bot-grid {
    grid-template-columns:1fr 1fr;
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns:1fr;
  }

  .bot-grid {
    grid-template-columns:1fr;
  }
}

/* greeting + clock */
.greeting-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--text-muted);
  background: var(--bg-input);
  border: 1px solid var(--border-nav);
  border-radius: 20px;
  padding: 3px 12px;
  margin-bottom: 6px
}

.time-display {
  font-size: 13px;
  color: var(--text-muted);
  font-family: monospace;
  letter-spacing: 1px;
  background: var(--bg-input);
  border: 1px solid var(--border-nav);
  border-radius: 8px;
  padding: 4px 10px
}

/* stat trend */
.stat-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px
}

.stat-trend {
  font-size: 11px
}

.trend-up {
  color: #6ee7b7
}

.trend-flat {
  color: var(--text-muted)
}

/* new stat themes */
.theme-teal {
  border-color: rgba(20, 184, 166, .2)
}

.theme-teal .stat-bar-fill {
  background: #5eead4
}

.theme-orange {
  border-color: rgba(249, 115, 22, .2)
}

.theme-orange .stat-bar-fill {
  background: #fb923c
}

.theme-red {
  border-color: rgba(239, 68, 68, .2)
}

.theme-red .stat-bar-fill {
  background: #fca5a5
}

.theme-cyan {
  border-color: rgba(6, 182, 212, .2)
}

.theme-cyan .stat-bar-fill {
  background: #67e8f9
}

/* hospital col */
.hospital-cell {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

/* alert banners */
.msg-alert {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 12px;
  padding: 8px 12px;
  background: rgba(99, 102, 241, .1);
  border: 1px solid rgba(99, 102, 241, .25);
  border-radius: 8px;
  cursor: pointer;
  font-size: 12px;
  color: var(--text-primary);
  transition: background .15s
}

.msg-alert:hover {
  background: rgba(99, 102, 241, .2)
}

.demand-alert {
  background: rgba(249, 115, 22, .1);
  border-color: rgba(249, 115, 22, .25)
}

.demand-alert:hover {
  background: rgba(249, 115, 22, .2)
}

.alert-arrow {
  margin-left: auto
}

/* stat card clickable */
.stat-card {
  cursor: pointer;
  transition: transform .15s, box-shadow .15s
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, .15)
}
</style>



