package com.os.operando.rebuildfm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.favoriteNavGraph(
    navController: NavHostController
) {
    composable(route = FavoriteNavGraph.favoriteRoute) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Favorite") },
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                Favorite(navController)
            }
        }
    }
}

@Composable
fun Favorite(navController: NavHostController) {
    Text(text = "favorite")
}

object FavoriteNavGraph {
    const val favoriteRoute = "favorite"
}