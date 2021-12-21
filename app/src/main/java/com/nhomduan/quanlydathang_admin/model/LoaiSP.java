package com.nhomduan.quanlydathang_admin.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LoaiSP implements Serializable {

    private String id;
    private String name;
    private String hinhanh;
    private int soSanPhamThuocLoai;

    public LoaiSP() {
    }


    public LoaiSP(String id, String name, String hinhanh, int soSanPhamThuocLoai) {
        this.id = id;
        this.name = name;
        this.hinhanh = hinhanh;
        this.soSanPhamThuocLoai = soSanPhamThuocLoai;
    }

    public int getSoSanPhamThuocLoai() {
        return soSanPhamThuocLoai;
    }

    public void setSoSanPhamThuocLoai(int soSanPhamThuocLoai) {
        this.soSanPhamThuocLoai = soSanPhamThuocLoai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("hinhanh", hinhanh);
        return map;
    }

    public Map<String, Object> toMapSoLuongSanPham() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("soSanPhamThuocLoai", soSanPhamThuocLoai);
        return map;
    }
}
