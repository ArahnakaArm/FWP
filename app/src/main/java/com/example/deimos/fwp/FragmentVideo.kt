package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.favorite.*
import kotlinx.android.synthetic.main.videofragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

data class addrees(val street:String,val suite:String)
data class Video(val name : String,
                 val username : String
                 ,val address:addrees)

data class VideosModel(var resultCode : String,var developerMessage : String , var resultData : ArrayList<resultData3?>,var rowCount : Int)
data class resultData3(var videoName : videoName,var videoDescription : videoDescription,var keywords : ArrayList<String>,var isPublish : Boolean
,var _id : String,var videoLink : String, var categoryType : String,var createdAt : String,var updatedAt : String,var categoryInfo : ArrayList<categoryInfoVideo>
,var image : String)
data class categoryInfoVideo(var categoryId : categoryid)
data class VideoImage(var path : String)
data class categoryid(var categoryName :categoryName)
data class categoryName(var en :String,var th : String)
data class  videoDescription(var en : String,var th : String)
data class  videoName(var en : String,var th : String)

class FragmentVideo : androidx.fragment.app.Fragment(),ILoadMore {
    lateinit var adapter: VideoAdapter
    private var mCurrentPage = 1
    private val mItemPerRow = 10
    private var sharedPreferences:SharedPreferences?=null
    private var searchText = ""
    private var searchState = false
    lateinit var layoutManager: LinearLayoutManager
    private var videos = ArrayList<resultData3?>()
    var mAPIService: ApiServiceContent? = null


    override fun onLoadMore() {
        try {
            d("Detect", "YEsss")
            videos.add(null)
            adapter.notifyItemInserted(videos.size - 1)
            Handler().postDelayed({
                try {
                    videos.removeAt(videos.size - 1)
                    // adapter.notifyItemRemoved(videos.size)
                    LoadMoreWithSearch(searchText)
                }catch (e:Exception){

                }

            }, 2000)
        }catch (e:Exception){

        }



    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable=true
        view.menu.getItem(1).isChecked=true
        return inflater.inflate(R.layout.videofragment,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setCancelable(false)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()
        mAPIService!!.getVideo(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,(mItemPerRow*(mCurrentPage-1)), 3).enqueue(object : Callback<VideosModel>{

            override fun onResponse(call: Call<VideosModel>, response: Response<VideosModel>) {

                try {
                    for (i in 0 until response.body()!!.resultData.size){
                        videos.add(response.body()!!.resultData[i])

                    }
                    d("Video", videos[0]!!._id)
                    mCurrentPage++
                    showData(videos)
                } catch (e: Exception) {
                    d("Video", e.toString())
                }


                mProgressDialog.dismiss();
            }

            override fun onFailure(call: Call<VideosModel>, t: Throwable) {
                d("arm","onFailure")
            }

        })*/

        /*searchvideo.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchvideoList(s.toString())
            }
        })*/
        searchvideo.textChanges().debounce(300, TimeUnit.MILLISECONDS).subscribe({ it ->
            try {
                d("SEARCH", it.toString())
                if(searchvideo.text.isEmpty()){
                    mCurrentPage=1
                    searchState =false
                    videos.clear()
                    searchText = ""
                    searchvideoList("")

                }
                else{
                    searchText=it.toString()
                    searchState= true
                    mCurrentPage=1
                    videos.clear()
                    searchvideoList(it.toString())
                }
               //
            }catch (e : Exception){

            }
        },{ throwable -> d("Error",throwable.message)})
    }

    companion object {
        fun newInstance(): FragmentVideo = FragmentVideo()
    }
    private fun  showData(users :ArrayList<resultData3?> ){
        try {
            users.sortBy{it?.updatedAt}
            users.reverse()



            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            list_recycler_view_video.layoutManager = layoutManager
            adapter = VideoAdapter(list_recycler_view_video,context!!,users,requireActivity())
            list_recycler_view_video.adapter = adapter
            adapter.setLoadMore(this)


          /*  list_recycler_view_video.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
               adapter = VideoAdapter(list_recycler_view_video,context,users,requireActivity())

            }*/

       }catch (e:Exception){


        }



    }

    override fun onDetach() {
        super.onDetach()

        d("arm","death")

    }

    override fun onResume() {
        d("Gumm","Yes")
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable=true
        view.menu.getItem(1).isChecked=true
        super.onResume()
    }
    private fun searchvideoList(text : String){
        mAPIService = ApiUtilsContent.apiService
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()

        mAPIService!!.getSearchVideo(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,text,(mItemPerRow*(mCurrentPage-1)), 10,"updatedAt").enqueue(object : Callback<VideosModel?>{

            override fun onResponse(call: Call<VideosModel?>, response: Response<VideosModel?>) {

                    try {
                        for (i in 0 until response.body()!!.resultData.size) {
                            videos.add(response.body()!!.resultData[i])

                        }
                        mCurrentPage++
                        showData(videos)

                    } catch (e: Exception) {

                    }


            }
            override fun onFailure(call: Call<VideosModel?>, t: Throwable) {
                d("arm",t.toString())

            }

        })
    }

  /*  private fun LoadMore(){
        mAPIService = ApiUtils.apiService
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getVideo(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,(mItemPerRow*(mCurrentPage-1)), 10).enqueue(object : Callback<VideosModel>{

            override fun onResponse(call: Call<VideosModel>, response: Response<VideosModel>) {

                    try {
                        for (i in 0 until response.body()!!.resultData.size) {
                            videos.add(response.body()!!.resultData[i])

                        }
                        adapter.notifyDataSetChanged()
                        adapter.setLoaded()
                        d("Video", videos[0]!!._id)
                        mCurrentPage++
                    } catch (e: Exception) {
                        d("Video2", e.toString())
                    }



            }

            override fun onFailure(call: Call<VideosModel>, t: Throwable) {
                d("arm",t.toString())
            }

        })

    }*/
    private fun LoadMoreWithSearch(text : String){
        try {
            mAPIService = ApiUtilsContent.apiService
            sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val partnerId = sharedPreferences!!.getString("partnerId", "-")
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            mAPIService!!.getSearchVideo(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r), partnerId, text, (mItemPerRow * (mCurrentPage - 1)), 10, "updatedAt").enqueue(object : Callback<VideosModel?> {

                override fun onResponse(call: Call<VideosModel?>, response: Response<VideosModel?>) {

                        try {
                            for (i in 0 until response.body()!!.resultData.size) {
                                videos.add(response.body()!!.resultData[i])

                            }
                            adapter.notifyDataSetChanged()
                            adapter.setLoaded()
                            d("Video", videos[0]!!._id)
                            mCurrentPage++
                        } catch (e: Exception) {
                            d("Video", e.toString())
                        }



                }
                override fun onFailure(call: Call<VideosModel?>, t: Throwable) {
                    d("arm", t.toString())
                }

            })
        }catch (e : Exception){

        }

    }

}