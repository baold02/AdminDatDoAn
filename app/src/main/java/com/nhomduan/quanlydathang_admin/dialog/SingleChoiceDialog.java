package com.nhomduan.quanlydathang_admin.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;

public class SingleChoiceDialog extends DialogFragment {

    private int position;
    private String[] objList;
    private String title;
    private String positiveButtonText;
    private String nagitiveButtonText;
    private ISingleChoiceDialog iSingleChoiceDialog;

    public SingleChoiceDialog(String[] objList, int position, String title, String positiveButtonText, String nagitiveButtonText, ISingleChoiceDialog iSingleChoiceDialog) {
        this.objList = objList;
        this.title = title;
        this.positiveButtonText = positiveButtonText;
        this.nagitiveButtonText = nagitiveButtonText;
        this.iSingleChoiceDialog = iSingleChoiceDialog;
        this.position = position;
    }

    public interface ISingleChoiceDialog {
        void onChoice(Object[] objList, int position);

        void onCancel();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireActivity())
                .setTitle(title)
                .setSingleChoiceItems(objList, position, (dialog, which) -> {
                    position = which;
                })
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    iSingleChoiceDialog.onChoice(objList, position);
                })
                .setNegativeButton(nagitiveButtonText, (dialog, which) -> {
                    iSingleChoiceDialog.onCancel();
                })
                .create();
    }
}
