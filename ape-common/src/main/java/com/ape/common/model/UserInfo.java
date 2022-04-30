package com.ape.common.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2022/4/23
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {

    private String lastLoginTime;
    private int gender;
    private String themeColor;
    private String profile;
    private List<String> roles;
    private String nickname;
    private String remark;
    private String id;
    private String avatar;

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
    public int getGender() {
        return gender;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }
    public String getThemeColor() {
        return themeColor;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getProfile() {
        return profile;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public List<String> getRoles() {
        return roles;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getRemark() {
        return remark;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

}