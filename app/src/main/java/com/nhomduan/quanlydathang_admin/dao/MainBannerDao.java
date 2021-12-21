package com.nhomduan.quanlydathang_admin.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlydathang_admin.adapter.BannerImage2Adapter;
import com.nhomduan.quanlydathang_admin.fragment.MainBannerFragment;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.model.BannerImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainBannerDao {
    private static MainBannerDao instance;

    private MainBannerDao() {
    }

    public static MainBannerDao getInstance() {
        if (instance == null) {
            instance = new MainBannerDao();
        }
        return instance;
    }

    public synchronized void getAllMainBannerListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("main_banner")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GenericTypeIndicator<List<BannerImage>> genericTypeIndicator = new GenericTypeIndicator<List<BannerImage>>() {};
                        List<BannerImage> bannerImageList = snapshot.getValue(genericTypeIndicator);
                        iAfterGetAllObject.iAfterGetAllObject(bannerImageList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertMainBanners(List<BannerImage> bannerImageList, IAfterInsertObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("main_banner")
                .setValue(bannerImageList, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error == null) {
                            iAfterGetAllObject.onSuccess(error);
                        } else {
                            iAfterGetAllObject.onError(error);
                        }
                    }
                });
    }

}
