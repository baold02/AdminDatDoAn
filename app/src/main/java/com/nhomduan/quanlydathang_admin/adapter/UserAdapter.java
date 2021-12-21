package com.nhomduan.quanlydathang_admin.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnLockUser;
import com.nhomduan.quanlydathang_admin.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private OnClickItem onClickItem;
    private OnLockUser onLockUser;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public UserAdapter(List<User> userList, OnClickItem onClickItem, OnLockUser onLockUser) {
        this.userList = userList;
        this.onClickItem = onClickItem;
        this.onLockUser = onLockUser;
    }

    public void setData(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = userList.get(position);
        if(user == null) {
            return;
        }
        viewBinderHelper.bind(holder.item, user.getUsername());

        if(user.isEnable()) {
            holder.tvLock.setText("Lock");
        } else {
            holder.tvLock.setText("Unlock");
        }
        holder.tvUserName.setText("UserName : " + user.getUsername());
        holder.tvSoDienThoai.setText("SĐT : " + user.getPhone_number());
        if(user.getAddress() != null && user.getAddress().length() > 0) {
            holder.tvDiaChi.setText("Địa chỉ: " + user.getAddress());
        } else {
            holder.tvDiaChi.setText("Chưa thêm địa chỉ");
        }


        String imgUser = user.getHinhanh();
        if(imgUser != null) {
            Picasso.get()
                    .load(imgUser)
                    .placeholder(R.drawable.ic_image)
                    .into(holder.imgUser);
        }

        holder.itemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClickItem(user);
            }
        });

        holder.tvLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.isEnable()) {
                    user.setEnable(false);
                    userList.set(position,user);
                    notifyDataSetChanged();
                } else {
                   user.setEnable(true);
                    userList.set(position,user);
                    notifyDataSetChanged();
                }
                onLockUser.onLock(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(userList != null) {
            return userList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout item;
        private TextView tvLock;
        private LinearLayout itemUser;
        private ImageView imgUser;
        private TextView tvUserName;
        private TextView tvSoDienThoai;
        private TextView tvDiaChi;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvLock = itemView.findViewById(R.id.tvLock);
            itemUser = itemView.findViewById(R.id.item_user);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvSoDienThoai = itemView.findViewById(R.id.tvSoDienThoai);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
        }
    }
}
