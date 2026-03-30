<template>
  <view class="container">
    <!-- 头部 -->
    <!-- <view class="header"> -->
     <!-- <view class="back-btn" @click="goBack">
        <text class="back-icon">‹</text>
      </view> -->
     <!-- <view class="title">在线客服</view>	
      <view class="status-dot" :class="isLoading ? 'thinking' : 'online'"></view>
    </view> -->

    <!-- 聊天时间 -->
    <view class="chat-time">
      <text class="time-text">今天 {{ currentDate }}</text>
    </view>

    <!-- 聊天内容 -->
    <scroll-view class="chat-content" scroll-y="true" :scroll-into-view="scrollAnchor" scroll-with-animation>
      <view v-for="(msg, idx) in messages" :key="idx" :id="'m' + idx"
            :class="['message-item', msg.type === 'mine' ? 'mine' : '']">
        <view class="message-avatar">
          <image v-if="msg.type === 'mine' && userAvatar" :src="userAvatar" class="avatar-img"
                 mode="aspectFill"/>
          <text v-else class="avatar-icon">{{ msg.type === 'mine' ? '👤' : '🤖' }}</text>
        </view>
        <view :class="['message-bubble', msg.type === 'mine' ? 'mine' : '']">
          <text class="message-text">{{ msg.content }}</text>
          <text v-if="msg.streaming" class="cursor-blink">▍</text>
          <text class="message-time">{{ msg.time }}</text>
        </view>
      </view>

      <!-- 思考中占位 -->
      <view v-if="thinking" class="message-item" id="m-thinking">
        <view class="message-avatar">
          <text class="avatar-icon">🤖</text>
        </view>
        <view class="message-bubble thinking-bubble">
          <text class="thinking-dot">···</text>
        </view>
      </view>

      <!-- 滚动锚点 -->
      <view id="scroll-bottom"></view>
    </scroll-view>

    <!-- 输入框 -->
    <view class="input-area">
      <input class="input-field" placeholder="请输入您的问题..." v-model="message" confirm-type="send"
             @confirm="sendMessage" :disabled="isLoading"/>
      <button class="send-btn" @click="sendMessage"
              :disabled="isLoading || !message.trim()">{{ isLoading ? '···' : '发送' }}
      </button>
    </view>
  </view>
</template>

<script>
import {
  sendChatStream
} from '@/utils/api';
import {getAvatarUrl} from '@/utils/api';

const FAQ_MAP = [
  {
    keys: ['预约陪诊', '怎么预约', '如何预约', '预约服务'],
    answer: '预约陪诊流程：\n1. 首页点击「导诊预约」\n2. 依次选择城市→医院→科室→医生\n3. 选择服务时间，填写就诊人信息\n4. 确认订单完成支付\n预约成功后可在「我的」→「我的业务」查看订单状态。'
  },
  {
    keys: ['费用', '多少钱', '价格', '收费'],
    answer: '陪诊费用根据服务类型和时长不同而有所差异。\n首页→导诊预约→服务类型页面，可查看各类服务详细报价。'
  },
  {
    keys: ['取消订单', '退款', '退钱', '撤销'],
    answer: '取消订单步骤：\n1. 底部导航点击「我的」\n2. 点击「我的业务」\n3. 找到对应订单点击「订单详情」\n4. 点击取消按钮申请\n退款将在1-3个工作日内原路退回。'
  },
  {
    keys: ['实名认证', '身份认证', '认证'],
    answer: '实名认证步骤：\n1. 底部导航点击「我的」\n2. 找到「实名认证」入口\n3. 按步骤1→2→3填写身份信息并上传证件照片\n4. 提交等待审核，通常1个工作日内完成\n认证通过后即可使用全部服务。'
  },
  {
    keys: ['成为陪诊师', '申请陪诊', '陪护师', '入驻', '兼职'],
    answer: '申请成为陪诊师步骤：\n1. 首页点击「陪护端」入口\n2. 进入「资格认证」页面填写信息\n3. 上传相关资质证书\n4. 提交审核1-3个工作日\n5. 审核通过后即可在「接单大厅」接单\n我们还提供「专业培训」和「高薪兼职」项目！'
  },
  {
    keys: ['智能分诊', '智能诊断', '问诊', '症状', '看什么科'],
    answer: '智能分诊使用方法：\n1. 首页点击「智能诊断」\n2. 在智能问答中描述您的症状\n3. AI将为您推荐合适的就诊科室\n4. 也可上传病历图片进行辅助分析\n如遇紧急情况请立即拨打120！'
  },
  {
    keys: ['查看订单', '我的订单', '订单状态'],
    answer: '查看订单步骤：\n1. 底部导航点击「我的」\n2. 点击「我的业务」\n3. 即可查看全部历史订单和当前状态\n订单详情页可查看服务进度、陪诊师联系方式及评价入口。'
  },
  {
    keys: ['忘记密码', '找回密码', '密码错误'],
    answer: '找回密码步骤：\n1. 打开登录页面，点击「忘记密码」\n2. 输入注册时使用的手机号\n3. 获取短信验证码完成验证\n4. 设置新密码即可'
  },
  {
    keys: ['增值服务', '饮食检测', '用药提醒', '血糖监测'],
    answer: '增值服务包括：\n饮食检测、用药提醒、血糖监测等健康管理功能\n进入方式：首页直接点击对应功能图标。'
  },
  {
    keys: ['优惠', '活动', '折扣', '优惠券'],
    answer: '查看优惠活动：首页点击「优惠活动」即可查看当前所有优惠信息，我们会定期推出陪诊套餐折扣、新用户专享优惠等。'
  },
  {
    keys: ['评价', '评分', '好评'],
    answer: '服务评价步骤：\n1. 底部导航「我的」→「我的业务」\n2. 找到已完成的订单\n3. 点击「服务评价」填写评分和评语后提交'
  },
  {
    keys: ['注册', '如何注册', '新用户'],
    answer: '注册步骤：\n1. 打开登录页面，点击「注册」\n2. 填写手机号并获取验证码\n3. 设置密码完成注册\n注册后建议先完成「实名认证」以使用全部服务。'
  },
  {
    keys: ['消息', '聊天', '联系陪诊师', '发消息'],
    answer: '与陪诊师聊天：\n1. 底部导航点击「消息」\n2. 找到对应的陪诊师会话\n3. 即可发送消息进行沟通\n下单后系统会自动为您建立与陪诊师的聊天通道。'
  }
];

function matchFaq(input) {
  for (const item of FAQ_MAP) {
    for (const key of item.keys) {
      if (input.includes(key)) return item.answer;
    }
  }
  return null;
}

export default {
  data() {
    return {
      message: '',
      messages: [{
        type: 'other',
        content: '您好，我是医路安心的智能客服小安~\n有什么可以帮助您的？可以直接提问，或点击下方常见问题快速咨询。',
        time: this.getNow()
      }],
      isLoading: false,
      thinking: false,
      scrollAnchor: '',
      userAvatar: '',
      quickQuestions: [
        '如何预约陪诊服务？', '陪诊费用大概多少？', '如何取消订单？',
        '实名认证怎么做？', '如何成为陪诊师？', '智能分诊怎么用？',
        '如何查看我的订单？', '忘记密码怎么办？'
      ],
      history: []
    }
  },
  onShow() {
    const info = uni.getStorageSync('userInfo');
    if (info) {
      this.userAvatar = getAvatarUrl(info.avatarUrl || info.avatar || '');
    }
  },
  computed: {
    currentDate() {
      const now = new Date();
      return (now.getMonth() + 1) + '月' + now.getDate() + '日';
    }
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },
    sendQuick(q) {
      this.message = q;
      this.sendMessage();
    },
    async sendMessage() {
      const content = this.message.trim();
      if (!content || this.isLoading) return;
      this.message = '';
      this.isLoading = true;
      this.messages.push({
        type: 'mine',
        content,
        time: this.getNow()
      });
      this.scrollToBottom();
      const faq = matchFaq(content);
      if (faq) {
        this.thinking = true;
        this.$nextTick(() => {
          this.scrollAnchor = 'm-thinking';
        });
        setTimeout(() => {
          this.thinking = false;
          this.isLoading = false;
          this.messages.push({
            type: 'other',
            content: faq,
            time: this.getNow()
          });
          this.scrollToBottom();
        }, 600);
        return;
      }
      this.history.push({ role: 'user', content });
      this.thinking = true;
      this.$nextTick(() => {
        this.scrollAnchor = 'm-thinking';
      });
      try {
        const token = uni.getStorageSync('token');
        if (!token) {
          this.thinking = false;
          this.isLoading = false;
          this.messages.push({
            type: 'other',
            content: '您还未登录，请先登录后再使用智能客服功能。',
            time: this.getNow()
          });
          this.scrollToBottom();
          return;
        }
        let aiIdx = -1, fullReply = '';
        await sendChatStream(
          { userInput: content, history: this.history.slice(0, -1) },
          (chunk) => {
            this.thinking = false;
            fullReply += chunk;
            if (aiIdx === -1) {
              this.messages.push({
                type: 'other',
                content: fullReply,
                time: this.getNow(),
                streaming: true
              });
              aiIdx = this.messages.length - 1;
            } else {
              this.$set(this.messages, aiIdx, Object.assign({}, this.messages[aiIdx], { content: fullReply }));
            }
            this.scrollToBottom();
          },
          (full) => {
            this.thinking = false;
            this.isLoading = false;
            const fc = full || fullReply;
            if (aiIdx >= 0) {
              this.$set(this.messages, aiIdx, Object.assign({}, this.messages[aiIdx], { content: fc, streaming: false }));
            } else if (fc) {
              this.messages.push({ type: 'other', content: fc, time: this.getNow() });
            }
            this.history.push({ role: 'assistant', content: fc });
            if (this.history.length > 20) this.history = this.history.slice(-20);
            this.scrollToBottom();
          },
          (err) => {
            this.thinking = false;
            this.isLoading = false;
            const e = (typeof err === 'string' && err) ? err : '抱歉，服务暂时不可用，请稍后重试。';
            if (aiIdx >= 0) {
              this.$set(this.messages, aiIdx, Object.assign({}, this.messages[aiIdx], { content: e, streaming: false }));
            } else {
              this.messages.push({ type: 'other', content: e, time: this.getNow() });
            }
            this.scrollToBottom();
          }
        );
      } catch (e) {
        this.thinking = false;
        this.isLoading = false;
        this.messages.push({
          type: 'other',
          content: '抱歉，连接异常，请稍后重试。',
          time: this.getNow()
        });
        this.scrollToBottom();
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        this.scrollAnchor = 'scroll-bottom';
      });
    },
    getNow() {
      const d = new Date();
      return d.getHours().toString().padStart(2, '0') + ':' + d.getMinutes().toString().padStart(2, '0');
    }
  }
}
</script>

<style scoped>
/* 整体容器 */
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f5f7fa;
}

/* 头部 */
.header {
  background-color: #4DD0E1;
  padding: 40rpx 30rpx 20rpx;
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  z-index: 1;
}

.back-icon {
  font-size: 56rpx;
  color: #fff;
  font-weight: bold;
  line-height: 1;
}

.title {
  flex: 1;
  text-align: center;
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
}

.status-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 50%;
  flex-shrink: 0;
}

.status-dot.online {
  background-color: #a5f3a5;
  box-shadow: 0 0 0 4rpx rgba(165, 243, 165, 0.4);
}

.status-dot.thinking {
  background-color: #ffd06e;
  box-shadow: 0 0 0 4rpx rgba(255, 208, 110, 0.4);
  animation: pulse 1.2s ease-in-out infinite;
}

/* 聊天时间 */
.chat-time {
  text-align: center;
  padding: 20rpx 0 10rpx;
  flex-shrink: 0;
}

.time-text {
  font-size: 22rpx;
  color: #bbb;
  background-color: rgba(0, 0, 0, 0.05);
  padding: 6rpx 20rpx;
  border-radius: 20rpx;
}

/* 快捷问题 */
.quick-list {
  padding: 10rpx 24rpx 20rpx;
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  flex-shrink: 0;
}

.quick-item {
  background-color: #E1F5FE;
  border-radius: 30rpx;
  padding: 12rpx 28rpx;
}

.quick-text {
  font-size: 24rpx;
  color: #26C6DA;
}

/* 聊天内容区 */
.chat-content {
  flex: 1;
  padding: 10rpx 0;
  overflow: hidden;
}

.message-item {
  display: flex;
  align-items: flex-end;
  padding: 14rpx 20rpx;
}

.message-item.mine {
  flex-direction: row-reverse;
}

.message-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 16rpx;
  flex-shrink: 0;
}

.avatar-img {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
}

.avatar-icon {
  font-size: 36rpx;
}

.message-bubble {
  max-width: 68%;
  background-color: #fff;
  padding: 18rpx 22rpx;
  border-radius: 20rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.06);
}

.message-bubble.mine {
  background-color: #E1F5FE;
}

.message-bubble.thinking-bubble {
  padding: 18rpx 30rpx;
}

.message-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.55;
  display: block;
}

.cursor-blink {
  font-size: 28rpx;
  color: #4DD0E1;
  animation: blink 0.7s step-end infinite;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

.thinking-dot {
  font-size: 36rpx;
  color: #999;
  letter-spacing: 6rpx;
  animation: pulse 1.2s ease-in-out infinite;
}

.message-time {
  font-size: 20rpx;
  color: #bbb;
  margin-top: 8rpx;
  display: block;
  text-align: right;
}

/* 输入区 */
.input-area {
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 20rpx 30rpx;
  border-top: 1rpx solid #eee;
  gap: 20rpx;
  flex-shrink: 0;
}

.input-field {
  flex: 1;
  height: 72rpx;
  background-color: #f5f5f5;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  color: #333;
}

.send-btn {
  width: 120rpx;
  height: 72rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 28rpx;
  font-weight: 500;
  border-radius: 36rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
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

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.4; }
}
</style> 