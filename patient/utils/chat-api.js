/**
 * 聊天相关API接口
 * 提供聊天记录、发送消息、聊天列表等功能
 */

import { request } from './config.js';



/**
 * 10.1 聊天管理
 */

/**
 * 获取聊天列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回聊天列表
 * @description 获取用户的聊天会话列表
 */
async function getChatList(params) {
  return request('/chat/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取聊天记录
 * @param {string} targetId - 聊天对象ID
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {string} params.before_time - 获取此时间之前的消息
 * @returns {Promise<object>} 返回聊天记录
 * @description 获取与指定对象的聊天记录
 */
async function getChatHistory(targetId, params) {
  return request(`/chat/history/${targetId}`, {
    method: 'GET',
    data: params
  });
}

/**
 * 发送消息
 * @param {object} data - 消息数据
 * @param {string} data.target_id - 接收者ID
 * @param {string} data.content - 消息内容
 * @param {string} data.type - 消息类型 (text: 文本, image: 图片, voice: 语音)
 * @param {string} data.extra - 额外数据（如图片URL等）
 * @returns {Promise<object>} 返回发送结果
 * @description 向指定用户发送消息
 */
async function sendMessage(data) {
  return request('/chat/send', {
    method: 'POST',
    data
  });
}

/**
 * 发送图片消息
 * @param {string} targetId - 接收者ID
 * @param {string} filePath - 本地图片路径
 * @returns {Promise<object>} 返回发送结果
 * @description 发送图片消息
 */
async function sendImageMessage(targetId, filePath) {
  const token = uni.getStorageSync('token');
  
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: baseUrl + '/chat/send-image',
      filePath: filePath,
      name: 'file',
      formData: {
        target_id: targetId
      },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (uploadFileRes) => {
        const data = JSON.parse(uploadFileRes.data);
        if (data.code === 200) {
          resolve(data);
        } else {
          reject(new Error(data.message || '发送失败'));
        }
      },
      fail: (error) => {
        reject(error);
      }
    });
  });
}

/**
 * 标记消息已读
 * @param {string} targetId - 聊天对象ID
 * @returns {Promise<object>} 返回标记结果
 * @description 将与指定对象的未读消息标记为已读
 */
async function markChatRead(targetId) {
  return request(`/chat/read/${targetId}`, {
    method: 'POST'
  });
}

/**
 * 删除聊天记录
 * @param {string} targetId - 聊天对象ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除与指定对象的聊天记录
 */
async function deleteChatHistory(targetId) {
  return request(`/chat/delete/${targetId}`, {
    method: 'DELETE'
  });
}

/**
 * 获取未读消息总数
 * @returns {Promise<object>} 返回未读消息数量
 * @description 获取所有聊天的未读消息总数
 */
async function getTotalUnreadCount() {
  return request('/chat/unread-total');
}

/**
 * 创建聊天会话
 * @param {object} data - 会话信息
 * @param {string} data.target_id - 聊天对象ID
 * @param {string} data.type - 会话类型 (customer: 客服, companion: 陪护员, system: 系统)
 * @returns {Promise<object>} 返回创建结果
 * @description 创建新的聊天会话
 */
async function createChatSession(data) {
  return request('/chat/session', {
    method: 'POST',
    data
  });
}

/**
 * 获取聊天会话信息
 * @param {string} targetId - 聊天对象ID
 * @returns {Promise<object>} 返回会话信息
 * @description 获取与指定对象的聊天会话信息
 */
async function getChatSession(targetId) {
  return request(`/chat/session/${targetId}`);
}

/**
 * 撤回消息
 * @param {string} messageId - 消息ID
 * @returns {Promise<object>} 返回撤回结果
 * @description 撤回已发送的消息
 */
async function recallMessage(messageId) {
  return request(`/chat/recall/${messageId}`, {
    method: 'POST'
  });
}

/**
 * 10.2 客服聊天
 */

/**
 * 获取客服信息
 * @returns {Promise<object>} 返回客服信息
 * @description 获取在线客服信息
 */
async function getCustomerServiceInfo() {
  return request('/chat/customer-service/info');
}

/**
 * 连接客服
 * @returns {Promise<object>} 返回连接结果
 * @description 连接到在线客服
 */
async function connectCustomerService() {
  return request('/chat/customer-service/connect', {
    method: 'POST'
  });
}

/**
 * 断开客服连接
 * @returns {Promise<object>} 返回断开结果
 * @description 断开与客服的连接
 */
async function disconnectCustomerService() {
  return request('/chat/customer-service/disconnect', {
    method: 'POST'
  });
}

/**
 * 获取常见问题列表
 * @returns {Promise<object>} 返回常见问题列表
 * @description 获取客服常见问题列表
 */
async function getFAQList() {
  return request('/chat/customer-service/faq');
}

export {
  getChatList,
  getChatHistory,
  sendMessage,
  sendImageMessage,
  markChatRead,
  deleteChatHistory,
  getTotalUnreadCount,
  createChatSession,
  getChatSession,
  recallMessage,
  getCustomerServiceInfo,
  connectCustomerService,
  disconnectCustomerService,
  getFAQList
};
