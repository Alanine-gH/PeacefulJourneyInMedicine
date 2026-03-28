/**
 * 支付相关API接口
 * 提供支付订单创建、支付状态查询、支付方式管理等功能
 */

import { BASE_URL as baseUrl } from './config.js';

/**
 * 通用请求方法
 * @param {string} url - 请求路径
 * @param {object} options - 请求配置选项
 * @param {string} options.method - 请求方法 (GET/POST/PUT/DELETE)
 * @param {object} options.data - 请求数据
 * @param {object} options.header - 请求头
 * @returns {Promise} 返回请求结果
 */
async function request(url, options = {}) {
  const token = uni.getStorageSync('token') || '';
  
  const config = {
    url: baseUrl + url,
    method: options.method || 'GET',
    header: {
      'Content-Type': 'application/json',
      ...options.header
    },
    data: options.data
  };
  
  if (token) {
    config.header['Authorization'] = 'Bearer ' + token;
  }
  
  try {
    const response = await uni.request(config);
    if (response.statusCode === 200) {
      return response.data;
    } else if (response.statusCode === 401) {
      uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' });
      uni.navigateTo({ url: '/pages/login/login' });
      throw new Error('Token 过期');
    } else {
      throw new Error(`请求失败: ${response.statusCode}`);
    }
  } catch (error) {
    console.error('API 请求错误:', error);
    uni.showToast({ title: '网络连接失败，请稍后重试', icon: 'none' });
    throw error;
  }
}

/**
 * 8.1 支付管理
 */

/**
 * 创建支付订单
 * @param {object} data - 支付信息
 * @param {string} data.order_no - 业务订单号
 * @param {string} data.payment_type - 支付类型 (wechat: 微信支付, alipay: 支付宝)
 * @param {number} data.amount - 支付金额
 * @param {string} data.description - 支付描述
 * @returns {Promise<object>} 返回支付订单信息，包含支付参数
 * @description 创建支付订单，获取支付所需参数
 */
async function createPayment(data) {
  return request('/payment/create', {
    method: 'POST',
    data
  });
}

/**
 * 获取支付状态
 * @param {string} paymentNo - 支付订单号
 * @returns {Promise<object>} 返回支付状态
 * @description 查询指定支付订单的状态
 */
async function getPaymentStatus(paymentNo) {
  return request(`/payment/status/${paymentNo}`);
}

/**
 * 确认支付
 * @param {string} paymentNo - 支付订单号
 * @param {object} data - 确认信息
 * @param {string} data.payment_type - 支付方式
 * @param {string} data.transaction_id - 第三方支付交易号
 * @returns {Promise<object>} 返回确认结果
 * @description 确认支付完成，更新订单状态
 */
async function confirmPayment(paymentNo, data) {
  return request(`/payment/confirm/${paymentNo}`, {
    method: 'POST',
    data
  });
}

/**
 * 取消支付
 * @param {string} paymentNo - 支付订单号
 * @returns {Promise<object>} 返回取消结果
 * @description 取消未完成的支付订单
 */
async function cancelPayment(paymentNo) {
  return request(`/payment/cancel/${paymentNo}`, {
    method: 'POST'
  });
}

/**
 * 获取支付方式列表
 * @returns {Promise<object>} 返回支付方式列表
 * @description 获取系统支持的支付方式列表
 */
async function getPaymentMethods() {
  return request('/payment/methods');
}

/**
 * 获取支付记录列表
 * @param {object} params - 查询参数
 * @param {string} params.status - 支付状态筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回支付记录列表
 * @description 获取用户的支付记录历史
 */
async function getPaymentRecords(params) {
  return request('/payment/records', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取支付记录详情
 * @param {string} paymentNo - 支付订单号
 * @returns {Promise<object>} 返回支付记录详情
 * @description 获取指定支付记录的详细信息
 */
async function getPaymentRecordDetail(paymentNo) {
  return request(`/payment/records/${paymentNo}`);
}

/**
 * 申请退款
 * @param {string} paymentNo - 支付订单号
 * @param {object} data - 退款信息
 * @param {number} data.amount - 退款金额
 * @param {string} data.reason - 退款原因
 * @returns {Promise<object>} 返回退款申请结果
 * @description 申请订单退款
 */
async function applyRefund(paymentNo, data) {
  return request(`/payment/refund/${paymentNo}`, {
    method: 'POST',
    data
  });
}

/**
 * 获取退款记录
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回退款记录列表
 * @description 获取用户的退款记录列表
 */
async function getRefundRecords(params) {
  return request('/payment/refund-records', {
    method: 'GET',
    data: params
  });
}

/**
 * 8.2 支付授权
 */

/**
 * 获取支付授权信息
 * @returns {Promise<object>} 返回支付授权信息
 * @description 获取用户的支付授权状态和信息
 */
async function getPaymentAuthInfo() {
  return request('/payment/auth/info');
}

/**
 * 设置支付密码
 * @param {object} data - 密码信息
 * @param {string} data.password - 支付密码
 * @param {string} data.confirm_password - 确认密码
 * @returns {Promise<object>} 返回设置结果
 * @description 设置或修改支付密码
 */
async function setPaymentPassword(data) {
  return request('/payment/auth/password', {
    method: 'POST',
    data
  });
}

/**
 * 验证支付密码
 * @param {string} password - 支付密码
 * @returns {Promise<object>} 返回验证结果
 * @description 验证支付密码是否正确
 */
async function verifyPaymentPassword(password) {
  return request('/payment/auth/verify', {
    method: 'POST',
    data: { password }
  });
}

/**
 * 绑定支付方式
 * @param {object} data - 绑定信息
 * @param {string} data.type - 支付方式类型
 * @param {string} data.account - 账号
 * @returns {Promise<object>} 返回绑定结果
 * @description 绑定支付方式到用户账户
 */
async function bindPaymentMethod(data) {
  return request('/payment/auth/bind', {
    method: 'POST',
    data
  });
}

/**
 * 解绑支付方式
 * @param {string} type - 支付方式类型
 * @returns {Promise<object>} 返回解绑结果
 * @description 解绑用户的支付方式
 */
async function unbindPaymentMethod(type) {
  return request(`/payment/auth/unbind/${type}`, {
    method: 'POST'
  });
}

export {
  createPayment,
  getPaymentStatus,
  confirmPayment,
  cancelPayment,
  getPaymentMethods,
  getPaymentRecords,
  getPaymentRecordDetail,
  applyRefund,
  getRefundRecords,
  getPaymentAuthInfo,
  setPaymentPassword,
  verifyPaymentPassword,
  bindPaymentMethod,
  unbindPaymentMethod
};
