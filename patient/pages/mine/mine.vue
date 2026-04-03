<template>
  <view class="container">
    <!-- 个人信息卡片 -->
    <view class="profile-card">
      <view v-if="!userInfo" class="user-unlogin" @click="login">
        <view class="profile-avatar">
          <text class="avatar-icon">👤</text>
        </view>
        <view class="login-btn">登录 / 注册</view>
      </view>
      <view v-else class="user-login">
        <view class="profile-main">
          <view class="profile-avatar">
            <image v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl" class="avatar-img" mode="aspectFill"/>
            <text v-else class="avatar-icon">👤</text>
          </view>
          <view class="profile-info">
            <view class="profile-name">{{ userInfo.realName || '患者' }}</view>
            <view class="profile-id">ID: {{ userInfo.id || '-' }}</view>
            <view class="profile-level">
              <text class="level-tag">{{ userInfo.nickname }}</text>
            </view>
          </view>
        </view>
        <view class="profile-actions">
          <view class="action-item" @click="navigateTo('info')">
            <text class="action-icon">📄</text>
            <text class="action-text">修改信息</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 我的订单 -->
    <view class="section-card">
      <view class="section-header" @click="viewAllOrders">
        <view class="section-title-wrapper">
          <text class="section-icon">📋</text>
          <text class="section-title">我的订单</text>
        </view>
        <text class="section-arrow">全部订单 ›</text>
      </view>
      <view class="order-grid">
        <view class="order-item" @click="viewOrders('pending_payment')">
          <view class="order-icon-wrapper blue">
            <text class="order-icon">💳</text>
          </view>
          <text class="order-text">待付款</text>
        </view>
        <view class="order-item" @click="viewOrders('pending_service')">
          <view class="order-icon-wrapper orange">
            <text class="order-icon">📋</text>
          </view>
          <text class="order-text">待服务</text>
        </view>
        <view class="order-item" @click="viewOrders('pending_review')">
          <view class="order-icon-wrapper pink">
            <text class="order-icon">⭐</text>
          </view>
          <text class="order-text">待评价</text>
        </view>
      </view>
    </view>

    <!-- 健康管理 -->
    <view class="section-card">
      <view class="section-header">
        <view class="section-title-wrapper">
          <text class="section-icon">💚</text>
          <text class="section-title">健康管理</text>
        </view>
      </view>
      <view class="menu-grid">
        <view class="menu-item" @click="navigateTo('blood_sugar')">
          <view class="menu-icon-wrapper teal">
            <text class="menu-icon">📊</text>
          </view>
          <text class="menu-text">血糖监测</text>
        </view>
        <view class="menu-item" @click="navigateTo('medication')">
          <view class="menu-icon-wrapper purple">
            <text class="menu-icon">💊</text>
          </view>
          <text class="menu-text">用药提醒</text>
        </view>
        <view class="menu-item" @click="navigateTo('diet')">
          <view class="menu-icon-wrapper orange">
            <text class="menu-icon">🍎</text>
          </view>
          <text class="menu-text">饮食监测</text>
        </view>
      </view>
    </view>

    <!-- 服务管理 -->
    <view class="section-card">
      <view class="section-header">
        <view class="section-title-wrapper">
          <text class="section-icon">⚙️</text>
          <text class="section-title">服务管理</text>
        </view>
      </view>
      <view class="menu-grid">
        <view class="menu-item" @click="navigateTo('appointment')">
          <view class="menu-icon-wrapper blue">
            <text class="menu-icon">📅</text>
          </view>
          <text class="menu-text">病例查询</text>
        </view>
        <view class="menu-item" @click="navigateTo('feedback')">
          <view class="menu-icon-wrapper green">
            <text class="menu-icon">💬</text>
          </view>
          <text class="menu-text">意见反馈</text>
        </view>
        <view class="menu-item" @click="handleLogout">
          <view class="menu-icon-wrapper red">
            <text class="menu-icon">🚪</text>
          </view>
          <text class="menu-text">退出登录</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getUserInfo, logout } from '../../utils/auth';
import { getAvatarUrl } from '../../utils/api';

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
      const cached = uni.getStorageSync('userInfo')
      if (cached) {
        this.userInfo = this.normalizeUser(cached)
      }
      this.fetchUserInfo()
    },
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
        confirmColor: '#d4a8b0',
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
      } catch (error) {}
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
          uni.reLaunch({ url: '/pages/login/login' })
        }
      })
    },
    login() {
      uni.navigateTo({ url: '/pages/login/login' })
    },
    viewAllOrders() {
      uni.navigateTo({ url: '/pages/order/order' });
    },
    viewOrders(status) {
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
        'feedback': '/pages/feedback/feedback',
        'diet': '/pages/diet/diet',
        'medication': '/pages/medication/medication',
        'blood_sugar': '/pages/blood_sugar/blood_sugar',
        'info': '/pages/info/info'
      };

      if (pageMap[page]) {
        uni.navigateTo({ url: pageMap[page] });
      } else {
        uni.showToast({ title: `跳转到${page}页面`, icon: 'none' });
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f5f3ef;
  min-height: 100vh;
  padding: 0 24rpx 40rpx;
}

/* 个人信息卡片 */
.profile-card {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 30rpx;
  padding: 50rpx 40rpx;
  margin: 20rpx 0 30rpx;
  box-shadow: 0 8rpx 32rpx rgba(141, 184, 182, 0.25);
}

.user-unlogin {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.profile-avatar {
  width: 140rpx;
  height: 140rpx;
  border-radius: 50%;
  background: rgba(255,255,255,0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid rgba(255,255,255,0.3);
  margin-bottom: 24rpx;
}

.avatar-icon {
  font-size: 64rpx;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
}

.login-btn {
  font-size: 30rpx;
  color: #fff;
  font-weight: 500;
  background: rgba(255,255,255,0.2);
  padding: 16rpx 50rpx;
  border-radius: 40rpx;
  border: 2rpx solid rgba(255,255,255,0.3);
}

.user-login {
  display: flex;
  flex-direction: column;
  gap: 30rpx;
}

.profile-main {
  display: flex;
  align-items: center;
  gap: 30rpx;
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 38rpx;
  font-weight: 700;
  color: #fff;
  margin-bottom: 10rpx;
}

.profile-id {
  font-size: 24rpx;
  color: rgba(255,255,255,0.8);
  margin-bottom: 16rpx;
}

.profile-level {
  display: flex;
  gap: 12rpx;
}

.level-tag {
  background: rgba(255,255,255,0.25);
  color: #fff;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
  font-size: 22rpx;
  font-weight: 500;
}

.profile-actions {
  display: flex;
  justify-content: flex-end;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 10rpx;
  background: rgba(255,255,255,0.15);
  padding: 14rpx 28rpx;
  border-radius: 30rpx;
}

.action-icon {
  font-size: 28rpx;
}

.action-text {
  font-size: 24rpx;
  color: #fff;
  font-weight: 500;
}

/* 区块卡片 */
.section-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.06);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.section-title-wrapper {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.section-icon {
  font-size: 32rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #3a3a4a;
}

.section-arrow {
  font-size: 26rpx;
  color: #9a9aaa;
}

/* 订单网格 */
.order-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
}

.order-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 16rpx;
  background: #f8f7f5;
  border-radius: 20rpx;
  transition: transform 0.2s ease;
}

.order-item:active {
  transform: scale(0.97);
}

.order-icon-wrapper {
  width: 90rpx;
  height: 90rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.order-icon-wrapper.blue {
  background: linear-gradient(135deg, #e8f0f8 0%, #d4e0f0 100%);
}

.order-icon-wrapper.orange {
  background: linear-gradient(135deg, #f8f0e8 0%, #f0e4d8 100%);
}

.order-icon-wrapper.pink {
  background: linear-gradient(135deg, #f8e8f0 0%, #f0d8e4 100%);
}

.order-icon {
  font-size: 44rpx;
}

.order-text {
  font-size: 26rpx;
  color: #3a3a4a;
  font-weight: 500;
}

/* 菜单网格 */
.menu-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
}

.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 16rpx;
  background: #f8f7f5;
  border-radius: 20rpx;
  transition: transform 0.2s ease;
}

.menu-item:active {
  transform: scale(0.97);
}

.menu-icon-wrapper {
  width: 90rpx;
  height: 90rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
}

.menu-icon-wrapper.teal {
  background: linear-gradient(135deg, #e8f4f4 0%, #d4e8e8 100%);
}

.menu-icon-wrapper.purple {
  background: linear-gradient(135deg, #f0e8f4 0%, #e4d8ec 100%);
}

.menu-icon-wrapper.orange {
  background: linear-gradient(135deg, #f8f0e8 0%, #f0e4d8 100%);
}

.menu-icon-wrapper.blue {
  background: linear-gradient(135deg, #e8f0f8 0%, #d4e0f0 100%);
}

.menu-icon-wrapper.green {
  background: linear-gradient(135deg, #e8f4f0 0%, #d4e8e0 100%);
}

.menu-icon-wrapper.red {
  background: linear-gradient(135deg, #f8e8e8 0%, #f0d8d8 100%);
}

.menu-icon {
  font-size: 44rpx;
}

.menu-text {
  font-size: 26rpx;
  color: #3a3a4a;
  font-weight: 500;
}
</style>
