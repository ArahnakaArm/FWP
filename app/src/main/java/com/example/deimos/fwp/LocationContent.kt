package com.example.deimos.fwp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils.split
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.favorite.*
import kotlinx.android.synthetic.main.articleinfo.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.locationcontent.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
data class LocationById(var resultCode : String,var developerMessage : String ,var resultData : resultLoca,var rowCount :Int)
data class resultLoca(var locationName : namelocation , var address : addresslocation, var subDistrict : subDistrictlocation , var city : citylocation,
                      var province : provincelocation , var region : regionlocation ,var map : maplocation ,var image : String ,var email : String,var mobileNo :String,var postalCode :String)
data class namelocation(var en : String,var th : String)
data class addresslocation(var en : String,var th : String)
data class  subDistrictlocation(var en: String,var th: String)
data class citylocation(var en : String,var th : String)
data class provincelocation(var en : String,var th : String)
data class regionlocation(var en : String,var th : String)
data class maplocation(var lat: Number , var long : Number)
data class imagelocation(var path : String)


class LocationContent : Activity(){
    internal var mMapView: MapView?=null
    private var sharedPreferences:SharedPreferences?=null
    private var googleMap: GoogleMap? = null
    private var latlng:String?=null
    private var location: LatLng?=null
    private var Name : String?=null
    var mAPIService: ApiServiceLocation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.locationcontent)
        var bundle: Bundle? = intent.extras
        var _id = bundle!!.getString("ID")


        mAPIService = ApiUtilsLocation.apiServiceLocation
        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getLocationById(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),_id!!,partnerId).enqueue(object : Callback<LocationById> {

            override fun onResponse(call: Call<LocationById>, response: Response<LocationById>) {
                d("location",response.body()!!.resultData!!.locationName.toString())
                latlng = response.body()!!.resultData.map.lat.toString()+","+response.body()!!.resultData.map.long.toString()

                Name = response.body()!!.resultData.locationName.th
                var gpsVal = latlng!!.split(",")
                var lat = java.lang.Double.parseDouble(gpsVal[0])
                var lon = java.lang.Double.parseDouble(gpsVal[1])
                location = LatLng(lat, lon)

                locationname.setText(response.body()!!.resultData.locationName.th)
                locationinfo.setText(response.body()!!.resultData.address.th+" "+response.body()!!.resultData.province.th+
                        " "+response.body()!!.resultData.city.th+" "+response.body()!!.resultData.subDistrict.th+" "+response.body()!!.resultData.postalCode)
                telinfo.setText(response.body()!!.resultData.mobileNo)
                emailinfo.setText(response.body()!!.resultData.email)
                var Url = response.body()!!.resultData.image
                Glide.with(this@LocationContent)
                        .load(Url)
                        .into(locationimage)

                mMapView =findViewById<View>(R.id.mapViewContent) as MapView
                mMapView?.onCreate(savedInstanceState)
                mMapView?.onResume()

                try {
                    MapsInitializer.initialize(this@LocationContent!!.applicationContext)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                mMapView?.getMapAsync { mMap ->
                    googleMap = mMap

                    val zoomLevel = 17f

                    val icon = BitmapDescriptorFactory.fromResource(R.drawable.marker)
                    try {

                        googleMap!!.addMarker(MarkerOptions().position(location!!).title(Name!!).icon(icon))
                        googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
                    }catch (e :Exception){
                        d("Ex",e.toString())
                    }

                }



            }


            override fun onFailure(call: Call<LocationById>, t: Throwable) {
                d("arm", t.toString())
            }

        })
















       /*
*/
        backlocation.setOnClickListener {
            onBackPressed()
        }


    }
    override fun onBackPressed()
    {
        super.onBackPressed()
    }
    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }
}