package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.favorite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

data class Bookmark(val title : String,val date : String)
class FavoriteList : AppCompatActivity() {
    private var sp : SharedPreferences?=null
    private var token : String?=null
    var mAPIService : ApiService?=null
    var favList = ArrayList<resultDataFav>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favorite)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        getBookMarkList()

        backprees.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }

    private fun getBookMarkList(){
        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val mProgressDialog = ProgressDialog(this@FavoriteList)
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setCancelable(false)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()
        mAPIService!!.getFavorite(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<Favorite> {

            override fun onResponse(call: Call<Favorite>, response: Response<Favorite>) {
                try {
                    showData(response.body()!!.resultData)
                    favList=response.body()!!.resultData
                }catch (e : Exception){

                }
                mProgressDialog.dismiss();
            }
            override fun onFailure(call: Call<Favorite>, t: Throwable) {
                d("arm","onFailure")
            }

        })
    }
    private fun showData(data : ArrayList<resultDataFav>){
        list_recycler_view.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@FavoriteList)
            adapter = BookMarkAdapter(context,data)
        }

    }

    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()

    }

    override fun onResume() {
        getBookMarkList()
        super.onResume()
    }

}