/**
 * 认证相关API工具
 * 提供用户登录、注册、验证码发送、密码重置等认证功能的接口
 */

import {
	request
} from './config.js';


/**
 * 获取本地存储的token
 * @returns {string} 返回token字符串，如果不存在则返回空字符串
 */
function getToken() {
	return uni.getStorageSync('token') || '';
}

/**
 * 用户登录
 * @param {string} username - 用户名
 * @param {string} password - 密码
 * @param {string} captcha - 图形验证码答案
 * @param {string} captchaKey - 验证码UUID
 * @returns {Promise<object>} 返回登录结果，包含token和用户信息
 * @description 用于用户登录认证，成功后返回token和用户基本信息
 */
async function login(username, password, captcha, captchaKey) {
	return request('/auth/login', {
		method: 'POST',
		data: {
			username,
			password,
			captcha,
			captchaKey
		}
	});
}

/**
 * 用户注册
 * @param {string} username - 用户名
 * @param {string} password - 密码
 * @param {string} phone - 手机号
 * @param {string} captcha 验证码答案
 * @param {string} captchaKey 验证码uuid
 * @param {byte} userType 用户类型（默认是患者，不可改）
 * @returns {Promise<object>} 返回注册结果
 * @description 用于新用户注册
 */
async function register(username, password, phone, captcha, captchaKey, userType) {
	return request('/auth/register', {
		method: 'POST',
		data: {
			username,
			password,
			phone,
			captcha,
			captchaKey,
			userType
		}
	});
}

/**
 * 发送验证码
 * @param {string} phone - 手机号
 * @returns {Promise<object>} 返回发送结果
 * @description 向指定手机号发送验证码，用于注册或重置密码
 */
async function sendVerificationCode(phone) {
	return request('/auth/send-code', {
		method: 'POST',
		data: {
			phone
		}
	});
}

/**
 * 重置密码
 * @param {string} phone - 手机号
 * @param {string} verificationCode - 验证码
 * @param {string} newPassword - 新密码
 * @returns {Promise<object>} 返回重置结果
 * @description 用于忘记密码场景，通过手机验证码重置密码
 */
async function resetPassword(phone, verificationCode, newPassword) {
	return request('/auth/reset-password', {
		method: 'POST',
		data: {
			phone,
			verification_code: verificationCode,
			new_password: newPassword
		}
	});
}

/**
 * 退出登录
 * @returns {Promise<object>} 返回退出结果
 * @description 用户退出登录，清除服务端session
 */
async function logout() {
	return request('/auth/logout', {
		method: 'POST'
	});
}

/**
 * 获取图形验证码
 * @returns {Promise<object>} 返回验证码数据，包含图片和UUID
 * @description 获取图形验证码，用于用户登录时的图形验证
 */
async function getCaptcha() {
	return request('/captcha', {
		method: 'GET'
	});
}

/**
 * 获取用户信息
 * @param {number} userId - 用户ID，不传则从本地存储读取
 * @returns {Promise<object>} 返回用户详细信息
 * @description 获取当前登录用户的详细信息，包括个人资料、权限等
 */
async function getUserInfo(userId) {
	const uid = userId || uni.getStorageSync('userId');
	if (!uid) throw new Error('未找到用户ID，请重新登录');
	return request(`/user/${uid}`);
}

export {
	request,
	getToken,
	login,
	register,
	sendVerificationCode,
	resetPassword,
	logout,
	getUserInfo,
	getCaptcha
};