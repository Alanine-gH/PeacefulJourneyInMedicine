<template>
	<view class="container">
		<!-- 头部 -->
		<view class="header">
			<view class="back-btn" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<view class="header-content">
				<view class="header-left"></view>
				<view class="header-title">健康体检</view>
				<view class="header-right"></view>
			</view>
			<!-- 搜索栏 -->
			<view class="search-bar">
				<text class="search-icon">🔍</text>
				<input class="search-input" v-model="keyword" placeholder="搜索套餐名称" @input="onKeywordInput"
					@confirm="onSearch" />
				<text v-if="keyword" class="clear-icon" @click="clearSearch">✕</text>
			</view>
		</view>

		<!-- 套餐类型筛选 -->
		<view class="filter-bar">
			<view v-for="tab in tabs" :key="tab.value" class="filter-tab" :class="{ active: activeTab === tab.value }"
				@click="switchTab(tab.value)">
				<text>{{ tab.label }}</text>
			</view>
		</view>

		<!-- 加载中 -->
		<view v-if="loading" class="loading-wrap">
			<text class="loading-text">加载中...</text>
		</view>

		<!-- 体检套餐列表 -->
		<view v-else class="package-section">
			<view v-if="packages.length === 0" class="empty-wrap">
				<text class="empty-text">{{ allPackages.length === 0 ? '暂无套餐数据' : '无匹配的套餐' }}</text>
			</view>
			<view v-for="pkg in packages" :key="pkg.id" class="package-item" @click="goDetail(pkg.id)">
				<!-- 封面图 -->
				<view class="pkg-cover">
					<image src="/static/help.png" class="cover-img" mode="aspectFill" />
				</view>

				<!-- 套餐信息 -->
				<view class="pkg-info">
					<view class="pkg-name-row">
						<text class="pkg-name">{{ pkg.packageName }}</text>
						<view class="pkg-type-tag" :class="typeClass(pkg.packageType)">
							<text class="type-text">{{ typeLabel(pkg.packageType) }}</text>
						</view>
					</view>
					<text v-if="pkg.packageNameEn" class="pkg-name-en">{{ pkg.packageNameEn }}</text>
					<text class="pkg-desc">{{ pkg.description || '暂无描述' }}</text>

					<view class="pkg-meta-row">
						<view class="meta-item">
							<text class="meta-label">服务时长</text>
							<text class="meta-val">{{ pkg.duration ? pkg.duration + 'h' : '-' }}</text>
						</view>
						<view class="meta-item">
							<text class="meta-label">单位</text>
							<text class="meta-val">{{ pkg.unit || '-' }}</text>
						</view>
						<view class="meta-item">
							<text class="meta-label">有效期</text>
							<text class="meta-val">{{ pkg.validDays ? pkg.validDays + '天' : '-' }}</text>
						</view>
					</view>

					<view class="pkg-bottom">
						<view class="pkg-price-wrap">
							<text class="price-symbol">¥</text>
							<text class="pkg-price">{{ pkg.price }}</text>
							<text class="price-unit">/ {{ pkg.unit || '次' }}</text>
						</view>
						<view class="detail-btn">
							<text class="detail-btn-text">查看详情 ›</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 加载更多 -->
			<view v-if="hasMore" class="load-more" @click="loadMore">
				<text class="load-more-text">点击加载更多</text>
			</view>
			<view v-else-if="allPackages.length > 0" class="no-more">
				<text class="no-more-text">— 已加载全部 —</text>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		getHealthExamPackages
	} from '@/utils/health-exam-api.js';

	export default {
		data() {
			return {
				keyword: '',
				activeTab: 0, // 0=全部 1=基础 2=增值 3=定制
				tabs: [{
						label: '全部',
						value: 0
					},
					{
						label: '基础套餐',
						value: 1
					},
					{
						label: '增值套餐',
						value: 2
					},
					{
						label: '定制套餐',
						value: 3
					}
				],
				allPackages: [], // 从后端拉取的全量数据（当页）
				page: 1,
				pageSize: 50, // 一次多拉，前端做筛选
				total: 0,
				loading: false,
				hasMore: false
			};
		},
		computed: {
			// 前端根据 activeTab + keyword 过滤
			packages() {
				let list = this.allPackages;
				if (this.activeTab !== 0) {
					list = list.filter(p => p.packageType === this.activeTab);
				}
				const kw = this.keyword.trim().toLowerCase();
				if (kw) {
					list = list.filter(p =>
						(p.packageName && p.packageName.toLowerCase().includes(kw)) ||
						(p.description && p.description.toLowerCase().includes(kw))
					);
				}
				return list;
			}
		},
		onLoad() {
			this.fetchPackages();
		},
		methods: {
			goBack() {
				const pages = getCurrentPages()
				if (pages.length > 1) {
					uni.navigateBack()
				} else {
					uni.switchTab({ url: '/pages/home/home' })
				}
			},
			async fetchPackages() {
				if (this.loading) return;
				this.loading = true;
				try {
					const params = {
						page: this.page,
						pageSize: this.pageSize
					};
					const res = await getHealthExamPackages(params);
					if (res && res.code === 200 && res.data) {
						const data = res.data;
						const records = data.records || [];
						this.total = data.total || 0;
						this.allPackages = [...this.allPackages, ...records];
						this.hasMore = this.allPackages.length < this.total;
					} else {
						uni.showToast({
							title: (res && res.msg) || '获取套餐失败',
							icon: 'none'
						});
					}
				} catch (e) {
					console.error('获取套餐列表失败', e);
					uni.showToast({
						title: '网络错误，请稍后重试',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},

			switchTab(val) {
				this.activeTab = val;
				// 如果还有更多数据未加载，继续拉取
				if (this.hasMore) {
					this.page += 1;
					this.fetchPackages();
				}
			},

			onKeywordInput(e) {
				this.keyword = e.detail.value;
			},

			onSearch() {
				// keyword 已通过 v-model 双向绑定，computed 自动过滤
			},

			clearSearch() {
				this.keyword = '';
			},

			loadMore() {
				if (!this.hasMore || this.loading) return;
				this.page += 1;
				this.fetchPackages();
			},

			goDetail(id) {
				uni.navigateTo({
					url: `/pages/health-exam/health-exam-detail?id=${id}`
				});
			},

			typeLabel(type) {
				const map = {
					1: '基础',
					2: '增值',
					3: '定制'
				};
				return map[type] || '套餐';
			},

			typeClass(type) {
				const map = {
					1: 'tag-basic',
					2: 'tag-value',
					3: 'tag-custom'
				};
				return map[type] || 'tag-basic';
			}
		}
	};
</script>

<style scoped>
	.container {
		background-color: #f4f6f9;
		min-height: 100vh;
	}

	.back-btn {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.back-arrow {
		font-size: 40rpx;
		color: #fff;
	}

	/* 头部 */
	.header {
		background: linear-gradient(135deg, #a8cece 0%, #8db8b6 100%);
		padding: 40rpx 30rpx 24rpx;
	}

	.header-content {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.header-left,
	.header-right {
		width: 60rpx;
	}

	.header-title {
		font-size: 34rpx;
		font-weight: 700;
		color: #fff;
		letter-spacing: 2rpx;
	}

	/* 搜索栏 */
	.search-bar {
		margin-top: 20rpx;
		background: rgba(255, 255, 255, 0.25);
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		padding: 14rpx 24rpx;
		gap: 12rpx;
	}

	.search-icon {
		font-size: 28rpx;
		color: #fff;
	}

	.search-input {
		flex: 1;
		font-size: 26rpx;
		color: #fff;
		background: transparent;
		border: none;
	}

	.search-input::placeholder {
		color: rgba(255, 255, 255, 0.7);
	}

	.clear-icon {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
		padding: 4rpx;
	}

	/* 筛选条 */
	.filter-bar {
		display: flex;
		background: #fff;
		padding: 0 20rpx;
		border-bottom: 1rpx solid #eef0f3;
		overflow-x: auto;
	}

	.filter-tab {
		flex-shrink: 0;
		padding: 24rpx 28rpx;
		font-size: 26rpx;
		color: #888;
		position: relative;
	}

	.filter-tab.active {
		color: #a8cece;
		font-weight: 700;
	}

	.filter-tab.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 40rpx;
		height: 4rpx;
		background: #a8cece;
		border-radius: 2rpx;
	}

	/* 加载 */
	.loading-wrap,
	.empty-wrap {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 80rpx 0;
	}

	.loading-text,
	.empty-text {
		font-size: 28rpx;
		color: #bbb;
	}

	/* 套餐列表 */
	.package-section {
		padding: 20rpx;
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.package-item {
		background: #fff;
		border-radius: 20rpx;
		overflow: hidden;
		display: flex;
		box-shadow: 0 2rpx 16rpx rgba(0, 0, 0, 0.06);
		active-opacity: 0.85;
	}

	.package-item:active {
		opacity: 0.85;
	}

	/* 封面 */
	.pkg-cover {
		width: 160rpx;
		flex-shrink: 0;
		background: #f0f8ff;
	}

	.cover-img {
		width: 160rpx;
		height: 100%;
		min-height: 200rpx;
	}

	.cover-placeholder {
		width: 160rpx;
		height: 100%;
		min-height: 200rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background: linear-gradient(135deg, #e0f7fa 0%, #b2ebf2 100%);
	}

	.cover-icon {
		font-size: 60rpx;
	}

	/* 套餐信息 */
	.pkg-info {
		flex: 1;
		padding: 22rpx 24rpx;
		display: flex;
		flex-direction: column;
		gap: 10rpx;
		min-width: 0;
	}

	.pkg-name-row {
		display: flex;
		align-items: center;
		gap: 12rpx;
		flex-wrap: wrap;
	}

	.pkg-name {
		font-size: 30rpx;
		font-weight: 700;
		color: #222;
	}

	.pkg-type-tag {
		border-radius: 8rpx;
		padding: 4rpx 14rpx;
	}

	.type-text {
		font-size: 20rpx;
		font-weight: 600;
	}

	.tag-basic .type-text {
		color: #1976d2;
	}

	.tag-value .type-text {
		color: #c2185b;
	}

	.tag-custom .type-text {
		color: #7b1fa2;
	}

	.pkg-name-en {
		font-size: 22rpx;
		color: #aaa;
		letter-spacing: 1rpx;
	}

	.pkg-desc {
		font-size: 24rpx;
		color: #777;
		line-height: 1.5;
		/* 最多2行 */
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}

	/* 元信息 */
	.pkg-meta-row {
		display: flex;
		gap: 20rpx;
		margin-top: 4rpx;
	}

	.meta-item {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.meta-label {
		font-size: 20rpx;
		color: #bbb;
	}

	.meta-val {
		font-size: 24rpx;
		color: #444;
		font-weight: 600;
		margin-top: 2rpx;
	}

	/* 底部价格行 */
	.pkg-bottom {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 8rpx;
	}

	.pkg-price-wrap {
		display: flex;
		align-items: baseline;
		gap: 4rpx;
	}

	.price-symbol {
		font-size: 24rpx;
		color: #f44336;
		font-weight: 600;
	}

	.pkg-price {
		font-size: 36rpx;
		font-weight: 700;
		color: #f44336;
	}

	.price-unit {
		font-size: 22rpx;
		color: #aaa;
	}

	.detail-btn {
		background: linear-gradient(135deg, #a8cece 0%, #8db8b6 100%);
		border-radius: 30rpx;
		padding: 10rpx 24rpx;
	}

	.detail-btn-text {
		font-size: 24rpx;
		color: #fff;
		font-weight: 600;
	}

	/* 加载更多 */
	.load-more,
	.no-more {
		display: flex;
		justify-content: center;
		padding: 30rpx 0;
	}

	.load-more-text {
		font-size: 26rpx;
		color: #a8cece;
		font-weight: 600;
	}

	.no-more-text {
		font-size: 24rpx;
		color: #ccc;
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


/* ── health exam page specifics ── */
.header { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  box-shadow: 0 4rpx 16rpx rgba(100,175,175,0.22) !important; padding-bottom: 24rpx !important; }
.header-title { color: #fff !important; font-weight: 600 !important; font-size: 34rpx !important; }
.back-arrow { color: #fff !important; font-size: 50rpx !important; }
.search-bar { background: rgba(255,255,255,0.22) !important; border-radius: 40rpx !important; }
.search-input,.search-icon { color: #fff !important; }
.filter-bar { background: #fff !important; padding: 16rpx 24rpx !important;
  margin: 0 !important; border-radius: 0 !important; }
.filter-tab { border-radius: 20rpx !important; padding: 10rpx 24rpx !important;
  color: #6a6a7a !important; font-size: 26rpx !important; }
.filter-tab.active { background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%) !important;
  color: #fff !important; }
.package-section { margin: 16rpx 24rpx !important; }
.package-item { background: #fff !important; border-radius: 16rpx !important;
  box-shadow: 0 4rpx 16rpx rgba(100,120,140,0.08) !important; margin-bottom: 20rpx !important;
  overflow: hidden !important; border: none !important; }
.pkg-name { color: #3a3a4a !important; font-weight: 700 !important; font-size: 30rpx !important; }
.pkg-desc { color: #6a6a7a !important; }
.pkg-price,.pkg-original-price { color: #d4a8b0 !important; }

</style>