package com.example.deimos.fwp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class RegionModel(var resultCode: String,var developerMessage: String,var resultData: ArrayList<resultRegion>)
data class resultRegion(var region : regionName,var priority : Int)
data class regionName(var th: String,var en: String)
data class LocationModel(var resultCode : String,var developerMessage : String,var resultData : ArrayList<result>,var rowCount : Int)
data class result(var locationName : locationame,var address : address,var map : map,var region : region,var image: String,var email : String,var _id : String)
data class imageinfo(var path : String)
data class locationame(var en : String,var th : String)
data class address(var en : String ,var th : String)
data class map(var lat: Number , var long : Number)
data class region(var en : String, var th : String)
data class regionsModel(var resultCode: String,var rowCount: Int,var resultData: ArrayList<resultRegions>)
data class resultRegions(var region: regions)
data class regions(var th: String,var en: String)
class LocationList : androidx.fragment.app.Fragment() {
    private var sharedPreferences : SharedPreferences?=null
    private var mAPIService: ApiServiceLocation? = null
   private var test = ArrayList<result>()
    private var regions = ArrayList<String>()
    private var test2 = ArrayList<result>()
    private var list = ArrayList<Company>()
    var dataLocation = HashMap<String,result>()
    var dataLocation2 = HashMap<String,String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(3).isCheckable=true
        view.menu.getItem(3).isChecked=true

        return inflater.inflate(R.layout.loacationlist,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)


        dataLocation2.put("sads","sadsad")
        dataLocation2.put("sads","Sadsasssssd")
        d("HASH",dataLocation2["sads"].toString())

        //getLocationAndRegion("")
        //getRegion("")
        map.setOnClickListener {
            startActivity(Intent(requireContext(),MapsActivity::class.java))
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }

      //  getLocationAndRegion()
       // getLocation()

     searchlocation.textChanges().debounce(300, TimeUnit.MILLISECONDS).subscribe({ it ->
            try {
                d("SEARCH", it.toString())
                activity!!.runOnUiThread(java.lang.Runnable {
                    regions.clear()
                    getRegion(it.toString())
                })


            }catch (e : Exception){
                d("ErrorKK",e.toString())

            }
        },{ throwable -> d("ErrorKK",throwable.message)})
    }

    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(3).isCheckable=true
        view.menu.getItem(3).isChecked=true
        super.onResume()
    }
    private fun getLocation(){
        mAPIService = ApiUtilsLocation.apiServiceLocation
        val companies = ArrayList<Company>()
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getLocation(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<LocationModel> {

            override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {

                ///        d("Location",response.body()!!.resultData[0]!!.locationName.th)
                //      d("Location",response.body()!!.resultData[1]!!.locationName.th.toString())
               /* try {
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

*/
               // mProgressDialog.dismiss();
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
        mAPIService = ApiUtilsLocation.apiServiceLocation
        val companies = ArrayList<Company>()
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getSearchLocation(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,text!!).enqueue(object : Callback<LocationModel> {

            override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {

                ///        d("Location",response.body()!!.resultData[0]!!.locationName.th)
                //      d("Location",response.body()!!.resultData[1]!!.locationName.th.toString())
             /*   try {
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

                }*/

            }

            override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                d("arm",t.toString())
            }

        })
    }
    private fun getLocationAndRegion(text : String,regions: ArrayList<String>){
        mAPIService = ApiUtilsLocation.apiServiceLocation
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val companies = ArrayList<Company>()
        var dataLocation = HashMap<String,ArrayList<Product>>()
        val location = ArrayList<ArrayList<Product>>()
        val r = (10..12).shuffled().first()
        /*val mProgressDialog = ProgressDialog(requireContext())
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setCancelable(false)
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()*/
        mAPIService!!.getSearchLocation(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId,text!!).enqueue(object : Callback<LocationModel> {

            override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {

//                Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image.path, "0978512369",  response.body()!!.resultData[i]!!.email,
//                        response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id

            try {
                for (i in 0 until response.body()!!.rowCount) {
                        var regionName = response.body()!!.resultData[i].region.th
                  if(dataLocation[regionName] == null){
//                        dataLocation.put(response.body()!!.resultData[i].region.th, ArrayList<Product>())
                      dataLocation.put(regionName, ArrayList<Product>())
                    }
                    dataLocation[regionName]!!.add(Product(response.body()!!.resultData[i]!!.locationName.th, response.body()!!.resultData[i]!!.image, "0978512369",  response.body()!!.resultData[i]!!.email,
                            response.body()!!.resultData[i]!!.map.lat.toString(),response.body()!!.resultData[i]!!.map.long.toString() ,response.body()!!.resultData[i]!!._id))
//                    dataLocation.put(response.body()!!.resultData[i].region.th, response.body()!!.resultData[i])

                }
                var regionName = ArrayList<String>(dataLocation.keys)
               // var locationItem = ArrayList<result>(dataLocation.values)

                for (i in 0 until regions.size) {
//                    var item = ArrayList<Product>()
//                    for(j in 0 until response.body()!!.rowCount) {
//                        var data = response.body()!!.resultData[j]
//                        when(data.region.th) {
//                            regionName[i] -> item.add(Product(data.locationName.th, data.address.th, "0", data.email, data.map.lat.toString(), data.map.long.toString(), data._id))
//                        }
//                        d("JJJ",j.toString())
//                    }
//
//                    companies.add(Company("" + regionName[i], item))
                    if (dataLocation[regions[i]] != null) {
                        companies.add(Company("" + regions[i], dataLocation[regions[i]]!!.toList()))
                    }
                   // d("Size",item.size.toString())



                }






                updateUi(companies)

            }catch (e : Exception){
                d("DDDA",e.toString())

            }
              //  mProgressDialog.dismiss()


            }

            override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                d("arm",t.toString())
               // mProgressDialog.dismiss()
            }

        })
    }

    private fun getRegion(text : String){
        mAPIService = ApiUtilsLocation.apiServiceLocation
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getSearchRegion(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<regionsModel> {

            override fun onResponse(call: Call<regionsModel>, response: Response<regionsModel>) {
                for(i in 0 until response.body()!!.rowCount){
                    regions.add(response.body()!!.resultData[i].region.th)
                    d("Regions",regions[i])
                    when(i){
                        response.body()!!.rowCount -1 ->  getLocationAndRegion(text,regions)
                    }
                }

            }

            override fun onFailure(call: Call<regionsModel>, t: Throwable) {
                d("arm",t.toString())
            }

        })
    }

}
