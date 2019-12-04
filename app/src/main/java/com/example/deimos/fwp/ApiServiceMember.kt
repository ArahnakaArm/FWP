package com.example.deimos.fwp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceMember {
    //// Fav ////

    @Headers("Content-Type: application/json")
    @GET("favoriteItems")
    fun getFavorite(@Header("Authorization") auth: String, @Header("x-session-id") header : String,
                    @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String): Call<Favorite>



    @Headers("Content-Type: application/json")
    @POST("favoriteItems")
    fun postFavorite(@Header("Authorization") auth: String, @Header("x-session-id") header : String,
                     @Header("x-tid") header2 : String, @Body PostFavoriteModel: postFavoriteModel, @Query("partnerId") partnerId : String): Call<ResponseFav>


    @Headers("Content-Type: application/json")
    @DELETE("favoriteItems/{favoriteId}")
    fun deleteFavorite(@Header("Authorization") auth: String, @Header("x-session-id") header : String,
                       @Header("x-tid") header2 : String, @Path("favoriteId")favoriteId:String, @Query("partnerId") partnerId : String): Call<ResponseFav>

    /// Fav ////


    @Headers("Content-Type: application/json")
    @PUT("members/{memberId}/change-password")
    fun changePassword (@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                        @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Body changeProfileModel: ChangePasswordModel,@Query("partnerId")partnerId:String): Call<ResponeModel>



    @Headers("Content-Type: application/json")
    @POST("forgot-password")
    fun forgotPassword (@Header ("x-session-id") header : String,
                        @Header ("x-tid") header2 : String, @Query("role") Member : String, @Body forgotModel: ForgotModel, @Query("partnerId") partnerId : String): Call<ResponeModel>

    @Headers("Content-Type: application/json")
    @POST("signup")
    fun createSignup(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                     @Body registerModel: RegisterModel,@Query("partnerId") partnerId : String):Call<RegisterModel>



    @Headers("Content-Type: application/json")
    @GET("members/me")
    fun getUser(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,@Query ("partnerId") partnerId : String): Call<UserProfile>

    @Headers("Content-Type: application/json")
    @PATCH("members/{memberId}")
    fun updateUser(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                   @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Body changeProfileModel: ChangeProfileModel): Call<ChangeProfileModel>


    @Multipart
    @Headers("Content-Type: application/json")
    @PUT("members/{memberId}/image")
    fun uploadImage (@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                     @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Part image: RequestBody): Call<ResponeModel>

    @Multipart
    @POST("members/{memberId}/image")
    fun postFile(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                 @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Part file: MultipartBody.Part): Call<UserProfile>


    @Headers("Content-Type: application/json")
    @PATCH("members/{memberId}")
    fun patchFile(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                 @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Query("partnerId")partnerId:String,@Body image:imageBody): Call<UserProfile>

}