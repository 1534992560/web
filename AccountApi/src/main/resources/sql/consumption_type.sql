/*
 Navicat Premium Data Transfer

 Source Server         : database1
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : family

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 06/06/2025 04:37:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for consumption_type
-- ----------------------------
DROP TABLE IF EXISTS `consumption_type`;
CREATE TABLE `consumption_type`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `house_id` int NULL DEFAULT NULL,
  `is_custom` int NULL DEFAULT NULL,
  `is_delete` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consumption_type
-- ----------------------------
INSERT INTO `consumption_type` VALUES (1, '餐饮', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (2, '交通', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (3, '服饰', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (4, '购物', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (5, '服务', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (6, '教育', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (7, '娱乐', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (8, '运动', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (9, '生活缴费', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (10, '旅行', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (11, '宠物', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (12, '医疗', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (13, '保险', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (14, '公益', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (15, '红包', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (16, '住房', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (17, '美容', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (18, '长辈', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (19, '亲子', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (20, '其他', '2024-11-13 12:00:54', '2024-11-13 12:00:54', 0, 0, 0);
INSERT INTO `consumption_type` VALUES (21, 'ads', '2025-06-05 14:04:59', '2025-06-05 14:04:59', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (22, 'asdasdasdsa', '2025-06-05 15:36:45', '2025-06-05 15:36:50', 1, 1, 0);
INSERT INTO `consumption_type` VALUES (23, 'sadsadadsa', '2025-06-05 17:06:11', '2025-06-05 17:06:11', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (24, 'w1ww', '2025-06-05 17:08:04', '2025-06-05 17:08:04', 1, 1, 0);
INSERT INTO `consumption_type` VALUES (25, 'sadasd', '2025-06-05 17:44:41', '2025-06-05 17:44:41', 5, 1, 0);
INSERT INTO `consumption_type` VALUES (26, 'ssssss', '2025-06-05 17:48:28', '2025-06-06 03:59:01', 1, 1, 0);
INSERT INTO `consumption_type` VALUES (27, 'sadsadasdadasd', '2025-06-05 17:53:33', '2025-06-05 17:53:33', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (28, 'sssss', '2025-06-05 17:58:43', '2025-06-05 17:58:43', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (29, 'sdsadsa', '2025-06-05 18:09:02', '2025-06-05 18:09:02', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (30, 'sdsadsa', '2025-06-05 18:09:24', '2025-06-05 18:09:24', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (31, 'sdasd', '2025-06-06 00:32:44', '2025-06-06 00:32:44', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (32, 'saca', '2025-06-06 00:47:12', '2025-06-06 00:47:12', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (33, 'asdsadsad', '2025-06-06 01:19:14', '2025-06-06 01:19:39', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (34, 'sad', '2025-06-06 01:22:23', '2025-06-06 01:22:23', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (35, 'aaaa', '2025-06-06 01:47:36', '2025-06-06 01:47:45', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (36, 'sada', '2025-06-06 03:11:10', '2025-06-06 03:11:23', 1, 1, 1);
INSERT INTO `consumption_type` VALUES (37, 'ssssssss', '2025-06-06 04:02:04', '2025-06-06 04:02:04', 1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
