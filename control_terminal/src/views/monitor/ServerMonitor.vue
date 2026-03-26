<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>服务监控</h2>
      <span class="page-desc">服务器运行状态与 JVM 信息实时监控</span>
    </div>
    <div class="stats-row">
      <div class="stat-card" v-for="s in stats" :key="s.label">
        <div class="stat-icon" :style="{background:s.color}">{{ s.icon }}</div>
        <div>
          <div class="stat-val">{{ s.val }}</div>
          <div class="stat-label">{{ s.label }}</div>
        </div>
      </div>
    </div>
    <div class="grid-2">
      <div class="info-card">
        <div class="card-title">服务器信息</div>
        <table class="info-table">
          <tr v-for="item in serverInfo" :key="item.key">
            <td class="info-key">{{ item.key }}</td>
            <td class="info-val">{{ item.val }}</td>
          </tr>
        </table>
      </div>
      <div class="info-card">
        <div class="card-title">JVM 信息</div>
        <table class="info-table">
          <tr v-for="item in jvmInfo" :key="item.key">
            <td class="info-key">{{ item.key }}</td>
            <td class="info-val">{{ item.val }}</td>
          </tr>
        </table>
      </div>
    </div>
    <div class="grid-2" style="margin-top:16px">
      <div class="info-card">
        <div class="card-title">CPU / 内存使用率</div>
        <div class="meter-label">CPU</div>
        <div class="gauge-bar">
          <div class="gauge-fill" :style="{width:cpuUsage+'%',background:barColor(cpuUsage)}"></div>
        </div>
        <div class="meter-pct" :style="{color:barColor(cpuUsage)}">{{ cpuUsage }}%</div>
        <div class="meter-label" style="margin-top:16px">内存</div>
        <div class="gauge-bar">
          <div class="gauge-fill" :style="{width:memUsage+'%',background:barColor(memUsage)}"></div>
        </div>
        <div class="meter-pct" :style="{color:barColor(memUsage)}">{{ memUsage }}%</div>
      </div>
      <div class="info-card">
        <div class="card-title">磁盘使用情况</div>
        <div v-for="disk in disks" :key="disk.path" style="margin-bottom:16px">
          <div class="disk-header"><span class="disk-path">{{ disk.path }}</span><span class="disk-size">{{ disk.used }} / {{
              disk.total
            }}</span></div>
          <div class="gauge-bar">
            <div class="gauge-fill" :style="{width:disk.pct+'%',background:barColor(disk.pct)}"></div>
          </div>
          <div class="meter-pct" :style="{color:barColor(disk.pct)}">{{ disk.pct }}%</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ServerMonitor',
  data() {
    return {
      cpuUsage: 0,
      memUsage: 0,
      stats: [
        {label: 'CPU 使用率', val: '-', icon: '⚡', color: 'rgba(16,185,129,0.15)'},
        {label: '内存使用率', val: '-', icon: '💾', color: 'rgba(59,130,246,0.15)'},
        {label: '运行时长', val: '-', icon: '⏱', color: 'rgba(234,179,8,0.15)'},
        {label: 'Java 版本', val: '-', icon: '☕', color: 'rgba(167,243,208,0.15)'},
      ],
      serverInfo: [],
      jvmInfo: [],
      disks: []
    }
  },
  mounted() {
    this.loadData()
  },
  activated() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const {getServerInfo} = await import('@/api/monitor.js')
        const res = await getServerInfo()
        // auth.js 响应拦截器已返回 response.data，即 R<T> 对象
        // 所以 res = { code, msg, data } ，再取 res.data 得到实际数据
        const d = (res && res.data) ? res.data : res
        const jvm = d.jvm || {}
        const sys = d.sys || {}
        this.cpuUsage = sys.cpuUsagePercent || 0
        this.memUsage = jvm.memUsagePercent || 0
        this.stats[0].val = this.cpuUsage + '%'
        this.stats[1].val = this.memUsage + '%'
        this.stats[2].val = this.formatUptime(jvm.uptimeMinutes)
        this.stats[3].val = 'JDK ' + (jvm.javaVersion || '-')
        this.serverInfo = [
          {key: '操作系统', val: sys.osName || '-'},
          {key: '系统架构', val: sys.osArch || '-'},
          {key: 'CPU 核心数', val: sys.availableProcessors || '-'},
          {key: 'CPU 负载', val: sys.systemLoadAverage != null ? Number(sys.systemLoadAverage).toFixed(2) : '-'},
          {key: '物理内存', val: sys.totalPhysicalMemoryMB ? sys.totalPhysicalMemoryMB + ' MB' : '-'},
          {key: '可用内存', val: sys.freePhysicalMemoryMB ? sys.freePhysicalMemoryMB + ' MB' : '-'},
          {key: '内存使用率', val: sys.memUsagePercent ? sys.memUsagePercent + '%' : '-'},
          {key: '工作目录', val: sys.userDir || '-'},
        ]
        this.jvmInfo = [
          {key: 'Java 版本', val: jvm.javaVersion || '-'},
          {key: 'JVM 名称', val: jvm.jvmName || '-'},
          {key: '启动时间', val: jvm.startTime || '-'},
          {key: '运行时长', val: this.formatUptime(jvm.uptimeMinutes)},
          {key: '堆内存总量', val: jvm.totalMemoryMB ? jvm.totalMemoryMB + ' MB' : '-'},
          {key: '堆内存已用', val: jvm.usedMemoryMB ? jvm.usedMemoryMB + ' MB' : '-'},
          {key: '堆内存最大', val: jvm.maxMemoryMB ? jvm.maxMemoryMB + ' MB' : '-'},
          {key: '内存使用率', val: jvm.memUsagePercent ? jvm.memUsagePercent + '%' : '-'},
        ]
        this.disks = (d.disks || []).map(disk => ({
          path: disk.path, used: disk.usedGB, total: disk.totalGB, pct: disk.usagePercent || 0
        }))
      } catch (e) {
        console.error('获取服务器信息失败', e)
      }
    },
    formatUptime(minutes) {
      if (!minutes) return '-'
      const m = parseInt(minutes)
      const d = Math.floor(m / 1440), h = Math.floor((m % 1440) / 60), min = m % 60
      return `${d}天${h}小时${min}分`
    },
    barColor(v) {
      return v > 80 ? 'rgba(239,68,68,0.8)' : v > 60 ? 'rgba(234,179,8,0.8)' : 'rgba(16,185,129,0.8)'
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
  margin-bottom: 20px;
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
  flex: 1;
  min-width: 140px;
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
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
}

.stat-label {
  font-size: 12px;
  color: var(--text-page-desc);
  margin-top: 4px;
}

.grid-2 {
  display: grid;
  grid-template-columns:1fr 1fr;
  gap: 16px;
}

.info-card {
  background: var(--bg-sidebar);
  border: 1px solid var(--border-nav);
  border-radius: 12px;
  padding: 20px 24px;
}

.card-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-accent);
  letter-spacing: 1px;
  margin-bottom: 14px;
}

.info-table {
  width: 100%;
  border-collapse: collapse;
}

.info-table tr {
  border-bottom: 1px solid var(--border-table-r);
}

.info-table tr:last-child {
  border-bottom: none;
}

.info-key {
  padding: 7px 0;
  font-size: 12px;
  color: var(--text-page-desc);
  width: 130px;
}

.info-val {
  padding: 7px 0;
  font-size: 13px;
  color: var(--text-input);
}

.meter-label {
  font-size: 12px;
  color: var(--text-th);
  margin-bottom: 6px;
}

.meter-pct {
  font-size: 12px;
  text-align: right;
  margin-top: 4px;
}

.gauge-bar {
  height: 8px;
  background: rgba(255, 255, 255, 0.08);
  border-radius: 4px;
  overflow: hidden;
}

.gauge-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s;
}

.disk-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.disk-path {
  font-size: 12px;
  color: var(--text-label);
}

.disk-size {
  font-size: 12px;
  color: var(--text-muted);
}
</style>
