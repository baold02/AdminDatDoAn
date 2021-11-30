package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.DonHangChiTietActivity;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.activities.ShowProductActivity;
import com.nhomduan.quanlydathang_admin.adapter.SanPhamAdapter;
import com.nhomduan.quanlydathang_admin.dao.OrderDao;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.UserDao;
import com.nhomduan.quanlydathang_admin.dialog.SingleChoiceDialog;
import com.nhomduan.quanlydathang_admin.interface_.IAfterDeleteObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.IOnChangeFragment;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnStopProduct;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;
import com.nhomduan.quanlydathang_admin.model.GioHang;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.nhomduan.quanlydathang_admin.model.TrangThai;
import com.nhomduan.quanlydathang_admin.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DanhSachSanPhamByLoaiSPFragment extends Fragment implements OnClickItem {

    private TextView tvSoSanPham;
    private RecyclerView rcvListSPByLoai;
    private final LoaiSP loaiSP;
    private List<Product> productList;
    private SanPhamAdapter sanPhamAdapter;

    private MainActivity activity;


    public DanhSachSanPhamByLoaiSPFragment(LoaiSP loaiSP) {
        this.loaiSP = loaiSP;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_san_pham_by_loai_s_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpListSanPhamByLoai(loaiSP);
    }

    private void setUpListSanPhamByLoai(LoaiSP loaiSP) {
        productList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(productList, this);
        rcvListSPByLoai.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvListSPByLoai.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvListSPByLoai.setAdapter(sanPhamAdapter);
        ProductDao.getInstance().getProductByProductType(loaiSP, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                productList = (List<Product>) obj;
                sanPhamAdapter.setData(productList);
                tvSoSanPham.setText("Số sản phẩm : " + productList.size() + " sp");
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void initView(View view) {
        tvSoSanPham = view.findViewById(R.id.tvSoSanPham);
        rcvListSPByLoai = view.findViewById(R.id.rcvListSPByLoai);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onClickItem(Object obj) {
        Product product = (Product) obj;
        Intent intent = new Intent(getContext(), ShowProductActivity.class);
        intent.putExtra("productId", product.getId());
        startActivity(intent);
    }

    @Override
    public void onUpdateItem(Object obj) {
        Product product = (Product) obj;
        UpdateSanPhamFragment updateSanPhamFragment = new UpdateSanPhamFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        updateSanPhamFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, updateSanPhamFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onDeleteItem(Object obj) {
        Product product = (Product) obj;
        if (product != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa?" +
                            "\n Bạn sẽ xóa sản phẩm yêu thích của khách hàng," +
                            "\n sản phẩm trong sản phẩm trong giỏ hàng," +
                            "\n sản phẩm trong đơn hàng chưa xác nhận")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, i) -> {
                        ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Đang xóa sản phẩm");
                        progressDialog.show();
                        ngungKinhDoanhPr(product, done0 -> {
                            if (done0) {
                                xoaSPDT(product, done -> {
                                    if (done) {
                                        xoaGioHang(product, done1 -> {
                                            if (done1) {
                                                FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                        if (error == null) {
                                                            OverUtils.makeToast(getContext(), "Xóa thành công");
                                                            progressDialog.dismiss();
                                                        } else {
                                                            progressDialog.cancel();
                                                        }
                                                    }
                                                });
                                            } else {
                                                progressDialog.cancel();
                                            }
                                        });
                                    } else {
                                        progressDialog.cancel();
                                    }
                                });
                            } else {
                                progressDialog.cancel();
                            }
                        });
                        xoaDonHang(product);
                    })
                    .show();

        }
    }

    private interface IDone {
        void onDone(boolean done);
    }


    public void xoaDonHang(Product product) {
        OrderDao.getInstance().getAllDonHang(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<DonHang> donHangList = (List<DonHang>) obj;
                for (DonHang donHang : donHangList) {
                    if (donHang.getTrang_thai().equals(TrangThai.CXN.getTrangThai())) {
                        List<DonHangChiTiet> donHangChiTietList = donHang.getDon_hang_chi_tiets();
                        if (donHangChiTietList.size() == 1) {
                            donHang.setTrang_thai(TrangThai.HD.getTrangThai());
                            donHang.setThong_tin_huy_don("Admin hủy đơn do sản phẩm "
                                    + donHangChiTietList.get(0).getProduct().getName() + " không còn được bán");
                            OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuyDon());
                        } else {
                            int viTri = -1;
                            for (int i = 0; i < donHangChiTietList.size(); i++) {
                                if (donHangChiTietList.get(i).getProduct().getId().equals(product.getId())) {
                                    viTri = i;
                                }
                            }
                            if (viTri != -1) {
                                donHang.setThong_tin_huy_don("Admin hủy sản phẩm "
                                        + donHangChiTietList.get(viTri).getProduct().getName()
                                        + " do không còn được bán!");
                                donHangChiTietList.remove(viTri);
                                donHang.setDon_hang_chi_tiets(donHangChiTietList);
                                OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuySPTrongDon());
                            }
                        }

                    }
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    public void ngungKinhDoanhPr(Product product, IDone iDone) {
        product.setTrang_thai(OverUtils.DUNG_KINH_DOANH);
        ProductDao.getInstance().updateProduct(product, product.toMapTrangThaiSP(), new IAfterUpdateObject() {
            @Override
            public void onSuccess(Object obj) {
                iDone.onDone(true);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }


    int couterXoaGioHang = 0;

    private void xoaGioHang(Product product, IDone iDone) {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    couterXoaGioHang++;
                    UserDao.getInstance().getGioHangOfUser(user, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<GioHang> gioHangList = (List<GioHang>) obj;
                            int viTri = -1;
                            for (int i = 0; i < gioHangList.size(); i++) {
                                if (gioHangList.get(i).getMa_sp().equals(product.getId())) {
                                    viTri = i;
                                    break;
                                }
                            }
                            if (viTri != -1) {
                                gioHangList.remove(viTri);
                                user.setGio_hang(gioHangList);
                                UserDao.getInstance().updateUser(user, user.toMapGioHang());
                            }
                            if (couterXoaGioHang == userList.size()) {
                                couterXoaGioHang = 0;
                                iDone.onDone(true);
                            }

                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                            iDone.onDone(false);
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }


    int couterSPDT = 0;

    private void xoaSPDT(Product product, IDone iDone) {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    couterSPDT++;
                    UserDao.getInstance().getSanPhamYeuThichOfUser(user, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<String> sanPhamYeuThichList = (List<String>) obj;
                            int viTri = -1;
                            for (int i = 0; i < sanPhamYeuThichList.size(); i++) {
                                if (sanPhamYeuThichList.get(i).equals(product.getId())) {
                                    viTri = i;
                                    break;
                                }
                            }
                            if (viTri != -1) {
                                sanPhamYeuThichList.remove(viTri);
                                user.setMa_sp_da_thich(sanPhamYeuThichList);
                                UserDao.getInstance().updateUser(user, user.toMapSPDaThich(), new IAfterUpdateObject() {
                                    @Override
                                    public void onSuccess(Object obj) {
                                        if (couterSPDT == userList.size()) {
                                            couterSPDT = 0;
                                            iDone.onDone(true);
                                        }
                                    }

                                    @Override
                                    public void onError(DatabaseError error) {
                                        iDone.onDone(false);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                            iDone.onDone(false);
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }
}