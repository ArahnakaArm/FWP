package com.example.deimos.fwp

class SearchModel {

    private var name: String? = null
   private var username: String? = null
    private var date : String?=null
    constructor(name: String, date: String,username: String) {
        this.name = name
        this.username = username
        this.date = date


    }
    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }
    fun getUsername(): String {
        return username.toString()
    }

    fun setUsername(username: String) {
        this.username = username
    }
    fun getDate(): String {
        return date.toString()
    }

    fun setDate(username: String) {
        this.date = date
    }


}