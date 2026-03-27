<template>
	<view class="container">
		<!-- 顶部个人信息区域 -->
		<view class="header">
			<view v-if="!userInfo" class="user-unlogin" @click="login">
				<view class="avatar">
					<text class="avatar-icon">👤</text>
				</view>
				<view class="login-btn">登录 / 注册</view>
			</view>
			<view v-else class="user-login">
				<view class="user-info">
					<image :src="userInfo.avatar" class="avatar-img" />
					<view class="user-details">
						<view class="user-name">{{ userInfo.nickname }}</view>
						<view class="user-age">{{ userInfo.age }}周岁</view>
					</view>
				</view>
				<view class="user-stats">
					<view class="stat-item">
						<view class="stat-label">关注</view>
						<view class="stat-value">100</view>
					</view>
					<view class="stat-item">
						<view class="stat-label">粉丝</view>
						<view class="stat-value">23</view>
					</view>
					<view class="stat-item">
						<view class="stat-label">被赞</view>
						<view class="stat-value">200</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 我的订单 -->
		<view class="order-section">
			<view class="section-header" @click="viewAllOrders">
				<text class="section-title">我的订单</text>
				<text class="section-arrow">全部订单 ›</text>
			</view>
			<view class="order-status">
				<view class="status-item" @click="viewOrders('pending_payment')">
					<view class="status-icon">💳</view>
					<text class="status-text">待付款</text>
				</view>
				<view class="status-item" @click="viewOrders('pending_service')">
					<view class="status-icon">📋</view>
					<text class="status-text">待服务</text>
				</view>
				<view class="status-item" @click="viewOrders('pending_review')">
					<view class="status-icon">⭐</view>
					<text class="status-text">待评价</text>
				</view>
			</view>
		</view>

		<!-- 健康管理 -->
		<view class="health-section">
			<view class="section-header">
				<text class="section-title">健康管理</text>
			</view>
			<view class="health-grid">
				<view class="health-item" @click="navigateTo('blood_sugar')">
					<view class="health-icon">📊</view>
					<text class="health-text">血糖监测</text>
					<text class="health-arrow">›</text>
				</view>
				<view class="health-item" @click="navigateTo('medication')">
					<view class="health-icon">💊</view>
					<text class="health-text">用药提醒</text>
					<text class="health-arrow">›</text>
				</view>
				<view class="health-item" @click="navigateTo('diet')">
					<view class="health-icon">🍎</view>
					<text class="health-text">饮食监测</text>
					<text class="health-arrow">›</text>
				</view>
			</view>
		</view>

		<!-- 服务管理 -->
		<view class="service-section">
			<view class="section-header">
				<text class="section-title">服务管理</text>
			</view>
			<view class="service-grid">
				<view class="service-item" @click="navigateTo('appointment')">
					<view class="service-icon">📅</view>
					<text class="service-text">预约挂号</text>
					<text class="service-arrow">›</text>
				</view>
				<view class="service-item" @click="navigateTo('help')">
					<view class="service-icon">❓</view>
					<text class="service-text">帮助与客服</text>
					<text class="service-arrow">›</text>
				</view>
				<view class="service-item" @click="navigateTo('feedback')">
					<view class="service-icon">💬</view>
					<text class="service-text">意见反馈</text>
					<text class="service-arrow">›</text>
				</view>
			</view>
		</view>

		<!-- 退出登录 -->
		<view class="logout-section" v-if="userInfo">
			<view class="logout-btn" @click="handleLogout">
				<text class="logout-icon">🚪</text>
				<text class="logout-text">退出登录</text>
			</view>
		</view>

		<!-- 底部转换按钮 -->
		<!-- <view class="bottom-btn">
			<button class="switch-btn" @click="switchToCompanion">切换到陪护端</button>
		</view> -->
	</view>
</template>

<script>
	import {
		getUserInfo,
		logout
	} from '../../utils/auth';
	import {
		getAvatarUrl
	} from '../../utils/api';

	export default {
		data() {
			return {
				userInfo: null
			}
		},
		onShow() {
			this.checkLoginStatus()
		},
		methods: {
			checkLoginStatus() {
				const token = uni.getStorageSync('token')
				if (!token) {
					this.userInfo = null
					return
				}
				// 优先用本地缓存快速展示
				const cached = uni.getStorageSync('userInfo')
				if (cached) {
					this.userInfo = this.normalizeUser(cached)
				}
				// 每次进入页面从后端刷新最新数据
				this.fetchUserInfo()
			},
			// 统一字段映射：兼容 LoginVO 和 UserDetailVO 两种结构
			normalizeUser(raw) {
				if (!raw) return null
				return {
					id: raw.id || raw.userId,
					nickname: raw.nickName || raw.username || raw.nickname || '用户',
					avatar: getAvatarUrl(raw.avatarUrl || raw.avatar),
					phone: raw.phone || '',
					realName: raw.realName || '',
					userType: raw.userType,
					username: raw.username || '',
					age:raw.age
				}
			},
			async fetchUserInfo() {
				try {
					const response = await getUserInfo();
					if (response && response.code === 200 && response.data) {
						const normalized = this.normalizeUser(response.data)
						this.userInfo = normalized
						uni.setStorageSync('userInfo', response.data)
					}
				} catch (error) {
					console.error('获取用户信息失败:', error);
				}
			},
			handleLogout() {
				uni.showModal({
					title: '退出登录',
					content: '确定要退出登录吗？',
					confirmText: '退出',
					confirmColor: '#F44336',
					success: (res) => {
						if (res.confirm) {
							this.logout();
						}
					}
				});
			},
			async logout() {
				try {
					await logout();
					uni.removeStorageSync('token');
					uni.removeStorageSync('userId');
					uni.removeStorageSync('userInfo');
					this.userInfo = null;
					uni.showToast({
						title: '退出登录成功',
						icon: 'success'
					})
				} catch (error) {
					uni.removeStorageSync('token');
					uni.removeStorageSync('userId');
					uni.removeStorageSync('userInfo');
					this.userInfo = null;
				}
			},
			login() {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			viewAllOrders() {
				uni.navigateTo({
					url: '/pages/order/order'
				});
			},
			viewOrders(status) {
				// 映射到订单页面的筛选状态
				const statusMap = {
					'pending_payment': 'unpaid', // 待付款 -> 待支付
					'pending_service': 'pending', // 待服务 -> 待服务
					'pending_review': 'completed' // 待评价 -> 已完成
				};
				const targetStatus = statusMap[status] || 'all';
				uni.navigateTo({
					url: `/pages/order/order?status=${targetStatus}`
				});
			},
			navigateTo(page) {
				const pageMap = {
					'appointment': '/pages/package/package',
					'favorites': '/pages/favorites/favorites',
					'help': '/pages/customer/customer',
					'feedback': '/pages/feedback/feedback',
					'diet': '/pages/diet/diet',
					'medication': '/pages/medication/medication',
					'blood_sugar': '/pages/blood_sugar/blood_sugar'
				};

				if (pageMap[page]) {
					uni.navigateTo({
						url: pageMap[page]
					});
				} else {
					uni.showToast({
						title: `跳转到${page}页面`,
						icon: 'none'
					});
				}
			},
			switchToCompanion() {
				// 跳转到陪护端页面
				uni.navigateTo({
					url: '/pages/companion/home'
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

	/* 顶部个人信息区域 */
	.header {
		background-color: #4DD0E1;
		padding: 40rpx 30rpx 50rpx;
		position: relative;
	}

	.user-unlogin {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 30rpx 0;
	}

	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		background-color: rgba(255, 255, 255, 0.2);
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 20rpx;
	}

	.avatar-icon {
		font-size: 60rpx;
		color: #fff;
	}

	.login-btn {
		font-size: 28rpx;
		color: #fff;
		font-weight: 500;
		background-color: rgba(255, 255, 255, 0.2);
		padding: 10rpx 40rpx;
		border-radius: 20rpx;
	}

	.user-login {
		padding: 20rpx 0;
	}

	.user-info {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.avatar-img {
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		margin-right: 20rpx;
	}

	.user-details {
		flex: 1;
	}

	.user-name {
		font-size: 40rpx;
		font-weight: 700;
		color: #000;
		margin-bottom: 8rpx;
	}

	.user-age {
		font-size: 28rpx;
		font-weight: 600;
		color: #000;
	}

	.user-stats {
		display: flex;
		justify-content: space-around;
		margin-top: 20rpx;
	}

	.stat-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		color: #000;
	}

	.stat-value {
		font-size: 36rpx;
		font-weight: 600;
		margin-top: 4rpx;
	}

	.stat-label {
		font-size: 24rpx;
		font-weight: 500;
	}

	/* 我的订单 */
	.order-section {
		background-color: #fff;
		margin: -30rpx 20rpx 20rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
	}

	.section-title {
		font-size: 30rpx;
		font-weight: 600;
		color: #333;
	}

	.section-arrow {
		font-size: 24rpx;
		color: #999;
	}

	.order-status {
		display: flex;
		justify-content: space-around;
	}

	.status-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		cursor: pointer;
	}

	.status-icon {
		font-size: 40rpx;
		margin-bottom: 10rpx;
	}

	.status-text {
		font-size: 24rpx;
		color: #666;
	}

	/* 健康管理 */
	.health-section {
		background-color: #fff;
		margin: 20rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.health-grid {
		display: flex;
		flex-wrap: wrap;
		margin-top: 20rpx;
	}

	.health-item {
		width: 33.33%;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 20rpx 0;
		cursor: pointer;
	}

	.health-icon {
		font-size: 40rpx;
		margin-bottom: 10rpx;
	}

	.health-text {
		font-size: 24rpx;
		color: #666;
		margin-bottom: 5rpx;
	}

	.health-arrow {
		font-size: 20rpx;
		color: #999;
	}

	/* 服务管理 */
	.service-section {
		background-color: #fff;
		margin: 20rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.service-grid {
		display: flex;
		flex-wrap: wrap;
		margin-top: 20rpx;
	}

	.service-item {
		width: 33.33%;
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 20rpx 0;
		cursor: pointer;
	}

	.service-icon {
		font-size: 40rpx;
		margin-bottom: 10rpx;
	}

	.service-text {
		font-size: 24rpx;
		color: #666;
		margin-bottom: 5rpx;
	}

	.service-arrow {
		font-size: 20rpx;
		color: #999;
	}

	/* 底部转换按钮 */
	.bottom-btn {
		padding: 30rpx 20rpx 50rpx;
	}

	.switch-btn {
		width: 100%;
		height: 90rpx;
		background-color: #4DD0E1;
		color: #fff;
		font-size: 32rpx;
		font-weight: 500;
		border-radius: 45rpx;
		border: none;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.switch-btn::after {
		border: none;
	}

	.switch-btn:active {
		opacity: 0.9;
		transform: scale(0.98);
	}

	/* 退出登录 */
	.logout-section {
		margin: 20rpx;
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.logout-btn {
		background-color: #fff;
		display: flex;
		align-items: center;
		padding: 35rpx 30rpx;
		cursor: pointer;
	}

	.logout-btn:active {
		background-color: #f5f5f5;
	}

	.logout-icon {
		font-size: 36rpx;
		margin-right: 20rpx;
	}

	.logout-text {
		font-size: 28rpx;
		color: #F44336;
		font-weight: 500;
	}
</style>