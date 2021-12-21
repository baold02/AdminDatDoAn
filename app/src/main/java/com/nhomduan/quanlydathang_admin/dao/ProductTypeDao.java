package com.nhomduan.quanlydathang_admin.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.nhomduan.quanlydathang_admin.model.Product;

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
    public void getAllProductTypeListener(IAfterGetAllObject iAfterGetAllObject) {
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

    public void getAllProductType(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot != null) {
                        List<LoaiSP> loaiSPList =  new ArrayList<>();
                        for(DataSnapshot data : dataSnapshot.getChildren()) {
                            LoaiSP loaiSP = data.getValue(LoaiSP.class);
                            loaiSPList.add(loaiSP);
                        }
                        iAfterGetAllObject.iAfterGetAllObject(loaiSPList);
                    } else {
                        iAfterGetAllObject.iAfterGetAllObject(new ArrayList<LoaiSP>());
                    }
                } else {
                    Log.e("TAG", "ERROR:" );
                    iAfterGetAllObject.iAfterGetAllObject(null);
                }
            }
        });
    }
    public void getProductTypeByIdListener(String id, IAfterGetAllObject iAfterGetAllObject) {
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

    public void getProductTypeById(String id, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp").child(id)
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot != null) {
                        LoaiSP loaiSP = dataSnapshot.getValue(LoaiSP.class);
                        iAfterGetAllObject.iAfterGetAllObject(loaiSP);
                    } else {
                        iAfterGetAllObject.iAfterGetAllObject(null);
                    }
                }
            }
        });
    }

    public void insertProductType(LoaiSP loaiSP, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp").child(loaiSP.getId())
                .setValue(loaiSP, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(loaiSP); // loaiSP đây là loaiSP đã insert thành công
                    } else {
                        iAfterInsertObject.onError(error);
                    }
                });
    }

    public void updateProductType(LoaiSP loaiSP, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp").child(loaiSP.getId())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(loaiSP); // @param -> loaiSP đã được update thành công
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateProductType(LoaiSP loaiSP, Map<String, Object> map) {
        FirebaseDatabase.getInstance().getReference().child("loai_sp").child(loaiSP.getId())
                .updateChildren(map);
    }

    public void deleteProductType(Context context, LoaiSP loaiSP, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn có chắc chắn muốn xóa?\n Bạn sẽ luôn cả sản phẩm thuộc loại!")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("loai_sp").child(loaiSP.getId())
                            .removeValue((error, ref) -> {
                                if (error == null) {
                                    iAfterDeleteObject.onSuccess(loaiSP); // @param -> loaiSP đã xóa
                                } else {
                                    iAfterDeleteObject.onError(error);
                                }
                            });
                    ProductDao.getInstance().getAllProduct(new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<Product> productList = (List<Product>) obj;
                            for(Product product : productList) {
                                if(product.getLoai_sp().equals(loaiSP.getId())) {

                                }
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {

                        }
                    });
                })
                .show();

    }


}
