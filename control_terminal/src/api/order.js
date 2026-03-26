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

// ==================== 订单管理 order_order ====================

/** 查询订单总数 */
export function getOrderCount() {
    return request({url: '/order/stat/count', method: 'get'})
}

/** 分页查询订单列表（多表联查） */
export function getOrderList(params) {
    return request({url: '/order/list', method: 'get', params: cleanParams(params)})
}

/** 获取订单详情（含服务项目） */
export function getOrderDetail(id) {
    return request({url: `/order/${id}`, method: 'get'})
}

/** 新增订单 */
export function addOrder(data) {
    return request({url: '/order', method: 'post', data})
}

/** 修改订单 */
export function updateOrder(id, data) {
    return request({url: `/order/${id}`, method: 'put', data})
}

/** 删除订单 */
export function deleteOrder(id) {
    return request({url: `/order/${id}`, method: 'delete'})
}

/** 修改订单状态 */
export function updateOrderStatus(id, orderStatus, reason) {
    return request({url: `/order/${id}/status`, method: 'put', data: {orderStatus, reason}})
}

// ==================== 支付记录 order_payment ====================

/** 分页查询支付记录 */
export function getPaymentList(params) {
    return request({url: '/order/payment/list', method: 'get', params: cleanParams(params)})
}

/** 获取支付详情 */
export function getPaymentDetail(id) {
    return request({url: `/order/payment/${id}`, method: 'get'})
}

/** 删除支付记录 */
export function deletePayment(id) {
    return request({url: `/order/payment/${id}`, method: 'delete'})
}

// ==================== 订单评价 order_evaluation ====================

/** 分页查询评价列表 */
export function getEvaluationList(params) {
    return request({url: '/order/evaluation/list', method: 'get', params: cleanParams(params)})
}

/** 获取评价详情 */
export function getEvaluationDetail(id) {
    return request({url: `/order/evaluation/${id}`, method: 'get'})
}

/** 删除评价 */
export function deleteEvaluation(id) {
    return request({url: `/order/evaluation/${id}`, method: 'delete'})
}
