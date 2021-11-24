package com.nhomduan.quanlydathang_admin.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlydathang_admin.model.Shipper;

import java.util.ArrayList;
import java.util.List;

public class ShipperUtils {
    private static FirebaseDatabase  database;

    public static DatabaseReference getDbRfShipper() {
        if(database == null) {
            database = FirebaseDatabase.getInstance();
        }
        return database.getReference("shipper");
    }

    public static List<Shipper> getAllShipper(DataSnapshot dataSnapshot) {
        List<Shipper> result = new ArrayList<>();
        result.add(0, new Shipper("", "Chọn shipper ", ""));
        for(DataSnapshot data : dataSnapshot.getChildren()) {
            Shipper shipper = data.getValue(Shipper.class);
            if(shipper != null) {
                result.add(shipper);
            }
        }
        return result;
    };
}
