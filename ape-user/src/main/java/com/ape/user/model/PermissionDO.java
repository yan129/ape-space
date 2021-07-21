package com.ape.user.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ape.common.model.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author Yan
 * @since 2021-07-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_permission")
@ApiModel(value="PermissionDO对象", description="权限表")
public class PermissionDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限名称")
    private String name;

    @ApiModelProperty(value = "权限请求路径")
    private String url;

    @ApiModelProperty(value = "描述")
    private String description;


}
