import request from './auth'

// 过滤空字符串参数，避免 Byte/Integer 类型转换异常
function cleanParams(params) {
    const result = {}
    for (const key in params) {
        const v = params[key]
        if (v !== '' && v !== null && v !== undefined) result[key] = v
    }
    return result
}

// ==================== 服务套餐 ====================
export function getPackageList(params) {
    return request({url: '/service/package/list', method: 'get', params: cleanParams(params)})
}

export function getPackageDetail(id) {
    return request({url: `/service/package/${id}`, method: 'get'})
}

export function addPackage(data) {
    return request({url: '/service/package', method: 'post', data})
}

export function updatePackage(id, data) {
    return request({url: `/service/package/${id}`, method: 'put', data})
}

export function deletePackage(id) {
    return request({url: `/service/package/${id}`, method: 'delete'})
}

export function updatePackageStatus(id, status) {
    return request({url: `/service/package/${id}/status`, method: 'put', data: {status}})
}

// ==================== 调度派单 ====================
export function getDispatchList(params) {
    return request({url: '/service/dispatch/list', method: 'get', params: cleanParams(params)})
}

export function getDispatchDetail(id) {
    return request({url: `/service/dispatch/${id}`, method: 'get'})
}

export function addDispatch(data) {
    return request({url: '/service/dispatch', method: 'post', data})
}

export function updateDispatchStatus(id, status, rejectReason) {
    return request({url: `/service/dispatch/${id}/status`, method: 'put', data: {status, rejectReason}})
}

export function deleteDispatch(id) {
    return request({url: `/service/dispatch/${id}`, method: 'delete'})
}

// ==================== 打卡记录 ====================
export function getCheckinList(params) {
    return request({url: '/service/checkin/list', method: 'get', params: cleanParams(params)})
}

export function getCheckinDetail(id) {
    return request({url: `/service/checkin/${id}`, method: 'get'})
}

export function deleteCheckin(id) {
    return request({url: `/service/checkin/${id}`, method: 'delete'})
}

// ==================== 陪诊师日程 ====================
export function getScheduleList(params) {
    return request({url: '/service/schedule/list', method: 'get', params: cleanParams(params)})
}

export function addSchedule(data) {
    return request({url: '/service/schedule', method: 'post', data})
}

export function updateSchedule(id, data) {
    return request({url: `/service/schedule/${id}`, method: 'put', data})
}

export function deleteSchedule(id) {
    return request({url: `/service/schedule/${id}`, method: 'delete'})
}
