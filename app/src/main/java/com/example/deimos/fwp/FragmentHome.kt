package com.example.deimos.fwp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.main.*

class FragmentHome : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.homefragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

       homepic1.setOnClickListener {
           val intent = Intent(activity, GalleryInfo::class.java)
           activity?.startActivity(intent)

       }
        sharebutton.setOnClickListener {
            Toast.makeText(context,"Shared",Toast.LENGTH_SHORT).show()
        }

        share.setOnClickListener {
            Toast.makeText(context,"Shared",Toast.LENGTH_SHORT).show()
        }








        li1.setOnClickListener {
            Toast.makeText(context,"Fetured",Toast.LENGTH_SHORT).show()
        }
        li2.setOnClickListener {
            Toast.makeText(context,"Article",Toast.LENGTH_SHORT).show()
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
    }
}

