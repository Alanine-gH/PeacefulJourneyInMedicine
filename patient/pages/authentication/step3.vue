<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="step-indicator">
        <view class="step active">1</view>
        <view class="step-line active"></view>
        <view class="step active">2</view>
        <view class="step-line active"></view>
        <view class="step active">3</view>
      </view>
    </view>

    <!-- 内容 -->
    <view class="content">
      <!-- 验证信息 -->
      <view class="verification-info">
        <view class="info-item">
          <text class="label">您的姓名：</text>
          <text class="value">{{ authForm.name }}</text>
        </view>
        <view class="info-item">
          <text class="label">{{ authForm.isForeigner ? '护照号：' : '身份证号：' }}</text>
          <text class="value">{{ maskedIdCard }}</text>
        </view>
      </view>

      <!-- 验证码区域 -->
      <view class="verification-section">
        <view class="section-title">手机号验证</view>

        <!-- 手机号输入框（预填 step1 的手机号，允许修改） -->
        <view class="phone-input-area">
          <input type="tel" placeholder="请输入手机号码" v-model="phone" class="phone-input" maxlength="11" />
        </view>

        <view class="code-input-area">
          <input type="number" placeholder="请输入验证码" v-model="verificationCode" class="code-input" maxlength="6" />
          <button class="send-code-btn" :disabled="countdown > 0 || sending" @click="sendCode">
            {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
          </button>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-btn">
      <button class="complete-btn" :disabled="submitting" @click="completeAuth">
        {{ submitting ? '提交中...' : '验证完成' }}
      </button>
    </view>
  </view>
</template>

<script>
import { sendVerificationCode } from '@/utils/auth.js'
import { submitIdentityAuth } from '@/utils/auth-api.js'

export default {
  data() {
    return {
      authForm: {},
      authImages: {},
      phone: '',
      verificationCode: '',
      countdown: 0,
      timer: null,
      sending: false,
      submitting: false
    }
  },
  computed: {
    maskedIdCard() {
      if (!this.authForm.idCard) return ''
      const idCard = this.authForm.idCard
      return idCard.substring(0, 3) + '***********' + idCard.substring(idCard.length - 4)
    }
  },
  onLoad() {
    this.authForm = uni.getStorageSync('authForm') || {}
    this.authImages = uni.getStorageSync('authImages') || {}
    // 预填 step1 输入的手机号
    if (this.authForm.phone) {
      this.phone = this.authForm.phone
    }
  },
  onUnload() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    async sendCode() {
      if (!this.phone || this.phone.length !== 11) {
        uni.showToast({ title: '请输入正确的11位手机号', icon: 'none' })
        return
      }
      this.sending = true
      try {
        const res = await sendVerificationCode(this.phone)
        if (res && (res.code === 200 || res.code === 1)) {
          uni.showToast({ title: '验证码已发送', icon: 'success' })
          this.countdown = 60
          this.timer = setInterval(() => {
            this.countdown--
            if (this.countdown <= 0) clearInterval(this.timer)
          }, 1000)
        } else {
          uni.showToast({ title: (res && res.msg) || '发送失败', icon: 'none' })
        }
      } catch (e) {
        console.error('发送验证码失败:', e)
        uni.showToast({ title: '发送失败，请稍后重试', icon: 'none' })
      } finally {
        this.sending = false
      }
    },

    async completeAuth() {
      if (!this.phone || this.phone.length !== 11) {
        uni.showToast({ title: '请输入正确的手机号码', icon: 'none' })
        return
      }
      if (!this.verificationCode) {
        uni.showToast({ title: '请输入验证码', icon: 'none' })
        return
      }

      const userId = uni.getStorageSync('userId')
      if (!userId) {
        uni.showToast({ title: '登录已过期，请重新登录', icon: 'none' })
        return
      }

      // 判断认证类型：身份证(2) 或 护照(1)
      const isForeigner = this.authForm.isForeigner
      const authType = isForeigner ? 1 : 2

      const submitData = {
        userId: userId,
        authType: authType,
        realName: this.authForm.name,
        phone: this.phone,
        verificationCode: this.verificationCode,
        // step1 数据
        gender: this.authForm.gender,
        birthDate: this.authForm.birthDate,
        address: this.authForm.address,
        bankCard: this.authForm.bankCard,
        bankType: this.authForm.bankType || '',
        emergencyContact: this.authForm.emergencyContact,
        emergencyPhone: this.authForm.emergencyPhone,
        // step2 图片
        photoFront: this.authImages.front,
        photoBack: this.authImages.back,
        // 证件号
        idCardNumber: isForeigner ? '' : this.authForm.idCard,
        passportNumber: isForeigner ? this.authForm.idCard : ''
      }

      this.submitting = true
      uni.showLoading({ title: '提交中...' })
      try {
        const res = await submitIdentityAuth(submitData)
        uni.hideLoading()
        if (res && (res.code === 200 || res.code === 1)) {
          // 清理 Storage
          uni.removeStorageSync('authForm')
          uni.removeStorageSync('authImages')
          uni.showToast({
            title: '认证提交成功，等待审核',
            icon: 'success',
            duration: 2000
          })
          setTimeout(() => {
            uni.navigateTo({ url: '/pages/home/home' })
          }, 2000)
        } else {
          uni.showToast({ title: (res && res.msg) || '提交失败，请重试', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('提交认证失败:', e)
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

.header {
  background-color: #fff;
  padding: 40rpx 30rpx 20rpx;
  border-bottom: 1rpx solid #e8e8e8;
}

.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20rpx;
}

.step {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background-color: #e8e8e8;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 500;
}

.step.active {
  background-color: #8db8b6;
  color: #fff;
}

.step-line {
  width: 80rpx;
  height: 2rpx;
  background-color: #e8e8e8;
}

.step-line.active {
  background-color: #8db8b6;
}

.content {
  padding: 30rpx;
}

.verification-info {
  background-color: #fff;
  border-radius: 15rpx;
  padding: 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.info-item:last-child {
  margin-bottom: 0;
}

.label {
  font-size: 28rpx;
  color: #666;
  width: 160rpx;
}

.value {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  flex: 1;
}

.verification-section {
  background-color: #fff;
  border-radius: 15rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
}

.phone-input-area {
  margin-bottom: 20rpx;
}

.phone-input {
  width: 100%;
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.code-input-area {
  display: flex;
  gap: 20rpx;
}

.code-input {
  flex: 1;
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.send-code-btn {
  width: 220rpx;
  height: 80rpx;
  background-color: #8db8b6;
  color: #fff;
  font-size: 26rpx;
  border-radius: 10rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.send-code-btn[disabled] {
  background-color: #e8e8e8;
  color: #999;
}

.send-code-btn::after {
  border: none;
}

.bottom-btn {
  padding: 30rpx;
  background-color: #fff;
  border-top: 1rpx solid #e8e8e8;
  margin-top: 20rpx;
}

.complete-btn {
  width: 100%;
  height: 90rpx;
  background-color: #8db8b6;
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.complete-btn[disabled] {
  background-color: #b0bec5;
}

.complete-btn::after {
  border: none;
}

/* ── Shared theme overrides ── */
page { background-color: #f4f2ee !important; }

.container { background-color: #f4f2ee !important; min-height: 100vh; }

/* Cards */
.patient-section,
.carousel-section,
.location-section,
.training-section,
.health-section,
.order-section,
.menu-section,
.profile-section,
.stats-section,
.orders-section,
.info-section,
.order-info,
.payment-methods,
.amount-section,
.step-indicator,
.filter-bar,
.package-section {
  background: #ffffff;
  border-radius: 20rpx !important;
  box-shadow: 0 4rpx 20rpx rgba(100,120,140,0.10) !important;
  margin: 16rpx 0 !important;
}

/* Icon squares */
.patient-icon,
.health-icon,
.stat-box-icon {
  background: linear-gradient(135deg, #c2dada 0%, #a8cece 100%) !important;
  border-radius: 16rpx !important;
  box-shadow: none !important;
}

/* Action buttons / primary CTAs */
.action-btn,
.login-btn,
.switch-btn,
.submit-btn,
.pay-btn,
.confirm-btn {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important;
  border-radius: 40rpx !important;
  box-shadow: 0 4rpx 16rpx rgba(100,175,175,0.28) !important;
  border: none !important;
}

/* Filter active pill */
.filter-item.active {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important;
}

/* Order left border accent */
.order-item {
  border-left: 5rpx solid #8db8b6 !important;
  border-radius: 16rpx !important;
  background: #f8f7f4 !important;
}

/* Status tags */
.order-status { color: #8db8b6 !important; }

/* Section titles */
.section-title { color: #3a3a4a !important; font-weight: 600 !important; }

/* Stat items */
.stat-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

/* Tab bar selected */
.tab-item.active,
.tab-item.active .tab-text { color: #8db8b6 !important; }

/* Profile avatar ring */
.profile-avatar {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 12rpx rgba(100,175,175,0.28) !important;
}

/* Level tag */
.level-tag {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important;
}

/* Loading spinner */
.loading-spinner {
  border-top-color: #8db8b6 !important;
}

/* Carousel items - remap class colors to softer palette */
.carousel-item        { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }
.carousel-item.vip    { background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important; }
.carousel-item.full   { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }
.carousel-item.consult{ background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important; }
.carousel-item.result { background: linear-gradient(135deg, #d4a8b0 0%, #c09098 100%) !important; }
.carousel-item.medicine{ background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%) !important; }
.carousel-item.basic  { background: linear-gradient(135deg, #a8cec8 0%, #8db8b0 100%) !important; }

/* Step indicator */
.step-num { border-color: #8db8b6 !important; color: #8db8b6 !important; }
.step-item.active .step-num,
.step-item.completed .step-num {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important; border-color: transparent !important;
}
.step-line.active { background-color: #8db8b6 !important; }

/* Health exam header */
.header { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }

/* Package type tags */
.pkg-type-tag { background: #e8f4f4 !important; }
.pkg-type-tag .type-text { color: #6a9ea0 !important; }

/* Input focus ring */
.input-item:focus-within { border-color: #8db8b6 !important; box-shadow: 0 0 0 3rpx rgba(141,184,182,0.18) !important; }

</style>