package io.iostwin.iostdex.module.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toolbar
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.R

@RouterUri(path = ["/web"])
class WebActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        toolbar = findViewById(R.id.toolbar)
        val webView = findViewById<WebView>(R.id.webView)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                toolbar.title = title
            }
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                webView.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(intent.data!!.getQueryParameter("url"))
    }
}
