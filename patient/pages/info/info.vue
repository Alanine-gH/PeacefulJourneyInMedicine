<template>
  <view class="container">
    <view class="form-wrap">
      <!-- 头像 -->
      <view class="avatar-row" @click="chooseAvatar">
        <view class="avatar-box">
          <image v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar-img" mode="aspectFill"/>
          <text v-else class="avatar-placeholder">👤</text>
        </view>
        <text class="avatar-tip">点击更换头像</text>
        <text class="arrow">›</text>
      </view>

      <!-- 用户名（只读） -->
      <view class="form-item">
        <text class="label">用户名</text>
        <input class="input readonly" :value="form.username" disabled placeholder="用户名"/>
      </view>

      <!-- 昵称 -->
      <view class="form-item">
        <text class="label">昵称</text>
        <input class="input" v-model="form.nickName" placeholder="请输入昵称" maxlength="20"/>
      </view>

      <!-- 真实姓名 -->
      <view class="form-item">
        <text class="label">真实姓名</text>
        <input class="input" v-model="form.realName" placeholder="请输入真实姓名" maxlength="20"/>
      </view>

      <!-- 手机号（只读） -->
      <view class="form-item">
        <text class="label">手机号</text>
        <input class="input readonly" :value="form.phone" disabled placeholder="手机号"/>
      </view>

      <!-- 邮箱 -->
      <view class="form-item">
        <text class="label">邮箱</text>
        <input class="input" v-model="form.email" placeholder="请输入邮箱" type="email" maxlength="50"/>
      </view>

      <!-- 性别 -->
      <view class="form-item">
        <text class="label">性别</text>
        <view class="radio-group">
          <view class="radio-item" :class="{active: form.gender === 1}" @click="form.gender = 1">男</view>
          <view class="radio-item" :class="{active: form.gender === 2}" @click="form.gender = 2">女</view>
          <view class="radio-item" :class="{active: form.gender === 0}" @click="form.gender = 0">保密</view>
        </view>
      </view>

      <!-- 年龄 -->
      <view class="form-item">
        <text class="label">年龄</text>
        <input class="input" v-model.number="form.age" placeholder="请输入年龄" type="number" maxlength="3"/>
      </view>

      <!-- 国家/地区 -->
      <view class="form-item" @click="showCountryPicker = true">
        <text class="label">国家/地区</text>
        <view class="picker-value">
          <text class="picker-text">{{ form.countryCode ? countryLabel(form.countryCode) : '请选择' }}</text>
          <text class="arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 国家选择弹窗 -->
    <view v-if="showCountryPicker" class="picker-mask" @click.self="showCountryPicker = false">
      <view class="picker-panel">
        <view class="picker-header">
          <text class="picker-title">选择国家/地区</text>
          <text class="picker-close" @click="showCountryPicker = false">✕</text>
        </view>
        <scroll-view class="picker-scroll" scroll-y="true">
          <view
            v-for="item in countryList"
            :key="item.code + item.name"
            class="picker-item"
            :class="{ 'picker-item-active': form.countryCode === item.code && countryLabel(item.code) === (item.name + ' ' + item.code) }"
            @click="selectCountry(item)"
          >
            <text class="picker-item-name">{{ item.name }}</text>
            <text class="picker-item-code">{{ item.code }}</text>
          </view>
        </scroll-view>
      </view>
    </view>

    <view class="bottom-bar">
      <button class="save-btn" :disabled="saving" @click="saveInfo">
        {{ saving ? '保存中...' : '保存修改' }}
      </button>
    </view>
  </view>
</template>

<script>
import { getUserInfo } from '@/utils/auth.js';
import { request, getFileUrl } from '@/utils/config.js';

export default {
  data() {
    return {
      userId: null,
      saving: false,
      showCountryPicker: false,
      countryList: [
        { code: '+86',  name: '中国大陆' },
        { code: '+852', name: '香港' },
        { code: '+853', name: '澳门' },
        { code: '+886', name: '台湾' },
        { code: '+1',   name: '美国/加拿大' },
        { code: '+44',  name: '英国' },
        { code: '+81',  name: '日本' },
        { code: '+82',  name: '韩国' },
        { code: '+61',  name: '澳大利亚' },
        { code: '+49',  name: '德国' },
        { code: '+33',  name: '法国' },
        { code: '+7',   name: '俄罗斯' },
        { code: '+65',  name: '新加坡' },
        { code: '+60',  name: '马来西亚' },
        { code: '+66',  name: '泰国' },
        { code: '+84',  name: '越南' },
        { code: '+62',  name: '印度尼西亚' },
        { code: '+91',  name: '印度' },
        { code: '+55',  name: '巴西' },
        { code: '+34',  name: '西班牙' },
        { code: '+39',  name: '意大利' },
        { code: '+31',  name: '荷兰' },
        { code: '+41',  name: '瑞士' },
        { code: '+46',  name: '瑞典' },
        { code: '+47',  name: '挪威' },
        { code: '+45',  name: '丹麦' },
        { code: '+358', name: '芬兰' },
        { code: '+48',  name: '波兰' },
        { code: '+20',  name: '埃及' },
        { code: '+27',  name: '南非' },
        { code: '+234', name: '尼日利亚' },
        { code: '+971', name: '阿联酋' },
        { code: '+966', name: '沙特阿拉伯' },
        { code: '+972', name: '以色列' },
        { code: '+90',  name: '土耳其' },
        { code: '+380', name: '乌克兰' },
        { code: '+64',  name: '新西兰' },
        { code: '+52',  name: '墨西哥' },
        { code: '+54',  name: '阿根廷' }
      ],
      form: {
        username: '',
        nickName: '',
        realName: '',
        phone: '',
        email: '',
        gender: 0,
        age: '',
        avatarUrl: '',
        countryCode: ''
      }
    }
  },
  onLoad() {
    this.userId = uni.getStorageSync('userId');
    this.loadUserInfo();
  },
  methods: {
    async loadUserInfo() {
      try {
        const res = await getUserInfo();
        if (res && res.code === 200 && res.data) {
          const d = res.data;
          this.form = {
            username:    d.username  || '',
            nickName:    d.nickName  || '',
            realName:    d.realName  || '',
            phone:       d.phone     || '',
            email:       d.email     || '',
            gender:      d.gender != null ? Number(d.gender) : 0,
            age:         d.age       || '',
            avatarUrl:   getFileUrl(d.avatarUrl),
            countryCode: d.countryCode || ''
          };
        }
      } catch (e) {
        uni.showToast({ title: '加载用户信息失败', icon: 'none' });
      }
    },
    async chooseAvatar() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: async (res) => {
          const tempPath = res.tempFilePaths[0];
          uni.showLoading({ title: '上传中...' });
          try {
            const uploadRes = await new Promise((resolve, reject) => {
              uni.uploadFile({
                url: 'http://localhost:8080/common/upload',
                filePath: tempPath,
                name: 'file',
                header: { Authorization: 'Bearer ' + uni.getStorageSync('token') },
                success: (r) => resolve(JSON.parse(r.data)),
                fail: reject
              });
            });
            if (uploadRes && (uploadRes.code === 200 || uploadRes.code === 1)) {
              this.form.avatarUrl = getFileUrl(uploadRes.data);
              this._rawAvatarUrl = uploadRes.data;
            } else {
              uni.showToast({ title: '上传失败', icon: 'none' });
            }
          } catch (e) {
            uni.showToast({ title: '上传失败', icon: 'none' });
          } finally {
            uni.hideLoading();
          }
        }
      });
    },
    countryLabel(code) {
      const found = this.countryList.find(c => c.code === code);
      return found ? `${found.name} ${found.code}` : code;
    },
    selectCountry(item) {
      this.form.countryCode = item.code;
      this.showCountryPicker = false;
    },
    async saveInfo() {
      if (!this.userId) {
        uni.showToast({ title: '请先登录', icon: 'none' });
        return;
      }
      this.saving = true;
      try {
        const payload = {
          nickName:    this.form.nickName,
          realName:    this.form.realName,
          email:       this.form.email,
          gender:      this.form.gender,
          age:         this.form.age ? Number(this.form.age) : null,
          avatarUrl:   this._rawAvatarUrl || (this.form.avatarUrl ? this.form.avatarUrl.replace('http://localhost:8080/common/download?name=', '') : null),
          countryCode: this.form.countryCode || null
        };
        const res = await request(`/user/${this.userId}`, {
          method: 'PUT',
          data: payload
        });
        if (res && (res.code === 200 || res.code === 1)) {
          uni.showToast({ title: '保存成功', icon: 'success' });
          const cached = uni.getStorageSync('userInfo') || {};
          uni.setStorageSync('userInfo', { ...cached, ...payload, avatarUrl: this._rawAvatarUrl || cached.avatarUrl });
          setTimeout(() => uni.navigateBack(), 1000);
        } else {
          uni.showToast({ title: (res && res.msg) || '保存失败', icon: 'none' });
        }
      } catch (e) {
        uni.showToast({ title: '网络错误，请重试', icon: 'none' });
      } finally {
        this.saving = false;
      }
    }
  }
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 130rpx;
}

.form-wrap {
  margin: 24rpx 24rpx 0;
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06);
}

.avatar-row {
  display: flex;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.avatar-box {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: #e8f5f6;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  margin-right: 20rpx;
}

.avatar-img {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
}

.avatar-placeholder {
  font-size: 52rpx;
}

.avatar-tip {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.arrow {
  font-size: 36rpx;
  color: #ccc;
}

.form-item {
  display: flex;
  align-items: center;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.form-item:last-child {
  border-bottom: none;
}

.label {
  width: 160rpx;
  font-size: 28rpx;
  color: #555;
  flex-shrink: 0;
}

.input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
  text-align: right;
}

.readonly {
  color: #999;
}

.radio-group {
  display: flex;
  gap: 16rpx;
  flex: 1;
  justify-content: flex-end;
}

.radio-item {
  padding: 8rpx 28rpx;
  border-radius: 30rpx;
  border: 1rpx solid #ddd;
  font-size: 26rpx;
  color: #666;
  background: #f9f9f9;
}

.radio-item.active {
  background: #8db8b6;
  color: #fff;
  border-color: #8db8b6;
}

/* 国家选择行 */
.picker-value {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8rpx;
}

.picker-text {
  font-size: 28rpx;
  color: #333;
}

/* 国家选择弹窗 */
.picker-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.45);
  z-index: 999;
  display: flex;
  align-items: flex-end;
}

.picker-panel {
  width: 100%;
  background: #fff;
  border-radius: 24rpx 24rpx 0 0;
  max-height: 70vh;
  display: flex;
  flex-direction: column;
}

.picker-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 30rpx 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
  flex-shrink: 0;
}

.picker-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
}

.picker-close {
  font-size: 32rpx;
  color: #999;
  padding: 10rpx;
}

.picker-scroll {
  flex: 1;
  height: 60vh;
}

.picker-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.picker-item-active {
  background: #e8f9fb;
}

.picker-item-name {
  font-size: 28rpx;
  color: #333;
}

.picker-item-code {
  font-size: 26rpx;
  color: #8db8b6;
  font-weight: 500;
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 24rpx 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0,0,0,0.06);
}

.save-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #8db8b6 0%, #a8cece 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.save-btn::after {
  border: none;
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