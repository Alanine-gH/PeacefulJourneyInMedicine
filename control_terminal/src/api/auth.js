import axios from 'axios'
import { BASE_URL, TIMEOUT } from '@/config'

const request = axios.create({
    baseURL: BASE_URL,
    timeout: TIMEOUT,
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token
        }
        return config
    },
    error => Promise.reject(error)
)

// 响应拦截器
request.interceptors.response.use(
    response => response.data,
    error => Promise.reject(error)
)

/**
 * 获取图形验证码
 * 后端返回: { code:1, data: { uuid, img, captchaEnabled } }
 */
export function getCaptcha() {
    return request.get('/captcha')
}

/**
 * 登录
 * @param {object} data - { username, password, captcha, captchaKey(uuid) }
 */
export function login(data) {
    return request.post('/auth/login', data)
}

/**
 * 注册
 * @param {object} data - { username, password, email, captcha, captchaKey(uuid) }
 */
export function register(data) {
    return request.post('/auth/register', data)
}

/**
 * 退出登录
 */
export function logout() {
    return request.post('/auth/logout')
}

export default request
