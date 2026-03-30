<template>
  <view class="success-page">
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
  background-color: #f4f2ee;
}

/* 步骤指示器 */
.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx 20rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
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
  color: #8db8b6;
}

.step-item.completed .step-num {
  background-color: #fff;
  color: #8db8b6;
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