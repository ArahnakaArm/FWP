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
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.bookmark.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater
class SearchNews : AppCompatActivity(){
    private var etsearch: EditText? = null
    internal var textlength = 0
    var mAPIService: ApiService? = null
    private val adaptertest: CustomAdapter? = null
    var usr : Userstate = Userstate()
    var array_sort = java.util.ArrayList<ArticleData>()
    private var CategoriesId: String?=null
    var news = ArrayList<ArticleData>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchnews)
        getCategories()

        ///// Searching /////
        etsearch = findViewById(R.id.searchbookmark) as EditText
        array_sort = java.util.ArrayList<ArticleData>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in news.indices) {
                    if (textlength <= news[i].articleName.th.length) {
                        Log.d("ertyyy", news[i].articleName.th.toLowerCase().trim())
                        if (news[i].articleName.th.toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(news[i])
                        }
                    }
                }
                list_recycler_view.apply {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchNews)
                    adapter = NewsAdapter(context,array_sort)

                }

            }
        })
///// Searching /////

        backprees.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

        }

        searchbookmark.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adaptertest?.filter?.filter(s)

            }

        })
    }




    companion object {
        fun newInstance(): SearchNews = SearchNews()
    }
    private fun populateList(): java.util.ArrayList<ArticleData> {

        val list = java.util.ArrayList<ArticleData>()

        for (i in 0..7) {

           // list.add(ArticleData("",""))
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(
            context: Context,
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            private val clickListener: ClickListener?
    ) : androidx.recyclerview.widget.RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

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
                    if(response.body()!!.resultData[0] != null){
                        upDateUi(response.body()!!.resultData,response.body()!!.rowCount)
                    }
                }catch (e : Exception){}


            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }
    private fun upDateUi(data : ArrayList<ArticleData>,Count : Int){
        list_recycler_view.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@SearchNews)
            adapter = NewsAdapter(context,data)
        }
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


}