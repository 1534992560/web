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

 Date: 06/06/2025 04:37:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for house
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `budget` float(255, 0) NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `is_delete` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of house
-- ----------------------------
INSERT INTO `house` VALUES (1, 'admin', 0, '2024-11-13 12:02:39', '2025-06-06 04:13:35', 0);
INSERT INTO `house` VALUES (2, '333', 0, '2024-11-13 12:27:08', '2024-11-13 12:27:08', 0);
INSERT INTO `house` VALUES (3, '555', 0, '2024-11-13 15:05:41', '2024-11-13 15:05:41', 0);
INSERT INTO `house` VALUES (4, '11', 0, '2024-11-13 15:12:03', '2024-11-13 15:12:03', 0);
INSERT INTO `house` VALUES (5, 'text666', 0, '2025-06-05 16:59:38', '2025-06-05 16:59:38', 0);
INSERT INTO `house` VALUES (6, 'text777', 0, '2025-06-06 02:22:06', '2025-06-06 02:22:06', 0);

SET FOREIGN_KEY_CHECKS = 1;
