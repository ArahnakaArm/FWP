package com.example.deimos.fwp

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceComplian {

    //////Complian //////
    @Headers("Content-Type: application/json")
    @GET("complians")
    fun getComplian(@Header("Authorization") auth: String, @Header("x-session-id") header : String, @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String): Call<List<CompliansData>>

    @Headers("Content-Type: application/json")
    @POST("complians")
    fun postComplians(@Header("Authorization") auth: String, @Header("x-session-id") header : String,
                      @Header("x-tid") header2 : String, @Body compliansRequestModel: CompliansRequestModel,@Query("partnerId")partnerId:String): Call<responseComplian>

    @Headers("Content-Type: application/json")
    @POST("complians")
    fun postCompliansNoImage(@Header("Authorization") auth: String, @Header("x-session-id") header : String,
                      @Header("x-tid") header2 : String, @Body compliansRequestModelNoImage: CompliansRequestModelNoImage,@Query("partnerId")partnerId:String): Call<responseComplian>







    @Headers("Content-Type: application/json")
    @POST("complians")
    fun postCompliansNoToken(@Header("x-session-id") header : String,
                             @Header("x-tid") header2 : String, @Body compliansRequestModel: CompliansRequestModel,@Query("partnerId")partnerId:String): Call<responseComplian>



    @Headers("Content-Type: application/json")
    @POST("complians")
    fun postCompliansNoTokenNoImage(@Header("x-session-id") header : String,
                             @Header("x-tid") header2 : String, @Body compliansRequestModel: CompliansRequestModelNoImage,@Query("partnerId")partnerId:String): Call<responseComplian>


    @Multipart
    @POST("complians/{compliansId}/image")
    fun postComplianImage(@Header("Authorization") auth: String, @Header("x-session-id") header : String,
                          @Header("x-tid") header2 : String, @Path("compliansId") compliansId : String, @Part file: MultipartBody.Part): Call<UserProfile>


    @Multipart
    @POST("complians/{compliansId}/image")
    fun postComplianImageNotoken(@Header("x-session-id") header : String,
                                 @Header("x-tid") header2 : String, @Path("compliansId") compliansId : String, @Part file: MultipartBody.Part): Call<UserProfile>



    @Headers("Content-Type: application/json")
    @GET("complianTypes")
    fun getType(@Header("x-session-id") header : String, @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String): Call<CompliansType>


    @Headers("Content-Type: application/json")
    @GET("complians")
    fun getComplianList(@Header("Authorization") auth: String, @Header("x-session-id") header : String, @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String): Call<ComplianModel>



    @Headers("Content-Type: application/json")
    @GET("complians")
    fun getSearchComplianList(@Header("Authorization") auth: String, @Header("x-session-id") header : String, @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String, @Query("searchAll") searchAll : String): Call<ComplianModel>


    //////Complaian //////

}