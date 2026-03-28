<template>
  <view class="container">
    <!-- 顶部导航栏 -->
    <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">病例详情</view>
        <view class="header-right"></view>
      </view>
    </view>

    <!-- 病例详情 -->
    <view class="record-detail">
      <view class="detail-section">
        <view class="section-title">基本信息</view>
        <view class="info-item">
          <text class="info-label">分诊单号：</text>
          <text class="info-value">{{ record.triageNo }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">就诊日期：</text>
          <text class="info-value">{{ record.date }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">患者姓名：</text>
          <text class="info-value">{{ record.userName }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">联系电话：</text>
          <text class="info-value">{{ record.userPhone }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">性别/年龄：</text>
          <text class="info-value">{{ record.gender }} / {{ record.age }}岁</text>
        </view>
        <view class="info-item">
          <text class="info-label">推荐医院：</text>
          <text class="info-value">{{ record.hospital }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">推荐科室：</text>
          <text class="info-value">{{ record.department }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">状态：</text>
          <text class="info-value status-{{ record.status }}">{{ record.statusText }}</text>
        </view>
      </view>

      <view class="detail-section">
        <view class="section-title">症状信息</view>
        <view class="info-item">
          <text class="info-label">主诉：</text>
          <text class="info-value">{{ record.chiefComplaint }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">症状描述：</text>
          <text class="info-value">{{ record.symptomDescription }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">疼痛部位：</text>
          <text class="info-value">{{ record.painLocation }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">疼痛等级：</text>
          <text class="info-value">{{ record.painLevel }} / 10</text>
        </view>
        <view class="info-item">
          <text class="info-label">疼痛持续时间：</text>
          <text class="info-value">{{ record.painDuration }}</text>
        </view>
      </view>

      <view class="detail-section">
        <view class="section-title">病史信息</view>
        <view class="info-item">
          <text class="info-label">既往病史：</text>
          <text class="info-value">{{ record.medicalHistory }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">过敏史：</text>
          <text class="info-value">{{ record.allergyHistory }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">当前用药：</text>
          <text class="info-value">{{ record.currentMedication }}</text>
        </view>
      </view>

      <view class="detail-section">
        <view class="section-title">分诊结果</view>
        <view class="info-item">
          <text class="info-label">AI分析结果：</text>
          <text class="info-value">{{ record.diagnosis }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">AI置信度：</text>
          <text class="info-value">{{ record.aiConfidence }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">AI分析时间：</text>
          <text class="info-value">{{ record.aiAnalysisTime }}</text>
        </view>
        <view class="info-item">
          <text class="info-label">人工复审时间：</text>
          <text class="info-value">{{ record.humanReviewTime }}</text>
        </view>
      </view>

      <view class="detail-section">
        <view class="section-title">检查报告</view>
        <view class="report-list">
          <view 
            v-for="(report, index) in record.reports" 
            :key="index" 
            class="report-item"
            @click="viewReport(report)"
          >
            <text class="report-name">{{ report.name }}</text>
            <text class="report-date">{{ report.date }}</text>
            <text class="report-arrow">›</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import {
  getTriageRecordDetail
} from '../../utils/api';

export default {
  data() {
    return {
      recordId: '',
      record: {
        id: '',
        title: '',
        date: '',
        hospital: '',
        doctor: '-',
        department: '',
        status: '',
        statusText: '',
        chiefComplaint: '',
        symptomDescription: '',
        diagnosis: '',
        medication: '',
        otherTreatment: '',
        advice: '',
        reports: [],
        triageNo: '',
        userName: '',
        userPhone: '',
        gender: '',
        age: '',
        painLocation: '',
        painLevel: '',
        painDuration: '',
        medicalHistory: '',
        allergyHistory: '',
        currentMedication: '',
        triageResult: '',
        aiConfidence: '',
        aiAnalysisTime: '',
        humanReviewTime: '',
        symptomImageUrls: ''
      },
      loading: false
    }
  },
  onLoad(options) {
    if (options.recordId) {
      this.recordId = options.recordId;
      this.fetchRecordDetail();
    }
  },
  methods: {

    async fetchRecordDetail() {
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
        
        const response = await getTriageRecordDetail(this.recordId);
        
        if (response && response.code === 200 && response.data) {
          // 验证病例是否属于当前用户
          if (String(response.data.userId) !== String(userId)) {
            uni.showToast({
              title: '无权查看此病例',
              icon: 'none'
            });
            uni.navigateBack();
            return;
          }
          
          this.record = this.transformRecordDetail(response.data);
        } else {
          uni.showToast({
            title: '获取病例详情失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('获取分诊记录详情失败:', error);
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    transformRecordDetail(backendRecord) {
      return {
        id: backendRecord.id,
        title: backendRecord.chiefComplaint || '分诊记录',
        date: this.formatDate(backendRecord.createTime),
        hospital: backendRecord.recommendedHospitals || '待推荐',
        doctor: '-',
        department: backendRecord.recommendedDepartment || '待分诊',
        status: this.mapStatus(backendRecord.triageStatus),
        statusText: this.getStatusText(backendRecord.triageStatus),
        chiefComplaint: backendRecord.chiefComplaint || '-',
        symptomDescription: backendRecord.symptomDescription || '-',
        diagnosis: backendRecord.triageResult || '-',
        medication: backendRecord.currentMedication || '-',
        otherTreatment: '-',
        advice: backendRecord.recommendedHospitals || '-',
        reports: this.generateReports(backendRecord),
        triageNo: backendRecord.triageNo || '-',
        userName: backendRecord.userName || '-',
        userPhone: backendRecord.userPhone || '-',
        gender: this.formatGender(backendRecord.gender),
        age: backendRecord.age || '-',
        painLocation: backendRecord.painLocation || '-',
        painLevel: backendRecord.painLevel || '-',
        painDuration: backendRecord.painDuration || '-',
        medicalHistory: backendRecord.medicalHistory || '-',
        allergyHistory: backendRecord.allergyHistory || '-',
        currentMedication: backendRecord.currentMedication || '-',
        triageResult: backendRecord.triageResult || '-',
        aiConfidence: backendRecord.aiConfidence || '-',
        aiAnalysisTime: this.formatDateTime(backendRecord.aiAnalysisTime),
        humanReviewTime: this.formatDateTime(backendRecord.humanReviewTime),
        symptomImageUrls: backendRecord.symptomImageUrls || ''
      };
    },
    generateReports(record) {
      const reports = [];
      if (record.symptomImageUrls) {
        const images = record.symptomImageUrls.split(',');
        images.forEach((url, index) => {
          reports.push({
            name: `症状图片 ${index + 1}`,
            date: this.formatDate(record.createTime)
          });
        });
      }
      if (record.triageResult) {
        reports.push({
          name: 'AI分诊结果',
          date: this.formatDate(record.aiAnalysisTime)
        });
      }
      return reports.length > 0 ? reports : [{ name: '暂无检查报告', date: '-' }];
    },
    formatDate(dateStr) {
      if (!dateStr) return '-';
      const date = new Date(dateStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },
    formatDateTime(dateStr) {
      if (!dateStr) return '-';
      const date = new Date(dateStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    },
    formatGender(gender) {
      const genderMap = {
        0: '未知',
        1: '男',
        2: '女'
      };
      return genderMap[gender] || '-';
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
    viewReport(report) {
      if (report.name === '暂无检查报告') {
        return;
      }
      uni.showToast({
        title: `查看${report.name}`,
        icon: 'none'
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

.header-left {
  width: 60rpx;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  flex: 1;
  text-align: center;
}

.header-right {
  width: 60rpx;
}

/* 病例详情 */
.record-detail {
  padding: 20rpx;
}

.detail-section {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
  padding-bottom: 15rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-item {
  display: flex;
  margin-bottom: 15rpx;
  align-items: flex-start;
}

.info-label {
  font-size: 26rpx;
  color: #666;
  width: 120rpx;
  flex-shrink: 0;
}

.info-value {
  font-size: 26rpx;
  color: #333;
  flex: 1;
  line-height: 1.4;
}

.info-value.status-normal {
  color: #4CAF50;
}

.info-value.status-pending {
  color: #FF9800;
}

.info-value.status-urgent {
  color: #F44336;
}

/* 检查报告 */
.report-list {
  margin-top: 10rpx;
}

.report-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
  cursor: pointer;
}

.report-item:last-child {
  border-bottom: none;
}

.report-name {
  font-size: 26rpx;
  color: #333;
  flex: 1;
}

.report-date {
  font-size: 24rpx;
  color: #999;
  margin-right: 15rpx;
}

.report-arrow {
  font-size: 24rpx;
  color: #999;
}

</style>