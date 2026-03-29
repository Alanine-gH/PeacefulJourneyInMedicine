<template>
  <view class="channel-page">
    <!-- 导航栏 -->
   <!-- <view class="nav-bar">
      <text class="nav-title">支付通道</text>
      <view class="placeholder"></view>
    </view> -->

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

    <!-- 信用卡支付 -->
    <view class="section">
      <text class="section-title">信用卡支付</text>
      <view class="channel-grid">
        <view class="channel-item" @click="selectChannel('visa')">
          <view class="channel-icon visa">VISA</view>
        </view>
        <view class="channel-item" @click="selectChannel('mastercard')">
          <view class="channel-icon mastercard">Master</view>
        </view>
        <view class="channel-item" @click="selectChannel('jcb')">
          <view class="channel-icon jcb">JCB</view>
        </view>
        <view class="channel-item" @click="selectChannel('amex')">
          <view class="channel-icon amex">AMEX</view>
        </view>
      </view>
    </view>

    <!-- 钱包支付 -->
    <view class="section">
      <text class="section-title">钱包支付</text>
      <view class="channel-grid">
        <view class="channel-item" @click="selectChannel('applepay')">
          <view class="channel-icon applepay">Apple</view>
        </view>
        <view class="channel-item" @click="selectChannel('googlepay')">
          <view class="channel-icon googlepay">Google</view>
        </view>
        <view class="channel-item" @click="selectChannel('samsungpay')">
          <view class="channel-icon samsungpay">Samsung</view>
        </view>
      </view>
    </view>

    <!-- 本地电子钱包 -->
    <view class="section">
      <text class="section-title">本地电子钱包</text>
      <view class="channel-grid">
        <view class="channel-item" @click="selectChannel('gcash')">
          <view class="channel-icon gcash">GCash</view>
        </view>
        <view class="channel-item" @click="selectChannel('paymaya')">
          <view class="channel-icon paymaya">PayMaya</view>
        </view>
        <view class="channel-item" @click="selectChannel('grabpay')">
          <view class="channel-icon grabpay">GrabPay</view>
        </view>
        <view class="channel-item" @click="selectChannel('dana')">
          <view class="channel-icon dana">DANA</view>
        </view>
        <view class="channel-item" @click="selectChannel('ovo')">
          <view class="channel-icon ovo">OVO</view>
        </view>
        <view class="channel-item" @click="selectChannel('linkaja')">
          <view class="channel-icon linkaja">LinkAja</view>
        </view>
        <view class="channel-item" @click="selectChannel('shopeepay')">
          <view class="channel-icon shopeepay">Shopee</view>
        </view>
        <view class="channel-item" @click="selectChannel('boost')">
          <view class="channel-icon boost">Boost</view>
        </view>
        <view class="channel-item" @click="selectChannel('touchngo')">
          <view class="channel-icon touchngo">TNG</view>
        </view>
      </view>
    </view>

    <!-- 其他支付 -->
    <view class="section">
      <text class="section-title">其他支付</text>
      <view class="channel-grid">
        <view class="channel-item" @click="selectChannel('kakaopay')">
          <view class="channel-icon kakaopay">Kakao</view>
        </view>
        <view class="channel-item" @click="selectChannel('truemoney')">
          <view class="channel-icon truemoney">True</view>
        </view>
        <view class="channel-item" @click="selectChannel('mcash')">
          <view class="channel-icon mcash">Mcash</view>
        </view>
        <view class="channel-item" @click="selectChannel('alipay')">
          <view class="channel-icon alipay">Alipay</view>
        </view>
        <view class="channel-item" @click="selectChannel('wechat')">
          <view class="channel-icon wechat">WeChat</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      channelNames: {
        'visa': 'VISA信用卡',
        'mastercard': '万事达信用卡',
        'jcb': 'JCB信用卡',
        'amex': '美国运通信用卡',
        'applepay': 'Apple Pay',
        'googlepay': 'Google Pay',
        'samsungpay': 'Samsung Pay',
        'gcash': 'GCash',
        'paymaya': 'PayMaya',
        'grabpay': 'GrabPay',
        'dana': 'DANA',
        'ovo': 'OVO',
        'linkaja': 'LinkAja',
        'shopeepay': 'ShopeePay',
        'boost': 'Boost',
        'touchngo': 'Touch \'n Go',
        'kakaopay': 'KakaoPay',
        'truemoney': 'TrueMoney',
        'mcash': 'Mcash',
        'alipay': '支付宝',
        'wechat': '微信支付'
      }
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    selectChannel(channel) {
      const channelName = this.channelNames[channel] || channel

      // 存储选择的银行卡信息
      uni.setStorageSync('selectedBankCard', channelName)

      // 设置标记并返回
      const pages = getCurrentPages()
      const prevPage = pages[pages.length - 2]
      if (prevPage && prevPage.$vm) {
        prevPage.$vm.fromChannel = true
        prevPage.$vm.selectedBankCard = channelName
      }

      uni.showToast({
        title: '已选择: ' + channelName,
        icon: 'none'
      })

      setTimeout(() => {
        uni.navigateBack()
      }, 500)
    }
  }
}
</script>

<style lang="scss">
.channel-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40rpx;
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

/* 分区 */
.section {
  background-color: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 16rpx;
}

.section-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
  display: block;
}

/* 支付通道网格 */
.channel-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.channel-item {
  width: calc(25% - 15rpx);
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f8f8f8;
  border-radius: 16rpx;
  border: 2rpx solid #eee;
}

.channel-item:active {
  background-color: #f0f0f0;
}

.channel-icon {
  font-size: 24rpx;
  color: #333;
  font-weight: 500;
  text-align: center;
}

.visa {
  color: #1a1f71;
}

.mastercard {
  color: #eb001b;
}

.jcb {
  color: #0066b3;
}

.amex {
  color: #006fcf;
}

.applepay {
  color: #000;
}

.googlepay {
  color: #4285f4;
}

.samsungpay {
  color: #1428a0;
}

.gcash {
  color: #005ce6;
}

.paymaya {
  color: #00a8e8;
}

.grabpay {
  color: #00b14f;
}

.dana {
  color: #008ceb;
}

.ovo {
  color: #4c3494;
}

.linkaja {
  color: #e31e24;
}

.shopeepay {
  color: #ee4d2d;
}

.boost {
  color: #ff6b00;
}

.touchngo {
  color: #0057b8;
}

.kakaopay {
  color: #ffcd00;
}

.truemoney {
  color: #e52822;
}

.mcash {
  color: #00a651;
}

.alipay {
  color: #1677ff;
}

.wechat {
  color: #07c160;
}
</style>