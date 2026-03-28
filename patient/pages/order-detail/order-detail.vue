<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="logo">
          <text class="logo-text">订单详情</text>
        </view>
        <view class="header-actions">
          <view class="customer-service" @click="openCustomerService">
            <text class="service-icon">💬</text>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 订单信息 -->
    <view class="order-info">
      <view class="info-section">
        <text class="section-title">订单信息</text>
        <view class="info-item">
          <text class="info-label">订单编号：</text>
          <text class="info-value">{{ orderDetail.orderNo || order_no }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">订单类型：</text>
          <text class="info-value">{{ getOrderTypeText(orderDetail.orderType) }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">订单状态：</text>
          <text class="info-value">{{ getOrderStatusText(orderDetail.orderStatus) }}</text>
        </view>
      </view>
      
      <view class="info-section">
        <text class="section-title">就诊信息</text>
        <view class="info-item">
          <text class="info-label">就诊医院：</text>
          <text class="info-value">{{ orderDetail.appointmentHospital || '-' }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">就诊科室：</text>
          <text class="info-value">{{ orderDetail.appointmentDepartment || '-' }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">预约日期：</text>
          <text class="info-value">{{ orderDetail.appointmentDate || '-' }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">服务时间：</text>
          <text class="info-value">{{ formatServiceTime(orderDetail.serviceStartTime, orderDetail.serviceEndTime) }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">就诊人员：</text>
          <text class="info-value">{{ orderDetail.patientName || '-' }} {{ getGenderText(orderDetail.patientGender) }} {{ orderDetail.patientAge ? orderDetail.patientAge + '周岁' : '' }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">联系电话：</text>
          <text class="info-value">{{ orderDetail.userPhone || '-' }}</text>
        </view>
      </view>
      
      <view class="info-section">
        <text class="section-title">服务需求</text>
        <view class="service-demand">
          <text>{{ orderDetail.diseaseDescription || '暂无服务需求描述' }}</text>
          <text v-for="(item, index) in defaultDemands" :key="index">{{ index + 1 }}. {{ item }}</text>
        </view>
      </view>
      
      <view class="info-section">
        <text class="section-title">费用信息</text>
        <view class="profit-item">
          <text class="profit-label">订单总额：</text>
          <text class="profit-value">{{ orderDetail.currency || 'CNY' }} {{ orderDetail.totalAmount || '0' }}</text>
        </view>
        <view class="profit-item">
          <text class="profit-label">定金金额：</text>
          <text class="profit-value">{{ orderDetail.currency || 'CNY' }} {{ orderDetail.depositAmount || '0' }}</text>
        </view>
        <view class="profit-item">
          <text class="profit-label">尾款金额：</text>
          <text class="profit-value">{{ orderDetail.currency || 'CNY' }} {{ orderDetail.balanceAmount || '0' }}</text>
        </view>
        <view class="profit-item" v-if="orderDetail.actualPaidAmount">
          <text class="profit-label">实付金额：</text>
          <text class="profit-value">{{ orderDetail.currency || 'CNY' }} {{ orderDetail.actualPaidAmount }}</text>
        </view>
        <view class="profit-tip">
          <text>注：完成服务后，收益将自动转入您的账户</text>
        </view>
      </view>
    </view>

    <!-- 评价按钮（已完成且未评价） -->
    <view class="bottom-action" v-if="orderDetail.orderStatus === 5 && !orderDetail.hasEvaluated">
      <button class="evaluate-btn" @click="goToEvaluate">立即评价</button>
    </view>
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { getPatientOrderDetail } from '@/utils/patient-api';

export default {
  data() {
    return {
      order_no: '',
      orderDetail: {
        id: 0,
        order_no: '',
        user_id: 0,
        user_name: '',
        user_phone: '',
        accompanist_id: 0,
        accompanist_name: '',
        accompanist_phone: '',
        order_type: 0,
        order_status: 0,
        service_start_time: '',
        service_end_time: '',
        service_days: 0,
        service_hours: 0,
        patient_name: '',
        patient_gender: 0,
        patient_age: 0,
        patient_passport: '',
        disease_description: '',
        appointment_hospital: '',
        appointment_department: '',
        appointment_date: '',
        total_amount: 0,
        deposit_amount: 0,
        balance_amount: 0,
        actual_paid_amount: 0,
        currency: 'USD'
      },
      defaultDemands: [
        '提前30分钟到达医院',
        '协助办理挂号、取号等手续',
        '陪同就诊，记录医生建议',
        '协助取药，讲解用药注意事项',
        '全程照顾，确保就诊顺利'
      ]
    }
  },
  onLoad(options) {
    this.order_no = options.order_no || options.orderNo || 'ORD20260301001'
    this.getOrderDetail()
  },
  onShow() {
    this.getOrderDetail()
  },
  methods: {
    getOrderTypeText(type) {
      const typeMap = {
        1: '全程陪诊',
        2: '代办手续',
        3: '接机服务',
        4: '送机服务'
      }
      return typeMap[type] || '未知'
    },
    
    getOrderStatusText(status) {
      const statusMap = {
        1: '待付款',
        2: '已付款',
        3: '已确认',
        4: '服务中',
        5: '已完成',
        6: '已取消',
        7: '退款中',
        8: '已退款'
      }
      return statusMap[status] || '未知'
    },
    
    getGenderText(gender) {
      const genderMap = {
        1: '男',
        2: '女',
        3: '未知'
      }
      return genderMap[gender] || '未知'
    },
    
    formatServiceTime(startTime, endTime) {
      if (!startTime && !endTime) return '02-02（今天） 15:50'
      if (!endTime) return startTime
      return `${startTime} - ${endTime}`
    },
    
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
    goToEvaluate() {
      const d = this.orderDetail;
      uni.navigateTo({
        url: `/pages/evaluation/evaluation?orderNo=${this.order_no}&accompanistName=${encodeURIComponent(d.accompanistName || '')}&accompanistId=${d.accompanistId || ''}`
      });
    },
    async checkEvaluated() {
      try {
        const { request } = await import('@/utils/config.js');
        const res = await request('/order/evaluation/list', { method: 'GET', data: { orderNo: this.order_no, pageNum: 1, pageSize: 1 } });
        if (res && res.data) this.$set(this.orderDetail, 'hasEvaluated', (res.data.total || 0) > 0);
      } catch (e) {}
    },
    async getOrderDetail() {
      try {
        const res = await getPatientOrderDetail(this.order_no);
        if (res && res.code === 200 && res.data) {
          this.orderDetail = res.data;
          uni.setStorageSync('orderDetail_' + this.order_no, res.data);
          await this.checkEvaluated();
        }
      } catch (error) {
        console.error('获取订单详情失败:', error);
        uni.showToast({ title: '获取订单详情失败', icon: 'none' });
        const cachedOrder = uni.getStorageSync('orderDetail_' + this.order_no);
        if (cachedOrder) { this.orderDetail = cachedOrder; }
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
  display: flex;
  flex-direction: column;
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

.logo {
  display: flex;
  align-items: center;
}

.logo-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.customer-service {
  width: 60rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.customer-service:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: scale(1.05);
}

.service-icon {
  font-size: 32rpx;
  color: #fff;
}

.order-info {
  flex: 1;
  padding: 20rpx 30rpx;
  margin-bottom: 100rpx;
}

.info-section {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border: 1rpx solid #e8e8e8;
}

.info-section:hover {
  transform: translateY(-4rpx);
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.2);
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 24rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.info-item {
  display: flex;
  margin-bottom: 20rpx;
  align-items: flex-start;
}

.info-item:last-child {
  margin-bottom: 0;
}

.info-label {
  font-size: 26rpx;
  color: #666;
  width: 160rpx;
  flex-shrink: 0;
  font-weight: 500;
}

.info-value {
  font-size: 26rpx;
  color: #333;
  flex: 1;
  line-height: 1.5;
}

.service-demand {
  font-size: 26rpx;
  color: #333;
  line-height: 1.6;
  white-space: pre-line;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 12rpx;
  border-left: 8rpx solid #4DD0E1;
}

.service-demand text {
  display: block;
  margin-bottom: 12rpx;
}

.service-demand text:last-child {
  margin-bottom: 0;
}

.profit-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding: 20rpx;
  background-color: #f9f9f9;
  border-radius: 12rpx;
}

.profit-label {
  font-size: 26rpx;
  color: #666;
  font-weight: 500;
}

.profit-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff6b6b;
}

.profit-tip {
  font-size: 24rpx;
  color: #999;
  text-align: right;
  font-style: italic;
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}
</style>