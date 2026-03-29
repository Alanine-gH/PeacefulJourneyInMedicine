<template>
  <view class="result-page">
    <!-- 导航栏 -->
    <!-- <view class="nav-bar"> -->
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
     <!-- <text class="nav-title">预约结果</text>
      <view class="placeholder"></view>
    </view> -->

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

    <!-- 审核状态 -->
    <view class="status-container">
      <view class="status-icon">
        <text class="icon">⏱</text>
      </view>
      <text class="status-title">审核中</text>
      <text class="status-desc">已提交申请，审核结果将以短信方式发送给您</text>
      <text class="status-desc">请保持注册提交的手机号畅通，耐心等待1-3个工作日。</text>

      <!-- 提示卡片 -->
      <view class="tip-card">
        <view class="tip-item">
          <text class="tip-icon">📱</text>
          <text class="tip-text">审核结果将通过短信通知</text>
        </view>
        <view class="tip-item">
          <text class="tip-icon">⏰</text>
          <text class="tip-text">审核时间：1-3个工作日</text>
        </view>
        <view class="tip-item">
          <text class="tip-icon">📞</text>
          <text class="tip-text">如有疑问，请联系客服</text>
        </view>
      </view>
    </view>

    <!-- 返回按钮 -->
    <view class="bottom-section">
      <button class="return-btn" @click="goHome">返回首页</button>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
    goHome() {
      uni.switchTab({
        url: '/pages/home/home'
      })
    }
  }
}
</script>

<style lang="scss">
.result-page {
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
/*
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
*/
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

/* 状态容器 */
.status-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60rpx 30rpx;
}

.status-icon {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
  animation: iconPulse 2s ease-in-out infinite;
}

@keyframes iconPulse {
  0% {
    box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
    transform: scale(1);
  }
  50% {
    box-shadow: 0 12rpx 32rpx rgba(77, 208, 225, 0.6);
    transform: scale(1.05);
  }
  100% {
    box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
    transform: scale(1);
  }
}

.icon {
  font-size: 70rpx;
  color: white;
}

.status-title {
  font-size: 40rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 30rpx;
}

.status-desc {
  font-size: 28rpx;
  color: #666;
  text-align: center;
  line-height: 1.6;
  margin-bottom: 15rpx;
}

/* 提示卡片 */
.tip-card {
  background-color: white;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-top: 40rpx;
  width: 100%;
  max-width: 600rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.tip-item {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.tip-item:last-child {
  margin-bottom: 0;
}

.tip-icon {
  font-size: 32rpx;
  margin-right: 20rpx;
  width: 40rpx;
  text-align: center;
}

.tip-text {
  font-size: 26rpx;
  color: #333;
  line-height: 1.4;
  flex: 1;
}

/* 底部按钮 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.return-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.return-btn::after {
  border: none;
}

.return-btn:active {
  opacity: 0.9;
}
</style>
