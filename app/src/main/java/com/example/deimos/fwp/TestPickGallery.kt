package com.example.deimos.fwp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.util.Log
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat

import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import droidninja.filepicker.PickerManager.setMaxCount
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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TestPickGallery : AppCompatActivity() {
    internal var filePath = ArrayList<String>()
    private var upload: Button? = null
    var sp: SharedPreferences? = null
    var token : String?=null
    private val MY_WRITE_PERMISSION_CODE = 200
    var id : String?=null
    var mAPIService: ApiService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        token = sp!!.getString("user_token","-")
        id="5dc0fe5cad39a90010dcb553"
        setContentView(R.layout.newpick)
        upload = findViewById(R.id.upButton)
        upload!!.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this@TestPickGallery, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_WRITE_PERMISSION_CODE);
            }

            if (ContextCompat.checkSelfPermission(this@TestPickGallery, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), MY_WRITE_PERMISSION_CODE);
            }



            FilePickerBuilder.instance.setMaxCount(9)
                    .setSelectedFiles(filePath)
                    .enableCameraSupport(false)
                    .setActivityTheme(R.style.LibAppTheme)
                    .pickPhoto(this@TestPickGallery)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FilePickerConst
                    .REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                filePath = ArrayList()
                filePath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA))
                val ss = Uri.fromFile(File(filePath[0].toString()))
                Log.d("Arm", ss.toString())
                Log.d("Arm", filePath[0].toString())
                importPhoto(ss)
            }
        }

    }
    fun importPhoto(uri: Uri): Boolean {
        if (!isImage(this@TestPickGallery,uri)) {
            // not image
            return false
        }

        return try {
            val photoFile = createImageFile(cacheDir)
            copyUriToFile( this@TestPickGallery,uri, photoFile)

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

    private fun createImageFile(dir: File, fileName: String? = null): File {
        if (fileName == null) {
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-kkmmss"))
            Log.d("FileName", " " + dir)
            uploadFile(File.createTempFile("IMG_$timestamp", ".jpg", dir))
            return File.createTempFile("IMG_$timestamp", ".jpg", dir)
        }
        Log.d("FileName", fileName)
        return File(dir, fileName)
    }
    fun isImage(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri) ?: return true
        return mimeType.startsWith("image/")
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
            mAPIService!!.postComplianImage("Bearer "+token,Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),id!!,body).enqueue(object : Callback<UserProfile> {
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
                    Log.d("Pic", t.toString())
                }
            })
        }
        //}
    }
}
