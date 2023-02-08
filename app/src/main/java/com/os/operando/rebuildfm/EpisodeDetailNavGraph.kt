package com.os.operando.rebuildfm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.episodeDetailNavGraph() {
    composable(route = EpisodeDetailNavGraph.episodeDetailRoute) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Rebuild.fm") },
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                Text(text = "Screen 2")
            }

        }
    }
}

object EpisodeDetailNavGraph {
    const val episodeDetailRoute = "episode_detail"
}