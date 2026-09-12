package com.example.data.model

import java.util.UUID

data class TabItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Inicio",
    val url: String = "",
    val isIncognito: Boolean = false,
    val isHome: Boolean = true,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val progress: Int = 0,
    val isLoading: Boolean = false,
    val pageSnippet: String = "",
    val isDesktopMode: Boolean = false,
    val textZoom: Int = 100,
    val isReaderMode: Boolean = false
)
