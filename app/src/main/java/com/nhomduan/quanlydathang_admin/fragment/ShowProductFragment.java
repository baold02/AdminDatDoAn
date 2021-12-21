package com.nhomduan.quanlydathang_admin.fragment;

import static com.nhomduan.quanlydathang_admin.Utils.OverUtils.ERROR_MESSAGE;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseError;
import com.nhomduan.quanlydathang_admin.R;
import com.nhomduan.quanlydathang_admin.Utils.OverUtils;
import com.nhomduan.quanlydathang_admin.dao.ProductDao;
import com.nhomduan.quanlydathang_admin.interface_.IAfterGetAllObject;
import com.nhomduan.quanlydathang_admin.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ShowProductFragment extends Fragment {
    private Toolbar toolbar;
    private ScrollView scrollView2;
    private TextView tvNameProduct;
    private ImageView imgProduct;
    private TextView btnDecrease;
    private TextView tvQuantity;
    private TextView btnIncrease;
    private TextView tvPriceProduct;
    private TextView tvSalePriceProduct;
    private TextView tvreduce;
    private TextView tvDescription;
    private TextView tvPreservation;
    private TextView tvRation;
    private TextView tvTimeManagement;
    private TextView tvProcessingTime;
    private LinearLayout linearLayout4;
    private ToggleButton btnLike;
    private AppCompatButton btnMuaNgay;
    private AppCompatButton btnAddToCard;


    private Product product;
    private String productId;

    private static int soLuong = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setUpToolbar();
        getData();
        setUpProduct();
        setUpActionEmulator();
    }

    private void setUpToolbar() {
        toolbar.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setUpActionEmulator() {
        btnAddToCard.setOnClickListener(v -> {
            OverUtils.makeToast(getContext(), "Khách hàng thêm sản phẩm vào giỏ hàng");
            soLuong = 1;
            tvQuantity.setText(String.valueOf(soLuong));
        });

        btnLike.setOnClickListener(v -> {
            if(btnLike.isChecked()) {
                OverUtils.makeToast(getContext(), "Khánh hàng yêu thích sản phẩm");
            } else {
                OverUtils.makeToast(getContext(), "Khánh hàng bỏ yêu thích sản phẩm");
            }
        });

        btnDecrease.setOnClickListener(v -> {
            OverUtils.makeToast(getContext(), "Khánh hàng giảm số lượng sản phẩm muốn chọn");
            soLuong--;
            tvQuantity.setText(String.valueOf(soLuong));
        });

        btnIncrease.setOnClickListener(v -> {
            OverUtils.makeToast(getContext(), "Khánh hàng tăng số lượng sản phẩm muốn chọn");
            soLuong++;
            tvQuantity.setText(String.valueOf(soLuong));
        });


    }

    private void setUpProduct() {
        ProductDao.getInstance().getProductByIdListener(productId, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if (obj != null) {
                    product = (Product) obj;
                    buildComponents(product);
                }
            }

            @Override
            public void onError(DatabaseError error) {
                OverUtils.makeToast(getContext(), ERROR_MESSAGE);
            }
        });
    }

    private void buildComponents(Product product) {
        String name = product.getName();
        String img = product.getImage();
        int price = product.getGia_ban();
        float sale = product.getKhuyen_mai();
        String description = product.getMota();
        String preservation = product.getThong_tin_bao_quan();
        String khauPhan = product.getKhau_phan();
        String Daysofstorage = product.getBao_quan();
        int Processingtime = product.getThoiGianCheBien();

        if(!product.getTrang_thai().equals(OverUtils.HOAT_DONG)) {
            if(product.getTrang_thai().equals(OverUtils.DUNG_KINH_DOANH)) {
                btnMuaNgay.setText("Dừng Bán");
            } else if(product.getTrang_thai().equals(OverUtils.HET_HANG)){
                btnMuaNgay.setText("Hết Hàng");
            } else if(product.getTrang_thai().equals(OverUtils.SAP_RA_MAT)) {
                btnMuaNgay.setText("Sắp ra mắt");
            }
            btnMuaNgay.setBackgroundColor(Color.LTGRAY);
            btnMuaNgay.setEnabled(false);
        } else {
            btnMuaNgay.setEnabled(true);
            btnMuaNgay.setOnClickListener(v -> {
                OverUtils.makeToast(getContext(), "Khách hàng được chuyển đến màn hình đặt hàng");
                soLuong = 1;
                tvQuantity.setText(String.valueOf(soLuong));
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
        tvProcessingTime.setText(Processingtime + " phút");
    }

    private void getData() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            productId = bundle.getString("product_id");
        }
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        scrollView2 = view.findViewById(R.id.scrollView2);
        tvNameProduct = view.findViewById(R.id.tvNameProduct);
        imgProduct = view.findViewById(R.id.imgProduct);
        btnDecrease = view.findViewById(R.id.btnDecrease);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        btnIncrease = view.findViewById(R.id.btnIncrease);
        tvPriceProduct = view.findViewById(R.id.tvPriceProduct);
        tvSalePriceProduct = view.findViewById(R.id.tvSalePriceProduct);
        tvreduce = view.findViewById(R.id.tvreduce);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvPreservation = view.findViewById(R.id.tvPreservation);
        tvRation = view.findViewById(R.id.tvRation);
        tvTimeManagement = view.findViewById(R.id.tvTimeManagement);
        tvProcessingTime = view.findViewById(R.id.tvProcessingTime);
        linearLayout4 = view.findViewById(R.id.linearLayout4);
        btnLike = view.findViewById(R.id.btnLike);
        btnMuaNgay = view.findViewById(R.id.btnMuaNgay);
        btnAddToCard = view.findViewById(R.id.btnAddToCard);
    }
}