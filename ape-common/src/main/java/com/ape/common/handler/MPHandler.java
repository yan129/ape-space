package com.ape.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/13
 */
@Component
public class MPHandler implements MetaObjectHandler {

    /**
     * 配置插入数据自动更新时间
     * 1.在实体字段添加 @TableField(fill = FieldFill.INSERT)
     * 2.配置handler
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "gmtCreated", Date.class, new Date());
        this.strictInsertFill(metaObject, "gmtModified", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "gmtModified", Date.class, new Date());
    }
}
