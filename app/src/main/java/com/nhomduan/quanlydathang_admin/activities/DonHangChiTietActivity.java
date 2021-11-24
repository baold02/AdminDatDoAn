package com.nhomduan.quanlydathang_admin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.DonHangUtils;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.Utils.ShipperUtils;
import com.nhomduan.quanlydathang_admin.adapter.DonHangChiTietAdapter;
import com.nhomduan.quanlydathang_admin.adapter.ShipperSpinnerAdapter;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;
import com.nhomduan.quanlydathang_admin.model.Shipper;
import com.nhomduan.quanlydathang_admin.model.TrangThai;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DonHangChiTietActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvHoTen;
    private TextView tvDiaChi;
    private TextView tvSDT;
    private RecyclerView rcvDonHangChiTiet;
    private Spinner spnTrangThai;
    private Spinner spnShipper;
    private TextView tvTongTien;
    private Button btnXacNhan;
    private TextView tvTGDatHang;

    private List<DonHangChiTiet> donHangChiTietList;
    private DonHangChiTietAdapter donHangChiTietAdapter;

    private List<Shipper> shipperList;
    private ShipperSpinnerAdapter shipperSpinnerAdapter;

    private static DonHang donHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang_chi_tiet);
        initView();
        getDuLieu();
        setUpComponents();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DonHangChiTietActivity.this.onBackPressed();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrangThai trangThai = (TrangThai) spnTrangThai.getSelectedItem();
                Shipper shipper = (Shipper) spnShipper.getSelectedItem();
                if (trangThai.equals(TrangThai.DGH) || trangThai.equals(TrangThai.HT)) {
                    if (shipper.getId().equals("")) {
                        OverUtils.makeToast(DonHangChiTietActivity.this, "Vui lòng chọn shipper");
                        return;
                    }
                }
                if(trangThai.equals(TrangThai.HT)) {
                    donHang.setThoiGianGiaoHang(OverUtils.simpleDateFormat.format(new Date(System.currentTimeMillis())));
                }
                donHang.setTrang_thai(trangThai.getTrangThai());
                donHang.setShipper(shipper);
                DonHangUtils.getDbRfDonHang().child(donHang.getId()).setValue(donHang, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            OverUtils.makeToast(DonHangChiTietActivity.this, "Xác nhận thành công");
                            DonHangUtils.getDbRfDonHang().child(donHang.getId())
                                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        DonHang donHang = task.getResult().getValue(DonHang.class);
                                        DonHangChiTietActivity.donHang = donHang;
                                        if (donHang.getTrang_thai().equals(TrangThai.HT.getTrangThai())) {
                                            btnXacNhan.setEnabled(false);
                                            spnShipper.setEnabled(false);
                                            spnTrangThai.setEnabled(false);
                                        }
                                        setUpTrangThai();
                                    }
                                }
                            });
                        }
                    }
                });


            }
        });

    }

    private void setUpComponents() {
        setUpDonHangChiTietList();
        tvHoTen.setText(donHang.getHo_ten());
        tvDiaChi.setText(donHang.getDia_chi());
        tvSDT.setText(donHang.getSdt());
        tvTongTien.setText(OverUtils.currencyFormat.format(donHang.getTong_tien()));
        tvTGDatHang.setText(donHang.getThoiGianDatHang());
        setUpTrangThai();

        ShipperUtils.getDbRfShipper().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shipperList = ShipperUtils.getAllShipper(snapshot);
                shipperSpinnerAdapter = new ShipperSpinnerAdapter(DonHangChiTietActivity.this, shipperList);
                spnShipper.setAdapter(shipperSpinnerAdapter);

                int viTri = 0;
                if (donHang.getShipper() != null) {
                    for (int i = 0; i < shipperList.size(); i++) {
                        if (shipperList.get(i).getId().equals(donHang.getShipper().getId())) {
                            viTri = i;
                        }

                    }
                }
                spnShipper.setSelection(viTri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpTrangThai() {
        List<TrangThai> trangThais = new ArrayList<>();
        for (int i = 0; i < TrangThai.values().length; i++) {
            trangThais.add(TrangThai.values()[i]);
        }

        if (donHang.getTrang_thai().equals(TrangThai.CB.getTrangThai())) {
            trangThais.remove(TrangThai.CXN);
        } else if (donHang.getTrang_thai().equals(TrangThai.DGH.getTrangThai())) {
            trangThais.remove(TrangThai.CXN);
            trangThais.remove(TrangThai.CB);
        } else if (donHang.getTrang_thai().equals(TrangThai.HT.getTrangThai())) {
            trangThais.remove(TrangThai.CXN);
            trangThais.remove(TrangThai.CB);
            trangThais.remove(TrangThai.DGH);
            btnXacNhan.setEnabled(false);
            spnShipper.setEnabled(false);
            spnTrangThai.setEnabled(false);
        }

        spnTrangThai.setAdapter(new ArrayAdapter<TrangThai>(DonHangChiTietActivity.this, android.R.layout.simple_spinner_dropdown_item, trangThais));
        int viTri = 0;
        for (int i = 0; i < trangThais.size(); i++) {
            if (donHang.getTrang_thai().equals(trangThais.get(i).getTrangThai())) {
                viTri = i;
            }
        }
        spnTrangThai.setSelection(viTri);
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        if (intent != null) {
            donHang = (DonHang) intent.getSerializableExtra("don_hang");
        }
    }

    private void setUpDonHangChiTietList() {
        donHangChiTietList = donHang.getDon_hang_chi_tiets();
        donHangChiTietAdapter = new DonHangChiTietAdapter(donHangChiTietList);
        rcvDonHangChiTiet.setLayoutManager(new LinearLayoutManager(DonHangChiTietActivity.this));
        rcvDonHangChiTiet.addItemDecoration(new DividerItemDecoration(DonHangChiTietActivity.this, DividerItemDecoration.VERTICAL));
        rcvDonHangChiTiet.setAdapter(donHangChiTietAdapter);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvSDT = findViewById(R.id.tvSDT);
        rcvDonHangChiTiet = findViewById(R.id.rcvDonHangChiTiet);
        spnTrangThai = findViewById(R.id.spnTrangThai);
        spnShipper = findViewById(R.id.spnShipper);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        tvTGDatHang = findViewById(R.id.tvTGDatHang);
    }
}