package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.favorite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.reflect.Array
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


data class sortArray(val articleName : String,val update : String,val _id : String,val imageThumbnail : String,val updatedAt : String)
data class Bookmark(val title : String,val date : String)
class FavoriteList : AppCompatActivity() {
    private var sp : SharedPreferences?=null
    private var token : String?=null
    var mAPIService : ApiServiceMember?=null
    var mAPIServiceContent : ApiServiceContent?=null
    var favList = ArrayList<resultDataFav>()
    var sharedPreferences:SharedPreferences?=null
    var idArticle = ArrayList<String>()
    var updateAt = ArrayList<String>()

    var articles = ArrayList<ArticledataById>()
    var sortArticle = ArrayList<sortArray>()
    var idFav = ArrayList<String>()
    var count = 0
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: BookMarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite)

        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")

       // getBookMarkList()

        backprees.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }

    private fun getBookMarkList(){

        mAPIService = ApiUtilsMember.apiServiceMember
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,"updatedAt").enqueue(object : Callback<Favorite> {

            override fun onResponse(call: Call<Favorite>, response: Response<Favorite>) {
                if (!response.body()!!.resultData.isEmpty()) {
                    try {
                        for (i in 0 until response.body()!!.rowCount) {

                            idArticle.add(response.body()!!.resultData[i].articleId)
                            idFav.add(response.body()!!.resultData[i]._id)
                            updateAt.add(response.body()!!.resultData[i].updatedAt)
                            d("Update", idFav[i])
                        }

                        getArticleById(idArticle, idFav, updateAt)
                        // favList=response.body()!!.resultData
                        //  d("Favorite",idArticle.toString())
                    } catch (e: Exception) {
                        d("Exception", e.toString())

                    }
                }
                else{
                    list_recycler_view.visibility = View.GONE
                }
               // mProgressDialog.dismiss();
            }
            override fun onFailure(call: Call<Favorite>, t: Throwable) {
                d("arm",t.toString())
            }

        })
    }
    private fun getArticleById(data : ArrayList<String>,dataFav : ArrayList<String>,update : ArrayList<String>){
        for (i in 0 until data.size) {

            mAPIServiceContent = ApiUtilsContent.apiService
            sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val partnerId = sharedPreferences!!.getString("partnerId", "-")
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            mAPIServiceContent!!.getArticlesById(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),data[i], partnerId).enqueue(object : Callback<ArticleById> {

                override fun onResponse(call: Call<ArticleById>, response: Response<ArticleById>) {
                    if (response.isSuccessful) {
                        try {

                            articles.add(response.body()!!.resultData)
                            sortArticle.add(sortArray(response.body()!!.resultData.articleName.th,update[i],response.body()!!.resultData._id,response.body()!!.resultData.imageThumbnail,response.body()!!.resultData.updatedAt))
                            //  d("Favorite",articles[i]._id)

                            when (articles.size) {
                                data.size -> {
                                    d("TTT","Do")
                                    sortArticle.sortBy { it.update }
                                    sortArticle.reverse()
                                    d("UPDATE22",sortArticle.toString())
                                    list_recycler_view.apply {
                                        articles.sortBy { it.updatedAt }
                                        articles.reverse()
                                        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@FavoriteList)
                                        adapter = BookMarkAdapter(context,sortArticle,dataFav)
                                    }
                                   /* layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@FavoriteList)
                                    list_recycler_view.layoutManager = layoutManager
                                    adapter = BookMarkAdapter(this@FavoriteList,articles,dataFav)
                                    list_recycler_view.adapter = adapter
                                    adapter.notifyDataSetChanged()*/
                                }
                            }


                        } catch (e: Exception) {
                                d("TTT","FAIL")
                        }
                        // mProgressDialog.dismiss();
                    }
                }
                override fun onFailure(call: Call<ArticleById>, t: Throwable) {
                    d("FailTest", t.toString())
                }

            })


        }



    }

    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()

    }

    override fun onResume() {
        d("RES","YES")
        idFav.clear()
        articles.clear()
        sortArticle.clear()
        idArticle.clear()
        getBookMarkList()
        super.onResume()
    }


}