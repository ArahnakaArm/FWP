package com.example.deimos.fwp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.deimos.fwp.R.layout.spinner_item
import kotlinx.android.synthetic.main.complianviewpager.*
import kotlinx.android.synthetic.main.compliantab1.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.register.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Tab1Complian  : androidx.fragment.app.Fragment() {
    var mAPIService: ApiService? = null
    var sp: SharedPreferences? = null
    private var Subject : String?=null
    private var Detail : String?=null
    private var Type : String?=null
    private var closebut : ImageView?=null
    private var Next : Button?=null
    private var viewp : androidx.viewpager.widget.ViewPager?=null

    private var arrayList : ArrayList<String>?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.compliantab1,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        var edditor = sp!!.edit()
        var arrayList = mutableListOf<String>()
        var arrayListGuide = mutableListOf<String>()
        arrayList.add("ประเภท")
        arrayListGuide.add("")
        closebut =activity!!.findViewById(R.id.closebutton)
        Next =activity!!.findViewById(R.id.next)
        viewp = activity!!.findViewById(R.id.viewpager)


        ///Get Type List ///
        subjectInput!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                edditor.putString("Subject",s.toString())
                edditor.commit()


            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
        })

        detailInput!!.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
               edditor.putString("Description",s.toString())
                edditor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })




        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        //token = sp!!.getString("user_token", "-")
        mAPIService!!.getType( Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),"5dbfe99c776a690010deb237").enqueue(object : Callback<CompliansType> {

            override fun onResponse(call: Call<CompliansType>, response: Response<CompliansType>) {
                if (response.isSuccessful()) {
                    try {
                        for (i in 0 until response.body()!!.rowCount) {
                            arrayList!!.add(response.body()!!.resultData[i].typeName)
                            arrayListGuide!!.add(response.body()!!.resultData[i].guideLabel)

                        }
                    }catch (e : Exception){

                    }

                    sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                    var edditor = sp!!.edit()
                    spinner.setSelection(sp!!.getInt("spin",0))

                } else if (response.code() == 401) {
                    val mAlert = AlertDialog.Builder(view!!.context)
                    mAlert.setTitle("พบข้อผิดพลาด")
                    mAlert.setMessage("รหัสผ่านของท่านผิด")
                    mAlert.setNegativeButton("ตกลง") { dialog, which ->
                        dialog.dismiss()
                    }
                    mAlert.show()

                }

            }

            override fun onFailure(call: Call<CompliansType>, t: Throwable) {
                /*  d("ss", t.toString())
                  val mAlert = AlertDialog.Builder(view.context)
                  mAlert.setTitle("พบข้อผิดพลาด")
                  mAlert.setMessage("ท่านไม่ได้เชื่อมต่ออินเตอร์เน็ต")
                  mAlert.setNegativeButton("ตกลง") { dialog, which ->
                      dialog.dismiss()
                  }
                  mAlert.show()*/
            }
        })






        ///Get Type List ///






        val arrayAdapter = ArrayAdapter<String>(context, R.layout.spinner_item, arrayList)
        val spinnerArrayAdapter = object : ArrayAdapter<String>(
                context, R.layout.spinner_item, arrayList) {
            override fun isEnabled(position: Int): Boolean {

                return if (position == 0) {

                    // Disable the first item from Spinner
                    // First item will be use for hint
                    false
                } else {
                    true
                }
            }

            override fun getDropDownView(position: Int, convertView: View?,
                                         parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val tv = view as TextView
                    tv.setTextColor(Color.BLACK)

                    if(arrayListGuide[position] != "" && arrayListGuide[position] != null ){
                        textGuide.text = "คำแนะนำ:"+arrayListGuide[position]
                        textGuide.visibility = View.VISIBLE
                    }else{
                        textGuide.visibility = View.GONE
                    }
                    //  Toast.makeText(context, arrayAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show()
                    Type = arrayAdapter.getItem(position).toString()
                    sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                    var edditor = sp!!.edit()
                    edditor.putInt("spin",position)
                    edditor.commit()

                }else if(position == 0){
                    Type = ""

                }
            }
        }




        closebut!!.setOnClickListener {
            if (subjectInput.text.toString() == "" && Type == "" && detailInput.text.toString() == "" && nameComplian.text.toString() == ""
                    && surnameInputComplian.text.toString() == "" && telcomplian.text.toString() == ""){
                activity!!.finish()
            }else{
                val mAlert = AlertDialog.Builder(requireContext())
                mAlert.setTitle("คุณยังไม่ได้บันทึกการร้องเรียน")
                mAlert.setMessage("คุณต้องการที่จะปิดโดยไม่บันทึกใช่หรือไหม่?")
                mAlert.setNegativeButton("ไม่ใช่") { dialog, which ->
                    dialog.dismiss()

                }
                mAlert.setPositiveButton("ใช่,ปิด") { dialog, which ->
                    dialog.dismiss()
                    sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                    var edditor = sp!!.edit()
                    edditor.putString("Subject","")
                    edditor.putString("Detail","")
                    edditor.putInt("spin",0)
                    edditor.commit()
                    activity!!.finish()
                }
                mAlert.show()
            }
            }



        Next!!.setOnClickListener {
            Subject = subjectInput.text.toString()
            Detail = detailInput.text.toString()
            if(!isValidate(Type,Subject,Detail)){
                val mAlert = AlertDialog.Builder(view!!.context)
                mAlert.setTitle("ลองใหม่อีกครั้ง")
                mAlert.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน")
                mAlert.setNegativeButton("ตกลง") { dialog, which ->
                    dialog.dismiss()
                }
                mAlert.show()

            }else{
                sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                var edditor = sp!!.edit()
                edditor.putString("Subject",Subject)
                edditor.putString("Detail",Detail)
                edditor.putString("Type",Type)
                viewp!!.setCurrentItem(Complian.Pager.getItem(+1,viewp!!))
            }

        }










    }

    override fun onResume() {
        super.onResume()
        if (sp?.getBoolean("LogIn_State", false) == true) {
            linearname.visibility = View.GONE
            linearsurname.visibility = View.GONE
            lineartel.visibility = View.GONE

        }
        d("A","yes")

    }
    private fun isValidate(type : String?,subject : String?, descrip : String?):Boolean{
        if(type == "" || subject == "" || descrip == "") {
            return false
        }
        else{
            return true
        }
    }

}