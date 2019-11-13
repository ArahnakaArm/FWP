package com.example.deimos.fwp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RegisterHolder : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registerholder)
        replaceFragment(Register())
    }
    public fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentcontainerregister, fragment)
        fragmentTransaction.commit()
    }
}
