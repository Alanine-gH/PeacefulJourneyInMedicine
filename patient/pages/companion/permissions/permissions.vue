<template>
  <view class="container">
    <!-- 头部 -->
 <!--   <view class="header">
      <view class="header-content">
        <view class="header-left"></view>
        <view class="header-title">权限管理</view>
        <view class="header-right"></view>
      </view>
    </view> -->

    <!-- 权限列表 -->
    <view class="permissions-section">
      <view class="section-title">应用权限</view>
      
      <!-- 位置权限 -->
      <view class="permission-item">
        <view class="permission-info">
          <view class="permission-icon location-icon">📍</view>
          <view class="permission-details">
            <view class="permission-name">位置信息</view>
            <view class="permission-desc">用于获取您的位置，提供更精准的服务</view>
          </view>
        </view>
        <switch 
          class="permission-switch" 
          :checked="permissions.location" 
          @change="togglePermission('location', $event)"
          color="#4DD0E1"
        />
      </view>

      <!-- 相机权限 -->
      <view class="permission-item">
        <view class="permission-info">
          <view class="permission-icon camera-icon">📷</view>
          <view class="permission-details">
            <view class="permission-name">相机</view>
            <view class="permission-desc">用于拍摄照片、上传证件等</view>
          </view>
        </view>
        <switch 
          class="permission-switch" 
          :checked="permissions.camera" 
          @change="togglePermission('camera', $event)"
          color="#4DD0E1"
        />
      </view>

      <!-- 相册权限 -->
      <view class="permission-item">
        <view class="permission-info">
          <view class="permission-icon album-icon">🖼️</view>
          <view class="permission-details">
            <view class="permission-name">相册</view>
            <view class="permission-desc">用于选择照片上传</view>
          </view>
        </view>
        <switch 
          class="permission-switch" 
          :checked="permissions.album" 
          @change="togglePermission('album', $event)"
          color="#4DD0E1"
        />
      </view>

      <!-- 麦克风权限 -->
      <view class="permission-item">
        <view class="permission-info">
          <view class="permission-icon microphone-icon">🎤</view>
          <view class="permission-details">
            <view class="permission-name">麦克风</view>
            <view class="permission-desc">用于语音通话、语音输入</view>
          </view>
        </view>
        <switch 
          class="permission-switch" 
          :checked="permissions.microphone" 
          @change="togglePermission('microphone', $event)"
          color="#4DD0E1"
        />
      </view>

      <!-- 通知权限 -->
      <view class="permission-item">
        <view class="permission-info">
          <view class="permission-icon notification-icon">🔔</view>
          <view class="permission-details">
            <view class="permission-name">通知</view>
            <view class="permission-desc">用于接收订单、消息提醒</view>
          </view>
        </view>
        <switch 
          class="permission-switch" 
          :checked="permissions.notification" 
          @change="togglePermission('notification', $event)"
          color="#4DD0E1"
        />
      </view>

      <!-- 存储权限 -->
      <view class="permission-item">
        <view class="permission-info">
          <view class="permission-icon storage-icon">💾</view>
          <view class="permission-details">
            <view class="permission-name">存储</view>
            <view class="permission-desc">用于保存数据、缓存文件</view>
          </view>
        </view>
        <switch 
          class="permission-switch" 
          :checked="permissions.storage" 
          @change="togglePermission('storage', $event)"
          color="#4DD0E1"
        />
      </view>
    </view>

    <!-- 隐私设置 -->
    <view class="privacy-section">
      <view class="section-title">隐私设置</view>
      
      <view class="privacy-item" @click="showPrivacyPolicy">
        <view class="privacy-info">
          <view class="privacy-icon">📄</view>
          <view class="privacy-name">隐私政策</view>
        </view>
        <view class="privacy-arrow">›</view>
      </view>

      <view class="privacy-item" @click="showUserAgreement">
        <view class="privacy-info">
          <view class="privacy-icon">📋</view>
          <view class="privacy-name">用户协议</view>
        </view>
        <view class="privacy-arrow">›</view>
      </view>

      <view class="privacy-item" @click="clearCache">
        <view class="privacy-info">
          <view class="privacy-icon">🗑️</view>
          <view class="privacy-name">清除缓存</view>
        </view>
        <view class="privacy-value">{{ cacheSize }}</view>
      </view>
    </view>

    <!-- 保存按钮 -->
    <button class="save-btn" @click="savePermissions">保存设置</button>

    <!-- 底部空白区域 -->
    <view class="bottom-space"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      permissions: {
        location: true,
        camera: true,
        album: true,
        microphone: false,
        notification: true,
        storage: true
      },
      cacheSize: '12.5MB'
    }
  },
  onLoad() {
    this.loadPermissions()
    this.calculateCacheSize()
  },
  methods: {
    navigateBack() {
      uni.navigateBack()
    },

    loadPermissions() {
      // 从本地存储加载权限设置
      const savedPermissions = uni.getStorageSync('companion_permissions')
      if (savedPermissions) {
        this.permissions = { ...this.permissions, ...savedPermissions }
      }
    },

    togglePermission(permissionKey, event) {
      const value = event.detail.value
      this.permissions[permissionKey] = value
      
      // 根据权限类型请求系统权限
      switch(permissionKey) {
        case 'location':
          if (value) {
            this.requestLocationPermission()
          }
          break
        case 'camera':
          if (value) {
            this.requestCameraPermission()
          }
          break
        case 'album':
          if (value) {
            this.requestAlbumPermission()
          }
          break
        case 'microphone':
          if (value) {
            this.requestMicrophonePermission()
          }
          break
        case 'notification':
          if (value) {
            this.requestNotificationPermission()
          }
          break
      }
    },

    requestLocationPermission() {
      uni.getLocation({
        success: () => {
          uni.showToast({ title: '位置权限已开启', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '位置权限获取失败', icon: 'none' })
          this.permissions.location = false
        }
      })
    },

    requestCameraPermission() {
      uni.chooseImage({
        count: 1,
        sourceType: ['camera'],
        success: () => {
          uni.showToast({ title: '相机权限已开启', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '相机权限获取失败', icon: 'none' })
          this.permissions.camera = false
        }
      })
    },

    requestAlbumPermission() {
      uni.chooseImage({
        count: 1,
        sourceType: ['album'],
        success: () => {
          uni.showToast({ title: '相册权限已开启', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '相册权限获取失败', icon: 'none' })
          this.permissions.album = false
        }
      })
    },

    requestMicrophonePermission() {
      uni.getRecorderManager()
      uni.showToast({ title: '麦克风权限设置成功', icon: 'success' })
    },

    requestNotificationPermission() {
      uni.requestSubscribeMessage({
        tmplIds: [''],
        success: () => {
          uni.showToast({ title: '通知权限已开启', icon: 'success' })
        },
        fail: () => {
          uni.showToast({ title: '通知权限获取失败', icon: 'none' })
          this.permissions.notification = false
        }
      })
    },

    savePermissions() {
      // 保存权限设置到本地存储
      uni.setStorageSync('companion_permissions', this.permissions)
      uni.showToast({
        title: '权限设置已保存',
        icon: 'success',
        success: () => {
          setTimeout(() => {
            uni.navigateBack()
          }, 1500)
        }
      })
    },

    showPrivacyPolicy() {
      uni.showModal({
        title: '隐私政策',
        content: '我们尊重并保护您的隐私。本应用收集的个人信息仅用于提供服务，不会用于其他目的。我们采用加密技术保护您的数据安全。',
        confirmText: '我知道了',
        confirmColor: '#4DD0E1'
      })
    },

    showUserAgreement() {
      uni.showModal({
        title: '用户协议',
        content: '欢迎使用本应用。使用本应用即表示您同意遵守本协议的所有条款。您应确保提供的信息真实、准确、完整。',
        confirmText: '我知道了',
        confirmColor: '#4DD0E1'
      })
    },

    calculateCacheSize() {
      // 计算缓存大小
      try {
        const info = uni.getStorageInfoSync()
        const size = (info.currentSize / 1024).toFixed(1)
        this.cacheSize = size + 'MB'
      } catch (e) {
        this.cacheSize = '0MB'
      }
    },

    clearCache() {
      uni.showModal({
        title: '清除缓存',
        content: '确定要清除所有缓存数据吗？',
        confirmText: '清除',
        confirmColor: '#F44336',
        success: (res) => {
          if (res.confirm) {
            try {
              uni.clearStorageSync()
              this.cacheSize = '0MB'
              uni.showToast({
                title: '缓存已清除',
                icon: 'success'
              })
            } catch (e) {
              uni.showToast({
                title: '清除失败',
                icon: 'none'
              })
            }
          }
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
}

/* 头部 */
.header {
  background-color: #4DD0E1;
  padding: 40rpx 30rpx 20rpx;
  position: relative;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
}

.header-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-icon {
  font-size: 40rpx;
  color: #fff;
  font-weight: bold;
}

.header-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #fff;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.header-right {
  width: 60rpx;
}

/* 权限区域 */
.permissions-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 2rpx solid #f0f0f0;
}

.permission-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.permission-item:last-child {
  border-bottom: none;
}

.permission-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.permission-icon {
  width: 80rpx;
  height: 80rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-right: 20rpx;
}

.location-icon {
  background-color: rgba(77, 208, 225, 0.1);
}

.camera-icon {
  background-color: rgba(255, 193, 7, 0.1);
}

.album-icon {
  background-color: rgba(156, 39, 176, 0.1);
}

.microphone-icon {
  background-color: rgba(244, 67, 54, 0.1);
}

.notification-icon {
  background-color: rgba(255, 152, 0, 0.1);
}

.storage-icon {
  background-color: rgba(76, 175, 80, 0.1);
}

.permission-details {
  flex: 1;
}

.permission-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
  margin-bottom: 8rpx;
}

.permission-desc {
  font-size: 24rpx;
  color: #999;
  line-height: 1.4;
}

.permission-switch {
  transform: scale(0.8);
}

/* 隐私设置区域 */
.privacy-section {
  background: #fff;
  margin: 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.privacy-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.privacy-item:last-child {
  border-bottom: none;
}

.privacy-info {
  display: flex;
  align-items: center;
}

.privacy-icon {
  width: 60rpx;
  height: 60rpx;
  border-radius: 15rpx;
  background-color: rgba(77, 208, 225, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
  margin-right: 20rpx;
}

.privacy-name {
  font-size: 30rpx;
  font-weight: 500;
  color: #333;
}

.privacy-arrow {
  font-size: 32rpx;
  color: #ccc;
}

.privacy-value {
  font-size: 28rpx;
  color: #999;
}

/* 保存按钮 */
.save-btn {
  width: calc(100% - 60rpx);
  height: 90rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
  margin: 40rpx 30rpx;
  box-shadow: 0 8rpx 24rpx rgba(77, 208, 225, 0.4);
}

.save-btn::after {
  border: none;
}

.save-btn:active {
  opacity: 0.9;
  transform: scale(0.98);
}

/* 底部空白区域 */
.bottom-space {
  height: 120rpx;
  width: 100%;
}

/* 响应式调整 */
@media (max-width: 375px) {
  .permissions-section,
  .privacy-section {
    margin: 15rpx;
    padding: 20rpx;
  }
  
  .permission-icon {
    width: 70rpx;
    height: 70rpx;
    font-size: 36rpx;
  }
  
  .permission-name {
    font-size: 28rpx;
  }
  
  .permission-desc {
    font-size: 22rpx;
  }
}
</style>