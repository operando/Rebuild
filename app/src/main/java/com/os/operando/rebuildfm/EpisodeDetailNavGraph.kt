package com.os.operando.rebuildfm

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

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
    viewModel.getEpisode()
    val episode = viewModel.episode.observeAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(episode?.title ?: "") },
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            Text(text = episode?.itunes?.subtitle ?: "")
        }

    }
}

object EpisodeDetailNavGraph {
    const val episodeDetailRoute = "episode_detail/{episodeId}"
    fun episodeDetailRoute(episodeUrl: String) =
        "episode_detail/${Uri.parse(episodeUrl).pathSegments.first()}"
}