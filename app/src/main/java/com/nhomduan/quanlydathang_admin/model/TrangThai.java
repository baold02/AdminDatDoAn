package com.nhomduan.quanlydathang_admin.model;

import java.util.ArrayList;
import java.util.List;

public enum TrangThai {
    CHUA_XAC_NHAN("Chưa xác nhận"), CHE_BIEN("Chế biến"), DANG_GIAO_HANG("Đang giao hàng"), HOAN_THANH("Hoàn thành"), HUY_DON("Hủy đơn");

    private String trangThai;

    private TrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
