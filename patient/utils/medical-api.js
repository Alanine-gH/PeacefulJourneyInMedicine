/**
 * 医疗相关API接口
 * 提供医院、科室、专家等医疗资源的查询功能
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
 * 4.1 医院管理
 */

/**
 * 获取医院列表
 * @param {object} params - 查询参数
 * @param {string} params.city - 城市筛选
 * @param {string} params.level - 医院等级筛选
 * @param {string} params.keyword - 搜索关键词
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回医院列表
 * @description 获取医院列表，支持按城市、等级、关键词筛选和分页
 */
async function getHospitals(params) {
  return request('/medical/hospitals', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取医院详情
 * @param {string} hospitalId - 医院ID
 * @returns {Promise<object>} 返回医院详细信息
 * @description 获取指定医院的详细信息，包括地址、电话、科室列表等
 */
async function getHospitalDetail(hospitalId) {
  return request(`/medical/hospitals/${hospitalId}`);
}

/**
 * 4.2 科室管理
 */

/**
 * 获取科室列表
 * @param {object} params - 查询参数
 * @param {string} params.hospital_id - 医院ID
 * @param {string} params.parent_id - 父科室ID (获取子科室时使用)
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回科室列表
 * @description 获取科室列表，支持按医院筛选，可获取一级科室或子科室
 */
async function getDepartments(params) {
  return request('/medical/departments', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取科室详情
 * @param {string} departmentId - 科室ID
 * @returns {Promise<object>} 返回科室详细信息
 * @description 获取指定科室的详细信息，包括科室介绍、专家列表等
 */
async function getDepartmentDetail(departmentId) {
  return request(`/medical/departments/${departmentId}`);
}

/**
 * 4.3 专家管理
 */

/**
 * 获取专家列表
 * @param {object} params - 查询参数
 * @param {string} params.hospital_id - 医院ID
 * @param {string} params.department_id - 科室ID
 * @param {string} params.keyword - 搜索关键词
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回专家列表
 * @description 获取专家列表，支持按医院、科室、关键词筛选和分页
 */
async function getExperts(params) {
  return request('/medical/experts', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取专家详情
 * @param {string} expertId - 专家ID
 * @returns {Promise<object>} 返回专家详细信息
 * @description 获取指定专家的详细信息，包括职称、专长、出诊时间等
 */
async function getExpertDetail(expertId) {
  return request(`/medical/experts/${expertId}`);
}

/**
 * 搜索医院
 * @param {object} params - 查询参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.city - 城市
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 */
async function searchHospitals(params) {
  return request('/hospital/search', {
    method: 'GET',
    data: params
  });
}

export {
  getHospitals,
  getHospitalDetail,
  getDepartments,
  getDepartmentDetail,
  getExperts,
  getExpertDetail,
  searchHospitals
};
