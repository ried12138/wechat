/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云私人数据库
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:54306
 Source Schema         : wechatDB

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 31/03/2025 09:26:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dict_item
-- ----------------------------
DROP TABLE IF EXISTS `dict_item`;
CREATE TABLE `dict_item` (
  `item_id` int NOT NULL AUTO_INCREMENT COMMENT '主键，唯一标识每个字典项。',
  `type_id` int DEFAULT NULL COMMENT '外键，关联到 dict_type 表的 type_id。',
  `grade` int NOT NULL DEFAULT '1' COMMENT '字典等级，1就等于是1级字典，2属于二级字典',
  `grade_type_id` int DEFAULT NULL COMMENT '是否有二级字典，如果有这里就指向二级字典的item_id',
  `item_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT ' 字典项的代码，通常是唯一的标识符。',
  `item_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典项的值或名称。',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '对字典项的描述。',
  `sort_order` int DEFAULT '0' COMMENT '排序字段，用于定义字典项在列表中的顺序。',
  `flag` int NOT NULL DEFAULT '1' COMMENT '是否启用 0=不可用 1=可用',
  PRIMARY KEY (`item_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `dict_item_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `dict_type` (`type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典字段';

SET FOREIGN_KEY_CHECKS = 1;
