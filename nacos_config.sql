/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 09/04/2021 18:19:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'spring-boot-http.yaml', 'COMMON_GROUP', 'spring: \r\n  http: \r\n    encoding: \r\n      charset: UTF-8 \r\n      force: true \r\n      enabled: true \r\n    messages: \r\n      encoding: UTF-8\r\n\r\nserver: \r\n  tomcat: \r\n    remote_ip_header: x-forwarded-for \r\n    protocol_header: x-forwarded-proto \r\n  servlet: \r\n    context-path: / \r\n  use-forward-headers: true\r\n\r\nmanagement: \r\n  endpoints: \r\n    web:exposure: \r\n      include: refresh,health,info,env', 'ca8e16327a77b1e9afe6214412a6ba4f', '2021-04-06 10:14:24', '2021-04-06 10:27:55', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (2, 'merchant-application.yaml', 'SHANJUPAY_GROUP', 'server: \r\n  servlet: \r\n    context-path: /merchant \r\n    \r\nswagger: \r\n  enable: true\r\n\r\nsms: \r\n  url: \"http://localhost:56085/sailing\" \r\n  effectiveTime: 600\r\n\r\noss:\r\n  qiniu: \r\n    url: \"\" \r\n    accessKey: \"\" \r\n    secretKey: \"\" \r\n    bucket: \"\"', '95553082b2c0bba10f795760da6e4c34', '2021-04-06 10:15:21', '2021-04-06 15:55:18', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (3, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: \r\n    context-path: /merchant-service\r\n\r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_merchant_service?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false \r\n      username: root \r\n      password: root\r\n\r\nmybatis-plus: \r\n  typeAliasesPackage: com.shanjupay.merchant.entity \r\n  mapper-locations: classpath:com/shanjupay/*/mapper/*.xml', 'aba9a701935452cbb29e25050258dcff', '2021-04-06 10:16:11', '2021-04-06 10:30:01', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (4, 'spring-boot-starter-druid.yaml', 'COMMON_GROUP', 'spring: \r\n  datasource: \r\n    type: com.alibaba.druid.pool.DruidDataSource \r\n    driver-class-name: com.mysql.cj.jdbc.Driver \r\n    url: jdbc:mysql://localhost:3306/oauth?useUnicode=true \r\n    username: root \r\n    password: yourpassword \r\n    druid: \r\n      initial-size: 5 \r\n      min-idle: 5 \r\n      max-active: 20 \r\n      max-wait: 60000 \r\n      time-between-eviction-runs-millis: 60000 \r\n      min-evictable-idle-time-millis: 300000 \r\n      validation-query: SELECT 1 FROM DUAL \r\n      test-while-idle: true \r\n      test-on-borrow: true \r\n      test-on-return: false \r\n      pool-prepared-statements: true\r\n      max-pool-prepared-statement-per-connection-size: 20 \r\n      filter: \r\n        stat: \r\n          slow-sql-millis: 1 \r\n          log-slow-sql: true \r\n      filters: config,stat,wall,log4j2 \r\n      web-stat-filter: \r\n        enabled: true \r\n        url-pattern: /* \r\n        exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" \r\n        session-stat-enable: false \r\n        session-stat-max-count: 1000 \r\n        principal-cookie-name: admin \r\n        principal-session-name: admin \r\n        profile-enable: true \r\n      stat-view-servlet: \r\n        enabled: true \r\n        url-pattern: /druid/* \r\n        allow: 127.0.0.1,192.168.163.1 \r\n        deny: 192.168.1.73 \r\n        reset-enable: false \r\n        login-password: admin \r\n        login-username: admin \r\n      aop-patterns: com.shanjupay.*.service.*', '4999ad03efcfae06ce511ddf434e99e0', '2021-04-06 10:19:52', '2021-04-06 10:27:32', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (6, 'spring-boot-mybatis-plus.yaml', 'COMMON_GROUP', 'mybatis-plus: \r\n  configuration: \r\n    cache-enabled: false \r\n    map-underscore-to-camel-case: true \r\n  global-config: \r\n    id-type: 0 \r\n    field-strategy: 0 \r\n    db-column-underline: true \r\n    refresh-mapper: true \r\n  typeAliasesPackage: com.shanjupay.user.entity\r\n  mapper-locations: classpath:com/shanjupay/*/mapper/*.xml', 'c2ff471b00464a6a2b2142cf2a1ac7a3', '2021-04-06 10:22:22', '2021-04-06 10:26:10', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (17, 'transaction-service.yaml', 'SHANJUPAY_GROUP', 'server: \r\n  servlet: \r\n    context-path: /transaction \r\n\r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_transaction?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root\r\n\r\nmybatis-plus:\r\n  typeAliasesPackage: com.shanjupay.transaction.entity\r\n  mapper-locations: classpath:com/shanjupay/*/mapper/*.xml', '28e1be09a2d59d67db42f81c3420ac0d', '2021-04-06 17:26:30', '2021-04-06 17:27:07', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (19, 'spring-boot-redis.yaml', 'COMMON_GROUP', 'spring:\r\n  redis: \r\n  # Redis?????????0? \r\n    database: 0\r\n    host: 127.0.0.1\r\n    port: 6379\r\n    # ?????????? \r\n    timeout: 1000ms\r\n    lettuce: \r\n      pool: \r\n      # ??????????? \r\n        max?idle: 8\r\n        # ???????????\r\n        min?idle: 0\r\n        # ???????????????????\r\n        max?active: 8\r\n        # ??????????????????????\r\n        max?wait: 1000ms\r\n      shutdown?timeout: 1000ms', '6a15709a1f97f03d6ad6f92ea483abe6', '2021-04-08 17:28:12', '2021-04-08 17:28:12', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (20, 'jwt.yaml', 'COMMON_GROUP', 'siging?key: shanju123', '72fc288c259bfcea4427b2607a7536e4', '2021-04-08 18:06:49', '2021-04-08 18:06:49', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (21, 'gateway-service.yaml', 'SHANJUPAY_GROUP', '#????\r\nzuul:\r\n  retryable: true\r\n  add-host-header: true\r\n  ignoredServices: \"*\"\r\n  sensitiveHeaders: \"*\"\r\n  routes:\r\n    operation-application:\r\n      path: /operation/**\r\n      stripPrefix: false\r\n    merchant-application:\r\n      path: /merchant/**\r\n      stripPrefix: false\r\n    uaa-service: \r\n      path: /uaa/**\r\n      stripPrefix: false\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n  compression:\r\n    request:\r\n      enabled: true # ????GZIP??\r\n      mime-types: [\"text/xml\",\"application/xml\",\"application/json\"] # ???????MIME TYPE\r\n      min-request-size: 2048 # ???????????\r\n    response:\r\n      enabled: true # ????GZIP??\r\n\r\nhystrix:\r\n  command:\r\n    default:\r\n      execution:\r\n        isolation:\r\n          thread:\r\n            timeoutInMilliseconds: 93000  # ????????  default 1000\r\n        timeout:\r\n          enabled: true # ???????? default true\r\n\r\nribbon:\r\n  nacos:\r\n    enabled: true # ???????\r\n  ConnectTimeout: 3000 # ???????? default 2000\r\n  ReadTimeout: 20000    # ????????  default 5000\r\n  OkToRetryOnAllOperations: false # ????????????  default false\r\n  MaxAutoRetriesNextServer: 1    # ?????????  default 1\r\n  MaxAutoRetries: 1     # ?????????? default 0', '36becc60dcc0d752986a63525eb61fce', '2021-04-08 18:08:49', '2021-04-08 18:08:49', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (22, 'uaa-service.yaml', 'SHANJUPAY_GROUP', '# ??spring?boot?http.yaml??? \r\nserver:\r\n  servlet:\r\n    context?path: /uaa\r\n# ??spring?boot?starter?druid.yaml???\r\nspring: \r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_uaa?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root', 'bd3b80e1b9162b9459e9511c056596cc', '2021-04-08 18:10:30', '2021-04-08 18:10:30', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (23, 'user-service.yaml', 'SHANJUPAY_GROUP', '# ??spring?boot?http.yaml???\r\nserver:\r\n  servlet:\r\n    context-path: /user\r\n# ??spring?boot?starter?druid.yaml???\r\nspring:\r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root\r\n# ??spring?boot?mybatis?plus.yaml???\r\nmybatis-plus:\r\n  typeAliasesPackage: com.shanjupay.user.entity\r\nmapper-locations: classpath:com/shanjupay/*/mapper/xml/*.xml\r\n\r\nsms:\r\n  url: \"http://localhost:56085/sailing\"\r\n  effectiveTime: 6000', '174d78283f90a05c77f88904de21867c', '2021-04-08 18:13:29', '2021-04-08 18:14:32', NULL, '127.0.0.1', '', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'null', 'null', 'null', 'yaml', 'null');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime(0) NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(64) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'spring-boot-http.yaml', 'COMMON_GROUP', '', 'spring: \r\n  http: \r\n    encoding: \r\n      charset: UTF?8 \r\n      force: true \r\n      enabled: true \r\n    messages: \r\n      encoding: UTF?8\r\n\r\nserver: \r\n  tomcat: \r\n    remote_ip_header: x?forwarded?for \r\n    protocol_header: x?forwarded?proto \r\n  servlet: \r\n    context?path: / \r\n  use?forward?headers: true\r\n\r\nmanagement: \r\n  endpoints: \r\n    web:exposure: \r\n      include: refresh,health,info,env', 'a9a40703e56eef624b4b06ca501c9786', '2010-05-05 00:00:00', '2021-04-06 10:14:24', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 2, 'merchant-application.yam', 'SHANJUPAY_GROUP', '', '#?????? \r\nserver: \r\n  servlet: \r\n    context?path: /merchant \r\n    \r\n#??Swagger \r\nswagger: \r\n  enable: true', '42b1ba544ac0a04587b1ada8496138e2', '2010-05-05 00:00:00', '2021-04-06 10:15:21', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 3, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: context?path: /merchant?service', 'fcb87de22b5857f4673bc68f6c7b1380', '2010-05-05 00:00:00', '2021-04-06 10:16:11', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 4, 'spring-boot-starter-druid.yaml', 'COMMON_GROUP', '', 'spring: \r\n  datasource: \r\n    type: com.alibaba.druid.pool.DruidDataSource \r\n    driver?class?name: com.mysql.cj.jdbc.Driver \r\n    url: jdbc:mysql://localhost:3306/oauth?useUnicode=true \r\n    username: root \r\n    password: yourpassword \r\n    druid: \r\n      initial?size: 5 \r\n      min?idle: 5 \r\n      max?active: 20 \r\n      max?wait: 60000 \r\n      time?between?eviction?runs?millis: 60000 \r\n      min?evictable?idle?time?millis: 300000 \r\n      validation?query: SELECT 1 FROM DUAL \r\n      test?while?idle: true \r\n      test?on?borrow: true \r\n      test?on?return: false \r\n      pool?prepared?statements: true\r\n      max?pool?prepared?statement?per?connection?size: 20 \r\n      filter: \r\n        stat: \r\n          slow?sql?millis: 1 \r\n          log?slow?sql: true \r\n      filters: config,stat,wall,log4j2 \r\n      web?stat?filter: \r\n        enabled: true \r\n        url?pattern: /* \r\n        exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" \r\n        session?stat?enable: false \r\n        session?stat?max?count: 1000 \r\n        principal?cookie?name: admin \r\n        principal?session?name: admin \r\n        profile?enable: true \r\n      stat?view?servlet: \r\n        enabled: true \r\n        url?pattern: /druid/* \r\n        allow: 127.0.0.1,192.168.163.1 \r\n        deny: 192.168.1.73 \r\n        reset?enable: false \r\n        login?password: admin \r\n        login?username: admin \r\n      aop?patterns: com.shanjupay.*.service.*', '952ecb40ed5b9f02ee21a5208b62f869', '2010-05-05 00:00:00', '2021-04-06 10:19:52', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (3, 5, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: context?path: /merchant?service', '47475a8847cb45bfa92f2db9bd786aa1', '2010-05-05 00:00:00', '2021-04-06 10:21:11', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 6, 'spring-boot-mybatis-plus.yaml', 'COMMON_GROUP', '', 'mybatis?plus: \r\n  configuration: \r\n    cache?enabled: false \r\n    map?underscore?to?camel?case: true \r\n  global?config: \r\n    id?type: 0 \r\n    field?strategy: 0 \r\n    db?column?underline: true \r\n    refresh?mapper: true \r\n  typeAliasesPackage: com.shanjupay.user.entity\r\n  mapper?locations: classpath:com/shanjupay/*/mapper/*.xml', 'c29dda8f579c9a9c5a1a786639398bd9', '2010-05-05 00:00:00', '2021-04-06 10:22:22', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (3, 7, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: context?path: /merchant?service\r\n\r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_merchant_service?useUnicode=true&characterEncoding=UTF?8&serverTimezone=Asia/Shanghai&useSSL=false \r\n      username: root \r\n      password: root', '422cb18b2050b705f10d2245d06b41b9', '2010-05-05 00:00:00', '2021-04-06 10:22:46', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (6, 8, 'spring-boot-mybatis-plus.yaml', 'COMMON_GROUP', '', 'mybatis?plus: \r\n  configuration: \r\n    cache?enabled: false \r\n    map?underscore?to?camel?case: true \r\n  global?config: \r\n    id?type: 0 \r\n    field?strategy: 0 \r\n    db?column?underline: true \r\n    refresh?mapper: true \r\n  typeAliasesPackage: com.shanjupay.user.entity\r\n  mapper?locations: classpath:com/shanjupay/*/mapper/*.xml', '42cbca06d81e857d9cd14ac97db59cff', '2010-05-05 00:00:00', '2021-04-06 10:26:10', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (4, 9, 'spring-boot-starter-druid.yaml', 'COMMON_GROUP', '', 'spring: \r\n  datasource: \r\n    type: com.alibaba.druid.pool.DruidDataSource \r\n    driver?class?name: com.mysql.cj.jdbc.Driver \r\n    url: jdbc:mysql://localhost:3306/oauth?useUnicode=true \r\n    username: root \r\n    password: yourpassword \r\n    druid: \r\n      initial?size: 5 \r\n      min?idle: 5 \r\n      max?active: 20 \r\n      max?wait: 60000 \r\n      time?between?eviction?runs?millis: 60000 \r\n      min?evictable?idle?time?millis: 300000 \r\n      validation?query: SELECT 1 FROM DUAL \r\n      test?while?idle: true \r\n      test?on?borrow: true \r\n      test?on?return: false \r\n      pool?prepared?statements: true\r\n      max?pool?prepared?statement?per?connection?size: 20 \r\n      filter: \r\n        stat: \r\n          slow?sql?millis: 1 \r\n          log?slow?sql: true \r\n      filters: config,stat,wall,log4j2 \r\n      web?stat?filter: \r\n        enabled: true \r\n        url?pattern: /* \r\n        exclusions: \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\" \r\n        session?stat?enable: false \r\n        session?stat?max?count: 1000 \r\n        principal?cookie?name: admin \r\n        principal?session?name: admin \r\n        profile?enable: true \r\n      stat?view?servlet: \r\n        enabled: true \r\n        url?pattern: /druid/* \r\n        allow: 127.0.0.1,192.168.163.1 \r\n        deny: 192.168.1.73 \r\n        reset?enable: false \r\n        login?password: admin \r\n        login?username: admin \r\n      aop?patterns: com.shanjupay.*.service.*', '9ff9acf13f7cedf29eff686398fd41e9', '2010-05-05 00:00:00', '2021-04-06 10:27:32', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (1, 10, 'spring-boot-http.yaml', 'COMMON_GROUP', '', 'spring: \r\n  http: \r\n    encoding: \r\n      charset: UTF?8 \r\n      force: true \r\n      enabled: true \r\n    messages: \r\n      encoding: UTF?8\r\n\r\nserver: \r\n  tomcat: \r\n    remote_ip_header: x?forwarded?for \r\n    protocol_header: x?forwarded?proto \r\n  servlet: \r\n    context?path: / \r\n  use?forward?headers: true\r\n\r\nmanagement: \r\n  endpoints: \r\n    web:exposure: \r\n      include: refresh,health,info,env', '5fde78de467b38f61bf7a121282365b9', '2010-05-05 00:00:00', '2021-04-06 10:27:55', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (3, 11, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: context?path: /merchant?service\r\n\r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_merchant_service?useUnicode=true&characterEncoding=UTF?8&serverTimezone=Asia/Shanghai&useSSL=false \r\n      username: root \r\n      password: root\r\n\r\nmybatis?plus: \r\n  typeAliasesPackage: com.shanjupay.merchant.entity \r\n  mapper?locations: classpath:com/shanjupay/*/mapper/*.xml', '28f62fa3408746067a1b89f978b3545d', '2010-05-05 00:00:00', '2021-04-06 10:28:31', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (2, 12, 'merchant-application.yam', 'SHANJUPAY_GROUP', '', '#?????? \r\nserver: \r\n  servlet: \r\n    context?path: /merchant \r\n    \r\n#??Swagger \r\nswagger: \r\n  enable: true', '715764a02321c0a7e780b21ffc5da95e', '2010-05-05 00:00:00', '2021-04-06 10:28:46', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (3, 13, 'merchant-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: \r\n    context-path: /merchant?service\r\n\r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_merchant_service?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false \r\n      username: root \r\n      password: root\r\n\r\nmybatis-plus: \r\n  typeAliasesPackage: com.shanjupay.merchant.entity \r\n  mapper-locations: classpath:com/shanjupay/*/mapper/*.xml', '57054c1c70961249906d3160c375a596', '2010-05-05 00:00:00', '2021-04-06 10:30:01', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (2, 14, 'merchant-application.yaml', 'SHANJUPAY_GROUP', '', '#?????? \r\nserver: \r\n  servlet: \r\n    context-path: /merchant \r\n    \r\n#??Swagger \r\nswagger: \r\n  enable: true', '96856131bbc8feb05025cc65dcc9685e', '2010-05-05 00:00:00', '2021-04-06 10:46:50', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (2, 15, 'merchant-application.yaml', 'SHANJUPAY_GROUP', '', 'server: \r\n  servlet: \r\n    context-path: /merchant \r\n    \r\nswagger: \r\n  enable: true', 'cf07b35760afa1ff9a852f5460096b1c', '2010-05-05 00:00:00', '2021-04-06 11:46:10', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (2, 16, 'merchant-application.yaml', 'SHANJUPAY_GROUP', '', 'server: \r\n  servlet: \r\n    context-path: /merchant \r\n    \r\nswagger: \r\n  enable: true\r\n\r\nsms: \r\n  url: \"http://localhost:56085/sailing\" \r\n  effectiveTime: 600', 'd045a52f3b5b9469c7f3dd10f5cfeca9', '2010-05-05 00:00:00', '2021-04-06 15:55:18', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 17, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: \r\n    context?path: /transaction \r\n# ??spring?boot?starter?druid.yaml??? \r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_transaction?useUnicode=true&characterEncoding=UTF?8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root\r\n# ??spring?boot?mybatis?plus.yaml???\r\nmybatis?plus:\r\n  typeAliasesPackage: com.shanjupay.transaction.entity\r\n  mapper?locations: classpath:com/shanjupay/*/mapper/*.xml', 'd51216ea781b7c784d2ab84371780ff8', '2010-05-05 00:00:00', '2021-04-06 17:26:30', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (17, 18, 'transaction-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver: \r\n  servlet: \r\n    context?path: /transaction \r\n# ??spring?boot?starter?druid.yaml??? \r\nspring: \r\n  datasource: \r\n    druid: \r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_transaction?useUnicode=true&characterEncoding=UTF?8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root\r\n# ??spring?boot?mybatis?plus.yaml???\r\nmybatis?plus:\r\n  typeAliasesPackage: com.shanjupay.transaction.entity\r\n  mapper?locations: classpath:com/shanjupay/*/mapper/*.xml', 'e88e8225cdd975997d86a59ff05f9c79', '2010-05-05 00:00:00', '2021-04-06 17:27:07', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 19, 'spring-boot-redis.yaml', 'COMMON_GROUP', '', 'spring:\r\n  redis: \r\n  # Redis?????????0? \r\n    database: 0\r\n    host: 127.0.0.1\r\n    port: 6379\r\n    # ?????????? \r\n    timeout: 1000ms\r\n    lettuce: \r\n      pool: \r\n      # ??????????? \r\n        max?idle: 8\r\n        # ???????????\r\n        min?idle: 0\r\n        # ???????????????????\r\n        max?active: 8\r\n        # ??????????????????????\r\n        max?wait: 1000ms\r\n      shutdown?timeout: 1000ms', '6a15709a1f97f03d6ad6f92ea483abe6', '2010-05-05 00:00:00', '2021-04-08 17:28:12', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 20, 'jwt.yaml', 'COMMON_GROUP', '', 'siging?key: shanju123', '72fc288c259bfcea4427b2607a7536e4', '2010-05-05 00:00:00', '2021-04-08 18:06:49', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 21, 'gateway-service.yaml', 'SHANJUPAY_GROUP', '', '#????\r\nzuul:\r\n  retryable: true\r\n  add-host-header: true\r\n  ignoredServices: \"*\"\r\n  sensitiveHeaders: \"*\"\r\n  routes:\r\n    operation-application:\r\n      path: /operation/**\r\n      stripPrefix: false\r\n    merchant-application:\r\n      path: /merchant/**\r\n      stripPrefix: false\r\n    uaa-service: \r\n      path: /uaa/**\r\n      stripPrefix: false\r\n\r\nfeign:\r\n  hystrix:\r\n    enabled: true\r\n  compression:\r\n    request:\r\n      enabled: true # ????GZIP??\r\n      mime-types: [\"text/xml\",\"application/xml\",\"application/json\"] # ???????MIME TYPE\r\n      min-request-size: 2048 # ???????????\r\n    response:\r\n      enabled: true # ????GZIP??\r\n\r\nhystrix:\r\n  command:\r\n    default:\r\n      execution:\r\n        isolation:\r\n          thread:\r\n            timeoutInMilliseconds: 93000  # ????????  default 1000\r\n        timeout:\r\n          enabled: true # ???????? default true\r\n\r\nribbon:\r\n  nacos:\r\n    enabled: true # ???????\r\n  ConnectTimeout: 3000 # ???????? default 2000\r\n  ReadTimeout: 20000    # ????????  default 5000\r\n  OkToRetryOnAllOperations: false # ????????????  default false\r\n  MaxAutoRetriesNextServer: 1    # ?????????  default 1\r\n  MaxAutoRetries: 1     # ?????????? default 0', '36becc60dcc0d752986a63525eb61fce', '2010-05-05 00:00:00', '2021-04-08 18:08:49', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 22, 'uaa-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml??? \r\nserver:\r\n  servlet:\r\n    context?path: /uaa\r\n# ??spring?boot?starter?druid.yaml???\r\nspring: \r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_uaa?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root', 'bd3b80e1b9162b9459e9511c056596cc', '2010-05-05 00:00:00', '2021-04-08 18:10:30', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (0, 23, 'user-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml???\r\nserver:\r\n  servlet:\r\n    context?path: /user\r\n# ??spring?boot?starter?druid.yaml???\r\nspring:\r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root\r\n# ??spring?boot?mybatis?plus.yaml???\r\nmybatis?plus:\r\n  typeAliasesPackage: com.shanjupay.user.entity\r\nmapper?locations: classpath:com/shanjupay/*/mapper/xml/*.xml\r\n\r\nsms:\r\n  url: \"http://localhost:56085/sailing\"\r\n  effectiveTime: 6000', '051c09620eb6717e0b1266272bda05d1', '2010-05-05 00:00:00', '2021-04-08 18:13:29', NULL, '127.0.0.1', 'I', '14d393fb-2d26-4e2a-b6d1-c152541749f7');
INSERT INTO `his_config_info` VALUES (23, 24, 'user-service.yaml', 'SHANJUPAY_GROUP', '', '# ??spring?boot?http.yaml???\r\nserver:\r\n  servlet:\r\n    context?path: /user\r\n# ??spring?boot?starter?druid.yaml???\r\nspring:\r\n  datasource:\r\n    druid:\r\n      url: jdbc:mysql://127.0.0.1:3306/shanjupay_user?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&useSSL=false\r\n      username: root\r\n      password: root\r\n# ??spring?boot?mybatis?plus.yaml???\r\nmybatis?plus:\r\n  typeAliasesPackage: com.shanjupay.user.entity\r\nmapper?locations: classpath:com/shanjupay/*/mapper/xml/*.xml\r\n\r\nsms:\r\n  url: \"http://localhost:56085/sailing\"\r\n  effectiveTime: 6000', '734c06bc354d84b8ceb5d552bb6517dd', '2010-05-05 00:00:00', '2021-04-08 18:14:32', NULL, '127.0.0.1', 'U', '14d393fb-2d26-4e2a-b6d1-c152541749f7');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `role` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime(0) NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', '14d393fb-2d26-4e2a-b6d1-c152541749f7', 'DEV', '????', 'nacos', 1617675084999, 1617675084999);
INSERT INTO `tenant_info` VALUES (2, '1', 'ed96517c-9a07-4a5c-825f-d92210557b41', 'TEST', '????', 'nacos', 1617675095762, 1617675095762);
INSERT INTO `tenant_info` VALUES (3, '1', '34fc4ff1-e323-4e16-84d5-272e603f89d3', 'PRO', '????', 'nacos', 1617675107840, 1617675107840);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
