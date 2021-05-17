package com.ape.article.factory;


import com.ape.article.filter.html.ArticleHtmlFilter;
import com.ape.common.model.BaseEntity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/16
 */
public class HtmlFilterFactory {
    public static <T extends BaseEntity> T buildModel(Class<?> clazz, String json) {
        if (clazz.getName().contains("ArticleDO")){
            return (T) new ArticleHtmlFilter().filter(clazz, json);
        }
        return (T) new BaseEntity();
    }
}
