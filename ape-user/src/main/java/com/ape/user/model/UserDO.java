package com.ape.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.ape.common.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;


/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Yan
 * @since 2021-06-02
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="UserDO对象", description="用户表")
public class UserDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户unionId")
    private Long unionId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    @JsonIgnore
    private String username;

    @ApiModelProperty(value = "密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "个性签名")
    @Length(max = 32, message = "签名大于32个字符")
    private String remark;

    @ApiModelProperty(value = "个人简介")
    @Length(max = 150, message = "简介大于150个字符")
    private String profile;

    @ApiModelProperty(value = "性别，默认 1 为男，0 为女，-1为未知")
    private Integer gender;

    @ApiModelProperty(value = "账号是否可用，默认 1 为可用，0 不可用")
    private Boolean available;

    @ApiModelProperty(value = "账号是否过期，默认 1 为不过期，0 为过期")
    private Boolean notExpired;

    @ApiModelProperty(value = "账号是否锁定，默认 1 为不锁定，0 为锁定")
    private Boolean accountNotLocked;

    @ApiModelProperty(value = "证书（密码）是否过期，默认 1 为不过期，0 为过期")
    private Boolean credentialsNotExpired;

    @ApiModelProperty(value = "上一次登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "主题颜色")
    private String themeColor;

}
