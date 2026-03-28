/**
 * 发布需求相关API接口
 * 提供需求发布、管理、查询等功能
 */

import { request } from './config.js';



/**
 * 14.1 需求发布管理
 */

/**
 * 发布需求
 * @param {object} data - 需求信息
 * @param {string} data.title - 需求标题
 * @param {string} data.description - 需求描述
 * @param {string} data.type - 需求类型 (accompany: 陪护, nursing: 护理, other: 其他)
 * @param {string} data.hospital_id - 医院ID
 * @param {string} data.department_id - 科室ID
 * @param {string} data.patient_id - 就诊人ID
 * @param {string} data.start_date - 开始日期
 * @param {string} data.end_date - 结束日期
 * @param {number} data.budget - 预算金额
 * @param {string} data.contact_name - 联系人姓名
 * @param {string} data.contact_phone - 联系人电话
 * @param {string} data.requirements - 特殊要求
 * @returns {Promise<object>} 返回发布结果
 * @description 发布新的陪护需求
 */
async function publishDemand(data) {
  return request('/demand/publish', {
    method: 'POST',
    data
  });
}

/**
 * 获取我的需求列表
 * @param {object} params - 查询参数
 * @param {string} params.status - 需求状态筛选 (pending: 待接单, accepted: 已接单, completed: 已完成, cancelled: 已取消)
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回需求列表
 * @description 获取当前用户发布的所有需求列表
 */
async function getMyDemands(params) {
  return request('/demand/my', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取需求详情
 * @param {string} demandId - 需求ID
 * @returns {Promise<object>} 返回需求详细信息
 * @description 获取指定需求的详细信息
 */
async function getDemandDetail(demandId) {
  return request(`/demand/detail/${demandId}`);
}

/**
 * 编辑需求
 * @param {string} demandId - 需求ID
 * @param {object} data - 需求信息
 * @returns {Promise<object>} 返回编辑结果
 * @description 编辑未接单的需求信息
 */
async function editDemand(demandId, data) {
  return request(`/demand/edit/${demandId}`, {
    method: 'PUT',
    data
  });
}

/**
 * 取消需求
 * @param {string} demandId - 需求ID
 * @param {object} data - 取消原因
 * @param {string} data.reason - 取消原因
 * @returns {Promise<object>} 返回取消结果
 * @description 取消已发布但未接单的需求
 */
async function cancelDemand(demandId, data) {
  return request(`/demand/cancel/${demandId}`, {
    method: 'POST',
    data
  });
}

/**
 * 删除需求
 * @param {string} demandId - 需求ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除已取消或已完成的需求
 */
async function deleteDemand(demandId) {
  return request(`/demand/delete/${demandId}`, {
    method: 'DELETE'
  });
}

/**
 * 获取需求列表（陪护端）
 * @param {object} params - 查询参数
 * @param {string} params.city - 城市
 * @param {string} params.type - 需求类型
 * @param {number} params.min_budget - 最低预算
 * @param {number} params.max_budget - 最高预算
 * @param {string} params.start_date - 开始日期
 * @param {string} params.end_date - 结束日期
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回需求列表
 * @description 陪护端获取可接单的需求列表
 */
async function getDemandListForCompanion(params) {
  return request('/demand/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 接单
 * @param {string} demandId - 需求ID
 * @returns {Promise<object>} 返回接单结果
 * @description 陪护员接单
 */
async function acceptDemand(demandId) {
  return request(`/demand/accept/${demandId}`, {
    method: 'POST'
  });
}

/**
 * 拒绝接单
 * @param {string} demandId - 需求ID
 * @param {object} data - 拒绝原因
 * @param {string} data.reason - 拒绝原因
 * @returns {Promise<object>} 返回拒绝结果
 * @description 陪护员拒绝接单
 */
async function rejectDemand(demandId, data) {
  return request(`/demand/reject/${demandId}`, {
    method: 'POST',
    data
  });
}

/**
 * 获取需求匹配推荐
 * @param {object} params - 查询参数
 * @param {number} params.limit - 数量限制
 * @returns {Promise<object>} 返回推荐列表
 * @description 根据用户历史获取推荐的需求
 */
async function getRecommendedDemands(params) {
  return request('/demand/recommended', {
    method: 'GET',
    data: params
  });
}

/**
 * 刷新需求
 * @param {string} demandId - 需求ID
 * @returns {Promise<object>} 返回刷新结果
 * @description 刷新需求排序，使其显示在前面
 */
async function refreshDemand(demandId) {
  return request(`/demand/refresh/${demandId}`, {
    method: 'POST'
  });
}

/**
 * 获取需求统计
 * @returns {Promise<object>} 返回统计数据
 * @description 获取用户发布需求的统计数据
 */
async function getDemandStatistics() {
  return request('/demand/statistics');
}

export {
  publishDemand,
  getMyDemands,
  getDemandDetail,
  editDemand,
  cancelDemand,
  deleteDemand,
  getDemandListForCompanion,
  acceptDemand,
  rejectDemand,
  getRecommendedDemands,
  refreshDemand,
  getDemandStatistics
};
