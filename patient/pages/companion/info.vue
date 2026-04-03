<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="工作台"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
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
        <view class="info-value">¥{{ (workInfo.monthlyIncome || 0).toLocaleString() }}</view>
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
          <text class="icon">📊</text>
          工作统计
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
          <text class="icon">📋</text>
          最近订单
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
import CompanionNavBar from '@/components/CompanionNavBar.vue'
import {getCompanionWorkInfo, getCompanionOrders} from '@/utils/companion-api';

export default {
  components: {
    CompanionTabBar,
    CompanionNavBar
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
          getCompanionOrders({ page: 1, pageSize: 5 })
        ]);

        if (workInfoRes.code === 200) {
          const data = workInfoRes.data || {};
          console.log('后端返回的数据:', data);
          // 后端字段: activeOrderCount(进行中), pendingOrderCount(待接单), serviceCount, ratingScore
          this.workInfo = {
            todayOrders: Number(data.activeOrderCount || data.today_orders || data.todayOrders || 0),
            pendingOrders: Number(data.pendingOrderCount || data.pending_orders || data.pendingOrders || 0),
            monthlyIncome: Number(data.monthlyIncome || data.monthly_income || data.monthly_income_total || 0),
            rating: Number(data.ratingScore || data.rating_score || data.rating || 0)
          };
          this.workStats = {
            serviceCount: Number(data.serviceCount || data.service_count || data.service_times || 0),
            goodRate: data.onTimeRate != null ? (data.onTimeRate + '%') : (data.good_rate || data.goodRate || '0%'),
            serviceHours: Number(data.serviceHours || data.service_hours || data.service_time || 0)
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
        uni.showToast({title: '获取工作信息失败', icon: 'none'});
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
      const typeMap = {1: '全程陪诊', 2: '代办手续', 3: '接机服务', 4: '送机服务'};
      return typeMap[type] || '陪诊服务';
    },
    formatOrderTime(dateStr) {
      if (!dateStr) return '时间待定';
      const d = new Date(dateStr);
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${m}-${day}`;
    },
  }
}
</script>

<style scoped>
.container {
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  min-height: 100vh;
  padding: 24rpx;
}

/* 信息列表 */
.info-section {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  padding: 32rpx;
  margin: 0 0 24rpx 0;
  border-radius: 22rpx;
  box-shadow: 0 8rpx 32rpx rgba(141, 184, 182, 0.3);
  position: relative;
  overflow: hidden;
}

.info-section::before {
  content: '';
  position: absolute;
  top: -20%;
  right: -20%;
  width: 40%;
  height: 40%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  filter: blur(40rpx);
}

.info-section::after {
  content: '';
  position: absolute;
  bottom: -20%;
  left: -20%;
  width: 40%;
  height: 40%;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  filter: blur(40rpx);
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 2rpx solid rgba(255, 255, 255, 0.2);
  position: relative;
  z-index: 1;
}

.info-item:last-child {
  border-bottom: none;
}

.info-label {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.info-value {
  font-size: 34rpx;
  font-weight: 700;
  color: #fff;
}

/* 工作统计 */
.stats-section {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20rpx);
  padding: 32rpx;
  margin: 0 0 24rpx 0;
  border-radius: 22rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.1);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-header::before {
  content: '';
  width: 8rpx;
  height: 32rpx;
  background: linear-gradient(180deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 4rpx;
  margin-right: 16rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #3a3a4a;
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
  padding: 24rpx;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.stat-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 4rpx 12rpx rgba(141, 184, 182, 0.2);
}

.stat-icon {
  font-size: 48rpx;
  margin-bottom: 16rpx;
}

.stat-value {
  font-size: 36rpx;
  font-weight: 700;
  color: #3a3a4a;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #6a6a7a;
  text-align: center;
}

/* 最近订单 */
.orders-section {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20rpx);
  padding: 32rpx;
  margin: 0 0 24rpx 0;
  border-radius: 22rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.1);
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.order-item {
  padding: 24rpx;
  background: rgba(141, 184, 182, 0.05);
  border-radius: 16rpx;
  border-left: 8rpx solid #8db8b6;
  transition: all 0.3s ease;
}

.order-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 4rpx 12rpx rgba(141, 184, 182, 0.2);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.order-id {
  font-size: 24rpx;
  color: #6a6a7a;
}

.order-status {
  font-size: 24rpx;
  font-weight: 600;
  color: #8db8b6;
  padding: 4rpx 16rpx;
  border-radius: 16rpx;
  background: rgba(141, 184, 182, 0.1);
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 12rpx;
}

.order-service {
  font-size: 28rpx;
  font-weight: 600;
  color: #3a3a4a;
}

.order-time {
  font-size: 24rpx;
  color: #6a6a7a;
}

.order-price {
  font-size: 28rpx;
  font-weight: 600;
  color: #8db8b6;
}

/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .stats-content {
    flex-direction: column;
    gap: 16rpx;
  }

  .stat-item {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .stat-icon {
    margin-bottom: 0;
    margin-right: 20rpx;
  }
}
</style>