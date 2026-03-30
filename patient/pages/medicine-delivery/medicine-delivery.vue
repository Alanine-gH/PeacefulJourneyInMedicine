<template>
  <view class="container">
    <!-- 配送服务列表 -->
    <view class="service-section">
      <view class="section-header">
        <text class="section-title">配送服务</text>
      </view>
      <view class="service-list">
        <view 
          v-for="(service, index) in deliveryServices" 
          :key="index"
          class="service-item"
        >
          <view class="service-image">
            <text class="image-icon">{{ service.icon }}</text>
          </view>
          <view class="service-info">
            <text class="service-name">{{ service.name }}</text>
            <text class="service-desc">{{ service.description }}</text>
            <text class="service-price">¥{{ service.price }}起</text>
            <view class="service-features">
              <text 
                v-for="(feature, idx) in service.features" 
                :key="idx"
                class="feature-tag"
              >
                {{ feature }}
              </text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 配送范围 -->
    <view class="area-section">
      <view class="section-header">
        <text class="section-title">配送范围</text>
      </view>
      <view class="area-list">
        <view 
          v-for="(area, index) in deliveryAreas" 
          :key="index"
          class="area-item"
        >
          <view class="area-info">
            <text class="area-name">{{ area.name }}</text>
            <text class="area-address">{{ area.address }}</text>
            <text class="area-time">配送时间: {{ area.time }}</text>
            <text class="area-fee">配送费: ¥{{ area.fee }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 常见问题 -->
    <view class="faq-section">
      <view class="section-header">
        <text class="section-title">常见问题</text>
      </view>
      <view class="faq-list">
        <view 
          v-for="(faq, index) in faqs" 
          :key="index"
          class="faq-item"
          @click="toggleFaq(index)"
        >
          <view class="faq-question">
            <text class="question-text">{{ faq.question }}</text>
            <text class="faq-icon">{{ faq.expanded ? '▼' : '▶' }}</text>
          </view>
          <view v-if="faq.expanded" class="faq-answer">
            <text class="answer-text">{{ faq.answer }}</text>
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
      deliveryServices: [
        {
          id: 1,
          name: '普通配送',
          description: '常规药品配送服务，3-5个工作日送达',
          price: '19',
          icon: '📦',
          features: ['全国配送', '冷链运输', '保险保障']
        },
        {
          id: 2,
          name: '加急配送',
          description: '紧急药品配送服务，24小时内送达',
          price: '39',
          icon: '🚚',
          features: ['同城配送', '专人配送', '实时跟踪']
        },
        {
          id: 3,
          name: '夜间配送',
          description: '夜间药品配送服务，晚上7-10点送达',
          price: '29',
          icon: '🌙',
          features: ['夜间配送', '上门服务', '安全可靠']
        },
        {
          id: 4,
          name: '冷链配送',
          description: '特殊药品冷链配送，保证药品质量',
          price: '49',
          icon: '❄️',
          features: ['全程冷链', '温度监控', '专业包装']
        }
      ],
      deliveryAreas: [
        {
          id: 1,
          name: '北京市',
          address: '北京市内所有区域',
          time: '当日达',
          fee: '10'
        },
        {
          id: 2,
          name: '上海市',
          address: '上海市内所有区域',
          time: '当日达',
          fee: '10'
        },
        {
          id: 3,
          name: '广州市',
          address: '广州市内所有区域',
          time: '当日达',
          fee: '10'
        },
        {
          id: 4,
          name: '其他城市',
          address: '全国主要城市',
          time: '3-5个工作日',
          fee: '15'
        }
      ],
      faqs: [
        {
          question: '药品配送需要什么资料？',
          answer: '药品配送需要您提供有效的处方和身份证明。我们会核实处方信息，确保药品配送的合法性和安全性。',
          expanded: false
        },
        {
          question: '配送费用如何计算？',
          answer: '配送费用根据配送距离和服务类型计算。普通配送费用较低，加急配送和特殊配送费用相对较高。',
          expanded: false
        },
        {
          question: '药品可以退换吗？',
          answer: '药品属于特殊商品，一经配送概不退换。如药品有质量问题，我们会按照相关法规进行处理。',
          expanded: false
        },
        {
          question: '配送过程中药品损坏怎么办？',
          answer: '如果配送过程中药品出现损坏，我们会立即为您补发或退款。所有配送都有保险保障，确保您的权益。',
          expanded: false
        }
      ]
    }
  },
  methods: {
    toggleFaq(index) {
      this.faqs[index].expanded = !this.faqs[index].expanded;
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 配送服务 */
.service-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-header {
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.service-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.service-item {
  display: flex;
  align-items: flex-start;
  padding: 25rpx;
  background: #f4f2ee;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.service-item:hover {
  background: #e9ecef;
  transform: translateY(-2rpx);
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.service-image {
  width: 100rpx;
  height: 100rpx;
  border-radius: 15rpx;
  background: linear-gradient(135deg, #667EEA 0%, #764BA2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(102, 126, 234, 0.3);
}

.image-icon {
  font-size: 50rpx;
  color: white;
}

.service-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.service-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.service-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.service-price {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff5722;
}

.service-features {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin-top: 5rpx;
}

.feature-tag {
  background-color: #E8EAF6;
  color: #2196F3;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
  font-weight: 500;
}

/* 配送范围 */
.area-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.area-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.area-item {
  display: flex;
  padding: 25rpx;
  background: #f4f2ee;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.area-item:hover {
  background: #e9ecef;
}

.area-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.area-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.area-address {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
  line-height: 1.4;
}

.area-time {
  font-size: 24rpx;
  color: #999;
  margin-bottom: 8rpx;
}

.area-fee {
  font-size: 24rpx;
  color: #ff5722;
  font-weight: 600;
}

/* 常见问题 */
.faq-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0 40rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.faq-list {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.faq-item {
  border-bottom: 1rpx solid #f0f0f0;
  padding-bottom: 15rpx;
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 15rpx 0;
}

.question-text {
  font-size: 28rpx;
  font-weight: 500;
  color: #333;
  flex: 1;
}

.faq-icon {
  font-size: 24rpx;
  color: #999;
  transition: transform 0.3s ease;
}

.faq-answer {
  padding: 15rpx 0;
  animation: slideDown 0.3s ease;
}

.answer-text {
  font-size: 24rpx;
  color: #666;
  line-height: 1.5;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式调整 */
@media (max-width: 375px) {
  .service-image {
    width: 80rpx;
    height: 80rpx;
  }
  
  .image-icon {
    font-size: 40rpx;
  }
  
  .service-name {
    font-size: 28rpx;
  }
  
  .service-desc {
    font-size: 22rpx;
  }
  
  .area-name {
    font-size: 28rpx;
  }
  
  .area-address {
    font-size: 22rpx;
  }
  
  .question-text {
    font-size: 26rpx;
  }
  
  .answer-text {
    font-size: 22rpx;
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

</style>