package com.os.operando.rebuildfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.os.operando.rebuildfm.ui.theme.RebuildfmTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RebuildfmTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column() {
//                        Button(onClick = {
//                            viewModel.add()
//                        }) {
//                            Text("Add")
//                        }
                        List1(viewModel)
                    }
                }
            }
        }

        viewModel.get()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "$name!")
}

@Composable
fun List1(viewModel: MainViewModel) {
    val episodes = viewModel.episodes.observeAsState().value
    LazyColumn {
        items(items = episodes!!) { episode ->
            Greeting(episode)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RebuildfmTheme {
        Greeting("Android")
    }
}