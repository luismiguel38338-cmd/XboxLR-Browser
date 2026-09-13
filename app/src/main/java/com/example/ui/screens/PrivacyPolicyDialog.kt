package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrivacyPolicyDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    if (!isVisible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Política de Privacidad y Seguridad",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "En Nova Browser la privacidad, la soberanía del usuario y la seguridad son principios fundamentales.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                PolicyBullet(
                    title = "1. Navegación Privada e Incógnito",
                    description = "En modo incógnito, no se almacena ningún registro de historial, cookies ni memoria caché en tu dispositivo. Al cerrar la pestaña de incógnito, la sesión se destruye por completo."
                )

                PolicyBullet(
                    title = "2. Protección de Inteligencia Artificial (Nova AI)",
                    description = "Nova AI no recopila información personal identificable. El asistente únicamente analiza el texto de la página web actual si mantienes activada la opción 'Permitir análisis de página web'. Tienes el control total para activar o desactivar este permiso en cualquier momento."
                )

                PolicyBullet(
                    title = "3. Sin Almacenamiento de Datos Sensibles",
                    description = "Nova Browser nunca almacena contraseñas, números de tarjeta ni credenciales bancarias. Las consultas se realizan mediante conexiones HTTPS cifradas de extremo a extremo."
                )

                PolicyBullet(
                    title = "4. Almacenamiento Local (Room Database)",
                    description = "Tus marcadores e historial se guardan de forma 100% local en la base de datos interna de tu dispositivo Android mediante Room. Ningún dato de navegación es vendido o transmitido a terceros."
                )

                PolicyBullet(
                    title = "5. Control de Borrado",
                    description = "Puedes eliminar tu historial, cookies y archivos en caché en cualquier instante desde el menú 'Borrar datos de navegación'."
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("privacy_policy_confirm_button")
            ) {
                Text("Entendido y Aceptar")
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
private fun PolicyBullet(
    title: String,
    description: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.sp
        )
    }
}
