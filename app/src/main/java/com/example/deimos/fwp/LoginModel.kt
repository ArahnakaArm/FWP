package com.example.deimos.fwp



class LoginModel {


    private var email : String?=null
    private var password : String?=null

    constructor(nemail:String,password: String) {

        this.email = email
        this. password = password




    }

    fun getEmail():String{
        return email.toString()
    }
    fun setEmail(email: String){
        this.email=email

    }
    fun getPassword():String{
        return password.toString()
    }
    fun setPassword(password: String){
        this.password=password
    }




}