import { BASE_URL } from '@/config'

/**
 * 过滤空字符串参数，避免 Byte/Integer 类型转换异常
 * @param {Object} params - 原始参数对象
 * @returns {Object} 过滤后的参数对象
 */
export function cleanParams(params) {
    const result = {}
    for (const key in params) {
        const v = params[key]
        if (v !== '' && v !== null && v !== undefined) result[key] = v
    }
    return result
}

/**
 * 根据 MinIO 文件名拼接预览地址
 * @param {string} filename - 文件名
 * @returns {string} 完整的文件URL
 */
export function getFileUrl(filename) {
    if (!filename) return ''
    if (filename.startsWith('http')) return filename
    return `${BASE_URL}/common/download?name=${encodeURIComponent(filename)}`
}

/**
 * 上传文件到 MinIO
 * @param {File} file - 文件对象
 * @param {Function} request - axios请求实例
 * @returns {Promise} 上传结果
 */
export function uploadFile(file, request) {
    const fd = new FormData()
    fd.append('file', file)
    return request({
        url: '/common/upload',
        method: 'post',
        data: fd,
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}

/**
 * 获取请求头（包含认证信息）
 * @returns {Object} 请求头对象
 */
export function getAuthHeaders() {
    const headers = {
        'Content-Type': 'application/json'
    }
    const token = localStorage.getItem('token')
    if (token) {
        headers['Authorization'] = 'Bearer ' + token
    }
    return headers
}

/**
 * 封装fetch请求（用于简单场景）
 * @param {string} url - 请求地址（不含baseURL）
 * @param {Object} options - 请求选项
 * @returns {Promise} 请求结果
 */
export async function apiRequest(url, options = {}) {
    const response = await fetch(BASE_URL + url, {
        headers: {
            'Content-Type': 'application/json',
            ...getAuthHeaders()
        },
        ...options
    })
    return response.json()
}
