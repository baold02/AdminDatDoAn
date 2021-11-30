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
                    if(loaiSPList != null) {
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

    @Override
    public void onDeleteItem(Object obj) {
        LoaiSP loaiSP = (LoaiSP) obj;
        if (loaiSP != null) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Xóa sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa?\n Bạn sẽ luôn cả sản phẩm thuộc loại!")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", (dialog, i) -> {
                        ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Đang xóa sản phẩm");
                        progressDialog.show();
                        FirebaseDatabase.getInstance().getReference().child("loai_sp").child(loaiSP.getId())
                                .removeValue();
                        ProductDao.getInstance().getAllProduct(new IAfterGetAllObject() {
                            @Override
                            public void iAfterGetAllObject(Object obj) {
                                List<Product> productList = (List<Product>) obj;
                                for(Product product : productList) {
                                    if(product.getLoai_sp().equals(loaiSP.getId())) {
                                        ngungKinhDoanhPr(product, done0 -> {
                                            if (done0) {
                                                xoaSPDT(product, done -> {
                                                    if (done) {
                                                        xoaGioHang(product, done1 -> {
                                                            if (done1) {
                                                                FirebaseDatabase.getInstance().getReference().child("san_pham").child(product.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                                        if (error == null) {
                                                                            OverUtils.makeToast(getContext(), "Xóa thành công");
                                                                            progressDialog.dismiss();
                                                                        } else {
                                                                            progressDialog.cancel();
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                progressDialog.cancel();
                                                            }
                                                        });
                                                    } else {
                                                        progressDialog.cancel();
                                                    }
                                                });
                                            } else {
                                                progressDialog.cancel();
                                            }
                                        });
                                        xoaDonHang(product);
                                        xoaSoLuongSanPhamCuaLoai(product);
                                    }
                                }
                            }

                            @Override
                            public void onError(DatabaseError error) {

                            }
                        });
                    })
                    .show();


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


    private void xoaSoLuongSanPhamCuaLoai(Product product) {
        String loaiSPId = product.getLoai_sp();
        ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<LoaiSP> loaiSPList = (List<LoaiSP>) obj;
                for(int i = 0; i < loaiSPList.size(); i++) {
                    if(loaiSPList.get(i).getId().equals(loaiSPId)) {
                        LoaiSP loaiSP = loaiSPList.get(i);
                        loaiSP.setSoSanPhamThuocLoai(loaiSP.getSoSanPhamThuocLoai() - 1);
                        ProductTypeDao.getInstance().updateProductType(loaiSPList.get(i), loaiSP.toMapSoLuongSanPham());
                        break;
                    }
                }

            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }

    public void xoaDonHang(Product product) {
        OrderDao.getInstance().getAllDonHang(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<DonHang> donHangList = (List<DonHang>) obj;
                for (DonHang donHang : donHangList) {
                    if (donHang.getTrang_thai().equals(TrangThai.CXN.getTrangThai())) {
                        List<DonHangChiTiet> donHangChiTietList = donHang.getDon_hang_chi_tiets();
                        if (donHangChiTietList.size() == 1) {
                            donHang.setTrang_thai(TrangThai.HD.getTrangThai());
                            donHang.setThong_tin_huy_don("Admin hủy đơn do sản phẩm "
                                    + donHangChiTietList.get(0).getProduct().getName() + " không còn được bán");
                            OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuyDon());
                        } else {
                            int viTri = -1;
                            for (int i = 0; i < donHangChiTietList.size(); i++) {
                                if (donHangChiTietList.get(i).getProduct().getId().equals(product.getId())) {
                                    viTri = i;
                                }
                            }
                            if (viTri != -1) {
                                donHang.setThong_tin_huy_don("Admin hủy sản phẩm "
                                        + donHangChiTietList.get(viTri).getProduct().getName()
                                        + " do không còn được bán!");
                                donHangChiTietList.remove(viTri);
                                donHang.setDon_hang_chi_tiets(donHangChiTietList);
                                OrderDao.getInstance().updateDonHang(donHang, donHang.toMapHuySPTrongDon());
                            }
                        }

                    }
                }
            }

            @Override
            public void onError(DatabaseError error) {

            }
        });
    }



    public void ngungKinhDoanhPr(Product product, IDone iDone) {
        product.setTrang_thai(OverUtils.DUNG_KINH_DOANH);
        ProductDao.getInstance().updateProduct(product, product.toMapTrangThaiSP(), new IAfterUpdateObject() {
            @Override
            public void onSuccess(Object obj) {
                iDone.onDone(true);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }


    int couterXoaGioHang = 0;

    private void xoaGioHang(Product product, IDone iDone) {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    couterXoaGioHang++;
                    UserDao.getInstance().getGioHangOfUser(user, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<GioHang> gioHangList = (List<GioHang>) obj;
                            int viTri = -1;
                            for (int i = 0; i < gioHangList.size(); i++) {
                                if (gioHangList.get(i).getMa_sp().equals(product.getId())) {
                                    viTri = i;
                                    break;
                                }
                            }
                            if (viTri != -1) {
                                gioHangList.remove(viTri);
                                user.setGio_hang(gioHangList);
                                UserDao.getInstance().updateUser(user, user.toMapGioHang());
                            }
                            if (couterXoaGioHang == userList.size()) {
                                couterXoaGioHang = 0;
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

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }


    int couterSPDT = 0;

    private void xoaSPDT(Product product, IDone iDone) {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                List<User> userList = (List<User>) obj;
                for (User user : userList) {
                    couterSPDT++;
                    UserDao.getInstance().getSanPhamYeuThichOfUser(user, new IAfterGetAllObject() {
                        @Override
                        public void iAfterGetAllObject(Object obj) {
                            List<String> sanPhamYeuThichList = (List<String>) obj;
                            int viTri = -1;
                            for (int i = 0; i < sanPhamYeuThichList.size(); i++) {
                                if (sanPhamYeuThichList.get(i).equals(product.getId())) {
                                    viTri = i;
                                    break;
                                }
                            }
                            if (viTri != -1) {
                                sanPhamYeuThichList.remove(viTri);
                                user.setMa_sp_da_thich(sanPhamYeuThichList);
                                UserDao.getInstance().updateUser(user, user.toMapSPDaThich());
                            }
                            if (couterSPDT == userList.size()) {
                                couterSPDT = 0;
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

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                iDone.onDone(false);
            }
        });
    }

}