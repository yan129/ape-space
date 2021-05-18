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

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 专题表
 * </p>
 *
 * @author Yan
 * @since 2021-05-18
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("theme")
@ApiModel(value="ThemeDO对象", description="专题表")
public class ThemeDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "专题名称")
    @NotBlank(message = "专题名称不能为空")
    private String name;

    @ApiModelProperty(value = "专题封面图")
    private String picture;


}
