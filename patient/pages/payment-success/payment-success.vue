<template>
  <view class="success-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <text class="nav-title">支付完成</text>
      <view class="placeholder"></view>
    </view>

    <!-- 步骤指示器 -->
    <view class="step-indicator">
      <view class="step-item completed">
        <view class="step-num">✓</view>
        <text class="step-text">填写订单</text>
      </view>
      <view class="step-line active"></view>
      <view class="step-item completed">
        <view class="step-num">✓</view>
        <text class="step-text">支付订单</text>
      </view>
      <view class="step-line active"></view>
      <view class="step-item active">
        <view class="step-num">3</view>
        <text class="step-text">审核结果</text>
      </view>
      <view class="step-line"></view>
      <view class="step-item">
        <view class="step-num">4</view>
        <text class="step-text">服务完成</text>
      </view>
    </view>

    <!-- 成功图标区域 -->
    <view class="success-content">
      <view class="success-icon">
        <text class="check-mark">✓</text>
      </view>
      <text class="success-text">支付成功</text>
      <text class="success-subtext">{{ countdown }}秒后自动跳转...</text>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      countdown: 3,
      timer: null
    }
  },
  onShow() {
    this.startCountdown()
  },
  onHide() {
    this.clearTimer()
  },
  onUnload() {
    this.clearTimer()
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    startCountdown() {
      this.countdown = 3
      this.timer = setInterval(() => {
        this.countdown--
        if (this.countdown <= 0) {
          this.clearTimer()
          this.redirectToResult()
        }
      }, 1000)
    },
    clearTimer() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    redirectToResult() {
      uni.redirectTo({
        url: '/pages/result/result'
      })
    }
  }
}
</script>

<style lang="scss">
.success-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 导航栏 */
.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0 30rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}

.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-arrow {
  font-size: 40rpx;
  color: #fff;
}

.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

/* 步骤指示器 */
.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx 20rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.step-num {
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.3);
  color: #fff;
  font-size: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10rpx;
}

.step-item.active .step-num {
  background-color: #fff;
  color: #4DD0E1;
}

.step-item.completed .step-num {
  background-color: #fff;
  color: #4DD0E1;
}

.step-text {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
}

.step-item.active .step-text {
  color: #fff;
}

.step-item.completed .step-text {
  color: #fff;
}

.step-line {
  width: 60rpx;
  height: 2rpx;
  background-color: rgba(255, 255, 255, 0.3);
  margin: 0 10rpx;
}

.step-line.active {
  background-color: #fff;
}

/* 成功内容区域 */
.success-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 200rpx;
  padding-bottom: 60rpx;
}

.success-icon {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #07c160 0%, #05a050 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40rpx;
}

.check-mark {
  font-size: 80rpx;
  color: #fff;
  font-weight: bold;
}

.success-text {
  font-size: 40rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
}

.success-subtext {
  font-size: 28rpx;
  color: #999;
}
</style>
