<template>
  <view class="nav-bar">
    <view class="nav-content">
      <view class="nav-left">
        <view v-if="showBack" class="back-btn" @click="goBack">
          <text class="back-icon">←</text>
        </view>
      </view>
      <view class="nav-title">{{ title }}</view>
      <view class="nav-right"></view>
    </view>
  </view>
</template>

<script>
export default {
  name: 'CompanionNavBar',
  props: {
    title: {
      type: String,
      default: '陪诊'
    },
    showBack: {
      type: [Boolean, String],
      default: false
    }
  },
  methods: {
    goBack() {
      uni.navigateBack({
        delta: 1
      })
    }
  }
}
</script>

<style scoped>
.nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 88rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
  box-shadow: 0 4rpx 12rpx rgba(100, 175, 175, 0.2);
}

.nav-content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24rpx;
}

.nav-left {
  width: 60rpx;
  display: flex;
  align-items: center;
}

.back-btn {
  width: 44rpx;
  height: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.back-btn:active {
  background: rgba(255, 255, 255, 0.3);
  transform: scale(0.95);
}

.back-icon {
  font-size: 32rpx;
  color: #fff;
  font-weight: bold;
}

.nav-title {
  flex: 1;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.nav-right {
  width: 60rpx;
}
</style>
