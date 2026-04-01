<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>就诊人管理</h2>
      <span class="page-desc">管理所有就诊人信息（user_patient 表）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.name" class="filter-input" placeholder="姓名" @keyup.enter="loadData">
        <input v-model="query.phone" class="filter-input" placeholder="手机号" @keyup.enter="loadData">
        <input v-model="query.userId" class="filter-input" placeholder="用户ID" @keyup.enter="loadData">
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">正常</option>
          <option value="2">停用</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增就诊人</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>用户ID</th>
          <th>姓名</th>
          <th>手机号</th>
          <th>性别</th>
          <th>与账户关系</th>
          <th>是否默认</th>
          <th>状态</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="loading">
          <td colspan="10" class="empty">加载中...</td>
        </tr>
        <tr v-else-if="list.length===0">
          <td colspan="10" class="empty">暂无数据</td>
        </tr>
        <tr v-for="row in list" :key="row.id">
          <td class="id-cell">{{ row.id }}</td>
          <td>{{ row.userId }}</td>
          <td>{{ row.name }}</td>
          <td>{{ row.phone || '-' }}</td>
          <td>{{ {0: '未知', 1: '男', 2: '女'}[row.gender] || '-' }}</td>
          <td>{{ row.relation || '-' }}</td>
          <td><span class="tag" :class="row.isDefault===1?'tag-green':'tag-gray'">{{
              row.isDefault === 1 ? '默认' : '否'
            }}</span></td>
          <td><span class="tag" :class="row.status===1?'tag-green':'tag-red'">{{
              row.status === 1 ? '正常' : '停用'
            }}</span></td>
          <td>{{ fmtTime(row.createTime) }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm btn-default" v-if="row.isDefault!==1" @click="setDefault(row)">设默认</button>
              <button class="btn-sm btn-del" @click="handleDelete(row)">删除</button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button :disabled="query.page<=1" @click="changePage(query.page-1)">上一页</button>
        <span>第 {{ query.page }} 页 / 共 {{ totalPages }} 页（{{ total }} 条）</span>
        <button :disabled="query.page>=totalPages" @click="changePage(query.page+1)">下一页</button>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="dialog.visible" class="modal-mask" @click.self="dialog.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>{{ dialog.isEdit ? '编辑就诊人' : '新增就诊人' }}</span>
          <button @click="dialog.visible=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-row form-tip"><span>带 <em>*</em> 为必填（不能为空）；其余字段可留空（后端允许为空）。</span></div>
          <div class="form-row"><label>用户ID *</label><input v-model="form.userId" placeholder="用户ID"></div>
          <div class="form-row"><label>姓名 *</label><input v-model="form.name" placeholder="姓名"></div>
          <div class="form-row"><label>手机号</label><input v-model="form.phone" placeholder="手机号"></div>
          <div class="form-row"><label>身份证号</label><input v-model="form.idCard" placeholder="身份证号"></div>
          <div class="form-row"><label>性别</label>
            <select v-model="form.gender">
              <option :value="0">未知</option>
              <option :value="1">男</option>
              <option :value="2">女</option>
            </select>
          </div>
          <div class="form-row"><label>与账户关系</label><input v-model="form.relation" placeholder="如：本人/父母/子女">
          </div>
          <div class="form-row"><label>地址</label><input v-model="form.address" placeholder="地址"></div>
          <div class="form-row"><label>是否默认</label><span
              class="form-readonly">{{ form.isDefault === 1 ? '是（默认）' : '否' }}<span class="form-readonly-tip">（请通过列表「设默认」按钮修改）</span></span>
          </div>
          <div class="form-row"><label>状态</label>
            <select v-model="form.status">
              <option :value="1">正常</option>
              <option :value="2">停用</option>
            </select>
          </div>
          <div class="form-row"><label>备注</label><input v-model="form.remark" placeholder="备注"></div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="dialog.visible=false">取消</button>
          <button class="btn-confirm" @click="submitForm">{{ dialog.isEdit ? '保存' : '新增' }}</button>
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="detail.visible" class="modal-mask" @click.self="detail.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>就诊人详情</span>
          <button @click="detail.visible=false">✕</button>
        </div>
        <div class="modal-body detail-body">
          <div v-for="(val,key) in detail.data" :key="key" class="detail-row">
            <span class="detail-label">{{ key }}</span><span class="detail-val">{{ val }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
const BASE = 'http://localhost:8080'

function authHeader() {
  const t = localStorage.getItem('token')
  return t ? {Authorization: 'Bearer ' + t} : {}
}

async function api(url, opt = {}) {
  const res = await fetch(BASE + url, {
    headers: {'Content-Type': 'application/json', ...authHeader()},
    ...opt
  })
  return res.json()
}

export default {
  data() {
    return {
      list: [], loading: false, total: 0, totalPages: 1,
      query: {name: '', phone: '', userId: '', status: '', page: 1, pageSize: 15},
      form: {
        userId: '',
        name: '',
        phone: '',
        idCard: '',
        gender: 0,
        relation: '',
        address: '',
        isDefault: 2,
        status: 1,
        remark: ''
      },
      dialog: {visible: false, isEdit: false, editId: null},
      detail: {visible: false, data: {}}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      const p = new URLSearchParams()
      Object.entries(this.query).forEach(([k, v]) => {
        if (v !== '') p.append(k, v)
      })
      const res = await api('/user/patient/list?' + p)
      if (res.code === 200 && res.data) {
        this.list = res.data.records || []
        this.total = res.data.total || 0
        this.totalPages = res.data.pages || 1
      }
      this.loading = false
    },
    resetQuery() {
      this.query = {name: '', phone: '', userId: '', status: '', page: 1, pageSize: 15}
      this.loadData()
    },
    changePage(p) {
      this.query.page = p;
      this.loadData()
    },
    openAdd() {
      this.form = {
        userId: '',
        name: '',
        phone: '',
        idCard: '',
        gender: 0,
        relation: '',
        address: '',
        isDefault: 2,
        status: 1,
        remark: ''
      }
      this.dialog = {visible: true, isEdit: false, editId: null}
    },
    openEdit(row) {
      this.form = {...row}
      this.dialog = {visible: true, isEdit: true, editId: row.id}
    },
    openDetail(row) {
      this.detail = {visible: true, data: row}
    },
    async submitForm() {
      if (!this.form.name) {
        alert('姓名不能为空');
        return
      }
      if (!this.form.userId) {
        alert('用户ID不能为空');
        return
      }
      let res
      if (this.dialog.isEdit) {
        res = await api(`/user/patient/${this.dialog.editId}`, {method: 'PUT', body: JSON.stringify(this.form)})
      } else {
        res = await api('/user/patient', {method: 'POST', body: JSON.stringify(this.form)})
      }
      if (res.code === 200 || res.code === 1) {
        this.dialog.visible = false
        this.loadData()
      } else {
        alert(res.msg || '操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm(`确定删除就诊人「${row.name}」？`)) return
      const res = await api(`/user/patient/${row.id}`, {method: 'DELETE'})
      if (res.code === 200 || res.code === 1) this.loadData()
      else alert(res.msg || '删除失败')
    },
    async setDefault(row) {
      if (!confirm(`确定将「${row.name}」设为默认就诊人？`)) return
      const res = await api(`/user/patient/${row.id}/default?userId=${row.userId}`, {method: 'POST'})
      if (res.code === 200 || res.code === 1) {
        this.loadData()
      } else alert(res.msg || '设置失败')
    },
    fmtTime(v) {
      if (!v) return '-'
      return new Date(v).toLocaleString('zh-CN', {hour12: false})
    }
  }
}
</script>

<style scoped>
.page-wrap {
  padding: 24px;
}

.page-header {
  margin-bottom: 18px;
}

.page-header h2 {
  font-size: 20px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 4px;
}

.page-desc {
  font-size: 13px;
  color: var(--text-muted);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
  flex-wrap: wrap;
  gap: 10px;
}

.filters {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.filter-input {
  padding: 7px 12px;
  border: 1px solid var(--border-input);
  border-radius: 6px;
  font-size: 13px;
  background: var(--bg-input);
  color: var(--text-primary);
  width: 130px;
}

.filter-select {
  padding: 7px 10px;
  border: 1px solid var(--border-input);
  border-radius: 6px;
  font-size: 13px;
  background: var(--bg-input);
  color: var(--text-primary);
}

.btn-search {
  padding: 7px 16px;
  background: #3b82f6;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}

.btn-reset {
  padding: 7px 14px;
  background: var(--bg-btn-reset);
  color: var(--text-primary);
  border: 1px solid var(--border-input);
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}

.btn-add {
  padding: 8px 18px;
  background: #10b981;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
}

.table-card {
  background: var(--bg-card);
  border-radius: 12px;
  border: 1px solid var(--border-card);
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.data-table th {
  padding: 12px 14px;
  background: var(--bg-thead);
  color: var(--text-muted);
  font-weight: 600;
  text-align: left;
  border-bottom: 1px solid var(--border-card);
}

.data-table td {
  padding: 11px 14px;
  border-bottom: 1px solid var(--border-row);
  color: var(--text-primary);
  vertical-align: middle;
}

.data-table tr:last-child td {
  border-bottom: none;
}

.data-table tr:hover td {
  background: var(--bg-row-hover);
}

.empty {
  text-align: center;
  color: var(--text-muted);
  padding: 40px;
}

.id-cell {
  font-family: monospace;
  color: var(--text-muted);
  font-size: 12px;
}

.tag {
  padding: 2px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.tag-green {
  background: #d1fae5;
  color: #065f46;
}

.tag-red {
  background: #fee2e2;
  color: #991b1b;
}

.tag-gray {
  background: #f3f4f6;
  color: #6b7280;
}

.action-btns {
  display: flex;
  gap: 6px;
}

.btn-sm {
  padding: 4px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.btn-view {
  background: #eff6ff;
  color: #2563eb;
}

.btn-edit {
  background: #fefce8;
  color: #b45309;
}

.btn-del {
  background: #fef2f2;
  color: #dc2626;
}

.btn-default {
  background: #f0fdf4;
  color: #16a34a;
}

.form-readonly {
  font-size: 13px;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-readonly-tip {
  font-size: 12px;
  color: #aaa;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 14px;
  padding: 14px;
  font-size: 13px;
  color: var(--text-muted);
}

.pagination button {
  padding: 5px 14px;
  border: 1px solid var(--border-input);
  border-radius: 5px;
  background: var(--bg-input);
  color: var(--text-primary);
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-box {
  background: var(--bg-card);
  border-radius: 12px;
  width: 520px;
  max-height: 85vh;
  overflow-y: auto;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.18);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-card);
  font-weight: 600;
  font-size: 15px;
  color: var(--text-primary);
}

.modal-header button {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: var(--text-muted);
}

.modal-body {
  padding: 20px;
}

.form-row {
  display: flex;
  align-items: center;
  margin-bottom: 14px;
}

.form-row label {
  width: 100px;
  font-size: 13px;
  color: var(--text-muted);
  flex-shrink: 0;
}

.form-row input, .form-row select {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid var(--border-input);
  border-radius: 6px;
  font-size: 13px;
  background: var(--bg-input);
  color: var(--text-primary);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 14px 20px;
  border-top: 1px solid var(--border-card);
}

.btn-cancel {
  padding: 8px 18px;
  border: 1px solid var(--border-input);
  border-radius: 6px;
  background: var(--bg-input);
  color: var(--text-primary);
  cursor: pointer;
}

.btn-confirm {
  padding: 8px 18px;
  background: #3b82f6;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
}

.detail-body {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.detail-row {
  display: flex;
  flex-direction: column;
  padding: 8px;
  background: var(--bg-thead);
  border-radius: 6px;
}

.detail-label {
  font-size: 11px;
  color: var(--text-muted);
  margin-bottom: 2px;
}

.detail-val {
  font-size: 13px;
  color: var(--text-primary);
  word-break: break-all;
}
</style>
