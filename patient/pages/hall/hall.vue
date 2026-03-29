<template>
  <view class="container">
    <!-- 头部 -->
    <!-- <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">选择订单</text>
        </view>
        <view class="header-actions">
          <view class="customer-service" @click="openCustomerService">
            <text class="service-icon">💬</text>
          </view>
        </view>
      </view>
    </view> -->
    
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
        <text class="empty-text">暂无待接订单</text>
      </view>
      
      <!-- 订单列表 -->
      <view v-else>
        <view v-for="(order, index) in orderList" :key="order.id" class="order-item" @click="goToOrderDetail(order.orderNo || order.order_no)">
          <view class="order-header">
            <text class="service-type">{{ getServiceTypeName(order.orderType || order.order_type) }}</text>
            <text v-if="(order.orderStatus || order.order_status) === 2" class="order-tag">急</text>
          </view>
          <view class="order-content">
            <image class="service-image" src="/static/logo.png" mode="aspectFill"></image>
            <view class="service-info">
              <text class="info-item">就诊医院：{{ order.appointmentHospital || order.appointment_hospital || '-' }}</text>
              <text class="info-item">就诊时间：{{ formatDate(order.appointmentDate || order.appointment_date) }} {{ (order.serviceStartTime || order.service_start_time) ? formatTime(order.serviceStartTime || order.service_start_time) : '' }}</text>
              <text class="info-item">就诊人员：{{ order.patientName || order.patient_name || '-' }} {{ (order.patientGender ?? order.patient_gender) === 1 ? '男' : (order.patientGender ?? order.patient_gender) === 2 ? '女' : '' }} {{ order.patientAge || order.patient_age ? (order.patientAge || order.patient_age) + '周岁' : '' }}</text>
              <text class="info-item">服务需求：{{ order.diseaseDescription || order.disease_description || '暂无' }}<text v-if="(order.diseaseDescription || order.disease_description) && (order.diseaseDescription || order.disease_description).length > 30" class="expand-btn" @click.stop="expandInfo(index)">展开</text></text>
            </view>
          </view>
          <view class="order-footer">
            <text class="profit">本单收益：<text class="profit-amount">{{ order.totalAmount || order.total_amount || 0 }}元</text></text>
            <button class="accept-btn" @click.stop="acceptOrder(order.orderNo || order.order_no)">接单</button>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getAvailableOrders, acceptOrder as acceptOrderApi } from '@/utils/companion-api.js'

export default {
  data() {
    return {
      loading: false,
      orderList: [],
      expandedItems: []
    }
  },
  onLoad() {
    this.loadAvailableOrders()
  },
  onPullDownRefresh() {
    this.loadAvailableOrders()
  },
  methods: {
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
    async loadAvailableOrders() {
      this.loading = true
      
      try {
        const res = await getAvailableOrders({
          page: 1,
          pageSize: 20
        })
        
        if (res.code === 200 && res.data) {
          // 后端返回 IPage，字段为 records
          this.orderList = res.data.records || res.data.list || (Array.isArray(res.data) ? res.data : [])
        } else {
          uni.showToast({
            title: res.msg || res.message || '获取订单失败',
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
    expandInfo(index) {
      this.expandedItems[index] = !this.expandedItems[index]
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
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${month}-${day}`
    },
    formatTime(timeStr) {
      if (!timeStr) return ''
      const time = new Date(timeStr)
      const hours = String(time.getHours()).padStart(2, '0')
      const minutes = String(time.getMinutes()).padStart(2, '0')
      return `${hours}:${minutes}`
    },
    goToOrderDetail(orderNo) {
      if (!orderNo) return
      uni.navigateTo({
        url: `/pages/order-detail/order-detail?orderId=${orderNo}`
      })
    },
    async acceptOrder(orderNo) {
      uni.showLoading({
        title: '接单中...'
      })
      
      try {
        const res = await acceptOrderApi(orderNo)
        
        if (res.code === 200) {
          uni.hideLoading()
          uni.showToast({
            title: '接单成功',
            icon: 'success'
          })
          
          // 接单成功后跳转到订单详情页面
          setTimeout(() => {
            uni.navigateTo({
              url: `/pages/order-detail/order-detail?orderId=${orderNo}`
            })
          }, 1500)
        } else {
          uni.hideLoading()
          uni.showToast({
            title: res.message || '接单失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading()
        console.error('接单失败:', error)
        uni.showToast({
          title: '接单失败，请重试',
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 100vh;
  background-color: #f8f9fa;
  display: flex;
  flex-direction: column;
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

.logo {
  display: flex;
  align-items: center;
}

.logo-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.customer-service {
  width: 60rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.customer-service:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: scale(1.05);
}

.service-icon {
  font-size: 32rpx;
  color: #fff;
}

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
  border-top: 4rpx solid #4DD0E1;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
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

.order-tag {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 20rpx;
  font-weight: 600;
  padding: 4rpx 12rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 8rpx rgba(77, 208, 225, 0.3);
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

.expand-btn {
  color: #4DD0E1;
  font-size: 24rpx;
  font-weight: 500;
  text-decoration: underline;
  transition: all 0.3s ease;
}

.expand-btn:hover {
  color: #26C6DA;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.profit {
  font-size: 26rpx;
  color: #666;
}

.profit-amount {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff6b6b;
}

.accept-btn {
  padding: 15rpx 40rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  border: none;
  border-radius: 30rpx;
  font-size: 26rpx;
  font-weight: 600;
  outline: none;
  box-shadow: 0 3rpx 10rpx rgba(77, 208, 225, 0.3);
  transition: all 0.3s ease;
}

.accept-btn:hover {
  box-shadow: 0 6rpx 20rpx rgba(77, 208, 225, 0.4);
  transform: translateY(-2rpx);
}

.accept-btn:active {
  transform: translateY(2rpx);
  box-shadow: 0 2rpx 6rpx rgba(77, 208, 225, 0.3);
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>
