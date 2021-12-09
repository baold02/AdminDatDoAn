package com.nhomduan.quanlydathang_admin.model;

import java.util.HashMap;
import java.util.Map;

public class Admin {
    private String userName;
    private String password;
    private Permission permission;

    public Admin() {
    }

    public Admin(String userName, String password, Permission permission) {
        this.userName = userName;
        this.password = password;
        this.permission = permission;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("password", password);
        map.put("permission", permission);
        return map;
    }
}
