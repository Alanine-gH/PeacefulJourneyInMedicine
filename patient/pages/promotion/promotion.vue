<template>
	<view class="promotion-page">
		<!-- 活动详情 -->
		<view class="promotion-detail">
			<!-- 顶部横幅 -->
			<view class="promotion-banner"
				:style="{ backgroundColor: serviceData.bannerColor, backgroundImage: serviceData.bannerGradient }">
				<view class="banner-content">
					<view v-if="serviceData.tag" class="banner-tag">{{ serviceData.tag }}</view>
					<view class="banner-price">{{ serviceData.price }}</view>
					<view class="banner-title">{{ serviceData.title }}</view>
					<view class="banner-subtitle">{{ serviceData.subtitle }}</view>
					<view v-if="serviceData.icon" class="banner-icon">
						<text>{{ serviceData.icon }}</text>
					</view>
					<view v-if="serviceData.heart" class="banner-heart">
						<text>{{ serviceData.heart }}</text>
					</view>
				</view>
			</view>

			<!-- 服务介绍 -->
			<view class="service-section">
				<view class="section-title">
					<text class="title-icon">🌟</text>
					<text>服务介绍</text>
				</view>
				<view class="service-content">
					<view v-for="(item, index) in serviceData.services" :key="index" class="service-item">
						<text class="service-icon">{{ item.icon }}</text>
						<text class="service-text">{{ item.text }}</text>
					</view>
				</view>
			</view>

			<!-- 适用范围 -->
			<view class="scope-section">
				<view class="section-title">
					<text class="title-icon">📍</text>
					<text>适用范围</text>
				</view>
				<view class="scope-content">
					<view v-for="(item, index) in serviceData.scope" :key="index" class="scope-item">
						<text class="scope-text">{{ item }}</text>
					</view>
				</view>
			</view>

			<!-- 注意事项 -->
			<view class="notice-section" v-if="serviceData.notice && serviceData.notice.length > 0">
				<view class="section-title">
					<text class="title-icon">⚠️</text>
					<text>注意事项</text>
				</view>
				<view class="notice-content"
					:style="{ borderLeftColor: serviceData.noticeBorderColor, backgroundColor: serviceData.noticeBgColor }">
					<view v-for="(item, index) in serviceData.notice" :key="index" class="notice-item">
						<text class="notice-text">{{ item }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部价格信息 -->
		<view class="footer-bar">
			<view class="footer-price">
				<text class="price-label">价格：</text>
				<text class="price-value" :style="{ color: serviceData.priceColor }">{{ serviceData.price }}</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				serviceData: {}
			}
		},
		onLoad(options) {
			if (options && options.index !== undefined) {
				const index = parseInt(options.index);
				this.setServiceData(index);
			} else {
				this.setServiceData(0);
			}
		},
		methods: {
			setServiceData(index) {
				const serviceList = [{
					// VIP陪诊服务
					price: '588元/次',
					title: 'VIP陪诊服务',
					subtitle: 'VIP陪诊体验，专车接送',
					tag: 'VIP',
					icon: '',
					navColor: '#E8C4A8',
					navGradient: 'linear-gradient(135deg, #E8C4A8 0%, #D4A88C 100%)',
					bannerColor: '#E8C4A8',
					bannerGradient: 'linear-gradient(135deg, #E8C4A8 0%, #D4A88C 100%)',
					numberColor: '#E8C4A8',
					numberGradient: 'linear-gradient(135deg, #E8C4A8 0%, #D4A88C 100%)',
					arrowColor: '#E8C4A8',
					priceColor: '#D4A88C',
					noticeBorderColor: '#E8C4A8',
					noticeBgColor: '#F9F0E8',
						services: [{
								icon: '🚗',
								text: '专车接送服务'
							},
							{
								icon: '👨‍⚕️',
								text: 'VIP陪诊体验'
							},
							{
								icon: '🏥',
								text: '全程导诊'
							},
							{
								icon: '🎯',
								text: '优先挂号'
							}
						],
						scope: [
							'适用于所有医院',
							'适用于所有科室',
							'适用于VIP客户'
						],
						process: [
							'在线预约，选择时间',
							'确认订单，支付费用',
							'专车上门接送',
							'全程VIP服务',
							'送回，服务完成'
						],
						notice: [
							'请至少提前24小时预约',
							'如需取消，请提前12小时',
							'服务时间：全天24小时',
							'节假日正常服务',
							'提供高端医疗服务'
						]
					},
					{
						// 全程陪诊
						price: '488元/次',
						title: '全程陪诊',
						subtitle: '全程陪同，手续代办',
						tag: '',
						icon: '🚑',
						navColor: '#8DB8B6',
						navGradient: 'linear-gradient(135deg, #8DB8B6 0%, #A8CECE 100%)',
						bannerColor: '#8DB8B6',
						bannerGradient: 'linear-gradient(135deg, #8DB8B6 0%, #A8CECE 100%)',
						numberColor: '#8DB8B6',
						numberGradient: 'linear-gradient(135deg, #8DB8B6 0%, #A8CECE 100%)',
						arrowColor: '#8DB8B6',
						priceColor: '#8DB8B6',
						noticeBorderColor: '#8DB8B6',
						noticeBgColor: '#E8F5F5',
						services: [{
								icon: '👨‍⚕️',
								text: '全程陪同'
							},
							{
								icon: '📋',
								text: '手续代办'
							},
							{
								icon: '🏥',
								text: '全程导诊'
							},
							{
								icon: '💼',
								text: '协助检查'
							}
						],
						scope: [
							'适用于所有医院',
							'适用于所有科室',
							'适用于需要全程服务的客户'
						],
						process: [
							'在线预约，选择时间',
							'确认订单，支付费用',
							'陪同就诊',
							'代办手续',
							'服务完成'
						],
						notice: [
							'请至少提前12小时预约',
							'如需取消，请提前6小时',
							'服务时间：8:00-18:00',
							'节假日正常服务'
						]
					},
					{
						// 代办问诊
						price: '388元/次',
						title: '代办问诊',
						subtitle: '代替客户到机构咨询',
						tag: '',
						icon: '💬',
						navColor: '#C0B0D8',
						navGradient: 'linear-gradient(135deg, #C0B0D8 0%, #A898C8 100%)',
						bannerColor: '#C0B0D8',
						bannerGradient: 'linear-gradient(135deg, #C0B0D8 0%, #A898C8 100%)',
						numberColor: '#C0B0D8',
						numberGradient: 'linear-gradient(135deg, #C0B0D8 0%, #A898C8 100%)',
						arrowColor: '#C0B0D8',
						priceColor: '#A898C8',
						noticeBorderColor: '#C0B0D8',
						noticeBgColor: '#F3EDF7',
						services: [{
								icon: '💬',
								text: '代替客户咨询'
							},
							{
								icon: '🏥',
								text: '到机构咨询'
							},
							{
								icon: '📋',
								text: '记录问诊信息'
							},
							{
								icon: '📱',
								text: '实时反馈'
							}
						],
						scope: [
							'适用于所有医院',
							'适用于所有科室',
							'适用于无法亲自就诊的客户'
						],
						process: [
							'在线预约，提交需求',
							'确认订单，支付费用',
							'到机构咨询',
							'记录问诊信息',
							'反馈结果给客户'
						],
						notice: [
							'请至少提前24小时预约',
							'如需取消，请提前12小时',
							'服务时间：8:00-17:00',
							'仅限工作日服务',
							'请提供详细的咨询需求'
						]
					},
					{
						// 取送结果
						price: '288元/次',
						title: '取送结果',
						subtitle: '取送结果到家',
						tag: '',
						icon: '📋',
						navColor: '#D4A8B0',
						navGradient: 'linear-gradient(135deg, #D4A8B0 0%, #C09098 100%)',
						bannerColor: '#D4A8B0',
						bannerGradient: 'linear-gradient(135deg, #D4A8B0 0%, #C09098 100%)',
						numberColor: '#D4A8B0',
						numberGradient: 'linear-gradient(135deg, #D4A8B0 0%, #C09098 100%)',
						arrowColor: '#D4A8B0',
						priceColor: '#C09098',
						noticeBorderColor: '#D4A8B0',
						noticeBgColor: '#FCE8EC',
						services: [{
								icon: '📋',
								text: '代取检查结果'
							},
							{
								icon: '🚗',
								text: '送结果到家'
							},
							{
								icon: '📱',
								text: '实时通知'
							},
							{
								icon: '🔒',
								text: '结果保密'
							}
						],
						scope: [
							'适用于所有医院',
							'适用于需要代取结果的客户',
							'适用于行动不便的客户'
						],
						process: [
							'在线预约，填写信息',
							'确认订单，支付费用',
							'到医院取结果',
							'送结果到家',
							'服务完成'
						],
						notice: [
							'请至少提前6小时预约',
							'如需取消，请提前2小时',
							'服务时间：8:00-18:00',
							'节假日正常服务',
							'请提供取结果的必要信息'
						]
					},
					{
						// 代办买药
						price: '188元/次',
						title: '代办买药',
						subtitle: '人工排队代买药',
						tag: '',
						icon: '💊',
						navColor: '#A8C0B8',
						navGradient: 'linear-gradient(135deg, #A8C0B8 0%, #8DB8A8 100%)',
						bannerColor: '#A8C0B8',
						bannerGradient: 'linear-gradient(135deg, #A8C0B8 0%, #8DB8A8 100%)',
						numberColor: '#A8C0B8',
						numberGradient: 'linear-gradient(135deg, #A8C0B8 0%, #8DB8A8 100%)',
						arrowColor: '#A8C0B8',
						priceColor: '#8DB8A8',
						noticeBorderColor: '#A8C0B8',
						noticeBgColor: '#E8F5F0',
						services: [{
								icon: '💊',
								text: '人工排队代买药'
							},
							{
								icon: '🏥',
								text: '到医院或药店'
							},
							{
								icon: '📋',
								text: '核对药品信息'
							},
							{
								icon: '🚗',
								text: '送药到家'
							}
						],
						scope: [
							'适用于所有医院和药店',
							'适用于需要代买药的客户',
							'适用于行动不便的客户'
						],
						process: [
							'在线预约，提供药品信息',
							'确认订单，支付费用',
							'排队代买药',
							'核对药品信息',
							'送药到家'
						],
						notice: [
							'请至少提前4小时预约',
							'如需取消，请提前1小时',
							'服务时间：8:00-18:00',
							'节假日正常服务',
							'请提供详细的药品信息和处方'
						]
					}
				];

				this.serviceData = serviceList[index] || serviceList[0];
			}
		}
	}
</script>

<style scoped>
	.promotion-page {
		min-height: 100vh;
		background-color: #f4f2ee;
		padding-bottom: 120rpx;
	}

	/* 顶部横幅 */
	.promotion-banner {
		border-radius: 20rpx;
		padding: 60rpx 40rpx;
		margin-bottom: 20rpx;
		position: relative;
		overflow: hidden;
	}

	.promotion-banner::before {
		content: '';
		position: absolute;
		top: -50%;
		right: -50%;
		width: 200%;
		height: 200%;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0) 70%);
		transform: rotate(30deg);
	}

	.banner-content {
		position: relative;
		z-index: 1;
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		position: relative;
	}

	.banner-tag {
		background-color: rgba(255, 255, 255, 0.2);
		color: #fff;
		font-size: 24rpx;
		padding: 8rpx 16rpx;
		border-radius: 8rpx;
		font-weight: 600;
		align-self: flex-end;
		margin-bottom: 20rpx;
	}

	.banner-icon {
		position: absolute;
		bottom: 20rpx;
		right: 20rpx;
		font-size: 80rpx;
		opacity: 0.8;
	}

	.banner-heart {
		position: absolute;
		bottom: 20rpx;
		right: 20rpx;
		font-size: 80rpx;
		opacity: 0.8;
	}

	.banner-price {
		font-size: 48rpx;
		font-weight: 700;
		color: #fff;
		margin-bottom: 12rpx;
	}

	.banner-title {
		font-size: 48rpx;
		font-weight: 700;
		color: #fff;
		margin-bottom: 8rpx;
	}

	.banner-subtitle {
		font-size: 32rpx;
		color: rgba(255, 255, 255, 0.9);
		margin-bottom: 30rpx;
	}


	/* 通用区块 */
	.service-section,
	.scope-section,
	.notice-section {
		background-color: #fff;
		border-radius: 20rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
	}

	.section-title {
		display: flex;
		align-items: center;
		font-size: 30rpx;
		font-weight: 600;
		color: #333;
		margin-bottom: 20rpx;
	}

	.title-icon {
		font-size: 32rpx;
		margin-right: 12rpx;
	}

	/* 服务介绍 */
	.service-content {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.service-item {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}

	.service-icon {
		font-size: 36rpx;
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #FFF3E0;
		border-radius: 12rpx;
		flex-shrink: 0;
	}

	.service-text {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
		flex: 1;
	}

	/* 适用范围 */
	.scope-content {
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}

	.scope-item {
		display: flex;
		align-items: center;
		padding: 16rpx;
		background-color: #f4f2ee;
		border-radius: 10rpx;
	}

	.scope-text {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
	}

	/* 注意事项 */
	.notice-content {
		background-color: #FFF3E0;
		padding: 20rpx;
		border-radius: 10rpx;
		border-left: 8rpx solid #FF9800;
	}

	.notice-item {
		margin-bottom: 12rpx;
	}

	.notice-item:last-child {
		margin-bottom: 0;
	}

	.notice-text {
		font-size: 26rpx;
		color: #666;
		line-height: 1.6;
	}

	/* 底部操作栏 */
	.footer-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background-color: #fff;
		padding: 20rpx 30rpx;
		padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
		box-shadow: 0 -2rpx 12rpx rgba(0, 0, 0, 0.05);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.footer-price {
		display: flex;
		align-items: center;
	}

	.price-label {
		font-size: 24rpx;
		color: #999;
		margin-right: 8rpx;
	}

	.price-value {
		font-size: 32rpx;
		font-weight: 700;
		color: #FF6B6B;
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