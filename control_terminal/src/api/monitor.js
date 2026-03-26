import request from './auth'

/**
 * 系统监控相关 API
 */

// ==================== 在线用户 ====================

/**
 * 获取在线用户列表
 * @param {string} ipaddr   可选，按IP过滤
 * @param {string} userName 可选，按用户名过滤
 */
export function getOnlineUsers(ipaddr, userName) {
    return request({
        url: '/monitor/online/list',
        method: 'get',
        params: {ipaddr, userName}
    })
}

/**
 * 强制退出指定用户
 * @param {string} tokenId 用户 tokenId（userId）
 */
export function forceLogout(tokenId) {
    return request({
        url: `/monitor/online/${tokenId}`,
        method: 'delete'
    })
}

// ==================== 缓存监控 ====================

/**
 * 获取 Redis 基本信息和命令统计
 */
export function getCacheInfo() {
    return request({
        url: '/monitor/cache',
        method: 'get'
    })
}

/**
 * 获取项目已知缓存前缀列表
 */
export function getCacheNames() {
    return request({
        url: '/monitor/cache/names',
        method: 'get'
    })
}

/**
 * 获取指定前缀的所有 Key
 * @param {string} cacheName key 前缀
 */
export function getCacheKeys(cacheName) {
    return request({
        url: `/monitor/cache/keys/${cacheName}`,
        method: 'get'
    })
}

/**
 * 清除指定前缀的所有缓存
 * @param {string} cacheName key 前缀
 */
export function clearCacheByName(cacheName) {
    return request({
        url: `/monitor/cache/clear/${cacheName}`,
        method: 'delete'
    })
}

/**
 * 清除单个缓存 Key
 * @param {string} cacheKey 完整 key
 */
export function clearCacheByKey(cacheKey) {
    return request({
        url: `/monitor/cache/key/${cacheKey}`,
        method: 'delete'
    })
}

// ==================== 服务监控 ====================

/**
 * 获取服务器/JVM/磁盘信息
 */
export function getServerInfo() {
    return request({
        url: '/monitor/server',
        method: 'get'
    })
}
