package com.nhomduan.quanlydathang_admin.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductUtils {

    public static DatabaseReference getDbRfProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("san_pham");
    }

    public static StorageReference getStRfProduct(String path) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        if(path == null) {
            return storage.getReference();
        }
        return storage.getReference(path);
    }

    public static List<Product> getProductUser(DataSnapshot dataSnapshot) {
        List<Product> result = new ArrayList<>();
        for(DataSnapshot obj : dataSnapshot.getChildren()) {
            Product product = obj.getValue(Product.class);
            if(product != null) {
                result.add(product);
            }
        }
        return result;
    }

    public static StorageReference getStRfProduct() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        return storage.getReference();
    }
}
