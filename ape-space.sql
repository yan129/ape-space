-- 文章表
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `id` CHAR(32) NOT NULL COMMENT '文章ID',
    `uid` CHAR(32) NOT NULL COMMENT '用户ID',
    `tid` CHAR(32) DEFAULT NULL COMMENT '主题ID',
    `title` VARCHAR(64) NOT NULL COMMENT '标题',
    `summary` VARCHAR(255) DEFAULT NULL COMMENT '概要',
    `picture` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
    `content` MEDIUMTEXT DEFAULT NULL COMMENT '内容',
    `views` BIGINT(20) DEFAULT '0' COMMENT '浏览数',
    `liked` BIGINT(20) DEFAULT '0' COMMENT '点赞数',
    `disliked` BIGINT(20) DEFAULT '0' COMMENT '踩点击数',
    `is_reward` TINYINT(1) DEFAULT '0' COMMENT '是否开启打赏，默认1开启，0不开启',
    `is_comment` TINYINT(1) DEFAULT '1' COMMENT '是否开启评论功能，默认1开启，0不开启',
    `is_publish` TINYINT(1) DEFAULT '0' COMMENT '1默认发布，0存入草稿箱',
    `is_top` TINYINT(1) DEFAULT '0' COMMENT '默认1置顶，0不置顶',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`uid`),
    KEY `idx_theme_id` (`tid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章表';

-- 专题表
DROP TABLE IF EXISTS `theme`;
CREATE TABLE `theme`(
    `id` CHAR(32) NOT NULL COMMENT '专题ID',
    `uid` CHAR(32) NOT NULL COMMENT '用户ID',
    `name` VARCHAR(32) DEFAULT NULL COMMENT '专题名称',
    `picture` VARCHAR(255) DEFAULT NULL COMMENT '专题封面图',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`uid`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='专题表';

-- 标签表
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label`(
    `id` CHAR(32) NOT NULL COMMENT '标签ID',
    `uid` CHAR(32) NOT NULL COMMENT '用户ID',
    `name` VARCHAR(32) DEFAULT NULL COMMENT '标签名称',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`uid`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='标签表';

-- 文章标签关联表，多对多
DROP TABLE IF EXISTS `article_label`;
CREATE TABLE `article_label`(
    `id` CHAR(32) NOT NULL COMMENT '关联ID',
    `aid` CHAR(32) NOT NULL COMMENT '文章ID',
    `lid` CHAR(32) NOT NULL COMMENT '标签ID',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章标签关联表';

-- 文章收藏表
drop table if exists `favorites`;
create table `favorites`(
    `id` CHAR(32) NOT NULL COMMENT 'ID',
    `uid` CHAR(32) NOT NULL COMMENT '用户ID',
    `aid` CHAR(32) NOT NULL COMMENT '文章ID',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`uid`),
    KEY `idx_article_id` (`aid`),
    UNIQUE KEY `aid` (`aid`)
)ENGINE=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文章收藏表';

-- 用户关注表
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`(
    `id` CHAR(32) NOT NULL COMMENT 'ID',
    `current_uid` CHAR(32) NOT NULL COMMENT '当前用户ID',
    `followed_uid` CHAR(32) NOT NULL COMMENT '被关注用户ID',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `followed_uid` (`followed_uid`),
    KEY `idx_current_uid` (`current_uid`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户关注表';

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`(
    `id` CHAR(32) NOT NULL COMMENT '用户ID',
    `nickname` VARCHAR(32) DEFAULT NULL COMMENT '昵称',
    `username` VARCHAR(32) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `remark` VARCHAR(64) DEFAULT NULL COMMENT '个性签名',
    `profile` VARCHAR(100) DEFAULT NULL COMMENT '个人简介',
    `gender` TINYINT(1) DEFAULT '1' COMMENT '性别，默认 1 为男，0 为女',
    `available` TINYINT(1) DEFAULT '1' COMMENT '账号是否可用，默认 1 为可用，0 不可用',
    `not_expired` TINYINT(1) DEFAULT '1' COMMENT '账号是否过期，默认 1 为不过期，0 为过期',
    `account_not_locked` TINYINT(1) DEFAULT '1' COMMENT '账号是否锁定，默认 1 为不锁定，0 为锁定',
    `credentials_not_expired` TINYINT(1) DEFAULT '1' COMMENT '证书（密码）是否过期，默认 1 为不过期，0 为过期',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '上一次登录时间',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    key `idx_username` (`username`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- 角色表
CREATE TABLE `sys_role`(
    `id` CHAR(32) NOT NULL COMMENT '角色ID',
    `role_name` VARCHAR(32) NOT NULL COMMENT '角色名',
    `role_description` VARCHAR(128) NOT NULL COMMENT '角色说明',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `role_name` (`role_name`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- 用户角色关联表
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`(
    `id` CHAR(32) NOT NULL COMMENT '用户角色关联主键ID',
    `uid` CHAR(32) NOT NULL COMMENT '用户ID',
    `rid` CHAR(32) NOT NULL COMMENT '角色ID',
    `is_deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
    `gmt_created` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`uid`),
    KEY `idx_role_id` (`rid`),
    CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `sys_user` (`id`),
    CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `sys_role` (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';


