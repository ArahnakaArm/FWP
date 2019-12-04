package com.example.deimos.fwp

import retrofit2.Call
import retrofit2.http.*

interface ApiServiceLocation {


    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getLocation(@Header("x-session-id") header : String,
                    @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String): Call<LocationModel>


    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getSearchLocation(@Header("x-session-id") header : String,
                          @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String, @Query("searchAll") searchAll : String): Call<LocationModel>



    @Headers("Content-Type: application/json")
    @GET("locations/{locationId}")
    fun getLocationById(@Header("x-session-id") header : String,
                        @Header("x-tid") header2 : String, @Path("locationId")locationId :String, @Query("partnerId") partnerId : String): Call<LocationById>



    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getLocationPinMap(@Header("x-session-id") header : String,
                          @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String): Call<LocationModel>




}

object ApiUtilsLocation2 {

    var BASE_URLLocation = "http://167.71.194.165:3000/olf/v1/"
    val apiServiceLocation: ApiServiceLocation
        get() = RetrofitClientLocation.getClient(BASE_URLLocation)!!.create(ApiServiceLocation::class.java)

}
