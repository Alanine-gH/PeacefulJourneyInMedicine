<template>
  <div class="page-wrap">
    <div class="page-header"><h2>支付记录管理</h2><span class="page-desc">管理所有支付记录（payment_record 表）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.userId" class="filter-input" placeholder="用户ID" @keyup.enter="loadData">
        <input v-model="query.orderNo" class="filter-input" placeholder="订单号" @keyup.enter="loadData">
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待支付</option>
          <option value="2">已支付</option>
          <option value="3">已取消</option>
          <option value="4">退款中</option>
          <option value="5">已退款</option>
        </select>
        <select v-model="query.paymentType" class="filter-select">
          <option value="">全部方式</option>
          <option value="wechat">微信</option>
          <option value="alipay">支付宝</option>
          <option value="cash">现金</option>
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
          <th>支付单号</th>
          <th>业务订单号</th>
          <th>用户ID</th>
          <th>支付方式</th>
          <th>金额</th>
          <th>状态</th>
          <th>支付时间</th>
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
          <td class="mono">{{ row.paymentNo }}</td>
          <td class="mono">{{ row.orderNo || '-' }}</td>
          <td>{{ row.userId }}</td>
          <td>{{ typeLabel(row.paymentType) }}</td>
          <td><span class="amount">{{ row.currency || 'CNY' }} {{ row.amount }}</span></td>
          <td><span class="tag" :class="statusClass(row.status)">{{ statusLabel(row.status) }}</span></td>
          <td>{{ fmtTime(row.payTime || row.createTime) }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-warn" v-if="row.status===2" @click="openRefund(row)">退款</button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button :disabled="query.page<=1" @click="changePage(query.page-1)">上一页</button>
        <span>第{{ query.page }}页/共{{ totalPages }}页（{{ total }}条）</span>
        <button :disabled="query.page>=totalPages" @click="changePage(query.page+1)">下一页</button>
      </div>
    </div>
    <!-- 退款弹窗 -->
    <div v-if="refund.visible" class="modal-mask" @click.self="refund.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>申请退款 - {{ refund.paymentNo }}</span>
          <button @click="refund.visible=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-row form-tip"><span>带 * 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。</span></div>
          <div class="form-row"><label>退款金额</label><input v-model="refund.amount" type="number"
                                                              placeholder="留空则全额退款"></div>
          <div class="form-row"><label>退款原因 *</label><textarea v-model="refund.reason" rows="3"
                                                                   placeholder="请输入退款原因"></textarea></div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="refund.visible=false">取消</button>
          <button class="btn-confirm" @click="submitRefund">提交退款</button>
        </div>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div v-if="detail.visible" class="modal-mask" @click.self="detail.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>支付详情</span>
          <button @click="detail.visible=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="detail-grid">
            <div v-for="(val,key) in detail.data" :key="key" class="detail-row"><span
                class="detail-label">{{ key }}</span><span class="detail-val">{{ val }}</span></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { BASE_URL } from '@/config'
import {getFileUrl, uploadFile} from "@/api/utils.js";
const H = () => {
  const t = localStorage.getItem('token');
  return t ? {Authorization: 'Bearer ' + t} : {}
}
const api = async (u, o = {}) => (await fetch(BASE_URL + u, {headers: {'Content-Type': 'application/json', ...H()}, ...o})).json()
export default {
  data() {
    return {
      list: [], loading: false, total: 0, totalPages: 1,
      query: {userId: '', orderNo: '', status: '', paymentType: '', page: 1, pageSize: 15},
      refund: {visible: false, paymentNo: '', amount: '', reason: ''},
      detail: {visible: false, data: {}}
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    getFileUrl,uploadFile,
    async loadData() {
      this.loading = true;
      const userId = this.query.userId || 1;
      const p = new URLSearchParams({userId, page: this.query.page, pageSize: this.query.pageSize});
      if (this.query.status) p.append('status', this.query.status);
      const r = await api('/payment/records?' + p);
      if (r.code === 200 && r.data) {
        this.list = r.data.records || [];
        this.total = r.data.total || 0;
        this.totalPages = r.data.pages || 1
      }
      this.loading = false
    },
    resetQuery() {
      this.query = {userId: '', orderNo: '', status: '', paymentType: '', page: 1, pageSize: 15};
      this.loadData()
    },
    changePage(p) {
      this.query.page = p;
      this.loadData()
    },
    openDetail(row) {
      this.detail = {visible: true, data: row}
    },
    openRefund(row) {
      this.refund = {visible: true, paymentNo: row.paymentNo, amount: '', reason: ''}
    },
    async submitRefund() {
      if (!this.refund.reason) {
        alert('退款原因不能为空');
        return
      }
      const body = {reason: this.refund.reason};
      if (this.refund.amount) body.amount = this.refund.amount;
      const r = await api('/payment/refund/' + this.refund.paymentNo, {method: 'POST', body: JSON.stringify(body)});
      if (r.code === 200 || r.code === 1) {
        this.refund.visible = false;
        this.loadData()
      } else alert(r.msg || '退款失败')
    },
    statusLabel(s) {
      return {1: '待支付', 2: '已支付', 3: '已取消', 4: '退款中', 5: '已退款'}[s] || '未知'
    },
    statusClass(s) {
      return {1: 'tag-orange', 2: 'tag-green', 3: 'tag-gray', 4: 'tag-blue', 5: 'tag-gray'}[s] || 'tag-gray'
    },
    typeLabel(t) {
      return {wechat: '微信支付', alipay: '支付宝', cash: '现金'}[t] || t || '-'
    },
    fmtTime(v) {
      if (!v) return '-';
      return new Date(v).toLocaleString('zh-CN', {hour12: false})
    }
  }
}
</script>
<style>
@import '@/assets/payment.css';
</style>
