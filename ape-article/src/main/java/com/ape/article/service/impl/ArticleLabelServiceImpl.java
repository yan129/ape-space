package com.ape.article.service.impl;

import com.ape.article.model.ArticleLabelDO;
import com.ape.article.mapper.ArticleLabelMapper;
import com.ape.article.service.ArticleLabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章标签关联表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-05-19
 */
@Service
public class ArticleLabelServiceImpl extends ServiceImpl<ArticleLabelMapper, ArticleLabelDO> implements ArticleLabelService {

}
