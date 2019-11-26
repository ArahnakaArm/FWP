package com.example.deimos.fwp

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

data class CateModel(var resultCode : String ,var developerMessage : String,var resultData : ArrayList<cateresult>)
data class cateresult(var categoryName : categoryNameArticle,var _id : String)
data class categoryNameArticle(var th : String,var en : String)
data class ArticleModel(var resultCode : String,var developerMessage : String,var resultData : ArrayList<ArticleData>, var rowCount : Int)
data class ArticleData(var articleName : articlename,var shortDescription : shortDescription,var imageThumbnail : ImageArticle,
                       var articleDescription : articleDescription,var updatedAt : String,var _id : String,var isSlideNew : Boolean,
                       var categoryId : cateInfo)
data class cateInfo(var categoryName : cateNameInfo)
data class cateNameInfo(var th : String , var en: String)
data class articlename(var en: String,var th :String)
data class articleDescription(var en:String,var th:String)
data class shortDescription(var en:String,var th : String)
data class ImageArticle(var path :String)
data class ImageArray(var url : String,var id : String)
class FragmentHome : androidx.fragment.app.Fragment() {
    var sp: SharedPreferences? = null
    private var mPager: androidx.viewpager.widget.ViewPager? = null
    var mAPIService: ApiService? = null
    private var NUM_PAGES = 0
    lateinit var layoutManager: LinearLayoutManager
    private var URLImage : String ="http://206.189.41.105:1210/"
    private var CategoriesId: String?=null
    private var idTopic1 : String?=null
    private var bigImageCount = 0
    private var idTopic2 : String?=null
    private var Page = 0
    private var count =1
    private var currentPage = 1
    private var ArticleSize = 3000
    private var idTopic3 : String?=null
    private var ImageStringUrl = ArrayList<String>()
    private var _NameImage  = ArrayList<String>()
    private var recyclerArticle = ArrayList<ArticleData>()
    private var CurrentrecyclerArticle = ArrayList<ArticleData>()
    private var mCurrentrecyclerArticle = ArrayList<ArticleData>()
    private var mCurrentPage = 1
    private val mItemPerRow = 4
    private var _idImage = ArrayList<String>()
    val lists: ArrayList<ImageArray> = ArrayList()
    var page = 0
    var isLoading = false
    val limit = 2
    lateinit var adapter: ArticleAdapter


    private val urls = arrayOf("https://demonuts.com/Demonuts/SampleImages/W-03.JPG","asdsdsads","https://demonuts.com/Demonuts/SampleImages/W-03.JPG","asdsdsads")
    private val urls2 = arrayOf("https://demonuts.com/Demonuts/SampleImages/W-03.JPG","asdsdsads")
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
        getCategories()
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
            Toast.makeText(context,"Fetured",Toast.LENGTH_SHORT).show()
        }
        li2.setOnClickListener {
            Toast.makeText(context,"Article",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        super.onResume()
    }
    private fun init(test: Array<Any>,test2 : Array<Any>,test3 : Array<Any> ) {
        mPager = view?.findViewById(R.id.pager) as androidx.viewpager.widget.ViewPager
        mPager?.setAdapter(SlidingImage_Adapter(requireContext(),test,test2,test3))
        val indicator = view?.findViewById(R.id.indicator) as CirclePageIndicator
        indicator.setViewPager(mPager)
        val density = resources.displayMetrics.density
        //Set circle indicator radius
        indicator.radius = 4 * density
        NUM_PAGES = urls.size
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
    private fun getCategories(){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getCategories(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<CateModel> {
            override fun onResponse(call: Call<CateModel>, response: Response<CateModel>) {
            try {
                CategoriesId = response.body()!!.resultData[0]._id
                if(CategoriesId != null){
                    getArticle(CategoriesId)
                    getSlide(CategoriesId!!)
                    getBigImage(CategoriesId!!)
                }
            }catch (e : Exception){
                }
            }
            override fun onFailure(call: Call<CateModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
    }
    private fun getSlide(id : String){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getArticlesSlide(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,id!!).enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {
                    if(response.body()!! != null){
                        upDateUiSlide(response.body()!!)
                    }
                }catch (e : Exception){}


            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm","onFailure")
            }
        })
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
    private fun upDateUiSlide(data : ArticleModel){
        data.resultData.sortBy { it.updatedAt }
        data.resultData.reverse()
        for (i in 0 until data.rowCount) {

                ImageStringUrl.add(URLImage+data.resultData[i].imageThumbnail.path)
                _idImage.add(data.resultData[i]._id)
                _NameImage.add(data.resultData[i].articleName.th)

        }
        val objects = ImageStringUrl.toArray()
        val objects2 = _idImage.toArray()
        val objects3  = _NameImage.toArray()
        _idImage.toArray(urls2)
        ImageStringUrl.toArray(urls)
        try {
            init(objects,objects2,objects3);
        }catch (e : Exception){}
    }
    private fun upDateUi(data : ArrayList<ArticleData>,Count : Int) {
       try {
           data.sortBy { it.updatedAt }
           data.reverse()
         for (i in 0 until Count) {
           recyclerArticle.add(data[i])
             d("Index",recyclerArticle.size.toString())

           }
           var date =recyclerArticle[0].updatedAt.substring(0..10)
           var dateOutput = Profilewithpicture.ConvertDate.ChangeFormatDate(date.substring(0..3),date.substring(5..6),date.substring(8..9))
           newsTopic1.setText(recyclerArticle[0].articleName.th)
           textdate1.setText(dateOutput)
           category1.setText(recyclerArticle[0].categoryId.categoryName.th)
           Glide.with(requireContext())
                   .load(URLImage + recyclerArticle[0].imageThumbnail.path)
                   .into(newsImage1)
           newsImage1.scaleType = ImageView.ScaleType.CENTER_CROP
           idTopic1 = recyclerArticle[0]._id
           recyclerArticle.removeAt(0)

           getArticleLimit(CategoriesId);

           layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity,2)
           list_recycler_view_article.layoutManager = layoutManager
           adapter = ArticleAdapter(list_recycler_view_article,context!!,requireActivity(),CurrentrecyclerArticle)
           list_recycler_view_article.adapter = adapter



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
                       },2000)
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
        if (((mItemPerRow * (mCurrentPage))) >= ArticleSize+4) {
            testLoad.visibility = View.GONE
            return
           // Toast.makeText(requireContext(), "No Data", Toast.LENGTH_SHORT).show()
        }
        else {
            mAPIService = ApiUtils.apiService
            val partnerId = "5dbfe99c776a690010deb237"
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            d("Exception",(mCurrentPage).toString())
            d("Off",((mItemPerRow*mCurrentPage).toString()))
            mAPIService!!.getArticlesLimit(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r), partnerId, id!!, (mItemPerRow*(mCurrentPage-1))+1, 4 ,false,"updatedAt").enqueue(object : Callback<ArticleModel> {

                override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                    //  d("Article",response.body()!!.developerMessage)
                    try {

                     //   d("Size",ArticleSize.toString())
                        ArticleSize = response.body()!!.rowCount

                        if (response.body()!!.resultData[0] != null) {
                            for (i in 0..response.body()!!.resultData.size-1) {
                               // d("Size",mCurrentPage.toString())
                                CurrentrecyclerArticle.add(response.body()!!.resultData[i])
                            }
                        }
                        if(count==1) {
                            layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 2)
                            list_recycler_view_article.layoutManager = layoutManager
                            adapter = ArticleAdapter(list_recycler_view_article, context!!, requireActivity(), CurrentrecyclerArticle)
                            list_recycler_view_article.adapter = adapter
                            count++
                        }
                        else{
                            adapter.notifyDataSetChanged()
                        }

                        mCurrentPage += 1
                        testLoad.visibility = View.GONE

                       // CurrentrecyclerArticle.sortBy { it.updatedAt }
                      //  CurrentrecyclerArticle.reverse()
                    } catch (e: Exception) {
                       // d("Exception",e.toString())
                    }


                }

                override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                    d("arm", "onFailure")
                }
            })
        }
    }
    private fun getBigImage(id : String){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        d("Off",((mItemPerRow*mCurrentPage).toString()))
        mAPIService!!.getArticlesBigImage(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r), partnerId, id!!,1,false,"updatedAt" ).enqueue(object : Callback<ArticleModel> {

            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                //  d("Article",response.body()!!.developerMessage)
                try {
                    var responseData = response.body()!!.resultData[0]

                        var date =responseData.updatedAt.substring(0..10)
                        var dateOutput = Profilewithpicture.ConvertDate.ChangeFormatDate(date.substring(0..3), date.substring(5..6), date.substring(8..9))
                        newsTopic1.setText(responseData.articleName.th)
                        textdate1.setText(dateOutput)
                        category1.setText(responseData.categoryId.categoryName.th)
                        Glide.with(requireContext())
                                .load(URLImage + responseData.imageThumbnail.path)
                                .into(newsImage1)
                        newsImage1.scaleType = ImageView.ScaleType.CENTER_CROP
                        idTopic1 = responseData._id
                        CurrentrecyclerArticle.removeAt(0)
                        bigImageCount ++

                } catch (e: Exception) {
                    d("Exception",e.toString())
                }


            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                d("arm", "onFailure")
            }
        })
    }
}

