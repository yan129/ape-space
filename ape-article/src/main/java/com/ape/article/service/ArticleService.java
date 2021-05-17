package com.ape.article.service;

import com.ape.article.model.ArticleDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 文章表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-05-15
 */
public interface ArticleService extends IService<ArticleDO> {

    /**
     * 新建空文章
     * @param uid
     * @return
     */
    ArticleDO create(String uid);

    /**
     * 发布文章
     * @param articleDO
     * @return
     */
    boolean publish(ArticleDO articleDO);
}
