package com.example.Payments.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_signature")
    private String roleSignature;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleSignature() {
        return roleSignature;
    }

    public void setRoleSignature(String roleSignature) {
        this.roleSignature = roleSignature;
    }

    @Override
    public String toString() {
        return  roleSignature  ;
    }
}
