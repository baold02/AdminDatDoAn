package com.nhomduan.quanlydathang_admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.fragment.AccountFragment;
import com.nhomduan.quanlydathang_admin.fragment.DanhSachDonHangFragment;
import com.nhomduan.quanlydathang_admin.fragment.DanhSachLoaiSanPhamFragment;
import com.nhomduan.quanlydathang_admin.fragment.DanhSachSanPhamFragment;
import com.nhomduan.quanlydathang_admin.fragment.DanhSachUserFragment;
import com.nhomduan.quanlydathang_admin.fragment.DashBroadFragment;
import com.nhomduan.quanlydathang_admin.fragment.EditAccountFragment;
import com.nhomduan.quanlydathang_admin.fragment.ShipperFragment;
import com.nhomduan.quanlydathang_admin.fragment.ThongKeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    private long backpressedTime;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        replaceFragment(new DanhSachDonHangFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_tool) {
            mDrawerLayout.openDrawer(GravityCompat.END);
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_donHang:
                replaceFragment(new DanhSachDonHangFragment());
                break;
            case R.id.nav_sanPham:
                replaceFragment(new DanhSachSanPhamFragment());
                break;
            case R.id.nav_loaiSP:
                replaceFragment(new DanhSachLoaiSanPhamFragment());
                break;
            case R.id.nav_quanLyUser:
                replaceFragment(new DanhSachUserFragment());
                break;
            case R.id.nav_thongKe:
                replaceFragment(new ThongKeFragment());
                break;
            case R.id.nav_quanLyShipper:
                replaceFragment(new ShipperFragment());
                break;
            case R.id.nav_TaiKhoan:
                replaceFragment(new AccountFragment());
                break;
            case R.id.nav_DieuKhien:
                replaceFragment(new DashBroadFragment());
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.END);

        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                if (backpressedTime + 2000 > System.currentTimeMillis()) {
                    toast.cancel();
                    super.onBackPressed();
                } else {
                    toast = Toast.makeText(MainActivity.this, "Nhấn trở về hai lần để thoát ứng dụng", Toast.LENGTH_LONG);
                    toast.show();
                }
                backpressedTime = System.currentTimeMillis();
            }

        }
    }

}