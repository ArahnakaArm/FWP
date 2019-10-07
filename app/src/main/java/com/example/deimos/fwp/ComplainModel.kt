package com.example.deimos.fwp

class ComplainModel {

    private var name: String? = null
    private var date: String? = null
    private var status: String? = null
    constructor(name: String, date: String,status:String) {
        this.name = name
        this.date = date
        this.status = status


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
    fun getStatus(): String {
        return status.toString()
    }

    fun setStatus(status: String) {
        this.status = status
    }


}