package com.example.deimos.fwp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.galleryinfo.*
import kotlinx.android.synthetic.main.homefragment.*

class GalleryInfo : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.galleryinfo)
        sharebuttoninfo.setOnClickListener {
            Toast.makeText(this@GalleryInfo,"Shared",Toast.LENGTH_SHORT).show()
        }
        bookmarkbutton.setOnClickListener {
            Toast.makeText(this@GalleryInfo,"Bookmarked",Toast.LENGTH_SHORT).show()
        }



        backprees.setOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed()
    {
        super.onBackPressed()
    }
}