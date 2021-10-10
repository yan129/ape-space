package com.ape.user.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import com.ape.common.exception.ServiceException;
import com.ape.common.model.ResponseCode;
import com.ape.common.utils.CaptchaUtil;
import com.ape.common.utils.CommonUtil;
import com.ape.common.utils.StringUtils;
import com.ape.user.bo.UserBO;
import com.ape.user.mapper.RoleMapper;
import com.ape.user.mapper.UserRoleMapper;
import com.ape.user.mapstruct.UserDOMapper;
import com.ape.user.model.RoleDO;
import com.ape.user.model.UserDO;
import com.ape.user.mapper.UserMapper;
import com.ape.user.model.UserRoleDO;
import com.ape.user.service.SocialUserDetailService;
import com.ape.user.service.UserService;
import com.ape.user.social.SocialService;
import com.ape.user.vo.LoginVO;
import com.ape.user.vo.RegisterVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SocialUserDetailService socialUserDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 因为设计上该用户表保存了第三方登录账号，所以前端输入框要限制能使用第三方账号的用户名密码登录，要判断是不是手机号登录 --> 这里放在了 SmsCodeAuthenticationProvider 类校验
//        if (!CommonUtil.telephoneRegex(username)) {
//            throw new UsernameNotFoundException(ResponseCode.USERNAME_NOT_EXIST.getMsg());
//        }
        UserDO searchUser = searchUserByUsername(username);
        // 账号不存在
        if (StringUtils.isEmpty(searchUser)){
            throw new UsernameNotFoundException(ResponseCode.USERNAME_NOT_EXIST.getMsg());
        }

        UserBO userBO = UserDOMapper.INSTANCE.doToBO(searchUser);
        loginException(userBO);
        List<RoleDO> roles = roleMapper.searchAllRoleByUid(searchUser.getId());
        userBO.setRoles(roles);
        return userBO;
    }

    public void loginException(UserBO searchUser){
        // 账号被禁用
        if (!searchUser.isEnabled()){
            throw new DisabledException(ResponseCode.USER_ACCOUNT_DISABLE.getMsg());
        }
        // 账号被锁定
        if (!searchUser.isAccountNonLocked()){
            throw new LockedException(ResponseCode.USER_ACCOUNT_LOCKED.getMsg());
        }

        // 账号过期
        if (!searchUser.isAccountNonExpired()){
            throw new AccountExpiredException(ResponseCode.USER_ACCOUNT_EXPIRED.getMsg());
        }

        // 密码过期
        if (!searchUser.isCredentialsNonExpired()){
            throw new CredentialsExpiredException(ResponseCode.USER_CREDENTIALS_EXPIRED.getMsg());
        }
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
    public OAuth2AccessToken register(LoginVO loginVO) {
        // 如果用户名存在，返回错误
        if (StringUtils.isNotEmpty(searchUserByUsername(loginVO.getUsername()))){
            throw new ServiceException(ResponseCode.USER_ACCOUNT_ALREADY_EXIST.getMsg());
        }

        // 隐藏手机号位数 180****1999
        String nickname = DesensitizedUtil.mobilePhone(loginVO.getUsername().trim());
        String password = passwordEncoder.encode(loginVO.getPassword().trim());

        UserDO userDO = new UserDO();
        userDO.setNickname(nickname);
        userDO.setUsername(loginVO.getUsername().trim());
        userDO.setPassword(password);

        this.save(userDO);
        this.assignNormalRole(userDO.getId());

        UserBO userBO = UserDOMapper.INSTANCE.doToBO(userDO);
        userBO.setRoles(SocialService.buildRoleList());

        // 注册成功进行登录
        return socialUserDetailService.generateToken(userBO);
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDO registerSocialUser(AuthUser authUser) {
        UserDO userDO = new UserDO();
        userDO.setUsername(authUser.getUuid());
        userDO.setNickname(authUser.getUsername());
        userDO.setPassword(passwordEncoder.encode(ACCOUNT_DEFAULT_PASSWORD));
        userDO.setAvatar(authUser.getAvatar());
        userDO.setGender(Integer.parseInt(authUser.getGender().getCode()));
        userDO.setRemark(authUser.getRemark());
        this.save(userDO);
        this.assignNormalRole(userDO.getId());
        return userDO;
    }

    /**
     * 免密注册
     * @param registerVO
     */
    @Override
    public OAuth2AccessToken noSecretRegister(RegisterVO registerVO) {
        String key = PHONE_PREFIX + registerVO.getUsername().trim();
        String registerCode = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(registerCode)){
            throw new ServiceException(ResponseCode.REGISTER_CODE_EXPIRED.getMsg());
        }
        if (!StringUtils.equals(registerVO.getCode(), registerCode)){
            throw new ServiceException(ResponseCode.REGISTER_CHECK_CODE.getMsg());
        }

        LoginVO loginVO = new LoginVO();
        BeanUtils.copyProperties(registerVO, loginVO);
        loginVO.setPassword(ACCOUNT_DEFAULT_PASSWORD);

        OAuth2AccessToken oAuth2AccessToken = register(loginVO);
        stringRedisTemplate.delete(key);

        return oAuth2AccessToken;
    }

    /**
     * 获取图形验证码
     * @return
     */
    @Override
    public Map<String, Object> getCaptchaImg() throws IOException {
        String uuid = IdUtil.fastSimpleUUID();
        CaptchaUtil captchaUtil = CaptchaUtil.newInstance();
        BufferedImage image = captchaUtil.getImage();
        String text = captchaUtil.getText();
        stringRedisTemplate.opsForValue().set(CaptchaUtil.PREFIX + uuid, text, CaptchaUtil.EXPIRE_TIME, TimeUnit.SECONDS);
        Map<String, Object> map = CollectionHelper.newHashMap();
        map.put("uuid", uuid);
        map.put("img", captchaUtil.output(image));
        return map;
    }
}
