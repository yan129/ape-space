package com.ape.article.service;

import com.ape.article.model.FollowDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关注表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-05-26
 */
public interface FollowService extends IService<FollowDO> {

    /**
     * 根据用户ID分页查看关注用户
     * @param pageNum
     * @param pageSize
     * @param uid
     * @return
     */
    List<Map<String, Object>> searchAllFollowByUid(long pageNum, long pageSize, String uid);

    /**
     * 统计关注用户总数和粉丝数量
     * @param uid
     * @return
     */
    Map<String, Object> countFollowAndFans(String uid);
}
