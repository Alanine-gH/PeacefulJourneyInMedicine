<template>
  <view class="container">
    <!-- 头部封面 -->
    <view class="cover-area">
      <image
          v-if="pkg.coverImageUrl"
          :src="pkg.coverImageUrl"
          class="cover-img"
          mode="aspectFill"
      />
      <view v-else class="cover-placeholder">
        <text class="cover-ph-icon">🏥</text>
      </view>
      <!-- 返回按钮 -->
      <!--      <view class="back-btn" @click="goBack">-->
      <!--        <text class="back-icon">‹</text>-->
      <!--      </view>-->
    </view>

    <!-- 加载中 -->
    <view v-if="loading" class="loading-wrap">
      <text class="loading-text">加载中...</text>
    </view>

    <block v-else-if="pkg.id">
      <!-- 基本信息卡 -->
      <view class="card">
        <view class="name-row">
          <text class="pkg-name">{{ pkg.packageName }}</text>
          <view class="type-tag" :class="typeClass">
            <text class="type-text">{{ typeLabel }}</text>
          </view>
        </view>
        <text v-if="pkg.packageNameEn" class="pkg-name-en">{{ pkg.packageNameEn }}</text>

        <!-- 价格 -->
        <view class="price-row">
          <text class="price-symbol">¥</text>
          <text class="price-val">{{ pkg.price }}</text>
          <text class="price-unit">/ {{ pkg.unit || '次' }}</text>
          <text v-if="pkg.currency && pkg.currency !== 'CNY'" class="currency-badge">{{ pkg.currency }}</text>
        </view>

        <!-- 描述 -->
        <text v-if="pkg.description" class="pkg-desc">{{ pkg.description }}</text>

        <!-- 基础属性网格 -->
        <view class="meta-grid">
          <view class="meta-cell">
            <text class="meta-label">套餐编码</text>
            <text class="meta-val">{{ pkg.packageCode || '-' }}</text>
          </view>
          <view class="meta-cell">
            <text class="meta-label">服务类型</text>
            <text class="meta-val">{{ serviceTypeLabel }}</text>
          </view>
          <view class="meta-cell">
            <text class="meta-label">服务时长</text>
            <text class="meta-val">{{ pkg.duration ? pkg.duration + ' 小时' : '-' }}</text>
          </view>
          <view class="meta-cell">
            <text class="meta-label">有效期</text>
            <text class="meta-val">{{ pkg.validDays ? pkg.validDays + ' 天' : '-' }}</text>
          </view>
          <view class="meta-cell">
            <text class="meta-label">最大次数</text>
            <text class="meta-val">{{ pkg.maxUsageCount === -1 ? '不限' : (pkg.maxUsageCount || '-') }}</text>
          </view>
          <view class="meta-cell">
            <text class="meta-label">计价单位</text>
            <text class="meta-val">{{ pkg.unit || '-' }}</text>
          </view>
        </view>
      </view>

      <!-- 包含项目 -->
      <view v-if="pkg.includesItems" class="card">
        <view class="section-title-row">
          <view class="title-bar"></view>
          <text class="section-title">包含项目</text>
        </view>
        <view class="tag-list">
          <view
              v-for="(item, i) in includesList"
              :key="i"
              class="include-tag"
          >
            <text class="tag-check">✓</text>
            <text class="tag-text">{{ item }}</text>
          </view>
        </view>
      </view>

      <!-- 不含项目 -->
      <view v-if="pkg.excludeItems" class="card">
        <view class="section-title-row">
          <view class="title-bar exclude"></view>
          <text class="section-title">不包含项目</text>
        </view>
        <view class="tag-list">
          <view
              v-for="(item, i) in excludesList"
              :key="i"
              class="exclude-tag"
          >
            <text class="tag-x">✕</text>
            <text class="tag-text">{{ item }}</text>
          </view>
        </view>
      </view>

      <!-- 服务内容 -->
      <view v-if="pkg.serviceContent" class="card">
        <view class="section-title-row">
          <view class="title-bar"></view>
          <text class="section-title">服务内容</text>
        </view>
        <text class="content-text">{{ serviceContentText }}</text>
      </view>

      <!-- 使用须知 -->
      <view v-if="pkg.usageNotice" class="card notice-card">
        <view class="section-title-row">
          <view class="title-bar notice"></view>
          <text class="section-title">使用须知</text>
        </view>
        <text class="notice-text">{{ pkg.usageNotice }}</text>
      </view>

      <!-- 备注 -->
      <view v-if="pkg.remark" class="card">
        <view class="section-title-row">
          <view class="title-bar remark"></view>
          <text class="section-title">备注</text>
        </view>
        <text class="content-text">{{ pkg.remark }}</text>
      </view>
    </block>

    <!-- 数据异常 -->
    <view v-else-if="!loading" class="empty-wrap">
      <text class="empty-text">套餐信息不存在</text>
      <view class="back-text-btn" @click="goBack">
        <text class="back-text">返回列表</text>
      </view>
    </view>

    <!-- 底部占位，防止内容被底栏遮挡 -->
    <view class="bottom-safe"></view>
  </view>
</template>

<script>
import { getHealthExamPackageDetail } from '@/utils/health-exam-api.js';

const PKG_TYPE_MAP = {1: '基础套餐', 2: '增值套餐', 3: '定制套餐'};
const SVC_TYPE_MAP = {1: '全程陪诊', 2: '代办手续', 3: '接机服务', 4: '送机服务', 5: '翻译服务'};

export default {
  data() {
    return {
      packageId: null,
      pkg: {},
      loading: false
    };
  },
  computed: {
    typeLabel() {
      return PKG_TYPE_MAP[this.pkg.packageType] || '套餐';
    },
    typeClass() {
      const map = {1: 'tag-basic', 2: 'tag-value', 3: 'tag-custom'};
      return map[this.pkg.packageType] || 'tag-basic';
    },
    serviceTypeLabel() {
      return SVC_TYPE_MAP[this.pkg.serviceType] || '-';
    },
    includesList() {
      if (!this.pkg.includesItems) return [];
      return this.pkg.includesItems.split(/[,，、\n]+/).map(s => s.trim()).filter(Boolean);
    },
    excludesList() {
      if (!this.pkg.excludeItems) return [];
      return this.pkg.excludeItems.split(/[,，、\n]+/).map(s => s.trim()).filter(Boolean);
    },
    serviceContentText() {
      if (!this.pkg.serviceContent) return '';
      // 尝试解析 JSON，否则直接展示字符串
      try {
        const parsed = JSON.parse(this.pkg.serviceContent);
        if (Array.isArray(parsed)) return parsed.join('\n');
        if (typeof parsed === 'object') return Object.values(parsed).join('\n');
        return String(parsed);
      } catch {
        return this.pkg.serviceContent;
      }
    }
  },
  onLoad(options) {
    this.packageId = options.id;
    if (this.packageId) {
      this.loadDetail();
    } else {
      uni.showToast({title: '参数错误', icon: 'none'});
    }
  },
  methods: {
    async loadDetail() {
      this.loading = true;
      try {
        const res = await getHealthExamPackageDetail(this.packageId);
        if (res && res.code === 200 && res.data) {
          this.pkg = res.data;
        } else {
          uni.showToast({title: (res && res.msg) || '获取详情失败', icon: 'none'});
        }
      } catch (e) {
        console.error('获取套餐详情失败', e);
        uni.showToast({title: '网络错误，请稍后重试', icon: 'none'});
      } finally {
        this.loading = false;
      }
    },
    goBack() {
      uni.navigateBack({delta: 1});
    }
  }
};
</script>

<style scoped>
.container {
  background: #f4f6f9;
  min-height: 100vh;
}

/* 封面 */
.cover-area {
  position: relative;
  width: 100%;
  height: 420rpx;
  background: linear-gradient(135deg, #26C6DA 0%, #4DD0E1 100%);
  overflow: hidden;
}

.cover-img {
  width: 100%;
  height: 100%;
}

.cover-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #26C6DA 0%, #80DEEA 100%);
}

.cover-ph-icon {
  font-size: 120rpx;
}

/* 返回按钮*/
/*
.back-btn {
  position: absolute;
  top: 40rpx;
  left: 24rpx;
  width: 72rpx;
  height: 72rpx;
  background: rgba(0, 0, 0, 0.28);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 52rpx;
  color: #fff;
  line-height: 1;
  margin-top: -4rpx;
}
*/
/* 卡片 */
.card {
  background: #fff;
  border-radius: 20rpx;
  margin: 20rpx 20rpx 0;
  padding: 30rpx;
  box-shadow: 0 2rpx 16rpx rgba(0, 0, 0, 0.05);
}

/* 名称行 */
.name-row {
  display: flex;
  align-items: center;
  gap: 14rpx;
  flex-wrap: wrap;
}

.pkg-name {
  font-size: 36rpx;
  font-weight: 800;
  color: #1a1a2e;
  letter-spacing: 1rpx;
}

.type-tag {
  border-radius: 10rpx;
  padding: 6rpx 18rpx;
}

.tag-basic {
  background: #e3f2fd;
}

.tag-value {
  background: #fce4ec;
}

.tag-custom {
  background: #f3e5f5;
}

.type-text {
  font-size: 22rpx;
  font-weight: 700;
}

.tag-basic .type-text {
  color: #1565c0;
}

.tag-value .type-text {
  color: #ad1457;
}

.tag-custom .type-text {
  color: #6a1b9a;
}

.pkg-name-en {
  font-size: 24rpx;
  color: #aaa;
  margin-top: 8rpx;
  letter-spacing: 1rpx;
}

/* 价格行 */
.price-row {
  display: flex;
  align-items: baseline;
  gap: 6rpx;
  margin: 20rpx 0 16rpx;
}

.price-symbol {
  font-size: 26rpx;
  color: #e53935;
  font-weight: 700;
}

.price-val {
  font-size: 52rpx;
  font-weight: 800;
  color: #e53935;
  line-height: 1;
}

.price-unit {
  font-size: 24rpx;
  color: #aaa;
}

.currency-badge {
  background: #fff3e0;
  color: #e65100;
  font-size: 20rpx;
  font-weight: 700;
  border-radius: 8rpx;
  padding: 4rpx 12rpx;
  margin-left: 8rpx;
}

/* 描述 */
.pkg-desc {
  font-size: 26rpx;
  color: #666;
  line-height: 1.7;
  margin-bottom: 20rpx;
}

/* 属性网格 */
.meta-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0;
  border: 1rpx solid #f0f0f0;
  border-radius: 14rpx;
  overflow: hidden;
  margin-top: 10rpx;
}

.meta-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 10rpx;
  border-right: 1rpx solid #f0f0f0;
  border-bottom: 1rpx solid #f0f0f0;
}

.meta-cell:nth-child(3n) {
  border-right: none;
}

.meta-cell:nth-last-child(-n+3) {
  border-bottom: none;
}

.meta-label {
  font-size: 20rpx;
  color: #bbb;
  margin-bottom: 6rpx;
}

.meta-val {
  font-size: 26rpx;
  font-weight: 700;
  color: #333;
  text-align: center;
}

/* 分区标题 */
.section-title-row {
  display: flex;
  align-items: center;
  gap: 14rpx;
  margin-bottom: 22rpx;
}

.title-bar {
  width: 8rpx;
  height: 32rpx;
  border-radius: 4rpx;
  background: linear-gradient(180deg, #26C6DA 0%, #4DD0E1 100%);
}

.title-bar.exclude {
  background: linear-gradient(180deg, #ef9a9a 0%, #e57373 100%);
}

.title-bar.notice {
  background: linear-gradient(180deg, #ffcc80 0%, #ffa726 100%);
}

.title-bar.remark {
  background: linear-gradient(180deg, #ce93d8 0%, #ab47bc 100%);
}

.section-title {
  font-size: 30rpx;
  font-weight: 700;
  color: #222;
}

/* 标签列表 */
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.include-tag,
.exclude-tag {
  display: flex;
  align-items: center;
  gap: 8rpx;
  border-radius: 30rpx;
  padding: 10rpx 20rpx;
}

.include-tag {
  background: #e8f5e9;
}

.exclude-tag {
  background: #fce4ec;
}

.tag-check {
  font-size: 22rpx;
  color: #43a047;
  font-weight: 700;
}

.tag-x {
  font-size: 22rpx;
  color: #e53935;
  font-weight: 700;
}

.tag-text {
  font-size: 24rpx;
  color: #444;
}

/* 服务内容 / 备注文本 */
.content-text {
  font-size: 26rpx;
  color: #555;
  line-height: 1.8;
  white-space: pre-wrap;
}

/* 使用须知 */
.notice-card {
  background: #fffde7;
}

.notice-text {
  font-size: 26rpx;
  color: #795548;
  line-height: 1.8;
  white-space: pre-wrap;
}

/* 空态 */
.loading-wrap,
.empty-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 0;
  gap: 30rpx;
}

.loading-text,
.empty-text {
  font-size: 28rpx;
  color: #bbb;
}

.back-text-btn {
  background: linear-gradient(135deg, #26C6DA 0%, #4DD0E1 100%);
  border-radius: 40rpx;
  padding: 18rpx 60rpx;
}

.back-text {
  font-size: 28rpx;
  color: #fff;
  font-weight: 600;
}

/* 底部安全距离 */
.bottom-safe {
  height: 60rpx;
}
</style>
