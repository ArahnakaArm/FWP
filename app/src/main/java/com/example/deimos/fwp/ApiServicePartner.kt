package com.example.deimos.fwp

import retrofit2.Call
import retrofit2.http.*

interface ApiServicePartner {

    @Headers("Content-Type: application/json")
    @GET("appIds/{appId}")
    fun getPartnerId(@Header("x-session-id") header : String,
                        @Header("x-tid") header2 : String, @Path("appId")appId :String): Call<PartnerModel>
}