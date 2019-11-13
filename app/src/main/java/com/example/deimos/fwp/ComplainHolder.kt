package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log.d
import java.util.*

class ComplainHolder : AppCompatActivity(){
    val stack = Stack<Int>()
    var x = 0
    var i = 0
    var sp: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complianholder)

        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);


        if(sp?.getBoolean("LogIn_State", false)==false) {
          //  replaceFragment(LogInWithSkip())
        }else{
            replaceFragment(FragmentComplain())
        }



        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        bottomNav.menu.getItem(1).isCheckable=true
        bottomNav.menu.getItem(1).isChecked=true
        bottomNav.menu.getItem(1).isEnabled=false

    }
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {

            R.id.video -> {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true
                item.isCheckable = true

                replaceFragmentHaveBack(FragmentVideo())
                item.isEnabled=false
                //  Toast.makeText(this@MainActivity,item.itemId.toString(),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@MainActivity,"Video",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.complain -> {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true

                /* if(sp?.getBoolean("LogIn_State", false)==false) {
                     replaceFragment(LogInWithSkip())
                 }else{
                     replaceFragment(FragmentComplain())
                 }*/
                val intent = Intent(this@ComplainHolder,ComplainHolder::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                item.isCheckable = true
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true

                replaceFragmentHaveBack(FragmentHome())
                item.isEnabled=false
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.location -> {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true
                item.isCheckable = false

                replaceFragmentHaveBack(LocationList())
                item.isEnabled=false
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(2).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true

                /*  if(sp?.getBoolean("LogIn_State", false)==false) {
                       replaceFragment(LogIn())
                   }else{
                       replaceFragment(Profilewithpicture())
                   }*/
                val intent = Intent(this@ComplainHolder, LogInAndProfileHolder::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                this@ComplainHolder.startActivity(intent)

                return@OnNavigationItemSelectedListener true

            }

        }


        false


    }
    public fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainercomplain, fragment)
        fragmentTransaction.commit()


    }
    public fun replaceFragmentHaveBack(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainercomplain, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()


    }

    override fun onBackPressed() {
            super.onBackPressed()
            overridePendingTransition(0, 0)
            val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNav.menu.getItem(0).isEnabled = true
            bottomNav.menu.getItem(1).isEnabled = true
            bottomNav.menu.getItem(2).isEnabled = true
            bottomNav.menu.getItem(3).isEnabled = true
            bottomNav.menu.getItem(4).isEnabled = true
        d("Res","YEs")
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val editor = sp?.edit()
        editor?.putString("LAT","-")
        editor?.putString("LONG","-")
        editor?.commit()
    }

    override fun onResume() {
        super.onResume()

        d("t",sp?.getBoolean("LogIn_State", false).toString())
        d("t","resume")
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.menu.getItem(1).isCheckable=true
        bottomNav.menu.getItem(1).isChecked=true
        if(sp?.getBoolean("LogIn_State", false)==false) {
            //replaceFragment(LogInWithSkip())
        }else{
            replaceFragment(FragmentComplain())
        }
    }

}