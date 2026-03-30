<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="header-title">编辑患者信息</view>
        <view class="save-btn" @click="savePatient">保存</view>
      </view>
    </view>

    <!-- 表单 -->
    <view class="form">
      <view class="form-item">
        <view class="form-label">姓名</view>
        <input 
          class="form-input" 
          v-model="formData.name" 
          placeholder="请输入姓名"
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
        <view class="form-label">手机号</view>
        <input 
          class="form-input" 
          v-model="formData.phone" 
          placeholder="请输入手机号"
          type="number"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">地址</view>
        <input 
          class="form-input" 
          v-model="formData.address" 
          placeholder="请输入地址"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">病史</view>
        <textarea 
          class="form-textarea" 
          v-model="formData.medicalHistory" 
          placeholder="请输入病史"
          rows="3"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">联系人</view>
        <input 
          class="form-input" 
          v-model="formData.contactPerson" 
          placeholder="请输入联系人"
        />
      </view>
      
      <view class="form-item">
        <view class="form-label">联系电话</view>
        <input 
          class="form-input" 
          v-model="formData.contactPhone" 
          placeholder="请输入联系电话"
          type="number"
        />
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      formData: {
        id: 0,
        name: '',
        phone: '',
        age: '',
        gender: 0,
        address: '',
        medicalHistory: '',
        contactPerson: '',
        contactPhone: ''
      }
    }
  },
  onLoad(options) {
    const patientId = options.id
    this.getPatientInfo(patientId)
  },
  methods: {
    getPatientInfo(patientId) {
      // 模拟获取患者信息
      const patients = [
        {
          id: 1,
          name: '张三',
          phone: '13800138000',
          age: 65,
          gender: 1,
          address: '北京市朝阳区',
          medicalHistory: '高血压、糖尿病',
          contactPerson: '李四',
          contactPhone: '13900139000'
        },
        {
          id: 2,
          name: '王五',
          phone: '13700137000',
          age: 72,
          gender: 0,
          address: '上海市浦东新区',
          medicalHistory: '冠心病',
          contactPerson: '赵六',
          contactPhone: '13600136000'
        },
        {
          id: 3,
          name: '钱七',
          phone: '13500135000',
          age: 58,
          gender: 1,
          address: '广州市天河区',
          medicalHistory: '脑梗塞',
          contactPerson: '孙八',
          contactPhone: '13400134000'
        }
      ]
      
      const patient = patients.find(p => p.id === parseInt(patientId))
      if (patient) {
        this.formData = patient
      }
    },
    
    savePatient() {
      // 表单验证
      if (!this.formData.name) {
        uni.showToast({ title: '请输入姓名', icon: 'none' });
        return;
      }
      
      if (!this.formData.phone) {
        uni.showToast({ title: '请输入手机号', icon: 'none' });
        return;
      }
      
      // 保存数据
      uni.showToast({ title: '保存成功', icon: 'success' });
      setTimeout(() => {
        uni.navigateBack();
      }, 1000);
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
  background-color: #8db8b6;
  color: #fff;
  border-color: #8db8b6;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
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