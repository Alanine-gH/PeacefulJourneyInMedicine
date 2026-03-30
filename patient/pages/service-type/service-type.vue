<template>
  <view class="service-type-page">
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
    selectService(item) {
      this.selectedService = item
      // 用 Storage 回传，避免 $vm 赋值失效
      uni.setStorageSync('_sel_serviceType', item.packageName || item.name)
      uni.setStorageSync('_sel_servicePackageId', item.id)
      uni.setStorageSync('_sel_servicePrice', item.price || null)
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
  background-color: #f4f2ee;
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