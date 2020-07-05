package com.yuvraj.tournament

import android.graphics.Bitmap
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*

class homeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if(homeView != null || btmBar != null){
            val homeSettings  = homeView!!.settings
            val btmSettings = btmBar!!.settings

            homeView.settings.javaScriptEnabled = true
            homeView!!.webViewClient = WebViewClient()
            homeView!!.webChromeClient = WebChromeClient()

            btmBar.settings.javaScriptEnabled = true
            btmBar!!.webViewClient = WebViewClient()
            btmBar!!.webChromeClient = WebChromeClient()

            homeView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick (strl: String?) {
                    Toast.makeText(applicationContext, strl, Toast.LENGTH_SHORT).show()

                }
            } , "valid" ) ;

            homeView!!.loadUrl("file:///android_asset/home.html")
            btmBar!!.loadUrl("file:///android_asset/btmbar.html")


            homeView!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
                override public fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    handler?.proceed(); // Ignore SSL certificate errors
                }
            }

            btmBar!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
        }
    }
    override fun onBackPressed() {
        if(homeView!!.canGoBack()){
            homeView!!.goBack()
        }else{
            super.onBackPressed()
        }
    }
}