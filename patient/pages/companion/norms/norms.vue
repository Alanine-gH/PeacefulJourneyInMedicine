<template>
  <view class="container">
    <!-- 头部 -->
<!--    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">陪护服务规范</text>
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
          src="/static/陪护.mp4" 
          controls 
          class="video-player"
          poster="/static/video-placeholder.png"
        ></video>
        <view class="video-info">
          <text class="video-title">陪护服务规范培训</text>
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
          <view class="knowledge-title">服务流程</view>
          <view class="knowledge-desc">
            <text>1. 接单确认：收到订单后及时确认，了解患者基本信息和需求</text>
            <text>2. 首次沟通：与患者及家属建立联系，了解具体服务内容</text>
            <text>3. 服务实施：按照规范提供专业陪护服务</text>
            <text>4. 服务记录：详细记录服务内容和患者状况</text>
            <text>5. 服务结束：与家属交接，确认服务满意度</text>
          </view>
        </view>
        
        <view class="knowledge-item">
          <view class="knowledge-title">服务标准</view>
          <view class="knowledge-desc">
            <text>• 着装整洁，佩戴工作证</text>
            <text>• 态度温和，语言文明</text>
            <text>• 专业操作，规范服务</text>
            <text>• 保护患者隐私</text>
            <text>• 及时沟通，反馈情况</text>
          </view>
        </view>
        
        <view class="knowledge-item">
          <view class="knowledge-title">注意事项</view>
          <view class="knowledge-desc">
            <text>• 严格遵守医院规章制度</text>
            <text>• 不擅自离开工作岗位</text>
            <text>• 不随意翻动患者物品</text>
            <text>• 遇到紧急情况及时联系医护人员</text>
            <text>• 保持服务区域清洁卫生</text>
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