package com.nhomduan.quanlydathang_admin.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.LoaiSanPhamUtils;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.LoaiSPAdapter;
import com.nhomduan.quanlydathang_admin.dao.ProductTypeDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.LoaiSP;
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
        ProductTypeDao.getInstance().getAllProductType(new IAfterGetAllObject() {
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
            LoaiSanPhamUtils.getDbRfLoaiSP().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot != null) {
                            List<LoaiSP> loaiSPList = LoaiSanPhamUtils.getAllLoaiSP(dataSnapshot);
                            if (validSPWhenAdd(loaiSP, loaiSPList)) {
                                saveAndGetImgLinkWhenAdd(loaiSP, imgUriWhenAdd);
                            }
                        } else {
                            OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                            dgAddLoading.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                        dgAddLoading.setVisibility(View.INVISIBLE);
                    }
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
                OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                dgAddLoading.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void insertLoaiSP(LoaiSP loaiSP, String imgUri) {
        loaiSP.setHinhanh(imgUri);
        loaiSP.setSoSanPhamThuocLoai(0);
        String idSP = LoaiSanPhamUtils.getDbRfLoaiSP().push().getKey();
        loaiSP.setId(idSP);
        if (idSP != null) {
            LoaiSanPhamUtils.getDbRfLoaiSP().child(loaiSP.getId()).setValue(loaiSP).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        OverUtils.makeToast(getContext(), "Lưu loại sản phẩm thành công");
                        dgAddLoading.setVisibility(View.INVISIBLE);
                        addDialog.dismiss();
                    } else {
                        OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                        dgAddLoading.setVisibility(View.INVISIBLE);
                    }
                }
            });
        } else {
            OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
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
                    .setTitle("Xóa loại sản phẩm")
                    .setMessage("Bạn có chắc chắn muốn xóa hay không?")
                    .setNegativeButton("Hủy", null)
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LoaiSanPhamUtils.getDbRfLoaiSP().child(loaiSP.getId()).removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error == null) {
                                        OverUtils.makeToast(getContext(), "Xóa thành công");
                                    } else {
                                        OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                                    }
                                }
                            });
                        }
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
            LoaiSanPhamUtils.getDbRfLoaiSP().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot != null) {
                            List<LoaiSP> loaiSPList = LoaiSanPhamUtils.getAllLoaiSP(dataSnapshot);
                            if (validSPWhenEdit(loaiSP, loaiSPList)) {
                                if (flagSuaAnh) {
                                    saveAndGetImgLinkWhenEdit(loaiSP, imgUriWhenEdit);
                                } else {
                                    updateLoaiSP(loaiSP);
                                }
                            }
                        } else {
                            OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                            dgEditLoading.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                        dgEditLoading.setVisibility(View.INVISIBLE);
                    }
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
                OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
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
        LoaiSanPhamUtils.getDbRfLoaiSP().child(loaiSP.getId())
                .updateChildren(loaiSP.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null) {
                            OverUtils.makeToast(getContext(), "Sửa thành công");
                            editDialog.dismiss();
                        } else {
                            OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                        }
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
                    OverUtils.makeToast(getContext(), OverUtils.ERROR_MESSAGE);
                }
            } else {
                imgUriWhenEdit = null;
                OverUtils.makeToast(getContext(), "Chưa chọn ảnh");
            }
        }
    }


//    LoaiSanPhamUtils.getDbRfLoaiSP().addChildEventListener(new ChildEventListener() {
//        @Override
//        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
//            if (loaiSP != null) {
//                loaiSPList.add(loaiSP);
//                loaiSPAdapter.notifyDataSetChanged();
//            }
//        }
//
//        @Override
//        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
//            if (loaiSP == null && loaiSPList.isEmpty()) {
//                return;
//            }
//
//            for (int i = 0; i < loaiSPList.size(); i++) {
//                if (loaiSP.getId() == loaiSPList.get(i).getId()) {
//                    loaiSPList.set(i, loaiSP);
//                }
//            }
//            loaiSPAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//            LoaiSP loaiSP = snapshot.getValue(LoaiSP.class);
//            if (loaiSP == null && loaiSPList.isEmpty()) {
//                return;
//            }
//
//            for (int i = 0; i < loaiSPList.size(); i++) {
//                if (loaiSP.getId() == loaiSPList.get(i).getId()) {
//                    loaiSPList.remove(loaiSPList.get(i));
//                }
//            }
//            loaiSPAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
}