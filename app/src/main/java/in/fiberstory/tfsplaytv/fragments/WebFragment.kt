package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.network.APIInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.HttpAuthHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.leanback.app.BrowseSupportFragment

class WebFragment : BrowseSupportFragment() {
    private var webNews: WebView? = null
    private var bundle: Bundle? = null
    private var webLink: String? = null
    private var web_loading: ImageView? = null
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        bundle = arguments
        web_loading = v.findViewById<View>(R.id.web_loading) as ImageView
        webNews = v.findViewById<View>(R.id.webView) as WebView
        webNews!!.isVerticalScrollBarEnabled = true
        webNews!!.isHorizontalScrollBarEnabled = true
        if (bundle != null) {
            webLink = bundle!!.getString("DATA")
        }
        if (webLink!!.isNotEmpty()) {
            webNews!!.settings.javaScriptEnabled = true
            webNews!!.settings.allowFileAccess = true
            webNews!!.settings.displayZoomControls = false
            webNews!!.settings.pluginState = WebSettings.PluginState.ON
            val webSettings = webNews!!.settings
            webSettings.builtInZoomControls = true
            webNews!!.isVerticalScrollBarEnabled = true
            webNews!!.isHorizontalScrollBarEnabled = true
            webNews!!.settings.loadWithOverviewMode = true
            webNews!!.settings.useWideViewPort = true
            webNews!!.settings.domStorageEnabled = true
            webNews!!.settings.loadsImagesAutomatically = true
            webNews!!.settings.pluginState = WebSettings.PluginState.ON
            webNews!!.settings.allowContentAccess = true
            webNews!!.webViewClient = Callback() //HERE IS THE MAIN CHANGE
            webNews!!.loadUrl(webLink!!)
        }
    }

    private inner class Callback : WebViewClient() {
        //HERE IS THE MAIN CHANGE.
        init {
            web_loading!!.visibility = View.VISIBLE
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
            web_loading!!.visibility = View.GONE
        }

        override fun onReceivedHttpAuthRequest(
            view: WebView,
            handler: HttpAuthHandler, host: String, realm: String
        ) {
            handler.proceed(APIInterface.AUTH_USER_NAME, APIInterface.AUTH_PASSWORD)
        }
    }
}