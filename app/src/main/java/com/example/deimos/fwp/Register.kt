package com.example.deimos.fwp

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat.getColor
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.deimos.fwp.R.id.buttest1
import com.google.gson.Gson
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.nextDown

class Register : Activity() {

    private var name : String?=null
    private var surname : String?=null
    private var birthday  : String?=null
    private var gender  : String?="Male"
    private var email : String?=null
    private var password : String?=null
    private var confirmpassword : String?=null
    var mAPIService: ApiService? = null



    var mDateSetListener  : DatePickerDialog.OnDateSetListener?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        mAPIService = ApiUtils.apiService
        emailinput!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                check.visibility=View.INVISIBLE
                if (!isEmailValid(s.toString())) {
                    checkwrong.visibility=View.VISIBLE
                    d("emailTest", "No")
                }
                else{
                    checkwrong.visibility=View.INVISIBLE
                    check.visibility=View.VISIBLE
                    d("emailTest", "Yes")
                }
            }

        })
        confirmpassinput!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                check2.visibility=View.INVISIBLE


                if (!isConfirmPasswordValid(s.toString(),passwordinput.text.toString())) {
                    checkwrong3.visibility=View.VISIBLE
                    d("emailTest", "No")
                }
                else if(s.isEmpty()){
                    checkwrong3.visibility=View.VISIBLE
                    check2.visibility=View.INVISIBLE

                }
                else{
                    checkwrong3.visibility=View.INVISIBLE
                    check2.visibility=View.VISIBLE
                    d("emailTest", "Yes")
                }
            }
        })

        passwordinput!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                check3.visibility=View.INVISIBLE
                checkwrong2.visibility=View.VISIBLE
                if (s.length >=6 ) {
                    checkwrong2.visibility=View.INVISIBLE
                    check3.visibility=View.VISIBLE
                    d("emailTest", "Yes")
                }
                if (!isConfirmPasswordValid(s.toString(),confirmpassinput.text.toString())) {
                    checkwrong3.visibility=View.VISIBLE
                    check2.visibility=View.INVISIBLE

                    d("emailTest", "No")
                }
                else{
                    checkwrong2.visibility=View.VISIBLE
                    d("emailTest", "No")
                }
            }
        })




    buttest1.setOnClickListener {
            buttest1.setBackgroundResource(R.drawable.buttonround)
            buttest1.setTextColor(resources.getColor(R.color.White))
            buttest2.setBackgroundResource(R.drawable.buttonround2)
            buttest2.setTextColor(resources.getColor(R.color.Grey))
            buttest3.setBackgroundResource(R.drawable.buttonround2)
            buttest3.setTextColor(resources.getColor(R.color.Grey))
            gender = "Male"

        }
        buttest2.setOnClickListener {
            buttest2.setBackgroundResource(R.drawable.buttonround)
            buttest2.setTextColor(resources.getColor(R.color.White))
            buttest1.setBackgroundResource(R.drawable.buttonround2)
            buttest1.setTextColor(resources.getColor(R.color.Grey))
            buttest3.setBackgroundResource(R.drawable.buttonround2)
            buttest3.setTextColor(resources.getColor(R.color.Grey))
            gender = "Female"

        }
        buttest3.setOnClickListener {
            buttest3.setBackgroundResource(R.drawable.buttonround)
            buttest3.setTextColor(resources.getColor(R.color.White))
            buttest1.setBackgroundResource(R.drawable.buttonround2)
            buttest1.setTextColor(resources.getColor(R.color.Grey))
            buttest2.setBackgroundResource(R.drawable.buttonround2)
            buttest2.setTextColor(resources.getColor(R.color.Grey))
            gender = "Lgbt"

        }


        backbuttonregister.setOnClickListener {
            onBackPressed()
        }
        birthinput.setOnClickListener {
            val c = Calendar.getInstance()
            var year : Int = c.get(Calendar.YEAR)
            var month : Int = c.get(Calendar.MONTH)

            var day : Int = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this@Register, DatePickerDialog.OnDateSetListener { view, yy, mm, dd ->

                var newMonth : String
                newMonth = (mm+1).toString()

                var newDay : String
                newDay = dd.toString()
                if(newMonth.length==1){
                    newMonth = "0"+newMonth
                }
                if (newDay.length==1){
                    newDay = "0"+newDay
                }
                birthday =""+yy+"-"+(newMonth)+"-"+newDay
                // Display Selected date in textbox

                birthinput.setText(""+ newDay + "-" + (newMonth)+ "-" +yy)
            }, year, month, day)
            dpd.show()
        }


        signinbut.setOnClickListener {
            if (!isValidate()){
                //Toast.makeText(this@Register,"No!!",Toast.LENGTH_SHORT).show()
                val mAlertDialog = AlertDialog.Builder(this@Register)
                mAlertDialog.setTitle("เกิดข้อผิดพลาด")
                mAlertDialog.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน")
                mAlertDialog.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlertDialog.show()
            }

            else if(!isEmailValid(emailinput.text.trim().toString())) {
                val mAlertDialog = AlertDialog.Builder(this@Register)
                mAlertDialog.setTitle("เกิดข้อผิดพลาด")
                mAlertDialog.setMessage("กรุณากรอกอีเมล์ให้ถูกต้อง")
                mAlertDialog.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlertDialog.show()
            }
            else if(!isConfirmPasswordValid(confirmpassinput.text.trim().toString(),passwordinput.text.trim().toString())) {
                val mAlertDialog = AlertDialog.Builder(this@Register)
                mAlertDialog.setTitle("เกิดข้อผิดพลาด")
                mAlertDialog.setMessage("กรุณากรอกยืนยันรหัสผ่านให้ถูกต้อง")
                mAlertDialog.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlertDialog.show()
            }
            else{
                name = nameinput.text.trim().toString()
                surname = surnameinput.text.trim().toString()
                email = emailinput.text.trim().toString()
                password = passwordinput.text.trim().toString()
                confirmpassword = confirmpassinput.text.trim().toString()
                var registermodel  = RegisterModel(name!!,surname!!,birthday!!,gender!!,email!!,password!!)
                val sdf = SimpleDateFormat("yyMMdd")
                val currentDate = sdf.format(Date())
                val r = (10..12).shuffled().first()
                mAPIService!!.createSignup(GenerateRandomString.randomString(22),"AND-"+currentDate+GenerateRandomString.randomString(r),registermodel).enqueue(object : Callback<RegisterModel> {

                    override fun onResponse(call: Call<RegisterModel>, response: Response<RegisterModel>) {
                        if (response.isSuccessful()) {
                            onBackPressed()
                        }
                        if(response.code().toString()=="409"){
                            Toast.makeText(this@Register,"Conflict",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                       //d("fail",t.toString())
                        val mAlert = AlertDialog.Builder(this@Register)
                        mAlert.setTitle("เกิดข้อผิดพลาด")
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

    override fun onBackPressed() {
        d("arm","Exited")
        super.onBackPressed()
    }

    private fun isValidate():Boolean{
        if(nameinput.text.isEmpty()||surnameinput.text.isEmpty()||birthinput.text.isEmpty()||emailinput.text.isEmpty()
        ||passwordinput.text.isEmpty()||confirmpassinput.text.isEmpty()) {
            return false
        }
        return true
    }
    private fun goSignIn(registermodel:RegisterModel){


    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun isConfirmPasswordValid(confirmpass : String,pass : String):Boolean{
        if (confirmpass.equals(pass)){
            return true
        }
        return false
    }

    object GenerateRandomString {

        val DATA = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
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

