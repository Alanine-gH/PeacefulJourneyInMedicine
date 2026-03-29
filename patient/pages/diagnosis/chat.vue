<template>
  <view class="container">
    <!-- 顶部导航 -->
    <!-- <view class="header">
      <view class="back-btn" @click="goBack">
        <text class="back-icon">←</text>
      </view>
      <view class="title">智能问答</view>
      <view class="placeholder"></view>
    </view> -->

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

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
import { sendChatStream as apiSendChatStream } from '../../utils/api';

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
    // goBack() {
    //   uni.navigateBack()
    // },
    
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
            const lastMessage = this.messages[this.messages.length - 1]
            if (lastMessage && lastMessage.role === 'assistant') {
              lastMessage.content += chunk
            } else {
              this.messages.push({
                role: 'assistant',
                content: chunk
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
  background-color: #f8f9fa;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
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
}

.placeholder {
  width: 36rpx;
}

/* 快捷问题 */
.quick-questions {
  padding: 20rpx;
  background-color: #f8f9fa;
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
  background-color: #f8f9fa;
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
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
  background-color: #f5f5f5;
  border-radius: 16rpx;
  font-size: 28rpx;
  color: #333;
  line-height: 1.5;
}

.send-btn {
  width: 120rpx;
  height: 80rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
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
  background-color: #f5f5f5;
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
</style>