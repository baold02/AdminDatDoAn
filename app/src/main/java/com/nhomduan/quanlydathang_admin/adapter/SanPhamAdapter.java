package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnStopProduct;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SanPhamViewHolder> {

    private List<Product> sanPhamList;
    private OnClickItem onClickItem;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();


    public SanPhamAdapter(List<Product> sanPhamList, OnClickItem onClickItem) {
        this.sanPhamList = sanPhamList;
        this.onClickItem = onClickItem;
    }

    @NonNull
    @Override
    public SanPhamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham, parent, false);
        return new SanPhamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamViewHolder holder, int position) {
        Product product = sanPhamList.get(position);
        if (product == null) {
            return;
        }

        viewBinderHelper.bind(holder.item, product.getId());

        Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_image)
                .into(holder.imgSanPham);
        holder.tvTenSanPham.setText("Tên SP: " + product.getName());
        holder.tvSoLuotThich.setText("Số Lượng yêu thích : " + product.getRate());

        holder.itemSanPham.setOnClickListener(view -> onClickItem.onClickItem(product.getId()));
        holder.tvDelete.setOnClickListener(view -> onClickItem.onDeleteItem(product));
        holder.tvEdit.setOnClickListener(view -> onClickItem.onUpdateItem(product));

    }

    @Override
    public int getItemCount() {
        if (sanPhamList != null) {
            return sanPhamList.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> productList) {
        this.sanPhamList = productList;
        notifyDataSetChanged();
    }

    public class SanPhamViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout item;
        private TextView tvEdit;
        private TextView tvDelete;
        private ImageView imgSanPham;
        private TextView tvTenSanPham;
        private TextView tvSoLuotThich;
        private LinearLayout itemSanPham;

        public SanPhamViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
            tvTenSanPham = itemView.findViewById(R.id.tvTenSanPham);
            tvSoLuotThich = itemView.findViewById(R.id.tvSoLuotThich);
            itemSanPham = itemView.findViewById(R.id.item_san_pham);
        }
    }
}
