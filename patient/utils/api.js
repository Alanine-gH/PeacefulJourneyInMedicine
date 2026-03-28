// API 工具文件 - 医路安心 AI 接口

import { BASE_URL as baseUrl, request } from './config.js';

/**
 * 将头像字段转换为后端代理下载 URL
 * 支持以下情况：
 * - 已是完整 http/https URL → 直接返回
 * - 对象名（如 avatars/xxx.jpg）→ 走后端 /common/download 接口
 * - 空值 → 返回默认头像
 */
function getAvatarUrl(avatarUrl) {
	if (!avatarUrl) return '/static/default-avatar.png';
	if (avatarUrl.startsWith('http://') || avatarUrl.startsWith('https://')) return avatarUrl;
	return baseUrl + '/common/download?name=' + encodeURIComponent(avatarUrl);
}

// 获取 token
function getToken() {
	return uni.getStorageSync('token') || '';
}

// 1. 同步聊天
async function sendChat(data) {
	return request('/ai/chat', {
		method: 'POST',
		data
	});
}

// 2. 流式聊天 - 微信小程序不支持 fetch/AbortController/ReadableStream
// 改为调用同步接口，前端模拟逐字打字机效果
async function sendChatStream(data, onChunk, onDone, onError) {
	const token = getToken();
	try {
		const response = await uni.request({
			url: baseUrl + '/ai/chat',
			method: 'POST',
			header: {
				'Content-Type': 'application/json',
				'Authorization': 'Bearer ' + token
			},
			data: data,
			timeout: 120000
		});

		if (response.statusCode === 401) {
			onError && onError('登录已过期，请重新登录');
			return;
		}
		if (response.statusCode !== 200) {
			onError && onError('请求失败: ' + response.statusCode);
			return;
		}

		const res = response.data;
		if (!res || res.code !== 200) {
			onError && onError(res?.msg || 'AI回复失败');
			return;
		}

		const fullText = res.data || '';
		if (!fullText) {
			onDone && onDone('');
			return;
		}

		// 模拟打字机效果：每15ms 推送一个字符
		const CHAR_DELAY = 15;
		let i = 0;
		const timer = setInterval(() => {
			if (i < fullText.length) {
				onChunk && onChunk(fullText[i]);
				i++;
			} else {
				clearInterval(timer);
				onDone && onDone(fullText);
			}
		}, CHAR_DELAY);

	} catch (error) {
		console.error('流式聊天失败:', error);
		onError && onError('网络连接出现问题，请稍后重试');
	}
}

// 3. 简单聊天
async function sendSimpleChat(userInput) {
	return request('/ai/simple', {
		method: 'POST',
		data: {
			userInput
		}
	});
}

// 4. 智能分诊
async function getTriage(symptoms) {
	return request('/ai/triage', {
		method: 'POST',
		data: {
			symptoms
		}
	});
}

// 5. 就诊建议
async function getAdvice(department, complaint) {
	return request('/ai/advice', {
		method: 'POST',
		data: {
			department,
			complaint
		}
	});
}

// 6. 创建分诊会话
async function startTriageSession(data) {
	return request('/ai/triage/session/start', {
		method: 'POST',
		data
	});
}

// 7. 同步发送消息到分诊会话
async function sendTriageMessage(sessionId, content) {
	return request(`/ai/triage/session/${sessionId}/message`, {
		method: 'POST',
		data: {
			content
		}
	});
}

// 8. 流式发送消息到分诊会话 - 微信小程序不支持 fetch，改为同步接口+打字机效果
async function sendTriageStream(sessionId, content, onChunk, onDone, onError) {
	const token = getToken();
	try {
		const response = await uni.request({
			url: baseUrl + `/ai/triage/session/${sessionId}/chat`,
			method: 'POST',
			header: {
				'Content-Type': 'application/json',
				'Authorization': 'Bearer ' + token
			},
			data: {
				content
			},
			timeout: 120000
		});

		if (response.statusCode === 401) {
			onError && onError('登录已过期，请重新登录');
			return;
		}
		if (response.statusCode !== 200) {
			onError && onError('请求失败: ' + response.statusCode);
			return;
		}

		const res = response.data;
		if (!res || res.code !== 200) {
			onError && onError(res?.msg || 'AI回复失败');
			return;
		}

		const fullText = res.data || '';
		if (!fullText) {
			onDone && onDone('');
			return;
		}

		// 模拟打字机效果
		const CHAR_DELAY = 15;
		let i = 0;
		const timer = setInterval(() => {
			if (i < fullText.length) {
				onChunk && onChunk(fullText[i]);
				i++;
			} else {
				clearInterval(timer);
				onDone && onDone(fullText);
			}
		}, CHAR_DELAY);

	} catch (error) {
		console.error('流式分诊会话失败:', error);
		onError && onError('网络连接出现问题，请稍后重试');
	}
}

// 9. 保存流式AI回复
async function saveTriageReply(sessionId, content) {
	return request(`/ai/triage/session/${sessionId}/save-reply`, {
		method: 'POST',
		data: {
			content
		}
	});
}

// 10. 结束分诊会话
async function finishTriageSession(sessionId) {
	return request(`/ai/triage/session/${sessionId}/finish`, {
		method: 'POST'
	});
}

// 11. 查询分诊会话详情
async function getTriageSession(sessionId) {
	return request(`/ai/triage/session/${sessionId}`);
}

// 导出所有 API 函数
export {
	request,
	getAvatarUrl,
	sendChat,
	sendChatStream,
	sendSimpleChat,
	getTriage,
	getAdvice,
	startTriageSession,
	sendTriageMessage,
	sendTriageStream,
	saveTriageReply,
	finishTriageSession,
	getTriageSession
};