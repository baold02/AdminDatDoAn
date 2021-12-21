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
import com.nhomduan.quanlydathang_admin.model.CustomModule;

import java.util.ArrayList;
import java.util.List;

public class CustomModuleDao {
    private static CustomModuleDao instance;

    private CustomModuleDao() {
    }

    public static CustomModuleDao getInstance() {
        if (instance == null) {
            instance = new CustomModuleDao();
        }
        return instance;
    }

    public void insertCustomModule(CustomModule customModule, IAfterInsertObject iAfterInsertObject) {
        DatabaseReference drf = FirebaseDatabase.getInstance().getReference().child("custom_module");
        String customModuleId = drf.push().getKey();
        customModule.setId(customModuleId);
        drf.child(customModule.getId()).setValue(customModule, (error, ref) -> {
            if (error == null) {
                iAfterInsertObject.onSuccess(customModule);
            } else {
                iAfterInsertObject.onError(error);
            }
        });
    }

    public void updateCustomModule(CustomModule customModule, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("custom_module").child(customModule.getId())
                .updateChildren(customModule.toMap(), (error, ref) -> {
                    if(error == null) {
                        iAfterUpdateObject.onSuccess(customModule);
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateCustomModule(CustomModule customModule) {
        FirebaseDatabase.getInstance().getReference().child("custom_module").child(customModule.getId())
                .updateChildren(customModule.toMap());
    }

    public void getAllCustomModuleListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("custom_module")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<CustomModule> customModuleList = new ArrayList<>();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            CustomModule customModule = data.getValue(CustomModule.class);
                            customModuleList.add(customModule);
                        }
                        iAfterGetAllObject.iAfterGetAllObject(customModuleList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }


    public void deleteModule(Context context, String id, IAfterDeleteObject iAfterDeleteObject) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa admin")
                .setMessage("Bạn có chắc chắn muốn xóa?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, i) -> {
                    FirebaseDatabase.getInstance().getReference().child("custom_module").child(id)
                            .removeValue((error, ref) -> {
                                if (error == null) {
                                    iAfterDeleteObject.onSuccess(id);
                                } else {
                                    iAfterDeleteObject.onError(error);
                                }
                            });

                })
                .show();
    }
}
