package com.example.deimos.fwp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.galleryinfo.*
import kotlinx.android.synthetic.main.homefragment.*
import kotlinx.android.synthetic.main.locationcontent.*

class LocationContent : Activity(){
    internal var mMapView: MapView?=null
    private var googleMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.locationcontent)
        var bundle: Bundle? = intent.extras
        var Name = bundle!!.getString("Name")
        var Info = bundle!!.getString("Info")
        var Tel = bundle!!.getString("Tel")
        var Email = bundle!!.getString("Email")

        locationname.setText(Name)
        locationinfo.setText(Info)
        telinfo.setText(Tel)
        emailinfo.setText(Email)

        backlocation.setOnClickListener {
            onBackPressed()
        }

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
            /*LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
            val zoomLevel = 17f

            val center = LatLng(13.7245601, 100.4930241)
            val bankok = LatLng(13.748106, 100.5650711)
            val thonburi = LatLng(13.7060328, 100.3491375)
            val cheangmai = LatLng(18.8831375, 98.8991867)
            val samhud = LatLng(13.5843948, 100.7934578)
            val chonburi = LatLng(13.0884603, 100.9145203)
            val nakornsawhun = LatLng(15.7077635, 100.131218)
            val phuket = LatLng(7.9070511, 98.4014075)
            val yala = LatLng(6.550913, 101.2844168)
            val ubon = LatLng(15.1961968, 104.8437812)
            val salaburi = LatLng(14.6087801, 100.9783937)
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.marker)

            var mcenter: Marker? = null
            var mbankok: Marker? = null
            var mthonburi: Marker? = null


            googleMap!!.addMarker(MarkerOptions().position(center).title("สาขากรุงเทพ").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(thonburi).title("สาขาฝั่งธนบุรี").icon(icon))
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))


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