<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>陪诊师管理</h2>
      <span class="page-desc">管理陪诊师资质与状态（user_accompanist JOIN user_user）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.realName" class="filter-input" placeholder="姓名/手机号" @keyup.enter="loadData">
        <select v-model="query.auditStatus" class="filter-select">
          <option value="">全部审核状态</option>
          <option value="1">待审核</option>
          <option value="2">已通过</option>
          <option value="3">已拒绝</option>
        </select>
        <select v-model="query.accompanyStatus" class="filter-select">
          <option value="">全部陪诊状态</option>
          <option value="1">空闲</option>
          <option value="2">服务中</option>
          <option value="3">休假</option>
          <option value="4">停用</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增陪诊师</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>姓名</th>
          <th>手机号</th>
          <th>性别</th>
          <th>职称</th>
          <th>评分</th>
          <th>服务次数</th>
          <th>准时率</th>
          <th>审核状态</th>
          <th>陪诊状态</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="loading">
          <td colspan="11" class="empty">加载中...</td>
        </tr>
        <tr v-else-if="list.length===0">
          <td colspan="11" class="empty">暂无数据</td>
        </tr>
        <tr v-for="row in list" :key="row.id">
          <td class="id-cell">{{ row.id }}</td>
          <td>
            <div class="user-cell"><img v-if="row.avatarUrl" :src="getFileUrl(row.avatarUrl)"
                                        class="avatar-sm avatar-img" alt="">
              <div v-else class="avatar-sm">{{ (row.realName || '?')[0] }}</div>
              {{ row.realName || '-' }}
            </div>
          </td>
          <td>{{ row.phone || '-' }}</td>
          <td>{{ genderLabel(row.gender) }}</td>
          <td>{{ row.professionalTitle || '-' }}</td>
          <td><span class="score">⭐ {{ row.ratingScore || '0.00' }}</span></td>
          <td>{{ row.serviceCount || 0 }}</td>
          <td>{{ row.onTimeRate || '100' }}%</td>
          <td><span class="tag" :class="auditClass(row.auditStatus)">{{ auditLabel(row.auditStatus) }}</span></td>
          <td><span class="tag" :class="accompanyClass(row.accompanyStatus)">{{
              accompanyLabel(row.accompanyStatus)
            }}</span></td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm btn-pass" v-if="row.auditStatus===1" @click="handleAudit(row,2)">通过</button>
              <button class="btn-sm btn-reject" v-if="row.auditStatus===1" @click="handleAudit(row,3)">拒绝</button>
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
    <!-- 新增/编辑弹窗 -->
    <div class="modal-mask" v-if="modal.show" @click.self="modal.show=false">
      <div class="modal">
        <div class="modal-header"><span>{{ modal.mode === 'add' ? '新增陪诊师' : '编辑陪诊师' }}</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item full form-tip">带 <em>*</em> 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。</div>
            <div class="form-item" v-if="modal.mode==='add'"><label>登录用户名 *</label><input v-model="form.username"
                                                                                               class="form-input"
                                                                                               placeholder="留空则需填写已有用户ID">
            </div>
            <div class="form-item" v-if="modal.mode==='add'"><label>登录密码 *</label><input v-model="form.password"
                                                                                             type="password"
                                                                                             class="form-input"
                                                                                             placeholder="至少8位">
            </div>
            <div class="form-item" v-if="modal.mode==='add'"><label>已有用户ID（二选一）</label><input
                v-model="form.userId" class="form-input" placeholder="填此项则无需填用户名密码"></div>
            <div class="form-item"><label>真实姓名 <em>*</em></label><input v-model="form.realName" class="form-input"
                                                                 placeholder="真实姓名"></div>
            <div class="form-item"><label>手机号 <em>*</em></label><input v-model="form.phone" class="form-input"
                                                               placeholder="手机号"></div>
            <div class="form-item"><label>性别</label>
              <select v-model="form.gender" class="form-select">
                <option :value="1">男</option>
                <option :value="2">女</option>
                <option :value="3">未知</option>
              </select>
            </div>
            <div class="form-item"><label>年龄（可留空）</label><input v-model="form.age" type="number" class="form-input"
                                                             placeholder="年龄"></div>
            <div class="form-item"><label>职称（可留空）</label><input v-model="form.professionalTitle" class="form-input"
                                                             placeholder="如主治医师"></div>
            <div class="form-item"><label>医学背景</label>
              <select v-model="form.medicalBackground" class="form-select">
                <option :value="1">有医学背景</option>
                <option :value="2">无医学背景</option>
              </select>
            </div>
            <div class="form-item"><label>陪诊状态</label>
              <select v-model="form.accompanyStatus" class="form-select">
                <option :value="1">空闲</option>
                <option :value="2">服务中</option>
                <option :value="3">休假</option>
                <option :value="4">停用</option>
              </select>
            </div>
            <div class="form-item full"><label>专业擅长</label><input v-model="form.specialties" class="form-input"
                                                                      placeholder="如骨科、脑科"></div>
            <div class="form-item full"><label>语言能力</label><input v-model="form.languageAbility" class="form-input"
                                                                      placeholder="如英语、日语"></div>
            <div class="form-item full">
              <label>证书照片</label>
              <div class="upload-row">
                <img v-if="form.certificatePhoto" :src="getFileUrl(form.certificatePhoto)" class="preview-img"
                     alt="证书照片">
                <label class="upload-btn">
                  {{
                    uploadingField === 'certificatePhoto' ? '上传中...' : (form.certificatePhoto ? '重新上传' : '点击上传')
                  }}
                  <input type="file" accept="image/*" style="display:none"
                         @change="e => handleUpload(e, 'certificatePhoto')" :disabled="!!uploadingField">
                </label>
                <button v-if="form.certificatePhoto" class="upload-clear" @click="form.certificatePhoto=''"
                        type="button">✕ 移除
                </button>
              </div>
            </div>
            <div class="form-item full"><label>备注</label><textarea v-model="form.remark" class="form-input"
                                                                     rows="2"></textarea></div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="modal.show=false">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="submitting">{{
              submitting ? '提交中...' : '确认'
            }}
          </button>
        </div>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div class="modal-mask" v-if="detailModal.show" @click.self="detailModal.show=false">
      <div class="modal modal-lg">
        <div class="modal-header"><span>陪诊师详情</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-section">
            <div class="section-title">基本信息</div>
            <div class="detail-grid">
              <div class="detail-item"><span class="dk">姓名</span><span class="dv">{{
                  detailModal.data.realName || '-'
                }}</span></div>
              <div class="detail-item"><span class="dk">手机号</span><span class="dv">{{
                  detailModal.data.phone || '-'
                }}</span></div>
              <div class="detail-item"><span class="dk">性别</span><span
                  class="dv">{{ genderLabel(detailModal.data.gender) }}</span></div>
              <div class="detail-item"><span class="dk">年龄</span><span class="dv">{{
                  detailModal.data.age || '-'
                }}</span></div>
              <div class="detail-item"><span class="dk">职称</span><span
                  class="dv">{{ detailModal.data.professionalTitle || '-' }}</span></div>
              <div class="detail-item"><span class="dk">医学背景</span><span
                  class="dv">{{ detailModal.data.medicalBackground === 1 ? '有' : '无' }}</span></div>
              <div class="detail-item"><span class="dk">评分</span><span
                  class="dv">⭐ {{ detailModal.data.ratingScore || '0.00' }}</span></div>
              <div class="detail-item"><span class="dk">服务次数</span><span
                  class="dv">{{ detailModal.data.serviceCount || 0 }}</span></div>
              <div class="detail-item"><span class="dk">准时率</span><span
                  class="dv">{{ detailModal.data.onTimeRate || '100' }}%</span></div>
              <div class="detail-item"><span class="dk">投诉次数</span><span
                  class="dv">{{ detailModal.data.complaintCount || 0 }}</span></div>
              <div class="detail-item full"><span class="dk">专业擅长</span><span
                  class="dv">{{ detailModal.data.specialties || '-' }}</span></div>
              <div class="detail-item full"><span class="dk">语言能力</span><span
                  class="dv">{{ detailModal.data.languageAbility || '-' }}</span></div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="detailModal.show=false">关闭</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getAccompanistList,
  getAccompanistDetail,
  addAccompanist,
  updateAccompanist,
  deleteAccompanist,
  auditAccompanist
} from '@/api/user.js'

export default {
  name: 'Accompanist',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {realName: '', auditStatus: '', accompanyStatus: '', pageNum: 1, pageSize: 10},
      modal: {show: false, mode: 'add'},
      detailModal: {show: false, data: null},
      form: {
        userId: '',
        username: '',
        password: '',
        realName: '',
        phone: '',
        gender: 1,
        age: '',
        professionalTitle: '',
        medicalBackground: 2,
        accompanyStatus: 1,
        specialties: '',
        languageAbility: '',
        certificateNumber: '',
        certificatePhoto: '',
        remark: ''
      },
      submitting: false,
      editId: null,
      uploadingField: null
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
    getFileUrl(filename) {
      if (!filename) return ''
      if (filename.startsWith('http')) return filename
      return 'http://localhost:8080/common/download?name=' + encodeURIComponent(filename)
    },
    async handleUpload(e, field) {
      const file = e.target.files[0]
      if (!file) return
      this.uploadingField = field
      try {
        const fd = new FormData()
        fd.append('file', file)
        const res = await fetch('http://localhost:8080/common/upload', {
          method: 'POST',
          headers: {'Authorization': 'Bearer ' + (localStorage.getItem('token') || '')},
          body: fd
        })
        const json = await res.json()
        if (json.code === 1 || json.code === 200) {
          this.$set(this.form, field, json.data)
        } else {
          alert('上传失败：' + (json.msg || json.message || '未知错误'))
        }
      } catch (err) {
        alert('上传失败')
      } finally {
        this.uploadingField = null;
        e.target.value = ''
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getAccompanistList(this.query)
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
      this.query = {realName: '', auditStatus: '', accompanyStatus: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    openAdd() {
      this.modal.mode = 'add'
      this.form = {
        userId: '',
        username: '',
        password: '',
        realName: '',
        phone: '',
        gender: 1,
        age: '',
        professionalTitle: '',
        medicalBackground: 2,
        accompanyStatus: 1,
        specialties: '',
        languageAbility: '',
        remark: ''
      }
      this.editId = null;
      this.modal.show = true
    },
    openEdit(row) {
      this.modal.mode = 'edit';
      this.editId = row.id
      this.form = {
        realName: row.realName || '',
        phone: row.phone || '',
        gender: row.gender || 1,
        age: row.age || '',
        professionalTitle: row.professionalTitle || '',
        medicalBackground: row.medicalBackground || 2,
        accompanyStatus: row.accompanyStatus || 1,
        specialties: row.specialties || '',
        languageAbility: row.languageAbility || '',
        certificatePhoto: row.certificatePhoto || '',
        remark: row.remark || ''
      }
      this.modal.show = true
    },
    async openDetail(row) {
      try {
        const res = await getAccompanistDetail(row.id);
        this.detailModal.data = (res && res.data) ? res.data : res;
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async handleSubmit() {
      if (!this.form.realName) return alert('真实姓名不能为空')
      if (!this.form.phone) return alert('手机号不能为空')
      if (this.modal.mode === 'add' && !this.form.userId && !this.form.username) return alert('登录用户名不能为空（或填写已有用户ID）')
      if (this.modal.mode === 'add' && !this.form.userId && (!this.form.password || this.form.password.length < 8)) return alert('登录密码不能为空且至少8位')
      this.submitting = true
      try {
        if (this.modal.mode === 'add') {
          await addAccompanist(this.form)
        } else {
          await updateAccompanist(this.editId, this.form)
        }
        this.modal.show = false;
        this.loadData()
      } catch (e) {
        const msg = e.response && e.response.data && e.response.data.msg
            ? e.response.data.msg
            : (e.message || '操作失败')
        alert('操作失败：' + msg)
      } finally {
        this.submitting = false
      }
    },
    async handleAudit(row, status) {
      const label = status === 2 ? '通过' : '拒绝'
      let reason = ''
      if (status === 3) {
        reason = prompt('请输入拒绝原因：') || ''
      }
      if (!confirm('确认' + label + '？')) return
      try {
        await auditAccompanist(row.id, {status: status, failReason: reason});
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除？')) return
      try {
        await deleteAccompanist(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    genderLabel(g) {
      return {1: '男', 2: '女', 3: '未知'}[g] || '-'
    },
    auditLabel(s) {
      return {1: '待审核', 2: '已通过', 3: '已拒绝'}[s] || '-'
    },
    auditClass(s) {
      return {1: 'tag-yellow', 2: 'tag-green', 3: 'tag-red'}[s] || ''
    },
    accompanyLabel(s) {
      return {1: '空闲', 2: '服务中', 3: '休假', 4: '停用'}[s] || '-'
    },
    accompanyClass(s) {
      return {1: 'tag-green', 2: 'tag-blue', 3: 'tag-yellow', 4: 'tag-red'}[s] || ''
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
  width: 160px;
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

.btn-add {
  background: var(--bg-btn-add);
  border: 1px solid rgba(110, 231, 183, 0.5);
  border-radius: 8px;
  padding: 8px 18px;
  color: var(--text-accent);
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
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

.user-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar-sm {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  background: var(--bg-avatar);
  color: var(--text-accent);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
  flex-shrink: 0;
}

.score {
  color: var(--text-yellow);
  font-size: 13px;
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

.btn-edit {
  background: var(--btn-edit-bg);
  border-color: var(--btn-edit-bd);
  color: var(--text-yellow);
}

.btn-pass {
  background: var(--tag-green-bg);
  border-color: var(--btn-ok-bd);
  color: var(--text-accent);
}

.btn-reject, .btn-del {
  background: var(--btn-warn-bg);
  border-color: var(--btn-warn-bd);
  color: var(--text-red);
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
  width: 700px;
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

.form-grid {
  display: grid;
  grid-template-columns:1fr 1fr;
  gap: 14px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-item.full {
  grid-column: 1/-1;
}

.form-item label {
  font-size: 12px;
  color: var(--text-label);
}

.form-item label em {
  color: var(--text-red);
  font-style: normal;
}

.form-input {
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 8px 12px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
  resize: vertical;
}

.form-select {
  background: var(--bg-select);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 8px 10px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
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

.btn-confirm {
  background: var(--bg-btn-add);
  border: 1px solid var(--border-active-nav);
  border-radius: 7px;
  padding: 8px 20px;
  color: var(--text-accent);
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
}

.btn-confirm:disabled {
  opacity: 0.5;
  cursor: not-allowed;
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

.detail-item.full {
  grid-column: 1/-1;
}

.dk {
  font-size: 11px;
  color: var(--text-page-desc);
}

.dv {
  font-size: 13px;
  color: var(--text-input);
}

.upload-row {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.preview-img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid var(--border-input);
}

.upload-btn {
  display: inline-flex;
  align-items: center;
  padding: 7px 16px;
  background: var(--bg-btn-search);
  border: 1px dashed var(--border-active-nav);
  border-radius: 7px;
  color: var(--text-accent);
  font-size: 13px;
  cursor: pointer;
}

.upload-btn:hover {
  background: var(--bg-active-nav);
}

.upload-clear {
  background: none;
  border: none;
  color: var(--text-red);
  font-size: 12px;
  cursor: pointer;
  padding: 4px 8px;
}

.avatar-img {
  object-fit: cover;
  background: none;
}
</style>