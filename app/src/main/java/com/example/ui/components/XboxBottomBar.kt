package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaBrightCyan
import com.example.ui.theme.NovaCyan
import com.example.ui.theme.NovaElectricBlue

@Composable
fun NovaBottomBar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    tabCount: Int,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onHome: () -> Unit,
    onOpenAi: () -> Unit,
    onOpenTabs: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Atrás
            IconButton(
                onClick = onBack,
                enabled = canGoBack,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("bottombar_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás",
                    tint = if (canGoBack) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }

            // Adelante
            IconButton(
                onClick = onForward,
                enabled = canGoForward,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("bottombar_forward_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Adelante",
                    tint = if (canGoForward) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }

            // CENTRAL PROMINENT GLOWING "NOVA AI" BUTTON
            Box(
                modifier = Modifier
                    .shadow(10.dp, RoundedCornerShape(26.dp), spotColor = NovaCyan, ambientColor = NovaBrightCyan)
                    .clip(RoundedCornerShape(26.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0C243B), Color(0xFF091626))
                        )
                    )
                    .border(1.5.dp, Brush.horizontalGradient(listOf(NovaCyan, NovaBrightCyan)), RoundedCornerShape(26.dp))
                    .clickable { onOpenAi() }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("bottombar_nova_ia_button"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NovaCyan, NovaElectricBlue)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Box(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Nova AI",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = NovaCyan,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            // Inicio
            IconButton(
                onClick = onHome,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("bottombar_home_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Inicio",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            // Pestañas
            IconButton(
                onClick = onOpenTabs,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("bottombar_tabs_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Tab,
                        contentDescription = "Pestañas",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun XboxBottomBar(
    canGoBack: Boolean,
    canGoForward: Boolean,
    tabCount: Int,
    onBack: () -> Unit,
    onForward: () -> Unit,
    onHome: () -> Unit,
    onOpenAi: () -> Unit,
    onOpenTabs: () -> Unit,
    modifier: Modifier = Modifier
) {
    NovaBottomBar(
        canGoBack = canGoBack,
        canGoForward = canGoForward,
        tabCount = tabCount,
        onBack = onBack,
        onForward = onForward,
        onHome = onHome,
        onOpenAi = onOpenAi,
        onOpenTabs = onOpenTabs,
        modifier = modifier
    )
}
