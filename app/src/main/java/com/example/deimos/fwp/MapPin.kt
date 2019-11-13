package com.example.deimos.fwp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient;
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
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.text.method.TextKeyListener.clear
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.mappin.*


class MapPin : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    var yourMarkerTag = MarkerModel()
    private var latitudeTextView: TextView? = null
    private var longitudeTextView: TextView? = null
    private var mylocation: Location? = null
    var yourMarkerTag2 = MarkerModel()
    private var googleApiClient: GoogleApiClient? = null
    var pinCount : Int?=null
    var sp: SharedPreferences? = null
    var mAPIService: ApiService? = null
    var pinId : String?=null
    var latlng : String?=null
    private var longitude: Double?=null
    private var latitude: Double?=null
    var icon : BitmapDescriptor?=null
    var Pins : LatLng?=null
    internal var mMapView: MapView?=null
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mappin)
        setUpGClient()
        /*confirm.isEnabled = false
        confirm.alpha = 0.5.toFloat()*/
        cancel.setOnClickListener {
            sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val editor = sp?.edit()
           editor?.putString("LAT","-")
           editor?.putString("LONG","-")
            editor?.commit()
            this.finish()
        }
        confirm.setOnClickListener {
            this.finish()
        }
        try {
            MapsInitializer.initialize(this@MapPin.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView = findViewById<View>(R.id.mapView2) as MapView
        mMapView?.onCreate(savedInstanceState)
        mMapView?.onResume()
        mMapView?.getMapAsync { mMap ->
            googleMap = mMap
            googleMap!!.uiSettings.isScrollGesturesEnabled =true
            val zoomLevel = 6f
            val center = LatLng(13.7245601, 100.4930241)
            googleMap!!.setOnMarkerClickListener {
                        val intent = Intent(this@MapPin,LocationContent::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        intent.putExtra("ID",it.title)
                        startActivity(intent)
                        //  Toast.makeText(requireContext(),it.title,Toast.LENGTH_SHORT).show()
                        true
                    }
                    googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))
                    googleMap!!.setOnMapClickListener {
                       // Toast.makeText(this@MapPin,it.toString(), Toast.LENGTH_SHORT).show()
                    }

                    googleMap!!.setOnMapLongClickListener {
                       /* googleMap!!.addMarker(MarkerOptions()
                                .position(it)
                                .draggable(true))*/
                    }


                    googleMap!!.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                        override fun onMarkerDragEnd(p0: Marker?) {

                            latitude = p0!!.position.latitude
                            longitude = p0!!.position.longitude
                            Log.d("Position", latitude.toString())
                            Log.d("Position", longitude.toString())

                            sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                            val editor = sp?.edit()
                            editor!!.putString("LAT",latitude.toString())
                            editor!!.putString("LONG",longitude.toString())
                            editor?.commit()
                            /* if (p0 != null) {
                                 val id: String = p0.tag as String

                             }*/
                            ///Toast.makeText(this@MapPin,p0!!.position.toString(), Toast.LENGTH_SHORT)
                        }

                        override fun onMarkerDragStart(p0: Marker?) {
                            confirm.isEnabled = true
                            confirm.alpha = 1.toFloat()
                            //Log.d("Position", p0!!.position.toString())
                            /* if (p0 != null) {
                                 val id: String = p0.tag as String

                             }*/
                            //Toast.makeText(this@MapPin,p0!!.position.toString(), Toast.LENGTH_SHORT)
                        }

                        override fun onMarkerDrag(p0: Marker?) {
                           // Log.d("Position", p0!!.position.toString())
                            /*  if (p0 != null) {
                                  val id: String = p0.tag as String

                              }*/
                           // Toast.makeText(this@MapPin,p0!!.position.toString(), Toast.LENGTH_SHORT)
                        }
                    })




        }

}


    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = supportFragmentManager?.beginTransaction()
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
       // Toast.makeText(this@MapPin,"asdasd", Toast.LENGTH_SHORT)
    }
    fun onMapLongClick(point: LatLng) {

      /*  googleMap!!.addMarker(MarkerOptions()
                .position(point)
                .draggable(true))
*/
        //  markerClicked = false
    }

    private fun getCurrentLocation() {
        googleMap!!.clear()

    }
    private fun moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        val latLng = LatLng(latitude!!, longitude!!)
        googleMap!!.addMarker(MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Marker in India"))

        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        googleMap!!.getUiSettings().setZoomControlsEnabled(true)

        googleApiClient!!.disconnect()
    }
    @Synchronized
    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        //googleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location) {
        mylocation = location
        if (mylocation != null) {
            latitude = mylocation!!.latitude
            longitude = mylocation!!.longitude
            moveMap()

        }
    }

    override fun onConnected(bundle: Bundle?) {
        checkPermissions()
    }

    override fun onConnectionSuspended(i: Int) {
        //Do whatever you need
        //You can display a message here
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        //You can display a message here
    }

    private fun getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient!!.isConnected) {
                val permissionLocation = ContextCompat.checkSelfPermission(this@MapPin,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    val locationRequest = LocationRequest()
                    locationRequest.interval = 100
                    locationRequest.fastestInterval = 3000
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    val builder = LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest)
                    builder.setAlwaysShow(true)
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this)
                    val result = LocationServices.SettingsApi
                            .checkLocationSettings(googleApiClient, builder.build())
                    result.setResultCallback { result ->
                        val status = result.status
                        when (status.statusCode) {
                            LocationSettingsStatusCodes.SUCCESS -> {
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                val permissionLocation = ContextCompat
                                        .checkSelfPermission(this@MapPin,
                                                Manifest.permission.ACCESS_FINE_LOCATION)
                                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                    mylocation = LocationServices.FusedLocationApi
                                            .getLastLocation(googleApiClient)
                                }
                            }
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(this@MapPin,
                                            REQUEST_CHECK_SETTINGS_GPS)
                                } catch (e: IntentSender.SendIntentException) {
                                    // Ignore the error.
                                }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            }
                        }// Location settings are not satisfied.
                        // However, we have no way
                        // to fix the
                        // settings so we won't show the dialog.
                        // finish();
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS_GPS -> when (resultCode) {
                Activity.RESULT_OK -> getMyLocation()
                Activity.RESULT_CANCELED -> finish()
            }
        }
    }

    private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(this@MapPin,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            }
        } else {
            getMyLocation()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val permissionLocation = ContextCompat.checkSelfPermission(this@MapPin,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation()
        }
    }

    companion object {
        private val REQUEST_CHECK_SETTINGS_GPS = 0x1
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2
    }
}