<template>
  <view class="container">
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
  background-color: #f4f2ee;
  padding-bottom: 100rpx;
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
  border-color: #8db8b6;
  box-shadow: 0 0 0 3rpx rgba(77, 208, 225, 0.1);
}

.service-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
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
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
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