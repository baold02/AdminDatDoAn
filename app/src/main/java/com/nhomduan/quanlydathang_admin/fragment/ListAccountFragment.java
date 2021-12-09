package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.AdminAdapter;
import com.nhomduan.quanlydathang_admin.dao.AdminDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.model.Admin;
import com.nhomduan.quanlydathang_admin.model.Permission;

import java.util.ArrayList;
import java.util.List;

public class ListAccountFragment extends Fragment {
    private Toolbar toolbar;
    private RecyclerView rcvAdminList;
    private FloatingActionButton btnAddAdmin;

    private AdminAdapter adminAdapter;
    private List<Admin> adminList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        setUpAdminList();
        setUpBtnAddAdmin();
    }

    private void setUpBtnAddAdmin() {
        btnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogToAddAdmin();
            }
        });
    }

    private void openDialogToAddAdmin() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.item_add_admin);

        TextInputEditText edtUserName;
        Button btnHuy;
        Button btnAddAdmin;
        TextInputEditText edtPassword;
        TextInputEditText edtRePassword;
        Spinner spnPermission;


        edtUserName = dialog.findViewById(R.id.edtUserName);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        btnAddAdmin = dialog.findViewById(R.id.btnAddAdmin);
        edtPassword = dialog.findViewById(R.id.edtPassword);
        edtRePassword = dialog.findViewById(R.id.edtRePassword);
        spnPermission = dialog.findViewById(R.id.spnPermission);

        ArrayAdapter<Permission> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, Permission.values());
        spnPermission.setAdapter(arrayAdapter);
        spnPermission.setSelection(0);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserName.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String rePassword = edtRePassword.getText().toString().trim();
                Permission permission = (Permission) spnPermission.getSelectedItem();

                if(userName.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    OverUtils.makeToast(getContext(), "Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                if(!password.equals(rePassword)) {
                    OverUtils.makeToast(getContext(), "Mật khẩu không trùng khớp");
                    return;
                }
                Admin admin = new Admin(userName, password, permission);
                AdminDao.getInstance().getAllAdmin(new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        List<Admin> adminList = (List<Admin>) obj;
                        if(validAdmin(admin, adminList)) {
                            AdminDao.getInstance().insertAdmin(admin, new IAfterInsertObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    OverUtils.makeToast(getContext(), "Thêm admin thành công");
                                    edtUserName.setText("");
                                    edtPassword.setText("");
                                    edtRePassword.setText("");
                                }

                                @Override
                                public void onError(DatabaseError exception) {
                                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                }
                            });
                        } else {
                            OverUtils.makeToast(getContext(), "Trùng tên đăng nhập của admin");
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });


            }
        });
        dialog.show();
    }

    private boolean validAdmin(Admin admin, List<Admin> adminList) {
        for(Admin ad : adminList) {
            if(ad.getUserName().equals(admin.getUserName())) {
                return false;
            }
        }
        return true;
    }

    private void setUpAdminList() {
        adminList = new ArrayList<>();
        adminAdapter = new AdminAdapter(getContext(), adminList);
        rcvAdminList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvAdminList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvAdminList.setAdapter(adminAdapter);

        AdminDao.getInstance().getAllAdminListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                adminList = (List<Admin>) obj;
                adminAdapter.setData(adminList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().requestFocus();
        getView().setFocusableInTouchMode(true);
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                requireActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvAdminList = view.findViewById(R.id.rcvAdminList);
        btnAddAdmin = view.findViewById(R.id.btnAddAdmin);
    }
}