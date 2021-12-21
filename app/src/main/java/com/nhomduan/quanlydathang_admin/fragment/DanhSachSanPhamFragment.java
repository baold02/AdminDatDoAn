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
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.DanhSachSanPhamPagerAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.util.List;

public class DanhSachSanPhamFragment extends Fragment{

    private Toolbar toolbar;
    private FloatingActionButton fBtnAddSanPham;
    private TabLayout tabLayout;
    private ViewPager2 viewpager;

    private FragmentActivity fragmentActivity;

    private List<LoaiSP> loaiSPList;
    private DanhSachSanPhamPagerAdapter danhSachSanPhamPagerAdapter;

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
        setUpToolbar();
        setUpTabLayoutAndViewPager();
        setUpfBtnAddSanPham();
    }

    private void initView(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewpager = view.findViewById(R.id.viewpager);
        toolbar = view.findViewById(R.id.toolbar);
        fBtnAddSanPham = view.findViewById(R.id.f_btnAddSanPham);
        fragmentActivity = getActivity();
    }

    private void setUpToolbar() {
        setHasOptionsMenu(true);
        MainActivity activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
    }

    private void setUpTabLayoutAndViewPager() {
        viewpager.setUserInputEnabled(false);
        viewpager.setSaveEnabled(false);
        ProductTypeDao.getInstance().getAllProductTypeListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                loaiSPList = (List<LoaiSP>) obj;
                danhSachSanPhamPagerAdapter = new DanhSachSanPhamPagerAdapter(fragmentActivity, loaiSPList);
                viewpager.setAdapter(danhSachSanPhamPagerAdapter);
                new TabLayoutMediator(tabLayout, viewpager,
                        (tab, position) -> tab.setText(loaiSPList.get(position).getName())).attach();
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void setUpfBtnAddSanPham() {
        fBtnAddSanPham.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new ThemSanPhamFragment())
                .addToBackStack(null)
                .commit());
    }

}