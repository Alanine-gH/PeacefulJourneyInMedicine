<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>订单评价</h2>
      <span class="page-desc">查看用户对陪诊服务的评价（order_evaluation JOIN user_user、user_accompanist）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.orderNo" class="filter-input" placeholder="订单号" @keyup.enter="loadData">
        <input v-model="query.minScore" class="filter-input w80" type="number" placeholder="最低分" min="1" max="5"
               step="0.5">
        <input v-model="query.maxScore" class="filter-input w80" type="number" placeholder="最高分" min="1" max="5"
               step="0.5">
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>订单号</th>
          <th>用户</th>
          <th>陪诊师</th>
          <th>评分</th>
          <th>标签</th>
          <th>评价内容</th>
          <th>是否回复</th>
          <th>评价时间</th>
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
          <td class="id-cell">{{ row.id }}</td>
          <td><code class="order-no">{{ row.orderNo }}</code></td>
          <td>{{ row.username || '-' }}</td>
          <td>{{ row.accompanistName || '-' }}</td>
          <td><span class="score-badge" :class="scoreClass(row.ratingScore)">⭐ {{ row.ratingScore }}</span></td>
          <td>
            <div class="label-wrap"><span class="label-tag" v-for="(lb,i) in splitLabels(row.ratingLabels)"
                                          :key="i">{{ lb }}</span></div>
          </td>
          <td><span class="content-clip">{{ row.evaluationContent || '-' }}</span></td>
          <td><span class="tag" :class="row.evaluationReply?'tag-green':'tag-gray'">{{
              row.evaluationReply ? '已回复' : '未回复'
            }}</span></td>
          <td>{{ fmtTime(row.createTime) }}</td>
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
        <div class="modal-header"><span>评价详情</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-grid">
            <div class="detail-item"><span class="dk">订单号</span><span class="dv mono">{{
                detailModal.data.orderNo
              }}</span></div>
            <div class="detail-item"><span class="dk">用户</span><span class="dv">{{ detailModal.data.username }}</span>
            </div>
            <div class="detail-item"><span class="dk">陪诊师</span><span
                class="dv">{{ detailModal.data.accompanistName || '-' }}</span></div>
            <div class="detail-item"><span class="dk">评分</span><span
                class="dv score-text">⭐ {{ detailModal.data.ratingScore }}</span></div>
            <div class="detail-item"><span class="dk">评分维度</span><span
                class="dv">{{ detailModal.data.ratingDimension || '-' }}</span></div>
            <div class="detail-item"><span class="dk">评价时间</span><span
                class="dv">{{ fmtTime(detailModal.data.createTime) }}</span></div>
            <div class="detail-item full"><span class="dk">评价标签</span>
              <div class="label-wrap"><span class="label-tag"
                                            v-for="(lb,i) in splitLabels(detailModal.data.ratingLabels)" :key="i">{{
                  lb
                }}</span></div>
            </div>
            <div class="detail-item full"><span class="dk">评价内容</span><span
                class="dv">{{ detailModal.data.evaluationContent || '-' }}</span></div>
            <div class="detail-item full" v-if="detailModal.data.evaluationImages">
              <span class="dk">评价图片</span>
              <div class="img-wrap"><img v-for="(img,i) in splitLabels(detailModal.data.evaluationImages)" :key="i"
                                         :src="img" class="eval-img"></div>
            </div>
            <div class="detail-item full" v-if="detailModal.data.evaluationReply"><span
                class="dk">陪诊师回复</span><span class="dv reply">{{ detailModal.data.evaluationReply }}</span></div>
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
import {getEvaluationList, getEvaluationDetail, deleteEvaluation} from '@/api/order.js'

export default {
  name: 'Evaluation',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {orderNo: '', minScore: '', maxScore: '', pageNum: 1, pageSize: 10},
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
        const res = await getEvaluationList(this.query)
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
      this.query = {orderNo: '', minScore: '', maxScore: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    async openDetail(row) {
      try {
        const res = await getEvaluationDetail(row.id);
        this.detailModal.data = (res && res.data) ? res.data : res;
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除该评价？')) return
      try {
        await deleteEvaluation(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    splitLabels(str) {
      return str ? str.split(',').filter(Boolean) : []
    },
    scoreClass(s) {
      const v = parseFloat(s);
      return v >= 4.5 ? 'score-high' : v >= 3 ? 'score-mid' : 'score-low'
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
  margin-bottom: 16px;
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

.filter-input.w80 {
  width: 80px;
}

.filter-input::placeholder {
  color: var(--text-placeholder);
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

.id-cell {
  color: var(--text-dim);
  font-size: 12px;
}

.order-no {
  font-family: monospace;
  font-size: 12px;
  color: var(--text-accent);
  background: var(--bg-hover-nav);
  padding: 2px 6px;
  border-radius: 4px;
}

.score-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.score-high {
  background: var(--tag-green-bg);
  color: var(--text-accent);
}

.score-mid {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
}

.score-low {
  background: var(--tag-red-bg);
  color: var(--text-red);
}

.score-text {
  color: var(--text-yellow);
  font-weight: 600;
}

.label-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.label-tag {
  background: rgba(59, 130, 246, 0.12);
  color: var(--text-blue);
  padding: 1px 7px;
  border-radius: 3px;
  font-size: 11px;
}

.content-clip {
  display: block;
  max-width: 180px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 500;
}

.tag-green {
  background: var(--tag-green-bg);
  color: var(--text-accent);
}

.tag-gray {
  background: var(--bg-collapse);
  color: var(--tag-gray-text);
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
  width: 680px;
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

.detail-grid {
  display: grid;
  grid-template-columns:1fr 1fr;
  gap: 10px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
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

.mono {
  font-family: monospace;
  font-size: 12px;
}

.reply {
  color: var(--text-accent);
  font-style: italic;
}

.img-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 4px;
}

.eval-img {
  width: 100px;
  height: 70px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid var(--border-input);
}
</style>