package com.ape.article.service.impl;

import com.ape.article.model.FavoritesDO;
import com.ape.article.mapper.FavoritesMapper;
import com.ape.article.service.FavoritesService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章收藏表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-05-25
 */
@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesMapper, FavoritesDO> implements FavoritesService {

    @Override
    public List<Map<String, Object>> searchAllFavoritesByUid(long pageNum, long pageSize, String uid) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        Page<Map<String, Object>> favorites = baseMapper.searchAllFavoritesByUid(page, uid);
        return favorites.getRecords();
    }
}
