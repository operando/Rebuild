package com.os.operando.rebuildfm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stalla.model.Episode
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val rssFeedRepository: RssFeedRepository
) : ViewModel() {
    private val url = "https://feeds.rebuild.fm/rebuildfm"

    private val _episodes = MutableLiveData<MutableList<Episode>>(mutableStateListOf())
    val episodes: LiveData<MutableList<Episode>> = _episodes

    fun get() {
        viewModelScope.launch {
            try {
                val podcast = rssFeedRepository.getPodcast(url)
                _episodes.value?.addAll(podcast.episodes)
                _episodes.postValue(episodes.value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}