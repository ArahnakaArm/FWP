package com.example.deimos.fwp

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import kotlinx.android.synthetic.main.videocontent.*
import android.support.v4.media.session.MediaControllerCompat.setMediaController
import android.text.Html
import android.util.Log.d
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import android.webkit.WebChromeClient
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.locationcontent.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
data class VideoById(var resultCode : String,var developerMessage : String ,var resultData : resultVideo)
data class resultVideo(var videoName : nameVideo , var videoDescription : videoDescriptionId,var videoLink : String,var categoryId : category,
                       var updatedAt : String,var viewCount : Int)
data class nameVideo(var en : String,var th : String)
data class videoDescriptionId(var en : String,var th : String)
data class category(var categoryName : cateName)
data class cateName(var en : String,var th :String)


class VideoContent : YouTubeBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocontent)
        var mYouTubePlayerView: YouTubePlayerView?=null
        var VideoId : String?=null
        var mAPIService: ApiService? = null
        var mOnInitializedListener: YouTubePlayer.OnInitializedListener?=null
        var bundle : Bundle ? =intent.extras

        var _id = bundle!!.getString("ID")

        mAPIService = ApiUtils.apiService
        val partnerId = "5dbfe99c776a690010deb237"
        val sdf = SimpleDateFormat("yyMMdd")
        val currentDate = sdf.format(java.util.Date())
        val r = (10..12).shuffled().first()
        mAPIService!!.getVideoById(Register.GenerateRandomString.randomString(22),"AND-"+currentDate+ Register.GenerateRandomString.randomString(r),_id!!,partnerId).enqueue(object : Callback<VideoById> {

            override fun onResponse(call: Call<VideoById>, response: Response<VideoById>) {
//                d("location",response.body()!!.resultData!!.toString())
                 VideoId = response.body()!!.resultData.videoLink.substringAfterLast("=")
                categorycontent.setText(response.body()!!.resultData.categoryId.categoryName.th)

                videotitle.setText(response.body()!!.resultData.videoName.th)
                date.setText(response.body()!!.resultData.updatedAt.substring(0..9))
                descripvideo.setText(response.body()!!.resultData.videoDescription.th)
                viewValue.setText(response.body()!!.resultData.viewCount.toString())


                d("VideoId",VideoId)

                mYouTubePlayerView = findViewById<View>(R.id.youtubeplayer) as YouTubePlayerView

                mOnInitializedListener = object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {

                        youTubePlayer.loadVideo(VideoId)
                    }

                    override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {

                    }
                }

                mYouTubePlayerView!!.initialize("AIzaSyAzPVSpG9bv1Wav2rld5ecRYzipe1qGnvU", mOnInitializedListener)





            }


            override fun onFailure(call: Call<VideoById>, t: Throwable) {
                d("arm","onFailure")
            }

        })











        backprees.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}