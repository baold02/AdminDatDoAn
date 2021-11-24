package com.nhomduan.quanlydathang_admin.model;

import java.io.Serializable;

public class DonHangChiTiet implements Serializable {
    private Product product;
    private int so_luong;

    public DonHangChiTiet(Product product, int so_luong) {
        this.product = product;
        this.so_luong = so_luong;
    }

    public DonHangChiTiet() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }
}
