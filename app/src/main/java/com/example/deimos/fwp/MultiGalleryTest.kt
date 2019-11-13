package com.example.deimos.fwp

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.util.Log
import android.util.Log.d

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.profiewithpicture.*
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

class MultiGalleryTest : AppCompatActivity() {
    var sp: SharedPreferences? = null
    private var btn: Button? = null
    private var PICK_IMAGE_MULTIPLE = 1
    private var imageEncoded: String? = null
    private var imagesEncodedList: MutableList<String>? = null
    private var gvGallery: GridView? = null
    var token : String?=null
    private val SELECT_IMAGE = 1338
    private  val REQUEST_PICK_PHOTO = 1
    private val GALLERY_REQUEST = 1889
    private val MY_WRITE_PERMISSION_CODE = 200
    private var galleryAdapter: GalleryAdapter? = null
    var mAPIService: ApiService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multigallery)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_PERMISSION_CODE);
        }


        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        btn = findViewById(R.id.btn)
        gvGallery = findViewById<View>(R.id.gv) as GridView

        btn!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, REQUEST_PICK_PHOTO)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        var uri : Uri
        if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK && data != null) {
            var fileUris: ArrayList<Uri> = ArrayList<Uri>()
            var clipData = data.clipData
            if(data.clipData!=null){

                for (i in 0 until data?.clipData!!.itemCount) {
                    var item = clipData.getItemAt(i)
                    uri = item.getUri()
                    //d("Testt",uri.toString())
                   importPhoto(uri)

                    // return s

                }
            }else{
                val uri = data?.data
                importPhoto(uri)
            }



        }
    }
    private fun uploadFile(file: File) {
       // d("Testt",fileUri.toString())
       // var file = File(getPath(fileUri));
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
            val compliansId = "5db6b397dbb3640010e21f17"
            val service = retrofit.create(ApiService::class.java!!)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("image", file.getName(), requestFile)
            val descriptionString = "Sample description"
            val description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString)
            mAPIService!!.postComplianImage("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),compliansId!!,body).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>,
                                        response: Response<UserProfile>) {
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
                    d("Pic",t.toString())
                }
            })
        }
        //}
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = getContentResolver()?.query(uri, projection, null, null, null) ?: return null
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s = cursor.getString(column_index)
        cursor.close()

        return s
    }
    fun isImage(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri) ?: return true
        return mimeType.startsWith("image/")
    }

    private fun createImageFile(dir: File, fileName: String? = null): File {
        if (fileName == null) {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-kkmmss"))
            d("FileName"," "+dir)
            uploadFile(File.createTempFile("IMG_$timestamp", ".jpg", dir))
            return File.createTempFile("IMG_$timestamp", ".jpg", dir)
        }
        d("FileName",fileName)
        return File(dir, fileName)
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

    fun importPhoto(uri: Uri): Boolean {
        if (!isImage(this@MultiGalleryTest, uri)) {
            // not image
            return false
        }

        return try {
            val photoFile = createImageFile(this@MultiGalleryTest.cacheDir)
            copyUriToFile(this@MultiGalleryTest, uri, photoFile)

            // addImageToGallery(photoFile)
            true
        }
        catch (e: IOException) {
            e.printStackTrace()
            // handle error
            false
        }
    }
    private fun uploadAlbum(fileUri: List<Uri>){
        var parts : ArrayList<Uri> = ArrayList()
        for(i in 0 until fileUri.size){
            parts.add(fileUri[i])
        }
        for(i in 0 until parts.size){
            d("TestNum",parts[i].toString())
          //  uploadFile(parts[i])
        }
    }

}