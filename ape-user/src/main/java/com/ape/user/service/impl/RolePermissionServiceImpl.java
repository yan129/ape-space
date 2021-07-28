package com.ape.user.service.impl;

import com.ape.common.utils.StringUtils;
import com.ape.user.constant.AuthConstant;
import com.ape.user.model.RolePermissionDO;
import com.ape.user.mapper.RolePermissionMapper;
import com.ape.user.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色权限关联表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-07-19
 */
@Slf4j
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermissionDO> implements RolePermissionService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询所有角色表和权限表关联的数据
     * @return
     */
    @Override
    public List<Map<String, Object>> selectAllLinkedData() {
        return baseMapper.selectAllLinkedData();
    }

    /**
     * 给权限分配角色
     * @param rolePermissionDO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void assignRolePermission(RolePermissionDO rolePermissionDO) {
        boolean result = this.save(rolePermissionDO);
        if (result){
            this.buildResourceRoleMap(this.selectAllLinkedData());
        }
    }

    /**
     * 给权限分配角色
     * @param rolePermissionDataList
     * @return
     */
    public Map<String, List<String>> buildResourceRoleMap(List<Map<String, Object>> rolePermissionDataList) {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        Map<String, List<Map<String, Object>>> groupMapByPermissionUrl = rolePermissionDataList.stream().filter(Objects::nonNull).collect(Collectors.groupingBy(data -> (String) data.get("permissionUrl")));

        for (Map.Entry<String, List<Map<String, Object>>> entry : groupMapByPermissionUrl.entrySet()) {
            List<String> roleNameList = entry.getValue().stream().filter(data -> StringUtils.isNotEmpty(data.get("roleName"))).map(data -> (String) data.get("roleName")).collect(Collectors.toList());
            resourceRolesMap.put(entry.getKey(), roleNameList);
        }

        // 将角色权限对应数据存入redis
//        redisTemplate.delete(AuthConstant.RESOURCE_ROLES_KEY);
//        redisTemplate.opsForHash().putAll(AuthConstant.RESOURCE_ROLES_KEY, resourceRolesMap);
        return resourceRolesMap;
    }

    /**
     * 删除角色权限
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeRolePermission(String id) {
        boolean result = this.removeById(id);
        if (result){
            this.buildResourceRoleMap(this.selectAllLinkedData());
        }
        return result;
    }
}
