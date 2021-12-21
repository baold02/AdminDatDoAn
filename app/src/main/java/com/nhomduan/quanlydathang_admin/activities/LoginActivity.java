package com.nhomduan.quanlydathang_admin.activities;

import static com.google.firebase.database.core.RepoManager.clear;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.AdminDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IDone;
import com.nhomduan.quanlydathang_admin.model.Admin;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageView2;
    private TextInputLayout tILEdtTenDangNhap;
    private TextInputEditText edtTenDangNhap;
    private TextInputLayout textInputLayout;
    private TextInputEditText edtMatKhau;
    private AppCompatCheckBox chkLuuMatKhau;
    private AppCompatButton btnDangNhap;
    private AppCompatButton btnHuyDangNhap;

    public static String loginedUserName;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        readInfo();

        btnHuyDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForm();
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenDangNhap = edtTenDangNhap.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                boolean remember;
                if(chkLuuMatKhau.isChecked()) {
                    remember = true;
                } else {
                    remember = false;
                }
                if(tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    return;
                }
                verifyAccount(tenDangNhap, matKhau, done -> {
                    if(done) {
                        remember(tenDangNhap, matKhau, remember);
                        loginedUserName = tenDangNhap;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });



    }

    private void verifyAccount(String tenDangNhap, String matKhau, IDone iDone) {
        AdminDao.getInstance().getAdminByUserName(tenDangNhap, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    Admin admin = (Admin) obj;
                    if(admin.getUserName() == null) {
                        OverUtils.makeToast(LoginActivity.this, "Tài khoản không tồn tại");
                        iDone.onDone(false);
                    } else {
                        if(!matKhau.equals(admin.getPassword())) {
                            OverUtils.makeToast(LoginActivity.this, "Mật khẩu không đúng");
                            iDone.onDone(false);
                        } else {
                            iDone.onDone(true);
                        }
                    }
                } else {
                    OverUtils.makeToast(LoginActivity.this, "Tài khoản không tồn tại");
                }

            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(LoginActivity.this, ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }

    private void clearForm() {
        edtTenDangNhap.setText("");
        edtMatKhau.setText("");
        chkLuuMatKhau.setChecked(false);
    }

    private void readInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
        boolean remember = sharedPreferences.getBoolean("remember", false);
        String tenDangNhap = sharedPreferences.getString("username", "");
        String matKhau = sharedPreferences.getString("password", "");
        if(remember) {
            edtTenDangNhap.setText(tenDangNhap);
            edtMatKhau.setText(matKhau);
            chkLuuMatKhau.setChecked(true);
        }
    }

    private void remember(String tenDangNhap, String matKhau, boolean remember) {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(remember) {
            editor.putString("username", tenDangNhap);
            editor.putString("password", matKhau);
            editor.putBoolean("remember", remember);
            editor.apply();
        } else {
            editor.clear();
        }
    }

    private void initView() {
        imageView2 = findViewById(R.id.imageView2);
        tILEdtTenDangNhap = findViewById(R.id.tIL_edtTenDangNhap);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        textInputLayout = findViewById(R.id.textInputLayout);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        chkLuuMatKhau = findViewById(R.id.chkLuuMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnHuyDangNhap = findViewById(R.id.btnHuyDangNhap);
    }
}
