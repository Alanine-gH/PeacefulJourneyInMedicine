<template>
  <view class="select-patient-page">
    <!-- 就诊人列表 -->
    <view class="patient-list">
      <view
          class="patient-card"
          v-for="(patient, index) in patientList"
          :key="index"
          :class="{ active: selectedPatient === patient.id }"
          @click="selectPatient(patient)"
      >
        <!-- 选中标记 -->
        <view class="check-mark" v-if="selectedPatient === patient.id">
          <text class="check-icon">✓</text>
        </view>

        <!-- 个人信息区域 -->
        <view class="patient-header">
          <image class="patient-avatar" :src="patient.avatar" mode="aspectFill"></image>
          <view class="patient-info">
            <view class="name-row">
              <text class="patient-name">{{ patient.name }}</text>
              <text class="patient-relation" v-if="patient.relation">{{ patient.relation }}</text>
              <text class="default-tag" v-if="patient.isDefault">默认</text>
            </view>
            <text class="patient-idcard">{{ patient.idcard }}</text>
          </view>
        </view>

        <!-- 分隔线 -->
        <view class="divider"></view>

        <!-- 就诊卡信息 -->
        <view class="medical-card-section">
          <view class="section-header">
            <text class="section-icon">🏥</text>
            <text class="section-title">就诊卡信息</text>
          </view>
          <view class="medical-card-list">
            <view class="medical-card-item" v-for="(card, cardIndex) in patient.medicalCards" :key="cardIndex">
              <view class="card-info">
                <text class="card-hospital">{{ card.hospital }}</text>
                <text class="card-number">卡号：{{ card.cardNo }}</text>
              </view>
              <!-- 二维码 -->
              <view class="qr-code" @click.stop="showQRCode(card)">
                <view class="qr-placeholder">
                  <text class="qr-icon">▣</text>
                </view>
                <text class="qr-text">二维码</text>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 添加就诊人按钮 -->
      <view class="add-patient-btn" @click="addPatient">
        <text class="add-icon">+</text>
        <text class="add-text">添加就诊人</text>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <button class="btn btn-cancel" @click="goBack">取消</button>
      <button class="btn btn-confirm" @click="confirmPatient">确定</button>
    </view>

    <!-- 二维码弹窗 -->
    <view class="qr-popup" v-if="showQRPopup" @click="hideQRCode">
      <view class="qr-popup-content" @click.stop>
        <view class="qr-popup-header">
          <text class="qr-popup-title">就诊卡二维码</text>
          <text class="qr-popup-close" @click="hideQRCode">✕</text>
        </view>
        <view class="qr-code-large">
          <view class="qr-placeholder-large">
            <text class="qr-icon-large">▣</text>
          </view>
        </view>
        <text class="qr-hospital">{{ currentQRCode.hospital }}</text>
        <text class="qr-card-no">卡号：{{ currentQRCode.cardNo }}</text>
        <text class="qr-tip">请在医院自助机或窗口出示此二维码</text>
      </view>
    </view>
  </view>
</template>

<script>
import {request} from '@/utils/auth';

export default {
  data() {
    return {
      selectedPatient: '',
      showQRPopup: false,
      currentQRCode: {},
      patientList: [
        {
          id: '1',
          name: '张三',
          relation: '本人',
          isDefault: true,
          avatar: '/static/avatar1.png',
          idcard: '310***********1234',
          medicalCards: [
            {
              hospital: '上海市第一人民医院',
              cardNo: '20240301001'
            },
            {
              hospital: '复旦大学附属华山医院',
              cardNo: '20240301002'
            }
          ]
        },
        {
          id: '2',
          name: '李四',
          relation: '父亲',
          isDefault: false,
          avatar: '/static/avatar2.png',
          idcard: '310***********5678',
          medicalCards: [
            {
              hospital: '上海市第一人民医院',
              cardNo: '20240302001'
            }
          ]
        }
      ]
    }
  },
  onLoad() {
    this.loadPatients();
  },
  methods: {
    async loadPatients() {
      try {
        const res = await request('/user/patient/list', {
          method: 'GET'
        });
        if (res.code === 200 && res.data && res.data.length > 0) {
          this.patientList = res.data;
        }
        // 如果API返回空数据，保留示例数据
      } catch (error) {
        console.error('加载就诊人列表失败:', error);
        // 使用示例数据，不显示错误提示
      }
    },
    goBack() {
      uni.navigateBack()
    },
    selectPatient(patient) {
      this.selectedPatient = patient.id
    },
    showQRCode(card) {
      this.currentQRCode = card
      this.showQRPopup = true
    },
    hideQRCode() {
      this.showQRPopup = false
    },
    addPatient() {
      uni.showToast({title: '添加就诊人功能开发中', icon: 'none'})
    },
    confirmPatient() {
      if (!this.selectedPatient) {
        uni.showToast({title: '请选择就诊人', icon: 'none'})
        return
      }
      const patient = this.patientList.find(p => p.id === this.selectedPatient)
      // 用 Storage 回传
      uni.setStorageSync('_sel_patient', patient.name)
      uni.setStorageSync('_sel_patientId', patient.id)
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.select-patient-page {
  min-height: 100vh;
  background-color: #f4f2ee;
  padding-bottom: 140rpx;
}

/* 就诊人列表 */
.patient-list {
  padding: 20rpx;
}

.patient-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  position: relative;
  border: 2rpx solid transparent;
}

.patient-card.active {
  border-color: #2196F3;
  background-color: #f8fbff;
}

.check-mark {
  position: absolute;
  top: 20rpx;
  right: 20rpx;
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background-color: #2196F3;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  font-size: 24rpx;
  color: #fff;
}

/* 个人信息区域 */
.patient-header {
  display: flex;
  align-items: center;
}

.patient-avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  background-color: #f0f0f0;
}

.patient-info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 10rpx;
}

.patient-name {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-right: 15rpx;
}

.patient-relation {
  font-size: 24rpx;
  color: #666;
  background-color: #f0f0f0;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  margin-right: 10rpx;
}

.default-tag {
  font-size: 22rpx;
  color: #fff;
  background-color: #2196F3;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.patient-idcard {
  font-size: 26rpx;
  color: #999;
}

/* 分隔线 */
.divider {
  height: 1rpx;
  background-color: #eee;
  margin: 25rpx 0;
}

/* 就诊卡信息 */
.medical-card-section {
  margin-top: 10rpx;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-icon {
  font-size: 28rpx;
  margin-right: 10rpx;
}

.section-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.medical-card-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.medical-card-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #f8f8f8;
  padding: 20rpx;
  border-radius: 12rpx;
}

.card-info {
  flex: 1;
}

.card-hospital {
  font-size: 28rpx;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.card-number {
  font-size: 24rpx;
  color: #999;
}

/* 二维码 */
.qr-code {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10rpx 20rpx;
  background-color: #fff;
  border-radius: 8rpx;
}

.qr-placeholder {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 5rpx;
}

.qr-icon {
  font-size: 48rpx;
  color: #333;
}

.qr-text {
  font-size: 20rpx;
  color: #666;
}

/* 添加就诊人按钮 */
.add-patient-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;
  border: 2rpx dashed #ddd;
  margin-top: 20rpx;
}

.add-icon {
  font-size: 40rpx;
  color: #2196F3;
  margin-right: 15rpx;
}

.add-text {
  font-size: 30rpx;
  color: #2196F3;
}

/* 底部按钮 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  display: flex;
  gap: 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.btn {
  flex: 1;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 45rpx;
  font-size: 32rpx;
  font-weight: 500;
  border: none;
}

.btn::after {
  border: none;
}

.btn-cancel {
  background-color: #f4f2ee;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  color: #fff;
}

/* 二维码弹窗 */
.qr-popup {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.qr-popup-content {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
  width: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qr-popup-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 30rpx;
}

.qr-popup-title {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
}

.qr-popup-close {
  font-size: 36rpx;
  color: #999;
  padding: 10rpx;
}

.qr-code-large {
  width: 300rpx;
  height: 300rpx;
  background-color: #f4f2ee;
  border-radius: 16rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
}

.qr-placeholder-large {
  width: 250rpx;
  height: 250rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  border-radius: 12rpx;
}

.qr-icon-large {
  font-size: 150rpx;
  color: #333;
}

.qr-hospital {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.qr-card-no {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 20rpx;
}

.qr-tip {
  font-size: 24rpx;
  color: #999;
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