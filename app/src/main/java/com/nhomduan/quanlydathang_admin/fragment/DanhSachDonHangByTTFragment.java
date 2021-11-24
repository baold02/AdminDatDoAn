package com.nhomduan.quanlydathang_admin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.DonHangUtils;
import com.nhomduan.quanlydathang_admin.activities.DonHangChiTietActivity;
import com.nhomduan.quanlydathang_admin.adapter.DonHangAdapter;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.TrangThai;

import java.util.ArrayList;
import java.util.List;


public class DanhSachDonHangByTTFragment extends Fragment implements OnClickItem {
    private final TrangThai trangThai;

    private RecyclerView rcvDanhSachDonHang;
    private List<DonHang> donHangList;
    private DonHangAdapter donHangAdapter;

    public DanhSachDonHangByTTFragment(TrangThai trangThai) {
        this.trangThai = trangThai;
    }


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
        setUpListDonHang();
    }

    private void initView(View view) {
        rcvDanhSachDonHang = view.findViewById(R.id.rcvDonHang);
    }

    private void setUpListDonHang() {
        donHangList = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(donHangList, this);
        rcvDanhSachDonHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDanhSachDonHang.setHasFixedSize(true);
        rcvDanhSachDonHang.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvDanhSachDonHang.setAdapter(donHangAdapter);

        Query query = DonHangUtils.getDbRfDonHang().orderByChild("trang_thai").equalTo(this.trangThai.getTrangThai());
        query.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                if(donHang != null) {
                    donHangList.add(donHang);
                    donHangAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                if(donHang == null || donHangList == null || donHangList.isEmpty()) {
                    return;
                }
                int viTri = -1;
                for(int i = 0; i < donHangList.size(); i++) {
                    if(donHangList.get(i).getId().equals(donHang.getId())) {
                        viTri = i;
                    }
                }
                donHangList.set(viTri, donHang);
                donHangAdapter.notifyDataSetChanged();
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DonHang donHang = snapshot.getValue(DonHang.class);
                if(donHang == null || donHangList == null || donHangList.isEmpty()) {
                    return;
                }
                int viTri = -1;
                for(int i = 0; i < donHangList.size(); i++) {
                    if(donHangList.get(i).getId().equals(donHang.getId())) {
                        viTri = i;
                    }
                }
                donHangList.remove(viTri);
                donHangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClickItem(Object obj) {
        Intent intent = new Intent(getContext(), DonHangChiTietActivity.class);
        intent.putExtra("don_hang", (DonHang) obj);
        startActivity(intent);
    }

    @Override
    public void onUpdateItem(Object obj) {}
    @Override
    public void onDeleteItem(Object obj) {}
}