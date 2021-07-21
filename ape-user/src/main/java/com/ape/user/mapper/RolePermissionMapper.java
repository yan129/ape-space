package com.ape.user.mapper;

import com.ape.user.model.RolePermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色权限关联表 Mapper 接口
 * </p>
 *
 * @author Yan
 * @since 2021-07-19
 */
public interface RolePermissionMapper extends BaseMapper<RolePermissionDO> {

    /**
     * 查询所有角色表和权限表关联的数据
     * @return
     */
    List<Map<String, Object>> selectAllLinkedData();

}
