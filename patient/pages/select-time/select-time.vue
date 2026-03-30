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
  background-color: #f5f5f5;
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
  background-color: #f5f5f5;
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
  background-color: #f5f5f5;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  color: #fff;
}
</style>
