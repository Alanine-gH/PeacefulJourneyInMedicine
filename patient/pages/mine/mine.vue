<template>
  <view class="container">
    <!-- 个人信息 -->
    <view class="profile-section">
      <view v-if="!userInfo" class="user-unlogin" @click="login">
        <view class="profile-avatar">
          <text class="avatar-icon">👤</text>
        </view>
        <view class="login-btn">登录 / 注册</view>
      </view>
      <view v-else class="user-login">
        <view class="profile-avatar">
          <image v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl" class="avatar-img" mode="aspectFill"/>
          <text v-else class="avatar-icon">👤</text>
        </view>
        <view class="profile-info">
          <view class="profile-name">{{ userInfo.realName || '患者' }}</view>
          <view class="profile-id">ID: {{ userInfo.id || '-' }} | 电话: {{ userInfo.phone || '-' }}</view>
          <view class="profile-level">
            <text class="level-tag">{{ userInfo.nickname }}</text>
            <text class="status-tag status-free" @click="navigateTo('info')">信息</text>
          </view>
        </view>
        <view class="info-button" @click="navigateTo('info')">
          <text class="info-button-icon">📄</text>
          <text class="info-button-text">修改信息</text>
        </view>
      </view>
    </view>

    <!-- 我的订单 -->
    <view class="order-section">
      <view class="section-header" @click="viewAllOrders">
        <text class="section-title">我的订单</text>
        <text class="section-arrow">全部订单 ›</text>
      </view>
      <view class="order-status">
        <view class="status-item" @click="viewOrders('pending_payment')">
          <view class="status-icon">💳</view>
          <text class="status-text">待付款</text>
        </view>
        <view class="status-item" @click="viewOrders('pending_service')">
          <view class="status-icon">📋</view>
          <text class="status-text">待服务</text>
        </view>
        <view class="status-item" @click="viewOrders('pending_review')">
          <view class="status-icon">⭐</view>
          <text class="status-text">待评价</text>
        </view>
      </view>
    </view>

    <!-- 健康管理 -->
    <view class="health-section">
      <view class="section-header">
        <text class="section-title">健康管理</text>
      </view>
      <view class="health-grid">
        <view class="health-item" @click="navigateTo('blood_sugar')">
          <view class="health-icon">📊</view>
          <text class="health-text">血糖监测</text>
          <text class="health-arrow">›</text>
        </view>
        <view class="health-item" @click="navigateTo('medication')">
          <view class="health-icon">💊</view>
          <text class="health-text">用药提醒</text>
          <text class="health-arrow">›</text>
        </view>
        <view class="health-item" @click="navigateTo('diet')">
          <view class="health-icon">🍎</view>
          <text class="health-text">饮食监测</text>
          <text class="health-arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 服务管理 -->
    <view class="service-section">
      <view class="section-header">
        <text class="section-title">服务管理</text>
      </view>
      <view class="service-grid">
        <view class="service-item" @click="navigateTo('appointment')">
          <view class="service-icon">📅</view>
          <text class="service-text">病例查询</text>
          <text class="service-arrow">›</text>
        </view>
        <view class="service-item" @click="handleLogout">
          <view class="service-icon">❓</view>
          <text class="service-text">退出登录</text>
          <text class="service-arrow">›</text>
        </view>
        <view class="service-item" @click="navigateTo('feedback')">
          <view class="service-icon">💬</view>
          <text class="service-text">意见反馈</text>
          <text class="service-arrow">›</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import {
  getUserInfo,
  logout
} from '../../utils/auth';
import {
  getAvatarUrl
} from '../../utils/api';

export default {
  data() {
    return {
      userInfo: null
    }
  },
  onShow() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = uni.getStorageSync('token')
      if (!token) {
        this.userInfo = null
        return
      }
      // 优先用本地缓存快速展示
      const cached = uni.getStorageSync('userInfo')
      if (cached) {
        this.userInfo = this.normalizeUser(cached)
      }
      // 每次进入页面从后端刷新最新数据
      this.fetchUserInfo()
    },
    // 统一字段映射：兼容 LoginVO 和 UserDetailVO 两种结构
    normalizeUser(raw) {
      if (!raw) return null
      return {
        id: raw.id || raw.userId,
        nickname: raw.nickName || raw.nickname || raw.username || '用户',
        avatarUrl: getAvatarUrl(raw.avatarUrl || raw.avatar || ''),
        phone: raw.phone || '',
        realName: raw.realName || '',
        userType: raw.userType,
        username: raw.username || '',
        age: raw.age,
        gender: raw.gender,
        email: raw.email || ''
      }
    },
    async fetchUserInfo() {
      try {
        const response = await getUserInfo();
        if (response && response.code === 200 && response.data) {
          const normalized = this.normalizeUser(response.data)
          this.userInfo = normalized
          uni.setStorageSync('userInfo', response.data)
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
      }
    },
    handleLogout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        confirmText: '退出',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            this.logout();
          }
        }
      });
    },
    async logout() {
      try {
        await logout();
      } catch (error) {
        // 忽略退出接口异常，仍执行本地退出
      }
      uni.removeStorageSync('token');
      uni.removeStorageSync('userId');
      uni.removeStorageSync('userInfo');
      uni.removeStorageSync('userType');
      uni.removeStorageSync('accompanistId');
      this.userInfo = null;
            uni.showToast({
              title: '退出登录成功',
              icon: 'success',
              duration: 600,
              complete: () => {
                uni.reLaunch({
                  url: '/pages/login/login'
                })
              }
            })
    },
    login() {
      uni.navigateTo({
        url: '/pages/login/login'
      })
    },
    viewAllOrders() {
      uni.navigateTo({
        url: '/pages/order/order'
      });
    },
    viewOrders(status) {
      // 映射到订单页面的筛选状态（与 order.vue 对齐）
      const statusMap = {
        'pending_payment': 1,
        'pending_service': 3,
        'pending_review': 5
      };
      const targetStatus = statusMap[status];
      uni.navigateTo({
        url: targetStatus ? `/pages/order/order?status=${targetStatus}` : '/pages/order/order'
      });
    },
    navigateTo(page) {
      const pageMap = {
        'appointment': '/pages/medical-record/medical-record',
        'favorites': '/pages/favorites/favorites',
        'help': '/pages/customer/customer',
        'feedback': '/pages/feedback/feedback',
        'diet': '/pages/diet/diet',
        'medication': '/pages/medication/medication',
        'blood_sugar': '/pages/blood_sugar/blood_sugar',
        'info': '/pages/info/info'
      };

      if (pageMap[page]) {
        uni.navigateTo({
          url: pageMap[page]
        });
      } else {
        uni.showToast({
          title: `跳转到${page}页面`,
          icon: 'none'
        });
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 个人信息 */
.profile-section {
  background: #fff;
  padding: 40rpx 30rpx;
  margin: 20rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 30rpx;
}

.user-unlogin {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  padding: 30rpx 0;
}

.profile-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.avatar-icon {
  font-size: 60rpx;
  color: white;
}

.avatar-img {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  object-fit: cover;
}

.login-btn {
  font-size: 28rpx;
  color: #8db8b6;
  font-weight: 500;
  background-color: rgba(77, 208, 225, 0.1);
  padding: 10rpx 40rpx;
  border-radius: 20rpx;
  margin-top: 20rpx;
}

.user-login {
  display: flex;
  align-items: center;
  gap: 30rpx;
  width: 100%;
  justify-content: space-between;
}

.info-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx;
  background-color: #f4f2ee;
  border-radius: 15rpx;
  cursor: pointer;
  transition: all 0.3s ease;
}

.info-button:hover {
  background-color: #e9ecef;
  transform: translateY(-2rpx);
}

.info-button-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.info-button-text {
  font-size: 24rpx;
  color: #666;
  font-weight: 500;
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.profile-id {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 10rpx;
}

.profile-level {
  display: inline-block;
}

.level-tag {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: white;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
  font-weight: 500;
  margin-right: 10rpx;
}

.status-tag {
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
  font-weight: 500;
}

.status-free {
  background: #E8F5E9;
  color: #4CAF50;
}

/* 我的订单 */
.order-section {
  background-color: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.section-arrow {
  font-size: 24rpx;
  color: #999;
}

.order-status {
  display: flex;
  justify-content: space-around;
}

.status-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.status-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.status-text {
  font-size: 24rpx;
  color: #666;
}

/* 健康管理 */
.health-section {
  background-color: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.health-grid {
  display: flex;
  flex-wrap: wrap;
  margin-top: 20rpx;
}

.health-item {
  width: 33.33%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
  cursor: pointer;
}

.health-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.health-text {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 5rpx;
}

.health-arrow {
  font-size: 20rpx;
  color: #999;
}

/* 服务管理 */
.service-section {
  background-color: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.service-grid {
  display: flex;
  flex-wrap: wrap;
  margin-top: 20rpx;
}

.service-item {
  width: 33.33%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
  cursor: pointer;
}

.service-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.service-text {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 5rpx;
}

.service-arrow {
  font-size: 20rpx;
  color: #999;
}


/* ── Shared theme overrides ── */
page { background-color: #f4f2ee !important; }

.container { background-color: #f4f2ee !important; min-height: 100vh; }

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
  box-shadow: 0 4rpx 20rpx rgba(100,120,140,0.10) !important;
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
  box-shadow: 0 4rpx 16rpx rgba(100,175,175,0.28) !important;
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
.order-status { color: #8db8b6 !important; }

/* Section titles */
.section-title { color: #3a3a4a !important; font-weight: 600 !important; }

/* Stat items */
.stat-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

/* Tab bar selected */
.tab-item.active,
.tab-item.active .tab-text { color: #8db8b6 !important; }

/* Profile avatar ring */
.profile-avatar {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 12rpx rgba(100,175,175,0.28) !important;
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
.carousel-item        { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }
.carousel-item.vip    { background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important; }
.carousel-item.full   { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }
.carousel-item.consult{ background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important; }
.carousel-item.result { background: linear-gradient(135deg, #d4a8b0 0%, #c09098 100%) !important; }
.carousel-item.medicine{ background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%) !important; }
.carousel-item.basic  { background: linear-gradient(135deg, #a8cec8 0%, #8db8b0 100%) !important; }

/* Step indicator */
.step-num { border-color: #8db8b6 !important; color: #8db8b6 !important; }
.step-item.active .step-num,
.step-item.completed .step-num {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important; border-color: transparent !important;
}
.step-line.active { background-color: #8db8b6 !important; }

/* Health exam header */
.header { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important; }

/* Package type tags */
.pkg-type-tag { background: #e8f4f4 !important; }
.pkg-type-tag .type-text { color: #6a9ea0 !important; }

/* Input focus ring */
.input-item:focus-within { border-color: #8db8b6 !important; box-shadow: 0 0 0 3rpx rgba(141,184,182,0.18) !important; }


/* ── mine page specifics ── */
.profile-section { padding: 40rpx 30rpx !important; margin: 16rpx 24rpx !important;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 8rpx 28rpx rgba(100,175,175,0.22) !important; }
.profile-name { color: #fff !important; font-size: 34rpx !important; font-weight: 700 !important; }
.profile-id { color: rgba(255,255,255,0.80) !important; }
.profile-avatar { background: rgba(255,255,255,0.25) !important; box-shadow: none !important; }
.avatar-icon { color: #fff !important; }
.level-tag { background: rgba(255,255,255,0.28) !important; color: #fff !important; }
.status-tag { backdrop-filter: blur(4px); }
.login-btn { background: rgba(255,255,255,0.25) !important; color: #fff !important;
  border: 2rpx solid rgba(255,255,255,0.5) !important; border-radius: 30rpx !important; }
.info-button { background: rgba(255,255,255,0.20) !important; border-radius: 12rpx !important; }
.info-button-text { color: #fff !important; }
.order-section { margin: 0 24rpx !important; padding: 24rpx !important; }
.health-section { margin: 0 24rpx !important; padding: 24rpx !important; }
.health-item { background: #f0f4f4 !important; border-radius: 14rpx !important; }
.status-item { background: #f0f4f4 !important; border-radius: 14rpx !important; }

</style>