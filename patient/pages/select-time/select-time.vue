<template>
  <view class="select-time-page">
    <!-- 时间选择区域 -->
    <view class="time-content">
      <!-- 日期选择 - 横向滚动 -->
      <scroll-view class="date-scroll" scroll-x="true" show-scrollbar="false">
        <view class="date-list">
          <view 
            class="date-item" 
            v-for="(date, index) in dateList" 
            :key="index"
            :class="{ active: selectedDateIndex === index }"
            @click="selectDate(index)"
          >
            <text class="date-day">{{date.dayText}}</text>
            <text class="date-text">{{date.dateText}}</text>
          </view>
        </view>
      </scroll-view>

      <!-- 时间段选择 -->
      <view class="time-section">
        <text class="section-title">选择时间段</text>
        <view class="time-grid">
          <view 
            class="time-item" 
            v-for="(time, index) in timeSlots" 
            :key="index"
            :class="{ active: selectedTime === time, disabled: isTimeDisabled(time) }"
            @click="selectTimeSlot(time)"
          >
            <text class="time-text">{{time}}</text>
          </view>
        </view>
      </view>

      <!-- 备注 -->
      <view class="note-section">
        <text class="note-title">备注说明</text>
        <textarea class="note-input" placeholder="请输入其他时间要求（选填）" v-model="note" maxlength="100" />
        <text class="word-count">{{note.length}}/100字</text>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <button class="btn btn-cancel" @click="goBack">取消</button>
      <button class="btn btn-confirm" @click="confirmTime">确定</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      selectedDateIndex: 0,
      selectedTime: '',
      note: '',
      dateList: [],
      timeSlots: [
        '08:00', '08:30', '09:00', '09:30', '10:00', '10:30',
        '11:00', '11:30', '14:00', '14:30', '15:00', '15:30',
        '16:00', '16:30', '17:00', '17:30'
      ]
    }
  },
  onLoad() {
    this.initDates()
  },
  methods: {
    initDates() {
      const now = new Date()
      const dateList = []
      
      for (let i = 0; i < 7; i++) {
        const date = new Date(now)
        date.setDate(date.getDate() + i)
        
        const month = date.getMonth() + 1
        const day = date.getDate()
        const weekDay = date.getDay()
        
        let dayText = ''
        if (i === 0) {
          dayText = '今天'
        } else if (i === 1) {
          dayText = '明天'
        } else if (i === 2) {
          dayText = '后天'
        } else {
          const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
          dayText = weekDays[weekDay]
        }
        
        dateList.push({
          dayText: dayText,
          dateText: `${month}月${day}日`,
          fullDate: date
        })
      }
      
      this.dateList = dateList
    },
    goBack() {
      uni.navigateBack()
    },
    selectDate(index) {
      this.selectedDateIndex = index
      // 切换日期时，如果选中的时间已过期，清空选中
      if (this.selectedTime && this.isTimeDisabled(this.selectedTime)) {
        this.selectedTime = ''
      }
    },
    selectTimeSlot(time) {
      if (this.isTimeDisabled(time)) {
        return
      }
      this.selectedTime = time
    },
    isTimeDisabled(time) {
      // 如果是今天（索引0），判断时间是否已过
      if (this.selectedDateIndex === 0) {
        const now = new Date()
        const currentHour = now.getHours()
        const currentMinute = now.getMinutes()
        const [hour, minute] = time.split(':').map(Number)
        
        if (hour < currentHour || (hour === currentHour && minute <= currentMinute)) {
          return true
        }
      }
      return false
    },
    confirmTime() {
      if (!this.selectedTime) {
        uni.showToast({ title: '请选择时间段', icon: 'none' })
        return
      }
      
      const selectedDate = this.dateList[this.selectedDateIndex]
      const visitTime = `${selectedDate.dateText} ${this.selectedTime}`
      
      // 通过 storage 传递数据，避免 $vm 直接赋值在部分场景下失效
      uni.setStorageSync('_sel_visitTime', visitTime)
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.select-time-page {
  min-height: 100vh;
  background-color: #f4f2ee;
  padding-bottom: 140rpx;
}

/* 时间内容 */
.time-content {
  padding: 20rpx;
}

/* 日期选择 - 横向滚动 */
.date-scroll {
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  white-space: nowrap;
}

.date-list {
  display: flex;
  padding: 20rpx;
}

.date-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 30rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  min-width: 120rpx;
}

.date-item:last-child {
  margin-right: 0;
}

.date-item.active {
  background-color: #e3f2fd;
}

.date-item.active .date-day,
.date-item.active .date-text {
  color: #2196F3;
}

.date-day {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.date-text {
  font-size: 22rpx;
  color: #999;
}

/* 时间段选择 */
.time-section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 30rpx;
  display: block;
}

.time-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.time-item {
  width: calc(25% - 15rpx);
  height: 80rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f4f2ee;
  border-radius: 12rpx;
  border: 2rpx solid transparent;
}

.time-item.active {
  background-color: #e3f2fd;
  border-color: #2196F3;
}

.time-item.disabled {
  background-color: #f0f0f0;
  opacity: 0.5;
}

.time-text {
  font-size: 28rpx;
  color: #333;
}

.time-item.active .time-text {
  color: #2196F3;
}

.time-item.disabled .time-text {
  color: #999;
}

/* 备注 */
.note-section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
}

.note-title {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
  display: block;
}

.note-input {
  width: 100%;
  height: 150rpx;
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

/* 底部按钮 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  display: flex;
  gap: 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.05);
}

.btn {
  flex: 1;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 45rpx;
  font-size: 32rpx;
  font-weight: 500;
  border: none;
}

.btn::after {
  border: none;
}

.btn-cancel {
  background-color: #f4f2ee;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  color: #fff;
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