/**
 * 病历和套餐相关API接口
 * 提供电子病历管理、检查报告上传、服务套餐浏览等功能
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
 * 6.1 电子病历
 */

/**
 * 获取病历列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回病历列表
 * @description 获取用户的电子病历列表
 */
async function getMedicalRecords(params) {
  return request('/medical/records', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取病历详情
 * @param {string} recordId - 病历ID
 * @returns {Promise<object>} 返回病历详细信息
 * @description 获取指定病历的详细信息，包括诊断、处方、检查报告等
 */
async function getMedicalRecordDetail(recordId) {
  return request(`/medical/records/${recordId}`);
}

/**
 * 上传检查报告
 * @param {object} data - 报告信息
 * @param {string} data.report_type - 报告类型 (如: blood_test, x_ray, ct等)
 * @param {string} data.report_date - 检查日期
 * @param {string} data.hospital_id - 检查医院ID
 * @param {string} data.report_file - 报告文件URL
 * @param {string} data.description - 报告描述
 * @returns {Promise<object>} 返回上传结果
 * @description 上传检查报告，支持多种类型的医疗检查报告
 */
async function uploadMedicalReport(data) {
  return request('/medical/report/upload', {
    method: 'POST',
    data
  });
}

/**
 * 获取检查报告列表
 * @param {object} params - 查询参数
 * @param {string} params.report_type - 报告类型筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回检查报告列表
 * @description 获取用户的检查报告列表，支持按类型筛选
 */
async function getMedicalReports(params) {
  return request('/medical/reports', {
    method: 'GET',
    data: params
  });
}

/**
 * 6.2 套餐管理
 */

/**
 * 获取套餐列表
 * @param {object} params - 查询参数
 * @param {string} params.category - 套餐分类筛选
 * @param {string} params.keyword - 搜索关键词
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回套餐列表
 * @description 获取服务套餐列表，支持按分类和关键词筛选
 */
async function getServicePackages(params) {
  return request('/service/packages', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取套餐详情
 * @param {string} packageId - 套餐ID
 * @returns {Promise<object>} 返回套餐详细信息
 * @description 获取指定服务套餐的详细信息，包括服务内容、价格、适用人群等
 */
async function getServicePackageDetail(packageId) {
  return request(`/service/packages/${packageId}`);
}

/**
 * 收藏套餐
 * @param {string} packageId - 套餐ID
 * @returns {Promise<object>} 返回收藏结果
 * @description 收藏指定的服务套餐
 */
async function favoritePackage(packageId) {
  return request('/service/packages/favorite', {
    method: 'POST',
    data: { package_id: packageId }
  });
}

/**
 * 取消收藏套餐
 * @param {string} packageId - 套餐ID
 * @returns {Promise<object>} 返回取消结果
 * @description 取消收藏指定的服务套餐
 */
async function unfavoritePackage(packageId) {
  return request('/service/packages/unfavorite', {
    method: 'POST',
    data: { package_id: packageId }
  });
}

export {
  getMedicalRecords,
  getMedicalRecordDetail,
  uploadMedicalReport,
  getMedicalReports,
  getServicePackages,
  getServicePackageDetail,
  favoritePackage,
  unfavoritePackage
};
