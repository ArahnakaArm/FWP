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
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.profiewithpicture.*
import android.provider.MediaStore
import android.Manifest.permission
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.widget.Toast
import android.support.annotation.NonNull
import android.R.attr.data
import android.graphics.Bitmap
import android.app.Activity
import android.support.design.widget.BottomSheetDialog
import android.widget.LinearLayout


class Profilewithpicture : Fragment() {
    private val CAMERA_REQUEST = 1888
    private var uri:Uri?=null
    private var bottomSheetDialog:BottomSheetDialog?=null
    private var linearCamera : LinearLayout?=null
    private var linearGallery : LinearLayout?=null
    private var linearCancel : LinearLayout?=null
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val GALLERY_REQUEST = 1889
    var usr : Userstate = Userstate()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.profiewithpicture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //d("arm",dbHelper?.userstete.toString())


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
            val sp = context?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
            val editor = sp?.edit()
            editor?.putBoolean("My_Value",false)
            editor?.commit()

           replaceFragment(FragmentProfile())
          /*  val intent = Intent(context,FragmentProfile::class.java)
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
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show()
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

}
