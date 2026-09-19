package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.SearchEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    SYSTEM, DARK, LIGHT
}

data class BrowserSettings(
    val searchEngine: SearchEngine = SearchEngine.GOOGLE,
    val themeMode: ThemeMode = ThemeMode.DARK,
    val homePageUrl: String = "",
    val incognitoByDefault: Boolean = false,
    val doNotTrack: Boolean = true,
    val clearOnExit: Boolean = false,
    val allowAiPageContext: Boolean = true,
    val customApiKey: String = "",
    val language: String = "es",
    val shieldProtection: Boolean = true,
    val smartDarkMode: Boolean = false,
    val autoBlockCookieBanners: Boolean = true,
    val aiModel: String = "gemini-3.5-flash"
)

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("nova_browser_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<BrowserSettings> = _settings.asStateFlow()

    private fun loadSettings(): BrowserSettings {
        val engineName = prefs.getString("search_engine", SearchEngine.GOOGLE.name) ?: SearchEngine.GOOGLE.name
        val engine = try { SearchEngine.valueOf(engineName) } catch (_: Exception) { SearchEngine.GOOGLE }
        val themeName = prefs.getString("theme_mode", ThemeMode.DARK.name) ?: ThemeMode.DARK.name
        val theme = try { ThemeMode.valueOf(themeName) } catch (_: Exception) { ThemeMode.DARK }

        return BrowserSettings(
            searchEngine = engine,
            themeMode = theme,
            homePageUrl = prefs.getString("home_page_url", "") ?: "",
            incognitoByDefault = prefs.getBoolean("incognito_by_default", false),
            doNotTrack = prefs.getBoolean("do_not_track", true),
            clearOnExit = prefs.getBoolean("clear_on_exit", false),
            allowAiPageContext = prefs.getBoolean("allow_ai_page_context", true),
            customApiKey = prefs.getString("custom_api_key", "") ?: "",
            language = prefs.getString("language", "es") ?: "es",
            shieldProtection = prefs.getBoolean("shield_protection", true),
            smartDarkMode = prefs.getBoolean("smart_dark_mode", false),
            autoBlockCookieBanners = prefs.getBoolean("auto_block_cookie_banners", true),
            aiModel = prefs.getString("ai_model", "gemini-3.5-flash") ?: "gemini-3.5-flash"
        )
    }

    fun updateSmartDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("smart_dark_mode", enabled).apply()
        _settings.value = _settings.value.copy(smartDarkMode = enabled)
    }

    fun updateAutoBlockCookieBanners(enabled: Boolean) {
        prefs.edit().putBoolean("auto_block_cookie_banners", enabled).apply()
        _settings.value = _settings.value.copy(autoBlockCookieBanners = enabled)
    }

    fun updateAiModel(model: String) {
        prefs.edit().putString("ai_model", model).apply()
        _settings.value = _settings.value.copy(aiModel = model)
    }

    fun updateShieldProtection(enabled: Boolean) {
        prefs.edit().putBoolean("shield_protection", enabled).apply()
        _settings.value = _settings.value.copy(shieldProtection = enabled)
    }

    fun updateSearchEngine(engine: SearchEngine) {
        prefs.edit().putString("search_engine", engine.name).apply()
        _settings.value = _settings.value.copy(searchEngine = engine)
    }

    fun updateThemeMode(theme: ThemeMode) {
        prefs.edit().putString("theme_mode", theme.name).apply()
        _settings.value = _settings.value.copy(themeMode = theme)
    }

    fun updateHomePageUrl(url: String) {
        prefs.edit().putString("home_page_url", url).apply()
        _settings.value = _settings.value.copy(homePageUrl = url)
    }

    fun updateIncognitoByDefault(enabled: Boolean) {
        prefs.edit().putBoolean("incognito_by_default", enabled).apply()
        _settings.value = _settings.value.copy(incognitoByDefault = enabled)
    }

    fun updateDoNotTrack(enabled: Boolean) {
        prefs.edit().putBoolean("do_not_track", enabled).apply()
        _settings.value = _settings.value.copy(doNotTrack = enabled)
    }

    fun updateClearOnExit(enabled: Boolean) {
        prefs.edit().putBoolean("clear_on_exit", enabled).apply()
        _settings.value = _settings.value.copy(clearOnExit = enabled)
    }

    fun updateAllowAiPageContext(enabled: Boolean) {
        prefs.edit().putBoolean("allow_ai_page_context", enabled).apply()
        _settings.value = _settings.value.copy(allowAiPageContext = enabled)
    }

    fun updateCustomApiKey(key: String) {
        prefs.edit().putString("custom_api_key", key).apply()
        _settings.value = _settings.value.copy(customApiKey = key)
    }

    fun updateLanguage(lang: String) {
        prefs.edit().putString("language", lang).apply()
        _settings.value = _settings.value.copy(language = lang)
    }
}
