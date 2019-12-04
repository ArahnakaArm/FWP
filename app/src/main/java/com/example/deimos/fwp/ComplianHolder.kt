package com.example.deimos.fwp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log.d
import kotlinx.android.synthetic.main.complianviewpager.*


class ComplianHolder : androidx.fragment.app.Fragment(){

    var sp: SharedPreferences? = null

    var token : String?=null
    var YOURLISTVIEW : CustomScrollView?=null
    var current : Int?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view :View = inflater.inflate(R.layout.complianviewpager,container,false)
        //YOURLISTVIEW = view.findViewById(R.id.myScroll) as CustomScrollView
        return view

    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragement(Tab1Complian(),"one")
        adapter.addFragement(Tab2Complian(),"two")
        adapter.addFragement(Tab3Complian(),"three")
        viewpager.adapter = adapter
        viewpager.beginFakeDrag();
        viewpager?.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
               // d("Tab",position.toString())
            }
            override fun onPageSelected(position: Int) {

                if (position == 0){
                    d("Tab",position.toString())

                    step1points.visibility = View.INVISIBLE
                    step2circle.visibility = View.INVISIBLE

                    backcomplian.visibility = View.INVISIBLE
                    next2.visibility = View.INVISIBLE


                }else if(position == 1){
                    d("Tab",position.toString())
                    step1points.visibility = View.VISIBLE
                    step2circle.visibility = View.VISIBLE

                    step2points.visibility = View.INVISIBLE
                    step3circle.visibility = View.INVISIBLE
                    backcomplian.visibility = View.VISIBLE
                    next2.visibility = View.INVISIBLE

                }else if(position ==2){
                    d("Tab",position.toString())
                    step2points.visibility = View.VISIBLE
                    step3circle.visibility = View.VISIBLE
                    backcomplian.visibility = View.VISIBLE

                    next.visibility = View.INVISIBLE
                    next2.visibility = View.VISIBLE

                }
            }

        })

        next.setOnClickListener {
            d("NEXT","Yes")
          d("Detail",sp!!.getString("Subject","-"))
       d("Detail",sp!!.getString("Description","-"))
            d("Detail",sp!!.getString("Type","-"))
            d("Detail",sp!!.getString("LAT","-"))
            d("Detail",sp!!.getString("LONG","-"))
            viewpager.setCurrentItem(getItem(+1), true)


        }

        next2.setOnClickListener {







        }

        backcomplian.setOnClickListener {
            d("NEXT","Yes")
            viewpager.setCurrentItem(getItem(-1), true)
        }

    }

    class ViewPagerAdapter(fragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(fragmentManager) {
        private val fragmentList : MutableList<androidx.fragment.app.Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()

        override fun getItem(p0: Int): androidx.fragment.app.Fragment {

            return fragmentList[p0]

        }

        override fun getCount(): Int {
            return  fragmentList.size
        }
        fun addFragement(fragment : androidx.fragment.app.Fragment, title : String){
            fragmentList.add(fragment)
            titleList.add(title)
        }


    }

    private fun getItem(i: Int): Int {
        return viewpager.getCurrentItem() + i
    }

    public fun getListView(): CustomScrollView {
        return YOURLISTVIEW!!
    }
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()

    }
}