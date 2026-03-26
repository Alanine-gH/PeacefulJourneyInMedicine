<template>
  <view class="container">
    <view class="header">
      <view class="title">信息</view>
    </view>
    <view class="content">
      <view class="info-card">
        <text class="info-title">信息页面</text>
        <text class="info-desc">这里是信息页面，您可以查看相关信息。</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {}
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
  padding-bottom: 100rpx;
}

.header {
  background-color: #2196F3;
  padding: 40rpx 30rpx 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.content {
  padding: 30rpx;
}

.info-card {
  background-color: #fff;
  border-radius: 15rpx;
  padding: 40rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  text-align: center;
}

.info-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 20rpx;
  display: block;
}

.info-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.5;
}
</style>