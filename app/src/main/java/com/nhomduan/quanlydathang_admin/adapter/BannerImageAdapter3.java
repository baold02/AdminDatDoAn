package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.model.BannerImage;
import com.nhomduan.quanlydathang_admin.model.CustomUri;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerImageAdapter3 extends RecyclerView.Adapter<BannerImageAdapter3.ViewHolder> {


    private List<BannerImage> bannerImageList;
    private OnClickCustomModuleAdapter onClickCustomModuleAdapter;
    private Context context;
    private List<CustomUri> uriList;

    public List<BannerImage> getBannerImageList() {
        return bannerImageList;
    }

    public BannerImageAdapter3(List<BannerImage> bannerImageList, OnClickCustomModuleAdapter onClickCustomModuleAdapter, Context context, List<CustomUri> uriList) {
        this.bannerImageList = bannerImageList;
        this.onClickCustomModuleAdapter = onClickCustomModuleAdapter;
        this.context = context;
        this.uriList = uriList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BannerImage> bannerImageList, List<CustomUri> uriList) {
        this.bannerImageList = bannerImageList;
        this.uriList = uriList;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<BannerImage> bannerImageList) {
        this.bannerImageList = bannerImageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BannerImageAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_image, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull BannerImageAdapter3.ViewHolder holder, int position) {
        int bannerImagePosition = position;
        BannerImage bannerImage = bannerImageList.get(position);
        if (bannerImage != null) {
            if (uriList.get(position).getUri() != null) {
                holder.imgBannerImage.setImageURI(uriList.get(position).getUri());
            } else {
                if (bannerImage.getImage() == null) {
                    holder.imgBannerImage.setImageResource(R.drawable.ic_add);
                } else {
                    Picasso.get()
                            .load(bannerImage.getImage())
                            .placeholder(R.drawable.ic_image)
                            .into(holder.imgBannerImage);
                }
            }

            holder.imgBannerImage.setOnClickListener(v -> {
                if (bannerImage.getImage() != null) {
                    onClickCustomModuleAdapter.onSeletedImage(position, null);
                } else {
                    onClickCustomModuleAdapter.onSeletedImage(position, bannerImage.getImage());
                }
            });

            ProductDao.getInstance().getAllProduct(new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    List<Product> productList = (List<Product>) obj;
                    Product product = new Product();
                    product.setName("Chưa chọn");
                    productList.add(0, product);
                    ProductSpinnerAdapter productSpinnerAdapter = new ProductSpinnerAdapter(productList, context);
                    holder.spnProduct.setAdapter(productSpinnerAdapter);
                    int selection = 0;
                    if (bannerImage.getProductId() != null) {
                        for (int i = 1; i < productList.size(); i++) {
                            if (productList.get(i).getId().equals(bannerImage.getProductId())) {
                                selection = i;
                            }
                        }
                    }
                    holder.spnProduct.setSelection(selection);
                    holder.spnProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            bannerImage.setProductId(productList.get(position).getId());
                            onClickCustomModuleAdapter.onSeletedFunction(bannerImagePosition, productList.get(position).getId());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });

            holder.imgClear.setOnClickListener(v -> {
                onClickCustomModuleAdapter.onDeleteItem(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (bannerImageList != null) {
            return bannerImageList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgBannerImage;
        private Spinner spnProduct;
        private ImageView imgClear;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBannerImage = itemView.findViewById(R.id.imgBannerImage);
            spnProduct = itemView.findViewById(R.id.spnProduct);
            imgClear = itemView.findViewById(R.id.imgClear);
        }
    }

    public interface OnClickCustomModuleAdapter {
        void onSeletedImage(int position, Object obj);
        void onDeleteItem(int position);
        void onSeletedFunction(int position, Object obj);
    }
}
