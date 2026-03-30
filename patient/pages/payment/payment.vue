<template>
  <view class="payment-page">
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

    <!-- 支付金额区域 -->
    <view class="amount-section">
      <view class="amount-row">
        <text class="amount-label">现在支付</text>
        <text class="amount-value">¥{{ amount }}</text>
      </view>
    </view>

    <!-- 支付方式列表 -->
    <view class="payment-methods">
      <!-- 银行卡 -->
      <view
        class="payment-item"
        :class="{ active: selectedMethod === 'bankcard' }"
        @click="selectMethod('bankcard')"
      >
        <view class="method-left">
          <view class="method-icon bankcard">
            <text class="iconfont bankcard-icon">卡</text>
          </view>
          <view class="method-info">
            <text class="method-name">银行卡</text>
            <text class="bank-card-name" v-if="selectedBankCard">{{ selectedBankCard }}</text>
          </view>
        </view>
        <view class="method-right">
          <view class="radio" :class="{ checked: selectedMethod === 'bankcard' }">
            <view class="radio-inner" v-if="selectedMethod === 'bankcard'"></view>
          </view>
        </view>
      </view>

      <!-- 支付宝 -->
      <view
        class="payment-item"
        :class="{ active: selectedMethod === 'alipay' }"
        @click="selectMethod('alipay')"
      >
        <view class="method-left">
          <view class="method-icon alipay">
            <text class="iconfont alipay-icon">支</text>
          </view>
          <text class="method-name">支付宝</text>
        </view>
        <view class="method-right">
          <view class="radio" :class="{ checked: selectedMethod === 'alipay' }">
            <view class="radio-inner" v-if="selectedMethod === 'alipay'"></view>
          </view>
        </view>
      </view>

      <!-- 微信支付 -->
      <view
        class="payment-item"
        :class="{ active: selectedMethod === 'wechat' }"
        @click="selectMethod('wechat')"
      >
        <view class="method-left">
          <view class="method-icon wechat">
            <text class="iconfont wechat-icon">微</text>
          </view>
          <text class="method-name">微信支付</text>
        </view>
        <view class="method-right">
          <view class="radio" :class="{ checked: selectedMethod === 'wechat' }">
            <view class="radio-inner" v-if="selectedMethod === 'wechat'"></view>
          </view>
        </view>
      </view>

      <!-- 花呗分期 -->
      <view
        class="payment-item"
        :class="{ active: selectedMethod === 'huabei' }"
        @click="selectMethod('huabei')"
      >
        <view class="method-left">
          <view class="method-icon huabei">
            <text class="iconfont huabei-icon">花</text>
          </view>
          <text class="method-name">花呗分期</text>
        </view>
        <view class="method-right">
          <view class="radio" :class="{ checked: selectedMethod === 'huabei' }">
            <view class="radio-inner" v-if="selectedMethod === 'huabei'"></view>
          </view>
        </view>
      </view>

      <!-- 其他支付方式 -->
      <view
        class="payment-item"
        :class="{ active: selectedMethod === 'more' }"
        @click="selectMethod('more')"
      >
        <view class="method-left">
          <view class="method-icon more">
            <text class="iconfont more-icon">+</text>
          </view>
          <text class="method-name">其他支付方式</text>
        </view>
        <view class="method-right">
          <view class="radio" :class="{ checked: selectedMethod === 'more' }">
            <view class="radio-inner" v-if="selectedMethod === 'more'"></view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <button v-if="selectedMethod === 'more' && !fromChannel" class="pay-btn" @click="goToMorePayment">进入更多支付方式</button>
      <button v-else class="pay-btn" @click="handlePay">立即支付</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      amount: '148.99',
      selectedMethod: 'alipay',
      selectedBankCard: '',
      fromChannel: false
    }
  },
  onShow() {
    // 检查是否从支付通道页面返回
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    if (currentPage && currentPage.$vm && currentPage.$vm.fromChannel) {
      this.fromChannel = true
      this.selectedMethod = 'more'
      // 获取选择的银行卡信息
      if (currentPage.$vm.selectedBankCard) {
        this.selectedBankCard = currentPage.$vm.selectedBankCard
      }
    }
    
    // 从本地存储获取银行卡信息
    const bankCard = uni.getStorageSync('selectedBankCard')
    if (bankCard) {
      this.selectedBankCard = bankCard
    }
  },
  methods: {
    selectMethod(method) {
      this.selectedMethod = method
      if (method === 'bankcard') {
        // 跳转到支付通道页面选择银行卡
        uni.navigateTo({
          url: '/pages/payment-channel/payment-channel'
        })
      } else if (method !== 'more') {
        this.fromChannel = false
      }
    },
    goToMorePayment() {
      uni.navigateTo({
        url: '/pages/payment-channel/payment-channel'
      })
    },
    handlePay() {
      // 存储支付信息
      uni.setStorageSync('paymentMethod', this.selectedMethod)
      uni.setStorageSync('paymentAmount', this.amount)
      
      // 跳转到支付认证页面
      uni.navigateTo({
        url: '/pages/payment-auth/payment-auth'
      })
    }
  }
}
</script>

<style lang="scss">
.payment-page {
  min-height: 100vh;
  background-color: #f5f5f5;
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
  color: #4DD0E1;
}

.step-item.completed .step-num {
  background-color: #fff;
  color: #4DD0E1;
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

/* 支付金额区域 */
.amount-section {
  background-color: #fff;
  padding: 40rpx 30rpx;
  margin: 20rpx;
  border-radius: 16rpx;
}

.amount-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.amount-label {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
}

.amount-value {
  font-size: 40rpx;
  color: #4DD0E1;
  font-weight: bold;
}

/* 支付方式列表 */
.payment-methods {
  background-color: #fff;
  margin: 0 20rpx;
  border-radius: 16rpx;
  padding: 0 30rpx;
}

.payment-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.payment-item:last-child {
  border-bottom: none;
}

.method-left {
  display: flex;
  align-items: center;
}

.method-info {
  display: flex;
  flex-direction: column;
}

.method-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.method-icon.alipay {
  background: linear-gradient(135deg, #1677ff 0%, #0056d6 100%);
}

.method-icon.wechat {
  background: linear-gradient(135deg, #07c160 0%, #05a050 100%);
}

.method-icon.huabei {
  background: linear-gradient(135deg, #1677ff 0%, #00a0e9 100%);
}

.method-icon.bankcard {
  background: linear-gradient(135deg, #ff6b35 0%, #e55a2b 100%);
}

.method-icon.more {
  background: linear-gradient(135deg, #999 0%, #666 100%);
}

.alipay-icon,
.wechat-icon,
.huabei-icon,
.bankcard-icon,
.more-icon {
  font-size: 28rpx;
  color: #fff;
  font-weight: bold;
}

.method-name {
  font-size: 30rpx;
  color: #333;
}

.bank-card-name {
  font-size: 24rpx;
  color: #999;
  margin-top: 4rpx;
}

/* 单选按钮 */
.radio {
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  border: 2rpx solid #ddd;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
}

.radio.checked {
  border-color: #4DD0E1;
  background-color: #4DD0E1;
}

.radio-inner {
  width: 20rpx;
  height: 20rpx;
  background-color: #fff;
  border-radius: 50%;
}

/* 底部支付按钮 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.pay-btn {
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

.pay-btn::after {
  border: none;
}

.pay-btn:active {
  opacity: 0.9;
}
</style>