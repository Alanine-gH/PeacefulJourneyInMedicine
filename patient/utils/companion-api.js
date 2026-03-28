/**
 * 陪护端API接口
 * 提供陪护员相关的所有功能接口，包括个人中心、订单管理、资格认证、日程管理、打卡管理和评价管理
 */

import { request } from './config.js';



/**
 * 1.1 陪护端个人中心
 */

/**
 * 获取陪护员信息
 * @param {string} companionId - 陪护员ID
 * @returns {Promise<object>} 返回陪护员详细信息
 * @description 获取当前登录陪护员的个人信息，包括基本资料、认证状态、评分等
 */
async function getCompanionProfile(companionId) {
  // companionId 是 user_accompanist.id，不是 user_user.id
  // 优先用已缓存的 accompanistId，否则按 userId 反查
  const id = companionId || uni.getStorageSync('accompanistId')
  if (id) {
    return request(`/user/accompanist/${id}`);
  }
  // 按 userId 查
  const userId = uni.getStorageSync('userId')
  return request(`/user/accompanist/by-user/${userId}`);
}

/**
 * 更新陪护员信息
 * @param {string} companionId - 陪护员ID
 * @param {object} data - 更新的信息
 * @param {string} data.nickname - 昵称
 * @param {string} data.avatar - 头像URL
 * @param {string} data.introduction - 个人简介
 * @returns {Promise<object>} 返回更新结果
 * @description 更新陪护员的个人资料信息
 */
async function updateCompanionProfile(companionId, data) {
  return request(`/user/accompanist/${companionId}`, {
    method: 'PUT',
    data
  });
}

/**
 * 获取工作信息
 * @returns {Promise<object>} 返回工作信息
 * @description 获取陪护员的工作统计信息，包括完成订单数、收入、评分等
 */
async function getCompanionWorkInfo() {
  const accompanistId = uni.getStorageSync('accompanistId') || '';
  if (!accompanistId) {
    return Promise.reject(new Error('accompanistId 未初始化，请先进入「我的」页面'));
  }
  return request('/companion/work-info', {
    method: 'GET',
    data: { accompanistId }
  });
}

/**
 * 1.2 订单管理
 */

/**
 * 获取待接订单列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回待接订单列表
 * @description 获取所有待接单的订单列表，供陪护员选择接单
 * 数据来源：order_order表，筛选条件为order_status=2（已付款）且accompanist_id为空
 */
async function getAvailableOrders(params) {
  return request('/companion/orders/available', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取订单列表
 * @param {object} params - 查询参数
 * @param {string} params.status - 订单状态筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回订单列表
 * @description 获取陪护员的订单列表，支持按状态筛选和分页
 */
async function getCompanionOrders(params) {
  const accompanistId = uni.getStorageSync('accompanistId') || '';
  if (!accompanistId) {
    return Promise.reject(new Error('accompanistId 未初始化'));
  }
  return request('/companion/orders', {
    method: 'GET',
    data: { accompanistId, ...params }
  });
}

/**
 * 获取订单详情
 * @param {string} orderNo - 订单编号
 * @returns {Promise<object>} 返回订单详细信息
 * @description 获取指定订单的详细信息，包括患者信息、服务内容、费用等
 */
async function getCompanionOrderDetail(orderNo) {
  return request(`/companion/orders/${orderNo}`);
}

/**
 * 接单
 * @param {string} orderNo - 订单编号
 * @returns {Promise<object>} 返回接单结果
 * @description 陪护员接受订单，订单状态会变更为已接单
 * 数据操作：更新order_order表的accompanist_id字段为当前陪护员ID，order_status更新为3（已确认）
 */
async function acceptOrder(orderNo) {
  const accompanistId = uni.getStorageSync('accompanistId') || '';
  return request(`/companion/orders/accept?accompanistId=${accompanistId}`, {
    method: 'POST',
    data: { order_no: orderNo }
  });
}

/**
 * 1.3 资格认证
 */

/**
 * 提交资格认证
 * @param {object} data - 认证信息
 * @param {string} data.real_name - 真实姓名
 * @param {string} data.phone - 手机号
 * @param {number} data.gender - 性别 (0: 女, 1: 男)
 * @param {number} data.age - 年龄
 * @param {string} data.professional_title - 职称
 * @param {number} data.medical_background - 医疗背景 (0: 无, 1: 有)
 * @param {string} data.certificate_number - 证书编号
 * @param {string} data.certificate_photo - 证书照片URL
 * @param {string} data.criminal_record_cert - 无犯罪记录证明URL
 * @param {string} data.business_license - 营业执照URL
 * @param {string} data.language_ability - 语言能力
 * @param {string} data.specialties - 专长
 * @returns {Promise<object>} 返回提交结果
 * @description 提交陪护员资格认证申请，需要上传相关证件照片
 */
async function submitCertification(data) {
  const accompanistId = data.accompanistId;
  // 移除 accompanistId，因为它作为 URL 参数传递
  const { accompanistId: _, ...certData } = data;
  
  return request(`/companion/certification?accompanistId=${accompanistId}`, {
    method: 'POST',
    data: certData
  });
}

/**
 * 查询认证状态
 * @returns {Promise<object>} 返回认证状态信息
 * @description 查询当前陪护员的认证审核状态，包括是否通过、审核意见等
 */
async function getCertificationStatus() {
  return request('/companion/certification/status');
}

/**
 * 1.4 日程管理
 */

/**
 * 获取日程列表
 * @param {object} params - 查询参数
 * @param {string} params.date - 日期筛选
 * @param {string} params.status - 日程状态
 * @returns {Promise<object>} 返回日程列表
 * @description 获取陪护员的工作日程安排
 */
async function getCompanionSchedule(params) {
  return request('/companion/schedule', {
    method: 'GET',
    data: params
  });
}

/**
 * 更新日程状态
 * @param {string} id - 日程ID
 * @param {object} data - 更新数据
 * @param {string} data.status - 日程状态
 * @param {string} data.remark - 备注
 * @returns {Promise<object>} 返回更新结果
 * @description 更新日程的状态信息
 */
async function updateCompanionSchedule(id, data) {
  return request(`/companion/schedule/${id}`, {
    method: 'PUT',
    data
  });
}

/**
 * 1.5 打卡管理
 */

/**
 * 打卡
 * @param {object} data - 打卡信息
 * @param {string} data.order_id - 订单ID
 * @param {number} data.latitude - 纬度
 * @param {number} data.longitude - 经度
 * @param {string} data.address - 地址
 * @param {string} data.photo - 打卡照片
 * @returns {Promise<object>} 返回打卡结果
 * @description 陪护员进行打卡签到，记录位置和照片
 */
async function companionCheckin(data) {
  return request('/companion/checkin', {
    method: 'POST',
    data
  });
}

/**
 * 获取打卡记录
 * @param {string} orderId - 订单ID
 * @returns {Promise<object>} 返回打卡记录列表
 * @description 获取指定订单的打卡记录
 */
async function getCompanionCheckinRecords(orderId) {
  return request(`/companion/checkin/${orderId}`);
}

/**
 * 1.6 评价管理
 */

/**
 * 获取评价列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回评价列表
 * @description 获取陪护员收到的评价列表
 */
async function getCompanionEvaluations(params) {
  return request('/companion/evaluations', {
    method: 'GET',
    data: params
  });
}

/**
 * 回复评价
 * @param {string} id - 评价ID
 * @param {string} reply - 回复内容
 * @returns {Promise<object>} 返回回复结果
 * @description 陪护员回复患者的评价
 */
async function replyCompanionEvaluation(id, reply) {
  return request(`/companion/evaluations/${id}/reply`, {
    method: 'POST',
    data: { evaluation_reply: reply }
  });
}

export {
  request,
  getCompanionProfile,
  updateCompanionProfile,
  getCompanionWorkInfo,
  getAvailableOrders,
  getCompanionOrders,
  getCompanionOrderDetail,
  acceptOrder,
  submitCertification,
  getCertificationStatus,
  getCompanionSchedule,
  updateCompanionSchedule,
  companionCheckin,
  getCompanionCheckinRecords,
  getCompanionEvaluations,
  replyCompanionEvaluation
};
