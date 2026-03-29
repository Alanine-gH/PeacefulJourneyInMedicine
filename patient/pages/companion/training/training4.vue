<template>
  <view class="container">
    <!-- 头部 -->
<!--    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">服务礼仪</text>
        </view>
      </view>
    </view> -->

    <!-- 视频部分 -->
    <view class="video-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎬</text> 培训视频
        </text>
      </view>
      <view class="video-container">
        <video 
          src="/static/服务礼仪.mp4" 
          controls 
          class="video-player"
          poster="/static/video-placeholder.png"
        ></video>
        <view class="video-info">
          <text class="video-title">服务礼仪培训</text>
          <text class="video-desc">时长：2小时 | 必修课程</text>
        </view>
      </view>
    </view>

    <!-- 相关知识部分 -->
    <view class="knowledge-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">📚</text> 相关知识
        </text>
      </view>
      <view class="knowledge-content">
        <view class="knowledge-item">
          <view class="knowledge-title">仪容仪表</view>
          <view class="knowledge-desc">
            <text>• 着装整洁，符合职业要求</text>
            <text>• 保持个人卫生，指甲干净</text>
            <text>• 头发整齐，不披散</text>
            <text>• 淡妆上岗，不浓妆艳抹</text>
          </view>
        </view>
        
        <view class="knowledge-item">
          <view class="knowledge-title">沟通技巧</view>
          <view class="knowledge-desc">
            <text>• 主动问候，使用礼貌用语</text>
            <text>• 倾听患者需求，不打断</text>
            <text>• 语言温和，语速适中</text>
            <text>• 肢体语言自然，微笑服务</text>
          </view>
        </view>
        
        <view class="knowledge-item">
          <view class="knowledge-title">服务规范</view>
          <view class="knowledge-desc">
            <text>• 尊重患者隐私</text>
            <text>• 保护患者尊严</text>
            <text>• 及时响应患者需求</text>
            <text>• 保持专业态度</text>
            <text>• 团队协作，互相支持</text>
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
  components: { CompanionTabBar },
  data() {
    return {
    }
  },
  onLoad() {
    // 页面加载时的逻辑
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
  padding-bottom: 120rpx;
}

/* 头部 */
.header {
  background-color: #4DD0E1;
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
  background: #f8f9fa;
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
</style>