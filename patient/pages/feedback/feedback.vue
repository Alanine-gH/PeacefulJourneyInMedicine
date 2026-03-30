<template>
  <!-- 意见反馈页面 -->
  <view class="container">
    <!-- 反馈表单 -->
    <view class="form-section">
      <view class="form-item">
        <view class="form-label">反馈类型</view>
        <view class="form-select">
          <view v-for="(type, index) in feedbackTypes"
                :key="index" class="select-item" :class="{ active: selectedType === type.value }"
                @click="selectedType = type.value">
            <text class="select-text">{{ type.label }}</text>
          </view>
        </view>
      </view>

      <view class="form-item">
        <view class="form-label">反馈内容</view>
        <textarea v-model="feedbackContent" class="form-textarea" placeholder="请详细描述您的问题或建议..."
                  maxlength="500"></textarea>
        <view class="textarea-counter">{{ feedbackContent.length }}/500</view>
      </view>

      <view class="form-item">
        <view class="form-label">联系方式</view>
        <input
            v-model="contactInfo"
            class="form-input"
            placeholder="请留下您的手机号或邮箱，方便我们回复"
        />
      </view>

      <view class="form-item">
        <view class="form-label">上传图片（可选）</view>
        <view class="upload-section">
          <view v-for="(image, index) in uploadedImages" :key="index" class="uploaded-image">
            <image :src="image" class="image-preview" mode="aspectFill"/>
            <view class="image-remove" @click="removeImage(index)">
              <text class="remove-icon">×</text>
            </view>
          </view>
          <view v-if="uploadedImages.length < 3" class="upload-btn" @click="chooseImage">
            <text class="upload-icon">+</text>
            <text class="upload-text">添加图片</text>
          </view>
        </view>
      </view>

      <!-- 提交按钮 -->
      <button class="submit-btn" :disabled="!canSubmit" @click="handleSubmitFeedback">
        提交反馈
      </button>
    </view>
  </view>
</template>

<script>
import {uploadFile, submitFeedback} from '../../utils/system-api';

export default {
  data() {
    return {
      feedbackTypes: [
        {label: '功能建议', value: 'suggestion'},
        {label: 'bug反馈', value: 'bug'},
        {label: '服务投诉', value: 'complaint'},
        {label: '其他', value: 'other'}
      ],
      selectedType: 'suggestion',
      feedbackContent: '',
      contactInfo: '',
      uploadedImages: []
    }
  },
  computed: {
    canSubmit() {
      return this.feedbackContent.trim().length > 0;
    }
  },
  methods: {


    chooseImage() {
      uni.chooseImage({
        count: 3 - this.uploadedImages.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.uploadedImages = [...this.uploadedImages, ...res.tempFilePaths];
        }
      });
    },

    removeImage(index) {
      this.uploadedImages.splice(index, 1);
    },

    async handleSubmitFeedback() {
      if (!this.canSubmit) {
        uni.showToast({
          title: '请输入反馈内容',
          icon: 'none'
        });
        return;
      }

      uni.showLoading({
        title: '提交中...'
      });

      try {
        // 上传图片
        const uploadedUrls = [];
        for (const imagePath of this.uploadedImages) {
          const result = await uploadFile(imagePath, 'feedback', 'feedback', Date.now());
          if (result.code === 200) {
            uploadedUrls.push(result.data.file_url);
          }
        }

        // 提交反馈
        const feedbackData = {
          type: this.selectedType,
          content: this.feedbackContent,
          contact: this.contactInfo,
          images: uploadedUrls
        };

        // 调用后端API提交反馈
        const response = await submitFeedback(feedbackData);

        uni.hideLoading();
        if (response.code === 200) {
          uni.showToast({
            title: '反馈提交成功',
            icon: 'success',
            success: () => {
              setTimeout(() => {
                uni.navigateBack();
              }, 1500);
            }
          });
        } else {
          uni.showToast({
            title: response.msg || '提交失败，请稍后重试',
            icon: 'none'
          });
        }
      } catch (error) {
        uni.hideLoading();
        console.error('提交反馈失败:', error);
        uni.showToast({
          title: '提交失败，请稍后重试',
          icon: 'none'
        });
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 头部 */
.header {
  background-color: #8db8b6;
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.header-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.header-right {
  width: 60rpx;
}

/* 表单区域 */
.form-section {
  padding: 30rpx;
}

.form-item {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.form-label {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
}

/* 类型选择 */
.form-select {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.select-item {
  padding: 15rpx 30rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 30rpx;
  font-size: 24rpx;
  color: #666;
  transition: all 0.3s ease;
}

.select-item.active {
  border-color: #8db8b6;
  color: #8db8b6;
  background-color: rgba(77, 208, 225, 0.1);
}

/* 文本域 */
.form-textarea {
  width: 100%;
  min-height: 200rpx;
  padding: 20rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  font-size: 28rpx;
  color: #333;
  resize: none;
  box-sizing: border-box;
}

.textarea-counter {
  text-align: right;
  font-size: 24rpx;
  color: #999;
  margin-top: 10rpx;
}

/* 输入框 */
.form-input {
  width: 100%;
  padding: 20rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

/* 上传区域 */
.upload-section {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.uploaded-image {
  width: 150rpx;
  height: 150rpx;
  position: relative;
  border-radius: 10rpx;
  overflow: hidden;
}

.image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-remove {
  position: absolute;
  top: 5rpx;
  right: 5rpx;
  width: 40rpx;
  height: 40rpx;
  background-color: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.remove-icon {
  color: #fff;
  font-size: 28rpx;
  font-weight: bold;
}

.upload-btn {
  width: 150rpx;
  height: 150rpx;
  border: 2rpx dashed #e8e8e8;
  border-radius: 10rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  transition: all 0.3s ease;
}

.upload-btn:active {
  background-color: rgba(77, 208, 225, 0.1);
  border-color: #8db8b6;
}

.upload-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.upload-text {
  font-size: 22rpx;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  margin-top: 40rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.submit-btn::after {
  border: none;
}

.submit-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

.submit-btn:disabled {
  background: #ccc;
  box-shadow: none;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .form-item {
    padding: 20rpx;
  }

  .select-item {
    padding: 12rpx 24rpx;
    font-size: 22rpx;
  }

  .form-textarea {
    min-height: 180rpx;
    font-size: 26rpx;
  }

  .uploaded-image,
  .upload-btn {
    width: 130rpx;
    height: 130rpx;
  }
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