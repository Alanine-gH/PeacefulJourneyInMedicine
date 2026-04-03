<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="陪护端首页"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 快捷功能区 -->
    <view class="quick-section">
      <view class="quick-grid">
        <view class="quick-item" @click="navigateTo('select-order')">
          <view class="quick-icon">
            <text>📋</text>
          </view>
          <text class="quick-text">选择订单</text>
        </view>
        <view class="quick-item" @click="navigateTo('certification')">
          <view class="quick-icon">
            <text>✅</text>
          </view>
          <text class="quick-text">资格认证</text>
        </view>
        <view class="quick-item" @click="navigateTo('my-orders')">
          <view class="quick-icon">
            <text>📊</text>
          </view>
          <text class="quick-text">我的订单</text>
        </view>
      </view>
    </view>

    <!-- 工作动态 -->
    <view class="work-section">
      <view class="section-header">
        <text class="section-title">工作动态</text>
      </view>
      <view class="swiper-container">
        <swiper indicator-dots="true" autoplay="true" interval="3000" duration="1000" circular="true">
          <swiper-item v-for="(card, index) in workCards" :key="index">
            <view class="work-card" :class="card.className" @click="navigateToWork(card.id)">
              <view class="work-content">
                <view class="work-subtitle">{{ card.subtitle }}</view>
                <view class="work-title">{{ card.title }}</view>
                <view class="work-price">
                  <text class="price-value">{{ card.price }}</text>
                  <text class="price-unit">{{ card.unit }}</text>
                </view>
                <view class="work-btn">{{ card.action }}</view>
                <view class="work-emoji">{{ card.emoji }}</view>
              </view>
            </view>
          </swiper-item>
        </swiper>
      </view>
    </view>

    <!-- 上岗培训 -->
    <view class="training-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎓</text>
          上岗培训
        </text>
      </view>
      <view class="training-list">
        <view class="training-item" v-for="(training, index) in trainingList" :key="index">
          <view class="training-info">
            <view class="training-name">{{ training.title }}</view>
            <view class="training-desc">{{ training.description }}</view>
            <view class="training-meta">
              <text class="meta-duration">时长：{{ training.duration }}</text>
              <text class="meta-divider">•</text>
              <view class="meta-status" :class="training.statusColor">
                {{ training.status }}
              </view>
            </view>
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
import CompanionNavBar from '@/components/CompanionNavBar.vue'

export default {
  components: {
    CompanionTabBar,
    CompanionNavBar
  },
  data() {
    return {
      workCards: [
        {
          id: 1,
          title: '急需陪诊',
          subtitle: '高薪兼职',
          price: '200',
          unit: '元/小时',
          action: '立即接单',
          className: 'green',
          emoji: '🩺'
        },
        {
          id: 2,
          title: '全程陪诊',
          subtitle: '长期服务',
          price: '500',
          unit: '元/次',
          action: '立即接单',
          className: 'purple',
          emoji: '💼'
        },
        {
          id: 3,
          title: '体检陪护',
          subtitle: '简单轻松',
          price: '150',
          unit: '元/次',
          action: '立即接单',
          className: 'orange',
          emoji: '🏥'
        }
      ],
      trainingList: [
        {
          id: 1,
          title: '陪护服务规范',
          description: '了解陪护服务的基本规范和流程',
          duration: '2小时',
          status: '必修',
          statusColor: 'status-green'
        },
        {
          id: 2,
          title: '医疗知识基础',
          description: '掌握基础的医疗知识和术语',
          duration: '4小时',
          status: '必修',
          statusColor: 'status-purple'
        },
        {
          id: 3,
          title: '应急处理',
          description: '学习紧急情况下的处理方法',
          duration: '3小时',
          status: '必修',
          statusColor: 'status-orange'
        },
        {
          id: 4,
          title: '服务礼仪',
          description: '掌握服务礼仪和沟通技巧',
          duration: '2小时',
          status: '必修',
          statusColor: 'status-pink'
        }
      ]
    }
  },
  methods: {
    navigateTo(page) {
      switch (page) {
        case 'select-order':
          uni.navigateTo({
            url: '/pages/hall/hall'
          })
          break
        case 'certification':
          uni.navigateTo({
            url: '/pages/certification/certification'
          })
          break
        case 'my-orders':
          uni.navigateTo({
            url: '/pages/companion/orders'
          })
          break
        default:
          uni.showToast({
            title: `跳转到${page}页面`,
            icon: 'none'
          })
      }
    },
    navigateToWork(id) {
      switch (id) {
        case 1:
          uni.navigateTo({
            url: '/pages/companion/parttime'
          })
          break
        case 2:
          uni.navigateTo({
            url: '/pages/companion/cooperation'
          })
          break
        case 3:
          uni.navigateTo({
            url: '/pages/companion/training'
          })
          break
        default:
          uni.showToast({
            title: '跳转到工作页面',
            icon: 'none'
          })
      }
    }
  }
}
</script>

<style scoped>
.container {
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  min-height: 100vh;
  padding: 24rpx;
  padding-bottom: 140rpx;
}

/* 快捷功能区 */
.quick-section {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20rpx);
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 32rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.05);
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32rpx;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
  transition: all 0.3s ease;
}

.quick-item:active {
  transform: scale(0.96);
}

.quick-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  transition: all 0.3s ease;
}

.quick-item:nth-child(1) .quick-icon {
  background: linear-gradient(135deg, #BBD1E8 0%, #AEC4DF 100%);
}

.quick-item:nth-child(2) .quick-icon {
  background: linear-gradient(135deg, #A8D4C4 0%, #96C4B4 100%);
}

.quick-item:nth-child(3) .quick-icon {
  background: linear-gradient(135deg, #E8BBD1 0%, #DFAEC4 100%);
}

.quick-text {
  font-size: 24rpx;
  color: #6B7C8A;
  text-align: center;
}

/* 工作动态 */
.work-section {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 32rpx;
}

.section-header::before {
  content: '';
  width: 8rpx;
  height: 32rpx;
  background: linear-gradient(180deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 4rpx;
  margin-right: 16rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #5B6B78;
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.section-title .icon {
  font-size: 32rpx;
}

.swiper-container {
  width: 100%;
  height: 360rpx;
  border-radius: 24rpx;
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

.work-card {
  position: relative;
  border-radius: 28rpx;
  padding: 48rpx;
  overflow: hidden;
  height: 100%;
  transition: all 0.3s ease;
}

.work-card::before {
  content: '';
  position: absolute;
  top: -20%;
  right: -20%;
  width: 40%;
  height: 40%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  filter: blur(40rpx);
}

.work-card::after {
  content: '';
  position: absolute;
  bottom: -20%;
  left: -20%;
  width: 40%;
  height: 40%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  filter: blur(40rpx);
}

.work-card.green {
  background: linear-gradient(135deg, #9DD4C8 0%, #78B4A8 100%);
  box-shadow: 0 8rpx 32rpx rgba(120, 180, 168, 0.3);
}

.work-card.purple {
  background: linear-gradient(135deg, #BDB4D8 0%, #9D94B8 100%);
  box-shadow: 0 8rpx 32rpx rgba(157, 148, 184, 0.3);
}

.work-card.orange {
  background: linear-gradient(135deg, #E5BCA5 0%, #CB9C85 100%);
  box-shadow: 0 8rpx 32rpx rgba(203, 156, 133, 0.3);
}

.work-content {
  position: relative;
  z-index: 1;
  color: #fff;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.work-subtitle {
  font-size: 24rpx;
  opacity: 0.9;
  margin-bottom: 8rpx;
}

.work-title {
  font-size: 40rpx;
  font-weight: 700;
  margin-bottom: 24rpx;
}

.work-price {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
  margin-bottom: 32rpx;
}

.price-value {
  font-size: 48rpx;
  font-weight: 700;
}

.price-unit {
  font-size: 24rpx;
  opacity: 0.85;
}

.work-btn {
  align-self: flex-start;
  background: #fff;
  color: #5B6B78;
  padding: 16rpx 40rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  font-weight: 600;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.work-btn:active {
  transform: scale(0.96);
}

.work-emoji {
  position: absolute;
  right: 48rpx;
  bottom: 48rpx;
  font-size: 120rpx;
  opacity: 0.9;
}

/* 上岗培训 */
.training-section {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
}

.training-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.training-item {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.training-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.training-info {
  flex: 1;
  margin-right: 24rpx;
}

.training-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #4A5A68;
  margin-bottom: 12rpx;
}

.training-desc {
  font-size: 24rpx;
  color: #8A9BA8;
  margin-bottom: 20rpx;
  line-height: 32rpx;
}

.training-meta {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.meta-duration {
  font-size: 20rpx;
  color: #A5B5C0;
}

.meta-divider {
  font-size: 20rpx;
  color: #A5B5C0;
}

.meta-status {
  font-size: 20rpx;
  padding: 4rpx 16rpx;
  border-radius: 16rpx;
  font-weight: 500;
}

.status-green {
  background: rgba(122, 168, 158, 0.2);
  color: #7AA89E;
}

.status-purple {
  background: rgba(149, 135, 171, 0.2);
  color: #9587AB;
}

.status-orange {
  background: rgba(184, 151, 126, 0.2);
  color: #B8977E;
}

.status-pink {
  background: rgba(184, 130, 153, 0.2);
  color: #B88299;
}

.training-action {
  margin-left: 16rpx;
}

.action-btn {
  background: linear-gradient(135deg, #A8D4C4 0%, #96C4B4 100%);
  color: #fff;
  padding: 16rpx 40rpx;
  border-radius: 14rpx;
  font-size: 24rpx;
  font-weight: 500;
  box-shadow: 0 4rpx 16rpx rgba(122, 168, 158, 0.3);
  transition: all 0.3s ease;
}

.action-btn:active {
  transform: scale(0.96);
  box-shadow: 0 2rpx 8rpx rgba(122, 168, 158, 0.3);
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .quick-grid {
    gap: 24rpx;
  }
  
  .work-card {
    padding: 32rpx;
  }
  
  .work-title {
    font-size: 32rpx;
  }
  
  .price-value {
    font-size: 36rpx;
  }
  
  .work-emoji {
    font-size: 80rpx;
    right: 32rpx;
    bottom: 32rpx;
  }
  
  .training-item {
    padding: 24rpx;
  }
}
</style>