package com.nhomduan.quanlydathang_admin.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nhomduan.quanlydathang_admin.fragment.DanhSachSanPhamByLoaiSPFragment;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.io.Serializable;
import java.util.List;

public class DanhSachSanPhamPagerAdapter extends FragmentStateAdapter {
    private final List<LoaiSP> loaiSPList;

    public DanhSachSanPhamPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<LoaiSP> loaiSPList) {
        super(fragmentActivity);
        this.loaiSPList = loaiSPList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new DanhSachSanPhamByLoaiSPFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("loai_sp", (Serializable) loaiSPList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        if(loaiSPList != null) {
            return loaiSPList.size();
        }
        return 0;
    }
}
