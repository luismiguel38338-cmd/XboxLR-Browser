package com.example.data.model

data class QuickShortcut(
    val id: String,
    val title: String,
    val url: String,
    val iconLetter: String,
    val colorHex: Long = 0xFF107C10
)
