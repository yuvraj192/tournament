package com.yuvraj.tournament

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var verificationId: String? = null
    private val mAuth: FirebaseAuth? = null

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
                fun performClick (pno: String) {
                    //Toast.makeText(applicationContext, pno, Toast.LENGTH_SHORT).show()

                    sendVerificationCode("+91"+pno)

                }
            } , "valid" ) ;

            loginView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick (code: String) {
                    //Toast.makeText(applicationContext, pno, Toast.LENGTH_SHORT).show()
                    verifyCode(code.trim())
                }
            } , "verify" ) ;

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

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signIn(credential)
    }

    private fun signIn(credential: PhoneAuthCredential) {
        Toast.makeText(applicationContext, "HERE", Toast.LENGTH_LONG).show()
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    val intent =
                        Intent(this@MainActivity, homeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun sendVerificationCode(phonenumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenumber,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallb
        )
    }

    private val mCallb: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
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