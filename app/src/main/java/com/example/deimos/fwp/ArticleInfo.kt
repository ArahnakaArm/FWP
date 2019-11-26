package com.example.deimos.fwp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.articleinfo.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class ArticleById(var resultCode: String,var developerMessage : String,var resultData: ArticledataById)
data class ArticledataById(var _id : String,var articleName: articledataName,var shortDescription : articledataDescription,var categoryId : categoryArticle,
                           var updatedAt :String,var imageThumbnail : ImageArticleById,var contentInfo : ArrayList<Content>,var viewCount : Int)
data class ImageArticleById(var path : String)
data class Content(var image : ArticleImage,var orderBy : Int,var messageType : String,var articleDescription : articleDescriptionInfo)
data class articleDescriptionInfo(var th: String,var en: String)
data class ArticleImage(var path : String)
data class articledataName(var en:String,var th:String)
data class articledataDescription(var en : String,var th :String)
data class categoryArticle(var categoryName : categoryNameArticledata)
data class categoryNameArticledata(var en:String,var th : String)
data class Favorite(var resultData : ArrayList<resultDataFav>,var rowCount:Int)
data class resultDataFav(var articleId :IdArticle,var _id : String)
data class nameArticle(var en: String,var th: String)
data class postFavoriteModel(var articleId : String , var partnerId : String)
data class ResponseFav(var resultCode: String,var developerMessage: String,var resultData:responeFavData)
data class responeFavData(var _id : String)

data class IdArticle(var _id : String,var updatedAt : String,var imageThumbnail : ImageThumb,var articleName :  nameArticle )
data class ImageThumb(var path : String)
class ArticleInfo : Activity(){
    private val URLImage : String ="http://206.189.41.105:1210/"
    var mAPIService: ApiService? = null
    private var token : String?=null
    private var articleID : String?=null
    private var favoriteID : String?=null
    private var sp : SharedPreferences?=null
    var ContentInfoArray = ArrayList<Content>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articleinfo)
        var bundle : Bundle ? =intent.extras
        var _id = bundle!!.getString("ID")
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        if(token == "-"){
            bookmarkbutton.visibility = View.INVISIBLE
            bookmarkbuttonorange.visibility = View.INVISIBLE
        }
    try {
        getContent(_id)

    }catch (e : Exception){

    }
        bookmarkbutton.setOnClickListener {
            try {
                postFavorite()
            }catch (e : Exception){

            }

        }

        bookmarkbuttonorange.setOnClickListener {
            try {
                deleteFavorite()
            }catch (e : Exception){

            }

        }
       backarrow.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
    }

    private fun getContent(id:String?){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(java.util.Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticlesById(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),id!!,partnerId).enqueue(object : Callback<ArticleById> {
            override fun onResponse(call: Call<ArticleById>, response: Response<ArticleById>) {
                    if(response.isSuccessful){
                        try {
                        var data  = response.body()!!.resultData
                            var dateFormate =data.updatedAt.substring(0..10)
                            var dateOutput =Profilewithpicture.ConvertDate.ChangeFormatDate(dateFormate.substring(0..3),dateFormate.substring(5..6),dateFormate.substring(8..9))
                        topicinfo.setText(data.articleName.th)
                        gallery.setText(data.categoryId.categoryName.th)
                            articleID = data._id
                        dateContent.setText(dateOutput)
                        viewValue.setText(data.viewCount.toString())
                        Glide.with(this@ArticleInfo)
                                .load(URLImage+data.imageThumbnail.path)
                                .into(imageinfo)
                    for (i in 0 until data.contentInfo.size){
                        ContentInfoArray.add(data.contentInfo[i])
                    }
                        ContentInfoArray.sortBy { it.orderBy }

                    d("TestInfo2",ContentInfoArray.size.toString())
                        list_recycler_view_article.apply {
                            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ArticleInfo)
                            adapter = ArticleInfoAdapter(this@ArticleInfo,ContentInfoArray)
                        }
                            getFavorite()
                    }catch (e : Exception){
                        }
                    }
                else{
                      }
                    }
            override fun onFailure(call: Call<ArticleById>, t: Throwable) {
                Log.d("arm", "onFailure")
            }

        })


    }
    private fun getFavorite(){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<Favorite>{
            override fun onResponse(call: Call<Favorite>, response: Response<Favorite>) {
                if (response.isSuccessful) {
                    try {
                        for (i in 0 until response.body()!!.rowCount){
                            d("CheckFav",response.body()!!.resultData[i].articleId._id)
                            if(response.body()!!.resultData[i].articleId._id == articleID){
                                d("CheckFav","Yes")
                                favoriteID = response.body()!!.resultData[i]._id
                                bookmarkbutton.visibility = View.INVISIBLE
                                bookmarkbuttonorange.visibility = View.VISIBLE
                            }
                        }

                    } catch (e: Exception) {
                        }
                }
            }
            override fun onFailure(call: Call<Favorite>, t: Throwable) {
                d("arm","onFailure")
            }

        })
    }
    private fun postFavorite(){
            mAPIService = ApiUtils.apiService
            val partnerId = "5dbfe99c776a690010deb237"
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            mAPIService!!.postFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),postFavoriteModel(articleID!!,partnerId)).enqueue(object : Callback<ResponseFav>{
                override fun onResponse(call: Call<ResponseFav>, response: Response<ResponseFav>) {
                    if (response.isSuccessful) {
                        try {
                            favoriteID = response.body()!!.resultData._id
                                    bookmarkbutton.visibility = View.INVISIBLE
                                    bookmarkbuttonorange.visibility = View.VISIBLE

                        } catch (e: Exception) {
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseFav>, t: Throwable) {
                    d("arm","onFailure")
                }

            })
        }
    private fun deleteFavorite(){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.deleteFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),favoriteID!!).enqueue(object : Callback<ResponseFav>{
            override fun onResponse(call: Call<ResponseFav>, response: Response<ResponseFav>) {
                if (response.isSuccessful) {
                    try {
                        bookmarkbutton.visibility = View.VISIBLE
                        bookmarkbuttonorange.visibility = View.INVISIBLE

                    } catch (e: Exception) {
                    }
                }
            }
            override fun onFailure(call: Call<ResponseFav>, t: Throwable) {
                d("arm","onFailure")
            }

        })
    }


}