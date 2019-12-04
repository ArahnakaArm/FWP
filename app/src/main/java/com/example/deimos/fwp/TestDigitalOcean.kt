package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.internal.StaticCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client

import androidx.appcompat.app.AppCompatActivity
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.services.s3.model.CannedAccessControlList
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.testdigitalocean.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

interface SpaceRegionRepresentable2 {
    fun endpoint(): String
}

/**
 * Represents a region in which a Digital Ocean Space can be created
 */
enum class SpaceRegion2: SpaceRegionRepresentable {
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



class TestDigitalOcean : AppCompatActivity() {
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testdigitalocean)
        d("Ocean",randomName())
        val credentials = StaticCredentialsProvider(BasicAWSCredentials(accesskey, secretkey))
        val client = AmazonS3Client(credentials, Region.getRegion("us-east-1"))
        client.endpoint = spaceregion.endpoint()
        transferUtility = TransferUtility.builder().s3Client(client).context(applicationContext).build()
        appContext = this@TestDigitalOcean

        btn_upload.setOnClickListener {
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
                    fileupload = File(filePath[i])
                    d("Ocean", fileupload!!.length().toString())
                    d("Ocean", fileupload!!.name)
                    uploadExampleFile(fileupload!!)
                }
            }


        }
    }

    fun uploadExampleFile(file:File){
        var name =  randomName()
        //Starts the upload of our file
        var listener = transferUtility!!.upload(spacename,  name, file,CannedAccessControlList.PublicRead)
        TransferNetworkLossHandler.getInstance(this@TestDigitalOcean)
        //Listens to the file upload progress, or any errors that might occur
        listener.setTransferListener(object: TransferListener {
            override fun onError(id: Int, ex: Exception?) {
                d("S3 Upload", ex.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                d("S3 Upload", "Progress ${((bytesCurrent/bytesTotal)*100)}")
            }

            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED){
                    d("S3 Upload", "Completed")
                    d("Ocean", name)
                }
            }
        })
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