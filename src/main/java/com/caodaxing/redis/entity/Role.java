package com.caodaxing.redis.entity;

import java.util.Date;

public class Role {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleRemark;

    /**
     * 创建时间
     */
    private Date createDt;

    /**
     * 更新时间
     */
    private Date updateDt;

    /**
     * 是否删除:0,未删除;1已删除
     */
    private Integer del;

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

    public String getRoleRemark() {
        return roleRemark;
    }

    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }
}