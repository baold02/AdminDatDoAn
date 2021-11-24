package com.nhomduan.quanlydathang_admin.interface_;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

public interface IAfterInsertObject {
    void onSuccess(Object obj);
    void onError(DatabaseError exception);
}
