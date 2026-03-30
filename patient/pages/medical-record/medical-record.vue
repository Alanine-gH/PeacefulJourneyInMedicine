<template>
  <view class="container">
    <!-- 病例列表 -->
    <view class="record-list">
      <view
          v-for="record in filteredRecords"
          :key="record.id"
          class="record-item"
          @click="viewRecordDetail(record.id)"
      >
        <view class="record-header">
          <text class="record-title">{{ record.title }}</text>
          <text class="record-date">{{ record.date }}</text>
        </view>
        <view class="record-content">
          <text class="record-hospital">{{ record.hospital }}</text>
          <text class="record-doctor">{{ record.department }}</text>
        </view>
        <view class="record-footer">
          <text class="record-department">{{ record.triageNo }}</text>
          <text class="record-status" :class="record.status">{{ record.statusText }}</text>
        </view>
      </view>

      <!-- 空状态 -->
      <view v-if="filteredRecords.length === 0" class="empty-state">
        <text class="empty-icon">📋</text>
        <text class="empty-text">暂无病例记录</text>
      </view>
    </view>
  </view>
</template>

<script>
import {
  getTriageRecords
} from '../../utils/api';

export default {
  data() {
    return {
      records: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  computed: {
    filteredRecords() {
      return this.records;
    }
  },
  onShow() {
    this.fetchTriageRecords();
  },
  methods: {


    async fetchTriageRecords() {
      try {
        this.loading = true;
        // 获取当前登录用户ID
        const userId = uni.getStorageSync('userId');

        if (!userId) {
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          });
          uni.navigateTo({
            url: '/pages/login/login'
          });
          return;
        }

        const response = await getTriageRecords({
          page: this.currentPage,
          size: this.pageSize,
          userId: userId
        });

        if (response && response.code === 200 && response.data) {
          // 获取当前登录用户ID
          const userId = uni.getStorageSync('userId');
          // 过滤只显示当前用户的病例
          const userRecords = (response.data.records || []).filter(record => {
            return String(record.userId) === String(userId);
          });
          this.records = this.transformRecords(userRecords);
          this.total = userRecords.length;
        } else {
          uni.showToast({
            title: '获取病例数据失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('获取分诊记录失败:', error);
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    transformRecords(backendRecords) {
      return backendRecords.map(record => ({
        id: record.id,
        title: record.chiefComplaint || '分诊记录',
        date: this.formatDate(record.createTime),
        hospital: record.recommendedHospitals || '待推荐',
        doctor: '-',
        department: record.recommendedDepartment || '待分诊',
        status: this.mapStatus(record.triageStatus),
        statusText: this.getStatusText(record.triageStatus),
        triageNo: record.triageNo,
        userName: record.userName,
        userPhone: record.userPhone,
        gender: record.gender,
        age: record.age,
        symptomDescription: record.symptomDescription,
        painLocation: record.painLocation,
        painLevel: record.painLevel,
        painDuration: record.painDuration,
        medicalHistory: record.medicalHistory,
        allergyHistory: record.allergyHistory,
        currentMedication: record.currentMedication,
        triageResult: record.triageResult,
        aiConfidence: record.aiConfidence,
        aiAnalysisTime: record.aiAnalysisTime,
        humanReviewTime: record.humanReviewTime,
        symptomImageUrls: record.symptomImageUrls
      }));
    },
    formatDate(dateStr) {
      if (!dateStr) return '-';
      const date = new Date(dateStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },
    mapStatus(status) {
      const statusMap = {
        1: 'pending',
        2: 'pending',
        3: 'normal',
        4: 'pending',
        5: 'normal',
        6: 'normal'
      };
      return statusMap[status] || 'pending';
    },
    getStatusText(status) {
      const statusTextMap = {
        1: '待分诊',
        2: 'AI分诊中',
        3: 'AI分诊完成',
        4: '待人工复审',
        5: '人工复审完成',
        6: '已生成方案'
      };
      return statusTextMap[status] || '未知';
    },
    viewRecordDetail(recordId) {
      uni.navigateTo({
        url: `/pages/medical-record/detail?recordId=${recordId}`
      });
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 病例列表 */
.record-list {
  padding: 20rpx;
}

.record-item {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.3s ease;
}

.record-item:hover {
  transform: translateY(-2rpx);
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

.record-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.record-date {
  font-size: 24rpx;
  color: #999;
}

.record-content {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15rpx;
}

.record-hospital {
  font-size: 26rpx;
  color: #666;
}

.record-doctor {
  font-size: 26rpx;
  color: #666;
}

.record-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.record-department {
  font-size: 24rpx;
  color: #999;
}

.record-status {
  font-size: 22rpx;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  font-weight: 500;
}

.record-status.normal {
  background-color: #E8F5E9;
  color: #4CAF50;
}

.record-status.pending {
  background-color: #FFF3E0;
  color: #FF9800;
}

.record-status.urgent {
  background-color: #FFEBEE;
  color: #F44336;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 0;
}

.empty-icon {
  font-size: 80rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

</style>