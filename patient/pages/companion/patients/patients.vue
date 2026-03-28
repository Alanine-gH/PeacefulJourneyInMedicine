<template>
  <view class="container">
    <!-- 头部 -->
    <!-- <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">患者管理</view>
        <view class="header-right"></view>
      </view>
    </view> -->

    <!-- 搜索框 -->
    <view class="search-section">
      <view class="search-box">
        <text class="search-icon">🔍</text>
        <input 
          class="search-input" 
          v-model="searchKeyword" 
          placeholder="搜索患者姓名或电话"
          @input="handleSearch"
        />
        <text v-if="searchKeyword" class="clear-btn" @click="clearSearch">✕</text>
      </view>
    </view>

    <!-- 患者列表 -->
    <view class="patient-list">
      <view 
        v-for="patient in filteredPatients" 
        :key="patient.id"
        class="patient-item"
        @click="viewPatientDetail(patient)"
      >
        <view class="patient-avatar">
          <text class="avatar-icon">{{ patient.name.charAt(0) }}</text>
        </view>
        <view class="patient-info">
          <view class="patient-name">{{ patient.name }}</view>
          <view class="patient-detail">
            <text class="patient-phone">{{ patient.phone }}</text>
            <text class="patient-age">{{ patient.age }}岁</text>
            <text class="patient-gender">{{ patient.gender === 1 ? '男' : '女' }}</text>
          </view>
        </view>
        <view class="patient-arrow">›</view>
      </view>
      
      <!-- 空状态 -->
      <view v-if="filteredPatients.length === 0" class="empty-state">
        <text class="empty-icon">👤</text>
        <text class="empty-text">暂无患者信息</text>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getCompanionPatients } from '@/utils/companion-api.js'

export default {
  data() {
    return {
      searchKeyword: '',
      patients: [],
      loading: false
    }
  },
  computed: {
    filteredPatients() {
      if (!this.searchKeyword) {
        return this.patients
      }
      const keyword = this.searchKeyword.toLowerCase()
      return this.patients.filter(patient => 
        patient.name.toLowerCase().includes(keyword) ||
        patient.phone.includes(keyword)
      )
    }
  },
  onLoad() {
    this.loadPatients()
  },
  onPullDownRefresh() {
    this.loadPatients()
  },
  methods: {

    
    handleSearch() {
      // 搜索逻辑
    },
    
    clearSearch() {
      this.searchKeyword = ''
    },
    
    async loadPatients() {
      this.loading = true
      try {
        const res = await getCompanionPatients()
        if (res.code === 200 && res.data) {
          this.patients = (Array.isArray(res.data) ? res.data : []).map(p => ({
            id: p.id || '',
            name: p.name || '',
            phone: p.phone || '',
            gender: p.gender || 0,
            age: p.age || 0,
            diseaseDescription: p.diseaseDescription || '',
            appointmentHospital: p.appointmentHospital || '',
            appointmentDepartment: p.appointmentDepartment || '',
            userId: p.userId || ''
          }))
        }
      } catch (error) {
        console.error('获取患者信息失败:', error)
        uni.showToast({ title: '获取患者信息失败', icon: 'none' })
      } finally {
        this.loading = false
        uni.stopPullDownRefresh()
      }
    },
    
    viewPatientDetail(patient) {
      const query = [
        `id=${encodeURIComponent(patient.id)}`,
        `name=${encodeURIComponent(patient.name || '')}`,
        `phone=${encodeURIComponent(patient.phone || '')}`,
        `age=${patient.age || 0}`,
        `gender=${patient.gender || 0}`,
        `diseaseDescription=${encodeURIComponent(patient.diseaseDescription || '')}`,
        `appointmentHospital=${encodeURIComponent(patient.appointmentHospital || '')}`,
        `appointmentDepartment=${encodeURIComponent(patient.appointmentDepartment || '')}`
      ].join('&')
      uni.navigateTo({
        url: `/pages/companion/patients/patient-detail?${query}`
      })
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

/* 搜索框 */
.search-section {
  padding: 20rpx 30rpx;
  background-color: #fff;
}

.search-box {
  display: flex;
  align-items: center;
  background-color: #f5f5f5;
  border-radius: 40rpx;
  padding: 0 30rpx;
  height: 80rpx;
}

.search-icon {
  font-size: 28rpx;
  color: #999;
  margin-right: 20rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
  height: 100%;
}

.clear-btn {
  font-size: 24rpx;
  color: #999;
  margin-left: 20rpx;
  cursor: pointer;
}

/* 患者列表 */
.patient-list {
  padding: 20rpx 0;
}

.patient-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  margin-bottom: 20rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.patient-item:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.patient-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
}

.avatar-icon {
  font-size: 36rpx;
  color: white;
  font-weight: 600;
}

.patient-info {
  flex: 1;
}

.patient-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
}

.patient-detail {
  display: flex;
  gap: 20rpx;
  font-size: 24rpx;
  color: #666;
}

.patient-arrow {
  font-size: 32rpx;
  color: #999;
  margin-left: 20rpx;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
  color: #999;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>