package com.os.operando.rebuildfm

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSource
import java.io.File

fun NavGraphBuilder.episodeDetailNavGraph() {
    composable(
        route = EpisodeDetailNavGraph.episodeDetailRoute,
        arguments = listOf(navArgument("episodeId") { type = NavType.StringType })
    ) {
        EpisodeDetail()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetail(viewModel: EpisodeDetailViewModel = hiltViewModel()) {
    viewModel.getEpisode(LocalContext.current)
    val episode = viewModel.episode.observeAsState().value
    val audio = viewModel.audio.observeAsState().value
    if (audio != null) {
        initPlayer(LocalContext.current, buildMediaSource(LocalContext.current, audio))
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(episode?.title ?: "") },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = episode?.itunes?.subtitle ?: "")
            Button(enabled = audio != null, onClick = {
                audioPlayer?.playWhenReady = true
            }) {
                Text(text = "再生")
            }
        }

    }
}

private var audioPlayer: ExoPlayer? = null

private val playerEventListener = object : Player.Listener {
}

private fun buildMediaSource(context: Context, file: File): MediaSource? {
    return try {
        val uri = Uri.fromFile(file)
        val factory = DefaultDataSource.Factory(
            context,
        )
        ProgressiveMediaSource.Factory(factory)
            .createMediaSource(MediaItem.Builder().setUri(uri).build())
    } catch (e: Exception) {
        null
    }
}

private fun initPlayer(context: Context, mediaSource: MediaSource?) {
    mediaSource ?: return
    val trackSelector = DefaultTrackSelector(context)
    val loadControl = DefaultLoadControl()
    audioPlayer =
        ExoPlayer.Builder(context).setTrackSelector(trackSelector).setLoadControl(loadControl)
            .build()
    audioPlayer?.setMediaSource(mediaSource)
    audioPlayer?.prepare()
    audioPlayer?.addListener(playerEventListener)
    audioPlayer?.playWhenReady = false
}


object EpisodeDetailNavGraph {
    const val episodeDetailRoute = "episode_detail/{episodeId}"
    fun episodeDetailRoute(episodeUrl: String) =
        "episode_detail/${Uri.parse(episodeUrl).pathSegments.first()}"
}