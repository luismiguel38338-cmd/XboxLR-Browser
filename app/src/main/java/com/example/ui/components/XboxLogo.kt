package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaBrightCyan
import com.example.ui.theme.NovaCyan
import com.example.ui.theme.NovaElectricBlue

@Composable
fun NovaLogo(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    if (isCompact) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NovaBrightCyan, NovaElectricBlue, Color(0xFF071B2B))
                        )
                    )
                    .border(1.5.dp, NovaCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "N",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Nova Browser",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp
                )
            }
        }
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Futuristic glowing celestial nova badge
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .shadow(16.dp, CircleShape, spotColor = NovaCyan, ambientColor = NovaBrightCyan)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF0E2238), Color(0xFF091321))
                        )
                    )
                    .border(2.5.dp, Brush.sweepGradient(listOf(NovaCyan, NovaBrightCyan, NovaElectricBlue, NovaCyan)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Inner stylized Nova ring
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(NovaCyan.copy(alpha = 0.35f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "N",
                        color = NovaCyan,
                        fontWeight = FontWeight.Black,
                        fontSize = 44.sp,
                        letterSpacing = (-1).sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Official App Title
            Text(
                text = "Nova Browser",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "NAVEGADOR INTELIGENTE • IA",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}

@Composable
fun XboxLogo(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    NovaLogo(modifier = modifier, isCompact = isCompact)
}
