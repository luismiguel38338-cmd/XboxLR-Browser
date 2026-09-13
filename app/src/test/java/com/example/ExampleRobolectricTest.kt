package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ai.AiAction
import com.example.data.model.SearchEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Nova Browser", appName)
    }

    @Test
    fun `search engine url encoding test`() {
        val query = "android studio compose"
        val googleUrl = SearchEngine.GOOGLE.buildQuery(query)
        assertTrue(googleUrl.startsWith("https://www.google.com/search?q="))

        val duckUrl = SearchEngine.DUCKDUCKGO.buildQuery(query)
        assertTrue(duckUrl.startsWith("https://duckduckgo.com/?q="))
    }

    @Test
    fun `ai action metadata test`() {
        assertEquals("Resumir Página", AiAction.SUMMARIZE_PAGE.title)
        assertEquals("Traducir", AiAction.TRANSLATE_PAGE.title)
        assertEquals("Analizar Web", AiAction.ANALYZE_PAGE.title)
    }
}
