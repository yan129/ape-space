package com.ape.article.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ape.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户关注表
 * </p>
 *
 * @author Yan
 * @since 2021-05-26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("follow")
@ApiModel(value="FollowDO对象", description="用户关注表")
public class FollowDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "当前用户ID")
    private String currentUid;

    @ApiModelProperty(value = "被关注用户ID")
    private String followedUid;


}
