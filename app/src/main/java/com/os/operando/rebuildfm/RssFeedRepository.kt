package com.os.operando.rebuildfm

import android.net.Uri
import android.util.Log
import dev.stalla.PodcastRssParser
import dev.stalla.model.Episode
import dev.stalla.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class RssFeedRepository {

    private val url = "https://feeds.rebuild.fm/rebuildfm"

    private val okHttpClient by lazy {
        OkHttpClient()
    }

    suspend fun getPodcast(url: String): Podcast = withContext(Dispatchers.IO) {
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body?.byteStream()
        val podcast = PodcastRssParser.parse(inputStream!!)
        Log.d("RssFeedRepository", podcast!!.episodes.first().toString())
        return@withContext podcast
    }

    suspend fun getEpisode(episodeId: String): Episode? = withContext(Dispatchers.IO) {
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body?.byteStream()
        val podcast = PodcastRssParser.parse(inputStream!!)
        return@withContext podcast?.episodes?.firstOrNull { Uri.parse(it.link).pathSegments.first() == episodeId }
    }
}