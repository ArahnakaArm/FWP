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
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.complianviewpager.*
import id.zelory.compressor.Compressor
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
data class CompliansRequestModel(var status : String,var subject : String,
                                 var complainType : String,var complainDesc : String,var map : complianRequstMap,var image : ArrayList<String>,var firstName : String,var lastName : String ,var partnerId : String)
data class complianRequstMap(var lat : Double,var long : Double)
data class CompliansRequestModelNoImage(var status : String,var subject : String,
                                 var complainType : String,var complainDesc : String,var map : complianRequstMap,var partnerId : String,var firstName : String,var lastName : String)
data class CompliansRequestModelNoTokenNoImage(var status : String,var subject : String,
                                        var complainType : String,var complainDesc : String,var map : complianRequstMap,var firstName : String,var lastName : String ,var partnerId : String)

data class CompliansRequestModelImage(var status : String,var subject : String,
                                 var complainType : String,var complainDesc : String,var map : complianRequstMap,var image : ArrayList<String>,var partnerId : String,var firstName : String,var lastName : String)


interface SpaceRegionRepresentableComplian {
    fun endpoint(): String
}

enum class SpaceRegionComplian: SpaceRegionRepresentableComplian {
    SFO {
        override fun endpoint(): String {
            return "https://sfo2.digitaloceanspaces.com"
        }
    }, AMS {
        override fun endpoint(): String {
            return "https://ams3.digitaloceanspaces.com"
        }
    }, SGP {
        override fun endpoint(): String {
            return "https://sgp1.digitaloceanspaces.com"
        }
    }
}

class Tab3Complian : androidx.fragment.app.Fragment() {
    var sp: SharedPreferences? = null
    private var isImageSelect =false
    private val accesskey = "DL7EOQXBNXRI2T7FGPHD"
    private val secretkey = "at24/UhN+eMAY9ZCvEH3Xs0oa6Ro0sneWB9+GGYruXA"
    private val spacename = "evolka-fwpapp-prod"
    private val spaceregion = SpaceRegion.SGP
    private val filename = "example_image"
    private val filetype = "jpg"
    private var LAT : Double?=null
    private var LONG : Double?=null
    var fileupload : File?=null
    private var countUpload =0
    private var ImageUploadPath = ArrayList<String>()
    private var transferUtility: TransferUtility?=null
    private var appContext: Context?=null
    private var fileToUpload = ArrayList<File>()
    var sharedPreferences : SharedPreferences?=null
    var Uris :Uri?=null
    var Uriss = ArrayList<Uri>()
    var token : String?=null
    private var complianNumber : String?=null
    private var complianrequstmap: complianRequstMap? = null
    var Send : Button?=null
    private var compliansrequestmodel: CompliansRequestModel? = null
    private var compliansrequestmodelImage: CompliansRequestModelImage? = null
    private val GALLERY_REQUEST = 1889
    private var compliansrequestmodelNoTokenNoImage : CompliansRequestModelNoTokenNoImage?=null
    private var compliansrequestmodelNoImage: CompliansRequestModelNoImage? = null
    private val MY_WRITE_PERMISSION_CODE = 200
    var mAPIService: ApiServiceComplian? = null
    private var viewp : androidx.viewpager.widget.ViewPager?=null
    private var filePath = ArrayList<String>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.compliantab3,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val credentials = StaticCredentialsProvider(BasicAWSCredentials(accesskey, secretkey))
        val client = AmazonS3Client(credentials, Region.getRegion("us-east-1"))
        client.endpoint = spaceregion.endpoint()
        transferUtility = TransferUtility.builder().s3Client(client).context(requireContext()).build()
        appContext = requireContext()

        viewp = activity!!.findViewById(R.id.viewpager)

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_PERMISSION_CODE);
        }

        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        Send =activity!!.findViewById(R.id.next2)

        sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);



        token = sp!!.getString("user_token","-")
        Send!!.setOnClickListener {
            if(isImageSelect) {
                ImageUploadPath.clear()
                SendComplian()
            }
            else if(!isImageSelect && token != "-" ){
                ImageUploadPath.clear()
                doComplianNoImage()
            }
            else if(!isImageSelect && token == "-"){
                ImageUploadPath.clear()
                doComplianNoTokenNoImage()
            }
        }






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
                isImageSelect=true

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

                }
            }
        }
    }

    private fun uploadFile(file: String,id :String) {
       var fileupload =File(file)
       var compressedImageFile = Compressor(requireContext()).setMaxHeight(640).setMaxWidth(640).setQuality(100).compressToFile(fileupload);
       d("Size","Compressed : "+compressedImageFile.length().toString())
       d("Size",fileupload.length().toString())
        mAPIService = ApiUtilsComplian.apiServiceComplian
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()

        if (fileupload.exists()) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile)
            val body = MultipartBody.Part.createFormData("image", compressedImageFile.getName(), requestFile)
            val descriptionString = "Sample description"
            mAPIService!!.postComplianImage("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),id,body).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>,
                                        response: Response<UserProfile>) {

                    if (response.isSuccessful){
                       try {
                       }catch (e : Exception){

                       }

                    }else{
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    Log.d("Pic", t.toString())
                }

            })
        }


    }

    private fun SendComplian(){
        uploadExampleFile(filePath)
    }

    public fun SendComplianNoToken(){
        uploadExampleFile(filePath)



    }
    private fun uploadFileNoToken(file: String,id :String) {
        // d("Testt",fileUri.toString())
        // var file = File(getPath(fileUri));
        var fileupload =File(file)
        mAPIService = ApiUtilsComplian.apiServiceComplian
        var compressedImageFile = Compressor(requireContext()).setMaxHeight(640).setMaxWidth(640).setQuality(100).compressToFile(fileupload);
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
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageFile)
            val body = MultipartBody.Part.createFormData("image", compressedImageFile.getName(), requestFile)
            val descriptionString = "Sample description"
            val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
            mAPIService!!.postComplianImageNotoken(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),id,body).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>,
                                        response: Response<UserProfile>) {

                    if (response.isSuccessful){

                    }else{

                    }

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

    }
    fun uploadExampleFile(file:ArrayList<String>){
        for (i in 0 until file.size) {
            var fileUpLoad = File(file[i])
            var name = randomName()
            var listener = transferUtility!!.upload(spacename, name, fileUpLoad, CannedAccessControlList.PublicRead)
            TransferNetworkLossHandler.getInstance(requireContext())
            listener.setTransferListener(object : TransferListener {
                override fun onError(id: Int, ex: Exception?) {
                    d("S3 Upload", ex.toString())
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    d("S3 Upload", "Progress ${((bytesCurrent / bytesTotal) * 100)}")
                }

                override fun onStateChanged(id: Int, state: TransferState?) {
                    if (state == TransferState.COMPLETED) {
                      //  d("Num", "Completed")
                       d("Num",i.toString())
                        d("Num",fileUpLoad.toString())
                        ImageUploadPath.add("https://evolka-fwpapp-prod.sgp1.digitaloceanspaces.com/"+name)

                        countUpload++
                        when(countUpload){

                            file.size -> {
                                d("Num", "Finish")
                                sp = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                                if(sp!!.getString("user_token","-") != "-") {

                                    doComplian()
                                }
                                else{

                                    doComplianNoToken()
                                }
                                countUpload = 0
                            }

                        }
                    }
                }
            })
        }

    }
    fun randomName():String{
        val sdf = SimpleDateFormat("ddMMYY")
        val currentDate = sdf.format(Date())

        var stringName = currentDate+randomString(6)


        return stringName
    }

    fun randomString(len: Int): String {
        val DATA = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var RANDOM = Random()
        val sb = StringBuilder(len)
        for (i in 0 until len) {
            sb.append(DATA[RANDOM.nextInt(DATA.length)])
        }
        return sb.toString()
    }
    private fun doComplian(){
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        LAT = sharedPreferences!!.getString("LAT","-").toDouble()
        LONG = sharedPreferences!!.getString("LONG","-").toDouble()
        d("LatLong", LAT.toString()+","+LONG.toString())
        val partnerId = sharedPreferences!!.getString("partnerId", "-")
        val firstName =  sharedPreferences!!.getString("firstName", "-")
        val lastName =  sharedPreferences!!.getString("lastName", "-")
        complianrequstmap = complianRequstMap(LAT!!, LONG!!)
        compliansrequestmodelImage = CompliansRequestModelImage("New", sp!!.getString("Subject", "-")!!, sp!!.getString("Type", "-")!!, sp!!.getString("Description", "-")!!, complianrequstmap!!, ImageUploadPath
                    , partnerId,firstName,lastName)
        compliansrequestmodelNoImage = CompliansRequestModelNoImage("New", sp!!.getString("Subject", "-")!!, sp!!.getString("Type", "-")!!, sp!!.getString("Description", "-")!!, complianrequstmap!!
                , partnerId,firstName,lastName)

            mAPIService = ApiUtilsComplian.apiServiceComplian
            val sdf = SimpleDateFormat("yyMMdd")
            val currentDate = sdf.format(Date())
            val r = (10..12).shuffled().first()
            mAPIService!!.postCompliansImage("Bearer " + token, Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                    compliansrequestmodelImage!!, partnerId).enqueue(object : Callback<responseComplian> {
                override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {
                    if (response.isSuccessful()) {
                        complianNumber = response.body()!!.resultData.complainNumber
                        if (filePath.size != 0) {
                            d("Complian", "Success")
                            d("AB", response.body()!!.resultData._id)
                            try {
                                sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                                var edditor = sp!!.edit()
                                edditor.putString("Subject", "")
                                edditor.putString("Detail", "")
                                edditor.putInt("spin", 0)
                                edditor.commit()
                                val intent = Intent(activity, Success::class.java)
                                intent.putExtra("ComplianNumber", complianNumber)
                                activity!!.finish()
                                activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                activity!!.startActivity(intent)

                            } catch (e: Exception) {

                            }
                        } else {

                            sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                            var edditor = sp!!.edit()
                            edditor.putString("Subject", "")
                            edditor.putString("Detail", "")
                            edditor.putInt("spin", 0)
                            edditor.commit()
                            val intent = Intent(activity, Success::class.java)
                            complianNumber = response.body()!!.resultData.complainNumber
                            intent.putExtra("ComplianNumber", complianNumber)
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

                }
            })





}
    private fun doComplianNoToken(){
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId", "-")
        val firstName =  sharedPreferences!!.getString("firstName", "-")
        val lastName =  sharedPreferences!!.getString("lastName", "-")
        LAT = sharedPreferences!!.getString("LAT","-").toDouble()
        LONG = sharedPreferences!!.getString("LONG","-").toDouble()
        d("LatLong", LAT.toString()+","+LONG.toString())
        complianrequstmap = complianRequstMap(LAT!!, LONG!!)
        compliansrequestmodel = CompliansRequestModel("New", sp!!.getString("Subject","-")!!, sp!!.getString("Type","-")!!, sp!!.getString("Description","-")!!,complianrequstmap!!,ImageUploadPath
                ,firstName,lastName,partnerId)
        mAPIService = ApiUtilsComplian.apiServiceComplian
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.postCompliansNoToken(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                compliansrequestmodel!!,partnerId).enqueue(object : Callback<responseComplian> {

            override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {

                if (response.isSuccessful()) {
                    complianNumber=response.body()!!.resultData.complainNumber
                    if(filePath.size != 0) {
                        d("Complian", "Success")
                        d("AB",response.body()!!.resultData._id)
                        try {

                                        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                                        var edditor = sp!!.edit()
                                        edditor.putString("Subject", "")
                                        edditor.putString("Detail", "")
                                        edditor.putInt("spin", 0)
                                        edditor.commit()
                                        val intent = Intent(activity, Success::class.java)
                                        intent.putExtra("ComplianNumber", complianNumber)
                                        activity!!.finish()
                                        activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                                        activity!!.startActivity(intent)
                        }catch (e : Exception){

                        }
                    }
                    else{

                    }


                } else {
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

            }
        })
    }
    private fun doComplianNoImage(){
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        LAT = sharedPreferences!!.getString("LAT","-").toDouble()
        LONG = sharedPreferences!!.getString("LONG","-").toDouble()
        d("LatLong", LAT.toString()+","+LONG.toString())
        val partnerId = sharedPreferences!!.getString("partnerId", "-")
        val firstName =  sharedPreferences!!.getString("firstName", "-")
        val lastName =  sharedPreferences!!.getString("lastName", "-")
        complianrequstmap = complianRequstMap(LAT!!, LONG!!)
        compliansrequestmodelNoImage = CompliansRequestModelNoImage("New", sp!!.getString("Subject","-")!!, sp!!.getString("Type","-")!!, sp!!.getString("Description","-")!!,complianrequstmap!!
                ,partnerId,firstName,lastName)
        mAPIService = ApiUtilsComplian.apiServiceComplian
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.postCompliansNoImage("Bearer " + token, Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                compliansrequestmodelNoImage!!,partnerId).enqueue(object : Callback<responseComplian> {
            override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {
                if (response.isSuccessful()) {
                    complianNumber=response.body()!!.resultData.complainNumber
                    if(filePath.size != 0) {
                        d("Complian", "Success")
                        d("AB", response.body()!!.resultData._id)
                        try {
                            sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                            var edditor = sp!!.edit()
                            edditor.putString("Subject", "")
                            edditor.putString("Detail", "")
                            edditor.putInt("spin", 0)
                            edditor.commit()
                            val intent = Intent(activity, Success::class.java)
                            intent.putExtra("ComplianNumber", complianNumber)
                            activity!!.finish()
                            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            activity!!.startActivity(intent)

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

            }
        })
    }
    private fun doComplianNoTokenNoImage(){
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        LAT = sharedPreferences!!.getString("LAT","-").toDouble()
        LONG = sharedPreferences!!.getString("LONG","-").toDouble()
        d("LatLong", LAT.toString()+","+LONG.toString())
        d("LatLong", sp!!.getString("LAT","-")+","+sp!!.getString("LONG","-").toDouble())
        val partnerId = sharedPreferences!!.getString("partnerId", "-")
        val firstName =  sharedPreferences!!.getString("firstName", "-")
        val lastName =  sharedPreferences!!.getString("lastName", "-")
        complianrequstmap = complianRequstMap(LAT!!, LONG!!)
        compliansrequestmodelNoTokenNoImage = CompliansRequestModelNoTokenNoImage("New", sp!!.getString("Subject","-")!!, sp!!.getString("Type","-")!!, sp!!.getString("Description","-")!!,complianrequstmap!!
                ,firstName,lastName,partnerId)
        mAPIService = ApiUtilsComplian.apiServiceComplian
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.postCompliansNoTokenNoImage(Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r),
                compliansrequestmodelNoTokenNoImage!!,partnerId).enqueue(object : Callback<responseComplian> {

            override fun onResponse(call: Call<responseComplian>, response: Response<responseComplian>) {

                if (response.isSuccessful()) {
                    complianNumber=response.body()!!.resultData.complainNumber
                        d("Complian", "Success")
                        d("AB",response.body()!!.resultData._id)
                        try {
                            sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                            var edditor = sp!!.edit()
                            edditor.putString("Subject", "")
                            edditor.putString("Detail", "")
                            edditor.putInt("spin", 0)
                            edditor.commit()
                            val intent = Intent(activity, Success::class.java)
                            intent.putExtra("ComplianNumber", complianNumber)
                            activity!!.finish()
                            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                            activity!!.startActivity(intent)
                        }catch (e : Exception){
                         d("VVV",e.toString())
                        }


                } else {
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

            }
        })
    }
}