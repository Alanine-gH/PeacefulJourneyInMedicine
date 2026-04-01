<template>
  <view class="container">
    <!-- 上传区域 -->
    <view class="upload-section">
      <view class="upload-title">上传病历图片</view>
      <view class="upload-desc">请上传清晰的病历照片，支持JPG、PNG格式</view>

      <view class="upload-area" @click="chooseImage">
        <view v-if="!imageUrl" class="upload-placeholder">
          <view class="upload-icon">+</view>
          <text class="upload-text">点击上传病历图片</text>
        </view>
        <image v-else :src="imageUrl" class="uploaded-image" mode="aspectFit"/>
      </view>
    </view>

    <!-- 病历信息 -->
    <view class="info-section">
      <view class="section-title">病历信息</view>

      <view class="form-item">
        <text class="form-label">就诊医院</text>
        <picker
            :range="hospitalNames"
            :value="hospitalIndex"
            @change="onHospitalChange"
        >
          <view class="form-picker" :class="{ placeholder: !formData.hospital }">
            {{ formData.hospital || (hospitalLoading ? '加载中...' : '请选择就诊医院') }}
            <text class="picker-arrow">▼</text>
          </view>
        </picker>
      </view>

      <view class="form-item" @click="openDepartmentPopup">
        <text class="form-label">就诊科室</text>
        <view class="form-picker" :class="{ placeholder: !formData.department }">
          {{ formData.department || (departmentLoading ? '加载中...' : '请选择就诊科室') }}
          <text class="picker-arrow">▼</text>
        </view>
      </view>

      <view class="form-item">
        <text class="form-label">就诊日期</text>
        <picker mode="date" :value="formData.date" @change="onDateChange">
          <view class="form-picker">{{ formData.date || '请选择就诊日期' }}</view>
        </picker>
      </view>

      <view class="form-item">
        <text class="form-label">诊断结果</text>
        <textarea class="form-textarea" v-model="formData.diagnosis" placeholder="请输入诊断结果"/>
      </view>

      <view class="form-item">
        <text class="form-label">备注</text>
        <textarea class="form-textarea" v-model="formData.remark" placeholder="请输入备注信息（选填）"/>
      </view>
    </view>

    <!-- 提交按钮 -->
    <view class="submit-section">
      <button class="submit-btn" @click="submitForm">提交病历</button>
    </view>

    <!-- 智能问答按钮 -->
    <view class="chat-section">
      <button class="chat-btn" @click="goToChat">智能问答</button>
    </view>

    <!-- 科室选择弹窗 -->
    <view v-if="showDepartmentPopup" class="modal-mask" @click="closeDepartmentPopup">
      <view class="modal-content department-modal" @click.stop>
        <view class="modal-title">选择就诊科室</view>
        <input
          class="department-search-input"
          v-model.trim="departmentKeyword"
          placeholder="请输入科室名称搜索"
        />
        <scroll-view class="department-list" scroll-y>
          <view
            v-for="dept in filteredDepartments"
            :key="dept.id"
            class="department-item"
            @click="selectDepartment(dept)"
          >
            {{ dept.departmentName || dept.name || dept.department_name }}
          </view>
          <view v-if="filteredDepartments.length === 0" class="department-empty">暂无匹配科室</view>
        </scroll-view>
        <button class="modal-btn modal-btn-home" @click="closeDepartmentPopup">取消</button>
      </view>
    </view>


    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import {getHospitals, getDepartments} from '@/utils/medical-api.js'

export default {
  data() {
    return {
      imageUrl: '',
      formData: {
        hospital: '',
        hospitalId: null,
        department: '',
        date: '',
        diagnosis: '',
        remark: ''
      },
      hospitalList: [],
      hospitalIndex: 0,
      hospitalLoading: false,
      showDepartmentPopup: false,
      departmentLoading: false,
      departmentKeyword: '',
      departmentList: []
    }
  },
  computed: {
    hospitalNames() {
      return this.hospitalList.map(h => h.hospitalName)
    },
    filteredDepartments() {
      const keyword = (this.departmentKeyword || '').toLowerCase()
      if (!keyword) return this.departmentList
      return this.departmentList.filter(d => {
        const name = d.departmentName || d.name || d.department_name || ''
        return name.toLowerCase().includes(keyword)
      })
    }
  },
  onLoad() {
    this.loadHospitals()
    this.loadDepartments()
  },
  methods: {
    async loadHospitals() {
      this.hospitalLoading = true
      try {
        const res = await getHospitals({page: 1, pageSize: 100})
        if (res && res.code === 200 && res.data) {
          this.hospitalList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('加载医院列表失败:', e)
      } finally {
        this.hospitalLoading = false
      }
    },
    async loadDepartments() {
      this.departmentLoading = true
      try {
        const res = await getDepartments({ page: 1, pageSize: 200 })
        if (res && res.code === 200 && res.data) {
          this.departmentList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('加载科室列表失败:', e)
      } finally {
        this.departmentLoading = false
      }
    },
    openDepartmentPopup() {
      this.showDepartmentPopup = true
    },
    closeDepartmentPopup() {
      this.showDepartmentPopup = false
      this.departmentKeyword = ''
    },
    selectDepartment(dept) {
      this.formData.department = dept.departmentName || dept.name || dept.department_name || ''
      this.closeDepartmentPopup()
    },
    onHospitalChange(e) {
      this.hospitalIndex = e.detail.value
      const h = this.hospitalList[this.hospitalIndex]
      if (h) {
        this.formData.hospital = h.hospitalName
        this.formData.hospitalId = h.id
      }
    },
    chooseImage() {
      uni.chooseImage({
        count: 1,
        sizeType: ['original', 'compressed'],
        sourceType: ['album', 'camera'],
        success: (res) => {
          this.imageUrl = res.tempFilePaths[0]
        }
      })
    },
    onDateChange(e) {
      this.formData.date = e.detail.value
    },
    // 保存病历到数据库
    async saveMedicalRecord() {
      uni.showLoading({
        title: '提交中...'
      })

      // 构建请求数据
      const requestData = {
        userId: uni.getStorageSync('userId') || '', // 用户ID
        imageUrl: this.imageUrl, // 病历图片地址
        hospital: this.formData.hospital, // 就诊医院
        department: this.formData.department, // 就诊科室
        visitDate: this.formData.date, // 就诊日期
        diagnosis: this.formData.diagnosis, // 诊断结果
        remark: this.formData.remark, // 备注
        createTime: new Date().toISOString() // 创建时间
      }

      try {
        // 真实API接口调用
        const response = await uni.request({
          url: 'http://localhost:8080/api/medical-record/save',
          method: 'POST',
          data: requestData,
          header: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + uni.getStorageSync('token') || ''
          }
        });

        uni.hideLoading();

        if (response.statusCode === 200 && (response.data.code === 200 || response.data.success)) {
          uni.showToast({
            title: '提交成功',
            icon: 'success',
            duration: 600,
            complete: () => {
              uni.switchTab({
                url: '/pages/home/home'
              });
            }
          });
        } else {
          uni.showToast({
            title: response.data.message || '提交失败',
            icon: 'none'
          });
        }
      } catch (error) {
        uni.hideLoading();
        console.error('保存病历失败:', error);
        uni.showToast({
          title: '网络连接失败，请稍后重试',
          icon: 'none'
        });
      }
    },

    submitForm() {
      if (!this.imageUrl) {
        uni.showToast({
          title: '请上传病历图片',
          icon: 'none'
        })
        return
      }

      if (!this.formData.hospital) {
        uni.showToast({
          title: '请输入就诊医院',
          icon: 'none'
        })
        return
      }

      if (!this.formData.department) {
        uni.showToast({
          title: '请输入就诊科室',
          icon: 'none'
        })
        return
      }

      if (!this.formData.date) {
        uni.showToast({
          title: '请选择就诊日期',
          icon: 'none'
        })
        return
      }

      if (!this.formData.diagnosis) {
        uni.showToast({
          title: '请输入诊断结果',
          icon: 'none'
        })
        return
      }

      // 调用保存病历接口
      this.saveMedicalRecord()
    },

    // 跳转到智能问答页面
    goToChat() {
      uni.navigateTo({
        url: '/pages/diagnosis/chat'
      })
    },
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 顶部导航 */
.header {
  background-color: #8db8b6;
  padding: 40rpx 30rpx 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
}

.placeholder {
  width: 36rpx;
}

/* 上传区域 */
.upload-section {
  background-color: #fff;
  margin: 20rpx;
  padding: 30rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.upload-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 10rpx;
}

.upload-desc {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 30rpx;
}

.upload-area {
  width: 100%;
  height: 400rpx;
  border: 2rpx dashed #ddd;
  border-radius: 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.upload-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background-color: #8db8b6;
  color: #fff;
  font-size: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20rpx;
}

.upload-text {
  font-size: 28rpx;
  color: #666;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  border-radius: 10rpx;
}

/* 病历信息 */
.info-section {
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  padding: 30rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #eee;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #333;
  margin-bottom: 15rpx;
  font-weight: 500;
}

.form-input {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

.form-picker {
  width: 100%;
  height: 80rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.form-picker.placeholder {
  color: #999;
}

.picker-arrow {
  font-size: 22rpx;
  color: #aaa;
}

.form-textarea {
  width: 100%;
  height: 160rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 8rpx;
  padding: 20rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
}

/* 提交按钮 */
.submit-section {
  margin: 0 20rpx 40rpx;
  padding: 0 20rpx;
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.submit-btn::after {
  border: none;
}

.submit-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 智能问答按钮 */
.chat-section {
  margin: 0 20rpx 40rpx;
  padding: 0 20rpx;
}

.chat-btn {
  width: 100%;
  height: 90rpx;
  background-color: #fff;
  color: #8db8b6;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: 2rpx solid #8db8b6;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 16rpx rgba(77, 208, 225, 0.2);
}

.chat-btn::after {
  border: none;
}

.chat-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
  background-color: rgba(77, 208, 225, 0.05);
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 科室弹窗 */
.department-modal {
  width: 680rpx;
  max-height: 70vh;
}

.department-search-input {
  width: 100%;
  height: 76rpx;
  border: 1rpx solid #e0e0e0;
  border-radius: 12rpx;
  padding: 0 20rpx;
  margin-bottom: 20rpx;
  box-sizing: border-box;
}

.department-list {
  max-height: 600rpx;
  border: 1rpx solid #f0f0f0;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.department-item {
  padding: 22rpx 20rpx;
  border-bottom: 1rpx solid #f5f5f5;
  font-size: 28rpx;
  color: #333;
}

.department-item:last-child {
  border-bottom: none;
}

.department-empty {
  padding: 40rpx 20rpx;
  text-align: center;
  color: #999;
  font-size: 26rpx;
}

/* 成功弹窗 */
.modal-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal-content {
  width: 600rpx;
  background-color: #fff;
  border-radius: 20rpx;
  padding: 60rpx 40rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.15);
}

.modal-icon {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.modal-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 15rpx;
}

.modal-desc {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 50rpx;
  text-align: center;
}

.modal-buttons {
  display: flex;
  gap: 20rpx;
  width: 100%;
}

.modal-btn {
  flex: 1;
  height: 90rpx;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.modal-btn::after {
  border: none;
}

.modal-btn-home {
  background-color: #fff;
  color: #8db8b6;
  border: 2rpx solid #8db8b6;
}

.modal-btn-chat {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
}

.modal-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
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