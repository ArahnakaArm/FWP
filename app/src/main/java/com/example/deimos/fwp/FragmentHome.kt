package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.main.*
import android.support.v4.view.ViewPager
import com.viewpagerindicator.CirclePageIndicator
import java.util.*


class FragmentHome : Fragment() {
    var sp: SharedPreferences? = null
    private var mPager: ViewPager? = null
    private var currentPage = 0
    private var NUM_PAGES = 0

    private val urls = arrayOf("https://demonuts.com/Demonuts/SampleImages/W-03.JPG",
            "https://demonuts.com/Demonuts/SampleImages/W-08.JPG",
            "https://demonuts.com/Demonuts/SampleImages/W-10.JPG",
            "https://demonuts.com/Demonuts/SampleImages/W-13.JPG")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        return inflater.inflate(R.layout.homefragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init();


        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
       homepic1.setOnClickListener {
           val intent = Intent(activity, GalleryInfo::class.java)
           activity?.startActivity(intent)

       }
        sharebutton.setOnClickListener {
            Toast.makeText(context,"Shared",Toast.LENGTH_SHORT).show()
        }

        share.setOnClickListener {
            Toast.makeText(context,"Shared",Toast.LENGTH_SHORT).show()
        }
/*
        butgoprofile.setOnClickListener {

                val intent = Intent(activity, LogInAndProfileHolder::class.java)
                activity?.startActivity(intent)

        }


*/



        li1.setOnClickListener {
            Toast.makeText(context,"Fetured",Toast.LENGTH_SHORT).show()
        }
        li2.setOnClickListener {
            Toast.makeText(context,"Article",Toast.LENGTH_SHORT).show()
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }
    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(2).isCheckable=true
        view.menu.getItem(2).isChecked=true
        super.onResume()
    }

    private fun init() {

        mPager = view?.findViewById(R.id.pager) as ViewPager
        mPager?.setAdapter(SlidingImage_Adapter(requireContext(), urls))

        val indicator = view?.findViewById(R.id.indicator) as CirclePageIndicator

        indicator.setViewPager(mPager)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
        indicator.radius = 4 * density


        NUM_PAGES = urls.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage === NUM_PAGES) {
                currentPage = 0
            }
            mPager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentPage = position

            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {

            }

            override fun onPageScrollStateChanged(pos: Int) {

            }
        })

    }
}

