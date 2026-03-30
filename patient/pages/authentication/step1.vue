<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="step-indicator">
        <view class="step active">1</view>
        <view class="step-line active"></view>
        <view class="step">2</view>
        <view class="step-line"></view>
        <view class="step">3</view>
      </view>
    </view>

    <!-- 表单内容 -->
    <view class="form-content">
      <!-- 姓名 -->
      <view class="form-item">
        <view class="label">姓名</view>
        <input type="text" placeholder="请输入姓名" v-model="form.name" class="input"/>
      </view>

      <!-- 性别 -->
      <view class="form-item">
        <view class="label">性别</view>
        <view class="gender-selector">
          <view class="gender-option" :class="form.gender === '男' ? 'active' : ''" @click="form.gender = '男'">
            <view class="radio" :class="form.gender === '男' ? 'checked' : ''"></view>
            <text>男</text>
          </view>
          <view class="gender-option" :class="form.gender === '女' ? 'active' : ''" @click="form.gender = '女'">
            <view class="radio" :class="form.gender === '女' ? 'checked' : ''"></view>
            <text>女</text>
          </view>
        </view>
      </view>

      <!-- 出生日期 -->
      <view class="form-item">
        <view class="label">出生日期</view>
        <view class="date-selector">
          <picker mode="date" @change="onDateChange" :value="form.birthDate">
            <view class="picker-display">
              {{ form.birthDate || '请选择出生日期' }}
              <text class="arrow">▼</text>
            </view>
          </picker>
        </view>
      </view>

      <!-- 地址 -->
      <view class="form-item">
        <view class="label">地址</view>
        <input type="text" placeholder="请输入地址" v-model="form.address" class="input"/>
      </view>

      <!-- 手机号码 -->
      <view class="form-item">
        <view class="label">手机号码</view>
        <input type="tel" placeholder="请输入手机号码" v-model="form.phone" class="input"/>
      </view>

      <!-- 身份证号 / 护照号（自动判断：18位纯数字为身份证，否则为护照） -->
      <view class="form-item">
        <view class="label">{{ isForeigner ? '护照号' : '身份证号' }}</view>
        <input type="text" :placeholder="isForeigner ? '请输入护照号' : '请输入身份证号'" v-model="form.idCard"
               class="input"/>
        <view class="id-hint">{{ isForeigner ? '已识别为外国人（护照号）' : '请输入18位身份证号' }}</view>
      </view>

      <!-- 银行卡号 -->
      <view class="form-item">
        <view class="label">银行卡号</view>
        <input type="text" placeholder="请输入银行卡号" v-model="form.bankCard" class="input"/>
      </view>

      <!-- 银行卡类型 -->
      <view class="form-item">
        <view class="label">银行卡类型</view>
        <view class="bank-selector">
          <picker @change="onBankChange" :range="bankList" :value="bankIndex">
            <view class="picker-display">
              {{ bankList[bankIndex] }}
              <text class="arrow">▼</text>
            </view>
          </picker>
        </view>
      </view>

      <!-- 紧急联系人 -->
      <view class="form-item">
        <view class="label">紧急联系人</view>
        <input type="text" placeholder="请输入紧急联系人" v-model="form.emergencyContact" class="input"/>
      </view>

      <!-- 紧急联系人电话 -->
      <view class="form-item">
        <view class="label">紧急联系人电话</view>
        <input type="tel" placeholder="请输入紧急联系人电话" v-model="form.emergencyPhone" class="input"/>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-btn">
      <button class="next-btn" @click="nextStep">下一步</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      form: {
        name: '',
        idCard: '',
        gender: '',
        birthDate: '',
        address: '',
        phone: '',
        bankCard: '',
        emergencyContact: '',
        emergencyPhone: ''
      },
      bankList: ['请选择', '中国工商银行', '中国建设银行', '中国农业银行', '中国银行', '中国邮政储蓄银行', '交通银行'],
      bankIndex: 0
    }
  },
  computed: {
    // 根据 idCard 自动判断是否外国人：18位纯数字为中国居民身份证，否则为外国人护照
    isForeigner() {
      return !/^\d{17}[\dXx]$/.test(this.form.idCard || '')
    }
  },
  methods: {
    onBankChange(e) {
      this.bankIndex = e.detail.value
    },
    onDateChange(e) {
      this.form.birthDate = e.detail.value
    },
    nextStep() {
      // 表单验证
      if (!this.form.name) {
        uni.showToast({title: '请输入姓名', icon: 'none'})
        return
      }
      if (!this.form.idCard) {
        uni.showToast({title: '请输入身份证号或护照号', icon: 'none'})
        return
      }
      if (!this.form.gender) {
        uni.showToast({title: '请选择性别', icon: 'none'})
        return
      }
      if (!this.form.birthDate) {
        uni.showToast({title: '请选择出生日期', icon: 'none'})
        return
      }
      if (!this.form.address) {
        uni.showToast({title: '请输入地址', icon: 'none'})
        return
      }
      if (!this.form.phone) {
        uni.showToast({title: '请输入手机号码', icon: 'none'})
        return
      }
      if (!this.form.bankCard) {
        uni.showToast({title: '请输入银行卡号', icon: 'none'})
        return
      }
      if (this.bankIndex === 0) {
        uni.showToast({title: '请选择银行卡类型', icon: 'none'})
        return
      }
      if (!this.form.emergencyContact) {
        uni.showToast({title: '请输入紧急联系人', icon: 'none'})
        return
      }
      if (!this.form.emergencyPhone) {
        uni.showToast({title: '请输入紧急联系人电话', icon: 'none'})
        return
      }

      // 保存表单数据（含自动计算的 isForeigner）
      uni.setStorageSync('authForm', {
        ...this.form,
        isForeigner: this.isForeigner
      })

      // 跳转到第二步
      uni.navigateTo({
        url: '/pages/authentication/step2'
      })
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
  background-color: #fff;
  padding: 40rpx 30rpx 20rpx;
  border-bottom: 1rpx solid #e8e8e8;
}

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
  background-color: #8db8b6;
  color: #fff;
}

.step-line {
  width: 80rpx;
  height: 2rpx;
  background-color: #e8e8e8;
}

.step-line.active {
  background-color: #8db8b6;
}

/* 表单内容 */
.form-content {
  background-color: #fff;
  margin: 20rpx 0;
  padding: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.input {
  width: 100%;
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.id-hint {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
}

/* 性别选择 */
.gender-selector {
  display: flex;
  gap: 40rpx;
}

.gender-option {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.radio {
  width: 30rpx;
  height: 30rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.radio.checked {
  background-color: #8db8b6;
  border-color: #8db8b6;
}

.radio.checked::after {
  content: '';
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  background-color: #fff;
}

/* 银行卡选择 */
.bank-selector {
  position: relative;
}

.picker-display {
  width: 100%;
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  padding: 0 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.arrow {
  font-size: 24rpx;
  color: #999;
}

/* 底部按钮 */
.bottom-btn {
  padding: 30rpx;
  background-color: #fff;
  border-top: 1rpx solid #e8e8e8;
  margin-top: 20rpx;
}

.next-btn {
  width: 100%;
  height: 90rpx;
  background-color: #8db8b6;
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
}

.next-btn::after {
  border: none;
}

.next-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
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