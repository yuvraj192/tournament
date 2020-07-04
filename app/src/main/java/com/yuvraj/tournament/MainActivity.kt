package com.yuvraj.tournament

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(loginView != null){
            val webSettings  = loginView!!.settings
            loginView.settings.javaScriptEnabled = true
            loginView!!.webViewClient = WebViewClient()
            loginView!!.webChromeClient = WebChromeClient()

            loginView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick (strl: String?) {
                    //Toast.makeText(this@MainActivity, strl, Toast.LENGTH_SHORT).show()

                }
            } , "valid" ) ;

            loginView!!.loadUrl("file:///android_asset/login.html")


            loginView!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
        }
    }
}