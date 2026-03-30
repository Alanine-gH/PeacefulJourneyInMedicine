<template>
  <view class="container">
    <!-- 陪护功能区 - 第一部分 -->
    <view class="patient-section">
      <view class="patient-grid">
        <view class="patient-item" @click="navigateTo('select-order')">
          <view class="patient-icon">
            <text>📋</text>
          </view>
          <text class="patient-text">选择订单</text>
        </view>
        <view class="patient-item" @click="navigateTo('certification')">
          <view class="patient-icon">
            <text>✅</text>
          </view>
          <text class="patient-text">资格认证</text>
        </view>
        <view class="patient-item" @click="navigateTo('my-orders')">
          <view class="patient-icon">
            <text>📊</text>
          </view>
          <text class="patient-text">我的订单</text>
        </view>
      </view>
    </view>

    <!-- 轮播图 - 第二部分 -->
    <view class="carousel-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎉</text>
          工作动态
        </text>
      </view>
      <view class="swiper-container">
        <swiper indicator-dots="true" autoplay="true" interval="3000" duration="1000" circular="true">
          <swiper-item v-for="(item, index) in carouselList" :key="index">
            <view class="carousel-item" :class="item.className" @click="navigateToCarousel(item.page)">
              <view class="carousel-content">
                <view class="carousel-price">{{ item.price }}</view>
                <view class="carousel-title">{{ item.title }}</view>
                <view class="carousel-subtitle">{{ item.subtitle }}</view>
                <view class="carousel-btn">{{ item.btnText }}</view>
                <view v-if="item.icon" class="carousel-image">
                  <text>{{ item.icon }}</text>
                </view>
                <view v-if="item.tag" class="carousel-tag">{{ item.tag }}</view>
                <view v-if="item.heart" class="carousel-heart">{{ item.heart }}</view>
              </view>
            </view>
          </swiper-item>
        </swiper>
      </view>
    </view>

    <!-- 上岗培训 - 第三部分 -->
    <view class="training-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎓</text>
          上岗培训
        </text>
      </view>
      <view class="training-list">
        <view class="training-item" @click="navigateTo('norms')">
          <view class="training-info">
            <view class="training-name">陪护服务规范</view>
            <view class="training-desc">了解陪护服务的基本规范和流程</view>
            <view class="training-duration">时长: 2小时</view>
            <view class="training-status">状态: 必修</view>
          </view>
          <view class="training-action">
            <text class="action-btn">培训</text>
          </view>
        </view>
        <view class="training-item" @click="navigateTo('training2')">
          <view class="training-info">
            <view class="training-name">医疗知识基础</view>
            <view class="training-desc">掌握基本的医疗知识和术语</view>
            <view class="training-duration">时长: 4小时</view>
            <view class="training-status">状态: 必修</view>
          </view>
          <view class="training-action">
            <text class="action-btn">培训</text>
          </view>
        </view>
        <view class="training-item" @click="navigateTo('training3')">
          <view class="training-info">
            <view class="training-name">应急处理</view>
            <view class="training-desc">学习紧急情况下的处理方法</view>
            <view class="training-duration">时长: 3小时</view>
            <view class="training-status">状态: 必修</view>
          </view>
          <view class="training-action">
            <text class="action-btn">培训</text>
          </view>
        </view>
        <view class="training-item" @click="navigateTo('training4')">
          <view class="training-info">
            <view class="training-name">服务礼仪</view>
            <view class="training-desc">提升服务意识和沟通技巧</view>
            <view class="training-duration">时长: 2小时</view>
            <view class="training-status">状态: 必修</view>
          </view>
          <view class="training-action">
            <text class="action-btn">培训</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>

    <!-- 底部导航栏 -->
    <CompanionTabBar activeTab="home"></CompanionTabBar>
  </view>
</template>

<script>
import CompanionTabBar from '@/components/CompanionTabBar.vue'

export default {
  components: {
    CompanionTabBar
  },
  data() {
    return {
      carouselList: [{
        price: '高薪兼职',
        title: '急招陪护',
        subtitle: '200元/小时',
        btnText: '立即接单',
        icon: '👨‍⚕️',
        className: '',
        page: 'parttime'
      },
        {
          price: '长期合作',
          title: '医院合作',
          subtitle: '稳定收入',
          btnText: '了解详情',
          tag: '推荐',
          className: 'vip',
          page: 'cooperation'
        },
        {
          price: '专业培训',
          title: '技能提升',
          subtitle: '免费培训',
          btnText: '报名参加',
          heart: '💖',
          className: 'basic',
          page: 'training'
        }
      ]
    }
  },
  methods: {
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
    navigateTo(page) {
      switch (page) {
        case 'select-order':
          // 跳转到选择订单页面（hall页面）
          uni.navigateTo({
            url: '/pages/hall/hall'
          })
          break
        case 'certification':
          // 跳转到资格认证页面
          uni.navigateTo({
            url: '/pages/certification/certification'
          })
          break
        case 'my-orders':
          // 跳转到我的订单页面
          uni.navigateTo({
            url: '/pages/companion/orders'
          })
          break
        case 'norms':
          // 跳转到陪护服务规范页面
          uni.navigateTo({
            url: '/pages/companion/norms/norms'
          })
          break
        case 'training2':
          // 跳转到医疗知识基础页面
          uni.navigateTo({
            url: '/pages/companion/training/training2'
          })
          break
        case 'training3':
          // 跳转到应急处理页面
          uni.navigateTo({
            url: '/pages/companion/training/training3'
          })
          break
        case 'training4':
          // 跳转到服务礼仪页面
          uni.navigateTo({
            url: '/pages/companion/training/training4'
          })
          break
        default:
          uni.showToast({
            title: `跳转到${page}页面`,
            icon: 'none'
          })
      }
    },
    navigateToCarousel(page) {
      switch (page) {
        case 'parttime':
          uni.navigateTo({
            url: '/pages/companion/parttime'
          })
          break
        case 'cooperation':
          uni.navigateTo({
            url: '/pages/companion/cooperation'
          })
          break
        case 'training':
          uni.navigateTo({
            url: '/pages/companion/training'
          })
          break
        default:
          uni.showToast({
            title: `跳转到${page}页面`,
            icon: 'none'
          })
      }
    },
    switchTab(tab) {
      if (tab === 'home') {
        uni.redirectTo({
          url: '/pages/companion/home'
        })
      } else if (tab === 'info') {
        uni.navigateTo({
          url: '/pages/companion/info'
        })
      } else if (tab === 'mine') {
        uni.navigateTo({
          url: '/pages/companion/mine'
        })
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
  padding-bottom: 120rpx;
}

/* 陪护功能区 - 第一部分 */
.patient-section {
  background: #fff;
  padding: 40rpx 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.patient-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30rpx;
}

.patient-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx 20rpx;
  background: #f4f2ee;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.patient-item:hover {
  transform: translateY(-5rpx);
  box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.1);
}

.patient-icon {
  width: 70rpx;
  height: 70rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 35rpx;
  margin-bottom: 15rpx;
  box-shadow: 0 3rpx 10rpx rgba(100, 181, 246, 0.3);
}

.patient-text {
  font-size: 26rpx;
  color: #333;
  text-align: center;
  font-weight: 500;
}

/* 轮播图 - 第二部分 */
.carousel-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.section-title .icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.swiper-container {
  width: 100%;
  height: 300rpx;
  border-radius: 20rpx;
  overflow: hidden;
}

.swiper-container swiper {
  width: 100%;
  height: 100%;
}

.swiper-container swiper-item {
  width: 100%;
  height: 100%;
}

.carousel-item {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 20rpx;
  position: relative;
  overflow: hidden;
  min-height: 200rpx;
  cursor: pointer;
  transition: all 0.3s ease;
}

.carousel-item:hover {
  transform: scale(1.02);
}

.carousel-item.vip {
  background: linear-gradient(135deg, #ffb74d 0%, #f59e0b 100%);
}

.carousel-item.basic {
  background: linear-gradient(135deg, #6ee7b7 0%, #34d399 100%);
}

.carousel-content {
  padding: 30rpx;
  color: #fff;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
}

.carousel-price {
  font-size: 28rpx;
  font-weight: 600;
}

.carousel-title {
  font-size: 36rpx;
  font-weight: 700;
}

.carousel-subtitle {
  font-size: 26rpx;
  opacity: 0.9;
}

.carousel-btn {
  background: rgba(255, 255, 255, 0.2);
  width: 150rpx;
  height: 60rpx;
  border-radius: 30rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26rpx;
  font-weight: 500;
}

.carousel-image {
  position: absolute;
  right: 30rpx;
  bottom: 30rpx;
  font-size: 80rpx;
  opacity: 0.8;
}

.carousel-tag {
  position: absolute;
  top: 20rpx;
  right: 20rpx;
  background: rgba(255, 255, 255, 0.2);
  padding: 8rpx 16rpx;
  border-radius: 15rpx;
  font-size: 24rpx;
  font-weight: 600;
}

.carousel-heart {
  position: absolute;
  right: 30rpx;
  bottom: 40rpx;
  font-size: 40rpx;
  opacity: 0.8;
  animation: pulse 2s infinite;
}

@keyframes pulse {

  0%,
  100% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.1);
  }
}

/* 上岗培训 - 第三部分 */
.training-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.training-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.training-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx;
  background: #f4f2ee;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.training-item:hover {
  background: #e9ecef;
}

.training-info {
  flex: 1;
}

.training-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.training-desc {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.training-duration {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.training-status {
  font-size: 24rpx;
  color: #999;
}

.training-action {
  margin-left: 20rpx;
}

.action-btn {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  padding: 15rpx 30rpx;
  border-radius: 20rpx;
  font-size: 26rpx;
  font-weight: 500;
  box-shadow: 0 3rpx 10rpx rgba(77, 208, 225, 0.3);
}

/* 底部导航栏 */
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -2rpx 15rpx rgba(0, 0, 0, 0.08);
  z-index: 100;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  transition: all 0.3s ease;
}

.tab-item.active {
  color: #8db8b6;
}

.tab-icon {
  font-size: 36rpx;
  margin-bottom: 5rpx;
}

.tab-text {
  font-size: 24rpx;
  font-weight: 500;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .patient-grid {
    gap: 20rpx;
  }

  .patient-icon {
    width: 60rpx;
    height: 60rpx;
    font-size: 30rpx;
  }

  .carousel-item {
    min-height: 180rpx;
  }
}

/* ── Shared theme overrides ── */
page {
  background-color: #f4f2ee !important;
}

.container {
  background-color: #f4f2ee !important;
  min-height: 100vh;
}

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
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.10) !important;
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
  box-shadow: 0 4rpx 16rpx rgba(100, 175, 175, 0.28) !important;
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
.order-status {
  color: #8db8b6 !important;
}

/* Section titles */
.section-title {
  color: #3a3a4a !important;
  font-weight: 600 !important;
}

/* Stat items */
.stat-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

/* Tab bar selected */
.tab-item.active,
.tab-item.active .tab-text {
  color: #8db8b6 !important;
}

/* Profile avatar ring */
.profile-avatar {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 12rpx rgba(100, 175, 175, 0.28) !important;
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
.carousel-item {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

.carousel-item.vip {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important;
}

.carousel-item.full {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

.carousel-item.consult {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important;
}

.carousel-item.result {
  background: linear-gradient(135deg, #d4a8b0 0%, #c09098 100%) !important;
}

.carousel-item.medicine {
  background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%) !important;
}

.carousel-item.basic {
  background: linear-gradient(135deg, #a8cec8 0%, #8db8b0 100%) !important;
}

/* Step indicator */
.step-num {
  border-color: #8db8b6 !important;
  color: #8db8b6 !important;
}

.step-item.active .step-num,
.step-item.completed .step-num {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important;
  border-color: transparent !important;
}

.step-line.active {
  background-color: #8db8b6 !important;
}

/* Health exam header */
.header {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

/* Package type tags */
.pkg-type-tag {
  background: #e8f4f4 !important;
}

.pkg-type-tag .type-text {
  color: #6a9ea0 !important;
}

/* Input focus ring */
.input-item:focus-within {
  border-color: #8db8b6 !important;
  box-shadow: 0 0 0 3rpx rgba(141, 184, 182, 0.18) !important;
}


/* ── companion home page specifics ── */
.patient-section {
  margin: 16rpx 24rpx !important;
  padding: 28rpx 24rpx !important;
}

.patient-item {
  background: #f0f4f4 !important;
  border-radius: 16rpx !important;
}

.carousel-section {
  margin: 0 24rpx !important;
  padding: 24rpx !important;
}

.training-section {
  margin: 0 24rpx 16rpx !important;
  padding: 24rpx !important;
}

.training-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

.training-name {
  color: #3a3a4a !important;
  font-weight: 600 !important;
}

.training-desc {
  color: #6a6a7a !important;
}

.training-duration, .training-status {
  color: #9a9aaa !important;
}

</style>