<template>
  <div class="page-wrap">
    <div class="page-header"><h2>支付记录管理</h2><span class="page-desc">管理所有支付记录（payment_record 表）</span></div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.userId" class="filter-input" placeholder="用户ID" @keyup.enter="loadData">
        <input v-model="query.orderNo" class="filter-input" placeholder="订单号" @keyup.enter="loadData">
        <select v-model="query.status" class="filter-select"><option value="">全部状态</option><option value="1">待支付</option><option value="2">已支付</option><option value="3">已取消</option><option value="4">退款中</option><option value="5">已退款</option></select>
        <select v-model="query.paymentType" class="filter-select"><option value="">全部方式</option><option value="wechat">微信</option><option value="alipay">支付宝</option><option value="cash">现金</option></select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead><tr><th>ID</th><th>支付单号</th><th>业务订单号</th><th>用户ID</th><th>支付方式</th><th>金额</th><th>状态</th><th>支付时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-if="loading"><td colspan="9" class="empty">加载中...</td></tr>
          <tr v-else-if="list.length===0"><td colspan="9" class="empty">暂无数据</td></tr>
          <tr v-for="row in list" :key="row.id">
            <td class="id-cell">{{row.id}}</td>
            <td class="mono">{{row.paymentNo}}</td>
            <td class="mono">{{row.orderNo||'-'}}</td>
            <td>{{row.userId}}</td>
            <td>{{typeLabel(row.paymentType)}}</td>
            <td><span class="amount">{{row.currency||'CNY'}} {{row.amount}}</span></td>
            <td><span class="tag" :class="statusClass(row.status)">{{statusLabel(row.status)}}</span></td>
            <td>{{fmtTime(row.payTime||row.createTime)}}</td>
            <td><div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-warn" v-if="row.status===2" @click="openRefund(row)">退款</button>
            </div></td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button :disabled="query.page<=1" @click="changePage(query.page-1)">上一页</button>
        <span>第{{query.page}}页/共{{totalPages}}页（{{total}}条）</span>
        <button :disabled="query.page>=totalPages" @click="changePage(query.page+1)">下一页</button>
      </div>
    </div>
    <!-- 退款弹窗 -->
    <div v-if="refund.visible" class="modal-mask" @click.self="refund.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>申请退款 - {{refund.paymentNo}}</span><button @click="refund.visible=false">✕</button></div>
        <div class="modal-body">
          <div class="form-row"><label>退款金额</label><input v-model="refund.amount" type="number" placeholder="留空则全额退款"></div>
          <div class="form-row"><label>退款原因 *</label><textarea v-model="refund.reason" rows="3" placeholder="请输入退款原因"></textarea></div>
        </div>
        <div class="modal-footer"><button class="btn-cancel" @click="refund.visible=false">取消</button><button class="btn-confirm" @click="submitRefund">提交退款</button></div>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div v-if="detail.visible" class="modal-mask" @click.self="detail.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>支付详情</span><button @click="detail.visible=false">✕</button></div>
        <div class="modal-body"><div class="detail-grid"><div v-for="(val,key) in detail.data" :key="key" class="detail-row"><span class="detail-label">{{key}}</span><span class="detail-val">{{val}}</span></div></div></div>
      </div>
    </div>
  </div>
</template>
<script>
const B='http://localhost:8080'
const H=()=>{const t=localStorage.getItem('token');return t?{Authorization:'Bearer '+t}:{}}
const api=async(u,o={})=>(await fetch(B+u,{headers:{'Content-Type':'application/json',...H()},...o})).json()
export default{
  data(){return{list:[],loading:false,total:0,totalPages:1,
    query:{userId:'',orderNo:'',status:'',paymentType:'',page:1,pageSize:15},
    refund:{visible:false,paymentNo:'',amount:'',reason:''},
    detail:{visible:false,data:{}}}},
  mounted(){this.loadData()},
  methods:{
    async loadData(){this.loading=true;const userId=this.query.userId||1;const p=new URLSearchParams({userId,page:this.query.page,pageSize:this.query.pageSize});if(this.query.status)p.append('status',this.query.status);const r=await api('/payment/records?'+p);if(r.code===200&&r.data){this.list=r.data.records||[];this.total=r.data.total||0;this.totalPages=r.data.pages||1}this.loading=false},
    resetQuery(){this.query={userId:'',orderNo:'',status:'',paymentType:'',page:1,pageSize:15};this.loadData()},
    changePage(p){this.query.page=p;this.loadData()},
    openDetail(row){this.detail={visible:true,data:row}},
    openRefund(row){this.refund={visible:true,paymentNo:row.paymentNo,amount:'',reason:''}},
    async submitRefund(){if(!this.refund.reason){alert('请填写退款原因');return}const body={reason:this.refund.reason};if(this.refund.amount)body.amount=this.refund.amount;const r=await api('/payment/refund/'+this.refund.paymentNo,{method:'POST',body:JSON.stringify(body)});if(r.code===200||r.code===1){this.refund.visible=false;this.loadData()}else alert(r.msg||'退款失败')},
    statusLabel(s){return{1:'待支付',2:'已支付',3:'已取消',4:'退款中',5:'已退款'}[s]||'未知'},
    statusClass(s){return{1:'tag-orange',2:'tag-green',3:'tag-gray',4:'tag-blue',5:'tag-gray'}[s]||'tag-gray'},
    typeLabel(t){return{wechat:'微信支付',alipay:'支付宝',cash:'现金'}[t]||t||'-'},
    fmtTime(v){if(!v)return'-';return new Date(v).toLocaleString('zh-CN',{hour12:false})}
  }
}
</script>
<style scoped>
.page-wrap{padding:24px}.page-header{margin-bottom:18px}.page-header h2{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-muted)}
.toolbar{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px;flex-wrap:wrap;gap:10px}.filters{display:flex;gap:8px;flex-wrap:wrap}
.filter-input{padding:7px 12px;border:1px solid var(--border-input);border-radius:6px;font-size:13px;background:var(--bg-input);color:var(--text-primary);width:130px}
.filter-select{padding:7px 10px;border:1px solid var(--border-input);border-radius:6px;font-size:13px;background:var(--bg-input);color:var(--text-primary)}
.btn-search{padding:7px 16px;background:#3b82f6;color:#fff;border:none;border-radius:6px;cursor:pointer;font-size:13px}
.btn-reset{padding:7px 14px;background:var(--bg-btn-reset);color:var(--text-primary);border:1px solid var(--border-input);border-radius:6px;cursor:pointer}
.table-card{background:var(--bg-card);border-radius:12px;border:1px solid var(--border-card);overflow:hidden}
.data-table{width:100%;border-collapse:collapse;font-size:13px}
.data-table th{padding:12px 14px;background:var(--bg-thead);color:var(--text-muted);font-weight:600;text-align:left;border-bottom:1px solid var(--border-card)}
.data-table td{padding:11px 14px;border-bottom:1px solid var(--border-row);color:var(--text-primary);vertical-align:middle}
.data-table tr:last-child td{border-bottom:none}.data-table tr:hover td{background:var(--bg-row-hover)}
.empty{text-align:center;color:var(--text-muted);padding:40px}.id-cell,.mono{font-family:monospace;color:var(--text-muted);font-size:12px}
.amount{font-weight:600;color:#059669}
.tag{padding:2px 10px;border-radius:20px;font-size:12px;font-weight:500}.tag-blue{background:#dbeafe;color:#1d4ed8}.tag-orange{background:#fef3c7;color:#b45309}.tag-green{background:#d1fae5;color:#065f46}.tag-gray{background:#f3f4f6;color:#6b7280}
.action-btns{display:flex;gap:6px}.btn-sm{padding:4px 10px;border:none;border-radius:4px;cursor:pointer;font-size:12px}.btn-view{background:#eff6ff;color:#2563eb}.btn-warn{background:#fef3c7;color:#b45309}
.pagination{display:flex;align-items:center;justify-content:center;gap:14px;padding:14px;font-size:13px;color:var(--text-muted)}
.pagination button{padding:5px 14px;border:1px solid var(--border-input);border-radius:5px;background:var(--bg-input);color:var(--text-primary);cursor:pointer}.pagination button:disabled{opacity:0.4;cursor:not-allowed}
.modal-mask{position:fixed;inset:0;background:rgba(0,0,0,0.45);z-index:1000;display:flex;align-items:center;justify-content:center}
.modal-box{background:var(--bg-card);border-radius:12px;width:520px;max-height:85vh;overflow-y:auto;box-shadow:0 8px 40px rgba(0,0,0,0.18)}
.modal-header{display:flex;justify-content:space-between;align-items:center;padding:16px 20px;border-bottom:1px solid var(--border-card);font-weight:600;font-size:15px;color:var(--text-primary)}
.modal-header button{background:none;border:none;font-size:18px;cursor:pointer;color:var(--text-muted)}
.modal-body{padding:20px}.form-row{display:flex;align-items:flex-start;margin-bottom:14px}
.form-row label{width:90px;font-size:13px;color:var(--text-muted);flex-shrink:0;padding-top:8px}
.form-row input,.form-row textarea{flex:1;padding:8px 12px;border:1px solid var(--border-input);border-radius:6px;font-size:13px;background:var(--bg-input);color:var(--text-primary)}
.form-row textarea{resize:vertical}
.modal-footer{display:flex;justify-content:flex-end;gap:10px;padding:14px 20px;border-top:1px solid var(--border-card)}
.btn-cancel{padding:8px 18px;border:1px solid var(--border-input);border-radius:6px;background:var(--bg-input);color:var(--text-primary);cursor:pointer}
.btn-confirm{padding:8px 18px;background:#3b82f6;color:#fff;border:none;border-radius:6px;cursor:pointer;font-weight:600}
.detail-grid{display:grid;grid-template-columns:1fr 1fr;gap:10px}.detail-row{display:flex;flex-direction:column;padding:8px;background:var(--bg-thead);border-radius:6px}.detail-label{font-size:11px;color:var(--text-muted);margin-bottom:2px}.detail-val{font-size:13px;color:var(--text-primary);word-break:break-all}
</style>
