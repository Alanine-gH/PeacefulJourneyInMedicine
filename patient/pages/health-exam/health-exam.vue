<template>
  <view class="container">
    <!-- 头部 -->
    <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">健康体检</view>
        <view class="header-right"></view>
      </view>
    </view>

    <!-- 体检套餐列表 -->
    <view class="package-section">
      <view class="section-header">
        <text class="section-title">体检套餐</text>
      </view>
      <view class="package-list">
        <view 
          v-for="(pkg, index) in examPackages" 
          :key="index"
          class="package-item"
        >
          <view class="package-image">
            <text class="image-icon">{{ pkg.icon }}</text>
          </view>
          <view class="package-info">
            <text class="package-name">{{ pkg.name }}</text>
            <text class="package-desc">{{ pkg.description }}</text>
            <text class="package-price">¥{{ pkg.price }}</text>
            <view class="package-features">
              <text 
                v-for="(feature, idx) in pkg.features" 
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

    <!-- 体检机构 -->
    <view class="hospital-section">
      <view class="section-header">
        <text class="section-title">合作机构</text>
      </view>
      <view class="hospital-list">
        <view 
          v-for="(hospital, index) in hospitals" 
          :key="index"
          class="hospital-item"
        >
          <view class="hospital-info">
            <text class="hospital-name">{{ hospital.name }}</text>
            <text class="hospital-address">{{ hospital.address }}</text>
            <text class="hospital-phone">联系电话: {{ hospital.phone }}</text>
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
      examPackages: [
        {
          id: 1,
          name: '基础体检套餐',
          description: '适合一般健康人群的基础体检',
          price: '399',
          icon: '🏥',
          features: ['血常规', '尿常规', '肝功能', '肾功能', '心电图']
        },
        {
          id: 2,
          name: '中年体检套餐',
          description: '适合40-60岁人群的全面体检',
          price: '699',
          icon: '🧪',
          features: ['基础套餐', '血脂', '血糖', '腹部B超', '胸部X光']
        },
        {
          id: 3,
          name: '老年体检套餐',
          description: '适合60岁以上人群的专项体检',
          price: '999',
          icon: '👴',
          features: ['中年套餐', '骨密度', '心脑血管检查', '肿瘤标志物', '眼底检查']
        },
        {
          id: 4,
          name: '女性体检套餐',
          description: '适合女性的专项体检',
          price: '799',
          icon: '👩',
          features: ['基础套餐', '妇科检查', '乳腺B超', '宫颈癌筛查', '激素水平']
        }
      ],
      hospitals: [
        {
          id: 1,
          name: '北京协和医院',
          address: '北京市东城区帅府园1号',
          phone: '010-69156114'
        },
        {
          id: 2,
          name: '上海瑞金医院',
          address: '上海市黄浦区瑞金二路197号',
          phone: '021-64370045'
        },
        {
          id: 3,
          name: '广州中山医院',
          address: '广州市越秀区中山二路58号',
          phone: '020-87755766'
        }
      ],
      faqs: [
        {
          question: '体检前需要注意什么？',
          answer: '体检前一天应避免剧烈运动，保持充足睡眠。体检当天需空腹，一般要求禁食8-12小时。女性应避开月经期，最好在月经结束后3-7天进行体检。',
          expanded: false
        },
        {
          question: '体检报告多久能出来？',
          answer: '一般情况下，体检报告会在3-5个工作日内出来。具体时间可能因体检项目多少和医院工作效率而有所不同。',
          expanded: false
        },
        {
          question: '体检套餐可以根据个人需求调整吗？',
          answer: '是的，我们可以根据您的具体需求和健康状况，为您定制个性化的体检套餐。您可以在预约时与我们的客服人员沟通。',
          expanded: false
        },
        {
          question: '体检结果异常怎么办？',
          answer: '如果体检结果出现异常，我们的医生会为您提供专业的解读和建议。对于严重异常的情况，我们会及时通知您并建议进一步检查或治疗。',
          expanded: false
        }
      ]
    }
  },
  methods: {
    selectPackage(pkg) {
      uni.showToast({
        title: `选择了${pkg.name}套餐`,
        icon: 'none'
      });
      // 这里可以跳转到预约页面
      // uni.navigateTo({
      //   url: `/pages/appointment/appointment?package_id=${pkg.id}`
      // });
    },
    toggleFaq(index) {
      this.faqs[index].expanded = !this.faqs[index].expanded;
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 头部 */
.header {
  background-color: #4DD0E1;
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.header-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.header-right {
  width: 60rpx;
}

/* 体检套餐 */
.package-section {
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

.package-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.package-item {
  display: flex;
  align-items: center;
  padding: 25rpx;
  background: #f8f9fa;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.package-item:hover {
  background: #e9ecef;
  transform: translateY(-2rpx);
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.package-image {
  width: 100rpx;
  height: 100rpx;
  border-radius: 15rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(77, 208, 225, 0.3);
}

.image-icon {
  font-size: 50rpx;
  color: white;
}

.package-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10rpx;
}

.package-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.package-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.package-price {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff5722;
}

.package-features {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin-top: 5rpx;
}

.feature-tag {
  background-color: #E1F5FE;
  color: #2196F3;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
  font-weight: 500;
}

/* 体检机构 */
.hospital-section {
  background: #fff;
  padding: 30rpx;
  margin: 20rpx 0;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.hospital-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.hospital-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx;
  background: #f8f9fa;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.hospital-item:hover {
  background: #e9ecef;
}

.hospital-info {
  flex: 1;
}

.hospital-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.hospital-address {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 8rpx;
  line-height: 1.4;
}

.hospital-phone {
  font-size: 24rpx;
  color: #999;
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
  .package-image {
    width: 80rpx;
    height: 80rpx;
  }
  
  .image-icon {
    font-size: 40rpx;
  }
  
  .package-name {
    font-size: 28rpx;
  }
  
  .package-desc {
    font-size: 22rpx;
  }
  
  .hospital-name {
    font-size: 28rpx;
  }
  
  .hospital-address {
    font-size: 22rpx;
  }
  
  .question-text {
    font-size: 26rpx;
  }
  
  .answer-text {
    font-size: 22rpx;
  }
}
</style>