package com.nhomduan.quanlydathang_admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.Shipper;

import java.util.List;

public class ShipperAdapter extends RecyclerView.Adapter<ShipperAdapter.ShipperViewHolder> {
    private List<Shipper>  shipperList;
    private OnClickItem onClickItem;

    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public ShipperAdapter(List<Shipper> shipperList, OnClickItem onClickItem) {
        this.shipperList = shipperList;
        this.onClickItem = onClickItem;
    }

    public void setData(List<Shipper> shipperList) {
        this.shipperList = shipperList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShipperAdapter.ShipperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipper, parent, false);
        return new ShipperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipperAdapter.ShipperViewHolder holder, int position) {
        Shipper shipper = shipperList.get(position);
        if(shipper == null) {
            return;
        }

        viewBinderHelper.bind(holder.item, shipper.getId());
        holder.tvTenShipper.setText(shipper.getName());
        holder.tvSDTShipper.setText(shipper.getPhone_number());
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onUpdateItem(shipper);
            }
        });

        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onDeleteItem(shipper);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(shipperList != null) {
            return shipperList.size();
        }
        return 0;
    }

    public class ShipperViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEdit;
        private TextView tvDelete;
        private LinearLayout itemShipper;
        private TextView tvTenShipper;
        private TextView tvSDTShipper;
        private SwipeRevealLayout item;


        public ShipperViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            itemShipper = itemView.findViewById(R.id.item_shipper);
            tvTenShipper = itemView.findViewById(R.id.tvTenShipper);
            tvSDTShipper = itemView.findViewById(R.id.tvSDTShipper);
            item = itemView.findViewById(R.id.item);
        }
    }
}
