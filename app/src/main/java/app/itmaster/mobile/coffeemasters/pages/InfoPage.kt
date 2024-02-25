package app.itmaster.mobile.coffeemasters.pages


import android.R
import android.app.Activity
import android.content.Context
import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoPage() {


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

    )

    { contentPadding ->

        AndroidView(
            factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient() // permite que ahora todos los links se sigan dentro de la app // permite que ahora todos los links se sigan dentro de la app
                    webChromeClient = object : WebChromeClient(){
                        override fun onJsAlert(
                            view: WebView,
                            url: String,
                            message: String,
                            result: JsResult
                        ): Boolean {
                            if (message == "showSnackbar") { // Corrected the comparison operator
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Thank you !!, you feedback was received",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                result.confirm() // Confirm the alert
                                return true
                            }
                            return super.onJsAlert(view, url, message, result)
                        }

                    }
                    loadUrl("file:///android_asset/index.html")
                    addJavascriptInterface(MyJsInterface(context), "android")

                }
            })


    }

}



class MyJsInterface(private val mContext: Context) {
    @JavascriptInterface
    fun getAndroidVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    @JavascriptInterface
    fun showToast() {
        Toast.makeText(mContext, "Feedback received", Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun showSnackbar(message: String?) {
        if (mContext is Activity) {
            val activity = mContext
            activity.runOnUiThread {
                make(
                    activity.findViewById(R.id.content),
                    message!!, Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


}


