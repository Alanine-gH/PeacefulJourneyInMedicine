/**
 * 患者端API接口
 * 提供患者相关的所有功能接口，包括个人中心、订单管理和收藏管理
 */

import { request } from './config.js';

/**
 * 2.1 患者个人中心
 */

/**
 * 获取患者信息
 * @returns {Promise<object>} 返回患者详细信息
 * @description 获取当前登录患者的个人信息，包括基本资料、健康档案等
 */
async function getPatientProfile() {
  return request('/patient/profile');
}

/**
 * 2.2 订单管理
 */

/**
 * 创建订单
 * @param {object} data - 订单信息
 * @param {string} data.service_type - 服务类型
 * @param {string} data.hospital_id - 医院ID
 * @param {string} data.department_id - 科室ID
 * @param {string} data.appointment_time - 预约时间
 * @param {string} data.patient_name - 患者姓名
 * @param {string} data.patient_phone - 患者电话
 * @param {string} data.patient_id_card - 患者身份证
 * @param {string} data.description - 服务描述
 * @returns {Promise<object>} 返回创建结果，包含订单号
 * @description 患者创建陪诊服务订单
 */
async function createPatientOrder(data) {
  return request('/patient/orders', {
    method: 'POST',
    data
  });
}

/**
 * 获取订单列表
 * @param {object} params - 查询参数
 * @param {string} params.status - 订单状态筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回订单列表
 * @description 获取患者的订单列表，支持按状态筛选和分页
 */
async function getPatientOrders(params) {
  return request('/order/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取订单详情
 * @param {string} orderNo - 订单编号
 * @returns {Promise<object>} 返回订单详细信息
 * @description 获取指定订单的详细信息，包括陪护员信息、服务内容、费用等
 */
async function getPatientOrderDetail(orderNo) {
  return request(`/order/order-no/${orderNo}`);
}

/**
 * 取消订单
 * @param {string} orderNo - 订单编号
 * @param {string} reason - 取消原因
 * @returns {Promise<object>} 返回取消结果
 * @description 患者取消订单，需要提供取消原因
 */
async function cancelPatientOrder(orderNo, reason) {
  // 先查询订单获取ID
  const detailResult = await getPatientOrderDetail(orderNo);
  const orderId = detailResult.data.id;
  
  // 调用后端订单状态变更接口（6表示已取消）
  return request(`/order/${orderId}/status`, {
    method: 'PUT',
    data: { orderStatus: 6, reason: reason }
  });
}

/**
 * 评价订单
 * @param {string} orderNo - 订单编号
 * @param {object} data - 评价信息
 * @param {number} data.rating - 评分 (1-5)
 * @param {string} data.content - 评价内容
 * @param {string[]} data.tags - 评价标签
 * @returns {Promise<object>} 返回评价结果
 * @description 患者对已完成的订单进行评价
 */
async function evaluateOrder(orderNo, data) {
  return request('/patient/orders/evaluate', {
    method: 'POST',
    data: { order_no: orderNo, ...data }
  });
}

/**
 * 2.3 收藏管理
 */

/**
 * 获取收藏列表
 * @param {object} params - 查询参数
 * @param {string} params.target_type - 收藏类型 (companion: 陪护员, package: 服务包)
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回收藏列表
 * @description 获取患者的收藏列表，支持按类型筛选
 */
async function getFavorites(params) {
  return request('/patient/favorites', {
    method: 'GET',
    data: params
  });
}

/**
 * 添加收藏
 * @param {string} targetType - 收藏类型 (companion: 陪护员, package: 服务包)
 * @param {string} targetId - 收藏目标ID
 * @returns {Promise<object>} 返回添加结果
 * @description 患者添加收藏，可以收藏陪护员或服务包
 */
async function addFavorite(targetType, targetId) {
  return request('/patient/favorites', {
    method: 'POST',
    data: { target_type: targetType, target_id: targetId }
  });
}

/**
 * 取消收藏
 * @param {string} targetType - 收藏类型 (companion: 陪护员, package: 服务包)
 * @param {string} targetId - 收藏目标ID
 * @returns {Promise<object>} 返回取消结果
 * @description 患者取消收藏
 */
async function removeFavorite(targetType, targetId) {
  return request('/patient/favorites/remove', {
    method: 'POST',
    data: { target_type: targetType, target_id: targetId }
  });
}

/**
 * 2.4 就诊人管理
 */

/**
 * 获取就诊人列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回就诊人列表
 * @description 获取患者的就诊人列表，包括就诊人基本信息和就诊卡信息
 */
async function getPatientList(params) {
  return request('/user/patient/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取就诊人详情
 * @param {string} patientId - 就诊人ID
 * @returns {Promise<object>} 返回就诊人详细信息
 * @description 获取指定就诊人的详细信息，包括基本信息、就诊卡列表等
 */
async function getPatientDetail(patientId) {
  return request(`/user/patient/${patientId}`);
}

/**
 * 添加就诊人
 * @param {object} data - 就诊人信息
 * @param {string} data.name - 姓名
 * @param {string} data.phone - 手机号
 * @param {string} data.id_card - 身份证号
 * @param {number} data.gender - 性别 (0: 女, 1: 男)
 * @param {string} data.birthday - 出生日期
 * @param {string} data.relation - 与患者关系
 * @param {string} data.address - 地址
 * @returns {Promise<object>} 返回添加结果
 * @description 添加新的就诊人信息
 */
async function addPatient(data) {
  return request('/user/patient', {
    method: 'POST',
    data
  });
}

/**
 * 更新就诊人信息
 * @param {string} patientId - 就诊人ID
 * @param {object} data - 就诊人信息
 * @param {string} data.name - 姓名
 * @param {string} data.phone - 手机号
 * @param {string} data.id_card - 身份证号
 * @param {number} data.gender - 性别
 * @param {string} data.birthday - 出生日期
 * @param {string} data.relation - 与患者关系
 * @param {string} data.address - 地址
 * @returns {Promise<object>} 返回更新结果
 * @description 更新指定就诊人的信息
 */
async function updatePatient(patientId, data) {
  return request(`/user/patient/${patientId}`, {
    method: 'PUT',
    data
  });
}

/**
 * 删除就诊人
 * @param {string} patientId - 就诊人ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除指定的就诊人信息
 */
async function deletePatient(patientId) {
  return request(`/user/patient/${patientId}`, {
    method: 'DELETE'
  });
}

/**
 * 设置默认就诊人
 * @param {string} patientId - 就诊人ID
 * @returns {Promise<object>} 返回设置结果
 * @description 将指定就诊人设置为默认就诊人
 */
async function setDefaultPatient(patientId) {
  return request(`/user/patient/${patientId}/default`, {
    method: 'POST'
  });
}

/**
 * 添加就诊卡
 * @param {string} patientId - 就诊人ID
 * @param {object} data - 就诊卡信息
 * @param {string} data.hospital_id - 医院ID
 * @param {string} data.hospital_name - 医院名称
 * @param {string} data.card_no - 就诊卡号
 * @returns {Promise<object>} 返回添加结果
 * @description 为指定就诊人添加就诊卡
 */
async function addMedicalCard(patientId, data) {
  return request(`/user/patient/${patientId}/medical-card`, {
    method: 'POST',
    data
  });
}

/**
 * 删除就诊卡
 * @param {string} patientId - 就诊人ID
 * @param {string} cardId - 就诊卡ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除指定的就诊卡
 */
async function deleteMedicalCard(patientId, cardId) {
  return request(`/user/patient/${patientId}/medical-card/${cardId}`, {
    method: 'DELETE'
  });
}

export {
  getPatientProfile,
  createPatientOrder,
  getPatientOrders,
  getPatientOrderDetail,
  cancelPatientOrder,
  evaluateOrder,
  getFavorites,
  addFavorite,
  removeFavorite,
  // 就诊人管理
  getPatientList,
  getPatientDetail,
  addPatient,
  updatePatient,
  deletePatient,
  setDefaultPatient,
  addMedicalCard,
  deleteMedicalCard
};
