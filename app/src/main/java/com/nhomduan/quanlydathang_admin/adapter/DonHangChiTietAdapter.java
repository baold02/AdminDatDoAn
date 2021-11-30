package com.nhomduan.quanlydathang_admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DonHangChiTietAdapter extends RecyclerView.Adapter<DonHangChiTietAdapter.DonHangViewHolder> {
    private List<DonHangChiTiet> donHangChiTietList;

    public DonHangChiTietAdapter(List<DonHangChiTiet> donHangChiTietList) {
        this.donHangChiTietList = donHangChiTietList;
    }

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang_chi_tiet, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHangChiTiet donHangChiTiet = donHangChiTietList.get(position);
        if(donHangChiTiet == null) {
            return;
        }
        holder.tvSoLuong.setText("x" + donHangChiTiet.getSo_luong());
        Product product = donHangChiTiet.getProduct();
        holder.tvTenSanPham.setText(product.getName());
        int soTienMotSP = (int) (product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai()));
        int tongTien = soTienMotSP * donHangChiTiet.getSo_luong();
        holder.tvGiaSanPham.setText(OverUtils.currencyFormat.format(tongTien));
        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgSanPham);
    }


    @Override
    public int getItemCount() {
        if(donHangChiTietList != null) {
            return donHangChiTietList.size();
        }
        return 0;
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSanPham;
        private TextView tvTenSanPham;
        private TextView tvGiaSanPham;
        private TextView tvSoLuong;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvGiaSanPham = itemView.findViewById(R.id.tvGiaSanPham);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
        }
    }
}
