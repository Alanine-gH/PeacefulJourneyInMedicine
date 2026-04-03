<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="我的订单" showBack="true"></CompanionNavBar>

    <!-- 顶部空白区域 -->
    

    <!-- 订单状态筛选 -->
    <view class="status-filter">
      <view v-for="status in statusFilters" :key="status.value" class="filter-item"
            :class="{ active: selectedStatus === status.value }" @click="selectStatus(status.value)">
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
        <view v-for="(order, index) in orderList" :key="order.id" class="order-item"
              @click="goToOrderDetail(order.orderNo || order.order_no)">
          <view class="order-header">
            <text class="service-type">{{ getServiceTypeName(order.orderType || order.order_type) }}</text>
            <text class="order-status"
                  :class="getStatusClass(order.orderStatus || order.order_status)">
              {{ getOrderStatusName(order.orderStatus || order.order_status) }}
            </text>
          </view>
          <view class="order-content">
            <image class="service-image" src="/static/hospital.png" mode="aspectFill"></image>
            <view class="service-info">
              <text
                  class="info-item">就诊医院：{{ order.appointmentHospital || order.appointment_hospital || '-' }}
              </text>
              <text
                  class="info-item">就诊时间：{{ formatDate(order.appointmentDate || order.appointment_date) }}
                {{
                  (order.serviceStartTime || order.service_start_time) ? formatTime(order.serviceStartTime || order.service_start_time) : ''
                }}
              </text>
              <text class="info-item">就诊人员：{{ order.patientName || order.patient_name || '-' }}
                {{
                  (order.patientGender ?? order.patient_gender) !== null && (order.patientGender ?? order.patient_gender) !== undefined ? ((order.patientGender ?? order.patient_gender) === 1 ? '男' : '女') : ''
                }}
                {{ (order.patientAge || order.patient_age) ? (order.patientAge || order.patient_age) + '周岁' : '' }}
              </text>
            </view>
          </view>
          <view class="order-footer">
            <text class="order-amount">订单金额：
              <text class="amount-value">{{ order.currency || 'CNY' }}
                {{ order.totalAmount || order.total_amount }}
              </text>
            </text>
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
import CompanionNavBar from '@/components/CompanionNavBar.vue'
import {
  getCompanionOrders
} from '@/utils/companion-api.js'

export default {
  components: {
    CompanionNavBar
  },
  data() {
    return {
      loading: false,
      orderList: [],
      selectedStatus: null,
      statusFilters: [{
        value: null,
        label: '全部'
      },
        {
          value: 2,
          label: '已付款'
        },
        {
          value: 3,
          label: '已确认'
        },
        {
          value: 4,
          label: '服务中'
        },
        {
          value: 5,
          label: '已完成'
        },
        {
          value: 6,
          label: '已取消'
        }
      ]
    }
  },
  onLoad() {
    this.loadOrders()
  },
  methods: {
    selectStatus(status) {
      this.selectedStatus = status
      this.loadOrders()
    },
    async loadOrders() {
      this.loading = true
      try {
        const params = {
          page: 1,
          pageSize: 50
        }
        if (this.selectedStatus !== null && this.selectedStatus !== undefined) {
          params.status = this.selectedStatus
        }
        const res = await getCompanionOrders(params)

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
    goToOrderDetail(orderNo) {
      if (!orderNo) return
      uni.navigateTo({
        url: `/pages/order-detail/order-detail?orderId=${orderNo}`
      })
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  display: flex;
  flex-direction: column;
}

/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
}

/* 订单状态筛选 */
.status-filter {
  display: flex;
  background: #fff;
  padding: 24rpx 30rpx;
  overflow-x: auto;
  white-space: nowrap;
  position: sticky;
  top: 88rpx;
  z-index: 10;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.06);
}

.filter-item {
  padding: 14rpx 28rpx;
  margin-right: 16rpx;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #6a6a7a;
  background: #f8f7f5;
  transition: all 0.3s ease;
  flex-shrink: 0;
}

.filter-item.active {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-weight: 500;
  box-shadow: 0 4rpx 16rpx rgba(141, 184, 182, 0.3);
}

/* 订单列表 */
.order-list {
  flex: 1;
  padding: 30rpx;
}

/* 加载状态 */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}

.loading-spinner {
  width: 56rpx;
  height: 56rpx;
  border: 4rpx solid #f0f0f0;
  border-top: 4rpx solid #8db8b6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-text {
  font-size: 28rpx;
  color: #9a9aaa;
  margin-top: 20rpx;
}

/* 空状态 */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 24rpx;
  opacity: 0.4;
}

.empty-text {
  font-size: 28rpx;
  color: #9a9aaa;
}

.order-item {
  background: #fff;
  border-radius: 24rpx;
  padding: 32rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
  transition: all 0.3s ease;
  border: 1rpx solid rgba(141, 184, 182, 0.08);
}

.order-item:active {
  transform: scale(0.98);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid rgba(0, 0, 0, 0.04);
}

.service-type {
  font-size: 32rpx;
  font-weight: 600;
  color: #3a3a4a;
}

.order-status {
  padding: 8rpx 18rpx;
  border-radius: 12rpx;
  font-size: 22rpx;
  font-weight: 500;
}

.status-waiting {
  background: #fff8e6;
  color: #e6a23c;
}

.status-paid {
  background: #e6f3ff;
  color: #409eff;
}

.status-confirmed {
  background: #e8f5e9;
  color: #67c23a;
}

.status-serving {
  background: #f3e5f5;
  color: #9c27b0;
}

.status-completed {
  background: #e0f7fa;
  color: #00acc1;
}

.status-canceled {
  background: #ffebee;
  color: #ef5350;
}

.status-refunding {
  background: #fff8e1;
  color: #ffca28;
}

.status-refunded {
  background: #f5f5f5;
  color: #9e9e9e;
}

.order-content {
  display: flex;
  margin-bottom: 24rpx;
  padding-bottom: 24rpx;
  border-bottom: 1rpx solid rgba(0, 0, 0, 0.04);
}

.service-image {
  width: 140rpx;
  height: 140rpx;
  border-radius: 16rpx;
  margin-right: 24rpx;
  background: #f8f7f5;
}

.service-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12rpx;
  justify-content: center;
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
  color: #d4a8b0;
}

.order-time {
  font-size: 24rpx;
  color: #9a9aaa;
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .status-filter {
    padding: 20rpx 24rpx;
  }

  .filter-item {
    padding: 12rpx 24rpx;
    font-size: 24rpx;
  }

  .order-item {
    padding: 28rpx;
  }

  .service-image {
    width: 120rpx;
    height: 120rpx;
  }
}

/* ── Shared theme overrides ── */
page {
  background-color: #f5f3ef !important;
}

.container {
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%) !important;
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


/* ── companion orders page specifics ── */
.status-filter {
  background: #fff !important;
  border-bottom-color: #f0ede8 !important;
}

.filter-item {
  color: #6a6a7a !important;
  border-radius: 20rpx !important;
}

.order-list {
  padding: 16rpx 24rpx !important;
}

.order-item {
  background: #fff !important;
  border-left-color: #8db8b6 !important;
  border-radius: 16rpx !important;
  margin-bottom: 20rpx !important;
  box-shadow: 0 4rpx 16rpx rgba(100, 120, 140, 0.08) !important;
  border: none !important;
}

.service-type {
  color: #3a3a4a !important;
  font-weight: 700 !important;
}

.info-item {
  color: #6a6a7a !important;
}

.amount-value {
  color: #d4a8b0 !important;
}

.order-time {
  color: #9a9aaa !important;
}
</style>