package com.example.deimos.fwp

class BookMarkModel {

    private var name: String? = null
    private var date: String? = null
    private var ImageUrl : String?=null
    constructor(name: String, date: String, ImageUrl : String) {
        this.name = name
        this.date = date
        this.ImageUrl = ImageUrl


    }
    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }
    fun getImageUrl(): String {
        return ImageUrl.toString()
    }

    fun setImageUrl(ImageUrl: String) {
        this.ImageUrl = ImageUrl
    }
    fun getDate(): String {
        return date.toString()
    }

    fun setDate(date: String) {
        this.date = date
    }


}