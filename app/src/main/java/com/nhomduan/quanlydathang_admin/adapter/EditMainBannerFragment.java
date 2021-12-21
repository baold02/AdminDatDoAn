package com.nhomduan.quanlydathang_admin.adapter;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.CustomModuleDao;
import com.nhomduan.quanlydathang_admin.dao.MainBannerDao;
import com.nhomduan.quanlydathang_admin.fragment.EditCustomModuleFragment;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.model.BannerImage;
import com.nhomduan.quanlydathang_admin.model.CustomModule;

import java.util.ArrayList;
import java.util.List;

public class EditMainBannerFragment extends Fragment implements BannerImageAdapter.OnClickCustomModuleAdapter {
    private Toolbar toolbar;
    private RecyclerView rcvBanner;
    private AppCompatButton btnReset;
    private AppCompatButton btnSave;
    private AppCompatButton btnAddItem;

    private ProgressDialog progressDialog;

    private List<BannerImage> bannerImageList;
    private BannerImageAdapter bannerImageAdapter;
    private List<Uri> uriList = new ArrayList<>();

    private static int position = -1;
    private final ActivityResultLauncher<String> result =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    uriList.set(position, uri);
                    bannerImageAdapter.notifyItemChanged(position);
                }
                position = -1;
            });
    private static List<Integer> extraImgPositions = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_main_banner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
//        setUpToolBar();
//        setUpListBanner();
//        setUpBtnAddItem();
//        setUpBtnReset();
//        setUpBtnSave();
    }

    private void setUpToolBar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }


    boolean saveImgDone = false;
    boolean saveDone = true;

    private void setUpBtnSave() {
        try {
            btnSave.setOnClickListener(v -> {
                OnSaveImageListener onSaveImageListener = customModule -> {
                    if (saveDone) {
                        if (saveImgDone) {
                            MainBannerDao.getInstance().insertMainBanners(bannerImageList, new IAfterInsertObject() {
                                @Override
                                public void onSuccess(Object obj) {
                                    progressDialog.cancel();
                                    progressDialog = null;
                                    OverUtils.makeToast(getContext(), "Lưu thành công");
                                    saveDone = false;
                                    saveImgDone = false;
                                }

                                @Override
                                public void onError(DatabaseError exception) {
                                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                }
                            });
                        }
                    }
                };

                for (int i = 0; i < extraImgPositions.size(); i++) {
                    if (uriList.get(extraImgPositions.get(i)) == null) {
                        OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh cho item vừa thêm");
                        return;
                    }
                }

                int soUriNonNull = getSizeOfNonNullUriList(uriList);
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Đang cập nhật");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (soUriNonNull == 0) {
                    saveImgDone = true;
                    saveDone = true;
                    onSaveImageListener.onSaveDone(bannerImageList);
                } else {
                    int count = 0;
                    for (int i = 0; i < bannerImageList.size(); i++) {
                        int finalI = i;
                        Uri uri = uriList.get(i);
                        if (uri != null) {
                            count++;
                            int finalCount = count;
                            String strImgUri = "banner" + "*" + System.currentTimeMillis() + "." + OverUtils.getExtensionFile(getContext(), uri);
                            StorageReference stRef = FirebaseStorage.getInstance().getReference().child("image/").child("module_images/").child(strImgUri);
                            stRef.putFile(uri)
                                    .addOnSuccessListener(taskSnapshot ->
                                            stRef.getDownloadUrl().addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    bannerImageList.get(finalI).setImage(String.valueOf(task.getResult()));
                                                    if (finalCount == soUriNonNull) {
                                                        saveImgDone = true;
                                                        onSaveImageListener.onSaveDone(bannerImageList);
                                                    }
                                                }
                                            }));
                        }
                        if (finalI == bannerImageList.size() - 1) {
                            saveDone = true;
                            onSaveImageListener.onSaveDone(bannerImageList);
                        }
                    }
                }
            });
        } catch (Exception e) {
            OverUtils.makeToast(getContext(), ERROR_MESSAGE);
        }
    }

    private interface OnSaveImageListener {
        void onSaveDone(List<BannerImage> bannerImageList);
    }

    private int getSizeOfNonNullUriList(List<Uri> uriList) {
        int result = 0;
        for (Uri uri : uriList) {
            if (uri != null) {
                result++;
            }
        }
        return result;
    }

    private void setUpBtnReset() {
        btnReset.setOnClickListener(v -> {
            setUpListBanner();
            uriList.clear();
            extraImgPositions.clear();
            for (int i = 0; i < bannerImageList.size(); i++) {
                uriList.add(null);
            }
        });
    }

    private void setUpBtnAddItem() {
        btnAddItem.setOnClickListener(v -> {
            bannerImageList.add(new BannerImage());
            uriList.add(null);
            extraImgPositions.add(bannerImageList.size() - 1);
            bannerImageAdapter.notifyItemInserted(bannerImageList.size() - 1);
        });
    }

    private void setUpListBanner() {
        MainBannerDao.getInstance().getAllMainBannerListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                bannerImageList = (List<BannerImage>) obj;
//                bannerImageAdapter = new BannerImageAdapter(bannerImageList, EditMainBannerFragment.this, getContext(), uriList);
                rcvBanner.setLayoutManager(new LinearLayoutManager(getContext()));
                rcvBanner.setAdapter(bannerImageAdapter);

                extraImgPositions.clear();
                uriList.clear();
                for (int i = 0; i < bannerImageList.size(); i++) {
                    uriList.add(null);
                }

            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvBanner = view.findViewById(R.id.rcvBanner);
        btnReset = view.findViewById(R.id.btnReset);
        btnSave = view.findViewById(R.id.btnSave);
        btnAddItem = view.findViewById(R.id.btnAddItem);
    }

    @Override
    public void onSeletedImage(int position, Object obj) {
        if (EditMainBannerFragment.position == -1) {
            EditMainBannerFragment.position = position;
            result.launch("image/*");
        }
    }

    @Override
    public void onDeleteItem(int position) {
        bannerImageList.remove(position);
        bannerImageAdapter.setData(bannerImageList);
        uriList.remove(position);
        int viTri = -1;
        for (int i = 0; i < extraImgPositions.size(); i++) {
            if (extraImgPositions.get(i) == position) {
                viTri = i;
            }
        }
        if (viTri != -1) {
            extraImgPositions.remove(viTri);
            for (int i = 0; i < extraImgPositions.size(); i++) {
                int value = extraImgPositions.get(i);
                extraImgPositions.set(i, value - 1);
            }
        }
    }

    @Override
    public void onSeletedFunction(int position, Object obj) {
        String productId = (String) obj;
        bannerImageList.get(position).setProductId(productId);
    }
}