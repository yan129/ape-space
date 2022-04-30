package com.ape.common.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2022/4/23
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfoWrapper {

    private UserInfo userInfo;
    private String userName;
    private List<String> scope;
    private long exp;
    private List<String> authorities;
    private String jti;
    private String clientId;

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }
    public List<String> getScope() {
        return scope;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
    public long getExp() {
        return exp;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
    public List<String> getAuthorities() {
        return authorities;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }
    public String getJti() {
        return jti;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}