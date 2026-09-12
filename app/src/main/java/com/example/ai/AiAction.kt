package com.example.ai

enum class AiAction(val title: String, val promptPrefix: String) {
    CHAT("Conversación", "Eres Xbox IA, el asistente inteligente integrado en el navegador web Xbox L.R. Responde de forma clara, moderna, precisa y profesional."),
    SUMMARIZE_PAGE("Resumir Página", "Por favor proporciona un resumen ejecutivo, claro y estructurado con viñetas de la siguiente página web:"),
    EXPLAIN_TEXT("Explicar", "Explica en términos sencillos, didácticos y comprensibles el siguiente contenido:"),
    TRANSLATE_PAGE("Traducir", "Traduce el siguiente contenido web al español de forma natural y fluida:"),
    ANALYZE_PAGE("Analizar Web", "Realiza un análisis detallado del contenido, propósito, veracidad y puntos clave de la siguiente página web:"),
    EXTRACT_INFO("Extraer Datos", "Extrae la información más importante (fechas, cifras, conceptos clave, conclusiones) en formato de lista estructurada del siguiente texto:"),
    HELP_WRITE("Redactar", "Ayúdame a redactar un texto profesional, claro y persuasivo con base en lo siguiente:"),
    SMART_SEARCH("Búsqueda Inteligente", "Genera una síntesis informativa y objetiva respondiendo a la siguiente búsqueda del usuario:")
}
