package com.nhomduan.quanlydathang_admin.model;

import java.io.Serializable;

public class GioHang implements Serializable {
    private String ma_sp; // mã sp
    private int so_luong;

    public GioHang(String ma_sp, int so_luong) {
        this.ma_sp = ma_sp;
        this.so_luong = so_luong;
    }


    public GioHang() {
    }

    public String getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(String ma_sp) {
        this.ma_sp = ma_sp;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }
}
