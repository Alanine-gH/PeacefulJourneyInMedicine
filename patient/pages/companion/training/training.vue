<template>
  <view class="container">
    <!-- 头部 -->
 <!--   <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">技能培训</text>
        </view>
      </view>
    </view> -->

    <!-- 培训列表 -->
    <view class="training-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎓</text> 上岗培训
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
    <CompanionTabBar activeTab="mine"></CompanionTabBar>
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
  methods: {
    navigateTo(page) {
      switch(page) {
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
          uni.showToast({ title: `跳转到${page}页面`, icon: 'none' })
      }
    }
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

/* 培训列表 */
.training-section {
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
  background: #f8f9fa;
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  padding: 15rpx 30rpx;
  border-radius: 20rpx;
  font-size: 26rpx;
  font-weight: 500;
  box-shadow: 0 3rpx 10rpx rgba(77, 208, 225, 0.3);
}

/* 底部空白区域 */
.bottom-space {
  height: 120rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .training-item {
    padding: 20rpx;
  }
  
  .training-name {
    font-size: 28rpx;
  }
  
  .training-desc,
  .training-duration,
  .training-status {
    font-size: 22rpx;
  }
}
</style>