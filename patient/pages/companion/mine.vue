<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">个人中心</text>
        </view>
        <view class="header-actions">
          <view class="customer-service" @click="openCustomerService">
            <text class="service-icon">💬</text>
          </view>
        </view>
      </view>
    </view>
    <!-- 个人信息 -->
    <view class="profile-section">
      <view class="profile-avatar">
        <image v-if="companionInfo.certificatePhoto || companionInfo.certificate_photo"
               :src="certificatePhotoSrc"
               class="avatar-img" mode="aspectFill" />
        <text v-else class="avatar-icon">👨‍⚕️</text>
      </view>
      <view class="profile-info">
        <view class="profile-name">{{ companionInfo.realName || companionInfo.real_name || '加载中...' }}</view>
        <view class="profile-id">ID: {{ companionInfo.id || '-' }} | 电话: {{ companionInfo.phone || '-' }}</view>
        <view class="profile-level">
          <text class="level-tag">{{ companionInfo.professionalTitle || companionInfo.professional_title || '陪诊师' }}</text>
          <text class="status-tag" :class="getStatusClass()">{{ getStatusText() }}</text>
        </view>
      </view>
    </view>

    <!-- 服务统计 -->
    <view class="stats-section">
      <view class="stat-item">
        <view class="stat-value">{{ companionInfo.serviceCount ?? companionInfo.service_count ?? '-' }}</view>
        <view class="stat-label">服务次数</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ companionInfo.ratingScore ?? companionInfo.rating_score ?? '-' }}</view>
        <view class="stat-label">服务评分</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ formatRate(companionInfo.onTimeRate ?? companionInfo.on_time_rate) }}</view>
        <view class="stat-label">准时率</view>
      </view>
      <view class="stat-item">
        <view class="stat-value">{{ companionInfo.complaintCount ?? companionInfo.complaint_count ?? '-' }}</view>
        <view class="stat-label">投诉次数</view>
      </view>
    </view>

    <!-- 功能列表 -->
    <view class="menu-section">
      <view class="menu-item" @click="navigateTo('orders')">
        <view class="menu-icon">📋</view>
        <view class="menu-text">我的订单</view>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="navigateTo('schedule')">
        <view class="menu-icon">📅</view>
        <view class="menu-text">日程安排</view>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="navigateTo('patients')">
        <view class="menu-icon">👤</view>
        <view class="menu-text">患者管理</view>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="navigateTo('settings')">
        <view class="menu-icon">⚙️</view>
        <view class="menu-text">个人设置</view>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="navigateTo('training')">
        <view class="menu-icon">🎓</view>
        <view class="menu-text">技能培训</view>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item" @click="navigateTo('help')">
        <view class="menu-icon">❓</view>
        <view class="menu-text">权限管理</view>
        <view class="menu-arrow">›</view>
      </view>
      <view class="menu-item logout-item" @click="handleLogout">
        <view class="menu-icon">🚪</view>
        <view class="menu-text logout-menu-text">退出登录</view>
        <view class="menu-arrow">›</view>
      </view>
    </view>

    <!-- 切换按钮 -->
   <!-- <view class="switch-section">
      <button class="switch-btn" @click="switchToPatient">切换到患者端</button>
    </view> -->

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>

    <!-- 底部导航栏 -->
    <CompanionTabBar activeTab="mine"></CompanionTabBar>
  </view>
</template>

<script>
import CompanionTabBar from '@/components/CompanionTabBar.vue'
import { getCompanionProfile, request } from '@/utils/companion-api';
import { getAvatarUrl } from '@/utils/api';
import { logout } from '@/utils/auth';

export default {
  components: {
    CompanionTabBar
  },
  computed: {
    certificatePhotoSrc() {
      const photo = this.companionInfo.certificatePhoto || this.companionInfo.certificate_photo
      return getAvatarUrl(photo)
    }
  },
  data() {
    return {
      companionInfo: {
        id: 0,
        user_id: 0,
        real_name: '',
        phone: '',
        gender: 0,
        age: 0,
        professional_title: '',
        medical_background: 0,
        certificate_number: '',
        certificate_photo: '',
        criminal_record_cert: '',
        business_license: '',
        language_ability: '',
        specialties: '',
        service_count: 0,
        rating_score: 0,
        on_time_rate: 0,
        complaint_count: 0,
        accompany_status: 0,
        audit_status: 0,
        audit_time: ''
      }
    }
  },
  onLoad() {
    this.getCompanionInfo()
  },
  onShow() {
    this.getCompanionInfo()
  },
  methods: {
    async getCompanionInfo() {
      try {
        // 先按 userId 反查陪诊师记录，获取正确的 accompanistId
        const userId = uni.getStorageSync('userId')
        const byUserRes = await request(`/user/accompanist/by-user/${userId}`)
        if (byUserRes.code === 200 && byUserRes.data) {
          // 缓存正确的 accompanistId（user_accompanist.id）
          uni.setStorageSync('accompanistId', byUserRes.data.id)
          this.companionInfo = byUserRes.data
          uni.setStorageSync('companionInfo', byUserRes.data)
          return
        }
      } catch (e) {
        console.warn('按userId反查失败，尝试缓存', e)
      }
      try {
        const res = await getCompanionProfile();
        if (res.code === 200) {
          this.companionInfo = res.data;
          if (res.data && res.data.id) {
            uni.setStorageSync('accompanistId', res.data.id)
          }
          uni.setStorageSync('companionInfo', res.data);
        }
      } catch (error) {
        console.error('获取陪护员信息失败:', error);
        uni.showToast({ title: '获取信息失败', icon: 'none' });
        const companionInfo = uni.getStorageSync('companionInfo');
        if (companionInfo) {
          this.companionInfo = { ...this.companionInfo, ...companionInfo };
        }
      }
    },
    
    getStatusText() {
      const s = this.companionInfo.accompanyStatus ?? this.companionInfo.accompany_status
      const map = { 1: '空闲', 2: '服务中', 3: '休假', 4: '停用' }
      return map[s] || '未知'
    },
    getStatusClass() {
      const s = this.companionInfo.accompanyStatus ?? this.companionInfo.accompany_status
      return s === 1 ? 'status-free' : s === 2 ? 'status-busy' : s === 3 ? 'status-leave' : 'status-off'
    },
    formatRate(val) {
      if (val === null || val === undefined) return '-'
      // 后端存的是百分比数值如 98.50，直接显示加%
      return val + '%'
    },
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
    handleLogout() {
      uni.showModal({
        title: '退出登录',
        content: '确定要退出登录吗？',
        confirmText: '退出',
        confirmColor: '#F44336',
        success: async (res) => {
          if (res.confirm) {
            try {
              await logout();
            } catch (e) {}
            uni.removeStorageSync('token');
            uni.removeStorageSync('userId');
            uni.removeStorageSync('accompanistId');
            uni.removeStorageSync('companionInfo');
            uni.showToast({ title: '退出登录成功', icon: 'success' });
            setTimeout(() => {
              uni.navigateTo({ url: '/pages/login/login' });
            }, 1000);
          }
        }
      });
    },
    navigateTo(page) {
      switch(page) {
        case 'orders':
          // 跳转到我的订单页面
          uni.navigateTo({
            url: '/pages/companion/orders'
          })
          break
        case 'schedule':
          // 跳转到日程安排页面
          uni.navigateTo({
            url: '/pages/companion/schedule/schedule'
          })
          break
        case 'training':
          // 跳转到技能培训页面
          uni.navigateTo({
            url: '/pages/companion/training/training'
          })
          break
        case 'help':
          // 跳转到权限管理页面
          uni.navigateTo({
            url: '/pages/companion/permissions/permissions'
          })
          break
        case 'settings':
          // 跳转到个人设置页面
          uni.navigateTo({
            url: '/pages/companion/settings/settings'
          })
          break
        case 'patients':
          // 跳转到患者管理页面
          uni.navigateTo({
            url: '/pages/companion/patients/patients'
          })
          break
        default:
          uni.showToast({ title: `跳转到${page}页面`, icon: 'none' })
      }
    },
    switchToPatient() {
      uni.switchTab({
        url: '/pages/home/home'
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

/* 个人信息 */
.profile-section {
  background: #fff;
  padding: 40rpx 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 30rpx;
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

.status-free { background: #E8F5E9; color: #4CAF50; }
.status-busy { background: #F3E5F5; color: #9C27B0; }
.status-leave { background: #FFF3E0; color: #FF9800; }
.status-off { background: #FFEBEE; color: #F44336; }

/* 服务统计 */
.stats-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
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

/* 功能列表 */
.menu-section {
  background: #fff;
  padding: 20rpx 0;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  transition: all 0.3s ease;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background-color: #f8f9fa;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
  width: 40rpx;
  text-align: center;
}

.menu-text {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 32rpx;
  color: #999;
}

/* 切换按钮 */
.switch-section {
  padding: 30rpx;
  margin: 20rpx 0;
}

.switch-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  border-radius: 45rpx;
  border: none;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
  transition: all 0.3s ease;
}

.switch-btn::after {
  border: none;
}

.switch-btn:active {
  transform: translateY(2rpx);
  box-shadow: 0 2rpx 6rpx rgba(77, 208, 225, 0.3);
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .stats-section {
    flex-direction: column;
  }
  
  .stat-item {
    flex-direction: row;
    justify-content: space-between;
  }
  
  .stat-value {
    margin-bottom: 0;
  }
}

/* 退出登录菜单项 */
.logout-item:active {
  background-color: #fff5f5;
}

.logout-menu-text {
  color: #F44336;
}
</style>
