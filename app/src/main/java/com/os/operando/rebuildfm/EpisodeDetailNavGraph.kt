package com.os.operando.rebuildfm

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.google.android.exoplayer2.util.EventLogger
import org.jsoup.Jsoup
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
    val doc = Jsoup.parse(episode?.description ?: "")
    val audio = viewModel.audio.observeAsState().value
    if (audio != null) {
        initPlayer(LocalContext.current, buildMediaSource(LocalContext.current, audio))
    }
    val chapters = viewModel.chapters.observeAsState().value
    if (chapters != null) {
        for (chapter in chapters) {
            println("Chapter title: ${chapter.id}")
            println("Start time: ${chapter.startTime}")
            println("End time: ${chapter.endTime}")
        }
    }
    val guests = viewModel.guests.observeAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(episode?.title ?: "") },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(rememberScrollState())) {
            Text(text = episode?.itunes?.subtitle ?: "")
            doc.getElementsByTag("a").map { it ->
                Text(text = it.text(), modifier = Modifier.clickable {
                    println(it.attr("href"))
                })
            }
            Button(enabled = audio != null, onClick = {
                audioPlayer?.playWhenReady = true
            }) {
                Text(text = "再生")
            }
            Button(enabled = audio != null, onClick = {
                audioPlayer?.playWhenReady = false
            }) {
                Text(text = "停止")
            }
            Button(enabled = audio != null, onClick = {
                audioPlayer?.setPlaybackSpeed(2.0f)
            }) {
                Text(text = "2倍速")
            }
            Text(text = "ゲスト", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                guests?.map { it ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1E9E9))
                    ) {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
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