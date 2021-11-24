package com.nhomduan.quanlydathang_admin.Utils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlydathang_admin.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserUtils {

    public static DatabaseReference getDbRfUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("user");
    }

    public static List<User> getAllUser(DataSnapshot dataSnapshot) {
        List<User> userList = new ArrayList<>();
        for(DataSnapshot data : dataSnapshot.getChildren()) {
            if(data != null) {
                User user = data.getValue(User.class);
                userList.add(user);
            }
        }
        return userList;
    }

}
