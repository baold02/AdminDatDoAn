package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;

import java.util.List;

public class DoanhThuAdapter extends RecyclerView.Adapter<DoanhThuAdapter.DoanhThuViewHolder> {

    private List<DonHang> donHangList;
    private OnClickItem onClickItem;

    public DoanhThuAdapter(List<DonHang> donHangList, OnClickItem onClickItem) {
        this.donHangList = donHangList;
        this.onClickItem = onClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<DonHang> donHangList) {
        this.donHangList = donHangList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoanhThuAdapter.DoanhThuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doanh_thu, parent, false);
        return new DoanhThuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoanhThuAdapter.DoanhThuViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        if(donHang == null) {
            return;
        }

        holder.tvMaDonHang.setText("MĐH: " + donHang.getId());
        StringBuilder sb = new StringBuilder();
        for(DonHangChiTiet dhct : donHang.getDon_hang_chi_tiets()) {
            sb.append(dhct.getProduct().getName() + ", ");
        }
        holder.tvSanPhams.setText("Sản phẩm: " + sb.toString());
        String tongTien = "Doanh Thu: <font color='green'>" + OverUtils.currencyFormat.format(donHang.getTong_tien()) + "</font>";
        holder.tvDoanhThu.setText(Html.fromHtml(tongTien), TextView.BufferType.SPANNABLE);
        holder.item.setOnClickListener(v -> {
            onClickItem.onClickItem(donHang.getId());
        });
    }

    @Override
    public int getItemCount() {
        if(donHangList != null) {
            return donHangList.size();
        }
        return 0;
    }

    public class DoanhThuViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private TextView tvMaDonHang;
        private TextView tvSanPhams;
        private TextView tvDoanhThu;

        public DoanhThuViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvMaDonHang = itemView.findViewById(R.id.tvMaDonHang);
            tvSanPhams = itemView.findViewById(R.id.tvSanPhams);
            tvDoanhThu = itemView.findViewById(R.id.tvDoanhThu);
        }
    }
}
