<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-icon">←</text>-->
<!--      </view>-->
      <view class="title">实名认证</view>
      <view class="step-indicator">
        <view class="step active">1</view>
        <view class="step-line active"></view>
        <view class="step active">2</view>
        <view class="step-line"></view>
        <view class="step">3</view>
      </view>
    </view>

    <!-- 内容 -->
    <view class="content">
      <view class="tips">
        <text>完成认证需要提交你的身份证，请保持本人身份证</text>
      </view>

      <!-- 身份证正面 -->
      <view class="upload-section">
        <view class="section-title">上传身份证正面</view>
        <view class="upload-area" @click="uploadFront">
          <view v-if="!frontImage" class="upload-placeholder">
            <view class="id-card-front">
              <view class="card-content">
                <view class="card-header">
                  <text class="card-title">居民身份证</text>
                  <text class="card-subtitle">RESIDENT ID CARD</text>
                </view>
                <view class="card-body">
                  <view class="photo-area"></view>
                  <view class="info-area">
                    <view class="info-item">姓名</view>
                    <view class="info-item">性别</view>
                    <view class="info-item">民族</view>
                    <view class="info-item">出生日期</view>
                    <view class="info-item">住址</view>
                    <view class="info-item">公民身份号码</view>
                  </view>
                </view>
                <view class="card-footer">
                  <text>签发机关</text>
                  <text>有效期限</text>
                </view>
              </view>
            </view>
            <view class="upload-text">点击上传身份证正面</view>
          </view>
          <view v-else class="uploaded-image">
            <image :src="frontImage" mode="aspectFill"></image>
            <view class="reupload-btn" @click.stop="uploadFront">重新上传</view>
          </view>
        </view>
      </view>

      <!-- 身份证反面 -->
      <view class="upload-section">
        <view class="section-title">上传身份证反面</view>
        <view class="upload-area" @click="uploadBack">
          <view v-if="!backImage" class="upload-placeholder">
            <view class="id-card-back">
              <view class="card-content">
                <view class="card-header">
                  <text class="card-title">居民身份证</text>
                </view>
                <view class="card-body">
                  <view class="chip-area"></view>
                  <view class="info-area">
                    <view class="info-item">中华人民共和国</view>
                    <view class="info-item">CHINA</view>
                    <view class="info-item">签发机关</view>
                    <view class="info-item">有效期限</view>
                  </view>
                </view>
              </view>
            </view>
            <view class="upload-text">点击上传身份证反面</view>
          </view>
          <view v-else class="uploaded-image">
            <image :src="backImage" mode="aspectFill"></image>
            <view class="reupload-btn" @click.stop="uploadBack">重新上传</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-btn">
      <button class="submit-btn" @click="submitStep">上传验证</button>
    </view>
  </view>
</template>

<script>
import { uploadFile } from '@/utils/system-api';

export default {
  data() {
    return {
      frontImage: '',
      backImage: ''
    }
  },
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
    async uploadFront() {
      try {
        const res = await uni.chooseImage({
          count: 1,
          sizeType: ['compressed'],
          sourceType: ['camera', 'album']
        });
        
        uni.showLoading({ title: '上传中...' });
        
        const uploadRes = await uploadFile(res.tempFilePaths[0], 'id_card', 'user', '');
        
        uni.hideLoading();
        
        if (uploadRes.code === 200) {
          this.frontImage = uploadRes.data.url;
          uni.showToast({ title: '上传成功', icon: 'success' });
        }
      } catch (error) {
        uni.hideLoading();
        console.error('上传身份证正面失败:', error);
        uni.showToast({ title: '上传失败', icon: 'none' });
      }
    },
    async uploadBack() {
      try {
        const res = await uni.chooseImage({
          count: 1,
          sizeType: ['compressed'],
          sourceType: ['camera', 'album']
        });
        
        uni.showLoading({ title: '上传中...' });
        
        const uploadRes = await uploadFile(res.tempFilePaths[0], 'id_card', 'user', '');
        
        uni.hideLoading();
        
        if (uploadRes.code === 200) {
          this.backImage = uploadRes.data.url;
          uni.showToast({ title: '上传成功', icon: 'success' });
        }
      } catch (error) {
        uni.hideLoading();
        console.error('上传身份证反面失败:', error);
        uni.showToast({ title: '上传失败', icon: 'none' });
      }
    },
    submitStep() {
      if (!this.frontImage) {
        uni.showToast({ title: '请上传身份证正面', icon: 'none' })
        return
      }
      if (!this.backImage) {
        uni.showToast({ title: '请上传身份证反面', icon: 'none' })
        return
      }

      uni.setStorageSync('authImages', {
        front: this.frontImage,
        back: this.backImage
      })

      uni.navigateTo({
        url: '/pages/authentication/step3'
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
  background-color: #fff;
  padding: 40rpx 30rpx 20rpx;
  border-bottom: 1rpx solid #e8e8e8;
}
/*
.back-btn {
  position: absolute;
  top: 40rpx;
  left: 30rpx;
  z-index: 1;
}

.back-icon {
  font-size: 36rpx;
  color: #333;
}
*/
.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  text-align: center;
  margin-bottom: 30rpx;
}

.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20rpx;
}

.step {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background-color: #e8e8e8;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
  font-weight: 500;
}

.step.active {
  background-color: #4DD0E1;
  color: #fff;
}

.step-line {
  width: 80rpx;
  height: 2rpx;
  background-color: #e8e8e8;
}

.step-line.active {
  background-color: #4DD0E1;
}

/* 内容 */
.content {
  padding: 30rpx;
}

.tips {
  background-color: #e3f2fd;
  padding: 20rpx;
  border-radius: 10rpx;
  margin-bottom: 30rpx;
}

.tips text {
  font-size: 26rpx;
  color: #1976D2;
  line-height: 1.5;
}

/* 上传区域 */
.upload-section {
  margin-bottom: 40rpx;
}

.section-title {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 20rpx;
}

.upload-area {
  background-color: #fff;
  border: 2rpx dashed #e8e8e8;
  border-radius: 15rpx;
  padding: 30rpx;
  min-height: 300rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-area:active {
  background-color: #f5f5f5;
}

/* 身份证模板 */
.id-card-front,
.id-card-back {
  width: 400rpx;
  height: 250rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
}

.card-content {
  width: 100%;
  height: 100%;
  background-color: #f8f9fa;
  padding: 20rpx;
  box-sizing: border-box;
}

.card-header {
  margin-bottom: 15rpx;
}

.card-title {
  font-size: 24rpx;
  font-weight: 600;
  color: #333;
  display: block;
}

.card-subtitle {
  font-size: 16rpx;
  color: #999;
  display: block;
  margin-top: 5rpx;
}

.card-body {
  display: flex;
  gap: 20rpx;
  margin-bottom: 15rpx;
}

.photo-area {
  width: 80rpx;
  height: 100rpx;
  background-color: #e8e8e8;
  border: 1rpx solid #d9d9d9;
}

.info-area {
  flex: 1;
}

.info-item {
  font-size: 16rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  font-size: 14rpx;
  color: #999;
}

/* 身份证反面 */
.id-card-back .card-body {
  flex-direction: column;
  gap: 10rpx;
}

.chip-area {
  width: 100%;
  height: 80rpx;
  background-color: #e8e8e8;
  border: 1rpx solid #d9d9d9;
  margin-bottom: 10rpx;
}

/* 上传提示 */
.upload-text {
  font-size: 26rpx;
  color: #999;
  text-align: center;
}

/* 已上传图片 */
.uploaded-image {
  position: relative;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.uploaded-image image {
  width: 400rpx;
  height: 250rpx;
  border-radius: 10rpx;
  object-fit: cover;
}

.reupload-btn {
  position: absolute;
  top: 10rpx;
  right: calc(50% - 190rpx);
  background-color: rgba(0, 0, 0, 0.6);
  color: #fff;
  padding: 10rpx 20rpx;
  border-radius: 15rpx;
  font-size: 22rpx;
  z-index: 10;
}

/* 底部按钮 */
.bottom-btn {
  padding: 30rpx;
  background-color: #fff;
  border-top: 1rpx solid #e8e8e8;
  margin-top: 20rpx;
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  background-color: #4DD0E1;
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.submit-btn::after {
  border: none;
}

.submit-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}
</style>