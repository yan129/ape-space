package com.ape.article.mapper;

import com.ape.article.model.FavoritesDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 文章收藏表 Mapper 接口
 * </p>
 *
 * @author Yan
 * @since 2021-05-25
 */
public interface FavoritesMapper extends BaseMapper<FavoritesDO> {

    /**
     * 根据用户ID分页查看收藏文章
     * @param page 分页
     * @param uid 用户ID
     * @return
     */
    Page<Map<String, Object>> searchAllFavoritesByUid(@Param("page") Page<Map<String, Object>> page, @Param("uid") String uid);
}
