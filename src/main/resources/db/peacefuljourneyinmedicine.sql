/*
 Navicat Premium Dump SQL

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : localhost:3306
 Source Schema         : peacefuljourneyinmedicine

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 26/03/2026 21:30:04
*/

CREATE DATABASE IF NOT EXISTS PeacefulJourneyInMedicine
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE PeacefulJourneyInMedicine;

-- ----------------------------
-- Table structure for medical_appointment
-- ----------------------------
DROP TABLE IF EXISTS `medical_appointment`;
CREATE TABLE `medical_appointment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `appointment_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '预约编号，唯一索引',
  `user_id` bigint NOT NULL COMMENT '患者ID，关联user_user表',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者姓名（冗余）',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者手机号（冗余）',
  `hospital_id` bigint NOT NULL COMMENT '医院ID，关联medical_hospital表',
  `hospital_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院名称（冗余）',
  `department_id` bigint NOT NULL COMMENT '科室ID，关联medical_department表',
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科室名称（冗余）',
  `expert_id` bigint NULL DEFAULT NULL COMMENT '专家ID，关联medical_expert表',
  `expert_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专家姓名（冗余）',
  `appointment_date` date NOT NULL COMMENT '预约日期',
  `appointment_time` time NULL DEFAULT NULL COMMENT '预约时间',
  `appointment_type` tinyint NULL DEFAULT NULL COMMENT '预约类型（1：初诊；2：复诊；3：会诊）',
  `appointment_status` tinyint NULL DEFAULT 1 COMMENT '预约状态（1：待确认；2：已确认；3：已取消；4：已完成；5：已过期）',
  `disease_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '病情描述',
  `visit_card_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '就诊卡号',
  `registration_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '挂号单号',
  `check_in_time` datetime NULL DEFAULT NULL COMMENT '签到时间',
  `doctor_advice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '医嘱',
  `prescription_urls` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '处方URL',
  `follow_up_needed` tinyint NULL DEFAULT 2 COMMENT '是否需要复诊（1：是；2：否）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `appointment_no`(`appointment_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_hospital_id`(`hospital_id` ASC) USING BTREE,
  INDEX `idx_department_id`(`department_id` ASC) USING BTREE,
  INDEX `idx_expert_id`(`expert_id` ASC) USING BTREE,
  INDEX `idx_appointment_date`(`appointment_date` ASC) USING BTREE,
  INDEX `idx_appointment_status`(`appointment_status` ASC) USING BTREE,
  INDEX `idx_hospital_dept_date`(`hospital_id` ASC, `department_id` ASC, `appointment_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预约信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medical_appointment
-- ----------------------------
INSERT INTO `medical_appointment` VALUES (1, 'APT20260310001', 1, '张三', '+86 13800138001', 1, '北京协和医院', 7, '神经科', 1, '张文宏', '2026-03-20', '09:00:00', 1, 2, '持续性头痛一周，伴有恶心', 'VC20260001', 'REG20260310001', NULL, NULL, NULL, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '预约成功');
INSERT INTO `medical_appointment` VALUES (2, 'APT20260311002', 2, '李四', '+86 13800138002', 4, '中山大学附属第一医院', 10, '消化内科', 7, '赵伟', '2026-03-22', '14:30:00', 1, 1, '反复胃痛三个月，饭后明显', 'VC20260002', NULL, NULL, NULL, NULL, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '待确认');
INSERT INTO `medical_appointment` VALUES (3, 'APT20260312003', 4, 'John Doe', '+1 5551234567', 6, 'Mayo Clinic', 9, '心内科', 5, 'Dr. Michael Chen', '2026-03-25', '10:00:00', 1, 1, 'Chest pain and shortness of breath', 'VC20260003', NULL, NULL, NULL, NULL, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '待确认');
INSERT INTO `medical_appointment` VALUES (4, 'APT20260313004', 5, '田中花子', '+81 90-1234-5678', 7, 'Tokyo University Hospital', 5, '眼科', 6, 'Dr. Sarah Yamamoto', '2026-03-28', '11:00:00', 1, 1, '目の疲れと頭痛', 'VC20260004', NULL, NULL, NULL, NULL, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '待确认');
INSERT INTO `medical_appointment` VALUES (5, 'APT20260314005', 3, '王五', '+86 13800138003', 1, '北京协和医院', 6, '骨科', 3, '王磊', '2026-03-18', '15:00:00', 2, 4, '膝关节疼痛复查', 'VC20260005', 'REG20260314005', NULL, NULL, NULL, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已完成');

-- ----------------------------
-- Table structure for medical_department
-- ----------------------------
DROP TABLE IF EXISTS `medical_department`;
CREATE TABLE `medical_department`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `department_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科室编码，唯一索引',
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '科室名称',
  `department_name_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科室英文名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父科室ID',
  `department_type` tinyint NULL DEFAULT NULL COMMENT '科室类型（1：内科；2：外科；3：妇产科；4：儿科；5：眼科；6：耳鼻喉科；7：皮肤科；8：骨科；9：神经科；10：其他）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '科室描述',
  `icon_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图标URL',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1：正常；2：停用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `department_code`(`department_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '科室信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medical_department
-- ----------------------------
INSERT INTO `medical_department` VALUES (1, 'DEPT001', '内科', 'Internal Medicine', 0, 1, '内科疾病诊治', 'https://example.com/dept/internal.jpg', 1, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '内科');
INSERT INTO `medical_department` VALUES (2, 'DEPT002', '外科', 'Surgery', 0, 2, '外科手术治疗', 'https://example.com/dept/surgery.jpg', 2, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '外科');
INSERT INTO `medical_department` VALUES (3, 'DEPT003', '妇产科', 'Obstetrics and Gynecology', 0, 3, '妇科及产科诊疗', 'https://example.com/dept/gynecology.jpg', 3, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '妇产科');
INSERT INTO `medical_department` VALUES (4, 'DEPT004', '儿科', 'Pediatrics', 0, 4, '儿童疾病诊治', 'https://example.com/dept/pediatrics.jpg', 4, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '儿科');
INSERT INTO `medical_department` VALUES (5, 'DEPT005', '眼科', 'Ophthalmology', 0, 5, '眼部疾病诊治', 'https://example.com/dept/ophthalmology.jpg', 5, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '眼科');
INSERT INTO `medical_department` VALUES (6, 'DEPT006', '骨科', 'Orthopedics', 0, 8, '骨骼及关节疾病', 'https://example.com/dept/orthopedics.jpg', 6, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '骨科');
INSERT INTO `medical_department` VALUES (7, 'DEPT007', '神经科', 'Neurology', 0, 9, '神经系统疾病', 'https://example.com/dept/neurology.jpg', 7, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '神经科');
INSERT INTO `medical_department` VALUES (8, 'DEPT008', '肿瘤科', 'Oncology', 0, 1, '肿瘤疾病诊治', 'https://example.com/dept/oncology.jpg', 8, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '肿瘤科');
INSERT INTO `medical_department` VALUES (9, 'DEPT009', '心内科', 'Cardiology', 0, 1, '心血管疾病诊治', 'https://example.com/dept/cardiology.jpg', 9, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '心内科');
INSERT INTO `medical_department` VALUES (10, 'DEPT010', '消化内科', 'Gastroenterology', 0, 1, '消化系统疾病诊治', 'https://example.com/dept/gastro.jpg', 10, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '消化内科');

-- ----------------------------
-- Table structure for medical_expert
-- ----------------------------
DROP TABLE IF EXISTS `medical_expert`;
CREATE TABLE `medical_expert`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `user_id` bigint NULL DEFAULT NULL COMMENT '关联用户ID（user_user.id），user_type=5',
  `expert_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '专家编码，唯一索引',
  `expert_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '专家姓名',
  `expert_name_en` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专家英文姓名',
  `hospital_id` bigint NOT NULL COMMENT '所属医院ID，关联medical_hospital表',
  `hospital_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院名称（冗余）',
  `department_id` bigint NOT NULL COMMENT '所属科室ID，关联medical_department表',
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科室名称（冗余）',
  `professional_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职称（如主任医师、副主任医师）',
  `specialty` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业擅长',
  `language_ability` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '语言能力',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `photo_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '照片URL',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '个人简介',
  `experience_years` int NULL DEFAULT NULL COMMENT '工作年限',
  `education_background` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '学历背景',
  `publication_count` int NULL DEFAULT 0 COMMENT '发表论文数',
  `appointment_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '预约费用',
  `is_accept_international` tinyint NULL DEFAULT 1 COMMENT '是否接受国际患者（1：是；2：否）',
  `is_recommend` tinyint NULL DEFAULT 2 COMMENT '是否推荐（1：是；2：否）',
  `rating_score` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '评分',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1：正常；2：停用）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `expert_code`(`expert_code` ASC) USING BTREE,
  INDEX `idx_hospital_id`(`hospital_id` ASC) USING BTREE,
  INDEX `idx_department_id`(`department_id` ASC) USING BTREE,
  INDEX `idx_professional_title`(`professional_title` ASC) USING BTREE,
  INDEX `idx_rating_score`(`rating_score` ASC) USING BTREE,
  INDEX `idx_is_recommend`(`is_recommend` ASC) USING BTREE,
  INDEX `idx_hospital_dept_rating`(`hospital_id` ASC, `department_id` ASC, `rating_score` ASC) USING BTREE,
  FULLTEXT INDEX `ft_expert_name`(`expert_name`, `expert_name_en`, `specialty`)
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '专家信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medical_expert
-- ----------------------------
INSERT INTO `medical_expert` VALUES (1, 15, 'EXP001', '张文宏', 'Zhang Wenhong', 1, '北京协和医院', 1, '内科', '主任医师', '感染性疾病,肝病,发热待查', '中文,英文', 1, 55, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '著名感染科专家，在传染病领域有丰富经验', 30, '复旦大学博士', 156, 800.00, 1, 1, 4.95, 234, 1, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '感染科权威');
INSERT INTO `medical_expert` VALUES (2, 16, 'EXP002', '李兰娟', 'Li Lanjuan', 1, '北京协和医院', 8, '肿瘤科', '院士', '肿瘤综合治疗,免疫治疗', '中文,英文', 2, 68, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '中国工程院院士，肿瘤治疗领域顶尖专家', 40, '浙江大学博士', 389, 1200.00, 1, 1, 4.98, 345, 1, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '肿瘤科权威');
INSERT INTO `medical_expert` VALUES (3, 17, 'EXP003', '王磊', 'Wang Lei', 2, '北京大学第一医院', 6, '骨科', '主任医师', '脊柱外科,关节疾病,运动损伤', '中文,英文', 1, 48, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '骨科专家，擅长微创手术', 25, '北京大学博士', 89, 600.00, 1, 1, 4.88, 167, 1, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '骨科专家');
INSERT INTO `medical_expert` VALUES (4, 18, 'EXP004', '陈静', 'Chen Jing', 3, '上海华山医院', 7, '神经科', '副主任医师', '脑血管病,帕金森病,癫痫', '中文,日文', 2, 42, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '神经科专家，在帕金森病治疗方面有深入研究', 18, '复旦大学博士', 67, 500.00, 1, 1, 4.82, 145, 1, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '神经科专家');
INSERT INTO `medical_expert` VALUES (5, 19, 'EXP005', 'Dr. Michael Chen', 'Michael Chen', 6, 'Mayo Clinic', 9, '心内科', 'Professor of Medicine', 'Cardiology, Interventional Cardiology, Heart Failure', 'English, Chinese', 1, 52, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '世界知名心脏病专家，在介入治疗方面有突出贡献', 28, 'Harvard MD, PhD', 234, 350.00, 1, 1, 4.93, 456, 1, 5, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '国际心内科专家');
INSERT INTO `medical_expert` VALUES (6, 20, 'EXP006', 'Dr. Sarah Yamamoto', 'Sarah Yamamoto', 7, 'Tokyo University Hospital', 5, '眼科', 'Associate Professor', 'Ophthalmology, Retinal Diseases, Cataract Surgery', 'Japanese, English', 2, 45, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '日本眼科专家，擅长视网膜疾病治疗', 20, 'Tokyo University MD, PhD', 78, 280.00, 1, 1, 4.90, 234, 1, 6, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '日本眼科专家');
INSERT INTO `medical_expert` VALUES (7, 21, 'EXP007', '赵伟', 'Zhao Wei', 4, '中山大学附属第一医院', 10, '消化内科', '主任医师', '胃肠道疾病,幽门螺杆菌感染,胃镜诊疗', '中文,英文', 1, 50, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '消化内科专家，擅长胃镜检查和治疗', 26, '中山大学博士', 98, 550.00, 1, 1, 4.86, 189, 1, 7, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '消化内科专家');
INSERT INTO `medical_expert` VALUES (8, 22, 'EXP008', '刘芳', 'Liu Fang', 5, '四川大学华西医院', 3, '妇产科', '主任医师', '妇科肿瘤,腹腔镜手术,产科高危妊娠', '中文,英文', 2, 46, 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', '妇产科专家，在妇科微创手术方面有丰富经验', 22, '四川大学博士', 112, 650.00, 1, 1, 4.91, 267, 1, 8, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '妇产科专家');

-- ----------------------------
-- Table structure for medical_hospital
-- ----------------------------
DROP TABLE IF EXISTS `medical_hospital`;
CREATE TABLE `medical_hospital`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `hospital_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '医院编码，唯一索引',
  `hospital_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '医院名称',
  `hospital_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院英文名称',
  `hospital_type` tinyint NULL DEFAULT NULL COMMENT '医院类型（1：三甲医院；2：二甲医院；3：专科医院；4：国际医院）',
  `hospital_level` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院等级（如三级甲等）',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '区/县',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院地址',
  `longitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 6) NULL DEFAULT NULL COMMENT '纬度',
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院电话',
  `international_phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国际部电话',
  `website` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院网站',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '医院简介',
  `featured_departments` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '特色科室（多个用逗号分隔）',
  `appointment_notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '预约须知',
  `has_international_dept` tinyint NULL DEFAULT 2 COMMENT '是否有国际部（1：是；2：否）',
  `is_cooperation` tinyint NULL DEFAULT 2 COMMENT '是否合作（1：是；2：否）',
  `cooperation_start_date` date NULL DEFAULT NULL COMMENT '合作开始日期',
  `logo_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院Logo URL',
  `cover_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `rating_score` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '评分',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1：正常；2：停用）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `hospital_code`(`hospital_code` ASC) USING BTREE,
  INDEX `idx_hospital_type`(`hospital_type` ASC) USING BTREE,
  INDEX `idx_province`(`province` ASC) USING BTREE,
  INDEX `idx_city`(`city` ASC) USING BTREE,
  INDEX `idx_is_cooperation`(`is_cooperation` ASC) USING BTREE,
  INDEX `idx_rating_score`(`rating_score` ASC) USING BTREE,
  INDEX `idx_city_type_rating`(`city` ASC, `hospital_type` ASC, `rating_score` ASC) USING BTREE,
  FULLTEXT INDEX `ft_hospital_name`(`hospital_name`, `hospital_name_en`)
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医院信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medical_hospital
-- ----------------------------
INSERT INTO `medical_hospital` VALUES (1, 'HOSP001', '北京协和医院', 'Peking Union Medical College Hospital', 1, '三级甲等', '北京市', '北京市', '东城区', '北京市东城区帅府园一号', 116.409286, 39.913918, '+86 10-69156114', '+86 10-69155564', 'www.pumch.cn', '北京协和医院是集医疗、教学、科研于一体的现代化综合三级甲等医院', '内科,外科,妇产科,骨科,神经科', NULL, 1, 1, '2025-01-01', 'https://example.com/logo/pumch.jpg', 'https://example.com/cover/pumch.jpg', 4.92, 856, 1, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '顶级三甲医院');
INSERT INTO `medical_hospital` VALUES (2, 'HOSP002', '北京大学第一医院', 'Peking University First Hospital', 1, '三级甲等', '北京市', '北京市', '西城区', '北京市西城区西什库大街8号', 116.373737, 39.913252, '+86 10-83572020', '+86 10-66551020', 'www.bddyyy.com.cn', '北京大学第一医院是一所融医疗、教学、科研、预防为一体的大型综合性三级甲等医院', '泌尿外科,肾内科,皮肤科', NULL, 1, 1, '2025-01-01', 'https://example.com/logo/pku.jpg', 'https://example.com/cover/pku.jpg', 4.85, 623, 1, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '三甲医院');
INSERT INTO `medical_hospital` VALUES (3, 'HOSP003', '上海华山医院', 'Huashan Hospital', 1, '三级甲等', '上海市', '上海市', '静安区', '上海市静安区乌鲁木齐中路12号', 121.447854, 31.208536, '+86 21-52889999', '+86 21-52887979', 'www.huashan.org.cn', '华山医院是复旦大学附属医院，是国内最著名、最具国际化特色的医教研中心之一', '神经外科,手外科,感染科', NULL, 1, 1, '2025-01-01', 'https://example.com/logo/huashan.jpg', 'https://example.com/cover/huashan.jpg', 4.88, 712, 1, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '顶级三甲医院');
INSERT INTO `medical_hospital` VALUES (4, 'HOSP004', '中山大学附属第一医院', 'The First Affiliated Hospital of Sun Yat-sen University', 1, '三级甲等', '广东省', '广州市', '越秀区', '广州市中山二路58号', 113.265529, 23.120538, '+86 20-87755766', '+86 20-87755693', 'www.gzsums.net', '中山一院是华南地区医疗、教学、科研、预防保健和康复的重要基地', '普外科,肾内科,妇产科', NULL, 1, 1, '2025-01-15', 'https://example.com/logo/zs.jpg', 'https://example.com/cover/zs.jpg', 4.80, 534, 1, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '华南顶级医院');
INSERT INTO `medical_hospital` VALUES (5, 'HOSP005', '四川大学华西医院', 'West China Hospital', 1, '三级甲等', '四川省', '成都市', '武侯区', '成都市武侯区国学巷37号', 104.065795, 30.659072, '+86 28-85422114', '+86 28-85422050', 'www.wchscu.cn', '华西医院是中国西部疑难危急重症诊疗的国家级中心', '麻醉科,放射科,重症医学', NULL, 1, 1, '2025-01-20', 'https://example.com/logo/hx.jpg', 'https://example.com/cover/hx.jpg', 4.90, 892, 1, 5, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '西部顶级医院');
INSERT INTO `medical_hospital` VALUES (6, 'HOSP006', 'Mayo Clinic', 'Mayo Clinic', 4, 'International Hospital', 'Rochester', 'Minnesota', 'Rochester', '200 1st St SW, Rochester, MN 55905', -92.466528, 44.046236, '+1 507-284-2511', '+1 507-538-3270', 'www.mayoclinic.org', 'Mayo Clinic is a nonprofit American academic medical center focused on integrated clinical practice, education, and research', 'All Departments', NULL, 1, 1, '2025-02-01', 'https://example.com/logo/mayo.jpg', 'https://example.com/cover/mayo.jpg', 4.95, 1234, 1, 6, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '国际顶级医院');
INSERT INTO `medical_hospital` VALUES (7, 'HOSP007', 'Tokyo University Hospital', 'Tokyo University Hospital', 4, 'International Hospital', 'Tokyo', 'Tokyo', 'Bunkyo-ku', '7-3-1 Hongo, Bunkyo-ku, Tokyo', 139.761471, 35.712569, '+81 3-5800-8630', '+81 3-5800-0149', 'www.h.u-tokyo.ac.jp', 'Tokyo University Hospital is one of the leading hospitals in Japan', 'All Departments', NULL, 1, 1, '2025-02-10', 'https://example.com/logo/utokyo.jpg', 'https://example.com/cover/utokyo.jpg', 4.88, 876, 1, 7, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '日本顶级医院');

-- ----------------------------
-- Table structure for medical_record_upload
-- ----------------------------
DROP TABLE IF EXISTS `medical_record_upload`;
CREATE TABLE `medical_record_upload`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `record_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '病历编号，唯一索引',
  `user_id` bigint NOT NULL COMMENT '患者ID，关联user_user表',
  `triage_id` bigint NULL DEFAULT NULL COMMENT '分诊记录ID，关联medical_triage_record表',
  `record_type` tinyint NULL DEFAULT NULL COMMENT '病历类型（1：诊断证明；2：检查报告；3：处方单；4：手术记录；5：影像资料；6：其他）',
  `record_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '病历标题',
  `record_date` date NULL DEFAULT NULL COMMENT '病历日期',
  `hospital_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医院名称',
  `department_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '科室名称',
  `doctor_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '医生姓名',
  `file_urls` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件URL（多个文件用逗号分隔，存储在MinIO）',
  `file_types` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型（如pdf、jpg、png）',
  `original_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '原始文本内容',
  `translated_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '翻译后的文本内容',
  `translate_status` tinyint NULL DEFAULT 1 COMMENT '翻译状态（1：待翻译；2：翻译中；3：翻译完成；4：翻译失败）',
  `source_language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '源语言（如en、ja、ko）',
  `target_language` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'zh-CN' COMMENT '目标语言（如zh-CN）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `record_no`(`record_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_triage_id`(`triage_id` ASC) USING BTREE,
  INDEX `idx_record_type`(`record_type` ASC) USING BTREE,
  INDEX `idx_translate_status`(`translate_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '病历资料表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medical_record_upload
-- ----------------------------
INSERT INTO `medical_record_upload` VALUES (1, 'REC20260325180411F1396E', 1, NULL, NULL, ' 阿迪斯', NULL, '北京协和医院', '大赛', NULL, 'http://tmp/m-x-K1yGFVgP6bed5f0ae4bc3c74ce0fdb8a862ae981.jpg', NULL, NULL, NULL, 1, NULL, 'zh-CN', NULL, '2026-03-25 18:04:11', NULL, '2026-03-25 18:04:11', '的阿萨');

-- ----------------------------
-- Table structure for medical_triage_record
-- ----------------------------
DROP TABLE IF EXISTS `medical_triage_record`;
CREATE TABLE `medical_triage_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `triage_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分诊单号，唯一索引',
  `user_id` bigint NOT NULL COMMENT '患者ID，关联user_user表',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者姓名（冗余）',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者手机号（冗余）',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `chief_complaint` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '主诉',
  `symptom_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '症状描述',
  `symptom_image_urls` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '症状图片URL（多张用逗号分隔）',
  `pain_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '疼痛部位（如头部、胸部、腹部）',
  `pain_level` tinyint NULL DEFAULT NULL COMMENT '疼痛等级（1-10）',
  `pain_duration` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '疼痛持续时间',
  `medical_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '既往病史',
  `allergy_history` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '过敏史',
  `current_medication` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '当前用药',
  `triage_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '分诊结果（AI分析结果）',
  `recommended_department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐科室',
  `recommended_hospitals` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐医院（多个用逗号分隔）',
  `triage_status` tinyint NULL DEFAULT 1 COMMENT '分诊状态（1：待分诊；2：AI分诊中；3：AI分诊完成；4：待人工复审；5：人工复审完成；6：已生成方案）',
  `ai_confidence` decimal(5, 4) NULL DEFAULT NULL COMMENT 'AI分诊置信度',
  `ai_analysis_time` datetime NULL DEFAULT NULL COMMENT 'AI分析时间',
  `human_review_time` datetime NULL DEFAULT NULL COMMENT '人工复审时间',
  `human_reviewer_id` bigint NULL DEFAULT NULL COMMENT '人工复审人ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `triage_no`(`triage_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_triage_status`(`triage_status` ASC) USING BTREE,
  INDEX `idx_recommended_department`(`recommended_department` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_user_status_time`(`user_id` ASC, `triage_status` ASC, `create_time` ASC) USING BTREE,
  FULLTEXT INDEX `ft_symptom`(`chief_complaint`, `symptom_description`)
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '智能分诊记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of medical_triage_record
-- ----------------------------
INSERT INTO `medical_triage_record` VALUES (1, 'TRI20260301001', 1, '张三', '+86 13800138001', 1, 35, '持续性头痛一周', '头痛伴有恶心，偶有视物模糊，主要集中在前额和太阳穴部位', NULL, '头部', 6, '一周', '无特殊病史', '无', '无', '根据症状分析，可能为紧张性头痛或偏头痛，建议首先到神经内科就诊，行头颅CT或MRI检查排除器质性病变', '神经科', '北京协和医院神经科,上海华山医院神经科', 6, 0.8925, '2026-03-01 14:30:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', 'AI分诊完成');
INSERT INTO `medical_triage_record` VALUES (2, 'TRI20260302002', 2, '李四', '+86 13800138002', 2, 28, '反复胃痛三个月', '饭后胃痛明显，伴有反酸烧心，有时恶心，食欲下降', NULL, '腹部', 5, '三个月', '慢性浅表性胃炎', '青霉素', '奥美拉唑', '根据症状分析，考虑慢性胃炎或消化性溃疡，建议到消化内科就诊，行胃镜检查明确诊断', '消化内科', '中山大学附属第一医院消化内科,北京大学第一医院消化内科', 6, 0.9150, '2026-03-02 10:15:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', 'AI分诊完成');
INSERT INTO `medical_triage_record` VALUES (3, 'TRI20260303003', 4, 'John Doe', '+1 5551234567', 1, 45, 'Chest pain for 3 days', 'Persistent chest pain with shortness of breath, especially when climbing stairs', NULL, 'Chest', 7, '3 days', 'Hypertension, Diabetes', 'Penicillin', 'Aspirin, Metformin', 'Based on symptoms, possible cardiac issues. Recommend cardiology department, ECG and stress test required.', '心内科', 'Mayo Clinic Cardiology', 6, 0.9234, '2026-03-03 08:45:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', 'AI分诊完成');
INSERT INTO `medical_triage_record` VALUES (4, 'TRI20260304004', 5, '田中花子', '+81 90-1234-5678', 2, 38, '目の疲れと頭痛', '最近目が疲れやすく、頭痛もする。長時間画面を見ていると症状が悪化する', NULL, '頭部', 4, '2週間', '近視', 'なし', 'なし', '目の使いすぎによる眼精疲労の可能性があります。眼科の受診をお勧めします。', '眼科', 'Tokyo University Hospital眼科', 6, 0.8845, '2026-03-04 16:20:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', 'AI分诊完成');
INSERT INTO `medical_triage_record` VALUES (5, 'TRI20260305005', 3, '王五', '+86 13800138003', 1, 52, '膝关节疼痛肿胀', '右膝关节疼痛伴有肿胀，上下楼梯时疼痛加重，休息后稍有缓解', NULL, '膝关节', 7, '一个月', '痛风病史', '海鲜', '秋水仙碱', '根据症状分析，考虑膝关节骨关节炎或痛风性关节炎，建议到骨科就诊，行X线和血尿酸检查', '骨科', '北京协和医院骨科,北京大学第一医院骨科', 6, 0.9056, '2026-03-05 11:30:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', 'AI分诊完成');

-- ----------------------------
-- Table structure for order_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `order_evaluation`;
CREATE TABLE `order_evaluation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `order_id` bigint NOT NULL COMMENT '订单ID，关联order_order表',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号（冗余）',
  `user_id` bigint NOT NULL COMMENT '评价用户ID，关联user_user表',
  `accompanist_id` bigint NULL DEFAULT NULL COMMENT '被评价陪诊师ID，关联user_accompanist表',
  `rating_score` decimal(3, 2) NULL DEFAULT NULL COMMENT '评分（1-5分）',
  `rating_dimension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评分维度（如服务态度、专业程度、准时程度）',
  `rating_labels` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价标签（如态度好、翻译准确、准时）',
  `evaluation_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '评价内容',
  `evaluation_images` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价图片（多张用逗号分隔）',
  `evaluation_reply` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '陪诊师回复',
  `reply_time` datetime NULL DEFAULT NULL COMMENT '回复时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_accompanist_id`(`accompanist_id` ASC) USING BTREE,
  INDEX `idx_rating_score`(`rating_score` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_evaluation
-- ----------------------------
INSERT INTO `order_evaluation` VALUES (1, 1, 'ORD20260301001', 1, 7, 5.00, '综合评分', '态度好,专业程度高,准时', '赵医生非常专业，陪我做了全面的检查，讲解详细，态度亲切，非常满意！', 'https://example.com/eval/1_1.jpg,https://example.com/eval/1_2.jpg', '感谢您的认可，祝您早日康复！', '2026-03-11 10:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '好评');
INSERT INTO `order_evaluation` VALUES (2, 5, 'ORD20260305005', 3, 9, 4.50, '综合评分', '服务态度好,效率高', '孙丽帮我代办手续省了很多时间，很专业，强烈推荐！', 'https://example.com/eval/5_1.jpg', '感谢您的支持，欢迎下次使用我们的服务！', '2026-03-06 09:30:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '好评');
INSERT INTO `order_evaluation` VALUES (3, 6, 'ORD20260306006', 1, 7, 4.80, '综合评分', '准时,服务态度好', '接机很准时，帮忙拿行李，服务周到', NULL, '不客气，祝您就医顺利！', '2026-03-02 10:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '好评');
INSERT INTO `order_evaluation` VALUES (4, 1, 'ORD20260301001', 1, 1, 5.00, '综合评分', '态度好,专业程度高,准时', '赵医生非常专业，陪我做了全面的检查，讲解详细，态度亲切，非常满意！', 'https://example.com/eval/1_1.jpg,https://example.com/eval/1_2.jpg', '感谢您的认可，祝您早日康复！', '2026-03-11 10:00:00', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '好评');
INSERT INTO `order_evaluation` VALUES (5, 5, 'ORD20260305005', 3, 3, 4.50, '综合评分', '服务态度好,效率高', '孙丽帮我代办手续省了很多时间，很专业，强烈推荐！', 'https://example.com/eval/5_1.jpg', '感谢您的支持，欢迎下次使用我们的服务！', '2026-03-06 09:30:00', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '好评');
INSERT INTO `order_evaluation` VALUES (6, 6, 'ORD20260306006', 1, 1, 4.80, '综合评分', '准时,服务态度好', '接机很准时，帮忙拿行李，服务周到', NULL, '不客气，祝您就医顺利！', '2026-03-02 10:00:00', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '好评');

-- ----------------------------
-- Table structure for order_order
-- ----------------------------
DROP TABLE IF EXISTS `order_order`;
CREATE TABLE `order_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号，唯一索引',
  `user_id` bigint NOT NULL COMMENT '患者ID，关联user_user表',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者姓名（冗余）',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者手机号（冗余）',
  `accompanist_id` bigint NULL DEFAULT NULL COMMENT '陪诊师ID，关联user_accompanist表',
  `accompanist_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '陪诊师姓名（冗余）',
  `accompanist_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '陪诊师手机号（冗余）',
  `order_type` tinyint NULL DEFAULT NULL COMMENT '订单类型（1：全程陪诊；2：代办手续；3：接机服务；4：送机服务）',
  `order_status` tinyint NULL DEFAULT 1 COMMENT '订单状态（1：待付款；2：已付款；3：已确认；4：服务中；5：已完成；6：已取消；7：退款中；8：已退款）',
  `service_start_time` datetime NULL DEFAULT NULL COMMENT '服务开始时间',
  `service_end_time` datetime NULL DEFAULT NULL COMMENT '服务结束时间',
  `service_days` int NULL DEFAULT NULL COMMENT '服务天数',
  `service_hours` int NULL DEFAULT NULL COMMENT '服务小时数',
  `patient_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者姓名（用于就诊）',
  `patient_gender` tinyint NULL DEFAULT NULL COMMENT '患者性别',
  `patient_age` int NULL DEFAULT NULL COMMENT '患者年龄',
  `patient_passport` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '患者护照号',
  `disease_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '病情描述',
  `appointment_hospital` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '预约医院（冗余）',
  `appointment_department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '预约科室（冗余）',
  `appointment_date` date NULL DEFAULT NULL COMMENT '预约日期',
  `total_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `deposit_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '定金金额',
  `balance_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '尾款金额',
  `actual_paid_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际支付金额',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'USD' COMMENT '货币类型（USD、CNY、JPY等）',
  `flight_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '航班号',
  `flight_arrival_time` datetime NULL DEFAULT NULL COMMENT '航班到达时间',
  `hotel_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '酒店名称',
  `hotel_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '酒店地址',
  `pick_up_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '接机地址',
  `drop_off_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '送机地址',
  `cancel_time` datetime NULL DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '取消原因',
  `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '退款时间',
  `refund_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退款原因',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_accompanist_id`(`accompanist_id` ASC) USING BTREE,
  INDEX `idx_order_status`(`order_status` ASC) USING BTREE,
  INDEX `idx_service_start_time`(`service_start_time` ASC) USING BTREE,
  INDEX `idx_appointment_date`(`appointment_date` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_user_status_time`(`user_id` ASC, `order_status` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_order
-- ----------------------------
INSERT INTO `order_order` VALUES (1, 'ORD20260301001', 1, '张三', '+86 13800138001', 7, '赵敏', '+86 13900139001', 1, 5, '2026-03-10 08:00:00', '2026-03-10 17:00:00', 1, 8, '张三', 1, 35, NULL, '头痛待查', '北京协和医院', '神经科', '2026-03-10', 800.00, 300.00, 500.00, 800.00, 'USD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已完成订单');
INSERT INTO `order_order` VALUES (2, 'ORD20260302002', 2, '李四', '+86 13800138002', 8, '钱多多', '+86 13900139002', 1, 4, '2026-03-15 09:00:00', NULL, NULL, NULL, '李四', 2, 28, NULL, '胃痛复查', '中山大学附属第一医院', '消化内科', '2026-03-15', 600.00, 200.00, 400.00, 600.00, 'USD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '服务中订单');
INSERT INTO `order_order` VALUES (3, 'ORD20260303003', 4, 'John Doe', '+1 5551234567', 11, 'Sarah Chen', '+1 5559876543', 1, 2, NULL, NULL, NULL, NULL, 'John Doe', 1, 45, NULL, 'Heart checkup', 'Mayo Clinic', 'Cardiology', '2026-03-25', 1200.00, 400.00, 800.00, 400.00, 'USD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已付定金');
INSERT INTO `order_order` VALUES (4, 'ORD20260304004', 5, '田中花子', '+81 90-1234-5678', 10, '李强', '+86 13900139004', 1, 1, NULL, NULL, NULL, NULL, '田中花子', 2, 38, NULL, 'Eye examination', 'Tokyo University Hospital', 'Ophthalmology', '2026-03-28', 900.00, 300.00, 600.00, 0.00, 'USD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '待付款');
INSERT INTO `order_order` VALUES (5, 'ORD20260305005', 3, '王五', '+86 13800138003', 9, '孙丽', '+86 13900139003', 2, 5, '2026-03-05 10:00:00', '2026-03-05 14:00:00', 0, 4, '王五', 1, 52, NULL, '膝关节检查', '北京大学第一医院', '骨科', '2026-03-05', 400.00, 150.00, 250.00, 400.00, 'USD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '代办手续已完成');
INSERT INTO `order_order` VALUES (6, 'ORD20260306006', 1, '张三', '+86 13800138001', 7, '赵敏', '+86 13900139001', 3, 5, '2026-03-01 14:00:00', '2026-03-01 16:00:00', 0, 2, '张三', 1, 35, NULL, '接机服务', NULL, NULL, NULL, 200.00, 100.00, 100.00, 200.00, 'USD', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '接机服务已完成');
INSERT INTO `order_order` VALUES (10, 'ORD20260310010', 1, '张三', '+86 13800138001', NULL, NULL, NULL, 1, 2, NULL, NULL, NULL, NULL, '张三', 1, 35, NULL, '腰椎间盘突出复查', '北京协和医院', '骨科', '2026-04-01', 700.00, 250.00, 450.00, 700.00, 'CNY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-25 10:00:00', NULL, '2026-03-25 10:00:00', '待接单-患者张三');
INSERT INTO `order_order` VALUES (11, 'ORD20260311011', 2, '李四', '+86 13800138002', 7, NULL, NULL, 1, 3, NULL, NULL, NULL, NULL, '李四', 1, 28, NULL, '肠胃镜检查', '中山大学附属第一医院', '消化内科', '2026-04-02', 500.00, 180.00, 320.00, 500.00, 'CNY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-25 11:00:00', NULL, '2026-03-26 19:58:00', '待接单-患者李四');
INSERT INTO `order_order` VALUES (12, 'ORD20260312012', 3, '王五', '+86 13800138003', 7, NULL, NULL, 2, 3, NULL, NULL, NULL, NULL, '王五', 1, 52, NULL, '心脏支架术后复查', '北京大学第一医院', '心内科', '2026-04-03', 600.00, 200.00, 400.00, 600.00, 'CNY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-25 12:00:00', NULL, '2026-03-25 19:45:57', '待接单-患者王五');
INSERT INTO `order_order` VALUES (13, 'ORD20260313013', 1, '张三', '+86 13800138001', 7, '赵敏', '+86 13900139001', 1, 4, '2026-03-25 09:00:00', '2026-03-25 17:00:00', 0, 1, '张三', 1, 35, NULL, '全身体检', '北京协和医院', '体检中心', '2026-03-25', 800.00, 300.00, 500.00, 800.00, 'CNY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-25 08:00:00', NULL, '2026-03-25 08:00:00', '赵敏服务中');
INSERT INTO `order_order` VALUES (14, 'ORD20260314014', 2, '李四', '+86 13800138002', 8, '钱多多', '+86 13900139002', 1, 3, NULL, NULL, NULL, NULL, '李四', 2, 28, NULL, '儿科复诊', '上海儿童医学中心', '儿科', '2026-03-28', 500.00, 180.00, 320.00, 500.00, 'CNY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'system', '2026-03-25 09:00:00', NULL, '2026-03-25 09:00:00', '钱多多已确认');

-- ----------------------------
-- Table structure for order_payment
-- ----------------------------
DROP TABLE IF EXISTS `order_payment`;
CREATE TABLE `order_payment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `payment_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '支付流水号，唯一索引',
  `order_id` bigint NOT NULL COMMENT '订单ID，关联order_order表',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号（冗余）',
  `user_id` bigint NOT NULL COMMENT '支付用户ID，关联user_user表',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户姓名（冗余）',
  `payment_type` tinyint NULL DEFAULT NULL COMMENT '支付类型（1：定金；2：尾款；3：全额；4：退款）',
  `payment_method` tinyint NULL DEFAULT NULL COMMENT '支付方式（1：信用卡；2：支付宝；3：微信支付；4：Stripe；5：Adyen；6：银行转账）',
  `payment_status` tinyint NULL DEFAULT 1 COMMENT '支付状态（1：待支付；2：支付中；3：支付成功；4：支付失败；5：已退款）',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '货币类型',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `exchange_rate` decimal(10, 4) NULL DEFAULT NULL COMMENT '汇率',
  `rmb_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '人民币金额',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `payment_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '支付渠道（如stripe、adyen、alipay、wechat）',
  `channel_transaction_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '渠道交易号',
  `channel_order_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '渠道订单号',
  `payer_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '付款人邮箱',
  `payer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '付款人手机号',
  `callback_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '支付回调数据（JSON）',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `payment_id`(`payment_id` ASC) USING BTREE,
  UNIQUE INDEX `channel_transaction_id`(`channel_transaction_id` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_payment_status`(`payment_status` ASC) USING BTREE,
  INDEX `idx_payment_time`(`payment_time` ASC) USING BTREE,
  INDEX `idx_order_user_status`(`order_id` ASC, `user_id` ASC, `payment_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_payment
-- ----------------------------
INSERT INTO `order_payment` VALUES (1, 'PAY20260301001', 1, 'ORD20260301001', 1, '张三', 1, 4, 3, 'USD', 300.00, 7.2000, 2160.00, '2026-03-05 10:00:00', 'stripe', 'ch_1ABC123DEF456', 'ord_1ABC123', 'zhangsan@example.com', '+86 13800138001', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '定金支付');
INSERT INTO `order_payment` VALUES (2, 'PAY20260301002', 1, 'ORD20260301001', 1, '张三', 2, 4, 3, 'USD', 500.00, 7.2000, 3600.00, '2026-03-10 08:30:00', 'stripe', 'ch_1ABC123DEF789', 'ord_1ABC124', 'zhangsan@example.com', '+86 13800138001', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '尾款支付');
INSERT INTO `order_payment` VALUES (3, 'PAY20260302001', 2, 'ORD20260302002', 2, '李四', 1, 5, 3, 'USD', 200.00, 7.2000, 1440.00, '2026-03-12 14:00:00', 'adyen', 'adyen_20260302001', 'ord_2DEF456', 'lisi@example.com', '+86 13800138002', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '定金支付');
INSERT INTO `order_payment` VALUES (4, 'PAY20260303001', 3, 'ORD20260303003', 4, 'John Doe', 1, 1, 3, 'USD', 400.00, 7.2000, 2880.00, '2026-03-15 09:00:00', 'credit_card', 'txn_3GHI789', 'ord_3GHI789', 'john.doe@example.com', '+1 5551234567', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '定金支付');
INSERT INTO `order_payment` VALUES (5, 'PAY20260305001', 5, 'ORD20260305005', 3, '王五', 3, 4, 3, 'USD', 400.00, 7.2000, 2880.00, '2026-03-04 15:00:00', 'stripe', 'ch_4JKL012', 'ord_4JKL012', 'wangwu@example.com', '+86 13800138003', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '全额支付');
INSERT INTO `order_payment` VALUES (6, 'PAY20260306001', 6, 'ORD20260306006', 1, '张三', 3, 4, 3, 'USD', 200.00, 7.2000, 1440.00, '2026-02-28 11:00:00', 'stripe', 'ch_5MNO345', 'ord_5MNO345', 'zhangsan@example.com', '+86 13800138001', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '全额支付');

-- ----------------------------
-- Table structure for order_service_item
-- ----------------------------
DROP TABLE IF EXISTS `order_service_item`;
CREATE TABLE `order_service_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `order_id` bigint NOT NULL COMMENT '订单ID，关联order_order表',
  `service_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务项目编码',
  `service_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务项目名称',
  `service_type` tinyint NULL DEFAULT NULL COMMENT '服务类型（1：基础服务；2：增值服务；3：附加服务）',
  `unit_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单价',
  `quantity` int NULL DEFAULT NULL COMMENT '数量',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位（小时、天、次）',
  `subtotal` decimal(10, 2) NULL DEFAULT NULL COMMENT '小计',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单服务项目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_service_item
-- ----------------------------
INSERT INTO `order_service_item` VALUES (1, 1, 'SVC001', '全程陪诊服务（8小时）', 1, 800.00, 1, '次', 800.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础服务');
INSERT INTO `order_service_item` VALUES (2, 2, 'SVC001', '全程陪诊服务（6小时）', 1, 600.00, 1, '次', 600.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础服务');
INSERT INTO `order_service_item` VALUES (3, 3, 'SVC001', '全程陪诊服务（2天）', 1, 1200.00, 1, '天', 1200.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础服务');
INSERT INTO `order_service_item` VALUES (4, 3, 'SVC002', '病历翻译服务', 2, 150.00, 1, '次', 150.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '增值服务');
INSERT INTO `order_service_item` VALUES (5, 4, 'SVC001', '全程陪诊服务（1天）', 1, 900.00, 1, '天', 900.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础服务');
INSERT INTO `order_service_item` VALUES (6, 5, 'SVC003', '代办手续服务（4小时）', 1, 400.00, 1, '次', 400.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础服务');
INSERT INTO `order_service_item` VALUES (7, 6, 'SVC004', '接机服务（2小时）', 1, 200.00, 1, '次', 200.00, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础服务');
INSERT INTO `order_service_item` VALUES (8, 1, 'SVC001', '全程陪诊服务（8小时）', 1, 800.00, 1, '次', 800.00, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '基础服务');
INSERT INTO `order_service_item` VALUES (9, 2, 'SVC001', '全程陪诊服务（6小时）', 1, 600.00, 1, '次', 600.00, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '基础服务');
INSERT INTO `order_service_item` VALUES (10, 3, 'SVC001', '全程陪诊服务（2天）', 1, 1200.00, 1, '天', 1200.00, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '基础服务');
INSERT INTO `order_service_item` VALUES (11, 4, 'SVC001', '全程陪诊服务（1天）', 1, 900.00, 1, '天', 900.00, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '基础服务');
INSERT INTO `order_service_item` VALUES (12, 5, 'SVC003', '代办手续服务（4小时）', 1, 400.00, 1, '次', 400.00, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '基础服务');
INSERT INTO `order_service_item` VALUES (13, 6, 'SVC004', '接机服务（2小时）', 1, 200.00, 1, '次', 200.00, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '基础服务');

-- ----------------------------
-- Table structure for payment_record
-- ----------------------------
DROP TABLE IF EXISTS `payment_record`;
CREATE TABLE `payment_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `payment_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付订单号',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `payment_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付类型(wechat/alipay/cash)',
  `amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
  `currency` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CNY',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transaction_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方交易号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1待支付 2已支付 3已取消 4退款中 5已退款)',
  `refund_amount` decimal(10, 2) NULL DEFAULT NULL,
  `refund_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `refund_time` datetime NULL DEFAULT NULL,
  `pay_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_payment_no`(`payment_no` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of payment_record
-- ----------------------------
INSERT INTO `payment_record` VALUES (1, 'PAY20260301001', 'ORD20260301001', 1, 'wechat', 800.00, 'CNY', '全程陪诊服务费', 'WX20260301001001', 2, NULL, NULL, NULL, '2026-03-01 09:30:00', '2026-03-01 09:00:00', '2026-03-10 17:30:00');
INSERT INTO `payment_record` VALUES (2, 'PAY20260302001', 'ORD20260302002', 2, 'alipay', 600.00, 'CNY', '陪诊服务费', 'ALI20260302001001', 2, NULL, NULL, NULL, '2026-03-02 10:15:00', '2026-03-02 09:50:00', '2026-03-02 10:15:00');
INSERT INTO `payment_record` VALUES (3, 'PAY20260302002', 'ORD20260302002', 2, 'alipay', 150.00, 'CNY', '退款-服务未完成', NULL, 5, 150.00, '服务未按约定完成', '2026-03-24 15:00:00', NULL, '2026-03-20 10:00:00', '2026-03-24 15:00:00');
INSERT INTO `payment_record` VALUES (4, 'PAY20260303001', 'ORD20260303003', 4, 'cash', 500.00, 'USD', 'Medical escort fee', NULL, 3, NULL, NULL, NULL, NULL, '2026-03-03 08:00:00', '2026-03-18 14:00:00');
INSERT INTO `payment_record` VALUES (5, 'PAY20260304001', 'ORD20260304004', 5, 'wechat', 1200.00, 'CNY', '全程陪诊-长期服务', 'WX20260304001001', 2, NULL, NULL, NULL, '2026-03-04 08:20:00', '2026-03-04 08:00:00', '2026-03-04 08:20:00');
INSERT INTO `payment_record` VALUES (6, 'PAY20260305001', 'ORD20260305005', 3, 'alipay', 450.00, 'CNY', '陪诊服务定金', 'ALI20260305001001', 2, NULL, NULL, NULL, '2026-03-05 09:10:00', '2026-03-05 09:00:00', '2026-03-05 09:10:00');
INSERT INTO `payment_record` VALUES (7, 'PAY20260306001', 'ORD20260306006', 1, 'wechat', 320.00, 'CNY', '代办手续服务费', 'WX20260306001001', 2, NULL, NULL, NULL, '2026-03-06 07:50:00', '2026-03-06 07:30:00', '2026-03-06 07:50:00');
INSERT INTO `payment_record` VALUES (8, 'PAY20260310001', NULL, 1, 'wechat', 200.00, 'CNY', '服务套餐预购', 'WX20260310001001', 2, NULL, NULL, NULL, '2026-03-10 10:05:00', '2026-03-10 10:00:00', '2026-03-10 10:05:00');
INSERT INTO `payment_record` VALUES (9, 'PAY20260315001', NULL, 6, 'alipay', 300.00, 'CNY', '慢性病护理套餐', 'ALI20260315001001', 1, NULL, NULL, NULL, NULL, '2026-03-15 11:00:00', '2026-03-15 11:00:00');
INSERT INTO `payment_record` VALUES (10, 'PAY20260320001', NULL, 2, 'cash', 180.00, 'CNY', '上门取药服务', NULL, 4, 180.00, '用户申请全额退款', NULL, NULL, '2026-03-20 14:00:00', '2026-03-23 09:00:00');

-- ----------------------------
-- Table structure for service_accompanist_schedule
-- ----------------------------
DROP TABLE IF EXISTS `service_accompanist_schedule`;
CREATE TABLE `service_accompanist_schedule`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `accompanist_id` bigint NOT NULL COMMENT '陪诊师ID，关联user_accompanist表',
  `schedule_date` date NOT NULL COMMENT '日期',
  `work_shift` tinyint NULL DEFAULT NULL COMMENT '班次（1：上午；2：下午；3：晚上；4：全天）',
  `start_time` time NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` time NULL DEFAULT NULL COMMENT '结束时间',
  `schedule_status` tinyint NULL DEFAULT 1 COMMENT '日程状态（1：可用；2：已预约；3：已占用；4：请假）',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID',
  `service_city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务城市',
  `service_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '服务地址',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_accompanist_id`(`accompanist_id` ASC) USING BTREE,
  INDEX `idx_schedule_date`(`schedule_date` ASC) USING BTREE,
  INDEX `idx_schedule_status`(`schedule_status` ASC) USING BTREE,
  INDEX `idx_service_city_date`(`service_city` ASC, `schedule_date` ASC) USING BTREE,
  INDEX `idx_city_status_date`(`service_city` ASC, `schedule_status` ASC, `schedule_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '陪诊师日程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of service_accompanist_schedule
-- ----------------------------
INSERT INTO `service_accompanist_schedule` VALUES (1, 7, '2026-03-18', 1, '08:00:00', '12:00:00', 2, 2, '北京', '北京协和医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (2, 7, '2026-03-19', 1, '08:00:00', '12:00:00', 1, NULL, '北京', '北京协和医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (3, 7, '2026-03-19', 2, '14:00:00', '18:00:00', 1, NULL, '北京', '北京大学第一医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (4, 7, '2026-03-20', 1, '08:00:00', '12:00:00', 2, 2, '北京', '北京协和医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (5, 8, '2026-03-18', 1, '08:00:00', '12:00:00', 1, NULL, '广州', '中山大学附属第一医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (6, 8, '2026-03-18', 2, '14:00:00', '18:00:00', 1, NULL, '广州', '中山大学附属第一医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (7, 9, '2026-03-18', 1, '08:00:00', '12:00:00', 3, NULL, '上海', '华山医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '请假');
INSERT INTO `service_accompanist_schedule` VALUES (8, 10, '2026-03-20', 4, '08:00:00', '18:00:00', 1, NULL, '北京', '北京协和医院', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (9, 11, '2026-03-25', 4, '08:00:00', '18:00:00', 2, 3, 'Rochester', 'Mayo Clinic', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (10, 11, '2026-03-26', 4, '08:00:00', '18:00:00', 2, 3, 'Rochester', 'Mayo Clinic', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (11, 1, '2026-03-18', 1, '08:00:00', '12:00:00', 2, 2, '北京', '北京协和医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (12, 1, '2026-03-19', 1, '08:00:00', '12:00:00', 1, NULL, '北京', '北京协和医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (13, 1, '2026-03-19', 2, '14:00:00', '18:00:00', 1, NULL, '北京', '北京大学第一医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (14, 1, '2026-03-20', 1, '08:00:00', '12:00:00', 2, 2, '北京', '北京协和医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (15, 2, '2026-03-18', 1, '08:00:00', '12:00:00', 1, NULL, '广州', '中山大学附属第一医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (16, 2, '2026-03-18', 2, '14:00:00', '18:00:00', 1, NULL, '广州', '中山大学附属第一医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (17, 3, '2026-03-18', 1, '08:00:00', '12:00:00', 3, NULL, '上海', '华山医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '请假');
INSERT INTO `service_accompanist_schedule` VALUES (18, 4, '2026-03-20', 4, '08:00:00', '18:00:00', 1, NULL, '北京', '北京协和医院', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '可用');
INSERT INTO `service_accompanist_schedule` VALUES (19, 5, '2026-03-25', 4, '08:00:00', '18:00:00', 2, 3, 'Rochester', 'Mayo Clinic', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '已预约');
INSERT INTO `service_accompanist_schedule` VALUES (20, 5, '2026-03-26', 4, '08:00:00', '18:00:00', 2, 3, 'Rochester', 'Mayo Clinic', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '已预约');

-- ----------------------------
-- Table structure for service_dispatch
-- ----------------------------
DROP TABLE IF EXISTS `service_dispatch`;
CREATE TABLE `service_dispatch`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `dispatch_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '派单编号，唯一索引',
  `order_id` bigint NOT NULL COMMENT '订单ID，关联order_order表',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '订单号（冗余）',
  `accompanist_id` bigint NOT NULL COMMENT '陪诊师ID，关联user_accompanist表',
  `accompanist_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '陪诊师姓名（冗余）',
  `accompanist_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '陪诊师手机号（冗余）',
  `dispatch_type` tinyint NULL DEFAULT NULL COMMENT '派单类型（1：自动派单；2：手动派单）',
  `dispatch_status` tinyint NULL DEFAULT 1 COMMENT '派单状态（1：待接单；2：已接单；3：已拒绝；4：已取消）',
  `push_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '推送时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '接单时间',
  `reject_time` datetime NULL DEFAULT NULL COMMENT '拒绝时间',
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `dispatch_no`(`dispatch_no` ASC) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_accompanist_id`(`accompanist_id` ASC) USING BTREE,
  INDEX `idx_dispatch_status`(`dispatch_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '调度派单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of service_dispatch
-- ----------------------------
INSERT INTO `service_dispatch` VALUES (1, 'DIS20260301001', 2, 'ORD20260302002', 7, '赵敏', '+86 13900139001', 1, 2, '2026-03-12 15:00:00', '2026-03-12 15:05:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '自动派单-已接单');
INSERT INTO `service_dispatch` VALUES (2, 'DIS20260301002', 2, 'ORD20260302002', 8, '钱多多', '+86 13900139002', 1, 3, '2026-03-12 15:00:00', NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '自动派单-已拒绝');
INSERT INTO `service_dispatch` VALUES (3, 'DIS20260302001', 3, 'ORD20260303003', 11, 'Sarah Chen', '+1 5559876543', 1, 2, '2026-03-15 09:30:00', '2026-03-15 09:35:00', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '自动派单-已接单');
INSERT INTO `service_dispatch` VALUES (4, 'DIS20260303001', 4, 'ORD20260304004', 10, '李强', '+86 13900139004', 2, 1, '2026-03-16 10:00:00', NULL, NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '手动派单-待接单');

-- ----------------------------
-- Table structure for service_package
-- ----------------------------
DROP TABLE IF EXISTS `service_package`;
CREATE TABLE `service_package`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `package_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐编码，唯一索引',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名称',
  `package_name_en` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '套餐英文名称',
  `package_type` tinyint NULL DEFAULT NULL COMMENT '套餐类型（1：基础套餐；2：增值套餐；3：定制套餐）',
  `service_type` tinyint NULL DEFAULT NULL COMMENT '服务类型（1：全程陪诊；2：代办手续；3：接机服务；4：送机服务；5：翻译服务）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '套餐描述',
  `service_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '服务内容（JSON格式）',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `currency` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'USD' COMMENT '货币类型',
  `unit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '单位（次、天、小时）',
  `duration` int NULL DEFAULT NULL COMMENT '服务时长（小时）',
  `max_usage_count` int NULL DEFAULT -1 COMMENT '最大使用次数（-1表示不限）',
  `valid_days` int NULL DEFAULT NULL COMMENT '有效期（天）',
  `cover_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `detail_image_urls` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '详情图片URL（多个用逗号分隔）',
  `includes_items` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '包含项目',
  `exclude_items` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '不包含项目',
  `usage_notice` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '使用须知',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1：上架；2：下架；3：停售）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `package_code`(`package_code` ASC) USING BTREE,
  INDEX `idx_package_type`(`package_type` ASC) USING BTREE,
  INDEX `idx_service_type`(`service_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '服务套餐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of service_package
-- ----------------------------
INSERT INTO `service_package` VALUES (1, 'PKG001', '全程陪诊基础套餐', 'Full Accompany Service Basic', 1, 1, '基础陪诊服务，适合普通门诊', '{\"service_hours\": 8, \"hospital_guide\": true, \"queue_assist\": true, \"report_collection\": true}', 800.00, 'USD', '次', 8, 1, 90, 'https://example.com/package/basic.jpg', NULL, '医院引导,排队协助,代取报告', '挂号费用,检查费用,药品费用', '请提前24小时预约', 1, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '基础套餐');
INSERT INTO `service_package` VALUES (2, 'PKG002', '全程陪诊尊享套餐', 'Full Accompany Service Premium', 2, 1, '尊享陪诊服务，包含增值服务', '{\"service_hours\": 8, \"hospital_guide\": true, \"queue_assist\": true, \"report_collection\": true, \"medical_translation\": true, \"follow_up_call\": true}', 1200.00, 'USD', '次', 8, 1, 90, 'https://example.com/package/premium.jpg', NULL, '医院引导,排队协助,代取报告,医学翻译,复诊电话', '挂号费用,检查费用,药品费用', '请提前48小时预约', 2, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '尊享套餐');
INSERT INTO `service_package` VALUES (3, 'PKG003', '代办手续服务', 'Procedures Agent Service', 1, 2, '代办理各种就医手续', '{\"procedures\": [\"挂号\", \"取药\", \"缴费\", \"检查预约\"], \"duration\": 4}', 400.00, 'USD', '次', 4, 1, 30, 'https://example.com/package/procedures.jpg', NULL, '代挂号,代取药,代缴费,检查预约', '挂号费用,药品费用', '请提供患者证件', 3, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '代办套餐');
INSERT INTO `service_package` VALUES (4, 'PKG004', '接机服务', 'Airport Pickup Service', 1, 3, '专业接机服务', '{\"waiting_time\": 2, \"flight_tracking\": true, \"help_luggage\": true}', 200.00, 'USD', '次', 2, 1, 30, 'https://example.com/package/pickup.jpg', NULL, '航班追踪,举牌接机,行李协助', '停车费用', '请提供航班信息', 4, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '接机套餐');
INSERT INTO `service_package` VALUES (5, 'PKG005', '送机服务', 'Airport Dropoff Service', 1, 4, '专业送机服务', '{\"help_checkin\": true, \"help_luggage\": true}', 180.00, 'USD', '次', 2, 1, 30, 'https://example.com/package/dropoff.jpg', NULL, '值机协助,行李协助,送至安检', '停车费用', '请提前3小时预约', 5, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '送机套餐');
INSERT INTO `service_package` VALUES (6, 'PKG006', '病历翻译服务', 'Medical Record Translation', 3, 5, '专业医学翻译', '{\"languages\": [\"中文\", \"英文\", \"日文\", \"韩文\"], \"translation_type\": \"medical\", \"delivery_time\": 48}', 150.00, 'USD', '次', 0, 1, 180, 'https://example.com/package/translation.jpg', NULL, '医学翻译,专业校对,电子版交付', '加急费用', '请提供清晰病历资料', 6, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '翻译套餐');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键，唯一索引',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '配置值',
  `config_type` tinyint NULL DEFAULT NULL COMMENT '配置类型（1：系统；2：业务；3：第三方）',
  `config_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置名称',
  `config_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置描述',
  `is_encrypt` tinyint NULL DEFAULT 2 COMMENT '是否加密（1：是；2：否）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1：正常；2：停用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'app_name', '医路安心', 1, '应用名称', '系统应用名称', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '医路安心平台');
INSERT INTO `sys_config` VALUES (2, 'app_version', '1.0.0', 1, '应用版本', '系统应用版本', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '初始版本');
INSERT INTO `sys_config` VALUES (3, 'jwt_secret', 'your_jwt_secret_key_here', 1, 'JWT密钥', 'JWT加密密钥', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要修改为实际密钥');
INSERT INTO `sys_config` VALUES (4, 'jwt_expiration', '604800', 1, 'JWT过期时间', 'JWT token过期时间（秒）', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '7天');
INSERT INTO `sys_config` VALUES (5, 'stripe_api_key', 'sk_test_your_stripe_key', 3, 'Stripe API密钥', 'Stripe支付网关API密钥', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要配置实际密钥');
INSERT INTO `sys_config` VALUES (6, 'adyen_api_key', 'your_adyen_api_key', 3, 'Adyen API密钥', 'Adyen支付网关API密钥', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要配置实际密钥');
INSERT INTO `sys_config` VALUES (7, 'alipay_app_id', 'your_alipay_app_id', 3, '支付宝AppID', '支付宝应用ID', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要配置实际ID');
INSERT INTO `sys_config` VALUES (8, 'wechat_app_id', 'your_wechat_app_id', 3, '微信AppID', '微信应用ID', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要配置实际ID');
INSERT INTO `sys_config` VALUES (9, 'minio_endpoint', 'http://localhost:9000', 3, 'MinIO端点', 'MinIO服务器地址', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '文件存储服务');
INSERT INTO `sys_config` VALUES (10, 'minio_access_key', 'minioadmin', 3, 'MinIO访问密钥', 'MinIO访问密钥', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要修改为实际密钥');
INSERT INTO `sys_config` VALUES (11, 'minio_secret_key', 'minioadmin', 3, 'MinIO秘密密钥', 'MinIO秘密密钥', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要修改为实际密钥');
INSERT INTO `sys_config` VALUES (12, 'redis_host', 'localhost', 3, 'Redis主机', 'Redis服务器地址', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '缓存服务');
INSERT INTO `sys_config` VALUES (13, 'redis_port', '6379', 3, 'Redis端口', 'Redis服务器端口', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '默认端口');
INSERT INTO `sys_config` VALUES (14, 'redis_password', '', 3, 'Redis密码', 'Redis服务器密码', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '如果需要认证');
INSERT INTO `sys_config` VALUES (15, 'mongodb_uri', 'mongodb://localhost:27017', 3, 'MongoDB连接', 'MongoDB数据库连接字符串', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', 'AI对话数据存储');
INSERT INTO `sys_config` VALUES (16, 'ai_model_name', 'gpt-4', 3, 'AI模型名称', '使用的AI模型', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '智能分诊使用的模型');
INSERT INTO `sys_config` VALUES (17, 'ai_api_key', 'your_ai_api_key', 3, 'AI API密钥', 'AI服务API密钥', 1, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '需要配置实际密钥');
INSERT INTO `sys_config` VALUES (18, 'sms_provider', 'twilio', 3, '短信提供商', '短信服务提供商', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '支持twilio、阿里云等');
INSERT INTO `sys_config` VALUES (19, 'email_smtp_host', 'smtp.gmail.com', 3, '邮件SMTP主机', '邮件服务器地址', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '邮件通知服务');
INSERT INTO `sys_config` VALUES (20, 'email_smtp_port', '587', 3, '邮件SMTP端口', '邮件服务器端口', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '通常为587或465');
INSERT INTO `sys_config` VALUES (21, 'email_from_address', 'noreply@yiluxinxin.com', 3, '邮件发送地址', '系统邮件发送地址', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '系统通知邮箱');
INSERT INTO `sys_config` VALUES (22, 'max_upload_file_size', '104857600', 2, '最大上传文件大小', '单位字节，默认100MB', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '100MB');
INSERT INTO `sys_config` VALUES (23, 'allowed_file_extensions', 'pdf,jpg,jpeg,png,doc,docx,xls,xlsx', 2, '允许上传文件类型', '逗号分隔', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '文件上传限制');
INSERT INTO `sys_config` VALUES (24, 'order_timeout_minutes', '30', 2, '订单超时时间', '未支付订单自动取消时间（分钟）', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '30分钟');
INSERT INTO `sys_config` VALUES (25, 'refund_deadline_days', '7', 2, '退款截止天数', '订单完成后可申请退款的天数', 2, 0, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '7天内可退款');

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `receiver_id` bigint NOT NULL COMMENT '接收用户ID',
  `sender_id` bigint NULL DEFAULT 0 COMMENT '发送用户ID(0=系统)',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息类型(system/order/chat)',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '消息内容',
  `related_id` bigint NULL DEFAULT NULL COMMENT '关联业务ID',
  `related_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联业务类型',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1未读 2已读)',
  `read_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_receiver_id`(`receiver_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_message
-- ----------------------------
INSERT INTO `sys_message` VALUES (1, 1, 0, 'system', '欢迎注册', '欢迎您注册医路安心平台！我们将为您提供专业的陪诊服务。', NULL, NULL, 2, '2026-01-10 09:05:00', '2026-01-10 09:00:00');
INSERT INTO `sys_message` VALUES (2, 1, 0, 'order', '订单确认通知', '您的订单 ORD20260301001 已确认，陪诊师赵敏将为您提供服务，请准时到达医院。', 1, 'order', 2, '2026-03-01 10:00:00', '2026-03-01 09:00:00');
INSERT INTO `sys_message` VALUES (3, 1, 0, 'order', '服务完成通知', '您的订单 ORD20260301001 已完成，感谢您的使用，请对本次服务进行评价。', 1, 'order', 2, '2026-03-10 18:00:00', '2026-03-10 17:30:00');
INSERT INTO `sys_message` VALUES (4, 1, 0, 'system', '系统维护通知', '平台将于2026-04-01 02:00-04:00进行系统维护，期间服务暂停，敬请谅解。', NULL, NULL, 1, NULL, '2026-03-20 10:00:00');
INSERT INTO `sys_message` VALUES (5, 1, 0, 'order', '需求已被接单', '您发布的需求「接送父母就医」已被陪诊师接单，请保持电话畅通。', 2, 'demand', 1, NULL, '2026-03-21 10:05:00');
INSERT INTO `sys_message` VALUES (6, 2, 0, 'system', '欢迎注册', '欢迎您注册医路安心平台！我们将为您提供专业的陪诊服务。', NULL, NULL, 2, '2026-01-12 11:05:00', '2026-01-12 11:00:00');
INSERT INTO `sys_message` VALUES (7, 2, 0, 'order', '订单支付成功', '您的订单 ORD20260302002 支付成功，金额 ¥600，请等待陪诊师确认。', 2, 'order', 2, '2026-03-02 10:30:00', '2026-03-02 10:15:00');
INSERT INTO `sys_message` VALUES (8, 2, 0, 'system', '实名认证提醒', '您尚未完成实名认证，完成后可享受更多服务权益，请前往个人中心认证。', NULL, NULL, 1, NULL, '2026-03-15 09:00:00');
INSERT INTO `sys_message` VALUES (9, 3, 0, 'system', '欢迎注册', '欢迎您注册医路安心平台！我们将为您提供专业的陪诊服务。', NULL, NULL, 2, '2026-01-13 12:05:00', '2026-01-13 12:00:00');
INSERT INTO `sys_message` VALUES (10, 3, 0, 'order', '订单取消通知', '您的订单 ORD20260303003 已取消，退款将在3-5个工作日内原路返回。', 3, 'order', 1, NULL, '2026-03-18 14:00:00');
INSERT INTO `sys_message` VALUES (11, 4, 0, 'system', '欢迎注册', 'Welcome to PeacefulJourney! We provide professional medical escort services.', NULL, NULL, 2, '2026-01-15 08:05:00', '2026-01-15 08:00:00');
INSERT INTO `sys_message` VALUES (12, 4, 0, 'order', 'Order Confirmed', 'Your order ORD20260303003 has been confirmed. Your escort will meet you at the hospital entrance.', 3, 'order', 2, '2026-03-03 09:00:00', '2026-03-03 08:30:00');
INSERT INTO `sys_message` VALUES (13, 5, 0, 'system', '欢迎注册', '欢迎您注册医路安心平台！我们将为您提供专业的陪诊服务。', NULL, NULL, 2, '2026-01-16 09:35:00', '2026-01-16 09:30:00');
INSERT INTO `sys_message` VALUES (14, 5, 0, 'order', '订单评价提醒', '您有一笔订单待评价，您的评价对我们非常重要，请抽空进行评价。', 5, 'order', 1, NULL, '2026-03-25 08:00:00');
INSERT INTO `sys_message` VALUES (15, 6, 0, 'system', '欢迎注册', '欢迎您注册医路安心平台！', NULL, NULL, 1, NULL, '2026-02-10 14:05:00');
INSERT INTO `sys_message` VALUES (16, 6, 0, 'system', '需求发布成功', '您的需求「慢性病复查」已发布成功，等待陪诊师接单中。', 8, 'demand', 1, NULL, '2026-03-24 09:05:00');
INSERT INTO `sys_message` VALUES (17, 1, 0, 'system', '积分到账通知', '恭喜您获得50积分，当前总积分：350分，可用于兑换优惠券。', NULL, NULL, 1, NULL, '2026-03-24 12:00:00');
INSERT INTO `sys_message` VALUES (18, 2, 0, 'order', '退款到账通知', '您申请的退款已处理完成，¥150已退回您的支付账户，请注意查收。', 2, 'order', 1, NULL, '2026-03-24 15:00:00');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `notice_type` tinyint NULL DEFAULT NULL COMMENT '通知类型（1：系统公告；2：活动通知；3：版本更新）',
  `notice_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知标题',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '通知内容',
  `notice_link` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '通知链接',
  `target_type` tinyint NULL DEFAULT NULL COMMENT '目标类型（1：全体用户；2：指定用户；3：指定角色）',
  `target_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目标ID列表',
  `publish_status` tinyint NULL DEFAULT 1 COMMENT '发布状态（1：待发布；2：已发布；3：已撤回）',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_notice_type`(`notice_type` ASC) USING BTREE,
  INDEX `idx_publish_status`(`publish_status` ASC) USING BTREE,
  INDEX `idx_publish_time`(`publish_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, 1, '系统升级通知', '系统将于2026年3月20日凌晨2:00-4:00进行升级维护，届时部分功能可能不可用，请提前做好准备。', NULL, 1, NULL, 2, '2026-03-15 10:00:00', '2026-03-21 00:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '系统公告');
INSERT INTO `sys_notice` VALUES (2, 2, '新春优惠活动', '即日起至2026年3月31日，新用户首单享受8折优惠，欢迎使用我们的陪诊服务！', '/promotion/spring2026', 1, NULL, 2, '2026-03-01 09:00:00', '2026-03-31 23:59:59', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '活动通知');
INSERT INTO `sys_notice` VALUES (3, 3, '版本更新 v2.1.0', '本次更新：1.优化AI分诊准确性 2.新增日语界面支持 3.修复若干已知问题', '/release/v2.1.0', 1, NULL, 2, '2026-03-10 15:00:00', NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '版本更新');
INSERT INTO `sys_notice` VALUES (4, 1, '系统升级通知', '系统将于2026年3月20日凌晨2:00-4:00进行升级维护，届时部分功能可能不可用，请提前做好准备。', NULL, 1, NULL, 2, '2026-03-15 10:00:00', '2026-03-21 00:00:00', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '系统公告');
INSERT INTO `sys_notice` VALUES (5, 2, '新春优惠活动', '即日起至2026年3月31日，新用户首单享受8折优惠，欢迎使用我们的陪诊服务！', '/promotion/spring2026', 1, NULL, 2, '2026-03-01 09:00:00', '2026-03-31 23:59:59', 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '活动通知');
INSERT INTO `sys_notice` VALUES (6, 3, '版本更新 v2.1.0', '本次更新：1.优化AI分诊准确性 2.新增日语界面支持 3.修复若干已知问题', '/release/v2.1.0', 1, NULL, 2, '2026-03-10 15:00:00', NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '版本更新');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `log_type` tinyint NULL DEFAULT NULL COMMENT '日志类型（1：操作日志；2：访问日志；3：异常日志）',
  `operate_module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作模块',
  `operate_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `operate_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人',
  `operate_by_id` bigint NULL DEFAULT NULL COMMENT '操作人ID',
  `operate_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作IP',
  `operate_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作地点',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求URL',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `response_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '响应结果',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `execute_time` int NULL DEFAULT NULL COMMENT '执行时长（毫秒）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operate_by_id`(`operate_by_id` ASC) USING BTREE,
  INDEX `idx_operate_module`(`operate_module` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, 1, '用户管理', '登录', 'zhangsan', 1, '223.104.3.25', '北京市', 'POST', '/api/user/login', '{\"username\":\"zhangsan\"}', '{\"code\":200,\"msg\":\"success\"}', NULL, 125, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '用户登录');
INSERT INTO `sys_operation_log` VALUES (2, 1, '订单管理', '创建订单', 'zhangsan', 1, '223.104.3.25', '北京市', 'POST', '/api/order/create', '{\"order_type\":1}', '{\"code\":200,\"msg\":\"success\",\"data\":{\"order_no\":\"ORD20260301001\"}}', NULL, 856, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '创建订单');
INSERT INTO `sys_operation_log` VALUES (3, 1, '支付管理', '支付', 'zhangsan', 1, '223.104.3.25', '北京市', 'POST', '/api/payment/pay', '{\"order_no\":\"ORD20260301001\"}', '{\"code\":200,\"msg\":\"success\"}', NULL, 1230, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '支付订单');
INSERT INTO `sys_operation_log` VALUES (4, 1, '分诊管理', 'AI分诊', 'system', NULL, '127.0.0.1', '系统', 'POST', '/api/triage/ai-analyze', '{\"triage_id\":1}', '{\"code\":200,\"msg\":\"success\"}', NULL, 3500, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', 'AI自动分诊');
INSERT INTO `sys_operation_log` VALUES (5, 1, '陪诊师管理', '审核通过', 'admin', 14, '192.168.1.100', '公司内网', 'POST', '/api/accompanist/audit', '{\"accompanist_id\":7,\"status\":2}', '{\"code\":200,\"msg\":\"success\"}', NULL, 256, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师审核');
INSERT INTO `sys_operation_log` VALUES (6, 1, '用户管理', '登录', 'zhangsan', 1, '223.104.3.25', '北京市', 'POST', '/api/user/login', '{\"username\":\"zhangsan\"}', '{\"code\":200,\"msg\":\"success\"}', NULL, 125, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '用户登录');
INSERT INTO `sys_operation_log` VALUES (7, 1, '订单管理', '创建订单', 'zhangsan', 1, '223.104.3.25', '北京市', 'POST', '/api/order/create', '{\"order_type\":1}', '{\"code\":200,\"msg\":\"success\",\"data\":{\"order_no\":\"ORD20260301001\"}}', NULL, 856, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '创建订单');
INSERT INTO `sys_operation_log` VALUES (8, 1, '支付管理', '支付', 'zhangsan', 1, '223.104.3.25', '北京市', 'POST', '/api/payment/pay', '{\"order_no\":\"ORD20260301001\"}', '{\"code\":200,\"msg\":\"success\"}', NULL, 1230, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '支付订单');
INSERT INTO `sys_operation_log` VALUES (9, 1, '分诊管理', 'AI分诊', 'system', NULL, '127.0.0.1', '系统', 'POST', '/api/triage/ai-analyze', '{\"triage_id\":1}', '{\"code\":200,\"msg\":\"success\"}', NULL, 3500, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', 'AI自动分诊');
INSERT INTO `sys_operation_log` VALUES (10, 1, '陪诊师管理', '审核通过', 'admin', 14, '192.168.1.100', '公司内网', 'POST', '/api/accompanist/audit', '{\"accompanist_id\":7,\"status\":2}', '{\"code\":200,\"msg\":\"success\"}', NULL, 256, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '陪诊师审核');

-- ----------------------------
-- Table structure for user_accompanist
-- ----------------------------
DROP TABLE IF EXISTS `user_accompanist`;
CREATE TABLE `user_accompanist`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `user_id` bigint NOT NULL COMMENT '用户ID，关联user_user表',
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名（冗余user_user.real_name）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号（冗余user_user.phone）',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别（1：男；2：女；3：未知）',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `professional_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职称（如主治医师、护士）',
  `medical_background` tinyint NULL DEFAULT NULL COMMENT '医学背景（1：有医学背景；2：无医学背景）',
  `certificate_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '证书编号',
  `certificate_photo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '证书照片URL',
  `criminal_record_cert` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '无犯罪记录证明URL',
  `business_license` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '营业执照URL',
  `language_ability` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '语言能力（如英语、日语）',
  `specialties` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '专业擅长（如骨科、脑科、妇产科）',
  `service_count` int NULL DEFAULT 0 COMMENT '已服务次数',
  `rating_score` decimal(3, 2) NULL DEFAULT 0.00 COMMENT '评分（如4.85）',
  `on_time_rate` decimal(5, 2) NULL DEFAULT 100.00 COMMENT '准时率（如98.50）',
  `complaint_count` int NULL DEFAULT 0 COMMENT '投诉次数',
  `accompany_status` tinyint NULL DEFAULT 1 COMMENT '陪诊状态（1：空闲；2：服务中；3：休假；4：停用）',
  `audit_status` tinyint NULL DEFAULT 1 COMMENT '审核状态（1：待审核；2：已通过；3：已拒绝）',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核通过时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_accompany_status`(`accompany_status` ASC) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_specialties`(`specialties` ASC) USING BTREE,
  INDEX `idx_rating_score`(`rating_score` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '陪诊师资质表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_accompanist
-- ----------------------------
INSERT INTO `user_accompanist` VALUES (7, 7, '赵敏', '+86 13900139001', 1, 20, '小赵', 1, 'HEA2020001', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', NULL, '中文,英文,日语', '儿科', 150, 4.99, 99.00, 0, 1, 2, '2026-01-14 16:00:00', 'system', '2026-01-14 16:00:00', NULL, '2026-01-14 16:00:00', '汉语陪诊师');
INSERT INTO `user_accompanist` VALUES (8, 8, '钱多多', '+86 13900139002', 2, 28, '健康管理师', 2, 'HEA2020002', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', NULL, '中文,沪语', '儿科,内科,体检', 203, 4.85, 99.20, 1, 1, 2, '2026-01-11 11:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '金牌陪诊师');
INSERT INTO `user_accompanist` VALUES (9, 9, '孙丽', '+86 13900139003', 2, 32, '医学翻译', 1, 'TRA2020003', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', NULL, '中文,英文,日语', '肿瘤科,心内科,眼科', 89, 4.50, 97.80, 0, 1, 2, '2026-01-12 14:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:06:05', '英文陪诊师');
INSERT INTO `user_accompanist` VALUES (10, 10, '李强', '+86 13900139004', 1, 40, '主治医师', 1, 'DOC2020004', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', NULL, '中文,日语,韩语', '消化内科,呼吸内科,老年科', 245, 4.95, 99.50, 0, 1, 2, '2026-01-13 09:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '日语陪诊师');
INSERT INTO `user_accompanist` VALUES (11, 11, 'Sarah Chen', '+1 5559876543', 2, 30, 'Medical Escort Specialist', 1, 'MES2020005', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', NULL, '英文,中文', 'All Departments', 67, 4.88, 98.00, 0, 1, 2, '2026-01-14 16:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '北美陪诊师');

-- ----------------------------
-- Table structure for user_demand
-- ----------------------------
DROP TABLE IF EXISTS `user_demand`;
CREATE TABLE `user_demand`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '发布用户ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '需求标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '需求描述',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '需求类型(accompany/nursing/other)',
  `hospital_id` bigint NULL DEFAULT NULL,
  `hospital_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department_id` bigint NULL DEFAULT NULL,
  `department_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `patient_id` bigint NULL DEFAULT NULL,
  `start_date` date NULL DEFAULT NULL,
  `end_date` date NULL DEFAULT NULL,
  `budget` decimal(10, 2) NULL DEFAULT NULL COMMENT '预算金额',
  `contact_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1待接单 2已接单 3已完成 4已取消)',
  `accept_accompanist_id` bigint NULL DEFAULT NULL,
  `accept_time` datetime NULL DEFAULT NULL,
  `cancel_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `refresh_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_city`(`city` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '需求发布表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_demand
-- ----------------------------
INSERT INTO `user_demand` VALUES (1, 1, '需要全程陪诊服务', '本人需要到协和医院做全面检查，需要专业陪诊人员全程陪同', 'accompany', 1, '北京协和医院', 1, '内科', 1, '2026-04-01', '2026-04-01', 500.00, '张三', '13800138001', '要求有医疗背景，会说英语', '北京', 1, NULL, NULL, NULL, NULL, '2026-03-20 09:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (2, 1, '接送父母就医', '需要接送父母从家到医院，并陪同就诊', 'accompany', 2, '北京大学第一医院', 2, '心内科', 2, '2026-04-05', '2026-04-05', 300.00, '张三', '13800138001', '需要有耐心，会照顾老人', '北京', 2, 7, '2026-03-21 10:00:00', NULL, NULL, '2026-03-21 08:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (3, 2, '术后护理需求', '手术后需要专业护理人员在家提供护理服务', 'nursing', NULL, NULL, NULL, NULL, 3, '2026-04-10', '2026-04-20', 2000.00, '李四', '13800138003', '需要有护理资质证书', '上海', 1, NULL, NULL, NULL, NULL, '2026-03-22 10:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (4, 3, '代办挂号取药', '需要代办挂号并取药送到指定地址', 'other', 4, '中山大学附属第一医院', NULL, NULL, 4, '2026-04-03', '2026-04-03', 200.00, '王五', '13800138004', '需要熟悉医院环境', '广州', 1, NULL, NULL, NULL, NULL, '2026-03-23 11:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (5, 4, 'Medical escort needed', '需要英语流利的陪诊人员', 'accompany', 1, '北京协和医院', 3, '骨科', 6, '2026-04-08', '2026-04-08', 800.00, 'John', '+15551234567', 'Must speak English fluently', '北京', 1, NULL, NULL, NULL, NULL, '2026-03-23 12:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (6, 1, '神经科复诊陪同', '定期复诊需要陪同，携带历次病历资料', 'accompany', 1, '北京协和医院', 4, '神经科', 1, '2026-05-01', '2026-05-01', 400.00, '张三', '13800138001', '需熟悉复诊流程', '北京', 3, 7, '2026-03-10 09:00:00', NULL, NULL, '2026-03-05 09:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (7, 2, '老人体检陪同', '需陪同老人做年度体检', 'accompany', 3, '上海交通大学医学院', 5, '体检科', 9, '2026-04-15', '2026-04-15', 350.00, '李四', '13800138003', '有体检陪诊经验优先', '上海', 4, NULL, NULL, NULL, NULL, '2026-03-24 08:00:00', NULL, NULL);
INSERT INTO `user_demand` VALUES (8, 6, '慢性病复查', '每月定期复查，需固定陪诊师', 'accompany', 6, '中国医科大学附属医院', 1, '内分泌科', 10, '2026-04-20', '2026-04-20', 300.00, '赵六', '13700137001', '有慢性病陪诊经验', '沈阳', 1, NULL, NULL, NULL, NULL, '2026-03-24 09:00:00', NULL, NULL);

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收藏类型(companion/package/hospital/expert)',
  `target_id` bigint NOT NULL COMMENT '收藏目标ID',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_target`(`user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite` VALUES (1, 1, 'hospital', 1, '2026-01-10 10:00:00');
INSERT INTO `user_favorite` VALUES (2, 1, 'hospital', 2, '2026-01-11 10:00:00');
INSERT INTO `user_favorite` VALUES (3, 1, 'expert', 1, '2026-01-12 10:00:00');
INSERT INTO `user_favorite` VALUES (4, 1, 'companion', 7, '2026-01-13 10:00:00');
INSERT INTO `user_favorite` VALUES (5, 1, 'package', 1, '2026-01-14 10:00:00');
INSERT INTO `user_favorite` VALUES (6, 2, 'hospital', 3, '2026-01-15 10:00:00');
INSERT INTO `user_favorite` VALUES (7, 2, 'expert', 2, '2026-01-16 10:00:00');
INSERT INTO `user_favorite` VALUES (8, 2, 'companion', 8, '2026-01-17 10:00:00');
INSERT INTO `user_favorite` VALUES (9, 3, 'hospital', 4, '2026-01-18 10:00:00');
INSERT INTO `user_favorite` VALUES (10, 3, 'package', 2, '2026-01-19 10:00:00');
INSERT INTO `user_favorite` VALUES (11, 4, 'hospital', 1, '2026-01-20 10:00:00');
INSERT INTO `user_favorite` VALUES (12, 5, 'expert', 3, '2026-01-21 10:00:00');
INSERT INTO `user_favorite` VALUES (13, 6, 'companion', 7, '2026-01-22 10:00:00');

-- ----------------------------
-- Table structure for user_identity_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_identity_auth`;
CREATE TABLE `user_identity_auth`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `user_id` bigint NOT NULL COMMENT '用户ID，关联user_user表',
  `auth_type` tinyint NULL DEFAULT NULL COMMENT '认证类型（1：护照；2：身份证；3：其他）',
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '实名认证的真实姓名',
  `id_card_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号（加密存储）',
  `passport_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '护照号',
  `passport_photo_front` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '护照照片正面URL',
  `passport_photo_back` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '护照照片背面URL',
  `id_card_photo_front` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证照片正面URL',
  `id_card_photo_back` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证照片背面URL',
  `auth_status` tinyint NULL DEFAULT 1 COMMENT '认证状态（1：待审核；2：已通过；3：已拒绝）',
  `auth_fail_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '认证拒绝原因',
  `auth_time` datetime NULL DEFAULT NULL COMMENT '认证通过时间',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_auth_status`(`auth_status` ASC) USING BTREE,
  INDEX `idx_auth_type`(`auth_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '实名认证表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_identity_auth
-- ----------------------------
INSERT INTO `user_identity_auth` VALUES (1, 1, 2, '张三', '110101199001011234', NULL, NULL, NULL, 'https://example.com/idcard/zhangsan_front.jpg', 'https://example.com/idcard/zhangsan_back.jpg', 2, NULL, '2026-01-15 10:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '身份证认证通过');
INSERT INTO `user_identity_auth` VALUES (2, 2, 2, '李四', '310101199002021234', NULL, NULL, NULL, 'https://example.com/idcard/lisi_front.jpg', 'https://example.com/idcard/lisi_back.jpg', 2, NULL, '2026-01-16 11:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '身份证认证通过');
INSERT INTO `user_identity_auth` VALUES (3, 3, 2, '王五', '440101199003031234', NULL, NULL, NULL, 'https://example.com/idcard/wangwu_front.jpg', 'https://example.com/idcard/wangwu_back.jpg', 2, NULL, '2026-01-17 14:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '身份证认证通过');
INSERT INTO `user_identity_auth` VALUES (4, 4, 1, 'John Doe', NULL, 'E12345678', 'https://example.com/passport/john_front.jpg', 'https://example.com/passport/john_back.jpg', NULL, NULL, 2, NULL, '2026-01-18 09:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '护照认证通过');
INSERT INTO `user_identity_auth` VALUES (5, 5, 1, '田中花子', NULL, 'RE1234567', 'https://example.com/passport/hanako_front.jpg', 'https://example.com/passport/hanako_back.jpg', NULL, NULL, 2, NULL, '2026-01-19 15:00:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '护照认证通过');
INSERT INTO `user_identity_auth` VALUES (6, 6, 1, '김민지', NULL, 'M1234567', 'https://example.com/passport/minji_front.jpg', 'https://example.com/passport/minji_back.jpg', NULL, NULL, 2, NULL, '2026-01-20 10:30:00', 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '护照认证通过');

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名（冗余）',
  `login_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录IP',
  `login_device` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录设备',
  `login_browser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '浏览器',
  `login_os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作系统',
  `login_location` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录地点',
  `login_status` tinyint NULL DEFAULT 1 COMMENT '登录状态（1：成功；2：失败）',
  `fail_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '失败原因',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_login_time`(`login_time` ASC) USING BTREE,
  INDEX `idx_login_ip`(`login_ip` ASC) USING BTREE,
  INDEX `idx_user_time_status`(`user_id` ASC, `login_time` ASC, `login_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '登录日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_login_log
-- ----------------------------
INSERT INTO `user_login_log` VALUES (1, 1, 'zhangsan', '2026-03-15 08:30:00', '223.104.3.25', 'iPhone 14 Pro', 'Safari', 'iOS 16.0', '北京市', 1, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者登录');
INSERT INTO `user_login_log` VALUES (2, 1, 'zhangsan', '2026-03-16 09:15:00', '223.104.3.25', 'iPhone 14 Pro', 'Safari', 'iOS 16.0', '北京市', 1, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者登录');
INSERT INTO `user_login_log` VALUES (3, 2, 'lisi', '2026-03-15 14:20:00', '114.114.114.114', 'MacBook Pro', 'Chrome', 'macOS 13.0', '上海市', 1, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者登录');
INSERT INTO `user_login_log` VALUES (4, 7, 'accompanist001', '2026-03-17 07:50:00', '123.125.115.110', 'HUAWEI Mate 60', 'Chrome', 'Android 14', '北京市', 1, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师登录');
INSERT INTO `user_login_log` VALUES (5, 8, 'accompanist002', '2026-03-17 08:10:00', '218.75.123.45', 'Xiaomi 14', 'Chrome', 'Android 14', '杭州市', 1, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师登录');
INSERT INTO `user_login_log` VALUES (6, 14, 'admin', '2026-03-17 09:00:00', '192.168.1.100', 'Windows 11 PC', 'Chrome', 'Windows 11', '公司内网', 1, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员登录');
INSERT INTO `user_login_log` VALUES (7, 1, 'zhangsan', '2026-03-15 08:30:00', '223.104.3.25', 'iPhone 14 Pro', 'Safari', 'iOS 16.0', '北京市', 1, NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '患者登录');
INSERT INTO `user_login_log` VALUES (8, 1, 'zhangsan', '2026-03-16 09:15:00', '223.104.3.25', 'iPhone 14 Pro', 'Safari', 'iOS 16.0', '北京市', 1, NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '患者登录');
INSERT INTO `user_login_log` VALUES (9, 2, 'lisi', '2026-03-15 14:20:00', '114.114.114.114', 'MacBook Pro', 'Chrome', 'macOS 13.0', '上海市', 1, NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '患者登录');
INSERT INTO `user_login_log` VALUES (10, 7, 'accompanist001', '2026-03-17 07:50:00', '123.125.115.110', 'HUAWEI Mate 60', 'Chrome', 'Android 14', '北京市', 1, NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '陪诊师登录');
INSERT INTO `user_login_log` VALUES (11, 8, 'accompanist002', '2026-03-17 08:10:00', '218.75.123.45', 'Xiaomi 14', 'Chrome', 'Android 14', '杭州市', 1, NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '陪诊师登录');
INSERT INTO `user_login_log` VALUES (12, 14, 'admin', '2026-03-17 09:00:00', '192.168.1.100', 'Windows 11 PC', 'Chrome', 'Windows 11', '公司内网', 1, NULL, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员登录');

-- ----------------------------
-- Table structure for user_medical_card
-- ----------------------------
DROP TABLE IF EXISTS `user_medical_card`;
CREATE TABLE `user_medical_card`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `patient_id` bigint NOT NULL COMMENT '就诊人ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `hospital_id` bigint NULL DEFAULT NULL COMMENT '医院ID',
  `hospital_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '医院名称',
  `card_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '就诊卡号',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1正常 2停用)',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_patient_id`(`patient_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '就诊卡信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_medical_card
-- ----------------------------
INSERT INTO `user_medical_card` VALUES (1, 1, 1, 1, '北京协和医院', 'PUMCH20260001', 1, '2026-01-10 09:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (2, 1, 1, 2, '北京大学第一医院', 'PKUFH20260001', 1, '2026-01-10 09:20:00', NULL);
INSERT INTO `user_medical_card` VALUES (3, 2, 1, 1, '北京协和医院', 'PUMCH20260002', 1, '2026-01-11 10:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (4, 3, 2, 3, '上海交通大学医学院', 'SJTU20260001', 1, '2026-01-12 11:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (5, 4, 3, 4, '中山大学附属第一医院', 'ZSYY20260001', 1, '2026-01-13 12:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (6, 5, 3, 4, '中山大学附属第一医院', 'ZSYY20260002', 1, '2026-01-14 13:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (7, 6, 4, 5, 'Peking Union Medical', 'INTL20260001', 1, '2026-01-15 08:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (8, 9, 2, 3, '上海交通大学医学院', 'SJTU20260002', 1, '2026-02-05 11:10:00', NULL);
INSERT INTO `user_medical_card` VALUES (9, 10, 6, 6, '中国医科大学附属医院', 'CMU20260001', 1, '2026-02-10 14:10:00', NULL);

-- ----------------------------
-- Table structure for user_patient
-- ----------------------------
DROP TABLE IF EXISTS `user_patient`;
CREATE TABLE `user_patient`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '所属用户ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `gender` tinyint NULL DEFAULT 0 COMMENT '性别(0未知 1男 2女)',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `relation` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '与账户人关系',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址',
  `is_default` tinyint NULL DEFAULT 2 COMMENT '是否默认(1是 2否)',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1正常 2停用)',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '就诊人信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_patient
-- ----------------------------
INSERT INTO `user_patient` VALUES (1, 1, '张三', '13800138001', '110101199001011234', 1, '1990-01-01', '本人', '北京市朝阳区建国路88号', 1, 1, 'system', '2026-01-10 09:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (2, 1, '张母', '13800138002', '110101196501015678', 2, '1965-01-01', '父母', '北京市朝阳区建国路88号', 2, 1, 'system', '2026-01-11 10:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (3, 2, '李四', '13800138003', '310101199203034321', 1, '1992-03-03', '本人', '上海市浦东新区陆家嘴金融路1号', 1, 1, 'system', '2026-01-12 11:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (4, 3, '王五', '13800138004', '440101199505058765', 2, '1995-05-05', '本人', '广州市天河区体育西路100号', 1, 1, 'system', '2026-01-13 12:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (5, 3, '王父', '13800138005', '440101196808082345', 1, '1968-08-08', '父母', '广州市天河区体育西路100号', 2, 1, 'system', '2026-01-14 13:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (6, 4, 'John Doe', '+15551234567', 'P12345678', 1, '1985-06-15', '本人', '123 Main St, New York, NY', 1, 1, 'system', '2026-01-15 08:00:00', NULL, NULL, '外籍患者');
INSERT INTO `user_patient` VALUES (7, 5, '田中花子', '+819012345678', 'JPN98765432', 2, '1998-07-22', '本人', '东京都新宿区1-2-3', 1, 1, 'system', '2026-01-16 09:30:00', NULL, NULL, '日籍患者');
INSERT INTO `user_patient` VALUES (8, 1, '张小明', '13900139001', '110101201501011111', 1, '2015-01-01', '子女', '北京市朝阳区建国路88号', 2, 1, 'system', '2026-02-01 10:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (9, 2, '李母', '13900139002', '310101197002024444', 1, '1970-02-02', '父母', '上海市浦东新区陆家嘴金融路1号', 2, 1, 'system', '2026-02-05 11:00:00', NULL, '2026-03-25 08:46:21', NULL);
INSERT INTO `user_patient` VALUES (10, 6, '赵六', '13700137001', '210101199808083333', 1, '1998-08-08', '本人', '沈阳市和平区中华路50号', 1, 1, 'system', '2026-02-10 14:00:00', NULL, NULL, NULL);
INSERT INTO `user_patient` VALUES (11, 1, '张小红', '13800138011', '110101200001011234', 2, '2000-01-01', '子女', '北京市朝阳区建国路88号', 1, 1, 'system', '2026-03-01 10:00:00', NULL, NULL, '张三女儿');
INSERT INTO `user_patient` VALUES (12, 2, '李父', '13800138012', '310101196001015678', 1, '1960-01-01', '父母', '上海市浦东新区陆家嘴金融路1号', 1, 1, 'system', '2026-03-02 10:00:00', NULL, NULL, '李四父亲');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色编码，唯一索引',
  `role_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色描述',
  `role_sort` int NULL DEFAULT 0 COMMENT '角色排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1：正常；2：停用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_code`(`role_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, '患者', 'patient', '患者角色', 1, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '患者端用户');
INSERT INTO `user_role` VALUES (2, '陪诊师', 'accompanist', '陪诊师角色', 2, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '陪诊师端用户');
INSERT INTO `user_role` VALUES (3, '客服', 'customer_service', '客服角色', 3, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '客服人员');
INSERT INTO `user_role` VALUES (4, '医学顾问', 'medical_advisor', '医学顾问角色', 4, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '医学顾问团队');
INSERT INTO `user_role` VALUES (5, '管理员', 'admin', '管理员角色', 5, 1, 'admin', '2026-03-18 11:59:29', NULL, '2026-03-18 11:59:29', '系统管理员');

-- ----------------------------
-- Table structure for user_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `user_role_permission`;
CREATE TABLE `user_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_role_id_permission_id`(`role_id` ASC, `permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role_permission
-- ----------------------------
INSERT INTO `user_role_permission` VALUES (1, 1, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (2, 1, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (3, 1, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (4, 1, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (5, 1, 5, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (6, 1, 6, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (7, 1, 7, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (8, 1, 8, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (9, 1, 9, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (10, 1, 10, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (11, 1, 11, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (12, 1, 12, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (13, 1, 13, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (17, 1, 50, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (18, 1, 51, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (19, 1, 52, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (20, 1, 53, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (21, 1, 54, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (22, 1, 55, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (23, 1, 56, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (24, 1, 57, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (25, 1, 58, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (26, 1, 59, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (27, 1, 60, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (28, 1, 61, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (29, 1, 62, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (30, 1, 63, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');
INSERT INTO `user_role_permission` VALUES (31, 1, 64, 'system', '2026-03-18 12:06:05', NULL, '2026-03-18 12:06:05', '管理员拥有所有权限');

-- ----------------------------
-- Table structure for user_user
-- ----------------------------
DROP TABLE IF EXISTS `user_user`;
CREATE TABLE `user_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名，唯一索引',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密存储）',
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱地址，唯一索引',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别（1：男；2：女；3：未知）',
  `age` int NULL DEFAULT NULL COMMENT '年龄',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号，唯一索引',
  `country_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '国家代码（如+86、+1）',
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `user_type` tinyint NULL DEFAULT NULL COMMENT '用户类型（1：患者；2：陪诊师；3：客服；4：管理员）',
  `status` tinyint NULL DEFAULT 1 COMMENT '账号状态（1：活跃；2：停用；3：禁用）',
  `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'zh-CN' COMMENT '语言设置（如zh-CN、en-US、ja-JP）',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_user_type`(`user_type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_user
-- ----------------------------
INSERT INTO `user_user` VALUES (1, 'zhangsan', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '张三', '小张', 'zhangsan@example.com', 1, 20, '+86 13800138001', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 1, 1, 'zh-CN', '2026-03-26 20:03:34', NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-26 20:03:34', '测试患者1');
INSERT INTO `user_user` VALUES (2, 'lisi', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '李四', 'Lily', 'lisi@example.com', 2, 25, '+86 13800138002', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 1, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '测试患者2');
INSERT INTO `user_user` VALUES (3, 'wangwu', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '王五', 'WangWu', 'wangwu@example.com', 3, 30, '+86 13800138003', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 1, 1, 'en-US', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '测试患者3');
INSERT INTO `user_user` VALUES (4, 'john_doe', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', 'John Doe', 'John', 'john.doe@example.com', 1, 20, '+1 5551234567', '+1', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 1, 1, 'en-US', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '国际患者1');
INSERT INTO `user_user` VALUES (5, 'tanaka_hanako', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '田中花子', 'Hanako', 'hanako.tanaka@example.com', 2, 25, '+81 90-1234-5678', '+81', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 1, 1, 'ja-JP', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '日本患者1');
INSERT INTO `user_user` VALUES (6, 'kim_minji', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '김민지', 'Minji', 'minji.kim@example.com', 2, 25, '+82 10-1234-5678', '+82', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 1, 1, 'ko-KR', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '韩国患者1');
INSERT INTO `user_user` VALUES (7, 'peizhen', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '赵敏', '小赵医生', 'zhaomin@example.com', 1, 20, '+86 13900139001', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 2, 1, 'zh-CN', '2026-03-26 19:58:47', NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-26 19:58:47', '资深陪诊师');
INSERT INTO `user_user` VALUES (8, 'accompanist002', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '钱多多', '钱姐', 'qianduoduo@example.com', 2, 25, '+86 13900139002', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 2, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '金牌陪诊师');
INSERT INTO `user_user` VALUES (9, 'accompanist003', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '孙丽', 'Sunny', 'sunli@example.com', 2, 30, '+86 13900139003', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 2, 1, 'en-US', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-24 12:55:05', '英文陪诊师');
INSERT INTO `user_user` VALUES (10, 'accompanist004', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '李强', '强子', 'liqiang@example.com', 2, 30, '+86 13900139004', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 2, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '日语陪诊师');
INSERT INTO `user_user` VALUES (11, 'accompanist005', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', 'Sarah Chen', 'Sarah', 'sarah.chen@example.com', 2, 25, '+1 5559876543', '+1', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 2, 1, 'en-US', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '北美陪诊师');
INSERT INTO `user_user` VALUES (12, 'service001', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '周客服', '小周', 'zhou@example.com', 2, 25, '+86 13700137001', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 3, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '中文客服');
INSERT INTO `user_user` VALUES (13, 'service002', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', 'Emily Wang', 'Emily', 'emily.wang@example.com', 2, 25, '+1 5551112222', '+1', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 3, 1, 'en-US', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '英文客服');
INSERT INTO `user_user` VALUES (14, 'admin', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '系统管理员', 'Admin', 'admin@example.com', 2, 25, '+86 13600136001', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 4, 1, 'zh-CN', '2026-03-26 20:51:36', NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-26 20:51:35', 'Alanine');
INSERT INTO `user_user` VALUES (15, 'expert001', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '张文宏', '张文宏', 'zhangwh@example.com', 2, 25, '+86 13500135001', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-26 21:07:29', '感染科专家');
INSERT INTO `user_user` VALUES (16, 'expert002', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '李兰娟', '李兰娟', 'lilanl@example.com', 2, 25, '+86 13500135002', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '肿瘤科专家');
INSERT INTO `user_user` VALUES (17, 'expert003', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '王磊', '王磊', 'wangl2@example.com', 2, 25, '+86 13500135003', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '骨科专家');
INSERT INTO `user_user` VALUES (18, 'expert004', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '陈静', '陈静', 'chenj2@example.com', 2, 25, '+86 13500135004', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '神经科专家');
INSERT INTO `user_user` VALUES (19, 'expert005', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', 'Michael Chen', 'Michael Chen', 'michael.chen@example.com', 2, 25, '+1 5551230001', '+1', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'en-US', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '心内科专家');
INSERT INTO `user_user` VALUES (20, 'expert006', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', 'Sarah Yamamoto', 'Sarah Yamamoto', 'sarah.y@example.com', 2, 25, '+81 90-1234-9999', '+81', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'ja-JP', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '眼科专家');
INSERT INTO `user_user` VALUES (21, 'expert007', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '赵伟', '赵伟', 'zhaow2@example.com', 2, 25, '+86 13500135007', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-18 12:01:09', NULL, '2026-03-19 16:59:41', '消化内科专家');
INSERT INTO `user_user` VALUES (22, 'expert008', '$2a$10$E3tbc5fkZzK2HWinhhK3recQNZ3yS50TE/U8avkJT3oD1tlISqrne', '刘芳', '刘芳', 'liufang@example.com', 2, 25, '13500135008', '+86', 'cdbc2d1f-0797-4664-9aa2-37f6e1555e63.jpg', 5, 1, 'zh-CN', NULL, NULL, 'system', '2026-03-19 16:53:48', NULL, '2026-03-19 16:53:48', '妇产科专家');
INSERT INTO `user_user` VALUES (23, 'asdfg', '$2a$10$J7TfaDBkR19T19iasVDTX.FtXnRm/Ijc0Law7lgh2go7vzFjYwMDq', NULL, 'asdfg', NULL, NULL, NULL, NULL, NULL, NULL, 1, 1, 'zh-CN', NULL, NULL, 'asdfg', '2026-03-25 19:34:02', NULL, '2026-03-25 19:34:02', NULL);

-- ----------------------------
-- Table structure for user_user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_user_role`;
CREATE TABLE `user_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID，自增',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id_role_id`(`user_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_user_role
-- ----------------------------
INSERT INTO `user_user_role` VALUES (1, 1, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者角色');
INSERT INTO `user_user_role` VALUES (2, 2, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者角色');
INSERT INTO `user_user_role` VALUES (3, 3, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者角色');
INSERT INTO `user_user_role` VALUES (4, 4, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者角色');
INSERT INTO `user_user_role` VALUES (5, 5, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者角色');
INSERT INTO `user_user_role` VALUES (6, 6, 4, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '患者角色');
INSERT INTO `user_user_role` VALUES (7, 7, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师角色');
INSERT INTO `user_user_role` VALUES (8, 8, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师角色');
INSERT INTO `user_user_role` VALUES (9, 9, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师角色');
INSERT INTO `user_user_role` VALUES (10, 10, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师角色');
INSERT INTO `user_user_role` VALUES (11, 11, 3, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '陪诊师角色');
INSERT INTO `user_user_role` VALUES (12, 12, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '客服角色');
INSERT INTO `user_user_role` VALUES (13, 13, 2, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '客服角色');
INSERT INTO `user_user_role` VALUES (14, 14, 1, 'system', '2026-03-18 12:01:09', NULL, '2026-03-18 12:01:09', '管理员角色');

-- ----------------------------
-- View structure for v_accompanist_performance
-- ----------------------------
DROP VIEW IF EXISTS `v_accompanist_performance`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_accompanist_performance` AS select `ua`.`id` AS `id`,`ua`.`real_name` AS `real_name`,`ua`.`phone` AS `phone`,`ua`.`service_count` AS `service_count`,`ua`.`rating_score` AS `rating_score`,`ua`.`on_time_rate` AS `on_time_rate`,`ua`.`complaint_count` AS `complaint_count`,count(distinct `o`.`id`) AS `recent_orders`,avg(`oe`.`rating_score`) AS `recent_avg_rating` from ((`user_accompanist` `ua` left join `order_order` `o` on(((`ua`.`id` = `o`.`accompanist_id`) and (`o`.`create_time` >= (now() - interval 30 day))))) left join `order_evaluation` `oe` on((`o`.`id` = `oe`.`order_id`))) group by `ua`.`id`;

-- ----------------------------
-- View structure for v_hospital_popularity
-- ----------------------------
DROP VIEW IF EXISTS `v_hospital_popularity`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_hospital_popularity` AS select `mh`.`id` AS `id`,`mh`.`hospital_name` AS `hospital_name`,`mh`.`city` AS `city`,count(distinct `ma`.`id`) AS `appointment_count`,count(distinct `o`.`id`) AS `order_count`,avg(`mh`.`rating_score`) AS `avg_rating`,`mh`.`comment_count` AS `comment_count` from ((`medical_hospital` `mh` left join `medical_appointment` `ma` on((`mh`.`id` = `ma`.`hospital_id`))) left join `order_order` `o` on((`mh`.`hospital_code` = `o`.`appointment_hospital`))) group by `mh`.`id`;

-- ----------------------------
-- View structure for v_order_statistics
-- ----------------------------
DROP VIEW IF EXISTS `v_order_statistics`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_order_statistics` AS select cast(`o`.`create_time` as date) AS `order_date`,count(0) AS `total_orders`,sum(`o`.`total_amount`) AS `total_amount`,count((case when (`o`.`order_status` = 5) then 1 end)) AS `completed_orders`,count((case when (`o`.`order_status` = 6) then 1 end)) AS `cancelled_orders`,avg(`oe`.`rating_score`) AS `avg_rating` from (`order_order` `o` left join `order_evaluation` `oe` on((`o`.`id` = `oe`.`order_id`))) group by cast(`o`.`create_time` as date);

-- ----------------------------
-- View structure for v_user_activity
-- ----------------------------
DROP VIEW IF EXISTS `v_user_activity`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `v_user_activity` AS select `uu`.`id` AS `id`,`uu`.`username` AS `username`,`uu`.`user_type` AS `user_type`,count(distinct `ull`.`id`) AS `login_count`,max(`ull`.`login_time`) AS `last_login_time`,count(distinct `o`.`id`) AS `order_count`,sum(`o`.`total_amount`) AS `total_spent` from ((`user_user` `uu` left join `user_login_log` `ull` on(((`uu`.`id` = `ull`.`user_id`) and (`ull`.`login_time` >= (now() - interval 30 day))))) left join `order_order` `o` on((`uu`.`id` = `o`.`user_id`))) group by `uu`.`id`;

-- ----------------------------
-- Procedure structure for sp_cancel_timeout_orders
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_cancel_timeout_orders`;
delimiter ;;
CREATE PROCEDURE `sp_cancel_timeout_orders`()
BEGIN
    DECLARE timeout_minutes INT DEFAULT 30;
    
    UPDATE order_order 
    SET order_status = 6, 
        cancel_time = NOW(),
        cancel_reason = '订单超时自动取消',
        update_time = NOW()
    WHERE order_status = 1 
    AND TIMESTAMPDIFF(MINUTE, create_time, NOW()) > timeout_minutes;
    
    SELECT ROW_COUNT() as cancelled_count;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_generate_order_report
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_generate_order_report`;
delimiter ;;
CREATE PROCEDURE `sp_generate_order_report`(IN p_start_date DATE, IN p_end_date DATE)
BEGIN
    SELECT 
        DATE(o.create_time) as order_date,
        COUNT(*) as total_orders,
        SUM(o.total_amount) as total_amount,
        COUNT(CASE WHEN o.order_status = 5 THEN 1 END) as completed_orders,
        COUNT(CASE WHEN o.order_status = 6 THEN 1 END) as cancelled_orders,
        COUNT(CASE WHEN o.order_status = 1 THEN 1 END) as pending_orders,
        AVG(oe.rating_score) as avg_rating
    FROM order_order o
    LEFT JOIN order_evaluation oe ON o.id = oe.order_id
    WHERE DATE(o.create_time) BETWEEN p_start_date AND p_end_date
    GROUP BY DATE(o.create_time)
    ORDER BY order_date DESC;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for sp_update_accompanist_rating
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_update_accompanist_rating`;
delimiter ;;
CREATE PROCEDURE `sp_update_accompanist_rating`(IN p_accompanist_id BIGINT)
BEGIN
    DECLARE v_avg_rating DECIMAL(3,2);
    DECLARE v_on_time_rate DECIMAL(5,2);
    DECLARE v_total_orders INT;
    
    -- 计算平均评分
    SELECT AVG(rating_score) INTO v_avg_rating
    FROM order_evaluation
    WHERE accompanist_id = p_accompanist_id;
    
    -- 计算准时率
    SELECT COUNT(*) INTO v_total_orders
    FROM order_order
    WHERE accompanist_id = p_accompanist_id AND order_status = 5;
    
    -- 更新陪诊师信息
    UPDATE user_accompanist
    SET rating_score = COALESCE(v_avg_rating, 0),
        service_count = v_total_orders,
        update_time = NOW()
    WHERE id = p_accompanist_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table medical_triage_record
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_triage_generate_no`;
delimiter ;;
CREATE TRIGGER `tr_triage_generate_no` BEFORE INSERT ON `medical_triage_record` FOR EACH ROW BEGIN
    IF NEW.triage_no IS NULL OR NEW.triage_no = '' THEN
        SET NEW.triage_no = CONCAT('TRI', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(FLOOR(RAND()*1000000), 6, '0'));
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_evaluation
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_evaluation_update_rating`;
delimiter ;;
CREATE TRIGGER `tr_evaluation_update_rating` AFTER INSERT ON `order_evaluation` FOR EACH ROW BEGIN
    DECLARE v_avg_rating DECIMAL(3,2);
    
    SELECT AVG(rating_score) INTO v_avg_rating
    FROM order_evaluation
    WHERE accompanist_id = NEW.accompanist_id;
    
    UPDATE user_accompanist
    SET rating_score = COALESCE(v_avg_rating, 0),
        update_time = NOW()
    WHERE id = NEW.accompanist_id;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_order
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_order_generate_no`;
delimiter ;;
CREATE TRIGGER `tr_order_generate_no` BEFORE INSERT ON `order_order` FOR EACH ROW BEGIN
    IF NEW.order_no IS NULL OR NEW.order_no = '' THEN
        SET NEW.order_no = CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(FLOOR(RAND()*1000000), 6, '0'));
    END IF;
END
;;
delimiter ;

-- ----------------------------
-- Triggers structure for table order_payment
-- ----------------------------
DROP TRIGGER IF EXISTS `tr_payment_update_order`;
delimiter ;;
CREATE TRIGGER `tr_payment_update_order` AFTER UPDATE ON `order_payment` FOR EACH ROW BEGIN
    IF NEW.payment_status = 3 AND OLD.payment_status != 3 THEN
        UPDATE order_order 
        SET order_status = 2,
            actual_paid_amount = NEW.amount,
            update_time = NOW()
        WHERE id = NEW.order_id;
    END IF;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
