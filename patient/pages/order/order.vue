<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-icon">←</text>-->
<!--      </view>-->
      <view class="title">我的业务</view>
    </view>
    
    <!-- 订单状态筛选 -->
    <view class="order-filter">
      <view class="filter-item" :class="{ active: activeFilter === 'all' }" @click="selectFilter('all')">全部</view>
      <view class="filter-item" :class="{ active: activeFilter === 'unpaid' }" @click="selectFilter('unpaid')">待支付</view>
      <view class="filter-item" :class="{ active: activeFilter === 'pending' }" @click="selectFilter('pending')">待服务</view>
      <view class="filter-item" :class="{ active: activeFilter === 'completed' }" @click="selectFilter('completed')">已完成</view>
      <view class="filter-item" :class="{ active: activeFilter === 'cancelled' }" @click="selectFilter('cancelled')">已取消</view>
    </view>
    
    <!-- 订单列表 -->
    <view class="order-list">
      <view v-if="orders.length === 0" class="empty-state">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无订单</text>
      </view>
      
      <view v-else>
        <view 
          class="order-item" 
          v-for="(order, index) in orders" 
          :key="index"
        >
          <view class="order-header">
            <text class="order-id">订单号：{{ order.orderNo }}</text>
            <text class="order-status" :class="getStatusClass(order.orderStatus)">
              {{ getOrderStatusText(order.orderStatus) }}
            </text>
          </view>
          <view class="order-content">
            <view class="order-image">
              <text class="image-icon">🏥</text>
            </view>
            <view class="order-info">
              <text class="order-title">{{ getOrderTypeText(order.orderType) }}</text>
              <text class="order-desc">{{ order.appointmentHospital || order.diseaseDescription || '全程陪同就医，提供专业医疗咨询' }}</text>
              <text class="order-price">¥{{ order.totalAmount || '0.00' }}</text>
            </view>
          </view>
          <view class="order-footer">
            <text class="order-time">{{ order.appointmentDate || order.serviceStartTime || order.createTime }}</text>
            <button class="order-btn" @click="viewOrderDetail(order.orderNo)">查看详情</button>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getPatientOrders } from '@/utils/patient-api';

export default {
  data() {
    return {
      activeFilter: 'all',
      orders: []
    }
  },
  onLoad(options) {
    // 如果有传入状态参数，设置当前筛选
    if (options && options.status) {
      this.activeFilter = options.status;
    }
    this.getOrders()
  },
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
    async getOrders() {
      try {
        const statusMap = {
          'all': '',
          'unpaid': 1,
          'pending': '2,3,4',
          'completed': 5,
          'cancelled': 6
        };
        
        const statusValue = statusMap[this.activeFilter];
        const params = {};
        if (statusValue !== '') {
          params.order_status = statusValue;
        }
        
        const res = await getPatientOrders(params);
        
          if (res.code === 200) {
          this.orders = (res.data && res.data.records) ? res.data.records : (Array.isArray(res.data) ? res.data : []);
        }
      } catch (error) {
        console.error('获取订单列表失败:', error);
        uni.showToast({ title: '获取订单列表失败', icon: 'none' });
      }
    },
    selectFilter(filter) {
      this.activeFilter = filter;
      this.getOrders();
    },
    viewOrderDetail(orderId) {
      uni.navigateTo({
        url: `/pages/order-detail/order-detail?order_no=${orderId}`
      })
    },
    getStatusClass(status) {
      return {
        completed: status === 5,
        cancelled: status === 6 || status === 8,
        serving: status === 4
      }
    },
    getOrderStatusText(status) {
      const statusMap = {
        1: '待支付',
        2: '已付款',
        3: '已确认',
        4: '服务中',
        5: '已完成',
        6: '已取消',
        7: '退款中',
        8: '已退款'
      };
      return statusMap[status] || '未知';
    },
    getOrderTypeText(type) {
      const typeMap = {
        1: '全程陪诊',
        2: '代办手续',
        3: '接机服务',
        4: '送机服务'
      };
      return typeMap[type] || '未知';
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
/*
.back-btn {
  position: absolute;
  top: 40rpx;
  left: 30rpx;
  z-index: 1;
}

.back-icon {
  font-size: 36rpx;
  color: #fff;
}*/

.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  text-align: center;
}

.order-filter {
  display: flex;
  background-color: white;
  border-bottom: 1rpx solid #e8e8e8;
  padding: 0 30rpx;
  overflow-x: auto;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.order-filter::-webkit-scrollbar {
  display: none;
}

.filter-item {
  padding: 20rpx 25rpx;
  font-size: 28rpx;
  color: #666;
  position: relative;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.filter-item.active {
  color: #4DD0E1;
  font-weight: 600;
}

.filter-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 25rpx;
  right: 25rpx;
  height: 4rpx;
  background-color: #4DD0E1;
  border-radius: 2rpx;
}

.order-list {
  flex: 1;
  padding: 20rpx 30rpx;
}

.order-item {
  background-color: white;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.order-item:hover {
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  transform: translateY(-2rpx);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.order-id {
  font-size: 26rpx;
  color: #666;
}

.order-status {
  font-size: 26rpx;
  color: #ff9800;
  font-weight: 600;
}

.order-status.completed {
  color: #4caf50;
}

.order-status.cancelled {
  color: #999;
}

.order-status.serving {
  color: #2196F3;
}

.order-content {
  display: flex;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.order-image {
  width: 120rpx;
  height: 120rpx;
  border-radius: 15rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.image-icon {
  font-size: 60rpx;
  color: white;
}

.order-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.order-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
}

.order-desc {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 15rpx;
  line-height: 1.4;
  flex: 1;
}

.order-price {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff5722;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-time {
  font-size: 24rpx;
  color: #999;
}

.order-btn {
  padding: 15rpx 30rpx;
  background-color: white;
  color: #4DD0E1;
  border: 2rpx solid #4DD0E1;
  border-radius: 40rpx;
  font-size: 26rpx;
  font-weight: 500;
  outline: none;
  transition: all 0.3s ease;
}

.order-btn:active {
  background-color: #E1F5FE;
  transform: scale(0.98);
}

.order-btn::after {
  border: none;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 30rpx;
  text-align: center;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 30rpx;
  opacity: 0.5;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
  line-height: 1.5;
}
</style>
