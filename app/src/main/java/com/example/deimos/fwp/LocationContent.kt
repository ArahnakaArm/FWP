package com.example.deimos.fwp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.galleryinfo.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.locationcontent.*

class LocationContent : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.locationcontent)
        var bundle : Bundle ? = intent.extras
        var Name = bundle!!.getString("Name")
        var Info = bundle!!.getString("Info")
        var Tel = bundle!!.getString("Tel")
        var Email = bundle!!.getString("Email")

        locationname.setText(Name)
        locationinfo.setText(Info)
        telinfo.setText(Tel)
        emailinfo.setText(Email)


    }
    override fun onBackPressed()
    {
        super.onBackPressed()
    }
}