package com.nhomduan.quanlydathang_admin.fragment;

import static android.app.Activity.RESULT_OK;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import static okhttp3.internal.Internal.instance;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.LoaiSanPhamUtils;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.Utils.ProductUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.LoaiSPSpinnerAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;

import java.util.ArrayList;
import java.util.List;


public class ThemSanPhamFragment extends Fragment implements View.OnClickListener {


    private static final int REQUEST_CODE_MAIN_IMG = 1;

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
    private ProgressBar progressBar;
    private EditText edKhauPhan;


    private Uri imgUri;

    private String loaiSPId;
    private FragmentManager fragmentManager;

    private Toolbar toolbar;
    private MainActivity activity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sanpham, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initView(view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        setSpUpLoaiSP();

        btnLuuSanPham.setOnClickListener(this);
        imgAnhChinhSP.setOnClickListener(this);
    }

    private void setSpUpLoaiSP() {
        List<LoaiSP> loaiSPList = new ArrayList<>();
        LoaiSPSpinnerAdapter loaiSPAdapter = new LoaiSPSpinnerAdapter(getContext(), loaiSPList);
        spLoaiSP.setAdapter(loaiSPAdapter);
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
        spLoaiSP.setSelection(0);

        LoaiSanPhamUtils.getDbRfLoaiSP().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if (loaiSP != null) {
                    loaiSPList.add(loaiSP);
                    loaiSPAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if (loaiSP != null) {
                    for (int i = 0; i < loaiSPList.size(); i++) {
                        if (loaiSP.getId().equals(loaiSPList.get(i).getId())) {
                            loaiSPList.set(i, loaiSP);
                        }
                    }
                    loaiSPAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
                if (loaiSP != null) {
                    for (int i = 0; i < loaiSPList.size(); i++) {
                        if (loaiSP.getId().equals(loaiSPList.get(i).getId())) {
                            loaiSPList.remove(i);
                            break;
                        }
                    }
                    loaiSPAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        progressBar = view.findViewById(R.id.progressBar);
        edKhauPhan = view.findViewById(R.id.ed_KhauPhan);

        //
        progressBar.setVisibility(View.INVISIBLE);
        edGiamGia.setText("0");

        toolbar = view.findViewById(R.id.toolbar);
        fragmentManager = getParentFragmentManager();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_anhChinhSP:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_MAIN_IMG);
                break;
            case R.id.btn_LuuSanPham:
                Product product = validateInputSP();
                if (product != null) {
                    luuSanPham(getContext(), product);
                }
                break;

        }
    }

    private void luuSanPham(Context context, Product product) {
        if (imgUri == null) {
            OverUtils.makeToast(getContext(), "Bạn cần chọn ảnh trước khi post sản phẩm");
            return;
        }

        StorageReference fileRef =
                ProductUtils.getStRfProduct().child(System.currentTimeMillis() + "." + OverUtils.getExtensionFile(context, imgUri));
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUri = String.valueOf(uri);
                        product.setImage(imageUri);
                        postSanPham(product);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }


    private void postSanPham(Product product) {
        String key = ProductUtils.getDbRfProduct().push().getKey();
        product.setId(key);
        ProductDao.getInstance().getAllProduct(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<Product> productList = (List<Product>) obj;
                if (!productList.isEmpty()) {
                    boolean valid = checkProduct(product, productList);
                    if (valid) {
                        ProductDao.getInstance().insertProduct(product, new IAfterInsertObject() {
                            @Override
                            public void onSuccess(Object obj) {
                                OverUtils.makeToast(getContext(), "Thêm thành công");
                                progressBar.setVisibility(View.INVISIBLE);
                                clearForm();
                                capNhatSoSanPhamCuaLoai(product.getLoai_sp());
                            }

                            @Override
                            public void onError(DatabaseError exception) {
                                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }









//    ProductUtils.getDbRfProduct().child(product.getId())
//            .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
//        @Override
//        public void onComplete(@NonNull Task<Void> task) {
//            if (task.isSuccessful()) {
//                OverUtils.makeToast(getContext(), "Thêm thành công");
//                progressBar.setVisibility(View.INVISIBLE);
//                clearForm();
//                capNhatSoSanPhamCuaLoai(product.getLoai_sp());
//            } else {
//                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
//            }
//        }
//    });

    private void capNhatSoSanPhamCuaLoai(String loai_sp) {
        LoaiSanPhamUtils.getDbRfLoaiSP().child(loai_sp).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    LoaiSP loaiSP = task.getResult().getValue(LoaiSP.class);
                    if (loaiSP != null) {
                        loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() + 1);
                        LoaiSanPhamUtils.getDbRfLoaiSP().child(loai_sp).setValue(loaiSP);
                    }
                }
            }
        });
    }

    private void clearForm() {
        edTenSP.setText("");
        edGiamGia.setText("0");
        edBaoQuan.setText("");
        edMoTa.setText("");
        edGiaBan.setText("");
        edHuongDanBaoQuan.setText("");
        edThoiGianCheBien.setText("");
        spLoaiSP.setSelection(0);
        imgAnhChinhSP.setImageResource(R.drawable.ic_add);
        imgUri = null;
    }

    private boolean checkProduct(Product product, List<Product> productList) {
        for (Product pr : productList) {
            if (pr.getName().equals(product.getName())) {
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
        String khauPhan = edKhauPhan.getText().toString().trim();

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
        if (khauPhan.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần nhập thông tin khẩu phần");
            return null;
        }

        product.setName(name);
        product.setGia_ban(Integer.parseInt(giaBan));
        product.setLoai_sp(loaiSP);
        product.setKhuyen_mai(Float.parseFloat(giamGia));
        product.setBao_quan(baoQuan);
        product.setMota(moTaSP);
        product.setThong_tin_bao_quan(huongDanBaoQuan);
        product.setThoiGianCheBien(Integer.parseInt(thoiGianCheBien));
        product.setKhau_phan(khauPhan);
        return product;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAIN_IMG) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    imgAnhChinhSP.setImageURI(data.getData());
                    imgUri = uri;
                }
            } else {
                imgAnhChinhSP.setImageResource(R.drawable.ic_add);
                imgUri = null;
            }

        }
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
                    Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

    }


}
