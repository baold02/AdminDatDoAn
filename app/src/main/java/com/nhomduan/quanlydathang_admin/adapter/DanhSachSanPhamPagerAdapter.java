package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nhomduan.quanlydathang_admin.fragment.DanhSachSanPhamByLoaiSPFragment;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.util.List;

public class DanhSachSanPhamPagerAdapter extends FragmentStateAdapter {
    private List<LoaiSP> loaiSPList;

    public DanhSachSanPhamPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<LoaiSP> loaiSPList) {
        super(fragmentManager, lifecycle);
        this.loaiSPList = loaiSPList;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<LoaiSP> loaiSPList) {
        this.loaiSPList = loaiSPList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new DanhSachSanPhamByLoaiSPFragment(loaiSPList.get(position));
    }

    @Override
    public int getItemCount() {
        if(loaiSPList != null) {
            return loaiSPList.size();
        }
        return 0;
    }
}
