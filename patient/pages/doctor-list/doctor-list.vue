<template>
  <view class="doctor-list-page">
    <view class="nav-bar">
      <text class="nav-title">选择医生</text>
      <view class="placeholder"></view>
    </view>
    <view class="info-bar">
      <text class="info-text">{{ hospitalName }}</text>
    </view>
    <view class="loading-wrap" v-if="loading">
      <text class="loading-text">加载中...</text>
    </view>
    <view class="doctor-list" v-else>
      <view class="doctor-card"
            v-for="(d, i) in doctorList" :key="d.id || i"
            :class="{ active: selectedId === d.id }"
            @click="selectDoctor(d)">
        <view class="doctor-header">
          <image class="doctor-avatar" :src="d.avatarUrl || d.avatar || '/static/doctor-default.png'"
                 mode="aspectFill"></image>
          <view class="doctor-info">
            <text class="doctor-name">{{ d.expertName || d.name }}</text>
            <text class="doctor-title">{{ d.title || d.professionalTitle }}</text>
            <view class="doctor-tags">
              <text class="tag" v-if="d.specialty">{{ d.specialty }}</text>
            </view>
          </view>
        </view>
        <view class="doctor-desc" v-if="d.introduction || d.description">
          <text class="desc-text">{{ d.introduction || d.description }}</text>
        </view>
        <view class="doctor-footer">
          <view class="doctor-stats">
            <text class="stat-item" v-if="d.ratingScore">评分: {{ d.ratingScore }}</text>
            <text class="stat-item" v-if="d.serviceCount">接诊: {{ d.serviceCount }}人</text>
          </view>
          <view class="select-btn" v-if="selectedId === d.id">
            <text class="btn-text">已选择</text>
          </view>
        </view>
      </view>
      <view class="empty-wrap" v-if="doctorList.length === 0">
        <text class="empty-text">暂无医生信息</text>
      </view>
    </view>
    <view class="bottom-section">
      <button class="confirm-btn" @click="confirmSelection" :disabled="!selectedId">确认选择</button>
    </view>
  </view>
</template>

<script>
import {getExperts} from '@/utils/medical-api.js'

export default {
  data() {
    return {
      loading: false,
      hospitalName: '',
      hospitalId: '',
      departmentName: '',
      departmentId: '',
      selectedId: null,
      selectedObj: null,
      doctorList: []
    }
  },
  onLoad(options) {
    this.hospitalName = decodeURIComponent(options.hospital)
    this.hospitalId = decodeURIComponent(options.hospitalId)
    this.departmentName = options.department || ''
    this.departmentId = options.departmentId || ''
    // console.hospitalId
    this.loadDoctors()
  },
  methods: {
    async loadDoctors() {
      this.loading = true
      try {
        const params = {page: 1, pageSize: 20}
        // hospitalId.console
        if (this.hospitalId) params.hospitalId = this.hospitalId
        if (this.departmentId) params.departmentId = this.departmentId

        const res = await getExperts(params)
        if (res && res.code === 200 && res.data) {
          this.doctorList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('加载医生列表失败:', e)
        uni.showToast({title: '加载失败', icon: 'none'})
      } finally {
        this.loading = false
      }
    },
    selectDoctor(d) {
      this.selectedId = d.id;
      this.selectedObj = d
    },
    confirmSelection() {
      if (!this.selectedId) {
        uni.showToast({title: '请选择医生', icon: 'none'});
        return
      }
      uni.setStorageSync('selectedDoctor', this.selectedObj.expertName || this.selectedObj.name)
      uni.setStorageSync('selectedDoctorId', this.selectedId)
      uni.navigateBack({delta: 2})
    }
  }
}
</script>

<style lang="scss">
.doctor-list-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0;
  background: linear-gradient(135deg, #4DD0E1, #26C6DA);
}

.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

.info-bar {
  background: #fff;
  padding: 30rpx;
  border-bottom: 1rpx solid #e8e8e8;
}

.info-text {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.loading-wrap, .empty-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80rpx;
}

.loading-text, .empty-text {
  font-size: 28rpx;
  color: #999;
}

.doctor-list {
  padding: 20rpx;
}

.doctor-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  border: 2rpx solid transparent;
}

.doctor-card.active {
  border-color: #4DD0E1;
}

.doctor-header {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.doctor-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  background: #f0f0f0;
}

.doctor-info {
  flex: 1;
}

.doctor-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.doctor-title {
  font-size: 26rpx;
  color: #666;
  display: block;
  margin-bottom: 12rpx;
}

.doctor-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
}

.tag {
  font-size: 22rpx;
  color: #4DD0E1;
  background: #E1F5FE;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.doctor-desc {
  margin-bottom: 20rpx;
  padding: 20rpx;
  background: #f9f9f9;
  border-radius: 12rpx;
}

.desc-text {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
}

.doctor-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.doctor-stats {
  display: flex;
  gap: 30rpx;
}

.stat-item {
  font-size: 24rpx;
  color: #999;
}

.select-btn {
  background: linear-gradient(135deg, #4DD0E1, #26C6DA);
  padding: 12rpx 30rpx;
  border-radius: 30rpx;
}

.btn-text {
  font-size: 26rpx;
  color: #fff;
  font-weight: 500;
}

.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.confirm-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1, #26C6DA);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.confirm-btn[disabled] {
  background: #ccc;
  color: #999;
}

.confirm-btn::after {
  border: none;
}
</style>
