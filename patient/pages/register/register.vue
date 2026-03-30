<template>
  <view class="container">
    <!-- Logo区域 -->
    <view class="logo-section">
      <view class="logo">
        <view class="logo-icon">
          <view class="heart">
            <view class="heart-inner"></view>
          </view>
        </view>
      </view>
      <text class="app-name">医路安心</text>
    </view>

    <!-- 注册表单 -->
    <form @submit="onRegister">
      <view class="form-section">
        <view class="input-group">
          <view class="input-item">
            <view class="input-icon user-icon"></view>
            <input type="text" placeholder="请输入用户名" v-model="username" class="input-field"
                   name="username"/>
          </view>
          <view class="input-item">
            <view class="input-icon lock-icon"></view>
            <input :type="showPassword ? 'text' : 'password'" placeholder="请输入密码" v-model="password"
                   class="input-field" name="password"/>
            <view class="eye-icon" :class="showPassword ? 'eye-open' : 'eye-close'" @click="togglePassword">
              <text v-if="!showPassword" class="eye-text">👁️</text>
              <text v-else class="eye-text">👁️‍🗨️</text>
            </view>
          </view>
          <view class="input-item">
            <view class="input-icon lock-icon"></view>
            <input :type="showConfirmPassword ? 'text' : 'password'" placeholder="请确认密码"
                   v-model="confirmPassword" class="input-field" name="confirmPassword"/>
            <view class="eye-icon" :class="showConfirmPassword ? 'eye-open' : 'eye-close'"
                  @click="toggleConfirmPassword">
              <text v-if="!showConfirmPassword" class="eye-text">👁️</text>
              <text v-else class="eye-text">👁️‍🗨️</text>
            </view>
          </view>
          <view class="input-item">
            <view class="input-icon phone-icon"></view>
            <input type="number" placeholder="请输入手机号" v-model="phone" class="input-field" maxlength="11"
                   name="phone"/>
          </view>
          <view class="registerUserType-selector">
            <view class="registerUserType-label">用户注册类型</view>
            <view class="registerUserType-options">
              <view class="registerUserType-option" :class="selectedregisterUserType === '1' ? 'active' : ''"
                    @click="selectregisterUserType('1')">患者
              </view>
              <view class="registerUserType-option" :class="selectedregisterUserType === '2' ? 'active' : ''"
                    @click="selectregisterUserType('2')">陪诊师
              </view>
            </view>
          </view>
          <view class="input-item captcha-item">
            <view class="input-item-inner">
              <view class="input-icon captcha-icon"></view>
              <input type="text" placeholder="请输入图形验证码" v-model="captcha" class="input-field"
                     name="captcha" maxlength="4"/>
            </view>
            <view class="captcha-box" @click="fetchCaptcha">
              <text v-if="captchaLoading" class="captcha-loading"></text>
              <image v-else-if="captchaImg" :src="'data:image/jpeg;base64,' + captchaImg"
                     class="captcha-img" mode="aspectFit"/>
              <text v-else class="captcha-placeholder">点击获取</text>
            </view>
          </view>

        </view>

        <!-- 注册按钮 -->
        <button class="register-btn" form-type="submit">立即注册</button>

        <!-- 已有账号 -->
        <view class="login-link-wrapper">
          <text class="login-text">已有账号，</text>
          <text class="login-link" @click="goToLogin">去登录</text>
        </view>
      </view>
    </form>

    <!-- 底部协议 -->
    <view class="agreement-section">
      <view class="checkbox-wrapper" @click="toggleAgreement">
        <view class="checkbox" :class="agreed ? 'checked' : ''"></view>
        <text class="agreement-text">我已阅读并同意</text>
      </view>
      <text class="agreement-link" @click="viewUserAgreement">《用户协议》</text>
      <text class="agreement-text">和</text>
      <text class="agreement-link" @click="viewServiceAgreement">《服务协议》</text>
    </view>
  </view>
</template>

<script>
import {
  register,
  getCaptcha
} from '../../utils/auth';

export default {
  data() {
    return {
      username: '',
      password: '',
      confirmPassword: '',
      phone: '',
      captcha: '',
      showPassword: false,
      showConfirmPassword: false,
      agreed: false,
      captchaLoading: false,
      captchaImg: '',
      selectedregisterUserType: '1',
      captchaKey: ''
    }
  },
  mounted() {
    this.fetchCaptcha()
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword
    },

    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword
    },

    toggleAgreement() {
      this.agreed = !this.agreed
    },
    selectregisterUserType(lang) {
      this.selectedregisterUserType = lang
    },
    async fetchCaptcha() {
      this.captchaLoading = true
      this.captchaImg = ''
      this.captcha = ''
      try {
        const res = await getCaptcha()
        const d = (res && res.data) ? res.data : res
        this.captchaKey = d.uuid || ''
        this.captchaImg = d.img || ''
      } catch (e) {
        this.captchaImg = ''
      } finally {
        this.captchaLoading = false
      }
    },

    async onRegister() {
      const {
        username,
        password,
        confirmPassword,
        phone,
        agreed,
        captcha,
        captchaKey
      } = this

      if (!username) {
        uni.showToast({
          title: '请输入用户名',
          icon: 'none'
        })
        return
      }

      if (!password) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return
      }

      if (password !== confirmPassword) {
        uni.showToast({
          title: '两次密码不一致',
          icon: 'none'
        })
        return
      }

      if (!phone) {
        uni.showToast({
          title: '请输入手机号',
          icon: 'none'
        })
        return
      }

      if (!/^1[3-9]\d{9}$/.test(phone)) {
        uni.showToast({
          title: '手机号格式不正确',
          icon: 'none'
        })
        return
      }

      if (!agreed) {
        uni.showToast({
          title: '请同意用户协议',
          icon: 'none'
        })
        return
      }

      if (!captcha) {
        uni.showToast({
          title: '请输入图形验证码',
          icon: 'none'
        })
        return
      }

      // 调用注册接口
      uni.showLoading({
        title: '注册中...'
      });
      let userType = this.selectedregisterUserType;
      try {
        const response = await register(username, password, phone, captcha, captchaKey, userType);
        uni.hideLoading();

        if (response.code === 200) {
          uni.showToast({
            title: '注册成功',
            icon: 'success',
            success: () => {
              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            }
          })
        } else {
          uni.showToast({
            title: response.msg || '注册失败',
            icon: 'none'
          })
        }
      } catch (error) {
        uni.hideLoading();
        console.error('注册失败:', error);
        uni.showToast({
          title: '注册失败，请稍后重试',
          icon: 'none'
        })
      }
    },

    goToLogin() {
      uni.navigateBack()
    },

    viewUserAgreement() {
      uni.showModal({
        title: '用户协议',
        content: '医路安心 用户协议\n' +
            '\n' +
            '版本：V1.0　生效日期：2026年3月28日\n' +
            '\n' +
            '一、总则\n' +
            '本协议由您（以下称"用户"）与医路安心平台（以下称"平台"）签订。使用本平台即表示您已阅读并同意本协议全部条款。\n' +
            '\n' +
            '二、账号注册与管理\n' +
            '1. 用户须年满18周岁，或在监护人陪同下使用本平台。\n' +
            '2. 注册时须提供真实、准确、完整的个人信息，如信息变更请及时更新。\n' +
            '3. 账号及密码由用户自行保管，因保管不当导致的损失由用户自行承担。\n' +
            '4. 禁止将账号转让、出借或出售给他人使用。\n' +
            '5. 用户可随时注销账号，注销后账号下的数据将按法规要求处理。\n' +
            '\n' +
            '三、平台服务\n' +
            '1. 平台提供智能分诊、陪诊预约、专家咨询、健康记录管理等服务。\n' +
            '2. 平台服务内容可能因运营需要调整，调整前将提前通知用户。\n' +
            '3. 平台依法运营，不从事违反国家法律法规的业务。\n' +
            '\n' +
            '四、用户行为规范\n' +
            '用户承诺不得：\n' +
            '1. 发布虚假病情或恶意评价，扰乱平台秩序；\n' +
            '2. 冒充他人（包括医生、陪诊师）进行欺诈；\n' +
            '3. 利用平台从事任何违法违规活动；\n' +
            '4. 干扰平台正常运营，攻击平台服务器或数据库；\n' +
            '5. 传播任何违反公序良俗的内容。\n' +
            '\n' +
            '五、个人信息与隐私\n' +
            '1. 平台依照《个人信息保护法》收集和使用用户信息，详见《隐私政策》。\n' +
            '2. 平台不会向第三方出售用户个人信息。\n' +
            '3. 平台采用加密存储、访问控制等措施保障用户数据安全。\n' +
            '4. 用户有权查阅、更正、删除其个人信息。\n' +
            '\n' +
            '六、免责声明\n' +
            '1. 平台提供的医疗健康内容仅供参考，不构成正式医疗建议，具体诊疗请遵从医生意见。\n' +
            '2. 因不可抗力（网络故障、自然灾害、政策变化等）导致的服务中断，平台不承担责任。\n' +
            '3. 用户因违反本协议导致的损失由用户自行承担。\n' +
            '\n' +
            '七、知识产权\n' +
            '平台所有内容（包括但不限于文字、图片、代码、设计）均受知识产权法保护，未经授权不得复制、传播或商业使用。\n' +
            '\n' +
            '八、协议变更\n' +
            '平台有权修改本协议，修改后将在平台内公告，继续使用视为同意修改后的协议。\n' +
            '\n' +
            '九、争议解决\n' +
            '本协议适用中华人民共和国法律。如发生争议，双方应协商解决；协商不成，提交平台注册地有管辖权的人民法院诉讼解决。\n' +
            '\n' +
            '十、联系我们\n' +
            '如有任何问题，请通过平台内"意见反馈"功能联系我们。\n' +
            '\n' +
            '医路安心平台\n' +
            '2026年3月28日',
        showCancel: false
      })
    },

    viewServiceAgreement() {
      uni.showModal({
        title: '服务协议',
        content: '医路安心 陪诊服务协议\n' +
            '\n' +
            '版本：V1.0　生效日期：2026年3月28日\n' +
            '\n' +
            '一、协议主体\n' +
            '本协议由患者用户（以下称"患者"）、陪诊师（以下称"陪诊师"）及医路安心平台（以下称"平台"）三方共同遵守。患者通过平台预约陪诊服务即视为同意本协议。\n' +
            '\n' +
            '二、服务内容\n' +
            '1. 陪同就医：陪诊师陪同患者前往医院，协助挂号、排队、取报告等就医流程。\n' +
            '2. 代办就医：由陪诊师代替患者前往医院办理相关手续（需患者书面授权）。\n' +
            '3. 专科协助：针对特定科室（骨科、脑科、妇产科等）提供专业陪诊服务。\n' +
            '4. 增值服务：包括接送、翻译、健康档案整理等可选服务（按实际套餐约定）。\n' +
            '\n' +
            '三、预约与确认\n' +
            '1. 患者通过平台下单，选择陪诊师、服务类型、就诊时间及医院。\n' +
            '2. 订单提交后，平台在30分钟内完成派单或通知患者。\n' +
            '3. 陪诊师接单后，双方信息互相可见，可通过平台内消息沟通。\n' +
            '4. 就诊前24小时内取消订单，平台将按实际情况处理退款。\n' +
            '\n' +
            '四、费用与支付\n' +
            '1. 服务费用按平台公示价格收取，具体以下单时显示金额为准。\n' +
            '2. 患者通过平台支付，平台按约定周期结算给陪诊师。\n' +
            '3. 发票需求请在下单时备注，平台将协助开具。\n' +
            '4. 因患者原因（迟到超过1小时、临时取消等）导致服务无法进行的，不予退款。\n' +
            '\n' +
            '五、陪诊师职责与规范\n' +
            '陪诊师承诺：\n' +
            '1. 持有效从业资质，通过平台审核方可接单；\n' +
            '2. 按时到达约定地点，准时率不低于95%；\n' +
            '3. 全程尊重患者隐私，不泄露患者病情及个人信息；\n' +
            '4. 不向患者推销任何药品、保健品或医疗器械；\n' +
            '5. 不收取平台规定以外的任何费用；\n' +
            '6. 遇到患者突发状况，第一时间协助就医并通知平台。\n' +
            '\n' +
            '六、患者权利与义务\n' +
            '患者承诺：\n' +
            '1. 如实告知病情及就诊需求，不隐瞒可能影响服务的重要信息；\n' +
            '2. 按时赴约，如需取消请提前24小时通知；\n' +
            '3. 尊重陪诊师，不得对陪诊师实施任何骚扰或侮辱行为；\n' +
            '4. 不要求陪诊师提供超出服务范围的内容（如私下金钱往来）；\n' +
            '5. 如实评价服务，不进行恶意差评。\n' +
            '\n' +
            '七、隐私保护\n' +
            '1. 患者的病历、就诊信息属于敏感个人信息，平台和陪诊师均须严格保密。\n' +
            '2. 陪诊师不得拍摄患者、录音或将患者信息用于任何非服务目的。\n' +
            '3. 平台对服务全程数据加密存储，保留期限符合法规要求。\n' +
            '\n' +
            '八、评价与投诉\n' +
            '1. 服务完成后，患者可对陪诊师进行评分和评价（1-5星）。\n' +
            '2. 如对服务有异议，可在服务完成后72小时内通过平台投诉。\n' +
            '3. 平台在收到投诉后3个工作日内响应处理。\n' +
            '4. 陪诊师累计投诉达到阈值，平台有权暂停或注销其账号。\n' +
            '\n' +
            '九、责任限制\n' +
            '1. 平台作为信息撮合方，不对陪诊师的医疗行为承担连带责任。\n' +
            '2. 因患者自身原因（病情变化、个人决定等）导致的就医结果，平台及陪诊师不承担责任。\n' +
            '3. 陪诊师执行服务期间发生的人身意外，由陪诊师自行购买相关保险保障。\n' +
            '\n' +
            '十、协议终止\n' +
            '1. 患者注销账号或陪诊师通过平台审核注销，本协议自动终止。\n' +
            '2. 任何一方严重违反本协议，平台有权立即终止服务并追究相关责任。\n' +
            '\n' +
            '十一、法律适用与争议解决\n' +
            '本协议适用中华人民共和国法律。如发生纠纷，三方应首先通过平台调解；调解不成，提交平台注册地有管辖权的人民法院处理。\n' +
            '\n' +
            '医路安心平台\n' +
            '2026年3月28日',
        showCancel: false
      })
    }
  }
}
</script>

<style scoped>
.container {
  background-color: #fff;
  min-height: 100vh;
  padding: 60rpx 50rpx;
  box-sizing: border-box;
}

/* Logo区域 */
.logo-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 60rpx;
  margin-bottom: 60rpx;
}

.logo {
  width: 120rpx;
  height: 120rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.3);
}

.logo-icon {
  width: 60rpx;
  height: 60rpx;
  position: relative;
}

.heart {
  width: 100%;
  height: 100%;
  position: relative;
}

.heart::before,
.heart::after {
  content: '';
  position: absolute;
  width: 30rpx;
  height: 48rpx;
  background: #fff;
  border-radius: 30rpx 30rpx 0 0;
  top: 6rpx;
}

.heart::before {
  left: 6rpx;
  transform: rotate(-45deg);
}

.heart::after {
  right: 6rpx;
  transform: rotate(45deg);
}

.heart-inner {
  position: absolute;
  width: 16rpx;
  height: 16rpx;
  background: #fff;
  border-radius: 50%;
  top: 20rpx;
  left: 50%;
  transform: translateX(-50%);
}

.app-name {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}

/* 表单区域 */
.form-section {
  margin-top: 20rpx;
}

.input-group {
  margin-bottom: 30rpx;
}

.input-item {
  display: flex;
  align-items: center;
  height: 100rpx;
  border-bottom: 2rpx solid #e8e8e8;
  margin: auto auto 20rpx;
}

.input-item-inner {
  margin: auto;
  display: flex;
  align-items: center;
  flex: 1;
}

.input-item.captcha-item {
  margin: auto;
  height: 140rpx;
  align-items: flex-start;
}

.captcha-box {
  margin: auto;
  width: 200rpx;
  height: 80rpx;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(77, 208, 225, 0.1);
  border: 2rpx solid rgba(77, 208, 225, 0.3);
  border-radius: 10rpx;
  overflow: hidden;
}

.captcha-box:active {
  background: rgba(77, 208, 225, 0.2);
  border-color: rgba(77, 208, 225, 0.5);
}

.captcha-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8rpx;
}

.captcha-placeholder {
  font-size: 22rpx;
  color: rgba(77, 208, 225, 0.6);
}

.captcha-loading {
  width: 32rpx;
  height: 32rpx;
  border: 3rpx solid rgba(77, 208, 225, 0.3);
  border-top-color: #8db8b6;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.input-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 20rpx;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

.user-icon {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>');
}

.lock-icon {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z"/></svg>');
}

.phone-icon {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M6.62 10.79c1.44 2.83 3.76 5.14 6.59 6.59l2.2-2.2c.27-.27.67-.36 1.02-.24 1.12.37 2.33.57 3.57.57.55 0 1 .45 1 1V20c0 .55-.45 1-1 1-9.39 0-17-7.61-17-17 0-.55.45-1 1-1h3.5c.55 0 1 .45 1 1 0 1.25.2 2.45.57 3.57.11.35.03.74-.25 1.02l-2.2 2.2z"/></svg>');
}

.captcha-icon {
  background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23999"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>');
}

.input-field {
  flex: 1;
  font-size: 30rpx;
  color: #333;
}

/* 语言选择 */
.registerUserType-selector {
  margin-top: 30rpx;
  margin-bottom: 20rpx;
}

.registerUserType-label {
  font-size: 26rpx;
  color: #666;
  margin-bottom: 15rpx;
}

.registerUserType-options {
  display: flex;
  gap: 20rpx;
}

.registerUserType-option {
  flex: 1;
  height: 80rpx;
  border: 2rpx solid #e8e8e8;
  border-radius: 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
  color: #333;
  transition: all 0.3s ease;
}

.registerUserType-option.active {
  border-color: #8db8b6;
  background-color: rgba(77, 208, 225, 0.05);
  color: #8db8b6;
}

.registerUserType-option:active {
  opacity: 0.8;
  transform: scale(0.98);
}

.eye-icon {
  width: 40rpx;
  height: 40rpx;
  padding: 10rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.eye-text {
  font-size: 32rpx;
  line-height: 1;
}

.eye-close {
  opacity: 0.6;
}

.eye-open {
  opacity: 1;
}

/* 注册按钮 */
.register-btn {
  width: 100%;
  height: 90rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 40rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.register-btn::after {
  border: none;
}

.register-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 登录链接 */
.login-link-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40rpx;
}

.login-text {
  font-size: 28rpx;
  color: #999;
}

.login-link {
  font-size: 28rpx;
  color: #8db8b6;
}

/* 底部协议 */
.agreement-section {
  position: fixed;
  bottom: 60rpx;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  padding: 0 50rpx;
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  margin-right: 10rpx;
}

.checkbox {
  width: 28rpx;
  height: 28rpx;
  border: 2rpx solid #ccc;
  border-radius: 50%;
  margin-right: 10rpx;
  position: relative;
}

.checkbox.checked {
  background-color: #8db8b6;
  border-color: #8db8b6;
}

.checkbox.checked::after {
  content: '';
  position: absolute;
  width: 12rpx;
  height: 6rpx;
  border-left: 3rpx solid #fff;
  border-bottom: 3rpx solid #fff;
  transform: rotate(-45deg);
  top: 6rpx;
  left: 6rpx;
}

.agreement-text {
  font-size: 24rpx;
  color: #999;
}

.agreement-link {
  font-size: 24rpx;
  color: #8db8b6;
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