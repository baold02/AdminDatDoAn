package com.nhomduan.quanlydathang_admin.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomModule implements Serializable{
    private String id;
    private String title;
    private Function function;
    private int soLuong;
    private List<BannerImage> imageList;

    public CustomModule() {
    }

    public CustomModule(String title, Function function, int soLuong, List<BannerImage> imageList) {
        this.title = title;
        this.function = function;
        this.soLuong = soLuong;
        this.imageList = imageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public List<BannerImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<BannerImage> imageList) {
        this.imageList = imageList;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("title", title);
        map.put("soLuong", soLuong);
        map.put("function", function);
        map.put("imageList", imageList);
        return map;
    }

    public CustomModule customClone() {
        CustomModule obj = new CustomModule();
        String id = "";
        id = this.getId();
        String title = "";
        title = this.title;
        int soLuong = this.soLuong;
        Function function = getFunction();
        function = this.getFunction();
        List<BannerImage> bannerImageList = new ArrayList<>();
        for(int i = 0; i < this.getImageList().size(); i++) {
            String imgLink = this.getImageList().get(i).getImage();
            String productId = this.getImageList().get(i).getProductId();
            BannerImage bannerImage = new BannerImage(imgLink, productId);
            bannerImageList.add(bannerImage);
        }

        obj.setId(id);
        obj.setSoLuong(soLuong);
        obj.setImageList(bannerImageList);
        obj.setFunction(function);
        obj.setTitle(title);
        return obj;
    }


}
