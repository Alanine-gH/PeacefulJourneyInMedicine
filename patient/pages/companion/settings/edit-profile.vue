<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="back-btn" @click="goBack">
          <text class="back-icon">‹</text>
        </view>
        <view class="header-title">编辑个人资料</view>
        <view class="save-btn" @click="saveProfile">保存</view>
      </view>
    </view>

    <!-- 表单 -->
    <view class="form">
      <view class="form-item">
        <view class="form-label">真实姓名</view>
        <input 
          class="form-input" 
          v-model="formData.real_name" 
          placeholder="请输入真实姓名"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">手机号</view>
        <input 
          class="form-input" 
          v-model="formData.phone" 
          placeholder="请输入手机号"
          type="number"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">性别</view>
        <view class="gender-select">
          <view 
            class="gender-option" 
            :class="{ active: formData.gender === 0 }"
            @click="formData.gender = 0"
          >
            女
          </view>
          <view 
            class="gender-option" 
            :class="{ active: formData.gender === 1 }"
            @click="formData.gender = 1"
          >
            男
          </view>
        </view>
      </view>
      
      <view class="form-item">
        <view class="form-label">年龄</view>
        <input 
          class="form-input" 
          v-model="formData.age" 
          placeholder="请输入年龄"
          type="number"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">职称</view>
        <input 
          class="form-input" 
          v-model="formData.professional_title" 
          placeholder="请输入职称"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">语言能力</view>
        <input 
          class="form-input" 
          v-model="formData.language_ability" 
          placeholder="请输入语言能力"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">专长</view>
        <textarea 
          class="form-textarea" 
          v-model="formData.specialties" 
          placeholder="请输入专长"
          rows="3"
        />
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getCompanionProfile, updateCompanionProfile } from '@/utils/companion-api';

export default {
  data() {
    return {
      formData: {
        real_name: '',
        phone: '',
        gender: 0,
        age: '',
        professional_title: '',
        language_ability: '',
        specialties: ''
      },
      companionId: ''
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
          this.companionId = res.data.id;
          this.formData = {
            real_name: res.data.real_name || '',
            phone: res.data.phone || '',
            gender: res.data.gender || 0,
            age: res.data.age || '',
            professional_title: res.data.professional_title || '',
            language_ability: res.data.language_ability || '',
            specialties: res.data.specialties || ''
          };
        }
      } catch (error) {
        console.error('获取陪护员信息失败:', error);
        uni.showToast({ title: '获取信息失败', icon: 'none' });
      }
    },
    
    async saveProfile() {
      // 表单验证
      if (!this.formData.real_name) {
        uni.showToast({ title: '请输入真实姓名', icon: 'none' });
        return;
      }
      
      if (!this.formData.phone) {
        uni.showToast({ title: '请输入手机号', icon: 'none' });
        return;
      }
      
      // 保存数据
      try {
        const res = await updateCompanionProfile(this.companionId, this.formData);
        if (res.code === 200) {
          uni.showToast({ title: '保存成功', icon: 'success' });
          setTimeout(() => {
            uni.navigateBack();
          }, 1000);
        } else {
          uni.showToast({ title: '保存失败', icon: 'none' });
        }
      } catch (error) {
        console.error('更新陪护员信息失败:', error);
        uni.showToast({ title: '保存失败', icon: 'none' });
      }
    },
    
    goBack() {
      uni.navigateBack()
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

.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.back-icon {
  font-size: 40rpx;
  color: #fff;
  font-weight: bold;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.save-btn {
  font-size: 28rpx;
  color: #fff;
  font-weight: 500;
  padding: 10rpx 20rpx;
  border-radius: 20rpx;
  background-color: rgba(255, 255, 255, 0.2);
  cursor: pointer;
}

/* 表单 */
.form {
  background: #fff;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  padding: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.form-input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 26rpx;
  color: #333;
  background-color: #f9f9f9;
}

.form-textarea {
  width: 100%;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  padding: 20rpx;
  font-size: 26rpx;
  color: #333;
  background-color: #f9f9f9;
  min-height: 120rpx;
  resize: none;
}

/* 性别选择 */
.gender-select {
  display: flex;
  gap: 20rpx;
}

.gender-option {
  flex: 1;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26rpx;
  color: #333;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: all 0.3s ease;
}

.gender-option.active {
  background-color: #4DD0E1;
  color: #fff;
  border-color: #4DD0E1;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>