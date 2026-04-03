<template>
  <view class="container">
    <!-- 导航栏 -->
    <view class="navbar">
      <view class="nav-content">
        <view class="nav-back" @click="goBack">
          <text>‹</text>
        </view>
        <text class="nav-title">专业培训</text>
        <view class="nav-placeholder"></view>
      </view>
    </view>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 顶部卡片 -->
    <view class="top-card">
      <view class="card-content">
        <text class="card-title">专业培训</text>
        <text class="card-subtitle">技能提升 · 免费培训</text>
        <view class="card-btn" @click="register">报名参加</view>
      </view>
    </view>

    <!-- 培训内容 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">培训内容</text>
      </view>
      <view class="content-list">
        <view class="content-item" v-for="item in trainingContent" :key="item.id">
          <view class="content-icon" :class="item.bgClass">
            <text>{{ item.icon }}</text>
          </view>
          <view class="content-info">
            <text class="content-title">{{ item.title }}</text>
            <text class="content-desc">{{ item.description }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 培训优势 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">培训优势</text>
      </view>
      <view class="advantages-section">
        <view class="advantage-item" v-for="item in advantages" :key="item.id">
          <view class="advantage-icon" :class="item.bgClass">
            <text>{{ item.icon }}</text>
          </view>
          <text class="advantage-text">{{ item.text }}</text>
        </view>
      </view>
    </view>

    <!-- 培训安排 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">培训安排</text>
      </view>
      <view class="schedule-list">
        <view class="schedule-item" v-for="item in schedules" :key="item.id" :class="[item.borderClass, item.bgClass]">
          <view class="schedule-info">
            <text class="schedule-title">
              {{ item.title }} <text class="schedule-duration">{{ item.duration }}</text>
            </text>
            <text class="schedule-subtitle">{{ item.subtitle }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部联系信息 -->
    <view class="contact-section">
      <text class="contact-text">
        培训咨询电话：<text class="contact-highlight">400-123-4567</text> 微信：<text class="contact-highlight">yiluanxin</text>
      </text>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-btn" @click="applyTraining">
      立即报名培训
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      trainingContent: [
        {
          id: 1,
          icon: '📚',
          title: '医疗知识培训',
          description: '了解常见疾病、急救知识、医疗术语等',
          bgClass: 'bg-blue'
        },
        {
          id: 2,
          icon: '👥',
          title: '护理技能培训',
          description: '学习专业护理技巧、康复指导等',
          bgClass: 'bg-orange'
        },
        {
          id: 3,
          icon: '💬',
          title: '沟通技巧培训',
          description: '提升与患者及家属的沟通能力',
          bgClass: 'bg-pink'
        },
        {
          id: 4,
          icon: '💻',
          title: '系统操作培训',
          description: '学习使用陪护服务系统和工具',
          bgClass: 'bg-green'
        }
      ],
      advantages: [
        {
          id: 1,
          icon: '🎓',
          text: '专业师资团队，经验丰富',
          bgClass: 'bg-purple'
        },
        {
          id: 2,
          icon: '🏆',
          text: '全程免费培训，无任何费用',
          bgClass: 'bg-blue'
        },
        {
          id: 3,
          icon: '🎯',
          text: '理论结合实践，效果显著',
          bgClass: 'bg-orange'
        },
        {
          id: 4,
          icon: '📄',
          text: '颁发培训证书，提升竞争力',
          bgClass: 'bg-green'
        }
      ],
      schedules: [
        {
          id: 1,
          title: '第一阶段',
          subtitle: '基础知识学习',
          duration: '(2周)',
          borderClass: 'border-blue',
          bgClass: 'bg-blue-light'
        },
        {
          id: 2,
          title: '第二阶段',
          subtitle: '技能实操训练',
          duration: '(2周)',
          borderClass: 'border-green',
          bgClass: 'bg-green-light'
        },
        {
          id: 3,
          title: '第三阶段',
          subtitle: '考核与认证',
          duration: '(1周)',
          borderClass: 'border-purple',
          bgClass: 'bg-purple-light'
        }
      ]
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    register() {
      uni.showToast({
        title: '报名参加功能开发中',
        icon: 'none'
      })
    },
    applyTraining() {
      uni.showToast({
        title: '报名培训功能开发中',
        icon: 'none'
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

/* 导航栏 */
.navbar {
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.5), rgba(150, 196, 180, 0.4));
  backdrop-filter: blur(20rpx);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.nav-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32rpx 24rpx;
}

.nav-back {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  color: #fff;
  transition: transform 0.3s ease;
}

.nav-back:active {
  transform: scale(1.1);
}

.nav-title {
  flex: 1;
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  text-align: center;
  margin-left: -48rpx;
}

.nav-placeholder {
  width: 48rpx;
}

/* 顶部空白区域 */
.top-space {
  height: 120rpx;
}

/* 顶部卡片 */
.top-card {
  background: linear-gradient(135deg, #9DD4C8, #78B4A8);
  border-radius: 28rpx;
  padding: 48rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(120, 180, 168, 0.3);
  position: relative;
  overflow: hidden;
}

.top-card::before {
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

.top-card::after {
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

.card-content {
  position: relative;
  z-index: 1;
  text-align: center;
}

.card-title {
  font-size: 40rpx;
  font-weight: 700;
  color: #fff;
  margin-bottom: 16rpx;
  display: block;
}

.card-subtitle {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 32rpx;
  display: block;
}

.card-btn {
  background: #fff;
  color: #5B6B78;
  padding: 16rpx 64rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  font-weight: 600;
  display: inline-block;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.card-btn:active {
  transform: scale(1.05);
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
}

/* 通用 section 样式 */
.section {
  margin-bottom: 24rpx;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
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
}

/* 培训内容 */
.content-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.content-item {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  display: flex;
  align-items: flex-start;
  gap: 24rpx;
}

.content-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.content-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.content-icon.bg-blue {
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.3), rgba(174, 196, 223, 0.25));
}

.content-icon.bg-orange {
  background: linear-gradient(135deg, rgba(230, 188, 165, 0.3), rgba(214, 166, 143, 0.25));
}

.content-icon.bg-pink {
  background: linear-gradient(135deg, rgba(232, 187, 209, 0.3), rgba(223, 174, 196, 0.25));
}

.content-icon.bg-green {
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.3), rgba(150, 196, 180, 0.25));
}

.content-info {
  flex: 1;
}

.content-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #4A5A68;
  margin-bottom: 8rpx;
  display: block;
}

.content-desc {
  font-size: 24rpx;
  color: #8A9BA8;
  line-height: 32rpx;
}

/* 培训优势 */
.advantages-section {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.advantage-item {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.advantage-icon {
  width: 48rpx;
  height: 48rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
}

.advantage-icon.bg-purple {
  background: linear-gradient(135deg, rgba(200, 184, 214, 0.25), rgba(184, 168, 201, 0.2));
}

.advantage-icon.bg-blue {
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.25), rgba(174, 196, 223, 0.2));
}

.advantage-icon.bg-orange {
  background: linear-gradient(135deg, rgba(230, 188, 165, 0.25), rgba(214, 166, 143, 0.2));
}

.advantage-icon.bg-green {
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.25), rgba(150, 196, 180, 0.2));
}

.advantage-text {
  flex: 1;
  font-size: 26rpx;
  color: #6B7C8A;
  line-height: 36rpx;
}

/* 培训安排 */
.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.schedule-item {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  border-left: 16rpx solid;
}

.schedule-item.border-blue {
  border-left-color: #BBD1E8;
}

.schedule-item.border-green {
  border-left-color: #A8D4C4;
}

.schedule-item.border-purple {
  border-left-color: #C8B8D6;
}

.schedule-item.bg-blue-light {
  background: rgba(187, 209, 232, 0.1);
}

.schedule-item.bg-green-light {
  background: rgba(168, 212, 196, 0.1);
}

.schedule-item.bg-purple-light {
  background: rgba(200, 184, 214, 0.1);
}

.schedule-info {
  flex: 1;
}

.schedule-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #4A5A68;
  margin-bottom: 8rpx;
  display: block;
}

.schedule-duration {
  font-size: 24rpx;
  color: #8A9BA8;
  font-weight: normal;
}

.schedule-subtitle {
  font-size: 24rpx;
  color: #8A9BA8;
}

/* 联系信息 */
.contact-section {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  margin-bottom: 24rpx;
}

.contact-text {
  font-size: 24rpx;
  color: #8A9BA8;
  text-align: center;
  line-height: 36rpx;
}

.contact-highlight {
  color: #7AA89E;
  font-weight: 500;
}

/* 底部按钮 */
.bottom-btn {
  background: linear-gradient(135deg, #9DD4C8, #78B4A8);
  color: #fff;
  padding: 32rpx;
  border-radius: 22rpx;
  font-size: 32rpx;
  font-weight: 600;
  text-align: center;
  box-shadow: 0 8rpx 32rpx rgba(120, 180, 168, 0.25);
  transition: all 0.3s ease;
}

.bottom-btn:active {
  transform: scale(0.98);
  box-shadow: 0 4rpx 16rpx rgba(120, 180, 168, 0.25);
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .top-card {
    padding: 32rpx;
  }
  
  .card-title {
    font-size: 36rpx;
  }
  
  .card-subtitle {
    font-size: 24rpx;
  }
  
  .content-item,
  .schedule-item {
    padding: 24rpx;
  }
  
  .advantages-section {
    padding: 24rpx;
  }
  
  .content-title,
  .schedule-title {
    font-size: 28rpx;
  }
}
</style>