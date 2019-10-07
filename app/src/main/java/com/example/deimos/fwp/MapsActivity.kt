package com.example.deimos.fwp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapsActivity : Fragment() {
    internal var mMapView: MapView?=null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.activity_map, container, false)
        mMapView = rootView.findViewById<View>(R.id.mapView) as MapView
        mMapView?.onCreate(savedInstanceState)

        mMapView?.onResume()

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
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
            val zoomLevel = 6f
            // Add a marker in Sydney, Australia, and move the camera.
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
            googleMap!!.addMarker(MarkerOptions().position(bankok).title("สาขากรุงเทพ").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(thonburi).title("สาขาฝั่งธนบุรี").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(cheangmai).title("สาขาเชียงใหม่").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(samhud).title("สาขาสมุทรปราการ").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(chonburi).title("สาขาชลบุรี").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(nakornsawhun).title("สาขานครสวรรค์").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(phuket).title("สาขาภูเก็ต").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(yala).title("สาขายะลา").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(ubon).title("สาขาอุบลราชธานี").icon(icon))
            googleMap!!.addMarker(MarkerOptions().position(salaburi).title("สาขาสระบุรี").icon(icon))
            googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(center, zoomLevel))
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maplist.setOnClickListener {
            replaceFragment(LocationList())
        }
    }


    private fun replaceFragment(fragment: Fragment){
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

}