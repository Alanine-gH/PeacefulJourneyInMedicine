<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="back-btn" @click="goBack">
          <text class="back-icon">‹</text>
        </view>
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
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 头部 */
.header {
  background-color: #4DD0E1;
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.back-icon {
  font-size: 40rpx;
  color: #fff;
  font-weight: bold;
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
  background-color: #4DD0E1;
  color: #fff;
  border-color: #4DD0E1;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>