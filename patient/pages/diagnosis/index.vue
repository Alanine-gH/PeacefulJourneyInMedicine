<template>
  <view class="container">
    <!-- 顶部导航 -->
 <!--   <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">←</text>
      </view>
      <view class="title">智能诊断</view>
      <view class="placeholder"></view>
    </view>-->

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
    // goBack() {
    //   uni.navigateBack()
    // },
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
  background-color: #f8f9fa;
  min-height: 100vh;
}

/* 顶部导航 */
.header {
  background-color: #4DD0E1;
  padding: 40rpx 30rpx 20rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #fff;
}
/*
.back-btn {
  z-index: 1;
}

.back-icon {
  font-size: 36rpx;
  color: #fff;
}
*/
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
  background-color: #4DD0E1;
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
  background-color: #f5f5f5;
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
  background-color: #4DD0E1;
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
  background-color: #f5f5f5;
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
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
</style>