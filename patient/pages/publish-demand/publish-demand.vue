<template>
  <view class="publish-page">
    <!-- 导航栏 -->
   <!-- <view class="nav-bar">
      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>
      <text class="nav-title">发布需求</text>
      <view class="placeholder"></view>
    </view> -->

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
          <text class="value-text">{{ serviceType || '请选择服务类型' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊医院 -->
      <view class="form-item" @click="selectHospital">
        <text class="label-text">就诊医院</text>
        <view class="form-value">
          <text class="value-text">{{ hospital || '请选择医院' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊时间 -->
      <view class="form-item" @click="selectTime">
        <text class="label-text">就诊时间</text>
        <view class="form-value">
          <text class="value-text">{{ visitTime || '请选择时间' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊人 -->
      <view class="form-item" @click="selectPatient">
        <text class="label-text">就诊人</text>
        <view class="form-value">
          <text class="value-text">{{ patient || '请选择就诊人' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <!-- 就诊医生 -->
      <view class="form-item" @click="selectDoctor">
        <text class="label-text">就诊医生</text>
        <view class="form-value">
          <text class="value-text">{{ doctor || '请选择医生' }}</text>
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
        <textarea class="form-textarea" placeholder="请输入信息内容" v-model="demand" maxlength="2000"/>
        <text class="word-count">{{ demand.length }}/2000字</text>
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
        <checkbox class="agreement-checkbox" :checked="agreed" @click="toggleAgreement"/>
        <text class="agreement-text">我已阅读并同意《用户协议》和《服务协议》</text>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <button class="submit-btn" @click="submitDemand">发布需求</button>
    </view>
  </view>
</template>

<script>
import { createPatientOrder } from '@/utils/patient-api.js';

export default {
  data() {
    return {
      serviceType: '',
      serviceTypeId: null,
      hospital: '',
      hospitalId: null,
      visitTime: '',
      patient: '',
      patientId: null,
      doctor: '',
      doctorId: null,
      department: '',
      phone: '',
      demand: '',
      agreed: false
    }
  },
  onShow() {
    // 从 Storage 读取各子页面回传的选择结果
    const st = uni.getStorageSync('selectedServiceType')
    if (st) { this.serviceType = st; this.serviceTypeId = uni.getStorageSync('selectedServiceTypeId') || null }
    const h = uni.getStorageSync('_sel_hospital')
    if (h) {
      this.hospital = h
      this.hospitalId = uni.getStorageSync('_sel_hospitalId') || null
      if (uni.getStorageSync('_sel_clearDoctor') === '1') {
        this.doctor = ''; this.doctorId = null; this.department = ''
        uni.removeStorageSync('_sel_clearDoctor')
        uni.removeStorageSync('selectedDoctor')
        uni.removeStorageSync('selectedDoctorId')
        uni.removeStorageSync('selectedDepartment')
      }
    }
    const t = uni.getStorageSync('_sel_visitTime')
    if (t) this.visitTime = t
    const p = uni.getStorageSync('selectedPatientName')
    if (p) { this.patient = p; this.patientId = uni.getStorageSync('selectedPatientId') || null }
    const doc = uni.getStorageSync('selectedDoctor')
    if (doc) { this.doctor = doc; this.doctorId = uni.getStorageSync('selectedDoctorId') || null }
    const dept = uni.getStorageSync('selectedDepartment')
    if (dept) this.department = dept
    // 预填手机号
    if (!this.phone) {
      const userInfo = uni.getStorageSync('userInfo')
      if (userInfo && userInfo.phone) this.phone = userInfo.phone
    }
  },
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
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
        uni.showToast({title: '请先选择就诊医院', icon: 'none'})
        return
      }
      uni.navigateTo({
        url: `/pages/select-department/select-department?hospital=${this.hospital}`
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
      // 验证所有必填项
      if (!this.serviceType) {
        uni.showToast({title: '请选择服务类型', icon: 'none'})
        return
      }
      if (!this.hospital) {
        uni.showToast({title: '请选择就诊医院', icon: 'none'})
        return
      }
      if (!this.visitTime) {
        uni.showToast({title: '请选择就诊时间', icon: 'none'})
        return
      }
      if (!this.patient) {
        uni.showToast({title: '请选择就诊人', icon: 'none'})
        return
      }
      if (!this.doctor) {
        uni.showToast({title: '请选择就诊医生', icon: 'none'})
        return
      }
      if (!this.phone) {
        uni.showToast({title: '请输入联系电话', icon: 'none'})
        return
      }
      if (this.phone.length !== 11) {
        uni.showToast({title: '请输入11位手机号码', icon: 'none'})
        return
      }
      if (!this.demand) {
        uni.showToast({title: '请输入服务需求', icon: 'none'})
        return
      }
      if (!this.agreed) {
        uni.showToast({title: '请同意用户协议和服务协议', icon: 'none'})
        return
      }

      uni.showLoading({ title: '发布中...' })
      try {
        const userId = uni.getStorageSync('userId')
        const orderData = {
          userId: userId,
          orderType: this.serviceTypeId || 1,
          appointmentHospital: this.hospital,
          appointmentDate: this.visitTime,
          patientName: this.patient,
          contactPhone: this.phone,
          requirementDesc: this.demand,
          doctorName: this.doctor,
          departmentName: this.department
        }
        const res = await createPatientOrder(orderData)
        uni.hideLoading()
        if (res && (res.code === 200 || res.code === 1)) {
          const orderNo = res.data && (res.data.orderNo || res.data.order_no || res.data)
          ;['selectedServiceType','selectedServiceTypeId','_sel_hospital','_sel_hospitalId',
            '_sel_visitTime','selectedPatientName','selectedPatientId',
            'selectedDoctor','selectedDoctorId','selectedDepartment'].forEach(k => uni.removeStorageSync(k))
          uni.navigateTo({ url: '/pages/payment/payment?orderNo=' + (orderNo || '') })
        } else {
          uni.showToast({ title: (res && res.msg) || '发布失败，请重试', icon: 'none' })
        }
      } catch (e) {
        uni.hideLoading()
        console.error('发布需求失败:', e)
        uni.showToast({ title: '网络错误，请重试', icon: 'none' })
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

/* 导航栏 */
.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0 30rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}
/*
.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-arrow {
  font-size: 40rpx;
  color: #fff;
}
*/
.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

/* 步骤指示器 */
.step-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
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
  background-color: rgba(255, 255, 255, 0.3);
  color: #fff;
  font-size: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10rpx;
}

.step-item.active .step-num {
  background-color: #fff;
  color: #2196F3;
}

.step-text {
  font-size: 22rpx;
  color: rgba(255, 255, 255, 0.8);
}

.step-item.active .step-text {
  color: #fff;
}

.step-line {
  width: 60rpx;
  height: 2rpx;
  background-color: rgba(255, 255, 255, 0.3);
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
  background-color: #2196F3;
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
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
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
