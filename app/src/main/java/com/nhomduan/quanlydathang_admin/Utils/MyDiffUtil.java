package com.nhomduan.quanlydathang_admin.Utils;

import android.widget.ProgressBar;

import androidx.recyclerview.widget.DiffUtil;

import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MyDiffUtil extends DiffUtil.Callback {

    public MyDiffUtil(List<Product> oldProductList, List<Product> newProductList) {
        this.oldProductList = oldProductList;
        this.newProductList = newProductList;
    }

    private List<Product> oldProductList;
    private List<Product> newProductList;
    @Override
    public int getOldListSize() {
        return oldProductList.size();
    }

    @Override
    public int getNewListSize() {
        return newProductList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProductList.get(oldItemPosition).getId().equals(newProductList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldProductList.get(oldItemPosition).isContentsTheSame(newProductList.get(newItemPosition));
    }
}
