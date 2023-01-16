package com.os.operando.rebuildfm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prof.rssparser.Parser
import dev.stalla.PodcastRssParser
import dev.stalla.model.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder

class MainViewModel : ViewModel() {
    private val url = "https://feeds.rebuild.fm/rebuildfm"
    private val parser = Parser.Builder().build()
    private val okHttpClient by lazy {
        OkHttpClient()
    }

    fun get() {
        viewModelScope.launch {
            try {
                val channel = parser.getChannel(url)
                Log.d("test", channel.articles.first().toString())
                parse()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun parse(): Podcast = withContext(Dispatchers.IO) {
        val request: Request = Builder()
            .url(url)
            .build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body?.byteStream()
        val podcast = PodcastRssParser.parse(inputStream!!)
        Log.d("test", podcast!!.episodes.first().toString())
        return@withContext podcast
    }
}