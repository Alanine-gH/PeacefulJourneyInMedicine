<template>
  <view class="container">
    <!-- 快捷问题 -->
    <view v-if="messages.length === 0" class="quick-questions">
      <view class="quick-questions-title">常见问题</view>
      <view class="quick-questions-list">
        <view 
          v-for="(question, index) in quickQuestions" 
          :key="index" 
          class="quick-question-item"
          @click="sendQuickQuestion(question)"
        >
          <text class="quick-question-text">{{ question }}</text>
        </view>
      </view>
    </view>

    <!-- 聊天区域 -->
    <view class="chat-container">
      <scroll-view class="chat-messages" scroll-y :scroll-into-view="scrollIntoView">
        <view 
          v-for="(message, index) in messages" 
          :key="index" 
          :id="'msg-' + index"
          class="message-item"
          :class="message.role === 'user' ? 'user-message' : 'ai-message'"
        >
          <view class="message-content">
            <text class="message-text">{{ message.content }}</text>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 提示框 -->
    <view v-if="showSuggestions" class="suggestions-container">
      <view class="suggestions-title">您可以继续了解：</view>
      <view class="suggestions-list">
        <view 
          v-for="(suggestion, index) in suggestions" 
          :key="index" 
          class="suggestion-item"
          @click="sendSuggestion(suggestion)"
        >
          <text class="suggestion-text">{{ suggestion }}</text>
        </view>
      </view>
    </view>

    <!-- 输入区域 -->
    <view class="input-section">
      <view class="input-container">
        <textarea 
          class="chat-input" 
          v-model="userInput" 
          placeholder="请输入您的问题..."
          :auto-height="true"
          :maxlength="500"
          @keyup.enter.exact="sendMessage"
          @focus="onInputStart"
        />
        <button class="send-btn" @click="sendMessage" :disabled="isLoading || !userInput.trim()">
          <text v-if="!isLoading">发送</text>
          <text v-else>...</text>
        </button>
      </view>
    </view>

    <!-- 返回首页按钮 -->
    <view class="home-section">
      <button class="home-btn" @click="goHome">回到首页</button>
    </view>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { sendChatStream as apiSendChatStream } from '../../utils/api';

function decodeUtf8Chunk(chunk) {
  try {
    return decodeURIComponent(escape(chunk));
  } catch (e) {
    return chunk;
  }
}

export default {
  data() {
    return {
      userInput: '',
      messages: [],
      isLoading: false,
      scrollIntoView: '',
      quickQuestions: [
        '做胃镜前需要注意什么？',
        '感冒了怎么办？',
        '如何预防高血压？',
        '糖尿病患者的饮食建议'
      ],
      showSuggestions: false,
      suggestions: [
        '还有其他健康问题吗？',
        '需要了解更多相关信息吗？',
        '想知道如何预防这种情况吗？',
        '需要了解治疗方案吗？'
      ]
    }
  },
  onLoad(options) {
    // 如果有传入的问题，自动发送
    if (options.question) {
      this.userInput = decodeURIComponent(options.question)
      this.sendMessage()
    }
  },
  methods: {
    sendQuickQuestion(question) {
      this.userInput = question
      this.sendMessage()
    },
    
    sendSuggestion(suggestion) {
      this.userInput = suggestion
      this.showSuggestions = false
      this.sendMessage()
    },
    
    onInputStart() {
      this.showSuggestions = false
    },
    
    async sendMessage() {
      if (!this.userInput.trim() || this.isLoading) {
        return
      }
      
      const question = this.userInput.trim()
      
      // 添加用户消息
      this.messages.push({
        role: 'user',
        content: question
      })
      
      this.userInput = ''
      this.isLoading = true
      this.scrollToBottom()
      
      try {
        await apiSendChatStream(
          { userInput: question },
          (chunk) => {
            // 处理流式响应
            const safeChunk = decodeUtf8Chunk(chunk)
            const lastMessage = this.messages[this.messages.length - 1]
            if (lastMessage && lastMessage.role === 'assistant') {
              lastMessage.content += safeChunk
            } else {
              this.messages.push({
                role: 'assistant',
                content: safeChunk
              })
            }
            this.scrollToBottom()
          },
          (full) => {
            // 处理完成
            console.log('完整回复:', full)
            this.isLoading = false
            // 显示提示框
            this.showSuggestions = true
          },
          (error) => {
            // 处理错误
            console.error('流式聊天错误:', error)
            this.messages.push({
              role: 'assistant',
              content: error || '抱歉，网络连接出现问题，请稍后重试。'
            })
            this.isLoading = false
            this.scrollToBottom()
            // 显示提示框
            this.showSuggestions = true
          }
        )
      } catch (error) {
        console.error('发送消息失败:', error)
        this.messages.push({
          role: 'assistant',
          content: '抱歉，网络连接出现问题，请稍后重试。'
        })
        this.isLoading = false
        this.scrollToBottom()
        // 显示提示框
        this.showSuggestions = true
      }
    },
    goHome() {
      uni.switchTab({
        url: '/pages/home/home'
      })
    },
    scrollToBottom() {
      this.$nextTick(() => {
        if (this.messages.length > 0) {
          this.scrollIntoView = 'msg-' + (this.messages.length - 1)
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #f4f2ee;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.title {
  font-size: 32rpx;
  font-weight: 600;
  flex: 1;
  text-align: center;
}

.placeholder {
  width: 36rpx;
}

/* 快捷问题 */
.quick-questions {
  padding: 20rpx;
  background-color: #f4f2ee;
  border-bottom: 1rpx solid #e0e0e0;
}

.quick-questions-title {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 15rpx;
}

.quick-questions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.quick-question-item {
  padding: 15rpx 20rpx;
  background-color: #fff;
  border: 1rpx solid #e0e0e0;
  border-radius: 20rpx;
  font-size: 26rpx;
  color: #666;
  cursor: pointer;
  transition: all 0.2s ease;
}

.quick-question-item:active {
  background-color: #f0f0f0;
  transform: scale(0.98);
}

/* 聊天区域 */
.chat-container {
  flex: 1;
  overflow: hidden;
  background-color: #f4f2ee;
}

.chat-messages {
  height: 100%;
  padding: 20rpx;
  box-sizing: border-box;
}

.message-item {
  margin-bottom: 30rpx;
  display: flex;
}

.user-message {
  justify-content: flex-end;
}

.ai-message {
  justify-content: flex-start;
}

.message-content {
  max-width: 70%;
  padding: 20rpx 24rpx;
  border-radius: 16rpx;
  word-wrap: break-word;
}

.user-message .message-content {
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  border-bottom-right-radius: 4rpx;
}

.ai-message .message-content {
  background-color: #fff;
  color: #333;
  border: 1rpx solid #e0e0e0;
  border-bottom-left-radius: 4rpx;
}

.message-text {
  font-size: 28rpx;
  line-height: 1.6;
}

/* 返回首页 */
.home-section {
  background-color: #fff;
  padding: 20rpx;
  border-top: 1rpx solid #eee;
}

.home-btn {
  width: 100%;
  height: 78rpx;
  background: #fff;
  border: 2rpx solid #8db8b6;
  color: #8db8b6;
  border-radius: 40rpx;
  font-size: 28rpx;
}

.home-btn::after {
  border: none;
}

/* 输入区域 */
.input-section {
  background-color: #fff;
  padding: 20rpx;
  border-top: 1rpx solid #e0e0e0;
}

.input-container {
  display: flex;
  gap: 20rpx;
  align-items: flex-end;
}

.chat-input {
  flex: 1;
  min-height: 80rpx;
  max-height: 200rpx;
  padding: 20rpx;
  background-color: #f4f2ee;
  border-radius: 16rpx;
  font-size: 28rpx;
  color: #333;
  line-height: 1.5;
}

.send-btn {
  width: 120rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 28rpx;
  font-weight: 500;
  border-radius: 40rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.send-btn::after {
  border: none;
}

.send-btn[disabled] {
  opacity: 0.5;
}

.send-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 提示框 */
.suggestions-container {
  background-color: #fff;
  padding: 20rpx;
  border-top: 1rpx solid #e0e0e0;
  border-bottom: 1rpx solid #e0e0e0;
}

.suggestions-title {
  font-size: 26rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 15rpx;
}

.suggestions-list {
  display: flex;
  flex-wrap: wrap;
  gap: 15rpx;
}

.suggestion-item {
  padding: 15rpx 20rpx;
  background-color: #f4f2ee;
  border: 1rpx solid #e0e0e0;
  border-radius: 20rpx;
  font-size: 26rpx;
  color: #666;
  cursor: pointer;
  transition: all 0.2s ease;
}

.suggestion-item:active {
  background-color: #e0e0e0;
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