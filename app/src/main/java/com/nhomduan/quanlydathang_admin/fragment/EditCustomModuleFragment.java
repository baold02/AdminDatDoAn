package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.BannerImageAdapter;
import com.nhomduan.quanlydathang_admin.adapter.BannerImageAdapter3;
import com.nhomduan.quanlydathang_admin.dao.CustomModuleDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterUpdateObject;
import com.nhomduan.quanlydathang_admin.model.BannerImage;
import com.nhomduan.quanlydathang_admin.model.CustomModule;
import com.nhomduan.quanlydathang_admin.model.CustomUri;
import com.nhomduan.quanlydathang_admin.model.Function;

import java.util.ArrayList;
import java.util.List;


public class EditCustomModuleFragment extends Fragment implements BannerImageAdapter3.OnClickCustomModuleAdapter {
    private Toolbar toolbar;
    private TextInputEditText edtTitle;
    private TextInputEditText edtAmount;
    private Spinner spnFunction;
    private ImageView imgAddExtraBannerImage;
    private RecyclerView rcvCustomModuleImgList;
    private Button btnReset;
    private Button btnEditModule;

    private CustomModule oldCustomModule;
    private CustomModule newCustomModule;

    private List<BannerImage> newBannerImageList;

    private BannerImageAdapter3 bannerImageAdapter;
    private List<CustomUri> uriList;
    private static int position = -1;

    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<String> result =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    uriList.set(position, new CustomUri(uri, position));
                    bannerImageAdapter.notifyItemChanged(position);
                }
                position = -1;
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_custom_module, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolBar();
        getDuLieu();
        setUpCustomModuleList(newCustomModule);
        setUpBtnAddExtraBannerImg();
        setUpBtnEditModule();
        setUpBtnReset();
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        edtTitle = view.findViewById(R.id.edtTitle);
        edtAmount = view.findViewById(R.id.edtAmount);
        spnFunction = view.findViewById(R.id.spnFunction);
        imgAddExtraBannerImage = view.findViewById(R.id.imgAddExtraBannerImage);
        rcvCustomModuleImgList = view.findViewById(R.id.rcvCustomModuleImgList);
        btnReset = view.findViewById(R.id.btnReset);
        btnEditModule = view.findViewById(R.id.btnEditModule);
    }

    private void setUpToolBar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void getDuLieu() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            oldCustomModule = (CustomModule) bundle.getSerializable("module");
            newCustomModule = oldCustomModule.customClone();

            uriList = new ArrayList<>();
            for (int i = 0; i < newCustomModule.getImageList().size(); i++) {
                uriList.add(new CustomUri(null, -1));
            }
        }

    }

    private void setUpCustomModuleList(CustomModule customModule) {
        edtTitle.setText(customModule.getTitle());
        edtAmount.setText(String.valueOf(customModule.getSoLuong()));
        newBannerImageList = customModule.getImageList();
        int viTri = 0;
        for (int i = 0; i < Function.values().length; i++) {
            if (customModule.getFunction().equals(Function.values()[i])) {
                viTri = i;
            }
        }
        ArrayAdapter<Function> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, Function.values());
        spnFunction.setAdapter(arrayAdapter);
        spnFunction.setSelection(viTri);

        bannerImageAdapter = new BannerImageAdapter3(newBannerImageList, this, getContext(), uriList);
        rcvCustomModuleImgList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCustomModuleImgList.setAdapter(bannerImageAdapter);
    }

    private void setUpBtnAddExtraBannerImg() {
        imgAddExtraBannerImage.setOnClickListener(v -> {
            newBannerImageList.add(new BannerImage());
            uriList.add(new CustomUri(null, newBannerImageList.size() - 1));
            bannerImageAdapter.notifyItemInserted(newBannerImageList.size() - 1);
        });
    }

    boolean saveImgDone = false;
    private void setUpBtnEditModule() {
        OnSaveImageListener onSaveImageListener = customModule -> {
            if (saveImgDone) {
                CustomModuleDao.getInstance().updateCustomModule(customModule, new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        progressDialog.cancel();
                        progressDialog = null;
                        OverUtils.makeToast(getContext(), "Lưu thành công");
                        oldCustomModule = customModule.customClone();
                        newCustomModule = oldCustomModule.customClone();

                        uriList.clear();
                        for (int i = 0; i < newCustomModule.getImageList().size(); i++) {
                            uriList.add(new CustomUri(null, -1));
                        }
                        setUpCustomModuleList(newCustomModule);
                        saveImgDone = false;
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                    }
                });
            }
        };

        btnEditModule.setOnClickListener(v -> {
            List<BannerImage> bannerImageList = newCustomModule.getImageList();
            String title = edtTitle.getText().toString().trim();
            String amount = edtAmount.getText().toString().trim();
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

            for (int i = 0; i < uriList.size(); i++) {
                if (uriList.get(i).getPosition() != -1 && uriList.get(i).getUri() == null) {
                    OverUtils.makeToast(getContext(), "Vui lòng chọn ảnh cho item vừa thêm");
                    return;
                }
            }

            newCustomModule.setId(oldCustomModule.getId());
            newCustomModule.setTitle(title);
            newCustomModule.setFunction(function);
            newCustomModule.setSoLuong(amountInt);


            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang cập nhật");
            progressDialog.setCancelable(false);
            progressDialog.show();

            List<CustomUri> hasUriList = getHasUriList(uriList);
            if (hasUriList.size() > 0) {
                int count = 0;
                for (int i = 0; i < hasUriList.size(); i++) {
                    CustomUri uri = hasUriList.get(i);
                    count++;
                    int finalCount = count;
                    String strImgUri = "banner" + "*" + System.currentTimeMillis() + "." + OverUtils.getExtensionFile(getContext(), uri.getUri());
                    StorageReference stRef = FirebaseStorage.getInstance().getReference().child("image/").child("module_images/").child(strImgUri);

                    stRef.putFile(uri.getUri())
                            .addOnSuccessListener(taskSnapshot ->
                                    stRef.getDownloadUrl().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            bannerImageList.get(uri.getPosition()).setImage(String.valueOf(task.getResult()));
                                            if (finalCount == hasUriList.size()) {
                                                saveImgDone = true;
                                                onSaveImageListener.onSaveDone(newCustomModule);
                                            }
                                        }
                                    }));
                }
            } else {
                saveImgDone = true;
                onSaveImageListener.onSaveDone(newCustomModule);
            }

        });
    }

    private List<CustomUri> getHasUriList(List<CustomUri> uriList) {
        List<CustomUri> result = new ArrayList<>();
        for (CustomUri customUri : uriList) {
            if (customUri.getUri() != null) {
                result.add(customUri);
            }
        }
        return result;
    }


    private void setUpBtnReset() {
        btnReset.setOnClickListener(v -> {
            newCustomModule = oldCustomModule.customClone();
            uriList.clear();
            for (int i = 0; i < newCustomModule.getImageList().size(); i++) {
                uriList.add(new CustomUri(null, -1));
            }
            setUpCustomModuleList(newCustomModule);
        });
    }

    private interface OnSaveImageListener {
        void onSaveDone(CustomModule customModule);
    }

    @Override
    public void onSeletedImage(int position, Object obj) {
        if (EditCustomModuleFragment.position == -1) {
            EditCustomModuleFragment.position = position;
            result.launch("image/*");
        }
    }

    @Override
    public void onDeleteItem(int position) {
        newBannerImageList.remove(position);
        bannerImageAdapter.setData(newBannerImageList);

        uriList.remove(position);
        for (int i = 0; i < uriList.size(); i++) {
            if (uriList.get(i).getPosition() > position && uriList.get(i).getPosition() != -1) {
                uriList.get(i).setPosition(uriList.get(i).getPosition() - 1);
            }
        }
    }

    @Override
    public void onSeletedFunction(int position, Object obj) {
        String productId = (String) obj;
        newCustomModule.getImageList().get(position).setProductId(productId);
    }
}