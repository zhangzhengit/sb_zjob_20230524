/*
 Navicat Premium Data Transfer

 Source Server         : 1.14-mysql-黄色国光
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 192.168.1.14:3306
 Source Schema         : zjob

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 27/05/2023 10:43:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client_online
-- ----------------------------
DROP TABLE IF EXISTS `client_online`;
CREATE TABLE `client_online`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT 'job表的id',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'client的host',
  `port` int(11) NULL DEFAULT NULL COMMENT 'client的port',
  `heartbeat_time` datetime(0) NULL DEFAULT NULL COMMENT 'client的心跳时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 277 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'job的名称',
  `cron` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `status` int(255) NOT NULL COMMENT '状态 1启用- 2- 停用',
  `create_time` datetime(0) NOT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最近一次编辑job的时间',
  `push_time` datetime(0) NULL DEFAULT NULL COMMENT '最近一次推送job信息的时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique_index`(`name`) USING BTREE COMMENT 'name的唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '\r\njob信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT 'job表的id，对应一个job表的数据',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'job表的id，同时对应@ZJobTask.name()属性值',
  `job_log` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '@ZJobTask标记的方法的执行的日志',
  `jobtask_id` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '@ZJobTask.id()属性值',
  `job_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '@ZJobTask.description()属性值',
  `create_time` datetime(0) NOT NULL,
  `job_host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'client的host',
  `job_port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'client的port',
  `execution_time` datetime(0) NULL DEFAULT NULL COMMENT 'client的@ZJobTask标记的方法的开始执行时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT 'client的@ZJobTask标记的方法的执行结束时间',
  `exception_time` datetime(0) NULL DEFAULT NULL COMMENT 'client的@ZJobTask标记的方法的执行异常时间',
  `status` int(255) NULL DEFAULT NULL COMMENT '执行状态 1-正常 2-失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2503 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'job执行的日志，有client推送到server中' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job_update_log
-- ----------------------------
DROP TABLE IF EXISTS `job_update_log`;
CREATE TABLE `job_update_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL,
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `job_log` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `old_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'job 更新前的值',
  `new_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'job 更新后的值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'job表的修改记录表，记录每次更新job的前后变化' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
