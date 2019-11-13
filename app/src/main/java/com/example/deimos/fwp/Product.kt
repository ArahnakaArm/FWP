package com.example.deimos.fwp

import android.os.Parcel
import android.os.Parcelable

class Product : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val name: String?
    val info: String?
    val tel: String?
    val email: String?
    val lat: String?
    val lng: String?
    val id: String?
    constructor(name: String, info: String, tel: String, email: String, lat: String, lng: String, id :String) {
        this.name = name
        this.info = info
        this.tel = tel
        this.email = email
        this.lat = lat
        this.id = id
        this.lng = lng

    }


    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        info = `in`.readString()
        tel = `in`.readString()
        email = `in`.readString()
        lat = `in`.readString()
        lng = `in`.readString()
        id = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }


}
