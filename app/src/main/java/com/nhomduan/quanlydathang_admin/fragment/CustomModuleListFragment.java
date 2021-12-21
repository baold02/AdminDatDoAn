package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.adapter.ModuleAdapter;
import com.nhomduan.quanlydathang_admin.dao.CustomModuleDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterDeleteObject;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.CustomModule;

import java.util.ArrayList;
import java.util.List;


public class CustomModuleListFragment extends Fragment implements OnClickItem {
    private Toolbar toolbar;
    private RecyclerView rcvModuleList;
    private FloatingActionButton btnAddModule;

    private List<CustomModule> customModuleList;
    private ModuleAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_module_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        setUpCustomModuleList();
        setUpBtnAddModule();
    }

    private void setUpToolbar() {
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setUpBtnAddModule() {
        btnAddModule.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFrame, new AddCustomModuleFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setUpCustomModuleList() {
        customModuleList = new ArrayList<>();
        adapter = new ModuleAdapter(getContext(), customModuleList, this);
        rcvModuleList.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvModuleList.setAdapter(adapter);

        CustomModuleDao.getInstance().getAllCustomModuleListener(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                customModuleList = (List<CustomModule>) obj;
                adapter.setData(customModuleList);
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvModuleList = view.findViewById(R.id.rcvModuleList);
        btnAddModule = view.findViewById(R.id.btnAddModule);
    }

    @Override
    public void onClickItem(Object obj) {

    }

    @Override
    public void onUpdateItem(Object obj) {
        CustomModule customModule = (CustomModule) obj;
        Fragment fragment = new EditCustomModuleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("module", customModule);
        fragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDeleteItem(Object obj) {
        CustomModule customModule = (CustomModule) obj;
        CustomModuleDao.getInstance().deleteModule(getContext(), customModule.getId(), new IAfterDeleteObject() {
            @Override
            public void onSuccess(Object obj) {
                OverUtils.makeToast(getContext(), "Xóa thành công module");
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }
}