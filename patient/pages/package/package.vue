<template>
  <view class="publish-page">
    <!-- 步骤指示器 -->
    <view class="step-indicator">
      <view class="step-item active">
        <view class="step-num">1</view>
        <text class="step-text">填写订单</text>
      </view>
      <view class="step-line"></view>
      <view class="step-item">
        <view class="step-num">2</view>
        <text class="step-text">支付订单</text>
      </view>
      <view class="step-line"></view>
      <view class="step-item">
        <view class="step-num">3</view>
        <text class="step-text">审核结果</text>
      </view>
      <view class="step-line"></view>
      <view class="step-item">
        <view class="step-num">4</view>
        <text class="step-text">服务完成</text>
      </view>
    </view>

    <!-- 表单内容 -->
    <view class="form-content">
      <!-- 服务类型 -->
      <view class="form-item" @click="selectServiceType">
        <view class="form-label">
          <text class="label-icon blue">✓</text>
          <text class="label-text">服务类型</text>
        </view>
        <view class="form-value">
          <text class="value-text">{{serviceType || '请选择服务类型'}}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊医院 -->
      <view class="form-item" @click="selectHospital">
        <text class="label-text">就诊医院</text>
        <view class="form-value">
          <text class="value-text">{{hospital || '请选择医院'}}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊时间 -->
      <view class="form-item" @click="selectTime">
        <text class="label-text">就诊时间</text>
        <view class="form-value">
          <text class="value-text">{{visitTime || '请选择时间'}}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊人 -->
      <view class="form-item" @click="selectPatient">
        <text class="label-text">就诊人</text>
        <view class="form-value">
          <text class="value-text">{{patient || '请选择就诊人'}}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊医生 -->
      <view class="form-item" @click="selectDoctor">
        <text class="label-text">就诊医生</text>
        <view class="form-value">
          <text class="value-text">{{doctor || '请选择医生'}}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 联系电话 -->
      <view class="form-item">
        <text class="label-text">联系电话</text>
        <input 
          class="form-input" 
          type="number" 
          placeholder="请输入11位手机号" 
          v-model="phone"
          maxlength="11"
          @input="onPhoneInput"
        />
      </view>

      <!-- 服务需求 -->
      <view class="form-section">
        <text class="section-title">服务需求</text>
        <textarea class="form-textarea" placeholder="请输入信息内容" v-model="demand" maxlength="2000" />
        <text class="word-count">{{demand.length}}/2000字</text>
      </view>

      <!-- 图片上传 -->
      <view class="upload-section">
        <view class="upload-item" @click="chooseImage">
          <text class="upload-icon">📷</text>
          <text class="upload-text">添加图片</text>
        </view>
        <view class="upload-item" @click="chooseVideo">
          <text class="upload-icon">📹</text>
          <text class="upload-text">1/9</text>
        </view>
      </view>

      <!-- 协议 -->
      <view class="agreement">
        <checkbox class="agreement-checkbox" :checked="agreed" @click="toggleAgreement" />
        <text class="agreement-text">我已阅读并同意《用户协议》和《服务协议》</text>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <button class="submit-btn" @click="submitDemand" :disabled="submitting">{{submitting ? '提交中...' : '发布需求'}}</button>
    </view>
  </view>
</template>

<script>
import { createPatientOrder } from '@/utils/patient-api.js'
export default {
  data() {
    return {
      serviceType: '',
      servicePackageId: null,
      servicePrice: null,
      hospital: '',
      hospitalId: null,
      visitTime: '',
      patient: '',
      patientId: null,
      doctor: '',
      doctorId: null,
      phone: '',
      demand: '',
      agreed: false,
      submitting: false
    }
  },
  onShow() {
    // 服务类型（service-type 页面写入）
    const selServiceType = uni.getStorageSync('_sel_serviceType')
    if (selServiceType) {
      this.serviceType = selServiceType
      this.servicePackageId = uni.getStorageSync('_sel_servicePackageId') || null
      this.servicePrice = uni.getStorageSync('_sel_servicePrice') || null
      uni.removeStorageSync('_sel_serviceType')
      uni.removeStorageSync('_sel_servicePackageId')
      uni.removeStorageSync('_sel_servicePrice')
    }
    // 就诊医院（select-hospital 页面写入）
    const selHospital = uni.getStorageSync('_sel_hospital')
    if (selHospital) {
      this.hospital = selHospital
      this.hospitalId = uni.getStorageSync('_sel_hospitalId') || null
      uni.removeStorageSync('_sel_hospital')
      uni.removeStorageSync('_sel_hospitalId')
    }
    // 换医院时清空已选医生
    const clearDoctor = uni.getStorageSync('_sel_clearDoctor')
    if (clearDoctor) {
      this.doctor = ''
      this.doctorId = null
      uni.removeStorageSync('_sel_clearDoctor')
    }
    // 就诊时间（select-time 页面写入）
    const selVisitTime = uni.getStorageSync('_sel_visitTime')
    if (selVisitTime) {
      this.visitTime = selVisitTime
      uni.removeStorageSync('_sel_visitTime')
    }
    // 就诊人（select-patient 页面写入）
    const selPatient = uni.getStorageSync('_sel_patient')
    if (selPatient) {
      this.patient = selPatient
      this.patientId = uni.getStorageSync('_sel_patientId') || null
      uni.removeStorageSync('_sel_patient')
      uni.removeStorageSync('_sel_patientId')
    }
    // 就诊医生（doctor-list 页面写入 selectedDoctor / selectedDoctorId）
    const selectedDoctor = uni.getStorageSync('selectedDoctor')
    if (selectedDoctor) {
      this.doctor = selectedDoctor
      this.doctorId = uni.getStorageSync('selectedDoctorId') || null
      uni.removeStorageSync('selectedDoctor')
      uni.removeStorageSync('selectedDoctorId')
    }
  },
  methods: {
    selectServiceType() {
      uni.navigateTo({
        url: '/pages/service-type/service-type'
      })
    },
    selectHospital() {
      uni.navigateTo({
        url: '/pages/select-hospital/select-hospital'
      })
    },
    selectTime() {
      uni.navigateTo({
        url: '/pages/select-time/select-time'
      })
    },
    selectPatient() {
      uni.navigateTo({
        url: '/pages/select-patient/select-patient'
      })
    },
    selectDoctor() {
      if (!this.hospital) {
        uni.showToast({ title: '请先选择就诊医院', icon: 'none' })
        return
      }
      uni.navigateTo({
        url: `/pages/select-department/select-department?hospital=${encodeURIComponent(this.hospital)}&hospitalId=${this.hospitalId || ''}`
      })
    },
    toggleAgreement() {
      this.agreed = !this.agreed
    },
    onPhoneInput(e) {
      // 限制只能输入数字，且最多11位
      let value = e.detail.value
      // 移除非数字字符
      value = value.replace(/\D/g, '')
      // 限制最多11位
      if (value.length > 11) {
        value = value.slice(0, 11)
      }
      this.phone = value
    },
    chooseImage() {
      uni.chooseImage({
        count: 9,
        sizeType: ['original', 'compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          uni.showToast({
            title: '选择了' + res.tempFilePaths.length + '张图片',
            icon: 'none'
          })
        }
      })
    },
    chooseVideo() {
      uni.chooseVideo({
        sourceType: ['album', 'camera'],
        maxDuration: 60,
        camera: 'back',
        success: (res) => {
          uni.showToast({
            title: '选择了视频',
            icon: 'none'
          })
        }
      })
    },
    async submitDemand() {
      if (!this.serviceType) { uni.showToast({ title: '请选择服务类型', icon: 'none' }); return }
      if (!this.hospital) { uni.showToast({ title: '请选择就诊医院', icon: 'none' }); return }
      if (!this.visitTime) { uni.showToast({ title: '请选择就诊时间', icon: 'none' }); return }
      if (!this.patient) { uni.showToast({ title: '请选择就诊人', icon: 'none' }); return }
      if (!this.doctor) { uni.showToast({ title: '请选择就诊医生', icon: 'none' }); return }
      if (!this.phone) { uni.showToast({ title: '请输入联系电话', icon: 'none' }); return }
      if (this.phone.length !== 11) { uni.showToast({ title: '请输入11位手机号码', icon: 'none' }); return }
      if (!this.demand) { uni.showToast({ title: '请输入服务需求', icon: 'none' }); return }
      if (!this.agreed) { uni.showToast({ title: '请同意用户协议和服务协议', icon: 'none' }); return }

      this.submitting = true
      uni.showLoading({ title: '提交中...' })
      try {
        const userId = uni.getStorageSync('userId')
        const orderData = {
          userId,
          serviceType: this.serviceType,
          servicePackageId: this.servicePackageId,
          hospitalId: this.hospitalId,
          hospitalName: this.hospital,
          patientId: this.patientId,
          patientName: this.patient,
          patientPhone: this.phone,
          expertId: this.doctorId,
          expertName: this.doctor,
          appointmentTime: this.visitTime,
          description: this.demand,
          totalAmount: this.servicePrice
        }
        const res = await createPatientOrder(orderData)
        uni.hideLoading()
        if (res && (res.code === 200 || res.code === 1)) {
          const orderNo = res.data && res.data.orderNo ? res.data.orderNo : ''
          uni.setStorageSync('currentOrderNo', orderNo)
          uni.setStorageSync('serviceType', this.serviceType)
          uni.setStorageSync('hospital', this.hospital)
          uni.setStorageSync('visitTime', this.visitTime)
          uni.setStorageSync('patient', this.patient)
          uni.setStorageSync('doctor', this.doctor)
          uni.navigateTo({ url: '/pages/payment/payment' })
        } else {
          uni.showToast({ title: (res && res.msg) || '提交失败，请重试', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('提交订单失败:', e)
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style lang="scss">
.publish-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 步骤指示器 */
.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.step-num {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background-color: rgba(255,255,255,0.3);
  color: #fff;
  font-size: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10rpx;
}

.step-item.active .step-num {
  background-color: #fff;
  color: #4DD0E1;
}

.step-text {
  font-size: 22rpx;
  color: rgba(255,255,255,0.8);
}

.step-item.active .step-text {
  color: #fff;
}

.step-line {
  width: 60rpx;
  height: 2rpx;
  background-color: rgba(255,255,255,0.3);
  margin: 0 10rpx;
}

/* 表单内容 */
.form-content {
  padding: 20rpx;
}

.form-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  margin-bottom: 2rpx;
}

.form-label {
  display: flex;
  align-items: center;
}

.label-icon {
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15rpx;
  font-size: 20rpx;
  color: #fff;
}

.label-icon.blue {
  background-color: #4DD0E1;
}

.label-text {
  font-size: 30rpx;
  color: #333;
}

.form-value {
  display: flex;
  align-items: center;
}

.value-text {
  font-size: 28rpx;
  color: #999;
  margin-right: 15rpx;
}

.arrow {
  font-size: 32rpx;
  color: #999;
}

.form-input {
  font-size: 28rpx;
  color: #333;
  text-align: right;
  width: 300rpx;
}

/* 表单区块 */
.form-section {
  background-color: #fff;
  padding: 30rpx;
  margin-top: 20rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
  display: block;
}

.form-textarea {
  width: 100%;
  height: 200rpx;
  font-size: 28rpx;
  color: #333;
  border: 1rpx solid #eee;
  border-radius: 12rpx;
  padding: 20rpx;
  box-sizing: border-box;
}

.word-count {
  font-size: 24rpx;
  color: #999;
  text-align: right;
  display: block;
  margin-top: 10rpx;
}

/* 图片上传 */
.upload-section {
  display: flex;
  padding: 30rpx;
  background-color: #fff;
  margin-top: 20rpx;
}

.upload-item {
  width: 120rpx;
  height: 120rpx;
  border: 2rpx dashed #ddd;
  border-radius: 12rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.upload-icon {
  font-size: 40rpx;
  margin-bottom: 10rpx;
}

.upload-text {
  font-size: 24rpx;
  color: #999;
}

/* 协议 */
.agreement {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  margin-top: 20rpx;
}

.agreement-checkbox {
  transform: scale(0.8);
  margin-right: 15rpx;
}

.agreement-text {
  font-size: 26rpx;
  color: #666;
}

/* 底部按钮 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.05);
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.submit-btn::after {
  border: none;
}
</style>