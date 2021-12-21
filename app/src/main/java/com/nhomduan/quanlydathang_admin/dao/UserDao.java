package com.nhomduan.quanlydathang_admin.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.model.GioHang;
import com.nhomduan.quanlydathang_admin.model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {
    private static UserDao instance;

    private UserDao() {
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }


    public void getAllUserListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference()
                .child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> userList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data != null) {
                        User user = data.getValue(User.class);
                        userList.add(user);
                    }
                }
                iAfterGetAllObject.iAfterGetAllObject(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getAllUser(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<User> userList = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data != null) {
                            User user = data.getValue(User.class);
                            userList.add(user);
                        }
                    }
                    iAfterGetAllObject.iAfterGetAllObject(userList);
                })
                .addOnFailureListener(e -> Log.e("TAG", "onFailure: "));
    }

    public void getAllUser2(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            if(dataSnapshot != null) {
                                List<User> userList = new ArrayList<>();
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (data != null) {
                                        User user = data.getValue(User.class);
                                        userList.add(user);
                                    }
                                }
                                iAfterGetAllObject.iAfterGetAllObject(userList);
                            }
                        }
                    }
                });
    }

    public void insertUser(User user, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .setValue(user, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(user);
                    } else {
                        iAfterInsertObject.onError(error);
                    }
                });
    }

    public void updateUser(User user, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(user); // trả về user đã được update
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateUser(User user, Map<String, Object> map) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .updateChildren(map);
    }


    public void getUserByUserName(String userName, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(userName)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null) {
                    User user = dataSnapshot.getValue(User.class);
                    iAfterGetAllObject.iAfterGetAllObject(user);
                } else {
                    iAfterGetAllObject.iAfterGetAllObject(new User());
                }
            }
        });
    }

    public void getUserByUserNameListener(String userName, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(userName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        iAfterGetAllObject.iAfterGetAllObject(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }


    public void getGioHangOfUser(User user, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
                .child("gio_hang").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                List<GioHang> gioHangList = new ArrayList<>();
                if (dataSnapshot != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        GioHang gioHang = data.getValue(GioHang.class);
                        gioHangList.add(gioHang);
                    }
                }
                iAfterGetAllObject.iAfterGetAllObject(gioHangList);
            }

        });

    }


    public void getSanPhamYeuThichOfUser(User user, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference()
                .child("user").child(user.getUsername()).child("ma_sp_da_thich")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        if(snapshot != null) {
                            List<String> sanPhamYeuThichList = new ArrayList<>();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                String maSP = data.getValue(String.class);
                                sanPhamYeuThichList.add(maSP);
                            }
                            iAfterGetAllObject.iAfterGetAllObject(sanPhamYeuThichList);
                        }
                    }
                });
    }
}
