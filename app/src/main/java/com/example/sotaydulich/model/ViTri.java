package com.example.sotaydulich.model;

import android.net.Uri;

import java.io.Serializable;

public class ViTri implements Serializable {
    private  String id;
    private String diaDiem;
    private String moTa;
    private byte[] uriImg;

    public ViTri(String id,String diaDiem, String moTa, byte[] uriImg) {
        this.id = id;
        this.diaDiem = diaDiem;
        this.moTa = moTa;
        this.uriImg = uriImg;
    }

    public ViTri() {
    }
    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setUriImg(byte[] uriImg) {
        this.uriImg = uriImg;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public String getMoTa() {
        return moTa;
    }

    public byte[] getUriImg() {
        return uriImg;
    }

    public String getId() {
        return id;
    }
}
