package com.ape.user.service.impl;

import com.ape.user.model.RolePermissionDO;
import com.ape.user.mapper.RolePermissionMapper;
import com.ape.user.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-07-19
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionDO> implements RolePermissionService {

    /**
     * 查询所有角色表和权限表关联的数据
     * @return
     */
    @Override
    public List<Map<String, Object>> selectAllLinkedData() {
        return baseMapper.selectAllLinkedData();
    }
}
