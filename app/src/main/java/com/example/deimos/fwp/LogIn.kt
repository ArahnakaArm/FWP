package com.example.deimos.fwp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.widget.LoginButton





data class UserProfile(var resultData : resultData2,
                       var resultCode : String
        ,var developerMessage:String
)

data class resultData2(var _id :String,var birthdate: String ,var email: String,var firstName : String ,
                       var gender : String,var lastName :String)

data class resultData(val access_token:String)
data class UserMo(val resultData : resultData,
                 val resultCode : String
                 ,val developerMessage:String)
class LogIn : Fragment() {
    var callback : CallbackManager?=null
    var sp: SharedPreferences? = null
    var loginButton : LoginButton?=null
    var mainActivity : MainActivity= MainActivity()
    var mAPIService: ApiService? = null
    private var token :String?=null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback = CallbackManager.Factory.create()


        loginButton = view.findViewById(R.id.facebooksignin) as LoginButton
        loginButton!!.setReadPermissions("email")
        // If using in a fragment
        loginButton!!.setFragment(this)

        // Callback registration
        loginButton!!.registerCallback(callback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                var access_token : String = loginResult.accessToken.token

                d("Token",access_token)


                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })



        facebooksignindummy.setOnClickListener {
            loginButton!!.performClick()
        }


        mAPIService = ApiUtils.apiService
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        signinbut.setOnClickListener {
           if ((emailinput.text.trim().toString().isEmpty())||(passwordinput.text.trim().toString().isEmpty())){
               d("Logs","Work!")
              val mAlert = AlertDialog.Builder(view.context)
               mAlert.setTitle("เกิดข้อผิดพลาด")
               mAlert.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน")
               mAlert.setNegativeButton("ตกลง"){dialog, which ->
                   dialog.dismiss()
               }
               mAlert.show()
           }

           else{
               var userLogin = UserModel(emailinput.text.toString(),passwordinput.text.toString())
               val sdf = SimpleDateFormat("yyMMdd")
               val currentDate = sdf.format(Date())
               val r = (10..12).shuffled().first()


               mAPIService!!.userLogin(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),userLogin).enqueue(object : Callback<UserMo> {

                   override fun onResponse(call: Call<UserMo>, response: Response<UserMo>) {

                       if (response.isSuccessful()) {

                           d("ss",response.body()!!.resultCode)
                           d("ss",response.body()!!.resultData.access_token)
                           d("ss",response.body()!!.developerMessage)
                           token = response.body()!!.resultData.access_token
                           val sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
                               val editor = sp?.edit()
                               editor?.putBoolean("LogIn_State", true)
                                editor?.putString("user_token", response.body()!!.resultData.access_token)
                               editor?.commit()


                               replaceFragmentToRight(Profilewithpicture())

                           getTest()

                           /*
                           }else if(response.body()!!.getresultCode()=="40101"){
                               d("ss",response.body()!!.getresultCode())
                               d("",response.code().toString())
                               val mAlert = AlertDialog.Builder(view.context)
                               mAlert.setTitle("เกิดข้อผิดพลาด")
                               mAlert.setMessage("รหัสผ่านของท่านผิด")
                               mAlert.setNegativeButton("ตกลง"){dialog, which ->
                                   dialog.dismiss()
                               }
                               mAlert.show()

                           }*/
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

                   override fun onFailure(call: Call<UserMo>, t: Throwable) {
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






           }

        }
        createacc.setOnClickListener {
            val intent = Intent(activity,Register::class.java)
            activity?.startActivity(intent)
            activity?.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        exitbut.setOnClickListener {

            activity?.finish()
        }

    }
    private fun replaceFragmentToRight(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        val bundle = Bundle()
        val myMessage = token
        bundle.putString("message", myMessage)
        fragment.arguments = bundle
        fragmentTransaction?.commit()
      //  fragmentTransaction?.addToBackStack(null)

    }
    fun getTest(){
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtils.apiService

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r)).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    d("ss",response.body()!!.resultData.firstName)

                }
            }
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {

            }
        })



    }

     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callback!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}
