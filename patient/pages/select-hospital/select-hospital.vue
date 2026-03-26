<template>
  <view class="select-hospital-page">
    <view class="nav-bar">
      <text class="nav-title">选择医院</text>
      <view class="placeholder"></view>
    </view>

    <!-- 城市选择栏 -->
    <view class="city-bar" @click="selectCity">
      <text class="city-text">{{ currentCity }}</text>
      <text class="city-arrow">▼</text>
    </view>

    <!-- 医院列表 -->
    <view class="hospital-list">
      <view
          class="hospital-item"
          v-for="(item, index) in hospitalList"
          :key="index"
          :class="{ active: selectedHospital === item.name }"
          @click="selectHospital(item)"
      >
        <image class="hospital-image" :src="item.image" mode="aspectFill"></image>
        <view class="hospital-info">
          <text class="hospital-name">{{ item.name }}</text>
          <view class="hospital-tags">
            <text class="tag">{{ item.level }}</text>
            <text class="tag">{{ item.type }}</text>
          </view>
          <text class="hospital-distance">距离：{{ item.distance }}</text>
        </view>
        <view class="hospital-check" v-if="selectedHospital === item.name">
          <text class="check-icon">✓</text>
        </view>
      </view>
    </view>

    <view class="bottom-section">
      <button class="btn btn-cancel" @click="goBack">取消</button>
      <button class="btn btn-confirm" @click="confirmHospital">确定</button>
    </view>
  </view>
</template>

<script>
import {getHospitals, searchHospitals} from '@/utils/medical-api.js'

export default {
  data() {
    return {
      currentCity: '北京市',
      keyword: '',
      loading: false,
      // selectedHospital: null,
      hospitalList: [],
      searchTimer: null,
      selectedHospital: '',
      hospitalData: {
        '北京市': [
          {
            name: '北京市第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {name: '弘爱医院', level: '三甲', type: '综合医院', distance: '11.2公里', image: '/static/hospital2.png'},
          {
            name: '北京大学附属中医院',
            level: '三甲',
            type: '中医医院',
            distance: '567米',
            image: '/static/hospital3.png'
          }
        ],
        '上海市': [
          {
            name: '上海市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '2.5公里',
            image: '/static/hospital1.png'
          },
          {name: '华山医院', level: '三甲', type: '综合医院', distance: '8.6公里', image: '/static/hospital2.png'},
          {name: '瑞金医院', level: '三甲', type: '综合医院', distance: '1.2公里', image: '/static/hospital3.png'}
        ],
        '广州市': [
          {
            name: '广州市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '中山大学附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.8公里',
            image: '/static/hospital2.png'
          },
          {name: '广东省中医院', level: '三甲', type: '中医医院', distance: '3.1公里', image: '/static/hospital3.png'}
        ],
        '武汉市': [
          {
            name: '武汉市第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '2.8公里',
            image: '/static/hospital1.png'
          },
          {name: '协和医院', level: '三甲', type: '综合医院', distance: '5.5公里', image: '/static/hospital2.png'},
          {name: '同济医院', level: '三甲', type: '综合医院', distance: '7.2公里', image: '/static/hospital3.png'}
        ],
        '南昌市': [
          {
            name: '南昌市第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.5公里',
            image: '/static/hospital1.png'
          },
          {
            name: '江西省人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.8公里',
            image: '/static/hospital2.png'
          },
          {
            name: '南昌大学附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '2.1公里',
            image: '/static/hospital3.png'
          }
        ],
        '长沙市': [
          {name: '湘雅医院', level: '三甲', type: '综合医院', distance: '4.5公里', image: '/static/hospital1.png'},
          {
            name: '湖南省人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.2公里',
            image: '/static/hospital2.png'
          },
          {name: '长沙市第一医院', level: '三甲', type: '综合医院', distance: '1.8公里', image: '/static/hospital3.png'}
        ],
        '西安市': [
          {name: '西京医院', level: '三甲', type: '综合医院', distance: '5.5公里', image: '/static/hospital1.png'},
          {
            name: '西安交通大学附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.8公里',
            image: '/static/hospital2.png'
          },
          {name: '西安市第一医院', level: '三甲', type: '综合医院', distance: '3.2公里', image: '/static/hospital3.png'}
        ],
        '青岛市': [
          {name: '青岛市立医院', level: '三甲', type: '综合医院', distance: '4.8公里', image: '/static/hospital1.png'},
          {
            name: '青岛大学附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.5公里',
            image: '/static/hospital2.png'
          },
          {name: '青岛市中医院', level: '三甲', type: '中医医院', distance: '2.9公里', image: '/static/hospital3.png'}
        ],
        '深圳市': [
          {
            name: '深圳市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '北京大学深圳医院',
            level: '三甲',
            type: '综合医院',
            distance: '8.1公里',
            image: '/static/hospital2.png'
          },
          {name: '深圳市中医院', level: '三甲', type: '中医医院', distance: '4.5公里', image: '/static/hospital3.png'}
        ],
        '济南市': [
          {name: '山东省立医院', level: '三甲', type: '综合医院', distance: '3.5公里', image: '/static/hospital1.png'},
          {name: '齐鲁医院', level: '三甲', type: '综合医院', distance: '5.2公里', image: '/static/hospital2.png'},
          {name: '济南市中医院', level: '三甲', type: '中医医院', distance: '2.8公里', image: '/static/hospital3.png'}
        ],
        '烟台市': [
          {
            name: '烟台毓璜顶医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '烟台市烟台山医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.8公里',
            image: '/static/hospital2.png'
          },
          {name: '烟台市中医院', level: '三甲', type: '中医医院', distance: '3.1公里', image: '/static/hospital3.png'}
        ],
        '潍坊市': [
          {
            name: '潍坊市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {name: '潍坊市中医院', level: '三甲', type: '中医医院', distance: '5.5公里', image: '/static/hospital2.png'},
          {
            name: '潍坊医学院附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '2.2公里',
            image: '/static/hospital3.png'
          }
        ],
        '临沂市': [
          {
            name: '临沂市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.5公里',
            image: '/static/hospital1.png'
          },
          {name: '临沂市中医院', level: '三甲', type: '中医医院', distance: '6.2公里', image: '/static/hospital2.png'},
          {name: '临沂市中心医院', level: '三甲', type: '综合医院', distance: '1.9公里', image: '/static/hospital3.png'}
        ],
        '南京市': [
          {
            name: '江苏省人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.2公里',
            image: '/static/hospital1.png'
          },
          {name: '南京鼓楼医院', level: '三甲', type: '综合医院', distance: '5.8公里', image: '/static/hospital2.png'},
          {name: '南京市中医院', level: '三甲', type: '中医医院', distance: '2.5公里', image: '/static/hospital3.png'}
        ],
        '苏州市': [
          {
            name: '苏州大学附属第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.1公里',
            image: '/static/hospital1.png'
          },
          {name: '苏州市立医院', level: '三甲', type: '综合医院', distance: '6.5公里', image: '/static/hospital2.png'},
          {name: '苏州市中医院', level: '三甲', type: '中医医院', distance: '3.3公里', image: '/static/hospital3.png'}
        ],
        '无锡市': [
          {
            name: '无锡市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.6公里',
            image: '/static/hospital1.png'
          },
          {
            name: '江南大学附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.9公里',
            image: '/static/hospital2.png'
          },
          {name: '无锡市中医院', level: '三甲', type: '中医医院', distance: '2.7公里', image: '/static/hospital3.png'}
        ],
        '杭州市': [
          {
            name: '浙江大学医学院附属第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {
            name: '浙江省人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.2公里',
            image: '/static/hospital2.png'
          },
          {name: '杭州市中医院', level: '三甲', type: '中医医院', distance: '2.4公里', image: '/static/hospital3.png'}
        ],
        '宁波市': [
          {
            name: '宁波市第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.3公里',
            image: '/static/hospital1.png'
          },
          {
            name: '宁波大学医学院附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.1公里',
            image: '/static/hospital2.png'
          },
          {name: '宁波市中医院', level: '三甲', type: '中医医院', distance: '3.5公里', image: '/static/hospital3.png'}
        ],
        '珠海市': [
          {
            name: '珠海市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.9公里',
            image: '/static/hospital1.png'
          },
          {
            name: '中山大学附属第五医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.7公里',
            image: '/static/hospital2.png'
          },
          {name: '珠海市中医院', level: '三甲', type: '中医医院', distance: '2.8公里', image: '/static/hospital3.png'}
        ],
        '佛山市': [
          {
            name: '佛山市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {name: '佛山市中医院', level: '三甲', type: '中医医院', distance: '5.8公里', image: '/static/hospital2.png'},
          {
            name: '南方医科大学顺德医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.1公里',
            image: '/static/hospital3.png'
          }
        ],
        '东莞市': [
          {
            name: '东莞市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.5公里',
            image: '/static/hospital1.png'
          },
          {name: '东莞市中医院', level: '三甲', type: '中医医院', distance: '6.3公里', image: '/static/hospital2.png'},
          {name: '东莞东华医院', level: '三甲', type: '综合医院', distance: '2.9公里', image: '/static/hospital3.png'}
        ],
        '株洲市': [
          {
            name: '株洲市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '湖南省直中医医院',
            level: '三甲',
            type: '中医医院',
            distance: '5.6公里',
            image: '/static/hospital2.png'
          },
          {
            name: '株洲市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '2.1公里',
            image: '/static/hospital3.png'
          }
        ],
        '湘潭市': [
          {
            name: '湘潭市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.1公里',
            image: '/static/hospital1.png'
          },
          {
            name: '湘潭市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.8公里',
            image: '/static/hospital2.png'
          },
          {name: '湘潭市中医院', level: '三甲', type: '中医医院', distance: '3.3公里', image: '/static/hospital3.png'}
        ],
        '衡阳市': [
          {
            name: '南华大学附属第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {
            name: '衡阳市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.5公里',
            image: '/static/hospital2.png'
          },
          {name: '衡阳市中医院', level: '三甲', type: '中医医院', distance: '2.7公里', image: '/static/hospital3.png'}
        ],
        '宜昌市': [
          {
            name: '宜昌市中心人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.5公里',
            image: '/static/hospital1.png'
          },
          {
            name: '宜昌市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.2公里',
            image: '/static/hospital2.png'
          },
          {name: '宜昌市中医院', level: '三甲', type: '中医医院', distance: '2.9公里', image: '/static/hospital3.png'}
        ],
        '襄阳市': [
          {
            name: '襄阳市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '襄阳市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.1公里',
            image: '/static/hospital2.png'
          },
          {name: '襄阳市中医院', level: '三甲', type: '中医医院', distance: '3.4公里', image: '/static/hospital3.png'}
        ],
        '荆州市': [
          {
            name: '荆州市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.6公里',
            image: '/static/hospital1.png'
          },
          {
            name: '荆州市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.9公里',
            image: '/static/hospital2.png'
          },
          {name: '荆州市中医院', level: '三甲', type: '中医医院', distance: '2.5公里', image: '/static/hospital3.png'}
        ],
        '九江市': [
          {
            name: '九江学院附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.9公里',
            image: '/static/hospital1.png'
          },
          {
            name: '九江市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.5公里',
            image: '/static/hospital2.png'
          },
          {name: '九江市中医院', level: '三甲', type: '中医医院', distance: '2.8公里', image: '/static/hospital3.png'}
        ],
        '赣州市': [
          {
            name: '赣州市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.3公里',
            image: '/static/hospital1.png'
          },
          {
            name: '赣南医学院第一附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.2公里',
            image: '/static/hospital2.png'
          },
          {name: '赣州市中医院', level: '三甲', type: '中医医院', distance: '3.1公里', image: '/static/hospital3.png'}
        ],
        '上饶市': [
          {
            name: '上饶市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.7公里',
            image: '/static/hospital1.png'
          },
          {name: '上饶市立医院', level: '三甲', type: '综合医院', distance: '6.4公里', image: '/static/hospital2.png'},
          {name: '上饶市中医院', level: '三甲', type: '中医医院', distance: '2.6公里', image: '/static/hospital3.png'}
        ],
        '太原市': [
          {
            name: '山西医科大学第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.4公里',
            image: '/static/hospital1.png'
          },
          {
            name: '山西省人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.8公里',
            image: '/static/hospital2.png'
          },
          {name: '太原市中医院', level: '三甲', type: '中医医院', distance: '2.3公里', image: '/static/hospital3.png'}
        ],
        '大同市': [
          {
            name: '大同市第三人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.1公里',
            image: '/static/hospital1.png'
          },
          {
            name: '大同市第五人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.9公里',
            image: '/static/hospital2.png'
          },
          {name: '大同市中医院', level: '三甲', type: '中医医院', distance: '3.2公里', image: '/static/hospital3.png'}
        ],
        '运城市': [
          {
            name: '运城市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {
            name: '运城市第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.3公里',
            image: '/static/hospital2.png'
          },
          {name: '运城市中医院', level: '三甲', type: '中医医院', distance: '2.9公里', image: '/static/hospital3.png'}
        ],
        '临汾市': [
          {
            name: '临汾市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '临汾市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.5公里',
            image: '/static/hospital2.png'
          },
          {name: '临汾市中医院', level: '三甲', type: '中医医院', distance: '3.6公里', image: '/static/hospital3.png'}
        ],
        '无锡市': [
          {
            name: '无锡市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.6公里',
            image: '/static/hospital1.png'
          },
          {
            name: '江南大学附属医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.9公里',
            image: '/static/hospital2.png'
          },
          {name: '无锡市中医院', level: '三甲', type: '中医医院', distance: '2.7公里', image: '/static/hospital3.png'}
        ],
        '常州市': [
          {
            name: '常州市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.9公里',
            image: '/static/hospital1.png'
          },
          {
            name: '常州市第二人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.7公里',
            image: '/static/hospital2.png'
          },
          {name: '常州市中医院', level: '三甲', type: '中医医院', distance: '2.8公里', image: '/static/hospital3.png'}
        ],
        '徐州市': [
          {
            name: '徐州市中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.1公里',
            image: '/static/hospital1.png'
          },
          {
            name: '徐州市第一人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.3公里',
            image: '/static/hospital2.png'
          },
          {name: '徐州市中医院', level: '三甲', type: '中医医院', distance: '3.5公里', image: '/static/hospital3.png'}
        ],
        '温州市': [
          {
            name: '温州医科大学附属第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.5公里',
            image: '/static/hospital1.png'
          },
          {
            name: '温州市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.8公里',
            image: '/static/hospital2.png'
          },
          {name: '温州市中医院', level: '三甲', type: '中医医院', distance: '3.2公里', image: '/static/hospital3.png'}
        ],
        '嘉兴市': [
          {
            name: '嘉兴市第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.7公里',
            image: '/static/hospital1.png'
          },
          {
            name: '嘉兴市第二医院',
            level: '三甲',
            type: '综合医院',
            distance: '6.4公里',
            image: '/static/hospital2.png'
          },
          {name: '嘉兴市中医院', level: '三甲', type: '中医医院', distance: '2.9公里', image: '/static/hospital3.png'}
        ],
        '绍兴市': [
          {
            name: '绍兴市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.3公里',
            image: '/static/hospital1.png'
          },
          {
            name: '绍兴市第二医院',
            level: '三甲',
            type: '综合医院',
            distance: '7.1公里',
            image: '/static/hospital2.png'
          },
          {name: '绍兴市中医院', level: '三甲', type: '中医医院', distance: '3.4公里', image: '/static/hospital3.png'}
        ],
        '重庆市': [
          {
            name: '重庆医科大学附属第一医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.5公里',
            image: '/static/hospital1.png'
          },
          {
            name: '重庆市人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '5.8公里',
            image: '/static/hospital2.png'
          },
          {name: '重庆市中医院', level: '三甲', type: '中医医院', distance: '2.6公里', image: '/static/hospital3.png'}
        ],
        '万州区': [
          {
            name: '重庆大学附属三峡医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {
            name: '万州区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '6.5公里',
            image: '/static/hospital2.png'
          },
          {name: '万州区中医院', level: '二甲', type: '中医医院', distance: '3.1公里', image: '/static/hospital3.png'}
        ],
        '涪陵区': [
          {name: '涪陵中心医院', level: '三甲', type: '综合医院', distance: '3.8公里', image: '/static/hospital1.png'},
          {
            name: '涪陵区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '5.9公里',
            image: '/static/hospital2.png'
          },
          {name: '涪陵区中医院', level: '二甲', type: '中医医院', distance: '2.7公里', image: '/static/hospital3.png'}
        ],
        '江津区': [
          {
            name: '江津区中心医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.1公里',
            image: '/static/hospital1.png'
          },
          {
            name: '江津区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '6.8公里',
            image: '/static/hospital2.png'
          },
          {name: '江津区中医院', level: '二甲', type: '中医医院', distance: '3.3公里', image: '/static/hospital3.png'}
        ],
        '合川区': [
          {
            name: '合川区人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.6公里',
            image: '/static/hospital1.png'
          },
          {name: '合川区中医院', level: '二甲', type: '中医医院', distance: '5.7公里', image: '/static/hospital2.png'},
          {
            name: '合川区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '2.8公里',
            image: '/static/hospital3.png'
          }
        ],
        '永川区': [
          {
            name: '重庆医科大学附属永川医院',
            level: '三甲',
            type: '综合医院',
            distance: '3.9公里',
            image: '/static/hospital1.png'
          },
          {
            name: '永川区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '6.2公里',
            image: '/static/hospital2.png'
          },
          {name: '永川区中医院', level: '二甲', type: '中医医院', distance: '2.9公里', image: '/static/hospital3.png'}
        ],
        '南川区': [
          {
            name: '南川区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '4.0公里',
            image: '/static/hospital1.png'
          },
          {name: '南川区中医院', level: '二甲', type: '中医医院', distance: '6.1公里', image: '/static/hospital2.png'},
          {
            name: '南川区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '3.0公里',
            image: '/static/hospital3.png'
          }
        ],
        '綦江区': [
          {
            name: '綦江区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '3.7公里',
            image: '/static/hospital1.png'
          },
          {name: '綦江区中医院', level: '二甲', type: '中医医院', distance: '5.8公里', image: '/static/hospital2.png'},
          {
            name: '綦江区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '2.5公里',
            image: '/static/hospital3.png'
          }
        ],
        '大足区': [
          {
            name: '大足区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '4.2公里',
            image: '/static/hospital1.png'
          },
          {name: '大足区中医院', level: '二甲', type: '中医医院', distance: '6.3公里', image: '/static/hospital2.png'},
          {
            name: '大足区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '3.1公里',
            image: '/static/hospital3.png'
          }
        ],
        '璧山区': [
          {
            name: '璧山区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '3.5公里',
            image: '/static/hospital1.png'
          },
          {name: '璧山区中医院', level: '二甲', type: '中医医院', distance: '5.6公里', image: '/static/hospital2.png'},
          {
            name: '璧山区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '2.4公里',
            image: '/static/hospital3.png'
          }
        ],
        '铜梁区': [
          {
            name: '铜梁区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '3.8公里',
            image: '/static/hospital1.png'
          },
          {name: '铜梁区中医院', level: '二甲', type: '中医医院', distance: '6.0公里', image: '/static/hospital2.png'},
          {
            name: '铜梁区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '2.7公里',
            image: '/static/hospital3.png'
          }
        ],
        '潼南区': [
          {
            name: '潼南区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '4.1公里',
            image: '/static/hospital1.png'
          },
          {name: '潼南区中医院', level: '二甲', type: '中医医院', distance: '6.4公里', image: '/static/hospital2.png'},
          {
            name: '潼南区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '3.2公里',
            image: '/static/hospital3.png'
          }
        ],
        '荣昌区': [
          {
            name: '荣昌区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '3.9公里',
            image: '/static/hospital1.png'
          },
          {name: '荣昌区中医院', level: '二甲', type: '中医医院', distance: '6.1公里', image: '/static/hospital2.png'},
          {
            name: '荣昌区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '2.8公里',
            image: '/static/hospital3.png'
          }
        ],
        '开州区': [
          {
            name: '开州区人民医院',
            level: '三甲',
            type: '综合医院',
            distance: '4.3公里',
            image: '/static/hospital1.png'
          },
          {name: '开州区中医院', level: '二甲', type: '中医医院', distance: '6.6公里', image: '/static/hospital2.png'},
          {
            name: '开州区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '3.4公里',
            image: '/static/hospital3.png'
          }
        ],
        '梁平区': [
          {
            name: '梁平区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '3.6公里',
            image: '/static/hospital1.png'
          },
          {name: '梁平区中医院', level: '二甲', type: '中医医院', distance: '5.9公里', image: '/static/hospital2.png'},
          {
            name: '梁平区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '2.6公里',
            image: '/static/hospital3.png'
          }
        ],
        '武隆区': [
          {
            name: '武隆区人民医院',
            level: '二甲',
            type: '综合医院',
            distance: '4.0公里',
            image: '/static/hospital1.png'
          },
          {name: '武隆区中医院', level: '二甲', type: '中医医院', distance: '6.3公里', image: '/static/hospital2.png'},
          {
            name: '武隆区妇幼保健院',
            level: '二甲',
            type: '专科医院',
            distance: '3.1公里',
            image: '/static/hospital3.png'
          }
        ]
      }
    }
  },
  computed: {
    hospitalList() {
      return this.hospitalData[this.currentCity] || this.hospitalData['北京市']
    }
  },
  onShow() {
    const pages = getCurrentPages()
    const currentPage = pages[pages.length - 1]
    if (currentPage.data && currentPage.data.selectedCity) {
      this.currentCity = currentPage.data.selectedCity
      // 清空已选医院，因为城市变了
      this.selectedHospital = ''
    }
  },
  methods: {
    async loadHospitals() {
      this.loading = true
      try {
        const res = await getHospitals({city: this.currentCity, page: 1, pageSize: 20})
        if (res && res.code === 200 && res.data) {
          this.hospitalList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('加载医院列表失败:', e)
        uni.showToast({title: '加载失败', icon: 'none'})
      } finally {
        this.loading = false
      }
    },
    onSearch() {
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.doSearch()
      }, 400)
    },
    async doSearch() {
      if (!this.keyword.trim()) {
        this.loadHospitals();
        return
      }
      this.loading = true
      try {
        const res = await searchHospitals({keyword: this.keyword, city: this.currentCity, page: 1, pageSize: 20})
        if (res && res.code === 200 && res.data) {
          this.hospitalList = res.data.records || res.data.list || []
        }
      } catch (e) {
        console.error('搜索医院失败:', e)
      } finally {
        this.loading = false
      }
    },
    goBack() {
      uni.navigateBack()
    },
    selectCity() {
      uni.navigateTo({url: '/pages/select-city/select-city'})
    },
    selectHospital(item) {
      this.selectedHospital = item
    },
    confirmHospital() {
      if (!this.selectedHospital) {
        uni.showToast({title: '请选择医院', icon: 'none'})
        return
      }
      // 通过 storage 传递数据，避免 $vm 直接赋值在部分场景下失效
      uni.setStorageSync('_sel_hospital', this.selectedHospital)
      uni.setStorageSync('_sel_hospitalId', '')
      // 换医院时清空已选医生
      uni.setStorageSync('_sel_clearDoctor', '1')
      uni.navigateBack()
    }
  }
}
</script>

<style lang="scss">
.select-hospital-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 140rpx;
}

/* 导航栏 */
.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 40rpx 30rpx 0 30rpx;
  background: linear-gradient(135deg, #4DD0E1 0%, #26C6DA 100%);
}

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

.nav-title {
  font-size: 36rpx;
  font-weight: 500;
  color: #fff;
}

.placeholder {
  width: 60rpx;
}

/* 城市选择栏 */
.city-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20rpx;
  background-color: #fff;
  border-bottom: 1rpx solid #eee;
}

.city-text {
  font-size: 30rpx;
  color: #2196F3;
  margin-right: 10rpx;
}

.city-arrow {
  font-size: 24rpx;
  color: #2196F3;
}

/* 医院列表 */
.hospital-list {
  padding: 20rpx;
}

.hospital-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #fff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  position: relative;
}

.hospital-item.active {
  border: 2rpx solid #2196F3;
}

.hospital-image {
  width: 140rpx;
  height: 140rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  background-color: #f0f0f0;
}

.hospital-info {
  flex: 1;
}

.hospital-name {
  font-size: 32rpx;
  color: #333;
  font-weight: 500;
  display: block;
  margin-bottom: 10rpx;
}

.hospital-tags {
  display: flex;
  margin-bottom: 10rpx;
}

.tag {
  font-size: 22rpx;
  color: #2196F3;
  background-color: #e3f2fd;
  padding: 4rpx 12rpx;
  border-radius: 8rpx;
  margin-right: 10rpx;
}

.hospital-distance {
  font-size: 26rpx;
  color: #999;
}

.hospital-check {
  position: absolute;
  right: 30rpx;
  top: 50%;
  transform: translateY(-50%);
  width: 44rpx;
  height: 44rpx;
  border-radius: 50%;
  background-color: #2196F3;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  font-size: 24rpx;
  color: #fff;
}

/* 底部按钮 */
.bottom-section {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 30rpx;
  background-color: #fff;
  display: flex;
  gap: 20rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.btn {
  flex: 1;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 45rpx;
  font-size: 32rpx;
  font-weight: 500;
  border: none;
}

.btn::after {
  border: none;
}

.btn-cancel {
  background-color: #f5f5f5;
  color: #666;
}

.btn-confirm {
  background: linear-gradient(135deg, #2196F3 0%, #1976D2 100%);
  color: #fff;
}
</style>
