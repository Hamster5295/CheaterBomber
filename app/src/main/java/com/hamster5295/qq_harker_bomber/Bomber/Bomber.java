package com.hamster5295.qq_harker_bomber.Bomber;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Bomber implements Serializable {

    private static final long serialVersionUID = 1386583756403881124L;

    private String url;

    private ArrayList<String> format;

    private String description;


    //1 true , 0 false
    private int useBase64 = 0;
    private int useProxy = 0;

    public boolean getUseGet() {
        return useGet == 1;
    }

    public void setUseGet(boolean useGet) {
        this.useGet = useGet ? 1 : 0;
    }

    private int useGet = 0;

    public Bomber(String url, String description, int useBase64, int useProxy) {
        this.url = url;
        this.description = description;
        this.useBase64 = useBase64;
        this.useProxy = useProxy;
    }

    public Bomber() {
        this.url = "www.sample.com";
        this.format = new ArrayList<>();
        this.description = "Input your url";
        this.useBase64 = 0;
        this.useProxy = 0;
    }

    protected Bomber(Parcel in) {
        url = in.readString();
        description = in.readString();
        useBase64 = in.readInt();
        useProxy = in.readInt();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFormat(ArrayList<String> format) {
        this.format = format;
    }

    public ArrayList<String> getFormat() {
        return format;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getUseBase64() {
        return useBase64 == 1;
    }

    public void setUseBase64(boolean useBase64) {
        this.useBase64 = useBase64 ? 1 : 0;
    }

    public boolean getUseProxy() {
        return useProxy == 1;
    }

    public void setUseProxy(int useProxy) {
        this.useProxy = useProxy;
    }


    @Override
    public String toString() {
        return url + "&" + description;
    }
}
