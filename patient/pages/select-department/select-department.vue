<template>
  <view class="select-department-page">
    <!-- 医院信息 -->
    <view class="hospital-info-bar">
      <!-- <image class="hospital-icon" src="/static/hospital1.png" mode="aspectFill"></image> -->
      <text class="hospital-name">{{ hospitalName }}</text>
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
          <text class="category-text">{{ category.name }}</text>
        </view>
      </scroll-view>

      <!-- 右侧科室详情 -->
      <scroll-view class="department-list" scroll-y="true">
        <view class="department-section">
          <text class="section-title">
            {{ departmentCategories[selectedCategory] && departmentCategories[selectedCategory].name }}
          </text>
          <view class="department-grid">
            <view
                class="department-item"
                v-for="(dept, index) in currentDepartments"
                :key="index"
                @click="selectDepartment(dept)"
            >
              <text class="department-name">{{ dept.name }}</text>
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
      hospitalId: '',
      departmentCategories: [
        {
          id: 1,
          name: '内科',
          departments: [
            {id: 35, name: '呼吸内科'},
            {id: 10, name: '消化内科'},
            {id: 9, name: '心血管内科'},
            {id: 7, name: '神经内科'},
            {id: 36, name: '内分泌科'},
            {id: 37, name: '肾内科'},
            {id: 38, name: '血液内科'},
            {id: 39, name: '风湿免疫科'}
          ]
        },
        {
          id: 2,
          name: '外科',
          departments: [
            {id: 48, name: '普外科'},
            {id: 6, name: '骨科'},
            {id: 49, name: '神经外科'},
            {id: 50, name: '心胸外科'},
            {id: 51, name: '泌尿外科'},
            {id: 52, name: '整形外科'}
          ]
        },
        {
          id: 3,
          name: '妇产科',
          departments: [
            {id: 44, name: '妇科'},
            {id: 45, name: '产科'},
            {id: 46, name: '生殖医学科'}
          ]
        },
        {
          id: 4,
          name: '儿科',
          departments: [
            {id: 40, name: '小儿内科'},
            {id: 41, name: '小儿外科'},
            {id: 42, name: '新生儿科'},
            {id: 43, name: '儿童保健科'}
          ]
        },
        {
          id: 23,
          name: '五官科',
          departments: [
            {id: 5, name: '眼科'},
            {id: 33, name: '耳鼻喉科'},
            {id: 34, name: '口腔科'}
          ]
        },
        {
          id: 22,
          name: '中医科',
          departments: [
            {id: 29, name: '中医内科'},
            {id: 30, name: '中医外科'},
            {id: 31, name: '针灸推拿科'},
            {id: 32, name: '中医骨伤科'}
          ]
        },
        {
          id: 21,
          name: '其他科室',
          departments: [
            {id: 24, name: '皮肤科'},
            {id: 8, name: '肿瘤科'},
            {id: 25, name: '康复医学科'},
            {id: 26, name: '急诊科'},
            {id: 27, name: '体检中心'},
            {id: 28, name: '放射科'}
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
      this.hospitalId = decodeURIComponent(options.hospitalId)
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
      uni.navigateTo({
        url: '/pages/doctor-list/doctor-list?hospital=' + encodeURIComponent(this.hospitalName) + '&hospitalId=' + encodeURIComponent(this.hospitalId) + '&department=' + encodeURIComponent(dept.name) + '&departmentId=' + dept.id
      })
    }
  }
}
</script>

<style lang="scss">
.select-department-page {
  min-height: 100vh;
  background-color: #f4f2ee;
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