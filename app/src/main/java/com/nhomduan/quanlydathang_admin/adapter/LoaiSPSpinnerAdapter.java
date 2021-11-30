package com.nhomduan.quanlydathang_admin.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;

import java.util.List;

public class LoaiSPSpinnerAdapter extends BaseAdapter {

    Context context;
    List<LoaiSP> loaiSPList;


    public LoaiSPSpinnerAdapter(Context context, List<LoaiSP> loaiSPList) {
        this.context = context;
        this.loaiSPList = loaiSPList;
    }

    public void setData(List<LoaiSP> loaiSPList) {
        this.loaiSPList = loaiSPList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(loaiSPList != null) {
            return loaiSPList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return loaiSPList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.item_sp_loai_sp, null);

        TextView tvTenLoaiSP = (TextView) view.findViewById(R.id.tvLoaiSP);

        LoaiSP loaiSP = loaiSPList.get(i);
        if(loaiSP != null) {
            tvTenLoaiSP.setText(loaiSP.getName());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.item_sp_loai_sp, null);

        TextView tvTenLoaiSP = convertView.findViewById(R.id.tvLoaiSP);

        LoaiSP loaiSP = loaiSPList.get(position);
        if(loaiSP != null) {
            tvTenLoaiSP.setText(loaiSP.getName());
        }
        return convertView;
    }


}
