package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.profiewithpicture.*

class Profilewithpicture : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profiewithpicture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logoutbut.setOnClickListener {
            replaceFragmentToLeft(FragmentProfile())
        }
        savelist.setOnClickListener {

            replaceFragmentToRight(FragmentBookmark())
        }

    }
    private fun replaceFragmentToRight(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)

        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)

    }
    private fun replaceFragmentToLeft(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)

        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)

    }
}
