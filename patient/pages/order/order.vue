<template>
  <view class="container">
    <!-- 头部 -->
   <!-- <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">我的订单</text>
        </view>
        <view class="header-actions">
          <view class="customer-service" @click="openCustomerService">
            <text class="service-icon">💬</text>
          </view>
        </view>
      </view>
    </view> -->
    
    <!-- 订单状态筛选 -->
    <view class="status-filter">
      <view 
        v-for="status in statusFilters" 
        :key="status.value"
        class="filter-item"
        :class="{ active: selectedStatus === status.value }"
        @click="selectStatus(status.value)"
      >
        <text class="filter-text">{{ status.label }}</text>
      </view>
    </view>
    
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
        <text class="empty-text">暂无订单</text>
      </view>
      
      <!-- 订单列表 -->
      <view v-else>
        <view v-for="(order, index) in orderList" :key="order.id" class="order-item" @click="goToOrderDetail(order.orderNo || order.order_no)">
          <view class="order-header">
            <text class="service-type">{{ getServiceTypeName(order.orderType || order.order_type) }}</text>
            <text class="order-status" :class="getStatusClass(order.orderStatus || order.order_status)">{{ getOrderStatusName(order.orderStatus || order.order_status) }}</text>
          </view>
          <view class="order-content">
            <image class="service-image" src="/static/logo.png" mode="aspectFill"></image>
            <view class="service-info">
              <text class="info-item">就诊医院：{{ order.appointmentHospital || order.appointment_hospital || '-' }}</text>
              <text class="info-item">就诊时间：{{ formatDate(order.appointmentDate || order.appointment_date) }} {{ (order.serviceStartTime || order.service_start_time) ? formatTime(order.serviceStartTime || order.service_start_time) : '' }}</text>
              <text class="info-item">就诊人员：{{ order.patientName || order.patient_name || '-' }} {{ (order.patientGender ?? order.patient_gender) !== null && (order.patientGender ?? order.patient_gender) !== undefined ? ((order.patientGender ?? order.patient_gender) === 1 ? '男' : '女') : '' }} {{ (order.patientAge || order.patient_age) ? (order.patientAge || order.patient_age) + '周岁' : '' }}</text>
            </view>
          </view>
          <view class="order-footer">
            <text class="order-amount">订单金额：<text class="amount-value">{{ order.currency || 'CNY' }} {{ order.totalAmount || order.total_amount }}</text></text>
            <text class="order-time">{{ formatOrderTime(order.createTime || order.create_time) }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getPatientOrders } from '@/utils/patient-api'

export default {
  data() {
    return {
      loading: false,
      orderList: [],
      selectedStatus: null,
      statusFilters: [
        { value: null, label: '全部' },
        { value: 2, label: '已付款' },
        { value: 3, label: '已确认' },
        { value: 4, label: '服务中' },
        { value: 5, label: '已完成' },
        { value: 6, label: '已取消' }
      ]
    }
  },
  onLoad() {
    this.loadOrders()
  },
  onPullDownRefresh() {
    this.loadOrders()
  },
  methods: {
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
    selectStatus(status) {
      this.selectedStatus = status
      this.loadOrders()
    },
    async loadOrders() {
      this.loading = true
	  // const userId  = uni.getStorageSync('userId');
      try {
        const params = { page: 1, pageSize: 50,userId:uni.getStorageSync('userId')}
        if (this.selectedStatus !== null && this.selectedStatus !== undefined) {
          params.orderStatus = this.selectedStatus
        }
        const res = await getPatientOrders(params)
        
        if (res.code === 200 && res.data) {
          // 后端返回 IPage，字段为 records，且字段为驼峰命名
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
    getServiceTypeName(type) {
      const typeMap = {
        1: '全程陪诊',
        2: '代办手续',
        3: '接机服务',
        4: '送机服务'
      }
      return typeMap[type] || '未知服务'
    },
    getOrderStatusName(status) {
      const statusMap = {
        1: '待付款',
        2: '已付款',
        3: '已确认',
        4: '服务中',
        5: '已完成',
        6: '已取消',
        7: '退款中',
        8: '已退款'
      }
      return statusMap[status] || '未知'
    },
    getStatusClass(status) {
      const classMap = {
        1: 'status-waiting',
        2: 'status-paid',
        3: 'status-confirmed',
        4: 'status-serving',
        5: 'status-completed',
        6: 'status-canceled',
        7: 'status-refunding',
        8: 'status-refunded'
      }
      return classMap[status] || ''
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
    },
    formatOrderTime(timeStr) {
      if (!timeStr) return ''
      const date = new Date(timeStr)
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${month}-${day} ${hours}:${minutes}`
    },
    goToOrderDetail(orderId) {
      if (!orderId) return
      uni.navigateTo({
        url: `/pages/order-detail/order-detail?order_no=${orderId}`
      })
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

/* 订单状态筛选 */
.status-filter {
  display: flex;
  background: #fff;
  padding: 20rpx 0;
  overflow-x: auto;
  white-space: nowrap;
  border-bottom: 1rpx solid #f0f0f0;
  position: sticky;
  top: 0;
  z-index: 10;
}

.filter-item {
  padding: 12rpx 24rpx;
  margin: 0 10rpx;
  border-radius: 20rpx;
  font-size: 26rpx;
  color: #666;
  transition: all 0.3s ease;
}

.filter-item.active {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-weight: 500;
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

.status-paid {
  background: #E3F2FD;
  color: #2196F3;
}

.status-confirmed {
  background: #E8F5E9;
  color: #4CAF50;
}

.status-serving {
  background: #F3E5F5;
  color: #9C27B0;
}

.status-completed {
  background: #E0F7FA;
  color: #00BCD4;
}

.status-canceled {
  background: #FFEBEE;
  color: #F44336;
}

.status-refunding {
  background: #FFF8E1;
  color: #FFC107;
}

.status-refunded {
  background: #F5F5F5;
  color: #9E9E9E;
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

.order-time {
  font-size: 24rpx;
  color: #999;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .status-filter {
    padding: 15rpx 0;
  }
  
  .filter-item {
    padding: 10rpx 20rpx;
    font-size: 24rpx;
  }
  
  .order-item {
    padding: 24rpx;
  }
  
  .service-image {
    width: 120rpx;
    height: 120rpx;
  }
}
</style>
