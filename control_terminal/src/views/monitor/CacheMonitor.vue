<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>缓存监控</h2>
      <span class="page-desc">Redis 缓存使用情况实时监控</span>
    </div>
    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card" v-for="s in stats" :key="s.label">
        <div class="stat-icon" :style="{background:s.color}">{{ s.icon }}</div>
        <div>
          <div class="stat-val">{{ s.val }}</div>
          <div class="stat-label">{{ s.label }}</div>
        </div>
      </div>
    </div>
    <!-- 基本信息 -->
    <div class="info-card">
      <div class="card-title-row">
        <span class="card-title">📋 基本信息</span>
        <button class="btn-refresh" @click="loadData">↻ 刷新</button>
      </div>
      <table class="base-table">
        <tbody>
        <tr>
          <td class="b-key">Redis 版本</td>
          <td class="b-val">{{ props['redis_version'] || '-' }}</td>
          <td class="b-key">运行模式</td>
          <td class="b-val">{{ props['redis_mode'] === 'standalone' ? '单机' : (props['redis_mode'] || '-') }}</td>
          <td class="b-key">端口</td>
          <td class="b-val">{{ props['tcp_port'] || '6379' }}</td>
          <td class="b-key">客户端数</td>
          <td class="b-val">{{ props['connected_clients'] || '-' }}</td>
        </tr>
        <tr>
          <td class="b-key">运行时间(天)</td>
          <td class="b-val">{{ props['uptime_in_days'] || '-' }}</td>
          <td class="b-key">使用内存</td>
          <td class="b-val">{{ props['used_memory_human'] || '-' }}</td>
          <td class="b-key">使用 CPU</td>
          <td class="b-val">{{ cpuVal }}</td>
          <td class="b-key">内存配置</td>
          <td class="b-val">{{ props['maxmemory_human'] || '-' }}</td>
        </tr>
        <tr>
          <td class="b-key">AOF 是否开启</td>
          <td class="b-val">{{ props['aof_enabled'] === '0' ? '否' : '是' }}</td>
          <td class="b-key">RDB 是否成功</td>
          <td class="b-val">{{ props['rdb_last_bgsave_status'] || '-' }}</td>
          <td class="b-key">Key 数量</td>
          <td class="b-val">{{ dbSize }}</td>
          <td class="b-key">网络入口/出口</td>
          <td class="b-val">{{ props['instantaneous_input_kbps'] || 0 }}kbps /
            {{ props['instantaneous_output_kbps'] || 0 }}kbps
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <!-- 图表区 -->
    <div class="grid-2" style="margin-top:16px">
      <div class="info-card">
        <div class="card-title">🥧 命令统计</div>
        <div ref="commandChart" class="chart-box"></div>
      </div>
      <div class="info-card">
        <div class="card-title">💾 内存信息</div>
        <div ref="memoryChart" class="chart-box"></div>
      </div>
    </div>
    <!-- 缓存 Key 列表 -->
    <div class="info-card" style="margin-top:16px">
      <div class="card-title-row">
        <span class="card-title">🔑 缓存 Key 列表</span>
      </div>
      <table class="data-table">
        <thead>
        <tr>
          <th>Key 前缀</th>
          <th>说明</th>
          <th>数量</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="cacheKeys.length===0">
          <td colspan="4" class="empty">暂无数据</td>
        </tr>
        <tr v-for="k in cacheKeys" :key="k.prefix">
          <td><code class="key-code">{{ k.prefix }}</code></td>
          <td>{{ k.desc }}</td>
          <td><span class="count-badge">{{ k.count }}</span></td>
          <td>
            <button class="btn-del" @click="clearKey(k)">清除</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {getCacheInfo, getCacheNames, clearCacheByName} from '@/api/monitor.js'

export default {
  name: 'CacheMonitor',
  data() {
    return {
      props: {},
      dbSize: '-',
      commandStats: [],
      cacheKeys: [],
      stats: [
        {label: '键总数', val: '-', icon: '🔑', color: 'rgba(16,185,129,0.15)'},
        {label: '命中率', val: '-', icon: '🎯', color: 'rgba(59,130,246,0.15)'},
        {label: '已用内存', val: '-', icon: '💾', color: 'rgba(234,179,8,0.15)'},
        {label: '连接数', val: '-', icon: '🔗', color: 'rgba(167,243,208,0.15)'},
      ],
      _cmdChart: null,
      _memChart: null
    }
  },
  computed: {
    cpuVal() {
      const v = this.props['used_cpu_user_children']
      return v ? parseFloat(v).toFixed(2) : '-'
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.resizeCharts)
  },
  activated() {
    this.loadData()
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this._cmdChart) this._cmdChart.dispose()
    if (this._memChart) this._memChart.dispose()
  },
  methods: {
    async loadData() {
      try {
        const [infoRes, namesRes] = await Promise.all([getCacheInfo(), getCacheNames()])
        const info = (infoRes && infoRes.data) ? infoRes.data : infoRes
        const names = (namesRes && namesRes.data) ? namesRes.data : namesRes
        this.props = info.info || {}
        this.dbSize = info.dbSize || '-'
        this.commandStats = info.commandStats || []
        this.cacheKeys = Array.isArray(names) ? names : []
        this.stats[0].val = info.dbSize || 0
        this.stats[2].val = this.props['used_memory_human'] || '-'
        this.stats[3].val = this.props['connected_clients'] || '-'
        this.$nextTick(() => {
          this.renderCommandChart()
          this.renderMemoryChart()
        })
      } catch (e) {
        console.error('获取缓存信息失败', e)
      }
    },
    renderCommandChart() {
      if (!this.$refs.commandChart) return
      if (!this._cmdChart) this._cmdChart = echarts.init(this.$refs.commandChart)
      const data = this.commandStats.map(item => ({name: item.name, value: parseInt(item.value) || 0}))
      this._cmdChart.setOption({
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(5,13,26,0.92)',
          borderColor: 'rgba(110,231,183,0.3)',
          textStyle: {color: '#e2fef4', fontSize: 12},
          formatter: '{a}<br/>{b}: {c} ({d}%)'
        },
        legend: {
          type: 'scroll', orient: 'vertical', right: '4%', top: 'middle',
          textStyle: {color: 'rgba(167,243,208,0.7)', fontSize: 11},
          pageTextStyle: {color: 'rgba(167,243,208,0.5)'}
        },
        series: [{
          name: '命令', type: 'pie', roseType: 'radius',
          radius: ['15%', '62%'], center: ['38%', '50%'],
          data: data.length ? data : [{name: '暂无数据', value: 1}],
          animationEasing: 'cubicInOut', animationDuration: 800,
          label: {color: 'rgba(167,243,208,0.8)', fontSize: 11},
          labelLine: {lineStyle: {color: 'rgba(110,231,183,0.3)'}},
          itemStyle: {borderColor: 'rgba(5,13,26,0.5)', borderWidth: 2}
        }]
      })
    },
    renderMemoryChart() {
      if (!this.$refs.memoryChart) return
      if (!this._memChart) this._memChart = echarts.init(this.$refs.memoryChart)
      const usedHuman = this.props['used_memory_human'] || '0B'
      const usedBytes = parseInt(this.props['used_memory'] || 0)
      const maxBytes = parseInt(this.props['maxmemory'] || 0) || parseInt(this.props['total_system_memory'] || 0) || 1
      const usedMB = Math.round(usedBytes / 1024 / 1024)
      const maxMB = Math.round(maxBytes / 1024 / 1024) || 512
      const pct = Math.min(Math.round(usedBytes / maxBytes * 100), 100)
      this._memChart.setOption({
        backgroundColor: 'transparent',
        tooltip: {
          backgroundColor: 'rgba(5,13,26,0.92)',
          borderColor: 'rgba(110,231,183,0.3)',
          textStyle: {color: '#e2fef4', fontSize: 12},
          formatter: '内存消耗<br/>已用: ' + usedHuman
        },
        series: [{
          name: '内存', type: 'gauge',
          min: 0, max: maxMB,
          radius: '85%', center: ['50%', '58%'],
          startAngle: 200, endAngle: -20, splitNumber: 5,
          axisLine: {
            lineStyle: {
              width: 18,
              color: [[0.3, 'rgba(16,185,129,0.8)'], [0.6, 'rgba(234,179,8,0.8)'], [1, 'rgba(239,68,68,0.8)']]
            }
          },
          pointer: {itemStyle: {color: '#6ee7b7'}},
          axisTick: {lineStyle: {color: 'rgba(167,243,208,0.3)'}},
          splitLine: {lineStyle: {color: 'rgba(167,243,208,0.3)', width: 2}},
          axisLabel: {color: 'rgba(167,243,208,0.6)', fontSize: 10, distance: 22},
          title: {color: 'rgba(167,243,208,0.7)', fontSize: 12, offsetCenter: [0, '80%']},
          detail: {
            valueAnimation: true,
            formatter: usedHuman + ' (' + pct + '%)',
            color: '#6ee7b7', fontSize: 14, fontWeight: 'bold',
            offsetCenter: [0, '40%']
          },
          data: [{value: usedMB, name: '内存消耗'}]
        }]
      })
    },
    resizeCharts() {
      if (this._cmdChart) this._cmdChart.resize()
      if (this._memChart) this._memChart.resize()
    },
    async clearKey(k) {
      if (!confirm(`确认清除 ${k.prefix}* 缓存？`)) return
      try {
        await clearCacheByName(encodeURIComponent(k.prefix))
        await this.loadData()
      } catch (e) {
        alert('清除失败')
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

.card-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.grid-2 {
  display: grid;
  grid-template-columns:1fr 1fr;
  gap: 16px;
}

/* 基本信息表格 */
.base-table {
  width: 100%;
  border-collapse: collapse;
}

.base-table tr {
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.base-table tr:last-child {
  border-bottom: none;
}

.b-key {
  padding: 9px 12px 9px 0;
  font-size: 12px;
  color: rgba(167, 243, 208, 0.55);
  white-space: nowrap;
  width: 90px;
}

.b-val {
  padding: 9px 20px 9px 0;
  font-size: 13px;
  color: var(--text-input);
  min-width: 80px;
}

/* 图表 */
.chart-box {
  height: 380px;
  width: 100%;
}

/* Key 列表 */
.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  padding: 10px 14px;
  text-align: left;
  font-size: 12px;
  color: var(--text-th);
  font-weight: 500;
  border-bottom: 1px solid var(--border-logo);
}

.data-table td {
  padding: 10px 14px;
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
  padding: 32px !important;
}

.key-code {
  background: var(--bg-hover-nav);
  color: var(--text-accent);
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-family: monospace;
}

.count-badge {
  background: var(--tag-blue-bg);
  color: var(--text-blue);
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 12px;
}

.btn-refresh {
  background: var(--tag-green-bg);
  border: 1px solid rgba(110, 231, 183, 0.3);
  border-radius: 8px;
  padding: 6px 14px;
  color: rgba(110, 231, 183, 0.9);
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.btn-refresh:hover {
  background: var(--bg-btn-add);
}

.btn-del {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 6px;
  padding: 3px 10px;
  color: var(--text-red);
  cursor: pointer;
  font-size: 12px;
}

.btn-del:hover {
  background: rgba(239, 68, 68, 0.2);
}
</style>
