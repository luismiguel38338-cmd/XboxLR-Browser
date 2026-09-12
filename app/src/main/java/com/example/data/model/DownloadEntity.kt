package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloads")
data class DownloadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileName: String,
    val url: String,
    val mimeType: String = "",
    val sizeBytes: Long = 0,
    val filePath: String = "",
    val status: String = "Completado",
    val timestamp: Long = System.currentTimeMillis()
)
