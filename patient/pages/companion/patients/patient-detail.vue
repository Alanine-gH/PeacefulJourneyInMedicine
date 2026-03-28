<template>
  <view class="container">
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
export default {
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
  background-color: #f8f9fa;
  min-height: 100vh;
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



/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>