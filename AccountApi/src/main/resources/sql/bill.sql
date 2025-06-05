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

 Date: 06/06/2025 04:37:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `money` float(10, 2) NULL DEFAULT NULL,
  `consumption_id` int NOT NULL DEFAULT 0,
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `record_time` datetime NULL DEFAULT NULL,
  `user_id` int NOT NULL,
  `house_id` int NOT NULL,
  `bill_type` int NULL DEFAULT NULL COMMENT '账单类型：1.消费，2.收入',
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `is_delete` int NULL DEFAULT 0,
  `income_id` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_house_id`(`house_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of bill
-- ----------------------------
INSERT INTO `bill` VALUES (1, 600.00, 2, '111111111', '2025-05-15 12:06:05', 1, 1, 1, '2024-11-13 12:06:17', '2025-06-05 14:03:04', 0, 0);
INSERT INTO `bill` VALUES (2, 3000.00, 2, '22', '2025-05-21 00:00:00', 1, 1, 1, '2024-11-13 12:07:15', '2025-06-05 14:03:16', 0, 0);
INSERT INTO `bill` VALUES (4, 300.00, 2, '', '2024-11-13 12:09:56', 2, 1, 1, '2024-11-13 12:10:19', '2024-11-13 12:10:19', 0, 0);
INSERT INTO `bill` VALUES (5, 200.00, 4, '挺好', '2024-11-13 14:22:31', 2, 1, 1, '2024-11-13 14:22:51', '2024-11-13 14:22:51', 0, 0);
INSERT INTO `bill` VALUES (6, 10.00, 2, '上班', '2024-11-11 00:00:00', 2, 1, 1, '2024-11-13 14:23:11', '2024-11-13 14:23:11', 0, 0);
INSERT INTO `bill` VALUES (7, 150.00, 3, '购物', '2024-11-11 00:00:00', 2, 1, 1, '2024-11-13 14:23:28', '2024-11-13 14:23:28', 0, 0);
INSERT INTO `bill` VALUES (8, 500.00, 7, '游乐场', '2024-11-10 00:00:00', 2, 1, 1, '2024-11-13 14:24:38', '2024-11-13 14:24:38', 0, 0);
INSERT INTO `bill` VALUES (10, 100.00, 2, '123', '2025-06-03 00:00:00', 1, 1, 1, '2025-06-05 14:03:39', '2025-06-05 14:03:39', 0, 0);
INSERT INTO `bill` VALUES (11, 2100.00, 2, 'asd', '2025-06-05 15:35:27', 1, 1, 1, '2025-06-05 15:35:50', '2025-06-05 15:35:50', 0, 0);
INSERT INTO `bill` VALUES (12, 2100.00, 2, '147258', '2025-06-05 15:36:04', 1, 1, 1, '2025-06-05 15:36:10', '2025-06-05 15:36:14', 1, 0);
INSERT INTO `bill` VALUES (13, 1000.00, 1, '1', '2025-06-05 16:14:14', 1, 1, 1, '2025-06-05 16:14:28', '2025-06-05 16:14:40', 1, 0);
INSERT INTO `bill` VALUES (14, 100.00, 2, 'asd', '2025-06-04 00:00:00', 1, 1, 1, '2025-06-06 01:20:14', '2025-06-06 01:20:14', 0, 0);
INSERT INTO `bill` VALUES (18, 100.00, 7, '11111', '2025-06-13 00:00:00', 1, 1, 1, '2025-06-06 03:22:44', '2025-06-06 03:22:44', 0, 0);
INSERT INTO `bill` VALUES (19, 100.00, 0, '10000', '2025-06-12 00:00:00', 1, 1, 2, '2025-06-06 03:24:37', '2025-06-06 03:24:37', 0, 41);
INSERT INTO `bill` VALUES (20, 1000.00, 0, '123', '2025-06-06 03:29:18', 1, 1, 2, '2025-06-06 03:29:21', '2025-06-06 03:29:21', 0, 43);
INSERT INTO `bill` VALUES (21, 100.00, 19, '111', '2025-06-06 03:31:03', 1, 1, 1, '2025-06-06 03:31:05', '2025-06-06 03:31:05', 0, 0);
INSERT INTO `bill` VALUES (22, 100.00, 0, 's', '2025-06-06 03:37:24', 1, 1, 2, '2025-06-06 03:37:27', '2025-06-06 03:37:27', 0, 41);
INSERT INTO `bill` VALUES (23, 1000.00, 4, 'ssss', '2025-06-01 00:00:00', 1, 1, 1, '2025-06-06 03:37:39', '2025-06-06 03:37:39', 0, 0);
INSERT INTO `bill` VALUES (24, 1000.00, 0, 'asd', '2024-06-21 00:00:00', 1, 1, 2, '2025-06-06 03:57:18', '2025-06-06 03:57:18', 0, 44);
INSERT INTO `bill` VALUES (25, 1000.00, 0, '123', '2025-03-05 00:00:00', 2, 1, 2, '2025-06-06 03:58:03', '2025-06-06 03:58:03', 0, 1);

SET FOREIGN_KEY_CHECKS = 1;
