package com.example.deimos.fwp

import retrofit2.Call
import retrofit2.http.*

interface ApiServiceAuth {

    @GET("/users")
    fun fecthAllUsers(): Call<ArrayList<SearchModel>>

    @Headers("Content-Type: application/json")
    @POST("signup")
    fun createSignup(@Header("x-session-id") header : String, @Header("x-tid") header2 : String,
                     @Body registerModel: RegisterModel): Call<RegisterModel>


    @Headers("Content-Type: application/json")
    @POST("auth-email")
    fun userLogin(@Header("x-session-id") header : String, @Header("x-tid") header2 : String,
                  @Body userModel: UserModel,@Query("partnerId") partnerId : String): Call<UserMo>


    @Headers("Content-Type: application/json")
    @GET("auth-google")
    fun userLoginGoogle(@Header("x-session-id") header : String, @Header("x-tid") header2 : String,
                        @Query("access_token") access_token : String,@Query("partnerId") partnerId : String): Call<UserMo>


    @Headers("Content-Type: application/json")
    @GET("auth-facebook")
    fun userLoginFacebook(@Header("x-session-id") header : String, @Header("x-tid") header2 : String,
                          @Query("access_token") access_token : String,@Query("partnerId") partnerId : String): Call<UserMo>

}