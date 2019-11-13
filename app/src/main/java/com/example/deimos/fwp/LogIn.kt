package com.example.deimos.fwp

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentManager
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

data class UserProfile(var resultData : resultData2,
                       var resultCode : String
        ,var developerMessage:String
)
data class resultData2(var _id :String,var birthDate: String ,var email: String,var firstName : String ,
                       var gender : String,var lastName :String,var image : image)
data class image(var path : String)
data class resultData(val access_token:String)
data class UserMo(val resultData : resultData,
                 val resultCode : String
                 ,val developerMessage:String)

class LogIn :AppCompatActivity() {
    var callback : CallbackManager?=null
    var sp: SharedPreferences? = null
    var loginButton : LoginButton?=null
    var mainActivity : MainActivity= MainActivity()
    var mAPIService: ApiService? = null
    private var token :String?=null
    private var tokenFacebook : String?=null
    private var tokenGoogle : String?=null
    var progressDoalog :ProgressDialog?=null
    private var authCodeGoogle: String? = null
    val TAG = "ServerAuthCodeActivity"
    private var acc : AccessToken?=null

    private val RC_GET_AUTH_CODE = 9003
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuthCodeTextView: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        /////Facebook/////



        callback = CallbackManager.Factory.create()
        loginButton = findViewById(R.id.facebooksignin) as LoginButton
        loginButton!!.setReadPermissions("email")
       // loginButton!!.setFragment(this)
        loginButton!!.registerCallback(callback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                val editor = sp?.edit()

                editor?.putBoolean("LogIn_State", true)
                editor?.commit()
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

        closebuttonlogin.setOnClickListener {
            finish()
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

        mGoogleSignInClient = GoogleSignIn.getClient(this@LogIn, gso)

        googlesignin.setOnClickListener {

            getAuthCodeGoogle()
        }

        /*logoutgoogle.setOnClickListener {
            signOutGoogle();
        }*/

        /////Google/////

        forgotingbutton.setOnClickListener {
            val intent = Intent(this@LogIn,ForgotPassword::class.java)
           startActivity(intent)
        }



        mAPIService = ApiUtils.apiService
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val editor = sp?.edit()

        editor?.commit()
        signinbut.setOnClickListener {
            if ((emailinput.text.trim().toString().isEmpty())||(passwordinput.text.trim().toString().isEmpty())){
                d("Logs","Work!")
                val mAlert = AlertDialog.Builder(this@LogIn)
                mAlert.setTitle("พบข้อผิดพลาด")
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
                            val editor = sp?.edit()
                            editor?.putBoolean("LogIn_State", true)
                            editor?.putString("user_token", response.body()!!.resultData.access_token)
                            editor?.putString("LogIn_Type", "Local")
                            d("AA",sp?.getBoolean("LogIn_State", false).toString())
                            editor?.putBoolean("isFromLogin", true)
                            editor?.commit()

                            finish()


                        }
                        else if(response.code()==401){
                            val mAlert = AlertDialog.Builder(this@LogIn)
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
                        val mAlert = AlertDialog.Builder(this@LogIn)
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
            val intent = Intent(this@LogIn,RegisterHolder::class.java)
            startActivity(intent)
            //replaceFragmentToRightToForgot(Register())
        }


    }



    private fun replaceFragmentToRight(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        val bundle = Bundle()
        val myMessage = token
        bundle.putString("message", myMessage)
        fragment.arguments = bundle
        //fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()


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
         if (requestCode == RC_GET_AUTH_CODE) {
             // [START get_auth_code]
             val task = GoogleSignIn.getSignedInAccountFromIntent(data)
             try {
                 val account = task.getResult(ApiException::class.java)
                 val authCode = account.serverAuthCode
                 Log.d("Test", authCode)
                 authCodeGoogle = authCode
                 sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                 val editor = sp?.edit()
                 editor?.putBoolean("LogIn_State", true)

                 editor?.commit()

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
            Toast.makeText(this@LogIn, message, Toast.LENGTH_LONG).show()
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
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this@LogIn,OnCompleteListener<Void> {

            Toast.makeText(this,"Loged Out",Toast.LENGTH_SHORT).show()
        })
    }
    private fun getLocalTokenGoogle(){
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val editor = sp?.edit()

        mAPIService!!.userLoginGoogle(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),tokenGoogle!!).enqueue(object : Callback<UserMo> {

            override fun onResponse(call: Call<UserMo>, response: Response<UserMo>) {

                    token=response.body()!!.resultData.access_token
                    // replaceFragmentToRight(Profilewithpicture())
                    d("Token","Local Token : "+token)
                    editor?.putBoolean("LogIn_State", true)
                    editor?.putBoolean("LogIn_StateGoogle", true)
                    editor?.putString("user_token", response.body()!!.resultData.access_token)
                    editor?.putString("LogIn_Type", "Social")
                    editor?.putBoolean("isFromLogin", true)
                    editor?.commit()
                         finish()

            }

            override fun onFailure(call: Call<UserMo>, t: Throwable) {
                d("ss",t.toString())
                val mAlert = AlertDialog.Builder(this@LogIn)
                mAlert.setTitle("พบข้อผิดพลาด")
                mAlert.setMessage("ท่านไม่ได้เชื่อมต่ออินเตอร์เน็ต")
                mAlert.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlert.show()
            }
        })
        d("What",sp!!.getBoolean("LogIn_State",false).toString())
    }
    private fun getLocalTokenFacebook(){
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.userLoginFacebook(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),tokenFacebook!!).enqueue(object : Callback<UserMo> {

            override fun onResponse(call: Call<UserMo>, response: Response<UserMo>) {
                if (response.isSuccessful()) {
                    token=response.body()!!.resultData.access_token
                    d("Token","Local Token Face: "+token)
                    var editor = sp?.edit()
                    editor?.putBoolean("LogIn_State", true)
                    editor?.putString("user_token", response.body()!!.resultData.access_token)
                    editor?.putString("LogIn_Type", "Social")
                    editor?.putBoolean("isFromLogin", true)
                    editor?.commit()
                    finish()
                }
                else if(response.code()==401){
                    val mAlert = AlertDialog.Builder(this@LogIn)
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
                val mAlert = AlertDialog.Builder(this@LogIn)
                mAlert.setTitle("พบข้อผิดพลาด")
                mAlert.setMessage("ท่านไม่ได้เชื่อมต่ออินเตอร์เน็ต")
                mAlert.setNegativeButton("ตกลง"){dialog, which ->
                    dialog.dismiss()
                }
                mAlert.show()
            }
        })

    }

    override fun onResume() {
        super.onResume()
    }


}


