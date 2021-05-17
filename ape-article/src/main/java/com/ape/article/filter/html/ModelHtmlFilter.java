package com.ape.article.filter.html;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/16
 */
public interface ModelHtmlFilter<T> {

    T filter(Class<?> clazz, String json);
}
