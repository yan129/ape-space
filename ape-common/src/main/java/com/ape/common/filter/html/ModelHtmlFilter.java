package com.ape.common.filter.html;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/16
 */
public interface ModelHtmlFilter<T> {

    /**
     * 过滤HTML标签
     * @param json
     * @return
     */
    T filter(String json);
}
