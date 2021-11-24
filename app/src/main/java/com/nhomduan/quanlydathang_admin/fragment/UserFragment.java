package com.nhomduan.quanlydathang_admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.nhomduan.quanlydathang_admin.R;

public class UserFragment extends Fragment {
    private FragmentManager mFragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quanlyuser, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentManager = getParentFragmentManager();

        mFragmentManager.beginTransaction()
                .replace(R.id.nguoiDungContainer, new DanhSachUserFragment())
                .commit();
    }
}
