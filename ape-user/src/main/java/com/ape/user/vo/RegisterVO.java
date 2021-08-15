package com.ape.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/13
 *
 * 免密注册VO
 */
@Data
public class RegisterVO {

    @ApiModelProperty(value = "用户名")
    @Length(min = 11, max = 11, message = "请输入正确手机号码")
    @Pattern(regexp = "^[1][34578]\\d{9}$", message = "请输入正确手机号码")
    private String username;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码无效")
    private String code;
}
