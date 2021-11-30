package com.nhomduan.quanlydathang_admin.activities;


import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;


public class ShowProductActivity extends AppCompatActivity {

    private TextView tvNameProduct;
    private TextView btnDecrease;
    private TextView tvQuantity;
    private TextView btnIncrease;
    private TextView tvreduce;
    private TextView tvPriceProduct, tvSalePriceProduct;
    private TextView tvDescription;
    private TextView tvPreservation;
    private TextView tvRation;
    private TextView tvTimeManagement;
    private TextView tvProcessingTime;
    private ImageView imgProduct;
    private AppCompatButton btnMuaNgay;




    private ToggleButton btnLike;
    private int soLuong = 1;

    private Product productDaChon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        anhXa();
        getDuLieu();
        updateQuantity();


        // setUp btnLike
        setUpBtnLike();
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        ProductDao.getInstance().getProductByIdListener(productId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if (obj != null) {
                    productDaChon = (Product) obj;
                    buildComponent();
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(ShowProductActivity.this, ERROR_MESSAGE);
            }
        });
    }

    private void setUpBtnLike() {
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLike.isChecked()) {
                    OverUtils.makeToast(ShowProductActivity.this, "Người dùng thích sản phẩm " + productDaChon.getName());
                } else {
                    OverUtils.makeToast(ShowProductActivity.this, "Người dùng bỏ thích sản phẩm " + productDaChon.getName());
                }
            }
        });
    }

    private void buildComponent() {
        String name = productDaChon.getName();
        String img = productDaChon.getImage();
        int price = productDaChon.getGia_ban();
        float sale = productDaChon.getKhuyen_mai();
        String description = productDaChon.getMota();
        String preservation = productDaChon.getThong_tin_bao_quan();
        String khauPhan = productDaChon.getKhau_phan();
        String Daysofstorage = productDaChon.getBao_quan();
        int Processingtime = productDaChon.getThoiGianCheBien();

        if(!productDaChon.getTrang_thai().equals(OverUtils.HOAT_DONG)) {
            if(productDaChon.getTrang_thai().equals(OverUtils.DUNG_KINH_DOANH)) {
                btnMuaNgay.setText("Dừng Bán");
            } else if(productDaChon.getTrang_thai().equals(OverUtils.HET_HANG)){
                btnMuaNgay.setText("Hết Hàng");
            } else if(productDaChon.getTrang_thai().equals(OverUtils.SAP_RA_MAT)) {
                btnMuaNgay.setText("Sắp ra mắt");
            }
            btnMuaNgay.setBackgroundColor(Color.LTGRAY);
            btnMuaNgay.setEnabled(false);
        } else {
            btnMuaNgay.setEnabled(true);
            btnMuaNgay.setOnClickListener(v -> {
                OverUtils.makeToast(getApplicationContext(), "Khách hàng được chuyển đến màn hình đặt hàng");
            });
        }

        tvNameProduct.setText(name);
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.ic_image)
                .into(imgProduct);

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);

        if (sale > 0) {
            tvreduce.setText((int) (sale * 100) + "%");
            tvPriceProduct.setText(currencyFormat.format((int) price));
            tvPriceProduct.setPaintFlags(tvPriceProduct.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvSalePriceProduct.setText(currencyFormat.format((int) (price - price * sale)));
        } else {
            tvreduce.setVisibility(View.INVISIBLE);
            tvreduce.setText("0%");
            tvPriceProduct.setText("");
            tvSalePriceProduct.setText(currencyFormat.format((int) price));
        }
        tvDescription.setText(description);
        tvPreservation.setText(preservation);
        tvRation.setText(khauPhan);
        tvTimeManagement.setText(Daysofstorage);
        tvProcessingTime.setText(Processingtime + " min");


    }


    private void updateQuantity() {
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuong > 1) {
                    soLuong -= 1;
                    tvQuantity.setText(String.valueOf(soLuong));
                }
            }
        });
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soLuong < 50) {
                    soLuong += 1;
                    tvQuantity.setText(String.valueOf(soLuong));
                }
            }
        });
    }

    private void anhXa() {
        tvNameProduct = findViewById(R.id.tvNameProduct);
        btnDecrease = findViewById(R.id.btnDecrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        tvreduce = findViewById(R.id.tvreduce);
        tvPriceProduct = findViewById(R.id.tvPriceProduct);
        tvDescription = findViewById(R.id.tvDescription);
        tvPreservation = findViewById(R.id.tvPreservation);
        tvRation = findViewById(R.id.tvRation);
        tvTimeManagement = findViewById(R.id.tvTimeManagement);
        tvProcessingTime = findViewById(R.id.tvProcessingTime);
        imgProduct = findViewById(R.id.imgProduct);
        tvSalePriceProduct = findViewById(R.id.tvSalePriceProduct);
        btnMuaNgay = findViewById(R.id.btnMuaNgay);

        btnLike = findViewById(R.id.btnLike);
        tvQuantity.setText(String.valueOf(soLuong));
    }


    public void btnAddToCard(View view) {
        OverUtils.makeToast(ShowProductActivity.this, "Người dùng đưa sản " + soLuong + " sản phẩm " + productDaChon.getName() + " vào giỏ hàng");
    }

}