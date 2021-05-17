package com.ape.article.filter.html;

import cn.hutool.json.JSONUtil;
import com.ape.article.model.ArticleDO;
import org.springframework.web.util.HtmlUtils;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/5/16
 */
public class ArticleHtmlFilter implements ModelHtmlFilter<ArticleDO> {

    @Override
    public ArticleDO filter(Class<?> clazz, String json) {
        ArticleDO articleDO = JSONUtil.toBean(json, ArticleDO.class);
        articleDO.setTitle(HtmlUtils.htmlEscape(articleDO.getTitle()));
        articleDO.setSummary(HtmlUtils.htmlEscape(articleDO.getSummary()));
        articleDO.setContent(HtmlUtils.htmlEscape(articleDO.getContent()));
        return articleDO;
    }
}
