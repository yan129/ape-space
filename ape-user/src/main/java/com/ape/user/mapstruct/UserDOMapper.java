package com.ape.user.mapstruct;

import com.ape.user.bo.UserBO;
import com.ape.user.model.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/2
 */
@Mapper
public interface UserDOMapper {

    UserDOMapper INSTANCE = Mappers.getMapper(UserDOMapper.class);

    UserBO doToBO(UserDO userDO);
}
