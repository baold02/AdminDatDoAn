package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.BannerImage2Adapter;
import com.nhomduan.quanlydathang_admin.adapter.EditMainBannerFragment;
import com.nhomduan.quanlydathang_admin.dao.MainBannerDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.model.BannerImage;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class MainBannerFragment extends Fragment {
    private Toolbar toolbar;
    private ViewPager2 viewpager;
    private AppCompatButton btnEditBanner;
    private CircleIndicator3 circleindicator;

    private List<BannerImage> bannerImageList;
    private BannerImage2Adapter bannerImage2Adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_banner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        setUpMainBannerList();
        setUpBtnChinhSua();

    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setUpBtnChinhSua() {
        btnEditBanner.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new EditMainBannerFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setUpMainBannerList() {
        bannerImageList = new ArrayList<>();
        bannerImage2Adapter = new BannerImage2Adapter(getContext(), bannerImageList);
        viewpager.setAdapter(bannerImage2Adapter);
        MainBannerDao.getInstance().getAllMainBannerListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                bannerImageList = (List<BannerImage>) obj;
                bannerImage2Adapter.setData(bannerImageList);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    if (viewpager.getCurrentItem() == bannerImageList.size() - 1) {
                        viewpager.setCurrentItem(0);
                    } else {
                        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                    }
                };

                circleindicator.setViewPager(viewpager);
                viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable, 5000);
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        viewpager = view.findViewById(R.id.viewpager);
        btnEditBanner = view.findViewById(R.id.btnEditBanner);
        circleindicator = view.findViewById(R.id.circleindicator);

    }
}