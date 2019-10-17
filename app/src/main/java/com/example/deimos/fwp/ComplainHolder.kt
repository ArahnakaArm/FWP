package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import java.util.*

class ComplainHolder : AppCompatActivity(){
    val stack = Stack<Int>()
    var x = 0
    var i = 0
    var sp: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complainholder)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);


        if(sp?.getBoolean("LogIn_State", false)==false) {
            replaceFragment(LogInWithSkip())
        }else{
            replaceFragment(FragmentComplain())
        }




        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
    }
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.video -> {
                item.isCheckable = true

                stack.push(x)
                replaceFragment(FragmentVideo())

                //  Toast.makeText(this@MainActivity,item.itemId.toString(),Toast.LENGTH_SHORT).show()
                //Toast.makeText(this@MainActivity,"Video",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.complain -> {


                ///Toast.makeText(this@MainActivity,"Edit",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                item.isCheckable = true
                x = 2
                stack.push(x)
                replaceFragment(FragmentHome())
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.location -> {
                item.isCheckable = false
                x = 3
                stack.push(x)
                replaceFragment(LocationList())
                //Toast.makeText(this@MainActivity,"Location",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                val intent = Intent(this@ComplainHolder, LogInAndProfileHolder::class.java)
                this@ComplainHolder.startActivity(intent)

                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }
    public fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainercomplain, fragment)
        fragmentTransaction.commit()


    }

    override fun onBackPressed() {

        super.onBackPressed()
    }

}