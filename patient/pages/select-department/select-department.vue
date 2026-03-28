<template>
  <view class="select-department-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
      <text class="nav-title">选择科室</text>
      <view class="placeholder"></view>
    </view>

    <!-- 医院信息 -->
    <view class="hospital-info-bar">
      <image class="hospital-icon" src="/static/hospital1.png" mode="aspectFill"></image>
      <text class="hospital-name">{{hospitalName}}</text>
    </view>

    <!-- 科室列表 -->
    <view class="department-content">
      <!-- 左侧科室分类 -->
      <scroll-view class="category-list" scroll-y="true">
        <view 
          class="category-item" 
          v-for="(category, index) in departmentCategories" 
          :key="index"
          :class="{ active: selectedCategory === index }"
          @click="selectCategory(index)"
        >
          <text class="category-text">{{category.name}}</text>
        </view>
      </scroll-view>

      <!-- 右侧科室详情 -->
      <scroll-view class="department-list" scroll-y="true">
        <view class="department-section">
          <text class="section-title">{{departmentCategories[selectedCategory] && departmentCategories[selectedCategory].name}}</text>
          <view class="department-grid">
            <view 
              class="department-item" 
              v-for="(dept, index) in currentDepartments" 
              :key="index"
              @click="selectDepartment(dept)"
            >
              <text class="department-name">{{dept.name}}</text>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      hospitalName: '',
      selectedCategory: 0,
      departmentCategories: [
        {
          name: '内科',
          departments: [
            { name: '呼吸内科' },
            { name: '消化内科' },
            { name: '心血管内科' },
            { name: '神经内科' },
            { name: '内分泌科' },
            { name: '肾内科' },
            { name: '血液内科' },
            { name: '风湿免疫科' }
          ]
        },
        {
          name: '外科',
          departments: [
            { name: '普外科' },
            { name: '骨科' },
            { name: '神经外科' },
            { name: '心胸外科' },
            { name: '泌尿外科' },
            { name: '整形外科' }
          ]
        },
        {
          name: '妇产科',
          departments: [
            { name: '妇科' },
            { name: '产科' },
            { name: '生殖医学科' },
            { name: '计划生育科' }
          ]
        },
        {
          name: '儿科',
          departments: [
            { name: '小儿内科' },
            { name: '小儿外科' },
            { name: '新生儿科' },
            { name: '儿童保健科' }
          ]
        },
        {
          name: '五官科',
          departments: [
            { name: '眼科' },
            { name: '耳鼻喉科' },
            { name: '口腔科' }
          ]
        },
        {
          name: '中医科',
          departments: [
            { name: '中医内科' },
            { name: '中医外科' },
            { name: '针灸推拿科' },
            { name: '中医骨伤科' }
          ]
        },
        {
          name: '其他科室',
          departments: [
            { name: '皮肤科' },
            { name: '肿瘤科' },
            { name: '康复医学科' },
            { name: '急诊科' },
            { name: '体检中心' },
            { name: '放射科' }
          ]
        }
      ]
    }
  },
  computed: {
    currentDepartments() {
      const cat = this.departmentCategories[this.selectedCategory]
      return cat ? cat.departments : []
    }
  },
  onLoad(options) {
    if (options.hospital) {
      this.hospitalName = decodeURIComponent(options.hospital)
    }
  },
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
    selectCategory(index) {
      this.selectedCategory = index
    },
    selectDepartment(dept) {
      uni.setStorageSync('selectedDepartment', dept.name)
      uni.navigateTo({
        url: '/pages/doctor-list/doctor-list?hospital=' + encodeURIComponent(this.hospitalName) + '&department=' + encodeURIComponent(dept.name)
      })
    }
  }
}
</script>

<style lang="scss">
.select-department-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

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
  font-size: 40rpx;
  color: #fff;
}
*/
.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

.hospital-info-bar {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;
}

.hospital-icon {
  width: 60rpx;
  height: 60rpx;
  border-radius: 8rpx;
  margin-right: 15rpx;
  background-color: #f0f0f0;
}

.hospital-name {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.department-content {
  display: flex;
  height: calc(100vh - 200rpx);
}

.category-list {
  width: 200rpx;
  background-color: #f8f8f8;
}

.category-item {
  padding: 30rpx 20rpx;
  text-align: center;
  border-left: 4rpx solid transparent;
}

.category-item.active {
  background-color: #fff;
  border-left-color: #2196F3;
}

.category-item.active .category-text {
  color: #2196F3;
  font-weight: 500;
}

.category-text {
  font-size: 28rpx;
  color: #666;
}

.department-list {
  flex: 1;
  background-color: #fff;
  padding: 20rpx;
}

.department-section {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 20rpx;
  display: block;
}

.department-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.department-item {
  width: calc(33.33% - 14rpx);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30rpx 10rpx;
  background-color: #f8f8f8;
  border-radius: 12rpx;
}

.department-name {
  font-size: 26rpx;
  color: #333;
  text-align: center;
}
</style>
