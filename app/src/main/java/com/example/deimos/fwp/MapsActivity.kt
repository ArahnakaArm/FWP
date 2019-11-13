    package com.example.deimos.fwp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    data class pinMapLocation(var resultCode : String,var developerMessage : String ,var resultData : resultPinmap,var rowCount : Int)
data class resultPinmap(var map : mapPinmap,var locationName : String,var _id : String)
data class mapPinmap(var lat : Number,var long : Number)

class MapsActivity : androidx.fragment.app.Fragment(){
    var yourMarkerTag = MarkerModel()
    var yourMarkerTag2 = MarkerModel()
    var pinCount : Int?=null
    var mAPIService: ApiService? = null
    var pinId : String?=null
    var latlng : String?=null
    var icon : BitmapDescriptor?=null
    var Pins : LatLng?=null
    internal var mMapView: MapView?=null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_map, container, false)

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }



        mMapView = rootView.findViewById<View>(R.id.mapView) as MapView
        mMapView?.onCreate(savedInstanceState)

        mMapView?.onResume()

        mMapView?.getMapAsync { mMap ->
            googleMap = mMap

            googleMap!!.uiSettings.isScrollGesturesEnabled =true
            val zoomLevel = 6f
            val center = LatLng(13.7245601, 100.4930241)
            icon = BitmapDescriptorFactory.fromResource(R.drawable.marker)


        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
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
                        val intent = Intent(requireContext(), LocationContent::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        intent.putExtra("ID", it.title)
                        startActivity(intent)
                        //  Toast.makeText(requireContext(),it.title,Toast.LENGTH_SHORT).show()
                    }

                    true
                }
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))
                googleMap!!.setOnMapClickListener {
                   // Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                }

                googleMap!!.setOnMapLongClickListener {
                    googleMap!!.addMarker(MarkerOptions()
                            .position(it)
                            .draggable(true))
                }


                googleMap!!.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                    override fun onMarkerDragEnd(p0: Marker?) {
                        d("Position",p0!!.position.toString())
                       /* if (p0 != null) {
                            val id: String = p0.tag as String

                        }*/
                       // Toast.makeText(context,p0!!.position.toString(),Toast.LENGTH_SHORT)
                    }

                    override fun onMarkerDragStart(p0: Marker?) {
                        d("Position",p0!!.position.toString())
                       /* if (p0 != null) {
                            val id: String = p0.tag as String

                        }*/
                      //  Toast.makeText(context,p0!!.position.toString(),Toast.LENGTH_SHORT)
                    }

                    override fun onMarkerDrag(p0: Marker?) {
                        d("Position",p0!!.position.toString())
                      /*  if (p0 != null) {
                            val id: String = p0.tag as String

                        }*/
                     //   Toast.makeText(context,p0!!.position.toString(),Toast.LENGTH_SHORT)
                    }
                })



            }
            override fun onFailure(call: Call<LocationModel>, t: Throwable) {
                Log.d("TestPin", t.toString())
            }

        })
        }







        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maplist.setOnClickListener {
            replaceFragment(LocationList())
        }
    }


    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)
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

    fun onMarkerDrag(marker: Marker) {
       // tvLocInfo.setText("Marker " + marker.id + " Drag@" + marker.position)
    }

    fun onMarkerDragEnd(marker: Marker) {
     //  Toast.makeText(context,"asdasd",Toast.LENGTH_SHORT)
    }
    fun onMapLongClick(point: LatLng) {

        googleMap!!.addMarker(MarkerOptions()
                .position(point)
                .draggable(true))

      //  markerClicked = false
    }

}