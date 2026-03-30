<template>
  <view class="chat-page">
    <!-- 导航栏 -->
    <view class="nav-bar">
      <text class="nav-title">{{ chatName }}</text>
      <view class="more-btn" @click="showMore">
        <text class="more-icon">⋯</text>
      </view>
    </view>

    <!-- 聊天时间 -->
    <view class="chat-time">
      <text class="time-text">{{ currentTime }}</text>
    </view>

    <!-- 消息列表 -->
    <scroll-view
        class="message-list"
        scroll-y="true"
        :scroll-top="scrollTop"
        scroll-with-animation
        @scrolltoupper="loadMore"
    >
      <view v-if="loadingMore" class="loading-more">
        <text class="loading-text">加载中...</text>
      </view>
      <view
          class="message-item"
          v-for="(msg, index) in messageList"
          :key="msg.id || index"
          :class="{ 'self': msg.isSelf, 'system-msg-wrap': msg.isSystem }"
      >
        <!-- 系统消息气泡（居中展示） -->
        <view v-if="msg.isSystem" class="system-bubble-wrap">
          <view class="system-bubble">
            <text class="system-bubble-icon">🔔</text>
            <text class="system-bubble-text">{{ msg.content }}</text>
          </view>
          <text class="system-bubble-time">{{ msg.time }}</text>
        </view>

        <!-- 对方头像（非系统消息） -->
        <image v-if="!msg.isSelf && !msg.isSystem" class="msg-avatar" :src="msg.avatar || '/static/default-avatar.png'"
               mode="aspectFill"></image>

        <!-- 消息内容（非系统消息） -->
        <view v-if="!msg.isSystem" class="msg-content" :class="{ 'self': msg.isSelf }">
          <view v-if="msg.type === 'text' || !msg.type" class="msg-bubble" :class="{ 'self': msg.isSelf }">
            <text class="msg-text">{{ msg.content }}</text>
          </view>
          <view v-if="msg.type === 'voice'" class="msg-bubble voice" :class="{ 'self': msg.isSelf }">
            <text class="voice-icon">🔊</text>
            <text class="voice-duration">{{ msg.duration }}"</text>
          </view>
        </view>

        <!-- 自己头像（非系统消息） -->
        <image v-if="msg.isSelf && !msg.isSystem" class="msg-avatar self"
               :src="myAvatar || '/static/default-avatar.png'" mode="aspectFill"></image>
      </view>

      <!-- 发送中占位 -->
      <view v-if="sending" class="message-item self">
        <view class="msg-content self">
          <view class="msg-bubble self sending">
            <text class="msg-text sending-dot">···</text>
          </view>
        </view>
        <image class="msg-avatar self" :src="myAvatar || '/static/default-avatar.png'" mode="aspectFill"></image>
      </view>
    </scroll-view>

    <!-- 系统消息只读提示条 -->
    <view class="readonly-bar" v-if="isSystemChat">
      <text class="readonly-text">系统消息，不可回复</text>
    </view>

    <!-- 底部输入栏（系统消息时隐藏） -->
    <view class="input-bar" v-if="!isSystemChat">
      <view class="voice-btn" @click="toggleVoice">
        <text class="voice-btn-icon">{{ isVoiceMode ? '⌨️' : '🎤' }}</text>
      </view>
      <view v-if="!isVoiceMode" class="input-wrap">
        <input
            class="chat-input"
            type="text"
            placeholder="请输入聊天内容"
            v-model="inputMessage"
            confirm-type="send"
            @confirm="sendMessage"
        />
      </view>
      <view v-else class="voice-wrap" @touchstart="startRecord" @touchend="stopRecord">
        <text class="voice-hint">按住说话</text>
      </view>
      <view class="emoji-btn" @click="toggleEmoji">
        <text class="emoji-icon">😊</text>
      </view>
      <view class="send-btn" @click="sendMessage" v-if="!isVoiceMode && inputMessage.trim()">
        <text class="send-text">发送</text>
      </view>
      <view v-else class="add-btn" @click="toggleMore">
        <text class="add-icon">+</text>
      </view>
    </view>

    <!-- 更多功能面板（系统消息时隐藏） -->
    <view class="more-panel" v-if="showMorePanel && !isSystemChat">
      <view class="more-grid">
        <view class="more-item" @click="chooseImage">
          <view class="more-icon-wrap">
            <text class="more-item-icon">📷</text>
          </view>
          <text class="more-item-text">相册</text>
        </view>
        <view class="more-item" @click="takePhoto">
          <view class="more-icon-wrap">
            <text class="more-item-icon">📸</text>
          </view>
          <text class="more-item-text">拍摄</text>
        </view>
        <view class="more-item" @click="sendLocation">
          <view class="more-icon-wrap">
            <text class="more-item-icon">📍</text>
          </view>
          <text class="more-item-text">位置</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import {request} from '@/utils/auth';
import {getMessageList} from '@/utils/message-api.js';

export default {
  data() {
    return {
      chatName: '聊天',
      targetUserId: null,
      myId: null,
      myAvatar: '/static/default-avatar.png',
      inputMessage: '',
      isVoiceMode: false,
      showMorePanel: false,
      scrollTop: 0,
      currentTime: '',
      messageList: [],
      sending: false,
      loadingMore: false,
      currentPage: 0,
      hasMore: true,
      pageSize: 20,
      isSystemChat: false
    }
  },
  onLoad(options) {
    if (options.name) this.chatName = decodeURIComponent(options.name);
    if (options.isSystem === '1') {
      this.isSystemChat = true;
      this.loadSystemMessages();
      return;
    }
    if (options.targetId) this.targetUserId = Number(options.targetId);
    const userId = uni.getStorageSync('userId');
    const userInfo = uni.getStorageSync('userInfo');
    if (userId) this.myId = Number(userId);
    if (userInfo) this.myAvatar = userInfo.avatarUrl || userInfo.avatar || '/static/default-avatar.png';
    this.currentTime = this.formatTime(new Date());
    if (this.targetUserId) {
      this.loadHistory();
    }
  },
  methods: {
    // ===== 加载聊天记录 =====
    async loadHistory(prepend = false) {
      if (!this.targetUserId) return;
      if (this.loadingMore) return;
      this.loadingMore = true;
      try {
        const res = await request(`/chat/history/${this.targetUserId}`, {
          method: 'GET',
          data: {page: this.currentPage, pageSize: this.pageSize}
        });
        if (res && res.code === 200 && res.data) {
          const records = (res.data.records || []).map(m => this.normalizeMsg(m));
          if (prepend) {
            this.messageList = [...records, ...this.messageList];
          } else {
            this.messageList = records;
            this.$nextTick(() => this.scrollToBottom());
          }
          this.hasMore = this.currentPage + 1 < res.data.pages;
        }
      } catch (e) {
        console.error('加载聊天记录失败:', e);
      } finally {
        this.loadingMore = false;
      }
    },

    // 上拉加载更多历史
    async loadMore() {
      if (!this.hasMore || this.loadingMore) return;
      this.currentPage++;
      await this.loadHistory(true);
    },

    // ===== 发送消息 =====
    async sendMessage() {
      const content = this.inputMessage.trim();
      if (!content || !this.targetUserId) return;
      this.inputMessage = '';
      this.sending = true;
      this.showMorePanel = false;
      try {
        const res = await request('/chat/send', {
          method: 'POST',
          data: {
            receiverId: this.targetUserId,
            content: content,
            type: 'text'
          }
        });
        if (res && res.code === 200 && res.data) {
          this.messageList.push(this.normalizeMsg(res.data));
          this.$nextTick(() => this.scrollToBottom());
        } else {
          // 回填输入框
          this.inputMessage = content;
          uni.showToast({title: res?.msg || '发送失败', icon: 'none'});
        }
      } catch (e) {
        this.inputMessage = content;
        uni.showToast({title: '发送失败，请重试', icon: 'none'});
      } finally {
        this.sending = false;
      }
    },

    // ===== 消息归一化 =====
    normalizeMsg(m) {
      const myId = this.myId || Number(uni.getStorageSync('userId'));
      return {
        id: m.id || m._id,
        type: m.type || 'text',
        content: m.content || '',
        isSelf: m.senderId === myId || m.sender_id === myId,
        avatar: '/static/default-avatar.png',
        time: this.formatTime(new Date(m.createTime || m.create_time)),
        duration: m.duration
      };
    },

    // goBack() { uni.navigateBack(); },
    showMore() {
      uni.showToast({title: '更多功能', icon: 'none'});
    },

    // ===== 系统消息加载 =====
    async loadSystemMessages() {
      this.loadingMore = true;
      try {
        const res = await getMessageList({page: 1, pageSize: 50});
        if (res && res.code === 200 && res.data) {
          const list = res.data.records || res.data.list || (Array.isArray(res.data) ? res.data : []);
          this.messageList = list.map((m, i) => ({
            id: m.id || i,
            type: 'system',
            content: m.content || m.title || m.message || JSON.stringify(m),
            isSelf: false,
            isSystem: true,
            avatar: '/static/system-avatar.png',
            time: this.formatTime(new Date(m.createTime || m.create_time || Date.now()))
          }));
          if (this.messageList.length === 0) {
            this.messageList = [{
              id: 'sys-welcome',
              type: 'system',
              content: '欢迎使用！这里是系统消息，将向您推送订单状态、服务通知等重要信息。',
              isSelf: false,
              isSystem: true,
              time: this.formatTime(new Date())
            }];
          }
          this.$nextTick(() => this.scrollToBottom());
        }
      } catch (e) {
        this.messageList = [{
          id: 'sys-welcome',
          type: 'system',
          content: '欢迎使用！这里是系统消息，将向您推送订单状态、服务通知等重要信息。',
          isSelf: false,
          isSystem: true,
          time: this.formatTime(new Date())
        }];
        this.$nextTick(() => this.scrollToBottom());
      } finally {
        this.loadingMore = false;
      }
    },
    toggleVoice() {
      this.isVoiceMode = !this.isVoiceMode;
      this.showMorePanel = false;
    },
    toggleEmoji() {
      uni.showToast({title: '表情功能开发中', icon: 'none'});
    },
    toggleMore() {
      this.showMorePanel = !this.showMorePanel;
    },
    startRecord() {
      uni.showToast({title: '开始录音...', icon: 'none'});
    },
    stopRecord() {
      uni.showToast({title: '录音结束', icon: 'none'});
    },
    chooseImage() {
      uni.chooseImage({count: 1, success: () => uni.showToast({title: '图片已选择', icon: 'none'})});
    },
    takePhoto() {
      uni.chooseImage({
        count: 1,
        sourceType: ['camera'],
        success: () => uni.showToast({title: '拍照成功', icon: 'none'})
      });
    },
    sendLocation() {
      uni.showToast({title: '位置功能开发中', icon: 'none'});
    },

    scrollToBottom() {
      this.scrollTop = this.messageList.length * 1000 + 9999;
    },

    formatTime(d) {
      if (!d || isNaN(d)) return '';
      const now = new Date();
      const h = d.getHours().toString().padStart(2, '0');
      const min = d.getMinutes().toString().padStart(2, '0');
      const isToday = d.toDateString() === now.toDateString();
      if (isToday) return `${h}:${min}`;
      const mo = (d.getMonth() + 1).toString().padStart(2, '0');
      const day = d.getDate().toString().padStart(2, '0');
      return `${mo}-${day} ${h}:${min}`;
    }
  }
}
</script>

<style lang="scss">
.chat-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0 30rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}

.back-btn, .more-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.more-icon {
  font-size: 40rpx;
  color: #fff;
}

.chat-time {
  text-align: center;
  padding: 20rpx;
}

.time-text {
  font-size: 24rpx;
  color: #999;
}

.message-list {
  flex: 1;
  padding: 20rpx;
  overflow-y: auto;
}

.loading-more {
  text-align: center;
  padding: 20rpx;
}

.loading-text {
  font-size: 24rpx;
  color: #999;
}

.message-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 30rpx;

  &.self {
    justify-content: flex-end;
  }
}

.msg-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  background-color: #f0f0f0;

  &.self {
    margin-right: 0;
    margin-left: 20rpx;
  }
}

.msg-content {
  max-width: 60%;
}

.msg-bubble {
  background-color: #fff;
  padding: 20rpx;
  border-radius: 16rpx;
  word-break: break-all;

  &.self {
    background-color: #e0f7fa;
  }

  &.sending {
    opacity: 0.6;
  }

  &.voice {
    display: flex;
    align-items: center;
    min-width: 120rpx;
  }
}

.msg-text {
  font-size: 30rpx;
  color: #333;
  line-height: 1.5;
}

.sending-dot {
  letter-spacing: 4rpx;
  color: #999;
}

.voice-icon {
  font-size: 32rpx;
  margin-right: 10rpx;
}

.voice-duration {
  font-size: 28rpx;
  color: #666;
}

// 系统消息气泡
.system-msg-wrap {
  justify-content: center;
}

.system-bubble-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.system-bubble {
  display: flex;
  align-items: flex-start;
  background: linear-gradient(135deg, #e8f5e9, #f1f8e9);
  border: 1rpx solid #c8e6c9;
  border-radius: 16rpx;
  padding: 20rpx 24rpx;
  max-width: 80%;
  margin-bottom: 8rpx;
}

.system-bubble-icon {
  font-size: 32rpx;
  margin-right: 12rpx;
  flex-shrink: 0;
}

.system-bubble-text {
  font-size: 28rpx;
  color: #2e7d32;
  line-height: 1.6;
}

.system-bubble-time {
  font-size: 22rpx;
  color: #bbb;
}

// 只读提示条
.readonly-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx;
  background: #f5f5f5;
  border-top: 1rpx solid #eee;
}

.readonly-text {
  font-size: 26rpx;
  color: #bbb;
}

.input-bar {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #f8f8f8;
  border-top: 1rpx solid #eee;
}

.voice-btn {
  width: 70rpx;
  height: 70rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15rpx;
}

.voice-btn-icon {
  font-size: 40rpx;
}

.input-wrap {
  flex: 1;
  background-color: #fff;
  border-radius: 35rpx;
  padding: 15rpx 25rpx;
}

.chat-input {
  font-size: 30rpx;
  color: #333;
}

.voice-wrap {
  flex: 1;
  background-color: #fff;
  border-radius: 35rpx;
  padding: 20rpx 25rpx;
  text-align: center;
}

.voice-hint {
  font-size: 30rpx;
  color: #666;
}

.emoji-btn {
  width: 70rpx;
  height: 70rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 15rpx;
}

.emoji-icon {
  font-size: 40rpx;
}

.send-btn {
  width: 100rpx;
  height: 70rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  border-radius: 35rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 10rpx;
}

.send-text {
  font-size: 28rpx;
  color: #fff;
  font-weight: 500;
}

.add-btn {
  width: 70rpx;
  height: 70rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 10rpx;
}

.add-icon {
  font-size: 48rpx;
  color: #666;
}

.more-panel {
  background-color: #fff;
  padding: 30rpx;
  border-top: 1rpx solid #eee;
}

.more-grid {
  display: flex;
  justify-content: flex-start;
}

.more-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 60rpx;
}

.more-icon-wrap {
  width: 100rpx;
  height: 100rpx;
  background-color: #f5f5f5;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 15rpx;
}

.more-item-icon {
  font-size: 50rpx;
}

.more-item-text {
  font-size: 24rpx;
  color: #666;
}
</style>
