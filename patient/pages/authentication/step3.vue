<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-icon">←</text>-->
<!--      </view>-->
      <!-- <view class="title">实名认证</view> -->
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
          <text class="label">身份证号：</text>
          <text class="value">{{ maskedIdCard }}</text>
        </view>
      </view>

      <!-- 验证码区域 -->
      <view class="verification-section">
        <view class="section-title">验证码</view>
        
        <!-- 手机号输入框 -->
        <view class="phone-input-area">
          <input type="tel" placeholder="请输入手机号码" v-model="phone" class="phone-input" />
        </view>
        
        <view class="code-input-area">
          <input type="number" placeholder="请输入验证码" v-model="verificationCode" class="code-input" />
          <button class="send-code-btn" :disabled="countdown > 0" @click="sendCode">
            {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
          </button>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-btn">
      <button class="complete-btn" @click="completeAuth">验证完成</button>
    </view>
  </view>
</template>

<script>
import { sendVerificationCode } from '../../utils/auth';

export default {
  data() {
    return {
      authForm: {},
      phone: '',
      verificationCode: '',
      countdown: 0,
      timer: null
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
    // 获取之前保存的表单数据
    this.authForm = uni.getStorageSync('authForm') || {}
  },
  onUnload() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
    async sendCode() {
      if (!this.phone) {
        uni.showToast({ title: '请先填写手机号码', icon: 'none' })
        return
      }

      try {
        const response = await sendVerificationCode(this.phone);
        if (response.code === 200) {
          // 开始倒计时
          this.countdown = 60
          this.timer = setInterval(() => {
            this.countdown--
            if (this.countdown <= 0) {
              clearInterval(this.timer)
            }
          }, 1000)
          
          uni.showToast({ title: '验证码已发送', icon: 'success' })
        } else {
          uni.showToast({
            title: response.msg || '发送失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('发送验证码失败:', error);
        uni.showToast({
          title: '发送失败，请稍后重试',
          icon: 'none'
        })
      }
    },
    completeAuth() {
      if (!this.phone) {
        uni.showToast({ title: '请输入手机号码', icon: 'none' })
        return
      }
      if (!this.verificationCode) {
        uni.showToast({ title: '请输入验证码', icon: 'none' })
        return
      }

      // 预留：调用实名认证验证接口
      // TODO: 集成实名认证验证接口
      // uni.request({
      //   url: 'http://localhost:8080/api/auth/verify-identity',
      //   method: 'POST',
      //   data: {
      //     name: this.authForm.name,
      //     idCard: this.authForm.idCard,
      //     phone: this.phone,
      //     code: this.verificationCode
      //   },
      //   header: {
      //     'Authorization': 'Bearer ' + uni.getStorageSync('token')
      //   },
      //   success: (res) => {
      //     if (res.data.code === 200) {
      //       uni.showToast({
      //         title: '认证成功',
      //         icon: 'success',
      //         success: () => {
      //           setTimeout(() => {
      //             uni.navigateTo({
      //               url: '/pages/home/home'
      //             })
      //           }, 1500)
      //         }
      //       })
      //     }
      //   }
      // })

      // 模拟验证
      uni.showLoading({ title: '验证中...' })

      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '认证成功',
          icon: 'success',
          success: () => {
            setTimeout(() => {
              // 跳转到首页
              uni.navigateTo({
                url: '/pages/home/home'
              })
            }, 1500)
          }
        })
      }, 1500)
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 头部 */
.header {
  background-color: #fff;
  padding: 40rpx 30rpx 20rpx;
  border-bottom: 1rpx solid #e8e8e8;
}
/*
.back-btn {
  position: absolute;
  top: 40rpx;
  left: 30rpx;
  z-index: 1;
}

.back-icon {
  font-size: 36rpx;
  color: #333;
}
*/
.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  text-align: center;
  margin-bottom: 30rpx;
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
  background-color: #4DD0E1;
  color: #fff;
}

.step-line {
  width: 80rpx;
  height: 2rpx;
  background-color: #e8e8e8;
}

.step-line.active {
  background-color: #4DD0E1;
}

/* 内容 */
.content {
  padding: 30rpx;
}

/* 验证信息 */
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
  width: 120rpx;
}

.value {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  flex: 1;
}

/* 验证码区域 */
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

.code-input-area {
  display: flex;
  gap: 20rpx;
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
  margin-bottom: 20rpx;
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
  width: 200rpx;
  height: 80rpx;
  background-color: #f5f5f5;
  color: #333;
  font-size: 26rpx;
  border-radius: 10rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.send-code-btn:disabled {
  background-color: #e8e8e8;
  color: #999;
}

.send-code-btn::after {
  border: none;
}

/* 底部按钮 */
.bottom-btn {
  padding: 30rpx;
  background-color: #fff;
  border-top: 1rpx solid #e8e8e8;
  margin-top: 20rpx;
}

.complete-btn {
  width: 100%;
  height: 90rpx;
  background-color: #4DD0E1;
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.complete-btn::after {
  border: none;
}

.complete-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}
</style>