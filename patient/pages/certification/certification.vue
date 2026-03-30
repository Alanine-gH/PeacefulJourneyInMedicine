<template>
  <view class="container">
    <!-- 表单内容 -->
    <view class="form-content">
      <!-- 真实姓名 -->
      <view class="form-item">
        <text class="form-label">真实姓名</text>
        <input class="form-input" placeholder="请填写真实姓名" v-model="formData.real_name"/>
      </view>

      <!-- 性别 -->
      <view class="form-item">
        <text class="form-label">性别</text>
        <view class="gender-select">
          <button class="gender-btn" :class="{ active: formData.gender === 1 }" @click="selectGender(1)">男</button>
          <button class="gender-btn" :class="{ active: formData.gender === 2 }" @click="selectGender(2)">女</button>
        </view>
      </view>

      <!-- 年龄 -->
      <view class="form-item">
        <text class="form-label">年龄</text>
        <input class="form-input" placeholder="请填写周岁年龄" type="number" v-model="formData.age"/>
      </view>

      <!-- 手机号码 -->
      <view class="form-item">
        <text class="form-label">手机号码</text>
        <input class="form-input" placeholder="请填写您的手机号" type="number" v-model="formData.phone"/>
      </view>

      <!-- 职称 -->
      <view class="form-item">
        <text class="form-label">职称</text>
        <input class="form-input" placeholder="如：护士、健康管理师" v-model="formData.professional_title"/>
      </view>

      <!-- 医学背景 -->
      <view class="form-item">
        <text class="form-label">医学背景</text>
        <view class="gender-select">
          <button class="gender-btn" :class="{ active: formData.medical_background === 1 }"
                  @click="selectMedicalBackground(1)">有医学背景
          </button>
          <button class="gender-btn" :class="{ active: formData.medical_background === 2 }"
                  @click="selectMedicalBackground(2)">无医学背景
          </button>
        </view>
      </view>

      <!-- 证书编号 -->
      <view class="form-item">
        <text class="form-label">证书编号</text>
        <input class="form-input" placeholder="请输入您的证书编号" v-model="formData.certificate_number"/>
      </view>

      <!-- 证书照片 -->
      <view class="form-item">
        <text class="form-label">证书照片</text>
        <view class="upload-btn" @click="uploadImage('certificate_photo')">
          <image v-if="formData.certificate_photo" :src="formData.certificate_photo" mode="aspectFill"></image>
          <image v-else src="/static/logo.png" mode="aspectFill"></image>
        </view>
      </view>

      <!-- 无犯罪记录证明 -->
      <view class="form-item">
        <text class="form-label">无犯罪记录证明</text>
        <view class="upload-btn" @click="uploadImage('criminal_record_cert')">
          <image v-if="formData.criminal_record_cert" :src="formData.criminal_record_cert" mode="aspectFill"></image>
          <image v-else src="/static/logo.png" mode="aspectFill"></image>
        </view>
      </view>

      <!-- 营业执照（可选） -->
      <view class="form-item">
        <text class="form-label">营业执照（可选）</text>
        <view class="upload-btn" @click="uploadImage('business_license')">
          <image v-if="formData.business_license" :src="formData.business_license" mode="aspectFill"></image>
          <image v-else src="/static/logo.png" mode="aspectFill"></image>
        </view>
      </view>

      <!-- 语言能力 -->
      <view class="form-item">
        <text class="form-label">语言能力</text>
        <input class="form-input" placeholder="如：中文、英文、日语" v-model="formData.language_ability"/>
      </view>

      <!-- 专业擅长 -->
      <view class="form-item">
        <text class="form-label">专业擅长</text>
        <input class="form-input" placeholder="如：骨科、神经科、妇产科" v-model="formData.specialties"/>
      </view>

      <!-- 隐私说明 -->
      <view class="privacy-note">
        <text class="privacy-text">隐私说明：以上信息在提交确认后，只有平台审核人员可见</text>
      </view>

      <!-- 协议同意 -->
      <view class="agreement-section">
        <view class="checkbox" :class="{ active: formData.agreed }" @click="toggleAgreement">
          <text v-if="formData.agreed">✓</text>
        </view>
        <text class="agreement-text">我已阅读并同意《用户协议》和《服务协议》</text>
      </view>

      <!-- 注册按钮 -->
      <view class="register-section">
        <button class="register-btn" :disabled="!formData.agreed" @click="submitForm">注册成为陪诊师</button>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import {submitCertification} from '@/utils/companion-api';
import {uploadFile} from '@/utils/system-api';
import {getFileUrl} from '@/utils/config.js';

export default {
  data() {
    return {
      formData: {
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
        agreed: false
      },
      fileNames: {
        certificate_photo: '',
        criminal_record_cert: '',
        business_license: ''
      }
    }
  },
  methods: {
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
    async uploadImage(field) {
      try {
        const res = await uni.chooseImage({
          count: 1,
          sizeType: ['original', 'compressed'],
          sourceType: ['album', 'camera']
        });

        const tempFilePath = res.tempFilePaths[0];

        uni.showLoading({title: '上传中...'});

        const uploadRes = await uploadFile(tempFilePath, 'certification', 'companion', '');

        uni.hideLoading();

        if (uploadRes.code === 200 || uploadRes.code === 1) {
          const fileName = uploadRes.data
          this.fileNames[field] = fileName
          this.formData[field] = getFileUrl(fileName)
          uni.showToast({title: '上传成功', icon: 'success'});
        } else {
          uni.showToast({title: uploadRes.msg || '上传失败', icon: 'none'});
        }
      } catch (error) {
        uni.hideLoading();
        // 用户取消选图，不提示错误
        if (error && (error.errMsg || '').includes('cancel')) return;
        console.error('上传图片失败:', error);
        uni.showToast({title: '上传失败', icon: 'none'});
      }
    },
    selectGender(gender) {
      this.formData.gender = gender
    },
    selectMedicalBackground(background) {
      this.formData.medical_background = background
    },
    toggleAgreement() {
      this.formData.agreed = !this.formData.agreed
    },
    submitForm() {
      if (!this.formData.agreed) {
        uni.showToast({
          title: '请阅读并同意协议',
          icon: 'none'
        })
        return
      }

      if (!this.formData.real_name) {
        uni.showToast({
          title: '请填写真实姓名',
          icon: 'none'
        })
        return
      }

      if (!this.formData.gender) {
        uni.showToast({
          title: '请选择性别',
          icon: 'none'
        })
        return
      }

      if (!this.formData.age) {
        uni.showToast({
          title: '请填写年龄',
          icon: 'none'
        })
        return
      }

      if (!this.formData.phone) {
        uni.showToast({
          title: '请填写手机号码',
          icon: 'none'
        })
        return
      }

      if (!this.formData.professional_title) {
        uni.showToast({
          title: '请填写职称',
          icon: 'none'
        })
        return
      }

      if (!this.formData.medical_background) {
        uni.showToast({
          title: '请选择医学背景',
          icon: 'none'
        })
        return
      }

      if (!this.formData.certificate_number) {
        uni.showToast({
          title: '请填写证书编号',
          icon: 'none'
        })
        return
      }

      if (!this.formData.certificate_photo) {
        uni.showToast({
          title: '请上传证书照片',
          icon: 'none'
        })
        return
      }

      if (!this.formData.criminal_record_cert) {
        uni.showToast({
          title: '请上传无犯罪记录证明',
          icon: 'none'
        })
        return
      }

      this.submitCertification()
    },
    async submitCertification() {
      try {
        uni.showLoading({title: '提交中...'});

        // 获取用户ID（后端通过 userId 关联 user_accompanist）
        const userId = uni.getStorageSync('userId');

        if (!userId) {
          uni.hideLoading();
          uni.showToast({
            title: '登录已过期，请重新登录',
            icon: 'none'
          });
          return;
        }
        // 将前端字段名映射到后端 DTO 字段名
        const submitData = {
          accompanistId: userId,
          realName: this.formData.real_name,
          phone: this.formData.phone,
          gender: this.formData.gender,
          age: this.formData.age,
          professionalTitle: this.formData.professional_title,
          medicalBackground: this.formData.medical_background,
          certificateNumber: this.formData.certificate_number,
          certificatePhoto: this.fileNames.certificate_photo,
          criminalRecordCert: this.fileNames.criminal_record_cert,
          businessLicense: this.fileNames.business_license,
          languageAbility: this.formData.language_ability,
          specialties: this.formData.specialties
        };

        const res = await submitCertification(submitData);

        uni.hideLoading();

        if (res.code === 200) {
          uni.showToast({
            title: '提交成功，等待审核',
            icon: 'success'
          });
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          uni.showToast({
            title: res.msg || '提交失败',
            icon: 'none'
          });
        }
      } catch (error) {
        uni.hideLoading();
        console.error('提交认证信息失败:', error);
        uni.showToast({
          title: '提交失败，请重试',
          icon: 'none'
        });
      }
    }
  }
}
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 100vh;
  background-color: #f4f2ee;
  display: flex;
  flex-direction: column;
}

.form-content {
  background-color: white;
  margin: 20rpx 0;
  padding: 40rpx 30rpx;
  flex: 1;
}

.form-item {
  margin-bottom: 40rpx;
}

.form-label {
  display: block;
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
}

.form-input {
  width: 100%;
  height: 88rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 16rpx;
  padding: 0 30rpx;
  font-size: 28rpx;
  color: #333;
  background-color: #f9f9f9;
}

.upload-btn {
  width: 160rpx;
  height: 160rpx;
  border: 1rpx dashed #e0e0e0;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  transition: all 0.3s ease;
}

.upload-btn:hover {
  border-color: #8db8b6;
  background-color: #f0f9fa;
}

.upload-btn image {
  width: 80rpx;
  height: 80rpx;
  opacity: 0.6;
}

.gender-select {
  display: flex;
  gap: 20rpx;
}

.gender-btn {
  flex: 1;
  height: 88rpx;
  border: 1rpx solid #e0e0e0;
  background-color: #f9f9f9;
  border-radius: 16rpx;
  font-size: 28rpx;
  color: #666;
  transition: all 0.3s ease;
}

.gender-btn.active {
  background-color: #8db8b6;
  color: white;
  border-color: #8db8b6;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.id-photo-upload {
  display: flex;
  gap: 20rpx;
}

.privacy-note {
  margin: 40rpx 0;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 16rpx;
  border-left: 8rpx solid #8db8b6;
}

.privacy-text {
  font-size: 24rpx;
  color: #666;
  line-height: 1.6;
}

.agreement-section {
  display: flex;
  align-items: center;
  margin: 40rpx 0;
}

.checkbox {
  width: 40rpx;
  height: 40rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  margin-right: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f9f9f9;
  transition: all 0.3s ease;
}

.checkbox.active {
  background-color: #8db8b6;
  border-color: #8db8b6;
  color: white;
  box-shadow: 0 2rpx 8rpx rgba(77, 208, 225, 0.3);
}

.agreement-text {
  flex: 1;
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.register-section {
  margin-top: 60rpx;
}

.register-btn {
  width: 100%;
  height: 96rpx;
  background-color: #e0e0e0;
  color: #999;
  border-radius: 48rpx;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
  outline: none;
  transition: all 0.3s ease;
}

.register-btn:not(:disabled) {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: white;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.register-btn:not(:disabled):active {
  transform: translateY(2rpx);
  box-shadow: 0 2rpx 6rpx rgba(77, 208, 225, 0.3);
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