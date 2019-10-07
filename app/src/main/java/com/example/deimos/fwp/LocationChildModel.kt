package com.example.deimos.fwp


class LocationChildModel {

    private var name: String? = null
    private var info: String? = null
    private var tel: String? = null
    private var email: String? = null
    constructor(name: String, info: String,tel:String,email:String) {
        this.name = name
        this.info = info
        this.tel = tel
        this.email = email
    }
    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }
    fun getInfo(): String {
        return info.toString()
    }

    fun setInfo(info: String) {
        this.info = info
    }
    fun getTel(): String {
        return tel.toString()
    }

    fun setTel(tel: String) {
        this.tel = tel
    }
    fun getEmail(): String {
        return email.toString()
    }

    fun setEmail(email: String) {
        this.email = info
    }


}