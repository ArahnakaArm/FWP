package com.example.deimos.fwp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class LogInAndProfileHolder : AppCompatActivity(){
    var sp: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginandprofileholder)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);


        if(sp?.getBoolean("LogIn_State", false)==false) {
            replaceFragment(LogIn())
        }else{
            replaceFragment(Profilewithpicture())
        }

    }

    public fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction.commit()
        // fragmentTransaction.addToBackStack(null)

    }
}