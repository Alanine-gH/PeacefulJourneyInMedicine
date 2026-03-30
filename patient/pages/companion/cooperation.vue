<template>
  <view class="container">
    <!-- 主要内容 -->
    <view class="main-content">
      <!-- 主题区域 -->
      <view class="hero-section">
        <view class="hero-content">
          <text class="hero-title">长期合作</text>
          <text class="hero-subtitle">医院合作 · 稳定收入</text>
          <view class="hero-buttons">
            <view class="primary-button" @click="learnMore">
              <text class="button-text">了解详情</text>
            </view>
            <view class="secondary-button" @click="recommend">
              <text class="button-text">推荐</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 内容卡片 -->
      <view class="content-section">
        <view class="card">
          <text class="card-title">合作优势</text>
          <view class="card-content">
            <view class="feature-item">
              <text class="feature-icon">🏥</text>
              <text class="feature-text">与多家知名医院建立长期合作关系</text>
            </view>
            <view class="feature-item">
              <text class="feature-icon">💼</text>
              <text class="feature-text">提供稳定的工作机会和收入来源</text>
            </view>
            <view class="feature-item">
              <text class="feature-icon">📈</text>
              <text class="feature-text">长期合作享受优先派单和晋升机会</text>
            </view>
            <view class="feature-item">
              <text class="feature-icon">🤝</text>
              <text class="feature-text">建立长期信任关系，客户满意度高</text>
            </view>
          </view>
        </view>

        <view class="card">
          <text class="card-title">合作模式</text>
          <view class="card-content">
            <view class="mode-item">
              <text class="mode-title">全职合作</text>
              <text class="mode-desc">每周工作40小时以上，享受更多福利</text>
            </view>
            <view class="mode-item">
              <text class="mode-title">兼职合作</text>
              <text class="mode-desc">灵活安排工作时间，按单计费</text>
            </view>
            <view class="mode-item">
              <text class="mode-title">长期签约</text>
              <text class="mode-desc">签订长期服务协议，享受稳定收入</text>
            </view>
          </view>
        </view>

        <view class="card">
          <text class="card-title">成功案例</text>
          <view class="card-content">
            <view class="case-item">
              <text class="case-name">张阿姨</text>
              <text class="case-desc">通过长期合作，月收入稳定在15000元以上</text>
            </view>
            <view class="case-item">
              <text class="case-name">李师傅</text>
              <text class="case-desc">与医院建立长期合作，获得优先派单机会</text>
            </view>
            <view class="case-item">
              <text class="case-name">王护士</text>
              <text class="case-desc">通过长期服务，客户满意度高，收入稳步增长</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部操作区 -->
    <view class="bottom-section">
      <view class="contact-info">
        <text class="contact-title">联系我们</text>
        <text class="contact-text">电话：400-123-4567</text>
        <text class="contact-text">微信：yiluanxin</text>
      </view>
      <view class="action-button" @click="applyNow">
        <text class="action-text">立即申请合作</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      loading: false
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    learnMore() {
      uni.showToast({
        title: '了解更多详情',
        icon: 'none'
      })
    },
    recommend() {
      uni.showToast({
        title: '推荐成功',
        icon: 'success'
      })
    },
    applyNow() {
      uni.showToast({
        title: '申请已提交',
        icon: 'success'
      })
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f4f2ee;
}

/* 主要内容 */
.main-content {
  flex: 1;
  padding: 30rpx;
}

/* 主题区域 */
.hero-section {
  background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
  border-radius: 20rpx;
  padding: 50rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(255, 152, 0, 0.3);
}

.hero-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.hero-title {
  font-size: 48rpx;
  font-weight: 700;
  color: #fff;
  margin-bottom: 20rpx;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.2);
}

.hero-subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 40rpx;
}

.hero-buttons {
  display: flex;
  gap: 20rpx;
}

.primary-button {
  background: rgba(255, 255, 255, 0.9);
  color: #F57C00;
  padding: 20rpx 40rpx;
  border-radius: 50rpx;
  font-size: 28rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.primary-button:hover {
  background: #fff;
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.15);
}

.secondary-button {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  padding: 20rpx 40rpx;
  border-radius: 50rpx;
  font-size: 28rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2rpx solid rgba(255, 255, 255, 0.5);
}

.secondary-button:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-4rpx);
}

.button-text {
  font-weight: 600;
}

/* 内容卡片 */
.content-section {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.card {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  border: 1rpx solid #e8e8e8;
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 24rpx rgba(255, 152, 0, 0.2);
}

.card-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 24rpx;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
  padding: 16rpx;
  background: #f4f2ee;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: #fff3e0;
  transform: translateX(8rpx);
}

.feature-icon {
  font-size: 36rpx;
  margin-top: 4rpx;
}

.feature-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.mode-item {
  padding: 20rpx;
  border-left: 4rpx solid #FF9800;
  background: #f4f2ee;
  border-radius: 0 12rpx 12rpx 0;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.mode-item:hover {
  background: #fff3e0;
  transform: translateX(8rpx);
}

.mode-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.mode-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.case-item {
  padding: 20rpx;
  background: #f4f2ee;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.case-item:hover {
  background: #fff3e0;
  transform: translateY(-4rpx);
}

.case-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.case-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

/* 底部操作区 */
.bottom-section {
  background: #fff;
  padding: 30rpx;
  border-top: 1rpx solid #f0f0f0;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

.contact-info {
  margin-bottom: 24rpx;
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.contact-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 12rpx;
}

.contact-text {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
  line-height: 1.4;
}

.action-button {
  background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
  color: #fff;
  padding: 24rpx;
  border-radius: 12rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 12rpx rgba(255, 152, 0, 0.3);
}

.action-button:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 16rpx rgba(255, 152, 0, 0.4);
}

.action-text {
  font-weight: 600;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .hero-section {
    padding: 40rpx;
  }

  .hero-title {
    font-size: 40rpx;
  }

  .card {
    padding: 24rpx;
  }

  .feature-item,
  .mode-item,
  .case-item {
    padding: 16rpx;
  }

  .feature-icon {
    font-size: 32rpx;
  }

  .feature-text {
    font-size: 24rpx;
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

</style>