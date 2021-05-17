package com.ape.article.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ape.article.model.ArticleDO;
import com.ape.article.mapper.ArticleMapper;
import com.ape.article.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 文章表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-05-15
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, ArticleDO> implements ArticleService {

    @Override
    public ArticleDO create(String uid) {
        if (StringUtils.isBlank(uid)){
            return null;
        }

        ArticleDO articleDO = new ArticleDO();
        articleDO.setUid(uid);
        articleDO.setTitle(DateUtil.format(new Date(), "yyyy-MM-dd"));

        this.save(articleDO);
        return articleDO;
    }

    @Override
    public boolean publish(ArticleDO articleDO) {
        articleDO.setIsPublish(true);

        if (StringUtils.isBlank(articleDO.getSummary())){
            if (articleDO.getContent().length() <= 100){
                articleDO.setSummary(articleDO.getContent());
            }
            articleDO.setSummary(articleDO.getContent().substring(0, 100));
        }

        QueryWrapper<ArticleDO> wrapper = new QueryWrapper<>();
        wrapper.eq("id", articleDO.getId());
        wrapper.eq("uid", articleDO.getUid());
        return this.update(articleDO, wrapper);
    }

}
