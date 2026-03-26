# 医路安心 API 接口文档

## 目录

1. [AI 接口 (api.js)](#1-ai-接口-apijs)
2. [陪护端接口 (companion-api.js)](#2-陪护端接口-companion-apijs)
3. [患者端接口 (patient-api.js)](#3-患者端接口-patient-apijs)
4. [医疗资源接口 (medical-api.js)](#4-医疗资源接口-medical-apijs)
5. [分诊预约接口 (triage-api.js)](#5-分诊预约接口-triage-apijs)
6. [病历套餐接口 (record-api.js)](#6-病历套餐接口-record-apijs)
7. [消息接口 (message-api.js)](#7-消息接口-message-apijs)
8. [支付接口 (payment-api.js)](#8-支付接口-payment-apijs)
9. [实名认证接口 (auth-api.js)](#9-实名认证接口-auth-apijs)
10. [系统接口 (system-api.js)](#10-系统接口-system-apijs)
11. [评价接口 (evaluation-api.js)](#11-评价接口-evaluation-apijs)
12. [医院科室接口 (hospital-api.js)](#12-医院科室接口-hospital-apijs)
13. [增值服务接口 (value-added-api.js)](#13-增值服务接口-value-added-apijs)
14. [需求发布接口 (demand-api.js)](#14-需求发布接口-demand-apijs)
15. [认证工具 (auth.js)](#15-认证工具-authjs)

---

## 1. AI 接口 (api.js)

**基础地址**: `http://localhost:8080`

### 1.1 同步聊天
- **函数**: `sendChat(data)`
- **请求方式**: POST
- **路径**: `/ai/chat`
- **参数**: `data` - 聊天数据

### 1.2 流式聊天 SSE
- **函数**: `sendChatStream(data, onChunk, onDone, onError)`
- **请求方式**: POST
- **路径**: `/ai/chat/stream`
- **参数**:
  - `data` - 聊天数据
  - `onChunk` - 接收消息回调
  - `onDone` - 完成回调
  - `onError` - 错误回调

### 1.3 简单聊天
- **函数**: `sendSimpleChat(userInput)`
- **请求方式**: POST
- **路径**: `/ai/simple`
- **参数**: `userInput` - 用户输入

### 1.4 智能分诊
- **函数**: `getTriage(symptoms)`
- **请求方式**: POST
- **路径**: `/ai/triage`
- **参数**: `symptoms` - 症状描述

### 1.5 就诊建议
- **函数**: `getAdvice(department, complaint)`
- **请求方式**: POST
- **路径**: `/ai/advice`
- **参数**:
  - `department` - 科室
  - `complaint` - 主诉

### 1.6 创建分诊会话
- **函数**: `startTriageSession(data)`
- **请求方式**: POST
- **路径**: `/ai/triage/session/start`
- **参数**: `data` - 会话数据

### 1.7 同步发送消息到分诊会话
- **函数**: `sendTriageMessage(sessionId, content)`
- **请求方式**: POST
- **路径**: `/ai/triage/session/{sessionId}/message`
- **参数**:
  - `sessionId` - 会话ID
  - `content` - 消息内容

### 1.8 流式发送消息到分诊会话
- **函数**: `sendTriageStream(sessionId, content, onChunk, onDone, onError)`
- **请求方式**: POST
- **路径**: `/ai/triage/session/{sessionId}/stream`
- **参数**:
  - `sessionId` - 会话ID
  - `content` - 消息内容
  - `onChunk` - 接收消息回调
  - `onDone` - 完成回调
  - `onError` - 错误回调

### 1.9 保存流式AI回复
- **函数**: `saveTriageReply(sessionId, content)`
- **请求方式**: POST
- **路径**: `/ai/triage/session/{sessionId}/save-reply`
- **参数**:
  - `sessionId` - 会话ID
  - `content` - 回复内容

### 1.10 结束分诊会话
- **函数**: `finishTriageSession(sessionId)`
- **请求方式**: POST
- **路径**: `/ai/triage/session/{sessionId}/finish`
- **参数**: `sessionId` - 会话ID

### 1.11 查询分诊会话详情
- **函数**: `getTriageSession(sessionId)`
- **请求方式**: GET
- **路径**: `/ai/triage/session/{sessionId}`
- **参数**: `sessionId` - 会话ID

---

## 2. 陪护端接口 (companion-api.js)

**基础地址**: `http://localhost:8080/api`

### 2.1 陪护端个人中心

#### 获取陪护员信息
- **函数**: `getCompanionProfile()`
- **请求方式**: GET
- **路径**: `/companion/profile`
- **描述**: 获取当前登录陪护员的个人信息

#### 更新陪护员信息
- **函数**: `updateCompanionProfile(data)`
- **请求方式**: PUT
- **路径**: `/companion/profile`
- **参数**:
  - `nickname` - 昵称
  - `avatar` - 头像URL
  - `introduction` - 个人简介

#### 获取工作信息
- **函数**: `getCompanionWorkInfo()`
- **请求方式**: GET
- **路径**: `/companion/work-info`
- **描述**: 获取工作统计信息

### 2.2 订单管理

#### 获取待接订单列表
- **函数**: `getAvailableOrders(params)`
- **请求方式**: GET
- **路径**: `/companion/orders/available`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取订单列表
- **函数**: `getCompanionOrders(params)`
- **请求方式**: GET
- **路径**: `/companion/orders`
- **参数**:
  - `status` - 订单状态
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取订单详情
- **函数**: `getCompanionOrderDetail(orderNo)`
- **请求方式**: GET
- **路径**: `/companion/orders/{orderNo}`
- **参数**: `orderNo` - 订单编号

#### 接单
- **函数**: `acceptOrder(orderNo)`
- **请求方式**: POST
- **路径**: `/companion/orders/accept`
- **参数**: `order_no` - 订单编号

### 2.3 资格认证

#### 提交资格认证
- **函数**: `submitCertification(data)`
- **请求方式**: POST
- **路径**: `/companion/certification`
- **参数**:
  - `real_name` - 真实姓名
  - `phone` - 手机号
  - `gender` - 性别 (0: 女, 1: 男)
  - `age` - 年龄
  - `professional_title` - 职称
  - `medical_background` - 医疗背景
  - `certificate_number` - 证书编号
  - `certificate_photo` - 证书照片URL
  - `criminal_record_cert` - 无犯罪记录证明URL
  - `business_license` - 营业执照URL
  - `language_ability` - 语言能力
  - `specialties` - 专长

#### 查询认证状态
- **函数**: `getCertificationStatus()`
- **请求方式**: GET
- **路径**: `/companion/certification/status`

### 2.4 日程管理

#### 获取日程列表
- **函数**: `getCompanionSchedule(params)`
- **请求方式**: GET
- **路径**: `/companion/schedule`
- **参数**:
  - `date` - 日期筛选
  - `status` - 日程状态

#### 更新日程状态
- **函数**: `updateCompanionSchedule(id, data)`
- **请求方式**: PUT
- **路径**: `/companion/schedule/{id}`
- **参数**:
  - `id` - 日程ID
  - `status` - 日程状态
  - `remark` - 备注

### 2.5 打卡管理

#### 打卡
- **函数**: `companionCheckin(data)`
- **请求方式**: POST
- **路径**: `/companion/checkin`
- **参数**:
  - `order_id` - 订单ID
  - `latitude` - 纬度
  - `longitude` - 经度
  - `address` - 地址
  - `photo` - 打卡照片

#### 获取打卡记录
- **函数**: `getCompanionCheckinRecords(orderId)`
- **请求方式**: GET
- **路径**: `/companion/checkin/{orderId}`
- **参数**: `orderId` - 订单ID

### 2.6 评价管理

#### 获取评价列表
- **函数**: `getCompanionEvaluations(params)`
- **请求方式**: GET
- **路径**: `/companion/evaluations`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 回复评价
- **函数**: `replyCompanionEvaluation(id, reply)`
- **请求方式**: POST
- **路径**: `/companion/evaluations/{id}/reply`
- **参数**:
  - `id` - 评价ID
  - `evaluation_reply` - 回复内容

---

## 3. 患者端接口 (patient-api.js)

**基础地址**: `http://localhost:8080/api`

### 3.1 患者个人中心

#### 获取患者信息
- **函数**: `getPatientProfile()`
- **请求方式**: GET
- **路径**: `/patient/profile`
- **描述**: 获取患者个人信息

### 3.2 订单管理

#### 创建订单
- **函数**: `createPatientOrder(data)`
- **请求方式**: POST
- **路径**: `/patient/orders`
- **参数**:
  - `service_type` - 服务类型
  - `hospital_id` - 医院ID
  - `department_id` - 科室ID
  - `appointment_time` - 预约时间
  - `patient_name` - 患者姓名
  - `patient_phone` - 患者电话
  - `patient_id_card` - 患者身份证
  - `description` - 服务描述

#### 获取订单列表
- **函数**: `getPatientOrders(params)`
- **请求方式**: GET
- **路径**: `/patient/orders`
- **参数**:
  - `status` - 订单状态
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取订单详情
- **函数**: `getPatientOrderDetail(orderNo)`
- **请求方式**: GET
- **路径**: `/patient/orders/{orderNo}`
- **参数**: `orderNo` - 订单编号

#### 取消订单
- **函数**: `cancelPatientOrder(orderNo, reason)`
- **请求方式**: POST
- **路径**: `/patient/orders/cancel`
- **参数**:
  - `order_no` - 订单编号
  - `cancel_reason` - 取消原因

#### 评价订单
- **函数**: `evaluateOrder(orderNo, data)`
- **请求方式**: POST
- **路径**: `/patient/orders/evaluate`
- **参数**:
  - `order_no` - 订单编号
  - `rating` - 评分 (1-5)
  - `content` - 评价内容
  - `tags` - 评价标签

### 3.3 收藏管理

#### 获取收藏列表
- **函数**: `getFavorites(params)`
- **请求方式**: GET
- **路径**: `/patient/favorites`
- **参数**:
  - `target_type` - 收藏类型
  - `page` - 页码
  - `pageSize` - 每页数量

#### 添加收藏
- **函数**: `addFavorite(targetType, targetId)`
- **请求方式**: POST
- **路径**: `/patient/favorites`
- **参数**:
  - `target_type` - 收藏类型
  - `target_id` - 目标ID

#### 取消收藏
- **函数**: `removeFavorite(targetType, targetId)`
- **请求方式**: POST
- **路径**: `/patient/favorites/remove`
- **参数**:
  - `target_type` - 收藏类型
  - `target_id` - 目标ID

### 3.4 就诊人管理

#### 获取就诊人列表
- **函数**: `getPatientList(params)`
- **请求方式**: GET
- **路径**: `/user/patient/list`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取就诊人详情
- **函数**: `getPatientDetail(patientId)`
- **请求方式**: GET
- **路径**: `/user/patient/{patientId}`
- **参数**: `patientId` - 就诊人ID

#### 添加就诊人
- **函数**: `addPatient(data)`
- **请求方式**: POST
- **路径**: `/user/patient`
- **参数**:
  - `name` - 姓名
  - `phone` - 手机号
  - `id_card` - 身份证号
  - `gender` - 性别
  - `birthday` - 出生日期
  - `relation` - 关系
  - `address` - 地址

#### 更新就诊人信息
- **函数**: `updatePatient(patientId, data)`
- **请求方式**: PUT
- **路径**: `/user/patient/{patientId}`
- **参数**: 同添加就诊人

#### 删除就诊人
- **函数**: `deletePatient(patientId)`
- **请求方式**: DELETE
- **路径**: `/user/patient/{patientId}`
- **参数**: `patientId` - 就诊人ID

#### 设置默认就诊人
- **函数**: `setDefaultPatient(patientId)`
- **请求方式**: POST
- **路径**: `/user/patient/{patientId}/default`
- **参数**: `patientId` - 就诊人ID

#### 添加就诊卡
- **函数**: `addMedicalCard(patientId, data)`
- **请求方式**: POST
- **路径**: `/user/patient/{patientId}/medical-card`
- **参数**:
  - `hospital_id` - 医院ID
  - `hospital_name` - 医院名称
  - `card_no` - 就诊卡号

#### 删除就诊卡
- **函数**: `deleteMedicalCard(patientId, cardId)`
- **请求方式**: DELETE
- **路径**: `/user/patient/{patientId}/medical-card/{cardId}`
- **参数**:
  - `patientId` - 就诊人ID
  - `cardId` - 就诊卡ID

---

## 4. 医疗资源接口 (medical-api.js)

**基础地址**: `http://localhost:8080/api`

### 4.1 医院管理

#### 获取医院列表
- **函数**: `getHospitals(params)`
- **请求方式**: GET
- **路径**: `/medical/hospitals`
- **参数**:
  - `city` - 城市
  - `level` - 医院等级
  - `keyword` - 关键词
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取医院详情
- **函数**: `getHospitalDetail(hospitalId)`
- **请求方式**: GET
- **路径**: `/medical/hospitals/{hospitalId}`
- **参数**: `hospitalId` - 医院ID

### 4.2 科室管理

#### 获取科室列表
- **函数**: `getDepartments(params)`
- **请求方式**: GET
- **路径**: `/medical/departments`
- **参数**:
  - `hospital_id` - 医院ID
  - `parent_id` - 父科室ID
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取科室详情
- **函数**: `getDepartmentDetail(departmentId)`
- **请求方式**: GET
- **路径**: `/medical/departments/{departmentId}`
- **参数**: `departmentId` - 科室ID

### 4.3 专家管理

#### 获取专家列表
- **函数**: `getExperts(params)`
- **请求方式**: GET
- **路径**: `/medical/experts`
- **参数**:
  - `hospital_id` - 医院ID
  - `department_id` - 科室ID
  - `keyword` - 关键词
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取专家详情
- **函数**: `getExpertDetail(expertId)`
- **请求方式**: GET
- **路径**: `/medical/experts/{expertId}`
- **参数**: `expertId` - 专家ID

---

## 5. 分诊预约接口 (triage-api.js)

**基础地址**: `http://localhost:8080/api`

### 5.1 分诊管理

#### 创建分诊记录
- **函数**: `createTriageRecord(data)`
- **请求方式**: POST
- **路径**: `/triage/records`
- **参数**:
  - `symptoms` - 症状描述
  - `pain_level` - 疼痛等级 (1-10)
  - `duration` - 持续时间
  - `medical_history` - 病史

#### 获取分诊记录列表
- **函数**: `getTriageRecords(params)`
- **请求方式**: GET
- **路径**: `/triage/records`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取分诊记录详情
- **函数**: `getTriageRecordDetail(triageId)`
- **请求方式**: GET
- **路径**: `/triage/records/{triageId}`
- **参数**: `triageId` - 分诊ID

#### 获取就医建议书
- **函数**: `getMedicalAdvice(triageId)`
- **请求方式**: GET
- **路径**: `/triage/advice/{triageId}`
- **参数**: `triageId` - 分诊ID

### 5.2 预约管理

#### 创建预约
- **函数**: `createAppointment(data)`
- **请求方式**: POST
- **路径**: `/appointment/create`
- **参数**:
  - `hospital_id` - 医院ID
  - `department_id` - 科室ID
  - `expert_id` - 专家ID
  - `appointment_date` - 预约日期
  - `appointment_time` - 预约时间
  - `patient_name` - 患者姓名
  - `patient_phone` - 患者电话
  - `patient_id_card` - 患者身份证

#### 获取预约列表
- **函数**: `getAppointments(params)`
- **请求方式**: GET
- **路径**: `/appointment/list`
- **参数**:
  - `status` - 预约状态
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取预约详情
- **函数**: `getAppointmentDetail(appointmentId)`
- **请求方式**: GET
- **路径**: `/appointment/detail/{appointmentId}`
- **参数**: `appointmentId` - 预约ID

#### 取消预约
- **函数**: `cancelAppointment(appointmentId, reason)`
- **请求方式**: POST
- **路径**: `/appointment/cancel/{appointmentId}`
- **参数**:
  - `appointmentId` - 预约ID
  - `cancel_reason` - 取消原因

---

## 6. 病历套餐接口 (record-api.js)

**基础地址**: `http://localhost:8080/api`

### 6.1 电子病历

#### 获取病历列表
- **函数**: `getMedicalRecords(params)`
- **请求方式**: GET
- **路径**: `/medical/records`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取病历详情
- **函数**: `getMedicalRecordDetail(recordId)`
- **请求方式**: GET
- **路径**: `/medical/records/{recordId}`
- **参数**: `recordId` - 病历ID

#### 上传检查报告
- **函数**: `uploadMedicalReport(data)`
- **请求方式**: POST
- **路径**: `/medical/report/upload`
- **参数**:
  - `report_type` - 报告类型
  - `report_date` - 检查日期
  - `hospital_id` - 医院ID
  - `report_file` - 报告文件URL
  - `description` - 报告描述

#### 获取检查报告列表
- **函数**: `getMedicalReports(params)`
- **请求方式**: GET
- **路径**: `/medical/reports`
- **参数**:
  - `report_type` - 报告类型
  - `page` - 页码
  - `pageSize` - 每页数量

### 6.2 套餐管理

#### 获取套餐列表
- **函数**: `getServicePackages(params)`
- **请求方式**: GET
- **路径**: `/service/packages`
- **参数**:
  - `category` - 套餐分类
  - `keyword` - 关键词
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取套餐详情
- **函数**: `getServicePackageDetail(packageId)`
- **请求方式**: GET
- **路径**: `/service/packages/{packageId}`
- **参数**: `packageId` - 套餐ID

#### 收藏套餐
- **函数**: `favoritePackage(packageId)`
- **请求方式**: POST
- **路径**: `/service/packages/favorite`
- **参数**: `package_id` - 套餐ID

#### 取消收藏套餐
- **函数**: `unfavoritePackage(packageId)`
- **请求方式**: POST
- **路径**: `/service/packages/unfavorite`
- **参数**: `package_id` - 套餐ID

---

## 7. 消息接口 (message-api.js)

**基础地址**: `http://localhost:8080/api`

### 7.1 消息管理

#### 获取消息列表
- **函数**: `getMessageList(params)`
- **请求方式**: GET
- **路径**: `/message/list`
- **参数**:
  - `type` - 消息类型
  - `status` - 消息状态
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取消息详情
- **函数**: `getMessageDetail(messageId)`
- **请求方式**: GET
- **路径**: `/message/detail/{messageId}`
- **参数**: `messageId` - 消息ID

#### 标记消息已读
- **函数**: `markMessageRead(messageId)`
- **请求方式**: POST
- **路径**: `/message/read/{messageId}`
- **参数**: `messageId` - 消息ID

#### 标记所有消息已读
- **函数**: `markAllMessagesRead(type)`
- **请求方式**: POST
- **路径**: `/message/read-all`
- **参数**: `type` - 消息类型（可选）

#### 删除消息
- **函数**: `deleteMessage(messageId)`
- **请求方式**: DELETE
- **路径**: `/message/delete/{messageId}`
- **参数**: `messageId` - 消息ID

#### 批量删除消息
- **函数**: `batchDeleteMessages(messageIds)`
- **请求方式**: POST
- **路径**: `/message/batch-delete`
- **参数**: `message_ids` - 消息ID数组

#### 获取未读消息数量
- **函数**: `getUnreadMessageCount()`
- **请求方式**: GET
- **路径**: `/message/unread-count`

#### 发送消息
- **函数**: `sendMessage(data)`
- **请求方式**: POST
- **路径**: `/message/send`
- **参数**:
  - `receiver_id` - 接收者ID
  - `content` - 消息内容
  - `type` - 消息类型

---

## 8. 支付接口 (payment-api.js)

**基础地址**: `http://localhost:8080/api`

### 8.1 支付管理

#### 创建支付订单
- **函数**: `createPayment(data)`
- **请求方式**: POST
- **路径**: `/payment/create`
- **参数**:
  - `order_no` - 业务订单号
  - `payment_type` - 支付类型
  - `amount` - 支付金额
  - `description` - 支付描述

#### 获取支付状态
- **函数**: `getPaymentStatus(paymentNo)`
- **请求方式**: GET
- **路径**: `/payment/status/{paymentNo}`
- **参数**: `paymentNo` - 支付订单号

#### 确认支付
- **函数**: `confirmPayment(paymentNo, data)`
- **请求方式**: POST
- **路径**: `/payment/confirm/{paymentNo}`
- **参数**:
  - `payment_type` - 支付方式
  - `transaction_id` - 第三方交易号

#### 取消支付
- **函数**: `cancelPayment(paymentNo)`
- **请求方式**: POST
- **路径**: `/payment/cancel/{paymentNo}`
- **参数**: `paymentNo` - 支付订单号

#### 获取支付方式列表
- **函数**: `getPaymentMethods()`
- **请求方式**: GET
- **路径**: `/payment/methods`

#### 获取支付记录列表
- **函数**: `getPaymentRecords(params)`
- **请求方式**: GET
- **路径**: `/payment/records`
- **参数**:
  - `status` - 支付状态
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取支付记录详情
- **函数**: `getPaymentRecordDetail(paymentNo)`
- **请求方式**: GET
- **路径**: `/payment/records/{paymentNo}`
- **参数**: `paymentNo` - 支付订单号

#### 申请退款
- **函数**: `applyRefund(paymentNo, data)`
- **请求方式**: POST
- **路径**: `/payment/refund/{paymentNo}`
- **参数**:
  - `amount` - 退款金额
  - `reason` - 退款原因

#### 获取退款记录
- **函数**: `getRefundRecords(params)`
- **请求方式**: GET
- **路径**: `/payment/refund-records`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

### 8.2 支付授权

#### 获取支付授权信息
- **函数**: `getPaymentAuthInfo()`
- **请求方式**: GET
- **路径**: `/payment/auth/info`

#### 设置支付密码
- **函数**: `setPaymentPassword(data)`
- **请求方式**: POST
- **路径**: `/payment/auth/password`
- **参数**:
  - `password` - 支付密码
  - `confirm_password` - 确认密码

#### 验证支付密码
- **函数**: `verifyPaymentPassword(password)`
- **请求方式**: POST
- **路径**: `/payment/auth/verify`
- **参数**: `password` - 支付密码

#### 绑定支付方式
- **函数**: `bindPaymentMethod(data)`
- **请求方式**: POST
- **路径**: `/payment/auth/bind`
- **参数**:
  - `type` - 支付方式类型
  - `account` - 账号

#### 解绑支付方式
- **函数**: `unbindPaymentMethod(type)`
- **请求方式**: POST
- **路径**: `/payment/auth/unbind/{type}`
- **参数**: `type` - 支付方式类型

---

## 9. 实名认证接口 (auth-api.js)

**基础地址**: `http://localhost:8080/api`

### 9.1 实名认证管理

#### 提交实名认证
- **函数**: `submitRealNameAuth(data)`
- **请求方式**: POST
- **路径**: `/auth/real-name`
- **参数**:
  - `real_name` - 真实姓名
  - `id_card` - 身份证号
  - `id_card_front` - 身份证正面照片URL
  - `id_card_back` - 身份证背面照片URL
  - `face_recognition` - 人脸识别照片URL

#### 获取实名认证状态
- **函数**: `getAuthStatus()`
- **请求方式**: GET
- **路径**: `/auth/real-name/status`

#### 获取实名认证信息
- **函数**: `getAuthInfo()`
- **请求方式**: GET
- **路径**: `/auth/real-name/info`

#### 上传身份证照片
- **函数**: `uploadIdCard(filePath, side)`
- **请求方式**: POST (文件上传)
- **路径**: `/auth/real-name/upload-idcard`
- **参数**:
  - `filePath` - 本地文件路径
  - `side` - 身份证面 (front/back)

#### 上传人脸识别照片
- **函数**: `uploadFaceRecognition(filePath)`
- **请求方式**: POST (文件上传)
- **路径**: `/auth/real-name/upload-face`
- **参数**: `filePath` - 本地文件路径

#### 重新提交实名认证
- **函数**: `resubmitRealNameAuth(data)`
- **请求方式**: POST
- **路径**: `/auth/real-name/resubmit`

#### 取消实名认证申请
- **函数**: `cancelRealNameAuth()`
- **请求方式**: POST
- **路径**: `/auth/real-name/cancel`

### 9.2 认证步骤管理

#### 保存认证步骤1数据
- **函数**: `saveAuthStep1(data)`
- **请求方式**: POST
- **路径**: `/auth/real-name/step1`
- **参数**:
  - `real_name` - 真实姓名
  - `id_card` - 身份证号

#### 保存认证步骤2数据
- **函数**: `saveAuthStep2(data)`
- **请求方式**: POST
- **路径**: `/auth/real-name/step2`
- **参数**:
  - `id_card_front` - 身份证正面照片URL
  - `id_card_back` - 身份证背面照片URL

#### 保存认证步骤3数据
- **函数**: `saveAuthStep3(data)`
- **请求方式**: POST
- **路径**: `/auth/real-name/step3`
- **参数**: `face_recognition` - 人脸识别照片URL

#### 获取认证步骤数据
- **函数**: `getAuthStepData(step)`
- **请求方式**: GET
- **路径**: `/auth/real-name/step/{step}`
- **参数**: `step` - 步骤编号 (1, 2, 3)

---

## 10. 系统接口 (system-api.js)

**基础地址**: `http://localhost:8080/api`

### 10.1 认证登录

#### 用户登录
- **函数**: `login(username, password)`
- **请求方式**: POST
- **路径**: `/auth/login`
- **参数**:
  - `username` - 用户名
  - `password` - 密码

#### 用户注册
- **函数**: `register(data)`
- **请求方式**: POST
- **路径**: `/auth/register`
- **参数**:
  - `username` - 用户名
  - `password` - 密码
  - `phone` - 手机号
  - `verification_code` - 验证码

### 10.2 文件上传

#### 上传文件
- **函数**: `uploadFile(filePath, fileCategory, relatedType, relatedId)`
- **请求方式**: POST (文件上传)
- **路径**: `/upload`
- **参数**:
  - `filePath` - 本地文件路径
  - `file_category` - 文件分类
  - `related_type` - 关联类型
  - `related_id` - 关联ID

---

## 11. 评价接口 (evaluation-api.js)

**基础地址**: `http://localhost:8080/api`

### 11.1 评价管理

#### 获取评价列表
- **函数**: `getEvaluationList(params)`
- **请求方式**: GET
- **路径**: `/evaluation/list`
- **参数**:
  - `target_type` - 评价对象类型
  - `target_id` - 评价对象ID
  - `rating` - 评分筛选
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取评价详情
- **函数**: `getEvaluationDetail(evaluationId)`
- **请求方式**: GET
- **路径**: `/evaluation/detail/{evaluationId}`
- **参数**: `evaluationId` - 评价ID

#### 获取我的评价列表
- **函数**: `getMyEvaluations(params)`
- **请求方式**: GET
- **路径**: `/evaluation/my`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取待评价订单列表
- **函数**: `getPendingEvaluationOrders(params)`
- **请求方式**: GET
- **路径**: `/evaluation/pending`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 删除评价
- **函数**: `deleteEvaluation(evaluationId)`
- **请求方式**: DELETE
- **路径**: `/evaluation/delete/{evaluationId}`
- **参数**: `evaluationId` - 评价ID

#### 点赞评价
- **函数**: `likeEvaluation(evaluationId)`
- **请求方式**: POST
- **路径**: `/evaluation/like/{evaluationId}`
- **参数**: `evaluationId` - 评价ID

#### 取消点赞评价
- **函数**: `unlikeEvaluation(evaluationId)`
- **请求方式**: POST
- **路径**: `/evaluation/unlike/{evaluationId}`
- **参数**: `evaluationId` - 评价ID

#### 获取评价统计
- **函数**: `getEvaluationStatistics(targetType, targetId)`
- **请求方式**: GET
- **路径**: `/evaluation/statistics`
- **参数**:
  - `target_type` - 评价对象类型
  - `target_id` - 评价对象ID

### 11.2 评价回复

#### 回复评价
- **函数**: `replyToEvaluation(evaluationId, data)`
- **请求方式**: POST
- **路径**: `/evaluation/reply/{evaluationId}`
- **参数**: `content` - 回复内容

#### 获取评价回复列表
- **函数**: `getEvaluationReplies(evaluationId, params)`
- **请求方式**: GET
- **路径**: `/evaluation/replies/{evaluationId}`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 删除评价回复
- **函数**: `deleteEvaluationReply(replyId)`
- **请求方式**: DELETE
- **路径**: `/evaluation/reply/delete/{replyId}`
- **参数**: `replyId` - 回复ID

---

## 12. 医院科室接口 (hospital-api.js)

**基础地址**: `http://localhost:8080/api`

### 12.1 医院选择

#### 按城市获取医院列表
- **函数**: `getHospitalListByCity(params)`
- **请求方式**: GET
- **路径**: `/hospital/list-by-city`
- **参数**:
  - `city` - 城市名称
  - `level` - 医院等级
  - `page` - 页码
  - `pageSize` - 每页数量

#### 搜索医院
- **函数**: `searchHospitals(params)`
- **请求方式**: GET
- **路径**: `/hospital/search`
- **参数**:
  - `keyword` - 搜索关键词
  - `city` - 城市名称
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取附近医院列表
- **函数**: `getNearbyHospitals(params)`
- **请求方式**: GET
- **路径**: `/hospital/nearby`
- **参数**:
  - `latitude` - 纬度
  - `longitude` - 经度
  - `distance` - 搜索半径
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取热门医院列表
- **函数**: `getPopularHospitals(params)`
- **请求方式**: GET
- **路径**: `/hospital/popular`
- **参数**:
  - `city` - 城市名称
  - `limit` - 数量限制

#### 获取医院详情
- **函数**: `getHospitalDetail(hospitalId)`
- **请求方式**: GET
- **路径**: `/hospital/detail/{hospitalId}`
- **参数**: `hospitalId` - 医院ID

#### 获取城市列表
- **函数**: `getCityList()`
- **请求方式**: GET
- **路径**: `/hospital/cities`

#### 获取定位城市
- **函数**: `getLocationCity(params)`
- **请求方式**: GET
- **路径**: `/hospital/location-city`
- **参数**:
  - `latitude` - 纬度
  - `longitude` - 经度

### 12.2 科室选择

#### 获取医院科室列表
- **函数**: `getDepartmentListByHospital(hospitalId, params)`
- **请求方式**: GET
- **路径**: `/hospital/{hospitalId}/departments`
- **参数**:
  - `hospitalId` - 医院ID
  - `parent_id` - 父科室ID

#### 获取科室详情
- **函数**: `getDepartmentDetail(departmentId)`
- **请求方式**: GET
- **路径**: `/department/detail/{departmentId}`
- **参数**: `departmentId` - 科室ID

#### 搜索科室
- **函数**: `searchDepartments(params)`
- **请求方式**: GET
- **路径**: `/department/search`
- **参数**:
  - `keyword` - 搜索关键词
  - `hospital_id` - 医院ID

#### 获取热门科室列表
- **函数**: `getPopularDepartments(params)`
- **请求方式**: GET
- **路径**: `/department/popular`
- **参数**: `limit` - 数量限制

#### 获取科室医生列表
- **函数**: `getDepartmentDoctors(departmentId, params)`
- **请求方式**: GET
- **路径**: `/department/{departmentId}/doctors`
- **参数**:
  - `departmentId` - 科室ID
  - `page` - 页码
  - `pageSize` - 每页数量

---

## 13. 增值服务接口 (value-added-api.js)

**基础地址**: `http://localhost:8080/api`

### 13.1 增值服务管理

#### 获取增值服务列表
- **函数**: `getValueAddedServices(params)`
- **请求方式**: GET
- **路径**: `/value-added/services`
- **参数**:
  - `category` - 服务分类
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取增值服务详情
- **函数**: `getValueAddedServiceDetail(serviceId)`
- **请求方式**: GET
- **路径**: `/value-added/service/{serviceId}`
- **参数**: `serviceId` - 服务ID

#### 选择增值服务
- **函数**: `selectValueAddedService(data)`
- **请求方式**: POST
- **路径**: `/value-added/select`
- **参数**:
  - `service_id` - 服务ID
  - `order_no` - 订单号
  - `quantity` - 数量

#### 取消选择的增值服务
- **函数**: `cancelValueAddedService(selectionId)`
- **请求方式**: POST
- **路径**: `/value-added/cancel/{selectionId}`
- **参数**: `selectionId` - 选择记录ID

#### 获取已选增值服务列表
- **函数**: `getSelectedValueAddedServices(orderNo)`
- **请求方式**: GET
- **路径**: `/value-added/selected/{orderNo}`
- **参数**: `orderNo` - 订单号

#### 获取增值服务分类列表
- **函数**: `getValueAddedCategories()`
- **请求方式**: GET
- **路径**: `/value-added/categories`

#### 获取热门增值服务
- **函数**: `getPopularValueAddedServices(params)`
- **请求方式**: GET
- **路径**: `/value-added/popular`
- **参数**: `limit` - 数量限制

#### 计算增值服务总价
- **函数**: `calculateValueAddedTotal(data)`
- **请求方式**: POST
- **路径**: `/value-added/calculate`
- **参数**:
  - `service_ids` - 服务ID数组
  - `order_no` - 订单号

#### 获取增值服务推荐
- **函数**: `getRecommendedValueAddedServices(orderNo)`
- **请求方式**: GET
- **路径**: `/value-added/recommended/{orderNo}`
- **参数**: `orderNo` - 订单号

---

## 14. 需求发布接口 (demand-api.js)

**基础地址**: `http://localhost:8080/api`

### 14.1 需求发布管理

#### 发布需求
- **函数**: `publishDemand(data)`
- **请求方式**: POST
- **路径**: `/demand/publish`
- **参数**:
  - `title` - 需求标题
  - `description` - 需求描述
  - `type` - 需求类型
  - `hospital_id` - 医院ID
  - `department_id` - 科室ID
  - `patient_id` - 就诊人ID
  - `start_date` - 开始日期
  - `end_date` - 结束日期
  - `budget` - 预算金额
  - `contact_name` - 联系人姓名
  - `contact_phone` - 联系人电话
  - `requirements` - 特殊要求

#### 获取我的需求列表
- **函数**: `getMyDemands(params)`
- **请求方式**: GET
- **路径**: `/demand/my`
- **参数**:
  - `status` - 需求状态
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取需求详情
- **函数**: `getDemandDetail(demandId)`
- **请求方式**: GET
- **路径**: `/demand/detail/{demandId}`
- **参数**: `demandId` - 需求ID

#### 编辑需求
- **函数**: `editDemand(demandId, data)`
- **请求方式**: PUT
- **路径**: `/demand/edit/{demandId}`
- **参数**: `demandId` - 需求ID, `data` - 需求信息

#### 取消需求
- **函数**: `cancelDemand(demandId, data)`
- **请求方式**: POST
- **路径**: `/demand/cancel/{demandId}`
- **参数**:
  - `demandId` - 需求ID
  - `reason` - 取消原因

#### 删除需求
- **函数**: `deleteDemand(demandId)`
- **请求方式**: DELETE
- **路径**: `/demand/delete/{demandId}`
- **参数**: `demandId` - 需求ID

#### 获取需求列表（陪护端）
- **函数**: `getDemandListForCompanion(params)`
- **请求方式**: GET
- **路径**: `/demand/list`
- **参数**:
  - `city` - 城市
  - `type` - 需求类型
  - `min_budget` - 最低预算
  - `max_budget` - 最高预算
  - `start_date` - 开始日期
  - `end_date` - 结束日期
  - `page` - 页码
  - `pageSize` - 每页数量

#### 接单
- **函数**: `acceptDemand(demandId)`
- **请求方式**: POST
- **路径**: `/demand/accept/{demandId}`
- **参数**: `demandId` - 需求ID

#### 拒绝接单
- **函数**: `rejectDemand(demandId, data)`
- **请求方式**: POST
- **路径**: `/demand/reject/{demandId}`
- **参数**:
  - `demandId` - 需求ID
  - `reason` - 拒绝原因

#### 获取需求匹配推荐
- **函数**: `getRecommendedDemands(params)`
- **请求方式**: GET
- **路径**: `/demand/recommended`
- **参数**: `limit` - 数量限制

#### 刷新需求
- **函数**: `refreshDemand(demandId)`
- **请求方式**: POST
- **路径**: `/demand/refresh/{demandId}`
- **参数**: `demandId` - 需求ID

#### 获取需求统计
- **函数**: `getDemandStatistics()`
- **请求方式**: GET
- **路径**: `/demand/statistics`

---

## 15. 认证工具 (auth.js)

**基础地址**: `http://localhost:8080/api`

### 通用方法

#### 获取 Token
- **函数**: `getToken()`
- **描述**: 从本地存储获取 token
- **返回**: token 字符串

#### 通用请求方法
- **函数**: `request(url, options)`
- **参数**:
  - `url` - 请求路径
  - `options.method` - 请求方法
  - `options.data` - 请求数据
  - `options.header` - 请求头

### 认证接口

#### 用户登录
- **函数**: `login(username, password)`
- **请求方式**: POST
- **路径**: `/auth/login`
- **参数**:
  - `username` - 用户名
  - `password` - 密码

#### 用户注册
- **函数**: `register(username, password, phone, verificationCode)`
- **请求方式**: POST
- **路径**: `/auth/register`
- **参数**:
  - `username` - 用户名
  - `password` - 密码
  - `phone` - 手机号
  - `verification_code` - 验证码

#### 发送验证码
- **函数**: `sendVerificationCode(phone)`
- **请求方式**: POST
- **路径**: `/auth/send-code`
- **参数**: `phone` - 手机号

#### 重置密码
- **函数**: `resetPassword(phone, verificationCode, newPassword)`
- **请求方式**: POST
- **路径**: `/auth/reset-password`
- **参数**:
  - `phone` - 手机号
  - `verification_code` - 验证码
  - `new_password` - 新密码

#### 退出登录
- **函数**: `logout()`
- **请求方式**: POST
- **路径**: `/auth/logout`

#### 获取用户信息
- **函数**: `getUserInfo()`
- **请求方式**: GET
- **路径**: `/auth/info`

---

## 16. 聊天接口 (chat-api.js)

**基础地址**: `http://localhost:8080/api`

### 16.1 聊天管理

#### 获取聊天列表
- **函数**: `getChatList(params)`
- **请求方式**: GET
- **路径**: `/chat/list`
- **参数**:
  - `page` - 页码
  - `pageSize` - 每页数量

#### 获取聊天记录
- **函数**: `getChatHistory(targetId, params)`
- **请求方式**: GET
- **路径**: `/chat/history/{targetId}`
- **参数**:
  - `targetId` - 聊天对象ID
  - `page` - 页码
  - `pageSize` - 每页数量
  - `before_time` - 获取此时间之前的消息

#### 发送消息
- **函数**: `sendMessage(data)`
- **请求方式**: POST
- **路径**: `/chat/send`
- **参数**:
  - `target_id` - 接收者ID
  - `content` - 消息内容
  - `type` - 消息类型
  - `extra` - 额外数据

#### 发送图片消息
- **函数**: `sendImageMessage(targetId, filePath)`
- **请求方式**: POST (文件上传)
- **路径**: `/chat/send-image`
- **参数**:
  - `targetId` - 接收者ID
  - `filePath` - 本地图片路径

#### 标记消息已读
- **函数**: `markChatRead(targetId)`
- **请求方式**: POST
- **路径**: `/chat/read/{targetId}`
- **参数**: `targetId` - 聊天对象ID

#### 删除聊天记录
- **函数**: `deleteChatHistory(targetId)`
- **请求方式**: DELETE
- **路径**: `/chat/delete/{targetId}`
- **参数**: `targetId` - 聊天对象ID

#### 获取未读消息总数
- **函数**: `getTotalUnreadCount()`
- **请求方式**: GET
- **路径**: `/chat/unread-total`

#### 创建聊天会话
- **函数**: `createChatSession(data)`
- **请求方式**: POST
- **路径**: `/chat/session`
- **参数**:
  - `target_id` - 聊天对象ID
  - `type` - 会话类型

#### 获取聊天会话信息
- **函数**: `getChatSession(targetId)`
- **请求方式**: GET
- **路径**: `/chat/session/{targetId}`
- **参数**: `targetId` - 聊天对象ID

---

## 通用说明

### 请求头

所有请求默认包含以下请求头：
- `Content-Type: application/json`
- `Authorization: Bearer {token}` (需要认证的接口)

### 响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 错误码

| 状态码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 401 | Token 过期或未携带 |
| 其他 | 请求失败 |

### Token 处理

当接口返回 401 状态码时，会自动：
1. 显示提示："登录已过期，请重新登录"
2. 跳转到登录页面
