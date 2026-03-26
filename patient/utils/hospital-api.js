/**
 * 医院/科室选择相关API接口
 * 提供医院列表、科室列表、搜索等功能
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
 * 12.1 医院选择
 */

/**
 * 按城市获取医院列表
 * @param {object} params - 查询参数
 * @param {string} params.city - 城市名称
 * @param {string} params.level - 医院等级筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回医院列表
 * @description 根据城市获取医院列表，支持按等级筛选
 */
async function getHospitalListByCity(params) {
  return request('/hospital/list-by-city', {
    method: 'GET',
    data: params
  });
}

/**
 * 搜索医院
 * @param {object} params - 查询参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.city - 城市名称
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回搜索结果
 * @description 根据关键词搜索医院
 */
async function searchHospitals(params) {
  return request('/hospital/search', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取附近医院列表
 * @param {object} params - 查询参数
 * @param {number} params.latitude - 纬度
 * @param {number} params.longitude - 经度
 * @param {number} params.distance - 搜索半径（公里）
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回附近医院列表
 * @description 根据当前位置获取附近的医院列表
 */
async function getNearbyHospitals(params) {
  return request('/hospital/nearby', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取热门医院列表
 * @param {object} params - 查询参数
 * @param {string} params.city - 城市名称
 * @param {number} params.limit - 数量限制
 * @returns {Promise<object>} 返回热门医院列表
 * @description 获取指定城市的热门医院列表
 */
async function getPopularHospitals(params) {
  return request('/hospital/popular', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取医院详情
 * @param {string} hospitalId - 医院ID
 * @returns {Promise<object>} 返回医院详细信息
 * @description 获取指定医院的详细信息
 */
async function getHospitalDetail(hospitalId) {
  return request(`/hospital/detail/${hospitalId}`);
}

/**
 * 获取城市列表
 * @returns {Promise<object>} 返回城市列表
 * @description 获取所有支持的城市列表
 */
async function getCityList() {
  return request('/hospital/cities');
}

/**
 * 获取定位城市
 * @param {object} params - 位置参数
 * @param {number} params.latitude - 纬度
 * @param {number} params.longitude - 经度
 * @returns {Promise<object>} 返回定位城市信息
 * @description 根据经纬度获取当前所在城市
 */
async function getLocationCity(params) {
  return request('/hospital/location-city', {
    method: 'GET',
    data: params
  });
}

/**
 * 12.2 科室选择
 */

/**
 * 获取医院科室列表
 * @param {string} hospitalId - 医院ID
 * @param {object} params - 查询参数
 * @param {string} params.parent_id - 父科室ID（获取子科室时使用）
 * @returns {Promise<object>} 返回科室列表
 * @description 获取指定医院的科室列表
 */
async function getDepartmentListByHospital(hospitalId, params) {
  return request(`/hospital/${hospitalId}/departments`, {
    method: 'GET',
    data: params
  });
}

/**
 * 获取科室详情
 * @param {string} departmentId - 科室ID
 * @returns {Promise<object>} 返回科室详细信息
 * @description 获取指定科室的详细信息
 */
async function getDepartmentDetail(departmentId) {
  return request(`/department/detail/${departmentId}`);
}

/**
 * 搜索科室
 * @param {object} params - 查询参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.hospital_id - 医院ID
 * @returns {Promise<object>} 返回搜索结果
 * @description 根据关键词搜索科室
 */
async function searchDepartments(params) {
  return request('/department/search', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取热门科室列表
 * @param {object} params - 查询参数
 * @param {number} params.limit - 数量限制
 * @returns {Promise<object>} 返回热门科室列表
 * @description 获取热门科室列表
 */
async function getPopularDepartments(params) {
  return request('/department/popular', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取科室医生列表
 * @param {string} departmentId - 科室ID
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回医生列表
 * @description 获取指定科室的医生列表
 */
async function getDepartmentDoctors(departmentId, params) {
  return request(`/department/${departmentId}/doctors`, {
    method: 'GET',
    data: params
  });
}

export {
  getHospitalListByCity,
  searchHospitals,
  getNearbyHospitals,
  getPopularHospitals,
  getHospitalDetail,
  getCityList,
  getLocationCity,
  getDepartmentListByHospital,
  getDepartmentDetail,
  searchDepartments,
  getPopularDepartments,
  getDepartmentDoctors
};
