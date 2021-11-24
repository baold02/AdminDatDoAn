package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.Utils.ProductUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.SanPhamAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterDeleteObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DanhSachSanPhamFragment extends Fragment implements OnClickItem {

    private Toolbar toolbar;
    private FloatingActionButton fBtnAddSanPham;
    private RecyclerView rcvDanhSachSanPham;

    private FragmentManager mFragmentManager;
    private MainActivity activity;

    private List<Product> productList;
    private SanPhamAdapter sanPhamAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_xem_san_pham, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mFragmentManager = getParentFragmentManager();
        setHasOptionsMenu(true);
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        setUpListSanPham();
        setUpfBtnAddSanPham();
    }

    private void setUpfBtnAddSanPham() {
        fBtnAddSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentManager.beginTransaction()
                        .replace(R.id.sanPhamContainer, new ThemSanPhamFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setUpListSanPham() {
        productList = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(productList, this);
        rcvDanhSachSanPham.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDanhSachSanPham.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvDanhSachSanPham.setAdapter(sanPhamAdapter);
        getProducList();
    }

    public void getProducList() {
        ProductUtils.getDbRfProduct().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot != null) {
                        productList = ProductUtils.getProductUser(dataSnapshot);
                        sanPhamAdapter.setData(productList);
                    }
                } else {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                }

            }
        });
    }

    private void initView(View view) {
        rcvDanhSachSanPham = view.findViewById(R.id.rcvDanhSachSanPham);
        toolbar = view.findViewById(R.id.toolbar);
        fBtnAddSanPham = view.findViewById(R.id.f_btnAddSanPham);
    }

    @Override
    public void onClickItem(Object obj) {
        Product product = (Product) obj;
    }

    @Override
    public void onUpdateItem(Object obj) {
        Product product = (Product) obj;
        UpdateSanPhamFragment updateSanPhamFragment = new UpdateSanPhamFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        updateSanPhamFragment.setArguments(bundle);
        mFragmentManager.beginTransaction()
                .replace(R.id.sanPhamContainer, updateSanPhamFragment, null)
                .addToBackStack("XemSPToUpdateSP")
                .commit();
    }


    @Override
    public void onDeleteItem(Object obj) {
        Product product = (Product) obj;
        if (product != null) {
            ProductDao.getInstance().deleteProduct(getContext(),
                    product, new IAfterDeleteObject() {
                        @Override
                        public void onSuccess(Object obj) {
                            OverUtils.makeToast(getContext(), "Xóa sản phẩm thành công");
                            getProducList();
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        }
                    });
        }

    }
}