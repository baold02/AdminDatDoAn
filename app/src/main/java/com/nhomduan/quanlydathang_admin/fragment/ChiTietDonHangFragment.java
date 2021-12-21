package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.DonHangChiTietAdapter;
import com.nhomduan.quanlydathang_admin.adapter.ShipperSpinnerAdapter;
import com.nhomduan.quanlydathang_admin.dao.OrderDao;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.ShiperDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.IDone;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.nhomduan.quanlydathang_admin.model.Shipper;
import com.nhomduan.quanlydathang_admin.model.TrangThai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChiTietDonHangFragment extends Fragment {
    private Toolbar toolbar;
    private TextView tvHoTen;
    private TextView tvDiaChi;
    private TextView tvSDT;
    private RecyclerView rcvDonHangChiTiet;
    private Spinner spnTrangThai;
    private Spinner spnShipper;
    private TextView tvTongTien;
    private TextView tvTGDatHang;
    private TextView tvTitleTGGiaoHang;
    private TextView tvTGGiaoHang;
    private Button btnXacNhan;
    private TextView tvTitleTTHuyDon;
    private TextView tvTTHuyDon;




    private DonHang donHang;
    private List<Shipper> shipperList;
    private ShipperSpinnerAdapter shipperSpinnerAdapter;

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chi_tiet_don_hang, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //code
        initView(view);
        setUpToolbar();
        getData(done -> {
            if(done) {
                setUpDonHangChiTietList();
                setUpOtherComponent();
            }
        });
        setUpBtnConfirm();
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvHoTen = view.findViewById(R.id.tvHoTen);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvSDT = view.findViewById(R.id.tvSDT);
        rcvDonHangChiTiet = view.findViewById(R.id.rcvDonHangChiTiet);
        spnTrangThai = view.findViewById(R.id.spnTrangThai);
        spnShipper = view.findViewById(R.id.spnShipper);
        tvTongTien = view.findViewById(R.id.tvTongTien);
        tvTGDatHang = view.findViewById(R.id.tvTGDatHang);
        tvTitleTGGiaoHang = view.findViewById(R.id.tvTitleTGGiaoHang);
        tvTGGiaoHang = view.findViewById(R.id.tvTGGiaoHang);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        tvTitleTTHuyDon = view.findViewById(R.id.tvTitleTTHuyDon);
        tvTTHuyDon = view.findViewById(R.id.tvTTHuyDon);
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void getData(IDone iDone) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String maDonHang = bundle.getString("ma_don_hang");
            OrderDao.getInstance().getDonHangById(maDonHang, new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    if(obj != null) {
                        donHang = (DonHang) obj;
                        iDone.onDone(true);
                    } else {
                        iDone.onDone(false);
                    }

                }

                @Override
                public void onError(DatabaseError error) {
                    iDone.onDone(false);
                }
            });

        }
    }

    private void setUpBtnConfirm() {
        btnXacNhan.setOnClickListener(v -> {
            TrangThai trangThai = (TrangThai) spnTrangThai.getSelectedItem();
            Shipper shipper = (Shipper) spnShipper.getSelectedItem();
            if (trangThai.equals(TrangThai.DANG_GIAO_HANG) || trangThai.equals(TrangThai.HOAN_THANH)) {
                if (shipper.getId().equals("")) {
                    OverUtils.makeToast(getContext(), "Vui lòng chọn shipper");
                    return;
                }
            }
            if (trangThai.equals(TrangThai.HOAN_THANH)) {
                donHang.setThoiGianGiaoHang(System.currentTimeMillis());
                List<DonHangChiTiet> donHangChiTietList = donHang.getDon_hang_chi_tiets();
                for (DonHangChiTiet dhct : donHangChiTietList) {
                    ProductDao.getInstance().getProductById(dhct.getProduct().getId(), new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            Product product = (Product) obj;
                            product.setSo_luong_da_ban(product.getSo_luong_da_ban() + dhct.getSo_luong());
                            ProductDao.getInstance().updateProduct(product, product.toMapSPDB());
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        }
                    });
                }

            }
            if(trangThai.equals(TrangThai.HUY_DON)) {
                donHang.setThong_tin_huy_don("Do Admin hủy đơn (" + OverUtils.simpleDateFormat.format(new Date(System.currentTimeMillis())) + ")");
            }
            donHang.setTrang_thai(trangThai.getTrangThai());
            if(!shipper.getId().equals("")) {
                donHang.setShipper(shipper);
            }
            OrderDao.getInstance().updateDonHang(donHang, donHang.toMapShipperAndTrangThai(), new IAfterUpdateObject() {
                @Override
                public void onSuccess(Object obj) {
                    OverUtils.makeToast(getContext(), "Xác nhận thành công");
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });


        });

    }

    private void setUpDonHangChiTietList() {
        List<DonHangChiTiet> donHangChiTietList = donHang.getDon_hang_chi_tiets();
        DonHangChiTietAdapter donHangChiTietAdapter = new DonHangChiTietAdapter(donHangChiTietList);
        rcvDonHangChiTiet.setLayoutManager(new LinearLayoutManager(mContext));
        rcvDonHangChiTiet.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        rcvDonHangChiTiet.setAdapter(donHangChiTietAdapter);
    }

    private void setUpOtherComponent() {
        tvHoTen.setText(donHang.getHo_ten());
        tvDiaChi.setText(donHang.getDia_chi());
        tvSDT.setText(donHang.getSdt());
        tvTongTien.setText(OverUtils.currencyFormat.format(donHang.getTong_tien()));
        tvTGDatHang.setText(donHang.getThoiGianDatHang());

        if(donHang.getThoiGianGiaoHang() != 0) {
            tvTGGiaoHang.setText(OverUtils.simpleDateFormat.format(new Date(donHang.getThoiGianGiaoHang())));
        }

        if(!donHang.getTrang_thai().equals(TrangThai.HOAN_THANH.getTrangThai())) {
            tvTGGiaoHang.setVisibility(View.GONE);
            tvTitleTGGiaoHang.setVisibility(View.GONE);
        }

        if(donHang.getThong_tin_huy_don() == null) {
            tvTitleTTHuyDon.setVisibility(View.GONE);
            tvTTHuyDon.setVisibility(View.GONE);
        } else {
            tvTitleTTHuyDon.setVisibility(View.VISIBLE);
            tvTTHuyDon.setVisibility(View.VISIBLE);
            tvTTHuyDon.setText(donHang.getThong_tin_huy_don());
        }

        List<TrangThai> trangThaiList =
                new ArrayList<>(Arrays.asList(TrangThai.values()));

        if (donHang.getTrang_thai().equals(TrangThai.CHE_BIEN.getTrangThai())) {
            trangThaiList.remove(TrangThai.CHUA_XAC_NHAN);
        } else if (donHang.getTrang_thai().equals(TrangThai.DANG_GIAO_HANG.getTrangThai())) {
            trangThaiList.remove(TrangThai.CHUA_XAC_NHAN);
            trangThaiList.remove(TrangThai.CHE_BIEN);
        } else if (donHang.getTrang_thai().equals(TrangThai.HOAN_THANH.getTrangThai())) {
            trangThaiList.remove(TrangThai.CHUA_XAC_NHAN);
            trangThaiList.remove(TrangThai.CHE_BIEN);
            trangThaiList.remove(TrangThai.DANG_GIAO_HANG);
            trangThaiList.remove(TrangThai.HUY_DON);
            btnXacNhan.setEnabled(false);
            spnShipper.setEnabled(false);
        } else if (donHang.getTrang_thai().equals(TrangThai.HUY_DON.getTrangThai())) {
            trangThaiList.remove(TrangThai.CHUA_XAC_NHAN);
            trangThaiList.remove(TrangThai.CHE_BIEN);
            trangThaiList.remove(TrangThai.HOAN_THANH);
            trangThaiList.remove(TrangThai.DANG_GIAO_HANG);
            btnXacNhan.setEnabled(false);
            spnShipper.setEnabled(false);
        }

        spnTrangThai.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, trangThaiList));
        int viTri = 0;
        for (int i = 0; i < trangThaiList.size(); i++) {
            if (donHang.getTrang_thai().equals(trangThaiList.get(i).getTrangThai())) {
                viTri = i;
            }
        }
        spnTrangThai.setSelection(viTri);

        ShiperDao.getInstance().getAllShipperListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                shipperList = (List<Shipper>) obj;
                shipperList.add(0, new Shipper("", "Chọn shipper ", ""));
                shipperSpinnerAdapter = new ShipperSpinnerAdapter(mContext, shipperList);
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
            public void onError(DatabaseError error) {

            }
        });

    }
}