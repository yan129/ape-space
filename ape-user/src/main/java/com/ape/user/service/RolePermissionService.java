package com.ape.user.service;

import com.ape.user.model.RolePermissionDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色权限关联表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-07-19
 */
public interface RolePermissionService extends IService<RolePermissionDO> {

    /**
     * 查询所有角色表和权限表关联的数据
     * @return
     */
    List<Map<String, Object>> selectAllLinkedData();

}
