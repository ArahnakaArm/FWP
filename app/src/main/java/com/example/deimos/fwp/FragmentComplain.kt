package com.example.deimos.fwp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker.checkCallingOrSelfPermission
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.complainfragment.*
import android.widget.ArrayAdapter
import android.widget.TextView

import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat


import androidx.appcompat.app.AppCompatActivity
import com.example.deimos.fwp.TestGoogle.TAG
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.changepassword.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
/*data class responseComplian (var resultCode : String,var developerMessage : String,var resultData : DataComplian)
data class DataComplian(var createdBy :String,var _id : String)
data class CompliansType(var resultCode : String,var developerMessage : String,var resultData : ArrayList<Res>,var rowCount : Int)
data class Res(var _id : String,var typeName : String)
data class CompliansRequestModel(var status : String,var partnerId : String,var subject : String,
                                 var complainType : String,var complainDesc : String,var map : complianRequstMap )
data class complianRequstMap(var lat : Double,var long : Double)*/
private const val PERMISSION_REQUEST = 10

private lateinit var fusedLocationClient: FusedLocationProviderClient
class FragmentComplain : androidx.fragment.app.Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    var locationManager: LocationManager? = null
    private var hasGps = false
    private var compliansrequestmodel: CompliansRequestModel? = null
    private val REQUEST_LOCATION = 1
    var mAPIService: ApiService? = null
    var token: String? = null
    private var latitude : Double?=null
    private var longitude : Double?=null
    private var Subject : String?=null
    private var Detail : String?=null
    private var Type : String?=null

    private var hasNetwork = false
    private var complianrequstmap: complianRequstMap? = null
    private var latitudeTextView: TextView? = null
    private var longitudeTextView: TextView? = null
    private var mylocation: Location? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationGps: Location? = null
    var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationNetwork: Location? = null

    var i: Int = 0
    var sp: SharedPreferences? = null
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    var sp2: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable = true
        view.menu.getItem(1).isChecked = true

        return inflater.inflate(R.layout.complainfragment, container, false);

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        if (sp?.getBoolean("LogIn_State", true) == true) {

            linearname.visibility = View.GONE
            linearsurname.visibility = View.GONE
            lineartel.visibility = View.GONE

        }
        //editor?.putBoolean("isComplain", false)

        var arrayList = mutableListOf<String>()
        arrayList.add("ประเภท")


        ///Get Type List ///

        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        //token = sp!!.getString("user_token", "-")
        mAPIService!!.getType( Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),"5dbfe99c776a690010deb237").enqueue(object : Callback<CompliansType> {

            override fun onResponse(call: Call<CompliansType>, response: Response<CompliansType>) {
                if (response.isSuccessful()) {

                    for (i in 0 until response.body()!!.rowCount) {
                        arrayList.add(response.body()!!.resultData[i].typeName)


                    }

                } else if (response.code() == 401) {
                    val mAlert = AlertDialog.Builder(view.context)
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

        goChooseGps.setOnClickListener {
            val intent = Intent(requireContext(),MapPin::class.java)
            startActivity(intent)
        }






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
                  //  Toast.makeText(context, arrayAdapter.getItem(position).toString(), Toast.LENGTH_SHORT).show()
                    Type = arrayAdapter.getItem(position).toString()
                }
            }
        }




        if (googleApiClient == null || googleApiClient!!.isConnected()==false) {

        }

        sendcomplainbutton.setOnClickListener {

            if (googleApiClient == null || googleApiClient!!.isConnected()==false) {
             //   setUpGClient()
            }
            Subject = subjectInput.text.toString()
            Detail = detailInput.text.toString()
            mAPIService = ApiUtils.apiService
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            token = sp!!.getString("user_token", "-")
            complianrequstmap = complianRequstMap(latitude!!, longitude!!)
            compliansrequestmodel = CompliansRequestModel("New", "5dbfe99c776a690010deb237", Subject!!, Type!!, Detail!!, complianrequstmap!!)
            val mProgressDialog = ProgressDialog(requireContext())
            mProgressDialog.isIndeterminate = true
            mProgressDialog.setCancelable(false)
            mProgressDialog.setMessage("Loading...")
            mProgressDialog.show()
            mAPIService!!.postComplians("Bearer " + token, Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                    compliansrequestmodel!!).enqueue(object : Callback<responseComplian> {

                override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {
                    //d("Complian", "Success")
                    if (response.isSuccessful()) {
                        d("Complian", "Success")
                        spinner.setSelection(0)
                        subjectInput.text.clear()
                        detailInput.text.clear()
                        GPSText.text = ""
                        mProgressDialog.dismiss();
                       // fragmentManager?.popBackStack()
                    } else if (response.code() == 401) {
                        val mAlert = AlertDialog.Builder(view.context)
                        mAlert.setTitle("พบข้อผิดพลาด")
                        mAlert.setMessage("รหัสผ่านของท่านผิด")
                        mAlert.setNegativeButton("ตกลง") { dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()

                    }

                }

                override fun onFailure(call: Call<responseComplian>, t: Throwable) {
                    d("Complian", t.toString())
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
            //textgps!!.text = "Latitude : $latitude"
        }
        ////POST /////





    ////POST /////

    }


    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentcontainercomplain,fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()


    }

    override fun onResume() {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(1).isCheckable=true
        view.menu.getItem(1).isChecked=true

      //  d("Res","YEs")
        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val editor = sp?.edit()
        if(sp!!.getString("LAT","-") == null || sp!!.getString("LAT","-") == "-"){
            GPSText.setText("")

        }else{
            GPSText.setText(sp!!.getString("LAT","-").substring(0..5)+","+  sp!!.getString("LONG","-").substring(0..5))
            latitude = sp!!.getString("LAT","-").toDouble()
            longitude=sp!!.getString("LONG","-").toDouble()
        }

        super.onResume()

       // set flag
    }

  /*  @Synchronized
    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(requireContext())
                .enableAutoManage(requireActivity(), 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient!!.connect()
    }
*/
    override fun onLocationChanged(location: Location) {
        mylocation = location
        if (mylocation != null) {
             latitude = mylocation!!.latitude
            longitude = mylocation!!.longitude

          //  longitudeTextView!!.text = "Longitude : $longitude"
            //Or Do whatever you want with your location
        }
    }

    override fun onConnected(bundle: Bundle?) {
        var activity = getActivity()
        if (isAdded() && activity != null) {
            checkPermissions()
        }

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
                    locationRequest.interval = 3000
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
                Activity.RESULT_CANCELED -> activity?.finish()
            }
        }
    }

    private fun checkPermissions() {
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

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val permissionLocation = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation()
        }
    }

    companion object {
        private val REQUEST_CHECK_SETTINGS_GPS = 0x1
        private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2
    }


    override fun onDetach() {

        super.onDetach()
        googleApiClient = null
        d("Res","YEs")

    }



    override fun onDestroy() {
        super.onDestroy()


    }



}