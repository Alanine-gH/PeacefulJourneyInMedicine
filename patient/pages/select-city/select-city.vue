<template>
  <view class="select-city-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <text class="nav-title">切换城市</text>
      <view class="placeholder"></view>
    </view>

    <!-- 搜索框 -->
    <view class="search-box">
      <input class="search-input" type="text" placeholder="搜索城市名称" v-model="searchKeyword"/>
      <text class="search-icon">🔍</text>
    </view>

    <!-- 搜索结果 -->
    <view class="search-results" v-if="isSearching">
      <view class="result-section" v-if="searchResults.length > 0">
        <view
            class="result-item"
            v-for="(item, index) in searchResults"
            :key="index"
            @click="selectSearchResult(item)"
        >
          <view class="result-info">
            <text class="result-name" v-if="item.type === 'province'">{{ item.name }}</text>
            <text class="result-name" v-else>{{ item.name }}</text>
            <text class="result-sub" v-if="item.type === 'city'">{{ item.province }}</text>
            <text class="result-sub" v-if="item.type === 'province'">{{ item.cities.join('、') }}</text>
          </view>
          <text class="result-type">{{ item.type === 'province' ? '省份' : '城市' }}</text>
        </view>
      </view>
      <view class="no-result" v-else>
        <text class="no-result-text">未找到相关城市</text>
      </view>
    </view>

    <!-- 城市列表 -->
    <view class="city-content" v-else>
      <!-- 左侧省份 -->
      <view class="province-list">
        <view
            class="province-item"
            v-for="(province, index) in provinceList"
            :key="index"
            :class="{ active: selectedProvince === province }"
            @click="selectProvince(province)"
        >
          <text class="province-text">{{ province }}</text>
        </view>
      </view>

      <!-- 右侧城市 -->
      <view class="city-list">
        <view class="city-section">
          <text class="section-title">{{ selectedProvince }}</text>
          <view
              class="city-item"
              v-for="(city, index) in cityList"
              :key="index"
              :class="{ active: selectedCity === city }"
              @click="selectCity(city)"
          >
            <text class="city-name">{{ city }}</text>
            <text class="location-icon" v-if="city === currentLocation">📍自动定位</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '',
      selectedProvince: '热门城市',
      selectedCity: '',
      currentLocation: '北京市',
      isSearching: false,
      searchResults: [],
      provinceList: ['热门城市', '北京', '上海', '广东', '四川', '国际'],
      cityData: {
        '热门城市': ['北京市', '上海市', '广州市', '成都市', 'Rochester', 'Tokyo'],
        '北京': ['北京市'],
        '上海': ['上海市'],
        '广东': ['广州市'],
        '四川': ['成都市'],
        '国际': ['Rochester', 'Tokyo']
      }
    }
  },
  computed: {
    cityList() {
      return this.cityData[this.selectedProvince] || []
    }
  },
  watch: {
    searchKeyword(newVal) {
      if (newVal.trim()) {
        this.performSearch(newVal.trim())
      } else {
        this.isSearching = false
        this.searchResults = []
      }
    }
  },
  methods: {
    performSearch(keyword) {
      this.isSearching = true
      this.searchResults = []

      // 搜索省份和城市
      for (let province in this.cityData) {
        // 如果省份名匹配
        if (province.includes(keyword)) {
          this.searchResults.push({
            type: 'province',
            name: province,
            cities: this.cityData[province]
          })
        }

        // 搜索城市
        this.cityData[province].forEach(city => {
          if (city.includes(keyword)) {
            this.searchResults.push({
              type: 'city',
              name: city,
              province: province
            })
          }
        })
      }
    },
    selectProvince(province) {
      this.selectedProvince = province
      this.searchKeyword = ''
      this.isSearching = false
    },
    selectCity(city) {
      this.selectedCity = city
      this.searchKeyword = ''
      this.isSearching = false
      // 返回医院选择页面并传递选中的城市
      const pages = getCurrentPages()
      const prevPage = pages[pages.length - 2]
      if (prevPage) {
        prevPage.setData({
          selectedCity: city
        })
      }
      uni.navigateBack()
    },
    selectSearchResult(item) {
      if (item.type === 'province') {
        this.selectProvince(item.name)
      } else {
        // 先切换到对应省份
        this.selectedProvince = item.province
        // 再选择城市
        this.selectCity(item.name)
      }
    }
  }
}
</script>

<style lang="scss">
.select-city-page {
  min-height: 100vh;
  background-color: #f4f2ee;
}

/* 搜索框 */
.search-box {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;
  position: relative;
}

.search-input {
  flex: 1;
  height: 70rpx;
  background-color: #f4f2ee;
  border-radius: 35rpx;
  padding: 0 70rpx 0 30rpx;
  font-size: 28rpx;
}

.search-icon {
  position: absolute;
  right: 50rpx;
  font-size: 28rpx;
}

/* 城市内容 */
.city-content {
  display: flex;
  height: calc(100vh - 260rpx);
}

/* 左侧省份 */
.province-list {
  width: 200rpx;
  background-color: #f8f8f8;
  overflow-y: auto;
}

.province-item {
  padding: 30rpx 20rpx;
  text-align: center;
}

.province-item.active {
  background-color: #fff;
}

.province-item.active .province-text {
  color: #2196F3;
  font-weight: 500;
}

.province-text {
  font-size: 28rpx;
  color: #666;
}

/* 右侧城市 */
.city-list {
  flex: 1;
  background-color: #fff;
  overflow-y: auto;
  padding: 20rpx;
}

.city-section {
  margin-bottom: 30rpx;
}

.section-title {
  font-size: 28rpx;
  color: #999;
  margin-bottom: 20rpx;
  display: block;
}

.city-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 25rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.city-item.active {
  background-color: #e3f2fd;
}

.city-name {
  font-size: 30rpx;
  color: #333;
}

.location-icon {
  font-size: 24rpx;
  color: #2196F3;
}

/* 搜索结果 */
.search-results {
  background-color: #fff;
  min-height: calc(100vh - 260rpx);
}

.result-section {
  padding: 0 30rpx;
}

.result-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.result-info {
  flex: 1;
}

.result-name {
  font-size: 30rpx;
  color: #333;
  display: block;
}

.result-sub {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

.result-type {
  font-size: 24rpx;
  color: #2196F3;
  background-color: #e3f2fd;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.no-result {
  display: flex;
  align-items: center;
  justify-content: center;
  padding-top: 200rpx;
}

.no-result-text {
  font-size: 28rpx;
  color: #999;
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