package com.example.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.DownloadListener
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import java.io.ByteArrayInputStream
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.TabItem
import com.example.viewmodel.NavigationCommand

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(
    tab: TabItem,
    navigationCommand: NavigationCommand?,
    onNavigationHandled: () -> Unit,
    onPageStarted: (String) -> Unit,
    onPageFinished: (url: String, title: String?, canGoBack: Boolean, canGoForward: Boolean) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onPageSnippetExtracted: (String) -> Unit,
    onDownloadRequested: (fileName: String, url: String, mimeType: String, sizeBytes: Long) -> Unit,
    onGoHome: () -> Unit,
    isShieldEnabled: Boolean = true,
    onTrackerBlocked: () -> Unit = {},
    onFindResult: (activeOrdinal: Int, numberOfMatches: Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Handle incoming navigation commands
    LaunchedEffect(navigationCommand) {
        val webView = webViewRef ?: return@LaunchedEffect
        when (navigationCommand) {
            is NavigationCommand.LoadUrl -> {
                hasError = false
                webView.loadUrl(navigationCommand.url)
                onNavigationHandled()
            }
            is NavigationCommand.GoBack -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    onGoHome()
                }
                onNavigationHandled()
            }
            is NavigationCommand.GoForward -> {
                if (webView.canGoForward()) {
                    webView.goForward()
                }
                onNavigationHandled()
            }
            is NavigationCommand.Reload -> {
                hasError = false
                webView.reload()
                onNavigationHandled()
            }
            is NavigationCommand.ClearCache -> {
                webView.clearCache(true)
                onNavigationHandled()
            }
            is NavigationCommand.FindInPage -> {
                if (navigationCommand.query.isNotBlank()) {
                    webView.findAllAsync(navigationCommand.query)
                    if (!navigationCommand.forward) {
                        webView.findNext(false)
                    }
                }
                onNavigationHandled()
            }
            is NavigationCommand.ClearMatches -> {
                webView.clearMatches()
                onNavigationHandled()
            }
            is NavigationCommand.SetTextZoom -> {
                webView.settings.textZoom = navigationCommand.zoom
                onNavigationHandled()
            }
            null -> {}
        }
    }

    var reloadKey by remember(tab.id) { mutableStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        key(tab.id, reloadKey) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("browser_webview"),
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            allowFileAccess = true
                            allowContentAccess = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

                            if (tab.isDesktopMode) {
                                userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36 Edg/128.0.0.0"
                            } else {
                                userAgentString = null
                            }
                            textZoom = tab.textZoom

                            if (tab.isIncognito) {
                                cacheMode = WebSettings.LOAD_NO_CACHE
                                CookieManager.getInstance().setAcceptCookie(false)
                            } else {
                                cacheMode = WebSettings.LOAD_DEFAULT
                                CookieManager.getInstance().setAcceptCookie(true)
                            }
                        }

                        setFindListener { activeOrdinal, numberOfMatches, _ ->
                            onFindResult(activeOrdinal, numberOfMatches)
                        }

                        webViewClient = object : WebViewClient() {
                            override fun shouldInterceptRequest(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): WebResourceResponse? {
                                if (isShieldEnabled) {
                                    val host = request?.url?.host?.lowercase() ?: ""
                                    val isAdOrTracker = listOf(
                                        "doubleclick", "googlesyndication", "adservice.google",
                                        "adnxs", "scorecardresearch", "taboola", "outbrain",
                                        "amazon-adsystem", "criteo", "popads", "analytics.google"
                                    ).any { host.contains(it) }

                                    if (isAdOrTracker) {
                                        onTrackerBlocked()
                                        return WebResourceResponse(
                                            "text/plain",
                                            "UTF-8",
                                            ByteArrayInputStream(ByteArray(0))
                                        )
                                    }
                                }
                                return super.shouldInterceptRequest(view, request)
                            }
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                hasError = false
                                if (url != null) {
                                    onPageStarted(url)
                                }
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                if (url != null) {
                                    onPageFinished(
                                        url,
                                        view?.title,
                                        view?.canGoBack() == true,
                                        view?.canGoForward() == true
                                    )

                                    // Extract clean text snippet for Nova AI context
                                    view?.evaluateJavascript(
                                        "(function() { return document.body ? document.body.innerText.substring(0, 4000) : ''; })();"
                                    ) { result ->
                                        val cleanText = result
                                            ?.replace("^\"|\"$".toRegex(), "")
                                            ?.replace("\\n", "\n")
                                            ?.replace("\\\"", "\"")
                                            ?: ""
                                        onPageSnippetExtracted(cleanText)
                                    }
                                }
                            }

                            override fun onRenderProcessGone(
                                view: WebView?,
                                detail: RenderProcessGoneDetail?
                            ): Boolean {
                                hasError = true
                                val didCrash = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    detail?.didCrash() == true
                                } else false
                                errorMessage = if (didCrash) {
                                    "El motor de renderizado se reinició. Pulsa 'Reintentar' para recargar."
                                } else {
                                    "El sistema liberó recursos del renderizador. Pulsa 'Reintentar' para recargar."
                                }
                                try {
                                    (view?.parent as? ViewGroup)?.removeView(view)
                                    view?.destroy()
                                } catch (_: Exception) {}
                                webViewRef = null
                                return true
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                if (request?.isForMainFrame == true) {
                                    hasError = true
                                    val desc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        error?.description?.toString() ?: "Error de red"
                                    } else {
                                        "Error al conectar con la página"
                                    }
                                    errorMessage = desc
                                }
                            }

                            override fun onReceivedSslError(
                                view: WebView?,
                                handler: SslErrorHandler?,
                                error: SslError?
                            ) {
                                // Proceed for seamless exploration, or prompt
                                handler?.proceed()
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                onProgressChanged(newProgress)
                            }
                        }

                        setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
                            val guessedFileName = android.webkit.URLUtil.guessFileName(url, contentDisposition, mimetype)
                            onDownloadRequested(guessedFileName, url, mimetype, contentLength)
                        })

                        if (tab.url.isNotBlank() && tab.url != "about:blank") {
                            loadUrl(tab.url)
                        }

                        webViewRef = this
                    }
                },
                update = { webView ->
                    // Ensure initial load if needed
                    if (webView.url.isNullOrBlank() && tab.url.isNotBlank() && tab.url != "about:blank") {
                        webView.loadUrl(tab.url)
                    }
                }
            )
        }

        // Error Screen overlay with retry
        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Error al cargar la página",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (errorMessage.isNotBlank()) errorMessage else "Comprueba tu conexión a internet o la dirección ingresada.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            hasError = false
                            val webView = webViewRef
                            if (webView != null) {
                                webView.reload()
                            } else {
                                reloadKey++
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("browser_error_retry_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reintentar")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onGoHome,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("browser_error_home_button")
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Volver al Inicio")
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.stopLoading()
        }
    }
}
