package com.example.deimos.fwp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import kotlinx.android.synthetic.main.bookmarkfragment.*
import kotlinx.android.synthetic.main.complainlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class ComplianModel(var resultCode : String,var developerMessage: String,var resultData : ArrayList<CompliansData>,var rowCount : Int)
data class CompliansData(var map : complianMap,var _id : String,var status : String, var subject : String ,var complainType : String,
                         var complainDesc : String,var updatedAt : String)
data class complianMap(var lat :Number,var long : Number)
class ComplainList : androidx.fragment.app.Fragment() {
    var sp: SharedPreferences? = null
    var token : String?=null
    var user_ID : String?=null
    var sp2: SharedPreferences? = null
    var mAPIService: ApiService? = null
    private var etsearch: EditText? = null
    internal var textlength = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complainlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAPIService = ApiUtils.apiService
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        user_ID = sp!!.getString("user_id","-")
        var array_sort = java.util.ArrayList<ComplainModel>()
        var mockcomplains = ArrayList<ComplainModel>()
        mockcomplains.add(ComplainModel("Complain 1", "17/8/2019", "Complete"))
        mockcomplains.add(ComplainModel("Complain 2", "1/8/2019", "In Progress"))
        mockcomplains.add(ComplainModel("Complain 3", "27/8/2019", "Complete"))
        mockcomplains.add(ComplainModel("Complain 4", "7/8/2019", "In Progress"))
        Log.d("Complain", token)
        val partnerId = "5dbfe99c776a690010deb237"
        //Log.d("arm", usr.state.toString())
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()

        mAPIService!!.getComplianList(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,user_ID!!).enqueue(object : Callback <ComplianModel> {

            override fun onResponse(call: Call <ComplianModel>, response: Response <ComplianModel>) {
              //  Log.d("Complain", response.body()!!.resultData[0].toString())
               // Log.d("Complain", response.body()!!)
                //showData(response.body()!!.resultData)

              try {
                    list_recycler_view_complain.apply {
                        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                        adapter = ComplainAdapter(context,response.body()!!.resultData)

                    }

                }catch (e:Exception){

                    d("Ex",e.toString())
                }
///// Searching /////




            }
///// Searching /////

            override fun onFailure(call: Call <ComplianModel>, t: Throwable) {
                Log.d("Complain", t.toString())
            }

        })
















        ///// Searching /////
    /*    etsearch = view.findViewById(R.id.searchcomplain) as EditText
        array_sort = java.util.ArrayList<ComplainModel>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in mockcomplains!!.indices) {
                    if (textlength <= mockcomplains!![i].getNames().length) {
                        Log.d("ertyyy", mockcomplains!![i].getNames().toLowerCase().trim())
                        if (mockcomplains!![i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(mockcomplains!![i])
                        }
                    }
                }
                list_recycler_view_complain.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = ComplainAdapter(context,array_sort)

                }

            }
        })
*/

///// Searching /////















        backpreescomplain.setOnClickListener {
           activity!!.onBackPressed()
        }



    }

    private fun  showData(complians :ArrayList<CompliansData> ){




    }


    private fun populateList(): java.util.ArrayList<ComplainModel> {

        val list = java.util.ArrayList<ComplainModel>()

        for (i in 0..7) {

            list.add(ComplainModel("","",""))
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

}