package com.example.deimos.fwp

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.complainfragment.*
import android.widget.ArrayAdapter
import android.widget.TextView





class FragmentComplain : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.complainfragment,container,false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var arrayList = mutableListOf<String>()
        arrayList.add("ประเภท")
        arrayList.add("Mock1")
        arrayList.add("Mock2")
        arrayList.add("Mock3")
        arrayList.add("Mock4")
        arrayList.add("Mock5")
        arrayList.add("Mock6")
        val arrayAdapter = ArrayAdapter<String>(context,R.layout.spinner_item, arrayList)
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
                context, R.layout.spinner_item, arrayList) {
            override fun isEnabled(position: Int): Boolean {
                return if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    false
                } else {
                    true
                }
            }

            override fun getDropDownView(position: Int, convertView: View?,
                                         parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position>0) {
                    Toast.makeText(context, arrayAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }



        sendcomplainbutton.setOnClickListener {
            Toast.makeText(context,"Sent",Toast.LENGTH_SHORT).show()
        }
    }




}