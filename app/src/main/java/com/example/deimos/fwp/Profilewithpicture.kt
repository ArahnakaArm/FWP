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
class Profilewithpicture : androidx.fragment.app.Fragment() {
    private val CAMERA_REQUEST = 1888
    private var uri:Uri?=null
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
    var usr : Userstate = Userstate()
    var usra : UserProfile?=null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    var userType : String?=null
    var token : String?=null
    var web: WebView? = null
    var imagePath : String?=null

    var image_name : String?=null
    var sp: SharedPreferences? = null
    var mAPIService: ApiService? = null
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

        getData()
        FWP.setOnClickListener {
            val intent =Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://futureforwardparty.org/join"));

                    startActivity(intent);
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

            replaceFragmentToRight(FragmentBookmark())
        }
        changepasswordbut.setOnClickListener {
            val intent = Intent(requireContext(),ChangePassword::class.java)
            activity?.startActivity(intent)
        }

        editprofile.setOnClickListener {
            val intent = Intent(requireContext(),EditProfile::class.java)
            activity?.startActivity(intent)
        }
        editprofilewithsocial.setOnClickListener {
            val intent = Intent(requireContext(),EditProfile::class.java)
            activity?.startActivity(intent)
        }

        setting.setOnClickListener {
            replaceFragmentToRight(ComplainList())
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
                uploadFile(uri!!); // uploads the file to the web service
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
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtils.apiService

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r)).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    var dataDate = response.body()!!.resultData.birthDate
                    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("th", "TH"))
                    d("DateCheck",inputFormat.format(Date()))
                    val date = inputFormat.parse(response.body()!!.resultData.birthDate)
                    d("DateCheck2",response.body()!!.resultData.birthDate.substring(5,7))
                    d("DateCheck3",ChangeFormatDate(dataDate.substring(0,4),dataDate.substring(5,7),dataDate.substring(8,10)))
                    val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)

                   try {
                       textEmail.text = response.body()!!.resultData.email
                      // memberid.text = response.body()!!.resultData._id
                       userIdTest = response.body()!!.resultData._id
                       firstName.text = response.body()!!.resultData.firstName
                       lastName.text = response.body()!!.resultData.lastName
                      birthday.text = ChangeFormatDate(dataDate.substring(0,4),dataDate.substring(5,7),dataDate.substring(8,10))
                       genderForChange = response.body()!!.resultData.gender
                       imageUrl = IMAGE_URL+response.body()!!.resultData.image.path
                       val editor = sp?.edit()
                       editor?.putString("user_id", response.body()!!.resultData._id)
                       editor?.putString("user_email", response.body()!!.resultData.email)
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

                   }
                }
            }
            override fun onFailure(call: Call<UserProfile>, t: Throwable) {

            }
        })



    }
    fun getDataGoogle(){
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtils.apiService

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r)).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    textEmail.text = response.body()!!.resultData.email
                  //  memberid.text = response.body()!!.resultData._id
                    firstName.text = response.body()!!.resultData.firstName
                    lastName.text = response.body()!!.resultData.lastName
                    birthday.text = response.body()!!.resultData.birthDate
                    genderForChange=response.body()!!.resultData.gender
                    val editor = sp?.edit()
                    editor?.putString("user_id",response.body()!!.resultData._id)
                    editor?.putString("user_email",response.body()!!.resultData.email)
                    editor?.commit()


                    if (genderForChange == "Male"){
                        gendericon.scaleType= ImageView.ScaleType.CENTER_CROP
                        gendericon.setImageResource(R.drawable.male)
                    }else if (genderForChange == "Female"){
                        gendericon.scaleType= ImageView.ScaleType.CENTER_CROP
                        gendericon.setImageResource(R.drawable.female)
                    }else if(genderForChange == "LGBT"){
                        gendericon.scaleType= ImageView.ScaleType.CENTER_CROP
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
    fun getDataFacebook(){
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
        mAPIService = ApiUtils.apiService

        mAPIService!!.getUser("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r)).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {

                if (response.isSuccessful()) {
                    textEmail.text = response.body()!!.resultData.email
                  //  memberid.text = response.body()!!.resultData._id
                    firstName.text = response.body()!!.resultData.firstName
                    lastName.text = response.body()!!.resultData.lastName
                    birthday.text = response.body()!!.resultData.birthDate
                    genderForChange=response.body()!!.resultData.gender
                    val editor = sp?.edit()
                    editor?.putString("user_id",response.body()!!.resultData._id)
                    editor?.putString("user_email",response.body()!!.resultData.email)
                    editor?.commit()


                    if (genderForChange == "Male"){
                        gendericon.scaleType= ImageView.ScaleType.CENTER_CROP
                        gendericon.setImageResource(R.drawable.male)
                    }else if (genderForChange == "Female"){
                        gendericon.scaleType= ImageView.ScaleType.CENTER_CROP
                        gendericon.setImageResource(R.drawable.female)
                    }else if(genderForChange == "LGBT"){
                        gendericon.scaleType= ImageView.ScaleType.CENTER_CROP
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
    private fun signOutGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(requireActivity(), OnCompleteListener<Void> {



        })
    }
    private fun uploadFile(fileUri: Uri) {
        var file = File(getPath(fileUri));
        mAPIService = ApiUtils.apiService
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(Date())
        val r = (10..12).shuffled().first()
       // val filePath = getImageFilePath(fileUri)
       // if (filePath != null && !filePath!!.isEmpty()) {
           // val file = File(filePath)
            if (file.exists()) {

                val retrofit = Retrofit.Builder()
                        .baseUrl("http://206.189.41.105:1210/api/v1/")
                        .build()

                val service = retrofit.create(ApiService::class.java!!)
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val body = MultipartBody.Part.createFormData("image", file.getName(), requestFile)
                val descriptionString = "Sample description"
                val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
                mAPIService!!.postFile("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),userIdTest!!,body).enqueue(object : Callback<UserProfile> {
                    override fun onResponse(call: Call<UserProfile>,
                                            response: Response<UserProfile>) {
                        d("Pic",response.body()!!.resultData.image.path)
                        imageUrl = IMAGE_URL+response.body()!!.resultData.image.path

                       try {
                           if (imageUrl !== null) {
                               Glide.with(this@Profilewithpicture)
                                       .load(imageUrl)
                                       .into(imageset)
                           }
                       }catch (e : Exception){

                       }
                    }

                    override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                        d("Pic",t.toString())
                    }
                })
            }
        //}
    }

   /* fun getRealPathFromUri(uri: Uri): String? {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(requireContext(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return "${Environment.getExternalStorageDirectory()}/${split[1]}"
                }
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(requireContext(), contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]

                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf<String>(split[1])

                return getDataColumn(requireContext(), contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(requireContext(), uri, null, null)

        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }// File
        // MediaStore (and general)

        return null
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                              selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                val index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.getAuthority()
    }
*/
    fun getImageFilePath(uri: Uri): String? {
        var path: String? = null
        var image_id: String? = null
        var cursor : Cursor?=null
             cursor = activity?.getContentResolver()?.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor!!.moveToFirst()
            image_id = cursor!!.getString(0)
            image_id = image_id!!.substring(image_id.lastIndexOf(":") + 1)
            cursor!!.close()
        }

       cursor = activity?.getContentResolver()?.query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", arrayOf<String>(image_id!!), null)
        if (cursor != null) {
            cursor!!.moveToFirst()
            path = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor!!.close()
        }
        return path
    }
    public fun replaceFisrtFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentcontainer, fragment)
        fragmentTransaction?.commit()


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
        super.onResume()
        d("Profile","TEsssssssssssssss")
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

    /*protected override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("key_user", Parcels.wrap(mUser))
    }

    protected fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mUser = Parcels.unwrap(savedInstanceState.getParcelable<Parcelable>("key_user"))

        val data = "Github Name :" + mUser.getName() +
                "\nWebsite :" + mUser.getBlog() +
                "\nCompany Name :" + mUser.getCompany()
        tvResult.setText(data)
        //แสดง layout result
        layoutForm.setVisibility(View.GONE)
        layoutResult.setVisibility(View.VISIBLE)
        layoutProgress.setVisibility(View.GONE)
    }
*/
    private fun ChangeFormatDate(year : String,month : String , day : String):String{
        var dayChanged : String?=null
        var monthChanged : String?=null
        var yearChanged : String?=null
        var yearChangedInt : Int?=null
        when(month) {
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
