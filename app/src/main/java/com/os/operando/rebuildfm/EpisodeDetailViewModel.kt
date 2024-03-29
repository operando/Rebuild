package com.os.operando.rebuildfm

import android.content.Context
import androidx.lifecycle.*
import com.mpatric.mp3agic.ID3v2ChapterFrameData
import com.mpatric.mp3agic.Mp3File
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stalla.model.Episode
import kotlinx.coroutines.launch
import okhttp3.*
import okio.BufferedSink
import okio.IOException
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val rssFeedRepository: RssFeedRepository
) : ViewModel() {
    private val _episode = MutableLiveData<Episode?>()
    val episode: LiveData<Episode?> = _episode
    private val _audio = MutableLiveData<File?>()
    val audio: LiveData<File?> = _audio
    private var episodeId: String = ""
    private val _chapters = MutableLiveData<List<ID3v2ChapterFrameData>?>()
    val chapters: LiveData<List<ID3v2ChapterFrameData>?> = _chapters
    private val _guests = MutableLiveData<List<String>>()
    val guests: LiveData<List<String>> = _guests

    init {
        savedStateHandle.get<String>("episodeId")?.let {
            episodeId = it
        }
    }

    fun getEpisode(context: Context) {
        viewModelScope.launch {
            val episode = rssFeedRepository.getEpisode(episodeId)
            _episode.postValue(episode)

            _guests.postValue(episode?.atom?.contributors?.filter { it.name != "miyagawa" }
                ?.map { it.name })

            val file = File(context.cacheDir, "$episodeId.mp3")
            if (file.exists()) {
                println("file.exists")
                _audio.postValue(file)
                val mp3File = Mp3File(file)
                val chapters = mp3File.id3v2Tag.chapters
                if (chapters != null) {
                    _chapters.postValue(chapters)
                }
                return@launch
            }

            val request = Request.Builder().apply {
                url(episode!!.enclosure.url)
                get()
            }.build()
            val okHttpClient = OkHttpClient()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    val file = File(context.cacheDir, "$episodeId.mp3")
                    val sink: BufferedSink = file.sink().buffer()
                    response.body?.source()?.let { sink.writeAll(it) }
                    sink.close()

                    _audio.postValue(file)
                    val mp3File = Mp3File(file)
                    val chapters = mp3File.id3v2Tag.chapters
                    if (chapters != null) {
                        _chapters.postValue(chapters)
                    }
                }
            })
        }
    }
}