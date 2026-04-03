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
  background-color: #f4f2ee;
  min-height: 100vh;
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
  background: #f4f2ee;
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
  background: #f4f2ee;
  border-radius: 15rpx;
  border-left: 5rpx solid #8db8b6;
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
  color: #8db8b6;
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

/* 顶部空白区域 */
.top-space {
  height: 88rpx;
  width: 100%;
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


/* ── companion info page specifics ── */
.info-section {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  padding: 32rpx !important;
  margin: 16rpx 24rpx !important;
  box-shadow: 0 8rpx 28rpx rgba(100, 175, 175, 0.22) !important;
}

.info-item {
  border-bottom-color: rgba(255, 255, 255, 0.20) !important;
  padding: 18rpx 0 !important;
}

.info-label {
  color: rgba(255, 255, 255, 0.80) !important;
  font-size: 26rpx !important;
}

.info-value {
  color: #fff !important;
  font-size: 34rpx !important;
  font-weight: 700 !important;
}

.stats-section {
  margin: 0 24rpx !important;
  padding: 28rpx !important;
}

.orders-section {
  margin: 20rpx 24rpx 16rpx !important;
  padding: 28rpx !important;
}

.stat-item {
  background: #f4f2ee !important;
}

.stat-icon {
  font-size: 40rpx;
}

.stat-value {
  color: #3a3a4a !important;
  font-size: 36rpx !important;
  font-weight: 700 !important;
}

.stat-label {
  color: #6a6a7a !important;
}

.order-item {
  border-left-color: #8db8b6 !important;
  background: #f4f2ee !important;
}

.order-status {
  color: #8db8b6 !important;
  font-weight: 600 !important;
}

.order-price {
  color: #d4a8b0 !important;
}

</style>