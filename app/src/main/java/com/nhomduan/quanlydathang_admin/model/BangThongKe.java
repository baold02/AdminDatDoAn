package com.nhomduan.quanlydathang_admin.model;

import java.util.List;

public class BangThongKe {
    private String ngayBD;
    private String ngayKT;
    private List<DonHang> donHangList;

    public BangThongKe(String ngayBD, String ngayKT, List<DonHang> donHangList) {
        this.ngayBD = ngayBD;
        this.ngayKT = ngayKT;
        this.donHangList = donHangList;
    }

    public String getNgayBD() {
        return ngayBD;
    }

    public void setNgayBD(String ngayBD) {
        this.ngayBD = ngayBD;
    }

    public String getNgayKT() {
        return ngayKT;
    }

    public void setNgayKT(String ngayKT) {
        this.ngayKT = ngayKT;
    }

    public List<DonHang> getDonHangList() {
        return donHangList;
    }

    public void setDonHangList(List<DonHang> donHangList) {
        this.donHangList = donHangList;
    }
}
