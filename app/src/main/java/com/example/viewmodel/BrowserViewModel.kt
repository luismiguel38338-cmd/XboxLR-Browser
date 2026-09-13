package com.example.viewmodel

import android.app.Application
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiAction
import com.example.ai.AiMessage
import com.example.ai.AiService
import com.example.ai.GeminiAiService
import com.example.data.local.AppDatabase
import com.example.data.local.BrowserSettings
import com.example.data.local.ThemeMode
import com.example.data.local.UserPreferences
import com.example.data.model.BookmarkEntity
import com.example.data.model.DownloadEntity
import com.example.data.model.HistoryEntity
import com.example.data.model.QuickShortcut
import com.example.data.model.SearchEngine
import com.example.data.model.TabItem
import com.example.data.repository.BrowserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BrowserViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val preferences = UserPreferences(application)
    private val repository = BrowserRepository(
        db.historyDao(),
        db.bookmarkDao(),
        db.downloadDao(),
        preferences
    )
    private val aiService: AiService = GeminiAiService()

    // Settings
    val settings: StateFlow<BrowserSettings> = repository.userPreferences.settings

    // History, Bookmarks, Downloads from Room
    val history: StateFlow<List<HistoryEntity>> = repository.history.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val downloads: StateFlow<List<DownloadEntity>> = repository.downloads.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Tabs Management
    private val initialTab = TabItem(
        title = "Inicio",
        url = "",
        isIncognito = false,
        isHome = true
    )
    private val _tabs = MutableStateFlow<List<TabItem>>(listOf(initialTab))
    val tabs: StateFlow<List<TabItem>> = _tabs.asStateFlow()

    private val _currentTabId = MutableStateFlow<String>(initialTab.id)
    val currentTabId: StateFlow<String> = _currentTabId.asStateFlow()

    // Current active tab helper
    val currentTab: TabItem
        get() = _tabs.value.find { it.id == _currentTabId.value } ?: _tabs.value.first()

    // Current page bookmark status
    private val _isCurrentBookmarked = MutableStateFlow(false)
    val isCurrentBookmarked: StateFlow<Boolean> = _isCurrentBookmarked.asStateFlow()

    // AI Messages
    private val _aiMessages = MutableStateFlow<List<AiMessage>>(
        listOf(
            AiMessage(
                isUser = false,
                text = "¡Hola! Soy **Nova AI**, tu copiloto inteligente en Nova Browser.\n\nPuedo responder cualquier duda, resumir páginas webs, explicar términos difíciles, traducir contenidos o ayudarte a redactar.",
                action = AiAction.CHAT
            )
        )
    )
    val aiMessages: StateFlow<List<AiMessage>> = _aiMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // UI Sheets and Overlays visibility
    private val _isAiSheetVisible = MutableStateFlow(false)
    val isAiSheetVisible: StateFlow<Boolean> = _isAiSheetVisible.asStateFlow()

    private val _isTabsSheetVisible = MutableStateFlow(false)
    val isTabsSheetVisible: StateFlow<Boolean> = _isTabsSheetVisible.asStateFlow()

    private val _isMenuSheetVisible = MutableStateFlow(false)
    val isMenuSheetVisible: StateFlow<Boolean> = _isMenuSheetVisible.asStateFlow()

    private val _activeOverlayScreen = MutableStateFlow<OverlayScreen?>(null)
    val activeOverlayScreen: StateFlow<OverlayScreen?> = _activeOverlayScreen.asStateFlow()

    private val _isClearDataDialogVisible = MutableStateFlow(false)
    val isClearDataDialogVisible: StateFlow<Boolean> = _isClearDataDialogVisible.asStateFlow()

    // Find in page state
    private val _isFindInPageVisible = MutableStateFlow(false)
    val isFindInPageVisible: StateFlow<Boolean> = _isFindInPageVisible.asStateFlow()

    private val _findQuery = MutableStateFlow("")
    val findQuery: StateFlow<String> = _findQuery.asStateFlow()

    private val _findMatchStatus = MutableStateFlow("0/0")
    val findMatchStatus: StateFlow<String> = _findMatchStatus.asStateFlow()

    // Shield protection & ad-blocking stats
    private val _blockedTrackerCount = MutableStateFlow(0)
    val blockedTrackerCount: StateFlow<Int> = _blockedTrackerCount.asStateFlow()

    private val _isShieldDialogVisible = MutableStateFlow(false)
    val isShieldDialogVisible: StateFlow<Boolean> = _isShieldDialogVisible.asStateFlow()

    // QR Code dialog
    private val _isQrDialogVisible = MutableStateFlow(false)
    val isQrDialogVisible: StateFlow<Boolean> = _isQrDialogVisible.asStateFlow()

    // Quick Shortcuts
    private val _shortcuts = MutableStateFlow<List<QuickShortcut>>(
        listOf(
            QuickShortcut("1", "Google", "https://www.google.com", "G", 0xFF4285F4),
            QuickShortcut("2", "YouTube", "https://www.youtube.com", "Y", 0xFFFF0000),
            QuickShortcut("3", "Wikipedia", "https://es.wikipedia.org", "W", 0xFF555555),
            QuickShortcut("4", "GitHub", "https://github.com", "GH", 0xFF24292E),
            QuickShortcut("5", "Reddit", "https://www.reddit.com", "R", 0xFFFF4500),
            QuickShortcut("6", "The Verge", "https://www.theverge.com", "V", 0xFF6366F1),
            QuickShortcut("7", "Noticias", "https://news.google.com", "N", 0xFF00A86B)
        )
    )
    val shortcuts: StateFlow<List<QuickShortcut>> = _shortcuts.asStateFlow()

    // UI event trigger to tell WebView to navigate
    private val _webViewNavigationEvent = MutableStateFlow<NavigationCommand?>(null)
    val webViewNavigationEvent: StateFlow<NavigationCommand?> = _webViewNavigationEvent.asStateFlow()

    fun clearNavigationCommand() {
        _webViewNavigationEvent.value = null
    }

    // --- Tab Management ---
    fun openNewTab(url: String = "", isIncognito: Boolean = false) {
        val newTab = TabItem(
            title = if (url.isBlank()) "Inicio" else "Cargando...",
            url = url,
            isIncognito = isIncognito,
            isHome = url.isBlank()
        )
        _tabs.value = _tabs.value + newTab
        _currentTabId.value = newTab.id
        _isTabsSheetVisible.value = false
        checkBookmarkState(url)
    }

    fun closeTab(tabId: String) {
        val currentList = _tabs.value
        if (currentList.size <= 1) {
            // Reset the only tab to home
            val resetTab = TabItem(title = "Inicio", url = "", isHome = true)
            _tabs.value = listOf(resetTab)
            _currentTabId.value = resetTab.id
            return
        }

        val closingIndex = currentList.indexOfFirst { it.id == tabId }
        val updatedList = currentList.filter { it.id != tabId }
        _tabs.value = updatedList

        if (_currentTabId.value == tabId) {
            val newIndex = (closingIndex - 1).coerceAtLeast(0)
            _currentTabId.value = updatedList[newIndex].id
            checkBookmarkState(updatedList[newIndex].url)
        }
    }

    fun switchToTab(tabId: String) {
        val tab = _tabs.value.find { it.id == tabId } ?: return
        _currentTabId.value = tab.id
        _isTabsSheetVisible.value = false
        checkBookmarkState(tab.url)
    }

    fun closeAllTabs() {
        val resetTab = TabItem(title = "Inicio", url = "", isHome = true)
        _tabs.value = listOf(resetTab)
        _currentTabId.value = resetTab.id
        _isTabsSheetVisible.value = false
    }

    // --- Navigation ---
    fun navigateTo(input: String) {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) return

        val targetUrl = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
            else -> settings.value.searchEngine.buildQuery(trimmed)
        }

        updateActiveTabUrl(targetUrl)
        _webViewNavigationEvent.value = NavigationCommand.LoadUrl(targetUrl)
    }

    fun searchDirectly(query: String) {
        val url = settings.value.searchEngine.buildQuery(query)
        updateActiveTabUrl(url)
        _webViewNavigationEvent.value = NavigationCommand.LoadUrl(url)
    }

    fun searchWithAiSummary(query: String) {
        // Perform search and open AI assistant with instant summary
        searchDirectly(query)
        askNovaAi(
            prompt = "Proporciona una síntesis completa y estructurada con información clave para la búsqueda: $query",
            action = AiAction.SMART_SEARCH,
            usePageContext = false
        )
        _isAiSheetVisible.value = true
    }

    fun goBack() {
        _webViewNavigationEvent.value = NavigationCommand.GoBack
    }

    fun goForward() {
        _webViewNavigationEvent.value = NavigationCommand.GoForward
    }

    fun reload() {
        _webViewNavigationEvent.value = NavigationCommand.Reload
    }

    fun goHome() {
        val homeUrl = settings.value.homePageUrl
        if (homeUrl.isNotBlank()) {
            navigateTo(homeUrl)
        } else {
            val updated = _tabs.value.map { tab ->
                if (tab.id == _currentTabId.value) {
                    tab.copy(isHome = true, url = "", title = "Inicio", progress = 0, isLoading = false)
                } else tab
            }
            _tabs.value = updated
            _webViewNavigationEvent.value = NavigationCommand.LoadUrl("about:blank")
        }
    }

    fun onPageStarted(url: String) {
        val updated = _tabs.value.map { tab ->
            if (tab.id == _currentTabId.value) {
                tab.copy(url = url, isLoading = true, isHome = false, progress = 10)
            } else tab
        }
        _tabs.value = updated
    }

    fun onPageFinished(url: String, title: String?, canGoBack: Boolean, canGoForward: Boolean) {
        val safeTitle = if (!title.isNullOrBlank()) title else url
        val currentTab = currentTab
        val updated = _tabs.value.map { tab ->
            if (tab.id == _currentTabId.value) {
                tab.copy(
                    url = url,
                    title = safeTitle,
                    isLoading = false,
                    progress = 100,
                    canGoBack = canGoBack,
                    canGoForward = canGoForward,
                    isHome = false
                )
            } else tab
        }
        _tabs.value = updated
        checkBookmarkState(url)

        // Record history only if NOT incognito
        if (!currentTab.isIncognito && url.isNotBlank() && !url.startsWith("about:")) {
            viewModelScope.launch {
                repository.addHistory(safeTitle, url)
            }
        }
    }

    fun onProgressChanged(newProgress: Int) {
        val updated = _tabs.value.map { tab ->
            if (tab.id == _currentTabId.value) {
                tab.copy(progress = newProgress, isLoading = newProgress < 100)
            } else tab
        }
        _tabs.value = updated
    }

    fun updatePageSnippet(snippet: String) {
        val updated = _tabs.value.map { tab ->
            if (tab.id == _currentTabId.value) {
                tab.copy(pageSnippet = snippet)
            } else tab
        }
        _tabs.value = updated
    }

    private fun updateActiveTabUrl(url: String) {
        val updated = _tabs.value.map { tab ->
            if (tab.id == _currentTabId.value) {
                tab.copy(url = url, isHome = false, isLoading = true, progress = 15)
            } else tab
        }
        _tabs.value = updated
    }

    // --- Bookmarks ---
    private fun checkBookmarkState(url: String) {
        viewModelScope.launch {
            _isCurrentBookmarked.value = repository.isBookmarked(url)
        }
    }

    fun toggleCurrentBookmark() {
        val tab = currentTab
        if (tab.url.isBlank() || tab.isHome) return
        viewModelScope.launch {
            val isNowBookmarked = repository.toggleBookmark(tab.title, tab.url)
            _isCurrentBookmarked.value = isNowBookmarked
        }
    }

    fun deleteBookmark(id: Long) {
        viewModelScope.launch {
            repository.deleteBookmark(id)
            checkBookmarkState(currentTab.url)
        }
    }

    // --- History ---
    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // --- Downloads ---
    fun recordDownload(fileName: String, url: String, mimeType: String, sizeBytes: Long, filePath: String) {
        viewModelScope.launch {
            repository.addDownload(fileName, url, mimeType, sizeBytes, filePath)
        }
    }

    fun deleteDownload(id: Long) {
        viewModelScope.launch {
            repository.deleteDownload(id)
        }
    }

    fun clearDownloads() {
        viewModelScope.launch {
            repository.clearDownloads()
        }
    }

    // --- Privacy & Clear Browsing Data ---
    fun clearBrowsingData(clearHistory: Boolean, clearCookies: Boolean, clearCache: Boolean) {
        viewModelScope.launch {
            if (clearHistory) {
                repository.clearHistory()
            }
            if (clearCookies) {
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
            }
            if (clearCache) {
                WebStorage.getInstance().deleteAllData()
                _webViewNavigationEvent.value = NavigationCommand.ClearCache
            }
            _isClearDataDialogVisible.value = false
        }
    }

    // --- Nova AI Assistant ---
    fun askNovaAi(
        prompt: String,
        action: AiAction = AiAction.CHAT,
        usePageContext: Boolean = true
    ) {
        if (prompt.isBlank()) return

        val tab = currentTab
        val pageTitle = if (usePageContext && !tab.isHome && settings.value.allowAiPageContext) tab.title else null
        val pageUrl = if (usePageContext && !tab.isHome && settings.value.allowAiPageContext) tab.url else null
        val pageContent = if (usePageContext && !tab.isHome && settings.value.allowAiPageContext) tab.pageSnippet else null

        val userMessage = AiMessage(
            isUser = true,
            text = prompt,
            action = action,
            pageContextTitle = pageTitle
        )
        _aiMessages.value = _aiMessages.value + userMessage
        _isAiLoading.value = true

        viewModelScope.launch {
            val result = aiService.sendPrompt(
                prompt = prompt,
                pageTitle = pageTitle,
                pageUrl = pageUrl,
                pageContent = pageContent,
                action = action,
                customKey = settings.value.customApiKey
            )

            val replyText = result.getOrElse { e ->
                "Hubo un inconveniente al consultar con Nova AI: ${e.localizedMessage ?: "Error desconocido"}. Verifica tu conexión a internet o la configuración de IA."
            }

            val aiMessage = AiMessage(
                isUser = false,
                text = replyText,
                action = action,
                pageContextTitle = pageTitle
            )
            _aiMessages.value = _aiMessages.value + aiMessage
            _isAiLoading.value = false
        }
    }

    fun askXboxAi(
        prompt: String,
        action: AiAction = AiAction.CHAT,
        usePageContext: Boolean = true
    ) = askNovaAi(prompt, action, usePageContext)

    fun clearAiConversation() {
        _aiMessages.value = listOf(
            AiMessage(
                isUser = false,
                text = "Conversación reiniciada. ¿En qué más puedo ayudarte hoy con Nova AI?",
                action = AiAction.CHAT
            )
        )
    }

    // --- Overlays & Visibility Controls ---
    fun setAiSheetVisible(visible: Boolean) {
        _isAiSheetVisible.value = visible
    }

    fun setTabsSheetVisible(visible: Boolean) {
        _isTabsSheetVisible.value = visible
    }

    fun setMenuSheetVisible(visible: Boolean) {
        _isMenuSheetVisible.value = visible
    }

    fun setClearDataDialogVisible(visible: Boolean) {
        _isClearDataDialogVisible.value = visible
    }

    fun openOverlay(screen: OverlayScreen) {
        _activeOverlayScreen.value = screen
        _isMenuSheetVisible.value = false
    }

    fun closeOverlay() {
        _activeOverlayScreen.value = null
    }

    // --- Settings Updates ---
    fun updateSearchEngine(engine: SearchEngine) = repository.userPreferences.updateSearchEngine(engine)
    fun updateThemeMode(theme: ThemeMode) = repository.userPreferences.updateThemeMode(theme)
    fun updateHomePageUrl(url: String) = repository.userPreferences.updateHomePageUrl(url)
    fun updateIncognitoByDefault(enabled: Boolean) = repository.userPreferences.updateIncognitoByDefault(enabled)
    fun updateDoNotTrack(enabled: Boolean) = repository.userPreferences.updateDoNotTrack(enabled)
    fun updateClearOnExit(enabled: Boolean) = repository.userPreferences.updateClearOnExit(enabled)
    fun updateAllowAiPageContext(enabled: Boolean) = repository.userPreferences.updateAllowAiPageContext(enabled)
    fun updateCustomApiKey(key: String) = repository.userPreferences.updateCustomApiKey(key)
    fun updateLanguage(lang: String) = repository.userPreferences.updateLanguage(lang)

    // Shortcut management
    fun addShortcut(title: String, url: String) {
        val letter = title.take(2).uppercase()
        val newShortcut = QuickShortcut(
            id = System.currentTimeMillis().toString(),
            title = title,
            url = if (!url.startsWith("http")) "https://$url" else url,
            iconLetter = letter
        )
        _shortcuts.value = _shortcuts.value + newShortcut
    }

    fun removeShortcut(id: String) {
        _shortcuts.value = _shortcuts.value.filter { it.id != id }
    }

    // Find in Page
    fun setFindInPageVisible(visible: Boolean) {
        _isFindInPageVisible.value = visible
        if (!visible) {
            _findQuery.value = ""
            _findMatchStatus.value = "0/0"
            _webViewNavigationEvent.value = NavigationCommand.ClearMatches
        }
    }

    fun updateFindQuery(query: String) {
        _findQuery.value = query
        if (query.isNotBlank()) {
            _webViewNavigationEvent.value = NavigationCommand.FindInPage(query, forward = true)
        } else {
            _findMatchStatus.value = "0/0"
            _webViewNavigationEvent.value = NavigationCommand.ClearMatches
        }
    }

    fun findNextMatch(forward: Boolean) {
        if (_findQuery.value.isNotBlank()) {
            _webViewNavigationEvent.value = NavigationCommand.FindInPage(_findQuery.value, forward = forward)
        }
    }

    fun updateFindMatchStatus(activeOrdinal: Int, numberOfMatches: Int) {
        _findMatchStatus.value = if (numberOfMatches > 0) "${activeOrdinal + 1}/$numberOfMatches" else "0/0"
    }

    // Desktop Mode
    fun toggleDesktopMode() {
        val tabId = _currentTabId.value
        val isDesktop = !currentTab.isDesktopMode
        _tabs.value = _tabs.value.map { tab ->
            if (tab.id == tabId) tab.copy(isDesktopMode = isDesktop) else tab
        }
        reload()
    }

    // Text Zoom
    fun updateTextZoom(zoom: Int) {
        val tabId = _currentTabId.value
        _tabs.value = _tabs.value.map { tab ->
            if (tab.id == tabId) tab.copy(textZoom = zoom) else tab
        }
        _webViewNavigationEvent.value = NavigationCommand.SetTextZoom(zoom)
    }

    // Reader Mode
    fun toggleReaderMode() {
        val tabId = _currentTabId.value
        val isReader = !currentTab.isReaderMode
        _tabs.value = _tabs.value.map { tab ->
            if (tab.id == tabId) tab.copy(isReaderMode = isReader) else tab
        }
    }

    // Shield Protection
    fun onTrackerBlocked() {
        _blockedTrackerCount.value += 1
    }

    fun setShieldDialogVisible(visible: Boolean) {
        _isShieldDialogVisible.value = visible
    }

    fun toggleShieldProtection() {
        val current = settings.value.shieldProtection
        repository.userPreferences.updateShieldProtection(!current)
    }

    // QR Code Dialog
    fun setQrDialogVisible(visible: Boolean) {
        _isQrDialogVisible.value = visible
    }
}

sealed interface NavigationCommand {
    data class LoadUrl(val url: String) : NavigationCommand
    object GoBack : NavigationCommand
    object GoForward : NavigationCommand
    object Reload : NavigationCommand
    object ClearCache : NavigationCommand
    data class FindInPage(val query: String, val forward: Boolean = true) : NavigationCommand
    object ClearMatches : NavigationCommand
    data class SetTextZoom(val zoom: Int) : NavigationCommand
}

enum class OverlayScreen {
    HISTORY, BOOKMARKS, DOWNLOADS, SETTINGS, PRIVACY_POLICY
}
