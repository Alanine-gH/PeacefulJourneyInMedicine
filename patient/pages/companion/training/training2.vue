<template>
  <view class="container">
    <!-- 头部 -->
<!--    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">医疗知识基础</text>
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
          <text class="icon">📚</text> 相关知识
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