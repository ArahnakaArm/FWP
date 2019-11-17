package com.example.deimos.fwp

import android.net.Uri
import android.provider.MediaStore
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET
import java.nio.file.Files.exists
import retrofit2.http.POST
import retrofit2.http.Multipart







interface ApiService {

    ////News////


 @Headers("Content-Type: application/json")
    @GET("categories")
    fun getCategories(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                        @Query ("partnerId") partnerId : String):Call<CateModel>





   @Headers("Content-Type: application/json")
   @GET("articles")
   fun getArticles(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                     @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String):Call<ArticleModel>


 @Headers("Content-Type: application/json")
 @GET("articles/{articleId}")
 fun getArticlesById(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                     @Path ("articleId")articleId :String, @Query ("partnerId") partnerId : String):Call<ArticleById>












 ////News////
    //////Profile ////////

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
    @GET("auth-google")
    fun userLoginGoogle(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                  @Query ("access_token") access_token : String):Call<UserMo>


    @Headers("Content-Type: application/json")
    @GET("auth-facebook")
    fun userLoginFacebook(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                        @Query ("access_token") access_token : String):Call<UserMo>




    @Headers("Content-Type: application/json")
    @GET("members/me")
    fun getUser(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String): Call<UserProfile>

    @Headers("Content-Type: application/json")
    @PATCH("members/{memberId}")
    fun updateUser(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                   @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Body changeProfileModel: ChangeProfileModel): Call<ChangeProfileModel>


    @Headers("Content-Type: application/json")
    @PUT("members/{memberId}/change-password")
    fun changePassword (@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                        @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Body changeProfileModel: ChangePasswordModel): Call<ResponeModel>



    @Headers("Content-Type: application/json")
    @POST("forgot-password")
    fun forgotPassword (@Header ("x-session-id") header : String,
                        @Header ("x-tid") header2 : String, @Query("role") Member : String, @Body forgotModel: ForgotModel): Call<ResponeModel>


    @Multipart
    @Headers("Content-Type: application/json")
    @PUT("members/{memberId}/image")
    fun uploadImage (@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                        @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Part image: RequestBody): Call<ResponeModel>

    @Multipart
    @POST("members/{memberId}/image")
    fun postFile(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                 @Header ("x-tid") header2 : String,@Path("memberId") memberId : String,@Part file: MultipartBody.Part): Call<UserProfile>


    //////Profile ////////


    //////Video ////////
    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getVideo( @Header ("x-session-id") header : String,
                   @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<VideosModel>



    @Headers("Content-Type: application/json")
    @GET("videos/{videoId}")
    fun getVideoById( @Header ("x-session-id") header : String,
                         @Header ("x-tid") header2 : String,@Path("videoId")locationId :String,@Query("partnerId") partnerId : String): Call<VideoById>









    //////Video ////////

    //////Complian //////
    @Headers("Content-Type: application/json")
    @GET("complians")
    fun getComplian(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<List<CompliansData>>

    @Headers("Content-Type: application/json")
    @POST("complians")
    fun postComplians(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,
                      @Header ("x-tid") header2 : String,@Body compliansRequestModel: CompliansRequestModel): Call<responseComplian>

    @Headers("Content-Type: application/json")
    @POST("complians")
    fun postCompliansNoToken(@Header ("x-session-id") header : String,
                      @Header ("x-tid") header2 : String,@Body compliansRequestModel: CompliansRequestModel): Call<responseComplian>


    @Multipart
    @POST("complians/{compliansId}/image")
    fun postComplianImage(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                 @Header ("x-tid") header2 : String,@Path("compliansId") compliansId : String,@Part file: MultipartBody.Part): Call<UserProfile>


    @Multipart
    @POST("complians/{compliansId}/image")
    fun postComplianImageNotoken(@Header ("x-session-id") header : String,
                          @Header ("x-tid") header2 : String,@Path("compliansId") compliansId : String,@Part file: MultipartBody.Part): Call<UserProfile>



    @Headers("Content-Type: application/json")
    @GET("complianTypes")
    fun getType(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<CompliansType>


   @Headers("Content-Type: application/json")
   @GET("complians")
   fun getComplianList(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call <ComplianModel>



    //////Complaian //////

    /////Location////////


    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getLocation( @Header ("x-session-id") header : String,
                  @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<LocationModel>





    @Headers("Content-Type: application/json")
    @GET("locations/{locationId}")
    fun getLocationById( @Header ("x-session-id") header : String,
                     @Header ("x-tid") header2 : String,@Path("locationId")locationId :String,@Query("partnerId") partnerId : String): Call<LocationById>



    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getLocationPinMap( @Header ("x-session-id") header : String,
                         @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<LocationModel>








    /////Location////////

    //// Fav ////

    @Headers("Content-Type: application/json")
    @GET("favoriteItems")
    fun getFavorite(@Header("Authorization") auth: String, @Header ("x-session-id") header : String,
                     @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<Favorite>


    /// Fav ////

}

object ApiUtils {

    val BASE_URL = "http://206.189.41.105:1210/api/v1/"

    val apiService: ApiService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(ApiService::class.java)

}

