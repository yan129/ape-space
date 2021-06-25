package com.ape.user.service.impl;

import com.ape.common.exception.ServiceException;
import com.ape.common.utils.StringUtils;
import com.ape.user.bo.UserBO;
import com.ape.user.mapper.RoleMapper;
import com.ape.user.mapper.UserRoleMapper;
import com.ape.user.mapstruct.UserDOMapper;
import com.ape.user.model.RoleDO;
import com.ape.user.model.UserDO;
import com.ape.user.mapper.UserMapper;
import com.ape.user.model.UserRoleDO;
import com.ape.user.service.UserService;
import com.ape.user.vo.LoginVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Yan
 * @since 2021-06-02
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService, UserDetailsService {

    private static final String ACCOUNT_DEFAULT_ROLE = "ROLE_NORMAL";

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDO searchUser = searchUserByUsername(username);

        if (StringUtils.isEmpty(searchUser)){
            throw new UsernameNotFoundException("");
        }

        List<RoleDO> roles = roleMapper.searchAllRoleByUid(searchUser.getId());
        UserBO userBO = UserDOMapper.INSTANCE.doToBO(searchUser);
        userBO.setRoles(roles);
        return userBO;
    }

    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    private UserDO searchUserByUsername(String username) {
        if (StringUtils.isBlank(username)){
            return null;
        }
        QueryWrapper<UserDO> wrapper = new QueryWrapper<UserDO>().eq("username", username.trim());
        return this.baseMapper.selectOne(wrapper);
    }

    /**
     * 用户注册
     * @param loginVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(LoginVO loginVO) {
        // 如果用户名存在，返回错误
        if (StringUtils.isNotEmpty(searchUserByUsername(loginVO.getUsername()))){
            throw new ServiceException();
        }

        StringBuilder sb = new StringBuilder(loginVO.getUsername().trim());
        sb.replace(3, 7, "****");
        String password = passwordEncoder.encode(loginVO.getPassword().trim());

        UserDO userDO = new UserDO();
        userDO.setNickname(sb.toString());
        userDO.setUsername(loginVO.getUsername().trim());
        userDO.setPassword(password);

        this.save(userDO);
        this.assignNormalRole(userDO.getId());
    }

    /**
     * 分配默认的角色
     * @param uid
     */
    private void assignNormalRole(String uid) {
        if (StringUtils.isNotBlank(uid)){
            QueryWrapper<RoleDO> wrapper = new QueryWrapper<>();
            wrapper.eq("role_name", ACCOUNT_DEFAULT_ROLE);
            RoleDO roleDO = roleMapper.selectOne(wrapper);

            if (StringUtils.isEmpty(roleDO)){
                throw new ServiceException();
            }

            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUid(uid);
            userRoleDO.setRid(roleDO.getId());
            userRoleMapper.insert(userRoleDO);
        }
    }
}
