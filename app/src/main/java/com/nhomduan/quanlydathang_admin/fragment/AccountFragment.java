package com.nhomduan.quanlydathang_admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.activities.LoginActivity;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.model.Admin;
import com.nhomduan.quanlydathang_admin.model.Permission;

public class AccountFragment extends Fragment {
    private Toolbar toolbar;
    private TextView tvChinhSuaTaiKhoan;
    private TextView tvXemTaiKhoan;
    private TextView tvLogOut;
    private CardView cardview;

    private MainActivity mainActivity;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();

        if(!LoginActivity.loginedUserName.equals("Admin")) {
            cardview.setVisibility(View.GONE);
        } else {
            tvXemTaiKhoan.setVisibility(View.VISIBLE);
            tvXemTaiKhoan.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFrame, new ListAccountFragment())
                        .addToBackStack(null)
                        .commit();
            });
        }

        tvChinhSuaTaiKhoan.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new EditAccountFragment())
                    .addToBackStack(null)
                    .commit();
        });

        tvLogOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void setUpToolbar() {
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) requireActivity();
        mainActivity.setSupportActionBar(toolbar);
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        tvChinhSuaTaiKhoan = view.findViewById(R.id.tvChinhSuaTaiKhoan);
        tvXemTaiKhoan = view.findViewById(R.id.tvXemTaiKhoan);
        tvLogOut = view.findViewById(R.id.tvLogOut);
        cardview = view.findViewById(R.id.cardview);

    }
}