<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">专业培训</text>
        </view>
        <view class="header-actions">
          <view class="back-button" @click="goBack">
            <text class="back-icon">←</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 主要内容 -->
    <view class="main-content">
      <!-- 主题区域 -->
      <view class="hero-section">
        <view class="hero-content">
          <text class="hero-title">专业培训</text>
          <text class="hero-subtitle">技能提升 · 免费培训</text>
          <view class="hero-buttons">
            <view class="primary-button" @click="register">
              <text class="button-text">报名参加</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 内容卡片 -->
      <view class="content-section">
        <view class="card">
          <text class="card-title">培训内容</text>
          <view class="card-content">
            <view class="course-item">
              <text class="course-icon">🏥</text>
              <text class="course-title">医疗知识培训</text>
              <text class="course-desc">了解常见疾病、急救知识、医疗术语等</text>
            </view>
            <view class="course-item">
              <text class="course-icon">👩‍⚕️</text>
              <text class="course-title">护理技能培训</text>
              <text class="course-desc">学习专业护理技巧、康复指导等</text>
            </view>
            <view class="course-item">
              <text class="course-icon">💬</text>
              <text class="course-title">沟通技巧培训</text>
              <text class="course-desc">提升与患者及家属的沟通能力</text>
            </view>
            <view class="course-item">
              <text class="course-icon">📱</text>
              <text class="course-title">系统操作培训</text>
              <text class="course-desc">学习使用陪护服务系统和工具</text>
            </view>
          </view>
        </view>

        <view class="card">
          <text class="card-title">培训优势</text>
          <view class="card-content">
            <view class="advantage-item">
              <text class="advantage-icon">🎓</text>
              <text class="advantage-text">专业师资团队，经验丰富</text>
            </view>
            <view class="advantage-item">
              <text class="advantage-icon">🆓</text>
              <text class="advantage-text">全程免费培训，无任何费用</text>
            </view>
            <view class="advantage-item">
              <text class="advantage-icon">📊</text>
              <text class="advantage-text">理论结合实践，效果显著</text>
            </view>
            <view class="advantage-item">
              <text class="advantage-icon">📄</text>
              <text class="advantage-text">颁发培训证书，提升竞争力</text>
            </view>
          </view>
        </view>

        <view class="card">
          <text class="card-title">培训安排</text>
          <view class="card-content">
            <view class="schedule-item">
              <text class="schedule-time">第一阶段</text>
              <text class="schedule-content">基础知识学习（2周）</text>
            </view>
            <view class="schedule-item">
              <text class="schedule-time">第二阶段</text>
              <text class="schedule-content">技能实操训练（2周）</text>
            </view>
            <view class="schedule-item">
              <text class="schedule-time">第三阶段</text>
              <text class="schedule-content">考核与认证（1周）</text>
            </view>
          </view>
        </view>

        <view class="card">
          <text class="card-title">成功案例</text>
          <view class="card-content">
            <view class="case-item">
              <text class="case-name">刘阿姨</text>
              <text class="case-desc">通过培训后，月收入提升30%</text>
            </view>
            <view class="case-item">
              <text class="case-name">陈师傅</text>
              <text class="case-desc">获得护理技能认证，成为金牌陪护</text>
            </view>
            <view class="case-item">
              <text class="case-name">赵护士</text>
              <text class="case-desc">培训后专业能力提升，客户满意度显著提高</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部操作区 -->
    <view class="bottom-section">
      <view class="contact-info">
        <text class="contact-title">培训咨询</text>
        <text class="contact-text">电话：400-123-4567</text>
        <text class="contact-text">微信：yiluanxin</text>
      </view>
      <view class="action-button" @click="registerNow">
        <text class="action-text">立即报名培训</text>
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
    register() {
      uni.showToast({
        title: '报名成功',
        icon: 'success'
      })
    },
    registerNow() {
      uni.showToast({
        title: '报名已提交',
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

/* 头部 */
.header {
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
}

.logo-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.back-button {
  width: 60rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-button:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: scale(1.05);
}

.back-icon {
  font-size: 32rpx;
  color: #fff;
  font-weight: bold;
}

/* 主要内容 */
.main-content {
  flex: 1;
  padding: 30rpx;
}

/* 主题区域 */
.hero-section {
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
  border-radius: 20rpx;
  padding: 50rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(76, 175, 80, 0.3);
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
  color: #2E7D32;
  padding: 20rpx 60rpx;
  border-radius: 50rpx;
  font-size: 28rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.primary-button:hover {
  background: #fff;
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.15);
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
  box-shadow: 0 8rpx 24rpx rgba(76, 175, 80, 0.2);
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

.course-item {
  display: flex;
  flex-direction: column;
  padding: 20rpx;
  background: #f4f2ee;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.course-item:hover {
  background: #e8f5e9;
  transform: translateX(8rpx);
}

.course-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.course-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.course-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.advantage-item {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
  padding: 16rpx;
  background: #f4f2ee;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.advantage-item:hover {
  background: #e8f5e9;
  transform: translateX(8rpx);
}

.advantage-icon {
  font-size: 36rpx;
  margin-top: 4rpx;
}

.advantage-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.schedule-item {
  padding: 20rpx;
  border-left: 4rpx solid #4CAF50;
  background: #f4f2ee;
  border-radius: 0 12rpx 12rpx 0;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.schedule-item:hover {
  background: #e8f5e9;
  transform: translateX(8rpx);
}

.schedule-time {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.schedule-content {
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
  background: #e8f5e9;
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
  background: linear-gradient(135deg, #4CAF50 0%, #2E7D32 100%);
  color: #fff;
  padding: 24rpx;
  border-radius: 12rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.3);
}

.action-button:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 16rpx rgba(76, 175, 80, 0.4);
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

  .course-item,
  .advantage-item,
  .schedule-item,
  .case-item {
    padding: 16rpx;
  }

  .course-icon {
    font-size: 40rpx;
  }

  .advantage-icon {
    font-size: 32rpx;
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