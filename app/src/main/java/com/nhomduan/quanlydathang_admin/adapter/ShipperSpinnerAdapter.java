package com.nhomduan.quanlydathang_admin.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.model.Shipper;

import java.util.List;

public class ShipperSpinnerAdapter extends ArrayAdapter<Shipper> {

    private Context context;
    private List<Shipper> shipperList;
    private TextView tvShipper;

    public ShipperSpinnerAdapter(@NonNull Context context, @NonNull List<Shipper> objects) {
        super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        this.context = context;
        this.shipperList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.item_spinner_shipper, null);
        }
        final Shipper shipper = shipperList.get(position);
        if(shipper != null) {
            tvShipper = v.findViewById(R.id.tvShipper);
            tvShipper.setText(shipper.getName() + "     " + shipper.getPhone_number());
        }

        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.item_spinner_shipper, null);
        }
        final Shipper shipper = shipperList.get(position);
        if(shipper != null) {
            tvShipper = v.findViewById(R.id.tvShipper);
            tvShipper.setText(shipper.getName() + " - " + shipper.getPhone_number());
        }

        return v;
    }
}
