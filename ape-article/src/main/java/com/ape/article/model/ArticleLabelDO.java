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
 * 文章标签关联表
 * </p>
 *
 * @author Yan
 * @since 2021-05-19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("article_label")
@ApiModel(value="ArticleLabelDO对象", description="文章标签关联表")
public class ArticleLabelDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    private String aid;

    @ApiModelProperty(value = "标签ID")
    private String lid;


}
