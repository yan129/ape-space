package com.ape.article.mapper;

import com.ape.article.model.FollowDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * <p>
 * 用户关注表 Mapper 接口
 * </p>
 *
 * @author Yan
 * @since 2021-05-26
 */
public interface FollowMapper extends BaseMapper<FollowDO> {

    /**
     * 根据用户ID分页查看关注用户
     * @param page 分页
     * @param uid 用户ID
     * @return
     */
    Page<Map<String, Object>> searchAllFollowByUid(Page<Map<String, Object>> page, String uid);
}
