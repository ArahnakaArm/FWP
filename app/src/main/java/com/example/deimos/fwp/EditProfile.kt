package com.example.deimos.fwp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.editprofile.*
import kotlinx.android.synthetic.main.profiewithpicture.*
import kotlinx.android.synthetic.main.register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat

import java.util.*
data class ChangeProfileModel(var firstName : String,var lastName : String,var gender : String,var birthDate : String)
class EditProfile : AppCompatActivity() {
    var sp: SharedPreferences? = null
    var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var name: String? = null
    private var surname: String? = null
    private var birthday: String? = null
    private var email: String? = null
    var mAPIService: ApiServiceMember? = null
    private var gender: String? = "Male"
    var token : String?=null
    private var sharedPreferences:SharedPreferences?=null
    private var changeprofilemodel: ChangeProfileModel? = null
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editprofile)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        d("getPara", sp!!.getString("user_id", ""))
        d("getPara", sp!!.getString("user_email", ""))
        email = sp!!.getString("user_email", "")
        userId = sp!!.getString("user_id", "")


        token = sp!!.getString("user_token","-")

        if(token != "-") {
            setField()
        }
        backbuttonedit.setOnClickListener {
            onBackPressed()
        }
        mAPIService = ApiUtilsMember.apiServiceMember
        butedit1.setOnClickListener {
            butedit1.setBackgroundResource(R.drawable.buttonround)
            butedit1.setTextColor(resources.getColor(R.color.White))
            butedit2.setBackgroundResource(R.drawable.buttonround2)
            butedit2.setTextColor(resources.getColor(R.color.Grey))
            butedit3.setBackgroundResource(R.drawable.buttonround2)
            butedit3.setTextColor(resources.getColor(R.color.Grey))
            gender = "Male"

        }
        butedit2.setOnClickListener {
            butedit2.setBackgroundResource(R.drawable.buttonround)
            butedit2.setTextColor(resources.getColor(R.color.White))
            butedit1.setBackgroundResource(R.drawable.buttonround2)
            butedit1.setTextColor(resources.getColor(R.color.Grey))
            butedit3.setBackgroundResource(R.drawable.buttonround2)
            butedit3.setTextColor(resources.getColor(R.color.Grey))
            gender = "Female"

        }
        butedit3.setOnClickListener {
            butedit3.setBackgroundResource(R.drawable.buttonround)
            butedit3.setTextColor(resources.getColor(R.color.White))
            butedit1.setBackgroundResource(R.drawable.buttonround2)
            butedit1.setTextColor(resources.getColor(R.color.Grey))
            butedit2.setBackgroundResource(R.drawable.buttonround2)
            butedit2.setTextColor(resources.getColor(R.color.Grey))
            gender = "LGBT"
        }
        dateedit.setOnClickListener {
            val c = Calendar.getInstance()
            var year: Int = c.get(Calendar.YEAR)
            var month: Int = c.get(Calendar.MONTH)

            var day: Int = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this@EditProfile, DatePickerDialog.OnDateSetListener { view, yy, mm, dd ->

                var newMonth: String
                newMonth = (mm + 1).toString()

                var newDay: String
                newDay = dd.toString()
                if (newMonth.length == 1) {
                    newMonth = "0" + newMonth
                }
                if (newDay.length == 1) {
                    newDay = "0" + newDay
                }
                birthday = "" + yy + "-" + (newMonth) + "-" + newDay
                // Display Selected date in textbox

                dateedit.setText("" + newDay + "-" + (newMonth) + "-" + yy)
            }, year, month, day)
            dpd.show()
        }
        savebut.setOnClickListener {
            if (nameinputedit.text.trim().toString().isEmpty() || surnameedit.text.trim().toString().isEmpty() ||
                    dateedit.text.trim().toString().isEmpty()) {
                val mAlertDialog = AlertDialog.Builder(it.context)
                mAlertDialog.setTitle("พบข้อผิดพลาด")
                mAlertDialog.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน")
                mAlertDialog.setNegativeButton("ตกลง") { dialog, which ->
                    dialog.dismiss()
                }
                mAlertDialog.show()

            } else {
                val sdf = SimpleDateFormat("yyMMdd")
                val currentDate = sdf.format(Date())
                val r = (10..12).shuffled().first()
                changeprofilemodel = ChangeProfileModel(nameinputedit.text.toString(),
                        surnameedit.text.toString(),
                        gender!!,birthday!!)
                d("CheckValue",changeprofilemodel.toString())
                d("CheckValue",userId.toString())
                d("CheckValue",token)
                sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                val partnerId = sharedPreferences!!.getString("partnerId","-")
                /// Save info ///
                val mAlert = AlertDialog.Builder(this@EditProfile)
                val mProgressDialog = ProgressDialog(this@EditProfile)
                mProgressDialog.isIndeterminate = true
                mProgressDialog.setCancelable(false)
                mProgressDialog.setMessage("Loading...")
                mProgressDialog.show()

                mAPIService!!.updateUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),userId!!,
                        changeprofilemodel!!,partnerId).enqueue(object : Callback<ChangeProfileModel> {

                    override fun onResponse(call: Call<ChangeProfileModel>, response: Response<ChangeProfileModel>) {
                        if (response.isSuccessful()) {

                            sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                            sp!!.edit {
                                putString("firstName",response.body()!!.firstName)
                                putString("lastName",response.body()!!.lastName)
                            }
                            d("CheckValue","Success")
                            mProgressDialog.dismiss();
                            mAlert.setTitle("สำเร็จ")
                            mAlert.setMessage("แก้ไขข้อมูลเรียบร้อยแล้ว")
                            mAlert.setPositiveButton("ปิด") { dialog, which ->
                                dialog.dismiss()
                                finish()
                            }
                            mAlert.show()

                        }
                        else{

                            mAlert.setTitle("พบข้อผิดพลาด")
                            mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                            mAlert.setNegativeButton("ตกลง"){dialog, which ->
                                dialog.dismiss()
                            }
                            mAlert.show()

                        }
                        mProgressDialog.dismiss()

                    }

                    override fun onFailure(call: Call<ChangeProfileModel>, t: Throwable) {
                        d("ss",t.toString())
                        val mAlert = AlertDialog.Builder(this@EditProfile)
                        mAlert.setTitle("พบข้อผิดพลาด")
                        mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                        mAlert.setNegativeButton("ตกลง"){dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()
                        mProgressDialog.dismiss()
                    }
                })


                /// End Save Info ///

            }

        }
    }


    private fun setField(){
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtilsMember.apiServiceMember

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    try {
                        nameinputedit.setText(response.body()!!.resultData.firstName)
                        surnameedit.setText(response.body()!!.resultData.lastName)
                        dateedit.setText(response.body()!!.resultData.birthDate)
                        if (response.body()!!.resultData.gender=="Male"){
                            gender="Male"
                            butedit1.performClick()
                        }
                        if (response.body()!!.resultData.gender=="Female"){
                            butedit2.performClick()
                            gender="Female"
                        }
                        if (response.body()!!.resultData.gender=="LGBT"){
                            butedit3.performClick()
                            gender="LGBT"
                        }
                        birthday=response.body()!!.resultData.birthDate

                    }catch (e : Exception){

                    }
                }
            }
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {

            }
        })





    }

    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()
    }

}
