package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
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

data class VideosModel(var resultCode : String,var developerMessage : String , var resultData : ArrayList<resultData3>)
data class resultData3(var videoName : videoName,var videoDescription : videoDescription,var keywords : ArrayList<String>,var isPublish : Boolean
,var _id : String,var videoLink : String, var categoryType : String,var createdAt : String,var updatedAt : String,var categoryId : categoryid
,var image : VideoImage)
data class VideoImage(var path : String)
data class categoryid(var categoryName :categoryName)
data class categoryName(var en :String,var th : String)
data class  videoDescription(var en : String,var th : String)
data class  videoName(var en : String,var th : String)
class FragmentVideo : androidx.fragment.app.Fragment() {
    var mAPIService: ApiService? = null

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
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setCancelable(false)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()
        mAPIService!!.getVideo(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<VideosModel>{

            override fun onResponse(call: Call<VideosModel>, response: Response<VideosModel>) {

                try {
                    d("Video", response.body()!!.resultData[0]!!.videoName.th.toString())
                    showData(response.body()!!.resultData)
                } catch (e: Exception) {

                }


                mProgressDialog.dismiss();
            }

            override fun onFailure(call: Call<VideosModel>, t: Throwable) {
                d("arm","onFailure")
            }

        })

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
                searchvideoList(it.toString())
            }catch (e : Exception){

            }
        },{ throwable -> d("Error",throwable.message)})
    }

    companion object {
        fun newInstance(): FragmentVideo = FragmentVideo()
    }
    private fun  showData(users :ArrayList<resultData3> ){
        try {
            users.sortBy{it.updatedAt}
            users.reverse()
            list_recycler_view_video.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
               adapter = VideoAdapter(context,users)

            }

       }catch (e:Exception){


        }



    }

    override fun onDetach() {
        super.onDetach()

        d("arm","death")

    }


    private fun populateList(): java.util.ArrayList<SearchModel> {

        val list = java.util.ArrayList<SearchModel>()

        for (i in 0..7) {

            list.add(SearchModel("","",""))
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


    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable=true
        view.menu.getItem(1).isChecked=true
        super.onResume()
    }
    private fun searchvideoList(text : String){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getSearchVideo(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,text).enqueue(object : Callback<VideosModel>{

            override fun onResponse(call: Call<VideosModel>, response: Response<VideosModel>) {

                try {
                    showData(response.body()!!.resultData)
                } catch (e: Exception) {

                }

            }

            override fun onFailure(call: Call<VideosModel>, t: Throwable) {
                d("arm","onFailure")
            }

        })
    }



}