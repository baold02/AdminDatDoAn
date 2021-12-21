package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.UserAdapter;
import com.nhomduan.quanlydathang_admin.dao.UserDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnLockUser;
import com.nhomduan.quanlydathang_admin.model.User;

import java.util.ArrayList;
import java.util.List;


public class DanhSachUserFragment extends Fragment implements OnClickItem, OnLockUser {

    private Toolbar toolbar;
    private RecyclerView rcvDanhSachUser;

    private MainActivity activity;

    private List<User> userList;
    private UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_danh_sach_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        setUpListUser();

    }

    private void setUpListUser() {
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this, this);
        rcvDanhSachUser.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvDanhSachUser.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvDanhSachUser.setAdapter(userAdapter);
        getUserList();
    }

    private void getUserList() {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                userList = (List<User>) obj;
                userAdapter.setData(userList);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvDanhSachUser = view.findViewById(R.id.rcvDanhSachUser);
    }

    @Override
    public void onClickItem(Object obj) {
        User user = (User) obj;
        Fragment fragment = new DetailUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", user.getUsername());
        fragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }

    @Override
    public void onLock(User user) {

        UserDao.getInstance().updateUser(user, user.toMap(), new IAfterUpdateObject() {
            @Override
            public void onSuccess(Object obj) {
                OverUtils.makeToast(getContext(), "Thành công");
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }


}