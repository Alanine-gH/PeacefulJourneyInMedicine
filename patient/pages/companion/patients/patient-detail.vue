<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">患者详情</view>
        <view class="header-right"></view>
      </view>
    </view>

    <!-- 患者基本信息 -->
    <view class="section">
      <view class="section-title">基本信息</view>
      <view class="info-item">
        <view class="info-label">姓名</view>
        <view class="info-value">{{ patientInfo.name }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">性别</view>
        <view class="info-value">{{ patientInfo.gender === 1 ? '男' : '女' }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">年龄</view>
        <view class="info-value">{{ patientInfo.age }}岁</view>
      </view>
      <view class="info-item">
        <view class="info-label">手机号</view>
        <view class="info-value">{{ patientInfo.phone }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">地址</view>
        <view class="info-value">{{ patientInfo.address }}</view>
      </view>
    </view>

    <!-- 医疗信息 -->
    <view class="section">
      <view class="section-title">医疗信息</view>
      <view class="info-item">
        <view class="info-label">病史</view>
        <view class="info-value">{{ patientInfo.medicalHistory }}</view>
      </view>
    </view>

    <!-- 联系人信息 -->
    <view class="section">
      <view class="section-title">联系人信息</view>
      <view class="info-item">
        <view class="info-label">联系人</view>
        <view class="info-value">{{ patientInfo.contactPerson }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">联系电话</view>
        <view class="info-value">{{ patientInfo.contactPhone }}</view>
      </view>
    </view>



    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getCompanionOrders } from '@/utils/companion-api.js'

export default {
  data() {
    return {
      patientInfo: {
        id: '',
        name: '',
        phone: '',
        age: 0,
        gender: 0,
        address: '',
        medicalHistory: '',
        contactPerson: '',
        contactPhone: ''
      },
      loading: false
    }
  },
  onLoad(options) {
    const patientId = options.id
    this.getPatientInfo(patientId)
  },
  methods: {
    async getPatientInfo(patientId) {
      this.loading = true
      try {
        const res = await getCompanionOrders({ page: 1, pageSize: 100 })
        
        if (res.code === 200 && res.data) {
          const orderList = res.data.records || res.data.list || (Array.isArray(res.data) ? res.data : [])
          
          // 从订单中提取患者信息
          let patient = null
          for (const order of orderList) {
            const patientName = order.patientName || order.patient_name
            const patientPhone = order.patientPhone || order.patient_phone || ''
            const currentPatientId = `${patientName}_${patientPhone || 'unknown'}`
            
            if (currentPatientId === patientId) {
              patient = {
                id: currentPatientId,
                name: patientName,
                phone: patientPhone,
                age: order.patientAge || order.patient_age || 0,
                gender: order.patientGender || order.patient_gender || 0,
                address: order.patientAddress || order.patient_address || '',
                medicalHistory: '',
                contactPerson: '',
                contactPhone: ''
              }
              break
            }
          }
          
          if (patient) {
            this.patientInfo = patient
          } else {
            uni.showToast({ title: '未找到患者信息', icon: 'none' })
          }
        }
      } catch (error) {
        console.error('获取患者信息失败:', error)
        uni.showToast({ title: '获取患者信息失败', icon: 'none' })
      } finally {
        this.loading = false
      }
    },
    

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