<template>
  <view class="doctor-detail-page">
    <!-- 医生基本信息卡片 -->
    <view class="doctor-header-card">
      <image class="doctor-avatar-large" :src="doctor.avatar" mode="aspectFill"></image>
      <view class="doctor-basic-info">
        <view class="name-row">
          <text class="doctor-name-large">{{ doctor.name }}</text>
          <text class="doctor-gender-large" v-if="doctor.gender === '男'">♂</text>
          <text class="doctor-gender-large female" v-else>♀</text>
        </view>
        <text class="doctor-title-large">{{ doctor.title }}</text>
        <view class="doctor-tags-large">
          <text class="tag-large" v-for="(tag, index) in doctor.tags" :key="index">{{ tag }}</text>
        </view>
      </view>
    </view>
    <!-- 医生详细信息 -->
    <view class="info-sections">
      <!-- 基本信息 -->
      <view class="info-section">
        <view class="section-header">
          <text class="section-icon">👤</text>
          <text class="section-title-text">基本信息</text>
        </view>
        <view class="info-list">
          <view class="info-item">
            <text class="info-label">年龄</text>
            <text class="info-value">{{ doctor.age }}岁</text>
          </view>
          <view class="info-item">
            <text class="info-label">性别</text>
            <text class="info-value">{{ doctor.gender }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">从业年限</text>
            <text class="info-value">{{ doctor.experience }}年</text>
          </view>
          <view class="info-item">
            <text class="info-label">毕业院校</text>
            <text class="info-value">{{ doctor.university }}</text>
          </view>
        </view>
      </view>
      <!-- 执业信息 -->
      <view class="info-section">
        <view class="section-header">
          <text class="section-icon">🏥</text>
          <text class="section-title-text">执业信息</text>
        </view>
        <view class="info-list">
          <view class="info-item">
            <text class="info-label">所属医院</text>
            <text class="info-value">{{ doctor.hospital }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">所属科室</text>
            <text class="info-value">{{ doctor.department }}</text>
          </view>
          <view class="info-item">
            <text class="info-label">职称</text>
            <text class="info-value">{{ doctor.title }}</text>
          </view>
        </view>
      </view>
      <!-- 联系方式 -->
      <view class="info-section">
        <view class="section-header">
          <text class="section-icon">📞</text>
          <text class="section-title-text">联系方式</text>
        </view>
        <view class="info-list">
          <view class="info-item">
            <text class="info-label">联系电话</text>
            <text class="info-value">{{ doctor.phone }}</text>
          </view>
        </view>
      </view>

      <!-- 医生简介 -->
      <view class="info-section">
        <view class="section-header">
          <text class="section-icon">📝</text>
          <text class="section-title-text">医生简介</text>
        </view>
        <view class="intro-content">
          <text class="intro-text">{{ doctor.introduction || '该医生暂无简介' }}</text>
        </view>
      </view>

      <!-- 擅长领域 -->
      <view class="info-section">
        <view class="section-header">
          <text class="section-icon">⭐</text>
          <text class="section-title-text">擅长领域</text>
        </view>
        <view class="specialty-tags">
          <text class="specialty-tag" v-for="(tag, index) in doctor.tags" :key="index">{{ tag }}</text>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <view class="doctor-stats-bar">
        <view class="stat-box">
          <text class="stat-number">{{ doctor.consultCount }}</text>
          <text class="stat-label">问诊量</text>
        </view>
        <view class="stat-box">
          <text class="stat-number">{{ doctor.rating }}%</text>
          <text class="stat-label">好评率</text>
        </view>
      </view>
      <button class="book-btn-large" @click="bookDoctor">立即预约</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      doctor: {
        id: '',
        name: '',
        title: '',
        gender: '',
        avatar: '',
        hospital: '',
        department: '',
        tags: [],
        consultCount: 0,
        rating: 0,
        experience: 0,
        age: 0,
        university: '',
        phone: '',
        introduction: ''
      },
      doctorsData: {
        '1': {
          id: '1',
          name: '王医生',
          title: '主任医师',
          gender: '男',
          avatar: '/static/doctor1.png',
          hospital: '北京市第一医院',
          department: '呼吸内科',
          tags: ['擅长哮喘', '慢阻肺', '肺炎'],
          consultCount: 12580,
          rating: 98,
          experience: 25,
          age: 52,
          university: '北京大学医学部',
          phone: '138****1234',
          introduction: '王医生从事呼吸内科临床工作25年，在哮喘、慢阻肺、肺炎等呼吸系统疾病的诊治方面具有丰富的临床经验。曾主持多项国家级科研项目，发表SCI论文20余篇。'
        },
        '2': {
          id: '2',
          name: '李医生',
          title: '副主任医师',
          gender: '女',
          avatar: '/static/doctor2.png',
          hospital: '北京市第一医院',
          department: '呼吸内科',
          tags: ['擅长肺癌', '肺结核', '支气管扩张'],
          consultCount: 8920,
          rating: 96,
          experience: 18,
          age: 45,
          university: '北京协和医学院',
          phone: '139****5678',
          introduction: '李医生专注于呼吸系统肿瘤及感染性疾病的诊治，在肺癌早期诊断和个体化治疗方面有深入研究。'
        },
        '3': {
          id: '3',
          name: '张医生',
          title: '主任医师',
          gender: '男',
          avatar: '/static/doctor3.png',
          hospital: '北京市第一医院',
          department: '呼吸内科',
          tags: ['擅长睡眠呼吸障碍', '肺栓塞', '间质性肺病'],
          consultCount: 15600,
          rating: 99,
          experience: 30,
          age: 58,
          university: '首都医科大学',
          phone: '137****9012',
          introduction: '张医生是呼吸内科资深专家，在睡眠呼吸障碍、肺栓塞等疑难杂症的诊治方面造诣深厚。'
        },
        '4': {
          id: '4',
          name: '刘医生',
          title: '主治医师',
          gender: '女',
          avatar: '/static/doctor1.png',
          hospital: '北京市第一医院',
          department: '呼吸内科',
          tags: ['擅长呼吸道感染', '咳嗽', '胸闷'],
          consultCount: 5680,
          rating: 94,
          experience: 8,
          age: 35,
          university: '复旦大学上海医学院',
          phone: '136****3456',
          introduction: '刘医生擅长常见呼吸系统疾病的诊治，对待患者耐心细致，深受患者好评。'
        },
        '5': {
          id: '5',
          name: '陈医生',
          title: '副主任医师',
          gender: '男',
          avatar: '/static/doctor2.png',
          hospital: '北京市第一医院',
          department: '呼吸内科',
          tags: ['擅长肺结节', '胸腔积液', '呼吸衰竭'],
          consultCount: 7890,
          rating: 97,
          experience: 15,
          age: 42,
          university: '中山大学中山医学院',
          phone: '135****7890',
          introduction: '陈医生在呼吸系统危重症救治方面有丰富经验，擅长肺结节的早期诊断和治疗。'
        }
      }
    }
  },
  onLoad(options) {
    if (options.id) {
      const doctorData = this.doctorsData[options.id]
      if (doctorData) {
        this.doctor = doctorData
      }
    }
  },
  methods: {
    bookDoctor() {
      // 返回发布需求页面并传递选中的医生
      const pages = getCurrentPages()
      // 找到发布需求页面（在页面栈中往前找）
      let targetPage = null
      for (let i = pages.length - 1; i >= 0; i--) {
        if (pages[i].route === 'pages/publish-demand/publish-demand') {
          targetPage = pages[i]
          break
        }
      }
      if (targetPage) {
        targetPage.setData({
          doctor: this.doctor.name,
          hospital: this.doctor.hospital
        })
      }
      // 返回到发布需求页面
      uni.navigateBack({
        delta: 3 // 返回三层（跳过医生列表和科室选择页面）
      })
    }
  }
}
</script>

<style lang="scss">
.doctor-detail-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 200rpx;
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

/* 医生头部卡片 */
.doctor-header-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  padding: 40rpx 30rpx;
}

.doctor-avatar-large {
  width: 160rpx;
  height: 160rpx;
  border-radius: 50%;
  border: 4rpx solid #fff;
  margin-right: 30rpx;
  background-color: #f0f0f0;
}

.doctor-basic-info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.doctor-name-large {
  font-size: 40rpx;
  color: #fff;
  font-weight: 500;
  margin-right: 20rpx;
}

.doctor-gender-large {
  font-size: 32rpx;
  color: #fff;
}

.doctor-gender-large.female {
  color: #ffcdd2;
}

.doctor-title-large {
  font-size: 28rpx;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 15rpx;
  display: block;
}

.doctor-tags-large {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
}

.tag-large {
  font-size: 22rpx;
  color: #fff;
  background-color: rgba(255, 255, 255, 0.2);
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
}

/* 信息区块 */
.info-sections {
  padding: 20rpx;
}

.info-section {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 25rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.section-icon {
  font-size: 32rpx;
  margin-right: 15rpx;
}

.section-title-text {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-size: 28rpx;
  color: #666;
}

.info-value {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.intro-content {
  padding: 10rpx 0;
}

.intro-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.specialty-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.specialty-tag {
  font-size: 26rpx;
  color: #2196F3;
  background-color: #e3f2fd;
  padding: 10rpx 24rpx;
  border-radius: 30rpx;
}

/* 底部区域 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.doctor-stats-bar {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.stat-box {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-number {
  font-size: 36rpx;
  color: #2196F3;
  font-weight: 600;
  margin-bottom: 5rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
}

.book-btn-large {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.book-btn-large::after {
  border: none;
}
</style>
