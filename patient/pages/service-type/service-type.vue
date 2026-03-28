<template>
  <view class="service-type-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
      <text class="nav-title">选择服务</text>
      <view class="placeholder"></view>
    </view>

    <!-- 加载中 -->
    <view class="loading-wrap" v-if="loading">
      <text class="loading-text">加载中...</text>
    </view>

    <!-- 服务列表 -->
    <view class="service-list" v-else>
      <view
          class="service-item"
          v-for="(item, index) in serviceList"
          :key="index"
          :class="{ active: selectedService && selectedService.id === item.id }"
          @click="selectService(item)"
      >
        <image class="service-image" :src="item.coverImage || '/static/service1.png'" mode="aspectFill"></image>
        <view class="service-info">
          <text class="service-name">{{ item.packageName || item.name }}</text>
          <text class="service-desc">{{ item.description || item.desc }}</text>
          <text class="service-price">¥{{ item.price }}元/次</text>
        </view>
        <view class="service-check" v-if="selectedService && selectedService.id === item.id">
          <text class="check-icon">✓</text>
        </view>
      </view>
      <view class="empty-wrap" v-if="serviceList.length === 0">
        <text class="empty-text">暂无服务套餐</text>
      </view>
    </view>
  </view>
</template>

<script>
import {getServicePackages} from '@/utils/record-api.js'

export default {
  data() {
    return {
      loading: false,
      selectedService: null,
      serviceList: []
    }
  },
  onLoad() {
    this.loadServicePackages()
  },
  methods: {
    async loadServicePackages() {
      this.loading = true
      try {
        const res = await getServicePackages({page: 1, pageSize: 20})
        if (res && res.code === 200 && res.data) {
          const records = res.data.records || res.data.list || (Array.isArray(res.data) ? res.data : [])
          this.serviceList = records
        }
      } catch (e) {
        console.error('加载服务套餐失败:', e)
        uni.showToast({title: '加载失败，使用默认数据', icon: 'none'})
        // fallback to static data
        this.serviceList = [
          {id: 1, packageName: 'VIP陪诊服务', description: 'VIP陪诊体验，专车接送', price: 588},
          {id: 2, packageName: '全程陪诊', description: '全程陪同，手续代办', price: 488},
          {id: 3, packageName: '代办问诊', description: '代替客户到机构咨询', price: 388},
          {id: 4, packageName: '取送结果', description: '取送结果到家', price: 288},
          {id: 5, packageName: '代办买药', description: '人工排队代买药', price: 188}
        ]
      } finally {
        this.loading = false
      }
    },
    // goBack() {
    //   uni.navigateBack()
    // },
    selectService(item) {
      this.selectedService = item
      // 用 Storage 回传，避免 $vm 赋值失效
      uni.setStorageSync('selectedServiceType', item.packageName || item.name)
      uni.setStorageSync('selectedServiceTypeId', item.id)
      setTimeout(() => {
        uni.navigateBack()
      }, 300)
    }
  }
}
</script>

<style lang="scss">
.service-type-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0 30rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}
/*
.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-arrow {
  font-size: 40rpx;
  color: #fff;
}
*/
.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
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

.service-list {
  padding: 20rpx;
}

.service-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  position: relative;
  border: 2rpx solid transparent;
}

.service-item.active {
  border-color: #2196F3;
}

.service-image {
  width: 120rpx;
  height: 120rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  background-color: #f0f0f0;
}

.service-info {
  flex: 1;
}

.service-name {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  display: block;
  margin-bottom: 10rpx;
}

.service-desc {
  font-size: 26rpx;
  color: #999;
  display: block;
  margin-bottom: 10rpx;
}

.service-price {
  font-size: 28rpx;
  color: #2196F3;
  font-weight: 500;
}

.service-check {
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: translateY(-50%);
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background-color: #2196F3;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  font-size: 24rpx;
  color: #fff;
}
</style>
