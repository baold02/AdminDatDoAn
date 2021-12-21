package com.nhomduan.quanlydathang_admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductSpinnerAdapter extends BaseAdapter {
    private List<Product> products;
    private Context context;

    public ProductSpinnerAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public void setData(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(products != null) {
            return products.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_product_spinner, parent, false);
        }

        TextView tvTenSP = convertView.findViewById(R.id.tvTenSanPham);
        Product product = products.get(position);
        tvTenSP.setText(product.getName());
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_product_spinner, parent, false);
        }

        TextView tvTenSP = convertView.findViewById(R.id.tvTenSanPham);
        Product product = products.get(position);
        tvTenSP.setText(product.getName());
        return convertView;
    }

}
