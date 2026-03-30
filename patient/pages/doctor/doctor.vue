<template>
  <view class="container">
    <!-- 订单状态 -->
    <view class="status-card" :class="orderStatusClass">
      <view class="status-icon">
        <text>{{ statusIcon }}</text>
      </view>
      <text class="status-text">{{ orderStatusText }}</text>
      <text class="status-subtext">{{ statusSubtext }}</text>
    </view>
    
    <!-- 订单信息 -->
    <view class="card order-info">
      <view class="section-title">订单信息</view>
      <view class="info-item">
        <text class="info-label">订单编号</text>
        <text class="info-value">{{ orderDetail.orderId || '---' }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">服务类型</text>
        <text class="info-value">{{ orderDetail.serviceType || '---' }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">订单金额</text>
        <text class="info-value fee">¥{{ orderDetail.amount || '0.00' }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">下单时间</text>
        <text class="info-value">{{ orderDetail.createTime || '---' }}</text>
      </view>
    </view>
    
    <!-- 服务信息 -->
    <view class="card service-info" v-if="orderDetail.serviceInfo">
      <view class="section-title">服务信息</view>
      <view class="doctor-header">
        <view class="doctor-avatar">
          <text class="avatar-icon">{{ orderDetail.serviceInfo.doctorAvatar || '👨‍⚕️' }}</text>
        </view>
        <view class="doctor-details">
          <text class="doctor-name">{{ orderDetail.serviceInfo.doctorName || '---' }}</text>
          <text class="doctor-title">{{ orderDetail.serviceInfo.doctorTitle || '---' }}</text>
          <text class="doctor-hospital">{{ orderDetail.serviceInfo.hospital || '---' }} | {{ orderDetail.serviceInfo.department || '---' }}</text>
        </view>
      </view>
      
      <view class="info-item">
        <text class="info-label">服务时间</text>
        <text class="info-value">{{ orderDetail.serviceInfo.serviceTime || '---' }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">服务地点</text>
        <text class="info-value">{{ orderDetail.serviceInfo.serviceLocation || '---' }}</text>
      </view>
    </view>
    
    <!-- 患者信息 -->
    <view class="card patient-info">
      <view class="section-title">患者信息</view>
      <view class="info-item">
        <text class="info-label">姓名</text>
        <text class="info-value">{{ orderDetail.patientInfo?.name || '---' }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">性别</text>
        <text class="info-value">{{ orderDetail.patientInfo?.gender || '---' }}</text>
      </view>
      <view class="info-item">
        <text class="info-label">联系电话</text>
        <text class="info-value">{{ orderDetail.patientInfo?.phone || '---' }}</text>
      </view>
    </view>
    
    <!-- 温馨提示 -->
    <view class="card tips-info">
      <view class="section-title">温馨提示</view>
      <text class="tips-content">1. 请提前30分钟到达医院，携带身份证和医保卡</text>
      <text class="tips-content">2. 如需取消预约，请提前24小时操作</text>
      <text class="tips-content">3. 如有疑问，请拨打医院服务热线：010-12345678</text>
    </view>
    
    <!-- 底部按钮 -->
    <view class="bottom-buttons">
      <button class="back-home-btn" @click="goHome">返回首页</button>
    </view>
  </view>
</template>

<script>
import { getPatientOrderDetail } from '@/utils/patient-api';

export default {
  data() {
    return {
      orderId: '',
      orderDetail: {
        orderId: '',
        serviceType: '',
        amount: '0.00',
        createTime: '',
        status: 'pending',
        serviceInfo: {
          doctorName: '',
          doctorTitle: '',
          hospital: '',
          department: '',
          serviceTime: '',
          serviceLocation: ''
        },
        patientInfo: {
          name: '',
          gender: '',
          phone: ''
        }
      }
    }
  },
  computed: {
    orderStatusClass() {
      const statusMap = {
        pending: 'status-pending',
        completed: 'status-completed',
        cancelled: 'status-cancelled',
        unpaid: 'status-unpaid'
      }
      return statusMap[this.orderDetail.status] || 'status-pending'
    },
    orderStatusText() {
      const statusMap = {
        pending: '待服务',
        completed: '已完成',
        cancelled: '已取消',
        unpaid: '待支付'
      }
      return statusMap[this.orderDetail.status] || '处理中'
    },
    statusIcon() {
      const iconMap = {
        pending: '⏱️',
        completed: '✅',
        cancelled: '❌',
        unpaid: '💳'
      }
      return iconMap[this.orderDetail.status] || '⏱️'
    },
    statusSubtext() {
      const subtextMap = {
        pending: '您的服务已确认，请按时前往',
        completed: '服务已完成，感谢您的使用',
        cancelled: '订单已取消',
        unpaid: '请尽快完成支付'
      }
      return subtextMap[this.orderDetail.status] || '正在处理中'
    }
  },
  onLoad(options) {
    if (options.orderId) {
      this.orderId = options.orderId
      this.getOrderDetail()
    }
  },
  methods: {
    goHome() {
      uni.switchTab({
        url: '/pages/home/home'
      })
    },
    async getOrderDetail() {
      try {
        const res = await getPatientOrderDetail(this.orderId);
        if (res.code === 200 && res.data) {
          const order = res.data;
          this.orderDetail = {
            orderId: order.order_no,
            serviceType: order.order_type === 1 ? '全程陪诊' : '代办手续',
            amount: order.total_amount || '0.00',
            createTime: order.created_at || '',
            status: this.mapOrderStatus(order.order_status),
            serviceInfo: {
              doctorName: order.accompanist_name || '',
              doctorTitle: order.professional_title || '',
              hospital: order.appointment_hospital || '',
              department: order.appointment_department || '',
              serviceTime: order.service_start_time || '',
              serviceLocation: order.appointment_hospital || ''
            },
            patientInfo: {
              name: order.patient_name || '',
              gender: order.patient_gender === 1 ? '男' : '女',
              phone: order.user_phone || ''
            }
          };
        }
      } catch (error) {
        console.error('获取订单详情失败:', error);
        uni.showToast({ title: '获取订单详情失败', icon: 'none' });
      }
    },
    mapOrderStatus(status) {
      const statusMap = {
        1: 'unpaid',
        2: 'pending',
        3: 'pending',
        4: 'pending',
        5: 'completed',
        6: 'cancelled'
      };
      return statusMap[status] || 'pending';
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

/* 订单状态卡片 */
.status-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: white;
  padding: 40rpx 30rpx;
  margin: 30rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.status-icon {
  font-size: 80rpx;
  margin-bottom: 20rpx;
  opacity: 0.8;
}

.status-text {
  font-size: 36rpx;
  font-weight: 600;
  margin-bottom: 10rpx;
  color: #333;
}

.status-subtext {
  font-size: 28rpx;
  color: #666;
  text-align: center;
  line-height: 1.4;
}

/* 不同状态的样式 */
.status-pending .status-icon {
  color: #ff9800;
}

.status-completed .status-icon {
  color: #4caf50;
}

.status-cancelled .status-icon {
  color: #f44336;
}

.status-unpaid .status-icon {
  color: #ff5722;
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

.doctor-header {
  display: flex;
  align-items: center;
  margin-bottom: 30rpx;
  padding-bottom: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.doctor-avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 30rpx;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.avatar-icon {
  font-size: 60rpx;
  color: white;
}

.doctor-name {
  font-size: 36rpx;
  font-weight: 600;
  margin-bottom: 10rpx;
  display: block;
  color: #333;
}

.doctor-title {
  font-size: 28rpx;
  color: #4DD0E1;
  margin-bottom: 10rpx;
  display: block;
}

.doctor-hospital {
  font-size: 26rpx;
  color: #666;
  display: block;
  line-height: 1.4;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 25rpx;
  padding-bottom: 15rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f9f9f9;
}

.info-item:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.info-label {
  font-size: 28rpx;
  color: #666;
  flex: 1;
  font-weight: 500;
}

.info-value {
  font-size: 28rpx;
  color: #333;
  flex: 2;
  text-align: right;
  word-break: break-word;
}

.fee {
  color: #f50057;
  font-weight: 600;
  font-size: 32rpx;
}

.tips-info {
  margin-bottom: 100rpx;
}

.tips-content {
  font-size: 26rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 15rpx;
  display: block;
  padding-left: 20rpx;
  position: relative;
}

.tips-content::before {
  content: '';
  position: absolute;
  left: 0;
  top: 12rpx;
  width: 4rpx;
  height: 4rpx;
  border-radius: 50%;
  background-color: #4DD0E1;
}

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

.back-home-btn {
  flex: 1;
  height: 90rpx;
  background-color: #4DD0E1;
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

.back-home-btn:active {
  transform: translateY(2rpx);
  box-shadow: 0 2rpx 6rpx rgba(77, 208, 225, 0.3);
}

.back-home-btn::after {
  border: none;
}

button {
  border: none;
  outline: none;
}
</style>
