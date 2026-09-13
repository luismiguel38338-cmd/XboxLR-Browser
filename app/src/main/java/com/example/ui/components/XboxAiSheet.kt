package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.AiAction
import com.example.ai.AiMessage
import com.example.data.model.TabItem
import com.example.ui.theme.NovaBrightCyan
import com.example.ui.theme.NovaCyan
import com.example.ui.theme.NovaElectricBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaAiSheet(
    isVisible: Boolean,
    currentTab: TabItem,
    messages: List<AiMessage>,
    isLoading: Boolean,
    allowAiContext: Boolean,
    onDismiss: () -> Unit,
    onSendMessage: (String, AiAction, Boolean) -> Unit,
    onClearChat: () -> Unit,
    onToggleAiContext: (Boolean) -> Unit
) {
    if (!isVisible) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .shadow(6.dp, CircleShape, spotColor = NovaCyan)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NovaCyan, NovaElectricBlue, Color(0xFF081C30))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Nova AI Copilot",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(NovaBrightCyan)
                            )
                        }
                        Text(
                            text = "Asistente del navegador • gemini-3.5-flash",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onClearChat,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("ai_clear_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Limpiar chat",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Current Page Context Privacy Bar
            if (!currentTab.isHome && currentTab.url.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = NovaBrightCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = "Contexto: ${currentTab.title}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (allowAiContext) "Autorizado para analizar esta web" else "Contexto desactivado (privacidad)",
                                    fontSize = 10.sp,
                                    color = if (allowAiContext) NovaBrightCyan else MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        Switch(
                            checked = allowAiContext,
                            onCheckedChange = onToggleAiContext,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = NovaBrightCyan
                            ),
                            modifier = Modifier.size(width = 44.dp, height = 24.dp)
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // Quick AI Action Chips Row
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AiActionChip(
                    icon = Icons.Default.Description,
                    label = "Resumir página",
                    onClick = {
                        onSendMessage("Resume los puntos más importantes de esta página web de forma estructurada.", AiAction.SUMMARIZE_PAGE, true)
                    }
                )
                AiActionChip(
                    icon = Icons.Default.FindInPage,
                    label = "Analizar web",
                    onClick = {
                        onSendMessage("Analiza esta página: fiabilidad, temática central y propósito informativo.", AiAction.ANALYZE_PAGE, true)
                    }
                )
                AiActionChip(
                    icon = Icons.Default.Translate,
                    label = "Traducir",
                    onClick = {
                        onSendMessage("Traduce y explica los conceptos de esta página web al español.", AiAction.TRANSLATE_PAGE, true)
                    }
                )
                AiActionChip(
                    icon = Icons.Default.AutoAwesome,
                    label = "Extraer datos",
                    onClick = {
                        onSendMessage("Extrae fechas, cifras, conclusiones y datos relevantes de este sitio.", AiAction.EXTRACT_INFO, true)
                    }
                )
                AiActionChip(
                    icon = Icons.Default.Edit,
                    label = "Ayuda a redactar",
                    onClick = {
                        onSendMessage("Ayúdame a redactar un texto profesional basado en la información que estamos viendo.", AiAction.HELP_WRITE, true)
                    }
                )
            }

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    AiMessageBubble(
                        message = message,
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(message.text))
                        }
                    )
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Nova AI está analizando...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Input Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Pregúntale a Nova AI...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                        BasicTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (inputText.isNotBlank()) {
                                        focusManager.clearFocus()
                                        val text = inputText
                                        inputText = ""
                                        onSendMessage(text, AiAction.CHAT, allowAiContext)
                                    }
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_input_field")
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                focusManager.clearFocus()
                                val text = inputText
                                inputText = ""
                                onSendMessage(text, AiAction.CHAT, allowAiContext)
                            }
                        },
                        enabled = inputText.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (inputText.isNotBlank() && !isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .testTag("ai_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar a Nova AI",
                            tint = if (inputText.isNotBlank() && !isLoading) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AiActionChip(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun AiMessageBubble(
    message: AiMessage,
    onCopy: () -> Unit
) {
    val isUser = message.isUser

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isUser && message.action != AiAction.CHAT) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = message.action.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(
                    if (isUser) Brush.horizontalGradient(listOf(NovaElectricBlue, Color(0xFF075985)))
                    else Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
                        )
                    )
                )
                .border(
                    1.dp,
                    if (isUser) NovaCyan.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .clickable { onCopy() }
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun XboxAiSheet(
    isVisible: Boolean,
    currentTab: TabItem,
    messages: List<AiMessage>,
    isLoading: Boolean,
    allowAiContext: Boolean,
    onDismiss: () -> Unit,
    onSendMessage: (String, AiAction, Boolean) -> Unit,
    onClearChat: () -> Unit,
    onToggleAiContext: (Boolean) -> Unit
) {
    NovaAiSheet(
        isVisible = isVisible,
        currentTab = currentTab,
        messages = messages,
        isLoading = isLoading,
        allowAiContext = allowAiContext,
        onDismiss = onDismiss,
        onSendMessage = onSendMessage,
        onClearChat = onClearChat,
        onToggleAiContext = onToggleAiContext
    )
}

@Composable
fun NovaAiSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    messages: List<AiMessage>,
    isLoading: Boolean,
    allowAiContext: Boolean,
    onToggleAiContext: (Boolean) -> Unit,
    onSendMessage: (String, AiAction, Boolean) -> Unit,
    onClearChat: () -> Unit,
    currentUrl: String,
    currentTitle: String,
    modifier: Modifier = Modifier
) {
    NovaAiSheet(
        isVisible = isVisible,
        currentTab = TabItem(url = currentUrl, title = currentTitle),
        messages = messages,
        isLoading = isLoading,
        allowAiContext = allowAiContext,
        onDismiss = onDismiss,
        onSendMessage = onSendMessage,
        onClearChat = onClearChat,
        onToggleAiContext = onToggleAiContext
    )
}
