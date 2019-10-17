package com.example.deimos.fwp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.profiewithpicture.*
import android.provider.MediaStore
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat.checkSelfPermission

import android.graphics.Bitmap
import android.app.Activity
import android.content.SharedPreferences
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class Profilewithpicture : Fragment() {
    private val CAMERA_REQUEST = 1888
    private var uri:Uri?=null
    private var bottomSheetDialog:BottomSheetDialog?=null
    private var linearCamera : LinearLayout?=null
    private var linearGallery : LinearLayout?=null
    private var linearCancel : LinearLayout?=null
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val GALLERY_REQUEST = 1889
    var genderForChange : String?=null
    var usr : Userstate = Userstate()
    var usra : UserProfile?=null
    var token : String?=null
    var sp: SharedPreferences? = null
    var mAPIService: ApiService? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")

        getData()







        return inflater.inflate(R.layout.profiewithpicture,container,false)







    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        service_image_on_orderpage.setOnClickListener {
            createBottomSheet()
/*
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST)

            */
/*
            if (checkSelfPermission(view.context,Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
            } else {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }*/
        }


        logoutbut.setOnClickListener {
            val sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
            val editor = sp?.edit()
            editor?.putString("user_token","-")
            editor?.putBoolean("LogIn_State",false)

            editor?.commit()
            activity?.finish()



            //replaceFragment(LogIn())
            /*  val intent = Intent(context,LogIn::class.java)
          context?.startActivity(intent)*/
        }
        savelist.setOnClickListener {

            replaceFragmentToRight(FragmentBookmark())
        }
        changepasswordbut.setOnClickListener {
            replaceFragmentToRight(ChangePassword())
        }

        editprofile.setOnClickListener {
            replaceFragmentToRight(EditProfile())
        }

        setting.setOnClickListener {
            replaceFragmentToRight(ComplainList())
        }
        exitbut.setOnClickListener {
            activity?.finish()
        }

    }

    private fun replaceFragmentToRight(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)

        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)

    }
    private fun replaceFragmentToLeft(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)

        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)

        fragmentTransaction?.commit()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri=data?.getData();
            imageset.setImageURI(uri)
        }
        if (requestCode === CAMERA_REQUEST && resultCode === Activity.RESULT_OK) {
            val photo = data?.getExtras()?.get("data") as Bitmap
            imageset.setImageBitmap(photo)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                d("Permission","camera permission granted")

                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                d("Permission","camera permission denied")

            }
        }
    }
    private fun createBottomSheet(){
        val view = layoutInflater.inflate(R.layout.bottomsheet,null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view)
        linearCamera =view.findViewById(R.id.option1)
        linearCamera!!.setOnClickListener {
            startCamera()
            dialog.dismiss()
        }
        linearGallery =view.findViewById(R.id.option2)
        linearGallery!!.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
        linearCancel =view.findViewById(R.id.option3)
        linearCancel!!.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()



    }
    private fun startCamera() {
        if (checkSelfPermission(requireContext(), Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
        } else {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

    }
    private fun startGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY_REQUEST)
    }
    fun getData(){
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtils.apiService

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r)).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    textEmail.text = response.body()!!.resultData.email
                    memberid.text = response.body()!!.resultData._id
                    firstName.text = response.body()!!.resultData.firstName
                    lastName.text = response.body()!!.resultData.lastName
                    birthday.text = response.body()!!.resultData.birthdate
                    genderForChange=response.body()!!.resultData.gender
                    val editor = sp?.edit()
                    editor?.putString("user_id",response.body()!!.resultData._id)
                    editor?.putString("user_email",response.body()!!.resultData.email)
                    editor?.commit()


                    if (genderForChange == "Male"){
                        gendericon.scaleType= ImageView.ScaleType.FIT_XY
                        gendericon.setImageResource(R.drawable.male)
                    }else if (genderForChange == "Female"){
                        gendericon.scaleType= ImageView.ScaleType.FIT_XY
                        gendericon.setImageResource(R.drawable.female)
                    }else if(genderForChange == "LGBT"){
                        gendericon.scaleType= ImageView.ScaleType.FIT_XY
                        gendericon.setImageResource(R.drawable.group2)
                    }
                    else{
                        d("CheckValue",genderForChange)
                    }

                }
            }
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {

            }
        })



    }


}
