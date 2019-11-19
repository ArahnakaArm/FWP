package com.example.deimos.fwp

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.changepassword.*
import kotlinx.android.synthetic.main.editprofile.*
import kotlinx.android.synthetic.main.register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity


data class ChangePasswordModel(var oldPassword : String,var newPassword : String)
class ChangePassword : AppCompatActivity(){
    var sp: SharedPreferences? = null
    var mAPIService: ApiService? = null
    private var userId: String? = null
    private var changepasswordmodel: ChangePasswordModel? = null
    var token : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.changepassword)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        userId = sp!!.getString("user_id", "")

        //Parameter Check//

        newpassword!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checknew1.visibility=View.INVISIBLE
                checkwrongnew1.visibility=View.VISIBLE
                if (s.length >=6 ) {
                    checkwrongnew1.visibility=View.INVISIBLE
                    checknew1.visibility=View.VISIBLE
                    d("emailTest", "Yes")
                }
                else if (!isConfirmPasswordValid(s.toString(),confirmnewpass.text.toString())) {
                    checkwrongnew1.visibility=View.VISIBLE
                    checknew1.visibility=View.INVISIBLE

                    d("emailTest", "No")
                }
                else{
                    checkwrongnew1.visibility=View.VISIBLE
                    d("emailTest", "No")
                }
            }
        })

        confirmnewpass!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checknews2.visibility=View.INVISIBLE
                if (s.length < 6 ) {
                    checkwrongnew2.visibility=View.VISIBLE
                    checknews2.visibility=View.INVISIBLE
                    d("emailTest", "Yes")
                }

                else if (!isConfirmPasswordValid(s.toString(),newpassword.text.toString())) {
                    checkwrongnew2.visibility=View.VISIBLE
                    d("emailTest", "No")
                }
                else if(s.isEmpty()){
                    checkwrongnew2.visibility=View.VISIBLE
                    checknews2.visibility=View.INVISIBLE

                }
                else{
                    checkwrongnew2.visibility=View.INVISIBLE
                    checknews2.visibility=View.VISIBLE
                    d("emailTest", "Yes")
                }
            }
        })

        //End of Parameter Check//
        savebutpassword.setOnClickListener {
            //Validation and PUT DATA//
            val mAlert = AlertDialog.Builder(this@ChangePassword)
            if(newpassword.text.isEmpty() || confirmnewpass.text.isEmpty() || oldpassinput.text.isEmpty()){
                //Validation//
                mAlert.setTitle("พบข้อผิดพลาด")
                mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                mAlert.setPositiveButton("ปิด") { dialog, which ->
                    dialog.dismiss()
                    fragmentManager?.popBackStack()
                }
                mAlert.show()

            }
            else {
                //PUT DATA//
                mAPIService = ApiUtils.apiService
                val sdf = SimpleDateFormat("yyMMdd")
                val currentDate = sdf.format(Date())
                val r = (10..12).shuffled().first()
                token = sp!!.getString("user_token","-")
                changepasswordmodel = ChangePasswordModel(oldpassinput.text.toString(), newpassword.text.toString())
                val mProgressDialog = ProgressDialog(this@ChangePassword)
                mProgressDialog.isIndeterminate = true
                mProgressDialog.setCancelable(false)
                mProgressDialog.setMessage("Loading...")
                mProgressDialog.show()
                mAPIService!!.changePassword("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),userId!!,
                        changepasswordmodel!!).enqueue(object : Callback<ResponeModel> {

                    override fun onResponse(call: Call<ResponeModel>, response: Response<ResponeModel>) {
                        if (response.isSuccessful()) {
                            mProgressDialog.dismiss();
                            mAlert.setTitle("สำเร็จ")
                            mAlert.setMessage("เปลี่ยนรหัสผ่านเรียบร้อยแล้ว")
                            mAlert.setPositiveButton("ปิด") { dialog, which ->
                                dialog.dismiss()
                                finish()
                            }
                            mAlert.show()
                        }
                        else{
                            mProgressDialog.dismiss();
                            mAlert.setTitle("พบข้อผิดพลาด")
                            mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                            mAlert.setNegativeButton("ตกลง"){dialog, which ->
                                dialog.dismiss()
                            }
                            mAlert.show()

                        }

                    }
                    override fun onFailure(call: Call<ResponeModel>, t: Throwable) {
                        mProgressDialog.dismiss();
                        d("ss",t.toString())
                        mAlert.setTitle("พบข้อผิดพลาด")
                        mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                        mAlert.setNegativeButton("ตกลง"){dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()
                    }

                })

            }
            //End of Validation and PUT DATA//
        }
        backbuttonchangepass.setOnClickListener {
            onBackPressed()
        }

    }

    fun isConfirmPasswordValid(confirmpass : String,pass : String):Boolean{
        if (confirmpass.equals(pass)){
            return true
        }
        return false
    }
}