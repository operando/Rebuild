package com.os.operando.rebuildfm

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.os.operando.rebuildfm.ui.theme.RebuildfmTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    data class BottomNavItem(
        val name: String,
        val route: String,
        val icon: ImageVector,
    )

    private val bottomNavItems = listOf(
        BottomNavItem(
            name = "Home",
            route = EpisodeListNavGraph.episodeListRoute,
            icon = Icons.Rounded.Home,
        ),
        BottomNavItem(
            name = "Favorite",
            route = FavoriteNavGraph.favoriteRoute,
            icon = Icons.Rounded.Favorite,
        ),
        BottomNavItem(
            name = "Settings",
            route = SettingNavGraph.settingRoute,
            icon = Icons.Rounded.Settings,
        ),
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RebuildfmTheme {
                Scaffold(bottomBar = {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            val backStackEntry = navController.currentBackStackEntryAsState()
                            val selected = item.route == backStackEntry.value?.destination?.route

                            NavigationBarItem(
                                selected = selected,
                                onClick = { navController.navigate(item.route) },
                                label = {
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = "${item.name} Icon",
                                    )
                                }
                            )
                        }
                    }
                }, content = {
                    NavHost(
                        navController = navController,
                        startDestination = EpisodeListNavGraph.episodeListRoute
                    ) {
                        episodeListNavGraph(viewModel, navController)
                        episodeDetailNavGraph()
                        favoriteNavGraph(navController)
                        settingNavGraph(navController)
                    }
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RebuildfmTheme {
    }
}