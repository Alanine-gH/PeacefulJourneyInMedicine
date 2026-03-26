/**
 * 增值服务相关API接口
 * 提供增值服务列表、选择、管理等功能
 */

const baseUrl = 'http://localhost:8080';

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
 * 13.1 增值服务管理
 */

/**
 * 获取增值服务列表
 * @param {object} params - 查询参数
 * @param {string} params.category - 服务分类
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回增值服务列表
 * @description 获取所有可用的增值服务列表
 */
async function getValueAddedServices(params) {
  return request('/value-added/services', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取增值服务详情
 * @param {string} serviceId - 服务ID
 * @returns {Promise<object>} 返回增值服务详细信息
 * @description 获取指定增值服务的详细信息
 */
async function getValueAddedServiceDetail(serviceId) {
  return request(`/value-added/service/${serviceId}`);
}

/**
 * 选择增值服务
 * @param {object} data - 选择信息
 * @param {string} data.service_id - 服务ID
 * @param {string} data.order_no - 关联订单号
 * @param {number} data.quantity - 数量
 * @returns {Promise<object>} 返回选择结果
 * @description 为订单选择增值服务
 */
async function selectValueAddedService(data) {
  return request('/value-added/select', {
    method: 'POST',
    data
  });
}

/**
 * 取消选择的增值服务
 * @param {string} selectionId - 选择记录ID
 * @returns {Promise<object>} 返回取消结果
 * @description 取消已选择的增值服务
 */
async function cancelValueAddedService(selectionId) {
  return request(`/value-added/cancel/${selectionId}`, {
    method: 'POST'
  });
}

/**
 * 获取已选增值服务列表
 * @param {string} orderNo - 订单号
 * @returns {Promise<object>} 返回已选增值服务列表
 * @description 获取指定订单已选择的所有增值服务
 */
async function getSelectedValueAddedServices(orderNo) {
  return request(`/value-added/selected/${orderNo}`);
}

/**
 * 获取增值服务分类列表
 * @returns {Promise<object>} 返回分类列表
 * @description 获取增值服务的所有分类
 */
async function getValueAddedCategories() {
  return request('/value-added/categories');
}

/**
 * 获取热门增值服务
 * @param {object} params - 查询参数
 * @param {number} params.limit - 数量限制
 * @returns {Promise<object>} 返回热门增值服务列表
 * @description 获取热门的增值服务列表
 */
async function getPopularValueAddedServices(params) {
  return request('/value-added/popular', {
    method: 'GET',
    data: params
  });
}

/**
 * 计算增值服务总价
 * @param {object} data - 计算参数
 * @param {string[]} data.service_ids - 服务ID数组
 * @param {string} data.order_no - 订单号
 * @returns {Promise<object>} 返回计算结果
 * @description 计算所选增值服务的总价
 */
async function calculateValueAddedTotal(data) {
  return request('/value-added/calculate', {
    method: 'POST',
    data
  });
}

/**
 * 获取增值服务推荐
 * @param {string} orderNo - 订单号
 * @returns {Promise<object>} 返回推荐列表
 * @description 根据订单信息推荐相关的增值服务
 */
async function getRecommendedValueAddedServices(orderNo) {
  return request(`/value-added/recommended/${orderNo}`);
}

export {
  getValueAddedServices,
  getValueAddedServiceDetail,
  selectValueAddedService,
  cancelValueAddedService,
  getSelectedValueAddedServices,
  getValueAddedCategories,
  getPopularValueAddedServices,
  calculateValueAddedTotal,
  getRecommendedValueAddedServices
};
