package com.example.deimos.fwp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.editprofile.*
import kotlinx.android.synthetic.main.register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

import java.util.*
data class ChangePasswordModel(var firstName : String,var lastName : String,var gender : String,var birthdate : String,var email:String)
class EditProfile :Fragment() {
    var sp: SharedPreferences? = null
    var mDateSetListener: DatePickerDialog.OnDateSetListener? = null
    private var name: String? = null
    private var surname: String? = null
    private var birthday: String? = null
    private var email: String? = null
    var mAPIService: ApiService? = null
    private var gender: String? = "Male"
    var token : String?=null
    private var changePasswordModel: ChangePasswordModel? = null
    private var userId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        d("getPara", sp!!.getString("user_id", ""))
        d("getPara", sp!!.getString("user_email", ""))
        email = sp!!.getString("user_email", "")
        userId = sp!!.getString("user_id", "")


        return inflater.inflate(R.layout.editprofile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backbuttonedit.setOnClickListener {
            fragmentManager?.popBackStack()
        }
        mAPIService = ApiUtils.apiService
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

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, yy, mm, dd ->

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
                mAlertDialog.setTitle("เกิดข้อผิดพลาด")
                mAlertDialog.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน")
                mAlertDialog.setNegativeButton("ตกลง") { dialog, which ->
                    dialog.dismiss()
                }
                mAlertDialog.show()

            } else {
                val sdf = SimpleDateFormat("yyMMdd")
                val currentDate = sdf.format(Date())
                val r = (10..12).shuffled().first()
                changePasswordModel = ChangePasswordModel(nameinputedit.text.toString(),
                        surnameedit.text.toString(),
                        gender!!,birthday!!,email!!)

                d("CheckValue",changePasswordModel.toString())
                d("CheckValue",userId.toString())

                token = sp!!.getString("user_token","-")
                d("CheckValue",token)
                /// Save info ///
                mAPIService!!.updateUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),userId!!,
                        changePasswordModel!!).enqueue(object : Callback<ChangePasswordModel> {

                    override fun onResponse(call: Call<ChangePasswordModel>, response: Response<ChangePasswordModel>) {
                        if (response.isSuccessful()) {
                            d("CheckValue","Success")

                            fragmentManager?.popBackStack()
                        }
                        else if(response.code()==401){
                            val mAlert = AlertDialog.Builder(view.context)
                            mAlert.setTitle("เกิดข้อผิดพลาด")
                            mAlert.setMessage("รหัสผ่านของท่านผิด")
                            mAlert.setNegativeButton("ตกลง"){dialog, which ->
                                dialog.dismiss()
                            }
                            mAlert.show()

                        }

                    }

                    override fun onFailure(call: Call<ChangePasswordModel>, t: Throwable) {
                        d("ss",t.toString())
                        val mAlert = AlertDialog.Builder(view.context)
                        mAlert.setTitle("เกิดข้อผิดพลาด")
                        mAlert.setMessage("ท่านไม่ได้เชื่อมต่ออินเตอร์เน็ต")
                        mAlert.setNegativeButton("ตกลง"){dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()
                    }
                })


                /// End Save Info ///

            }

        }

    }
}
