<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="back-btn" @click="goHome">
        <text class="back-icon">←</text>
      </view>
      <view class="title">增值服务</view>
    </view>
    
    <!-- 服务选项 -->
    <view class="service-grid">
      <view class="service-item" :class="{ active: selectedService === 0 }" @click="selectService(0)">
        <view class="service-icon">
          <text class="icon">🏠</text>
        </view>
        <text class="service-name">病历翻译</text>
      </view>
      
      <view class="service-item" :class="{ active: selectedService === 1 }" @click="selectService(1)">
        <view class="service-icon">
          <text class="icon">📋</text>
        </view>
        <text class="service-name">处方解读</text>
      </view>
      
      <view class="service-item" :class="{ active: selectedService === 2 }" @click="selectService(2)">
        <view class="service-icon">
          <text class="icon">📄</text>
        </view>
        <text class="service-name">线上视频复诊</text>
      </view>
      
      <view class="service-item" :class="{ active: selectedService === 3 }" @click="selectService(3)">
        <view class="service-icon">
          <text class="icon">🏥</text>
        </view>
        <text class="service-name">术后康养</text>
      </view>
    </view>
    
    <!-- 底部按钮 -->
    <view class="bottom-buttons">
      <button class="confirm-btn" @click="confirmSelection">立即预约</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedService: -1 // 默认未选择
    }
  },
  methods: {
    goHome() {
      uni.switchTab({
        url: '/pages/home/home'
      })
    },
    selectService(index) {
      this.selectedService = index
    },
    confirmSelection() {
      if (this.selectedService === -1) {
        uni.showToast({
          title: '请选择一项服务',
          icon: 'none'
        })
      } else {
        uni.showToast({
          title: '预约成功',
          icon: 'success'
        })
        // 延迟跳转，让用户看到成功提示
        setTimeout(() => {
          this.goHome()
        }, 1500)
      }
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 100vh;
  background-color: #f8f9fa;
  padding-bottom: 100rpx;
}

/* 头部 */
.header {
  background-color: #4DD0E1;
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.back-btn {
  position: absolute;
  top: 40rpx;
  left: 30rpx;
  z-index: 1;
}

.back-icon {
  font-size: 36rpx;
  color: #fff;
}

.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  text-align: center;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
  padding: 30rpx;
}

.service-item {
  background-color: white;
  border-radius: 20rpx;
  padding: 40rpx 30rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  border: 2rpx solid transparent;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.service-item:hover {
  transform: translateY(-5rpx);
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.2);
}

.service-item.active {
  border-color: #4DD0E1;
  box-shadow: 0 0 0 3rpx rgba(77, 208, 225, 0.1);
}

.service-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 25rpx;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.icon {
  font-size: 60rpx;
  color: white;
}

.service-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  text-align: center;
}

.bottom-buttons {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background-color: white;
  padding: 30rpx;
  box-shadow: 0 -2rpx 15rpx rgba(0, 0, 0, 0.08);
}

.confirm-btn {
  flex: 1;
  height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: white;
  border-radius: 45rpx;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.confirm-btn:active {
  transform: translateY(2rpx);
  box-shadow: 0 2rpx 6rpx rgba(77, 208, 225, 0.3);
}

.confirm-btn::after {
  border: none;
}

button {
  border: none;
  outline: none;
}
</style>
