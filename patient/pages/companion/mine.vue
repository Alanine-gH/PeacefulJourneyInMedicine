<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="个人中心"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
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
import CompanionNavBar from '@/components/CompanionNavBar.vue'
import { getCompanionProfile, request } from '@/utils/companion-api';
import { getAvatarUrl } from '@/utils/api';
import { logout } from '@/utils/auth';

export default {
  components: {
    CompanionTabBar,
    CompanionNavBar
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
            uni.removeStorageSync('userType');
            uni.showToast({ title: '退出登录成功', icon: 'success', duration: 600, complete: () => {
              uni.reLaunch({ url: '/pages/login/login' });
            }});
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
  background: linear-gradient(180deg, #f5f3ef 0%, #f0eeea 100%);
  min-height: 100vh;
  padding: 24rpx;
  padding-bottom: 140rpx;
}

/* 个人信息 */
.profile-section {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  padding: 40rpx 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 8rpx 32rpx rgba(141, 184, 182, 0.25);
  display: flex;
  align-items: center;
  gap: 24rpx;
}

.profile-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4rpx solid rgba(255, 255, 255, 0.3);
}

.avatar-icon {
  font-size: 60rpx;
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
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
  margin-bottom: 8rpx;
}

.profile-id {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 12rpx;
}

.profile-level {
  display: flex;
  gap: 12rpx;
}

.level-tag {
  background: rgba(255, 255, 255, 0.25);
  color: #fff;
  padding: 6rpx 16rpx;
  border-radius: 12rpx;
  font-size: 22rpx;
  font-weight: 500;
}

.status-tag {
  padding: 6rpx 16rpx;
  border-radius: 12rpx;
  font-size: 22rpx;
  font-weight: 500;
}

.status-free { background: rgba(255, 255, 255, 0.25); color: #fff; }
.status-busy { background: rgba(212, 168, 176, 0.4); color: #fff; }
.status-leave { background: rgba(232, 196, 168, 0.4); color: #fff; }
.status-off { background: rgba(239, 83, 80, 0.4); color: #fff; }

/* 服务统计 */
.stats-section {
  background: #fff;
  padding: 32rpx;
  margin-bottom: 24rpx;
  border-radius: 28rpx;
  box-shadow: 0 4rpx 24rpx rgba(100, 120, 140, 0.06);
  display: flex;
  justify-content: space-around;
  gap: 16rpx;
}

.stat-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 16rpx;
  background: linear-gradient(135deg, #f8f7f5 0%, #f4f3f1 100%);
  border-radius: 20rpx;
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

/* 功能列表 */
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
  padding: 28rpx 32rpx;
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
  width: 72rpx;
  height: 72rpx;
  border-radius: 18rpx;
  background: linear-gradient(135deg, #e8f0f0 0%, #d8e8e8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  margin-right: 24rpx;
}

.menu-text {
  flex: 1;
  font-size: 30rpx;
  color: #3a3a4a;
  font-weight: 500;
}

.menu-arrow {
  font-size: 32rpx;
  color: #c8c8d8;
}

/* 切换按钮 */
.switch-section {
  padding: 0;
  margin-bottom: 24rpx;
}

.switch-btn {
  width: 100%;
  height: 96rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  border-radius: 48rpx;
  border: none;
  box-shadow: 0 8rpx 24rpx rgba(141, 184, 182, 0.35);
  transition: all 0.3s ease;
}

.switch-btn::after {
  border: none;
}

.switch-btn:active {
  transform: scale(0.98);
  box-shadow: 0 4rpx 16rpx rgba(141, 184, 182, 0.3);
}

/* 底部空白区域 */
.bottom-space {
  height: 40rpx;
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


/* ── companion mine page specifics ── */
.profile-section { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  margin: 16rpx 24rpx !important; padding: 36rpx 28rpx !important;
  box-shadow: 0 8rpx 28rpx rgba(100,175,175,0.22) !important; }
.profile-name { color: #fff !important; }
.profile-id { color: rgba(255,255,255,0.82) !important; }
.profile-avatar { background: rgba(255,255,255,0.25) !important; box-shadow: none !important; }
.avatar-icon { color: #fff !important; }
.stats-section { margin: 0 24rpx !important; padding: 28rpx !important;
  background: #fff !important; }
.menu-section { margin: 0 24rpx 16rpx !important; padding: 8rpx 0 !important; }
.menu-item { padding: 28rpx 30rpx !important; border-bottom-color: #f0ede8 !important; }
.menu-icon { font-size: 34rpx !important; }
.menu-text { color: #3a3a4a !important; font-size: 28rpx !important; }
.menu-arrow { color: #c0b0d8 !important; font-size: 34rpx !important; }
.logout-menu-text { color: #d4a8b0 !important; }

</style>