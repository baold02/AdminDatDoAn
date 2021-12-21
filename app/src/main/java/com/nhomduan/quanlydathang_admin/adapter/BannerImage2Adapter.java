package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.model.BannerImage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerImage2Adapter extends RecyclerView.Adapter<BannerImage2Adapter.ViewHolder> {

    public Context context;
    public List<BannerImage> bannerImageList;

    public BannerImage2Adapter(Context context, List<BannerImage> bannerImageList) {
        this.context = context;
        this.bannerImageList = bannerImageList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BannerImage> bannerImageList) {
        this.bannerImageList = bannerImageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_image_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BannerImage bannerImage = bannerImageList.get(position);
        if(bannerImage != null) {
            Picasso.get()
                    .load(bannerImage.getImage())
                    .placeholder(R.drawable.ic_image)
                    .into(holder.imgBanner);

            holder.item.setOnClickListener(v -> {
//                     Intent intent = new Intent(context, ShowProductActivity.class);
//                     intent.putExtra("productId", bannerImage.getProductId());
//                     context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(bannerImageList != null) {
            return bannerImageList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout item;
        private ImageView imgBanner;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            imgBanner = itemView.findViewById(R.id.imgBanner);
        }
    }
}
