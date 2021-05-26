package com.ape.article.service;

import com.ape.article.model.FavoritesDO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章收藏表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-05-25
 */
public interface FavoritesService extends IService<FavoritesDO> {

    /**
     * 根据用户ID分页查看收藏文章
     * @param pageNum 页数，第几页
     * @param pageSize 每页的数据条数，每页xx条
     * @param uid 用户ID
     * @return
     */
    List<Map<String, Object>> searchAllFavoritesByUid(long pageNum, long pageSize, String uid);
}
