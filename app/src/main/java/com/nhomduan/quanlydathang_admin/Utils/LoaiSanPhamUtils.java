package com.nhomduan.quanlydathang_admin.Utils;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.util.ArrayList;
import java.util.List;

public class LoaiSanPhamUtils {

    public static DatabaseReference getDbRfLoaiSP() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("loai_sp");
    }

    public static List<LoaiSP> getAllLoaiSP(DataSnapshot dataSnapshot) {
        List<LoaiSP> result = new ArrayList<>();
        for(DataSnapshot obj : dataSnapshot.getChildren()) {
            LoaiSP loaiSP = obj.getValue(LoaiSP.class);
            if(loaiSP != null) {
                result.add(loaiSP);
            }
        }
        return result;
    }




}
