package com.example.deimos.fwp

import com.google.gson.annotations.SerializedName


class UserModel {

    private var resultCode:String?=null
    private var developerMessage:String?=null


    private var email: String? = null
    private var password: String? = null


    constructor(email: String,password: String) {
        this.email = email
        this.password = password




    }
    fun getEmail(): String {
        return email.toString()
    }
    fun getresultCode(): String {
        return resultCode.toString()
    }
    fun getdeveloperMessage(): String {
        return developerMessage.toString()
    }


    fun setEmail(email: String) {
        this.email = email
    }
    fun getPassword(): String {
        return password.toString()
    }

    fun setPassword(password: String) {
        this.password = password
    }


    class resultData {

        private var access_token: String? = null


        constructor(access_token: String) {
            this.access_token = access_token





        }


        fun getToken(): String {
            return access_token.toString()
        }

        fun setToken(access_token: String) {
            this.access_token = access_token
        }
    }
}



