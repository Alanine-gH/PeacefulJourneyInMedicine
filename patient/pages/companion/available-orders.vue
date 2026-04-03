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
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  min-height: 100vh;
  padding: 24rpx;
}

/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
}

/* 订单列表 */
.order-list {
  flex: 1;
  padding: 0;
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
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.1);
  transition: all 0.3s ease;
  border-left: 8rpx solid #8db8b6;
}

.order-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(141, 184, 182, 0.2);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid rgba(141, 184, 182, 0.1);
}

.service-type {
  font-size: 32rpx;
  font-weight: 600;
  color: #3a3a4a;
}

.order-status {
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: 500;
  background: linear-gradient(135deg, #e8f4f4 0%, #d8e8e8 100%);
  color: #8db8b6;
}

.status-waiting {
  background: linear-gradient(135deg, #e8f4f4 0%, #d8e8e8 100%);
  color: #8db8b6;
}

.order-content {
  display: flex;
  margin-bottom: 24rpx;
  padding-bottom: 24rpx;
  border-bottom: 2rpx solid rgba(141, 184, 182, 0.1);
}

.service-image {
  width: 160rpx;
  height: 160rpx;
  border-radius: 16rpx;
  margin-right: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.service-image:active {
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
  color: #6a6a7a;
  line-height: 1.5;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-amount {
  font-size: 26rpx;
  color: #6a6a7a;
}

.amount-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #8db8b6;
}

.accept-btn {
  padding: 16rpx 48rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 26rpx;
  font-weight: 500;
  border-radius: 30rpx;
  border: none;
  transition: all 0.3s ease;
  box-shadow: 0 4rpx 16rpx rgba(141, 184, 182, 0.28);
}

.accept-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
  box-shadow: 0 2rpx 8rpx rgba(141, 184, 182, 0.28);
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
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
    padding: 12rpx 36rpx;
    font-size: 24rpx;
  }
}
</style>