package com.ape.user.mapstruct;

import com.ape.user.model.SocialUserDetailDO;
import me.zhyd.oauth.model.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/8/6
 */
@Mapper
public interface SocialUserDetailDOMapper {

    SocialUserDetailDOMapper INSTANCE = Mappers.getMapper(SocialUserDetailDOMapper.class);

//    SocialUserDetailDO authUserToSocialUserDetail(AuthUser authUser);
}
