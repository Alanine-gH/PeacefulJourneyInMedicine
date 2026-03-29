<template>
  <view class="add-patient-page">
  <!--  <view class="nav-bar">
      <view class="back-btn" @click="goBack">-->
<!--        <text class="back-arrow">‹</text>-->
<!--      </view>
      <text class="nav-title">添加就诊人</text>
      <view class="placeholder"></view>
    </view> -->

    <view class="form-wrap">
      <view class="form-item">
        <text class="form-label">姓名</text>
        <input class="form-input" placeholder="请输入真实姓名" v-model="form.name"/>
      </view>
      <view class="form-item">
        <text class="form-label">手机号</text>
        <input class="form-input" type="number" placeholder="请输入手机号" v-model="form.phone" maxlength="11"/>
      </view>
      <view class="form-item">
        <text class="form-label">身份证号</text>
        <input class="form-input" placeholder="请输入身份证号" v-model="form.idCard" maxlength="18"/>
      </view>
      <view class="form-item">
        <text class="form-label">与本人关系</text>
        <view class="relation-group">
          <view
              class="relation-tag"
              v-for="r in relations" :key="r"
              :class="{ active: form.relation === r }"
              @click="form.relation = r"
          >
            <text>{{ r }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="bottom-section">
      <button class="submit-btn" @click="submitAdd" :disabled="submitting">{{ submitting ? '保存中...' : '保存' }}
      </button>
    </view>
  </view>
</template>

<script>
import {addPatient} from '@/utils/patient-api.js'

export default {
  data() {
    return {
      submitting: false,
      relations: ['本人', '父母', '子女', '配偶', '兄弟姐妹', '其他'],
      form: {
        name: '',
        phone: '',
        idCard: '',
        relation: '本人'
      }
    }
  },
  methods: {
    // goBack() {
    //   uni.navigateBack()
    // },
    async submitAdd() {
      const {name, phone, idCard, relation} = this.form
      if (!name.trim()) {
        uni.showToast({title: '请输入姓名', icon: 'none'});
        return
      }
      if (!phone || !/^1[3-9]\d{9}$/.test(phone)) {
        uni.showToast({title: '请输入正确的手机号', icon: 'none'});
        return
      }
      if (!idCard.trim()) {
        uni.showToast({title: '请输入身份证号', icon: 'none'});
        return
      }

      this.submitting = true
      uni.showLoading({title: '保存中...'})
      try {
        const userId = uni.getStorageSync('userId')
        const res = await addPatient({
          userId,
          name: name.trim(),
          phone,
          idCard: idCard.trim(),
          relation
        })
        uni.hideLoading()
        if (res && (res.code === 200 || res.code === 1)) {
          uni.showToast({title: '添加成功', icon: 'success'})
          setTimeout(() => {
            // 返回就诊人列表页并刷新
            const pages = getCurrentPages()
            const prevPage = pages[pages.length - 2]
            if (prevPage && prevPage.$vm && prevPage.$vm.loadPatients) {
              prevPage.$vm.loadPatients()
            }
            uni.navigateBack()
          }, 1200)
        } else {
          uni.showToast({title: (res && res.msg) || '添加失败，请重试', icon: 'none'})
        }
      } catch (e) {
        uni.hideLoading()
        console.error('添加就诊人失败:', e)
        uni.showToast({title: '网络错误，请重试', icon: 'none'})
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style lang="scss">
.add-patient-page {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 120rpx;
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0;
  background: linear-gradient(135deg, #4DD0E1, #26C6DA);
}
/*
.back-btn {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.back-arrow {
  font-size: 40rpx;
  color: #fff;
}
*/
.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

.form-wrap {
  padding: 20rpx;
}

.form-item {
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 16rpx;
}

.form-label {
  font-size: 28rpx;
  color: #666;
  display: block;
  margin-bottom: 16rpx;
}

.form-input {
  font-size: 30rpx;
  color: #333;
  width: 100%;
  border-bottom: 1rpx solid #eee;
  padding-bottom: 12rpx;
}

.relation-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.relation-tag {
  padding: 12rpx 28rpx;
  border-radius: 30rpx;
  background: #f5f5f5;
  border: 2rpx solid transparent;
}

.relation-tag text {
  font-size: 28rpx;
  color: #666;
}

.relation-tag.active {
  background: #e3f2fd;
  border-color: #2196F3;
}

.relation-tag.active text {
  color: #2196F3;
  font-weight: 500;
}

.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  background: linear-gradient(135deg, #2196F3, #1976D2);
  color: #fff;
  font-size: 32rpx;
  font-weight: 500;
  border-radius: 45rpx;
  border: none;
}

.submit-btn::after {
  border: none;
}

.submit-btn[disabled] {
  background: #ccc;
  color: #999;
}
</style>
