package com.example.deimos.fwp

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.EditText
import com.example.deimos.fwp.Complian.Pager.getItem
import kotlinx.android.synthetic.main.complianviewpager.*


class Complian : AppCompatActivity(){
    var sp: SharedPreferences? = null
    var token : String?=null
    var textListener : String?=null
    var pg : androidx.viewpager.widget.ViewPager?=null
    var YOURLISTVIEW : CustomScrollView?=null
    var current : Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complianviewpager)

        pg = viewpager
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
       /* if(sp?.getBoolean("LogIn_State", false)==false) {
            val intent = Intent(this@Complian,LogInWithSkip::class.java)
            startActivity(intent)
        }else{
        }*/
        val adapter = ComplianHolder.ViewPagerAdapter(supportFragmentManager)
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
                    Log.d("Tab", position.toString())

                    step1points.visibility = View.INVISIBLE
                    step2circle.visibility = View.INVISIBLE

                    backcomplian.visibility = View.INVISIBLE
                    next2.visibility = View.INVISIBLE
                    next.visibility = View.VISIBLE

                }else if(position == 1){
                    Log.d("Tab", position.toString())
                    step1points.visibility = View.VISIBLE
                    step2circle.visibility = View.VISIBLE

                    step2points.visibility = View.INVISIBLE
                    step3circle.visibility = View.INVISIBLE
                    backcomplian.visibility = View.VISIBLE
                    next2.visibility = View.INVISIBLE
                    next.visibility = View.VISIBLE

                }else if(position == 2){
                    Log.d("Tab", position.toString())
                    step2points.visibility = View.VISIBLE
                    step3circle.visibility = View.VISIBLE
                    backcomplian.visibility = View.VISIBLE

                    next.visibility = View.INVISIBLE
                    next2.visibility = View.VISIBLE

                }else{

                }
            }

        })

        next.setOnClickListener {
            Log.d("NEXT", "Yes")
            Log.d("Detail", sp!!.getString("Subject", "-"))
            Log.d("Detail", sp!!.getString("Description", "-"))
            Log.d("Detail", sp!!.getString("Type", "-"))
            Log.d("Detail", sp!!.getString("LAT", "-"))
            Log.d("Detail", sp!!.getString("LONG", "-"))

            val textFragment = findViewById(R.id.subjectInput) as EditText
            d("TestK",textFragment.text.toString())
            viewpager.setCurrentItem(getItem(+1,viewpager), true)




        }
        next2.setOnClickListener {
        }

        backcomplian.setOnClickListener {
            Log.d("NEXT", "Yes")
            viewpager.setCurrentItem(getItem(-1,viewpager), true)
        }
        closebutton.setOnClickListener {
                d("TEE", textListener.toString())
                val mAlert = AlertDialog.Builder(this@Complian)
                mAlert.setTitle("คุณยังไม่ได้บันทึกการร้องเรียน")
                mAlert.setMessage("คุณต้องการที่จะปิดโดยไม่บันทึกใช่หรือไหม่?")
                mAlert.setNegativeButton("ไม่ใช่") { dialog, which ->
                    dialog.dismiss()

                }
                mAlert.setPositiveButton("ใช่,ปิด") { dialog, which ->
                    dialog.dismiss()
                    sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                    var edditor = sp!!.edit()
                    edditor.putString("Subject","")
                    edditor.putString("Detail","")
                    edditor.putInt("spin",0)
                    edditor.commit()
                    finish()
                }
                mAlert.show()
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
object Pager {


    fun getItem(i: Int,pg : androidx.viewpager.widget.ViewPager): Int {
        return pg.getCurrentItem() + i
    }

}
    public fun getListView(): CustomScrollView {
        return YOURLISTVIEW!!
    }
    public fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainercomplian,fragment)
        fragmentTransaction?.commit()

    }
    public fun replace(){

    }


}