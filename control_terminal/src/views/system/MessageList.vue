<template>
  <div class="page-wrap">
    <div class="page-header"><h2>系统消息管理</h2><span class="page-desc">管理所有系统消息（sys_message 表）</span></div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.receiverId" class="filter-input" placeholder="接收用户ID" @keyup.enter="loadData">
        <select v-model="query.type" class="filter-select"><option value="">全部类型</option><option value="system">系统</option><option value="order">订单</option><option value="chat">聊天</option></select>
        <select v-model="query.status" class="filter-select"><option value="">全部状态</option><option value="1">未读</option><option value="2">已读</option></select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openSend">+ 发送消息</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead><tr><th>ID</th><th>接收用户ID</th><th>类型</th><th>标题</th><th>内容摘要</th><th>状态</th><th>创建时间</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-if="loading"><td colspan="8" class="empty">加载中...</td></tr>
          <tr v-else-if="list.length===0"><td colspan="8" class="empty">暂无数据</td></tr>
          <tr v-for="row in list" :key="row.id">
            <td class="id-cell">{{row.id}}</td><td>{{row.receiverId}}</td>
            <td><span class="tag" :class="typeClass(row.type)">{{row.type||'-'}}</span></td>
            <td>{{row.title||'-'}}</td>
            <td class="content-cell">{{row.content||'-'}}</td>
            <td><span class="tag" :class="row.status===1?'tag-orange':'tag-green'">{{row.status===1?'未读':'已读'}}</span></td>
            <td>{{fmtTime(row.createTime)}}</td>
            <td><div class="action-btns"><button class="btn-sm btn-view" @click="openDetail(row)">详情</button><button class="btn-sm btn-del" @click="handleDelete(row)">删除</button></div></td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button :disabled="query.page<=1" @click="changePage(query.page-1)">上一页</button>
        <span>第{{query.page}}页/共{{totalPages}}页（{{total}}条）</span>
        <button :disabled="query.page>=totalPages" @click="changePage(query.page+1)">下一页</button>
      </div>
    </div>
    <div v-if="dialog.visible" class="modal-mask" @click.self="dialog.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>发送系统消息</span><button @click="dialog.visible=false">✕</button></div>
        <div class="modal-body">
          <div class="form-row"><label>接收用户ID *</label><input v-model="form.receiverId" placeholder="用户ID"></div>
          <div class="form-row"><label>消息类型</label><select v-model="form.type"><option value="system">系统</option><option value="order">订单</option><option value="chat">聊天</option></select></div>
          <div class="form-row"><label>标题</label><input v-model="form.title" placeholder="消息标题"></div>
          <div class="form-row"><label>内容 *</label><textarea v-model="form.content" rows="4" placeholder="消息内容"></textarea></div>
        </div>
        <div class="modal-footer"><button class="btn-cancel" @click="dialog.visible=false">取消</button><button class="btn-confirm" @click="submitSend">发送</button></div>
      </div>
    </div>
    <div v-if="detail.visible" class="modal-mask" @click.self="detail.visible=false">
      <div class="modal-box">
        <div class="modal-header"><span>消息详情</span><button @click="detail.visible=false">✕</button></div>
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
    query:{receiverId:'',type:'',status:'',page:1,pageSize:15},
    form:{receiverId:'',type:'system',title:'',content:''},
    dialog:{visible:false},detail:{visible:false,data:{}}}},
  mounted(){this.loadData()},
  methods:{
    async loadData(){this.loading=true;const p=new URLSearchParams();Object.entries(this.query).forEach(([k,v])=>{if(v!=='')p.append(k,v)});const r=await api('/message/list?'+p);if(r.code===200&&r.data){this.list=r.data.records||[];this.total=r.data.total||0;this.totalPages=r.data.pages||1}this.loading=false},
    resetQuery(){this.query={receiverId:'',type:'',status:'',page:1,pageSize:15};this.loadData()},
    changePage(p){this.query.page=p;this.loadData()},
    openSend(){this.form={receiverId:'',type:'system',title:'',content:''};this.dialog.visible=true},
    openDetail(row){this.detail={visible:true,data:row}},
    async submitSend(){if(!this.form.receiverId){alert('请填写接收用户ID');return}if(!this.form.content){alert('请填写消息内容');return}const sid=JSON.parse(localStorage.getItem('userInfo')||'{}').id||0;const r=await api('/message/send?senderId='+sid,{method:'POST',body:JSON.stringify({receiver_id:this.form.receiverId,content:this.form.content,type:this.form.type,title:this.form.title})});if(r.code===200||r.code===1){this.dialog.visible=false;this.loadData()}else alert(r.msg||'发送失败')},
    async handleDelete(row){if(!confirm('确定删除该消息？'))return;const r=await api('/message/delete/'+row.id,{method:'DELETE'});if(r.code===200||r.code===1)this.loadData();else alert(r.msg||'删除失败')},
    typeClass(t){return{system:'tag-blue',order:'tag-orange',chat:'tag-green'}[t]||'tag-gray'},
    fmtTime(v){if(!v)return'-';return new Date(v).toLocaleString('zh-CN',{hour12:false})}
  }
}
</script>
<style scoped>
.page-wrap{padding:24px}.page-header{margin-bottom:18px}.page-header h2{font-size:20px;font-weight:700;color:var(--text-primary);margin:0 0 4px}.page-desc{font-size:13px;color:var(--text-muted)}
.toolbar{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px;flex-wrap:wrap;gap:10px}.filters{display:flex;gap:8px;flex-wrap:wrap}
.filter-input{padding:7px 12px;border:1px solid var(--border-input);border-radius:6px;font-size:13px;background:var(--bg-input);color:var(--text-primary);width:140px}
.filter-select{padding:7px 10px;border:1px solid var(--border-input);border-radius:6px;font-size:13px;background:var(--bg-input);color:var(--text-primary)}
.btn-search{padding:7px 16px;background:#3b82f6;color:#fff;border:none;border-radius:6px;cursor:pointer;font-size:13px}
.btn-reset{padding:7px 14px;background:var(--bg-btn-reset);color:var(--text-primary);border:1px solid var(--border-input);border-radius:6px;cursor:pointer}
.btn-add{padding:8px 18px;background:#10b981;color:#fff;border:none;border-radius:6px;cursor:pointer;font-size:13px;font-weight:600}
.table-card{background:var(--bg-card);border-radius:12px;border:1px solid var(--border-card);overflow:hidden}
.data-table{width:100%;border-collapse:collapse;font-size:13px}
.data-table th{padding:12px 14px;background:var(--bg-thead);color:var(--text-muted);font-weight:600;text-align:left;border-bottom:1px solid var(--border-card)}
.data-table td{padding:11px 14px;border-bottom:1px solid var(--border-row);color:var(--text-primary);vertical-align:middle}
.data-table tr:last-child td{border-bottom:none}.data-table tr:hover td{background:var(--bg-row-hover)}
.empty{text-align:center;color:var(--text-muted);padding:40px}.id-cell{font-family:monospace;color:var(--text-muted);font-size:12px}
.content-cell{max-width:200px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
.tag{padding:2px 10px;border-radius:20px;font-size:12px;font-weight:500}.tag-blue{background:#dbeafe;color:#1d4ed8}.tag-orange{background:#fef3c7;color:#b45309}.tag-green{background:#d1fae5;color:#065f46}.tag-gray{background:#f3f4f6;color:#6b7280}
.action-btns{display:flex;gap:6px}.btn-sm{padding:4px 10px;border:none;border-radius:4px;cursor:pointer;font-size:12px}.btn-view{background:#eff6ff;color:#2563eb}.btn-del{background:#fef2f2;color:#dc2626}
.pagination{display:flex;align-items:center;justify-content:center;gap:14px;padding:14px;font-size:13px;color:var(--text-muted)}
.pagination button{padding:5px 14px;border:1px solid var(--border-input);border-radius:5px;background:var(--bg-input);color:var(--text-primary);cursor:pointer}.pagination button:disabled{opacity:0.4;cursor:not-allowed}
.modal-mask{position:fixed;inset:0;background:rgba(0,0,0,0.45);z-index:1000;display:flex;align-items:center;justify-content:center}
.modal-box{background:var(--bg-card);border-radius:12px;width:520px;max-height:85vh;overflow-y:auto;box-shadow:0 8px 40px rgba(0,0,0,0.18)}
.modal-header{display:flex;justify-content:space-between;align-items:center;padding:16px 20px;border-bottom:1px solid var(--border-card);font-weight:600;font-size:15px;color:var(--text-primary)}
.modal-header button{background:none;border:none;font-size:18px;cursor:pointer;color:var(--text-muted)}
.modal-body{padding:20px}.form-row{display:flex;align-items:flex-start;margin-bottom:14px}
.form-row label{width:100px;font-size:13px;color:var(--text-muted);flex-shrink:0;padding-top:8px}
.form-row input,.form-row select,.form-row textarea{flex:1;padding:8px 12px;border:1px solid var(--border-input);border-radius:6px;font-size:13px;background:var(--bg-input);color:var(--text-primary)}
.form-row textarea{resize:vertical}
.modal-footer{display:flex;justify-content:flex-end;gap:10px;padding:14px 20px;border-top:1px solid var(--border-card)}
.btn-cancel{padding:8px 18px;border:1px solid var(--border-input);border-radius:6px;background:var(--bg-input);color:var(--text-primary);cursor:pointer}
.btn-confirm{padding:8px 18px;background:#3b82f6;color:#fff;border:none;border-radius:6px;cursor:pointer;font-weight:600}
.detail-grid{display:grid;grid-template-columns:1fr 1fr;gap:10px}.detail-row{display:flex;flex-direction:column;padding:8px;background:var(--bg-thead);border-radius:6px}.detail-label{font-size:11px;color:var(--text-muted);margin-bottom:2px}.detail-val{font-size:13px;color:var(--text-primary);word-break:break-all}
</style>
