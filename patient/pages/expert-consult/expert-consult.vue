<template>
  <view class="container">
    <!-- 专家列表 -->
    <view class="expert-section">
      <view class="section-header">
        <text class="section-title">专家团队</text>
      </view>
      <view class="expert-list">
        <view v-if="loading" class="loading-tip"><text>加载中...</text></view>
        <view v-else-if="experts.length === 0" class="loading-tip"><text>暂无专家数据</text></view>
        <view
          v-for="(expert, index) in experts"
          :key="expert.id || index"
          class="expert-item"
        >
          <view class="expert-image">
            <image v-if="expert.photoUrl" :src="expert.photoUrl" class="expert-photo" mode="aspectFill"/>
            <text v-else class="image-icon">{{ expert.gender === 2 ? '👩‍⚕️' : '👨‍⚕️' }}</text>
          </view>
          <view class="expert-info">
            <text class="expert-name">{{ expert.expertName || '-' }}</text>
            <text class="expert-title">{{ expert.professionalTitle || '-' }}</text>
            <text class="expert-hospital">{{ expert.hospitalName || '-' }}{{ expert.departmentName ? ' · ' + expert.departmentName : '' }}</text>
            <text class="expert-desc">{{ expert.specialty || '-' }}</text>
            <text class="expert-price" v-if="expert.appointmentFee">¥{{ expert.appointmentFee }}/次</text>
            <view class="expert-specialties" v-if="expert.languageAbility">
              <text class="specialty-tag">{{ expert.languageAbility }}</text>
            </view>
            <view class="expert-specialties" v-if="expert.experienceYears">
              <text class="specialty-tag">从业{{ expert.experienceYears }}年</text>
              <text class="specialty-tag" v-if="expert.isAcceptInternational === 1">接受国际患者</text>
              <text class="specialty-tag" v-if="expert.isRecommend === 1">平台推荐</text>
            </view>
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
import { request, getFileUrl } from '../../utils/config.js';

export default {
  data() {
    return {
      experts: [],
      loading: false,
      faqs: [
        {
          question: '专家咨询如何进行？',
          answer: '您可以通过我们的平台选择合适的专家，预约咨询时间。咨询可以通过视频、电话或图文方式进行，方便快捷。',
          expanded: false
        },
        {
          question: '咨询费用如何计算？',
          answer: '专家咨询费用按次计算，不同级别的专家价格不同。您可以根据需求选择合适的专家，费用透明公开。',
          expanded: false
        },
        {
          question: '咨询结果可以作为诊断依据吗？',
          answer: '专家咨询可以为您提供专业的医疗建议，但不能替代医院的正式诊断。如需确诊和治疗，建议前往医院就诊。',
          expanded: false
        },
        {
          question: '可以更换专家吗？',
          answer: '可以的，如果您对当前专家不满意，可以申请更换其他专家。我们会根据您的需求为您推荐合适的专家。',
          expanded: false
        }
      ]
    }
  },
  onLoad() {
    this.loadExperts()
  },
  methods: {
    async loadExperts() {
      this.loading = true
      try {
        const res = await request('/medical/expert/list', {
          method: 'GET',
          data: { status: 1, pageNum: 1, pageSize: 100 }
        })
        const d = (res && res.data) ? res.data : res
        const list = d.records || d.list || (Array.isArray(d) ? d : [])
        // 把 photoUrl 转为完整可访问 URL
        this.experts = list.map(e => ({
          ...e,
          photoUrl: getFileUrl(e.photoUrl)
        }))
      } catch (e) {
        uni.showToast({ title: '加载专家列表失败', icon: 'none' })
      } finally {
        this.loading = false
      }
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

/* 专家列表 */
.expert-section {
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

.expert-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.expert-item {
  display: flex;
  align-items: flex-start;
  padding: 25rpx;
  background: #f8f9fa;
  border-radius: 15rpx;
  transition: all 0.3s ease;
}

.expert-item:hover {
  background: #e9ecef;
  transform: translateY(-2rpx);
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.08);
}

.expert-image {
  width: 100rpx;
  height: 100rpx;
  border-radius: 15rpx;
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(156, 39, 176, 0.3);
}

.image-icon {
  font-size: 50rpx;
  color: white;
}

.expert-photo {
  width: 100rpx;
  height: 100rpx;
  border-radius: 15rpx;
  object-fit: cover;
}

.loading-tip {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 26rpx;
}

.expert-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.expert-name {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.expert-title {
  font-size: 26rpx;
  color: #666;
  font-weight: 500;
}

.expert-hospital {
  font-size: 24rpx;
  color: #999;
}

.expert-desc {
  font-size: 24rpx;
  color: #666;
  line-height: 1.4;
}

.expert-price {
  font-size: 32rpx;
  font-weight: 600;
  color: #ff5722;
}

.expert-specialties {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin-top: 5rpx;
}

.specialty-tag {
  background-color: #F3E5F5;
  color: #9C27B0;
  padding: 5rpx 15rpx;
  border-radius: 15rpx;
  font-size: 20rpx;
  font-weight: 500;
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
  .expert-image {
    width: 80rpx;
    height: 80rpx;
  }
  
  .image-icon {
    font-size: 40rpx;
  }
  
  .expert-name {
    font-size: 28rpx;
  }
  
  .expert-desc {
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