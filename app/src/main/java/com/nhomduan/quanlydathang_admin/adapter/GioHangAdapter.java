package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.model.GioHang;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.DonHangViewHolder> {

    private List<GioHang> gioHangList;

    public GioHangAdapter(List<GioHang> gioHangList) {
        this.gioHangList = gioHangList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<GioHang> gioHangList) {
        this.gioHangList = gioHangList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang_chi_tiet, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        if (gioHang == null) {
            return;
        }

        ProductDao.getInstance().getProductById(gioHang.getMa_sp(), new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if (obj != null) {
                    Product product = (Product) obj;

                    holder.tvTenSanPham.setText(product.getName());
                    int giaBanTT = (int) ((product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())));
                    holder.tvGiaSanPham.setText(OverUtils.currencyFormat.format(giaBanTT));
                    Picasso.get()
                            .load(product.getImage())
                            .placeholder(R.drawable.ic_image)
                            .into(holder.imgSanPham);
                }
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });
        holder.tvSoLuong.setText("x" + gioHang.getSo_luong());

    }


    @Override
    public int getItemCount() {
        if (gioHangList != null) {
            return gioHangList.size();
        }
        return 0;
    }

    public static class DonHangViewHolder extends RecyclerView.ViewHolder {
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
