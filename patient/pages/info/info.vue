<template>
  <view class="container">
<!--    <view class="header">-->
<!--      <text class="title">修改个人信息</text>-->
<!--    </view>-->

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
      form: {
        username: '',
        nickName: '',
        realName: '',
        phone: '',
        email: '',
        gender: 0,
        age: '',
        avatarUrl: ''
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
            username: d.username || '',
            nickName: d.nickName || '',
            realName: d.realName || '',
            phone: d.phone || '',
            email: d.email || '',
            gender: d.gender != null ? Number(d.gender) : 0,
            age: d.age || '',
            avatarUrl: getFileUrl(d.avatarUrl)
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
    async saveInfo() {
      if (!this.userId) {
        uni.showToast({ title: '请先登录', icon: 'none' });
        return;
      }
      this.saving = true;
      try {
        const payload = {
          nickName: this.form.nickName,
          realName: this.form.realName,
          email: this.form.email,
          gender: this.form.gender,
          age: this.form.age ? Number(this.form.age) : null,
          avatarUrl: this._rawAvatarUrl || (this.form.avatarUrl ? this.form.avatarUrl.replace('http://localhost:8080/common/download?name=', '') : null)
        };
        const res = await request(`/user/${this.userId}`, {
          method: 'PUT',
          data: payload
        });
        if (res && (res.code === 200 || res.code === 1)) {
          uni.showToast({ title: '保存成功', icon: 'success' });
          // 更新本地缓存
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

.header {
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  padding: 50rpx 30rpx 30rpx;
  text-align: center;
}

.title {
  font-size: 34rpx;
  font-weight: 600;
  color: #fff;
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
  background: #4DD0E1;
  color: #fff;
  border-color: #4DD0E1;
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
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.save-btn::after {
  border: none;
}
</style>
