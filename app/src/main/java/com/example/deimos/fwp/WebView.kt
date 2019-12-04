package com.example.deimos.fwp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.webview.*

class WebView : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview)
        webView.loadUrl("https://futureforwardparty.org/join")

        backarrow.setOnClickListener {
            finish()
            this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)

        }
    }

    override fun onBackPressed() {
        finish()
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        super.onBackPressed()

    }

}