package com.nhomduan.quanlydathang_admin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.adapter.DonHangAdapter;
import com.nhomduan.quanlydathang_admin.dao.OrderDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.TrangThai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DanhSachDonHangByTTFragment extends Fragment implements OnClickItem {
    private TrangThai trangThai;
    private RecyclerView rcvDanhSachDonHang;
    private List<DonHang> donHangList;
    private DonHangAdapter donHangAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_danh_sach_don_hang_by_t_t, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        getData();
        setUpListDonHang();
    }

    private void initView(View view) {
        rcvDanhSachDonHang = view.findViewById(R.id.rcvDonHang);
    }

    private void getData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            trangThai = (TrangThai) bundle.getSerializable("trang_thai");
        }
    }



    private void setUpListDonHang() {
        donHangList = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(donHangList, this);
        rcvDanhSachDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDanhSachDonHang.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        rcvDanhSachDonHang.setAdapter(donHangAdapter);

        OrderDao.getInstance().getDonHangByTrangThai(trangThai, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                donHangList = (List<DonHang>) obj;
                Collections.reverse(donHangList);
                donHangAdapter.setData(donHangList);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
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
    public void onUpdateItem(Object obj) {}
    @Override
    public void onDeleteItem(Object obj) {}
}