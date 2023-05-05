package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.network.APIInterface
import `in`.fiberstory.tfsplaytv.utility.CustomProgressDialog
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity


class WebActivity : FragmentActivity() {
    var webview: WebView? = null
    var url: String? = null
    var full_layout: RelativeLayout? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        url = intent.getStringExtra("DATA")
        full_layout = findViewById(R.id.full_layout)
        webview = findViewById(R.id.webview)
        webview!!.isVerticalScrollBarEnabled = true
        webview!!.isHorizontalScrollBarEnabled = true

        if(url?.isNotEmpty() == true){
            webview!!.settings.javaScriptEnabled = true
            webview!!.settings.allowFileAccess = true
            webview!!.settings.displayZoomControls = false
            webview!!.settings.pluginState = WebSettings.PluginState.ON
            val webSettings = webview!!.settings
            webSettings.builtInZoomControls = true
            webview!!.isVerticalScrollBarEnabled = true
            webview!!.isHorizontalScrollBarEnabled = true
            webview!!.settings.loadWithOverviewMode = true
            webview!!.settings.useWideViewPort = true
            webview!!.settings.domStorageEnabled = true
            webview!!.settings.loadsImagesAutomatically = true
            webview!!.settings.pluginState = WebSettings.PluginState.ON
            webview!!.settings.allowContentAccess = true
            webview!!.webViewClient = Callback() //HERE IS THE MAIN CHANGE
            url.let {
                if (it != null) {
                    webview?.loadUrl(it)
                }
            }
        }
//        try {
//            webview?.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(
//                    view: WebView,
//                    request: WebResourceRequest
//                ): Boolean {
//                    return super.shouldOverrideUrlLoading(view, request)
//                }
//
//                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//                    super.onPageStarted(view, url, favicon)
//                }
//
//                override fun onPageFinished(view: WebView, url: String) {
//                    // hide print and view logs button by class name
//                    HideLoader()
//                    full_layout?.visibility = View.GONE
//                    webview?.visibility = View.VISIBLE
//                    //                    Log.d("ganesh", url);
//                }
//            }
//            webview?.webChromeClient = MyChrome()
//
//            url.let {
//                if (it != null) {
//                    webview?.loadUrl(it)
//                }
//            }
////            webview?.loadUrl(url)
//            //            webview.getSettings().setLoadWithOverviewMode(true);
//            webview?.settings?.useWideViewPort = true
//            //            webview.getSettings().setBuiltInZoomControls(true);
////            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            webview?.settings?.domStorageEnabled = true
//            webview?.settings?.javaScriptEnabled = true
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    internal inner class MyChrome : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(
                applicationContext.resources,
                2130837573
            )
        }

        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: CustomViewCallback
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
            mOriginalOrientation = requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (window.decorView as FrameLayout).addView(mCustomView, FrameLayout.LayoutParams(-1, -1))
            window.decorView.systemUiVisibility = 3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    fun Showloader() {
        customProgressDialog = CustomProgressDialog.newInstance()
        val ft = supportFragmentManager.beginTransaction()
        customProgressDialog?.show(ft, "dialog")
        customProgressDialog?.setCancelable(false)
    }

    fun HideLoader() {
        if (customProgressDialog != null) customProgressDialog?.dismiss()
    }

    companion object {
        var customProgressDialog: CustomProgressDialog? = null
    }

    private inner class Callback : WebViewClient() {
        //HERE IS THE MAIN CHANGE.
        init {
            Showloader()
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            HideLoader()
            webview?.visibility = View.VISIBLE
        }

        override fun onReceivedHttpAuthRequest(
            view: WebView,
            handler: HttpAuthHandler, host: String, realm: String
        ) {
            handler.proceed(APIInterface.AUTH_USER_NAME, APIInterface.AUTH_PASSWORD)
        }
    }

}