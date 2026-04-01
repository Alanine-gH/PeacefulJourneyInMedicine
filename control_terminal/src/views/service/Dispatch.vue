<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>调度派单</h2>
      <span class="page-desc">管理陪诊师订单派单记录（service_dispatch）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.orderNo" class="filter-input" placeholder="订单号" @keyup.enter="loadData">
        <input v-model="query.accompanistName" class="filter-input" placeholder="陪诊师姓名" @keyup.enter="loadData">
        <select v-model="query.dispatchType" class="filter-select">
          <option value="">全部类型</option>
          <option value="1">自动派单</option>
          <option value="2">手动派单</option>
        </select>
        <select v-model="query.dispatchStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待接单</option>
          <option value="2">已接单</option>
          <option value="3">已拒绝</option>
          <option value="4">已取消</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 手动派单</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>派单编号</th>
          <th>订单号</th>
          <th>陪诊师</th>
          <th>派单类型</th>
          <th>状态</th>
          <th>推送时间</th>
          <th>接单时间</th>
          <th>拒绝原因</th>
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
          <td><code class="code-tag">{{ row.dispatchNo }}</code></td>
          <td><code class="code-tag green">{{ row.orderNo }}</code></td>
          <td>
            <div class="user-cell"><img v-if="row.accompanistAvatar" :src="getFileUrl(row.accompanistAvatar)"
                                        class="avatar-sm avatar-img" alt="">
              <div v-else class="avatar-sm">{{ (row.accompanistName || '?')[0] }}</div>
              <div>
                <div>{{ row.accompanistName }}</div>
                <div class="sub">{{ row.accompanistPhone || '' }}</div>
              </div>
            </div>
          </td>
          <td><span class="tag" :class="row.dispatchType===1?'tag-blue':'tag-purple'">{{
              row.dispatchType === 1 ? '自动' : '手动'
            }}</span></td>
          <td><span class="tag" :class="dispatchStatusClass(row.dispatchStatus)">{{
              dispatchStatusLabel(row.dispatchStatus)
            }}</span></td>
          <td>{{ fmtTime(row.pushTime) }}</td>
          <td>{{ row.receiveTime ? fmtTime(row.receiveTime) : '-' }}</td>
          <td><span class="reason-clip">{{ row.rejectReason || '-' }}</span></td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-ok" v-if="row.dispatchStatus===1" @click="acceptDispatch(row)">接单</button>
              <button class="btn-sm btn-warn" v-if="row.dispatchStatus===1" @click="cancelDispatch(row)">取消</button>
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
    <!-- 手动派单弹窗 -->
    <div class="modal-mask" v-if="modal.show" @click.self="modal.show=false">
      <div class="modal">
        <div class="modal-header"><span>手动派单</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item full form-tip">带 * 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。</div>
            <div class="form-item"><label>订单ID *</label><input v-model="form.orderId" type="number"
                                                                 class="form-input"></div>
            <div class="form-item"><label>陪诊师ID *</label><input v-model="form.accompanistId" type="number"
                                                                   class="form-input"></div>
            <div class="form-item full"><label>备注</label><textarea v-model="form.remark" class="form-input"
                                                                     rows="2"></textarea></div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="modal.show=false">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="submitting">{{
              submitting ? '派单中...' : '确认派单'
            }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {getDispatchList, addDispatch, updateDispatchStatus, deleteDispatch} from '@/api/service.js'
import { BASE_URL } from '@/config'
import {getFileUrl,uploadFile} from "@/api/utils.js";

export default {
  name: 'Dispatch',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {orderNo: '', accompanistName: '', dispatchType: '', dispatchStatus: '', pageNum: 1, pageSize: 10},
      modal: {show: false},
      form: {}, submitting: false
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
    async loadData() {
      this.loading = true
      try {
        const res = await getDispatchList(this.query)
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
      this.query = {orderNo: '', accompanistName: '', dispatchType: '', dispatchStatus: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    openAdd() {
      this.form = {remark: ''};
      this.modal.show = true
    },
    async handleSubmit() {
      if (!this.form.orderId) return alert('订单ID不能为空')
      if (!this.form.accompanistId) return alert('陪诊师ID不能为空')
      this.submitting = true
      try {
        await addDispatch(this.form);
        this.modal.show = false;
        this.loadData()
      } catch (e) {
        alert('派单失败')
      } finally {
        this.submitting = false
      }
    },
    async acceptDispatch(row) {
      if (!confirm('确认代为陆诊师接单？')) return
      try {
        await updateDispatchStatus(row.id, 2);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async cancelDispatch(row) {
      if (!confirm('确认取消该派单？')) return
      try {
        await updateDispatchStatus(row.id, 4);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除派单「' + row.dispatchNo + '」？')) return
      try {
        await deleteDispatch(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    dispatchStatusLabel(s) {
      return {1: '待接单', 2: '已接单', 3: '已拒绝', 4: '已取消'}[s] || '-'
    },
    dispatchStatusClass(s) {
      return {1: 'tag-yellow', 2: 'tag-green', 3: 'tag-red', 4: 'tag-gray'}[s] || ''
    },
    fmtTime(t) {
      return t ? new Date(t).toLocaleString('zh-CN') : '-'
    }
  }
}
</script>
<style>
@import "@/assets/service.css";
</style>
<style scoped>


.code-tag.green {
  color: var(--text-accent);
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

.reason-clip {
  display: block;
  max-width: 120px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: rgba(252, 165, 165, 0.8);
}

.tag-purple {
  background: var(--tag-purple-bg);
  color: #c4b5fd;
}

.tag-yellow {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
}

.avatar-img {
  object-fit: cover;
  background: none;
}
</style>