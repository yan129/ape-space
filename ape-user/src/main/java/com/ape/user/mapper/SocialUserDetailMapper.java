package com.ape.user.mapper;

import com.ape.user.model.SocialUserDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * 第三方登录用户表 Mapper 接口
 * </p>
 *
 * @author Yan
 * @since 2021-08-06
 */
public interface SocialUserDetailMapper extends BaseMapper<SocialUserDetailDO> {

    /**
     * 查找oauth_client_details表信息
     * @param clientId
     * @return
     */
    Map<String, Object> selectOauthClientDetailsByClientId(String clientId);

}
