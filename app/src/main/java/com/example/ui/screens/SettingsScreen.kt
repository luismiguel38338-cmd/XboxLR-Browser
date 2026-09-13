package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.BrowserSettings
import com.example.data.local.ThemeMode
import com.example.data.model.SearchEngine
import com.example.ui.theme.NovaBrightCyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: BrowserSettings,
    onUpdateSearchEngine: (SearchEngine) -> Unit,
    onUpdateThemeMode: (ThemeMode) -> Unit,
    onUpdateHomePage: (String) -> Unit,
    onUpdateIncognitoByDefault: (Boolean) -> Unit,
    onUpdateDoNotTrack: (Boolean) -> Unit,
    onUpdateClearOnExit: (Boolean) -> Unit,
    onUpdateAllowAiContext: (Boolean) -> Unit,
    onUpdateCustomApiKey: (String) -> Unit,
    onClearHistory: () -> Unit,
    onOpenDownloads: () -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onBack: () -> Unit
) {
    var showEngineDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showHomeDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Ajustes de Nova Browser",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("settings_back_button")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección General
            SettingsSectionHeader(title = "Navegación General")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    SettingsClickableRow(
                        icon = Icons.Default.Search,
                        title = "Motor de búsqueda",
                        subtitle = settings.searchEngine.displayName,
                        onClick = { showEngineDialog = true }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsClickableRow(
                        icon = Icons.Default.DarkMode,
                        title = "Tema de la aplicación",
                        subtitle = when (settings.themeMode) {
                            ThemeMode.SYSTEM -> "Predeterminado del sistema"
                            ThemeMode.DARK -> "Modo Oscuro (Obsidian & Cyan)"
                            ThemeMode.LIGHT -> "Modo Claro"
                        },
                        onClick = { showThemeDialog = true }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsClickableRow(
                        icon = Icons.Default.Home,
                        title = "Página de inicio",
                        subtitle = if (settings.homePageUrl.isBlank()) "Nova Browser Inicio" else settings.homePageUrl,
                        onClick = { showHomeDialog = true }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsClickableRow(
                        icon = Icons.Default.Language,
                        title = "Idioma",
                        subtitle = if (settings.language == "es") "Español" else "English",
                        onClick = { /* Default Spanish */ }
                    )
                }
            }

            // Sección Privacidad y Seguridad
            SettingsSectionHeader(title = "Privacidad y Seguridad")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    SettingsToggleRow(
                        icon = Icons.Default.Security,
                        title = "Modo incógnito predeterminado",
                        subtitle = "Abrir siempre nuevas pestañas en modo privado",
                        checked = settings.incognitoByDefault,
                        onCheckedChange = onUpdateIncognitoByDefault
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsToggleRow(
                        icon = Icons.Default.Security,
                        title = "No rastrear (Do Not Track)",
                        subtitle = "Solicita a los sitios no rastrear tu navegación",
                        checked = settings.doNotTrack,
                        onCheckedChange = onUpdateDoNotTrack
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsToggleRow(
                        icon = Icons.Default.Delete,
                        title = "Limpiar datos al salir",
                        subtitle = "Borra historial y cookies al cerrar la app",
                        checked = settings.clearOnExit,
                        onCheckedChange = onUpdateClearOnExit
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsClickableRow(
                        icon = Icons.Default.Delete,
                        title = "Borrar historial ahora",
                        subtitle = "Elimina todo el registro de navegación local",
                        onClick = onClearHistory
                    )
                }
            }

            // Sección Inteligencia Artificial (Nova AI)
            SettingsSectionHeader(title = "Inteligencia Artificial (Nova AI)")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    SettingsToggleRow(
                        icon = Icons.Default.Psychology,
                        title = "Permitir análisis de página web",
                        subtitle = "Autoriza a Nova AI a leer el texto de la página que estás viendo para resumir o responder preguntas",
                        checked = settings.allowAiPageContext,
                        onCheckedChange = onUpdateAllowAiContext
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsClickableRow(
                        icon = Icons.Default.Key,
                        title = "Proveedor y Clave API de IA",
                        subtitle = if (settings.customApiKey.isNotBlank()) "Clave personalizada configurada" else "Google Gemini 3.5 Flash (Servidor por defecto)",
                        onClick = { showApiKeyDialog = true }
                    )
                }
            }

            // Sección Descargas y Acerca de
            SettingsSectionHeader(title = "Descargas y Política")
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    SettingsClickableRow(
                        icon = Icons.Default.Download,
                        title = "Gestionar Descargas",
                        subtitle = "Ver y administrar archivos descargados",
                        onClick = onOpenDownloads
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                    SettingsClickableRow(
                        icon = Icons.Default.Policy,
                        title = "Política de Privacidad",
                        subtitle = "Información sobre tus datos y seguridad",
                        onClick = onOpenPrivacyPolicy
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Modal Selección de Motor de Búsqueda
    if (showEngineDialog) {
        AlertDialog(
            onDismissRequest = { showEngineDialog = false },
            title = { Text("Seleccionar Motor de Búsqueda", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    SearchEngine.entries.forEach { engine ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUpdateSearchEngine(engine)
                                    showEngineDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = settings.searchEngine == engine,
                                onClick = {
                                    onUpdateSearchEngine(engine)
                                    showEngineDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(engine.displayName, fontSize = 15.sp)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showEngineDialog = false }) { Text("Cerrar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal Selección de Tema
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Tema de la Aplicación", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    ThemeMode.entries.forEach { mode ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUpdateThemeMode(mode)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = settings.themeMode == mode,
                                onClick = {
                                    onUpdateThemeMode(mode)
                                    showThemeDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (mode) {
                                    ThemeMode.SYSTEM -> "Sistema (Automático)"
                                    ThemeMode.DARK -> "Oscuro (Nova Cyan)"
                                    ThemeMode.LIGHT -> "Claro"
                                },
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showThemeDialog = false }) { Text("Cerrar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal Página de Inicio
    if (showHomeDialog) {
        var tempUrl by remember { mutableStateOf(settings.homePageUrl) }
        AlertDialog(
            onDismissRequest = { showHomeDialog = false },
            title = { Text("Configurar Página de Inicio", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Deja el campo vacío para usar la pantalla de inicio oficial de Nova Browser.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = tempUrl,
                        onValueChange = { tempUrl = it },
                        placeholder = { Text("https://...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onUpdateHomePage(tempUrl.trim())
                    showHomeDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showHomeDialog = false }) { Text("Cancelar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal Configuración de Clave API de IA (Permite cambiar proveedor)
    if (showApiKeyDialog) {
        var tempKey by remember { mutableStateOf(settings.customApiKey) }
        AlertDialog(
            onDismissRequest = { showApiKeyDialog = false },
            title = { Text("Configuración de Proveedor de IA", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Nova Browser utiliza de forma nativa la API de Gemini (gemini-3.5-flash) inyectada de forma segura por el backend. Puedes ingresar una clave API personalizada o endpoint propio si deseas utilizar otro proveedor.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tempKey,
                        onValueChange = { tempKey = it },
                        label = { Text("Clave API / Token personalizado") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onUpdateCustomApiKey(tempKey.trim())
                    showApiKeyDialog = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showApiKeyDialog = false }) { Text("Cancelar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
    )
}

@Composable
private fun SettingsClickableRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                checkedTrackColor = NovaBrightCyan
            )
        )
    }
}
