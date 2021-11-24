package com.nhomduan.quanlydathang_admin.interface_;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.util.List;

public interface IAfterGetAllObject {
    void iAfterGetAllObject(Object obj);
    void onError(DatabaseError error);
}
