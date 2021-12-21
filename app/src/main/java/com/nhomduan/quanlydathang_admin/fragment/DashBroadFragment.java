package com.nhomduan.quanlydathang_admin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;


public class DashBroadFragment extends Fragment {
    private Toolbar toolbar;
    private TextView tvEditModule;
    private TextView tvMainBanner;


    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dash_broad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) requireActivity();
        mainActivity.setSupportActionBar(toolbar);

        tvEditModule.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new CustomModuleListFragment())
                    .addToBackStack(null)
                    .commit();
        });

        tvMainBanner.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new MainBannerFragment())
                    .addToBackStack(null)
                    .commit();
        });

    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvEditModule = view.findViewById(R.id.tvEditModule);
        tvMainBanner = view.findViewById(R.id.tvMainBanner);
    }
}