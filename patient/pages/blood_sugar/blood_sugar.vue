<template>
  <view class="blood-sugar-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
      <text class="nav-title">血糖监测</text>
      <view class="placeholder"></view>
    </view>

    <!-- 今日血糖概览 -->
    <view class="overview-section">
      <view class="overview-card">
        <view class="overview-header">
          <text class="overview-title">今日血糖概览</text>
          <text class="overview-date">{{ todayDate }}</text>
        </view>
        <view class="blood-sugar-stats">
          <view class="stat-item">
            <view class="stat-label">空腹血糖</view>
            <view class="stat-value">{{ fastingBloodSugar }}</view>
            <view class="stat-unit">mmol/L</view>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <view class="stat-label">餐后血糖</view>
            <view class="stat-value">{{ postMealBloodSugar }}</view>
            <view class="stat-unit">mmol/L</view>
          </view>
        </view>
        <view class="status-section">
          <view class="status-indicator" :class="bloodSugarStatus">
            <text class="status-text">{{ bloodSugarStatusText }}</text>
          </view>
          <view class="normal-range">
            <text class="range-text">正常范围: 3.9-6.1 mmol/L</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 血糖趋势 -->
    <view class="trend-section">
      <view class="section-title">
        <text class="title-icon">📊</text>
        <text>血糖趋势</text>
      </view>
      <view class="trend-card">
        <view class="trend-chart">
          <view class="chart-container">
            <view class="chart-grid">
              <view class="grid-line" v-for="i in 5" :key="i"></view>
            </view>
            <view class="chart-data">
              <view v-for="(item, index) in bloodSugarTrend" :key="index" class="data-point">
                <view class="point" :style="{ bottom: (item.value - 3) * 60 + 'rpx' }"></view>
                <view class="point-value">{{ item.value }}</view>
                <view class="point-label">{{ item.time }}</view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 血糖记录 -->
    <view class="record-section">
      <view class="section-header">
        <view class="section-title">
          <text class="title-icon">📝</text>
          <text>血糖记录</text>
        </view>
        <view class="add-btn" @click="showAddModal = true">
          <text>+ 添加</text>
        </view>
      </view>
      <view class="record-list">
        <view v-for="(record, index) in bloodSugarRecords" :key="index" class="record-item">
          <view class="record-time">
            <text class="time-text">{{ record.time }}</text>
            <text class="type-text">{{ record.type }}</text>
          </view>
          <view class="record-value" :class="getRecordStatus(record.value)">
            <text class="value-text">{{ record.value }}</text>
            <text class="unit-text">mmol/L</text>
          </view>
          <view class="record-note" v-if="record.note">
            <text>{{ record.note }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 血糖建议 -->
    <view class="advice-section">
      <view class="section-title">
        <text class="title-icon">💡</text>
        <text>血糖建议</text>
      </view>
      <view class="advice-list">
        <view v-for="(advice, index) in bloodSugarAdvices" :key="index" class="advice-item">
          <view class="advice-icon">{{ advice.icon }}</view>
          <view class="advice-content">
            <view class="advice-title">{{ advice.title }}</view>
            <view class="advice-text">{{ advice.text }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 添加血糖记录弹窗 -->
    <view v-if="showAddModal" class="modal-overlay" @click="showAddModal = false">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">添加血糖记录</text>
          <view class="modal-close" @click="showAddModal = false">✕</view>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">测量时间</text>
            <picker mode="time" @change="onTimeChange">
              <view class="form-input">{{ selectedTime }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">测量类型</text>
            <picker mode="selector" :range="measureTypes" @change="onTypeChange">
              <view class="form-input">{{ selectedType }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">血糖值 (mmol/L)</text>
            <input class="form-input" v-model="newRecord.value" placeholder="请输入血糖值" type="number" />
          </view>
          <view class="form-item">
            <text class="form-label">备注</text>
            <input class="form-input" v-model="newRecord.note" placeholder="请输入备注" />
          </view>
        </view>
        <view class="modal-footer">
          <view class="cancel-btn" @click="showAddModal = false">取消</view>
          <view class="confirm-btn" @click="addRecord">确认添加</view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      todayDate: '',
      fastingBloodSugar: 5.2,
      postMealBloodSugar: 7.8,
      bloodSugarStatus: 'normal',
      bloodSugarStatusText: '正常',
      bloodSugarTrend: [
        { time: '08:00', value: 5.2 },
        { time: '12:30', value: 7.8 },
        { time: '18:30', value: 6.5 },
        { time: '22:00', value: 5.8 }
      ],
      bloodSugarRecords: [
        {
          time: '08:00',
          type: '空腹',
          value: 5.2,
          note: '早餐前'
        },
        {
          time: '12:30',
          type: '餐后2小时',
          value: 7.8,
          note: '午餐后'
        },
        {
          time: '18:30',
          type: '餐后2小时',
          value: 6.5,
          note: '晚餐后'
        }
      ],
      bloodSugarAdvices: [
        {
          icon: '🥗',
          title: '控制饮食',
          text: '建议控制碳水化合物摄入，多吃蔬菜和蛋白质食物。'
        },
        {
          icon: '🏃',
          title: '适量运动',
          text: '每天坚持30分钟中等强度运动，有助于控制血糖。'
        },
        {
          icon: '⏰',
          title: '规律监测',
          text: '建议每天固定时间监测血糖，了解血糖变化趋势。'
        },
        {
          icon: '💧',
          title: '多喝水',
          text: '保持充足水分，有助于稀释血液，稳定血糖。'
        }
      ],
      showAddModal: false,
      measureTypes: ['空腹', '餐后2小时', '随机'],
      selectedType: '空腹',
      selectedTime: '08:00',
      newRecord: {
        time: '',
        type: '',
        value: '',
        note: ''
      }
    }
  },
  onLoad() {
    this.setTodayDate()
    this.updateBloodSugarStatus()
  },
  methods: {
    setTodayDate() {
      const date = new Date()
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      this.todayDate = `${year}年${month}月${day}日`
    },
    // goBack() {
    //   uni.navigateBack()
    // },
    updateBloodSugarStatus() {
      const avgBloodSugar = (this.fastingBloodSugar + this.postMealBloodSugar) / 2
      if (avgBloodSugar < 3.9) {
        this.bloodSugarStatus = 'low'
        this.bloodSugarStatusText = '偏低'
      } else if (avgBloodSugar > 6.1) {
        this.bloodSugarStatus = 'high'
        this.bloodSugarStatusText = '偏高'
      } else {
        this.bloodSugarStatus = 'normal'
        this.bloodSugarStatusText = '正常'
      }
    },
    getRecordStatus(value) {
      if (value < 3.9) return 'low'
      if (value > 6.1) return 'high'
      return 'normal'
    },
    onTimeChange(e) {
      this.selectedTime = e.detail.value
    },
    onTypeChange(e) {
      this.selectedType = this.measureTypes[e.detail.value]
    },
    addRecord() {
      if (!this.newRecord.value) {
        uni.showToast({
          title: '请填写血糖值',
          icon: 'none'
        })
        return
      }
      
      const newBloodSugarRecord = {
        time: this.selectedTime,
        type: this.selectedType,
        value: parseFloat(this.newRecord.value),
        note: this.newRecord.note || ''
      }
      
      this.bloodSugarRecords.unshift(newBloodSugarRecord)
      
      // 更新趋势数据
      if (this.bloodSugarTrend.length >= 4) {
        this.bloodSugarTrend.shift()
      }
      this.bloodSugarTrend.push({
        time: this.selectedTime,
        value: parseFloat(this.newRecord.value)
      })
      
      // 更新空腹和餐后血糖
      if (this.selectedType === '空腹') {
        this.fastingBloodSugar = parseFloat(this.newRecord.value)
      } else if (this.selectedType === '餐后2小时') {
        this.postMealBloodSugar = parseFloat(this.newRecord.value)
      }
      
      this.updateBloodSugarStatus()
      
      this.newRecord = {
        time: '',
        type: '',
        value: '',
        note: ''
      }
      this.showAddModal = false
      
      uni.showToast({
        title: '添加成功',
        icon: 'success'
      })
    }
  }
}
</script>

<style scoped>
.blood-sugar-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40rpx;
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
  font-size: 48rpx;
  color: #fff;
  font-weight: 300;
}
*/
.nav-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

/* 今日血糖概览 */
.overview-section {
  padding: 20rpx;
}

.overview-card {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.3);
}

.overview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.overview-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.overview-date {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.blood-sugar-stats {
  display: flex;
  justify-content: space-around;
  align-items: center;
  margin-bottom: 30rpx;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 8rpx;
}

.stat-value {
  font-size: 48rpx;
  font-weight: 700;
  color: #fff;
  margin-bottom: 4rpx;
}

.stat-unit {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.8);
}

.stat-divider {
  width: 1rpx;
  height: 80rpx;
  background: rgba(255, 255, 255, 0.3);
}

.status-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20rpx;
}

.status-indicator {
  padding: 12rpx 24rpx;
  border-radius: 20rpx;
  font-size: 24rpx;
  font-weight: 600;
}

.status-indicator.normal {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
}

.status-indicator.high {
  background: rgba(255, 107, 107, 0.2);
  color: #FF6B6B;
}

.status-indicator.low {
  background: rgba(255, 152, 0, 0.2);
  color: #FF9800;
}

.normal-range {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.8);
}

/* 血糖趋势 */
.trend-section {
  padding: 0 20rpx 20rpx;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12rpx;
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
}

.title-icon {
  font-size: 32rpx;
}

.trend-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 30rpx;
}

.trend-chart {
  height: 300rpx;
  position: relative;
}

.chart-container {
  position: relative;
  height: 100%;
}

.chart-grid {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
}

.grid-line {
  position: absolute;
  left: 0;
  right: 0;
  height: 1rpx;
  background: #f0f0f0;
}

.grid-line:nth-child(1) { top: 0; }
.grid-line:nth-child(2) { top: 25%; }
.grid-line:nth-child(3) { top: 50%; }
.grid-line:nth-child(4) { top: 75%; }
.grid-line:nth-child(5) { bottom: 0; }

.chart-data {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 2;
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  padding-bottom: 40rpx;
}

.data-point {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.point {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  position: absolute;
  bottom: 0;
  transform: translateY(-50%);
}

.point-value {
  font-size: 24rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.point-label {
  font-size: 20rpx;
  color: #999;
}

/* 血糖记录 */
.record-section {
  padding: 0 20rpx 20rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.add-btn {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 24rpx;
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
}

.record-list {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.record-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.record-item:last-child {
  border-bottom: none;
}

.record-time {
  width: 160rpx;
  margin-right: 24rpx;
}

.time-text {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 4rpx;
}

.type-text {
  font-size: 20rpx;
  color: #999;
}

.record-value {
  flex: 1;
  text-align: center;
  padding: 16rpx;
  border-radius: 12rpx;
  margin-right: 24rpx;
}

.record-value.normal {
  background: #E8F5E9;
  color: #4CAF50;
}

.record-value.high {
  background: #FFEBEE;
  color: #F44336;
}

.record-value.low {
  background: #FFF3E0;
  color: #FF9800;
}

.value-text {
  font-size: 32rpx;
  font-weight: 700;
  display: block;
}

.unit-text {
  font-size: 20rpx;
  opacity: 0.8;
}

.record-note {
  font-size: 24rpx;
  color: #666;
  flex: 1;
}

/* 血糖建议 */
.advice-section {
  padding: 0 20rpx;
}

.advice-list {
  background: #fff;
  border-radius: 20rpx;
  padding: 20rpx;
}

.advice-item {
  display: flex;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.advice-item:last-child {
  border-bottom: none;
}

.advice-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.advice-content {
  flex: 1;
}

.advice-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.advice-text {
  font-size: 24rpx;
  color: #666;
  line-height: 1.6;
}

/* 弹窗 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-content {
  width: 600rpx;
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.modal-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.modal-close {
  font-size: 40rpx;
  color: #999;
  line-height: 1;
}

.modal-body {
  padding: 30rpx;
}

.form-item {
  margin-bottom: 24rpx;
}

.form-label {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 12rpx;
  display: block;
}

.form-input {
  width: 100%;
  height: 80rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  color: #333;
  box-sizing: border-box;
  display: flex;
  align-items: center;
}

.modal-footer {
  display: flex;
  border-top: 1rpx solid #f0f0f0;
}

.cancel-btn,
.confirm-btn {
  flex: 1;
  height: 100rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
}

.cancel-btn {
  color: #666;
  border-right: 1rpx solid #f0f0f0;
}

.confirm-btn {
  color: #4DD0E1;
  font-weight: 600;
}
</style>
