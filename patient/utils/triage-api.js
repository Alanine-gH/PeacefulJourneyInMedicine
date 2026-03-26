/**
 * 分诊和预约相关API接口
 * 提供智能分诊、就医建议、预约挂号等功能
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
 * 5.1 分诊管理
 */

/**
 * 创建分诊记录
 * @param {object} data - 分诊信息
 * @param {string} data.symptoms - 症状描述
 * @param {number} data.pain_level - 疼痛等级 (1-10)
 * @param {string} data.duration - 持续时间
 * @param {string[]} data.medical_history - 病史
 * @returns {Promise<object>} 返回创建结果，包含分诊ID
 * @description 创建智能分诊记录，系统会根据症状推荐科室
 */
async function createTriageRecord(data) {
  return request('/triage/records', {
    method: 'POST',
    data
  });
}

/**
 * 获取分诊记录列表
 * @param {object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回分诊记录列表
 * @description 获取用户的分诊记录历史
 */
async function getTriageRecords(params) {
  return request('/triage/records', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取分诊记录详情
 * @param {string} triageId - 分诊记录ID
 * @returns {Promise<object>} 返回分诊记录详细信息
 * @description 获取指定分诊记录的详细信息，包括症状、推荐科室、建议等
 */
async function getTriageRecordDetail(triageId) {
  return request(`/triage/records/${triageId}`);
}

/**
 * 获取就医建议书
 * @param {string} triageId - 分诊记录ID
 * @returns {Promise<object>} 返回就医建议书
 * @description 根据分诊记录生成详细的就医建议书，包括推荐科室、医生、注意事项等
 */
async function getMedicalAdvice(triageId) {
  return request(`/triage/advice/${triageId}`);
}

/**
 * 5.2 预约管理
 */

/**
 * 创建预约
 * @param {object} data - 预约信息
 * @param {string} data.hospital_id - 医院ID
 * @param {string} data.department_id - 科室ID
 * @param {string} data.expert_id - 专家ID (可选)
 * @param {string} data.appointment_date - 预约日期
 * @param {string} data.appointment_time - 预约时间段
 * @param {string} data.patient_name - 患者姓名
 * @param {string} data.patient_phone - 患者电话
 * @param {string} data.patient_id_card - 患者身份证
 * @returns {Promise<object>} 返回创建结果，包含预约号
 * @description 创建门诊预约挂号
 */
async function createAppointment(data) {
  return request('/appointment/create', {
    method: 'POST',
    data
  });
}

/**
 * 获取预约列表
 * @param {object} params - 查询参数
 * @param {string} params.status - 预约状态筛选
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页数量
 * @returns {Promise<object>} 返回预约列表
 * @description 获取用户的预约挂号列表，支持按状态筛选
 */
async function getAppointments(params) {
  return request('/appointment/list', {
    method: 'GET',
    data: params
  });
}

/**
 * 获取预约详情
 * @param {string} appointmentId - 预约ID
 * @returns {Promise<object>} 返回预约详细信息
 * @description 获取指定预约的详细信息，包括医院、科室、医生、时间等
 */
async function getAppointmentDetail(appointmentId) {
  return request(`/appointment/detail/${appointmentId}`);
}

/**
 * 取消预约
 * @param {string} appointmentId - 预约ID
 * @param {string} reason - 取消原因
 * @returns {Promise<object>} 返回取消结果
 * @description 取消预约挂号，需要提供取消原因
 */
async function cancelAppointment(appointmentId, reason) {
  return request(`/appointment/cancel/${appointmentId}`, {
    method: 'POST',
    data: { cancel_reason: reason }
  });
}

export {
  createTriageRecord,
  getTriageRecords,
  getTriageRecordDetail,
  getMedicalAdvice,
  createAppointment,
  getAppointments,
  getAppointmentDetail,
  cancelAppointment
};
