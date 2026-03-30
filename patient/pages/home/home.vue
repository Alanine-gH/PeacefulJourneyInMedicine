<template>
  <view class="container">
    <!-- 患者功能区 - 第一部分 -->
    <view class="patient-section">
      <view class="patient-grid">
        <view class="patient-item" @click="goToAuthentication">
          <view class="patient-icon">
            <text>📝</text>
          </view>
          <text class="patient-text">实名认证</text>
        </view>
        <view class="patient-item" @click="navigateTo('diagnosis')">
          <view class="patient-icon">
            <text>🤖</text>
          </view>
          <text class="patient-text">智能诊断</text>
        </view>
        <view class="patient-item" @click="navigateTo('appointment')">
          <view class="patient-icon">
            <text>📅</text>
          </view>
          <text class="patient-text">导诊预约</text>
        </view>
        <view class="patient-item" @click="navigateTo('business')">
          <view class="patient-icon">
            <text>📊</text>
          </view>
          <text class="patient-text">我的业务</text>
        </view>
      </view>
    </view>

    <!-- 轮播图 - 第二部分 -->
    <view class="carousel-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">🎉</text> 优惠信息
        </text>
      </view>
      <view class="swiper-container">
        <swiper indicator-dots="true" autoplay="true" interval="3000" duration="1000" circular="true">
          <swiper-item v-for="(item, index) in carouselList" :key="index">
            <view class="carousel-item" :class="item.className" @click="goToPromotion(index)">
              <view class="carousel-content">
                <view class="carousel-price">{{ item.price }}</view>
                <view class="carousel-title">{{ item.title }}</view>
                <view class="carousel-subtitle">{{ item.subtitle }}</view>
                <view class="carousel-btn">{{ item.btnText }}</view>
                <view v-if="item.icon" class="carousel-image">
                  <text>{{ item.icon }}</text>
                </view>
                <view v-if="item.tag" class="carousel-tag">{{ item.tag }}</view>
                <view v-if="item.heart" class="carousel-heart">{{ item.heart }}</view>
              </view>
            </view>
          </swiper-item>
        </swiper>
      </view>
    </view>

    <!-- 业务 - 第三部分 -->
    <view class="location-section">
      <view class="section-header">
        <text class="section-title">
          <text class="icon">📊</text> 业务
        </text>
      </view>
      <view class="location-list">
        <view class="location-item" @click="navigateTo('health-exam')">
          <view class="location-info">
            <view class="location-name">健康体检</view>
            <view class="location-address">专业体检套餐</view>
            <view class="location-distance">价格: ¥399起</view>
            <view class="location-phone">预约: 立即预约</view>
          </view>
          <view class="location-action">
            <text class="action-btn">查看</text>
          </view>
        </view>
        <view class="location-item" @click="navigateTo('expert-consult')">
          <view class="location-info">
            <view class="location-name">专家咨询</view>
            <view class="location-address">一对一专家服务</view>
            <view class="location-distance">价格: ¥299起</view>
            <view class="location-phone">预约: 立即预约</view>
          </view>
          <view class="location-action">
            <text class="action-btn">查看</text>
          </view>
        </view>
        <view class="location-item" @click="navigateTo('medicine-delivery')">
          <view class="location-info">
            <view class="location-name">药品配送</view>
            <view class="location-address">送药上门服务</view>
            <view class="location-distance">价格: ¥19起</view>
            <view class="location-phone">预约: 立即预约</view>
          </view>
          <view class="location-action">
            <text class="action-btn">查看</text>
          </view>
        </view>
        <view class="location-item" @click="navigateTo('evaluation')">
          <view class="location-info">
            <view class="location-name">服务评价</view>
            <view class="location-address">为服务体验打分</view>
            <view class="location-distance">评价: 随时评价</view>
            <view class="location-phone">状态: 可评价</view>
          </view>
          <view class="location-action">
            <text class="action-btn">评价</text>
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
          price: '588元/次',
          title: 'VIP陪诊服务',
          subtitle: 'VIP陪诊体验，专车接送',
          btnText: '立即体验',
          tag: 'VIP',
          className: 'vip'
        },
        {
          price: '488元/次',
          title: '全程陪诊',
          subtitle: '全程陪同，手续代办',
          btnText: '立即体验',
          className: 'full'
        },
        {
          price: '388元/次',
          title: '代办问诊',
          subtitle: '代替客户到机构咨询',
          btnText: '立即体验',
          className: 'consult'
        },
        {
          price: '288元/次',
          title: '取送结果',
          subtitle: '取送结果到家',
          btnText: '立即体验',
          className: 'result'
        },
        {
          price: '188元/次',
          title: '代办买药',
          subtitle: '人工排队代买药',
          btnText: '立即体验',
          className: 'medicine'
        }
      ]
    }
  },
  methods: {
    openCustomerService() {
      uni.navigateTo({
        url: '/pages/customer/customer'
      })
    },
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
      // 预留：调用相关业务接口
      // TODO: 集成各业务模块接口
      // switch(page) {
      //   case 'appointment':
      //     // 调用预约挂号接口
      //     break;
      //   case 'business':
      //     // 调用我的业务接口
      //     break;
      //   case 'service1':
      //   case 'service2':
      //   case 'service3':
      //     // 调用服务详情接口
      //     break;
      // }
      
      if (page === 'diagnosis') {
        uni.navigateTo({
          url: '/pages/diagnosis/index'
        })
      } else if (page === 'appointment') {
        uni.navigateTo({
          url: '/pages/package/package'
        })
      } else if (page === 'business') {
        uni.navigateTo({
          url: '/pages/order/order'
        })
      } else if (page === 'evaluation') {
        uni.navigateTo({
          url: '/pages/order/order?status=5',
        })
      } else if (page === 'expert-consult') {
        uni.navigateTo({
          url: '/pages/expert-consult/expert-consult',
        })
      }else if (page === 'health-exam') {
        uni.navigateTo({
          url: '/pages/health-exam/health-exam',
        })
      }else if (page === 'medicine-delivery') {
        uni.navigateTo({
          url: '/pages/medicine-delivery/medicine-delivery',
        })
      }else {
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
  background-color: #f4f2ee;
  min-height: 100vh;
}

.customer-service {
  width: 60rpx;
  height: 60rpx;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.customer-service:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: scale(1.05);
}

.service-icon {
  font-size: 32rpx;
  color: #fff;
}

/* 患者功能区 - 第一部分 */
.patient-section {
  background: #fff;
  padding: 40rpx 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.patient-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30rpx;
}

.patient-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30rpx 20rpx;
  background: #f4f2ee;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.patient-item:hover {
  transform: translateY(-5rpx);
  box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.1);
}

.patient-icon {
  width: 70rpx;
  height: 70rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 35rpx;
  margin-bottom: 15rpx;
  box-shadow: 0 3rpx 10rpx rgba(100, 181, 246, 0.3);
}

.patient-text {
  font-size: 26rpx;
  color: #333;
  text-align: center;
  font-weight: 500;
}

/* 轮播图 - 第二部分 */
.carousel-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.section-title .icon {
  font-size: 36rpx;
  margin-right: 10rpx;
}

.swiper-container {
  width: 100%;
  height: 300rpx;
  border-radius: 20rpx;
  overflow: hidden;
}

.swiper-container swiper {
  width: 100%;
  height: 100%;
}

.swiper-container swiper-item {
  width: 100%;
  height: 100%;
}

.carousel-item {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 20rpx;
  position: relative;
  overflow: hidden;
  min-height: 200rpx;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 0 40rpx;
}

.carousel-item.vip {
  background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
}

.carousel-item.full {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
}

.carousel-item.consult {
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
}

.carousel-item.result {
  background: linear-gradient(135deg, #E91E63 0%, #C2185B 100%);
}

.carousel-item.medicine {
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
}

.carousel-content {
  color: #fff;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  flex: 1;
  position: relative;
  padding: 40rpx 0;
}

.carousel-price {
  font-size: 36rpx;
  font-weight: 700;
  margin-bottom: 8rpx;
}

.carousel-title {
  font-size: 40rpx;
  font-weight: 700;
  margin-bottom: 4rpx;
}

.carousel-subtitle {
  font-size: 28rpx;
  opacity: 0.9;
  margin-bottom: 20rpx;
}

.carousel-btn {
  background: rgba(255, 255, 255, 0.2);
  padding: 12rpx 36rpx;
  border-radius: 25rpx;
  font-size: 24rpx;
  font-weight: 600;
  align-self: flex-start;
}

.carousel-image {
  position: absolute;
  bottom: 20rpx;
  right: -20rpx;
  font-size: 80rpx;
  opacity: 0.8;
}

.carousel-tag {
  position: absolute;
  top: -10rpx;
  right: -10rpx;
  background: rgba(255, 255, 255, 0.2);
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  font-size: 24rpx;
  font-weight: 600;
}

.carousel-heart {
  position: absolute;
  right: 30rpx;
  bottom: 40rpx;
  font-size: 40rpx;
  opacity: 0.8;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

/* 地点信息 - 第三部分 */
.location-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.location-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.location-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx;
  background: #f4f2ee;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.location-item:hover {
  background: #e9ecef;
}

.location-info {
  flex: 1;
}

.location-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.location-address {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
}

.location-distance {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.location-phone {
  font-size: 24rpx;
  color: #999;
}

.location-action {
  margin-left: 20rpx;
}

.action-btn {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  padding: 15rpx 30rpx;
  border-radius: 20rpx;
  font-size: 26rpx;
  font-weight: 500;
  box-shadow: 0 3rpx 10rpx rgba(77, 208, 225, 0.3);
}

/* 响应式调整 */
@media (max-width: 375px) {
  .patient-grid {
    gap: 20rpx;
  }
  
  .patient-icon {
    width: 60rpx;
    height: 60rpx;
    font-size: 30rpx;
  }
  
  .carousel-item {
    min-height: 180rpx;
  }
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


/* ── home page specifics ── */
.container { padding: 0 !important; }
.patient-section {
  margin: 16rpx 24rpx !important;
  padding: 36rpx 24rpx !important;
}
.patient-section::before {
  content: '欢迎使用医路安心';
  display: block;
  font-size: 36rpx;
  font-weight: 700;
  color: #3a3a4a;
  margin-bottom: 4rpx;
}
.patient-section::after {
  content: '专业的陪诊服务，让就医更安心';
  display: block;
  font-size: 24rpx;
  color: #9a9aaa;
  margin-bottom: 28rpx;
}
.patient-item { background: #f0f4f4 !important; border-radius: 16rpx !important; }
.patient-item:active { transform: scale(0.97); }
.carousel-section { margin: 0 24rpx 16rpx !important; padding: 24rpx !important; }
.location-section { margin: 0 24rpx 24rpx !important; padding: 24rpx !important; }
.swiper-container { border-radius: 16rpx !important; overflow: hidden; }
.carousel-btn { background: rgba(255,255,255,0.28) !important; border-radius: 30rpx !important; }
.location-item { background: #f4f2ee !important; border-radius: 14rpx !important; }
.location-name  { color: #3a3a4a !important; font-weight: 600 !important; }
.location-address { color: #6a6a7a !important; }
.location-distance,.location-phone { color: #9a9aaa !important; }

</style>