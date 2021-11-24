package com.nhomduan.quanlydathang_admin.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class OverUtils {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm * dd/MM/yyyy");
    private static Locale locale = new Locale("vi", "VN");
    public static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
    public static final String ERROR_MESSAGE = "Lỗi thực hiện";

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getExtensionFile(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}
