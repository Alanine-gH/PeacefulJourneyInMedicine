<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/* 全局主题色变量 */
	page {
		--primary-color: #8db8b6;
		--primary-light: #a8cece;
		--primary-lighter: #c2dada;
		--secondary-purple: #c0b0d8;
		--secondary-pink: #d4a8b0;
		--secondary-orange: #e8c4a8;
		--secondary-green: #a8c0b8;
		--bg-color: #f5f3ef;
		--card-bg: #ffffff;
		--text-primary: #3a3a4a;
		--text-secondary: #6a6a7a;
		--text-tertiary: #9a9aaa;
		--border-color: rgba(141, 184, 182, 0.15);
		--shadow-soft: 0 4rpx 20rpx rgba(100, 120, 140, 0.08);
		--shadow-medium: 0 8rpx 32rpx rgba(100, 120, 140, 0.12);
		--radius-sm: 12rpx;
		--radius-md: 16rpx;
		--radius-lg: 20rpx;
		--radius-xl: 30rpx;
	}

	/* 全局页面背景 */
	page {
		background-color: var(--bg-color) !important;
		font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
	}

	/* 通用容器 */
	.container {
		background-color: var(--bg-color);
		min-height: 100vh;
		padding: -2px 24rpx 40rpx;
	}

	/* 通用卡片样式 */
	.card {
		background: var(--card-bg);
		border-radius: var(--radius-lg);
		box-shadow: var(--shadow-soft);
		padding: 30rpx;
		margin-bottom: 20rpx;
	}

	/* 通用区块标题 */
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 24rpx;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: 600;
		color: var(--text-primary);
		display: flex;
		align-items: center;
		gap: 12rpx;
	}

	.section-title::before {
		content: '';
		width: 6rpx;
		height: 28rpx;
		background: linear-gradient(180deg, var(--primary-color) 0%, var(--primary-light) 100%);
		border-radius: 3rpx;
	}

	.section-arrow {
		font-size: 26rpx;
		color: var(--text-tertiary);
	}

	/* 通用按钮样式 */
	.btn-primary {
		background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-light) 100%);
		color: #fff;
		border-radius: 40rpx;
		padding: 20rpx 40rpx;
		font-size: 28rpx;
		font-weight: 500;
		box-shadow: 0 4rpx 16rpx rgba(141, 184, 182, 0.3);
		border: none;
	}

	.btn-secondary {
		background: var(--card-bg);
		color: var(--primary-color);
		border-radius: 40rpx;
		padding: 16rpx 32rpx;
		font-size: 26rpx;
		font-weight: 500;
		border: 2rpx solid var(--border-color);
	}

	/* 通用输入框 */
	.input-field {
		background: var(--bg-color);
		border-radius: var(--radius-md);
		padding: 24rpx 30rpx;
		font-size: 28rpx;
		color: var(--text-primary);
		border: 2rpx solid transparent;
		transition: all 0.3s ease;
	}

	.input-field:focus {
		border-color: var(--primary-light);
		box-shadow: 0 0 0 4rpx rgba(141, 184, 182, 0.1);
	}

	/* 通用标签 */
	.tag {
		display: inline-flex;
		align-items: center;
		padding: 8rpx 20rpx;
		background: rgba(141, 184, 182, 0.12);
		color: var(--primary-color);
		font-size: 22rpx;
		border-radius: 20rpx;
		font-weight: 500;
	}

	/* 通用列表项 */
	.list-item {
		display: flex;
		align-items: center;
		padding: 28rpx 0;
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.04);
	}

	.list-item:last-child {
		border-bottom: none;
	}

	/* 通用图标容器 */
	.icon-box {
		width: 80rpx;
		height: 80rpx;
		border-radius: var(--radius-md);
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 36rpx;
	}

	.icon-box.primary {
		background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-light) 100%);
	}

	.icon-box.purple {
		background: linear-gradient(135deg, var(--secondary-purple) 0%, #a898c8 100%);
	}

	.icon-box.pink {
		background: linear-gradient(135deg, var(--secondary-pink) 0%, #c09098 100%);
	}

	.icon-box.orange {
		background: linear-gradient(135deg, var(--secondary-orange) 0%, #d4a88c 100%);
	}

	.icon-box.green {
		background: linear-gradient(135deg, var(--secondary-green) 0%, #8db8a8 100%);
	}

	.icon-box.light {
		background: rgba(141, 184, 182, 0.12);
	}

	/* 状态标签 */
	.status-tag {
		display: inline-flex;
		align-items: center;
		padding: 6rpx 16rpx;
		border-radius: 12rpx;
		font-size: 22rpx;
		font-weight: 500;
	}

	.status-tag.primary {
		background: rgba(141, 184, 182, 0.15);
		color: var(--primary-color);
	}

	.status-tag.success {
		background: rgba(168, 206, 206, 0.2);
		color: #5a9a98;
	}

	.status-tag.warning {
		background: rgba(232, 196, 168, 0.2);
		color: #c9946c;
	}

	.status-tag.danger {
		background: rgba(212, 168, 176, 0.2);
		color: #b87880;
	}

	/* 空状态 */
	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 40rpx;
	}

	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 30rpx;
		opacity: 0.5;
	}

	.empty-text {
		font-size: 28rpx;
		color: var(--text-tertiary);
	}

	/* 加载动画 */
	.loading-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 4rpx solid rgba(141, 184, 182, 0.2);
		border-top-color: var(--primary-color);
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		to { transform: rotate(360deg); }
	}

	/* 渐变文字 */
	.gradient-text {
		background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-light) 100%);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
		background-clip: text;
	}

	/* 分割线 */
	.divider {
		height: 1rpx;
		background: linear-gradient(90deg, transparent 0%, rgba(0,0,0,0.06) 50%, transparent 100%);
		margin: 20rpx 0;
	}

	/* 安全区域 */
	.safe-area-bottom {
		padding-bottom: constant(safe-area-inset-bottom);
		padding-bottom: env(safe-area-inset-bottom);
	}
</style>
