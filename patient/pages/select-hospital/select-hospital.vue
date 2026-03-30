<template>
  <view class="select-hospital-page">
    <!-- 城市选择栏 -->
    <view class="city-bar" @click="selectCity">
      <text class="city-text">{{ currentCity }}</text>
      <text class="city-arrow">▼</text>
    </view>
    <!-- 医院列表 -->
    <view class="hospital-list">
      <view
          class="hospital-item"
          v-for="(item, index) in hospitalList"
          :key="index"
          :class="{ active: selectedHospital === item.name }"
          @click="selectHospital(item)"
      >
        <image class="hospital-image" :src="item.image" mode="aspectFill"></image>
        <view class="hospital-info">
          <text class="hospital-name">{{ item.name }}</text>
          <view class="hospital-tags">
            <text class="tag">{{ item.level }}</text>
            <text class="tag">{{ item.type }}</text>
          </view>
          <text class="hospital-distance">距离：{{ item.distance }}</text>
        </view>
        <view class="hospital-check" v-if="selectedHospital === item.name">
          <text class="check-icon">✓</text>
        </view>
      </view>
    </view>

    <view class="bottom-section">
      <button class="btn btn-cancel" @click="goBack">取消</button>
      <button class="btn btn-confirm" @click="confirmHospital">确定</button>
    </view>
  </view>
</template>

<script>
import {getHospitals, searchHospitals} from '@/utils/medical-api.js'

export default {
  data() {
    return {
      currentCity: '北京市',
      keyword: '',
      loading: false,
      hospitalList: [],
      searchTimer: null,
      selectedHospital: '',
      selectedHospitalId: '',
      hospitalData: {
        '北京市': [
          {
            id: 1,
            name: '北京协和医院',
            level: '三级甲等',
            type: '三甲医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {
            id: 2,
            name: '北京大学第一医院',
            level: '三级甲等',
            type: '三甲医院',
            distance: '5.5公里',
            image: '/static/hospital2.png'
          }
        ],
        '上海市': [
          {
            id: 3,
            name: '上海华山医院',
            level: '三级甲等',
            type: '三甲医院',
            distance: '2.5公里',
            image: '/static/hospital1.png'
          }
        ],
        '广州市': [
          {
            id: 4,
            name: '中山大学附属第一医院',
            level: '三级甲等',
            type: '三甲医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          }
        ],
        '成都市': [
          {
            id: 5,
            name: '四川大学华西医院',
            level: '三级甲等',
            type: '三甲医院',
            distance: '3.5公里',
            image: '/static/hospital1.png'
          }
        ],
        'Rochester': [
          {
            id: 6,
            name: 'Mayo Clinic',
            level: 'International Hospital',
            type: '国际医院',
            distance: '5.5公里',
            image: '/static/hospital1.png'
          }
        ],
        'Tokyo': [
          {
            id: 7,
            name: 'Tokyo University Hospital',
            level: 'International Hospital',
            type: '国际医院',
            distance: '6.2公里',
            image: '/static/hospital1.png'
          }
        ]
      }
    }
  },
  computed: {
    hospitalList() {
      return this.hospitalData[this.currentCity] || this.hospitalData['北京市']
    }
  },
  onShow() {
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    if (currentPage.data && currentPage.data.selectedCity) {
      this.currentCity = currentPage.data.selectedCity
      // 清空已选医院，因为城市变了
      this.selectedHospital = ''
    }
  },
  methods: {
    async loadHospitals() {
      this.loading = true
      try {
        const res = await getHospitals({city: this.currentCity, page: 1, pageSize: 20})
        if (res && res.code === 200 && res.data) {
          this.hospitalList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('加载医院列表失败:', e)
        uni.showToast({title: '加载失败', icon: 'none'})
      } finally {
        this.loading = false
      }
    },
    onSearch() {
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.doSearch()
      }, 400)
    },
    async doSearch() {
      if (!this.keyword.trim()) {
        this.loadHospitals();
        return
      }
      this.loading = true
      try {
        const res = await searchHospitals({keyword: this.keyword, city: this.currentCity, page: 1, pageSize: 20})
        if (res && res.code === 200 && res.data) {
          this.hospitalList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('搜索医院失败:', e)
      } finally {
        this.loading = false
      }
    },
    goBack() {
      uni.navigateBack()
    },
    selectCity() {
      uni.navigateTo({url: '/pages/select-city/select-city'})
    },
    selectHospital(item) {
      this.selectedHospital = item.name
      this.selectedHospitalId = item.id || ''
    },
    confirmHospital() {
      if (!this.selectedHospital) {
        uni.showToast({title: '请选择医院', icon: 'none'})
        return
      }
      uni.setStorageSync('_sel_hospital', this.selectedHospital)
      uni.setStorageSync('_sel_hospitalId', this.selectedHospitalId || '')
      // 换医院时清空已选医生
      uni.setStorageSync('_sel_clearDoctor', '1')
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.select-hospital-page {
  min-height: 100vh;
  background-color: #f4f2ee;
  padding-bottom: 140rpx;
}

/* 城市选择栏 */
.city-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;
}

.city-text {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  margin-right: 10rpx;
}

.city-arrow {
  font-size: 24rpx;
  color: #999;
}

/* 医院列表 */
.hospital-list {
  padding: 20rpx;
}

.hospital-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.hospital-item.active {
  border: 2rpx solid #8db8b6;
  background-color: #E0F7FA;
}

.hospital-image {
  width: 120rpx;
  height: 120rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  background-color: #f0f0f0;
}

.hospital-info {
  flex: 1;
}

.hospital-name {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  display: block;
  margin-bottom: 12rpx;
}

.hospital-tags {
  display: flex;
  gap: 12rpx;
  margin-bottom: 12rpx;
}

.tag {
  font-size: 24rpx;
  color: #8db8b6;
  background-color: #E0F7FA;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
}

.hospital-distance {
  font-size: 26rpx;
  color: #999;
}

.hospital-check {
  width: 48rpx;
  height: 48rpx;
  border-radius: 50%;
  background-color: #8db8b6;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  font-size: 28rpx;
  color: #fff;
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
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.btn {
  flex: 1;
  height: 88rpx;
  line-height: 88rpx;
  border-radius: 44rpx;
  font-size: 32rpx;
  text-align: center;
}

.btn-cancel {
  background-color: #f4f2ee;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
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