package com.example.deimos.fwp

class NotificationModel {

    private var name: String? = null
    private var date: String? = null
    private var info :String?=null
    constructor(name: String, date: String,info : String) {
        this.name = name
        this.date = date
        this.info = info


    }
    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }
    fun getDate(): String {
        return date.toString()
    }

    fun setDate(date: String) {
        this.date = date
    }
    fun getInfo(): String {
        return info.toString()
    }

    fun setInfo(info: String) {
        this.info = info
    }


}