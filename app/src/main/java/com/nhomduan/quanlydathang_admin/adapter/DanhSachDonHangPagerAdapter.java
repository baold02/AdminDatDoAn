package com.nhomduan.quanlydathang_admin.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.nhomduan.quanlydathang_admin.fragment.DanhSachDonHangByTTFragment;
import com.nhomduan.quanlydathang_admin.model.TrangThai;

import java.util.List;

public class DanhSachDonHangPagerAdapter extends FragmentStateAdapter {

    TrangThai[] trangThais = TrangThai.values();

    public DanhSachDonHangPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new DanhSachDonHangByTTFragment(trangThais[position]);
    }

    @Override
    public int getItemCount() {
        return trangThais.length;
    }
}
