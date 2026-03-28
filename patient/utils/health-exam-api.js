/**
 * 健康体检 - 服务套餐相关API
 * 对应后端 ServicePackageApiController
 */

import { request } from './api.js';

/**
 * 获取体检套餐列表（分页）
 * GET /service/packages
 * @param {object} params
 * @param {number}  params.page        页码（默认1）
 * @param {number}  params.pageSize    每页条数（默认10）
 * @param {number}  [params.packageType] 套餐类型 1基础 2增值 3定制
 * @param {string}  [params.keyword]   名称/描述模糊搜索
 * @returns {Promise<{code,data:{records:ServicePackage[],total:number},msg}>}
 */
export function getHealthExamPackages(params = {}) {
  return request('/service/packages', { method: 'GET', data: params });
}

/**
 * 获取体检套餐详情
 * GET /service/packages/{packageId}
 * @param {number|string} packageId  套餐ID
 * @returns {Promise<{code,data:ServicePackage,msg}>}
 */
export function getHealthExamPackageDetail(packageId) {
  return request(`/service/packages/${packageId}`, { method: 'GET' });
}
