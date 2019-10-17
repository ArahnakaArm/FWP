package com.example.deimos.fwp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.loginwithskip.*


class LogInWithSkip : Fragment() {
    var sp: SharedPreferences? = null
    var mainActivity : MainActivity= MainActivity()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable=true
        view.menu.getItem(1).isChecked=true
        return inflater.inflate(R.layout.loginwithskip,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        signinbut.setOnClickListener {
            val sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
            val editor = sp?.edit()
            editor?.putBoolean("LogIn_State", true)
            editor?.commit()
            replaceFragmentToRight(FragmentComplain())
        }
        createacc.setOnClickListener {
            val intent = Intent(activity,Register::class.java)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        skip.setOnClickListener {
            val sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
            val editor = sp?.edit()
            editor?.putBoolean("LogIn_State", false)
            editor?.commit()
           replaceFragmentToRight(FragmentComplain())
        }

    }
    private fun replaceFragmentToRight(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainercomplain,fragment)

        fragmentTransaction?.commit()
       // fragmentTransaction?.addToBackStack(null)

    }

    override fun onResume() {
        if(sp!!.getBoolean("LogIn_State",false)==true){
            replaceFragmentToRight(FragmentComplain())

        }
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable=true
        view.menu.getItem(1).isChecked=true
        super.onResume()
    }
}