package com.ape.article.service.impl;

import com.ape.article.model.FollowDO;
import com.ape.article.mapper.FollowMapper;
import com.ape.article.service.FollowService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户关注表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-05-26
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, FollowDO> implements FollowService {

    @Override
    public List<Map<String, Object>> searchAllFollowByUid(long pageNum, long pageSize, String uid) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        Page<Map<String, Object>> follows = baseMapper.searchAllFollowByUid(page, uid);
        return follows.getRecords();
    }

    @Override
    public Map<String, Object> countFollowAndFans(String uid) {
        QueryWrapper<FollowDO> followWrapper = new QueryWrapper<>();
        followWrapper.eq("current_uid", uid);

        QueryWrapper<FollowDO> fansWrapper = new QueryWrapper<>();
        fansWrapper.eq("followed_uid", uid);

        int follow = this.count(followWrapper);
        int fans = this.count(fansWrapper);

        Map<String, Object> count = CollectionHelper.newHashMap();
        count.put("follow", follow);
        count.put("fans", fans);
        return count;
    }
}
