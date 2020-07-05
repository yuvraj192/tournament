package com.yuvraj.tournament

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.typeOf

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
                fun performClick (pno: String?) {
                    //Toast.makeText(applicationContext, pno, Toast.LENGTH_SHORT).show()
                    goHome()

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

    private fun goHome(){
        val intent = Intent(this, homeActivity::class.java)
        //intent.putExtra("user", usr)
        startActivity(intent);
        finish()
    }


    override fun onBackPressed() {
        if(loginView!!.canGoBack()){
            loginView!!.goBack()
        }else{
            super.onBackPressed()
        }
    }
}