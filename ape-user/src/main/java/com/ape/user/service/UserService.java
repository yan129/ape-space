package com.ape.user.service;

import com.ape.user.model.UserDO;
import com.ape.user.vo.LoginVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Yan
 * @since 2021-06-02
 */
public interface UserService extends IService<UserDO> {

    /**
     * 用户注册
     * @param loginVO
     */
    void register(LoginVO loginVO);
}
