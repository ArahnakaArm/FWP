package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import kotlinx.android.synthetic.main.bookmarkfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Video2(val title : String,val date : String)
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
    private var etsearch: EditText? = null
    internal var textlength = 0
    private var token : String?=null
    var mAPIService: ApiService? = null
    private var adapter: VideoAdapter? = null

    var array_sort = java.util.ArrayList<SearchModel>()
    var usr : Userstate = Userstate()
   /* private  val VideoList = listOf(
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","14/12/61"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","15/8/61"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","14/9/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","1/5/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","4/7/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","8/7/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","19/7/60"),
            Video("“ธนาธร” ชี้ปัญหา “รัฐราชการรวมศูนย์”\n" +
                    "ทำประเทศไม่พัฒนา","23/7/60")
    )
*/
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
        usr.setState(true)
        val partnerId = "5dbfe99c776a690010deb237"
        Log.d("arm", usr.state.toString())
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
                 d("Video",response.body()!!.resultData[0]!!.videoName.th.toString())
                 showData(response.body()!!.resultData)
             }catch (e : Exception){

             }


                mProgressDialog.dismiss();

///// Searching /////




            }
///// Searching /////

            override fun onFailure(call: Call<VideosModel>, t: Throwable) {
                d("arm","onFailure")
            }

        })


    }

    companion object {
        fun newInstance(): FragmentVideo = FragmentVideo()
    }
    private fun  showData(users :ArrayList<resultData3> ){
        try {
            users.sortBy{it.updatedAt}
            users.reverse()
            list_recycler_view.apply {
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



}