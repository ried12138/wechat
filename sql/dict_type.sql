/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云私人数据库
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 39.106.75.80:54306
 Source Schema         : wechatDB

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/03/2025 12:06:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type` (
  `type_id` int NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识每种字典类型。',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典类型的名称。',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '对字典类型的描述。',
  `flag` int NOT NULL DEFAULT '1' COMMENT '是否启用 0=不启用，1=启用',
  PRIMARY KEY (`type_id`),
  UNIQUE KEY `type_name` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='类型字典';

SET FOREIGN_KEY_CHECKS = 1;
