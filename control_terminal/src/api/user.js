import request from './auth'

// 过滤空字符串参数，避免 Byte/Integer 类型转换异常
function cleanParams(params) {
    const result = {}
    for (const key in params) {
        const v = params[key]
        if (v !== '' && v !== null && v !== undefined) result[key] = v
    }
    return result
}

/** 上传文件到 MinIO，返回文件名 */
export function uploadFile(file) {
    const fd = new FormData()
    fd.append('file', file)
    return request({url: '/common/upload', method: 'post', data: fd, headers: {'Content-Type': 'multipart/form-data'}})
}

/** 根据 MinIO 文件名拼接预览地址 */
export function getFileUrl(filename) {
    if (!filename) return ''
    if (filename.startsWith('http')) return filename
    return 'http://localhost:8080/common/download?name=' + encodeURIComponent(filename)
}

// ==================== 用户管理 user_user ====================

/** 查询用户总数 */
export function getUserCount() {
    return request({url: '/user/stat/count', method: 'get'})
}

/** 分页查询用户列表（多表联查：含角色、认证状态） */
export function getUserList(params) {
    return request({url: '/user/list', method: 'get', params: cleanParams(params)})
}

/** 获取用户详情（含角色、认证、陪诊师信息） */
export function getUserDetail(id) {
    return request({url: `/user/${id}`, method: 'get'})
}

/** 新增用户 */
export function addUser(data) {
    return request({url: '/user', method: 'post', data})
}

/** 修改用户 */
export function updateUser(id, data) {
    return request({url: `/user/${id}`, method: 'put', data})
}

/** 删除用户 */
export function deleteUser(id) {
    return request({url: `/user/${id}`, method: 'delete'})
}

/** 修改用户状态 */
export function updateUserStatus(id, status) {
    return request({url: `/user/${id}/status`, method: 'put', data: {status}})
}

/** 重置密码 */
export function resetPassword(id, newPassword) {
    return request({url: `/user/${id}/password`, method: 'put', data: {newPassword}})
}

/** 获取用户登录日志 */
export function getUserLoginLog(userId, params) {
    return request({url: `/user/${userId}/login-log`, method: 'get', params})
}

// ==================== 陪诊师管理 user_accompanist ====================

/** 查询陪诊师总数 */
export function getAccompanistCount() {
    return request({url: '/user/accompanist/stat/count', method: 'get'})
}

/** 分页查询陪诊师列表（多表联查 user_user + user_accompanist） */
export function getAccompanistList(params) {
    return request({url: '/user/accompanist/list', method: 'get', params: cleanParams(params)})
}

/** 获取陪诊师详情 */
export function getAccompanistDetail(id) {
    return request({url: `/user/accompanist/${id}`, method: 'get'})
}

/** 新增陪诊师资质 */
export function addAccompanist(data) {
    return request({url: '/user/accompanist', method: 'post', data})
}

/** 修改陪诊师信息 */
export function updateAccompanist(id, data) {
    return request({url: `/user/accompanist/${id}`, method: 'put', data})
}

/** 删除陪诊师 */
export function deleteAccompanist(id) {
    return request({url: `/user/accompanist/${id}`, method: 'delete'})
}

/** 审核陪诊师（通过/拒绝） */
export function auditAccompanist(id, data) {
    return request({url: `/user/accompanist/${id}/audit`, method: 'put', data})
}

/** 修改陪诊师状态 */
export function updateAccompanistStatus(id, status) {
    return request({url: `/user/accompanist/${id}/status`, method: 'put', data: {status}})
}

// ==================== 实名认证 user_identity_auth ====================

/** 分页查询实名认证列表（多表联查 user_identity_auth + user_user） */
export function getIdentityList(params) {
    return request({url: '/user/identity/list', method: 'get', params: cleanParams(params)})
}

/** 获取认证详情 */
export function getIdentityDetail(id) {
    return request({url: `/user/identity/${id}`, method: 'get'})
}

/** 审核实名认证（通过/拒绝） */
export function auditIdentity(id, data) {
    return request({url: `/user/identity/${id}/audit`, method: 'put', data})
}

/** 删除认证记录 */
export function deleteIdentity(id) {
    return request({url: `/user/identity/${id}`, method: 'delete'})
}

