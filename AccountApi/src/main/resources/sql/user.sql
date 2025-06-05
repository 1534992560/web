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

 Date: 06/06/2025 04:37:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名称',
  `password` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户密码',
  `is_admin` tinyint NULL DEFAULT 0 COMMENT '是否管理员：1是，0否',
  `house_id` int NOT NULL COMMENT '家庭id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint NULL DEFAULT 0 COMMENT '是否删除：1是，0否',
  `email` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '4QrcOUm6Wau+VuBX8g+IPg==', 1, 1, '2024-11-13 12:02:39', '2025-06-06 02:33:51', 0, '');
INSERT INTO `user` VALUES (2, '张三', '4QrcOUm6Wau+VuBX8g+IPg==', 0, 1, '2024-11-13 12:02:59', '2025-06-05 15:52:28', 0, '');
INSERT INTO `user` VALUES (3, '李四', '4QrcOUm6Wau+VuBX8g+IPg==', 1, 2, '2024-11-13 12:27:08', '2024-11-13 12:27:08', 0, '');
INSERT INTO `user` VALUES (7, '王五', 'cY6loKHas+1m4mz6FTf18w==', 0, 1, '2024-11-13 15:36:19', '2025-06-06 01:49:55', 0, '');
INSERT INTO `user` VALUES (11, 'text666', 'z3QtRIRYsVF1P2AkFvZxrQ==', 1, 5, '2025-06-05 16:59:38', '2025-06-06 02:20:31', 0, '');
INSERT INTO `user` VALUES (17, 'sadsadsad', '+eqCeFCuZFft+t5baWwgbA==', 0, 1, '2025-06-05 17:38:14', '2025-06-05 17:38:55', 1, 'sa51@16.com');
INSERT INTO `user` VALUES (18, 'asdsadasd', 'K3ID4dGxw91GFb3Q/+YHNg==', 0, 1, '2025-06-05 17:41:09', '2025-06-05 17:41:12', 1, 'asda@16.com');
INSERT INTO `user` VALUES (19, 'sadsadsa', 'jY0l0JBKWolfP4g2PyIT6w==', 0, 5, '2025-06-05 17:43:02', '2025-06-05 17:43:02', 0, 'sada@63.com');
INSERT INTO `user` VALUES (20, 'sadsasd', 'fGeS0vZBTuf3fc9jQdBi1A==', 0, 1, '2025-06-05 17:50:22', '2025-06-05 17:50:24', 1, '9116@163cas.com');
INSERT INTO `user` VALUES (21, 'sadsadas', 'nmMtYcWVH/X2dTGhWhv78g==', 0, 1, '2025-06-05 17:52:47', '2025-06-05 17:52:53', 1, 'sada@163.com');
INSERT INTO `user` VALUES (22, 'sada', 'jY0l0JBKWolfP4g2PyIT6w==', 0, 1, '2025-06-05 17:53:19', '2025-06-05 17:57:18', 1, 'sadsad@163.com');
INSERT INTO `user` VALUES (23, 'asdad', 'dNUI32K46f3QIyYMu1g37w==', 0, 1, '2025-06-05 17:58:33', '2025-06-05 18:03:30', 1, '162@163.com');
INSERT INTO `user` VALUES (24, 'sadsads', 'pGCLnskwgzxQFdy5F2op9Q==', 0, 1, '2025-06-05 18:00:21', '2025-06-05 18:00:30', 1, 'adsa@163.com');
INSERT INTO `user` VALUES (25, 'sadsad', '3b51fl4dRBSHLAbG8fOYbw==', 0, 1, '2025-06-05 18:02:36', '2025-06-05 18:03:21', 1, 'asdsad@163.com');
INSERT INTO `user` VALUES (26, 'saddsadsa', 'XDdW/g6i8IPnu3UuQVxEJQ==', 0, 1, '2025-06-05 18:08:57', '2025-06-05 18:09:41', 1, 'sdad@163.com');
INSERT INTO `user` VALUES (27, 'wqewqe', 'GrTAkKg9zSVFDO2BHJdSfA==', 0, 1, '2025-06-05 20:15:21', '2025-06-05 20:15:25', 1, 'wqeqe@163.com');
INSERT INTO `user` VALUES (28, 'asdasd', 'rV/7C6B9dRvi+G1B8TByRw==', 0, 1, '2025-06-06 00:34:32', '2025-06-06 00:34:38', 1, 'asdsa@163.com');
INSERT INTO `user` VALUES (29, 'zxczxcz', 'dZ8M//eD0U+jZIPrCetmpg==', 0, 1, '2025-06-06 00:47:47', '2025-06-06 00:47:49', 1, 'zxczxcx@163.com');
INSERT INTO `user` VALUES (30, 'asdsadas', 'Jx+HSx4SkpE4BJ0EjwQ3Hw==', 0, 1, '2025-06-06 01:05:59', '2025-06-06 01:06:11', 1, 'asdsad@163.com');
INSERT INTO `user` VALUES (31, 'asdsadas', 'yZoRpTo3SCaeP4bXrDjfEQ==', 0, 1, '2025-06-06 01:13:06', '2025-06-06 01:13:08', 1, 'adsadas21@163.com');
INSERT INTO `user` VALUES (32, 'asdasd', 'Yw+qBeoixI7VxLFq1k9t+g==', 0, 1, '2025-06-06 01:13:17', '2025-06-06 01:13:41', 1, 'dasdas@163.com');
INSERT INTO `user` VALUES (33, 'sadas', 'jY0l0JBKWolfP4g2PyIT6w==', 0, 1, '2025-06-06 01:46:52', '2025-06-06 01:47:06', 1, 'sadad@163.com');
INSERT INTO `user` VALUES (34, 'text777', '4QrcOUm6Wau+VuBX8g+IPg==', 1, 6, '2025-06-06 02:22:06', '2025-06-06 04:00:43', 0, '13879512324@163.com');
INSERT INTO `user` VALUES (35, 'asdasd', 'qPXxZ/RPSWTmyZje6CcRDA==', 0, 1, '2025-06-06 02:27:59', '2025-06-06 02:28:13', 1, '1387951324@163.com');
INSERT INTO `user` VALUES (36, 'asdasd', 'dvziPTM5yb8bMO37ayEdUg==', 0, 1, '2025-06-06 02:28:16', '2025-06-06 02:28:20', 1, 'adad@163.com');
INSERT INTO `user` VALUES (37, 'sadad', 'tEfCegDjo0iIGwAwF3AAzQ==', 0, 1, '2025-06-06 03:13:38', '2025-06-06 03:13:40', 1, 'adsd2@163.com');
INSERT INTO `user` VALUES (38, 'sadsad', 'tEfCegDjo0iIGwAwF3AAzQ==', 0, 1, '2025-06-06 03:39:40', '2025-06-06 03:39:48', 1, 'sadas@163.com');
INSERT INTO `user` VALUES (39, 'asdsad', 'JaTzeMMxIpQlm4jWbCR40g==', 0, 1, '2025-06-06 03:40:05', '2025-06-06 03:40:14', 1, 'asdsad@163.com');

SET FOREIGN_KEY_CHECKS = 1;
