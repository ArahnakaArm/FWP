package com.example.deimos.fwp

class BookMarkModel {

    private var name: String? = null
    private var date: String? = null
    constructor(name: String, date: String) {
        this.name = name
        this.date = date


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


}