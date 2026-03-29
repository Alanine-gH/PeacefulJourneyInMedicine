<template>
  <view class="container">
    <!-- 头部 -->
<!--    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">高薪兼职</text>
        </view>
        <view class="header-actions">
          <view class="back-button" @click="goBack">
            <text class="back-icon">←</text>
          </view>
        </view>
      </view>
    </view>
    -->
    <!-- 主要内容 -->
    <view class="main-content">
      <!-- 主题区域 -->
      <view class="hero-section">
        <view class="hero-content">
          <text class="hero-title">高薪兼职</text>
          <text class="hero-subtitle">急招陪护 · 200元/小时</text>
          <view class="hero-buttons">
            <view class="primary-button" @click="startOrder">
              <text class="button-text">立即接单</text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 内容卡片 -->
      <view class="content-section">
        <view class="card">
          <text class="card-title">薪资待遇</text>
          <view class="card-content">
            <view class="salary-item">
              <text class="salary-icon">💰</text>
              <text class="salary-amount">200元/小时</text>
              <text class="salary-desc">高于行业平均水平</text>
            </view>
            <view class="salary-item">
              <text class="salary-icon">📈</text>
              <text class="salary-amount">绩效奖金</text>
              <text class="salary-desc">根据服务质量额外奖励</text>
            </view>
            <view class="salary-item">
              <text class="salary-icon">⏰</text>
              <text class="salary-amount">灵活计费</text>
              <text class="salary-desc">按实际服务时间计算</text>
            </view>
          </view>
        </view>
        
        <view class="card">
          <text class="card-title">招聘要求</text>
          <view class="card-content">
            <view class="requirement-item">
              <text class="requirement-icon">✅</text>
              <text class="requirement-text">年龄18-55周岁，身体健康</text>
            </view>
            <view class="requirement-item">
              <text class="requirement-icon">✅</text>
              <text class="requirement-text">有爱心、耐心，责任心强</text>
            </view>
            <view class="requirement-item">
              <text class="requirement-icon">✅</text>
              <text class="requirement-text">具备基本护理知识优先</text>
            </view>
            <view class="requirement-item">
              <text class="requirement-icon">✅</text>
              <text class="requirement-text">良好的沟通能力</text>
            </view>
            <view class="requirement-item">
              <text class="requirement-icon">✅</text>
              <text class="requirement-text">熟悉医院流程优先</text>
            </view>
          </view>
        </view>
        
        <view class="card">
          <text class="card-title">工作内容</text>
          <view class="card-content">
            <view class="job-item">
              <text class="job-icon">🏥</text>
              <text class="job-title">陪同就医</text>
              <text class="job-desc">全程陪同患者就诊，协助办理手续</text>
            </view>
            <view class="job-item">
              <text class="job-icon">💊</text>
              <text class="job-title">用药提醒</text>
              <text class="job-desc">提醒患者按时服药，记录用药情况</text>
            </view>
            <view class="job-item">
              <text class="job-icon">🍎</text>
              <text class="job-title">生活照料</text>
              <text class="job-desc">协助患者饮食、起居等日常活动</text>
            </view>
            <view class="job-item">
              <text class="job-icon">📞</text>
              <text class="job-title">沟通协调</text>
              <text class="job-desc">与患者家属保持沟通，及时反馈情况</text>
            </view>
          </view>
        </view>
        
        <view class="card">
          <text class="card-title">工作时间</text>
          <view class="card-content">
            <view class="time-item">
              <text class="time-label">工作日</text>
              <text class="time-range">8:00-18:00（灵活安排）</text>
            </view>
            <view class="time-item">
              <text class="time-label">周末</text>
              <text class="time-range">9:00-17:00（按需接单）</text>
            </view>
            <view class="time-item">
              <text class="time-label">节假日</text>
              <text class="time-range">全天可接单（双倍薪资）</text>
            </view>
          </view>
        </view>
        
        <view class="card">
          <text class="card-title">成功案例</text>
          <view class="card-content">
            <view class="case-item">
              <text class="case-name">张师傅</text>
              <text class="case-desc">兼职陪护，月收入超过15000元</text>
            </view>
            <view class="case-item">
              <text class="case-name">李阿姨</text>
              <text class="case-desc">利用空闲时间接单，月额外收入8000元</text>
            </view>
            <view class="case-item">
              <text class="case-name">王护士</text>
              <text class="case-desc">专业技能突出，月收入达到20000元</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部操作区 -->
    <view class="bottom-section">
      <view class="contact-info">
        <text class="contact-title">招聘咨询</text>
        <text class="contact-text">电话：400-123-4567</text>
        <text class="contact-text">微信：yiluanxin</text>
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
    startOrder() {
      uni.navigateTo({
        url: '/pages/hall/hall'
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
  background-color: #f8f9fa;
}

/* 头部 */
.header {
  background: linear-gradient(135deg, #2196F3 0%, #1565C0 100%);
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
  background: linear-gradient(135deg, #2196F3 0%, #1565C0 100%);
  border-radius: 20rpx;
  padding: 50rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(33, 150, 243, 0.3);
  position: relative;
  overflow: hidden;
}

.hero-section::after {
  content: '👨‍⚕️';
  font-size: 200rpx;
  position: absolute;
  bottom: -40rpx;
  right: -40rpx;
  opacity: 0.2;
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
  color: #1565C0;
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
  box-shadow: 0 8rpx 24rpx rgba(33, 150, 243, 0.2);
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

.salary-item {
  display: flex;
  flex-direction: column;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.salary-item:hover {
  background: #e3f2fd;
  transform: translateX(8rpx);
}

.salary-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.salary-amount {
  font-size: 32rpx;
  font-weight: 600;
  color: #1565C0;
  margin-bottom: 8rpx;
}

.salary-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.requirement-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 16rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.requirement-item:hover {
  background: #e3f2fd;
  transform: translateX(8rpx);
}

.requirement-icon {
  font-size: 32rpx;
  color: #4CAF50;
}

.requirement-text {
  flex: 1;
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.job-item {
  display: flex;
  flex-direction: column;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  transition: all 0.3s ease;
}

.job-item:hover {
  background: #e3f2fd;
  transform: translateX(8rpx);
}

.job-icon {
  font-size: 48rpx;
  margin-bottom: 12rpx;
}

.job-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.job-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.time-item {
  padding: 20rpx;
  border-left: 4rpx solid #2196F3;
  background: #f8f9fa;
  border-radius: 0 12rpx 12rpx 0;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.time-item:hover {
  background: #e3f2fd;
  transform: translateX(8rpx);
}

.time-label {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.time-range {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.case-item {
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.case-item:hover {
  background: #e3f2fd;
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
  
  .salary-item,
  .requirement-item,
  .job-item,
  .time-item,
  .case-item {
    padding: 16rpx;
  }
  
  .salary-icon,
  .job-icon {
    font-size: 40rpx;
  }
  
  .requirement-icon {
    font-size: 28rpx;
  }
}
</style>
