<template>
  <view class="tab-bar">
    <view
      class="tab-item"
      :class="{ active: activeTab === 'home' }"
      @click="switchTab('home')"
    >
      <image
        class="tab-icon"
        :src="activeTab === 'home' ? '/static/home.png' : '/static/disHome.png'"
        mode="aspectFit"
      ></image>
      <text class="tab-text">首页</text>
    </view>
    <view
      class="tab-item"
      :class="{ active: activeTab === 'info' }"
      @click="switchTab('info')"
    >
      <image
        class="tab-icon"
        :src="activeTab === 'info' ? '/static/message.png' : '/static/disMessage.png'"
        mode="aspectFit"
      ></image>
      <text class="tab-text">信息</text>
    </view>
    <view
      class="tab-item"
      :class="{ active: activeTab === 'mine' }"
      @click="switchTab('mine')"
    >
      <image
        class="tab-icon"
        :src="activeTab === 'mine' ? '/static/mine.png' : '/static/disMine.png'"
        mode="aspectFit"
      ></image>
      <text class="tab-text">我的</text>
    </view>
  </view>
</template>

<script>
export default {
  name: 'CompanionTabBar',
  props: {
    activeTab: {
      type: String,
      default: 'home'
    }
  },
  methods: {
    switchTab(tab) {
      const pages = getCurrentPages()
      const currentPage = pages[pages.length - 1]
      const currentRoute = currentPage.route

      if (currentRoute.includes(`companion/${tab}`)) {
        return
      }

      if (tab === 'home') {
        uni.redirectTo({ url: '/pages/companion/home' })
      } else if (tab === 'info') {
        uni.redirectTo({ url: '/pages/companion/info' })
      } else if (tab === 'mine') {
        uni.redirectTo({ url: '/pages/companion/mine' })
      }
    }
  }
}
</script>

<style scoped>
.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-around;
  box-shadow: 0 -2rpx 15rpx rgba(0, 0, 0, 0.08);
  border-top: 1rpx solid #e8e8e8;
  z-index: 100;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  height: 100%;
  transition: all 0.3s ease;
  color: #999;
}

.tab-item.active {
  color: #4DD0E1;
}

.tab-icon {
  width: 48rpx;
  height: 48rpx;
  margin-bottom: 5rpx;
}

.tab-text {
  font-size: 24rpx;
  font-weight: 500;
}
</style>
