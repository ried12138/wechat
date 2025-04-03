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

 Date: 31/03/2025 16:57:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_data_info
-- ----------------------------
DROP TABLE IF EXISTS `file_data_info`;
CREATE TABLE `file_data_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `open_id` int NOT NULL COMMENT '绑定的openId',
  `acc_id` int NOT NULL COMMENT '绑定的账号account_info id值',
  `file_name` varchar(255) NOT NULL COMMENT '文件名称',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件url',
  `file_bucket_name` varchar(255) NOT NULL COMMENT '文件所在桶(minIO)',
  `creation_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `flag` int NOT NULL DEFAULT '1' COMMENT '是否展示0=不展示 1=展示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
