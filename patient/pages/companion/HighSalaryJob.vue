<template>
  <view class="container">
    <!-- 导航栏 -->
    <view class="navbar">
      <view class="nav-content">
        <view class="nav-back" @click="goBack">
          <text>‹</text>
        </view>
        <text class="nav-title">高薪兼职</text>
        <view class="nav-placeholder"></view>
      </view>
    </view>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 顶部卡片 -->
    <view class="top-card">
      <view class="card-content">
        <view class="card-icon">🩺</view>
        <text class="card-title">高薪兼职</text>
        <text class="card-subtitle">急需陪护 · 200元/小时</text>
        <view class="card-btn" @click="applyJob">立即接单</view>
      </view>
    </view>

    <!-- 薪资待遇 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">薪资待遇</text>
      </view>
      <view class="salary-list">
        <view class="salary-item" v-for="item in salaryItems" :key="item.id">
          <view class="salary-icon" :class="item.bgClass">
            <text>{{ item.icon }}</text>
          </view>
          <view class="salary-info">
            <text class="salary-title" :class="item.titleClass">{{ item.title }}</text>
            <text class="salary-desc">{{ item.description }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 招聘要求 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">招聘要求</text>
      </view>
      <view class="requirements">
        <view class="requirement-item" v-for="item in requirements" :key="item.id">
          <view class="requirement-icon">
            <text>{{ item.icon }}</text>
          </view>
          <text class="requirement-text">{{ item.text }}</text>
        </view>
      </view>
    </view>

    <!-- 工作内容 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">工作内容</text>
      </view>
      <view class="work-list">
        <view class="work-item" v-for="item in workContent" :key="item.id">
          <view class="work-icon" :class="item.bgClass">
            <text>{{ item.icon }}</text>
          </view>
          <view class="work-info">
            <text class="work-title">{{ item.title }}</text>
            <text class="work-desc">{{ item.description }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部联系信息 -->
    <view class="contact-section">
      <text class="contact-text">
        招聘咨询电话：<text class="contact-highlight">400-123-4567</text> 微信：<text class="contact-highlight">yiluanxin</text>
      </text>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-btn" @click="submitResume">
      立即投递简历
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      salaryItems: [
        {
          id: 1,
          icon: '💰',
          title: '200元/小时',
          description: '高于行业平均水平',
          bgClass: 'bg-orange',
          titleClass: 'text-orange'
        },
        {
          id: 2,
          icon: '📈',
          title: '绩效奖金',
          description: '根据服务质量获得额外奖励',
          bgClass: 'bg-blue',
          titleClass: 'text-blue'
        },
        {
          id: 3,
          icon: '⏰',
          title: '灵活计费',
          description: '按实际服务时间计算',
          bgClass: 'bg-pink',
          titleClass: 'text-pink'
        }
      ],
      requirements: [
        {
          id: 1,
          text: '年龄18-55周岁，身体健康',
          icon: '✓'
        },
        {
          id: 2,
          text: '有爱心、耐心、责任心强',
          icon: '❤️'
        },
        {
          id: 3,
          text: '具备基本护理知识优先',
          icon: '🧠'
        },
        {
          id: 4,
          text: '良好的沟通能力',
          icon: '💬'
        },
        {
          id: 5,
          text: '熟悉医院流程优先',
          icon: '✓'
        }
      ],
      workContent: [
        {
          id: 1,
          icon: '🩺',
          title: '陪同就医',
          description: '全程陪同患者看诊，协助办理手续',
          bgClass: 'bg-blue'
        },
        {
          id: 2,
          icon: '💊',
          title: '用药提醒',
          description: '提醒患者按时服药，记录用药情况',
          bgClass: 'bg-pink'
        },
        {
          id: 3,
          icon: '🍎',
          title: '生活照料',
          description: '协助患者饮食，起居等日常活动',
          bgClass: 'bg-orange'
        }
      ]
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    applyJob() {
      uni.showToast({
        title: '立即接单功能开发中',
        icon: 'none'
      })
    },
    submitResume() {
      uni.showToast({
        title: '简历投递功能开发中',
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
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.5), rgba(174, 196, 223, 0.4));
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
  background: linear-gradient(135deg, #A8C8E8, #88A8C8);
  border-radius: 28rpx;
  padding: 48rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(136, 168, 200, 0.3);
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

.card-icon {
  position: absolute;
  top: 24rpx;
  right: 32rpx;
  font-size: 120rpx;
  opacity: 0.3;
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

/* 薪资待遇 */
.salary-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.salary-item {
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

.salary-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.salary-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.salary-icon.bg-orange {
  background: linear-gradient(135deg, rgba(230, 188, 165, 0.3), rgba(214, 166, 143, 0.25));
}

.salary-icon.bg-blue {
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.3), rgba(174, 196, 223, 0.25));
}

.salary-icon.bg-pink {
  background: linear-gradient(135deg, rgba(232, 187, 209, 0.3), rgba(223, 174, 196, 0.25));
}

.salary-info {
  flex: 1;
}

.salary-title {
  font-size: 32rpx;
  font-weight: 600;
  margin-bottom: 8rpx;
  display: block;
}

.salary-title.text-orange {
  color: #C9967F;
}

.salary-title.text-blue {
  color: #8299B8;
}

.salary-title.text-pink {
  color: #B88299;
}

.salary-desc {
  font-size: 24rpx;
  color: #8A9BA8;
  line-height: 32rpx;
}

/* 招聘要求 */
.requirements {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.requirement-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.requirement-icon {
  width: 24rpx;
  height: 24rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.3), rgba(150, 196, 180, 0.25));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16rpx;
  color: #7AA89E;
}

.requirement-text {
  font-size: 26rpx;
  color: #6B7C8A;
  flex: 1;
}

/* 工作内容 */
.work-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.work-item {
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

.work-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.work-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.work-icon.bg-blue {
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.3), rgba(174, 196, 223, 0.25));
}

.work-icon.bg-pink {
  background: linear-gradient(135deg, rgba(232, 187, 209, 0.3), rgba(223, 174, 196, 0.25));
}

.work-icon.bg-orange {
  background: linear-gradient(135deg, rgba(230, 188, 165, 0.3), rgba(214, 166, 143, 0.25));
}

.work-info {
  flex: 1;
}

.work-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #4A5A68;
  margin-bottom: 8rpx;
  display: block;
}

.work-desc {
  font-size: 24rpx;
  color: #8A9BA8;
  line-height: 32rpx;
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
  color: #8299B8;
  font-weight: 500;
}

/* 底部按钮 */
.bottom-btn {
  background: linear-gradient(135deg, #A8C8E8, #88A8C8);
  color: #fff;
  padding: 32rpx;
  border-radius: 22rpx;
  font-size: 32rpx;
  font-weight: 600;
  text-align: center;
  box-shadow: 0 8rpx 32rpx rgba(136, 168, 200, 0.25);
  transition: all 0.3s ease;
}

.bottom-btn:active {
  transform: scale(0.98);
  box-shadow: 0 4rpx 16rpx rgba(136, 168, 200, 0.25);
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
  
  .salary-item,
  .work-item {
    padding: 24rpx;
  }
  
  .salary-title,
  .work-title {
    font-size: 28rpx;
  }
  
  .requirements {
    padding: 24rpx;
  }
  
  .requirement-text {
    font-size: 24rpx;
  }
}
</style>