/**
 * 消息相关API接口
 * 提供消息列表、消息详情、消息状态管理等功能
 */

import { request } from './config.js';

/**
 * 7.1 消息管理
 */

/**
 * 获取消息列表
 * @param {object} params - 查询参数
 * @param {string} params.type - 消息类型 (system: 系统消息, order: 订单消息, chat: 聊天消息)
 * @param {string} params.status - 消息状态 (unread: 未读, read: 已读)
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回消息列表
 * @description 获取用户的消息列表，支持按类型和状态筛选
 */
async function getMessageList(params) {
  return request('/message/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取消息详情
 * @param {string} messageId - 消息ID
 * @returns {Promise<object>} 返回消息详细信息
 * @description 获取指定消息的详细内容
 */
async function getMessageDetail(messageId) {
  return request(`/message/detail/${messageId}`);
}

/**
 * 标记消息已读
 * @param {string} messageId - 消息ID
 * @returns {Promise<object>} 返回标记结果
 * @description 将指定消息标记为已读状态
 */
async function markMessageRead(messageId) {
  return request(`/message/read/${messageId}`, {
    method: 'POST'
  });
}

/**
 * 标记所有消息已读
 * @param {string} type - 消息类型 (可选，不传则标记所有类型)
 * @returns {Promise<object>} 返回标记结果
 * @description 将所有消息或指定类型的消息标记为已读
 */
async function markAllMessagesRead(type) {
  return request('/message/read-all', {
    method: 'POST',
    data: type ? { type } : {}
  });
}

/**
 * 删除消息
 * @param {string} messageId - 消息ID
 * @returns {Promise<object>} 返回删除结果
 * @description 删除指定消息
 */
async function deleteMessage(messageId) {
  return request(`/message/delete/${messageId}`, {
    method: 'DELETE'
  });
}

/**
 * 批量删除消息
 * @param {string[]} messageIds - 消息ID数组
 * @returns {Promise<object>} 返回删除结果
 * @description 批量删除多条消息
 */
async function batchDeleteMessages(messageIds) {
  return request('/message/batch-delete', {
    method: 'POST',
    data: { message_ids: messageIds }
  });
}

/**
 * 获取未读消息数量
 * @returns {Promise<object>} 返回未读消息数量
 * @description 获取用户所有未读消息的总数
 */
async function getUnreadMessageCount() {
  return request('/message/unread-count');
}

/**
 * 发送消息
 * @param {object} data - 消息数据
 * @param {string} data.receiver_id - 接收者ID
 * @param {string} data.content - 消息内容
 * @param {string} data.type - 消息类型
 * @returns {Promise<object>} 返回发送结果
 * @description 向指定用户发送消息
 */
async function sendMessage(data) {
  return request('/message/send', {
    method: 'POST',
    data
  });
}

export {
  getMessageList,
  getMessageDetail,
  markMessageRead,
  markAllMessagesRead,
  deleteMessage,
  batchDeleteMessages,
  getUnreadMessageCount,
  sendMessage
};
