package com.example.ai

import java.util.UUID

data class AiMessage(
    val id: String = UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val action: AiAction = AiAction.CHAT,
    val timestamp: Long = System.currentTimeMillis(),
    val pageContextTitle: String? = null
)
