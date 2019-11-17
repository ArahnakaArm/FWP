package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import kotlinx.android.synthetic.main.bookmarkfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

data class Bookmark(val title : String,val date : String)
class FragmentBookmark : androidx.fragment.app.Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private var sp : SharedPreferences?=null
    private var token : String?=null
    private val adaptertest: CustomAdapter? = null
    var usr : Userstate = Userstate()
    var mAPIService : ApiService?=null
    var array_sort = java.util.ArrayList<BookMarkModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bookmarkfragment,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        getBookMarkList()





        ///// Searching /////
     /*   etsearch = view.findViewById(R.id.searchbookmark) as EditText
        array_sort = java.util.ArrayList<BookMarkModel>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in bookmarks.indices) {
                    if (textlength <= bookmarks[i].getNames().length) {
                        Log.d("ertyyy", bookmarks[i].getNames().toLowerCase().trim())
                        if (bookmarks[i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(bookmarks[i])
                        }
                    }
                }
                list_recycler_view.apply {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                    adapter = BookMarkAdapter(context,array_sort)

                }

            }
        })
*/

///// Searching /////






        backprees.setOnClickListener {

            fragmentManager?.popBackStack()

        }
        //d("armtesttext",Bok.toString())

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
        fun newInstance(): FragmentBookmark = FragmentBookmark()
    }
    private fun populateList(): java.util.ArrayList<BookMarkModel> {

        val list = java.util.ArrayList<BookMarkModel>()

        for (i in 0..7) {

            list.add(BookMarkModel("","",""))
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
    private fun getBookMarkList(){
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
        mAPIService!!.getFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<Favorite> {

            override fun onResponse(call: Call<Favorite>, response: Response<Favorite>) {

                try {
                   // d("Video",response.body()!!.resultData[0]!!.videoName.th.toString())
                    showData(response.body()!!.resultData)
                }catch (e : Exception){

                }


                mProgressDialog.dismiss();

///// Searching /////




            }
///// Searching /////

            override fun onFailure(call: Call<Favorite>, t: Throwable) {
                d("arm","onFailure")
            }

        })
    }
    private fun showData(data : ArrayList<resultDataFav>){
        list_recycler_view.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            adapter = BookMarkAdapter(context,data)
        }

    }

}