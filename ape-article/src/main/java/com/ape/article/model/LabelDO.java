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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 标签表
 * </p>
 *
 * @author Yan
 * @since 2021-05-19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("label")
@ApiModel(value="LabelDO对象", description="标签表")
public class LabelDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "标签名称")
    @NotBlank(message = "标签名称不能为空")
    @Length(max = 16, message = "标签名称不能超过16个字符")
    private String name;


}
