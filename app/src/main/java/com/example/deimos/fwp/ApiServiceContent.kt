package com.example.deimos.fwp

import retrofit2.Call
import retrofit2.http.*

interface ApiServiceContent {

    ////News////


    @Headers("Content-Type: application/json")
    @GET("categories")
    fun getCategories(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                          @Query ("partnerId") partnerId : String,@Query ("categoryName.th") categoryName : String):Call<CateModel>

    @Headers("Content-Type: application/json")
    @GET("categories")
    fun getCategoriesAll(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                      @Query ("partnerId") partnerId : String):Call<CateModel>



    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticles(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                    @Query ("partnerId") partnerId : String,@Query("categoryInfo.categoryId")categoryId:String):Call<ArticleModel>

    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesSearchLimit(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                               @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String,@Query ("offset") offset : Int, @Query ("limit") limit : Int,@Query("searchAll")searchAll:String,@Query ("orderby") orderby : String):Call<ArticleModel>

    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesSearchLimitAll(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                               @Query ("partnerId") partnerId : String,@Query ("offset") offset : Int, @Query ("limit") limit : Int,@Query("searchAll")searchAll:String,@Query ("orderby") orderby : String,@Query ("isPublish") isPublish : Boolean = true):Call<ArticleModel>




    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesLimit(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                         @Query ("partnerId") partnerId : String,@Query("categoryInfo.categoryId")categoryId:String,@Query ("offset") offset : Int, @Query ("limit") limit : Int,@Query ("isSlideNew") isSlideNew : Boolean,@Query ("orderby") orderby : String,@Query ("isPublish") isPublish : Boolean = true):Call<ArticleModel>

    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesBigImage(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                            @Query ("partnerId") partnerId : String,@Query("categoryInfo.categoryId")categoryId:String, @Query ("limit") limit : Int,@Query ("isSlideNew") isSlideNew : Boolean,@Query ("orderby") orderby : String,@Query ("isPublish") isPublish : Boolean = true):Call<ArticleModel>



    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getArticlesSlide(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                         @Query ("partnerId") partnerId : String,@Query("categoryInfo.categoryId")categoryId:String,@Query ("isSlideNew") isSlideNew : Boolean = true,@Query ("isPublish") isPublish : Boolean =true):Call<ArticleModel>





    @Headers("Content-Type: application/json")
    @GET("articles")
    fun getSearchArticles(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                          @Query ("partnerId") partnerId : String,@Query("categoryId")categoryId:String,@Query("searchAll")searchAll:String):Call<ArticleModel>



    @Headers("Content-Type: application/json")
    @GET("articles/{articleId}")
    fun getArticlesById(@Header ("x-session-id") header : String,@Header ("x-tid") header2 : String,
                        @Path ("articleId")articleId :String, @Query ("partnerId") partnerId : String):Call<ArticleById>

    ////News////

    //////Video ////////
    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getVideo(@Header("x-session-id") header : String,
                 @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String, @Query("offset") offset : Int, @Query("limit") limit : Int): Call<VideosModel>




    @Headers("Content-Type: application/json")
    @GET("videos")
    fun getSearchVideo(@Header("x-session-id") header : String,
                       @Header("x-tid") header2 : String, @Query("partnerId") partnerId : String, @Query("searchAll") searchAll : String, @Query("offset") offset : Int, @Query("limit") limit : Int, @Query("orderby") orderby : String,@Query("isPublish") isPublish : Boolean = true): Call<VideosModel?>


    @Headers("Content-Type: application/json")
    @GET("videos/{videoId}")
    fun getVideoById(@Header("x-session-id") header : String,
                     @Header("x-tid") header2 : String, @Path("videoId")locationId :String, @Query("partnerId") partnerId : String): Call<VideoById>


    //////Video ////////

}
object ApiUtilsContent {
    var API = BuildConfig.API_BASE
    var PDT ="fwp-api.evolka.in"
    var DEV = "167.71.194.165"
    var SQA = "206.189.41.105"
    var BASE_URL = "https://$API/ctf/v1/"
    val apiService: ApiServiceContent
        get() = RetrofitClient.getClient(BASE_URL)!!.create(ApiServiceContent::class.java)

}
