<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>订单列表</h2>
      <span class="page-desc">管理所有陪诊订单（order_order 多表联查）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.orderNo" class="filter-input" placeholder="订单号" @keyup.enter="loadData">
        <input v-model="query.userName" class="filter-input" placeholder="患者姓名/手机" @keyup.enter="loadData">
        <input v-model="query.accompanistName" class="filter-input w120" placeholder="陪诊师" @keyup.enter="loadData">
        <select v-model="query.orderType" class="filter-select">
          <option value="">全部类型</option>
          <option value="1">全程陪诊</option>
          <option value="2">代办手续</option>
          <option value="3">接机服务</option>
          <option value="4">送机服务</option>
        </select>
        <select v-model="query.orderStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待付款</option>
          <option value="2">已付款</option>
          <option value="3">已确认</option>
          <option value="4">服务中</option>
          <option value="5">已完成</option>
          <option value="6">已取消</option>
          <option value="7">退款中</option>
          <option value="8">已退款</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>订单号</th>
          <th>患者</th>
          <th>陪诊师</th>
          <th>类型</th>
          <th>状态</th>
          <th>金额</th>
          <th>服务/预约日期</th>
          <th>下单时间</th>
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
          <td><code class="order-no">{{ row.orderNo }}</code></td>
          <td>
            <div class="user-cell"><img v-if="row.userAvatar" :src="getFileUrl(row.userAvatar)"
                                        class="avatar-sm avatar-img" alt="">
              <div v-else class="avatar-sm">{{ (row.userName || '?')[0] }}</div>
              <div>
                <div>{{ row.userName || '-' }}</div>
                <div class="sub">{{ row.userPhone || '' }}</div>
              </div>
            </div>
          </td>
          <td>{{ row.accompanistName || '未分配' }}</td>
          <td><span class="tag tag-blue">{{ orderTypeLabel(row.orderType) }}</span></td>
          <td><span class="tag" :class="statusClass(row.orderStatus)">{{ statusLabel(row.orderStatus) }}</span></td>
          <td><span class="amount">{{ row.currency }} {{ row.totalAmount }}</span></td>
          <td>{{ row.serviceStartTime ? fmtDate(row.serviceStartTime) : (row.appointmentDate || '-') }}</td>
          <td>{{ fmtTime(row.createTime) }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm btn-cancel" v-if="row.orderStatus<=3" @click="handleCancel(row)">取消</button>
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
    <!-- 编辑弹窗 -->
    <div class="modal-mask" v-if="modal.show" @click.self="modal.show=false">
      <div class="modal modal-lg">
        <div class="modal-header"><span>编辑订单</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item full form-tip">带 <em>*</em> 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。
            </div>
            <div class="form-item"><label>陪诊师ID</label><input v-model="form.accompanistId" class="form-input"
                                                                 placeholder="陪诊师ID"></div>
            <div class="form-item"><label>预约医院</label><input v-model="form.appointmentHospital" class="form-input">
            </div>
            <div class="form-item"><label>预约科室</label><input v-model="form.appointmentDepartment"
                                                                 class="form-input"></div>
            <div class="form-item"><label>预约日期</label><input v-model="form.appointmentDate" type="date"
                                                                 class="form-input"></div>
            <div class="form-item"><label>服务开始时间</label><input v-model="form.serviceStartTime"
                                                                     type="datetime-local" class="form-input"></div>
            <div class="form-item"><label>服务结束时间</label><input v-model="form.serviceEndTime" type="datetime-local"
                                                                     class="form-input"></div>
            <div class="form-item"><label>总金额</label><input v-model="form.totalAmount" type="number"
                                                               class="form-input"></div>
            <div class="form-item"><label>货币</label><input v-model="form.currency" class="form-input"
                                                             placeholder="USD"></div>
            <div class="form-item"><label>航班号</label><input v-model="form.flightNumber" class="form-input"></div>
            <div class="form-item"><label>酒店名称</label><input v-model="form.hotelName" class="form-input"></div>
            <div class="form-item full"><label>病情描述</label><textarea v-model="form.diseaseDescription"
                                                                         class="form-input" rows="2"></textarea></div>
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
      <div class="modal modal-xl">
        <div class="modal-header"><span>订单详情 — {{ detailModal.data && detailModal.data.orderNo }}</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-section">
            <div class="section-title">基本信息</div>
            <div class="detail-grid">
              <div class="detail-item"><span class="dk">患者</span><span class="dv">{{
                  detailModal.data.userName
                }} {{ detailModal.data.userPhone }}</span></div>
              <div class="detail-item"><span class="dk">陪诊师</span><span
                  class="dv">{{ detailModal.data.accompanistName || '未分配' }}</span></div>
              <div class="detail-item"><span class="dk">订单类型</span><span
                  class="dv">{{ orderTypeLabel(detailModal.data.orderType) }}</span></div>
              <div class="detail-item"><span class="dk">订单状态</span><span
                  class="dv">{{ statusLabel(detailModal.data.orderStatus) }}</span></div>
              <div class="detail-item"><span class="dk">总金额</span><span
                  class="dv amount">{{ detailModal.data.currency }} {{ detailModal.data.totalAmount }}</span></div>
              <div class="detail-item"><span class="dk">实付金额</span><span
                  class="dv amount">{{ detailModal.data.currency }} {{
                  detailModal.data.actualPaidAmount || '0.00'
                }}</span></div>
              <div class="detail-item"><span class="dk">预约医院</span><span
                  class="dv">{{ detailModal.data.appointmentHospital || '-' }}</span></div>
              <div class="detail-item"><span class="dk">预约科室</span><span
                  class="dv">{{ detailModal.data.appointmentDepartment || '-' }}</span></div>
              <div class="detail-item"><span class="dk">就诊患者</span><span
                  class="dv">{{ detailModal.data.patientName || '-' }}</span></div>
              <div class="detail-item"><span class="dk">护照号</span><span
                  class="dv">{{ detailModal.data.patientPassport || '-' }}</span></div>
              <div class="detail-item"><span class="dk">航班号</span><span
                  class="dv">{{ detailModal.data.flightNumber || '-' }}</span></div>
              <div class="detail-item"><span class="dk">酒店</span><span class="dv">{{
                  detailModal.data.hotelName || '-'
                }}</span></div>
              <div class="detail-item full"><span class="dk">病情描述</span><span
                  class="dv">{{ detailModal.data.diseaseDescription || '-' }}</span></div>
              <div class="detail-item full" v-if="detailModal.data.cancelReason"><span class="dk">取消原因</span><span
                  class="dv fail">{{ detailModal.data.cancelReason }}</span></div>
            </div>
          </div>
          <div class="detail-section" v-if="detailModal.data.serviceItems&&detailModal.data.serviceItems.length">
            <div class="section-title">服务项目</div>
            <table class="sub-table">
              <thead>
              <tr>
                <th>名称</th>
                <th>类型</th>
                <th>单价</th>
                <th>数量</th>
                <th>单位</th>
                <th>小计</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="s in detailModal.data.serviceItems" :key="s.id">
                <td>{{ s.serviceName }}</td>
                <td>{{ svcTypeLabel(s.serviceType) }}</td>
                <td>{{ s.unitPrice }}</td>
                <td>{{ s.quantity }}</td>
                <td>{{ s.unit }}</td>
                <td class="amount">{{ s.subtotal }}</td>
              </tr>
              </tbody>
            </table>
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
import {getOrderList, getOrderDetail, updateOrder, deleteOrder, updateOrderStatus} from '@/api/order.js'

export default {
  name: 'OrderList',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {orderNo: '', userName: '', accompanistName: '', orderType: '', orderStatus: '', pageNum: 1, pageSize: 10},
      modal: {show: false}, detailModal: {show: false, data: null},
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
        const res = await getOrderList(this.query)
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
        orderNo: '',
        userName: '',
        accompanistName: '',
        orderType: '',
        orderStatus: '',
        pageNum: 1,
        pageSize: 10
      };
      this.loadData()
    },
    toLocalDT(val) {
      // 将后端返回的 ISO 字符串或数组转成 datetime-local 格式 "YYYY-MM-DDTHH:mm"
      if (!val) return ''
      try {
        const d = new Date(Array.isArray(val) ? new Date(...val) : val)
        if (isNaN(d)) return ''
        const pad = n => String(n).padStart(2, '0')
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) +
            'T' + pad(d.getHours()) + ':' + pad(d.getMinutes())
      } catch {
        return ''
      }
    },
    openEdit(row) {
      this.editId = row.id
      this.form = {
        accompanistId: row.accompanistId,
        appointmentHospital: row.appointmentHospital || '',
        appointmentDepartment: row.appointmentDepartment || '',
        appointmentDate: row.appointmentDate || '',
        serviceStartTime: this.toLocalDT(row.serviceStartTime),
        serviceEndTime: this.toLocalDT(row.serviceEndTime),
        totalAmount: row.totalAmount,
        currency: row.currency || 'USD',
        flightNumber: row.flightNumber || '',
        hotelName: row.hotelName || '',
        diseaseDescription: row.diseaseDescription || '',
        remark: row.remark || ''
      }
      this.modal.show = true
    },
    async openDetail(row) {
      try {
        const res = await getOrderDetail(row.id);
        this.detailModal.data = (res && res.data) ? res.data : res;
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async handleSubmit() {
      this.submitting = true
      const payload = Object.assign({}, this.form)
      if (!payload.accompanistId) payload.accompanistId = null
      try {
        await updateOrder(this.editId, payload);
        this.modal.show = false;
        this.loadData()
      } catch (e) {
        alert('操作失败：' + (e.response && e.response.data && e.response.data.msg ? e.response.data.msg : '请检查输入'))
      } finally {
        this.submitting = false
      }
    },
    async handleCancel(row) {
      const reason = prompt('请输入取消原因：')
      if (reason === null) return
      try {
        await updateOrderStatus(row.id, 6, reason);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除订单 ' + row.orderNo + '？')) return
      try {
        await deleteOrder(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    orderTypeLabel(t) {
      return {1: '全程陪诊', 2: '代办手续', 3: '接机服务', 4: '送机服务'}[t] || '-'
    },
    statusLabel(s) {
      return {
        1: '待付款',
        2: '已付款',
        3: '已确认',
        4: '服务中',
        5: '已完成',
        6: '已取消',
        7: '退款中',
        8: '已退款'
      }[s] || '-'
    },
    statusClass(s) {
      return {
        1: 'tag-yellow',
        2: 'tag-blue',
        3: 'tag-blue',
        4: 'tag-green',
        5: 'tag-green',
        6: 'tag-gray',
        7: 'tag-red',
        8: 'tag-gray'
      }[s] || ''
    },
    svcTypeLabel(t) {
      return {1: '基础服务', 2: '增值服务', 3: '附加服务'}[t] || '-'
    },
    fmtDate(t) {
      return t ? t.toString().slice(0, 10) : '-'
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

.filter-input.w120 {
  width: 110px;
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


.order-no {
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

.tag-gray {
  background: var(--bg-collapse);
  color: var(--tag-gray-text);
}

.amount {
  color: var(--text-yellow);
  font-weight: 600;
}

.text-muted {
  color: var(--text-placeholder);
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

.btn-cancel, .btn-del {
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
  width: 600px;
  max-width: 95vw;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.modal-lg {
  width: 700px;
}

.modal-xl {
  width: 820px;
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

.fail {
  color: var(--text-red);
}

.sub-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.sub-table th {
  padding: 8px 12px;
  text-align: left;
  color: var(--text-th);
  font-size: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.sub-table td {
  padding: 8px 12px;
  color: var(--text-nav);
  border-bottom: 1px solid var(--border-table-r);
}

.avatar-img {
  object-fit: cover;
  background: none;
}
</style>