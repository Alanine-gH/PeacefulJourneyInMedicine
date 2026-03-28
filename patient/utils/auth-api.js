/**
 * 实名认证相关API接口
 * 提供实名认证、身份证上传、认证状态查询等功能
 */

import { request } from './config.js';

/**
 * 9.1 实名认证管理
 */

/**
 * 提交实名认证
 * @param {object} data - 认证信息
 * @param {string} data.real_name - 真实姓名
 * @param {string} data.id_card - 身份证号
 * @param {string} data.id_card_front - 身份证正面照片URL
 * @param {string} data.id_card_back - 身份证背面照片URL
 * @param {string} data.face_recognition - 人脸识别照片URL
 * @returns {Promise<object>} 返回提交结果
 * @description 提交实名认证申请，包括身份证信息和人脸识别
 */
async function submitRealNameAuth(data) {
  return request('/auth/real-name', {
    method: 'POST',
    data
  });
}

/**
 * 获取实名认证状态
 * @returns {Promise<object>} 返回认证状态信息
 * @description 查询当前用户的实名认证状态
 */
async function getAuthStatus() {
  return request('/auth/real-name/status');
}

/**
 * 获取实名认证信息
 * @returns {Promise<object>} 返回认证详细信息
 * @description 获取用户的实名认证详细信息
 */
async function getAuthInfo() {
  return request('/auth/real-name/info');
}

/**
 * 上传身份证照片
 * @param {string} filePath - 本地文件路径
 * @param {string} side - 身份证面 (front: 正面, back: 背面)
 * @returns {Promise<object>} 返回上传结果，包含照片URL
 * @description 上传身份证照片到服务器
 */
async function uploadIdCard(filePath, side) {
  const token = uni.getStorageSync('token');
  
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: baseUrl + '/auth/real-name/upload-idcard',
      filePath: filePath,
      name: 'file',
      formData: {
        side: side
      },
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (uploadFileRes) => {
        const data = JSON.parse(uploadFileRes.data);
        if (data.code === 200) {
          resolve(data);
        } else {
          reject(new Error(data.message || '上传失败'));
        }
      },
      fail: (error) => {
        reject(error);
      }
    });
  });
}

/**
 * 上传人脸识别照片
 * @param {string} filePath - 本地文件路径
 * @returns {Promise<object>} 返回上传结果，包含照片URL
 * @description 上传人脸识别照片到服务器
 */
async function uploadFaceRecognition(filePath) {
  const token = uni.getStorageSync('token');
  
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: baseUrl + '/auth/real-name/upload-face',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': 'Bearer ' + token
      },
      success: (uploadFileRes) => {
        const data = JSON.parse(uploadFileRes.data);
        if (data.code === 200) {
          resolve(data);
        } else {
          reject(new Error(data.message || '上传失败'));
        }
      },
      fail: (error) => {
        reject(error);
      }
    });
  });
}

/**
 * 重新提交实名认证
 * @param {object} data - 认证信息
 * @returns {Promise<object>} 返回提交结果
 * @description 实名认证被拒绝后重新提交
 */
async function resubmitRealNameAuth(data) {
  return request('/auth/real-name/resubmit', {
    method: 'POST',
    data
  });
}

/**
 * 取消实名认证申请
 * @returns {Promise<object>} 返回取消结果
 * @description 取消待审核的实名认证申请
 */
async function cancelRealNameAuth() {
  return request('/auth/real-name/cancel', {
    method: 'POST'
  });
}

/**
 * 9.2 认证步骤管理
 */

/**
 * 保存认证步骤1数据
 * @param {object} data - 步骤数据
 * @param {string} data.real_name - 真实姓名
 * @param {string} data.id_card - 身份证号
 * @returns {Promise<object>} 返回保存结果
 * @description 保存实名认证步骤1的基本信息
 */
async function saveAuthStep1(data) {
  return request('/auth/real-name/step1', {
    method: 'POST',
    data
  });
}

/**
 * 保存认证步骤2数据
 * @param {object} data - 步骤数据
 * @param {string} data.id_card_front - 身份证正面照片URL
 * @param {string} data.id_card_back - 身份证背面照片URL
 * @returns {Promise<object>} 返回保存结果
 * @description 保存实名认证步骤2的身份证照片
 */
async function saveAuthStep2(data) {
  return request('/auth/real-name/step2', {
    method: 'POST',
    data
  });
}

/**
 * 保存认证步骤3数据
 * @param {object} data - 步骤数据
 * @param {string} data.face_recognition - 人脸识别照片URL
 * @returns {Promise<object>} 返回保存结果
 * @description 保存实名认证步骤3的人脸识别照片
 */
async function saveAuthStep3(data) {
  return request('/auth/real-name/step3', {
    method: 'POST',
    data
  });
}

/**
 * 获取认证步骤数据
 * @param {number} step - 步骤编号 (1, 2, 3)
 * @returns {Promise<object>} 返回步骤数据
 * @description 获取指定认证步骤的已保存数据
 */
async function getAuthStepData(step) {
  return request(`/auth/real-name/step/${step}`);
}

export {
  submitRealNameAuth,
  getAuthStatus,
  getAuthInfo,
  uploadIdCard,
  uploadFaceRecognition,
  resubmitRealNameAuth,
  cancelRealNameAuth,
  saveAuthStep1,
  saveAuthStep2,
  saveAuthStep3,
  getAuthStepData
};
