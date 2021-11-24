package com.nhomduan.quanlydathang_admin.fragment;

import static android.app.Activity.RESULT_OK;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.getExtensionFile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.LoaiSanPhamUtils;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.Utils.ProductUtils;
import com.nhomduan.quanlydathang_admin.adapter.LoaiSPSpinnerAdapter;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UpdateSanPhamFragment extends Fragment {

    private static final int REQUEST_CODE_IMG = 1;

    private ImageView imgAnhChinhSP;
    private EditText edTenSP;
    private Spinner spLoaiSP;
    private EditText edGiaBan;
    private EditText edGiamGia;
    private EditText edMoTa;
    private EditText edBaoQuan;
    private EditText edHuongDanBaoQuan;
    private EditText edThoiGianCheBien;
    private Button btnLuuSanPham;

    private Product product;

    private String loaiSPId;
    LoaiSPSpinnerAdapter loaiSPAdapter;

    private Uri imgUri;
    private boolean flagSuaAnh;

    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private FragmentManager fragmentManager;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chi_tiet_san_pham, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        getDuLieu();
        setUpThongTin(product);

        btnLuuSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = validateInputSP();
                if(product != null) {
                    chinhSuaSanPham(product);
                }
            }
        });


        imgAnhChinhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagSuaAnh = true;
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_IMG);
            }
        });

    }




    private void chinhSuaSanPham(Product product) {
        makeProgressBar();
        ProductUtils.getDbRfProduct().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                if(dataSnapshot != null) {
                    List<Product> productList = ProductUtils.getProductUser(dataSnapshot);
                    boolean valid = checkProduct(product, productList);
                    if(valid) {
                        if(flagSuaAnh) {
                            if(imgUri == null) {
                                OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh");
                                progressDialog.cancel();
                                return;
                            }
                            getImgLinkAndPostSanPham(product, imgUri);
                        } else {
                            product.setImage(UpdateSanPhamFragment.this.product.getImage());
                            postSanPham(product);
                        }
                    } else {
                        OverUtils.makeToast(getContext(), "Trùng tên sản phẩm trong hệ thống");
                        progressDialog.cancel();
                    }
                } else {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    progressDialog.cancel();
                }
            }
        });
    }

    private void getImgLinkAndPostSanPham(Product product, Uri imgUri) {
        StorageReference stRef = ProductUtils.getStRfProduct().child("image/sanpham")
                .child(product.getId() + "*" + System.currentTimeMillis() + "." + getExtensionFile(getContext(), imgUri));
        stRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                stRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        product.setImage(String.valueOf(uri));
                        postSanPham(product);
                    }
                });
            }
        });
    }

    private void postSanPham(Product product) {
        ProductUtils.getDbRfProduct().child(product.getId())
                .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    OverUtils.makeToast(getContext(), "Sửa thành công");
                    UpdateSanPhamFragment.this.product.setName(product.getName());
                               progressDialog.cancel();
                } else {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    progressDialog.cancel();
                }
            }
        });
    }

    private boolean checkProduct(Product product, List<Product> productList) {
        for (Product pr : productList) {
            if (pr.getName().equals(product.getName())) {
                if(pr.getName().equals(this.product.getName())) {
                    continue;
                }
                OverUtils.makeToast(getContext(), "Đã tồn tại sản phẩm này");
                return false;
            }
        }
        return true;
    }

    private Product validateInputSP() {
        Product product = new Product();
        String name = edTenSP.getText().toString().trim();
        String giaBan = edGiaBan.getText().toString().trim();
        String loaiSP = loaiSPId;
        String giamGia = edGiamGia.getText().toString().trim();
        String moTaSP = edMoTa.getText().toString().trim();
        String baoQuan = edBaoQuan.getText().toString().trim();
        String huongDanBaoQuan = edHuongDanBaoQuan.getText().toString().trim();
        String thoiGianCheBien = edThoiGianCheBien.getText().toString().trim();

        if (name.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm thông tin sản phẩm");
            return null;
        }
        if (giaBan.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm giá bán");
            return null;
        } else {
            try {
                Integer.parseInt(giaBan);
            } catch (Exception e) {
                OverUtils.makeToast(getContext(), "thông tin giảm giá là một số");
                return null;
            }
        }
        if (giamGia.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm thông tin giảm giá");
        } else {
            try {
                Double.parseDouble(giamGia);
            } catch (Exception e) {
                OverUtils.makeToast(getContext(), "thông tin giảm giá là một số ( vd: 0.3 ) ");
                return null;
            }
        }

        try {
            Integer.parseInt(thoiGianCheBien);
        } catch (Exception e) {
            OverUtils.makeToast(getContext(), "thời gian chế biến là một số (phút) ");
            return null;
        }

        product.setId(this.product.getId());
        product.setName(name);
        product.setGia_ban(Integer.parseInt(giaBan));
        product.setLoai_sp(loaiSP);
        product.setKhuyen_mai(Float.parseFloat(giamGia));
        product.setBao_quan(baoQuan);
        product.setMota(moTaSP);
        product.setThong_tin_bao_quan(huongDanBaoQuan);
        product.setThoiGianCheBien(Integer.parseInt(thoiGianCheBien));

        return product;
    }

    private void setUpThongTin(Product product) {
        if(product != null) {
            edTenSP.setText(product.getName());
            edBaoQuan.setText(product.getBao_quan());
            edHuongDanBaoQuan.setText(product.getThong_tin_bao_quan());
            edMoTa.setText(product.getMota());
            edGiamGia.setText(String.valueOf(product.getKhuyen_mai()));
            edThoiGianCheBien.setText(String.valueOf(product.getThoiGianCheBien()));
            edGiaBan.setText(String.valueOf(product.getGia_ban()));
            Picasso.get()
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_image)
                    .into(imgAnhChinhSP);

            // set up loại sản phẩm
            List<LoaiSP> loaiSPList = new ArrayList<>();
            LoaiSanPhamUtils.getDbRfLoaiSP().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if(dataSnapshot != null) {
                        for(DataSnapshot data : dataSnapshot.getChildren()) {
                            LoaiSP loaiSP = data.getValue(LoaiSP.class);
                            loaiSPList.add(loaiSP);
                        }
                        loaiSPAdapter = new LoaiSPSpinnerAdapter(getContext(), loaiSPList);
                        spLoaiSP.setAdapter(loaiSPAdapter);
                        for(int i = 0; i < loaiSPList.size(); i++) {
                            if(product.getId().equals(loaiSPList.get(i).getId())) {
                                spLoaiSP.setSelection(i);
                            }
                        }

                    } else {
                        OverUtils.makeToast(getContext(),ERROR_MESSAGE);
                    }
                }
            });
            spLoaiSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    LoaiSP loaiSP = loaiSPList.get(i);
                    loaiSPId = loaiSP.getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } else {
            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
        }
    }


    private void getDuLieu() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            Product product = (Product) bundle.getSerializable("product");
            this.product = product;
        }
    }

    private void initView(View view) {
        imgAnhChinhSP = view.findViewById(R.id.img_anhChinhSP);
        edTenSP = view.findViewById(R.id.ed_TenSP);
        spLoaiSP = view.findViewById(R.id.sp_LoaiSP);
        edGiaBan = view.findViewById(R.id.ed_GiaBan);
        edGiamGia = view.findViewById(R.id.ed_GiamGia);
        edMoTa = view.findViewById(R.id.ed_MoTa);
        edBaoQuan = view.findViewById(R.id.ed_BaoQuan);
        edHuongDanBaoQuan = view.findViewById(R.id.ed_HuongDanBaoQuan);
        edThoiGianCheBien = view.findViewById(R.id.ed_ThoiGianCheBien);
        btnLuuSanPham = view.findViewById(R.id.btn_LuuSanPham);

        toolbar = view.findViewById(R.id.toolbar);
        fragmentManager = getParentFragmentManager();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_IMG) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    Uri uri = data.getData();
                    imgUri = uri;
                    imgAnhChinhSP.setImageURI(imgUri);
                }
            } else {
                imgUri = null;
                imgAnhChinhSP.setImageResource(R.drawable.ic_add);
                OverUtils.makeToast(getContext(), "Chưa chọn ảnh");
            }
        }
    }

    private void makeProgressBar() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }
        progressDialog.setMessage("Đang cập nhật ...");
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    fragmentManager.popBackStack();
                    return true;
                }
                return false;
            }
        });

    }



}