<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="患者管理" showBack="true"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
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
import CompanionNavBar from '@/components/CompanionNavBar.vue'
import {getCompanionPatients} from '@/utils/companion-api.js'

export default {
  components: {
    CompanionNavBar
  },
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
        uni.showToast({title: '获取患者信息失败', icon: 'none'})
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
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 搜索框 */
.search-section {
  padding: 20rpx 30rpx;
  background-color: #fff;
}

.search-box {
  display: flex;
  align-items: center;
  background-color: #f4f2ee;
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
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
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
page {
  background-color: #f4f2ee !important;
}

.container {
  background-color: #f4f2ee !important;
  min-height: 100vh;
}

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
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.10) !important;
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
  box-shadow: 0 4rpx 16rpx rgba(100, 175, 175, 0.28) !important;
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
.order-status {
  color: #8db8b6 !important;
}

/* Section titles */
.section-title {
  color: #3a3a4a !important;
  font-weight: 600 !important;
}

/* Stat items */
.stat-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

/* Tab bar selected */
.tab-item.active,
.tab-item.active .tab-text {
  color: #8db8b6 !important;
}

/* Profile avatar ring */
.profile-avatar {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 12rpx rgba(100, 175, 175, 0.28) !important;
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
.carousel-item {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

.carousel-item.vip {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important;
}

.carousel-item.full {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

.carousel-item.consult {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important;
}

.carousel-item.result {
  background: linear-gradient(135deg, #d4a8b0 0%, #c09098 100%) !important;
}

.carousel-item.medicine {
  background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%) !important;
}

.carousel-item.basic {
  background: linear-gradient(135deg, #a8cec8 0%, #8db8b0 100%) !important;
}

/* Step indicator */
.step-num {
  border-color: #8db8b6 !important;
  color: #8db8b6 !important;
}

.step-item.active .step-num,
.step-item.completed .step-num {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important;
  border-color: transparent !important;
}

.step-line.active {
  background-color: #8db8b6 !important;
}

/* Health exam header */
.header {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

/* Package type tags */
.pkg-type-tag {
  background: #e8f4f4 !important;
}

.pkg-type-tag .type-text {
  color: #6a9ea0 !important;
}

/* Input focus ring */
.input-item:focus-within {
  border-color: #8db8b6 !important;
  box-shadow: 0 0 0 3rpx rgba(141, 184, 182, 0.18) !important;
}

</style>