package com.example.deimos.fwp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.profiewithpicture.*
import android.provider.MediaStore
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.checkSelfPermission

import android.graphics.Bitmap
import android.app.Activity
import android.content.*
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.edit
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.bumptech.glide.Glide
import com.facebook.internal.Utility
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.youtube.player.internal.i
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.xpath.XPathConstants.STRING

data class ProfileFacebookGoogle(var resultData : resultDataFacebookGoogle,
                                  var resultCode : String
                                  ,var developerMessage:String
)
data class resultDataFacebookGoogle(var _id :String,var birthDate: String ,var email: String,var firstName : String ,
                       var gender : String,var lastName :String)
data class imageBody(var image : String)
interface SpaceRegionRepresentable {
    fun endpoint(): String
}


enum class SpaceRegion: SpaceRegionRepresentable {
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

class Profilewithpicture : androidx.fragment.app.Fragment() {
    private val accesskey = "DL7EOQXBNXRI2T7FGPHD"
    private val secretkey = "at24/UhN+eMAY9ZCvEH3Xs0oa6Ro0sneWB9+GGYruXA"
    private val spacename = "evolka-fwpapp-prod"
    private val spaceregion = SpaceRegion.SGP
    internal var filePath = ArrayList<String>()
    private val filename = "example_image"
    private val filetype = "jpg"
    var fileupload : File?=null
    private var transferUtility: TransferUtility?=null
    private var appContext: Context?=null
    var date : Date?=null
    private val CAMERA_REQUEST = 1888
    private var uri:Uri?=null
    private var sharedPreferences:SharedPreferences?=null
    private var bottomSheetDialog: BottomSheetDialog?=null
    private var linearCamera : LinearLayout?=null
    private var linearGallery : LinearLayout?=null
    private var linearCancel : LinearLayout?=null
    private var imageUrl : String?=null
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val MY_WRITE_PERMISSION_CODE = 200
    private val GALLERY_REQUEST = 1889
    private val IMAGE_URL = "http://206.189.41.105:1210/"
    var genderForChange : String?=null
    var userIdTest : String?=null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var userType : String?=null
    var token : String?=null
    var web: WebView? = null


    var image_name : String?=null
    var sp: SharedPreferences? = null
    var mAPIService: ApiServiceMember? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = activity!!.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        view.menu.getItem(4).isCheckable=true
        view.menu.getItem(4).isChecked=true

        sp = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        userType = sp!!.getString("LogIn_Type","-")
        d("See",sp!!.getBoolean("LogIn_State",false).toString())


        //webView//


        //webView//
       // checkPermission()

        val serverClientId = "138956954782-2vpo3krp7om6ruuacbiparld63u5a13f.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

/*

        when (userType) {
            "local" -> getData()
            "goolge" -> getDataGoogle()
            "facebook" -> getDataFacebook()

            else -> println("Number too high")
        }



*/


        return inflater.inflate(R.layout.profiewithpicture,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val credentials = StaticCredentialsProvider(BasicAWSCredentials(accesskey, secretkey))
        val client = AmazonS3Client(credentials, Region.getRegion("us-east-1"))
        client.endpoint = spaceregion.endpoint()
        transferUtility = TransferUtility.builder().s3Client(client).context(requireContext()).build()
        appContext = requireContext()
        d("Type",userType)
        if(userType == "Local"){
            linearLocal.visibility = View.VISIBLE
            linearSocial.visibility = View.INVISIBLE
        }
        else if(userType == "Social"){
            linearSocial.visibility = View.VISIBLE
            linearLocal.visibility = View.INVISIBLE
        }

        if (checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_PERMISSION_CODE);
        }
        try {
            getData()
        }catch (e :Exception){

        }
        FWP.setOnClickListener {
            startActivity(Intent(requireContext(),com.example.deimos.fwp.WebView::class.java))
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
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
             signOutGoogle()
            LoginManager.getInstance().logOut()
            editor?.putString("user_token","-")
            editor?.putBoolean("LogIn_State",false)
            editor?.putBoolean("LogIn_StateFacebook", false)
            editor?.putBoolean("LogIn_StateGoogle", false)
            editor?.commit()
            replaceFragment(FragmentHome())

         //   activity?.finish()



            //replaceFragment(LogIn())
            /*  val intent = Intent(context,LogIn::class.java)
          context?.startActivity(intent)*/
        }
        savelist.setOnClickListener {
            savelist.isEnabled = false
            startActivity(Intent(requireContext(), FavoriteList::class.java))
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        changepasswordbut.setOnClickListener {
            val intent = Intent(requireContext(),ChangePassword::class.java)
            activity?.startActivity(intent)
        }

        editprofile.setOnClickListener {
            val intent = Intent(requireContext(),EditProfile::class.java)
            activity?.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        editprofilewithsocial.setOnClickListener {
            val intent = Intent(requireContext(),EditProfile::class.java)
            activity?.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }

        complianlist.setOnClickListener {
           startActivity(Intent(requireContext(),ComplainList::class.java))
            activity!!.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }


    }

    private fun replaceFragmentToRight(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()

    }
    private fun replaceFragmentToLeft(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)

        fragmentTransaction?.commit()
        fragmentTransaction?.addToBackStack(null)

    }
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentcontainer,fragment)
        fragmentTransaction?.commit()
      //  fragmentTransaction?.addToBackStack(null)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri=data?.getData()
            if (uri != null) {
                uploadFile(uri!!);
            }



          //  imageset.setImageURI(uri)
        }
        if (requestCode === CAMERA_REQUEST && resultCode ==RESULT_OK) {
            uri=data?.getData()
            val photo = data?.getExtras()?.get("data") as Bitmap
            val uri2 = getImageUri(requireContext(),photo)
            if (uri2 != null) {
                d("De","Not Null")
                uploadFile(uri2!!); // uploads the file to the web service
            }else{
                d("De","Null")
            }
           // imageset.setImageBitmap(photo)
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
        if(requestCode == GALLERY_REQUEST){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                  val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  startActivityForResult(galleryIntent, GALLERY_REQUEST);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
            }
      /*  if(requestCode == MY_WRITE_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {
                //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
            }
        }*/


       /* if (!(grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(requireContext(), "Permission denied to access your location.", Toast.LENGTH_SHORT).show()
        }*/
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
        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST)

        }
        else{
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST)
        }

    }
    fun getData(){
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId","-")
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtilsMember.apiServiceMember

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),partnerId).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    try {
                    userIdTest = response.body()!!.resultData._id
                    d("ID",userIdTest)
                    var dataDate = response.body()!!.resultData.birthDate
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("th", "TH"))
                    d("DateCheck",inputFormat.format(Date()))
                        if (response.body()!!.resultData.birthDate != "") {
                             date = inputFormat.parse(response.body()!!.resultData.birthDate)
                            d("DateCheck2",response.body()!!.resultData.birthDate.substring(5,7))
                            birthday.text = Profilewithpicture.ConvertDate.ChangeFormatDate(dataDate.substring(0,4),dataDate.substring(5,7),dataDate.substring(8,10))

                            val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
                            d("DateCheck3",Profilewithpicture.ConvertDate.ChangeFormatDate(dataDate.substring(0,4),dataDate.substring(5,7),dataDate.substring(8,10)))
                        }


                       textEmail.text = response.body()!!.resultData.email
                      // memberid.text = response.body()!!.resultData._id


                       firstName.text = response.body()!!.resultData.firstName
                       lastName.text = response.body()!!.resultData.lastName

                       genderForChange = response.body()!!.resultData.gender
                       imageUrl = response.body()!!.resultData.image
                       val editor = sp?.edit()

                        sp!!.edit {
                            putString("firstName", response.body()!!.resultData.firstName)
                            putString("lastName", response.body()!!.resultData.lastName)
                           putString("user_id", response.body()!!.resultData._id)
                            putString("user_email", response.body()!!.resultData.email)
                        }

                       editor?.commit()
                       if(response.body()?.resultData?.birthDate == null){
                           birthday.setText("   -    -    -")
                       }
                       if(response.body()?.resultData?.firstName == null){
                           firstName.setText("-")
                       }
                       if(response.body()?.resultData?.lastName == null){
                           lastName.setText("-")
                       }
                       if(imageUrl !== null){
                           Glide.with(this@Profilewithpicture)
                                   .load(imageUrl)
                                   .into(imageset)
                       }


                       if (genderForChange == "Male") {
                           gendericon.scaleType = ImageView.ScaleType.CENTER_CROP
                           gendericon.setImageResource(R.drawable.male3x)
                       } else if (genderForChange == "Female") {
                           gendericon.scaleType = ImageView.ScaleType.CENTER_CROP
                           gendericon.setImageResource(R.drawable.female3x)
                       } else if (genderForChange == "LGBT") {
                           gendericon.scaleType = ImageView.ScaleType.CENTER_CROP
                           gendericon.setImageResource(R.drawable.group23x)
                       } else {
                           d("CheckValue", genderForChange)
                       }
                   }catch (e : Exception){
                        d("Exception",e.toString())
                   }
                }
            }
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                d("Exception",t.toString())

            }
        })



    }

    private fun signOutGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(requireActivity(), OnCompleteListener<Void> {



        })
    }
    private fun uploadFile(fileUri: Uri) {
        var file = File(getPath(fileUri));
        sharedPreferences = activity!!.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        val partnerId = sharedPreferences!!.getString("partnerId", "-")
        d("TestUpload", file.toString())
        var name = randomName()
        var listener = transferUtility!!.upload(spacename, name, file, CannedAccessControlList.PublicRead)
        TransferNetworkLossHandler.getInstance(requireContext())
        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object : TransferListener {
            override fun onError(id: Int, ex: Exception?) {
                d("S3 Upload", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                d("S3 Upload", "Progress ${((bytesCurrent / bytesTotal) * 100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    d("S3 Upload", "Completed")
                    d("Ocean", name)
                    var imagePath = "https://evolka-fwpapp-prod.sgp1.digitaloceanspaces.com/" + name
                    var imagebody = imageBody(imagePath)
                    mAPIService = ApiUtilsMember.apiServiceMember
                    val sdf = SimpleDateFormat("yyMMdd")
                    val currentDate = sdf.format(Date())
                    val r = (10..12).shuffled().first()

                    mAPIService!!.patchFile("Bearer " + token, Register.GenerateRandomString.randomString(22), "AND-" + currentDate + Register.GenerateRandomString.randomString(r), userIdTest!!, partnerId, imagebody).enqueue(object : Callback<UserProfile> {
                        override fun onResponse(call: Call<UserProfile>,
                                                response: Response<UserProfile>) {
//                            d("Pic",response.body()!!.resultData.image)
                            imageUrl = response.body()!!.resultData.image

                            try {
                                if (imageUrl !== null) {
                                    Glide.with(this@Profilewithpicture)
                                            .load(imageUrl)
                                            .into(imageset)
                                }
                            } catch (e: Exception) {

                            }
                        }

                        override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                            d("Pic", t.toString())
                        }
                    })

                }
            }
        })
    }
    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = activity?.getContentResolver()?.query(uri, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()
        return s
    }
    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun checkPermission() {

        if (checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_CAMERA_PERMISSION_CODE)
        }
    }

    override fun onResume() {
        getData()
        savelist.isEnabled = true
        super.onResume()
      /*  if(sp!!.getBoolean("LogIn_State",false)==false) {
            replaceFisrtFragment(LogIn())
        }else{
            replaceFisrtFragment(Profilewithpicture())
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        d("Arm","Destroyed")
    }


    object ConvertDate {
         fun ChangeFormatDate(year: String, month: String, day: String): String {
            var dayChanged: String? = null
            var monthChanged: String? = null
            var yearChanged: String? = null
            var yearChangedInt: Int? = null
            when (month) {
                "01" -> monthChanged = "ม.ค."
                "02" -> monthChanged = "ก.พ."
                "03" -> monthChanged = "มี.ค."
                "04" -> monthChanged = "เม.ย."
                "05" -> monthChanged = "พ.ค."
                "06" -> monthChanged = "มิ.ย."
                "07" -> monthChanged = "ก.ค."
                "08" -> monthChanged = "ส.ค."
                "09" -> monthChanged = "ก.ย."
                "10" -> monthChanged = "ต.ค."
                "11" -> monthChanged = "พ.ย."
                "12" -> monthChanged = "ธ.ค."

            }
            yearChangedInt = year.toInt()
            yearChangedInt += 543

            yearChanged = yearChangedInt.toString()
            dayChanged = day
            return monthChanged!! + " " + dayChanged + "," + yearChanged
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

}
