<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="长期合作" showBack="true"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
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
import CompanionNavBar from '@/components/CompanionNavBar.vue'

export default {
  components: {
    CompanionNavBar
  },
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
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  min-height: 100vh;
  padding: 24rpx;
}

/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
}

/* 主要内容 */
.main-content {
  flex: 1;
  padding: 0;
}

/* 主题区域 */
.hero-section {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 28rpx;
  padding: 48rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(141, 184, 182, 0.3);
  position: relative;
  overflow: hidden;
}

.hero-section::before {
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

.hero-section::after {
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

.hero-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  position: relative;
  z-index: 1;
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
  color: #8db8b6;
  padding: 20rpx 48rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.primary-button:active {
  background: #fff;
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 16rpx rgba(0, 0, 0, 0.15);
}

.secondary-button {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  padding: 20rpx 48rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2rpx solid rgba(255, 255, 255, 0.5);
}

.secondary-button:active {
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
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.1);
  transition: all 0.3s ease;
}

.card:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(141, 184, 182, 0.2);
}

.card-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 24rpx;
  padding-bottom: 16rpx;
  border-bottom: 2rpx solid rgba(141, 184, 182, 0.1);
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
  padding: 20rpx;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.feature-item:active {
  background: rgba(141, 184, 182, 0.1);
  transform: translateX(8rpx);
}

.feature-icon {
  font-size: 40rpx;
  margin-top: 4rpx;
}

.feature-text {
  flex: 1;
  font-size: 26rpx;
  color: #6a6a7a;
  line-height: 1.5;
}

.mode-item {
  padding: 24rpx;
  border-left: 8rpx solid #8db8b6;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 0 16rpx 16rpx 0;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.mode-item:active {
  background: rgba(141, 184, 182, 0.1);
  transform: translateX(8rpx);
}

.mode-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 8rpx;
}

.mode-desc {
  font-size: 24rpx;
  color: #6a6a7a;
  line-height: 1.4;
}

.case-item {
  padding: 24rpx;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 16rpx;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.case-item:active {
  background: rgba(141, 184, 182, 0.1);
  transform: translateY(-4rpx);
}

.case-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 8rpx;
}

.case-desc {
  font-size: 24rpx;
  color: #6a6a7a;
  line-height: 1.4;
}

/* 底部操作区 */
.bottom-section {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20rpx);
  padding: 32rpx;
  border-radius: 22rpx;
  margin-top: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.1);
}

.contact-info {
  margin-bottom: 24rpx;
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid rgba(141, 184, 182, 0.1);
}

.contact-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 12rpx;
}

.contact-text {
  font-size: 24rpx;
  color: #6a6a7a;
  margin-bottom: 8rpx;
  line-height: 1.4;
}

.action-button {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  padding: 28rpx;
  border-radius: 22rpx;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 16rpx rgba(141, 184, 182, 0.28);
}

.action-button:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 24rpx rgba(141, 184, 182, 0.4);
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
    font-size: 36rpx;
  }

  .feature-text {
    font-size: 24rpx;
  }
}
</style>