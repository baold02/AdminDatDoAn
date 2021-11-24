package com.nhomduan.quanlydathang_admin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;


public class ThongKeFragment extends Fragment {

    private Toolbar toolbar;
    private Button btnChonNgayBD;
    private TextView tvNgayBD;
    private Button btnChonNgayKT;
    private TextView tvNgayKT;
    private TextView tvSoDonHang;
    private TextView tvChiTietDonHang;
    private TextView tvSoDoanhThu;
    private TextView tvChiTietDoanhThu;

    private MainActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thongke, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);


    }


    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        btnChonNgayBD = view.findViewById(R.id.btn_ChonNgayBD);
        tvNgayBD = view.findViewById(R.id.tv_NgayBD);
        btnChonNgayKT = view.findViewById(R.id.btn_ChonNgayKT);
        tvNgayKT = view.findViewById(R.id.tv_NgayKT);
        tvSoDonHang = view.findViewById(R.id.tvSoDonHang);
        tvChiTietDonHang = view.findViewById(R.id.tvChiTietDonHang);
        tvSoDoanhThu = view.findViewById(R.id.tvSoDoanhThu);
        tvChiTietDoanhThu = view.findViewById(R.id.tvChiTietDoanhThu);
    }
}
