package com.nhomduan.quanlydathang_admin.adapter;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.OnAddToCard;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.BannerImage;
import com.nhomduan.quanlydathang_admin.model.CustomModule;
import com.nhomduan.quanlydathang_admin.model.Function;
import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> implements OnClickItem, OnAddToCard {

    private Context mContext;
    private List<CustomModule> customModuleList;
    private OnClickItem onClickItem;

    public ModuleAdapter(Context mContext, List<CustomModule> customModuleList, OnClickItem onClickItem) {
        this.mContext = mContext;
        this.customModuleList = customModuleList;
        this.onClickItem = onClickItem;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CustomModule> customModuleList) {
        this.customModuleList = customModuleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_module, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomModule customModule = customModuleList.get(position);
        if (customModule != null) {
            holder.tvTitle.setText(customModule.getTitle());


            List<BannerImage> bannerImageList = customModule.getImageList();

            Handler handler = new Handler();
            Runnable runnable = () -> {
                if (holder.viewpager.getCurrentItem() == bannerImageList.size() - 1) {
                    holder.viewpager.setCurrentItem(0);
                } else {
                    holder.viewpager.setCurrentItem(holder.viewpager.getCurrentItem() + 1);
                }
            };

            BannerImage2Adapter bannerImage2Adapter = new BannerImage2Adapter(mContext, bannerImageList);
            holder.viewpager.setAdapter(bannerImage2Adapter);
            holder.circleindicator.setViewPager(holder.viewpager);
            holder.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 5000);
                }
            });

            holder.cvDeleteModule.setOnClickListener(v -> {
                onClickItem.onDeleteItem(customModule);
            });

            holder.cvEditModule.setOnClickListener(v -> {
                onClickItem.onUpdateItem(customModule);
            });

            holder.rcvProductList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            if (customModule.getFunction().equals(Function.Khuyen_Mai)) {
                ProductDao.getInstance().getSanPhamKhuyenMai(customModule.getSoLuong(), new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        setUpAdapters(holder.rcvProductList,obj, customModule.getFunction());
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(mContext, ERROR_MESSAGE);
                    }
                });
            } else if (customModule.getFunction().equals(Function.Pho_Bien)) {
                ProductDao.getInstance().getSanPhamPhoBien(customModule.getSoLuong(), new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        setUpAdapters(holder.rcvProductList,obj, customModule.getFunction());
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(mContext, ERROR_MESSAGE);
                    }
                });
            } else if (customModule.getFunction().equals(Function.Moi_Nhat)) {
                ProductDao.getInstance().getSanPhamMoi(customModule.getSoLuong(), new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        setUpAdapters(holder.rcvProductList,obj, customModule.getFunction());
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(mContext, ERROR_MESSAGE);
                    }
                });
            }


        }
    }

    private void setUpAdapters(RecyclerView recyclerView, Object obj, Function function) {
        List<Product> productList = (List<Product>) obj;
        HorizontalProductAdapter horizontalProductAdapter =
                new HorizontalProductAdapter(productList, ModuleAdapter.this, ModuleAdapter.this, function);
        recyclerView.setAdapter(horizontalProductAdapter);
    }

    @Override
    public int getItemCount() {
        if (customModuleList != null) {
            return customModuleList.size();
        }
        return 0;
    }

    @Override
    public void onClickItem(Object obj) {
//        String productId = (String) obj;
//        Intent intent = new Intent(mContext, ShowProductActivity.class);
//        intent.putExtra("productId", productId);
//        mContext.startActivity(intent);
    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }

    @Override
    public void onAddToCard(Product product) {
        OverUtils.makeToast(mContext, "Khánh hàng thêm sản phẩm " + product.getName() + " vào giỏ");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ViewPager2 viewpager;
        private CircleIndicator3 circleindicator;
        private RecyclerView rcvProductList;
        private CardView cvDeleteModule;
        private CardView cvEditModule;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            viewpager = itemView.findViewById(R.id.viewpager);
            circleindicator = itemView.findViewById(R.id.circleindicator);
            rcvProductList = itemView.findViewById(R.id.rcvProductList);
            cvDeleteModule = itemView.findViewById(R.id.cvDeleteModule);
            cvEditModule = itemView.findViewById(R.id.cvEditModule);
        }
    }
}
