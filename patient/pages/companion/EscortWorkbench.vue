<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="工作台"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 统计卡片 -->
    <view class="stats-card">
      <view class="stats-item">
        <text class="stats-label">今日订单</text>
        <text class="stats-value">1</text>
      </view>
      <view class="stats-item">
        <text class="stats-label">待处理订单</text>
        <text class="stats-value">1</text>
      </view>
      <view class="stats-item">
        <text class="stats-label">本月收入</text>
        <text class="stats-value">¥0</text>
      </view>
      <view class="stats-item">
        <text class="stats-label">服务评分</text>
        <text class="stats-value">4.99</text>
      </view>
    </view>

    <!-- 工作统计 -->
    <view class="work-stats">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">📈</text>
          工作统计
        </text>
      </view>
      <view class="stats-grid">
        <view class="stat-card">
          <view class="stat-icon calendar">
            <text>📅</text>
          </view>
          <view class="stat-content">
            <text class="stat-number">150</text>
            <text class="stat-desc">本月服务次数</text>
          </view>
        </view>
        <view class="stat-card">
          <view class="stat-icon star">
            <text>⭐</text>
          </view>
          <view class="stat-content">
            <text class="stat-number">99%</text>
            <text class="stat-desc">好评率</text>
          </view>
        </view>
        <view class="stat-card">
          <view class="stat-icon clock">
            <text>⏰</text>
          </view>
          <view class="stat-content">
            <text class="stat-number">0</text>
            <text class="stat-desc">服务时长(小时)</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 最近订单 -->
    <view class="recent-orders">
      <view class="section-header">
        <text class="section-title">最近订单</text>
      </view>
      <view class="order-list">
        <view class="order-item" v-for="order in orders" :key="order.id">
          <view class="order-header">
            <text class="order-id">订单号：{{ order.id }}</text>
            <view class="order-status" :class="order.statusColor">
              {{ order.status }}
            </view>
          </view>
          <view class="order-service">{{ order.service }}</view>
          <view class="order-date">{{ order.date }}</view>
          <view class="order-footer">
            <text class="order-amount">¥{{ order.amount.toFixed(2) }}</text>
            <view class="order-detail" @click="viewOrderDetail(order.id)">
              <text>查看详情</text>
              <text class="arrow">›</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>

    <!-- 底部导航栏 -->
    <CompanionTabBar activeTab="workbench"></CompanionTabBar>
  </view>
</template>

<script>
import CompanionTabBar from '@/components/CompanionTabBar.vue'
import CompanionNavBar from '@/components/CompanionNavBar.vue'

export default {
  components: {
    CompanionTabBar,
    CompanionNavBar
  },
  data() {
    return {
      orders: [
        {
          id: 'ORD20260312012',
          service: '代办手续',
          date: '04-03',
          amount: 600.00,
          status: '已确认',
          statusColor: 'status-green'
        },
        {
          id: 'ORD20260311011',
          service: '全程陪诊',
          date: '04-02',
          amount: 500.00,
          status: '已确认',
          statusColor: 'status-green'
        },
        {
          id: 'ORD20260313013',
          service: '全程陪诊',
          date: '03-25',
          amount: 800.00,
          status: '服务中',
          statusColor: 'status-orange'
        },
        {
          id: 'ORD20260301001',
          service: '全程陪诊',
          date: '03-10',
          amount: 800.00,
          status: '已完成',
          statusColor: 'status-purple'
        },
        {
          id: 'ORD20260306006',
          service: '接机服务',
          date: '时间待定',
          amount: 200.00,
          status: '已完成',
          statusColor: 'status-purple'
        }
      ]
    }
  },
  methods: {
    viewOrderDetail(orderId) {
      uni.navigateTo({
        url: `/pages/companion/orders?orderId=${orderId}`
      })
    }
  }
}
</script>

<style scoped>
.container {
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  min-height: 100vh;
  padding: 24rpx;
  padding-bottom: 140rpx;
}

/* 统计卡片 */
.stats-card {
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.5), rgba(150, 196, 180, 0.4));
  backdrop-filter: blur(20rpx);
  border-radius: 28rpx;
  padding: 40rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.05);
}

.stats-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid rgba(255, 255, 255, 0.3);
}

.stats-item:last-child {
  border-bottom: none;
}

.stats-label {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
}

.stats-value {
  font-size: 36rpx;
  font-weight: 700;
  color: #fff;
}

/* 工作统计 */
.work-stats {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 32rpx;
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
  color: #5B6B78;
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.section-title .icon {
  font-size: 32rpx;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24rpx;
}

.stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
}

.stat-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.stat-icon.calendar {
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.3), rgba(174, 196, 223, 0.25));
}

.stat-icon.star {
  background: linear-gradient(135deg, rgba(230, 188, 165, 0.3), rgba(214, 166, 143, 0.25));
}

.stat-icon.clock {
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.3), rgba(150, 196, 180, 0.25));
}

.stat-content {
  text-align: center;
}

.stat-number {
  font-size: 32rpx;
  font-weight: 700;
  color: #4A5A68;
  margin-bottom: 4rpx;
  display: block;
}

.stat-desc {
  font-size: 20rpx;
  color: #8A9BA8;
}

/* 最近订单 */
.recent-orders {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.order-item {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(20rpx);
  border-radius: 22rpx;
  padding: 32rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border-left: 16rpx solid rgba(168, 212, 196, 0.4);
}

.order-item:active {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.order-id {
  font-size: 22rpx;
  color: #A5B5C0;
}

.order-status {
  font-size: 20rpx;
  padding: 4rpx 16rpx;
  border-radius: 16rpx;
  font-weight: 500;
}

.status-green {
  background: rgba(122, 168, 158, 0.2);
  color: #7AA89E;
}

.status-orange {
  background: rgba(201, 150, 127, 0.2);
  color: #C9967F;
}

.status-purple {
  background: rgba(149, 135, 171, 0.2);
  color: #9587AB;
}

.order-service {
  font-size: 30rpx;
  font-weight: 600;
  color: #4A5A68;
  margin-bottom: 8rpx;
}

.order-date {
  font-size: 22rpx;
  color: #8A9BA8;
  margin-bottom: 24rpx;
}

.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 24rpx;
  border-top: 1rpx solid rgba(229, 227, 223, 0.5);
}

.order-amount {
  font-size: 32rpx;
  font-weight: 700;
  color: #B88299;
}

.order-detail {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: #A8D4C4;
  font-size: 24rpx;
  transition: color 0.3s ease;
}

.order-detail:active {
  color: #96C4B4;
}

.arrow {
  font-size: 28rpx;
  font-weight: bold;
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .stats-card {
    padding: 32rpx;
  }
  
  .stats-value {
    font-size: 32rpx;
  }
  
  .stat-icon {
    width: 48rpx;
    height: 48rpx;
    font-size: 24rpx;
  }
  
  .stat-number {
    font-size: 28rpx;
  }
  
  .order-item {
    padding: 24rpx;
  }
}
</style>