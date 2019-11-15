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
                           var updatedAt :String,var imageThumbnail : ImageArticleById,var contentInfo : ArrayList<Content>)
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
data class IdArticle(var _id : String)
class ArticleInfo : Activity(){
    private val URLImage : String ="http://206.189.41.105:1210/"
    var mAPIService: ApiService? = null
    private var token : String?=null
    private var articleID : String?=null
    private var sp : SharedPreferences?=null
    var ContentInfoArray = ArrayList<Content>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articleinfo)
        var bundle : Bundle ? =intent.extras
        var _id = bundle!!.getString("ID")


        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")


       /* if(token == "-"){
            bookmarkbutton.visibility = View.INVISIBLE
            bookmarkbuttonorange.visibility = View.INVISIBLE
        }*/
    try {
        getContent(_id)

    }catch (e : Exception){

    }

      //  Toast.makeText(this@GalleryInfo,_id,Toast.LENGTH_SHORT).show()
        setContentView(R.layout.articleinfo)
        sharebuttoninfo.setOnClickListener {
          //  Toast.makeText(this@GalleryInfo,"Shared",Toast.LENGTH_SHORT).show()
        }
        bookmarkbutton.setOnClickListener {
            bookmarkbutton.visibility = View.INVISIBLE
            bookmarkbuttonorange.visibility = View.VISIBLE
        }

        bookmarkbuttonorange.setOnClickListener {
            bookmarkbutton.visibility = View.VISIBLE
            bookmarkbuttonorange.visibility = View.INVISIBLE
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
                        topicinfo.setText(data.articleName.th)
                        gallery.setText(data.categoryId.categoryName.th)
                            articleID = data._id
                        dateContent.setText(data.updatedAt.substring(0..9))
                       // descriptiongallery.setText(data.shortDescription.th)
                       // d("OrderTest",data.updatedAt.substring(0..9))

                       /* descriptiongallery.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                                getResources().getDimension(R.dimen.result_font))*/
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
                                bookmarkbutton.visibility = View.INVISIBLE
                                bookmarkbuttonorange.visibility = View.VISIBLE
                            }
                        }


                    } catch (e: Exception) {

                    }



                }
///// Searching /////




            }
///// Searching /////

            override fun onFailure(call: Call<Favorite>, t: Throwable) {
                d("arm","onFailure")
            }

        })
    }
}