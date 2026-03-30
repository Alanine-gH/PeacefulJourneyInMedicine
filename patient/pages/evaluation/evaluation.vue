<template>
  <view class="container">
    <!-- 医生信息 -->
    <view class="doctor-info">
      <view class="avatar">
        <text class="avatar-icon">👨‍⚕️</text>
      </view>
      <text class="doctor-name">{{ doctorInfo.name || '医生' }}</text>
      <text class="doctor-stats">服务{{ doctorInfo.serviceCount || 0 }}次 综合评分{{ doctorInfo.rating || 0 }}</text>
    </view>

    <!-- 服务评分 -->
    <view class="card rating-section">
      <text class="rating-label">服务评分</text>
      <view class="stars">
        <text v-for="(star, index) in 5"
              :key="index" class="star" :class="{ active: selectedRating > index }"
              @click="selectRating(index + 1)">★
        </text>
      </view>
      <text class="rating-text" v-if="selectedRating > 0">{{ getRatingText(selectedRating) }}</text>
    </view>

    <!-- 评价内容 -->
    <view class="card comment-section">
      <textarea
          class="comment-input"
          placeholder="说说您的感受~\n\n例如：医生态度很好，诊疗过程很专业..."
          v-model="comment"
          maxlength="200"
      ></textarea>
      <text class="comment-count" :class="{ warning: comment.length > 180 }">{{ comment.length }}/200字</text>
    </view>

    <!-- 匿名评价选项 -->
    <view class="card anonymous-section">
      <view class="anonymous-option">
        <text class="option-label">匿名评价</text>
        <view class="switch" :class="{ active: anonymous }" @click="toggleAnonymous">
          <view class="switch-handle"></view>
        </view>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="bottom-buttons">
      <button class="submit-btn" @click="submitEvaluation">提交评价</button>
    </view>
  </view>
</template>

<script>
import {evaluateOrder} from '@/utils/patient-api';

export default {
  data() {
    return {
      orderNo: '',
      selectedRating: 0,
      comment: '',
      anonymous: false,
      doctorInfo: {
        name: '王医生',
        serviceCount: 108,
        rating: 4.85
      }
    }
  },
  onLoad(options) {
    this.orderNo = options.orderNo || '';
    if (options.doctorName) {
      this.doctorInfo.name = options.doctorName;
    }
    if (options.serviceCount) {
      this.doctorInfo.serviceCount = parseInt(options.serviceCount);
    }
    if (options.rating) {
      this.doctorInfo.rating = parseFloat(options.rating);
    }
  },
  methods: {
    selectRating(rating) {
      this.selectedRating = rating
    },
    getRatingText(rating) {
      const texts = ['', '非常差', '差', '一般', '好', '非常好']
      return texts[rating]
    },
    toggleAnonymous() {
      this.anonymous = !this.anonymous
    },
    async submitEvaluation() {
      if (this.selectedRating === 0) {
        uni.showToast({
          title: '请选择服务评分',
          icon: 'none'
        })
        return
      }

      if (!this.orderNo) {
        uni.showToast({
          title: '订单信息缺失',
          icon: 'none'
        })
        return
      }

      try {
        const res = await evaluateOrder(this.orderNo, {
          rating: this.selectedRating,
          content: this.comment,
          anonymous: this.anonymous
        });

        if (res.code === 200) {
          uni.showToast({
            title: '评价成功',
            icon: 'success'
          });
          setTimeout(() => {
            uni.navigateTo({
              url: '/pages/value-added/value-added'
            });
          }, 1500);
        }
      } catch (error) {
        console.error('提交评价失败:', error);
        uni.showToast({
          title: '评价失败，请稍后重试',
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
  background-color: #f8f9fa;
  padding-bottom: 100rpx;
}

/* 医生信息 */
.doctor-info {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  padding: 60rpx 30rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.2);
}

.avatar {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
  border: 4rpx solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  animation: avatarPulse 2s ease-in-out infinite;
}

.avatar-icon {
  font-size: 80rpx;
  color: white;
}

@keyframes avatarPulse {
  0% {
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  }
  50% {
    box-shadow: 0 6rpx 20rpx rgba(77, 208, 225, 0.4);
  }
  100% {
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
  }
}

.doctor-name {
  font-size: 36rpx;
  font-weight: 600;
  color: white;
  margin-bottom: 15rpx;
  text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
}

.doctor-stats {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.9);
  background-color: rgba(255, 255, 255, 0.2);
  padding: 8rpx 24rpx;
  border-radius: 20rpx;
}

.card {
  background-color: white;
  padding: 30rpx;
  margin: 30rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  transform: translateY(-2rpx);
}

/* 评分部分 */
.rating-label {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 30rpx;
  display: block;
}

.stars {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.star {
  font-size: 50rpx;
  color: #e0e0e0;
  margin-right: 25rpx;
  cursor: pointer;
  transition: all 0.3s ease;
  text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.1);
}

.star:hover {
  transform: scale(1.1);
}

.star.active {
  color: #ffc107;
  animation: starPulse 0.5s ease;
}

@keyframes starPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}

.rating-text {
  font-size: 28rpx;
  color: #4DD0E1;
  font-weight: 600;
  margin-top: 15rpx;
  display: block;
}

/* 评价内容 */
.comment-input {
  width: 100%;
  min-height: 200rpx;
  border: 2rpx solid #f0f0f0;
  border-radius: 15rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  resize: none;
  transition: all 0.3s ease;
  line-height: 1.5;
  font-family: inherit;
}

.comment-input:focus {
  border-color: #4DD0E1;
  box-shadow: 0 0 0 3rpx rgba(77, 208, 225, 0.1);
  outline: none;
}

.comment-count {
  position: absolute;
  bottom: 25rpx;
  right: 40rpx;
  font-size: 24rpx;
  color: #999;
  transition: all 0.3s ease;
}

.comment-count.warning {
  color: #ff9800;
  font-weight: 600;
}

/* 匿名选项 */
.anonymous-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.option-label {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.switch {
  width: 80rpx;
  height: 40rpx;
  background-color: #e0e0e0;
  border-radius: 20rpx;
  position: relative;
  transition: all 0.3s ease;
  cursor: pointer;
}

.switch.active {
  background-color: #4DD0E1;
}

.switch-handle {
  width: 36rpx;
  height: 36rpx;
  background-color: white;
  border-radius: 50%;
  position: absolute;
  top: 2rpx;
  left: 2rpx;
  transition: all 0.3s ease;
  box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.2);
}

.switch.active .switch-handle {
  left: 42rpx;
}

/* 底部按钮 */
.bottom-buttons {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background-color: white;
  padding: 30rpx;
  box-shadow: 0 -2rpx 15rpx rgba(0, 0, 0, 0.08);
}

.submit-btn {
  flex: 1;
  height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: white;
  border-radius: 45rpx;
  font-size: 32rpx;
  font-weight: 600;
  border: none;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.submit-btn:active {
  transform: translateY(2rpx);
  box-shadow: 0 2rpx 6rpx rgba(77, 208, 225, 0.3);
}

.submit-btn::after {
  border: none;
}

button {
  border: none;
  outline: none;
}
</style>
