<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>专家管理</h2>
      <span class="page-desc">管理医院专家信息（medical_expert）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.expertName" class="filter-input" placeholder="专家姓名" @keyup.enter="loadData">
        <input v-model="query.professionalTitle" class="filter-input" placeholder="职称" @keyup.enter="loadData">
        <select v-model="query.isRecommend" class="filter-select">
          <option value="">推荐状态</option>
          <option value="1">推荐</option>
          <option value="2">不推荐</option>
        </select>
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">正常</option>
          <option value="2">停用</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增专家</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>编码</th>
          <th>专家</th>
          <th>医院</th>
          <th>科室</th>
          <th>职称</th>
          <th>预约费</th>
          <th>接受国际</th>
          <th>推荐</th>
          <th>评分</th>
          <th>状态</th>
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
          <td><code class="code-tag">{{ row.expertCode }}</code></td>
          <td>
            <div class="expert-cell">
              <img v-if="row.photoUrl" :src="getFileUrl(row.photoUrl)" class="avatar-img" alt="">
              <div v-else class="avatar-sm">{{ (row.expertName || '?')[0] }}</div>
              <div>
                <div class="expert-name">{{ row.expertName }}</div>
                <div class="sub">{{ row.expertNameEn || '' }}</div>
              </div>
            </div>
          </td>
          <td><span class="ellipsis">{{ row.hospitalName || '-' }}</span></td>
          <td>{{ row.departmentName || '-' }}</td>
          <td><span class="tag tag-blue">{{ row.professionalTitle || '-' }}</span></td>
          <td><span class="amount">¥{{ row.appointmentFee || '-' }}</span></td>
          <td><span class="tag" :class="row.isAcceptInternational===1?'tag-green':'tag-gray'">{{
              row.isAcceptInternational === 1 ? '接受' : '不接受'
            }}</span></td>
          <td><span class="tag" :class="row.isRecommend===1?'tag-yellow':'tag-gray'">{{
              row.isRecommend === 1 ? '推荐' : '普通'
            }}</span></td>
          <td><span class="score">⭐ {{ row.ratingScore || '-' }}</span></td>
          <td><span class="tag" :class="row.status===1?'tag-green':'tag-gray'">{{
              row.status === 1 ? '正常' : '停用'
            }}</span></td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm" :class="row.status===1?'btn-warn':'btn-ok'" @click="toggleStatus(row)">
                {{ row.status === 1 ? '停用' : '启用' }}
              </button>
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
      <div class="modal modal-lg">
        <div class="modal-header"><span>{{ modal.isEdit ? '编辑专家' : '新增专家' }}</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item"><label>专家编码</label><input v-model="form.expertCode" class="form-input"
                                                                 :disabled="modal.isEdit"></div>
            <div class="form-item"><label>专家姓名 *</label><input v-model="form.expertName" class="form-input"></div>
            <div class="form-item" v-if="!modal.isEdit"><label>登录用户名 *</label><input v-model="form.username"
                                                                                          class="form-input"
                                                                                          placeholder="用于登录的账号">
            </div>
            <div class="form-item" v-if="!modal.isEdit"><label>登录密码 *</label><input v-model="form.password"
                                                                                        type="password"
                                                                                        class="form-input"
                                                                                        placeholder="至少8位"></div>
            <div class="form-item"><label>英文姓名</label><input v-model="form.expertNameEn" class="form-input"></div>
            <div class="form-item"><label>医院ID *</label><input v-model="form.hospitalId" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>医院名称</label><input v-model="form.hospitalName" class="form-input"></div>
            <div class="form-item"><label>科室ID *</label><input v-model="form.departmentId" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>科室名称</label><input v-model="form.departmentName" class="form-input"></div>
            <div class="form-item"><label>职称</label><input v-model="form.professionalTitle" class="form-input"></div>
            <div class="form-item"><label>预约费用</label><input v-model="form.appointmentFee" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>工作年限</label><input v-model="form.experienceYears" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>是否接受国际患者</label>
              <select v-model="form.isAcceptInternational" class="form-input">
                <option :value="1">接受</option>
                <option :value="2">不接受</option>
              </select>
            </div>
            <div class="form-item"><label>是否推荐</label>
              <select v-model="form.isRecommend" class="form-input">
                <option :value="1">推荐</option>
                <option :value="2">不推荐</option>
              </select>
            </div>
            <div class="form-item"><label>排序</label><input v-model="form.sort" type="number" class="form-input"></div>
            <div class="form-item full"><label>语言能力</label><input v-model="form.languageAbility" class="form-input">
            </div>
            <div class="form-item full"><label>专业擅长</label><textarea v-model="form.specialty" class="form-input"
                                                                         rows="2"></textarea></div>
            <div class="form-item full"><label>个人简介</label><textarea v-model="form.introduction" class="form-input"
                                                                         rows="3"></textarea></div>
            <div class="form-item full">
              <label>专家照片</label>
              <div class="upload-row">
                <img v-if="form.photoUrl" :src="getFileUrl(form.photoUrl)" class="preview-img" alt="专家照片">
                <label class="upload-btn">
                  {{ uploadingField === 'photoUrl' ? '上传中...' : (form.photoUrl ? '重新上传' : '点击上传') }}
                  <input type="file" accept="image/*" style="display:none" @change="e => handleUpload(e, 'photoUrl')"
                         :disabled="uploadingField==='photoUrl'">
                </label>
                <button v-if="form.photoUrl" class="upload-clear" @click="form.photoUrl=''" type="button">✕ 移除
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
  </div>
</template>
<script>
import {getExpertList, addExpert, updateExpert, deleteExpert, updateExpertStatus} from '@/api/medical.js'

export default {
  name: 'Expert',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {expertName: '', professionalTitle: '', isRecommend: '', status: '', pageNum: 1, pageSize: 10},
      modal: {show: false, isEdit: false},
      form: {}, editId: null, submitting: false, uploadingField: null
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
        const token = localStorage.getItem('token') || ''
        const res = await fetch('http://localhost:8080/common/upload', {
          method: 'POST',
          headers: token ? {'Authorization': 'Bearer ' + token} : {},
          body: fd
        })
        const text = await res.text()
        let json
        try {
          json = JSON.parse(text)
        } catch (_) {
          alert('服务器响应异常：' + text.substring(0, 300));
          return
        }
        if (json.code === 200) {
          this.form[field] = json.data
        } else {
          alert('上传失败：' + (json.msg || json.message || JSON.stringify(json)))
        }
      } catch (err) {
        alert('上传异常：' + err.message)
      } finally {
        this.uploadingField = null;
        e.target.value = ''
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getExpertList(this.query)
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
      this.query = {expertName: '', professionalTitle: '', isRecommend: '', status: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    openAdd() {
      this.editId = null;
      this.form = {isAcceptInternational: 1, isRecommend: 2, sort: 0, username: '', password: ''};
      this.modal = {show: true, isEdit: false}
    },
    openEdit(row) {
      this.editId = row.id
      this.form = {
        expertCode: row.expertCode,
        expertName: row.expertName,
        expertNameEn: row.expertNameEn || '',
        hospitalId: row.hospitalId,
        hospitalName: row.hospitalName || '',
        departmentId: row.departmentId,
        departmentName: row.departmentName || '',
        professionalTitle: row.professionalTitle || '',
        specialty: row.specialty || '',
        languageAbility: row.languageAbility || '',
        experienceYears: row.experienceYears,
        appointmentFee: row.appointmentFee,
        isAcceptInternational: row.isAcceptInternational,
        isRecommend: row.isRecommend,
        introduction: row.introduction || '',
        photoUrl: row.photoUrl || '',
        sort: row.sort || 0,
        remark: row.remark || ''
      }
      this.modal = {show: true, isEdit: true}
    },
    async handleSubmit() {
      if (!this.form.expertName) return alert('专家姓名不能为空')
      if (!this.form.hospitalId) return alert('请填写医院ID')
      if (!this.form.departmentId) return alert('请填写科室ID')
      if (!this.modal.isEdit && !this.form.username) return alert('请填写登录用户名')
      if (!this.modal.isEdit && (!this.form.password || this.form.password.length < 8)) return alert('密码至少8位')
      this.submitting = true
      try {
        if (this.modal.isEdit) await updateExpert(this.editId, this.form)
        else await addExpert(this.form)
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
    async toggleStatus(row) {
      const ns = row.status === 1 ? 2 : 1
      if (!confirm('确认' + (ns === 2 ? '停用' : '启用') + '该专家？')) return
      try {
        await updateExpertStatus(row.id, ns);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除专家「' + row.expertName + '」？')) return
      try {
        await deleteExpert(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
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
  width: 140px;
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
  border: 1px solid var(--border-active-nav);
  border-radius: 7px;
  padding: 7px 16px;
  color: var(--text-accent);
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  white-space: nowrap;
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

.code-tag {
  font-family: monospace;
  font-size: 12px;
  color: var(--text-accent);
  background: var(--bg-hover-nav);
  padding: 2px 6px;
  border-radius: 4px;
}

.expert-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar-sm {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--bg-avatar);
  color: var(--text-accent);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  flex-shrink: 0;
}

.avatar-img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.expert-name {
  font-weight: 500;
  color: var(--text-input);
}

.sub {
  font-size: 11px;
  color: var(--text-dim);
}

.ellipsis {
  display: block;
  max-width: 120px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.amount {
  color: var(--text-yellow);
  font-weight: 600;
}

.score {
  color: var(--text-yellow);
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

.tag-gray {
  background: var(--bg-collapse);
  color: var(--tag-gray-text);
}

.tag-yellow {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
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

.btn-edit {
  background: var(--btn-edit-bg);
  border-color: var(--btn-edit-bd);
  color: var(--text-yellow);
}

.btn-warn {
  background: var(--btn-warn-bg);
  border-color: var(--btn-warn-bd);
  color: var(--text-red);
}

.btn-ok {
  background: var(--tag-green-bg);
  border-color: var(--btn-ok-bd);
  color: var(--text-accent);
}

.btn-del {
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

.form-input {
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 8px 12px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
  resize: vertical;
  width: 100%;
  box-sizing: border-box;
}

.form-input:disabled {
  opacity: 0.4;
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
</style> 