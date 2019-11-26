package com.example.deimos.fwp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.favorite.*
import kotlinx.android.synthetic.main.complainlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

data class ComplianModel(var resultCode : String,var developerMessage: String,var resultData : ArrayList<CompliansData>,var rowCount : Int)
data class CompliansData(var map : complianMap,var _id : String,var status : String, var subject : String ,var complainType : String,
                         var complainDesc : String,var updatedAt : String , var complainNumber : String)
data class complianMap(var lat :Number,var long : Number)
class ComplainList : AppCompatActivity() {
    var sp: SharedPreferences? = null
    var token : String?=null
    var user_ID : String?=null
    var mAPIService: ApiService? = null
    var arrayComplian = ArrayList<CompliansData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.complainlist)
        mAPIService = ApiUtils.apiService
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        user_ID = sp!!.getString("user_id","-")
        var mockcomplains = ArrayList<ComplainModel>()
        mockcomplains.add(ComplainModel("Complain 1", "17/8/2019", "Complete"))
        mockcomplains.add(ComplainModel("Complain 2", "1/8/2019", "In Progress"))
        mockcomplains.add(ComplainModel("Complain 3", "27/8/2019", "Complete"))
        mockcomplains.add(ComplainModel("Complain 4", "7/8/2019", "In Progress"))
        Log.d("Complain", token)
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()

        mAPIService!!.getComplianList(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback <ComplianModel> {

            override fun onResponse(call: Call <ComplianModel>, response: Response <ComplianModel>) {


                try {
                    for(i in 0 until response.body()!!.rowCount) {
                        arrayComplian.add(response.body()!!.resultData[i])

                        }
                    arrayComplian.sortBy { it.updatedAt }
                    arrayComplian.reverse()
                    list_recycler_view_complain.apply {
                        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ComplainList)
                        adapter = ComplainAdapter(context,arrayComplian)

                    }

                }catch (e:Exception){

                    d("Ex",e.toString())
                }
            }
            override fun onFailure(call: Call <ComplianModel>, t: Throwable) {
                Log.d("Complain", t.toString())
            }

        })


        searchcomplain.textChanges().debounce(300, TimeUnit.MILLISECONDS).subscribe({ it ->
            try {
                arrayComplian.clear()
                d("SEARCH", it.toString())
                searchComplianList(it.toString())
            }catch (e : Exception){

            }
        },{ throwable -> d("Error",throwable.message)})

        backpreescomplain.setOnClickListener {
            onBackPressed()
        }

    }


    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()
    }
    private fun searchComplianList(text : String){
        mAPIService = ApiUtils.apiService
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        user_ID = sp!!.getString("user_id","-")
        val partnerId = "5dbfe99c776a690010deb237"
        //Log.d("arm", usr.state.toString())
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getSearchComplianList(token!!,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,text).enqueue(object : Callback <ComplianModel> {

            override fun onResponse(call: Call <ComplianModel>, response: Response <ComplianModel>) {


                try {
                    for(i in 0 until response.body()!!.rowCount) {

                        arrayComplian.add(response.body()!!.resultData[i])

                    }
                    arrayComplian.sortBy { it.updatedAt }
                    arrayComplian.reverse()
                    list_recycler_view_complain.apply {
                        layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ComplainList)
                        adapter = ComplainAdapter(context,arrayComplian)

                    }

                }catch (e:Exception){

                    d("Ex",e.toString())
                }
            }
            override fun onFailure(call: Call <ComplianModel>, t: Throwable) {
                Log.d("Complain", t.toString())
            }

        })

    }

}