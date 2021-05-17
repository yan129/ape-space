package com.ape.article.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ape.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author Yan
 * @since 2021-05-15
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("article")
@ApiModel(value="ArticleDO对象", description="文章表")
public class ArticleDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不存在，请先登录")
    private String uid;

    @ApiModelProperty(value = "主题ID")
    private String tid;

    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    @Length(max = 32, message = "标题字数超过32个字符")
    private String title;

    @ApiModelProperty(value = "概要")
    @Length(max = 255, message = "摘要字数超过255个字符")
    private String summary;

    @ApiModelProperty(value = "封面图")
    private String picture;

    @ApiModelProperty(value = "内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @ApiModelProperty(value = "浏览数")
    private Long views;

    @ApiModelProperty(value = "点赞数")
    private Long liked;

    @ApiModelProperty(value = "踩点击数")
    private Long disliked;

    @ApiModelProperty(value = "是否开启打赏，默认1开启，0不开启")
    private Boolean isReward;

    @ApiModelProperty(value = "是否开启评论功能，默认1开启，0不开启")
    private Boolean isComment;

    @ApiModelProperty(value = "1默认发布，0存入草稿箱")
    private Boolean isPublish;

    @ApiModelProperty(value = "默认1置顶，0不置顶")
    private Boolean isTop;


}
