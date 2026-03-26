<template>
  <div class="login-root">
    <canvas ref="canvasRef" class="bg-canvas"></canvas>
    <div class="orb orb-1"></div>
    <div class="orb orb-2"></div>
    <div class="orb orb-3"></div>
    <div class="login-form" :class="{ 'form-ready': cardVisible }">
      <div class="brand">
        <div class="brand-icon">
          <svg viewBox="0 0 40 40" fill="none">
            <circle cx="20" cy="20" r="19" stroke="url(#g1)" stroke-width="2"/>
            <path d="M20 10v20M10 20h20" stroke="url(#g2)" stroke-width="2.5" stroke-linecap="round"/>
            <defs>
              <linearGradient id="g1" x1="0" y1="0" x2="40" y2="40">
                <stop offset="0%" stop-color="#6ee7b7"/>
                <stop offset="100%" stop-color="#3b82f6"/>
              </linearGradient>
              <linearGradient id="g2" x1="0" y1="0" x2="40" y2="40">
                <stop offset="0%" stop-color="#6ee7b7"/>
                <stop offset="100%" stop-color="#3b82f6"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <h3 class="title">医路安心管理控制台</h3>
        <p class="sub-title">Peaceful Journey In Medicine</p>
      </div>
      <form @submit.prevent="handleLogin">
        <div class="form-item" :class="{ error: errors.username }">
          <label>账号</label>
          <div class="input-wrap" :class="{ focused: focusedField === 'username' }">
            <span class="input-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                          stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle
                cx="12" cy="7" r="4"/></svg></span>
            <input v-model="loginForm.username" type="text" placeholder="请输入账号" autocomplete="username"
                   @focus="focusedField='username'" @blur="focusedField=''; validateField('username')"/>
          </div>
          <span v-if="errors.username" class="field-error">{{ errors.username }}</span>
        </div>
        <div class="form-item" :class="{ error: errors.password }">
          <label>密码</label>
          <div class="input-wrap" :class="{ focused: focusedField === 'password' }">
            <span class="input-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                          stroke-width="2"><rect x="3" y="11" width="18" height="11" rx="2"/><path
                d="M7 11V7a5 5 0 0 1 10 0v4"/></svg></span>
            <input v-model="loginForm.password" :type="showPwd ? 'text' : 'password'" placeholder="请输入密码"
                   autocomplete="current-password" @keyup.enter="handleLogin" @focus="focusedField='password'"
                   @blur="focusedField=''; validateField('password')"/>
            <button type="button" class="eye-btn" @click="showPwd = !showPwd">
              <svg v-if="!showPwd" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="2">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                <circle cx="12" cy="12" r="3"/>
              </svg>
              <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path
                    d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19"/>
                <line x1="1" y1="1" x2="23" y2="23"/>
              </svg>
            </button>
          </div>
          <span v-if="errors.password" class="field-error">{{ errors.password }}</span>
        </div>
        <div class="form-item" :class="{ error: errors.captcha }">
          <label>验证码</label>
          <div class="captcha-row">
            <div class="input-wrap captcha-input yanzhengma" :class="{ focused: focusedField === 'captcha' }">
              <span class="input-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                            stroke-width="2"><path
                  d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg></span>
              <input v-model="loginForm.captcha" type="text" placeholder="验证码" maxlength="6"
                     @keyup.enter="handleLogin" @focus="focusedField='captcha'"
                     @blur="focusedField=''; validateField('captcha')"/>
            </div>
            <div class="captcha-box" @click="fetchCaptcha" title="点击刷新">
              <span v-if="captchaLoading"><span class="spinner"></span></span>
              <img v-else-if="captchaImg" :src="'data:image/jpeg;base64,' + captchaImg" class="captcha-img"
                   alt="验证码"/>
              <span v-else class="captcha-placeholder">点击获取</span>
            </div>
          </div>
          <span v-if="errors.captcha" class="field-error">{{ errors.captcha }}</span>
        </div>
        <div class="form-options"><label class="remember-me"><input type="checkbox"
                                                                    v-model="loginForm.rememberMe"/><span>记住密码</span></label>
        </div>
        <div v-if="errorMsg" class="error-alert">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="8" x2="12" y2="12"/>
            <line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
          {{ errorMsg }}
        </div>
        <button type="submit" class="submit-btn" :disabled="submitting"><span v-if="!submitting">登 录</span><span
            v-else class="btn-loading"><span class="spinner"></span> 登 录 中...</span></button>
        <div class="form-footer"><span>还没有账号？</span>
          <router-link to="/register" class="link">立即注册</router-link>
        </div>
      </form>
    </div>
    <div class="page-footer">Copyright &copy; 2026 医路安心 &middot; PJDM All Rights Reserved</div>
  </div>
</template>

<script>
import {getCaptcha, login} from '@/api/auth.js'
import {useTheme} from '@/theme.js'

export default {
  name: 'Login',
  data() {
    return {
      cardVisible: false,
      focusedField: '',
      showPwd: false,
      submitting: false,
      errorMsg: '',
      captchaLoading: false,
      captchaImg: '',
      captchaKey: '',
      loginForm: {
        username: '',
        password: '',
        captcha: '',
        rememberMe: false
      },
      errors: {
        username: '',
        password: '',
        captcha: ''
      },
      animId: null
    }
  },
  mounted() {
    setTimeout(() => {
      this.cardVisible = true
    }, 100)
    this.initCanvas()
    this.fetchCaptcha()
  },
  beforeUnmount() {
    if (this.animId) cancelAnimationFrame(this.animId)
  },
  methods: {
    validateField(field) {
      if (field === 'username') this.errors.username = this.loginForm.username ? '' : '请输入账号'
      else if (field === 'password') this.errors.password = this.loginForm.password ? '' : '请输入密码'
      else if (field === 'captcha') this.errors.captcha = this.loginForm.captcha ? '' : '请输入验证码'
    },
    validate() {
      this.validateField('username')
      this.validateField('password')
      this.validateField('captcha')
      return !this.errors.username && !this.errors.password && !this.errors.captcha
    },
    initCanvas() {
      const canvas = this.$refs.canvasRef
      if (!canvas) return
      const ctx = canvas.getContext('2d')
      const resize = () => {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight
      }
      resize()
      window.addEventListener('resize', resize)
      const pts = Array.from({length: 70}, () => ({
        x: Math.random() * canvas.width, y: Math.random() * canvas.height,
        r: Math.random() * 2 + 0.5, dx: (Math.random() - 0.5) * 0.4,
        dy: (Math.random() - 0.5) * 0.4, alpha: Math.random() * 0.5 + 0.2
      }))
      const draw = () => {
        ctx.clearRect(0, 0, canvas.width, canvas.height)
        for (let i = 0; i < pts.length; i++) {
          for (let j = i + 1; j < pts.length; j++) {
            const dx = pts[i].x - pts[j].x, dy = pts[i].y - pts[j].y
            const d = Math.sqrt(dx * dx + dy * dy)
            if (d < 120) {
              ctx.beginPath()
              ctx.strokeStyle = `rgba(110,231,183,${0.15 * (1 - d / 120)})`
              ctx.lineWidth = 0.6
              ctx.moveTo(pts[i].x, pts[i].y)
              ctx.lineTo(pts[j].x, pts[j].y)
              ctx.stroke()
            }
          }
        }
        pts.forEach(p => {
          ctx.beginPath();
          ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2)
          ctx.fillStyle = `rgba(167,243,208,${p.alpha})`;
          ctx.fill()
          p.x += p.dx;
          p.y += p.dy
          if (p.x < 0 || p.x > canvas.width) p.dx *= -1
          if (p.y < 0 || p.y > canvas.height) p.dy *= -1
        })
        this.animId = requestAnimationFrame(draw)
      }
      draw()
    },
    async fetchCaptcha() {
      this.captchaLoading = true
      this.captchaImg = ''
      this.loginForm.captcha = ''
      try {
        const res = await getCaptcha()
        // auth.js 拦截器返回 response.data，即 R<T>，再取 .data 才是验证码数据
        const d = (res && res.data) ? res.data : res
        this.captchaKey = d.uuid || ''
        this.captchaImg = d.img || ''
      } catch (e) {
        this.captchaImg = ''
      } finally {
        this.captchaLoading = false
      }
    },
    async handleLogin() {
      this.errorMsg = ''
      if (!this.validate()) return
      this.submitting = true
      try {
        const res = await login({
          username: this.loginForm.username,
          password: this.loginForm.password,
          captcha: this.loginForm.captcha,
          captchaKey: this.captchaKey
        })
        // res = R<LoginVO>，取 res.data 得到 LoginVO
        const data = (res && res.data) ? res.data : res
        if (data.token) {
          localStorage.setItem('token', data.token)
          localStorage.setItem('userInfo', JSON.stringify({
            userId: data.userId,
            username: data.username,
            nickName: data.nickName,
            userType: data.userType,
            avatarUrl: data.avatarUrl || ''
          }))
          this.$router.push('/dashboard')
        } else {
          this.errorMsg = res.msg || res.message || '登录失败'
          this.fetchCaptcha()
        }
      } catch (e) {
        this.errorMsg = e?.response?.data?.msg || '网络异常，请稍后重试'
        this.fetchCaptcha()
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@400;500;700&display=swap');

.login-root {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: radial-gradient(ellipse at 20% 50%, #0a2a1f 0%, #071a2e 50%, #050d1a 100%);
  overflow: hidden;
  position: relative;
  font-family: 'Noto Serif SC', serif;
}

.bg-canvas {
  position: absolute;
  inset: 0;
  z-index: 0;
  pointer-events: none;
}

.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  animation: orbFloat 8s ease-in-out infinite;
  pointer-events: none;
  z-index: 0;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(16, 185, 129, 0.18) 0%, transparent 70%);
  top: -100px;
  left: -100px;
}

.yanzhengma {
  width: 50%;
}

.orb-2 {
  width: 350px;
  height: 350px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.15) 0%, transparent 70%);
  bottom: -80px;
  right: -80px;
  animation-delay: -3s;
}

.orb-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, rgba(167, 243, 208, 0.1) 0%, transparent 70%);
  top: 50%;
  right: 25%;
  animation-delay: -6s;
}

@keyframes orbFloat {
  0%, 100% {
    transform: translateY(0) scale(1)
  }
  50% {
    transform: translateY(-30px) scale(1.05)
  }
}

.login-form {
  position: relative;
  z-index: 10;
  width: 420px;
  padding: 40px 40px 32px;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4), 0 1px 0 rgba(255, 255, 255, 0.08) inset;
  opacity: 0;
  transform: translateY(24px);
  transition: opacity 0.6s cubic-bezier(0.16, 1, 0.3, 1), transform 0.6s cubic-bezier(0.16, 1, 0.3, 1);
}

.login-form.form-ready {
  opacity: 1;
  transform: translateY(0);
}

.brand {
  text-align: center;
  margin-bottom: 28px;
}

.brand-icon {
  width: 44px;
  height: 44px;
  margin: 0 auto 10px;
  animation: iconPulse 3s ease-in-out infinite;
}

@keyframes iconPulse {
  0%, 100% {
    transform: scale(1);
    filter: drop-shadow(0 0 6px rgba(110, 231, 183, 0.4))
  }
  50% {
    transform: scale(1.08);
    filter: drop-shadow(0 0 14px rgba(110, 231, 183, 0.7))
  }
}

.title {
  font-size: 20px;
  font-weight: 700;
  color: #ecfdf5;
  letter-spacing: 3px;
  text-shadow: 0 0 20px rgba(110, 231, 183, 0.3);
  margin: 0 0 4px;
}

.sub-title {
  font-size: 11px;
  color: rgba(167, 243, 208, 0.55);
  letter-spacing: 2px;
  font-style: italic;
  margin: 0;
}

form {
  display: flex;
  flex-direction: column;
  gap: 14px;
  margin-top: 4px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.form-item label {
  font-size: 12px;
  color: rgba(167, 243, 208, 0.7);
  letter-spacing: 1px;
}

.input-wrap {
  position: relative;
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  transition: border-color 0.25s, box-shadow 0.25s;
}

.input-wrap.focused {
  border-color: rgba(110, 231, 183, 0.5);
  box-shadow: 0 0 0 3px rgba(110, 231, 183, 0.08);
}

.form-item.error .input-wrap {
  border-color: rgba(239, 68, 68, 0.4);
}

.input-icon {
  padding: 0 10px 0 14px;
  color: rgba(110, 231, 183, 0.5);
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.input-wrap input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  color: #e2fef4;
  font-size: 14px;
  padding: 12px 12px 12px 0;
  font-family: 'Noto Serif SC', serif;
}

.input-wrap input::placeholder {
  color: rgba(255, 255, 255, 0.25);
}

.eye-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  color: rgba(110, 231, 183, 0.5);
  padding: 0 12px;
  display: flex;
  align-items: center;
  transition: color 0.2s;
}

.eye-btn:hover {
  color: rgba(110, 231, 183, 0.9);
}

.field-error {
  font-size: 11px;
  color: #fca5a5;
  padding-left: 2px;
}

.captcha-row {
  display: flex;
  gap: 10px;
}

.captcha-input {
  flex: 1;
}

.captcha-box {
  width: 120px;
  height: 46px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(110, 231, 183, 0.25);
  border-radius: 10px;
  cursor: pointer;
  transition: background 0.2s, border-color 0.2s;
  user-select: none;
}

.captcha-box:hover {
  background: rgba(16, 185, 129, 0.18);
  border-color: rgba(110, 231, 183, 0.5);
}

.captcha-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 9px;
  display: block;
}

.captcha-placeholder {
  font-size: 12px;
  color: rgba(110, 231, 183, 0.5);
}

.spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid rgba(110, 231, 183, 0.3);
  border-top-color: #6ee7b7;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg)
  }
}

.form-options {
  display: flex;
  align-items: center;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: rgba(167, 243, 208, 0.6);
  cursor: pointer;
  user-select: none;
}

.remember-me input[type=checkbox] {
  accent-color: #6ee7b7;
  width: 14px;
  height: 14px;
}

.error-alert {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 14px;
  border-radius: 8px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.25);
  color: #fca5a5;
  font-size: 13px;
}

.submit-btn {
  margin-top: 4px;
  padding: 13px;
  background: linear-gradient(135deg, #059669 0%, #0284c7 100%);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 3px;
  font-family: 'Noto Serif SC', serif;
  transition: opacity 0.2s, transform 0.15s, box-shadow 0.2s;
  box-shadow: 0 4px 20px rgba(5, 150, 105, 0.3);
  width: 100%;
}

.submit-btn:hover:not(:disabled) {
  opacity: 0.92;
  transform: translateY(-1px);
  box-shadow: 0 6px 24px rgba(5, 150, 105, 0.4);
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.form-footer {
  text-align: center;
  margin-top: 4px;
  font-size: 13px;
  color: rgba(255, 255, 255, 0.35);
}

.link {
  color: rgba(110, 231, 183, 0.8);
  text-decoration: none;
  transition: color 0.2s;
}

.link:hover {
  color: #6ee7b7;
}

.page-footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.25);
  font-size: 12px;
  letter-spacing: 1px;
  padding: 12px 0;
  z-index: 5;
}
</style>
