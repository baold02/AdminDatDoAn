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
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductTypeDao {
    private static ProductTypeDao instance;

    private ProductTypeDao() {
    }

    public static ProductTypeDao getInstance() {
        if (instance == null) {
            instance = new ProductTypeDao();
        }
        return instance;
    }

    // sử dụng phương thức addValueEventListener vì là dự án ở trường không quá nhiều dữ liệu
    // có thể thay thế bằng phương thức addChildEventListener
    public void getAllProductType(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<LoaiSP> result = new ArrayList<>();
                        for (DataSnapshot obj : snapshot.getChildren()) {
                            LoaiSP loaiSP = obj.getValue(LoaiSP.class);
                            if (loaiSP != null) {
                                result.add(loaiSP);
                            }
                        }
                        iAfterGetAllObject.iAfterGetAllObject(result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void getProductTypeById(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp").child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                        iAfterGetAllObject.iAfterGetAllObject(loaiSP);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }

    public void insertProductType(LoaiSP loaiSP, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child(loaiSP.getId())
                .setValue(loaiSP, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            iAfterInsertObject.onSuccess(loaiSP); // loaiSP đây là loaiSP đã insert thành công
                        } else {
                            iAfterInsertObject.onError(error);
                        }
                    }
                });
    }

    public void updateProductType(LoaiSP loaiSP, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child(loaiSP.getId())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(loaiSP); // @param -> loaiSP đã được update thành công
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void deleteProductType(Context context, LoaiSP loaiSP, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child(loaiSP.getId())
                            .removeValue((error, ref) -> {
                                if (error == null) {
                                    iAfterDeleteObject.onSuccess(loaiSP); // @param -> loaiSP đã xóa
                                } else {
                                    iAfterDeleteObject.onError(error);
                                }
                            });
                })
                .show();

    }
}
