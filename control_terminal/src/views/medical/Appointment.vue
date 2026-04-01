<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>预约管理</h2>
      <span class="page-desc">管理患者医院预约信息（medical_appointment）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.appointmentNo" class="filter-input" placeholder="预约编号" @keyup.enter="loadData">
        <input v-model="query.userName" class="filter-input" placeholder="患者姓名/手机" @keyup.enter="loadData">
        <select v-model="query.appointmentStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待确认</option>
          <option value="2">已确认</option>
          <option value="3">已取消</option>
          <option value="4">已完成</option>
          <option value="5">已过期</option>
        </select>
        <input v-model="query.appointmentDate" type="date" class="filter-input w140">
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增预约</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>预约编号</th>
          <th>患者</th>
          <th>医院</th>
          <th>科室</th>
          <th>专家</th>
          <th>预约日期</th>
          <th>类型</th>
          <th>状态</th>
          <th>复诊</th>
          <th>下单时间</th>
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
          <td><code class="code-tag">{{ row.appointmentNo }}</code></td>
          <td>
            <div class="user-cell"><img v-if="row.userAvatar" :src="getFileUrl(row.userAvatar)"
                                        class="avatar-sm avatar-img" alt="">
              <div v-else class="avatar-sm">{{ (row.userName || '?')[0] }}</div>
              <div>
                <div>{{ row.userName }}</div>
                <div class="sub">{{ row.userPhone || '' }}</div>
              </div>
            </div>
          </td>
          <td><span class="ellipsis">{{ row.hospitalName || '-' }}</span></td>
          <td>{{ row.departmentName || '-' }}</td>
          <td>{{ row.expertName || '未指定' }}</td>
          <td><span class="date-badge">{{ row.appointmentDate }}</span>
            <div class="sub">{{ row.appointmentTime || '' }}</div>
          </td>
          <td><span class="tag tag-blue">{{ apptTypeLabel(row.appointmentType) }}</span></td>
          <td><span class="tag"
                    :class="apptStatusClass(row.appointmentStatus)">{{ apptStatusLabel(row.appointmentStatus) }}</span>
          </td>
          <td><span class="tag" :class="row.followUpNeeded===1?'tag-yellow':'tag-gray'">{{
              row.followUpNeeded === 1 ? '需复诊' : '否'
            }}</span></td>
          <td>{{ fmtTime(row.createTime) }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm btn-ok" v-if="row.appointmentStatus===1" @click="confirmAppt(row)">确认</button>
              <button class="btn-sm btn-green" v-if="row.appointmentStatus===2" @click="completeAppt(row)">完成</button>
              <button class="btn-sm btn-warn" v-if="row.appointmentStatus===1" @click="cancelAppt(row)">取消</button>
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
        <div class="modal-header"><span>{{ modal.isEdit ? '编辑预约' : '新增预约' }}</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item full form-tip">带 * 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。</div>
            <div class="form-item"><label>患者用户ID *</label><input v-model="form.userId" type="number"
                                                                     class="form-input"></div>
            <div class="form-item"><label>医院ID *</label><input v-model="form.hospitalId" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>医院名称</label><input v-model="form.hospitalName" class="form-input"></div>
            <div class="form-item"><label>科室ID *</label><input v-model="form.departmentId" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>科室名称</label><input v-model="form.departmentName" class="form-input"></div>
            <div class="form-item"><label>专家ID</label><input v-model="form.expertId" type="number" class="form-input">
            </div>
            <div class="form-item"><label>专家姓名</label><input v-model="form.expertName" class="form-input"></div>
            <div class="form-item"><label>预约日期 *</label><input v-model="form.appointmentDate" type="date"
                                                                   class="form-input"></div>
            <div class="form-item"><label>预约时间</label><input v-model="form.appointmentTime" type="time"
                                                                 class="form-input"></div>
            <div class="form-item"><label>预约类型</label>
              <select v-model="form.appointmentType" class="form-input">
                <option value="1">初诊</option>
                <option value="2">复诊</option>
                <option value="3">会诊</option>
              </select>
            </div>
            <div class="form-item"><label>就诊卡号</label><input v-model="form.visitCardNo" class="form-input"></div>
            <div class="form-item full"><label>病情描述</label><textarea v-model="form.diseaseDescription"
                                                                         class="form-input" rows="3"></textarea></div>
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
import {
  getAppointmentList,
  addAppointment,
  updateAppointment,
  deleteAppointment,
  updateAppointmentStatus
} from '@/api/medical.js'

export default {
  name: 'Appointment',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {appointmentNo: '', userName: '', appointmentStatus: '', appointmentDate: '', pageNum: 1, pageSize: 10},
      modal: {show: false, isEdit: false},
      form: {}, editId: null, submitting: false
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
      if (!filename) return '';
      if (filename.startsWith('http')) return filename;
      return 'http://localhost:8080/common/download?name=' + encodeURIComponent(filename)
    },
    async loadData() {
      this.loading = true
      try {
        const res = await getAppointmentList(this.query)
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
        appointmentNo: '',
        userName: '',
        appointmentStatus: '',
        appointmentDate: '',
        pageNum: 1,
        pageSize: 10
      };
      this.loadData()
    },
    openAdd() {
      this.editId = null;
      this.form = {appointmentType: 1};
      this.modal = {show: true, isEdit: false}
    },
    openEdit(row) {
      this.editId = row.id
      this.form = {
        userId: row.userId,
        hospitalId: row.hospitalId,
        hospitalName: row.hospitalName || '',
        departmentId: row.departmentId,
        departmentName: row.departmentName || '',
        expertId: row.expertId || '',
        expertName: row.expertName || '',
        appointmentDate: row.appointmentDate || '',
        appointmentTime: row.appointmentTime || '',
        appointmentType: Number(row.appointmentType) || 1,
        visitCardNo: row.visitCardNo || '',
        diseaseDescription: row.diseaseDescription || '',
        remark: row.remark || ''
      }
      this.modal = {show: true, isEdit: true}
    },
    async handleSubmit() {
      if (!this.form.userId) return alert('患者用户ID不能为空')
      if (!this.form.hospitalId) return alert('医院ID不能为空')
      if (!this.form.departmentId) return alert('科室ID不能为空')
      if (!this.form.appointmentDate) return alert('预约日期不能为空')
      this.submitting = true
      try {
        if (this.modal.isEdit) await updateAppointment(this.editId, this.form)
        else await addAppointment(this.form)
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
    async confirmAppt(row) {
      if (!confirm('确认该预约？')) return
      try {
        await updateAppointmentStatus(row.id, 2);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async completeAppt(row) {
      if (!confirm('确认完成该预约？')) return
      try {
        await updateAppointmentStatus(row.id, 4);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async cancelAppt(row) {
      if (!confirm('确认取消该预约？')) return
      try {
        await updateAppointmentStatus(row.id, 3);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除预约「' + row.appointmentNo + '」？')) return
      try {
        await deleteAppointment(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    apptTypeLabel(t) {
      return {1: '初诊', 2: '复诊', 3: '会诊'}[t] || '-'
    },
    apptStatusLabel(s) {
      return {1: '待确认', 2: '已确认', 3: '已取消', 4: '已完成', 5: '已过期'}[s] || '-'
    },
    apptStatusClass(s) {
      return {1: 'tag-yellow', 2: 'tag-blue', 3: 'tag-gray', 4: 'tag-green', 5: 'tag-gray'}[s] || ''
    },
    fmtTime(t) {
      return t ? new Date(t).toLocaleString('zh-CN') : '-'
    }
  }
}
</script>
<style scoped>
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

.filter-input.w140 {
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


.code-tag {
  font-family: monospace;
  font-size: 12px;
  color: var(--text-accent);
  background: var(--bg-hover-nav);
  padding: 2px 6px;
  border-radius: 4px;
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

.sub {
  font-size: 11px;
  color: var(--text-dim);
}

.ellipsis {
  display: block;
  max-width: 110px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.date-badge {
  color: var(--text-cyan);
  font-weight: 500;
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

.avatar-img {
  object-fit: cover;
  background: none;
}
</style>