package com.nhomduan.quanlydathang_admin.fragment;

import static android.app.Activity.RESULT_OK;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.LoaiSPAdapter;
import com.nhomduan.quanlydathang_admin.dao.OrderDao;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.dao.UserDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterDeleteObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.interface_.IDone;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.interface_.OnDone2;
import com.nhomduan.quanlydathang_admin.interface_.onDone3;
import com.nhomduan.quanlydathang_admin.model.DonHang;
import com.nhomduan.quanlydathang_admin.model.DonHangChiTiet;
import com.nhomduan.quanlydathang_admin.model.GioHang;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.nhomduan.quanlydathang_admin.model.TrangThai;
import com.nhomduan.quanlydathang_admin.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DanhSachLoaiSanPhamFragment extends Fragment implements OnClickItem {
    private static final int REQUEST_CODE_IMG_URI_WHEN_ADD = 1;
    private static final int REQUEST_CODE_IMG_URI_WHEN_EDIT = 2;

    private FloatingActionButton btnThemLoai;
    private RecyclerView rcvLoaiSP;
    private List<LoaiSP> loaiSPList;
    private LoaiSPAdapter loaiSPAdapter;


    // dialog thêm
    private Dialog addDialog;
    private EditText dgAddEdtTenLoaiSP;
    private ImageView dgAddImgLoaiSP;
    private Button btnThemLoaiSP;
    private Button btnHuyThemLoaiSP;
    private ProgressBar dgAddLoading;

    // dialog sua
    private Dialog editDialog;
    private EditText dgEditEdtTenLoaiSP;
    private ImageView dgEditImgLoaiSP;
    private Button btnSuaLoaiSP;
    private Button btnHuySuaLoaiSP;
    private ProgressBar dgEditLoading;
    private TextView dgEditTitle;

    private Uri imgUriWhenAdd;
    private Uri imgUriWhenEdit;

    private boolean flagSuaAnh;

    private Toolbar toolbar;
    private MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loai_s_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setHasOptionsMenu(true);
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        btnThemLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogThemLoaiSP();
            }
        });
        setUpListLoaiSP();
    }

    private void setUpListLoaiSP() {
        loaiSPList = new ArrayList<>();
        loaiSPAdapter = new LoaiSPAdapter(loaiSPList, this);
        rcvLoaiSP.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvLoaiSP.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvLoaiSP.setAdapter(loaiSPAdapter);
        ProductTypeDao.getInstance().getAllProductTypeListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                loaiSPList = (List<LoaiSP>) obj;
                loaiSPAdapter.setData(loaiSPList);
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        btnThemLoai = view.findViewById(R.id.btnThemLoai);
        rcvLoaiSP = view.findViewById(R.id.rcvLoaiSP);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void openDialogThemLoaiSP() {
        addDialog = new Dialog(getContext());
        addDialog.setContentView(R.layout.dialog_them_loai_sp);

        dgAddEdtTenLoaiSP = addDialog.findViewById(R.id.dgAdd_edtTenLoaiSP);
        dgAddImgLoaiSP = addDialog.findViewById(R.id.dgAdd_imgLoaiSP);
        btnThemLoaiSP = addDialog.findViewById(R.id.btnThemLoaiSP);
        btnHuyThemLoaiSP = addDialog.findViewById(R.id.btnHuyThemLoaiSP);
        dgAddLoading = addDialog.findViewById(R.id.dgAdd_loading);


        dgAddImgLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgUriWhenAdd = null;
                dgAddImgLoaiSP.setImageResource(R.drawable.ic_add);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_IMG_URI_WHEN_ADD);
            }
        });

        btnThemLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themLoaiSP();
            }
        });

        btnHuyThemLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }

    private void themLoaiSP() {
        dgAddLoading.setVisibility(View.VISIBLE);
        if (validateInputThemLoaiSP()) {
            LoaiSP loaiSP = new LoaiSP();
            loaiSP.setName(dgAddEdtTenLoaiSP.getText().toString().trim());
            ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    List<LoaiSP> loaiSPList = (List<LoaiSP>) obj;
                    if (loaiSPList != null) {
                        if (validSPWhenAdd(loaiSP, loaiSPList)) {
                            saveAndGetImgLinkWhenAdd(loaiSP, imgUriWhenAdd);
                        }
                    } else {
                        OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        dgAddLoading.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
        }
    }

    private boolean validateInputThemLoaiSP() {
        String tenLoaiSP = dgAddEdtTenLoaiSP.getText().toString().trim();
        if (tenLoaiSP.isEmpty()) {
            OverUtils.makeToast(getContext(), "Vui lòng nhập tên loại sản phẩm");
            dgAddLoading.setVisibility(View.INVISIBLE);
            return false;
        }
        if (imgUriWhenAdd == null) {
            OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh trước khi thêm loại sản phẩm");
            dgAddLoading.setVisibility(View.INVISIBLE);
            return false;
        }
        return true;
    }

    public boolean validSPWhenAdd(LoaiSP loaiSP, List<LoaiSP> loaiSPList) {
        for (LoaiSP lSp : loaiSPList) {
            if (lSp.getName().equals(loaiSP.getName())) {
                dgAddLoading.setVisibility(View.INVISIBLE);
                OverUtils.makeToast(getContext(), "Đã tồn tại loại sp này trong hệ thống");
                return false;
            }
        }
        return true;
    }

    public void saveAndGetImgLinkWhenAdd(LoaiSP loaiSP, Uri imgUri) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String strImgUri = loaiSP.getName() + "*" + System.currentTimeMillis() + "." + OverUtils.getExtensionFile(getContext(), imgUri);
        StorageReference stRef = firebaseStorage.getReference("image/loai_sp").child(strImgUri);
        stRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                stRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String uri = String.valueOf(task.getResult());
                            insertLoaiSP(loaiSP, uri);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                dgAddLoading.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void insertLoaiSP(LoaiSP loaiSP, String imgUri) {
        loaiSP.setHinhanh(imgUri);
        loaiSP.setSoSanPhamThuocLoai(0);
        String idSP = FirebaseDatabase.getInstance().getReference("loai_sp").push().getKey();
        loaiSP.setId(idSP);
        if (idSP != null) {
            ProductTypeDao.getInstance().insertProductType(loaiSP, new IAfterInsertObject() {
                @Override
                public void onSuccess(Object obj) {
                    OverUtils.makeToast(getContext(), "Lưu loại sản phẩm thành công");
                    dgAddLoading.setVisibility(View.INVISIBLE);
                    addDialog.dismiss();
                }

                @Override
                public void onError(DatabaseError exception) {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    dgAddLoading.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            dgAddLoading.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onClickItem(Object obj) {
    }

    @Override
    public void onUpdateItem(Object obj) {
        LoaiSP loaiSP = (LoaiSP) obj;
        if (loaiSP != null) {
            openDialogSuaLoaiSP(loaiSP);
        }
    }

    private static ProgressDialog progressDialog;
    private static LoaiSP loaiSP;

    @Override
    public void onDeleteItem(Object obj) {
        loaiSP = (LoaiSP) obj;
        if (loaiSP != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xóa loại sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa?\n Bạn sẽ luôn cả sản phẩm thuộc loại!")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, i) -> {
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Đang xóa loại sản phẩm");
                        progressDialog.show();
                        ProductDao.getInstance().getProductByProductType2(loaiSP, new IAfterGetAllObject() {
                            @Override
                            public void iAfterGetAllObject(Object obj) {
                                List<Product> productList = (List<Product>) obj;
                                ngungKinhDoanhPr(productList, done -> {
                                    if (done) {
                                        deleteCart(productList);
                                        deleteFavoriteProduct(productList);
                                    }
                                });
                                xoaDonHang(productList);
                            }

                            @Override
                            public void onError(DatabaseError error) {

                            }
                        });
                    })
                    .show();


        }

    }

    private static boolean finishDeleteCart = false;
    private static int counterDeleteCart = 0;

    private void deleteCart(List<Product> productList) {
        UserDao.getInstance().getAllUser2(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    counterDeleteCart++;
                    UserDao.getInstance().getGioHangOfUser(user, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<GioHang> gioHangList = (List<GioHang>) obj;
                            List<GioHang> idList = new ArrayList<>();
                            for (int k = 0; k < productList.size(); k++) {
                                for (int i = 0; i < gioHangList.size(); i++) {
                                    if (productList.get(k).getId().equals(gioHangList.get(i).getMa_sp())) {
                                        idList.add(gioHangList.get(i));
                                    }
                                }
                            }

                            for (int i = 0; i < idList.size(); i++) {
                                gioHangList.remove(idList.get(i));
                            }
                            user.setGio_hang(gioHangList);
                            UserDao.getInstance().updateUser(user, user.toMapGioHang());
                            if (counterDeleteCart == userList.size()) {
                                counterDeleteCart = 0;
                                finishDeleteCart = true;
                                checkDelete(productList);
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }


    private static boolean finishDeleteFavoriteProduct = false;
    private static int counterFavoriteProduct = 0;

    private void deleteFavoriteProduct(List<Product> productList) {
        UserDao.getInstance().getAllUser2(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    counterFavoriteProduct++;
                    UserDao.getInstance().getSanPhamYeuThichOfUser(user, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<String> sanPhamYeuThichList = (List<String>) obj;
                            List<String> sanPhamYeuThichBiXoa = new ArrayList<>();
                            for (int k = 0; k < productList.size(); k++) {
                                for (int i = 0; i < sanPhamYeuThichList.size(); i++) {
                                    if (productList.get(k).getId().equals(sanPhamYeuThichList.get(i))) {
                                        sanPhamYeuThichBiXoa.add(sanPhamYeuThichList.get(i));
                                    }
                                }
                            }

                            for (int i = 0; i < sanPhamYeuThichBiXoa.size(); i++) {
                                sanPhamYeuThichList.remove(sanPhamYeuThichBiXoa.get(i));
                            }
                            user.setMa_sp_da_thich(sanPhamYeuThichList);
                            UserDao.getInstance().updateUser(user, user.toMapSPDaThich());
                            if (counterFavoriteProduct == userList.size()) {
                                finishDeleteFavoriteProduct = true;
                                counterFavoriteProduct = 0;
                                checkDelete(productList);
                            }
                        }

                        @Override
                        public void onError(DatabaseError error) {
                            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                        }
                    });
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    static int countDeleteProduct = 0;

    private synchronized void checkDelete(List<Product> productList) {
        if (finishDeleteCart && finishDeleteFavoriteProduct) {
            for (int i = 0; i < productList.size(); i++) {
                countDeleteProduct++;
                ProductDao.getInstance().deleteProduct(productList.get(i), new IAfterDeleteObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        if (countDeleteProduct == productList.size()) {
                            countDeleteProduct = 0;
                            FirebaseDatabase.getInstance().getReference()
                                    .child("loai_sp").child(loaiSP.getId())
                                    .removeValue((error, ref) -> {
                                        if (error == null) {
                                            progressDialog.dismiss();
                                            OverUtils.makeToast(getContext(), "Xóa loại sản phẩm thành công");
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }
        }
    }


    private void openDialogSuaLoaiSP(LoaiSP loaiSP) {
        // set cờ
        flagSuaAnh = false;

        editDialog = new Dialog(getContext());
        editDialog.setContentView(R.layout.dialog_sua_loai_sp);

        dgEditEdtTenLoaiSP = editDialog.findViewById(R.id.dgEdit_edtTenLoaiSP);
        dgEditImgLoaiSP = editDialog.findViewById(R.id.dgEdit_imgLoaiSP);
        btnSuaLoaiSP = editDialog.findViewById(R.id.btnSuaLoaiSP);
        btnHuySuaLoaiSP = editDialog.findViewById(R.id.btnHuySuaLoaiSP);
        dgEditLoading = editDialog.findViewById(R.id.dgEdit_loading);
        dgEditTitle = editDialog.findViewById(R.id.dgEdit_title);

        Picasso.get()
                .load(loaiSP.getHinhanh())
                .placeholder(R.drawable.ic_image)
                .into(dgEditImgLoaiSP);
        dgEditEdtTenLoaiSP.setText(loaiSP.getName());

        btnSuaLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaLoaiSP(loaiSP);
            }
        });

        btnHuySuaLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.dismiss();
            }
        });

        dgEditImgLoaiSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagSuaAnh = true;
                dgEditImgLoaiSP.setImageResource(R.drawable.ic_add);
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_IMG_URI_WHEN_EDIT);
            }
        });

        editDialog.show();
    }


    private void suaLoaiSP(LoaiSP loaiSP) {
        dgEditLoading.setVisibility(View.VISIBLE);
        if (validateInputSuaLoaiSP()) {
            loaiSP.setName(dgEditEdtTenLoaiSP.getText().toString().trim());
            ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    List<LoaiSP> loaiSPList = (List<LoaiSP>) obj;
                    if (validSPWhenEdit(loaiSP, loaiSPList)) {
                        if (flagSuaAnh) {
                            saveAndGetImgLinkWhenEdit(loaiSP, imgUriWhenEdit);
                        } else {
                            updateLoaiSP(loaiSP);
                        }
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    dgEditLoading.setVisibility(View.INVISIBLE);
                }
            });

        }
    }


    public boolean validSPWhenEdit(LoaiSP loaiSP, List<LoaiSP> loaiSPList) {
        for (LoaiSP lSp : loaiSPList) {
            if (lSp.getId().equals(loaiSP.getId())) {
                continue;
            }
            if (lSp.getName().equals(loaiSP.getName())) {
                dgEditLoading.setVisibility(View.INVISIBLE);
                OverUtils.makeToast(getContext(), "Đã tồn tại loại sp này trong hệ thống");
                return false;
            }
        }
        return true;
    }

    public void saveAndGetImgLinkWhenEdit(LoaiSP loaiSP, Uri imgUri) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        String strImgUri = loaiSP.getName() + "*" + System.currentTimeMillis() + "." + OverUtils.getExtensionFile(getContext(), imgUri);
        StorageReference stRef = firebaseStorage.getReference("image/loai_sp").child(strImgUri);
        stRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                stRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String uri = String.valueOf(task.getResult());
                            loaiSP.setHinhanh(uri);
                            updateLoaiSP(loaiSP);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                dgAddLoading.setVisibility(View.INVISIBLE);
            }
        });
    }


    private boolean validateInputSuaLoaiSP() {
        if (dgEditEdtTenLoaiSP.getText().toString().trim().isEmpty()) {
            OverUtils.makeToast(getContext(), "Cần nhập tên loại sp");
            dgEditLoading.setVisibility(View.INVISIBLE);
            return false;
        }

        if (flagSuaAnh && imgUriWhenEdit == null) {
            OverUtils.makeToast(getContext(), "Cần thêm ảnh cho loại sp");
            dgEditLoading.setVisibility(View.INVISIBLE);
            return false;
        }
        return true;
    }

    private void updateLoaiSP(LoaiSP loaiSP) {
        ProductTypeDao.getInstance().updateProductType(loaiSP, loaiSP.toMap(), new IAfterUpdateObject() {
            @Override
            public void onSuccess(Object obj) {
                OverUtils.makeToast(getContext(), "Sửa thành công");
                dgEditLoading.setVisibility(View.INVISIBLE);
                editDialog.dismiss();

            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                dgEditLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMG_URI_WHEN_ADD) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    dgAddImgLoaiSP.setImageURI(uri);
                    imgUriWhenAdd = uri;
                }
            } else {
                imgUriWhenAdd = null;
                OverUtils.makeToast(getContext(), "Chưa chọn ảnh");
            }
        }

        if (requestCode == REQUEST_CODE_IMG_URI_WHEN_EDIT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    dgEditImgLoaiSP.setImageURI(uri);
                    imgUriWhenEdit = uri;
                } else {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                }
            } else {
                imgUriWhenEdit = null;
                OverUtils.makeToast(getContext(), "Chưa chọn ảnh");
            }
        }
    }

    public void xoaDonHang(List<Product> productList) {
        OrderDao.getInstance().getAllDonHang(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<DonHang> donHangList = (List<DonHang>) obj;
                for (DonHang donHang : donHangList) {
                    if (donHang.getTrang_thai().equals(TrangThai.CHUA_XAC_NHAN.getTrangThai())) {
                        List<DonHangChiTiet> donHangChiTietList = donHang.getDon_hang_chi_tiets();
                        List<DonHangChiTiet> donHangChiTietBiXoaList = new ArrayList<>();
                        for (int i = 0; i < productList.size(); i++) {
                            for (int k = 0; k < donHangChiTietList.size(); k++) {
                                if (donHangChiTietList.get(k).getProduct().getId().equals(productList.get(i).getId())) {
                                    donHangChiTietBiXoaList.add(donHangChiTietList.get(k));
                                }
                            }
                        }

                        int soTienCanXoa = 0;
                        long thoiGianGiam = 0;
                        for (int i = 0; i < donHangChiTietBiXoaList.size(); i++) {
                            Product product = donHangChiTietBiXoaList.get(i).getProduct();
                            int soLuong = donHangChiTietBiXoaList.get(i).getSo_luong();

                            soTienCanXoa += (product.getGia_ban() - (product.getGia_ban() * product.getKhuyen_mai())) * soLuong;
                            thoiGianGiam += product.getThoiGianCheBien();
                        }

                        for (int i = 0; i < donHangChiTietBiXoaList.size(); i++) {
                            donHangChiTietList.remove(donHangChiTietBiXoaList.get(i));
                        }

                        if (donHangChiTietList.size() == 0) {
                            donHang.setTrang_thai(TrangThai.HUY_DON.getTrangThai());
                            donHang.setThong_tin_huy_don("Admin hủy đơn do sản phẩm loại "
                                    + loaiSP.getName() + " không còn được bán");
                            OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuyDon());
                        } else {
                            donHang.setThoiGianGiaoHangDuKien(donHang.getThoiGianGiaoHangDuKien() - thoiGianGiam);
                            donHang.setTong_tien(donHang.getTong_tien() - soTienCanXoa);
                            donHang.setThong_tin_huy_don("Admin hủy sản phẩm loại "
                                    + loaiSP.getName()
                                    + " do không còn được bán!");
                            donHang.setDon_hang_chi_tiets(donHangChiTietList);
                            OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuySPTrongDon());
                        }
                    }

                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }


    public void ngungKinhDoanhPr(List<Product> productList, IDone iDone) {
        if(productList.size() == 0) {
            FirebaseDatabase.getInstance().getReference()
                    .child("loai_sp").child(loaiSP.getId())
                    .removeValue((error, ref) -> {
                        if (error == null) {
                            progressDialog.dismiss();
                            OverUtils.makeToast(getContext(), "Xóa loại sản phẩm thành công");
                        }
                    });
            return;
        }
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            product.setTrang_thai(OverUtils.DUNG_KINH_DOANH);
            int finalI = i;
            ProductDao.getInstance().updateProduct(product, product.toMapTrangThaiSP(), new IAfterUpdateObject() {
                @Override
                public void onSuccess(Object obj) {
                    if (finalI == productList.size() - 1) {
                        iDone.onDone(true);
                    }
                }

                @Override
                public void onError(DatabaseError error) {
                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    iDone.onDone(false);
                }
            });
        }

    }

}