package com.os.operando.rebuildfm

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.settingNavGraph(
    navController: NavHostController
) {
    composable(route = SettingNavGraph.settingRoute) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Setting") },
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                Setting(navController)
            }
        }
    }
}

@Composable
fun Setting(navController: NavHostController) {
    Text(text = "setting")
}

object SettingNavGraph {
    const val settingRoute = "setting"
}