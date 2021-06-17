package com.ape.user.mapper;

import com.ape.user.model.RoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author Yan
 * @since 2021-06-03
 */
public interface RoleMapper extends BaseMapper<RoleDO> {

    /**
     * 根据用户ID查找对应角色
     * @param id
     * @return
     */
    List<RoleDO> searchAllRoleByUid(String id);
}
