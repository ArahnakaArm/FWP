package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log.d

class LogInAndProfileHolder : AppCompatActivity(){
    var sp: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        d("Detect","dd")
        setContentView(R.layout.loginandprofileholder)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);




        if(sp?.getBoolean("LogIn_State", false)==false) {
           // replaceFisrtFragment(LogIn())
        }else{
           // replaceFisrtFragment(Profilewithpicture())
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        bottomNav.menu.getItem(4).isCheckable=true
        bottomNav.menu.getItem(4).isChecked=true
         bottomNav.menu.getItem(4).isEnabled=false
    }

    public fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()
         fragmentTransaction.addToBackStack(null)

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

                replaceFragment(FragmentVideo())
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
                val intent = Intent(this@LogInAndProfileHolder,ComplainHolder::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                ///Toast.makeText(this@MainActivity,"Edit",Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                item.isCheckable = true
                val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNav.menu.getItem(0).isEnabled = true
                bottomNav.menu.getItem(1).isEnabled = true
                bottomNav.menu.getItem(3).isEnabled = true
                bottomNav.menu.getItem(4).isEnabled = true

                replaceFragment(FragmentHome())
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

                replaceFragment(LocationList())
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
                val intent = Intent(this@LogInAndProfileHolder, LogInAndProfileHolder::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                this@LogInAndProfileHolder.startActivity(intent)

                return@OnNavigationItemSelectedListener true

            }

        }


        false


    }
    public fun replaceFisrtFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()


    }

    override fun onResume() {

        super.onResume()
        d("t",sp?.getString("user_token", "-").toString())
        d("t","resume")
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.menu.getItem(4).isCheckable=true
        bottomNav.menu.getItem(4).isChecked=true
      if(sp!!.getBoolean("LogIn_State",false)==false) {
           // replaceFisrtFragment(LogIn())
        }else{
          //  replaceFisrtFragment(Profilewithpicture())
        }
    }






}