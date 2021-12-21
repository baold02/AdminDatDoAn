package com.nhomduan.quanlydathang_admin.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonHang implements Serializable {
    private String id;
    private int tong_tien;
    private String user_id;
    private String ho_ten;
    private String dia_chi;
    private List<DonHangChiTiet> don_hang_chi_tiets;
    private Shipper shipper;
    private String ghi_chu;
    private String trang_thai;
    private String thoiGianDatHang;
    private long thoiGianGiaoHang;
    private String sdt;
    private String thong_tin_huy_don;
    private long thoiGianGiaoHangDuKien;


    public DonHang() {
    }

    public long getThoiGianGiaoHangDuKien() {
        return thoiGianGiaoHangDuKien;
    }

    public void setThoiGianGiaoHangDuKien(long thoiGianGiaoHangDuKien) {
        this.thoiGianGiaoHangDuKien = thoiGianGiaoHangDuKien;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getThoiGianDatHang() {
        return thoiGianDatHang;
    }

    public void setThoiGianDatHang(String thoiGianDatHang) {
        this.thoiGianDatHang = thoiGianDatHang;
    }

    public long getThoiGianGiaoHang() {
        return thoiGianGiaoHang;
    }

    public void setThoiGianGiaoHang(long thoiGianGiaoHang) {
        this.thoiGianGiaoHang = thoiGianGiaoHang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(int tong_tien) {
        this.tong_tien = tong_tien;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }


    public List<DonHangChiTiet> getDon_hang_chi_tiets() {
        return don_hang_chi_tiets;
    }

    public void setDon_hang_chi_tiets(List<DonHangChiTiet> don_hang_chi_tiets) {
        this.don_hang_chi_tiets = don_hang_chi_tiets;
    }

    public Shipper getShipper() {
        return shipper;
    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public String getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(String trang_thai) {
        this.trang_thai = trang_thai;
    }

    public String getThong_tin_huy_don() {
        return thong_tin_huy_don;
    }

    public void setThong_tin_huy_don(String thong_tin_huy_don) {
        this.thong_tin_huy_don = thong_tin_huy_don;
    }

    public Map<String, Object> toMapShipperAndTrangThai() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("thoiGianGiaoHang", thoiGianGiaoHang);
        map.put("shipper", shipper);
        map.put("trang_thai", trang_thai);
        return map;
    }

    public Map<String, Object> toMapHuySPTrongDon() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("don_hang_chi_tiets", don_hang_chi_tiets);
        map.put("thong_tin_huy_don", thong_tin_huy_don);
        map.put("tong_tien", tong_tien);
        map.put("thoiGianGiaoHangDuKien", thoiGianGiaoHangDuKien);
        return map;
    }

    public Map<String, Object> toMapHuyDon() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("thong_tin_huy_don", thong_tin_huy_don);
        map.put("trang_thai", trang_thai);
        return map;
    }
}
