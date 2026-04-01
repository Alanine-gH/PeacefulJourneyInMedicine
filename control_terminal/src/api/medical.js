import request from './auth'
import {cleanParams} from './utils'

// ==================== 医院管理 ====================
export function getHospitalCount() {
    return request({url: '/medical/hospital/stat/count', method: 'get'})
}

export function getHospitalList(params) {
    return request({url: '/medical/hospital/list', method: 'get', params: cleanParams(params)})
}

export function getHospitalDetail(id) {
    return request({url: `/medical/hospital/${id}`, method: 'get'})
}

export function addHospital(data) {
    return request({url: '/medical/hospital', method: 'post', data})
}

export function updateHospital(id, data) {
    return request({url: `/medical/hospital/${id}`, method: 'put', data})
}

export function deleteHospital(id) {
    return request({url: `/medical/hospital/${id}`, method: 'delete'})
}

export function updateHospitalStatus(id, status) {
    return request({url: `/medical/hospital/${id}/status`, method: 'put', data: {status}})
}

// ==================== 专家管理 ====================
export function getExpertList(params) {
    return request({url: '/medical/expert/list', method: 'get', params: cleanParams(params)})
}

export function getExpertDetail(id) {
    return request({url: `/medical/expert/${id}`, method: 'get'})
}

export function addExpert(data) {
    return request({url: '/medical/expert', method: 'post', data})
}

export function updateExpert(id, data) {
    return request({url: `/medical/expert/${id}`, method: 'put', data})
}

export function deleteExpert(id) {
    return request({url: `/medical/expert/${id}`, method: 'delete'})
}

export function updateExpertStatus(id, status) {
    return request({url: `/medical/expert/${id}/status`, method: 'put', data: {status}})
}

// ==================== 预约管理 ====================
export function getAppointmentList(params) {
    return request({url: '/medical/appointment/list', method: 'get', params: cleanParams(params)})
}

export function getAppointmentDetail(id) {
    return request({url: `/medical/appointment/${id}`, method: 'get'})
}

export function addAppointment(data) {
    return request({url: '/medical/appointment', method: 'post', data})
}

export function updateAppointment(id, data) {
    return request({url: `/medical/appointment/${id}`, method: 'put', data})
}

export function deleteAppointment(id) {
    return request({url: `/medical/appointment/${id}`, method: 'delete'})
}

export function updateAppointmentStatus(id, status) {
    return request({url: `/medical/appointment/${id}/status`, method: 'put', data: {status}})
}

// ==================== 智能分诊 ====================
export function getTriageList(params) {
    return request({url: '/medical/triage/list', method: 'get', params: cleanParams(params)})
}

export function getTriageDetail(id) {
    return request({url: `/medical/triage/${id}`, method: 'get'})
}

export function updateTriageStatus(id, status) {
    return request({url: `/medical/triage/${id}/status`, method: 'put', data: {status}})
}

export function deleteTriage(id) {
    return request({url: `/medical/triage/${id}`, method: 'delete'})
}
