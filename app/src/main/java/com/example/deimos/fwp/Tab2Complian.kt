package com.example.deimos.fwp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings


import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat

import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.complianviewpager.*
import kotlinx.android.synthetic.main.compliantab2.*
import kotlinx.android.synthetic.main.mappin.*
import java.util.ArrayList

class Tab2Complian  : androidx.fragment.app.Fragment(),GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
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
    val PERMISSION_ID = 42
    var myFragment : androidx.fragment.app.Fragment?=null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var Pins : LatLng?=null
    internal var mMapView2: MapView?=null
    private var googleMap2: GoogleMap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.compliantab2,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val editor = sp?.edit()
        editor!!.putBoolean("Mark",false)
        editor!!.commit()


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastLocation()

        try {
            MapsInitializer.initialize(requireContext().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView2 = view.findViewById<View>(R.id.mapViewComplian) as MapView
        mMapView2?.onCreate(savedInstanceState)
        mMapView2?.onResume()
        mMapView2?.getMapAsync { mMap ->
            googleMap2 = mMap
            googleMap2!!.uiSettings.isScrollGesturesEnabled =true
            val zoomLevel = 6f
            val center = LatLng(13.7245601, 100.4930241)
            googleMap2!!.setOnMarkerClickListener {
                if(it.title != null) {
                    val intent = Intent(requireContext(), LocationContent::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                    intent.putExtra("ID", it.title)
                    startActivity(intent)
                }


                //  Toast.makeText(requireContext(),it.title,Toast.LENGTH_SHORT).show()
                true
            }
            googleMap2!!.setOnCameraIdleListener {
                d("Maps",googleMap2!!.cameraPosition.target.latitude.toString())
                d("Maps",googleMap2!!.cameraPosition.target.longitude.toString())
                sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                val editor = sp?.edit()
                editor!!.putString("LAT",googleMap2!!.cameraPosition.target.latitude.toString())
                editor!!.putString("LONG",googleMap2!!.cameraPosition.target.longitude.toString())
            }
            googleMap2!!.setOnCameraMoveListener {
                googleMap2!!.clear()
                // display imageView

            }


        }
    }
    override fun onResume() {
        super.onResume()
        mMapView2?.onResume()
        getLastLocation()
    }



    override fun onDestroy() {
        super.onDestroy()
        mMapView2?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView2?.onLowMemory()
    }



    private fun getCurrentLocation() {
        googleMap2!!.clear()

    }
    private fun moveMap(lat : Double,long : Double) {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        try {
            val latLng = LatLng(lat!!, long!!)
            googleMap2!!.addMarker(MarkerOptions()
                    .position(latLng)
                    .draggable(true)
            )

            googleMap2!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap2!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
            googleMap2!!.getUiSettings().setZoomControlsEnabled(true)
            sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val editor = sp?.edit()
            editor!!.putString("LAT",lat.toString())
            editor!!.putString("LONG",long.toString())
            editor?.commit()

          //  googleApiClient!!.disconnect()
        }catch (e : Exception){}

    }
  /*  @Synchronized
    private fun setUpGClient() {
        try {
            googleApiClient = GoogleApiClient.Builder(requireContext())
                    .enableAutoManage(requireActivity(), 0, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        }catch (e : java.lang.Exception){

        }

        //googleApiClient!!.connect()
    }
*/


    override fun onLocationChanged(location: Location) {
       mylocation = location
        if (mylocation != null) {
            latitude = mylocation!!.latitude
            longitude = mylocation!!.longitude
            moveMap(latitude!!,longitude!!)

        }
    }

    override fun onConnected(bundle: Bundle?) {
     //   checkPermissions()
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
                val permissionLocation = ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    val locationRequest = LocationRequest()
                    locationRequest.interval = 1
                    locationRequest.fastestInterval = 1
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
                                        .checkSelfPermission(requireContext(),
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
                                    status.startResolutionForResult(requireActivity(),
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
                Activity.RESULT_CANCELED -> activity!!.finish()
            }
        }
    }

   /* private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(requireActivity(),
                        listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            }
        } else {
            getMyLocation()
        }

    }*/


    companion object {
        private val REQUEST_CHECK_SETTINGS_GPS = 0x1
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                        moveMap(latitude!!,longitude!!)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
            moveMap(latitude!!,longitude!!)

        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


}