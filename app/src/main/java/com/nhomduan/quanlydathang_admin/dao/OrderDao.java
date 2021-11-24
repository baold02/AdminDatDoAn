package com.nhomduan.quanlydathang_admin.dao;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlydathang_admin.interface_.IAfterDeleteObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.model.DonHang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDao {
    private static OrderDao instance;

    private OrderDao() {
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDao();
        }
        return instance;
    }

    public void getAllDonHang(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("don_hang")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<DonHang> donHangList = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            DonHang donHang = data.getValue(DonHang.class);
                            donHangList.add(donHang);
                        }
                        iAfterGetAllObject.iAfterGetAllObject(donHangList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void getDonHangById(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("don_hang").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DonHang newDonHang = snapshot.getValue(DonHang.class);
                        iAfterGetAllObject.iAfterGetAllObject(newDonHang);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertDonHang(DonHang donHang, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("don_hang").child(donHang.getId())
                .setValue(donHang, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            iAfterInsertObject.onSuccess(donHang);
                        } else {
                            iAfterInsertObject.onError(error);
                        }
                    }
                });
    }

    public void updateDonHang(DonHang donHang, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("don_hang").child(donHang.getId())
                .updateChildren(map, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            iAfterUpdateObject.onSuccess(donHang);
                        } else {
                            iAfterUpdateObject.onError(error);
                        }
                    }
                });
    }

    public void deleteDonHang(Context context, DonHang donHang, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("don_hang").child(donHang.getId())
                            .removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        iAfterDeleteObject.onSuccess(donHang);
                                    } else {
                                        iAfterDeleteObject.onError(error);
                                    }
                                }
                            });
                })
                .show();

    }
}
