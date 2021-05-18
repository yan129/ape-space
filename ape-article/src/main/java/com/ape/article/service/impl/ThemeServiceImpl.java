package com.ape.article.service.impl;

import com.ape.article.model.ThemeDO;
import com.ape.article.mapper.ThemeMapper;
import com.ape.article.service.ThemeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专题表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-05-18
 */
@Service
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, ThemeDO> implements ThemeService {

}
