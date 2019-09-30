package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.complainfragment.*

class FragmentComplain : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complainfragment,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sendcomplainbutton.setOnClickListener {
            Toast.makeText(context,"Sent",Toast.LENGTH_SHORT).show()
        }
    }
}