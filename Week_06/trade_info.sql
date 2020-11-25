/*
 Navicat MySQL Data Transfer

 Source Server         : HOME_COMPUTER1_MYSQL1
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : java_course

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 25/11/2020 23:34:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for trade_info
-- ----------------------------
DROP TABLE IF EXISTS `trade_info`;
CREATE TABLE `trade_info`  (
  `goods_id` int NOT NULL,
  `user_id` int NOT NULL,
  `update_time` datetime(0) NOT NULL,
  `trade_time` datetime(0) NOT NULL,
  `status` int NOT NULL,
  `goods_basic_version` int NOT NULL,
  `goods_detail_version` int NOT NULL,
  PRIMARY KEY (`goods_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
