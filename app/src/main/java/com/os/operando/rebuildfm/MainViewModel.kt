package com.os.operando.rebuildfm

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prof.rssparser.Parser
import dev.stalla.PodcastRssParser
import dev.stalla.model.Episode
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

    private val _episodes = MutableLiveData<MutableList<Episode>>(mutableStateListOf())
    val episodes: LiveData<MutableList<Episode>> = _episodes

    fun get() {
        viewModelScope.launch {
            try {
                val channel = parser.getChannel(url)
                Log.d("test", channel.articles.first().toString())
                val podcast = parse()
                _episodes.value?.addAll(podcast.episodes)
                _episodes.postValue(episodes.value)
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