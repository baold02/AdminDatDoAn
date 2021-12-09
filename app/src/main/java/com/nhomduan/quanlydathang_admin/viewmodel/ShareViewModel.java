package com.nhomduan.quanlydathang_admin.viewmodel;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nhomduan.quanlydathang_admin.model.BangThongKe;

public class ShareViewModel extends ViewModel {
    public MutableLiveData<BangThongKe> data = new MutableLiveData<>();

    public void setData(BangThongKe data){
        this.data.setValue(data);
    }
}
