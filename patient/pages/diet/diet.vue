<template>
  <view class="diet-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
      <text class="nav-title">饮食检测</text>
      <view class="placeholder"></view>
    </view>

    <!-- 今日摄入概览 -->
    <view class="overview-section">
      <view class="overview-card">
        <view class="overview-header">
          <text class="overview-title">今日摄入概览</text>
          <text class="overview-date">{{ todayDate }}</text>
        </view>
        <view class="calorie-ring">
          <view class="ring-container">
            <view class="ring-value">{{ totalCalories }}</view>
            <view class="ring-unit">千卡</view>
          </view>
          <view class="ring-progress">
            <view class="progress-text">目标: {{ targetCalories }} 千卡</view>
            <view class="progress-bar">
              <view class="progress-fill" :style="{ width: progressPercent + '%' }"></view>
            </view>
            <view class="progress-percent">{{ progressPercent }}%</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 营养素摄入 -->
    <view class="nutrients-section">
      <view class="section-title">
        <text class="title-icon">🍎</text>
        <text>营养素摄入</text>
      </view>
      <view class="nutrients-grid">
        <view class="nutrient-item">
          <view class="nutrient-icon">🍚</view>
          <view class="nutrient-info">
            <view class="nutrient-name">碳水化合物</view>
            <view class="nutrient-value">{{ nutrients.carbs }}g</view>
          </view>
          <view class="nutrient-progress">
            <view class="mini-progress">
              <view class="mini-fill carbs" :style="{ width: (nutrients.carbs / nutrients.carbsTarget * 100) + '%' }"></view>
            </view>
          </view>
        </view>
        <view class="nutrient-item">
          <view class="nutrient-icon">🥩</view>
          <view class="nutrient-info">
            <view class="nutrient-name">蛋白质</view>
            <view class="nutrient-value">{{ nutrients.protein }}g</view>
          </view>
          <view class="nutrient-progress">
            <view class="mini-progress">
              <view class="mini-fill protein" :style="{ width: (nutrients.protein / nutrients.proteinTarget * 100) + '%' }"></view>
            </view>
          </view>
        </view>
        <view class="nutrient-item">
          <view class="nutrient-icon">🥑</view>
          <view class="nutrient-info">
            <view class="nutrient-name">脂肪</view>
            <view class="nutrient-value">{{ nutrients.fat }}g</view>
          </view>
          <view class="nutrient-progress">
            <view class="mini-progress">
              <view class="mini-fill fat" :style="{ width: (nutrients.fat / nutrients.fatTarget * 100) + '%' }"></view>
            </view>
          </view>
        </view>
        <view class="nutrient-item">
          <view class="nutrient-icon">🧂</view>
          <view class="nutrient-info">
            <view class="nutrient-name">钠</view>
            <view class="nutrient-value">{{ nutrients.sodium }}mg</view>
          </view>
          <view class="nutrient-progress">
            <view class="mini-progress">
              <view class="mini-fill sodium" :style="{ width: (nutrients.sodium / nutrients.sodiumTarget * 100) + '%' }"></view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 今日饮食记录 -->
    <view class="record-section">
      <view class="section-header">
        <view class="section-title">
          <text class="title-icon">📝</text>
          <text>今日饮食记录</text>
        </view>
        <view class="add-btn" @click="showAddModal = true">
          <text>+ 添加</text>
        </view>
      </view>
      <view class="meal-list">
        <view v-for="(meal, index) in mealRecords" :key="index" class="meal-item">
          <view class="meal-time">{{ meal.time }}</view>
          <view class="meal-type">{{ meal.type }}</view>
          <view class="meal-content">
            <view v-for="(food, idx) in meal.foods" :key="idx" class="food-tag">
              {{ food.name }} {{ food.amount }}
            </view>
          </view>
          <view class="meal-calories">{{ meal.calories }} 千卡</view>
        </view>
      </view>
    </view>

    <!-- 饮食建议 -->
    <view class="advice-section">
      <view class="section-title">
        <text class="title-icon">💡</text>
        <text>饮食建议</text>
      </view>
      <view class="advice-list">
        <view v-for="(advice, index) in dietAdvices" :key="index" class="advice-item">
          <view class="advice-icon">{{ advice.icon }}</view>
          <view class="advice-content">
            <view class="advice-title">{{ advice.title }}</view>
            <view class="advice-text">{{ advice.text }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 添加饮食弹窗 -->
    <view v-if="showAddModal" class="modal-overlay" @click="showAddModal = false">
      <view class="modal-content" @click.stop>
        <view class="modal-header">
          <text class="modal-title">添加饮食记录</text>
          <view class="modal-close" @click="showAddModal = false">✕</view>
        </view>
        <view class="modal-body">
          <view class="form-item">
            <text class="form-label">用餐时间</text>
            <picker mode="selector" :range="mealTypes" @change="onMealTypeChange">
              <view class="form-input">{{ selectedMealType }}</view>
            </picker>
          </view>
          <view class="form-item">
            <text class="form-label">食物名称</text>
            <input class="form-input" v-model="newFood.name" placeholder="请输入食物名称" />
          </view>
          <view class="form-item">
            <text class="form-label">食物重量</text>
            <input class="form-input" v-model="newFood.amount" placeholder="例如: 100g" />
          </view>
          <view class="form-item">
            <text class="form-label">热量(千卡)</text>
            <input class="form-input" v-model="newFood.calories" placeholder="请输入热量" type="number" />
          </view>
        </view>
        <view class="modal-footer">
          <view class="cancel-btn" @click="showAddModal = false">取消</view>
          <view class="confirm-btn" @click="addFood">确认添加</view>
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
      totalCalories: 1250,
      targetCalories: 2000,
      nutrients: {
        carbs: 156,
        carbsTarget: 250,
        protein: 68,
        proteinTarget: 100,
        fat: 42,
        fatTarget: 65,
        sodium: 1200,
        sodiumTarget: 2000
      },
      mealRecords: [
        {
          time: '08:30',
          type: '早餐',
          foods: [
            { name: '小米粥', amount: '1碗' },
            { name: '鸡蛋', amount: '1个' },
            { name: '全麦面包', amount: '2片' }
          ],
          calories: 380
        },
        {
          time: '12:00',
          type: '午餐',
          foods: [
            { name: '米饭', amount: '1碗' },
            { name: '清炒西兰花', amount: '150g' },
            { name: '红烧鱼块', amount: '100g' }
          ],
          calories: 520
        },
        {
          time: '18:30',
          type: '晚餐',
          foods: [
            { name: '杂粮粥', amount: '1碗' },
            { name: '凉拌黄瓜', amount: '100g' },
            { name: '蒸蛋', amount: '1份' }
          ],
          calories: 350
        }
      ],
      dietAdvices: [
        {
          icon: '🥗',
          title: '增加蔬菜摄入',
          text: '建议每餐蔬菜摄入量达到200g以上，有助于补充膳食纤维和维生素。'
        },
        {
          icon: '💧',
          title: '多喝水',
          text: '建议每日饮水量达到2000ml，有助于促进新陈代谢。'
        },
        {
          icon: '⏰',
          title: '规律用餐',
          text: '建议按时用餐，避免暴饮暴食，保持良好的饮食习惯。'
        }
      ],
      showAddModal: false,
      mealTypes: ['早餐', '午餐', '晚餐', '加餐'],
      selectedMealType: '早餐',
      newFood: {
        name: '',
        amount: '',
        calories: ''
      }
    }
  },
  computed: {
    progressPercent() {
      return Math.min(Math.round((this.totalCalories / this.targetCalories) * 100), 100)
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
    onMealTypeChange(e) {
      this.selectedMealType = this.mealTypes[e.detail.value]
    },
    addFood() {
      if (!this.newFood.name || !this.newFood.calories) {
        uni.showToast({
          title: '请填写完整信息',
          icon: 'none'
        })
        return
      }
      
      const now = new Date()
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      
      const newMeal = {
        time: `${hours}:${minutes}`,
        type: this.selectedMealType,
        foods: [
          {
            name: this.newFood.name,
            amount: this.newFood.amount || '适量'
          }
        ],
        calories: parseInt(this.newFood.calories)
      }
      
      this.mealRecords.unshift(newMeal)
      this.totalCalories += parseInt(this.newFood.calories)
      
      this.newFood = {
        name: '',
        amount: '',
        calories: ''
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
.diet-page {
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

/* 今日摄入概览 */
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

.calorie-ring {
  display: flex;
  align-items: center;
  gap: 40rpx;
}

.ring-container {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.ring-value {
  font-size: 48rpx;
  font-weight: 700;
  color: #fff;
}

.ring-unit {
  font-size: 20rpx;
  color: rgba(255, 255, 255, 0.8);
}

.ring-progress {
  flex: 1;
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
  margin-bottom: 12rpx;
}

.progress-fill {
  height: 100%;
  background: #fff;
  border-radius: 8rpx;
  transition: width 0.3s ease;
}

.progress-percent {
  font-size: 28rpx;
  font-weight: 600;
  color: #fff;
}

/* 营养素摄入 */
.nutrients-section {
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

.nutrients-grid {
  background: #fff;
  border-radius: 20rpx;
  padding: 20rpx;
}

.nutrient-item {
  display: flex;
  align-items: center;
  padding: 20rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.nutrient-item:last-child {
  border-bottom: none;
}

.nutrient-icon {
  font-size: 40rpx;
  margin-right: 20rpx;
}

.nutrient-info {
  flex: 1;
}

.nutrient-name {
  font-size: 28rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.nutrient-value {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.nutrient-progress {
  width: 120rpx;
}

.mini-progress {
  width: 100%;
  height: 12rpx;
  background: #f0f0f0;
  border-radius: 6rpx;
  overflow: hidden;
}

.mini-fill {
  height: 100%;
  border-radius: 6rpx;
  transition: width 0.3s ease;
}

.mini-fill.carbs {
  background: linear-gradient(90deg, #FFB74D, #FF9800);
}

.mini-fill.protein {
  background: linear-gradient(90deg, #4FC3F7, #29B6F6);
}

.mini-fill.fat {
  background: linear-gradient(90deg, #81C784, #66BB6A);
}

.mini-fill.sodium {
  background: linear-gradient(90deg, #E57373, #EF5350);
}

/* 今日饮食记录 */
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

.meal-list {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
}

.meal-item {
  padding: 24rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.meal-item:last-child {
  border-bottom: none;
}

.meal-time {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.meal-type {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 12rpx;
}

.meal-content {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  margin-bottom: 12rpx;
}

.food-tag {
  background: #f5f5f5;
  color: #666;
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.meal-calories {
  font-size: 28rpx;
  font-weight: 600;
  color: #4DD0E1;
  text-align: right;
}

/* 饮食建议 */
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
