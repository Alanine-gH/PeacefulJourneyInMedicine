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

    <!-- 忘记密码表单 -->
    <form @submit="onConfirm">
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
              placeholder="请设置新密码" 
              v-model="password"
              class="input-field"
              name="password"
            />
            <view class="eye-icon" :class="showPassword ? 'eye-open' : 'eye-close'" @click.stop="togglePassword">
              <text v-if="!showPassword" class="eye-text">👁️</text>
              <text v-else class="eye-text">👁️‍🗨️</text>
            </view>
          </view>
          <view class="input-item">
            <view class="input-icon lock-icon"></view>
            <input 
              :type="showConfirmPassword ? 'text' : 'password'" 
              placeholder="请确认新密码" 
              v-model="confirmPassword"
              class="input-field"
              name="confirmPassword"
            />
            <view class="eye-icon" :class="showConfirmPassword ? 'eye-open' : 'eye-close'" @click.stop="toggleConfirmPassword">
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
              name="phone"
              maxlength="11"
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
              <image v-else-if="captchaImg" :src="'data:image/jpeg;base64,' + captchaImg" class="captcha-img" mode="aspectFit" />
              <text v-else class="captcha-placeholder">点击获取</text>
            </view>
          </view>

        </view>

        <!-- 确认按钮 -->
        <button class="confirm-btn" form-type="submit">确认</button>
      </view>
    </form>
  </view>
</template>

<script>
import { getCaptcha, resetPassword } from '../../utils/auth';

export default {
  data() {
    return {
      username: '',
      phone: '',
      captcha: '',
      password: '',
      confirmPassword: '',
      showPassword: false,
      showConfirmPassword: false,
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

    async onConfirm() {
      const { username, phone, password, confirmPassword } = this

      if (!username) {
        uni.showToast({
          title: '请输入用户名',
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
          title: '请输入正确的手机号',
          icon: 'none'
        })
        return
      }

      if (!password) {
        uni.showToast({
          title: '请输入新密码',
          icon: 'none'
        })
        return
      }

      if (password.length < 8) {
        uni.showToast({
          title: '密码长度不能少于8位',
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

      if (!this.captcha) {
        uni.showToast({
          title: '请输入图形验证码',
          icon: 'none'
        })
        return
      }

      uni.showLoading({
        title: '重置中...'
      })

      try {
        const response = await resetPassword(username, phone, password, this.captcha, this.captchaKey);
        uni.hideLoading();

        if (response.code === 200) {
          uni.showToast({
            title: '密码重置成功',
            icon: 'success',
            success: () => {
              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            }
          })
        } else {
          uni.showToast({
            title: response.msg || '密码重置失败',
            icon: 'none'
          })
          // 重置失败时重新获取验证码
          this.fetchCaptcha();
        }
      } catch (error) {
        uni.hideLoading();
        console.error('密码重置失败:', error);
        uni.showToast({
          title: '密码重置失败，请稍后重试',
          icon: 'none'
        })
        // 网络错误时重新获取验证码
        this.fetchCaptcha();
      }
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
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(33, 150, 243, 0.3);
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
  margin-bottom: 60rpx;
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
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M20 15.5c-1.2 0-2.4-.2-3.6-.6-.3-.1-.7 0-1 .2l-2.2 2.2c-2.8-1.4-5.1-3.8-6.6-6.6l2.2-2.2c.3-.3.4-.7.2-1-.3-1.1-.5-2.3-.5-3.5 0-.6-.4-1-1-1H4c-.6 0-1 .4-1 1 0 9.4 7.6 17 17 17 .6 0 1-.4 1-1v-3.5c0-.6-.4-1-1-1zM19 12c-.6 0-1.1-.4-1.1-1s.5-1 1.1-1 1.1.4 1.1 1-.5 1-1.1 1z"/></svg>');
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

/* 确认按钮 */
.confirm-btn {
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

.confirm-btn::after {
  border: none;
}

.confirm-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}
</style>
