package com.example.deimos.fwp;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    public final String name;
    public final String info;
    public final String tel;
    public final String email;

    public Product(String name,String info,String tel,String email) {
        this.name = name;
        this.info = info;
        this.tel = tel;
        this.email = email;

    }


    protected Product(Parcel in) {
        name = in.readString();
        info = in.readString();
        tel = in.readString();
        email = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
