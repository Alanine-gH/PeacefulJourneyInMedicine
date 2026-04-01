<template>
  <div class="page-wrap">
    <div class="page-header">
      <h2>用户列表</h2>
      <span class="page-desc">管理所有注册用户（user_user 多表联查）</span>
    </div>
    <div class="toolbar">
      <div class="filters">
        <input v-model="query.username" class="filter-input" placeholder="用户名/昵称" @keyup.enter="loadData">
        <input v-model="query.phone" class="filter-input" placeholder="手机号" @keyup.enter="loadData">
        <select v-model="query.userType" class="filter-select">
          <option value="">全部类型</option>
          <option value="1">患者</option>
          <option value="2">陪诊师</option>
          <option value="3">客服</option>
          <option value="4">管理员</option>
          <option value="5">专家</option>
        </select>
        <select v-model="query.status" class="filter-select">
          <option value="">全部状态</option>
          <option value="1">活跃</option>
          <option value="2">停用</option>
          <option value="3">禁用</option>
        </select>
        <button class="btn-search" @click="loadData">🔍 查询</button>
        <button class="btn-reset" @click="resetQuery">↺ 重置</button>
      </div>
      <button class="btn-add" @click="openAdd">+ 新增用户</button>
    </div>
    <div class="table-card">
      <table class="data-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>昵称</th>
          <th>手机号</th>
          <th>用户类型</th>
          <th>状态</th>
          <th>认证状态</th>
          <th>年龄/性别</th>
          <th>最后登录</th>
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
          <td>
            <div class="user-cell"><img v-if="row.avatarUrl" :src="getFileUrl(row.avatarUrl)"
                                        class="avatar-sm avatar-img" alt="">
              <div v-else class="avatar-sm">{{ (row.nickName || row.username || '?')[0] }}</div>
              {{ row.username }}
            </div>
          </td>
          <td>{{ row.nickName || '-' }}</td>
          <td>{{ row.phone || '-' }}</td>
          <td><span class="tag" :class="typeClass(row.userType)">{{ typeLabel(row.userType) }}</span></td>
          <td><span class="tag" :class="statusClass(row.status)">{{ statusLabel(row.status) }}</span></td>
          <td><span class="tag" :class="authClass(row.authStatus)">{{ authLabel(row.authStatus) }}</span></td>
          <td><span v-if="row.age">{{ row.age }}岁</span><span v-if="row.gender" style="margin-left:4px">{{
              {
                '1': '男',
                '2': '女',
                1: '男',
                2: '女'
              }[row.gender] || '-'
            }}</span><span v-if="!row.age && !row.gender">-</span></td>
          <td>{{ row.lastLoginTime ? fmtTime(row.lastLoginTime) : '-' }}</td>
          <td>
            <div class="action-btns">
              <button class="btn-sm btn-view" @click="openDetail(row)">详情</button>
              <button class="btn-sm btn-edit" @click="openEdit(row)">编辑</button>
              <button class="btn-sm" :class="row.status===1?'btn-stop':'btn-start'" @click="toggleStatus(row)">
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
      <div class="modal">
        <div class="modal-header"><span>{{ modal.mode === 'add' ? '新增用户' : '编辑用户' }}</span>
          <button class="modal-close" @click="modal.show=false">✕</button>
        </div>
        <div class="modal-body">
          <div class="form-grid">
            <div class="form-item full form-tip">带 * 为必填（不能为空）；未标注 * 的字段可留空，后端允许为空。</div>
            <div class="form-item"><label>用户名 <em>*</em></label>
              <input v-model="form.username" :disabled="modal.mode==='edit'" class="form-input" placeholder="4-20位">
            </div>
            <div class="form-item" v-if="modal.mode==='add'"><label>密码 <em>*</em></label>
              <input v-model="form.password" type="password" class="form-input" placeholder="至少8位">
            </div>
            <div class="form-item"><label>昵称（可留空）</label>
              <input v-model="form.nickName" class="form-input" placeholder="昵称">
            </div>
            <div class="form-item"><label>真实姓名（可留空）</label>
              <input v-model="form.realName" class="form-input" placeholder="真实姓名">
            </div>
            <div class="form-item"><label>手机号（可留空）</label>
              <input v-model="form.phone" class="form-input" placeholder="手机号">
            </div>
            <div class="form-item"><label>邮箱（可留空）</label>
              <input v-model="form.email" class="form-input" placeholder="邮箱">
            </div>
            <div class="form-item"><label>用户类型</label>
              <select v-model="form.userType" class="form-select">
                <option :value="1">患者</option>
                <option :value="3">客服</option>
              </select>
            </div>
            <div class="form-item"><label>状态</label>
              <select v-model="form.status" class="form-select">
                <option :value="1">活跃</option>
                <option :value="2">停用</option>
                <option :value="3">禁用</option>
              </select>
            </div>
            <div class="form-item"><label>年龄</label>
              <input v-model.number="form.age" type="number" min="0" max="150" class="form-input" placeholder="年龄">
            </div>
            <div class="form-item"><label>性别</label>
              <select v-model.number="form.gender" class="form-select">
                <option :value="0">未知</option>
                <option :value="1">男</option>
                <option :value="2">女</option>
              </select>
            </div>
            <div class="form-item full">
              <label>头像</label>
              <div class="upload-row">
                <img v-if="form.avatarUrl" :src="getFileUrl(form.avatarUrl)" class="preview-img" alt="头像">
                <label class="upload-btn">
                  {{ uploadingField === 'avatarUrl' ? '上传中...' : (form.avatarUrl ? '重新上传' : '点击上传') }}
                  <input type="file" accept="image/*" style="display:none" @change="e => handleUpload(e, 'avatarUrl')"
                         :disabled="!!uploadingField">
                </label>
                <button v-if="form.avatarUrl" class="upload-clear" @click="form.avatarUrl=''" type="button">✕ 移除
                </button>
              </div>
            </div>
            <div class="form-item full"><label>备注</label>
              <textarea v-model="form.remark" class="form-input" rows="2" placeholder="备注"></textarea>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="modal.show=false">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="submitting">
            {{ submitting ? '提交中...' : '确认' }}
          </button>
        </div>
      </div>
    </div>
    <!-- 详情弹窗 -->
    <div class="modal-mask" v-if="detailModal.show" @click.self="detailModal.show=false">
      <div class="modal modal-lg">
        <div class="modal-header"><span>用户详情</span>
          <button class="modal-close" @click="detailModal.show=false">✕</button>
        </div>
        <div class="modal-body" v-if="detailModal.data">
          <div class="detail-section">
            <div class="section-title">基本信息</div>
            <div class="detail-grid">
              <div class="detail-item"><span class="dk">用户名</span>
                <span class="dv">{{ detailModal.data.username }}</span>
              </div>
              <div class="detail-item"><span class="dk">真实姓名</span>
                <span class="dv">{{ detailModal.data.realName || '-' }}</span>
              </div>
              <div class="detail-item"><span class="dk">手机号</span>
                <span class="dv">{{ detailModal.data.phone || '-' }}</span>
              </div>
              <div class="detail-item"><span class="dk">邮箱</span>
                <span class="dv">{{ detailModal.data.email || '-' }}</span>
              </div>
              <div class="detail-item"><span class="dk">用户类型</span>
                <span class="dv">{{ typeLabel(detailModal.data.userType) }}</span>
              </div>
              <div class="detail-item"><span class="dk">状态</span>
                <span class="dv">{{ statusLabel(detailModal.data.status) }}</span>
              </div>
              <div class="detail-item"><span class="dk">年龄</span>
                <span class="dv">{{ detailModal.data.age || '-' }}</span>
              </div>
              <div class="detail-item"><span class="dk">性别</span>
                <span class="dv">{{ {0: '未知', 1: '男', 2: '女'}[detailModal.data.gender] || '-' }}</span>
              </div>
              <div class="detail-item"><span class="dk">注册时间</span><span
                  class="dv">{{ fmtTime(detailModal.data.createTime) }}</span></div>
              <div class="detail-item"><span class="dk">最后登录IP</span><span
                  class="dv">{{ detailModal.data.lastLoginIp || '-' }}</span></div>
            </div>
          </div>
          <div class="detail-section" v-if="detailModal.data.roles">
            <div class="section-title">角色信息</div>
            <div class="role-tags">
              <span class="role-tag" v-for="r in detailModal.data.roles" :key="r.id">{{ r.roleName }}</span>
              <span v-if="!detailModal.data.roles||!detailModal.data.roles.length" class="text-muted">暂无角色</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="detailModal.show=false">关闭</button>
          <button class="btn-confirm" @click="resetPwd(detailModal.data)">重置密码</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {
  getUserList,
  getUserDetail,
  addUser,
  updateUser,
  deleteUser,
  updateUserStatus,
  resetPassword
} from '@/api/user.js'

export default {
  name: 'UserList',
  data() {
    return {
      loading: false, list: [], total: 0,
      query: {username: '', phone: '', userType: '', status: '', pageNum: 1, pageSize: 10},
      modal: {show: false, mode: 'add'},
      detailModal: {show: false, data: null},
      form: {
        username: '',
        password: '',
        nickName: '',
        realName: '',
        phone: '',
        email: '',
        userType: 1,
        status: 1,
        remark: '',
        avatarUrl: ''
      },
      editId: null, submitting: false, uploadingField: null, age: '', gender: 0
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
    async handleUpload(e, field) {
      const file = e.target.files[0];
      if (!file) return;
      this.uploadingField = field;
      try {
        const fd = new FormData();
        fd.append('file', file);
        const token = localStorage.getItem('token') || '';
        const res = await fetch('http://localhost:8080/common/upload', {
          method: 'POST',
          headers: token ? {'Authorization': 'Bearer ' + token} : {},
          body: fd
        });
        const text = await res.text();
        let json;
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
        const res = await getUserList(this.query)
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
      this.query = {username: '', phone: '', userType: '', status: '', pageNum: 1, pageSize: 10};
      this.loadData()
    },
    openAdd() {
      this.modal.mode = 'add'
      this.form = {
        username: '',
        password: '',
        nickName: '',
        realName: '',
        phone: '',
        email: '',
        userType: 1,
        status: 1,
        remark: '',
        avatarUrl: '',
        age: '',
        gender: 0
      }
      this.editId = null;
      this.modal.show = true
    },
    openEdit(row) {
      this.modal.mode = 'edit';
      this.editId = row.id
      this.form = {
        nickName: row.nickName || '',
        realName: row.realName || '',
        phone: row.phone || '',
        email: row.email || '',
        userType: Number(row.userType) || 1,
        status: Number(row.status) || 1,
        remark: row.remark || '',
        avatarUrl: row.avatarUrl || '',
        age: row.age || '',
        gender: row.gender !== undefined && row.gender !== null ? Number(row.gender) : 0
      }
      this.modal.show = true
    },
    async openDetail(row) {
      try {
        const res = await getUserDetail(row.id)
        this.detailModal.data = (res && res.data) ? res.data : res
        this.detailModal.show = true
      } catch (e) {
        alert('获取详情失败')
      }
    },
    async handleSubmit() {
      if (this.modal.mode === 'add' && !this.form.username) {
        alert('用户名不能为空');
        return
      }
      if (this.modal.mode === 'add' && (!this.form.password || this.form.password.length < 8)) {
        alert('密码不能为空且至少8位');
        return
      }
      this.submitting = true
      try {
        if (this.modal.mode === 'add') {
          await addUser(this.form)
        } else {
          await updateUser(this.editId, this.form)
        }
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
    async toggleStatus(row) {
      const ns = row.status === 1 ? 2 : 1
      if (!confirm('确认' + (ns === 2 ? '停用' : '启用') + '用户 ' + row.username + '？')) return
      try {
        await updateUserStatus(row.id, ns);
        this.loadData()
      } catch (e) {
        alert('操作失败')
      }
    },
    async handleDelete(row) {
      if (!confirm('确认删除用户 ' + row.username + '？')) return
      try {
        await deleteUser(row.id);
        this.loadData()
      } catch (e) {
        alert('删除失败')
      }
    },
    async resetPwd(row) {
      const p = prompt('重置 ' + row.username + ' 的密码：')
      if (!p || p.length < 8) {
        alert('密码不能为空且至少8位');
        return
      }
      try {
        await resetPassword(row.id, p);
        alert('密码重置成功')
      } catch (e) {
        alert('重置失败')
      }
    },
    typeLabel(t) {
      return {1: '患者', 2: '陪诊师', 3: '客服', 4: '管理员', 5: '专家'}[t] || '-'
    },
    typeClass(t) {
      return {1: 'tag-blue', 2: 'tag-green', 3: 'tag-yellow', 4: 'tag-red', 5: 'tag-green'}[t] || ''
    },
    statusLabel(s) {
      return {1: '活跃', 2: '停用', 3: '禁用'}[s] || '-'
    },
    statusClass(s) {
      return {1: 'tag-green', 2: 'tag-yellow', 3: 'tag-red'}[s] || ''
    },
    authLabel(s) {
      return ({1: '待审核', 2: '已通过', 3: '已拒绝'})[s] || '未认证'
    },
    authClass(s) {
      return ({1: 'tag-yellow', 2: 'tag-green', 3: 'tag-red'})[s] || 'tag-gray'
    },
    fmtTime(t) {
      return t ? new Date(t).toLocaleString('zh-CN') : '-'
    }
  }
}
</script>

<style>
@import '@/assets/user.css';
</style>