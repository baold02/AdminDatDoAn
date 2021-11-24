package com.nhomduan.quanlydathang_admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhomduan.quanlydathang_admin.R;


public class SanPhamFragment extends Fragment {

    private FragmentManager mFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_san_pham, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentManager = getChildFragmentManager();
        setUpFragmentManager();

    }

    private void setUpFragmentManager() {
        mFragmentManager.beginTransaction()
                .replace(R.id.sanPhamContainer, new DanhSachSanPhamFragment(), null)
                .commit();
    }
}