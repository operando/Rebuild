package com.os.operando.rebuildfm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.episodeListNavGraph(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    composable(route = EpisodeListNavGraph.episodeListRoute) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Rebuild.fm") },
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                EpisodeList(viewModel, navController)
            }
        }
    }
}

@Composable
fun EpisodeList(viewModel: MainViewModel, navController: NavHostController) {
    viewModel.get()
    val episodes = viewModel.episodes.observeAsState().value
    LazyColumn {
        items(items = episodes!!) { episode ->
            Card(
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        navController.navigate(
                            EpisodeDetailNavGraph.episodeDetailRoute(
                                episode.link ?: ""
                            )
                        )
                    }
                    .wrapContentSize()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = episode.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.heightIn(8.dp))
                    Text(text = episode.itunes?.subtitle ?: "", color = Color.DarkGray)
//                    Spacer(modifier = Modifier.heightIn(4.dp))
//                    Text(text = episode.pubDate.toString())
                }
            }
        }
    }
}

object EpisodeListNavGraph {
    const val episodeListRoute = "episode_list"
}