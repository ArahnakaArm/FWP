package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import android.widget.ListAdapter
import com.example.deimos.fwp.R.id.recyclerView
import kotlinx.android.synthetic.main.bookmarkfragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

data class Video2(val title : String,val date : String)
data class addrees(val street:String,val suite:String)
data class Video(val name : String,
                 val username : String
                 ,val address:addrees)
class FragmentVideo : Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private var adapter: SearchAdapter? = null

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
        view.menu.getItem(0).isCheckable=true
        view.menu.getItem(0).isChecked=true
        return inflater.inflate(R.layout.videofragment,container,false)


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usr.setState(true)

        Log.d("arm", usr.state.toString())

        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(ApiService::class.java)

        api.fecthAllUsers().enqueue(object : Callback<ArrayList<SearchModel>>{

            override fun onResponse(call: Call<ArrayList<SearchModel>>, response: Response<ArrayList<SearchModel>>) {

                showData(response.body()!!)

///// Searching /////
                etsearch = view.findViewById(R.id.searchvideo) as EditText
                array_sort = java.util.ArrayList<SearchModel>()
                array_sort = populateList()
                etsearch!!.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        textlength = etsearch!!.text.length
                        array_sort.clear()
                        for (i in response.body()!!.indices) {
                            if (textlength <= response.body()!![i].getNames().length) {
                                Log.d("ertyyy", response.body()!![i].getNames().toLowerCase().trim())
                                if (response.body()!![i].getNames().toLowerCase().trim().contains(
                                                etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                                ) {
                                   array_sort.add(response.body()!![i])
                                }
                            }
                        }
                        list_recycler_view.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = SearchAdapter(context,array_sort)

                        }

                    }
                })

            }
///// Searching /////




            override fun onFailure(call: Call<ArrayList<SearchModel>>, t: Throwable) {
                d("arm","onFailure")
            }

        })


    }

    companion object {
        fun newInstance(): FragmentVideo = FragmentVideo()
    }
    private fun  showData(users : ArrayList<SearchModel>){
        try {
            list_recycler_view.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = SearchAdapter(context,users)

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
            recyclerView: RecyclerView,
            private val clickListener: ClickListener?
    ) : RecyclerView.OnItemTouchListener {

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

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)


    }
    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(0).isCheckable=true
        view.menu.getItem(0).isChecked=true
        super.onResume()
    }



}