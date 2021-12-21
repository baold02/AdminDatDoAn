package com.nhomduan.quanlydathang_admin.fragment;

import static android.app.Activity.RESULT_OK;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;
import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.getExtensionFile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.LoaiSPSpinnerAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.dialog.SingleChoiceDialog;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.IDone;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class UpdateSanPhamFragment extends Fragment {
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
    private TextView tvTrangThai;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    private String productId;
    private Product product;

    private String loaiSPId;
    private List<LoaiSP> loaiSPList;
    private LoaiSPSpinnerAdapter loaiSPAdapter;

    private Uri imgUri;
    private boolean flagSuaAnh;

    private final ActivityResultLauncher<String> getImg =  registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if(uri != null) {
                    imgAnhChinhSP.setImageURI(uri);
                    imgUri = uri;
                    flagSuaAnh = true;
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chi_tiet_san_pham, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        getDuLieu();
        setUpShowProduct();
        setUpGetImg();
        setUpSaveProduct();
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
        tvTrangThai = view.findViewById(R.id.tvTrangThai);
        toolbar = view.findViewById(R.id.toolbar);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang cập nhật ...");
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(view -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void getDuLieu() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            productId = bundle.getString("productId");
        }
    }

    private void setUpShowProduct() {
        ProductDao.getInstance().getProductById(productId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    product = (Product) obj;
                    setUpComponents(product);
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void setUpGetImg() {
        imgAnhChinhSP.setOnClickListener(view -> getImg.launch("image/*"));
    }

    private void setUpSaveProduct() {
        btnLuuSanPham.setOnClickListener(v -> {
            getAndValidateInput(new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    if(obj != null) {
                        Product product = (Product) obj;
                        getImgAndUpdateProduct(product);
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        });
    }

    private void getImgAndUpdateProduct(Product product) {
        if(saveImg) {
            progressDialog.show();
            StorageReference stRef = FirebaseStorage.getInstance().getReference().child("image/sanpham")
                    .child(product.getId() + "*" + System.currentTimeMillis() + "." + getExtensionFile(getContext(), imgUri));
            stRef.putFile(imgUri).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    stRef.getDownloadUrl().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            product.setImage(String.valueOf(task1.getResult()));
                            postSanPham(product);
                        }
                    });
                }
            });
        } else {
            postSanPham(product);
        }
    }


    private void setUpComponents(Product product) {
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
        loaiSPList = new ArrayList<>();
        ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                loaiSPList = (List<LoaiSP>) obj;
                loaiSPAdapter = new LoaiSPSpinnerAdapter(getContext(), loaiSPList);
                spLoaiSP.setAdapter(loaiSPAdapter);
                for (int i = 0; i < loaiSPList.size(); i++) {
                    if (product.getLoai_sp().equals(loaiSPList.get(i).getId())) {
                        spLoaiSP.setSelection(i);
                    }
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
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

        String[] trangThaiSPList = requireActivity().getResources().getStringArray(R.array.choice_trang_thai_sp);
        int position = 0;
        for (int i = 0; i < trangThaiSPList.length; i++) {
            if (product.getTrang_thai().equals(trangThaiSPList[i])) {
                position = i;
            }
        }

        tvTrangThai.setText(trangThaiSPList[position]);
        int finalPosition = position;
        tvTrangThai.setOnClickListener(v -> {
            SingleChoiceDialog singleChoiceDialog = new SingleChoiceDialog(trangThaiSPList,
                    finalPosition,
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


    private void postSanPham(Product product) {
        String loaiSP = this.product.getLoai_sp();
        ProductDao.getInstance().insertProduct(product, new IAfterInsertObject() {
            @Override
            public void onSuccess(Object obj) {
                OverUtils.makeToast(getContext(), "Sửa thành công");
                progressDialog.cancel();
                saveImg = false;
                imgUri = null;
                flagSuaAnh = false;
                if(!loaiSP.equals(product.getLoai_sp())) {
                    capNhatSoLuongLoaiSanPham(loaiSP, -1);
                    capNhatSoLuongLoaiSanPham(product.getLoai_sp(), 1);
                }
                Fragment fragment = new ShowProductFragment();
                Bundle args = new Bundle();
                args.putString("product_id", product.getId());
                fragment.setArguments(args);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentFrame, fragment)
                        .addToBackStack(null)
                        .commit();

            }

            @Override
            public void onError(DatabaseError exception) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                progressDialog.cancel();
            }
        });
    }

    private synchronized void capNhatSoLuongLoaiSanPham(String loaiSPId, int congTru) {
        ProductTypeDao.getInstance().getProductTypeById(loaiSPId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                LoaiSP loaiSP = (LoaiSP) obj;
                if (loaiSP.getId() != null) {
                    if(congTru == -1) {
                        loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() - 1);
                    } else {
                        loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() + 1);
                    }
                    ProductTypeDao.getInstance().updateProductType(loaiSP, loaiSP.toMapSoLuongSanPham());
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    static boolean saveImg;
    private void getAndValidateInput(IAfterGetAllObject iAfterGetAllObject) {
        Product product = new Product();
        String name = edTenSP.getText().toString().trim();
        String giaBan = edGiaBan.getText().toString().trim();
        String loaiSP = loaiSPId;
        String giamGia = edGiamGia.getText().toString().trim();
        String moTaSP = edMoTa.getText().toString().trim();
        String baoQuan = edBaoQuan.getText().toString().trim();
        String huongDanBaoQuan = edHuongDanBaoQuan.getText().toString().trim();
        String thoiGianCheBien = edThoiGianCheBien.getText().toString().trim();
        String trangThai = tvTrangThai.getText().toString().trim();

        if (name.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm thông tin sản phẩm");
            return;
        }
        if (giaBan.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm giá bán");
            return;
        } else {
            try {
                Integer.parseInt(giaBan);
            } catch (Exception e) {
                OverUtils.makeToast(getContext(), "thông tin giảm giá là một số");
                return;
            }
        }
        if (giamGia.isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần thêm thông tin giảm giá");
        } else {
            try {
                Double.parseDouble(giamGia);
            } catch (Exception e) {
                OverUtils.makeToast(getContext(), "thông tin giảm giá là một số ( vd: 0.3 ) ");
                return;
            }
        }

        try {
            Integer.parseInt(thoiGianCheBien);
        } catch (Exception e) {
            OverUtils.makeToast(getContext(), "thời gian chế biến là một số (phút) ");
            return;
        }

        if(flagSuaAnh && imgUri == null) {
            OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh sản phẩm");
            return;
        } else if(!flagSuaAnh && imgUri == null) {
            product.setImage(this.product.getImage());
            saveImg = false;
        } else {
            saveImg = true;
        }


        int soChoPhepLap;
        if(name.equals(this.product.getName())) {
            soChoPhepLap = 1;
        } else {
            soChoPhepLap = 0;
        }
        ProductDao.getInstance().isDuplicateProductName(name, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    if((Boolean) obj) {
                        OverUtils.makeToast(getContext(), "Tên sản phẩm trùng lặp");
                    } else {
                        product.setId(UpdateSanPhamFragment.this.product.getId());
                        product.setSo_luong_da_ban(UpdateSanPhamFragment.this.product.getSo_luong_da_ban());
                        product.setRate(UpdateSanPhamFragment.this.product.getRate());
                        product.setName(name);
                        product.setGia_ban(Integer.parseInt(giaBan));
                        product.setLoai_sp(loaiSP);
                        product.setKhuyen_mai(Float.parseFloat(giamGia));
                        product.setBao_quan(baoQuan);
                        product.setMota(moTaSP);
                        product.setThong_tin_bao_quan(huongDanBaoQuan);
                        product.setThoiGianCheBien(Integer.parseInt(thoiGianCheBien));
                        product.setTrang_thai(trangThai);
                        iAfterGetAllObject.iAfterGetAllObject(product);
                    }
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        }, soChoPhepLap);
    }



}