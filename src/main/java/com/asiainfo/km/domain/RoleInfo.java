package com.asiainfo.km.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
public class RoleInfo implements Serializable {

    @Id
    @SequenceGenerator(name = "ROLE_ID",sequenceName = "ROLE_ID_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ROLE_ID")
    private Long roleId;
    private String roleName;
    private String roleCode;
    @ManyToOne
    private UserInfo user;

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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
