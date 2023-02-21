package com.os.operando.rebuildfm

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.stalla.model.Episode
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val rssFeedRepository = RssFeedRepository()
    private val _episode = MutableLiveData<Episode?>()
    val episode: LiveData<Episode?> = _episode
    private var episodeId: String = ""

    init {
        savedStateHandle.get<String>("episodeId")?.let {
            episodeId = it
        }
    }

    fun getEpisode() {
        viewModelScope.launch {
            val episode = rssFeedRepository.getEpisode(episodeId)
            _episode.postValue(episode)
        }
    }
}