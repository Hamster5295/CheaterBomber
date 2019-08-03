package com.hamster5295.qq_harker_bomber.Bomber;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Bomber implements Parcelable, Serializable {

    private static final long serialVersionUID = 1386583756403881124L;

    private String url;
    private String userKey;
    private String passwordKey;
    private String extra = "";
    private String prefix = "";

    private String description;


    //1 true , 0 false
    private int useExtra = 1;
    private int usePrefix = 0;
    private int useBase64 = 0;
    private int useProxy = 0;

    public boolean getUseGet() {
        return useGet == 1;
    }

    public void setUseGet(boolean useGet) {
        this.useGet = useGet ? 1 : 0;
    }

    private int useGet = 0;

    public Bomber(String url, String userKey, String passwordKey, String extra, String prefix, String description, int useExtra, int usePrefix, int useBase64, int useProxy) {
        this.url = url;
        this.userKey = userKey;
        this.passwordKey = passwordKey;
        this.extra = extra;
        this.prefix = prefix;
        this.description = description;
        this.useExtra = useExtra;
        this.usePrefix = usePrefix;
        this.useBase64 = useBase64;
        this.useProxy = useProxy;
    }

    public Bomber() {
        this.url = "www.sample.com";
        this.userKey = "";
        this.passwordKey = "";
        this.extra = "";
        this.prefix = "";
        this.description = "Input your url";
        this.useExtra = 1;
        this.usePrefix = 0;
        this.useBase64 = 0;
        this.useProxy = 0;
    }

    protected Bomber(Parcel in) {
        url = in.readString();
        userKey = in.readString();
        passwordKey = in.readString();
        extra = in.readString();
        prefix = in.readString();
        description = in.readString();
        useExtra = in.readInt();
        usePrefix = in.readInt();
        useBase64 = in.readInt();
        useProxy = in.readInt();
    }

    public static final Creator<Bomber> CREATOR = new Creator<Bomber>() {
        @Override
        public Bomber createFromParcel(Parcel in) {
            return new Bomber(in);
        }

        @Override
        public Bomber[] newArray(int size) {
            return new Bomber[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getPasswordKey() {
        return passwordKey;
    }

    public void setPasswordKey(String passwordKey) {
        this.passwordKey = passwordKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getUseExtra() {
        return useExtra == 1;
    }

    public void setUseExtra(boolean useExtra) {
        this.useExtra = useExtra ? 1 : 0;
    }

    public boolean getUsePrefix() {
        return usePrefix == 1;
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix ? 1 : 0;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel p, int i) {
        p.writeString(url);
        p.writeString(userKey);
        p.writeString(passwordKey);
        p.writeString(extra);
        p.writeString(prefix);
        p.writeString(description);

        p.writeInt(useExtra);
        p.writeInt(usePrefix);
        p.writeInt(useBase64);
        p.writeInt(useProxy);
    }

    @Override
    public String toString() {
        return url + "&" + description;
    }
}
