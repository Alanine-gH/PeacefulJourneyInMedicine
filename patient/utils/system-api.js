/**
 * 系统相关API工具
 * 提供文件上传、系统配置等功能的接口
 */

import { request } from './config.js';

/**
 * 上传文件
 * @param {string} filePath - 文件路径
 * @param {string} bizType - 业务类型
 * @param {string} module - 模块
 * @param {number} timestamp - 时间戳
 * @returns {Promise<object>} 返回上传结果
 * @description 用于上传图片、视频等文件到服务器
 */
async function uploadFile(filePath, bizType, module, timestamp) {
  const token = uni.getStorageSync('token') || '';
  
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: baseUrl + '/api/file/upload',
      filePath: filePath,
      name: 'file',
      formData: {
        biz_type: bizType,
        module: module,
        timestamp: timestamp
      },
      header: {
        'Authorization': token ? 'Bearer ' + token : ''
      },
      success: (uploadFileRes) => {
        try {
          const response = JSON.parse(uploadFileRes.data);
          resolve(response);
        } catch (error) {
          reject(new Error('上传失败：' + uploadFileRes.data));
        }
      },
      fail: (err) => {
        reject(err);
      }
    });
  });
}

/**
 * 获取系统配置
 * @param {string} key - 配置键
 * @returns {Promise<object>} 返回配置值
 * @description 获取系统配置信息
 */
async function getSystemConfig(key) {
  return request('/api/config/get', {
    method: 'GET',
    data: { key }
  });
}

/**
 * 提交意见反馈
 * @param {object} data - 反馈数据
 * @param {string} data.type - 反馈类型
 * @param {string} data.content - 反馈内容
 * @param {string} data.contact - 联系方式
 * @param {array} data.images - 图片链接数组
 * @returns {Promise<object>} 返回提交结果
 * @description 提交用户意见反馈
 */
async function submitFeedback(data) {
  return request('/api/feedback/submit', {
    method: 'POST',
    data: data
  });
}

export {
  request,
  uploadFile,
  getSystemConfig,
  submitFeedback
};