package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.OverlayScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserMenuSheet(
    isVisible: Boolean,
    currentUrl: String,
    isDesktopMode: Boolean = false,
    isReaderMode: Boolean = false,
    textZoom: Int = 100,
    onDismiss: () -> Unit,
    onNewTab: (Boolean) -> Unit,
    onOpenOverlay: (OverlayScreen) -> Unit,
    onOpenClearData: () -> Unit,
    onToggleDesktop: () -> Unit = {},
    onToggleReader: () -> Unit = {},
    onFindInPage: () -> Unit = {},
    onOpenShield: () -> Unit = {},
    onOpenQr: () -> Unit = {},
    onUpdateZoom: (Int) -> Unit = {}
) {
    if (!isVisible) return

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Menú de Xbox L.R.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Basic tab actions
            MenuItemRow(
                icon = Icons.Default.Add,
                label = "Nueva Pestaña",
                onClick = {
                    onDismiss()
                    onNewTab(false)
                },
                testTag = "menu_new_tab"
            )

            MenuItemRow(
                icon = Icons.Default.Security,
                label = "Nueva Pestaña de Incógnito",
                onClick = {
                    onDismiss()
                    onNewTab(true)
                },
                testTag = "menu_new_incognito"
            )

            // Web page specific actions
            if (currentUrl.isNotBlank() && !currentUrl.startsWith("about:")) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                MenuItemRow(
                    icon = Icons.Default.FindInPage,
                    label = "Buscar en la página",
                    onClick = {
                        onDismiss()
                        onFindInPage()
                    },
                    testTag = "menu_find_in_page"
                )

                MenuItemRowWithToggle(
                    icon = Icons.Default.DesktopWindows,
                    label = "Sitio para computadoras",
                    isChecked = isDesktopMode,
                    onClick = {
                        onDismiss()
                        onToggleDesktop()
                    },
                    testTag = "menu_toggle_desktop"
                )

                MenuItemRowWithToggle(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    label = "Modo Lectura Xbox",
                    isChecked = isReaderMode,
                    onClick = {
                        onDismiss()
                        onToggleReader()
                    },
                    testTag = "menu_toggle_reader"
                )

                MenuItemRow(
                    icon = Icons.Default.Shield,
                    label = "Escudo de Protección Xbox",
                    onClick = {
                        onDismiss()
                        onOpenShield()
                    },
                    testTag = "menu_shield"
                )

                MenuItemRow(
                    icon = Icons.Default.QrCode2,
                    label = "Código QR de la página",
                    onClick = {
                        onDismiss()
                        onOpenQr()
                    },
                    testTag = "menu_qr_code"
                )

                MenuItemRow(
                    icon = Icons.Default.Share,
                    label = "Compartir enlace",
                    onClick = {
                        onDismiss()
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_TEXT, currentUrl)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Compartir con Xbox L.R.")
                        context.startActivity(shareIntent)
                    },
                    testTag = "menu_share"
                )

                // Zoom control row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Zoom: $textZoom%",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(80, 100, 125, 150).forEach { level ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (textZoom == level) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onUpdateZoom(level) }
                            ) {
                                Text(
                                    text = "$level%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (textZoom == level) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            MenuItemRow(
                icon = Icons.Default.History,
                label = "Historial",
                onClick = {
                    onOpenOverlay(OverlayScreen.HISTORY)
                },
                testTag = "menu_history"
            )

            MenuItemRow(
                icon = Icons.Default.Bookmark,
                label = "Favoritos",
                onClick = {
                    onOpenOverlay(OverlayScreen.BOOKMARKS)
                },
                testTag = "menu_bookmarks"
            )

            MenuItemRow(
                icon = Icons.Default.Download,
                label = "Descargas",
                onClick = {
                    onOpenOverlay(OverlayScreen.DOWNLOADS)
                },
                testTag = "menu_downloads"
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            MenuItemRow(
                icon = Icons.Default.CleaningServices,
                label = "Borrar datos de navegación",
                onClick = {
                    onDismiss()
                    onOpenClearData()
                },
                testTag = "menu_clear_data"
            )

            MenuItemRow(
                icon = Icons.Default.Settings,
                label = "Ajustes",
                onClick = {
                    onOpenOverlay(OverlayScreen.SETTINGS)
                },
                testTag = "menu_settings"
            )

            MenuItemRow(
                icon = Icons.Default.Policy,
                label = "Política de Privacidad",
                onClick = {
                    onOpenOverlay(OverlayScreen.PRIVACY_POLICY)
                },
                testTag = "menu_privacy"
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MenuItemRowWithToggle(
    icon: ImageVector,
    label: String,
    isChecked: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun MenuItemRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp
        )
    }
}
