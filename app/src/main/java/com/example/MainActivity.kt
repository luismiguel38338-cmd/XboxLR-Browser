package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.AiAction
import com.example.ui.components.BrowserMenuSheet
import com.example.ui.components.ClearDataDialog
import com.example.ui.components.FindInPageBar
import com.example.ui.components.NovaAiSheet
import com.example.ui.components.NovaBottomBar
import com.example.ui.components.NovaTopBar
import com.example.ui.components.QrCodeDialog
import com.example.ui.components.ReaderModeView
import com.example.ui.components.ShieldDialog
import com.example.ui.components.TabsSheet
import com.example.ui.screens.BookmarksScreen
import com.example.ui.screens.BrowserScreen
import com.example.ui.screens.DownloadsScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PrivacyPolicyDialog
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.NovaTheme
import com.example.viewmodel.BrowserViewModel
import com.example.viewmodel.OverlayScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: BrowserViewModel = viewModel()
            val settings by viewModel.settings.collectAsState()
            val tabs by viewModel.tabs.collectAsState()
            val currentTabId by viewModel.currentTabId.collectAsState()
            val currentTab = viewModel.currentTab

            NovaTheme(
                themeMode = settings.themeMode,
                isIncognito = currentTab.isIncognito
            ) {
                NovaBrowserApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun XboxLRApp(viewModel: BrowserViewModel) = NovaBrowserApp(viewModel = viewModel)

@Composable
fun NovaBrowserApp(viewModel: BrowserViewModel) {
    val settings by viewModel.settings.collectAsState()
    val tabs by viewModel.tabs.collectAsState()
    val currentTabId by viewModel.currentTabId.collectAsState()
    val currentTab = viewModel.currentTab
    val isBookmarked by viewModel.isCurrentBookmarked.collectAsState()
    val navigationCommand by viewModel.webViewNavigationEvent.collectAsState()

    // Overlay screens & sheets
    val activeOverlay by viewModel.activeOverlayScreen.collectAsState()
    val isAiSheetVisible by viewModel.isAiSheetVisible.collectAsState()
    val isTabsSheetVisible by viewModel.isTabsSheetVisible.collectAsState()
    val isMenuSheetVisible by viewModel.isMenuSheetVisible.collectAsState()
    val isClearDataDialogVisible by viewModel.isClearDataDialogVisible.collectAsState()

    // AI State
    val aiMessages by viewModel.aiMessages.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    // Data lists
    val historyList by viewModel.history.collectAsState()
    val bookmarksList by viewModel.bookmarks.collectAsState()
    val downloadsList by viewModel.downloads.collectAsState()
    val shortcuts by viewModel.shortcuts.collectAsState()

    // New feature states: Find in Page, Shield, QR
    val isFindInPageVisible by viewModel.isFindInPageVisible.collectAsState()
    val findQuery by viewModel.findQuery.collectAsState()
    val findMatchStatus by viewModel.findMatchStatus.collectAsState()
    val isShieldDialogVisible by viewModel.isShieldDialogVisible.collectAsState()
    val blockedTrackerCount by viewModel.blockedTrackerCount.collectAsState()
    val isQrDialogVisible by viewModel.isQrDialogVisible.collectAsState()

    // Handle back button hierarchically
    BackHandler(enabled = true) {
        when {
            isFindInPageVisible -> viewModel.setFindInPageVisible(false)
            isShieldDialogVisible -> viewModel.setShieldDialogVisible(false)
            isQrDialogVisible -> viewModel.setQrDialogVisible(false)
            currentTab.isReaderMode -> viewModel.toggleReaderMode()
            activeOverlay != null -> viewModel.closeOverlay()
            isAiSheetVisible -> viewModel.setAiSheetVisible(false)
            isTabsSheetVisible -> viewModel.setTabsSheetVisible(false)
            isMenuSheetVisible -> viewModel.setMenuSheetVisible(false)
            currentTab.canGoBack -> viewModel.goBack()
            !currentTab.isHome -> viewModel.goHome()
            else -> {
                // At home with nowhere to go back, let standard behavior exit
            }
        }
    }

    Scaffold(
        topBar = {
            if (activeOverlay == null) {
                NovaTopBar(
                    tab = currentTab,
                    tabCount = tabs.size,
                    isBookmarked = isBookmarked,
                    onNavigate = { input -> viewModel.navigateTo(input) },
                    onReload = { viewModel.reload() },
                    onToggleBookmark = { viewModel.toggleCurrentBookmark() },
                    onOpenTabs = { viewModel.setTabsSheetVisible(true) },
                    onOpenMenu = { viewModel.setMenuSheetVisible(true) },
                    onOpenShield = { viewModel.setShieldDialogVisible(true) }
                )
            }
        },
        bottomBar = {
            if (activeOverlay == null) {
                NovaBottomBar(
                    canGoBack = currentTab.canGoBack,
                    canGoForward = currentTab.canGoForward,
                    tabCount = tabs.size,
                    onBack = { viewModel.goBack() },
                    onForward = { viewModel.goForward() },
                    onHome = { viewModel.goHome() },
                    onOpenAi = { viewModel.setAiSheetVisible(true) },
                    onOpenTabs = { viewModel.setTabsSheetVisible(true) }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (activeOverlay) {
                OverlayScreen.HISTORY -> {
                    HistoryScreen(
                        historyList = historyList,
                        onSelectUrl = { url ->
                            viewModel.navigateTo(url)
                            viewModel.closeOverlay()
                        },
                        onDeleteEntry = { id -> viewModel.deleteHistory(id) },
                        onClearAll = { viewModel.clearHistory() },
                        onBack = { viewModel.closeOverlay() }
                    )
                }
                OverlayScreen.BOOKMARKS -> {
                    BookmarksScreen(
                        bookmarksList = bookmarksList,
                        onSelectUrl = { url ->
                            viewModel.navigateTo(url)
                            viewModel.closeOverlay()
                        },
                        onDeleteBookmark = { id -> viewModel.deleteBookmark(id) },
                        onBack = { viewModel.closeOverlay() }
                    )
                }
                OverlayScreen.DOWNLOADS -> {
                    DownloadsScreen(
                        downloadsList = downloadsList,
                        onDeleteDownload = { id -> viewModel.deleteDownload(id) },
                        onClearAll = { viewModel.clearDownloads() },
                        onBack = { viewModel.closeOverlay() }
                    )
                }
                OverlayScreen.SETTINGS -> {
                    SettingsScreen(
                        settings = settings,
                        onUpdateSearchEngine = { engine -> viewModel.updateSearchEngine(engine) },
                        onUpdateThemeMode = { mode -> viewModel.updateThemeMode(mode) },
                        onUpdateHomePage = { url -> viewModel.updateHomePageUrl(url) },
                        onUpdateIncognitoByDefault = { enabled -> viewModel.updateIncognitoByDefault(enabled) },
                        onUpdateDoNotTrack = { enabled -> viewModel.updateDoNotTrack(enabled) },
                        onUpdateClearOnExit = { enabled -> viewModel.updateClearOnExit(enabled) },
                        onUpdateAllowAiContext = { enabled -> viewModel.updateAllowAiPageContext(enabled) },
                        onUpdateCustomApiKey = { key -> viewModel.updateCustomApiKey(key) },
                        onClearHistory = { viewModel.clearHistory() },
                        onOpenDownloads = { viewModel.openOverlay(OverlayScreen.DOWNLOADS) },
                        onOpenPrivacyPolicy = { viewModel.openOverlay(OverlayScreen.PRIVACY_POLICY) },
                        onBack = { viewModel.closeOverlay() }
                    )
                }
                OverlayScreen.PRIVACY_POLICY -> {
                    PrivacyPolicyDialog(
                        isVisible = true,
                        onDismiss = { viewModel.closeOverlay() }
                    )
                }
                null -> {
                    // Standard Browser or Home View
                    if (currentTab.isHome) {
                        HomeScreen(
                            shortcuts = shortcuts,
                            onSearchWeb = { query -> viewModel.searchDirectly(query) },
                            onAskAi = { prompt ->
                                viewModel.askNovaAi(prompt, AiAction.CHAT, usePageContext = false)
                                viewModel.setAiSheetVisible(true)
                            },
                            onSmartSearch = { query -> viewModel.searchWithAiSummary(query) },
                            onNavigateShortcut = { url -> viewModel.navigateTo(url) },
                            onAddShortcut = { title, url -> viewModel.addShortcut(title, url) },
                            onRemoveShortcut = { id -> viewModel.removeShortcut(id) }
                        )
                    } else if (currentTab.isReaderMode) {
                        ReaderModeView(
                            tab = currentTab,
                            onCloseReader = { viewModel.toggleReaderMode() },
                            onSummarizeWithAi = {
                                viewModel.askNovaAi(
                                    "Resume este artículo de forma clara y destacando los puntos más importantes.",
                                    AiAction.SUMMARIZE_PAGE,
                                    usePageContext = true
                                )
                                viewModel.setAiSheetVisible(true)
                            }
                        )
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (isFindInPageVisible) {
                                FindInPageBar(
                                    query = findQuery,
                                    matchStatus = findMatchStatus,
                                    onQueryChange = { query -> viewModel.updateFindQuery(query) },
                                    onNext = { viewModel.findNextMatch(forward = true) },
                                    onPrevious = { viewModel.findNextMatch(forward = false) },
                                    onClose = { viewModel.setFindInPageVisible(false) }
                                )
                            }
                            BrowserScreen(
                                tab = currentTab,
                                navigationCommand = navigationCommand,
                                onNavigationHandled = { viewModel.clearNavigationCommand() },
                                onPageStarted = { url -> viewModel.onPageStarted(url) },
                                onPageFinished = { url, title, canBack, canForward ->
                                    viewModel.onPageFinished(url, title, canBack, canForward)
                                },
                                onProgressChanged = { progress -> viewModel.onProgressChanged(progress) },
                                onPageSnippetExtracted = { snippet -> viewModel.updatePageSnippet(snippet) },
                                onDownloadRequested = { fileName, url, mimeType, size ->
                                    viewModel.recordDownload(fileName, url, mimeType, size, "")
                                },
                                onGoHome = { viewModel.goHome() },
                                isShieldEnabled = settings.shieldProtection,
                                onTrackerBlocked = { viewModel.onTrackerBlocked() },
                                onFindResult = { active, count -> viewModel.updateFindMatchStatus(active, count) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal Sheet: Nova AI Copilot
    NovaAiSheet(
        isVisible = isAiSheetVisible,
        currentTab = currentTab,
        messages = aiMessages,
        isLoading = isAiLoading,
        allowAiContext = settings.allowAiPageContext,
        onDismiss = { viewModel.setAiSheetVisible(false) },
        onSendMessage = { prompt, action, useContext ->
            viewModel.askNovaAi(prompt, action, useContext)
        },
        onClearChat = { viewModel.clearAiConversation() },
        onToggleAiContext = { enabled -> viewModel.updateAllowAiPageContext(enabled) }
    )

    // Modal Sheet: Tabs Switcher
    TabsSheet(
        isVisible = isTabsSheetVisible,
        tabs = tabs,
        currentTabId = currentTabId,
        onDismiss = { viewModel.setTabsSheetVisible(false) },
        onSelectTab = { tabId -> viewModel.switchToTab(tabId) },
        onCloseTab = { tabId -> viewModel.closeTab(tabId) },
        onNewTab = { isIncognito -> viewModel.openNewTab(isIncognito = isIncognito) },
        onCloseAll = { viewModel.closeAllTabs() }
    )

    // Modal Sheet: 3-Dots Menu
    BrowserMenuSheet(
        isVisible = isMenuSheetVisible,
        currentUrl = currentTab.url,
        isDesktopMode = currentTab.isDesktopMode,
        isReaderMode = currentTab.isReaderMode,
        textZoom = currentTab.textZoom,
        onDismiss = { viewModel.setMenuSheetVisible(false) },
        onNewTab = { isIncognito -> viewModel.openNewTab(isIncognito = isIncognito) },
        onOpenOverlay = { screen -> viewModel.openOverlay(screen) },
        onOpenClearData = { viewModel.setClearDataDialogVisible(true) },
        onToggleDesktop = { viewModel.toggleDesktopMode() },
        onToggleReader = { viewModel.toggleReaderMode() },
        onFindInPage = { viewModel.setFindInPageVisible(true) },
        onOpenShield = { viewModel.setShieldDialogVisible(true) },
        onOpenQr = { viewModel.setQrDialogVisible(true) },
        onUpdateZoom = { zoom -> viewModel.updateTextZoom(zoom) }
    )

    // Dialog: Clear Browsing Data
    ClearDataDialog(
        isVisible = isClearDataDialogVisible,
        onDismiss = { viewModel.setClearDataDialogVisible(false) },
        onConfirmClear = { hist, cookies, cache ->
            viewModel.clearBrowsingData(hist, cookies, cache)
        }
    )

    // Dialog: Nova Shield Protection
    ShieldDialog(
        isVisible = isShieldDialogVisible,
        isEnabled = settings.shieldProtection,
        blockedCount = blockedTrackerCount,
        onToggle = { viewModel.toggleShieldProtection() },
        onDismiss = { viewModel.setShieldDialogVisible(false) }
    )

    // Dialog: QR Code Share
    QrCodeDialog(
        isVisible = isQrDialogVisible,
        url = currentTab.url,
        onDismiss = { viewModel.setQrDialogVisible(false) }
    )
}
