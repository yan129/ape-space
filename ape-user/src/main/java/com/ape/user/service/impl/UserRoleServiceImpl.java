package com.ape.user.service.impl;

import com.ape.user.model.UserRoleDO;
import com.ape.user.mapper.UserRoleMapper;
import com.ape.user.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-06-03
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleDO> implements UserRoleService {

}
