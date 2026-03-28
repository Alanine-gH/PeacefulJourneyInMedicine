<template>
  <view class="container">
    <!-- 头部 -->
<!--    <view class="header">-->
<!--      <view class="header-content">-->
<!--        <view class="logo">-->
<!--          <text class="logo-text">工作信息</text>-->
<!--        </view>-->
<!--        <view class="header-actions">-->
<!--          <view class="customer-service" @click="openCustomerService">-->
<!--            <text class="service-icon">💬</text>-->
<!--          </view>-->
<!--        </view>-->
<!--      </view>-->
<!--    </view>-->

    <!-- 信息列表 -->
    <view class="info-section">
      <view class="info-item">
        <view class="info-label">今日订单</view>
        <view class="info-value">{{ workInfo.todayOrders }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">待处理订单</view>
        <view class="info-value">{{ workInfo.pendingOrders }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">本月收入</view>
        <view class="info-value">¥{{ workInfo.monthlyIncome.toLocaleString() }}</view>
      </view>
      <view class="info-item">
        <view class="info-label">服务评分</view>
        <view class="info-value">{{ workInfo.rating }}</view>
      </view>
    </view>

    <!-- 工作统计 -->
    <view class="stats-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">📊</text> 工作统计
        </text>
      </view>
      <view class="stats-content">
        <view class="stat-item">
          <view class="stat-icon">📅</view>
          <view class="stat-info">
            <view class="stat-value">{{ workStats.serviceCount }}</view>
            <view class="stat-label">本月服务次数</view>
          </view>
        </view>
        <view class="stat-item">
          <view class="stat-icon">⭐</view>
          <view class="stat-info">
            <view class="stat-value">{{ workStats.goodRate }}</view>
            <view class="stat-label">好评率</view>
          </view>
        </view>
        <view class="stat-item">
          <view class="stat-icon">⏱️</view>
          <view class="stat-info">
            <view class="stat-value">{{ workStats.serviceHours }}</view>
            <view class="stat-label">服务时长(小时)</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 最近订单 -->
    <view class="orders-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">📋</text> 最近订单
        </text>
      </view>
      <view class="order-list">
        <view class="order-item" v-for="(order, index) in recentOrders" :key="index">
          <view class="order-header">
            <view class="order-id">订单号: {{ order.order_id }}</view>
            <view class="order-status">{{ order.status }}</view>
          </view>
          <view class="order-info">
            <view class="order-service">{{ order.service }}</view>
            <view class="order-time">{{ order.time }}</view>
            <view class="order-price">¥{{ order.price.toFixed(2) }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>

    <!-- 底部导航栏 -->
    <CompanionTabBar activeTab="info"></CompanionTabBar>
  </view>
</template>

<script>
import CompanionTabBar from '@/components/CompanionTabBar.vue'
import { getCompanionWorkInfo, getCompanionOrders } from '@/utils/companion-api';

export default {
  components: {
    CompanionTabBar
  },
  data() {
    return {
      workInfo: {
        todayOrders: 0,
        pendingOrders: 0,
        monthlyIncome: 0,
        rating: 0
      },
      workStats: {
        serviceCount: 0,
        goodRate: '',
        serviceHours: 0
      },
      recentOrders: []
    }
  },
  onLoad() {
    this.getWorkInfo()
  },
  methods: {
    async getWorkInfo() {
      try {
        const [workInfoRes, ordersRes] = await Promise.all([
          getCompanionWorkInfo(),
          getCompanionOrders({ limit: 5 })
        ]);
        
        if (workInfoRes.code === 200) {
          const data = workInfoRes.data || {};
          // 后端字段: activeOrderCount(进行中), pendingOrderCount(待接单), serviceCount, ratingScore
          this.workInfo = {
            todayOrders: data.activeOrderCount || data.today_orders || 0,
            pendingOrders: data.pendingOrderCount || data.pending_orders || 0,
            monthlyIncome: data.monthlyIncome || data.monthly_income || 0,
            rating: data.ratingScore || data.rating_score || 0
          };
          this.workStats = {
            serviceCount: data.serviceCount || data.service_count || 0,
            goodRate: data.onTimeRate != null ? (data.onTimeRate + '%') : (data.good_rate || '0%'),
            serviceHours: data.serviceHours || data.service_hours || 0
          };
        }

        if (ordersRes.code === 200) {
          const list = ordersRes.data?.records || ordersRes.data?.list || (Array.isArray(ordersRes.data) ? ordersRes.data : []);
          this.recentOrders = list.map(order => ({
            order_id: order.orderNo || order.order_no,
            status: this.getOrderStatusText(order.orderStatus || order.order_status),
            service: this.getServiceTypeName(order.orderType || order.order_type),
            time: this.formatOrderTime(order.appointmentDate || order.appointment_date),
            price: order.totalAmount || order.total_amount || 0
          }));
        }
      } catch (error) {
        console.error('获取工作信息失败:', error);
        uni.showToast({ title: '获取工作信息失败', icon: 'none' });
      }
    },
    getOrderStatusText(status) {
      const statusMap = {
        1: '待付款', 2: '已付款', 3: '已确认',
        4: '服务中', 5: '已完成', 6: '已取消',
        7: '退款中', 8: '已退款'
      };
      return statusMap[status] || '未知';
    },
    getServiceTypeName(type) {
      const typeMap = { 1: '全程陪诊', 2: '代办手续', 3: '接机服务', 4: '送机服务' };
      return typeMap[type] || '陪诊服务';
    },
    formatOrderTime(dateStr) {
      if (!dateStr) return '时间待定';
      const d = new Date(dateStr);
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${m}-${day}`;
    },
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
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

/* 信息列表 */
.info-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 28rpx;
  color: #666;
}

.info-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

/* 工作统计 */
.stats-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.section-title .icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.stats-content {
  display: flex;
  justify-content: space-around;
  gap: 20rpx;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 15rpx;
}

.stat-icon {
  font-size: 48rpx;
  margin-bottom: 15rpx;
}

.stat-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 5rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #666;
  text-align: center;
}

/* 最近订单 */
.orders-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.order-item {
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 15rpx;
  border-left: 5rpx solid #4DD0E1;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.order-id {
  font-size: 24rpx;
  color: #666;
}

.order-status {
  font-size: 24rpx;
  font-weight: 600;
  color: #4DD0E1;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.order-service {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.order-time {
  font-size: 24rpx;
  color: #666;
}

.order-price {
  font-size: 28rpx;
  font-weight: 600;
  color: #ff6b6b;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .stats-content {
    flex-direction: column;
  }
  
  .stat-item {
    flex-direction: row;
    justify-content: space-between;
  }
  
  .stat-icon {
    margin-bottom: 0;
    margin-right: 20rpx;
  }
}
</style>
