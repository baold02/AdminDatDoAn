package com.nhomduan.quanlydathang_admin.model;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class BannerImage implements Serializable, Cloneable{
    private String image;
    private String productId;

    public BannerImage() {
    }

    public BannerImage(String image, String productId) {
        this.image = image;
        this.productId = productId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
