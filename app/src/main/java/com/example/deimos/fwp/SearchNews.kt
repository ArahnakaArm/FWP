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
import kotlinx.android.synthetic.main.bookmarkfragment.*
import java.util.zip.Inflater
class SearchNews : androidx.fragment.app.Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private val adaptertest: CustomAdapter? = null
    var usr : Userstate = Userstate()
    var array_sort = java.util.ArrayList<NewsModel>()

    var news = ArrayList<NewsModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance= true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        return inflater.inflate(R.layout.searchnews,container,false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        news.add(NewsModel("dawr","14/12/61"))
        news.add(NewsModel("hgjg","14/12/61"))
        news.add(NewsModel("agewwcxvs","14/12/61"))
        news.add(NewsModel("agrtrcs","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))
        news.add(NewsModel("agsadths","14/12/61"))



        list_recycler_view.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            adapter = NewsAdapter(context,news)
        }

        ///// Searching /////
        etsearch = view.findViewById(R.id.searchbookmark) as EditText
        array_sort = java.util.ArrayList<NewsModel>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in news.indices) {
                    if (textlength <= news[i].getNames().length) {
                        Log.d("ertyyy", news[i].getNames().toLowerCase().trim())
                        if (news[i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(news[i])
                        }
                    }
                }
                list_recycler_view.apply {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                    adapter = NewsAdapter(context,array_sort)

                }

            }
        })
///// Searching /////

        backprees.setOnClickListener {
            fragmentManager?.popBackStack()
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
    private fun populateList(): java.util.ArrayList<NewsModel> {

        val list = java.util.ArrayList<NewsModel>()

        for (i in 0..7) {

            list.add(NewsModel("",""))
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
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        super.onResume()
    }

}