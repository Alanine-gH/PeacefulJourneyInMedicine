/**
 * 全局配置文件
 * 修改后端地址只需改这一处
 * 通用请求方法统一在此定义，所有 api 文件直接 import { request } from './config.js'
 */

// 后端服务器地址（含端口），末尾不加斜杠
export const BASE_URL = 'http://localhost:8080';

/**
 * 将后端返回的文件名/路径转为可访问的完整 URL
 * @param {string} filename - 后端存储的文件名或路径
 * @returns {string} 完整图片 URL
 */
export function getFileUrl(filename) {
  if (!filename) return '';
  if (filename.startsWith('http')) return filename;
  return BASE_URL + '/common/download?name=' + encodeURIComponent(filename);
}

/**
 * 通用请求方法
 * @param {string} url - 请求路径（不含域名，如 '/user/info'）
 * @param {object} options - 请求配置
 * @param {string} [options.method='GET'] - 请求方法
 * @param {object} [options.data] - 请求数据
 * @param {object} [options.header] - 额外请求头
 * @returns {Promise} 返回后端响应数据
 */
export async function request(url, options = {}) {
  const token = uni.getStorageSync('token') || '';

  const config = {
    url: BASE_URL + url,
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
