<template>
  <view class="container">
    <!-- 视频部分 -->
    <view class="video-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎬</text>
          培训视频
        </text>
      </view>
      <view class="video-container">
        <video
            src="/static/医疗知识基础.mp4"
            controls
            class="video-player"
            poster="/static/video-placeholder.png"
        ></video>
        <view class="video-info">
          <text class="video-title">医疗知识基础培训</text>
          <text class="video-desc">时长：4小时 | 必修课程</text>
        </view>
      </view>
    </view>

    <!-- 相关知识部分 -->
    <view class="knowledge-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">📚</text>
          相关知识
        </text>
      </view>
      <view class="knowledge-content">
        <view class="knowledge-item">
          <view class="knowledge-title">基础知识</view>
          <view class="knowledge-desc">
            <text>• 人体基本结构和生理功能</text>
            <text>• 常见疾病的基本症状和表现</text>
            <text>• 常用医疗术语和缩写</text>
            <text>• 基础医学伦理知识</text>
          </view>
        </view>

        <view class="knowledge-item">
          <view class="knowledge-title">护理技能</view>
          <view class="knowledge-desc">
            <text>• 生命体征监测方法</text>
            <text>• 基础伤口护理</text>
            <text>• 正确的体位摆放</text>
            <text>• 协助患者活动的方法</text>
          </view>
        </view>

        <view class="knowledge-item">
          <view class="knowledge-title">注意事项</view>
          <view class="knowledge-desc">
            <text>• 不擅自进行医疗操作</text>
            <text>• 及时向医护人员汇报患者情况</text>
            <text>• 正确使用医疗设备</text>
            <text>• 保持学习更新医疗知识</text>
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
  components: {CompanionTabBar},
  data() {
    return {}
  },
  onLoad() {
    // 页面加载时的逻辑
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
  padding-bottom: 120rpx;
}

/* 视频部分 */
.video-section {
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

.video-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.video-player {
  width: 100%;
  height: 400rpx;
  border-radius: 15rpx;
  background-color: #000;
}

.video-info {
  margin-top: 20rpx;
  text-align: center;
  width: 100%;
}

.video-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
  display: block;
}

.video-desc {
  font-size: 24rpx;
  color: #666;
  display: block;
}

/* 相关知识部分 */
.knowledge-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.knowledge-content {
  display: flex;
  flex-direction: column;
  gap: 25rpx;
}

.knowledge-item {
  background: #f4f2ee;
  padding: 25rpx;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.knowledge-item:hover {
  background: #e9ecef;
}

.knowledge-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 15rpx;
  padding-bottom: 10rpx;
  border-bottom: 1rpx solid #e0e0e0;
}

.knowledge-desc {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.knowledge-desc text {
  font-size: 24rpx;
  color: #666;
  line-height: 1.5;
}

/* 底部空白区域 */
.bottom-space {
  height: 120rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .video-player {
    height: 300rpx;
  }

  .knowledge-item {
    padding: 20rpx;
  }

  .knowledge-title {
    font-size: 26rpx;
  }

  .knowledge-desc text {
    font-size: 22rpx;
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