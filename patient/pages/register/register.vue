<template>
  <view class="container">
    <!-- Logo区域 -->
    <view class="logo-section">
      <view class="logo">
        <view class="logo-icon">
          <view class="heart">
            <view class="heart-inner"></view>
          </view>
        </view>
      </view>
      <text class="app-name">医路安心</text>
    </view>

    <!-- 注册表单 -->
    <form @submit="onRegister">
      <view class="form-section">
        <view class="input-group">
          <view class="input-item">
            <view class="input-icon user-icon"></view>
            <input
                type="text"
                placeholder="请输入用户名"
                v-model="username"
                class="input-field"
                name="username"
            />
          </view>
          <view class="input-item">
            <view class="input-icon lock-icon"></view>
            <input
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                v-model="password"
                class="input-field"
                name="password"
            />
            <view class="eye-icon" :class="showPassword ? 'eye-open' : 'eye-close'" @click="togglePassword">
              <text v-if="!showPassword" class="eye-text">👁️</text>
              <text v-else class="eye-text">👁️‍🗨️</text>
            </view>
          </view>
          <view class="input-item">
            <view class="input-icon lock-icon"></view>
            <input
                :type="showConfirmPassword ? 'text' : 'password'"
                placeholder="请确认密码"
                v-model="confirmPassword"
                class="input-field"
                name="confirmPassword"
            />
            <view class="eye-icon" :class="showConfirmPassword ? 'eye-open' : 'eye-close'"
                  @click="toggleConfirmPassword">
              <text v-if="!showConfirmPassword" class="eye-text">👁️</text>
              <text v-else class="eye-text">👁️‍🗨️</text>
            </view>
          </view>
          <view class="input-item">
            <view class="input-icon phone-icon"></view>
            <input
                type="number"
                placeholder="请输入手机号"
                v-model="phone"
                class="input-field"
                maxlength="11"
                name="phone"
            />
          </view>
          <view class="input-item captcha-item">
            <view class="input-item-inner">
              <view class="input-icon captcha-icon"></view>
              <input
                  type="text"
                  placeholder="请输入图形验证码"
                  v-model="captcha"
                  class="input-field"
                  name="captcha"
                  maxlength="4"
              />
            </view>
            <view class="captcha-box" @click="fetchCaptcha">
              <text v-if="captchaLoading" class="captcha-loading"></text>
              <image v-else-if="captchaImg" :src="'data:image/jpeg;base64,' + captchaImg" class="captcha-img"
                     mode="aspectFit"/>
              <text v-else class="captcha-placeholder">点击获取</text>
            </view>
          </view>

        </view>

        <!-- 注册按钮 -->
        <button class="register-btn" form-type="submit">立即注册</button>

        <!-- 已有账号 -->
        <view class="login-link-wrapper">
          <text class="login-text">已有账号，</text>
          <text class="login-link" @click="goToLogin">去登录</text>
        </view>
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
import {register, getCaptcha} from '../../utils/auth';

export default {
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: '',
      phone: '',
      captcha: '',
      showPassword: false,
      showConfirmPassword: false,
      agreed: false,
      captchaLoading: false,
      captchaImg: '',
      captchaKey: ''
    }
  },
  mounted() {
    this.fetchCaptcha()
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword
    },

    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword
    },

    toggleAgreement() {
      this.agreed = !this.agreed
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

    async onRegister() {
      const {username, password, confirmPassword, phone, agreed, captcha, captchaKey} = this

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

      if (password !== confirmPassword) {
        uni.showToast({
          title: '两次密码不一致',
          icon: 'none'
        })
        return
      }

      if (!phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }

      if (!/^1[3-9]\d{9}$/.test(phone)) {
        uni.showToast({
          title: '手机号格式不正确',
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

      if (!captcha) {
        uni.showToast({
          title: '请输入图形验证码',
          icon: 'none'
        })
        return
      }

      // 调用注册接口
      uni.showLoading({
        title: '注册中...'
      })

      try {
        const response = await register(username, password, phone, captcha, captchaKey);
        uni.hideLoading();

        if (response.code === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success',
            success: () => {
              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            }
          })
        } else {
          uni.showToast({
            title: response.msg || '注册失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading();
        console.error('注册失败:', error);
        uni.showToast({
          title: '注册失败，请稍后重试',
          icon: 'none'
        })
      }
    },

    goToLogin() {
      uni.navigateBack()
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
  margin-top: 60rpx;
  margin-bottom: 60rpx;
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
  margin-top: 20rpx;
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

.input-item-inner {
  display: flex;
  align-items: center;
  flex: 1;
}

.input-item.captcha-item {
  height: 140rpx;
  align-items: flex-start;
}

.captcha-box {
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

.phone-icon {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/></svg>');
}

.captcha-icon {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>');
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

/* 注册按钮 */
.register-btn {
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
  margin-top: 40rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.register-btn::after {
  border: none;
}

.register-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 登录链接 */
.login-link-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40rpx;
}

.login-text {
  font-size: 28rpx;
  color: #999;
}

.login-link {
  font-size: 28rpx;
  color: #4DD0E1;
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
  background-color: #4DD0E1;
  border-color: #4DD0E1;
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

.agreement-link {
  font-size: 24rpx;
  color: #4DD0E1;
}
</style>
