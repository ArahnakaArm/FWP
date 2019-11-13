package com.example.deimos.fwp

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.articleinfo.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
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
class GalleryInfo : Activity(){
    private val URLImage : String ="http://206.189.41.105:1210/"
    var mAPIService: ApiService? = null
    var ContentInfoArray = ArrayList<Content>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bundle : Bundle ? =intent.extras

        var _id = bundle!!.getString("ID")

        getContent(_id)
      //  Toast.makeText(this@GalleryInfo,_id,Toast.LENGTH_SHORT).show()
        setContentView(R.layout.articleinfo)
        sharebuttoninfo.setOnClickListener {
          //  Toast.makeText(this@GalleryInfo,"Shared",Toast.LENGTH_SHORT).show()
        }
        bookmarkbutton.setOnClickListener {
           // Toast.makeText(this@GalleryInfo,"Bookmarked",Toast.LENGTH_SHORT).show()
        }
        backprees.setOnClickListener {
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
                        var data  = response.body()!!.resultData
                        topicinfo.setText(data.articleName.th)
                        gallery.setText(data.categoryId.categoryName.th)
                        dateContent.setText(data.updatedAt.substring(0..9))
                        descriptiongallery.setText(data.shortDescription.th)


                       /* descriptiongallery.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                                getResources().getDimension(R.dimen.result_font))*/
                        Glide.with(this@GalleryInfo)
                                .load(URLImage+data.imageThumbnail.path)
                                .into(imageinfo)
                    for (i in 0 until data.contentInfo.size){
                        ContentInfoArray.add(data.contentInfo[i])
                    }
                        ContentInfoArray.sortBy { it.orderBy }
                    for (i in 0 until ContentInfoArray.size){
                        if(ContentInfoArray[i].messageType == "Image"){
                            d("ImageDetect",ContentInfoArray[i].image.path)
                        }
                        else{
                            d("ImageDetect",ContentInfoArray[i].articleDescription.th)
                        }
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
}