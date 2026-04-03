<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="专业培训" showBack="true"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>

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
  padding: 20rpx 64rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.primary-button:active {
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

.course-item {
  display: flex;
  flex-direction: column;
  padding: 24rpx;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.course-item:active {
  background: rgba(141, 184, 182, 0.1);
  transform: translateX(8rpx);
}

.course-icon {
  font-size: 48rpx;
  margin-bottom: 16rpx;
}

.course-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 8rpx;
  border-bottom: none;
  padding-bottom: 0;
  margin-bottom: 8rpx;
}

.course-desc {
  font-size: 24rpx;
  color: #6a6a7a;
  line-height: 1.4;
}

.advantage-item {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
  padding: 20rpx;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.advantage-item:active {
  background: rgba(141, 184, 182, 0.1);
  transform: translateX(8rpx);
}

.advantage-icon {
  font-size: 40rpx;
  margin-top: 4rpx;
}

.advantage-text {
  flex: 1;
  font-size: 26rpx;
  color: #6a6a7a;
  line-height: 1.5;
}

.schedule-item {
  padding: 24rpx;
  border-left: 8rpx solid #8db8b6;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 0 16rpx 16rpx 0;
  margin-bottom: 16rpx;
  transition: all 0.3s ease;
}

.schedule-item:active {
  background: rgba(141, 184, 182, 0.1);
  transform: translateX(8rpx);
}

.schedule-time {
  font-size: 28rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 8rpx;
}

.schedule-content {
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
    font-size: 36rpx;
  }
}
</style>