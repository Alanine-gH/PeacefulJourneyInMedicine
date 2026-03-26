/**
 * 评价相关API接口
 * 提供评价列表、评价详情、评价管理等功能
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
 * 11.1 评价管理
 */

/**
 * 获取评价列表
 * @param {object} params - 查询参数
 * @param {string} params.target_type - 评价对象类型 (companion: 陪护员, order: 订单)
 * @param {string} params.target_id - 评价对象ID
 * @param {number} params.rating - 评分筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回评价列表
 * @description 获取评价列表，支持按对象类型、对象ID和评分筛选
 */
async function getEvaluationList(params) {
  return request('/order/evaluation/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取评价详情
 * @param {string} evaluationId - 评价ID
 * @returns {Promise<object>} 返回评价详细信息
 * @description 获取指定评价的详细内容
 */
async function getEvaluationDetail(evaluationId) {
  return request(`/order/evaluation/${evaluationId}`);
}

/**
 * 获取我的评价列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回我的评价列表
 * @description 获取当前用户发表的所有评价
 */
async function getMyEvaluations(params) {
  return request('/evaluation/my', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取待评价订单列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回待评价订单列表
 * @description 获取已完成但尚未评价的订单列表
 */
async function getPendingEvaluationOrders(params) {
  return request('/evaluation/pending', {
    method: 'GET',
    data: params
  });
}

/**
 * 删除评价
 * @param {string} evaluationId - 评价ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除自己发表的评价
 */
async function deleteEvaluation(evaluationId) {
  return request(`/evaluation/delete/${evaluationId}`, {
    method: 'DELETE'
  });
}

/**
 * 点赞评价
 * @param {string} evaluationId - 评价ID
 * @returns {Promise<object>} 返回点赞结果
 * @description 对指定评价点赞
 */
async function likeEvaluation(evaluationId) {
  return request(`/evaluation/like/${evaluationId}`, {
    method: 'POST'
  });
}

/**
 * 取消点赞评价
 * @param {string} evaluationId - 评价ID
 * @returns {Promise<object>} 返回取消点赞结果
 * @description 取消对指定评价的点赞
 */
async function unlikeEvaluation(evaluationId) {
  return request(`/evaluation/unlike/${evaluationId}`, {
    method: 'POST'
  });
}

/**
 * 获取评价统计
 * @param {string} targetType - 评价对象类型
 * @param {string} targetId - 评价对象ID
 * @returns {Promise<object>} 返回评价统计数据
 * @description 获取指定对象的评价统计数据，包括平均分、各评分数量等
 */
async function getEvaluationStatistics(targetType, targetId) {
  return request('/evaluation/statistics', {
    method: 'GET',
    data: { target_type: targetType, target_id: targetId }
  });
}

/**
 * 11.2 评价回复
 */

/**
 * 回复评价
 * @param {string} evaluationId - 评价ID
 * @param {object} data - 回复内容
 * @param {string} data.content - 回复内容
 * @returns {Promise<object>} 返回回复结果
 * @description 对指定评价进行回复
 */
async function replyToEvaluation(evaluationId, data) {
  return request(`/evaluation/reply/${evaluationId}`, {
    method: 'POST',
    data
  });
}

/**
 * 获取评价回复列表
 * @param {string} evaluationId - 评价ID
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回回复列表
 * @description 获取指定评价的所有回复
 */
async function getEvaluationReplies(evaluationId, params) {
  return request(`/evaluation/replies/${evaluationId}`, {
    method: 'GET',
    data: params
  });
}

/**
 * 删除评价回复
 * @param {string} replyId - 回复ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除自己的评价回复
 */
async function deleteEvaluationReply(replyId) {
  return request(`/evaluation/reply/delete/${replyId}`, {
    method: 'DELETE'
  });
}

export {
  getEvaluationList,
  getEvaluationDetail,
  getMyEvaluations,
  getPendingEvaluationOrders,
  deleteEvaluation,
  likeEvaluation,
  unlikeEvaluation,
  getEvaluationStatistics,
  replyToEvaluation,
  getEvaluationReplies,
  deleteEvaluationReply
};
