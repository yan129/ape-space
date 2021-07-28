package com.ape.common.factory;

import com.ape.common.model.BaseEntity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/7/28
 */
public interface HtmlFilterSuperFactory {

    /**
     * HTML标签过滤工厂父类接口
     * @param clazz
     * @param json
     * @param <T>
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    <T extends BaseEntity> T buildModel(Class<?> clazz, String json) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException;

}
