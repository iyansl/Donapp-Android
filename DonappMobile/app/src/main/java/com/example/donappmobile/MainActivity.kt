package com.example.donappmobile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.donappmobile.R.*

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private val fileChooser =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val resultCode = activityResult.resultCode
            val data: Intent? = activityResult.data
            if (resultCode == RESULT_OK) {
                data?.let {
                    val result = it.data
                    mUploadMessage?.onReceiveValue(arrayOf(result!!))
                    mUploadMessage = null
                }
            } else {
                mUploadMessage?.onReceiveValue(null)
                mUploadMessage = null
            }
        }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        webView = findViewById(id.webview)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                mUploadMessage?.onReceiveValue(null)
                mUploadMessage = filePathCallback
                val intent = fileChooserParams?.createIntent()
                fileChooser.launch(intent)
                return true
            }
        }
        webView.loadUrl("https://donapp-25d4421f1d6f.herokuapp.com/")
    }
}
