package com.example.deimos.fwp

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.videocontent.*

class VideoContent : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocontent)

        var bundle : Bundle ? =intent.extras
        var Title = bundle!!.getString("Title")
        var Date = bundle!!.getString("Date")

        videotitle.setText(Title)
        date.setText(Date)
        backprees.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}