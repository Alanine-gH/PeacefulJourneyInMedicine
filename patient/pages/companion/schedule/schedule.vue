<template>
  <view class="container">
    <!-- 头部 -->
<!--    <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">日程安排</view>
        <view class="header-right"></view>
      </view>
    </view> -->

    <!-- 日历区域 -->
    <view class="calendar-section">
      <!-- 月份导航 -->
      <view class="calendar-header">
        <view class="month-nav" @click="prevMonth">
          <text class="nav-icon">‹</text>
        </view>
        <view class="current-month">{{ currentYear }}年{{ currentMonth + 1 }}月</view>
        <view class="month-nav" @click="nextMonth">
          <text class="nav-icon">›</text>
        </view>
      </view>

      <!-- 星期标题 -->
      <view class="week-header">
        <view class="week-day" v-for="day in weekDays" :key="day">{{ day }}</view>
      </view>

      <!-- 日历网格 -->
      <view class="calendar-grid">
        <!-- 上个月的日期 -->
        <view 
          v-for="prevDay in prevMonthDays" 
          :key="'prev-' + prevDay"
          class="calendar-day other-month"
        >
          <text class="day-number">{{ prevDay }}</text>
        </view>

        <!-- 当前月的日期 -->
        <view 
          v-for="day in currentMonthDays" 
          :key="'current-' + day"
          class="calendar-day"
          :class="{ 
            'today': isToday(day),
            'has-note': hasNote(day)
          }"
          @click="toggleNote(day)"
        >
          <text class="day-number">{{ day }}</text>
          <view v-if="hasNote(day)" class="note-indicator"></view>
        </view>

        <!-- 下个月的日期 -->
        <view 
          v-for="nextDay in nextMonthDays" 
          :key="'next-' + nextDay"
          class="calendar-day other-month"
        >
          <text class="day-number">{{ nextDay }}</text>
        </view>
      </view>
    </view>

    <!-- 备注输入区域 -->
    <view v-if="showNoteInput" class="note-section">
      <view class="note-header">
        <view class="note-date">{{ currentYear }}年{{ currentMonth + 1 }}月{{ selectedDay }}日</view>
        <view class="note-close" @click="closeNoteInput">
          <text class="close-icon">×</text>
        </view>
      </view>
      <textarea 
        v-model="currentNote"
        class="note-textarea"
        placeholder="请输入备注..."
        maxlength="200"
      ></textarea>
      <view class="note-actions">
        <view class="note-counter">{{ currentNote.length }}/200</view>
        <button class="save-note-btn" @click="saveNote">保存备注</button>
      </view>
    </view>

    <!-- 备注显示区域 -->
    <view v-else-if="showNoteDetail" class="note-section">
      <view class="note-header">
        <view class="note-date">{{ currentYear }}年{{ currentMonth + 1 }}月{{ selectedDay }}日</view>
        <view class="note-close" @click="closeNoteDetail">
          <text class="close-icon">×</text>
        </view>
      </view>
      <view class="note-content">{{ currentNote }}</view>
      <view class="note-actions">
        <button class="edit-note-btn" @click="editNote">编辑备注</button>
        <button class="delete-note-btn" @click="deleteNote">删除备注</button>
      </view>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>

    <!-- 底部导航栏 -->
    <CompanionTabBar activeTab="schedule"></CompanionTabBar>
  </view>
</template>

<script>
import CompanionTabBar from '@/components/CompanionTabBar.vue'

export default {
  components: {
    CompanionTabBar
  },
  data() {
    return {
      currentDate: new Date(),
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth(),
      selectedDay: null,
      showNoteInput: false,
      showNoteDetail: false,
      currentNote: '',
      notes: {},
      weekDays: ['日', '一', '二', '三', '四', '五', '六']
    }
  },
  computed: {
    // 当前月的天数
    currentMonthDays() {
      return new Date(this.currentYear, this.currentMonth + 1, 0).getDate()
    },
    // 当月第一天是星期几
    firstDayOfMonth() {
      return new Date(this.currentYear, this.currentMonth, 1).getDay()
    },
    // 上个月需要显示的天数
    prevMonthDays() {
      const prevMonthLastDay = new Date(this.currentYear, this.currentMonth, 0).getDate()
      const days = []
      for (let i = prevMonthLastDay - this.firstDayOfMonth + 1; i <= prevMonthLastDay; i++) {
        days.push(i)
      }
      return days
    },
    // 下个月需要显示的天数
    nextMonthDays() {
      const totalDays = this.firstDayOfMonth + this.currentMonthDays
      const days = []
      for (let i = 1; i <= 42 - totalDays; i++) {
        days.push(i)
      }
      return days
    }
  },
  onLoad() {
    this.loadNotes()
  },
  methods: {
    // 加载备注数据
    loadNotes() {
      const savedNotes = uni.getStorageSync('companion_schedule_notes')
      if (savedNotes) {
        this.notes = savedNotes
      }
    },

    // 保存备注数据
    saveNotes() {
      uni.setStorageSync('companion_schedule_notes', this.notes)
    },

    // 上个月
    prevMonth() {
      this.currentMonth--
      if (this.currentMonth < 0) {
        this.currentMonth = 11
        this.currentYear--
      }
    },

    // 下个月
    nextMonth() {
      this.currentMonth++
      if (this.currentMonth > 11) {
        this.currentMonth = 0
        this.currentYear++
      }
    },

    // 判断是否是今天
    isToday(day) {
      const today = new Date()
      return today.getFullYear() === this.currentYear && 
             today.getMonth() === this.currentMonth && 
             today.getDate() === day
    },

    // 判断是否有备注
    hasNote(day) {
      const dateKey = `${this.currentYear}-${this.currentMonth + 1}-${day}`
      return !!this.notes[dateKey]
    },

    // 切换备注显示/输入
    toggleNote(day) {
      this.selectedDay = day
      const dateKey = `${this.currentYear}-${this.currentMonth + 1}-${day}`
      
      if (this.notes[dateKey]) {
        // 显示已有备注
        this.currentNote = this.notes[dateKey]
        this.showNoteDetail = true
        this.showNoteInput = false
      } else {
        // 显示输入框
        this.currentNote = ''
        this.showNoteInput = true
        this.showNoteDetail = false
      }
    },

    // 关闭备注输入
    closeNoteInput() {
      this.showNoteInput = false
      this.selectedDay = null
    },

    // 关闭备注详情
    closeNoteDetail() {
      this.showNoteDetail = false
      this.selectedDay = null
    },

    // 保存备注
    saveNote() {
      if (this.currentNote.trim()) {
        const dateKey = `${this.currentYear}-${this.currentMonth + 1}-${this.selectedDay}`
        this.notes[dateKey] = this.currentNote.trim()
        this.saveNotes()
        uni.showToast({
          title: '备注保存成功',
          icon: 'success'
        })
        this.showNoteInput = false
        this.selectedDay = null
      } else {
        uni.showToast({
          title: '请输入备注内容',
          icon: 'none'
        })
      }
    },

    // 编辑备注
    editNote() {
      this.showNoteDetail = false
      this.showNoteInput = true
    },

    // 删除备注
    deleteNote() {
      uni.showModal({
        title: '删除备注',
        content: '确定要删除这条备注吗？',
        confirmText: '删除',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            const dateKey = `${this.currentYear}-${this.currentMonth + 1}-${this.selectedDay}`
            delete this.notes[dateKey]
            this.saveNotes()
            uni.showToast({
              title: '备注已删除',
              icon: 'success'
            })
            this.showNoteDetail = false
            this.selectedDay = null
          }
        }
      })
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
  position: relative;
}

.header-left {
  width: 60rpx;
  height: 60rpx;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.header-right {
  width: 60rpx;
}

/* 日历区域 */
.calendar-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 月份导航 */
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.month-nav {
  width: 60rpx;
  height: 60rpx;
  border-radius: 50%;
  background-color: rgba(77, 208, 225, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.month-nav:active {
  background-color: rgba(77, 208, 225, 0.2);
}

.nav-icon {
  font-size: 32rpx;
  color: #4DD0E1;
  font-weight: bold;
}

.current-month {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

/* 星期标题 */
.week-header {
  display: flex;
  margin-bottom: 20rpx;
}

.week-day {
  flex: 1;
  text-align: center;
  font-size: 28rpx;
  font-weight: 500;
  color: #666;
  padding: 15rpx 0;
}

/* 日历网格 */
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 10rpx;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 15rpx;
  position: relative;
  transition: all 0.3s ease;
}

.calendar-day:active {
  background-color: rgba(77, 208, 225, 0.1);
}

.other-month {
  opacity: 0.3;
}

.today {
  background-color: rgba(77, 208, 225, 0.2);
}

.has-note {
  position: relative;
}

.note-indicator {
  position: absolute;
  bottom: 5rpx;
  width: 8rpx;
  height: 8rpx;
  border-radius: 50%;
  background-color: #4DD0E1;
}

.day-number {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
}

.other-month .day-number {
  color: #999;
}

.today .day-number {
  color: #4DD0E1;
  font-weight: 600;
}

/* 备注区域 */
.note-section {
  background: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.note-date {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.note-close {
  width: 50rpx;
  height: 50rpx;
  border-radius: 50%;
  background-color: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.note-close:active {
  background-color: #e0e0e0;
}

.close-icon {
  font-size: 32rpx;
  color: #999;
  font-weight: bold;
}

/* 备注输入 */
.note-textarea {
  width: 100%;
  min-height: 200rpx;
  padding: 20rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  font-size: 28rpx;
  color: #333;
  resize: none;
  box-sizing: border-box;
  margin-bottom: 20rpx;
}

/* 备注内容 */
.note-content {
  font-size: 28rpx;
  color: #333;
  line-height: 1.5;
  margin-bottom: 30rpx;
  min-height: 150rpx;
  white-space: pre-wrap;
}

/* 备注操作 */
.note-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.note-counter {
  font-size: 24rpx;
  color: #999;
}

.save-note-btn {
  padding: 0 40rpx;
  height: 70rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 28rpx;
  font-weight: 500;
  border-radius: 35rpx;
  border: none;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.4);
}

.save-note-btn::after {
  border: none;
}

.save-note-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

.edit-note-btn,
.delete-note-btn {
  flex: 1;
  height: 70rpx;
  font-size: 28rpx;
  font-weight: 500;
  border-radius: 35rpx;
  border: 2rpx solid #e8e8e8;
  margin: 0 10rpx;
}

.edit-note-btn {
  color: #4DD0E1;
  background-color: #fff;
}

.delete-note-btn {
  color: #F44336;
  background-color: #fff;
}

.edit-note-btn::after,
.delete-note-btn::after {
  border: none;
}

.edit-note-btn:active,
.delete-note-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .calendar-section {
    margin: 15rpx;
    padding: 20rpx;
  }
  
  .calendar-header {
    margin-bottom: 20rpx;
  }
  
  .current-month {
    font-size: 28rpx;
  }
  
  .week-day {
    font-size: 24rpx;
    padding: 10rpx 0;
  }
  
  .day-number {
    font-size: 26rpx;
  }
  
  .note-section {
    margin: 0 15rpx 15rpx;
    padding: 20rpx;
  }
  
  .note-date {
    font-size: 26rpx;
  }
  
  .note-textarea {
    font-size: 26rpx;
    min-height: 180rpx;
  }
  
  .note-content {
    font-size: 26rpx;
  }
}

/* 底部空白区域 */
.bottom-space {
  height: 120rpx;
  width: 100%;
}
</style>