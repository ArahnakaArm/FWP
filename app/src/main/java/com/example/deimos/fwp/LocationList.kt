package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import kotlinx.android.synthetic.main.bookmarkfragment.*

import kotlinx.android.synthetic.main.loacationlist.*

import java.util.ArrayList

class LocationList : Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private var adapter2: LocationChildAdapter? = null
    private val companyViewHolder: CompanyViewHolder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.loacationlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var array_sort = ArrayList<LocationChildModel>()

        recyclerView.layoutManager = LinearLayoutManager(context)

        val companies = ArrayList<Company>()

        val googleProduct2 = ArrayList<LocationChildModel>()
        googleProduct2.add(LocationChildModel("Location 1","Info(Location1)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 2","Info(Location2)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 3","Info(Location3)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 4","Info(Location4)","0978512369","Location1@Bankok.com"))
        googleProduct2.add(LocationChildModel("Location 5","Info(Location5)","0978512369","Location1@Bankok.com"))



        val googleProduct = ArrayList<Product>()
        googleProduct.add(Product("Location 1","Info(Location1)","0978512369","Location1@Bankok.com"))
        googleProduct.add(Product("Location 2","Info(Location2)","0978512369","Location2@Bankok.com"))
        googleProduct.add(Product("Location 3","Info(Location3)","0978512369","Location3@Bankok.com"))
        googleProduct.add(Product("Location 4","Info(Location4)","0978512369","Location4@Bankok.com"))
        googleProduct.add(Product("Location 5","Info(Location5)","0978512369","Location5@Bankok.com"))

        val google = Company("" +
                "Bankok And Metropolitan Region", googleProduct)
        companies.add(google)

        val microsoftProduct = ArrayList<Product>()
        microsoftProduct.add(Product("Location 1","Info(Location1)","0978512369","Location1@Central.com"))
        microsoftProduct.add(Product("Location 2","Info(Location2)","0978512369","Location2@Central.com"))
        microsoftProduct.add(Product("Location 3","Info(Location3)","0978512369","Location3@Central.com"))
        microsoftProduct.add(Product("Location 4","Info(Location4)","0978512369","Location4@Central.com"))

        val microsoft = Company("Central Region", microsoftProduct)
        companies.add(microsoft)

        var adapter = ProductAdapter(companies)
        //Toast.makeText(context, Integer.toString(adapter.itemCount), Toast.LENGTH_SHORT).show()


       for (i in adapter.groups.size - 1 downTo 0) {
            adapter.toggleGroup(i)
        }
        recyclerView.adapter=adapter

       // recyclerView.adapter = adapter



        map.setOnClickListener {
            replaceFragment(MapsActivity())
        }


        ///// Searching /////
        etsearch = view.findViewById(R.id.searchlocation) as EditText
        array_sort = ArrayList<LocationChildModel>()
        array_sort = populateList()
        etsearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = etsearch!!.text.length
                array_sort.clear()
                for (i in googleProduct2!!.indices) {
                    if (textlength <= googleProduct2!![i].getNames().length) {
                        Log.d("ertyyy", googleProduct2!![i].getNames().toLowerCase().trim())
                        if (googleProduct2!![i].getNames().toLowerCase().trim().contains(
                                        etsearch!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort.add(googleProduct2[i])
                        }
                        var adapter2 = LocationChildAdapter(view.context,array_sort)
                        recyclerView.adapter=adapter2
                    }
                    else{

                    }

                    if (textlength.equals(0)) {
                        var adapter = ProductAdapter(companies)
                        for (i in adapter.groups.size - 1 downTo 0) {
                            adapter.toggleGroup(i)
                        }
                        recyclerView.adapter=adapter
                        d("zerodetect", "!!")
                    }
                }


            }
        })

///// Searching /////

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }
    private fun populateList(): java.util.ArrayList<LocationChildModel> {

        val list = java.util.ArrayList<LocationChildModel>()

        for (i in 0..7) {

            list.add(LocationChildModel("","","",""))
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
