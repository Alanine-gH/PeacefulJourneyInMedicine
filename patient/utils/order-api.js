/**
 * 订单相关API接口
 * 提供订单创建、管理、查询等功能
 */

import { request } from './config.js';

/**
 * 订单管理接口
 */

/**
 * 创建订单
 * @param {object} data - 订单信息
 * @param {string} data.serviceType - 服务类型
 * @param {string} data.hospital - 就诊医院
 * @param {string} data.visitTime - 就诊时间
 * @param {string} data.patient - 就诊人
 * @param {string} data.doctor - 就诊医生
 * @param {string} data.phone - 联系电话
 * @param {string} data.demand - 服务需求
 * @returns {Promise<object>} 返回创建结果
 * @description 创建新的患者订单，状态为预约中
 */
async function createOrder(data) {
  return request('/order/create', {
    method: 'POST',
    data
  });
}

/**
 * 获取我的订单列表
 * @param {object} params - 查询参数
 * @param {string} params.status - 订单状态筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回订单列表
 * @description 获取当前用户的所有订单列表
 */
async function getMyOrders(params) {
  return request('/order/my', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取订单详情
 * @param {string} orderNo - 订单编号
 * @returns {Promise<object>} 返回订单详细信息
 * @description 获取指定订单的详细信息
 */
async function getOrderDetail(orderNo) {
  return request(`/order/detail/${orderNo}`);
}

/**
 * 取消订单
 * @param {string} orderNo - 订单编号
 * @param {object} data - 取消原因
 * @param {string} data.reason - 取消原因
 * @returns {Promise<object>} 返回取消结果
 * @description 取消已创建的订单
 */
async function cancelOrder(orderNo, data) {
  return request(`/order/cancel/${orderNo}`, {
    method: 'POST',
    data
  });
}

/**
 * 更新订单状态
 * @param {string} orderNo - 订单编号
 * @param {object} data - 状态信息
 * @param {number} data.status - 订单状态
 * @returns {Promise<object>} 返回更新结果
 * @description 更新订单状态
 */
async function updateOrderStatus(orderNo, data) {
  return request(`/order/update-status/${orderNo}`, {
    method: 'PUT',
    data
  });
}

/**
 * 获取订单统计
 * @returns {Promise<object>} 返回统计数据
 * @description 获取用户订单的统计数据
 */
async function getOrderStatistics() {
  return request('/order/statistics');
}

export {
  createOrder,
  getMyOrders,
  getOrderDetail,
  cancelOrder,
  updateOrderStatus,
  getOrderStatistics
};
