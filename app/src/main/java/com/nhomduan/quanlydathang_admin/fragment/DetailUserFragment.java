package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.FavoriteProductAdapter;
import com.nhomduan.quanlydathang_admin.adapter.GioHangAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.UserDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.GioHang;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.nhomduan.quanlydathang_admin.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailUserFragment extends Fragment implements OnClickItem {
    private CircleImageView imgUser;
    private TextView tvUserName;
    private TextView tvHoTen;
    private TextView tvDiaChi;
    private TextView tvSDT;
    private TextView tvGioHang;
    private TextView tvSanPhamYeuThich;
    private TextView tvHoatDong;
    private ToggleButton btnEnable;
    private Toolbar toolbar;
    private RecyclerView rcvGioHang;
    private RecyclerView rcvSanPhamYT;

    private List<GioHang> gioHangList;
    GioHangAdapter gioHangAdapter;

    private List<Product> productList;
    FavoriteProductAdapter favoriteProductAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        setUpDuLieu();
        setUpGioHangList();
        setUpProductList();
    }

    private void setUpProductList() {
        productList = new ArrayList<>();
        favoriteProductAdapter = new FavoriteProductAdapter(getContext(), productList, this);
        rcvSanPhamYT.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSanPhamYT.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvSanPhamYT.setAdapter(favoriteProductAdapter);
    }

    private void setUpGioHangList() {
        gioHangList = new ArrayList<>();
        gioHangAdapter = new GioHangAdapter(gioHangList);
        rcvGioHang.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvGioHang.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvGioHang.setAdapter(gioHangAdapter);
    }

    private void setUpTongButton(User user) {
        btnEnable.setOnClickListener(v -> {
            if (!btnEnable.isChecked()) {
                user.setEnable(false);
                UserDao.getInstance().updateUser(user, user.toMapLock(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        OverUtils.makeToast(getContext(), "Khóa thành công user");
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }
            if (btnEnable.isChecked()) {
                user.setEnable(true);
                UserDao.getInstance().updateUser(user, user.toMapLock(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        OverUtils.makeToast(getContext(), "Mở khóa thành công user");
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }
        });
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setUpDuLieu() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String userName = bundle.getString("userName");
            UserDao.getInstance().getUserByUserNameListener(userName, new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    User user = (User) obj;
                    if (user.getUsername() != null) {
                        buildComponent(user);
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        }

    }

    private void buildComponent(User user) {
        setUpTongButton(user);
        Picasso.get()
                .load(user.getHinhanh())
                .into(imgUser);

        tvUserName.setText(user.getUsername());
        tvSDT.setText("Số điện thoại : " + user.getPhone_number());

        if (user.getName() != null && user.getUsername().length() > 0) {
            tvHoTen.setText("Họ tên : " + user.getName());
        } else {
            tvHoTen.setText("Chưa cập nhật");
        }

        if (user.getAddress() != null && user.getAddress().length() > 0) {
            tvDiaChi.setText("Địa chỉ : " + user.getAddress());
        } else {
            tvDiaChi.setText("Chưa cập nhật");
        }

        if (user.getGio_hang() != null) {
            String gioHang = user.getGio_hang().size() + " sản phẩm";
            tvGioHang.setText(Html.fromHtml(gioHang), TextView.BufferType.SPANNABLE);
        } else {
            tvGioHang.setText("Không có sản phẩm trong giỏ hàng");
        }

        if (user.getMa_sp_da_thich() != null) {
            String sanPhamYeuThich = user.getMa_sp_da_thich().size() + " sản phẩm";
            tvSanPhamYeuThich.setText(Html.fromHtml(sanPhamYeuThich), TextView.BufferType.SPANNABLE);
        } else {
            tvSanPhamYeuThich.setText("Không có sản phẩm trong yêu thích");
        }

        gioHangList = user.getGio_hang();
        if (gioHangList != null) {
            gioHangAdapter.setData(gioHangList);
        }



        List<String> productCodeList = user.getMa_sp_da_thich();
        if (productCodeList != null) {
            productList.clear();
            for (String prCode : productCodeList) {
                ProductDao.getInstance().getProductById(prCode, new IAfterGetAllObject() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        if(obj != null) {
                            productList.add((Product) obj);
                            favoriteProductAdapter.notifyDataSetChanged();
                            String sanPhamYeuThich = productList.size() + " sản phẩm";
                            tvSanPhamYeuThich.setText(Html.fromHtml(sanPhamYeuThich), TextView.BufferType.SPANNABLE);
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    }
                });
            }
        }

        if (user.isEnable()) {
            btnEnable.setChecked(true);
            tvHoatDong.setText("Đang hoạt động");
            btnEnable.setBackgroundColor(Color.RED);

        } else {
            btnEnable.setChecked(false);
            tvHoatDong.setText("Ngừng hoạt động");
            btnEnable.setBackgroundColor(Color.GREEN);
        }

    }

    private void initView(View view) {
        imgUser = view.findViewById(R.id.imgUser);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvHoTen = view.findViewById(R.id.tvHoTen);
        tvDiaChi = view.findViewById(R.id.tvDiaChi);
        tvSDT = view.findViewById(R.id.tvSDT);
        tvGioHang = view.findViewById(R.id.tvGioHang);
        tvSanPhamYeuThich = view.findViewById(R.id.tvSanPhamYeuThich);
        tvHoatDong = view.findViewById(R.id.tvHoatDong);
        btnEnable = view.findViewById(R.id.btnEnable);
        toolbar = view.findViewById(R.id.toolbar);
        rcvGioHang = view.findViewById(R.id.rcvGioHang);
        rcvSanPhamYT = view.findViewById(R.id.rcvSanPhamYT);
    }

    @Override
    public void onClickItem(Object obj) {
        Fragment fragment = new ShowProductFragment();
        Bundle args = new Bundle();
        args.putString("product_id", (String) obj);
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
}