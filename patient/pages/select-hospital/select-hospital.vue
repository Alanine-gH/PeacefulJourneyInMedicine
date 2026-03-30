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
  background-color: #f5f5f5;
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
  border: 2rpx solid #4DD0E1;
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
  color: #4DD0E1;
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
  background-color: #4DD0E1;
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
  background-color: #f5f5f5;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
}
</style>
