package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.DonHangViewHolder>{

    private List<DonHang> donHangList;
    private final OnClickItem onClickItem;

    public DonHangAdapter(List<DonHang> donHangList, OnClickItem onClickItem) {
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
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang,parent,false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        if(donHang == null) {
            return;
        }
        holder.tvHoTen.setText("Họ tên : " + donHang.getHo_ten());
        holder.tvDiaChi.setText("Địa chỉ : " + donHang.getDia_chi());
        holder.tvSDT.setText("SĐT : " + donHang.getSdt());
        holder.tvTrangThai.setText("Trạng thái : " + donHang.getTrang_thai());
        holder.tvTongTien.setText("Tổng tiền : " + OverUtils.currencyFormat.format(donHang.getTong_tien()));

        StringBuilder stringBuilder = new StringBuilder("Sản Phẩm : ");
        for(DonHangChiTiet donHangChiTiet : donHang.getDon_hang_chi_tiets()) {
            stringBuilder.append( "\n" + donHangChiTiet.getProduct().getName() + " (" + donHangChiTiet.getSo_luong() + ")");
        }
        holder.tvSanPhams.setText(stringBuilder.toString());
        holder.item.setOnClickListener(view -> onClickItem.onClickItem(donHang.getId()));
    }

    @Override
    public int getItemCount() {
        if(donHangList != null) {
            return donHangList.size();
        }
        return 0;
    }



    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHoTen;
        private TextView tvSDT;
        private TextView tvDiaChi;
        private TextView tvSanPhams;
        private TextView tvTrangThai;
        private LinearLayout item;
        private TextView tvTongTien;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvHoTen);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            tvSanPhams = itemView.findViewById(R.id.tvSanPhams);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            item = itemView.findViewById(R.id.item);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);

        }
    }
}
