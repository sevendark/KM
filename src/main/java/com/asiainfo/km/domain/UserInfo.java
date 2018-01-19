package com.asiainfo.km.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class UserInfo implements UserDetails, Serializable {
    @Id
    @SequenceGenerator(name = "USER_ID",sequenceName = "USER_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_ID")
    private Long userId;
    private String password;
    private String username;
    private Boolean accountNonExpired;      // 账号是否未过期
    private Boolean accountNonLocked;       // 账号是否未锁定
    private Boolean credentialsNonExpired;  // 账号凭证是否未过期
    private Boolean enabled;
    @OneToMany(fetch = FetchType.EAGER)
    private List<RoleInfo> roleList = new ArrayList<>();

    private Timestamp createTime;
    private Boolean delFlag;


    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roleList == null || roleList.size() < 1){
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        StringBuilder sb = new StringBuilder();
        for(RoleInfo role : roleList){
            sb.append(role.getRoleCode()).append(",");
        }
        String authorities = sb.substring(0,sb.length()-1);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<RoleInfo> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleInfo> roleList) {
        this.roleList = roleList;
    }
}
