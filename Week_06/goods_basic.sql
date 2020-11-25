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

 Date: 25/11/2020 23:33:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods_basic
-- ----------------------------
DROP TABLE IF EXISTS `goods_basic`;
CREATE TABLE `goods_basic`  (
  `ID` int NOT NULL,
  `TITLE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `PRICE` decimal(10, 2) NOT NULL,
  `UPDATE_TIME` datetime(0) NOT NULL,
  `VERSION` int NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
