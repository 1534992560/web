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

 Date: 06/06/2025 04:37:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for income_type
-- ----------------------------
DROP TABLE IF EXISTS `income_type`;
CREATE TABLE `income_type`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `house_id` int NULL DEFAULT NULL,
  `is_custom` int NULL DEFAULT NULL,
  `is_delete` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of income_type
-- ----------------------------
INSERT INTO `income_type` VALUES (1, '奖金', '2025-06-06 02:57:46', '2025-06-06 03:06:45', 1, 1, 0);
INSERT INTO `income_type` VALUES (36, 'sadasd', '2025-06-06 03:06:27', '2025-06-06 03:06:27', 1, 1, 1);
INSERT INTO `income_type` VALUES (38, 'asdsassss', '2025-06-06 03:18:13', '2025-06-06 03:20:56', 1, 1, 1);
INSERT INTO `income_type` VALUES (39, 'sadadas', '2025-06-06 03:21:19', '2025-06-06 03:21:19', 1, 1, 1);
INSERT INTO `income_type` VALUES (40, 'sadadasa', '2025-06-06 03:21:23', '2025-06-06 03:21:23', 1, 1, 1);
INSERT INTO `income_type` VALUES (41, '红包', '2025-06-06 03:21:37', '2025-06-06 03:21:37', 1, 1, 0);
INSERT INTO `income_type` VALUES (42, '年终奖', '2025-06-06 03:21:43', '2025-06-06 03:21:43', 1, 1, 0);
INSERT INTO `income_type` VALUES (43, '兼职', '2025-06-06 03:21:51', '2025-06-06 03:21:51', 1, 1, 0);
INSERT INTO `income_type` VALUES (44, '股票', '2025-06-06 03:33:15', '2025-06-06 03:33:15', 1, 1, 0);
INSERT INTO `income_type` VALUES (45, '股票ss', '2025-06-06 03:33:21', '2025-06-06 03:33:21', 1, 1, 1);
INSERT INTO `income_type` VALUES (46, '捡到钱了', '2025-06-06 03:59:13', '2025-06-06 03:59:13', 1, 1, 0);
INSERT INTO `income_type` VALUES (47, 'asdasdss', '2025-06-06 04:01:27', '2025-06-06 04:01:35', 1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
