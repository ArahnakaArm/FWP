package com.example.deimos.fwp

import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET



interface ApiService {
    @GET("/users")
    fun fecthAllUsers(): Call<ArrayList<SearchModel>>

    @Headers("Content-Type: application/json")
    @POST("signup")
    fun createSignup(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
    @Body registerModel: RegisterModel):Call<RegisterModel>


    @Headers("Content-Type: application/json")
    @POST("auth-email")
    fun userLogin(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                     @Body userModel: UserModel):Call<UserMo>


    @Headers("Content-Type: application/json")
    @GET("members/me")
    fun getUser(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String): Call<UserProfile>

    @Headers("Content-Type: application/json")
    @PUT("members/{memberId}")
    fun updateUser(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                   @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Body changePasswordModel: ChangePasswordModel): Call<ChangePasswordModel>




}

object ApiUtils {

    val BASE_URL = "http://206.189.41.105:1210/api/v1/"

    val apiService: ApiService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(ApiService::class.java)

}