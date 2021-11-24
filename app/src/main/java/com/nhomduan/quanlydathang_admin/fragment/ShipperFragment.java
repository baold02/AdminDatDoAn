package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.Utils.ShipperUtils;
import com.nhomduan.quanlydathang_admin.activities.MainActivity;
import com.nhomduan.quanlydathang_admin.adapter.ShipperAdapter;
import com.nhomduan.quanlydathang_admin.dao.ShiperDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterInsertObject;
import com.nhomduan.quanlydathang_admin.interface_.OnClickItem;
import com.nhomduan.quanlydathang_admin.model.Shipper;

import java.util.ArrayList;
import java.util.List;

public class ShipperFragment extends Fragment implements OnClickItem {
    private Toolbar toolbar;
    private RecyclerView rcvShipper;
    private FloatingActionButton btnThemShipper;

    private List<Shipper> shipperList;
    private ShipperAdapter shipperAdapter;
    private MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar(view);

        btnThemShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_them_shipper);

                EditText edtThemTenShipper;
                EditText edtThemSDTShipper;
                TextView tvTitleThemShipper;
                Button btnHuy;
                Button btnThemShipper;

                edtThemTenShipper = dialog.findViewById(R.id.edtThemTenShipper);
                edtThemSDTShipper = dialog.findViewById(R.id.edtThemSDTShipper);
                tvTitleThemShipper = dialog.findViewById(R.id.tvTitleThemShipper);
                btnHuy = dialog.findViewById(R.id.btnHuy);
                btnThemShipper = dialog.findViewById(R.id.btnThemShipper);

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnThemShipper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tenShipper = edtThemTenShipper.getText().toString().trim();
                        String sdtShipper = edtThemSDTShipper.getText().toString().trim();
                        if(tenShipper.length() == 0 || sdtShipper.length() == 0) {
                            OverUtils.makeToast(getContext(), "Vui lòng nhập đầy đủ thông tin");
                            return;
                        }
                        ShipperUtils.getDbRfShipper().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    List<Shipper> shipperList = ShipperUtils.getAllShipper(task.getResult());
                                    boolean validShipper = validShipper(sdtShipper, shipperList);
                                    if (validShipper) {
                                        String key = ShipperUtils.getDbRfShipper().push().getKey();
                                        Shipper shipper = new Shipper();
                                        shipper.setId(key);
                                        shipper.setName(tenShipper);
                                        shipper.setPhone_number(sdtShipper);
                                        ShiperDao.getInstance().insertShipper(shipper, new IAfterInsertObject() {
                                            @Override
                                            public void onSuccess(Object obj) {
                                                OverUtils.makeToast(getContext(), "Thêm thành công");
                                            }

                                            @Override
                                            public void onError(DatabaseError exception) {
                                                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });

                dialog.show();
            }
        });
        setUpListShipper();
    }

    private void setUpToolbar(View view) {
        setHasOptionsMenu(true);
        activity = (MainActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

    }

    private void setUpListShipper() {
        shipperList = new ArrayList<>();
        shipperAdapter = new ShipperAdapter(shipperList, this);
        rcvShipper.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvShipper.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rcvShipper.setAdapter(shipperAdapter);

        ShipperUtils.getDbRfShipper().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Shipper shipper = snapshot.getValue(Shipper.class);
                if (shipper != null) {
                    shipperList.add(shipper);
                    shipperAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Shipper shipper = snapshot.getValue(Shipper.class);
                if (shipper == null && shipperList == null) {
                    return;
                }
                for (int i = 0; i < shipperList.size(); i++) {
                    if (shipperList.get(i).getId().equals(shipper.getId())) {
                        shipperList.set(i, shipper);
                        shipperAdapter.notifyDataSetChanged();
                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Shipper shipper = snapshot.getValue(Shipper.class);
                if (shipper == null && shipperList == null) {
                    return;
                }
                for (int i = 0; i < shipperList.size(); i++) {
                    if (shipperList.get(i).getId().equals(shipper.getId())) {
                        shipperList.remove(i);
                        shipperAdapter.notifyDataSetChanged();
                        break;
                    }
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


    private boolean validShipper(String sdtShipper, List<Shipper> shipperList) {
        for (Shipper shipper : shipperList) {
            if (shipper.getPhone_number().equals(sdtShipper)) {
                OverUtils.makeToast(getContext(), "Số điện thoại này đang thuộc một shipper khác");
                return false;
            }
        }
        return true;
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvShipper = view.findViewById(R.id.rcvShipper);
        btnThemShipper = view.findViewById(R.id.btnThemShipper);

    }

    @Override
    public void onClickItem(Object obj) {}

    @Override
    public void onUpdateItem(Object obj) {
        Shipper currentShipper = (Shipper) obj;

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_sua_shipper);

        EditText edtSuaTenShipper;
        EditText edtSuaSDTShipper;
        TextView tvTitleSuaShipper;
        Button btnHuy;
        Button btnSuaShipper;

        edtSuaTenShipper = dialog.findViewById(R.id.edtSuaTenShipper);
        edtSuaSDTShipper = dialog.findViewById(R.id.edtSuaSDTShipper);
        tvTitleSuaShipper = dialog.findViewById(R.id.tvTitleSuaShipper);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        btnSuaShipper = dialog.findViewById(R.id.btnSuaShipper);

        edtSuaSDTShipper.setText(currentShipper.getPhone_number());
        edtSuaTenShipper.setText(currentShipper.getName());


        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSuaShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenShipper = edtSuaTenShipper.getText().toString().trim();
                String sdtShipper = edtSuaSDTShipper.getText().toString().trim();
                if(tenShipper.length() == 0 || sdtShipper.length() == 0) {
                    OverUtils.makeToast(getContext(), "Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                ShipperUtils.getDbRfShipper().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            List<Shipper> shipperList = ShipperUtils.getAllShipper(task.getResult());
                            boolean validShipper = validShipperWhenEdit(currentShipper ,sdtShipper, shipperList);
                            if(validShipper) {
                                ShipperUtils.getDbRfShipper().child(currentShipper.getId())
                                        .setValue(new Shipper(currentShipper.getId(), tenShipper, sdtShipper), new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                if(error == null) {
                                                    OverUtils.makeToast(getContext(), "Sửa thành công");
                                                    dialog.cancel();
                                                }
                                            }
                                        });
                            }

                        }
                    }
                });
            }
        });

        dialog.show();
    }


    private boolean validShipperWhenEdit(Shipper currentShipper, String sdtShipper, List<Shipper> shipperList) {
        for(Shipper shipper : shipperList) {
            if(shipper.getId().equals(currentShipper.getId())) {
                continue;
            } else {
                if(sdtShipper.equals(shipper.getPhone_number())) {
                    OverUtils.makeToast(getContext(), "Số điện thoại này đang thuộc một shipper khác");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onDeleteItem(Object obj) {
        Shipper shipper = (Shipper) obj;
        new AlertDialog.Builder(getContext())
                .setTitle("Xóa shipper")
                .setMessage("Bạn có chắc chắn muốn xóa shipper này ?")
                .setNegativeButton("HỦY", null)
                .setPositiveButton("XÓA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShipperUtils.getDbRfShipper().child(shipper.getId()).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                OverUtils.makeToast(getContext(), "Xóa thành công");
                            }
                        });
                    }
                }).show();

    }
}