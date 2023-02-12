package com.os.operando.rebuildfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.os.operando.rebuildfm.ui.theme.RebuildfmTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            RebuildfmTheme {
                NavHost(
                    navController = navController,
                    startDestination = EpisodeListNavGraph.episodeListRoute
                ) {
                    episodeListNavGraph(viewModel, navController)
                    episodeDetailNavGraph()
                }
            }
        }

        viewModel.get()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RebuildfmTheme {
    }
}