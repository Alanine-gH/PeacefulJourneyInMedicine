<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>医院管理</h2>
      <span class="page-desc">管理合作医院信息（medical_hospital）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.hospitalName" class="filter-input" placeholder="医院名称" @keyup.enter="loadData">
        <input v-model="query.city" class="filter-input w100" placeholder="城市" @keyup.enter="loadData">
        <select v-model="query.hospitalType" class="filter-select">
          <option value="">全部类型</option>
          <option value="1">三甲医院</option>
          <option value="2">二甲医院</option>
          <option value="3">专科医院</option>
          <option value="4">国际医院</option>
        </select>
        <select v-model="query.isCooperation" class="filter-select">
          <option value="">合作状态</option>
          <option value="1">合作</option>
          <option value="2">未合作</option>
        </select>
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">正常</option>
          <option value="2">停用</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增医院</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>编码</th>
          <th>医院名称</th>
          <th>类型</th>
          <th>城市</th>
          <th>合作</th>
          <th>国际部</th>
          <th>评分</th>
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
          <td><code class="code-tag">{{ row.hospitalCode }}</code></td>
          <td>
            <div class="hosp-cell">
              <div class="hosp-logo-placeholder">🏥</div>
              <div>
                <div class="hosp-name">{{ row.hospitalName }}</div>
                <div class="sub">{{ row.hospitalNameEn || '' }}</div>
              </div>
            </div>
          </td>
          <td><span class="tag tag-blue">{{ hospTypeLabel(row.hospitalType) }}</span></td>
          <td>{{ row.city || '-' }}</td>
          <td><span class="tag" :class="row.isCooperation===1?'tag-green':'tag-gray'">{{
              row.isCooperation === 1 ? '合作' : '未合作'
            }}</span></td>
          <td><span class="tag" :class="row.hasInternationalDept===1?'tag-purple':'tag-gray'">{{
              row.hasInternationalDept === 1 ? '有' : '无'
            }}</span></td>
          <td><span class="score">⭐ {{ row.ratingScore || '-' }}</span></td>
          <td><span class="tag" :class="row.status===1?'tag-green':'tag-gray'">{{
              row.status === 1 ? '正常' : '停用'
            }}</span></td>
          <td>{{ row.sort }}</td>
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
        <div class="modal-header"><span>{{ modal.isEdit ? '编辑医院' : '新增医院' }}</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item full form-tip">带 * 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。</div>
            <div class="form-item"><label>医院编码</label><input v-model="form.hospitalCode" class="form-input"
                                                                 :disabled="modal.isEdit"></div>
            <div class="form-item"><label>医院名称 *</label><input v-model="form.hospitalName" class="form-input"></div>
            <div class="form-item"><label>英文名称</label><input v-model="form.hospitalNameEn" class="form-input"></div>
            <div class="form-item"><label>医院类型</label>
              <select v-model="form.hospitalType" class="form-input">
                <option value="1">三甲医院</option>
                <option value="2">二甲医院</option>
                <option value="3">专科医院</option>
                <option value="4">国际医院</option>
              </select>
            </div>
            <div class="form-item"><label>省份</label><input v-model="form.province" class="form-input"></div>
            <div class="form-item"><label>城市</label><input v-model="form.city" class="form-input"></div>
            <div class="form-item"><label>联系电话</label><input v-model="form.phone" class="form-input"></div>
            <div class="form-item"><label>国际部电话</label><input v-model="form.internationalPhone" class="form-input">
            </div>
            <div class="form-item"><label>是否合作</label>
              <select v-model="form.isCooperation" class="form-input">
                <option value="1">合作</option>
                <option value="2">未合作</option>
              </select>
            </div>
            <div class="form-item"><label>是否有国际部</label>
              <select v-model="form.hasInternationalDept" class="form-input">
                <option value="1">有</option>
                <option value="2">无</option>
              </select>
            </div>
            <div class="form-item"><label>排序</label><input v-model="form.sort" type="number" class="form-input"></div>
            <div class="form-item full"><label>医院地址</label><input v-model="form.address" class="form-input"></div>
            <div class="form-item full"><label>特色科室（逗号分隔）</label><input v-model="form.featuredDepartments"
                                                                                class="form-input"></div>
            <div class="form-item full"><label>医院简介</label><textarea v-model="form.introduction" class="form-input"
                                                                         rows="3"></textarea></div>
            <div class="form-item full">
              <label>医院Logo</label>
              <div class="upload-row">
                <img v-if="form.logoUrl" :src="getFileUrl(form.logoUrl)" class="preview-img" alt="Logo">
                <label class="upload-btn">
                  {{ uploadingField === 'logoUrl' ? '上传中...' : (form.logoUrl ? '重新上传' : '点击上传') }}
                  <input type="file" accept="image/*" style="display:none" @change="e => handleUpload(e, 'logoUrl')"
                         :disabled="!!uploadingField">
                </label>
                <button v-if="form.logoUrl" class="upload-clear" @click="form.logoUrl=''" type="button">✕ 移除</button>
              </div>
            </div>
            <div class="form-item full">
              <label>封面图片</label>
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
import {getHospitalList, addHospital, updateHospital, deleteHospital, updateHospitalStatus} from '@/api/medical.js'
import { BASE_URL } from '@/config'
import {uploadFile,getFileUrl} from "@/api/utils.js";

export default {
  name: 'Hospital',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {hospitalName: '', city: '', hospitalType: '', isCooperation: '', status: '', pageNum: 1, pageSize: 10},
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
    getFileUrl,uploadFile,
    async handleUpload(e, field) {
      const file = e.target.files[0]
      if (!file) return
      this.uploadingField = field
      try {
        const fd = new FormData()
        fd.append('file', file)
        const token = localStorage.getItem('token') || ''
        const res = await fetch(BASE_URL + '/common/upload', {
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
        const res = await getHospitalList(this.query)
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
      this.query = {
        hospitalName: '',
        city: '',
        hospitalType: '',
        isCooperation: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      };
      this.loadData()
    },
    openAdd() {
      this.editId = null;
      this.form = {isCooperation: 2, hasInternationalDept: 2, sort: 0};
      this.modal = {show: true, isEdit: false}
    },
    openEdit(row) {
      this.editId = row.id
      this.form = {
        hospitalCode: row.hospitalCode,
        hospitalName: row.hospitalName,
        hospitalNameEn: row.hospitalNameEn || '',
        hospitalType: row.hospitalType,
        province: row.province || '',
        city: row.city || '',
        address: row.address || '',
        phone: row.phone || '',
        internationalPhone: row.internationalPhone || '',
        isCooperation: row.isCooperation,
        hasInternationalDept: row.hasInternationalDept,
        featuredDepartments: row.featuredDepartments || '',
        introduction: row.introduction || '',
        logoUrl: row.logoUrl || '',
        coverImageUrl: row.coverImageUrl || '',
        sort: row.sort || 0,
        remark: row.remark || ''
      }
      this.modal = {show: true, isEdit: true}
    },
    async handleSubmit() {
      if (!this.form.hospitalName) return alert('医院名称不能为空')
      this.submitting = true
      try {
        if (this.modal.isEdit) await updateHospital(this.editId, this.form)
        else await addHospital(this.form)
        this.modal.show = false;
        this.loadData()
      } catch (e) {
        alert('操作失败')
      } finally {
        this.submitting = false
      }
    },
    async toggleStatus(row) {
      const ns = row.status === 1 ? 2 : 1
      const label = ns === 2 ? '停用' : '启用'
      if (!confirm('确认' + label + '该医院？')) return
      try {
        await updateHospitalStatus(row.id, ns);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除医院「' + row.hospitalName + '」？')) return
      try {
        await deleteHospital(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    hospTypeLabel(t) {
      return {1: '三甲医院', 2: '二甲医院', 3: '专科医院', 4: '国际医院'}[t] || '-'
    }
  }
}
</script>
<style>
@import '@/assets/medical.css';
</style>
<style scoped>
.hosp-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.hosp-logo-placeholder {
  width: 30px;
  height: 30px;
  border-radius: 6px;
  background: var(--bg-active-nav);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  flex-shrink: 0;
}

.hosp-name {
  font-weight: 500;
  color: var(--text-input);
}

.hosp-logo-img {
  width: 30px;
  height: 30px;
  border-radius: 6px;
  object-fit: cover;
  flex-shrink: 0;
}
</style>
