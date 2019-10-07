package com.example.deimos.fwp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat.getColor
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.deimos.fwp.R.id.buttest1
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.register.*

class Register : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        buttest1.setOnClickListener {
            buttest1.setBackgroundResource(R.drawable.buttonround)
            buttest1.setTextColor(resources.getColor(R.color.White))
            buttest2.setBackgroundResource(R.drawable.buttonround2)
            buttest2.setTextColor(resources.getColor(R.color.Grey))
            buttest3.setBackgroundResource(R.drawable.buttonround2)
            buttest3.setTextColor(resources.getColor(R.color.Grey))

        }
        buttest2.setOnClickListener {
            buttest2.setBackgroundResource(R.drawable.buttonround)
            buttest2.setTextColor(resources.getColor(R.color.White))
            buttest1.setBackgroundResource(R.drawable.buttonround2)
            buttest1.setTextColor(resources.getColor(R.color.Grey))
            buttest3.setBackgroundResource(R.drawable.buttonround2)
            buttest3.setTextColor(resources.getColor(R.color.Grey))
        }
        buttest3.setOnClickListener {
            buttest3.setBackgroundResource(R.drawable.buttonround)
            buttest3.setTextColor(resources.getColor(R.color.White))
            buttest1.setBackgroundResource(R.drawable.buttonround2)
            buttest1.setTextColor(resources.getColor(R.color.Grey))
            buttest2.setBackgroundResource(R.drawable.buttonround2)
            buttest2.setTextColor(resources.getColor(R.color.Grey))
        }
        backbuttonregister.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        d("arm","Exited")

        super.onBackPressed()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}

