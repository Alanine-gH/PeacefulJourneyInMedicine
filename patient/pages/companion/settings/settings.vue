<template>
  <view class="container">
    <!-- 导航栏 -->
    <CompanionNavBar title="个人设置" showBack="true"></CompanionNavBar>
    
    <!-- 顶部空白区域 -->
    <view class="top-space"></view>
    
    <!-- 个人信息设置 -->
    <view class="section">
      <view class="section-title">个人信息</view>
      <view class="setting-item" @click="editProfile">
        <view class="setting-label">基本资料</view>
        <view class="setting-value">{{ companionInfo.realName || companionInfo.real_name || '未设置' }}</view>
        <view class="setting-arrow">›</view>
      </view>
      <view class="setting-item" @click="editAvatar">
        <view class="setting-label">头像</view>
        <view class="avatar-preview">
          <image v-if="companionInfo.certificatePhoto || companionInfo.certificate_photo" 
                 :src="certificatePhotoSrc" 
                 class="avatar-img" mode="aspectFill" />
          <text v-else class="avatar-icon">👨‍⚕️</text>
        </view>
        <view class="setting-arrow">›</view>
      </view>
      <view class="setting-item">
        <view class="setting-label">手机号</view>
        <view class="setting-value">{{ companionInfo.phone || '未设置' }}</view>
        <view class="setting-arrow">›</view>
      </view>
    </view>

    <!-- 账号安全 -->
    <view class="section">
      <view class="section-title">账号安全</view>
      <view class="setting-item" @click="changePassword">
        <view class="setting-label">修改密码</view>
        <view class="setting-arrow">›</view>
      </view>
      <!-- <view class="setting-item" @click="bindPhone">
        <view class="setting-label">绑定手机</view>
        <view class="setting-value">{{ companionInfo.phone ? '已绑定' : '未绑定' }}</view>
        <view class="setting-arrow">›</view>
      </view> -->
    </view>

    <!-- 工作设置 -->
    <!-- <view class="section">
      <view class="section-title">工作设置</view>
      <view class="setting-item" @click="setWorkStatus">
        <view class="setting-label">工作状态</view>
        <view class="setting-value">{{ getStatusText() }}</view>
        <view class="setting-arrow">›</view>
      </view>
      <view class="setting-item" @click="setWorkTime">
        <view class="setting-label">工作时间</view>
        <view class="setting-value">设置</view>
        <view class="setting-arrow">›</view>
      </view>
    </view> -->
    <!-- 存储管理 -->
    <view class="section">
      <view class="section-title">存储管理</view>
      <view class="setting-item" @click="clearCache">
        <view class="setting-label">清除缓存</view>
        <view class="setting-value">释放本地存储空间</view>
        <view class="setting-arrow">›</view>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import CompanionNavBar from '@/components/CompanionNavBar.vue'
import { getCompanionProfile, updateCompanionProfile } from '@/utils/companion-api';
import { getAvatarUrl } from '@/utils/api';

export default {
  components: {
    CompanionNavBar
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
  computed: {
    certificatePhotoSrc() {
      const photo = this.companionInfo.certificatePhoto || this.companionInfo.certificate_photo
      return getAvatarUrl(photo)
    }
  },
  onLoad() {
    this.getCompanionInfo()
  },
  methods: {
    async getCompanionInfo() {
      try {
        const res = await getCompanionProfile();
        if (res.code === 200) {
          this.companionInfo = res.data;
        }
      } catch (error) {
        console.error('获取陪护员信息失败:', error);
        uni.showToast({ title: '获取信息失败', icon: 'none' });
      }
    },
    
    getStatusText() {
      const s = this.companionInfo.accompanyStatus ?? this.companionInfo.accompany_status
      const map = { 1: '空闲', 2: '服务中', 3: '休假', 4: '停用' }
      return map[s] || '未知'
    },
    

    
    editProfile() {
      // 跳转到编辑个人资料页面
      uni.navigateTo({
        url: '/pages/companion/settings/edit-profile'
      })
    },
    
    editAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          uni.showLoading({ title: '上传中...' })
          const token = uni.getStorageSync('token') || ''
          uni.uploadFile({
            url: 'http://localhost:8080/upload',
            filePath: tempFilePath,
            name: 'file',
            header: { 'Authorization': 'Bearer ' + token },
            success: async (uploadRes) => {
              uni.hideLoading()
              try {
                const data = JSON.parse(uploadRes.data)
                if (data.code === 200 || data.code === 1) {
                  const fileName = data.data
                  // 获取陪诊师ID
                  const id = this.companionInfo.id || uni.getStorageSync('accompanistId')
                  if (!id) {
                    uni.showToast({ title: '未找到陪诊师ID，请重新登录', icon: 'none' })
                    return
                  }
                  uni.showLoading({ title: '保存中...' })
                  try {
                    // 构造完整的更新实体，带上所有现有字段 + 新头像
                    const updateData = {
                      realName: this.companionInfo.realName || this.companionInfo.real_name || '',
                      phone: this.companionInfo.phone || '',
                      gender: this.companionInfo.gender || 0,
                      age: this.companionInfo.age || 0,
                      professionalTitle: this.companionInfo.professionalTitle || this.companionInfo.professional_title || '',
                      languageAbility: this.companionInfo.languageAbility || this.companionInfo.language_ability || '',
                      specialties: this.companionInfo.specialties || '',
                      certificatePhoto: fileName
                    }
                    const saveRes = await updateCompanionProfile(id, updateData)
                    uni.hideLoading()
                    if (saveRes && (saveRes.code === 200 || saveRes.code === 1)) {
                      // 更新本地预览（用 getAvatarUrl 转为完整 URL）
                      this.$set(this.companionInfo, 'certificatePhoto', fileName)
                      this.$set(this.companionInfo, 'certificate_photo', fileName)
                      uni.showToast({ title: '头像更新成功', icon: 'success' })
                    } else {
                      uni.showToast({ title: (saveRes && saveRes.msg) || '保存失败', icon: 'none' })
                    }
                  } catch (e) {
                    uni.hideLoading()
                    console.error('保存头像失败:', e)
                    uni.showToast({ title: '保存失败，请重试', icon: 'none' })
                  }
                } else {
                  uni.showToast({ title: data.msg || '上传失败', icon: 'none' })
                }
              } catch (e) {
                uni.showToast({ title: '上传响应解析失败', icon: 'none' })
              }
            },
            fail: () => {
              uni.hideLoading()
              uni.showToast({ title: '上传失败，请重试', icon: 'none' })
            }
          })
        }
      })
    },
    
    changePassword() {
      // 跳转到修改密码页面
      uni.navigateTo({
        url: '/pages/forgot-password/forgot-password'
      })
    },
    
    bindPhone() {
      uni.showToast({ title: '绑定手机功能开发中', icon: 'none' });
    },
    
    setWorkStatus() {
      uni.showToast({ title: '工作状态设置功能开发中', icon: 'none' });
    },
    
    clearCache() {
      uni.showModal({
        title: '清除缓存',
        content: '确定清除本地缓存吗？不会影响账号数据。',
        success: (res) => {
          if (!res.confirm) return
          const token = uni.getStorageSync('token')
          const userInfo = uni.getStorageSync('userInfo')
          const userId = uni.getStorageSync('userId')
          uni.clearStorageSync()
          if (token) uni.setStorageSync('token', token)
          if (userInfo) uni.setStorageSync('userInfo', userInfo)
          if (userId) uni.setStorageSync('userId', userId)
          uni.showToast({ title: '缓存已清除', icon: 'success' })
        }
      })
    },
    setWorkTime() {
      uni.showToast({ title: '工作时间设置功能开发中', icon: 'none' });
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/*  section */
.section {
  background: #fff;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  padding: 30rpx 30rpx 10rpx;
}

/* 设置项 */
.setting-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
  transition: all 0.3s ease;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-item:hover {
  background-color: #f4f2ee;
}

.setting-label {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.setting-value {
  font-size: 26rpx;
  color: #999;
  margin-right: 20rpx;
}

.setting-arrow {
  font-size: 32rpx;
  color: #999;
}

/* 头像预览 */
.avatar-preview {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.avatar-icon {
  font-size: 32rpx;
  color: white;
}

.avatar-img {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  object-fit: cover;
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

</style>