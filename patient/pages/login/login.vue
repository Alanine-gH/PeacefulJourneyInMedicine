<template>
	<view class="container">
		<!-- Logo区域 -->
		<view class="logo-section">
			<!-- <view v-if="mode === 'companion'" class="mode-banner">
				<text class="mode-banner-text">🩺 陪护端登录</text>
			</view> -->
			<view class="logo">
				<view class="logo-icon">
					<view class="heart">
						<view class="heart-inner"></view>
					</view>
				</view>
			</view>
			<text class="app-name">医路安心</text>
		</view>

		<!-- 登录表单 -->
		<form @submit="onLogin">
			<view class="form-section">
				<view class="input-group">
					<view class="input-item">
						<view class="input-icon user-icon"></view>
						<input type="text" placeholder="请输入用户名" v-model="username" class="input-field"
							name="username" />
					</view>
					<view class="input-item">
						<view class="input-icon lock-icon"></view>
						<input :type="showPassword?'text':'password'" placeholder="请输入密码" v-model="password"
							class="input-field" name="password" />
						<view class="eye-icon" :class="showPassword ? 'eye-open' : 'eye-close'" @click="togglePassword">
							<text v-if="!showPassword" class="eye-text">👁️</text>
							<text v-else class="eye-text">👁️‍🗨️</text>
						</view>
					</view>

					<!-- 图形验证码 -->
					<view class="input-item captcha-item">
						<view class="input-item-inner">
							<view class="input-icon captcha-icon"></view>
							<input type="text" placeholder="请输入验证码" v-model="captcha" class="input-field" name="captcha"
								maxlength="4" />
						</view>
						<view class="captcha-box" @click="fetchCaptcha">
							<text v-if="captchaLoading" class="captcha-loading"></text>
							<image v-else-if="captchaImg" :src="'data:image/jpeg;base64,' + captchaImg"
								class="captcha-img" mode="aspectFit" />
							<text v-else class="captcha-placeholder">点击获取</text>
						</view>
					</view>

					<!-- 语言选择 -->
					<view class="language-selector">
						<view class="language-label">语言 / Language</view>
						<view class="language-options">
							<view class="language-option" :class="selectedLanguage === 'zh' ? 'active' : ''"
								@click="selectLanguage('zh')">中文
							</view>
							<view class="language-option" :class="selectedLanguage === 'en' ? 'active' : ''"
								@click="selectLanguage('en')">English
							</view>
						</view>
					</view>
				</view>

				<!-- 辅助链接 -->
				<view class="helper-links">
					<text class="link-text" @click="goToForgotPassword">忘记密码</text>
					<text class="link-text register" @click="goToRegister">注册新用户</text>
				</view>

				<!-- 登录按钮 -->
				<button class="login-btn" form-type="submit">登录</button>
			</view>
		</form>

		<!-- 底部协议 -->
		<view class="agreement-section">
			<view class="checkbox-wrapper" @click="toggleAgreement">
				<view class="checkbox" :class="agreed ? 'checked' : ''"></view>
				<text class="agreement-text">我已阅读并同意</text>
			</view>
			<text class="agreement-link" @click="viewUserAgreement">《用户协议》</text>
			<text class="agreement-text">和</text>
			<text class="agreement-link" @click="viewServiceAgreement">《服务协议》</text>
		</view>
	</view>
</template>

<script>
	import {
		login,
		getCaptcha
	} from '../../utils/auth';

	export default {
		data() {
			return {
				username: '',
				password: '',
				captcha: '',
				captchaImg: '',
				captchaKey: '',
				captchaLoading: false,
				showPassword: false,
				agreed: false,
				selectedLanguage: 'zh',
				mode: 'patient' // 'patient' | 'companion'
			}
		},
		onLoad(options) {
			this.mode = options.mode || 'patient'
			this.fetchCaptcha()
		},
		methods: {
			togglePassword() {
				this.showPassword = !this.showPassword
			},

			async fetchCaptcha() {
				this.captchaLoading = true
				this.captchaImg = ''
				this.captcha = ''
				try {
					const res = await getCaptcha()
					const d = (res && res.data) ? res.data : res
					this.captchaKey = d.uuid || ''
					this.captchaImg = d.img || ''
				} catch (e) {
					this.captchaImg = ''
				} finally {
					this.captchaLoading = false
				}
			},

			toggleAgreement() {
				this.agreed = !this.agreed
			},

			selectLanguage(lang) {
				this.selectedLanguage = lang
			},

			async onLogin() {
				const {
					username,
					password,
					captcha,
					captchaKey,
					agreed
				} = this

				if (!username) {
					uni.showToast({
						title: '请输入用户名',
						icon: 'none'
					})
					return
				}

				if (!password) {
					uni.showToast({
						title: '请输入密码',
						icon: 'none'
					})
					return
				}

				if (!captcha) {
					uni.showToast({
						title: '请输入验证码',
						icon: 'none'
					})
					return
				}

				if (!agreed) {
					uni.showToast({
						title: '请同意用户协议',
						icon: 'none'
					})
					return
				}

				// 真实登录
				uni.showLoading({
					title: '登录中...'
				})

				try {
					const response = await login(username, password, captcha, captchaKey);
					uni.hideLoading();

					if (response.code === 200) {
						const data = response.data
						const userType = data.userType // 1=患者 2=陪诊师 3=客服 4=管理员

						// 仅支持患者和陪诊师在小程序端登录
						if (userType !== 1 && userType !== 2) {
							this.fetchCaptcha()
							uni.showToast({
								title: '该账号身份暂不支持在此端登录',
								icon: 'none',
								duration: 3000
							})
							return
						}

						// 保存登录信息
						uni.setStorageSync('token', data.token)
						uni.setStorageSync('userId', data.userId)
						uni.setStorageSync('userType', userType)
						uni.setStorageSync('userInfo', data)
						if (userType === 2) {
							uni.setStorageSync('accompanistId', data.userId)
						}

						uni.showToast({
							title: '登录成功',
							icon: 'success',
							success: () => {
								setTimeout(() => {
									if (userType === 2) {
										uni.reLaunch({
											url: '/pages/companion/home'
										})
									} else {
										uni.switchTab({
											url: '/pages/home/home'
										})
									}
								}, 1000)
							}
						})
					} else {
						this.fetchCaptcha()
						uni.showToast({
							title: response.msg || response.message || '登录失败',
							icon: 'none'
						})
					}
				} catch (error) {
					uni.hideLoading();
					console.error('登录失败:', error);
					this.fetchCaptcha();
					uni.showToast({
						title: '登录失败，请稍后重试',
						icon: 'none'
					})
				}
			},

			goToForgotPassword() {
				uni.navigateTo({
					url: '/pages/forgot-password/forgot-password'
				})
			},

			goToRegister() {
				uni.navigateTo({
					url: '/pages/register/register'
				})
			},

			viewUserAgreement() {
				uni.showModal({
					title: '用户协议',
					content: '这里是用户协议内容...',
					showCancel: false
				})
			},

			viewServiceAgreement() {
				uni.showModal({
					title: '服务协议',
					content: '这里是服务协议内容...',
					showCancel: false
				})
			}
		}
	}
</script>

<style scoped>
	.container {
		background-color: #fff;
		min-height: 100vh;
		padding: 60rpx 50rpx;
		box-sizing: border-box;
	}

	/* Logo区域 */
	.logo-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-top: 80rpx;
		margin-bottom: 80rpx;
	}

	.logo {
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 30rpx;
		box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.3);
	}

	.logo-icon {
		width: 60rpx;
		height: 60rpx;
		position: relative;
	}

	.heart {
		width: 100%;
		height: 100%;
		position: relative;
	}

	.heart::before,
	.heart::after {
		content: '';
		position: absolute;
		width: 30rpx;
		height: 48rpx;
		background: #fff;
		border-radius: 30rpx 30rpx 0 0;
		top: 6rpx;
	}

	.heart::before {
		left: 6rpx;
		transform: rotate(-45deg);
	}

	.heart::after {
		right: 6rpx;
		transform: rotate(45deg);
	}

	.heart-inner {
		position: absolute;
		width: 16rpx;
		height: 16rpx;
		background: #fff;
		border-radius: 50%;
		top: 20rpx;
		left: 50%;
		transform: translateX(-50%);
	}

	.app-name {
		font-size: 36rpx;
		font-weight: 600;
		color: #333;
	}

	/* 表单区域 */
	.form-section {
		margin-top: 40rpx;
	}

	.input-group {
		margin-bottom: 30rpx;
	}

	.input-item {
		display: flex;
		align-items: center;
		height: 100rpx;
		border-bottom: 2rpx solid #e8e8e8;
		margin-bottom: 20rpx;
	}

	.input-icon {
		width: 40rpx;
		height: 40rpx;
		margin-right: 20rpx;
		background-size: contain;
		background-repeat: no-repeat;
		background-position: center;
	}

	.user-icon {
		background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>');
	}

	.lock-icon {
		background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/></svg>');
	}

	.input-field {
		flex: 1;
		font-size: 30rpx;
		color: #333;
	}

	.eye-icon {
		width: 40rpx;
		height: 40rpx;
		padding: 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}

	.eye-text {
		font-size: 32rpx;
		line-height: 1;
	}

	.eye-close {
		opacity: 0.6;
	}

	.eye-open {
		opacity: 1;
	}

	/* 验证码 */
	.input-item-inner {
		margin: auto;
		display: flex;
		align-items: center;
		flex: 1;
	}

	.input-item.captcha-item {
		height: 140rpx;
		align-items: flex-start;
	}

	.captcha-box {
		margin: auto;
		width: 200rpx;
		height: 80rpx;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		background: rgba(77, 208, 225, 0.1);
		border: 2rpx solid rgba(77, 208, 225, 0.3);
		border-radius: 10rpx;
		overflow: hidden;
	}

	.captcha-box:active {
		background: rgba(77, 208, 225, 0.2);
		border-color: rgba(77, 208, 225, 0.5);
	}

	.captcha-img {
		width: 100%;
		height: 100%;
		object-fit: cover;
		border-radius: 8rpx;
	}

	.captcha-placeholder {
		font-size: 22rpx;
		color: rgba(77, 208, 225, 0.6);
	}

	.captcha-loading {
		width: 32rpx;
		height: 32rpx;
		border: 3rpx solid rgba(77, 208, 225, 0.3);
		border-top-color: #4DD0E1;
		border-radius: 50%;
		animation: spin 0.7s linear infinite;
	}

	@keyframes spin {
		to {
			transform: rotate(360deg);
		}
	}

	.captcha-icon {
		background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>');
	}

	/* 语言选择 */
	.language-selector {
		margin-top: 30rpx;
		margin-bottom: 20rpx;
	}

	.language-label {
		font-size: 26rpx;
		color: #666;
		margin-bottom: 15rpx;
	}

	.language-options {
		display: flex;
		gap: 20rpx;
	}

	.language-option {
		flex: 1;
		height: 80rpx;
		border: 2rpx solid #e8e8e8;
		border-radius: 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		color: #333;
		transition: all 0.3s ease;
	}

	.language-option.active {
		border-color: #4DD0E1;
		background-color: rgba(77, 208, 225, 0.05);
		color: #4DD0E1;
	}

	.language-option:active {
		opacity: 0.8;
		transform: scale(0.98);
	}

	/* 辅助链接 */
	.helper-links {
		display: flex;
		justify-content: space-between;
		margin-bottom: 60rpx;
		margin-top: 20rpx;
	}

	.link-text {
		font-size: 28rpx;
		color: #4DD0E1;
	}

	.link-text.register {
		color: #4DD0E1;
	}

	/* 登录按钮 */
	.login-btn {
		width: 100%;
		height: 90rpx;
		background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
		color: #fff;
		font-size: 32rpx;
		font-weight: 500;
		border-radius: 45rpx;
		border: none;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
	}

	.login-btn::after {
		border: none;
	}

	.login-btn:active {
		opacity: 0.9;
		transform: scale(0.98);
	}

	/* 底部协议 */
	.agreement-section {
		position: fixed;
		bottom: 60rpx;
		left: 0;
		right: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-wrap: wrap;
		padding: 0 50rpx;
	}

	.checkbox-wrapper {
		display: flex;
		align-items: center;
		margin-right: 10rpx;
	}

	.checkbox {
		width: 28rpx;
		height: 28rpx;
		border: 2rpx solid #ccc;
		border-radius: 50%;
		margin-right: 10rpx;
		position: relative;
	}

	.checkbox.checked {
		background-color: #2196F3;
		border-color: #2196F3;
	}

	.checkbox.checked::after {
		content: '';
		position: absolute;
		width: 12rpx;
		height: 6rpx;
		border-left: 3rpx solid #fff;
		border-bottom: 3rpx solid #fff;
		transform: rotate(-45deg);
		top: 6rpx;
		left: 6rpx;
	}

	.agreement-text {
		font-size: 24rpx;
		color: #999;
	}

	.mode-banner {
		background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
		border-radius: 20rpx;
		padding: 16rpx 40rpx;
		margin-bottom: 30rpx;
		text-align: center;
	}

	.mode-banner-text {
		font-size: 28rpx;
		color: #fff;
		font-weight: 600;
		letter-spacing: 2rpx;
	}
</style>