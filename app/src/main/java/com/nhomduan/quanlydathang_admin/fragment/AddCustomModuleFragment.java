package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.BannerImageAdapter;
import com.nhomduan.quanlydathang_admin.dao.CustomModuleDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.model.BannerImage;
import com.nhomduan.quanlydathang_admin.model.CustomModule;
import com.nhomduan.quanlydathang_admin.model.CustomUri;
import com.nhomduan.quanlydathang_admin.model.Function;

import java.util.ArrayList;
import java.util.List;


public class AddCustomModuleFragment extends Fragment implements BannerImageAdapter.OnClickCustomModuleAdapter {

    private Toolbar toolbar;
    private EditText edtTitle;
    private EditText edtProductAmount;
    private Spinner spnFunction;
    private RecyclerView rcvCustomModuleImgList;
    private Button btnReset;
    private Button btnAddModule;
    private ImageView imgAddExtraBannerImage;


    private List<BannerImage> bannerImageList;
    private List<CustomUri> uriList;
    private BannerImageAdapter bannerImageAdapter;
    private static int position = -1;

    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    uriList.set(position, new CustomUri(uri, position));
                    bannerImageAdapter.notifyItemChanged(position);
                    position = -1;
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_module, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // code
        initView(view);
        setUpToolbar();
        setUpImageList();
        setUpSpnChucNang();
        setUpBtnAddModule();
        setUpBtnAddExtraBannerImg();
        setUpBtnClear();
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        edtTitle = view.findViewById(R.id.edtTitle);
        edtProductAmount = view.findViewById(R.id.edtProductAmount);
        spnFunction = view.findViewById(R.id.spnFunction);
        rcvCustomModuleImgList = view.findViewById(R.id.rcvCustomModuleImgList);
        btnReset = view.findViewById(R.id.btnReset);
        btnAddModule = view.findViewById(R.id.btnAddModule);
        imgAddExtraBannerImage = view.findViewById(R.id.imgAddExtraBannerImage);
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setUpImageList() {
        bannerImageList = new ArrayList<>();
        uriList = new ArrayList<>();

        bannerImageList.add(0, new BannerImage());
        uriList.add(new CustomUri(null, bannerImageList.size() - 1));

        bannerImageAdapter = new BannerImageAdapter(bannerImageList, this, getContext(), uriList);
        rcvCustomModuleImgList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCustomModuleImgList.setAdapter(bannerImageAdapter);
    }

    private void setUpSpnChucNang() {
        ArrayAdapter<Function> arrayAdapter
                = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, Function.values());
        spnFunction.setAdapter(arrayAdapter);
        spnFunction.setSelection(0);
    }


    private void setUpBtnAddModule() {
        btnAddModule.setOnClickListener(v -> {
            List<BannerImage> bannerImageList = bannerImageAdapter.getBannerImageList();
            if (bannerImageList.size() == 0) {
                OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh đại diện cho module");
                return;
            }
            for (CustomUri uri : uriList) {
                if (uri.getUri() == null) {
                    OverUtils.makeToast(getContext(), "Vui lòng chọn đầy đủ ảnh");
                    return;
                }
            }

            String title = edtTitle.getText().toString().trim();
            String amount = edtProductAmount.getText().toString().trim();
            Function function = (Function) spnFunction.getSelectedItem();

            if (title.isEmpty() || amount.isEmpty()) {
                OverUtils.makeToast(getContext(), "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            int amountInt;
            try {
                amountInt = Integer.parseInt(amount);
            } catch (NumberFormatException ex) {
                OverUtils.makeToast(getContext(), "Số lượng phải là một số");
                return;
            }

            if (amountInt < 1) {
                OverUtils.makeToast(getContext(), "Số lượng phải là một số lớn hơn hoặc bằng 1");
                return;
            }

            if (function.equals(Function.Chua_Chon)) {
                OverUtils.makeToast(getContext(), "Vui lòng chọn chọn chức năng cho module");
                return;
            }


            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang thêm module ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            int count = 0;
            for (int i = 0; i < bannerImageList.size(); i++) {
                count++;
                int finalI = i;
                int finalCount = count;
                Uri uri = uriList.get(i).getUri();
                String strImgUri = "banner" + "." + System.currentTimeMillis();
                StorageReference stRef = FirebaseStorage.getInstance().getReference().child("image/").child("module_images/").child(strImgUri);
                stRef.putFile(uri)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                stRef.getDownloadUrl().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        bannerImageList.get(finalI).setImage(String.valueOf(task2.getResult()));
                                        if (finalCount == bannerImageList.size()) {
                                            CustomModule customModule = new CustomModule(title, function, amountInt, bannerImageList);
                                            CustomModuleDao.getInstance().insertCustomModule(customModule, new IAfterInsertObject() {
                                                @Override
                                                public void onSuccess(Object obj) {
                                                    try {
                                                        OverUtils.makeToast(getContext(), "Thêm module thành công");
                                                        progressDialog.cancel();
                                                        progressDialog = null;
                                                        clearForm();
                                                    } catch (Exception e) {
                                                        Log.e("TAG", "Lỗi null");
                                                    }
                                                }

                                                @Override
                                                public void onError(DatabaseError exception) {
                                                    OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                                }
                                            });
                                        }
                                    } else {
                                        Log.e("TAG", "" + task.getException().getMessage());
                                    }
                                });
                            } else {
                                Log.e("TAG", task.getException().getMessage());
                            }
                        });
            }
        });
    }


    private void setUpBtnAddExtraBannerImg() {
        imgAddExtraBannerImage.setOnClickListener(v -> {
            bannerImageList.add(new BannerImage());
            uriList.add(new CustomUri(null, bannerImageList.size() - 1));
            bannerImageAdapter.notifyItemInserted(bannerImageList.size() - 1);
        });
    }

    private void setUpBtnClear() {
        btnReset.setOnClickListener(v -> {
            clearForm();
        });
    }

    @Override
    public void onSeletedImage(int position, Object obj) {
        AddCustomModuleFragment.position = position;
        mGetContent.launch("image/*");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDeleteItem(int position) {
        bannerImageList.remove(position);
        bannerImageAdapter.notifyDataSetChanged();

        uriList.remove(position);
        for (int i = 0; i < uriList.size(); i++) {
            if (uriList.get(i).getPosition() > position) {
                uriList.get(i).setPosition(uriList.get(i).getPosition() - 1);
            }
        }
    }

    @Override
    public void onSeletedFunction(int position, Object obj) {
        bannerImageList.get(position).setProductId((String) obj);
    }

    private void clearForm() {
        edtTitle.setText("");
        edtProductAmount.setText("");
        spnFunction.setSelection(0);

        bannerImageList.clear();
        bannerImageList.add(0, new BannerImage());
        bannerImageAdapter.setData(bannerImageList, uriList);

        uriList.clear();
        uriList.add(new CustomUri(null, bannerImageList.size() - 1));
    }
}