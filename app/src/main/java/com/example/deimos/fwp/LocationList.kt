package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Log.d
import android.view.*
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.android.synthetic.main.favorite.*

import kotlinx.android.synthetic.main.loacationlist.*
import kotlinx.android.synthetic.main.searchnews.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class LocationModel(var resultCode : String,var developerMessage : String,var resultData : ArrayList<result>,var rowCount : Int)
data class result(var locationName : locationame,var address : address,var map : map,var region : region,var image: imageinfo,var email : String,var _id : String)
data class imageinfo(var path : String)
data class locationame(var en : String,var th : String)
data class address(var en : String ,var th : String)
data class map(var lat: Number , var long : Number)
data class region(var en : String, var th : String)
class LocationList : androidx.fragment.app.Fragment() {
    private var etsearch: EditText? = null
    internal var textlength = 0
    private var adapter2: LocationChildAdapter? = null
    private val companyViewHolder: CompanyViewHolder? = null
    var mAPIService: ApiService? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(0).isCheckable=true
        view.menu.getItem(0).isChecked=true

        return inflater.inflate(R.layout.loacationlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        map.setOnClickListener {
            startActivity(Intent(requireContext(),MapsActivity::class.java))
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        getLocation()

        searchlocation.textChanges().debounce(300, TimeUnit.MILLISECONDS).subscribe({ it ->
            try {
                d("SEARCH", it.toString())
                searchingLocation(it.toString())
            }catch (e : Exception){

            }
        },{ throwable -> d("Error",throwable.message)})
    }

    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(0).isCheckable=true
        view.menu.getItem(0).isChecked=true
        super.onResume()
    }
    private fun getLocation(){
        mAPIService = ApiUtils.apiService
        val companies = ArrayList<Company>()
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val northList= ArrayList<Product>()
        val centralList = ArrayList<Product>()
        val westList = ArrayList<Product>()
        val eastList = ArrayList<Product>()
        val eastnorthList = ArrayList<Product>()
        val southList = ArrayList<Product>()
        val bankkok = ArrayList<Product>()
        val mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()
        mAPIService!!.getLocation(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<LocationModel> {

            override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {

                ///        d("Location",response.body()!!.resultData[0]!!.locationName.th)
                //      d("Location",response.body()!!.resultData[1]!!.locationName.th.toString())
                try {
                    for (i in 0..response.body()!!.rowCount-1) {

                        when(response.body()!!.resultData[i]!!.region.th){
                            "ภาคเหนือ" -> {
                                var lt : String = response.body()!!.resultData[i].map.lat.toString()
                                var ln  : String = response.body()!!.resultData[i].map.long.toString()
                                northList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369", response.body()!!.resultData[i]!!.email,
                                        lt,ln,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคกลาง" -> {
                                centralList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                            }
                            "ภาคใต้" -> {
                                southList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคตะวันตก" -> {
                                westList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคตะวันออก" -> {
                                eastList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคตะวันออกเฉียงเหนือ" -> {
                                eastnorthList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "กรุงเทพมหานคร" -> {
                                bankkok.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                        }

                        /*
                        if(response.body()!!.resultData[i]!!.region.th=="ภาคเหนือ") {
                            var lt : String = response.body()!!.resultData[i].map.lat.toString()
                            var ln  : String = response.body()!!.resultData[i].map.long.toString()
                            northList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369", response.body()!!.resultData[i]!!.email,
                                    lt,ln,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคกลาง") {
                            centralList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคใต้") {
                            southList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันตก") {
                            westList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันออก") {
                            eastList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันออกเฉียงเหนือ") {
                            eastnorthList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="กรุงเทพมหานคร") {
                            bankkok.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                        }
                        */
                    }
                    val Bankok = Company("" +
                            "กรุงเทพมหานคร", bankkok)
                    companies.add(Bankok)
                    val North = Company("" +
                            "ภาคเหนือ", northList)
                    companies.add(North)
                    val Central = Company("" +
                            "ภาคกลาง", centralList)
                    companies.add(Central)
                    val South = Company("" +
                            "ภาคใต้", southList)
                    companies.add(South)
                    val  West= Company("" +
                            "ภาคตะวันตก", westList)
                    companies.add(West)

                    val East = Company("" +
                            "ภาคตะวันออก", eastList)
                    companies.add(East)

                    val EastNorth = Company("" +
                            "ภาคตะวันออกเฉียงเหนือ", eastnorthList)
                    companies.add(EastNorth)

                    /*var adapter = ProductAdapter(companies)
                     /* for (i in adapter.groups.size - 1 downTo 0) {
                          adapter.toggleGroup(i)
                      }*/
                      recyclerView.adapter = adapter
                  */

                    updateUi(companies)
                }catch (e : Exception){

                }


                mProgressDialog.dismiss();
            }

            override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                d("arm",t.toString())
            }

        })

    }
    private fun updateUi(data :ArrayList<Company>){
        recyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
            adapter = ProductAdapter(data)
        }
    }
    private fun searchingLocation(text : String){
        mAPIService = ApiUtils.apiService
        val companies = ArrayList<Company>()
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        val northList= ArrayList<Product>()
        val centralList = ArrayList<Product>()
        val westList = ArrayList<Product>()
        val eastList = ArrayList<Product>()
        val eastnorthList = ArrayList<Product>()
        val southList = ArrayList<Product>()
        val bankkok = ArrayList<Product>()
        mAPIService!!.getSearchLocation(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,text!!).enqueue(object : Callback<LocationModel> {

            override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {

                ///        d("Location",response.body()!!.resultData[0]!!.locationName.th)
                //      d("Location",response.body()!!.resultData[1]!!.locationName.th.toString())
                try {
                    for (i in 0..response.body()!!.rowCount-1) {

                        when(response.body()!!.resultData[i]!!.region.th){
                            "ภาคเหนือ" -> {
                                var lt : String = response.body()!!.resultData[i].map.lat.toString()
                                var ln  : String = response.body()!!.resultData[i].map.long.toString()
                                northList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369", response.body()!!.resultData[i]!!.email,
                                        lt,ln,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคกลาง" -> {
                                centralList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                            }
                            "ภาคใต้" -> {
                                southList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคตะวันตก" -> {
                                westList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคตะวันออก" -> {
                                eastList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "ภาคตะวันออกเฉียงเหนือ" -> {
                                eastnorthList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                            "กรุงเทพมหานคร" -> {
                                bankkok.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                            }
                        }

                        /*
                        if(response.body()!!.resultData[i]!!.region.th=="ภาคเหนือ") {
                            var lt : String = response.body()!!.resultData[i].map.lat.toString()
                            var ln  : String = response.body()!!.resultData[i].map.long.toString()
                            northList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369", response.body()!!.resultData[i]!!.email,
                                    lt,ln,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคกลาง") {
                            centralList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคใต้") {
                            southList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันตก") {
                            westList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันออก") {
                            eastList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="ภาคตะวันออกเฉียงเหนือ") {
                            eastnorthList.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))

                        }
                        else if(response.body()!!.resultData[i]!!.region.th=="กรุงเทพมหานคร") {
                            bankkok.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
                                    response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
                        }
                        */
                    }
                    when(bankkok.size) {
                         0 ->{ }

                          else ->{
                              val Bankok = Company("" +
                                  "กรุงเทพมหานคร", bankkok)
                              companies.add(Bankok)
                          }

                    }
                    when(northList.size) {
                        0 ->{ }

                        else ->{
                            val North = Company("" +
                                    "ภาคเหนือ", northList)
                            companies.add(North)
                        }

                    }
                    when(centralList.size) {
                        0 ->{ }

                        else ->{
                            val Central = Company("" +
                                    "ภาคกลาง", centralList)
                            companies.add(Central)
                        }

                    }
                    when(southList.size) {
                        0 ->{ }

                        else ->{
                            val South = Company("" +
                                    "ภาคใต้", southList)
                            companies.add(South)
                        }

                    }
                    when(westList.size) {
                        0 ->{ }

                        else ->{
                            val  West= Company("" +
                                    "ภาคตะวันตก", westList)
                            companies.add(West)
                        }

                    }
                    when(eastList.size) {
                        0 ->{ }

                        else ->{
                            val East = Company("" +
                                    "ภาคตะวันออก", eastList)
                            companies.add(East)

                        }

                    }
                    when(eastnorthList.size) {
                        0 ->{ }

                        else ->{

                            val EastNorth = Company("" +
                                    "ภาคตะวันออกเฉียงเหนือ", eastnorthList)
                            companies.add(EastNorth)

                        }

                    }



                    /*var adapter = ProductAdapter(companies)
                     /* for (i in adapter.groups.size - 1 downTo 0) {
                          adapter.toggleGroup(i)
                      }*/
                      recyclerView.adapter = adapter
                  */

                    updateUi(companies)
                }catch (e : Exception){

                }

            }

            override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                d("arm",t.toString())
            }

        })
    }
}
