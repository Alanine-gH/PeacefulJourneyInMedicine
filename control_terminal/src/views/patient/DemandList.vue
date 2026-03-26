<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>需求发布管理</h2>
      <span class="page-desc">管理患者发布的陪诊需求（user_demand 表）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.title" class="filter-input" placeholder="需求标题" @keyup.enter="loadData">
        <input v-model="query.city" class="filter-input" placeholder="城市" @keyup.enter="loadData">
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待接单</option><option value="2">已接单</option>
          <option value="3">已完成</option><option value="4">已取消</option>
        </select>
        <select v-model="query.type" class="filter-select">
          <option value="">全部类型</option>
          <option value="accompany">陪诊</option>
          <option value="nursing">护理</option>
          <option value="other">其他</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead><tr>
          <th>ID</th><th>用户ID</th><th>标题</th><th>类型</th>
          <th>城市</th><th>预算</th><th>状态</th><th>创建时间</th><th>操作</th>
        </tr></thead>
        <tbody>
          <tr v-if="loading"><td colspan="9" class="empty">加载中...</td></tr>
          <tr v-else-if="list.length===0"><td colspan="9" class="empty">暂无数据</td></tr>
          <tr v-for="row in list" :key="row.id">
            <td class="id-cell">{{ row.id }}</td>
            <td>{{ row.userId }}</td>
            <td class="title-cell">{{ row.title }}</td>
            <td>{{ typeLabel(row.type) }}</td>
            <td>{{ row.city || '-' }}</td>
            <td>{{ row.budget ? '¥' + row.budget : '-' }}</td>
            <td><span class="tag" :class="statusClass(row.status)">{{ statusLabel(row.status) }}</span></td>
            <td>{{ fmtTime(row.createTime) }}</td>
            <td>
              <div class="action-btns">
                <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
                <button class="btn-sm btn-del" @click="handleDelete(row)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button :disabled="query.pageNum<=1" @click="changePage(query.pageNum-1)">上一页</button>
        <span>第 {{ query.pageNum }} 页 / 共 {{ totalPages }} 页（{{ total }} 条）</span>
        <button :disabled="query.pageNum>=totalPages" @click="changePage(query.pageNum+1)">下一页</button>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div v-if="detail.visible" class="modal-mask" @click.self="detail.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>需求详情</span><button @click="detail.visible=false">✕</button></div>
        <div class="modal-body">
          <div class="detail-grid">
            <div v-for="(val,key) in detail.data" :key="key" class="detail-row">
              <span class="detail-label">{{ key }}</span><span class="detail-val">{{ val }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const BASE = 'http://localhost:8080'
function authHeader() { const t = localStorage.getItem('token'); return t ? { Authorization: 'Bearer ' + t } : {} }
async function api(url, opt={}) {
  const res = await fetch(BASE + url, { headers: { 'Content-Type': 'application/json', ...authHeader() }, ...opt })
  return res.json()
}
export default {
  data() {
    return {
      list: [], loading: false, total: 0, totalPages: 1,
      query: { title:'', city:'', status:'', type:'', pageNum:1, pageSize:15 },
      detail: { visible:false, data:{} }
    }
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      this.loading = true
      const p = new URLSearchParams()
      Object.entries(this.query).forEach(([k,v]) => { if(v!=='') p.append(k,v) })
      const res = await api('/demand/list?' + p)
      if (res.code===200 && res.data) {
        this.list = res.data.records || []
        this.total = res.data.total || 0
        this.totalPages = res.data.pages || 1
      }
      this.loading = false
    },
    resetQuery() { this.query = { title:'', city:'', status:'', type:'', pageNum:1, pageSize:15 }; this.loadData() },
    changePage(p) { this.query.pageNum = p; this.loadData() },
    openDetail(row) { this.detail = { visible:true, data: row } },
    async handleDelete(row) {
      if (!confirm(`确定删除需求「${row.title}」？`)) return
      const res = await api(`/demand/delete/${row.id}`, { method:'DELETE' })
      if (res.code===200 || res.code===1) this.loadData()
      else alert(res.msg || '删除失败')
    },
    statusLabel(s) { return {1:'待接单',2:'已接单',3:'已完成',4:'已取消'}[s] || '未知' },
    statusClass(s) { return {1:'tag-blue',2:'tag-orange',3:'tag-green',4:'tag-gray'}[s] || 'tag-gray' },
    typeLabel(t) { return {accompany:'陪诊',nursing:'护理',other:'其他'}[t] || t || '-' },
    fmtTime(v) { if(!v) return '-'; return new Date(v).toLocaleString('zh-CN',{hour12:false}) }
  }
}
</script>

<style scoped>
.page-wrap { padding: 24px; }
.page-header { margin-bottom: 18px; }
.page-header h2 { font-size: 20px; font-weight: 700; color: var(--text-primary); margin: 0 0 4px; }
.page-desc { font-size: 13px; color: var(--text-muted); }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; flex-wrap: wrap; gap: 10px; }
.filters { display: flex; gap: 8px; flex-wrap: wrap; }
.filter-input { padding: 7px 12px; border: 1px solid var(--border-input); border-radius: 6px; font-size: 13px; background: var(--bg-input); color: var(--text-primary); width: 130px; }
.filter-select { padding: 7px 10px; border: 1px solid var(--border-input); border-radius: 6px; font-size: 13px; background: var(--bg-input); color: var(--text-primary); }
.btn-search { padding: 7px 16px; background: #3b82f6; color: #fff; border: none; border-radius: 6px; cursor: pointer; font-size: 13px; }
.btn-reset { padding: 7px 14px; background: var(--bg-btn-reset); color: var(--text-primary); border: 1px solid var(--border-input); border-radius: 6px; cursor: pointer; font-size: 13px; }
.table-card { background: var(--bg-card); border-radius: 12px; border: 1px solid var(--border-card); overflow: hidden; }
.data-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.data-table th { padding: 12px 14px; background: var(--bg-thead); color: var(--text-muted); font-weight: 600; text-align: left; border-bottom: 1px solid var(--border-card); }
.data-table td { padding: 11px 14px; border-bottom: 1px solid var(--border-row); color: var(--text-primary); vertical-align: middle; }
.data-table tr:last-child td { border-bottom: none; }
.data-table tr:hover td { background: var(--bg-row-hover); }
.empty { text-align: center; color: var(--text-muted); padding: 40px; }
.id-cell { font-family: monospace; color: var(--text-muted); font-size: 12px; }
.title-cell { max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.tag { padding: 2px 10px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.tag-blue { background: #dbeafe; color: #1d4ed8; }
.tag-orange { background: #fef3c7; color: #b45309; }
.tag-green { background: #d1fae5; color: #065f46; }
.tag-gray { background: #f3f4f6; color: #6b7280; }
.action-btns { display: flex; gap: 6px; }
.btn-sm { padding: 4px 10px; border: none; border-radius: 4px; cursor: pointer; font-size: 12px; }
.btn-view { background: #eff6ff; color: #2563eb; }
.btn-del { background: #fef2f2; color: #dc2626; }
.pagination { display: flex; align-items: center; justify-content: center; gap: 14px; padding: 14px; font-size: 13px; color: var(--text-muted); }
.pagination button { padding: 5px 14px; border: 1px solid var(--border-input); border-radius: 5px; background: var(--bg-input); color: var(--text-primary); cursor: pointer; }
.pagination button:disabled { opacity: 0.4; cursor: not-allowed; }
.modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.45); z-index: 1000; display: flex; align-items: center; justify-content: center; }
.modal-box { background: var(--bg-card); border-radius: 12px; width: 560px; max-height: 85vh; overflow-y: auto; box-shadow: 0 8px 40px rgba(0,0,0,0.18); }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 16px 20px; border-bottom: 1px solid var(--border-card); font-weight: 600; font-size: 15px; color: var(--text-primary); }
.modal-header button { background: none; border: none; font-size: 18px; cursor: pointer; color: var(--text-muted); }
.modal-body { padding: 20px; }
.detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.detail-row { display: flex; flex-direction: column; padding: 8px; background: var(--bg-thead); border-radius: 6px; }
.detail-label { font-size: 11px; color: var(--text-muted); margin-bottom: 2px; }
.detail-val { font-size: 13px; color: var(--text-primary); word-break: break-all; }
</style>
