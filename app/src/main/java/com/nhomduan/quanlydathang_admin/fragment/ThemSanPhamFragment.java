package com.nhomduan.quanlydathang_admin.fragment;

import static android.app.Activity.RESULT_OK;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.activities.ShowProductActivity;
import com.nhomduan.quanlydathang_admin.adapter.LoaiSPSpinnerAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.dialog.SingleChoiceDialog;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
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
    private TextView tvTrangThai;





    private Uri imgUri;

    private String loaiSPId;
    private FragmentManager fragmentManager;

    private Toolbar toolbar;
    private MainActivity activity;

    private List<LoaiSP> loaiSPList;
    private LoaiSPSpinnerAdapter loaiSPAdapter;


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
                activity.getSupportFragmentManager().popBackStack();
            }
        });

        setSpUpLoaiSP();
        setUpChonTrangThai();

        btnLuuSanPham.setOnClickListener(this);
        imgAnhChinhSP.setOnClickListener(this);
    }

    private void setUpChonTrangThai() {
        tvTrangThai.setOnClickListener(v -> {
            String[] trangThaiSPList = requireActivity().getResources().getStringArray(R.array.choice_trang_thai_sp);
            SingleChoiceDialog singleChoiceDialog = new SingleChoiceDialog(trangThaiSPList,
                    0,
                    "Chọn trạng thái sản phẩm",
                    "Chọn",
                    "Hủy",
                    new SingleChoiceDialog.ISingleChoiceDialog() {
                        @Override
                        public void onChoice(Object[] objList, int position) {
                            tvTrangThai.setText((String) objList[position]);
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
            singleChoiceDialog.show(requireActivity().getSupportFragmentManager(), "Single choice dialog");
        });
    }

    private void setSpUpLoaiSP() {
        loaiSPList = new ArrayList<>();
        loaiSPAdapter = new LoaiSPSpinnerAdapter(getContext(), loaiSPList);
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

        ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                loaiSPList = (List<LoaiSP>) obj;
                loaiSPAdapter.setData(loaiSPList);
                spLoaiSP.setSelection(0);
            }

            @Override
            public void onError(DatabaseError error) {

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
        tvTrangThai = view.findViewById(R.id.tvTrangThai);

        //
        progressBar.setVisibility(View.INVISIBLE);
        edGiamGia.setText("0");

        toolbar = view.findViewById(R.id.toolbar);
        fragmentManager = getParentFragmentManager();
        activity = (MainActivity) getActivity();

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
                FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + OverUtils.getExtensionFile(context, imgUri));
        fileRef.putFile(imgUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUri = String.valueOf(uri);
                product.setImage(imageUri);
                postSanPham(product);
            }
        })).addOnProgressListener(snapshot -> progressBar.setVisibility(View.VISIBLE)).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }


    private void postSanPham(Product product) {
        String key = FirebaseDatabase.getInstance().getReference("san_pham").push().getKey();
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
                                Intent intent = new Intent(getContext(), ShowProductActivity.class);
                                intent.putExtra("productId", product.getId());
                                startActivity(intent);
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

    private void capNhatSoSanPhamCuaLoai(String loai_spId) {
        ProductTypeDao.getInstance().getProductTypeById(loai_spId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                LoaiSP loaiSP = (LoaiSP) obj;
                if (loaiSP.getId() != null) {
                    loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() + 1);
                    ProductTypeDao.getInstance().updateProductType(loaiSP, loaiSP.toMapSoLuongSanPham());
                }
            }

            @Override
            public void onError(DatabaseError error) {

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
        edKhauPhan.setText("");
        tvTrangThai.setText(getString(R.string.nhan_de_chon_trang_thai));

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
        String trangThai = tvTrangThai.getText().toString().trim();

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
        if(trangThai.equals(getString(R.string.nhan_de_chon_trang_thai))) {
            OverUtils.makeToast(getContext(), "Cần chọn trạng thái trước khi lưu sản phẩm");
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
        product.setTrang_thai(trangThai);
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
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contentFrame, new DanhSachSanPhamFragment())
                            .commit();
                    return true;
                }
                return false;
            }
        });

    }


}
