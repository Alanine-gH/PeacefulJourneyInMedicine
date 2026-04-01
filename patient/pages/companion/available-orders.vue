<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="选择订单" rightText="刷新" @rightClick="loadOrders"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>

    <!-- 订单列表 -->
    <view class="order-list">
      <!-- 加载状态 -->
      <view v-if="loading" class="loading-container">
        <view class="loading-spinner"></view>
        <text class="loading-text">加载中...</text>
      </view>

      <!-- 空状态 -->
      <view v-else-if="orderList.length === 0" class="empty-container">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无预约中订单</text>
      </view>

      <!-- 订单列表 -->
      <view v-else>
        <view v-for="(order, index) in orderList" :key="order.id" class="order-item">
          <view class="order-header">
            <text class="service-type">{{ getServiceTypeName(order.order_type) }}</text>
            <text class="order-status status-waiting">预约中</text>
          </view>
          <view class="order-content">
            <image class="service-image" src="/static/logo.png" mode="aspectFill"></image>
            <view class="service-info">
              <text class="info-item">就诊医院：{{ order.appointment_hospital }}</text>
              <text class="info-item">就诊时间：{{ formatDate(order.appointment_date) }}
                {{ order.service_start_time ? formatTime(order.service_start_time) : '' }}
              </text>
              <text class="info-item">就诊人员：{{ order.patient_name }}
                {{ order.patient_gender !== null ? (order.patient_gender === 1 ? '男' : '女') : '' }}
                {{ order.patient_age }}周岁
              </text>
            </view>
          </view>
          <view class="order-footer">
            <text class="order-amount">订单金额：
              <text class="amount-value">{{ order.currency || 'USD' }} {{ order.total_amount }}</text>
            </text>
            <button class="accept-btn" @click="acceptOrder(order.order_no)">接取订单</button>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import CompanionNavBar from '@/components/CompanionNavBar.vue'
import {getAvailableOrders, acceptOrder} from '@/utils/companion-api.js'

export default {
  components: {
    CompanionNavBar
  },
  data() {
    return {
      loading: false,
      orderList: []
    }
  },
  onLoad() {
    this.loadOrders()
  },
  onPullDownRefresh() {
    this.loadOrders()
  },
  methods: {
    async loadOrders() {
      this.loading = true

      try {
        const res = await getAvailableOrders({
          page: 1,
          pageSize: 50
        })

        if (res.code === 200 && res.data) {
          this.orderList = res.data.list || res.data || []
        } else {
          uni.showToast({
            title: res.message || '获取订单失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('获取订单失败:', error)
        uni.showToast({
          title: '获取订单失败，请重试',
          icon: 'none'
        })
      } finally {
        this.loading = false
        uni.stopPullDownRefresh()
      }
    },
    async acceptOrder(orderNo) {
      uni.showModal({
        title: '确认接取',
        content: '确定要接取此订单吗？',
        success: async (res) => {
          if (res.confirm) {
            this.loading = true
            try {
              const result = await acceptOrder(orderNo)
              if (result.code === 200) {
                uni.showToast({
                  title: '接取成功',
                  icon: 'success'
                })
                // 重新加载订单列表
                setTimeout(() => {
                  this.loadOrders()
                }, 1000)
              } else {
                uni.showToast({
                  title: result.message || '接取失败',
                  icon: 'none'
                })
              }
            } catch (error) {
              console.error('接取订单失败:', error)
              uni.showToast({
                title: '接取订单失败，请重试',
                icon: 'none'
              })
            } finally {
              this.loading = false
            }
          }
        }
      })
    },
    getServiceTypeName(type) {
      const typeMap = {
        1: '全程陪诊',
        2: '代办手续',
        3: '接机服务',
        4: '送机服务'
      }
      return typeMap[type] || '未知服务'
    },
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    formatTime(timeStr) {
      if (!timeStr) return ''
      const time = new Date(timeStr)
      const hours = String(time.getHours()).padStart(2, '0')
      const minutes = String(time.getMinutes()).padStart(2, '0')
      return `${hours}:${minutes}`
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 100vh;
  background-color: #f4f2ee;
  display: flex;
  flex-direction: column;
}

/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
}

/* 订单列表 */
.order-list {
  flex: 1;
  padding: 20rpx 30rpx;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.loading-spinner {
  width: 60rpx;
  height: 60rpx;
  border: 4rpx solid #f3f3f3;
  border-top: 4rpx solid #8db8b6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: 28rpx;
  color: #999;
  margin-top: 20rpx;
}

/* 空状态 */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 20rpx;
  opacity: 0.5;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.order-item {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border: 1rpx solid #e8e8e8;
}

.order-item:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.2);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.service-type {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.order-status {
  padding: 6rpx 16rpx;
  border-radius: 15rpx;
  font-size: 22rpx;
  font-weight: 500;
}

.status-waiting {
  background: #FFF3E0;
  color: #FF9800;
}

.order-content {
  display: flex;
  margin-bottom: 24rpx;
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.service-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 12rpx;
  margin-right: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.service-image:hover {
  transform: scale(1.05);
}

.service-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.info-item {
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-amount {
  font-size: 26rpx;
  color: #666;
}

.amount-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff6b6b;
}

.accept-btn {
  padding: 12rpx 36rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 26rpx;
  font-weight: 500;
  border-radius: 25rpx;
  border: none;
  transition: all 0.3s ease;
}

.accept-btn:hover {
  opacity: 0.9;
  transform: scale(1.05);
}

.accept-btn:active {
  opacity: 0.8;
  transform: scale(0.95);
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .order-item {
    padding: 24rpx;
  }

  .service-image {
    width: 120rpx;
    height: 120rpx;
  }

  .accept-btn {
    padding: 10rpx 30rpx;
    font-size: 24rpx;
  }
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