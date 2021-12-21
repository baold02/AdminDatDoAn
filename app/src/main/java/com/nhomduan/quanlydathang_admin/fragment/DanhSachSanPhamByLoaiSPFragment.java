package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.SanPhamAdapter;
import com.nhomduan.quanlydathang_admin.dao.OrderDao;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.dao.UserDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.IDone;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnDelete;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;
import com.nhomduan.quanlydathang_admin.model.GioHang;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.nhomduan.quanlydathang_admin.model.TrangThai;
import com.nhomduan.quanlydathang_admin.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DanhSachSanPhamByLoaiSPFragment extends Fragment implements OnClickItem, OnDelete {
    private TextView tvSoSanPham;
    private RecyclerView rcvListSPByLoai;
    private List<Product> productList;
    private SanPhamAdapter sanPhamAdapter;

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_danh_sach_san_pham_by_loai_s_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //code
        initView(view);
        getData();
    }

    private void initView(View view) {
        tvSoSanPham = view.findViewById(R.id.tvSoSanPham);
        rcvListSPByLoai = view.findViewById(R.id.rcvListSPByLoai);
    }

    private void getData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            LoaiSP loaiSP = (LoaiSP) bundle.getSerializable("loai_sp");
            if(loaiSP != null) {
                setUpListSanPhamByLoai(loaiSP);
            }
        }

    }

    private void setUpListSanPhamByLoai(LoaiSP loaiSP) {
        productList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(productList, this);
        rcvListSPByLoai.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
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

    @Override
    public void onClickItem(Object obj) {
        Fragment fragment = new ShowProductFragment();
        Bundle args = new Bundle();
        args.putString("product_id", String.valueOf(obj));
        fragment.setArguments(args);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpdateItem(Object obj) {
        Product product = (Product) obj;
        UpdateSanPhamFragment updateSanPhamFragment = new UpdateSanPhamFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productId", product.getId());
        updateSanPhamFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, updateSanPhamFragment)
                .addToBackStack(null)
                .commit();
    }


    private static Product productNeedDelete;
    private static ProgressDialog progressDialog;
    @Override
    public void onDeleteItem(Object obj) {
        productNeedDelete = (Product) obj;
        if (productNeedDelete != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa?" +
                            "\n Bạn sẽ xóa sản phẩm yêu thích của khách hàng," +
                            "\n sản phẩm trong sản phẩm trong giỏ hàng," +
                            "\n sản phẩm trong đơn hàng chưa xác nhận")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, i) -> {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Đang xóa sản phẩm");
                        progressDialog.show();
                        ngungKinhDoanhPr(productNeedDelete, done -> {
                            if(done) {
                                deleteCart(productNeedDelete, DanhSachSanPhamByLoaiSPFragment.this);
                                deleteFavoriteProduct(productNeedDelete, DanhSachSanPhamByLoaiSPFragment.this);
                            }
                        });
                        xoaDonHang(productNeedDelete);
                        xoaSoLuongSanPhamCuaLoai(productNeedDelete);
                    })
                    .show();

        }
    }



    private void xoaSoLuongSanPhamCuaLoai(Product product) {
        String loaiSPId = product.getLoai_sp();
        ProductTypeDao.getInstance().getProductTypeById(loaiSPId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    LoaiSP loaiSP = (LoaiSP) obj;
                    loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() - 1);
                    ProductTypeDao.getInstance().updateProductType(loaiSP, loaiSP.toMapSoLuongSanPham());
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
                OverUtils.makeToast(mContext, ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }

    public void xoaDonHang(Product product) {
        OrderDao.getInstance().getAllDonHang(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<DonHang> donHangList = (List<DonHang>) obj;
                for (DonHang donHang : donHangList) {
                    if (donHang.getTrang_thai().equals(TrangThai.CHUA_XAC_NHAN.getTrangThai())) {
                        List<DonHangChiTiet> donHangChiTietList = donHang.getDon_hang_chi_tiets();
                        if (donHangChiTietList.size() == 1 && donHangChiTietList.get(0).getProduct().getId().equals(product.getId())) {
                            donHang.setTrang_thai(TrangThai.HUY_DON.getTrangThai());
                            donHang.setThong_tin_huy_don("Admin hủy đơn do sản phẩm "
                                    + donHangChiTietList.get(0).getProduct().getName() + " không còn được bán("
                                    + OverUtils.simpleDateFormat.format(new Date(System.currentTimeMillis())) + ")");
                            OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuyDon());
                        } else {
                            int viTri = -1;
                            for (int i = 0; i < donHangChiTietList.size(); i++) {
                                if (donHangChiTietList.get(i).getProduct().getId().equals(product.getId())) {
                                    viTri = i;
                                }
                            }
                            if (viTri != -1) {
                                DonHangChiTiet donHangChiTiet = donHang.getDon_hang_chi_tiets().get(viTri);
                                Product product = donHangChiTiet.getProduct();
                                donHang.setThoiGianGiaoHangDuKien(donHang.getThoiGianGiaoHangDuKien() -
                                        (product.getThoiGianCheBien()));
                                donHang.setTong_tien((int) (donHang.getTong_tien()
                                        - ((product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())) * donHangChiTiet.getSo_luong())));
                                donHang.setThong_tin_huy_don("Admin hủy sản phẩm "
                                        + donHangChiTietList.get(viTri).getProduct().getName()
                                        + " do không còn được bán!(" + OverUtils.simpleDateFormat.format(new Date(System.currentTimeMillis())) + ")");
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
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private static boolean finishDeleteCart = false;
    private static int counterDeleteCart = 0;
    private void deleteCart(Product product, OnDelete onDelete) {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    counterDeleteCart++;
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
                            if (counterDeleteCart == userList.size()) {
                                counterDeleteCart = 0;
                                finishDeleteCart = true;
                                onDelete.onDelete();
                            }

                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                            finishDeleteCart = false;
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                finishDeleteCart = false;
            }
        });
    }


    private static boolean finishDeleteFavoriteProduct = false;
    private static int counterFavoriteProduct = 0;
    private void deleteFavoriteProduct(Product product, OnDelete onDelete) {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    counterFavoriteProduct++;
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
                                UserDao.getInstance().updateUser(user, user.toMapSPDaThich());
                            }
                            if (counterFavoriteProduct == userList.size()) {
                                counterFavoriteProduct = 0;
                                finishDeleteFavoriteProduct = true;
                                onDelete.onDelete();
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                            finishDeleteFavoriteProduct = false;
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                finishDeleteFavoriteProduct = false;
            }
        });
    }

    @Override
    public synchronized void onDelete() {
        if(finishDeleteCart && finishDeleteFavoriteProduct) {
            FirebaseDatabase.getInstance().getReference().child("san_pham").child(productNeedDelete.getId())
                    .removeValue((error, ref) -> {
                        if (error == null) {
                            OverUtils.makeToast(mContext, "Xóa thành công");
                            productNeedDelete = null;
                            progressDialog.dismiss();
                        } else {
                            productNeedDelete = null;
                            progressDialog.dismiss();
                        }
                    });
        }

    }
}