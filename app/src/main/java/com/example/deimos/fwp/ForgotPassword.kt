package com.example.deimos.fwp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.forgotpassword.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

data class ForgotModel(var email : String)
class ForgotPassword : AppCompatActivity() {
    var mAPIService: ApiService? = null
    var Sendemail : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)

        backbuttonforgot.setOnClickListener {
           onBackPressed()
        }
        savebutforgotpassword.setOnClickListener {
            mAPIService = ApiUtils.apiService
            Sendemail = forgotemail.text.trim().toString()
            var forgotModel  = ForgotModel(Sendemail!!)
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            val mAlert = AlertDialog.Builder(this@ForgotPassword)
            mAPIService!!.forgotPassword(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),"Member",forgotModel).enqueue(object : Callback<ResponeModel> {

                override fun onResponse(call: Call<ResponeModel>, response: Response<ResponeModel>) {
                    if (response.isSuccessful()) {
                        finish()
                    }
                    else{
                        mAlert.setTitle("พบข้อผิดพลาด")
                        mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                        mAlert.setNegativeButton("ตกลง"){dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()
                    }
                }
                override fun onFailure(call: Call<ResponeModel>, t: Throwable) {
                    //d("fail",t.toString())
                    mAlert.setTitle("พบข้อผิดพลาด")
                    mAlert.setMessage("ท่านไม่ได้เชื่อมต่ออินเตอร์เน็ต")
                    mAlert.setNegativeButton("ตกลง"){dialog, which ->
                        dialog.dismiss()
                    }
                    mAlert.show()
                }
            })
        }

    }


}

