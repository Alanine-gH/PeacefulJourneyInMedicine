<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">个人设置</view>
        <view class="header-right"></view>
      </view>
    </view>

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
      <view class="setting-item" @click="bindPhone">
        <view class="setting-label">绑定手机</view>
        <view class="setting-value">{{ companionInfo.phone ? '已绑定' : '未绑定' }}</view>
        <view class="setting-arrow">›</view>
      </view>
    </view>

    <!-- 工作设置 -->
    <view class="section">
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
    </view>



    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getCompanionProfile, updateCompanionProfile } from '@/utils/companion-api';
import { getAvatarUrl } from '@/utils/api';

export default {
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
      // 选择头像并上传
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          // 这里应该上传图片到服务器
          uni.showToast({ title: '头像上传功能开发中', icon: 'none' });
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
    
    setWorkTime() {
      uni.showToast({ title: '工作时间设置功能开发中', icon: 'none' });
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

.header-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.header-right {
  width: 60rpx;
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
  background-color: #f8f9fa;
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
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

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>