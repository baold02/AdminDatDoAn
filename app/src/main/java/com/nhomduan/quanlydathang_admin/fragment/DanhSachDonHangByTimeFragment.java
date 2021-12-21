package com.nhomduan.quanlydathang_admin.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.adapter.DonHangAdapter;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.DonHang;

import java.util.List;


public class DanhSachDonHangByTimeFragment extends Fragment implements OnClickItem {
    private Toolbar toolbar;
    private RecyclerView rcvDonHanByTime;

    private List<DonHang> donHangList;
    private DonHangAdapter donHangAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_don_hang_thong_ke_by_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getDuLieu();
        setUpToolbar();
        setUpDonHangList();
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void getDuLieu() {
        Bundle bundle = getArguments();
        donHangList = (List<DonHang>) bundle.get("don_hang");
    }

    private void setUpDonHangList() {
        donHangAdapter = new DonHangAdapter(donHangList, this);
        rcvDonHanByTime.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDonHanByTime.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvDonHanByTime.setAdapter(donHangAdapter);
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvDonHanByTime = view.findViewById(R.id.rcvDonHanByTime);
    }

    @Override
    public void onClickItem(Object obj) {
        Fragment fragment = new ChiTietDonHangFragment();
        Bundle args = new Bundle();
        args.putString("ma_don_hang", String.valueOf(obj));
        fragment.setArguments(args);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                    return true;
                }
                return false;
            }
        });

    }
}