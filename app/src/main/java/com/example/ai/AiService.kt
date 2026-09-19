package com.example.ai

interface AiService {
    val providerName: String
    suspend fun sendPrompt(
        prompt: String,
        pageTitle: String? = null,
        pageUrl: String? = null,
        pageContent: String? = null,
        action: AiAction = AiAction.CHAT,
        customKey: String? = null,
        modelName: String = "gemini-3.5-flash"
    ): Result<String>
}
