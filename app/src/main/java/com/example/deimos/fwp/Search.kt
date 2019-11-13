package com.example.deimos.fwp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.util.ArrayList

class Search : AppCompatActivity() {
    private  var BookmarkList = ArrayList<SearchModel>()
    private var recyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private var adapter: VideoAdapter? = null
    private var etsearch: EditText? = null
    private var moviewList: Array<String>? = null
    internal var textlength = 0
    val bookmarklist = ArrayList<SearchModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        bookmarklist.add(SearchModel("sadasd","14/5/2019","dasd@asd"))
        bookmarklist.add(SearchModel("sadasd","11/2/2019","dasd@ifjf"))
        bookmarklist.add(SearchModel("sadasd","2/5/2019","asdasdas"))
        moviewList = arrayOf(
                "Xmen",
                "Titanic",
                "Captain America",
                "Iron man",
                "Rocky",
                "Transporter",
                "Lord of the rings",
                "The jungle book",
                "Tarzan",
                "Cars",
                "Shreck"
        )
        d("hjhjh", moviewList.toString())
        recyclerView = findViewById(R.id.recycler) as androidx.recyclerview.widget.RecyclerView

        movieNamesArrayList = bookmarklist
        d("hjhjh", movieNamesArrayList.toString())
        Log.d("hjhjh", movieNamesArrayList.size.toString() + "")
       // adapter = VideoAdapter(this, movieNamesArrayList)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        etsearch = findViewById(R.id.editText) as EditText
        array_sort = ArrayList<SearchModel>()
        array_sort = populateList()

        recyclerView!!.addOnItemTouchListener(
                RecyclerTouchListener(
                        applicationContext,
                        recyclerView!!,
                        object : ClickListener {

                            override fun onClick(view: View, position: Int) {
                                Toast.makeText(this@Search, array_sort[position].getNames(), Toast.LENGTH_SHORT).show()
                            }

                            override fun onLongClick(view: View?, position: Int) {

                            }
                        })
        )


        etsearch!!.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in movieNamesArrayList.indices) {
                    if (textlength <= movieNamesArrayList[i].getNames().length) {
                        Log.d("ertyyy", movieNamesArrayList[i].getNames().toLowerCase().trim())
                        if (movieNamesArrayList[i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(movieNamesArrayList[i])
                        }
                    }
                }
             //   adapter = VideoAdapter(this@Search, array_sort)
                recyclerView!!.adapter = adapter
                recyclerView!!.layoutManager =
                        androidx.recyclerview.widget.LinearLayoutManager(applicationContext, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

            }
        })

    }

    private fun populateList(): ArrayList<SearchModel> {

        val list = ArrayList<SearchModel>()

        for (i in 0..7) {

            list.add(SearchModel("asdsad","sadasd",""))
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

    companion object {
        lateinit var movieNamesArrayList: ArrayList<SearchModel>
        lateinit var array_sort: ArrayList<SearchModel>
    }

}