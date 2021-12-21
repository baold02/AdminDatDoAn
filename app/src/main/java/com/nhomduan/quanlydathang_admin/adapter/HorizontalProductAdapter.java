package com.nhomduan.quanlydathang_admin.adapter;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.MyDiffUtil;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.OnAddToCard;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.Function;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.PopularViewHolder> {
    private List<Product> productList;
    private final OnClickItem onClickItem;
    private final OnAddToCard onAddToCard;
    private final Function function;

    public HorizontalProductAdapter(List<Product> productList, OnClickItem onClickItem, OnAddToCard onAddToCard, Function function) {
        this.onClickItem = onClickItem;
        this.onAddToCard = onAddToCard;
        this.function = function;
        this.productList = productList;
    }

    public void setData(List<Product> productList) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffUtil(this.productList, productList));
        this.productList = productList;
        result.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public PopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new PopularViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }

        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgProduct);
        holder.tvNameProduct.setText(product.getName());
        holder.tvPriceProduct.setText(OverUtils.currencyFormat.format(product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())));
        holder.item.setOnClickListener(v -> onClickItem.onClickItem(product.getId()));

        holder.btnAddProduct.setOnClickListener(v -> onAddToCard.onAddToCard(product));
        if (function.equals(Function.Pho_Bien)) {
            holder.tvSoLuongDanBan.setText(product.getSo_luong_da_ban() + " ps");
            holder.tvNew.setVisibility(View.INVISIBLE);
            holder.tvKhuyenMai.setVisibility(View.INVISIBLE);
        } else if (function.equals(Function.Khuyen_Mai)) {
            holder.tvSoLuongDanBan.setVisibility(View.GONE);
            holder.tvKhuyenMai.setVisibility(View.VISIBLE);
            holder.tvKhuyenMai.setText("Sale " + (int) (product.getKhuyen_mai() * 100) + "%");
            holder.tvNew.setVisibility(View.INVISIBLE);
        } else if (function.equals(Function.Moi_Nhat)) {
            holder.tvSoLuongDanBan.setVisibility(View.GONE);
            holder.tvKhuyenMai.setVisibility(View.INVISIBLE);
            holder.tvNew.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public class PopularViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private ImageView imgProduct;
        private TextView tvNameProduct;
        private TextView tvPriceProduct;
        private TextView btnAddProduct;
        private TextView tvSoLuongDanBan;
        private TextView tvKhuyenMai;
        private TextView tvNew;

        public PopularViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            btnAddProduct = itemView.findViewById(R.id.btnAddProduct);
            tvSoLuongDanBan = itemView.findViewById(R.id.tvSoLuongDanBan);
            tvKhuyenMai = itemView.findViewById(R.id.tvKhuyenMai);
            tvNew = itemView.findViewById(R.id.tvNew);
        }
    }
}
