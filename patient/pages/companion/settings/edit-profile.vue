<template>
  <view class="container">
    <!-- 表单 -->
    <view class="form">
      <view class="form-item">
        <view class="form-label">真实姓名</view>
        <input
            class="form-input"
            v-model="formData.realName"
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
            v-model="formData.professionalTitle"
            placeholder="请输入职称"
        />
      </view>

      <view class="form-item">
        <view class="form-label">语言能力</view>
        <input
            class="form-input"
            v-model="formData.languageAbility"
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

    <!-- 保存按钮 -->
    <view class="bottom-btn-wrap">
      <button class="save-submit-btn" @click="saveProfile">保存</button>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import {getCompanionProfile, updateCompanionProfile} from '@/utils/companion-api';

export default {
  data() {
    return {
      formData: {
        realName: '',
        phone: '',
        gender: 0,
        age: '',
        professionalTitle: '',
        languageAbility: '',
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
          const d = res.data
          this.companionId = d.id;
          this.formData = {
            realName: d.realName || d.real_name || '',
            phone: d.phone || '',
            gender: d.gender || 0,
            age: d.age || '',
            professionalTitle: d.professionalTitle || d.professional_title || '',
            languageAbility: d.languageAbility || d.language_ability || '',
            specialties: d.specialties || ''
          };
        }
      } catch (error) {
        console.error('获取陪护员信息失败:', error);
        uni.showToast({title: '获取信息失败', icon: 'none'});
      }
    },

    async saveProfile() {
      if (!this.formData.realName) {
        uni.showToast({title: '请输入真实姓名', icon: 'none'});
        return;
      }
      if (!this.formData.phone) {
        uni.showToast({title: '请输入手机号', icon: 'none'});
        return;
      }
      try {
        const res = await updateCompanionProfile(this.companionId, this.formData);
        if (res.code === 200) {
          uni.showToast({title: '保存成功', icon: 'success'});
          setTimeout(() => {
            uni.navigateBack();
          }, 1000);
        } else {
          uni.showToast({title: res.msg || '保存失败', icon: 'none'});
        }
      } catch (error) {
        console.error('更新陪护员信息失败:', error);
        uni.showToast({title: '保存失败', icon: 'none'});
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
  background-color: #f4f2ee;
  min-height: 100vh;
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
  background-color: #8db8b6;
  color: #fff;
  border-color: #8db8b6;
}

/* 底部保存按钮 */
.bottom-btn-wrap {
  padding: 30rpx;
  background-color: #f4f2ee;
}

.save-submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  border-radius: 45rpx;
  border: none;
  text-align: center;
}

.save-submit-btn::after {
  border: none;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* ── Shared theme overrides ── */
page {
  background-color: #f4f2ee !important;
}

.container {
  background-color: #f4f2ee !important;
  min-height: 100vh;
}

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
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.10) !important;
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
  box-shadow: 0 4rpx 16rpx rgba(100, 175, 175, 0.28) !important;
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
.order-status {
  color: #8db8b6 !important;
}

/* Section titles */
.section-title {
  color: #3a3a4a !important;
  font-weight: 600 !important;
}

/* Stat items */
.stat-item {
  background: #f4f2ee !important;
  border-radius: 14rpx !important;
}

/* Tab bar selected */
.tab-item.active,
.tab-item.active .tab-text {
  color: #8db8b6 !important;
}

/* Profile avatar ring */
.profile-avatar {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 12rpx rgba(100, 175, 175, 0.28) !important;
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
.carousel-item {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

.carousel-item.vip {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important;
}

.carousel-item.full {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

.carousel-item.consult {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%) !important;
}

.carousel-item.result {
  background: linear-gradient(135deg, #d4a8b0 0%, #c09098 100%) !important;
}

.carousel-item.medicine {
  background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%) !important;
}

.carousel-item.basic {
  background: linear-gradient(135deg, #a8cec8 0%, #8db8b0 100%) !important;
}

/* Step indicator */
.step-num {
  border-color: #8db8b6 !important;
  color: #8db8b6 !important;
}

.step-item.active .step-num,
.step-item.completed .step-num {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important;
  border-color: transparent !important;
}

.step-line.active {
  background-color: #8db8b6 !important;
}

/* Health exam header */
.header {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
}

/* Package type tags */
.pkg-type-tag {
  background: #e8f4f4 !important;
}

.pkg-type-tag .type-text {
  color: #6a9ea0 !important;
}

/* Input focus ring */
.input-item:focus-within {
  border-color: #8db8b6 !important;
  box-shadow: 0 0 0 3rpx rgba(141, 184, 182, 0.18) !important;
}

</style>