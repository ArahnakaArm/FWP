package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import kotlinx.android.synthetic.main.bookmarkfragment.*
import kotlinx.android.synthetic.main.complainlist.*
import java.lang.Exception

class ComplainList : Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complainlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var array_sort = java.util.ArrayList<ComplainModel>()
        var mockcomplains = ArrayList<ComplainModel>()
        mockcomplains.add(ComplainModel("Complain 1", "17/8/2019", "Complete"))
        mockcomplains.add(ComplainModel("Complain 2", "1/8/2019", "In Progress"))
        mockcomplains.add(ComplainModel("Complain 3", "27/8/2019", "Complete"))
        mockcomplains.add(ComplainModel("Complain 4", "7/8/2019", "In Progress"))


        ///// Searching /////
        etsearch = view.findViewById(R.id.searchcomplain) as EditText
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


///// Searching /////













        try {
            list_recycler_view_complain.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = ComplainAdapter(context, mockcomplains)


            }
        }catch (e:Exception){

        }

        backpreescomplain.setOnClickListener {
            fragmentManager?.popBackStack()
        }



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

}