<template>
  <view class="container">
    <!-- 人体2D图 -->
    <view class="body-diagram">
      <view class="body-image-container">
        <image src="/static/body-before.png" class="body-image" />
        <!-- 可点击的身体部位文字 -->
        <view class="body-parts">
          <view class="body-part" @click="selectBodyPart('head')">
            <text class="body-part-text">头部</text>
          </view>
          <view class="body-part" @click="selectBodyPart('neck')">
            <text class="body-part-text">颈部</text>
          </view>
          <view class="body-part" @click="selectBodyPart('chest')">
            <text class="body-part-text">胸部</text>
          </view>
          <view class="body-part" @click="selectBodyPart('abdomen')">
            <text class="body-part-text">腹部</text>
          </view>
          <view class="body-part" @click="selectBodyPart('arm')">
            <text class="body-part-text">手臂</text>
          </view>
          <view class="body-part" @click="selectBodyPart('leg')">
            <text class="body-part-text">腿部</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 疾病风险 -->
    <view class="disease-risk" v-if="selectedPart">
      <view class="risk-header">
        <text class="risk-title">疾病风险</text>
      </view>
      <view class="risk-content">
        <view class="risk-item" v-for="(risk, index) in bodyParts[selectedPart].risks" :key="index">
          <text class="risk-number">{{ index + 1 }}</text>
          <text class="risk-text">{{ risk }}</text>
        </view>
      </view>
    </view>

    <!-- 检测项目 -->
    <view class="detection-project" v-if="selectedPart">
      <view class="project-header">
        <text class="project-title">检测项目</text>
      </view>
      <view class="project-content">
        <view class="project-table">
          <view class="table-header">
            <text class="table-cell">检测项目</text>
            <text class="table-cell">结果</text>
          </view>
          <view class="table-row" v-for="(item, index) in bodyParts[selectedPart].detections" :key="index">
            <text class="table-cell">{{ item.item }}</text>
            <text class="table-cell">{{ item.result }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 上传病历按钮 -->
    <view class="upload-btn-container" v-if="selectedPart">
      <button class="upload-btn" @click="uploadMedicalRecord">上传病历</button>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentView: 'front',
      selectedPart: null,
      bodyParts: {
        head: {
          risks: ['[头痛: 低危风险]', '[眩晕: 低危风险]'],
          detections: [
            { item: '头部CT', result: '未见明显异常' },
            { item: '血压测量', result: '正常范围' }
          ]
        },
        neck: {
          risks: ['[颈椎病: 中危风险]'],
          detections: [
            { item: '颈椎X光', result: '轻度退行性变' },
            { item: '颈部活动度', result: '轻度受限' }
          ]
        },
        chest: {
          risks: ['[支气管炎: 中危风险]', '[肺炎: 低危风险]'],
          detections: [
            { item: 'X光照射', result: '可见细微阴影' },
            { item: '肺功能', result: '轻度下降' }
          ]
        },
        abdomen: {
          risks: ['[脂肪肝: 中危风险]', '[胃炎: 低危风险]'],
          detections: [
            { item: '腹部超声', result: '轻度脂肪肝' },
            { item: '胃镜', result: '慢性浅表性胃炎' }
          ]
        },
        arm: {
          risks: ['[肩周炎: 低危风险]'],
          detections: [
            { item: '肩部X光', result: '未见明显异常' },
            { item: '肩关节活动度', result: '轻度受限' }
          ]
        },
        leg: {
          risks: ['[膝关节炎: 低危风险]'],
          detections: [
            { item: '膝关节X光', result: '轻度退行性变' },
            { item: '膝关节活动度', result: '正常' }
          ]
        },
        back: {
          risks: ['[腰椎间盘突出: 中危风险]', '[腰肌劳损: 低危风险]'],
          detections: [
            { item: '腰椎CT', result: '轻度椎间盘突出' },
            { item: '腰部活动度', result: '轻度受限' }
          ]
        }
      }
    }
  },
  methods: {
    selectBodyPart(part) {
      this.selectedPart = part
      // 预留：调用身体部位诊断接口
      // TODO: 集成身体部位诊断接口
      // uni.request({
      //   url: `http://localhost:8080/api/diagnosis/body-part/${part}`,
      //   method: 'GET',
      //   header: {
      //     'Authorization': 'Bearer ' + uni.getStorageSync('token')
      //   },
      //   success: (res) => {
      //     if (res.data.code === 200) {
      //       // 更新疾病风险和检测项目数据
      //       this.bodyParts[part] = res.data.data
      //     }
      //   }
      // })
    },
    uploadMedicalRecord() {
      uni.navigateTo({
        url: '/pages/diagnosis/upload'
      })
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
}

/* 顶部导航 */
.header {
  background-color: #8db8b6;
  padding: 40rpx 30rpx 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
}

.title {
  font-size: 32rpx;
  font-weight: 600;
  flex: 1;
  text-align: center;
  margin-left: -36rpx;
}

.placeholder {
  width: 60rpx;
}

/* 前后切换 */
.view-toggle {
  display: flex;
  background-color: #fff;
  margin: 20rpx;
  border-radius: 10rpx;
  padding: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.toggle-btn {
  flex: 1;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #666;
  border-radius: 8rpx;
  transition: all 0.3s ease;
}

.toggle-btn.active {
  background-color: #8db8b6;
  color: #fff;
  font-weight: 500;
}

/* 风险等级说明 */
.risk-levels {
  display: flex;
  justify-content: space-around;
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  padding: 20rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 人体2D图 */
.body-diagram {
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  padding: 40rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: center;
}

.body-image-container {
  position: relative;
  width: 280rpx;
  height: 560rpx;
}

.body-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.body-parts {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.body-part {
  position: absolute;
  padding: 10rpx;
  cursor: pointer;
  transition: all 0.3s ease;
}

.body-part:hover {
  background-color: rgba(77, 208, 225, 0.1);
  border-radius: 8rpx;
}

.body-part-text {
  font-size: 24rpx;
  font-weight: 500;
  color: #333;
  background-color: rgba(255, 255, 255, 0.8);
  padding: 5rpx 10rpx;
  border-radius: 15rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

/* 身体部位文字位置 */
.body-part:nth-child(1) { /* 头部 */
  top: 80rpx;
  left: 110rpx;
}

.body-part:nth-child(2) { /* 颈部 */
  top: 180rpx;
  left: 110rpx;
}

.body-part:nth-child(3) { /* 胸部 */
  top: 250rpx;
  left: 100rpx;
}

.body-part:nth-child(4) { /* 腹部 */
  top: 350rpx;
  left: 100rpx;
}

.body-part:nth-child(5) { /* 手臂 */
  top: 280rpx;
  left: 20rpx;
}

.body-part:nth-child(6) { /* 腿部 */
  top: 450rpx;
  left: 100rpx;
}

.body-svg {
  width: 280rpx;
  height: 560rpx;
}

.body-svg g {
  cursor: pointer;
  transition: all 0.3s ease;
}

.body-svg g:hover {
  opacity: 0.8;
  transform: scale(1.05);
}

/* 疾病风险 */
.disease-risk {
  background-color: #fff;
  margin: 0 20rpx 20rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.risk-header {
  background-color: #f4f2ee;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #e0e0e0;
}

.risk-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.risk-content {
  padding: 30rpx;
}

.risk-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20rpx;
}

.risk-number {
  width: 30rpx;
  height: 30rpx;
  border-radius: 50%;
  background-color: #8db8b6;
  color: #fff;
  font-size: 20rpx;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15rpx;
  flex-shrink: 0;
}

.risk-text {
  flex: 1;
  font-size: 26rpx;
  color: #333;
  line-height: 1.5;
}

/* 检测项目 */
.detection-project {
  background-color: #fff;
  margin: 0 20rpx 40rpx;
  border-radius: 10rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.project-header {
  background-color: #f4f2ee;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #e0e0e0;
}

.project-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.project-content {
  padding: 30rpx;
}

.project-table {
  width: 100%;
  border-collapse: collapse;
}

.table-header {
  display: flex;
  border-bottom: 2rpx solid #e0e0e0;
  padding-bottom: 15rpx;
  margin-bottom: 15rpx;
}

.table-row {
  display: flex;
  border-bottom: 1rpx solid #f0f0f0;
  padding: 15rpx 0;
}

.table-row:last-child {
  border-bottom: none;
}

.table-cell {
  flex: 1;
  font-size: 26rpx;
  color: #333;
}

.table-cell:first-child {
  font-weight: 500;
}

/* 上传病历按钮 */
.upload-btn-container {
  margin: 0 20rpx 40rpx;
  padding: 0 20rpx;
}

.upload-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.upload-btn::after {
  border: none;
}

.upload-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 底部空白区域 */
.bottom-space {
  height: 20vh;
  width: 100%;
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