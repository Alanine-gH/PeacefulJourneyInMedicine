<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="患者详情" showBack="true"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 患者基本信息 -->
    <view class="section">
      <view class="section-title">基本信息</view>
      <view class="info-item">
        <view class="info-label">姓名</view>
        <view class="info-value">{{ patientInfo.name || '暂无' }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">性别</view>
        <view class="info-value">{{ patientInfo.gender === 1 ? '男' : patientInfo.gender === 2 ? '女' : '未知' }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">年龄</view>
        <view class="info-value">{{ patientInfo.age ? patientInfo.age + '岁' : '暂无' }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">手机号</view>
        <view class="info-value">{{ patientInfo.phone || '暂无' }}</view>
      </view>
    </view>

    <!-- 就诊信息 -->
    <view class="section">
      <view class="section-title">就诊信息</view>
      <view class="info-item" v-if="patientInfo.appointmentHospital">
        <view class="info-label">就诊医院</view>
        <view class="info-value">{{ patientInfo.appointmentHospital }}</view>
      </view>
      <view class="info-item" v-if="patientInfo.appointmentDepartment">
        <view class="info-label">就诊科室</view>
        <view class="info-value">{{ patientInfo.appointmentDepartment }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">病情描述</view>
        <view class="info-value">{{ patientInfo.diseaseDescription || '暂无' }}</view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
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
      patientInfo: {
        id: '',
        name: '',
        phone: '',
        age: 0,
        gender: 0,
        diseaseDescription: '',
        appointmentHospital: '',
        appointmentDepartment: ''
      }
    }
  },
  onLoad(options) {
    try {
      this.patientInfo = {
        id: options.id || '',
        name: decodeURIComponent(options.name || ''),
        phone: decodeURIComponent(options.phone || ''),
        age: options.age ? Number(options.age) : 0,
        gender: options.gender ? Number(options.gender) : 0,
        diseaseDescription: decodeURIComponent(options.diseaseDescription || ''),
        appointmentHospital: decodeURIComponent(options.appointmentHospital || ''),
        appointmentDepartment: decodeURIComponent(options.appointmentDepartment || '')
      }
    } catch (e) {
      console.error('解析患者信息失败:', e)
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 头部 */
.header {
  background-color: #8db8b6;
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.header-right {
  width: 60rpx;
}

/*  section */
.section {
  background: #fff;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  padding: 30rpx 30rpx 10rpx;
}

/* 信息项 */
.info-item {
  display: flex;
  align-items: center;
  padding: 25rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  width: 120rpx;
  font-size: 26rpx;
  color: #666;
}

.info-value {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}



/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* ── Shared theme overrides ── */
page { background-color: #f4f2ee !important; }

.container { background-color: #f4f2ee !important; min-height: 100vh; }

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
  box-shadow: 0 4rpx 20rpx rgba(100,120,140,0.10) !important;
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
  box-shadow: 0 4rpx 16rpx rgba(100,175,175,0.28) !important;
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
.order-status { color: #8db8b6 !important; }

/* Section titles */
.section-title { color: #3a3a4a !important; font-weight: 600 !important; }

/* Stat items */
.stat-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

/* Tab bar selected */
.tab-item.active,
.tab-item.active .tab-text { color: #8db8b6 !important; }

/* Profile avatar ring */
.profile-avatar {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 12rpx rgba(100,175,175,0.28) !important;
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
.carousel-item        { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }
.carousel-item.vip    { background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important; }
.carousel-item.full   { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }
.carousel-item.consult{ background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important; }
.carousel-item.result { background: linear-gradient(135deg, #d4a8b0 0%, #c09098 100%) !important; }
.carousel-item.medicine{ background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%) !important; }
.carousel-item.basic  { background: linear-gradient(135deg, #a8cec8 0%, #8db8b0 100%) !important; }

/* Step indicator */
.step-num { border-color: #8db8b6 !important; color: #8db8b6 !important; }
.step-item.active .step-num,
.step-item.completed .step-num {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important; border-color: transparent !important;
}
.step-line.active { background-color: #8db8b6 !important; }

/* Health exam header */
.header { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }

/* Package type tags */
.pkg-type-tag { background: #e8f4f4 !important; }
.pkg-type-tag .type-text { color: #6a9ea0 !important; }

/* Input focus ring */
.input-item:focus-within { border-color: #8db8b6 !important; box-shadow: 0 0 0 3rpx rgba(141,184,182,0.18) !important; }

</style>