<template>
  <view class="medication-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
      <text class="nav-title">用药提醒</text>
      <view class="placeholder"></view>
    </view>

    <!-- 今日用药概览 -->
    <view class="overview-section">
      <view class="overview-card">
        <view class="overview-header">
          <text class="overview-title">今日用药概览</text>
          <text class="overview-date">{{ todayDate }}</text>
        </view>
        <view class="medication-stats">
          <view class="stat-item">
            <view class="stat-value">{{ completedMedications }}</view>
            <view class="stat-label">已完成</view>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <view class="stat-value">{{ pendingMedications }}</view>
            <view class="stat-label">待服用</view>
          </view>
          <view class="stat-divider"></view>
          <view class="stat-item">
            <view class="stat-value">{{ totalMedications }}</view>
            <view class="stat-label">总计</view>
          </view>
        </view>
        <view class="progress-section">
          <view class="progress-text">完成率: {{ completionRate }}%</view>
          <view class="progress-bar">
            <view class="progress-fill" :style="{ width: completionRate + '%' }"></view>
          </view>
        </view>
      </view>
    </view>

    <!-- 用药提醒列表 -->
    <view class="reminder-section">
      <view class="section-header">
        <view class="section-title">
          <text class="title-icon">💊</text>
          <text>用药提醒</text>
        </view>
        <view class="add-btn" @click="showAddModal = true">
          <text>+ 添加</text>
        </view>
      </view>
      <view class="reminder-list">
        <view v-for="(reminder, index) in medicationReminders" :key="index" class="reminder-item" :class="{ 'completed': reminder.completed }">
          <view class="reminder-time">
            <text class="time-text">{{ reminder.time }}</text>
            <text class="period-text">{{ reminder.period }}</text>
          </view>
          <view class="reminder-content">
            <view class="medication-info">
              <view class="medication-name">{{ reminder.name }}</view>
              <view class="medication-dosage">{{ reminder.dosage }}</view>
            </view>
            <view class="reminder-actions">
              <view v-if="!reminder.completed" class="complete-btn" @click="completeMedication(index)">
                <text>✓</text>
              </view>
              <view v-else class="completed-btn">
                <text>已完成</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 用药记录 -->
    <view class="history-section">
      <view class="section-title">
        <text class="title-icon">📋</text>
        <text>用药记录</text>
      </view>
      <view class="history-list">
        <view v-for="(record, index) in medicationHistory" :key="index" class="history-item">
          <view class="history-time">{{ record.time }}</view>
          <view class="history-medication">{{ record.medication }}</view>
          <view class="history-status" :class="record.status">
            {{ record.status === 'completed' ? '已服用' : '未服用' }}
          </view>
        </view>
      </view>
    </view>

    <!-- 用药建议 -->
    <view class="advice-section">
      <view class="section-title">
        <text class="title-icon">💡</text>
        <text>用药建议</text>
      </view>
      <view class="advice-list">
        <view v-for="(advice, index) in medicationAdvices" :key="index" class="advice-item">
          <view class="advice-icon">{{ advice.icon }}</view>
          <view class="advice-content">
            <view class="advice-title">{{ advice.title }}</view>
            <view class="advice-text">{{ advice.text }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 添加用药提醒弹窗 -->
    <view v-if="showAddModal" class="modal-overlay" @click="showAddModal = false">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">添加用药提醒</text>
          <view class="modal-close" @click="showAddModal = false">✕</view>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">药品名称</text>
            <input class="form-input" v-model="newReminder.name" placeholder="请输入药品名称" />
          </view>
          <view class="form-item">
            <text class="form-label">用药时间</text>
            <picker mode="time" @change="onTimeChange">
              <view class="form-input">{{ selectedTime }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">用药周期</text>
            <picker mode="selector" :range="periods" @change="onPeriodChange">
              <view class="form-input">{{ selectedPeriod }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">用药剂量</text>
            <input class="form-input" v-model="newReminder.dosage" placeholder="例如: 1片" />
          </view>
          <view class="form-item">
            <text class="form-label">用药说明</text>
            <input class="form-input" v-model="newReminder.note" placeholder="请输入用药说明" />
          </view>
        </view>
        <view class="modal-footer">
          <view class="cancel-btn" @click="showAddModal = false">取消</view>
          <view class="confirm-btn" @click="addReminder">确认添加</view>
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
      completedMedications: 2,
      pendingMedications: 1,
      totalMedications: 3,
      medicationReminders: [
        {
          time: '08:00',
          period: '早餐后',
          name: '阿司匹林',
          dosage: '1片',
          note: '饭后服用',
          completed: true
        },
        {
          time: '12:30',
          period: '午餐后',
          name: '布洛芬',
          dosage: '1片',
          note: '疼痛时服用',
          completed: true
        },
        {
          time: '18:30',
          period: '晚餐后',
          name: '维生素C',
          dosage: '1片',
          note: '增强免疫力',
          completed: false
        }
      ],
      medicationHistory: [
        {
          time: '昨天 18:30',
          medication: '维生素C (1片)',
          status: 'completed'
        },
        {
          time: '昨天 12:30',
          medication: '布洛芬 (1片)',
          status: 'completed'
        },
        {
          time: '昨天 08:00',
          medication: '阿司匹林 (1片)',
          status: 'completed'
        }
      ],
      medicationAdvices: [
        {
          icon: '⏰',
          title: '按时服药',
          text: '请严格按照规定时间服药，不要随意更改用药时间。'
        },
        {
          icon: '💧',
          title: '多喝水',
          text: '服药时建议用温水送服，有助于药物吸收。'
        },
        {
          icon: '🍎',
          title: '注意饮食',
          text: '某些药物需要空腹服用，某些需要饭后服用，请按照医嘱执行。'
        },
        {
          icon: '📱',
          title: '设置提醒',
          text: '建议开启手机提醒功能，确保按时服药。'
        }
      ],
      showAddModal: false,
      periods: ['早餐前', '早餐后', '午餐前', '午餐后', '晚餐前', '晚餐后', '睡前'],
      selectedPeriod: '早餐后',
      selectedTime: '08:00',
      newReminder: {
        name: '',
        time: '',
        period: '',
        dosage: '',
        note: ''
      }
    }
  },
  computed: {
    completionRate() {
      return Math.round((this.completedMedications / this.totalMedications) * 100)
    }
  },
  onLoad() {
    this.setTodayDate()
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
    onTimeChange(e) {
      this.selectedTime = e.detail.value
    },
    onPeriodChange(e) {
      this.selectedPeriod = this.periods[e.detail.value]
    },
    addReminder() {
      if (!this.newReminder.name || !this.newReminder.dosage) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      
      const newMedication = {
        time: this.selectedTime,
        period: this.selectedPeriod,
        name: this.newReminder.name,
        dosage: this.newReminder.dosage,
        note: this.newReminder.note || '',
        completed: false
      }
      
      this.medicationReminders.push(newMedication)
      this.totalMedications++
      this.pendingMedications++
      
      this.newReminder = {
        name: '',
        time: '',
        period: '',
        dosage: '',
        note: ''
      }
      this.showAddModal = false
      
      uni.showToast({
        title: '添加成功',
        icon: 'success'
      })
    },
    completeMedication(index) {
      this.medicationReminders[index].completed = true
      this.completedMedications++
      this.pendingMedications--
      
      const now = new Date()
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      
      const newRecord = {
        time: `今天 ${hours}:${minutes}`,
        medication: `${this.medicationReminders[index].name} (${this.medicationReminders[index].dosage})`,
        status: 'completed'
      }
      
      this.medicationHistory.unshift(newRecord)
      
      uni.showToast({
        title: '已标记为完成',
        icon: 'success'
      })
    }
  }
}
</script>

<style scoped>
.medication-page {
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

/* 今日用药概览 */
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

.medication-stats {
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

.stat-value {
  font-size: 48rpx;
  font-weight: 700;
  color: #fff;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
}

.stat-divider {
  width: 1rpx;
  height: 60rpx;
  background: rgba(255, 255, 255, 0.3);
}

.progress-section {
  margin-top: 20rpx;
}

.progress-text {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 16rpx;
}

.progress-bar {
  width: 100%;
  height: 16rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 8rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: #fff;
  border-radius: 8rpx;
  transition: width 0.3s ease;
}

/* 用药提醒列表 */
.reminder-section {
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

.reminder-list {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.reminder-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
  transition: all 0.3s ease;
}

.reminder-item:last-child {
  border-bottom: none;
}

.reminder-item.completed {
  opacity: 0.6;
}

.reminder-time {
  width: 120rpx;
  margin-right: 24rpx;
}

.time-text {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 4rpx;
}

.period-text {
  font-size: 20rpx;
  color: #999;
}

.reminder-content {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.medication-info {
  flex: 1;
}

.medication-name {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.medication-dosage {
  font-size: 24rpx;
  color: #666;
}

.reminder-actions {
  margin-left: 20rpx;
}

.complete-btn {
  width: 52rpx;
  height: 52rpx;
  border: 2rpx solid #4DD0E1;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4DD0E1;
  font-size: 32rpx;
  font-weight: bold;
  transition: all 0.3s ease;
}

.complete-btn:active {
  background: #4DD0E1;
  color: #fff;
}

.completed-btn {
  background: #E8F5E9;
  color: #4CAF50;
  font-size: 20rpx;
  padding: 8rpx 16rpx;
  border-radius: 16rpx;
}

/* 用药记录 */
.history-section {
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

.history-list {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.history-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.history-item:last-child {
  border-bottom: none;
}

.history-time {
  width: 160rpx;
  font-size: 24rpx;
  color: #999;
  margin-right: 24rpx;
}

.history-medication {
  flex: 1;
  font-size: 28rpx;
  color: #333;
  margin-right: 24rpx;
}

.history-status {
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 16rpx;
}

.history-status.completed {
  background: #E8F5E9;
  color: #4CAF50;
}

.history-status.pending {
  background: #FFF3E0;
  color: #FF9800;
}

/* 用药建议 */
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
