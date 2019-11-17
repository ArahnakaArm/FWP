package com.example.deimos.fwp

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.complianviewpager.*

import kotlinx.android.synthetic.main.compliantab3.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
data class responseComplian (var resultCode : String,var developerMessage : String,var resultData : DataComplian)
data class DataComplian(var createdBy :String,var _id : String,var complainNumber : String)
data class CompliansType(var resultCode : String, var developerMessage : String, var resultData : java.util.ArrayList<Res>, var rowCount : Int)
data class Res(var _id : String,var typeName : String,var guideLabel : String)
data class CompliansRequestModel(var status : String,var partnerId : String,var subject : String,
                                 var complainType : String,var complainDesc : String,var map : complianRequstMap )
data class complianRequstMap(var lat : Double,var long : Double)
class Tab3Complian : androidx.fragment.app.Fragment() {
    var sp: SharedPreferences? = null
    private var btn: Button? = null

    var Uris :Uri?=null
    var Uriss = ArrayList<Uri>()
    private val MY_READ_PERMISSION_CODE = 200
    private var PICK_IMAGE_MULTIPLE = 1
    private var imageEncoded: String? = null
    private var imagesEncodedList: MutableList<String>? = null
    private var gvGallery: GridView? = null
    var token : String?=null
    private var complianNumber : String?=null
    private val SELECT_IMAGE = 1338
    private var complianrequstmap: complianRequstMap? = null
    private  val REQUEST_PICK_PHOTO = 1
    var Send : Button?=null
    var fileUris: ArrayList<Uri> = ArrayList<Uri>()
    private var compliansrequestmodel: CompliansRequestModel? = null
    private val GALLERY_REQUEST = 1889
    private val MY_WRITE_PERMISSION_CODE = 200
    private var galleryAdapter: GalleryAdapter? = null
    var mAPIService: ApiService? = null
    private var viewp : androidx.viewpager.widget.ViewPager?=null
    internal var filePath = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.compliantab3,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        viewp = activity!!.findViewById(R.id.viewpager)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_PERMISSION_CODE);
        }
        d("Check",sp!!.getString("Type","-"))
        Send =activity!!.findViewById(R.id.next2)
        Send!!.setOnClickListener {
            sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            token = sp!!.getString("user_token", "-")
            complianrequstmap = complianRequstMap(sp!!.getString("LAT","-")!!.toDouble(), sp!!.getString("LONG","-")!!.toDouble())
            compliansrequestmodel = CompliansRequestModel("New", "5dbfe99c776a690010deb237", sp!!.getString("Subject","-")!!, sp!!.getString("Type","-")!!, sp!!.getString("Description","-")!!,complianrequstmap!!)
            if(sp!!.getString("user_token","-") != "-") {
                SendComplian(compliansrequestmodel!!)
            }
            else{
                SendComplianNoToken(compliansrequestmodel!!)
            }
        }
        sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")



        upphotobut!!.setOnClickListener {

                FilePickerBuilder.instance.setMaxCount(9)
                        .setSelectedFiles(filePath)
                        .enableCameraSupport(false)
                        .setActivityTheme(R.style.LibAppTheme)
                        .pickPhoto(this)

        }
    }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        when (requestCode) {
            FilePickerConst
                    .REQUEST_CODE_PHOTO -> if (resultCode == RESULT_OK && data != null) {

                filePath = ArrayList()
                filePath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))

                for (i in 0 until filePath.size) {

                    Image1.setImageResource(R.drawable.inputboxcomplain)
                    Image1.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image2.setImageResource(R.drawable.inputboxcomplain)
                    Image2.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image3.setImageResource(R.drawable.inputboxcomplain)
                    Image3.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image4.setImageResource(R.drawable.inputboxcomplain)
                    Image4.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image5.setImageResource(R.drawable.inputboxcomplain)
                    Image5.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image6.setImageResource(R.drawable.inputboxcomplain)
                    Image6.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image7.setImageResource(R.drawable.inputboxcomplain)
                    Image7.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image8.setImageResource(R.drawable.inputboxcomplain)
                    Image8.scaleType= ImageView.ScaleType.CENTER_CROP

                    Image9.setImageResource(R.drawable.inputboxcomplain)
                    Image9.scaleType= ImageView.ScaleType.CENTER_CROP


                }

                for (i in 0 until filePath.size) {
                    Uris = Uri.fromFile(File(filePath[i].toString()))

                    Log.d("Arm", Uris.toString())
                    Log.d("Arm", filePath[0].toString())
                    Uriss.add(Uris!!)
                    if(i == 0){
                        Image1.setImageURI(Uris)
                        Image1.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 1){
                        Image2.setImageURI(Uris)
                        Image2.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 2){
                        Image3.setImageURI(Uris)
                        Image3.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 3){
                        Image4.setImageURI(Uris)
                        Image4.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 4){
                        Image5.setImageURI(Uris)
                        Image5.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 5){
                        Image6.setImageURI(Uris)
                        Image6.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 6){
                        Image7.setImageURI(Uris)
                        Image7.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 7){
                        Image8.setImageURI(Uris)
                        Image8.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                    if(i == 8){
                        Image9.setImageURI(Uris)
                        Image9.scaleType= ImageView.ScaleType.CENTER_CROP
                    }
                 //uploadFile(File(filePath[0]),"5dcf905e05226f0010975f7b")
                }
            }


        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Arm","yes")
        when (requestCode) {FilePickerConst.REQUEST_CODE_PHOTO -> if(resultCode == Activity.RESULT_OK && data != null) {
                filePath = ArrayList()
                filePath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                for (i in 0 until filePath.size) {
                    Uris = Uri.fromFile(File(filePath[i].toString()))
                    Log.d("Arm", Uris.toString())
                    Log.d("Arm", filePath[0].toString())
                    //importPhoto(Uris!!)
                }
            }
        }

    }*/
    private fun uploadFile(file: String,id :String) {
        // d("Testt",fileUri.toString())
        // var file = File(getPath(fileUri));
       var fileupload =File(file)
       d("Size",fileupload.length().toString())
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        // val filePath = getImageFilePath(fileUri)
        // if (filePath != null && !filePath!!.isEmpty()) {
        // val file = File(filePath)
        if (fileupload.exists()) {

            val retrofit = Retrofit.Builder()
                    .baseUrl("http://206.189.41.105:1210/api/v1/")
                    .build()
            val compliansId = "5db6b397dbb3640010e21f17"
            val service = retrofit.create(ApiService::class.java!!)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileupload)
            val body = MultipartBody.Part.createFormData("image", fileupload.getName(), requestFile)
            val descriptionString = "Sample description"
            val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
            mAPIService!!.postComplianImage("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),id,body).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>,
                                        response: Response<UserProfile>) {

                    if (response.isSuccessful){
                        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                        var edditor = sp!!.edit()
                        edditor.putString("Subject","")
                        edditor.putString("Detail","")
                        edditor.putInt("spin",0)
                        edditor.commit()
                     //   d("IDTEST",response.body()!!.resultData.complainNumber)

                        val intent = Intent(activity,Success::class.java)
                        intent.putExtra("ComplianNumber",complianNumber)
                        activity!!.finish()
                        activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        activity!!.startActivity(intent)


                    }else{
                        val mAlert = AlertDialog.Builder(view!!.context)
                        mAlert.setTitle("พบข้อผิดพลาด")
                        mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                        mAlert.setNegativeButton("ตกลง") { dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()
                    }
                    /* d("Pic",response.body()!!.resultData.image.path)
                     imageUrl = IMAGE_URL+response.body()!!.resultData.image.path

                     try {
                         if (imageUrl !== null) {
                             Glide.with(this@Profilewithpicture)
                                     .load(imageUrl)
                                     .into(imageset)
                         }
                     }catch (e : Exception){

                     }*/
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    Log.d("Pic", t.toString())
                }

            })
        }


        //}
    }
    fun importPhoto(uri: Uri,id : String): Boolean {
        if (!isImage(requireContext(),uri)) {
            // not image
            return false
        }

        return try {
            val photoFile = createImageFile(requireActivity().cacheDir,null,id)
            copyUriToFile( requireContext(),uri, photoFile)

            // addImageToGallery(photoFile)
            true
        }
        catch (e: IOException) {
            e.printStackTrace()
            // handle error
            false
        }
    }
    fun copyUriToFile(context: Context, uri: Uri, outputFile: File) {
        val inputStream = context.contentResolver.openInputStream(uri)

        // copy inputStream to file using okio
        /*
        val source = Okio.buffer(Okio.source(inputStream))
        val sink = Okio.buffer(Okio.sink(outputFile))

        sink.writeAll(source)

        sink.close()
        source.close()
         */

        val outputStream = FileOutputStream(outputFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    private fun createImageFile(dir: File, fileName: String? = null,id : String): File {
        if (fileName == null) {
            sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-kkmmss"))
            Log.d("FileName", " " + dir)
            if(sp!!.getString("user_token","-") != "-") {
             //   uploadFile(File.createTempFile("IMG_$timestamp", ".jpg", dir), id)
            }
            else{
               // uploadFileNoToken(File.createTempFile("IMG_$timestamp", ".jpg", dir), id)
            }
            return File.createTempFile("IMG_$timestamp", ".jpg", dir)
        }
        Log.d("FileName", fileName)
        return File(dir, fileName)
    }
    fun isImage(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri) ?: return true
        return mimeType.startsWith("image/")
    }

    public fun SendComplian(model : CompliansRequestModel){
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()

        mAPIService!!.postComplians("Bearer " + token, Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                model!!).enqueue(object : Callback<responseComplian> {

            override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {

                //d("Complian", "Success")
                if (response.isSuccessful()) {
                    complianNumber=response.body()!!.resultData.complainNumber
                    if(filePath.size != 0) {
                        d("Complian", "Success")
                        d("AB", response.body()!!.resultData._id)
                        try {
                            for (i in 0 until filePath.size) {
                                uploadFile(filePath[i], response.body()!!.resultData._id)

                            }

                        } catch (e: Exception) {

                        }
                    }
                        else{

                            sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                            var edditor = sp!!.edit()
                            edditor.putString("Subject","")
                            edditor.putString("Detail","")
                            edditor.putInt("spin",0)
                            edditor.commit()
                        val intent = Intent(activity,Success::class.java)
                        complianNumber=response.body()!!.resultData.complainNumber
                        intent.putExtra("ComplianNumber",complianNumber)
                        activity!!.finish()
                        activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        activity!!.startActivity(intent)

                        }




                } else if (response.code() == 401) {
                    val mAlert = AlertDialog.Builder(view!!.context)
                    mAlert.setTitle("พบข้อผิดพลาด")
                    mAlert.setMessage("")
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
    public fun SendComplianNoToken(model : CompliansRequestModel){
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()

        mAPIService!!.postCompliansNoToken(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                model!!).enqueue(object : Callback<responseComplian> {

            override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {

                //d("Complian", "Success")
                if (response.isSuccessful()) {
                    complianNumber=response.body()!!.resultData.complainNumber
                    if(filePath.size != 0) {
                    d("Complian", "Success")
                    d("AB",response.body()!!.resultData._id)
                    try {
                        for (i in 0 until filePath.size) {
                            uploadFileNoToken(filePath[i], response.body()!!.resultData._id)


                        }

                    }catch (e : Exception){

                    }
                    }
                    else{

                        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                        var edditor = sp!!.edit()
                        edditor.putString("Subject","")
                        edditor.putString("Detail","")
                        edditor.putInt("spin",0)
                        complianNumber=response.body()!!.resultData.complainNumber
                        val intent = Intent(activity,Success::class.java)
                        intent.putExtra("ComplianNumber",complianNumber)
                        edditor.commit()
                        activity!!.finish()
                        activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                        activity!!.startActivity(intent)

                    }


                } else if (response.code() == 401) {
                    val mAlert = AlertDialog.Builder(view!!.context)
                    mAlert.setTitle("พบข้อผิดพลาด")
                    mAlert.setMessage("")
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
    private fun uploadFileNoToken(file: String,id :String) {
        // d("Testt",fileUri.toString())
        // var file = File(getPath(fileUri));
        var fileupload =File(file)
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        // val filePath = getImageFilePath(fileUri)
        // if (filePath != null && !filePath!!.isEmpty()) {
        // val file = File(filePath)
        if (fileupload.exists()) {

            val retrofit = Retrofit.Builder()
                    .baseUrl("http://206.189.41.105:1210/api/v1/")
                    .build()
            val compliansId = "5db6b397dbb3640010e21f17"
            val service = retrofit.create(ApiService::class.java!!)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileupload)
            val body = MultipartBody.Part.createFormData("image", fileupload.getName(), requestFile)
            val descriptionString = "Sample description"
            val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
            mAPIService!!.postComplianImageNotoken(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),id,body).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>,
                                        response: Response<UserProfile>) {

                    if (response.isSuccessful){
                        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                        var edditor = sp!!.edit()
                        val intent = Intent(activity,Success::class.java)
                        intent.putExtra("ComplianNumber",response.body()!!.resultData.complainNumber)
                        edditor.putString("Subject","")
                        edditor.putString("Detail","")
                        edditor.putInt("spin",0)
                        edditor.commit()




                    }else{
                        val mAlert = AlertDialog.Builder(view!!.context)
                        mAlert.setTitle("พบข้อผิดพลาด")
                        mAlert.setMessage("กรุณาลองใหม่อีกครั้ง")
                        mAlert.setNegativeButton("ตกลง") { dialog, which ->
                            dialog.dismiss()
                        }
                        mAlert.show()
                    }
                    /* d("Pic",response.body()!!.resultData.image.path)
                     imageUrl = IMAGE_URL+response.body()!!.resultData.image.path

                     try {
                         if (imageUrl !== null) {
                             Glide.with(this@Profilewithpicture)
                                     .load(imageUrl)
                                     .into(imageset)
                         }
                     }catch (e : Exception){

                     }*/
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    Log.d("Pic", t.toString())
                }

            })
        }

        val intent = Intent(activity,Success::class.java)
        intent.putExtra("ComplianNumber",complianNumber)
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        activity!!.startActivity(intent)
        //}
    }

}