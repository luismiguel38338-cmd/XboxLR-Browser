package com.example.data.repository

import com.example.data.local.BookmarkDao
import com.example.data.local.DownloadDao
import com.example.data.local.HistoryDao
import com.example.data.local.UserPreferences
import com.example.data.model.BookmarkEntity
import com.example.data.model.DownloadEntity
import com.example.data.model.HistoryEntity
import kotlinx.coroutines.flow.Flow

class BrowserRepository(
    private val historyDao: HistoryDao,
    private val bookmarkDao: BookmarkDao,
    private val downloadDao: DownloadDao,
    val userPreferences: UserPreferences
) {
    val history: Flow<List<HistoryEntity>> = historyDao.getAllHistory()
    val bookmarks: Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
    val downloads: Flow<List<DownloadEntity>> = downloadDao.getAllDownloads()

    suspend fun addHistory(title: String, url: String) {
        if (url.isBlank() || url.startsWith("about:") || url.startsWith("data:") || url.startsWith("file:")) return
        val cleanTitle = if (title.isBlank()) url else title
        historyDao.insertHistory(HistoryEntity(title = cleanTitle, url = url))
    }

    suspend fun deleteHistory(id: Long) = historyDao.deleteHistoryById(id)
    suspend fun clearHistory() = historyDao.clearAllHistory()

    suspend fun isBookmarked(url: String): Boolean {
        if (url.isBlank()) return false
        return bookmarkDao.getBookmarkByUrl(url) != null
    }

    suspend fun toggleBookmark(title: String, url: String): Boolean {
        if (url.isBlank()) return false
        val existing = bookmarkDao.getBookmarkByUrl(url)
        return if (existing != null) {
            bookmarkDao.deleteBookmarkByUrl(url)
            false
        } else {
            val cleanTitle = if (title.isBlank()) url else title
            bookmarkDao.insertBookmark(BookmarkEntity(title = cleanTitle, url = url))
            true
        }
    }

    suspend fun deleteBookmark(id: Long) = bookmarkDao.deleteBookmarkById(id)

    suspend fun addDownload(fileName: String, url: String, mimeType: String, sizeBytes: Long, filePath: String) {
        downloadDao.insertDownload(
            DownloadEntity(
                fileName = fileName,
                url = url,
                mimeType = mimeType,
                sizeBytes = sizeBytes,
                filePath = filePath
            )
        )
    }

    suspend fun deleteDownload(id: Long) = downloadDao.deleteDownloadById(id)
    suspend fun clearDownloads() = downloadDao.clearAllDownloads()
}
