package com.nhomduan.quanlydathang_admin.adapter;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.AdminDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterDeleteObject;
import com.nhomduan.quanlydathang_admin.model.Admin;
import com.nhomduan.quanlydathang_admin.model.Permission;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private List<Admin> adminList;
    private Context context;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Admin> adminList) {
        this.adminList = adminList;
        notifyDataSetChanged();
    }

    public AdminAdapter(Context context, List<Admin> adminList) {
        this.adminList = adminList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Admin admin = adminList.get(position);
        if (admin != null) {
            holder.tvAdmin.setText("Admin : " + admin.getUserName());
            if (admin.getPermission().equals(Permission.TOAN_QUYEN)) {
                holder.tvPermission.setText("Quyền : Toàn quyền");
                holder.btnDelete.setVisibility(View.GONE);
            } else {
                holder.tvPermission.setText("Quyền : Giới hạn quyền");
                holder.btnDelete.setOnClickListener(v ->
                        AdminDao.getInstance().deleteAdmin(context, admin, new IAfterDeleteObject() {
                            @Override
                            public void onSuccess(Object obj) {
                                OverUtils.makeToast(context, "Xóa thành công!");
                            }

                            @Override
                            public void onError(DatabaseError error) {
                                OverUtils.makeToast(context, ERROR_MESSAGE);
                            }
                        }));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (adminList != null) {
            return adminList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAdmin;
        private TextView tvPermission;
        private CardView btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdmin = itemView.findViewById(R.id.tvAdmin);
            tvPermission = itemView.findViewById(R.id.tvPermission);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
