package com.example.deimos.fwp

import com.google.gson.annotations.SerializedName


class ResponeModel {
    @SerializedName("body")
    private var resultCode: String? = null
    private var developerMessage: String? = null


    constructor(resultCode: String,developerMessage: String) {
        this.resultCode = resultCode
        this.developerMessage = developerMessage




    }
    fun getResultCode(): String {
        return resultCode.toString()
    }

    fun setResultCode(resultCode: String) {
        this.resultCode = resultCode
    }
    fun getdeveloperMessage(): String {
        return developerMessage.toString()
    }

    fun setdeveloperMessage(developerMessage: String) {
        this.developerMessage = developerMessage
    }





}