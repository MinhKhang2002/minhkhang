package com.laptrinhjavaweb.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_role")
@IdClass(UserRoleKey.class)
public class UserRoleEntity implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
