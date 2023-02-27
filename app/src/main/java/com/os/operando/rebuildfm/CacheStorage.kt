package com.os.operando.rebuildfm

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class CacheStorage(private val context: Context) {

    suspend fun writeCache(inputStream: InputStream) = withContext(Dispatchers.IO) {
        cacheFile().writeBytes(inputStream.readBytes())
    }

    suspend fun readCache(): InputStream = withContext(Dispatchers.IO) {
        cacheFile().inputStream()
    }

    private fun cacheFile(): File {
        return File(context.cacheDir, "rss_cache.xml")
    }
}