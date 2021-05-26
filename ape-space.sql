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




