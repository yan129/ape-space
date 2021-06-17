package com.ape.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/3
 * 登录注册VO
 */
@Data
public class LoginVO {

    @ApiModelProperty(value = "用户名")
    @Length(min = 11, max = 11, message = "请输入正确手机号码")
    @Pattern(regexp = "^[1][34578]\\d{9}$", message = "请输入正确手机号码")
    private String username;

    @ApiModelProperty(value = "密码")
    @Length(min = 6, max = 20, message = "密码长度介于6~20之间")
    private String password;
}
