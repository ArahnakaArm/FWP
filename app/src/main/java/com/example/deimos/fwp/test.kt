package com.example.deimos.fwp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button

import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class test : YouTubeBaseActivity() {
    var mYouTubePlayerView: YouTubePlayerView?=null
    var mOnInitializedListener: YouTubePlayer.OnInitializedListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testvideo)

        mYouTubePlayerView = findViewById<View>(R.id.youtubeplay) as YouTubePlayerView

        mOnInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {

                youTubePlayer.cueVideo("VhqJT1HcWt0")
            }

            override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {

            }
        }

        mYouTubePlayerView!!.initialize("AIzaSyAzPVSpG9bv1Wav2rld5ecRYzipe1qGnvU", mOnInitializedListener)
    }
}
