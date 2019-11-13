package com.example.deimos.fwp

import com.google.gson.annotations.SerializedName


class RegisterModel {
    private var resultCode:String?=null
    private var developerMessage:String?=null

    private var firstName: String? = null
    private var lastName: String? = null
    private var birthDate : String?=null
    private var gender  : String?=null
    private var email : String?=null
    private var password : String?=null

    constructor(firstName: String,lastName: String,birthDate: String,gender : String,email:String,password:String) {
        this.firstName = firstName
        this.lastName = lastName
        this.birthDate = birthDate
        this.gender = gender
        this.email = email
        this. password = password



    }
    fun getResultCode():String{
        return resultCode.toString()
    }

    fun getFirstName(): String {
        return firstName.toString()
    }


    fun setFirstNAme(firstName: String) {
        this.firstName = firstName
    }
    fun getLastName(): String {
        return lastName.toString()
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
    }
    fun getBirthdate(): String {
        return birthDate.toString()
    }

    fun setBirthdate(birthdate: String) {
        this.birthDate = birthDate
    }
    fun getGender():String{
        return gender.toString()
    }
    fun setGender(gender: String){
        this.gender = gender

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