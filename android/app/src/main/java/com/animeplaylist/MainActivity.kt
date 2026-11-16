package com.animeplaylist

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.animeplaylist.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView: WebView = binding.webView
        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.allowFileAccess = false
        settings.allowContentAccess = false
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        loadAppHtml(webView)
    }

    /** Fetch the HTML and explicitly load it as text/html. The raw GitHub URL
     * may otherwise be interpreted by WebView as plain text, showing source code.
     */
    private fun loadAppHtml(webView: WebView) {
        Thread {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(APP_URL).openConnection() as HttpURLConnection
                connection.connectTimeout = 15_000
                connection.readTimeout = 15_000
                connection.requestMethod = "GET"

                val status = connection.responseCode
                if (status !in 200..299) {
                    throw IllegalStateException("Server returned HTTP $status")
                }

                val html = connection.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
                runOnUiThread {
                    webView.loadDataWithBaseURL(
                        APP_BASE_URL,
                        html,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            } catch (error: Exception) {
                runOnUiThread {
                    val message = "App load nahi ho paya. Internet connection check karke dobara kholiye."
                    webView.loadDataWithBaseURL(
                        null,
                        "<html><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><body style=\"font:16px sans-serif;padding:24px\"><h2>Meri Anime List</h2><p>$message</p><button onclick=\"location.reload()\">Dobara koshish karein</button></body></html>",
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            } finally {
                connection?.disconnect()
            }
        }.start()
    }

    @Deprecated("Deprecated in Android API; retained for compatibility")
    override fun onBackPressed() {
        if (::binding.isInitialized && binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private companion object {
        const val APP_BASE_URL = "https://raw.githubusercontent.com/shawpreet6-a11y/Anime-playlist-/main/"
        const val APP_URL = APP_BASE_URL + "index.html"
    }
}
