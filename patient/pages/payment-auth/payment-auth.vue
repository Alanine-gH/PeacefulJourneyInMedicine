<template>
  <view class="auth-page">
    <!-- 步骤指示器 -->
    <view class="step-indicator">
      <view class="step-item completed">
        <view class="step-num">✓</view>
        <text class="step-text">填写订单</text>
      </view>
      <view class="step-line active"></view>
      <view class="step-item active">
        <view class="step-num">2</view>
        <text class="step-text">支付订单</text>
      </view>
      <view class="step-line"></view>
      <view class="step-item">
        <view class="step-num">3</view>
        <text class="step-text">审核结果</text>
      </view>
      <view class="step-line"></view>
      <view class="step-item">
        <view class="step-num">4</view>
        <text class="step-text">服务完成</text>
      </view>
    </view>

    <!-- 中间内容区域 -->
    <view class="content-wrapper">
      <text class="auth-title">请输入支付密码</text>
      
      <!-- 密码框 -->
      <view class="password-box">
        <view class="password-item" v-for="(item, index) in 6" :key="index">
          <view class="password-dot" v-if="password.length > index"></view>
        </view>
      </view>
    </view>

    <!-- 安全输入键盘 - 固定在底部 -->
    <view class="keyboard-section">
      <text class="keyboard-title">安全输入键盘</text>
      <view class="keyboard">
        <view class="keyboard-row" v-for="(row, rowIndex) in keyboardLayout" :key="rowIndex">
          <view 
            class="keyboard-key" 
            v-for="(key, keyIndex) in row" 
            :key="keyIndex"
            @click="inputKey(key)"
          >
            <text class="key-text">{{ key }}</text>
          </view>
        </view>
        <view class="keyboard-row">
          <view class="keyboard-key empty"></view>
          <view class="keyboard-key" @click="inputKey('0')">
            <text class="key-text">0</text>
          </view>
          <view class="keyboard-key delete" @click="deleteKey">
            <text class="key-text">⌫</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      password: '',
      keyboardLayout: [
        ['1', '2', '3'],
        ['4', '5', '6'],
        ['7', '8', '9']
      ]
    }
  },
  methods: {
    inputKey(key) {
      if (this.password.length < 6) {
        this.password += key
        if (this.password.length === 6) {
          // 模拟支付成功
          uni.showToast({
            title: '支付成功',
            icon: 'success'
          })
          
          setTimeout(() => {
            // 跳转到支付成功页面
            uni.navigateTo({
              url: '/pages/payment-success/payment-success'
            })
          }, 1500)
        }
      }
    },
    deleteKey() {
      if (this.password.length > 0) {
        this.password = this.password.slice(0, -1)
      }
    }
  }
}
</script>

<style lang="scss">
.auth-page {
  min-height: 100vh;
  background-color: #f4f2ee;
  display: flex;
  flex-direction: column;
}

/* 步骤指示器 */
.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx 20rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  flex-shrink: 0;
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
  background-color: rgba(255,255,255,0.3);
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
  color: rgba(255,255,255,0.8);
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
  background-color: rgba(255,255,255,0.3);
  margin: 0 10rpx;
}

.step-line.active {
  background-color: #fff;
}

/* 中间内容区域 - 居中 */
.content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0 60rpx;
}

.auth-title {
  font-size: 32rpx;
  color: #666;
  text-align: center;
  display: block;
  margin-bottom: 60rpx;
}

/* 密码框 */
.password-box {
  display: flex;
  justify-content: center;
  gap: 20rpx;
}

.password-item {
  width: 80rpx;
  height: 80rpx;
  border: 2rpx solid #ddd;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
}

.password-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background-color: #8db8b6;
}

/* 键盘区域 - 固定在底部 */
.keyboard-section {
  flex-shrink: 0;
  padding: 30rpx 30rpx 60rpx 30rpx;
  background-color: #f4f2ee;
}

.keyboard-title {
  font-size: 24rpx;
  color: #999;
  text-align: center;
  display: block;
  margin-bottom: 30rpx;
}

.keyboard {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
}

.keyboard-row {
  display: flex;
  justify-content: center;
  gap: 40rpx;
  margin-bottom: 30rpx;
}

.keyboard-row:last-child {
  margin-bottom: 0;
}

.keyboard-key {
  width: 120rpx;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f8f8;
  border-radius: 16rpx;
  border: 2rpx solid #eee;
}

.keyboard-key:active {
  background-color: #e8e8e8;
}

.keyboard-key.empty {
  background-color: transparent;
  border: none;
}

.keyboard-key.delete {
  background-color: #ffe5e5;
  border-color: #ffcccc;
}

.key-text {
  font-size: 36rpx;
  color: #333;
  font-weight: 500;
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