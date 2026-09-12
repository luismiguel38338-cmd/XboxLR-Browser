package com.example.data.model

import java.net.URLEncoder

enum class SearchEngine(val displayName: String, val searchUrl: String, val homeUrl: String) {
    GOOGLE("Google", "https://www.google.com/search?q=", "https://www.google.com"),
    BING("Bing", "https://www.bing.com/search?q=", "https://www.bing.com"),
    DUCKDUCKGO("DuckDuckGo", "https://duckduckgo.com/?q=", "https://duckduckgo.com"),
    ECOSIA("Ecosia", "https://www.ecosia.org/search?q=", "https://www.ecosia.org"),
    YAHOO("Yahoo", "https://search.yahoo.com/search?p=", "https://www.yahoo.com");

    fun buildQuery(query: String): String {
        return try {
            searchUrl + URLEncoder.encode(query, "UTF-8")
        } catch (_: Exception) {
            searchUrl + query
        }
    }
}
