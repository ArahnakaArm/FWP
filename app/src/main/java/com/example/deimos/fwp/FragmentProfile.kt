package com.example.deimos.fwp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.profilefragment.*

class FragmentProfile : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profilefragment,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createacc.setOnClickListener {
            replaceFragment(Register())
    }

}

private fun replaceFragment(fragment: Fragment){
    val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
    fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
    fragmentTransaction?.commit()
    fragmentTransaction?.addToBackStack(null)
    }
}