package com.example.deimos.fwp

import android.content.res.Resources
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings.Global.getString
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
    @GET("articles")
    fun getArticlesSearchLimit(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                    @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String,@Query ("offset") offset : Int, @Query ("limit") limit : Int,@Query("searchAll")searchAll:String,@Query ("orderby") orderby : String):Call<ArticleModel>

    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesLimit(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                    @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String,@Query ("offset") offset : Int, @Query ("limit") limit : Int,@Query ("isSlideNew") isSlideNew : Boolean,@Query ("orderby") orderby : String):Call<ArticleModel>

    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesBigImage(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                         @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String, @Query ("limit") limit : Int,@Query ("isSlideNew") isSlideNew : Boolean,@Query ("orderby") orderby : String):Call<ArticleModel>



    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesSlide(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                         @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String,@Query ("isSlideNew") isSlideNew : Boolean = true):Call<ArticleModel>





    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getSearchArticles(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                    @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String,@Query("searchAll")searchAll:String):Call<ArticleModel>



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
                   @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String,@Query("offset") offset : Int,@Query("limit") limit : Int,@Query("isPublish") isPublish : Boolean = true): Call<VideosModel>




    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getSearchVideo( @Header ("x-session-id") header : String,
                  @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String,@Query("searchAll") searchAll : String,@Query("offset") offset : Int,@Query("limit") limit : Int,@Query("orderby") orderby : String,@Query("isPublish") isPublish : Boolean = true): Call<VideosModel?>


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



    @Headers("Content-Type: application/json")
    @GET("complians")
    fun getSearchComplianList(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String,@Query("searchAll") searchAll : String): Call <ComplianModel>


    //////Complaian //////

    /////Location////////


    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getLocation( @Header ("x-session-id") header : String,
                  @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String): Call<LocationModel>


    @Headers("Content-Type: application/json")
    @GET("locations")
    fun getSearchLocation( @Header ("x-session-id") header : String,
                     @Header ("x-tid") header2 : String,@Query("partnerId") partnerId : String,@Query("searchAll") searchAll : String): Call<LocationModel>



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



    @Headers("Content-Type: application/json")
    @POST("favoriteItems")
    fun postFavorite(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,
                      @Header ("x-tid") header2 : String,@Body PostFavoriteModel: postFavoriteModel): Call<ResponseFav>


    @Headers("Content-Type: application/json")
    @DELETE("favoriteItems/{favoriteId}")
    fun deleteFavorite(@Header("Authorization") auth: String,@Header ("x-session-id") header : String,
                     @Header ("x-tid") header2 : String,@Path("favoriteId")favoriteId:String): Call<ResponseFav>

    /// Fav ////

}

object ApiUtils {

    val BASE_URL = "http://167.71.194.165:1210/api/v1/"

    val apiService: ApiService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(ApiService::class.java)

}
object ApiUtilsLocation {
    var DEV = "167.71.194.165"
    var SQA = "206.189.41.105"
    var BASE_URLLocation = "http://$SQA:3000/olf/v1/"
    val apiServiceLocation: ApiServiceLocation
        get() = RetrofitClientLocation.getClient(BASE_URLLocation)!!.create(ApiServiceLocation::class.java)

}
object ApiUtilsMember {
    var DEV = "167.71.194.165"
    var SQA = "206.189.41.105"
    var BASE_URLMember = "http://$SQA:3000/mmf/v1/"
    val apiServiceMember: ApiServiceMember
        get() = RetrofitClientMember.getClient(BASE_URLMember)!!.create(ApiServiceMember::class.java)

}
object ApiUtilsComplian {
    var DEV = "167.71.194.165"
    var SQA = "206.189.41.105"
    var BASE_URLComplian = "http://$SQA:3000/cpf/v1/"
    val apiServiceComplian: ApiServiceComplian
        get() = RetrofitClientComplian.getClient(BASE_URLComplian)!!.create(ApiServiceComplian::class.java)

}
object ApiUtilsAuth {
    var DEV = "167.71.194.165"
    var SQA = "206.189.41.105"
    var BASE_URLAuth = "http://$SQA:3000/mmf/v1/"
    val apiServiceAuth: ApiServiceAuth
        get() = RetrofitClientAuth.getClient(BASE_URLAuth)!!.create(ApiServiceAuth::class.java)

}
object ApiUtilsPartner {
    var DEV = "167.71.194.165"
    var SQA = "206.189.41.105"
    var BASE_URLPartner = "http://$SQA:3000/umf/v1/"
    val apiServicePartner: ApiServicePartner
        get() = RetrofitClientPartner.getClient(BASE_URLPartner)!!.create(ApiServicePartner::class.java)

}
