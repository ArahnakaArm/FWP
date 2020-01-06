package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.testpager.*
import java.util.*

class TestPager : AppCompatActivity() {
    var x = 0
    var i = 0
    var pg : androidx.viewpager.widget.ViewPager?=null
    var sharedPreferences : SharedPreferences?=null
    var sp: SharedPreferences? = null

    val stack = Stack<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.testpager)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(navListener)


        pg = viewPager
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        /* if(sp?.getBoolean("LogIn_State", false)==false) {
             val intent = Intent(this@Complian,LogInWithSkip::class.java)
             startActivity(intent)
         }else{
         }*/
        val adapter = ComplianHolder.ViewPagerAdapter(supportFragmentManager)
        adapter.addFragement(FragmentHome(),"one")
        adapter.addFragement(LocationList(),"two")
        adapter.addFragement(FragmentVideo(),"three")
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = adapter
        viewPager.beginFakeDrag();
        viewPager.setCurrentItem(0)

    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {

            R.id.video -> {
                viewPager.setCurrentItem(2,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.complain -> {


                if(sp?.getBoolean("LogIn_State", false)==false){
                    val intent = Intent(this@TestPager, LogInWithSkip::class.java)
                    startActivity(intent)
                }else {
                    val intent = Intent(this@TestPager, Complian::class.java)
                    startActivity(intent)
                }


                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                viewPager.setCurrentItem(0,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.location -> {
                viewPager.setCurrentItem(1,false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {

                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                val goLogIn = Intent(this@TestPager,LogIn::class.java)
                if(sp?.getBoolean("LogIn_State", false)==false) {
                    this@TestPager.startActivity(goLogIn)

                }else{
                    replaceFisrtFragment(Profilewithpicture())
                }
                /*  val intent = Intent(this@MainActivity, LogInAndProfileHolder::class.java)
                  intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION*/


                return@OnNavigationItemSelectedListener true

            }

        }


        false


    }

    public fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment,"fragment_tag").commit()
        /* fragmentTransaction
                 .add(R.id.fragmentcontainer, fragment, "frag1")
                 .commit()
 */

    }
    public fun replaceFisrtFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)

        fragmentTransaction.commit()


    }
    public fun replaceBack(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment,"BACK")
        fragmentTransaction.commit()


    }


    /* override fun onBackPressed() {


            if (supportFragmentManager.backStackEntryCount <= 1) {
                finish()
            } else {
                super.onBackPressed()
            }




      /*
        /*
        val count = supportFragmentManager.backStackEntryCount
        d("Test",count.toString())
        if(count == 1) {
            d("Test", "OK")
        }
        supportFragmentManager.popBackStackImmediate()*/
        super.onBackPressed();
*/
    }*/
    override fun onBackPressed() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.menu.getItem(0).isEnabled = true
        bottomNav.menu.getItem(1).isEnabled = true
        bottomNav.menu.getItem(2).isEnabled = true
        bottomNav.menu.getItem(3).isEnabled = true
        bottomNav.menu.getItem(4).isEnabled = true



        val fragmentManager = supportFragmentManager

        val currentFragment = fragmentManager.findFragmentById(R.id.fragmentcontainer)

        if (currentFragment!!.tag == "fragment_tag") {
            Log.d("BACK", "Ar")
            finish()

        }
        if (supportFragmentManager.backStackEntryCount == 0 && currentFragment!!.tag != "fragment_tag"){
            replaceFragment(FragmentHome())
        }
        supportFragmentManager.popBackStack()
    }


    override fun onResume() {
        super.onResume()
        if(sp!!.getBoolean("isFromLogin",false)==true && (sp!!.getBoolean("LogIn_State",false))==true ) {
            replaceFisrtFragment(Profilewithpicture())
            sp!!.edit {
                putBoolean("isFromLogin", false)
            }

        }
        Log.d("Resume", "YES")
    }


}

