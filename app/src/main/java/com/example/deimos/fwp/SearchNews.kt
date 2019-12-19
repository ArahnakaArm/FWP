package com.example.deimos.fwp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import bolts.Task.call
import kotlinx.android.synthetic.main.favorite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jakewharton.rxbinding3.widget.textChanges;
import kotlinx.android.synthetic.main.searchnews.*
import rx.functions.Action1
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.zip.Inflater
import kotlin.collections.ArrayList

class SearchNews : AppCompatActivity(),ILoadMore{
    lateinit var adapter: NewsAdapter
    var mAPIService: ApiServiceContent? = null
    private var CategoriesId: String?=null
    private var news = ArrayList<ArticleData?>()
    private var mCurrentPage =0
    private val mItemPerRow = 10
    private var searchState = false
    private var searchText = ""
    private var sharedPreferences:SharedPreferences?=null

    override fun onLoadMore() {
        try {
            d("Detect", "YEsss")
            news.add(null)
            adapter.notifyItemInserted(news.size - 1)
            Handler().postDelayed({
                news.removeAt(news.size - 1)

                LoadMore(CategoriesId!!,searchText)
            }, 2000)
        }catch (e:Exception){

        }



    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchnews)
        getCategories()


        searchnews.textChanges().debounce(450,TimeUnit.MILLISECONDS).subscribe({it ->
            d("Text","Changed")

            try {
                if(searchnews.text.isEmpty()){
                    mCurrentPage=0
                    searchState =false
                    news.clear()
                    searchText = ""
                    getArticle(CategoriesId,searchText)
                }else{
                    searchText=it.toString()
                    searchState= true
                    mCurrentPage=0
                    news.clear()
                    getArticle(CategoriesId,searchText)
                }
              //  d("SEARCH", it.toString())
               // searchingArticle(it.toString())
            }catch (e : Exception){

            }
        },{ throwable -> d("Error",throwable.message)})




        ///// Searching  API/////
        backpreesnews.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

        }

    }




    private fun getArticle(id : String?,text : String){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticlesSearchLimitAll(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,(mItemPerRow*(mCurrentPage)), 10,text!!,"updatedAt").enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {

                    for(i in 0 until response.body()!!.resultData.size) {

                        news.add(response.body()!!.resultData[i])

                    }
                    mCurrentPage++
                    upDateUi(news)

                }catch (e : Exception){}
            }
            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }
    private fun upDateUi(data : ArrayList<ArticleData?>){
        var layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchNews)
        list_recycler_view_news.layoutManager = layoutManager
         adapter = NewsAdapter(list_recycler_view_news,this@SearchNews,data,this@SearchNews)
        list_recycler_view_news.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setLoadMore(this)


     /*   list_recycler_view_news.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchNews)
            adapter = NewsAdapter(context,data)

        }

*/

    }
    private fun getCategories(){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getCategoriesAll(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<CateModel> {

            override fun onResponse(call: Call<CateModel>, response: Response<CateModel>) {
                //   d("Video",response.body()!!.resultData[0]._id)
                try {
                    CategoriesId = response.body()!!.resultData[0]._id
                    if(CategoriesId != null){
                        //getArticle(CategoriesId,searchText)
                    }
                }catch (e : Exception){

                }


            }

            override fun onFailure(call: Call<CateModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }

    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()
    }
    private fun searchingArticle(text : String){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticlesSearchLimit(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,CategoriesId!!,(mItemPerRow*(mCurrentPage-1)), 10,text!!,"updatedAt").enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {
                    for (i in 0 until response.body()!!.resultData.size){

                        news.add(response.body()!!.resultData[i])
                    }

                    upDateUi(news)

                }catch (e : Exception){}
            }
            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }
    private fun LoadMore(id : String,text : String){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences =getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticlesSearchLimitAll(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,(mItemPerRow*(mCurrentPage)), 10,text!!,"updatedAt").enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {

                    for(i in 0 until response.body()!!.resultData.size) {

                        news.add(response.body()!!.resultData[i])


                    }

                     adapter.notifyDataSetChanged()
                    adapter.setLoaded()
                    mCurrentPage++

                }catch (e : Exception){}
            }
            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }



}