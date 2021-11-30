package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LoaiSPAdapter extends RecyclerView.Adapter<LoaiSPAdapter.LoaiSPViewHolder> {

    private List<LoaiSP> loaiSPList;
    private OnClickItem onClickItem;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public LoaiSPAdapter(List<LoaiSP> loaiSPList, OnClickItem onClickItem) {
        this.loaiSPList = loaiSPList;
        this.onClickItem = onClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<LoaiSP> loaiSPList) {
        this.loaiSPList = loaiSPList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LoaiSPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loai_sp, parent, false);
        return new LoaiSPViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoaiSPViewHolder holder, int position) {
        LoaiSP loaiSP = loaiSPList.get(position);
        if (loaiSP == null) {
            return;
        }

        viewBinderHelper.bind(holder.item, loaiSP.getName());
        viewBinderHelper.bind(holder.item, loaiSP.getName());

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onDeleteItem(loaiSP);
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onUpdateItem(loaiSP);
            }
        });

        holder.tvTenLoaiSP.setText("Tên loại sp: " + loaiSP.getName());
        holder.tvSoLuongOfLoai.setText("Số lượng sản phẩm hiện có : " + loaiSP.getSoSanPhamThuocLoai());


        String imgUri = null;
        imgUri = loaiSP.getHinhanh();
        Picasso.get()
                .load(imgUri)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(holder.imvLoaiSP);


    }

    @Override
    public int getItemCount() {
        if (loaiSPList != null) {
            return loaiSPList.size();
        }
        return 0;
    }

    public class LoaiSPViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout item;
        private TextView tvEdit;
        private TextView tvDelete;
        private ImageView imvLoaiSP;
        private TextView tvTenLoaiSP;
        private TextView tvSoLuongOfLoai;


        public LoaiSPViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            imvLoaiSP = itemView.findViewById(R.id.imvLoaiSP_s);
            tvTenLoaiSP = itemView.findViewById(R.id.tvTenLoaiSP);
            tvSoLuongOfLoai = itemView.findViewById(R.id.tvSoLuongOfLoai);
        }
    }
}
