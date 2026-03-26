<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>实名认证</h2>
      <span class="page-desc">审核用户实名认证申请（user_identity_auth JOIN user_user）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.realName" class="filter-input" placeholder="真实姓名" @keyup.enter="loadData">
        <select v-model="query.authStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待审核</option>
          <option value="2">已通过</option>
          <option value="3">已拒绝</option>
        </select>
        <select v-model="query.authType" class="filter-select">
          <option value="">全部类型</option>
          <option value="1">护照</option>
          <option value="2">身份证</option>
          <option value="3">其他</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>真实姓名</th>
          <th>认证类型</th>
          <th>证件号</th>
          <th>认证状态</th>
          <th>认证时间</th>
          <th>申请时间</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="loading">
          <td colspan="9" class="empty">加载中...</td>
        </tr>
        <tr v-else-if="list.length===0">
          <td colspan="9" class="empty">暂无数据</td>
        </tr>
        <tr v-for="row in list" :key="row.id">
          <td class="id-cell">{{ row.id }}</td>
          <td>{{ row.username || '-' }}</td>
          <td>{{ row.realName || '-' }}</td>
          <td><span class="tag tag-blue">{{ authTypeLabel(row.authType) }}</span></td>
          <td>{{ maskIdCard(row.idCardNumber || row.passportNumber) }}</td>
          <td><span class="tag" :class="statusClass(row.authStatus)">{{ statusLabel(row.authStatus) }}</span></td>
          <td>{{ row.authTime ? fmtTime(row.authTime) : '-' }}</td>
          <td>{{ fmtTime(row.createTime) }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-pass" v-if="row.authStatus===1" @click="handleAudit(row,2)">通过</button>
              <button class="btn-sm btn-reject" v-if="row.authStatus===1" @click="handleAudit(row,3)">拒绝</button>
              <button class="btn-sm btn-del" @click="handleDelete(row)">删除</button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination">
        <span class="page-info">共 {{ total }} 条</span>
        <button class="page-btn" :disabled="query.pageNum<=1" @click="query.pageNum--;loadData()">‹</button>
        <span class="page-cur">{{ query.pageNum }}/{{ totalPages }}</span>
        <button class="page-btn" :disabled="query.pageNum>=totalPages" @click="query.pageNum++;loadData()">›</button>
        <select v-model="query.pageSize" class="page-size" @change="query.pageNum=1;loadData()">
          <option value="10">10条/页</option>
          <option value="20">20条/页</option>
          <option value="50">50条/页</option>
        </select>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div class="modal-mask" v-if="detailModal.show" @click.self="detailModal.show=false">
      <div class="modal modal-lg">
        <div class="modal-header"><span>认证详情</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-section">
            <div class="section-title">认证信息</div>
            <div class="detail-grid">
              <div class="detail-item"><span class="dk">用户名</span><span
                  class="dv">{{ detailModal.data.username || '-' }}</span></div>
              <div class="detail-item"><span class="dk">真实姓名</span><span
                  class="dv">{{ detailModal.data.realName || '-' }}</span></div>
              <div class="detail-item"><span class="dk">认证类型</span><span
                  class="dv">{{ authTypeLabel(detailModal.data.authType) }}</span></div>
              <div class="detail-item"><span class="dk">认证状态</span><span
                  class="dv">{{ statusLabel(detailModal.data.authStatus) }}</span></div>
              <div class="detail-item"><span class="dk">身份证号</span><span
                  class="dv">{{ detailModal.data.idCardNumber || '-' }}</span></div>
              <div class="detail-item"><span class="dk">护照号</span><span
                  class="dv">{{ detailModal.data.passportNumber || '-' }}</span></div>
              <div class="detail-item"><span class="dk">认证时间</span><span
                  class="dv">{{ detailModal.data.authTime ? fmtTime(detailModal.data.authTime) : '-' }}</span></div>
              <div class="detail-item"><span class="dk">拒绝原因</span><span
                  class="dv fail">{{ detailModal.data.authFailReason || '-' }}</span></div>
            </div>
          </div>
          <div class="detail-section" v-if="detailModal.data.idCardPhotoFront||detailModal.data.passportPhotoFront">
            <div class="section-title">证件照片</div>
            <div class="photo-grid">
              <div class="photo-item" v-if="detailModal.data.idCardPhotoFront">
                <span class="photo-label">身份证正面</span>
                <img :src="detailModal.data.idCardPhotoFront" class="photo-img" alt="身份证正面">
              </div>
              <div class="photo-item" v-if="detailModal.data.idCardPhotoBack">
                <span class="photo-label">身份证背面</span>
                <img :src="detailModal.data.idCardPhotoBack" class="photo-img" alt="身份证背面">
              </div>
              <div class="photo-item" v-if="detailModal.data.passportPhotoFront">
                <span class="photo-label">护照正面</span>
                <img :src="detailModal.data.passportPhotoFront" class="photo-img" alt="护照正面">
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="detailModal.show=false">关闭</button>
          <button class="btn-pass" v-if="detailModal.data&&detailModal.data.authStatus===1"
                  @click="handleAudit(detailModal.data,2)">审核通过
          </button>
          <button class="btn-reject" v-if="detailModal.data&&detailModal.data.authStatus===1"
                  @click="handleAudit(detailModal.data,3)">审核拒绝
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {getIdentityList, getIdentityDetail, auditIdentity, deleteIdentity} from '@/api/user.js'

export default {
  name: 'Identity',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {realName: '', authStatus: '', authType: '', pageNum: 1, pageSize: 10},
      detailModal: {show: false, data: null}
    }
  },
  computed: {
    totalPages() {
      return Math.max(1, Math.ceil(this.total / this.query.pageSize))
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getIdentityList(this.query)
        const d = (res && res.data) ? res.data : res
        this.list = d.records || d.list || (Array.isArray(d) ? d : [])
        this.total = d.total || this.list.length
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    resetQuery() {
      this.query = {realName: '', authStatus: '', authType: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    async openDetail(row) {
      try {
        const res = await getIdentityDetail(row.id);
        this.detailModal.data = (res && res.data) ? res.data : res;
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async handleAudit(row, status) {
      const label = status === 2 ? '通过' : '拒绝'
      let reason = ''
      if (status === 3) {
        reason = prompt('请输入拒绝原因：') || ''
      }
      if (!confirm('确认审核' + label + '？')) return
      try {
        await auditIdentity(row.id, {status: status, failReason: reason})
        this.detailModal.show = false;
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除该认证记录？')) return
      try {
        await deleteIdentity(row.id);
        this.loadData()
      } catch (e) {
        const msg = e.response && e.response.data && e.response.data.msg
            ? e.response.data.msg
            : (e.message || '删除失败')
        alert('删除失败：' + msg)
      }
    },
    authTypeLabel(t) {
      return {1: '护照', 2: '身份证', 3: '其他'}[t] || '-'
    },
    statusLabel(s) {
      return {1: '待审核', 2: '已通过', 3: '已拒绝'}[s] || '-'
    },
    statusClass(s) {
      return {1: 'tag-yellow', 2: 'tag-green', 3: 'tag-red'}[s] || ''
    },
    maskIdCard(v) {
      if (!v) return '-';
      return v.length > 6 ? v.slice(0, 3) + '***' + v.slice(-3) : v
    },
    fmtTime(t) {
      return t ? new Date(t).toLocaleString('zh-CN') : '-'
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
  margin-bottom: 20px;
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

.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  gap: 12px;
  flex-wrap: wrap;
}

.filters {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  align-items: center;
}

.filter-input {
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 7px 12px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
  width: 150px;
}

.filter-input::placeholder {
  color: var(--text-placeholder);
}

.filter-select {
  background: var(--bg-select);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 7px 10px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
}

.btn-search {
  background: var(--bg-btn-search);
  border: 1px solid var(--border-active-nav);
  border-radius: 7px;
  padding: 7px 14px;
  color: var(--text-accent);
  cursor: pointer;
  font-size: 13px;
}

.btn-reset {
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 7px 14px;
  color: var(--text-sub);
  cursor: pointer;
  font-size: 13px;
}

.table-card {
  background: var(--bg-card);
  border: 1px solid var(--border-main);
  border-radius: 12px;
  overflow: hidden;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  padding: 11px 14px;
  text-align: left;
  font-size: 12px;
  color: var(--text-th);
  font-weight: 500;
  border-bottom: 1px solid var(--border-logo);
  white-space: nowrap;
}

.data-table td {
  padding: 11px 14px;
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

.id-cell {
  color: var(--text-dim);
  font-size: 12px;
}

.tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.tag-blue {
  background: var(--tag-blue-bg);
  color: var(--text-blue);
}

.tag-green {
  background: var(--tag-green-bg);
  color: var(--text-accent);
}

.tag-yellow {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
}

.tag-red {
  background: var(--tag-red-bg);
  color: var(--text-red);
}

.action-btns {
  display: flex;
  gap: 5px;
}

.btn-sm {
  padding: 3px 10px;
  border-radius: 5px;
  font-size: 12px;
  cursor: pointer;
  border: 1px solid transparent;
  white-space: nowrap;
}

.btn-view {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.3);
  color: var(--text-blue);
}

.btn-pass {
  background: var(--tag-green-bg);
  border-color: var(--btn-ok-bd);
  color: var(--text-accent);
  border-radius: 7px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 13px;
}

.btn-reject, .btn-del {
  background: var(--btn-warn-bg);
  border-color: var(--btn-warn-bd);
  color: var(--text-red);
  border-radius: 7px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 13px;
}

.empty {
  text-align: center;
  color: var(--text-empty);
  padding: 40px !important;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  border-top: 1px solid var(--border-page);
}

.page-info {
  font-size: 12px;
  color: var(--text-dim);
}

.page-btn {
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 5px;
  padding: 4px 10px;
  color: var(--text-sub);
  cursor: pointer;
}

.page-btn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.page-cur {
  font-size: 13px;
  color: var(--text-sub);
  min-width: 50px;
  text-align: center;
}

.page-size {
  background: var(--bg-select);
  border: 1px solid var(--border-input);
  border-radius: 5px;
  padding: 4px 8px;
  color: var(--text-input);
  font-size: 12px;
  margin-left: auto;
}

.modal-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: var(--bg-modal);
  border: 1px solid var(--border-modal);
  border-radius: 14px;
  width: 560px;
  max-width: 95vw;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.modal-lg {
  width: 720px;
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-main);
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.modal-close {
  background: none;
  border: none;
  color: var(--text-muted);
  font-size: 18px;
  cursor: pointer;
}

.modal-body {
  padding: 20px;
  overflow-y: auto;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 14px 20px;
  border-top: 1px solid var(--border-main);
}

.btn-cancel {
  background: var(--bg-collapse);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 8px 20px;
  color: var(--text-sub);
  cursor: pointer;
  font-size: 13px;
}

.detail-section {
  margin-bottom: 20px;
}

.section-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-accent);
  letter-spacing: 1px;
  margin-bottom: 12px;
  padding-bottom: 6px;
  border-bottom: 1px solid var(--border-logo);
}

.detail-grid {
  display: grid;
  grid-template-columns:1fr 1fr;
  gap: 10px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.dk {
  font-size: 11px;
  color: var(--text-page-desc);
}

.dv {
  font-size: 13px;
  color: var(--text-input);
}

.fail {
  color: var(--text-red);
}

.photo-grid {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.photo-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.photo-label {
  font-size: 12px;
  color: var(--text-th);
}

.photo-img {
  width: 180px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid var(--border-input);
}
</style>
