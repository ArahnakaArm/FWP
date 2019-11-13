package com.example.deimos.fwp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.success.*
import java.util.*

class Success : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.success)
        goHome.setOnClickListener {
            finish()
            finish()
        }
        idcomplian.setText("หมายเลขคำร้องของคุณคือ "+Success.GenerateRandomString2.randomString(5))
    }

    object GenerateRandomString2 {

        val DATA = "0123456789"
        var RANDOM = Random()

        fun randomString(len: Int): String {
            val sb = StringBuilder(len)

            for (i in 0 until len) {
                sb.append(DATA[RANDOM.nextInt(DATA.length)])
            }

            return sb.toString()
        }

    }
}