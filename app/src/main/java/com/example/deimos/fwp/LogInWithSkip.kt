package com.example.deimos.fwp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.NonNull
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.loginwithskip.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import com.facebook.login.LoginManager


class LogInWithSkip : AppCompatActivity() {
    var callback : CallbackManager?=null
    var sp: SharedPreferences? = null
    var loginButton : LoginButton?=null
    var mainActivity : MainActivity= MainActivity()
    var mAPIService: ApiServiceAuth? = null
    private var token :String?=null
    private var tokenFacebook : String?=null
    private var tokenGoogle : String?=null
    private var authCodeGoogle: String? = null
    val TAG = "ServerAuthCodeActivity"
    private var acc : AccessToken?=null
    private var sharedPreferences:SharedPreferences?=null
    private val RC_GET_AUTH_CODE = 9003
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuthCodeTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginwithskip)

        /////Facebook/////


        closebuttonloginwithskip.setOnClickListener {
            finish()
        }
        callback = CallbackManager.Factory.create()
        loginButton = findViewById(R.id.facebooksignin) as LoginButton
        loginButton!!.setReadPermissions("email")
       // loginButton!!.setFragment(this@LogInWithSkip)

        loginButton!!.registerCallback(callback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                var access_token : String = loginResult.accessToken.token
                tokenFacebook = access_token
                d("Token","Facebook Token :"+ tokenFacebook)
                acc = AccessToken.getCurrentAccessToken();
                getLocalTokenFacebook()



            }
            override fun onCancel() {
            }

            override fun onError(exception: FacebookException) {
            }
        })

        facebooksignindummy.setOnClickListener {
            loginButton!!.performClick()
        }

        /*logoutfacebook.setOnClickListener {

                LoginManager.getInstance().logOut()

        }*/
        /////Facebook/////

        /////Google/////
        validateServerClientIDGoogle()
        val serverClientId = "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this@LogInWithSkip, gso)

        googlesignin.setOnClickListener {

            getAuthCodeGoogle()
        }

        /*logoutgoogle.setOnClickListener {
            signOutGoogle();
        }*/

        /////Google/////

        forgotingbutton.setOnClickListener {
            val intent = Intent(this@LogInWithSkip,ForgotPassword::class.java)
            startActivity(intent)
        }



        mAPIService = ApiUtilsAuth.apiServiceAuth
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);




        signinbut.setOnClickListener {
            if ((emailinput.text.trim().toString().isEmpty())||(passwordinput.text.trim().toString().isEmpty())){
                d("Logs","Work!")
                val mAlert = AlertDialog.Builder(applicationContext)
                mAlert.setTitle("พบข้อผิดพลาด")
                mAlert.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน")
                mAlert.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlert.show()
            }

            else{
                sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                val partnerId = sharedPreferences!!.getString("partnerId","-")
                var userLogin = UserModel(emailinput.text.toString(),passwordinput.text.toString())
                val sdf = SimpleDateFormat("yyMMdd")
                val currentDate = sdf.format(Date())
                val r = (10..12).shuffled().first()
                mAPIService!!.userLogin(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),userLogin,partnerId).enqueue(object : Callback<UserMo> {

                    override fun onResponse(call: Call<UserMo>, response: Response<UserMo>) {

                        if (response.isSuccessful()) {

                            d("ss",response.body()!!.resultCode)
                            d("ss",response.body()!!.resultData.access_token)
                            d("ss",response.body()!!.developerMessage)
                            token = response.body()!!.resultData.access_token
                            val sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
                            val editor = sp?.edit()
                            editor?.putBoolean("LogIn_State", true)
                            editor?.putString("user_token", response.body()!!.resultData.access_token)
                            editor?.putString("LogIn_Type", "Local")
                            d("AA",sp?.getBoolean("LogIn_State", false).toString())
                            editor?.commit()
                            val intent = Intent(this@LogInWithSkip, Complian::class.java)
                            startActivity(intent)
                            finish()


                        }
                        else{
                            val mAlert = AlertDialog.Builder(applicationContext)
                            mAlert.setTitle("พบข้อผิดพลาด")
                            mAlert.setMessage("รหัสผ่านของท่านผิด")
                            mAlert.setNegativeButton("ตกลง"){dialog, which ->
                                dialog.dismiss()
                            }
                            mAlert.show()

                        }


                    }

                    override fun onFailure(call: Call<UserMo>, t: Throwable) {
                        d("ss",t.toString())
                        val mAlert = AlertDialog.Builder(applicationContext)
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
        createacc.setOnClickListener {
            val intent = Intent(this@LogInWithSkip,RegisterHolder::class.java)
            startActivity(intent)
        }
        skip.setOnClickListener {
           val intent = Intent(this@LogInWithSkip,Complian::class.java)
            startActivity(intent)
            finish()
        }

    }


    private fun replaceFragmentToRight(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = supportFragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        val bundle = Bundle()
        val myMessage = token
        bundle.putString("message", myMessage)
        fragment.arguments = bundle
        fragmentTransaction?.commit()
        //fragmentTransaction?.addToBackStack(null)

    }
    private fun replaceFragmentToRightToForgot(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = supportFragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        val bundle = Bundle()

        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)

    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GET_AUTH_CODE) {
            // [START get_auth_code]
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val authCode = account.serverAuthCode
                Log.d("Test", authCode)
                authCodeGoogle = authCode
                // Show signed-un UI
                getTokenGoogle()


                // TODO(developer): send code to server and exchange for access/refresh/ID tokens
            } catch (e: ApiException) {
                Log.w(TAG, "Sign-in failed", e)

            }

            // [END get_auth_code]
        }
        callback!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun validateServerClientIDGoogle() {
        val serverClientId = "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com"
        val suffix = ".apps.googleusercontent.com"
        if (!serverClientId.trim { it <= ' ' }.endsWith(suffix)) {
            val message = "Invalid server client ID in strings.xml, must end with $suffix"

            Log.w(TAG, message)
            Toast.makeText(this@LogInWithSkip, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun getAuthCodeGoogle() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE)
    }

    fun getTokenGoogle() {
        val client = OkHttpClient()
        val requestBody = FormEncodingBuilder()
                .add("grant_type", "authorization_code")
                .add("client_id", "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com")
                .add("client_secret", "m9NhWM4uKEAxtL-awlfpr9M8")
                .add("redirect_uri", "")
                .add("code", authCodeGoogle)
                .build()
        val request = Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(requestBody)
                .build()
        client.newCall(request).enqueue(object : com.squareup.okhttp.Callback {
            override fun onFailure(request: Request, e: IOException) {
                Log.e("Test", e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(response: com.squareup.okhttp.Response) {
                try {
                    //JSONArray arr = new JSONArray(response);
                    //  JSONObject jObj = arr.getJSONObject(0);
                    //
                    val jsonObject = JSONObject(response.body().string())
                    val token = jsonObject.getString("access_token")
                    val message = jsonObject.toString(5)
                    Log.i("Test", message)
                    tokenGoogle =token
                    Log.i("Token", "Google Token :" + tokenGoogle)
                    getLocalTokenGoogle()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }

    private fun signOutGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this@LogInWithSkip,OnCompleteListener<Void> {

            Toast.makeText(this@LogInWithSkip,"Loged Out",Toast.LENGTH_SHORT).show()
        })
    }
    private fun getLocalTokenGoogle(){
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        mAPIService = ApiUtilsAuth.apiServiceAuth
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val editor = sp?.edit()
        editor?.commit()
        editor?.putBoolean("LogIn_State", true)
        d("AA",sp?.getBoolean("LogIn_State", false).toString())

        mAPIService!!.userLoginGoogle(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),tokenGoogle!!,partnerId).enqueue(object : Callback<UserMo> {

            override fun onResponse(call: Call<UserMo>, response: Response<UserMo>) {
                if (response.isSuccessful()) {
                    token=response.body()!!.resultData.access_token
                    d("Token","Local Token : "+token)
                    val sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)

                    val editor = sp?.edit()
                    editor?.putBoolean("LogIn_State", true)
                    editor?.putBoolean("LogIn_StateGoogle", true)
                    editor?.putString("user_token", response.body()!!.resultData.access_token)
                    editor?.putString("LogIn_Type", "Social")

                    editor?.commit()

                    val intent = Intent(this@LogInWithSkip, Complian::class.java)
                    startActivity(intent)
                    finish()
                    //replaceFragmentToRight(ComplianHolder())




                }
                else if(response.code()==401){
                    val mAlert = AlertDialog.Builder(this@LogInWithSkip)
                    mAlert.setTitle("พบข้อผิดพลาด")
                    mAlert.setMessage("รหัสผ่านของท่านผิด")
                    mAlert.setNegativeButton("ตกลง"){dialog, which ->
                        dialog.dismiss()
                    }
                    mAlert.show()

                }

            }

            override fun onFailure(call: Call<UserMo>, t: Throwable) {
                d("ss",t.toString())
                val mAlert = AlertDialog.Builder(this@LogInWithSkip)
                mAlert.setTitle("พบข้อผิดพลาด")
                mAlert.setMessage("ท่านไม่ได้เชื่อมต่ออินเตอร์เน็ต")
                mAlert.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlert.show()
            }
        })

    }
    private fun getLocalTokenFacebook(){
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        mAPIService = ApiUtilsAuth.apiServiceAuth
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.userLoginFacebook(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),tokenFacebook!!,partnerId).enqueue(object : Callback<UserMo> {

            override fun onResponse(call: Call<UserMo>, response: Response<UserMo>) {
                if (response.isSuccessful()) {
                    token=response.body()!!.resultData.access_token
                    d("Token","Local Token Face: "+token)
                    var sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
                    var editor = sp?.edit()
                    editor?.putBoolean("LogIn_State", true)
                    editor?.putString("user_token", response.body()!!.resultData.access_token)
                    editor?.putString("LogIn_Type", "Social")
                    editor?.commit()
                    val intent = Intent(this@LogInWithSkip, Complian::class.java)
                    startActivity(intent)
                    finish()
                }
                else if(response.code()==401){
                    val mAlert = AlertDialog.Builder(this@LogInWithSkip)
                    mAlert.setTitle("พบข้อผิดพลาด")
                    mAlert.setMessage("รหัสผ่านของท่านผิด")
                    mAlert.setNegativeButton("ตกลง"){dialog, which ->
                        dialog.dismiss()
                    }
                    mAlert.show()

                }

            }

            override fun onFailure(call: Call<UserMo>, t: Throwable) {
                d("ss",t.toString())
                val mAlert = AlertDialog.Builder(this@LogInWithSkip)
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


