<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="个人中心"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 用户信息卡片 -->
    <view class="profile-card">
      <view class="profile-avatar">
        <text>👨‍⚕️</text>
      </view>
      <view class="profile-info">
        <text class="profile-name">赵敏</text>
        <text class="profile-details">ID: 71 | 电话: +86 13900139001</text>
        <view class="profile-tags">
          <view class="tag">小双</view>
          <view class="tag member">会员</view>
        </view>
      </view>
    </view>

    <!-- 统计数据 -->
    <view class="stats-section">
      <view class="stat-item">
        <view class="stat-icon calendar">
          <text>📅</text>
        </view>
        <text class="stat-number">150</text>
        <text class="stat-label">服务次数</text>
      </view>
      <view class="stat-item">
        <view class="stat-icon star">
          <text>⭐</text>
        </view>
        <text class="stat-number">4.99</text>
        <text class="stat-label">服务评分</text>
      </view>
      <view class="stat-item">
        <view class="stat-icon clock">
          <text>⏰</text>
        </view>
        <text class="stat-number">99%</text>
        <text class="stat-label">准时率</text>
      </view>
      <view class="stat-item">
        <view class="stat-icon alert">
          <text>⚠️</text>
        </view>
        <text class="stat-number">0</text>
        <text class="stat-label">投诉次数</text>
      </view>
    </view>

    <!-- 功能菜单 -->
    <view class="menu-section">
      <view class="menu-item" v-for="item in menuItems" :key="item.id" @click="handleMenuClick(item.id)">
        <view class="menu-icon" :class="item.bgColor">
          <text>{{ item.icon }}</text>
        </view>
        <text class="menu-title">{{ item.title }}</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>

    <!-- 底部导航栏 -->
    <CompanionTabBar activeTab="mine"></CompanionTabBar>
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
      menuItems: [
        {
          id: 1,
          icon: '📋',
          title: '我的订单',
          bgColor: 'bg-orange'
        },
        {
          id: 2,
          icon: '📅',
          title: '日程安排',
          bgColor: 'bg-blue'
        },
        {
          id: 3,
          icon: '👥',
          title: '患者管理',
          bgColor: 'bg-purple'
        },
        {
          id: 4,
          icon: '⚙️',
          title: '个人设置',
          bgColor: 'bg-gray'
        },
        {
          id: 5,
          icon: '🎓',
          title: '技能培训',
          bgColor: 'bg-purple'
        },
        {
          id: 6,
          icon: '🔒',
          title: '权限管理',
          bgColor: 'bg-red'
        },
        {
          id: 7,
          icon: '🚪',
          title: '退出登录',
          bgColor: 'bg-gray'
        }
      ]
    }
  },
  methods: {
    handleMenuClick(id) {
      switch (id) {
        case 1:
          uni.navigateTo({
            url: '/pages/companion/orders'
          })
          break
        case 2:
          uni.navigateTo({
            url: '/pages/companion/schedule/schedule'
          })
          break
        case 3:
          uni.navigateTo({
            url: '/pages/companion/patients/patients'
          })
          break
        case 4:
          uni.navigateTo({
            url: '/pages/companion/settings/settings'
          })
          break
        case 5:
          uni.navigateTo({
            url: '/pages/companion/training'
          })
          break
        case 6:
          uni.navigateTo({
            url: '/pages/companion/permissions/permissions'
          })
          break
        case 7:
          uni.showModal({
            title: '退出登录',
            content: '确定要退出登录吗？',
            success: (res) => {
              if (res.confirm) {
                uni.navigateTo({
                  url: '/pages/login/login'
                })
              }
            }
          })
          break
        default:
          uni.showToast({
            title: '功能开发中',
            icon: 'none'
          })
      }
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

/* 用户信息卡片 */
.profile-card {
  background: linear-gradient(135deg, rgba(168, 212, 196, 0.5), rgba(150, 196, 180, 0.4));
  backdrop-filter: blur(20rpx);
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 32rpx;
}

.profile-avatar {
  width: 128rpx;
  height: 128rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #C8C4BF 0%, #B8B4AF 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 32rpx rgba(150, 196, 180, 0.25);
  font-size: 64rpx;
}

.profile-info {
  flex: 1;
}

.profile-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
  margin-bottom: 8rpx;
  display: block;
}

.profile-details {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16rpx;
  display: block;
}

.profile-tags {
  display: flex;
  gap: 12rpx;
}

.tag {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.9);
  background: rgba(255, 255, 255, 0.25);
  backdrop-filter: blur(10rpx);
  padding: 6rpx 16rpx;
  border-radius: 16rpx;
}

.tag.member {
  background: rgba(168, 212, 196, 0.4);
  backdrop-filter: blur(10rpx);
}

/* 统计数据 */
.stats-section {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24rpx;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
}

.stat-icon {
  width: 48rpx;
  height: 48rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
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

.stat-icon.alert {
  background: linear-gradient(135deg, rgba(232, 187, 209, 0.3), rgba(223, 174, 196, 0.25));
}

.stat-number {
  font-size: 28rpx;
  font-weight: 700;
  color: #4A5A68;
}

.stat-label {
  font-size: 20rpx;
  color: #8A9BA8;
  text-align: center;
}

/* 功能菜单 */
.menu-section {
  background: #fff;
  padding: 16rpx 0;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx;
  border-bottom: 1rpx solid rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:active {
  background-color: rgba(141, 184, 182, 0.05);
}

.menu-icon {
  width: 48rpx;
  height: 48rpx;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  margin-right: 24rpx;
}

.menu-icon.bg-orange {
  background: linear-gradient(135deg, rgba(232, 207, 187, 0.2), rgba(223, 196, 174, 0.15));
}

.menu-icon.bg-blue {
  background: linear-gradient(135deg, rgba(187, 209, 232, 0.2), rgba(174, 196, 223, 0.15));
}

.menu-icon.bg-purple {
  background: linear-gradient(135deg, rgba(200, 184, 214, 0.2), rgba(184, 168, 201, 0.15));
}

.menu-icon.bg-gray {
  background: linear-gradient(135deg, rgba(165, 181, 192, 0.2), rgba(149, 165, 176, 0.15));
}

.menu-icon.bg-red {
  background: linear-gradient(135deg, rgba(230, 166, 143, 0.2), rgba(214, 150, 127, 0.15));
}

.menu-title {
  flex: 1;
  font-size: 30rpx;
  color: #4A5A68;
  font-weight: 500;
}

.menu-arrow {
  font-size: 32rpx;
  color: #A5B5C0;
  font-weight: bold;
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .profile-card {
    padding: 32rpx;
    gap: 24rpx;
  }
  
  .profile-avatar {
    width: 100rpx;
    height: 100rpx;
    font-size: 50rpx;
  }
  
  .profile-name {
    font-size: 32rpx;
  }
  
  .stats-section {
    gap: 16rpx;
  }
  
  .stat-icon {
    width: 40rpx;
    height: 40rpx;
    font-size: 20rpx;
  }
  
  .stat-number {
    font-size: 24rpx;
  }
  
  .menu-item {
    padding: 28rpx;
  }
}
</style>