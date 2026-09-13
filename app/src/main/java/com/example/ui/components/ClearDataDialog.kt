package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NovaBrightCyan

@Composable
fun ClearDataDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirmClear: (clearHistory: Boolean, clearCookies: Boolean, clearCache: Boolean) -> Unit
) {
    if (!isVisible) return

    var clearHistory by remember { mutableStateOf(true) }
    var clearCookies by remember { mutableStateOf(true) }
    var clearCache by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Borrar Datos de Navegación",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Selecciona los elementos que deseas eliminar de Nova Browser:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = clearHistory,
                        onCheckedChange = { clearHistory = it },
                        colors = CheckboxDefaults.colors(checkedColor = NovaBrightCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Historial de navegación", fontSize = 14.sp)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = clearCookies,
                        onCheckedChange = { clearCookies = it },
                        colors = CheckboxDefaults.colors(checkedColor = NovaBrightCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cookies y datos de páginas web", fontSize = 14.sp)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = clearCache,
                        onCheckedChange = { clearCache = it },
                        colors = CheckboxDefaults.colors(checkedColor = NovaBrightCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Archivos e imágenes en caché", fontSize = 14.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmClear(clearHistory, clearCookies, clearCache)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("dialog_confirm_clear_button")
            ) {
                Text(text = "Eliminar Datos", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_cancel_clear_button")
            ) {
                Text(text = "Cancelar")
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
