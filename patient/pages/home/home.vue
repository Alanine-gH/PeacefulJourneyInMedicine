<template>
  <view class="container">
    <!-- 顶部欢迎横幅 -->
    <view class="hero-banner">
      <view class="hero-content">
        <text class="hero-title">欢迎使用医路安心</text>
        <text class="hero-subtitle">专业的陪诊服务，让就医更安心</text>
      </view>
      <view class="hero-decoration">
        <view class="deco-circle deco-1"></view>
        <view class="deco-circle deco-2"></view>
        <view class="deco-circle deco-3"></view>
      </view>
    </view>

    <!-- 功能图标区 -->
    <view class="feature-card">
      <view class="feature-grid">
        <view class="feature-item" @click="goToAuthentication">
          <view class="feature-icon icon-auth">
            <text class="icon-text">🛡️</text>
          </view>
          <text class="feature-text">实名认证</text>
        </view>
        <view class="feature-item" @click="navigateTo('diagnosis')">
          <view class="feature-icon icon-diagnosis">
            <text class="icon-text">🤖</text>
          </view>
          <text class="feature-text">智能诊断</text>
        </view>
        <view class="feature-item" @click="navigateTo('appointment')">
          <view class="feature-icon icon-appointment">
            <text class="icon-text">📅</text>
          </view>
          <text class="feature-text">导诊预约</text>
        </view>
        <view class="feature-item" @click="navigateTo('business')">
          <view class="feature-icon icon-business">
            <text class="icon-text">📋</text>
          </view>
          <text class="feature-text">我的业务</text>
        </view>
      </view>
    </view>

    <!-- 优惠信息轮播 -->
    <view class="section-wrapper">
      <view class="section-header">
        <view class="section-title-wrapper">
          <text class="section-icon">✨</text>
          <text class="section-title">优惠信息</text>
        </view>
      </view>
      <view class="carousel-wrapper">
        <swiper 
          class="promo-swiper" 
          indicator-dots="true" 
          autoplay="true" 
          interval="4000" 
          duration="800" 
          circular="true"
          indicator-color="rgba(255,255,255,0.4)"
          indicator-active-color="#fff"
        >
          <swiper-item v-for="(item, index) in carouselList" :key="index">
            <image class="carousel-bg" :src="item.bgImage" mode="aspectFill"></image>
            <view class="promo-card" :class="item.className" @click="goToPromotion(index)">
              <view class="promo-tag">
                <text class="tag-icon">✦</text>
                <text class="tag-text">限时优惠</text>
              </view>
              <view class="promo-price">
                <text class="price-num">{{ item.priceNum }}</text>
                <text class="price-unit">{{ item.priceUnit }}</text>
              </view>
              <view class="promo-title">{{ item.title }}</view>
              <view class="promo-subtitle">{{ item.subtitle }}</view>
            </view>
          </swiper-item>
        </swiper>
      </view>
    </view>

    <!-- 服务项目 -->
    <view class="section-wrapper">
      <view class="section-header">
        <view class="section-title-wrapper">
          <text class="section-icon">💝</text>
          <text class="section-title">服务项目</text>
        </view>
      </view>
      <view class="service-list">
        <view class="service-item" v-for="(service, index) in serviceList" :key="index" @click="navigateTo(service.page)">
          <view class="service-icon-wrapper" :class="service.iconClass">
            <text class="service-icon">{{ service.icon }}</text>
          </view>
          <view class="service-info">
            <view class="service-name">{{ service.name }}</view>
            <view class="service-desc">{{ service.desc }}</view>
            <view class="service-meta">
              <text class="service-price">{{ service.price }}</text>
              <text class="service-dot">·</text>
              <text class="service-action">{{ service.action }}</text>
            </view>
          </view>
          <view class="service-btn" :class="service.btnClass">
            <text class="btn-text">{{ service.btnText }}</text>
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
      carouselList: [
        {
          priceNum: '588',
          priceUnit: '元/次',
          title: 'VIP陪诊服务',
          subtitle: 'VIP陪诊体验，专车接送',
          btnText: '立即体验',
          className: 'vip',
          bgImage: '/static/lun1.png'
        },
        {
          priceNum: '488',
          priceUnit: '元/次',
          title: '全程陪诊',
          subtitle: '全程陪同，手续代办',
          btnText: '立即体验',
          className: 'full',
          bgImage: '/static/lun2.png'
        },
        {
          priceNum: '388',
          priceUnit: '元/次',
          title: '代办问诊',
          subtitle: '代替客户到机构咨询',
          btnText: '立即体验',
          className: 'consult',
          bgImage: '/static/lun3.png'
        },
        {
          priceNum: '288',
          priceUnit: '元/次',
          title: '取送结果',
          subtitle: '取送结果到家',
          btnText: '立即体验',
          className: 'result',
          bgImage: '/static/lun4.png'
        },
        {
          priceNum: '188',
          priceUnit: '元/次',
          title: '代办买药',
          subtitle: '人工排队代买药',
          btnText: '立即体验',
          className: 'medicine',
          bgImage: '/static/lun5.png'
        }
      ],
      serviceList: [
        {
          name: '健康体检',
          desc: '专业体检套餐',
          price: '¥399起',
          action: '立即预约',
          icon: '❤️',
          iconClass: 'green',
          btnText: '查看',
          btnClass: 'primary',
          page: 'health-exam'
        },
        {
          name: '专家咨询',
          desc: '一对一专家服务',
          price: '¥299起',
          action: '可预约',
          icon: '👤',
          iconClass: 'purple',
          btnText: '查看',
          btnClass: 'purple',
          page: 'expert-consult'
        },
        {
          name: '药品配送',
          desc: '送药上门服务',
          price: '¥19起',
          action: '立即预约',
          icon: '💊',
          iconClass: 'teal',
          btnText: '查看',
          btnClass: 'teal',
          page: 'medicine-delivery'
        },
        {
          name: '服务评价',
          desc: '为服务体验打分',
          price: '随时评价',
          action: '可评价',
          icon: '⭐',
          iconClass: 'orange',
          btnText: '评价',
          btnClass: 'orange',
          page: 'evaluation'
        }
      ]
    }
  },
  methods: {
    goToPromotion(index) {
      uni.navigateTo({
        url: `/pages/promotion/promotion?index=${index}`
      })
    },
    goToAuthentication() {
      uni.navigateTo({
        url: '/pages/authentication/step1'
      })
    },
    navigateTo(page) {
      const routes = {
        'diagnosis': '/pages/diagnosis/index',
        'appointment': '/pages/package/package',
        'business': '/pages/order/order',
        'evaluation': '/pages/order/order?status=5&pendingEval=1',
        'expert-consult': '/pages/expert-consult/expert-consult',
        'health-exam': '/pages/health-exam/health-exam',
        'medicine-delivery': '/pages/medicine-delivery/medicine-delivery'
      }
      
      if (routes[page]) {
        uni.navigateTo({ url: routes[page] })
      } else {
        uni.showToast({
          title: `跳转到${page}页面`,
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f5f3ef;
  min-height: 100vh;
  padding: 0 24rpx 40rpx;
}

/* 顶部欢迎横幅 */
.hero-banner {
  position: relative;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 50%, #c2dada 100%);
  border-radius: 30rpx;
  padding: 50rpx 40rpx;
  margin: 20rpx 0 30rpx;
  overflow: hidden;
}

.hero-content {
  position: relative;
  z-index: 2;
}

.hero-title {
  display: block;
  font-size: 42rpx;
  font-weight: 700;
  color: #fff;
  margin-bottom: 12rpx;
  text-shadow: 0 2rpx 8rpx rgba(0,0,0,0.1);
}

.hero-subtitle {
  display: block;
  font-size: 26rpx;
  color: rgba(255,255,255,0.9);
  font-weight: 400;
}

.hero-decoration {
  position: absolute;
  top: 0;
  right: 0;
  width: 200rpx;
  height: 100%;
}

.deco-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255,255,255,0.15);
}

.deco-1 {
  width: 180rpx;
  height: 180rpx;
  top: -40rpx;
  right: -40rpx;
}

.deco-2 {
  width: 120rpx;
  height: 120rpx;
  bottom: 20rpx;
  right: 60rpx;
  background: rgba(255,255,255,0.1);
}

.deco-3 {
  width: 80rpx;
  height: 80rpx;
  top: 60rpx;
  right: 100rpx;
  background: rgba(255,255,255,0.08);
}

/* 功能图标区 */
.feature-card {
  background: #fff;
  border-radius: 24rpx;
  padding: 40rpx 30rpx;
  margin-bottom: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.06);
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20rpx;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 10rpx;
  transition: transform 0.2s ease;
}

.feature-item:active {
  transform: scale(0.95);
}

.feature-icon {
  width: 100rpx;
  height: 100rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0,0,0,0.08);
}

.icon-auth {
  background: linear-gradient(135deg, #e8f4f4 0%, #d4e8e8 100%);
}

.icon-diagnosis {
  background: linear-gradient(135deg, #f0e8f4 0%, #e4d8ec 100%);
}

.icon-appointment {
  background: linear-gradient(135deg, #f4f0e8 0%, #ece4d8 100%);
}

.icon-business {
  background: linear-gradient(135deg, #f4e8e8 0%, #ecd8d8 100%);
}

.icon-text {
  font-size: 44rpx;
}

.feature-text {
  font-size: 26rpx;
  color: #3a3a4a;
  font-weight: 500;
}

/* 区块标题 */
.section-wrapper {
  margin-bottom: 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title-wrapper {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.section-icon {
  font-size: 32rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #3a3a4a;
}

/* 轮播图 */
.carousel-wrapper {
  border-radius: 24rpx;
  overflow: hidden;
}

.promo-swiper {
  height: 360rpx;
}

.carousel-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.promo-card {
  height: 100%;
  border-radius: 24rpx;
  padding: 40rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.promo-tag {
  position: absolute;
  top: 30rpx;
  left: 30rpx;
  display: flex;
  align-items: center;
  gap: 8rpx;
  background: rgba(255,255,255,0.25);
  padding: 8rpx 16rpx;
  border-radius: 20rpx;
  z-index: 2;
}

.promo-price {
  z-index: 2;
}

.promo-title {
  z-index: 2;
}

.promo-subtitle {
  z-index: 2;
}



.promo-price {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
  margin-bottom: 12rpx;
}

.price-num {
  font-size: 56rpx;
  font-weight: 700;
  color: #fff;
}

.price-unit {
  font-size: 26rpx;
  color: rgba(255,255,255,0.9);
}

.promo-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #fff;
  margin-bottom: 8rpx;
}

.promo-subtitle {
  font-size: 26rpx;
  color: rgba(255,255,255,0.85);
  margin-bottom: 30rpx;
}

/* 服务列表 */
.service-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.service-item {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 24rpx;
  padding: 30rpx;
  box-shadow: 0 4rpx 20rpx rgba(100, 120, 140, 0.06);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.service-item:active {
  transform: translateY(-2rpx);
  box-shadow: 0 8rpx 30rpx rgba(100, 120, 140, 0.1);
}

.service-icon-wrapper {
  width: 90rpx;
  height: 90rpx;
  border-radius: 22rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 24rpx;
  flex-shrink: 0;
}

.service-icon-wrapper.green {
  background: linear-gradient(135deg, #f0f4f0 0%, #e0e8e4 100%);
}

.service-icon-wrapper.purple {
  background: linear-gradient(135deg, #f4f0f4 0%, #e8e0ec 100%);
}

.service-icon-wrapper.teal {
  background: linear-gradient(135deg, #f0f4f4 0%, #e0e8e8 100%);
}

.service-icon-wrapper.orange {
  background: linear-gradient(135deg, #f4f0ec 0%, #ece4dc 100%);
}

.service-icon {
  font-size: 44rpx;
}

.service-info {
  flex: 1;
  min-width: 0;
}

.service-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #3a3a4a;
  margin-bottom: 8rpx;
}

.service-desc {
  font-size: 24rpx;
  color: #6a6a7a;
  margin-bottom: 8rpx;
}

.service-meta {
  display: flex;
  align-items: center;
  gap: 12rpx;
}

.service-price {
  font-size: 24rpx;
  color: #9a9aaa;
}

.service-dot {
  font-size: 24rpx;
  color: #c0c0cc;
}

.service-action {
  font-size: 24rpx;
  color: #9a9aaa;
}

.service-btn {
  padding: 16rpx 32rpx;
  border-radius: 28rpx;
  margin-left: 20rpx;
  flex-shrink: 0;
}

.service-btn.primary {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
}

.service-btn.purple {
  background: linear-gradient(135deg, #c0b0d8 0%, #a898c8 100%);
}

.service-btn.teal {
  background: linear-gradient(135deg, #a8c0b8 0%, #8db8a8 100%);
}

.service-btn.orange {
  background: linear-gradient(135deg, #e8c4a8 0%, #d4a88c 100%);
}

.service-btn .btn-text {
  color: #fff;
  font-size: 26rpx;
  font-weight: 500;
}
</style>
