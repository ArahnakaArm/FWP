package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
data class mapPinmap(var lat : Number,var long : Number)

class MapsActivity : AppCompatActivity(){

    var pinCount : Int?=null
    var mAPIService: ApiServiceLocation? = null
    var latlng : String?=null
    var icon : BitmapDescriptor?=null
    var Pins : LatLng?=null
    private var sharedPreferences:SharedPreferences?=null
    internal var mMapView: MapView?=null
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        try {
            MapsInitializer.initialize(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }



        mMapView =findViewById<View>(R.id.mapView) as MapView
        mMapView?.onCreate(savedInstanceState)

        mMapView?.onResume()

        mMapView?.getMapAsync { mMap ->
            googleMap = mMap

            googleMap!!.uiSettings.isScrollGesturesEnabled =true
            val zoomLevel = 6f
            val center = LatLng(13.7245601, 100.4930241)
            icon = BitmapDescriptorFactory.fromResource(R.drawable.marker)


            mAPIService = ApiUtilsLocation.apiServiceLocation
            sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val partnerId = sharedPreferences!!.getString("partnerId","-")
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()

            mAPIService!!.getLocationPinMap(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<LocationModel>{
                override fun onResponse(call: Call<LocationModel>, response: Response<LocationModel>) {
                    pinCount = response.body()!!.rowCount.toInt()
                    d("TestPin",pinCount.toString())
                    for(i in 0..response.body()!!.rowCount-1) {
                        latlng = response.body()!!.resultData[i].map.lat.toString() + "," + response.body()!!.resultData[i].map.long.toString()
                        var gpsVal = latlng!!.split(",")
                        var lat = java.lang.Double.parseDouble(gpsVal[0])
                        var lon = java.lang.Double.parseDouble(gpsVal[1])
                        Pins=(LatLng(lat,lon))
                        googleMap!!.addMarker(MarkerOptions().position(Pins!!).draggable(true).title(response.body()!!.resultData[i]._id).icon(icon))
                    }

                    googleMap!!.setOnMarkerClickListener {
                        if(it.title!=null) {
                            val intent = Intent(this@MapsActivity, LocationContent::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                            intent.putExtra("ID", it.title)
                            startActivity(intent)
                        }
                        true
                    }
                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))
                    googleMap!!.setOnMapClickListener {
                    }
                    googleMap!!.setOnMapLongClickListener {
                        googleMap!!.addMarker(MarkerOptions()
                                .position(it)
                                .draggable(true))
                    }
                    googleMap!!.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                        override fun onMarkerDragEnd(p0: Marker?) {
                            d("Position",p0!!.position.toString())
                        }

                        override fun onMarkerDragStart(p0: Marker?) {
                            d("Position",p0!!.position.toString())
                        }

                        override fun onMarkerDrag(p0: Marker?) {
                            d("Position",p0!!.position.toString())

                        }
                    })



                }
                override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                    Log.d("TestPin", t.toString())
                }

            })
        }


        maplist2.setOnClickListener {
            onBackPressed()
        }




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

    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()
    }

}