<template>
  <view class="message-page">
    <scroll-view class="message-list" scroll-y="true">

      <!-- 系统消息（置顶，只读） -->
      <view class="message-item system-item" @click="openSystemChat">
        <view class="avatar-wrap">
          <view class="system-avatar">
            <text class="system-avatar-icon">🔔</text>
          </view>
          <view class="unread-badge" v-if="systemUnread > 0">
            <text class="unread-count">{{ systemUnread > 99 ? '99+' : systemUnread }}</text>
          </view>
        </view>
        <view class="message-content">
          <view class="message-header">
            <text class="message-name">系统消息</text>
            <text class="message-time">{{ systemLastTime }}</text>
          </view>
          <text class="message-preview">{{ systemLastMsg }}</text>
        </view>
      </view>

      <!-- 加载中 -->
      <view v-if="loading" class="empty-tip">
        <text class="empty-text">加载中...</text>
      </view>

      <!-- 用户会话列表 -->
      <view
          class="message-item"
          v-for="(session, index) in sessionList"
          :key="index"
          @click="openChat(session)"
      >
        <view class="avatar-wrap">
          <image class="avatar" :src="session.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
          <view class="unread-badge" v-if="session.unreadCount > 0">
            <text class="unread-count">{{ session.unreadCount > 99 ? '99+' : session.unreadCount }}</text>
          </view>
        </view>
        <view class="message-content">
          <view class="message-header">
            <text class="message-name">{{ session.name || ('用户' + session.peerId) }}</text>
            <text class="message-time">{{ formatTime(session.lastTime) }}</text>
          </view>
          <text class="message-preview">{{ session.lastMessage || '暂无消息' }}</text>
        </view>
      </view>

      <view v-if="!loading && sessionList.length === 0" class="empty-tip no-top-border">
        <text class="empty-text">暂无聊天会话</text>
      </view>

    </scroll-view>
  </view>
</template>

<script>
import {request} from '@/utils/auth';
import {getMessageList} from '@/utils/message-api.js';

export default {
  data() {
    return {
      sessionList: [],
      loading: false,
      systemUnread: 0,
      systemLastMsg: '欢迎使用，系统通知将在此展示',
      systemLastTime: ''
    }
  },
  onShow() {
    this.loadSessions();
    this.loadSystemMessages();
  },
  methods: {
    async loadSystemMessages() {
      try {
        const res = await getMessageList({type: 'system', page: 1, pageSize: 1});
        if (res && res.code === 200 && res.data) {
          const list = res.data.records || res.data.list || (Array.isArray(res.data) ? res.data : []);
          if (list.length > 0) {
            this.systemLastMsg = list[0].content || list[0].title || this.systemLastMsg;
            this.systemLastTime = this.formatTime(list[0].createTime || list[0].create_time);
          }
          this.systemUnread = res.data.total || list.filter(m => !m.isRead && !m.is_read).length || 0;
        }
      } catch (e) {
        // 接口不存在时静默处理，保留默认文本
        console.warn('系统消息加载失败:', e);
      }
    },
    async loadSessions() {
      const token = uni.getStorageSync('token');
      if (!token) {
        this.sessionList = [];
        return;
      }
      this.loading = true;
      try {
        const res = await request('/chat/sessions', {method: 'GET'});
        if (res && res.code === 200 && Array.isArray(res.data)) {
          this.sessionList = res.data;
        } else {
          this.sessionList = [];
        }
      } catch (e) {
        console.error('加载会话列表失败:', e);
        this.sessionList = [];
      } finally {
        this.loading = false;
      }
    },
    openSystemChat() {
      uni.navigateTo({url: '/pages/chat/chat?isSystem=1&name=%E7%B3%BB%E7%BB%9F%E6%B6%88%E6%81%AF'});
    },
    openChat(session) {
      uni.navigateTo({
        url: `/pages/chat/chat?targetId=${session.peerId}&name=${encodeURIComponent(session.name || '用户' + session.peerId)}`
      });
    },
    formatTime(val) {
      if (!val) return '';
      const d = new Date(val);
      if (isNaN(d)) return '';
      const now = new Date();
      const h = d.getHours().toString().padStart(2, '0');
      const min = d.getMinutes().toString().padStart(2, '0');
      if (d.toDateString() === now.toDateString()) return `${h}:${min}`;
      const mo = (d.getMonth() + 1).toString().padStart(2, '0');
      const day = d.getDate().toString().padStart(2, '0');
      return `${mo}-${day}`;
    }
  }
}
</script>

<style lang="scss">
.message-page {
  min-height: 100vh;
  background-color: #f4f2ee;
}

.message-list {
  padding: 20rpx;
  height: calc(100vh - 168rpx);
}

.message-item {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-radius: 16rpx;
  padding: 25rpx;
  margin-bottom: 15rpx;
}

.system-item {
  border-left: 6rpx solid #8db8b6;
}

.system-avatar {
  width: 90rpx;
  height: 90rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, #8db8b6, #a8cece);
  display: flex;
  align-items: center;
  justify-content: center;
}

.system-avatar-icon {
  font-size: 44rpx;
}

.avatar-wrap {
  position: relative;
  margin-right: 20rpx;
}

.avatar {
  width: 90rpx;
  height: 90rpx;
  border-radius: 50%;
  background-color: #f0f0f0;
}

.unread-badge {
  position: absolute;
  top: -5rpx;
  right: -5rpx;
  min-width: 36rpx;
  height: 36rpx;
  background-color: #ff4d4f;
  border-radius: 18rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 8rpx;
}

.unread-count {
  font-size: 20rpx;
  color: #fff;
}

.message-content {
  flex: 1;
  overflow: hidden;
}

.message-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10rpx;
}

.message-name {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.message-time {
  font-size: 24rpx;
  color: #999;
}

.message-preview {
  font-size: 26rpx;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.empty-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
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