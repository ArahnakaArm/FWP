package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.main.*
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.viewpagerindicator.CirclePageIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class PartnerModel(var resultCode: String,var developerMessage: String,var resultData: resultDataPartner)
data class resultDataPartner(var _id : String)
data class CateModel(var resultCode : String ,var developerMessage : String,var resultData : ArrayList<cateresult>)
data class cateresult(var categoryName : categoryNameArticle,var _id : String)
data class categoryNameArticle(var th : String,var en : String)
data class ArticleModel(var resultCode : String,var developerMessage : String,var resultData : ArrayList<ArticleData>, var rowCount : Int)
data class ArticleData(var articleName : articlename,var shortDescription : shortDescription,var imageThumbnail : String,
                       var articleDescription : articleDescription,var updatedAt : String,var _id : String,var isSlideNew : Boolean,
                       var categoryInfo : ArrayList<cateInfo>)
data class cateInfo(var _id : String,var categoryId: cateId)
data class cateId(var categoryName:cateNameInfo)
data class cateNameInfo(var en:String,var th:String)
data class articlename(var en: String,var th :String)
data class articleDescription(var en:String,var th:String)
data class shortDescription(var en:String,var th : String)
data class ImageArticle(var path :String)
data class ImageArray(var url : String,var id : String)
class FragmentHome : androidx.fragment.app.Fragment() {
    var sp: SharedPreferences? = null
    private var mPager: androidx.viewpager.widget.ViewPager? = null
    private var mAPIServicePartner: ApiServicePartner? = null
    private var mAPIService: ApiServiceContent? = null
    private var NUM_PAGES = 0
    lateinit var layoutManager: LinearLayoutManager
    private var CategoriesId: String?=null
    private var idTopic1 : String?=null
    private var bigImageCount = 0
    private var sharedPreferences : SharedPreferences?=null
    private var count =1
    private var currentPage = 1
    private var init = true
    private var ArticleSize = 3000
    private var ImageStringUrl = ArrayList<String>()
    private var _NameImage  = ArrayList<String>()
    private var recyclerArticle = ArrayList<ArticleData>()
    private var CurrentrecyclerArticle = ArrayList<ArticleData>()
    private var mCurrentPage = 1
    private var noSlide = false
    private var noBigImage = false
    private var noNews = false
    private val mItemPerRow = 10
    private var _idImage = ArrayList<String>()
    val lists: ArrayList<ImageArray> = ArrayList()
    var page = 0
    val limit = 2

    lateinit var adapter: ArticleAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        return inflater.inflate(R.layout.homefragment, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getPartner()

       // getCategories()
        layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity,2)
        list_recycler_view_article.layoutManager =layoutManager
        search.setOnClickListener {
           startActivity(Intent(requireContext(),SearchNews::class.java))
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        d("T","YEs")
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
       Topic1.setOnClickListener {
           val intent = Intent(activity, ArticleInfo::class.java)
           intent.putExtra("ID",idTopic1)
           activity?.startActivity(intent)
       }
        li1.setOnClickListener {
           _NameImage  = ArrayList<String>()
           CurrentrecyclerArticle = ArrayList<ArticleData>()
            _idImage = ArrayList<String>()
            ImageStringUrl = ArrayList<String>()
            init = false
            mCurrentPage = 1
            bigImageCount=0
            count =1
            if (noBigImage && noNews && noSlide){
                showNoArticles()
            }
            li1.isEnabled = false
            li1.isClickable = false
            recyclerViewNews.visibility = View.GONE
            slide.visibility = View.GONE
            Topic1.visibility = View.GONE
         /* CurrentrecyclerArticle.clear()
            _idImage.clear()
            ImageStringUrl.clear()
            _NameImage.clear()
*/
            sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            getCategories(sharedPreferences!!.getString("partnerId","-"),"Top")
        }
        li2.setOnClickListener {

           _NameImage  = ArrayList<String>()
           CurrentrecyclerArticle = ArrayList<ArticleData>()
            _idImage = ArrayList<String>()
             ImageStringUrl = ArrayList<String>()
            init = false
            mCurrentPage = 1
            bigImageCount=0
            count =1
            li2.isEnabled = false
            li2.isClickable = false
            recyclerViewNews.visibility = View.GONE
            slide.visibility = View.GONE
            Topic1.visibility = View.GONE
           /* CurrentrecyclerArticle.clear()
            _idImage.clear()
            ImageStringUrl.clear()
            _NameImage.clear()*/
            sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            getCategories(sharedPreferences!!.getString("partnerId","-"),"ข่าวสาร")

        }
        li3.setOnClickListener {

             _NameImage  = ArrayList<String>()
             CurrentrecyclerArticle = ArrayList<ArticleData>()
             _idImage = ArrayList<String>()
             ImageStringUrl = ArrayList<String>()
            init = false
            count =1
            mCurrentPage = 1

            bigImageCount=0
            li3.isEnabled = false
            li3.isClickable = false
            recyclerViewNews.visibility = View.GONE
            slide.visibility = View.GONE
            Topic1.visibility = View.GONE
         /* CurrentrecyclerArticle.clear()
            _idImage.clear()
            ImageStringUrl.clear()
            _NameImage.clear()
*/
            sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            getCategories(sharedPreferences!!.getString("partnerId","-"),"กิจกรรม")

        }
    }

    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        super.onResume()
    }
    private fun init(test: Array<Any>,test2 : Array<Any>,test3 : Array<Any> ) {
        d("BUG",test2.size.toString())
        if (test.size == 0 || test2.size == 0 || test3.size ==0){
            indicator.visibility = View.GONE
        }else{
            indicator.visibility = View.VISIBLE
        }

        mPager = view?.findViewById(R.id.pager) as androidx.viewpager.widget.ViewPager
        mPager?.setAdapter(SlidingImage_Adapter(requireContext(),test,test2,test3))
        val indicator = view?.findViewById(R.id.indicator) as CirclePageIndicator
        indicator.setViewPager(mPager)
        val density = resources.displayMetrics.density
        //Set circle indicator radius
        indicator.radius = 4 * density
        NUM_PAGES = test.size
        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
            }
            mPager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        mPager!!.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                d("Sliding",currentPage.toString())
            }
        })
        indicator.setOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }
            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {
            }
            override fun onPageScrollStateChanged(pos: Int) {
            }
        })
        mPager!!.setOnClickListener {
            d("Sliding",currentPage.toString())
        }
    }
    private fun getCategories(partnerId:String,cateName : String){
        mAPIService = ApiUtilsContent.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setCancelable(false)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()
        mAPIService!!.getCategories(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,cateName).enqueue(object : Callback<CateModel> {
            override fun onResponse(call: Call<CateModel>, response: Response<CateModel>) {
            try {
                CategoriesId = response.body()!!.resultData[0]._id
                if(CategoriesId != null){
                    getArticle(CategoriesId);
                   getSlide(CategoriesId!!)
                    getBigImage(CategoriesId!!)
                }
            }catch (e : Exception){
                }
                mProgressDialog.dismiss()

            }
            override fun onFailure(call: Call<CateModel>, t: Throwable) {
                d("arm","onFailure")
                mProgressDialog.dismiss()
            }
        })
    }
    private fun getSlide(id : String){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticlesSlide(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,id!!).enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {

                    if(response.body()!!.rowCount != 0) {
                        noSlide = false
                        slide.visibility = View.VISIBLE
                        if (response.body()!! != null) {
                            upDateUiSlide(response.body()!!)
                        }
                    }else{
                        noSlide = true
                        slide.visibility = View.GONE

                    }
                }catch (e : Exception){
                    d("Exception",e.toString()+"SLIDE1")
                }


            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }

    private fun getArticle(id : String?){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
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
    private fun upDateUiSlide(data : ArticleModel) {

        data.resultData.sortBy { it.updatedAt }
        data.resultData.reverse()
        for (i in 0 until data.rowCount) {

            ImageStringUrl.add(data.resultData[i].imageThumbnail)
            _idImage.add(data.resultData[i]._id)
            _NameImage.add(data.resultData[i].articleName.th)

        }
        val objects = ImageStringUrl.toArray()
        val objects2 = _idImage.toArray()
        val objects3 = _NameImage.toArray()




        if (init) {
            try {
                init(objects, objects2, objects3);
            } catch (e: Exception) {
            }
        }else {

            mPager?.setAdapter(SlidingImage_Adapter(requireContext(), objects, objects2, objects3))
        }


    }
    private fun upDateUi(data : ArrayList<ArticleData>,Count : Int) {
       try {
           data.sortBy { it.updatedAt }
           data.reverse()
         for (i in 0 until Count) {
           recyclerArticle.add(data[i])
             d("Index",recyclerArticle.size.toString())

           }

           getArticleLimit(CategoriesId);
/*
           layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity,2)
           list_recycler_view_article.layoutManager = layoutManager
           adapter = ArticleAdapter(list_recycler_view_article,context!!,requireActivity(),CurrentrecyclerArticle)
           list_recycler_view_article.adapter = adapter


*/
           nest.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
               var totalItemCount = 0
               override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                   val TAG = "TestScroll"
                   if (scrollY > oldScrollY) {
                       d(TAG, "Scroll DOWN");
                   }
                   if (scrollY < oldScrollY) {
                       d(TAG, "Scroll UP");
                   }

                   if (scrollY == 0) {
                       d(TAG, "TOP SCROLL");
                   }

                   if (scrollY == (v!!.getChildAt(0).getMeasuredHeight() - v!!.getMeasuredHeight())) {
                       d(TAG, "BOTTOM SCROLL");
                      /* if (!isRecyclerViewWaitingtoLaadData) //check for scroll down
                       {

                           if (!loadedAllItems) {
                               showUnSentData();
                           }
                       }*/

                       testLoad.visibility = View.VISIBLE
                       Handler().postDelayed({
                           getArticleLimit(CategoriesId);
                           adapter.notifyDataSetChanged();
                       },1500)
                       testLoad.visibility = View.GONE
                   }


               }

           })
         /*  list_recycler_view_article.apply {
               layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity,2)
               adapter = ArticleAdapter(context,recyclerArticle)


           }

*/
        }catch (e : Exception){}
       /* val objects = ImageStringUrl.toArray()
        val objects2 = _idImage.toArray()
        val objects3  = _NameImage.toArray()
        _idImage.toArray(urls2)
        ImageStringUrl.toArray(urls)
        try {
            init(objects,objects2,objects3);
        }catch (e : Exception){}
*/
    }


    private fun getData() {

        if (mItemPerRow * mCurrentPage >= ArticleSize) {
            Toast.makeText(requireContext(), "No Data", Toast.LENGTH_SHORT).show()
            return
        }
        for (i in 0 until mItemPerRow) {
           // getArticleLimit(CategoriesId);
          CurrentrecyclerArticle.add(recyclerArticle.get(i + (mCurrentPage - 1) * mItemPerRow))

        }

    }
    private fun getArticleLimit(id : String?){
       // d("Exception",(mItemPerRow*(mCurrentPage)).toString())
      d("Size",mCurrentPage.toString())
        if (((mItemPerRow * (mCurrentPage))) >= ArticleSize+10) {
            try {
                testLoad.visibility = View.GONE
            }catch (e : Exception){

                d("Exception",e.toString()+"LIMIT")
            }
            return
           // Toast.makeText(requireContext(), "No Data", Toast.LENGTH_SHORT).show()
        }
        else {
            mAPIService = ApiUtilsContent.apiService
            sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val partnerId = sharedPreferences!!.getString("partnerId","-")
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            d("Exception",(mCurrentPage).toString())
            d("Off",((mItemPerRow*mCurrentPage).toString()))

            mAPIService!!.getArticlesLimit(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r), partnerId, id!!, (mItemPerRow*(mCurrentPage-1))+1, 10 ,false,"updatedAt").enqueue(object : Callback<ArticleModel> {

                override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                    //  d("Article",response.body()!!.developerMessage)
                    try {
                        if (response.body()!!.rowCount != 0) {
                            recyclerViewNews.visibility = View.VISIBLE
                            noNews = false
                            //   d("Size",ArticleSize.toString())
                            ArticleSize = response.body()!!.rowCount

                            if (response.body()!!.resultData[0] != null) {
                                for (i in 0..response.body()!!.resultData.size - 1) {
                                    // d("Size",mCurrentPage.toString())
                                    CurrentrecyclerArticle.add(response.body()!!.resultData[i])
                                }
                            }
                            if (count == 1) {
                                layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 2)
                                list_recycler_view_article.layoutManager = layoutManager
                                adapter = ArticleAdapter(list_recycler_view_article, context!!, requireActivity(), CurrentrecyclerArticle)
                                list_recycler_view_article.adapter = adapter
                                count++
                            } else {
                                adapter.notifyDataSetChanged()
                            }

                            mCurrentPage += 1
                            testLoad.visibility = View.GONE
                        }
                        else{
                            recyclerViewNews.visibility = View.GONE
                            noNews = true
                            d("TEE","YES")
                        }
                       // CurrentrecyclerArticle.sortBy { it.updatedAt }
                      //  CurrentrecyclerArticle.reverse()
                    } catch (e: Exception) {
                        d("Exception",e.toString()+"LIMIT2")
                       // d("Exception",e.toString())
                    }
                    try {
                        li1.isEnabled = true
                        li1.isClickable = true
                        li2.isEnabled = true
                        li2.isClickable = true
                        li3.isEnabled = true
                        li3.isClickable = true
                    }catch (e : Exception){

                    }

                }

                override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                    d("arm", "onFailure")
                }
            })
        }
    }
    private fun getBigImage(id : String){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")

        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        d("Off",((mItemPerRow*mCurrentPage).toString()))
        mAPIService!!.getArticlesBigImage(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r), partnerId, id!!,1,false,"updatedAt" ).enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {
                    if(response.body()!!.rowCount != 0) {
                        Topic1.visibility = View.VISIBLE
                        noBigImage=false
                        var responseData = response.body()!!.resultData[0]
                        d("Test", response.body()!!.resultData[0].imageThumbnail)
                        var date = responseData.updatedAt.substring(0..10)
                        var dateOutput = Profilewithpicture.ConvertDate.ChangeFormatDate(date.substring(0..3), date.substring(5..6), date.substring(8..9))
                        newsTopic1.setText(responseData.articleName.th)
                        textdate1.setText(dateOutput)

                        //category1.setText(responseData.categoryId.categoryName.th)
                        for (i in 0 until response.body()!!.resultData[0].categoryInfo.size) {
                            d("TestArray", i.toString())
                            if (response.body()!!.resultData[0].categoryInfo[i].categoryId.categoryName.th != "Top") {
                                category1.setText(response.body()!!.resultData[0].categoryInfo[i].categoryId.categoryName.th)
                            }
                        }

                        Glide.with(requireContext())
                                .load(response.body()!!.resultData[0].imageThumbnail)
                                .into(newsImage1)

                        newsImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                        idTopic1 = responseData._id
                    }else{
                        Topic1.visibility = View.GONE
                        noBigImage = true
                    }

                } catch (e: Exception) {

                    d("Exception",e.toString())
                }


            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("Exception", t.toString())
            }
        })
    }
    private fun getPartner(){
        mAPIServicePartner = ApiUtilsPartner.apiServicePartner
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        d("Response",sharedPreferences!!.getString("partnerId","-"))
        mAPIServicePartner!!.getPartnerId(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),"111400").enqueue(object : Callback<PartnerModel> {
            override fun onResponse(call: Call<PartnerModel>, response: Response<PartnerModel>) {
                try {
                    //d("Response",response.body()!!.resultData._id)
                    sharedPreferences!!.edit { putString("partnerId", response.body()!!.resultData._id) }
                    d("Response",sharedPreferences!!.getString("partnerId","-"))
                    getCategories(response.body()!!.resultData._id,"Top")


                }catch (e : Exception){
                    d("Response",e.toString())
                }
            }
            override fun onFailure(call: Call<PartnerModel>, t: Throwable) {
                d("Response",t.toString())
            }
        })
    }
    private fun refreshArticles(){

    }
    private fun showNoArticles(){

    }
}

