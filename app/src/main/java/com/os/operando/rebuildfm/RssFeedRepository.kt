package com.os.operando.rebuildfm

import android.net.Uri
import dev.stalla.PodcastRssParser
import dev.stalla.model.Episode
import dev.stalla.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class RssFeedRepository(private val cacheStorage: CacheStorage) {

    private val okHttpClient by lazy {
        OkHttpClient()
    }

    private var podcast: Podcast? = null

    suspend fun getPodcast(url: String): Podcast = withContext(Dispatchers.IO) {
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body?.byteStream()
        inputStream?.let { cacheStorage.writeCache(it) }
        podcast = PodcastRssParser.parse(cacheStorage.readCache())
        return@withContext podcast!!
    }

    suspend fun getEpisode(episodeId: String): Episode? = withContext(Dispatchers.IO) {
        return@withContext podcast?.episodes?.firstOrNull { Uri.parse(it.link).pathSegments.first() == episodeId }
    }
}