package com.nhomduan.quanlydathang_admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.fragment.ShowProductFragment;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> list;
    private OnClickItem onClickItem;

    public FavoriteProductAdapter(Context context, List<Product> list, OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem = onClickItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham_yeu_thich, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = list.get(position);
        if(product != null) {
            Picasso.get()
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_image)
                    .into(holder.imgProduct);
            holder.tvNameProduct.setText(product.getName());
            holder.tvTimeProduct.setText(product.getThoiGianCheBien() + " phút");
            holder.tvSoNguoiThichSP.setText(String.valueOf(product.getRate()));
            holder.tvSoNguoiMuaSP.setText(String.valueOf(product.getSo_luong_da_ban()));

            Locale locale = new Locale("vi", "VN");
            NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
            holder.tvPriceProduct.setText(currencyFormat.format((int) (product.getGia_ban() - product.getGia_ban() * product.getKhuyen_mai())) + " VNĐ");

            holder.viewHolderProduct.setOnClickListener(v -> {
                onClickItem.onClickItem(product.getId());
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        ToggleButton imgHearthLike;
        TextView tvNameProduct, tvTimeProduct, tvPriceProduct;
        public LinearLayout viewHolderProduct;

        //
        private TextView tvSoNguoiThichSP;
        private TextView tvSoNguoiMuaSP;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvTimeProduct = itemView.findViewById(R.id.tvTimeProduct);
            viewHolderProduct = itemView.findViewById(R.id.viewHolderProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvSoNguoiThichSP = itemView.findViewById(R.id.tvSoNguoiThichSP);
            tvSoNguoiMuaSP = itemView.findViewById(R.id.tvSoNguoiMuaSP);
        }
    }
}
