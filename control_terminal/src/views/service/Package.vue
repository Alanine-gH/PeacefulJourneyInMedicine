<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>服务套餐</h2>
      <span class="page-desc">管理陪诊服务套餐配置（service_package）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.packageName" class="filter-input" placeholder="套餐名称" @keyup.enter="loadData">
        <select v-model="query.packageType" class="filter-select">
          <option value="">全部类型</option>
          <option value="1">基础套餐</option>
          <option value="2">增值套餐</option>
          <option value="3">定制套餐</option>
        </select>
        <select v-model="query.serviceType" class="filter-select">
          <option value="">全部服务</option>
          <option value="1">全程陪诊</option>
          <option value="2">代办手续</option>
          <option value="3">接机服务</option>
          <option value="4">送机服务</option>
          <option value="5">翻译服务</option>
        </select>
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">上架</option>
          <option value="2">下架</option>
          <option value="3">停售</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增套餐</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>编码</th>
          <th>套餐名称</th>
          <th>套餐类型</th>
          <th>服务类型</th>
          <th>价格</th>
          <th>时长</th>
          <th>有效期</th>
          <th>状态</th>
          <th>排序</th>
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
          <td><code class="code-tag">{{ row.packageCode }}</code></td>
          <td>
            <div class="pkg-cell">
              <div class="pkg-icon">📦</div>
              <div>
                <div class="pkg-name">{{ row.packageName }}</div>
                <div class="sub">{{ row.packageNameEn || '' }}</div>
              </div>
            </div>
          </td>
          <td><span class="tag tag-purple">{{ pkgTypeLabel(row.packageType) }}</span></td>
          <td><span class="tag tag-blue">{{ svcTypeLabel(row.serviceType) }}</span></td>
          <td><span class="amount">{{ row.currency }} {{ row.price }}</span>
            <div class="sub">/{{ row.unit || '-' }}</div>
          </td>
          <td>{{ row.duration ? row.duration + 'h' : '-' }}</td>
          <td>{{ row.validDays ? row.validDays + '天' : '不限' }}</td>
          <td><span class="tag" :class="pkgStatusClass(row.status)">{{ pkgStatusLabel(row.status) }}</span></td>
          <td>{{ row.sort }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm btn-ok" v-if="row.status!==1" @click="setStatus(row,1)">上架</button>
              <button class="btn-sm btn-warn" v-if="row.status===1" @click="setStatus(row,2)">下架</button>
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
        <div class="modal-header"><span>{{ modal.isEdit ? '编辑套餐' : '新增套餐' }}</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item"><label>套餐编码</label><input v-model="form.packageCode" class="form-input"
                                                                 :disabled="modal.isEdit"></div>
            <div class="form-item"><label>套餐名称 *</label><input v-model="form.packageName" class="form-input"></div>
            <div class="form-item"><label>英文名称</label><input v-model="form.packageNameEn" class="form-input"></div>
            <div class="form-item"><label>套餐类型</label>
              <select v-model="form.packageType" class="form-input">
                <option value="1">基础套餐</option>
                <option value="2">增值套餐</option>
                <option value="3">定制套餐</option>
              </select>
            </div>
            <div class="form-item"><label>服务类型</label>
              <select v-model="form.serviceType" class="form-input">
                <option value="1">全程陪诊</option>
                <option value="2">代办手续</option>
                <option value="3">接机服务</option>
                <option value="4">送机服务</option>
                <option value="5">翻译服务</option>
              </select>
            </div>
            <div class="form-item"><label>价格</label><input v-model="form.price" type="number" class="form-input">
            </div>
            <div class="form-item"><label>货币</label><input v-model="form.currency" class="form-input"
                                                             placeholder="USD"></div>
            <div class="form-item"><label>单位</label><input v-model="form.unit" class="form-input"
                                                             placeholder="次/天/小时"></div>
            <div class="form-item"><label>时长(h)</label><input v-model="form.duration" type="number"
                                                                class="form-input"></div>
            <div class="form-item"><label>有效期(天)</label><input v-model="form.validDays" type="number"
                                                                   class="form-input"></div>
            <div class="form-item"><label>最大使用次数</label><input v-model="form.maxUsageCount" type="number"
                                                                     class="form-input" placeholder="-1不限"></div>
            <div class="form-item"><label>排序</label><input v-model="form.sort" type="number" class="form-input"></div>
            <div class="form-item full"><label>包含项目</label><input v-model="form.includesItems" class="form-input">
            </div>
            <div class="form-item full"><label>不含项目</label><input v-model="form.excludeItems" class="form-input">
            </div>
            <div class="form-item full"><label>套餐描述</label><textarea v-model="form.description" class="form-input"
                                                                         rows="3"></textarea></div>
            <div class="form-item full"><label>使用须知</label><textarea v-model="form.usageNotice" class="form-input"
                                                                         rows="2"></textarea></div>
            <div class="form-item full">
              <label>套餐封面图</label>
              <div class="upload-row">
                <img v-if="form.coverImageUrl" :src="getFileUrl(form.coverImageUrl)" class="preview-img" alt="封面">
                <label class="upload-btn">
                  {{
                    uploadingField === 'coverImageUrl' ? '上传中...' : (form.coverImageUrl ? '重新上传' : '点击上传')
                  }}
                  <input type="file" accept="image/*" style="display:none"
                         @change="e => handleUpload(e, 'coverImageUrl')" :disabled="!!uploadingField">
                </label>
                <button v-if="form.coverImageUrl" class="upload-clear" @click="form.coverImageUrl=''" type="button">✕
                  移除
                </button>
              </div>
            </div>
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
import {getPackageList, addPackage, updatePackage, deletePackage, updatePackageStatus} from '@/api/service.js'

export default {
  name: 'Package',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {packageName: '', packageType: '', serviceType: '', status: '', pageNum: 1, pageSize: 10},
      modal: {show: false, isEdit: false},
      form: {}, editId: null, submitting: false, uploadingField: null, uploadingField: null
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
        const text = await res.text()
        let json
        try { json = JSON.parse(text) } catch (_) {
          alert('服务器响应异常：' + text.substring(0, 300)); return
        }
        if (json.code === 1 || json.code === 200) {
          this.$set(this.form, field, json.data)
        } else {
          alert('上传失败：' + (json.msg || json.message || '未知错误'))
        }
      } catch (err) {
        alert('上传失败：' + err.message)
      } finally {
        this.uploadingField = null
        e.target.value = ''
      }
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getPackageList(this.query)
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
      this.query = {packageName: '', packageType: '', serviceType: '', status: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    openAdd() {
      this.editId = null;
      this.form = {currency: 'USD', maxUsageCount: -1, sort: 0, status: 1};
      this.modal = {show: true, isEdit: false}
    },
    openEdit(row) {
      this.editId = row.id
      this.form = {
        packageCode: row.packageCode,
        packageName: row.packageName,
        packageNameEn: row.packageNameEn || '',
        packageType: row.packageType,
        serviceType: row.serviceType,
        price: row.price,
        currency: row.currency || 'USD',
        unit: row.unit || '',
        duration: row.duration,
        validDays: row.validDays,
        maxUsageCount: row.maxUsageCount,
        includesItems: row.includesItems || '',
        excludeItems: row.excludeItems || '',
        description: row.description || '',
        usageNotice: row.usageNotice || '',
        coverImageUrl: row.coverImageUrl || '',
        sort: row.sort || 0
      }
      this.modal = {show: true, isEdit: true}
    },
    async handleSubmit() {
      if (!this.form.packageName) return alert('套餐名称不能为空')
      this.submitting = true
      try {
        if (this.modal.isEdit) await updatePackage(this.editId, this.form)
        else await addPackage(this.form)
        this.modal.show = false;
        this.loadData()
      } catch (e) {
        alert('操作失败')
      } finally {
        this.submitting = false
      }
    },
    async setStatus(row, s) {
      const label = {1: '上架', 2: '下架', 3: '停售'}[s]
      if (!confirm('确认' + label + '套餐「' + row.packageName + '」？')) return
      try {
        await updatePackageStatus(row.id, s);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除套餐「' + row.packageName + '」？')) return
      try {
        await deletePackage(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    pkgTypeLabel(t) {
      return {1: '基础套餐', 2: '增値套餐', 3: '定制套餐'}[t] || '-'
    },
    svcTypeLabel(t) {
      return {1: '全程陪诊', 2: '代办手续', 3: '接机服务', 4: '送机服务', 5: '翻译服务'}[t] || '-'
    },
    pkgStatusLabel(s) {
      return {1: '上架', 2: '下架', 3: '停售'}[s] || '-'
    },
    pkgStatusClass(s) {
      return {1: 'tag-green', 2: 'tag-gray', 3: 'tag-red'}[s] || ''
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

.pkg-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pkg-icon {
  width: 28px;
  height: 28px;
  border-radius: 6px;
  background: rgba(139, 92, 246, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
}

.pkg-name {
  font-weight: 500;
  color: var(--text-input);
}

.sub {
  font-size: 11px;
  color: var(--text-dim);
}

.amount {
  color: var(--text-yellow);
  font-weight: 600;
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

.tag-purple {
  background: var(--tag-purple-bg);
  color: #c4b5fd;
}

.tag-red {
  background: rgba(239, 68, 68, 0.1);
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

.btn-edit {
  background: var(--btn-edit-bg);
  border-color: var(--btn-edit-bd);
  color: var(--text-yellow);
}

.btn-ok {
  background: var(--tag-green-bg);
  border-color: var(--btn-ok-bd);
  color: var(--text-accent);
}

.btn-warn {
  background: var(--btn-warn-bg);
  border-color: var(--btn-warn-bd);
  color: var(--text-red);
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