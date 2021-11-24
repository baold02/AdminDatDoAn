package com.nhomduan.quanlydathang_admin.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlydathang_admin.model.DonHang;

import java.util.ArrayList;
import java.util.List;

public class DonHangUtils {
    public static DatabaseReference getDbRfDonHang() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("don_hang");
    }

    public static List<DonHang> getAllDonHang(DataSnapshot dataSnapshot) {
        List<DonHang> donHangList = new ArrayList<>();
        for(DataSnapshot data : dataSnapshot.getChildren()) {
            DonHang donHang = data.getValue(DonHang.class);
            donHangList.add(donHang);
        }
        return donHangList;
    }
}
