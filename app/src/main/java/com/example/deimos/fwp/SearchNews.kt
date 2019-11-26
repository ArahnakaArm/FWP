package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
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
class SearchNews : AppCompatActivity(){
    var mAPIService: ApiService? = null
    private var CategoriesId: String?=null
    var news = ArrayList<ArticleData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchnews)
        getCategories()


        searchnews.textChanges().debounce(300,TimeUnit.MILLISECONDS).subscribe({it ->
            try {
                d("SEARCH", it.toString())
                searchingArticle(it.toString())
            }catch (e : Exception){

            }
        },{ throwable -> d("Error",throwable.message)})




        ///// Searching  API/////
        backpreesnews.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

        }

    }




    private fun getArticle(id : String?){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticles(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,id!!).enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {

                        upDateUi(response.body()!!.resultData,response.body()!!.rowCount)

                }catch (e : Exception){}
            }
            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }
    private fun upDateUi(data : ArrayList<ArticleData>,Count : Int){
        var layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchNews)
        list_recycler_view_news.layoutManager = layoutManager

        var adapter = NewsAdapter(this@SearchNews,data)
        list_recycler_view_news.adapter = adapter
     /*   list_recycler_view_news.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchNews)
            adapter = NewsAdapter(context,data)

        }

*/


        list_recycler_view_news.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var previousTotal = 0
            var isLoading = false
            val visibleThreshold = 10
            var firstVisibleItem = 0
            var visibleItemCount = 0
            var totalItemCount = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = recyclerView.childCount
                totalItemCount = layoutManager!!.itemCount
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
              //  d("TestScroll",firstVisibleItem.toString())
                d("TestScroll","visibleItemCount : "+ visibleItemCount.toString())
                d("TestScroll","totalItemCount : "+ totalItemCount.toString())
                d("TestScroll","firstVisibleItem : "+ firstVisibleItem.toString())
                if (!isLoading) {

                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                     /*   page++
                        getPage()*/
                       d("TestScroll","Yes")
                    }

                }
            }

        })

    }
    private fun getCategories(){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getCategories(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<CateModel> {

            override fun onResponse(call: Call<CateModel>, response: Response<CateModel>) {
                //   d("Video",response.body()!!.resultData[0]._id)
                try {
                    CategoriesId = response.body()!!.resultData[0]._id
                    if(CategoriesId != null){
                        getArticle(CategoriesId)
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
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getSearchArticles(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,CategoriesId!!,text!!).enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {

                    upDateUi(response.body()!!.resultData,response.body()!!.rowCount)

                }catch (e : Exception){}
            }
            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }




}