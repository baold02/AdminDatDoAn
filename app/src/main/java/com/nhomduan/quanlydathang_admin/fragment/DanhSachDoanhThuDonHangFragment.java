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
import com.nhomduan.quanlydathang_admin.activities.DonHangChiTietActivity;
import com.nhomduan.quanlydathang_admin.adapter.DoanhThuAdapter;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.DonHang;

import java.util.List;

public class DanhSachDoanhThuDonHangFragment extends Fragment implements OnClickItem {

    private Toolbar toolbar;
    private RecyclerView rcvDoanhThuByTime;

    private List<DonHang> donHangList;
    private DoanhThuAdapter doanhThuAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_danh_sach_doanh_thu_don_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getDuLieu();
        setUpToolbar();
        setUpListDoanhThu();
    }

    private void getDuLieu() {
        Bundle bundle = getArguments();
        donHangList = (List<DonHang>) bundle.getSerializable("don_hang");
    }

    private void setUpListDoanhThu() {
        doanhThuAdapter = new DoanhThuAdapter(donHangList, this);
        rcvDoanhThuByTime.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDoanhThuByTime.setAdapter(doanhThuAdapter);
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvDoanhThuByTime = view.findViewById(R.id.rcvDoanhThuByTime);
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

    @Override
    public void onClickItem(Object obj) {
        DonHang donHang = (DonHang) obj;
        Intent intent = new Intent(getContext(), DonHangChiTietActivity.class);
        intent.putExtra("don_hang", donHang);
        startActivity(intent);
    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }
}