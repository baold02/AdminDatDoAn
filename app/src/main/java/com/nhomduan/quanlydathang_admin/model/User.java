package com.nhomduan.quanlydathang_admin.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String password;
    private String phone_number;
    private boolean enable;
    private String hinhanh;

    private List<String> ma_sp_da_thich;
    private List<GioHang> gio_hang;
    private String address;
    private String name;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String phone_number, boolean enable) {
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.enable = enable;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public List<String> getMa_sp_da_thich() {
        return ma_sp_da_thich;
    }

    public void setMa_sp_da_thich(List<String> ma_sp_da_thich) {
        this.ma_sp_da_thich = ma_sp_da_thich;
    }

    public List<GioHang> getGio_hang() {
        return gio_hang;
    }

    public void setGio_hang(List<GioHang> gio_hang) {
        this.gio_hang = gio_hang;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("password", password);
        map.put("enable", enable);
        map.put("phone_number", phone_number);
        return map;
    }

    public Map<String, Object> toMapThongTinGiaoHang() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("phone_number", phone_number);
        map.put("address", address);
        map.put("name", name);
        return map;
    }

    public Map<String, Object> toMapGioHang() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("gio_hang", gio_hang);
        return map;
    }

    public Map<String, Object> toMapSPDaThich() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("ma_sp_da_thich",ma_sp_da_thich);
        return map;
    }


    public Map<String, Object> toMapLock() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("enable",enable);
        return map;
    }
}

