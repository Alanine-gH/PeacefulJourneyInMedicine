<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>智能分诊</h2>
      <span class="page-desc">查看和管理患者智能分诊记录（medical_triage_record）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.userName" class="filter-input" placeholder="患者姓名/手机" @keyup.enter="loadData">
        <input v-model="query.recommendedDepartment" class="filter-input" placeholder="推荐科室"
               @keyup.enter="loadData">
        <select v-model="query.triageStatus" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">待分诊</option>
          <option value="2">AI分诊中</option>
          <option value="3">AI完成</option>
          <option value="4">待人工复审</option>
          <option value="5">人工复审完成</option>
          <option value="6">已生成方案</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>分诊单号</th>
          <th>患者</th>
          <th>主诉</th>
          <th>疼痛部位</th>
          <th>疼痛等级</th>
          <th>推荐科室</th>
          <th>AI置信度</th>
          <th>分诊状态</th>
          <th>AI分析时间</th>
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
          <td><code class="code-tag">{{ row.triageNo }}</code></td>
          <td>
            <div class="user-cell">
              <div class="avatar-sm">{{ (row.userName || '?')[0] }}</div>
              <div>
                <div>{{ row.userName }}</div>
                <div class="sub">{{ row.userPhone || '' }}</div>
              </div>
            </div>
          </td>
          <td><span class="content-clip">{{ row.chiefComplaint || '-' }}</span></td>
          <td>{{ row.painLocation || '-' }}</td>
          <td><span class="pain-badge" :class="painClass(row.painLevel)">{{ row.painLevel || '-' }}</span></td>
          <td><span class="dept-tag">{{ row.recommendedDepartment || '-' }}</span></td>
          <td>
            <div class="confidence-bar" v-if="row.aiConfidence">
              <div class="confidence-fill" :style="{width: (row.aiConfidence*100).toFixed(0)+'%'}"></div>
              <span class="confidence-text">{{ (row.aiConfidence * 100).toFixed(1) }}%</span>
            </div>
            <span v-else class="text-muted">-</span></td>
          <td><span class="tag" :class="triageStatusClass(row.triageStatus)">{{
              triageStatusLabel(row.triageStatus)
            }}</span></td>
          <td>{{ row.aiAnalysisTime ? fmtTime(row.aiAnalysisTime) : '-' }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-ok" v-if="row.triageStatus===4" @click="doReview(row)">复审</button>
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
      <div class="modal modal-xl">
        <div class="modal-header"><span>分诊详情 — {{ detailModal.data && detailModal.data.triageNo }}</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-section">
            <div class="section-title">患者基本信息</div>
            <div class="detail-grid">
              <div class="detail-item"><span class="dk">姓名</span><span class="dv">{{
                  detailModal.data.userName
                }}</span></div>
              <div class="detail-item"><span class="dk">手机</span><span class="dv">{{
                  detailModal.data.userPhone || '-'
                }}</span></div>
              <div class="detail-item"><span class="dk">性别</span><span
                  class="dv">{{ detailModal.data.gender === 1 ? '\u7537' : '\u5973' }}</span></div>
              <div class="detail-item"><span class="dk">年龄</span><span class="dv">{{
                  detailModal.data.age || '-'
                }}</span></div>
              <div class="detail-item"><span class="dk">疼痛部位</span><span
                  class="dv">{{ detailModal.data.painLocation || '-' }}</span></div>
              <div class="detail-item"><span class="dk">疼痛等级</span><span
                  class="dv">{{ detailModal.data.painLevel || '-' }}/10</span></div>
              <div class="detail-item full"><span class="dk">主诉</span><span
                  class="dv">{{ detailModal.data.chiefComplaint || '-' }}</span></div>
              <div class="detail-item full"><span class="dk">症状描述</span><span
                  class="dv">{{ detailModal.data.symptomDescription || '-' }}</span></div>
              <div class="detail-item full"><span class="dk">既往病史</span><span
                  class="dv">{{ detailModal.data.medicalHistory || '-' }}</span></div>
              <div class="detail-item full"><span class="dk">过敏史</span><span
                  class="dv">{{ detailModal.data.allergyHistory || '-' }}</span></div>
            </div>
          </div>
          <div class="detail-section">
            <div class="section-title">AI 分诊结果</div>
            <div class="detail-grid">
              <div class="detail-item"><span class="dk">推荐科室</span><span
                  class="dv dept">{{ detailModal.data.recommendedDepartment || '-' }}</span></div>
              <div class="detail-item"><span class="dk">AI置信度</span><span class="dv">{{
                  detailModal.data.aiConfidence ? (detailModal.data.aiConfidence * 100).toFixed(1) + '%' : '-'
                }}</span></div>
              <div class="detail-item full"><span class="dk">推荐医院</span><span
                  class="dv">{{ detailModal.data.recommendedHospitals || '-' }}</span></div>
              <div class="detail-item full"><span class="dk">分诊结果</span><span
                  class="dv ai-result">{{ detailModal.data.triageResult || '-' }}</span></div>
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
import {getTriageList, getTriageDetail, updateTriageStatus, deleteTriage} from '@/api/medical.js'

export default {
  name: 'Triage',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {userName: '', recommendedDepartment: '', triageStatus: '', pageNum: 1, pageSize: 10},
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
        const res = await getTriageList(this.query)
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
      this.query = {userName: '', recommendedDepartment: '', triageStatus: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    async openDetail(row) {
      try {
        const res = await getTriageDetail(row.id);
        this.detailModal.data = (res && res.data) ? res.data : res;
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async doReview(row) {
      if (!confirm('确认完成人工复审？')) return
      try {
        await updateTriageStatus(row.id, 5);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除分诊记录 ' + row.triageNo + '？')) return
      try {
        await deleteTriage(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    triageStatusLabel(s) {
      return {1: '待分诊', 2: 'AI分诊中', 3: 'AI完成', 4: '待人工复审', 5: '人工复审完成', 6: '已生成方案'}[s] || '-'
    },
    triageStatusClass(s) {
      return {1: 'tag-gray', 2: 'tag-blue', 3: 'tag-blue', 4: 'tag-yellow', 5: 'tag-green', 6: 'tag-green'}[s] || ''
    },
    painClass(p) {
      const v = parseInt(p);
      return v >= 8 ? 'pain-high' : v >= 5 ? 'pain-mid' : 'pain-low'
    },
    fmtTime(t) {
      return t ? new Date(t).toLocaleString('zh-CN') : '-'
    }
  }
}
</script>
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

.filter-input {
  background: var(--bg-input);
  border: 1px solid var(--border-input);
  border-radius: 7px;
  padding: 7px 12px;
  color: var(--text-input);
  font-size: 13px;
  outline: none;
  width: 150px;
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

.content-clip {
  display: block;
  max-width: 150px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pain-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 700;
}

.pain-high {
  background: rgba(239, 68, 68, 0.2);
  color: var(--text-red);
}

.pain-mid {
  background: var(--tag-yellow-bg);
  color: var(--text-yellow);
}

.pain-low {
  background: var(--tag-green-bg);
  color: var(--text-accent);
}

.dept-tag {
  color: var(--text-cyan);
  font-weight: 500;
}

.confidence-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 80px;
}

.confidence-fill {
  height: 4px;
  background: linear-gradient(90deg, #6ee7b7, #3b82f6);
  border-radius: 2px;
  transition: width 0.3s;
}

.confidence-text {
  font-size: 11px;
  color: var(--text-sub);
  white-space: nowrap;
}

.text-muted {
  color: var(--text-placeholder);
}

.btn-view {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.3);
  color: var(--text-blue);
}

.modal-xl {
  width: 820px;
}

.detail-section {
  margin-bottom: 18px;
}

.section-title {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-accent);
  letter-spacing: 1px;
  margin-bottom: 10px;
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

.dept {
  color: var(--text-cyan);
  font-weight: 600;
}

.ai-result {
  color: #d1fae5;
  line-height: 1.6;
  white-space: pre-wrap;
}
</style>

<style>
@import '@/assets/medical.css';
</style>