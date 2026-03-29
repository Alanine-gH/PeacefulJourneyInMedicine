<template>
  <view class="container">
    <!-- 顶部个人信息区域 -->
    <!-- <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">个人中心</text>
        </view>
      </view>
    </view> -->

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
        uni.removeStorageSync('token');
        uni.removeStorageSync('userId');
        uni.removeStorageSync('userInfo');
        this.userInfo = null;
        uni.showToast({
          title: '退出登录成功',
          icon: 'success'
        })
      } catch (error) {
        uni.removeStorageSync('token');
        uni.removeStorageSync('userId');
        uni.removeStorageSync('userInfo');
        this.userInfo = null;
      }
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
      // 映射到订单页面的筛选状态
      const statusMap = {
        'pending_payment': 'unpaid', // 待付款 -> 待支付
        'pending_service': 'pending', // 待服务 -> 待服务
        'pending_review': 'completed' // 待评价 -> 已完成
      };
      const targetStatus = statusMap[status] || 'all';
      uni.navigateTo({
        url: `/pages/order/order?status=${targetStatus}`
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
    },
    switchToCompanion() {
      // 跳转到陪护端页面
      uni.navigateTo({
        url: '/pages/companion/home'
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
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
  color: #4DD0E1;
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
  background-color: #f8f9fa;
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
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

.status-busy {
  background: #F3E5F5;
  color: #9C27B0;
}

.status-leave {
  background: #FFF3E0;
  color: #FF9800;
}

.status-off {
  background: #FFEBEE;
  color: #F44336;
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

</style>