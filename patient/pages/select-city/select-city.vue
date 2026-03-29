<template>
  <view class="select-city-page">
    <!-- 导航栏 -->
    <!-- <view class="nav-bar"> -->
<!--      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>-->
      <!-- <text class="nav-title">切换城市</text>
      <view class="placeholder"></view>
    </view> -->

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
      provinceList: ['热门城市', '北京', '上海', '广东', '湖北', '湖南', '江西', '重庆', '山西', '山东', '江苏', '浙江'],
      cityData: {
        '热门城市': ['全国', '北京市', '武汉市', '广州市', '上海市', '南昌市', '长沙市', '西安市', '青岛市', '深圳市'],
        '北京': ['北京市'],
        '上海': ['上海市'],
        '广东': ['广州市', '深圳市', '珠海市', '佛山市', '东莞市'],
        '湖北': ['武汉市', '宜昌市', '襄阳市', '荆州市'],
        '湖南': ['长沙市', '株洲市', '湘潭市', '衡阳市'],
        '江西': ['南昌市', '九江市', '赣州市', '上饶市'],
        '重庆': ['重庆市', '万州区', '涪陵区', '江津区', '合川区', '永川区', '南川区', '綦江区', '大足区', '璧山区', '铜梁区', '潼南区', '荣昌区', '开州区', '梁平区', '武隆区'],
        '山西': ['太原市', '大同市', '运城市', '临汾市'],
        '山东': ['济南市', '青岛市', '烟台市', '潍坊市', '临沂市'],
        '江苏': ['南京市', '苏州市', '无锡市', '常州市', '徐州市'],
        '浙江': ['杭州市', '宁波市', '温州市', '嘉兴市', '绍兴市']
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
    // goBack() {
    //   uni.navigateBack()
    // },
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
  background-color: #f5f5f5;
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
  background-color: #f5f5f5;
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
</style>
