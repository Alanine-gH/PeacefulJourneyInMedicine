/**
 * 健康体检 - 服务套餐相关API
 * 对应后端 ServicePackageApiController
 */

import { request } from './config.js';

/**
 * 获取体检套餐列表（分页）
 * GET /service/packages
 * 后端支持的参数：page, pageSize, keyword（名称/描述模糊搜索）
 * 注意：后端不支持 packageType 筛选，类型筛选由前端完成
 * @param {object} params
 * @param {number}  params.page        页码（默认1）
 * @param {number}  params.pageSize    每页条数（默认10）
 * @param {string}  [params.keyword]   名称/描述模糊搜索
 * @returns {Promise}
 */
export function getHealthExamPackages(params = {}) {
  return request('/service/packages', { method: 'GET', data: params });
}

/**
 * 获取体检套餐详情
 * GET /service/packages/{packageId}
 * @param {number|string} packageId  套餐ID
 * @returns {Promise}
 */
export function getHealthExamPackageDetail(packageId) {
  return request('/service/packages/' + packageId, { method: 'GET' });
}
