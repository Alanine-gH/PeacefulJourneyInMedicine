<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>支付记录</h2>
      <span class="page-desc">查看所有支付流水（order_payment）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.orderNo" class="filter-input" placeholder="订单号" @keyup.enter="loadData">
        <input v-model="query.paymentId" class="filter-input" placeholder="流水号" @keyup.enter="loadData">
        <input v-model="query.userName" class="filter-input" placeholder="用户姓名" @keyup.enter="loadData">
        <select v-model="query.paymentStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待支付</option>
          <option value="2">支付中</option>
          <option value="3">支付成功</option>
          <option value="4">支付失败</option>
          <option value="5">已退款</option>
        </select>
        <select v-model="query.paymentMethod" class="filter-select">
          <option value="">全部方式</option>
          <option value="1">信用卡</option>
          <option value="2">支付宝</option>
          <option value="3">微信支付</option>
          <option value="4">Stripe</option>
          <option value="5">Adyen</option>
          <option value="6">银行转账</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>流水号</th>
          <th>订单号</th>
          <th>用户</th>
          <th>支付类型</th>
          <th>支付方式</th>
          <th>状态</th>
          <th>金额</th>
          <th>人民币</th>
          <th>支付渠道</th>
          <th>支付时间</th>
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
          <td><code class="order-no">{{ (row.paymentId || '').slice(0, 16) }}...</code></td>
          <td><code class="order-no">{{ row.orderNo }}</code></td>
          <td>{{ row.userName || '-' }}</td>
          <td><span class="tag tag-blue">{{ payTypeLabel(row.paymentType) }}</span></td>
          <td>{{ methodLabel(row.paymentMethod) }}</td>
          <td><span class="tag" :class="payStatusClass(row.paymentStatus)">{{
              payStatusLabel(row.paymentStatus)
            }}</span></td>
          <td><span class="amount">{{ row.currency }} {{ row.amount }}</span></td>
          <td><span class="amount-rmb">¥ {{ row.rmbAmount || '-' }}</span></td>
          <td>{{ row.paymentChannel || '-' }}</td>
          <td>{{ row.paymentTime ? fmtTime(row.paymentTime) : '-' }}</td>
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
        <div class="modal-header"><span>支付详情</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-grid">
            <div class="detail-item"><span class="dk">流水号</span><span class="dv mono">{{
                detailModal.data.paymentId
              }}</span></div>
            <div class="detail-item"><span class="dk">订单号</span><span class="dv mono">{{
                detailModal.data.orderNo
              }}</span></div>
            <div class="detail-item"><span class="dk">用户</span><span class="dv">{{ detailModal.data.userName }}</span>
            </div>
            <div class="detail-item"><span class="dk">支付类型</span><span
                class="dv">{{ payTypeLabel(detailModal.data.paymentType) }}</span></div>
            <div class="detail-item"><span class="dk">支付方式</span><span
                class="dv">{{ methodLabel(detailModal.data.paymentMethod) }}</span></div>
            <div class="detail-item"><span class="dk">支付状态</span><span
                class="dv">{{ payStatusLabel(detailModal.data.paymentStatus) }}</span></div>
            <div class="detail-item"><span class="dk">金额</span><span class="dv amount">{{ detailModal.data.currency }} {{
                detailModal.data.amount
              }}</span></div>
            <div class="detail-item"><span class="dk">人民币金额</span><span
                class="dv amount">¥ {{ detailModal.data.rmbAmount || '-' }}</span></div>
            <div class="detail-item"><span class="dk">汇率</span><span
                class="dv">{{ detailModal.data.exchangeRate || '-' }}</span></div>
            <div class="detail-item"><span class="dk">支付渠道</span><span
                class="dv">{{ detailModal.data.paymentChannel || '-' }}</span></div>
            <div class="detail-item"><span class="dk">渠道交易号</span><span
                class="dv mono">{{ detailModal.data.channelTransactionId || '-' }}</span></div>
            <div class="detail-item"><span class="dk">付款人邮箱</span><span
                class="dv">{{ detailModal.data.payerEmail || '-' }}</span></div>
            <div class="detail-item"><span class="dk">支付时间</span><span
                class="dv">{{ detailModal.data.paymentTime ? fmtTime(detailModal.data.paymentTime) : '-' }}</span></div>
            <div class="detail-item"><span class="dk">回调时间</span><span
                class="dv">{{ detailModal.data.callbackTime ? fmtTime(detailModal.data.callbackTime) : '-' }}</span>
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
import {getPaymentList, getPaymentDetail, deletePayment} from '@/api/order.js'
import {getFileUrl, uploadFile} from "@/api/utils.js";

export default {
  name: 'Payment',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {orderNo: '', paymentId: '', userName: '', paymentStatus: '', paymentMethod: '', pageNum: 1, pageSize: 10},
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
    getFileUrl,uploadFile,
    async loadData() {
      this.loading = true
      try {
        const res = await getPaymentList(this.query)
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
        paymentId: '',
        userName: '',
        paymentStatus: '',
        paymentMethod: '',
        pageNum: 1,
        pageSize: 10
      };
      this.loadData()
    },
    async openDetail(row) {
      try {
        const res = await getPaymentDetail(row.id);
        this.detailModal.data = (res && res.data) ? res.data : res;
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除该支付记录？')) return
      try {
        await deletePayment(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    payTypeLabel(t) {
      return {1: '定金', 2: '尾款', 3: '全额', 4: '退款'}[t] || '-'
    },
    methodLabel(m) {
      return {1: '信用卡', 2: '支付宝', 3: '微信支付', 4: 'Stripe', 5: 'Adyen', 6: '银行转账'}[m] || '-'
    },
    payStatusLabel(s) {
      return {1: '待支付', 2: '支付中', 3: '支付成功', 4: '支付失败', 5: '已退款'}[s] || '-'
    },
    payStatusClass(s) {
      return {1: 'tag-yellow', 2: 'tag-blue', 3: 'tag-green', 4: 'tag-red', 5: 'tag-gray'}[s] || ''
    },
    fmtTime(t) {
      return t ? new Date(t).toLocaleString('zh-CN') : '-'
    }
  }
}
</script>
<style>
@import "@/assets/order.css";
</style>
<style scoped>

.toolbar {
  margin-bottom: 16px;
}

.filters {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  align-items: center;
}

.amount-rmb {
  color: rgba(253, 224, 71, 0.7);
}

.btn-del {
  background: var(--btn-warn-bg);
  border-color: var(--btn-warn-bd);
  color: var(--text-red);
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
  width: 680px;
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

.mono {
  font-family: monospace;
  font-size: 12px;
}
</style>