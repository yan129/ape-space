package com.ape.user.bo;

import com.ape.user.model.RoleDO;
import com.ape.user.model.UserDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: Yan
 * @date: 2021/6/2
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserBO业务对象")
public class UserBO extends UserDO implements UserDetails {

    @ApiModelProperty(value = "用户拥有角色")
    private List<RoleDO> roles;

    /**
     * 获取当前用户对象所具有的角色信息
     * @return
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = CollectionHelper.newArrayList(roles.size());
        for (RoleDO role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.getNotExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.getAccountNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.getCredentialsNotExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.getAvailable();
    }

}
