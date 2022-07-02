package com.ape.user.model;

import com.ape.common.model.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 第三方登录用户表
 * </p>
 *
 * @author Yan
 * @since 2021-08-06
 */
@Data
@Accessors(chain = true)
@TableName("sys_social_user_detail")
@ApiModel(value="SocialUserDetailDO对象", description="第三方登录用户表")
public class SocialUserDetailDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户唯一ID")
    private String uuid;

    @ApiModelProperty(value = "平台来源")
    private String source;

    @ApiModelProperty(value = "token令牌")
    private String accessToken;

    @ApiModelProperty(value = "token令牌过期时间，单位秒")
    private Integer expireIn;

    @ApiModelProperty(value = "refresh_token刷新")
    private String refreshToken;

    @ApiModelProperty(value = "token令牌过期时间，0不过期，单位秒")
    private Integer refreshTokenExpireIn;

    @ApiModelProperty(value = "token令牌类型")
    private String tokenType;

}
