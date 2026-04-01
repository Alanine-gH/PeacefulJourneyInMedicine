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

// 2. 流式聊天 - 使用微信小程序 enableChunked 接收 SSE 分块推送
// 后端接口：POST /ai/chat/stream，事件格式：event: message/done/error
async function sendChatStream(data, onChunk, onDone, onError) {
	const token = getToken();

	// #ifdef MP-WEIXIN
	// 微信小程序：用 enableChunked 模式接收 SSE
	let fullReply = '';
	let buffer = '';
	let isDone = false;

	const task = wx.request({
		url: baseUrl + '/ai/chat/stream',
		method: 'POST',
		header: {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer ' + token,
			'Accept': 'text/event-stream'
		},
		data: JSON.stringify(data),
		enableChunked: true,
		timeout: 120000,
		success() {},
		fail(err) {
			if (!isDone) {
				onError && onError('网络连接出现问题，请稍后重试');
			}
		}
	});

	task.onChunkReceived((res) => {
		try {
			// ArrayBuffer → UTF-8 string
			const decoder = new TextDecoder('utf-8');
			const chunk = decoder.decode(new Uint8Array(res.data));
			buffer += chunk;
			// 按行解析 SSE
			const lines = buffer.split('\n');
			buffer = lines.pop(); // 最后一行可能不完整，留到下次
			let currentEvent = 'message';
			for (const line of lines) {
				if (line.startsWith('event:')) {
					currentEvent = line.slice(6).trim();
				} else if (line.startsWith('data:')) {
					const payload = line.slice(5).trim();
					if (currentEvent === 'message') {
						fullReply += payload;
						onChunk && onChunk(payload);
					} else if (currentEvent === 'done') {
						isDone = true;
						onDone && onDone(payload || fullReply);
					} else if (currentEvent === 'error') {
						isDone = true;
						onError && onError(payload || 'AI回复失败');
					}
					// 每条 data 后重置 event
					currentEvent = 'message';
				}
			}
		} catch (e) {
			console.error('[SSE] chunk 解析失败:', e);
		}
	});

	task.onHeadersReceived((res) => {
		if (res.statusCode === 401) {
			isDone = true;
			task.abort();
			onError && onError('登录已过期，请重新登录');
		} else if (res.statusCode !== 200) {
			isDone = true;
			task.abort();
			onError && onError('请求失败: ' + res.statusCode);
		}
	});
	// #endif

	// #ifndef MP-WEIXIN
	// 非小程序环境（H5/App）：同步接口 + 打字机效果兜底
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
		if (response.statusCode === 401) { onError && onError('登录已过期，请重新登录'); return; }
		if (response.statusCode !== 200) { onError && onError('请求失败: ' + response.statusCode); return; }
		const res = response.data;
		if (!res || res.code !== 200) { onError && onError(res?.msg || 'AI回复失败'); return; }
		const fullText = res.data || '';
		if (!fullText) { onDone && onDone(''); return; }
		const CHAR_DELAY = 15;
		let i = 0;
		const timer = setInterval(() => {
			if (i < fullText.length) { onChunk && onChunk(fullText[i]); i++; }
			else { clearInterval(timer); onDone && onDone(fullText); }
		}, CHAR_DELAY);
	} catch (error) {
		console.error('流式聊天失败:', error);
		onError && onError('网络连接出现问题，请稍后重试');
	}
	// #endif
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

// 8. 流式发送消息到分诊会话 - 使用微信小程序 enableChunked 接收 SSE 分块推送
// 后端接口：POST /ai/triage/session/{id}/chat/stream，事件格式：event: message/done/error
async function sendTriageStream(sessionId, content, onChunk, onDone, onError) {
	const token = getToken();

	// #ifdef MP-WEIXIN
	let fullReply = '';
	let buffer = '';
	let isDone = false;

	const task = wx.request({
		url: baseUrl + '/ai/triage/session/' + sessionId + '/stream',
		method: 'POST',
		header: {
			'Content-Type': 'application/json',
			'Authorization': 'Bearer ' + token,
			'Accept': 'text/event-stream'
		},
		data: JSON.stringify({ content }),
		enableChunked: true,
		timeout: 120000,
		success() {},
		fail(err) {
			if (!isDone) {
				onError && onError('网络连接出现问题，请稍后重试');
			}
		}
	});

	task.onChunkReceived((res) => {
		try {
			const chunk = String.fromCharCode.apply(null, new Uint8Array(res.data));
			buffer += chunk;
			const lines = buffer.split('\n');
			buffer = lines.pop();
			let currentEvent = 'message';
			for (const line of lines) {
				if (line.startsWith('event:')) {
					currentEvent = line.slice(6).trim();
				} else if (line.startsWith('data:')) {
					const payload = line.slice(5).trim();
					if (currentEvent === 'message') {
						fullReply += payload;
						onChunk && onChunk(payload);
					} else if (currentEvent === 'done') {
						isDone = true;
						onDone && onDone(payload || fullReply);
					} else if (currentEvent === 'error') {
						isDone = true;
						onError && onError(payload || 'AI回复失败');
					}
					currentEvent = 'message';
				}
			}
		} catch (e) {
			console.error('[SSE] triage chunk 解析失败:', e);
		}
	});

	task.onHeadersReceived((res) => {
		if (res.statusCode === 401) {
			isDone = true;
			task.abort();
			onError && onError('登录已过期，请重新登录');
		} else if (res.statusCode !== 200) {
			isDone = true;
			task.abort();
			onError && onError('请求失败: ' + res.statusCode);
		}
	});
	// #endif

	// #ifndef MP-WEIXIN
	// 非小程序环境兜底：同步接口 + 打字机效果
	try {
		const response = await uni.request({
			url: baseUrl + '/ai/triage/session/' + sessionId + '/chat',
			method: 'POST',
			header: {
				'Content-Type': 'application/json',
				'Authorization': 'Bearer ' + token
			},
			data: { content },
			timeout: 120000
		});
		if (response.statusCode === 401) { onError && onError('登录已过期，请重新登录'); return; }
		if (response.statusCode !== 200) { onError && onError('请求失败: ' + response.statusCode); return; }
		const res = response.data;
		if (!res || res.code !== 200) { onError && onError(res?.msg || 'AI回复失败'); return; }
		const fullText = res.data || '';
		if (!fullText) { onDone && onDone(''); return; }
		const CHAR_DELAY = 15;
		let i = 0;
		const timer = setInterval(() => {
			if (i < fullText.length) { onChunk && onChunk(fullText[i]); i++; }
			else { clearInterval(timer); onDone && onDone(fullText); }
		}, CHAR_DELAY);
	} catch (error) {
		console.error('流式分诊会话失败:', error);
		onError && onError('网络连接出现问题，请稍后重试');
	}
	// #endif
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

// 12. 获取分诊记录列表
async function getTriageRecords(params) {
	return request('/medical/triage/list', {
		method: 'GET',
		data: params
	});
}

// 13. 获取分诊记录详情
async function getTriageRecordDetail(id) {
	return request(`/medical/triage/${id}`, {
		method: 'GET'
	});
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
	getTriageSession,
	getTriageRecords,
	getTriageRecordDetail
};
