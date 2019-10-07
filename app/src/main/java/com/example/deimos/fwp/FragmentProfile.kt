package com.example.deimos.fwp

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.profilefragment.*


class FragmentProfile : Fragment() {
    var mainActivity : MainActivity= MainActivity()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profilefragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signinbut.setOnClickListener {
            val sp = context?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
            val editor = sp?.edit()
            editor?.putBoolean("My_Value",true)
            editor?.commit()

            val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            view.menu.getItem(2).isCheckable=true
            view.menu.getItem(2).isChecked=true



            replaceFragment(FragmentHome())
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()


    }
}