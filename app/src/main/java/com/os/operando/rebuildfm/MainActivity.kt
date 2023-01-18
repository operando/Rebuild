package com.os.operando.rebuildfm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun List1(viewModel: MainViewModel) {
    val episodes = viewModel.episodes.observeAsState().value
    LazyColumn {
        items(items = episodes!!) { episode ->
            Card(
                colors = cardColors(Color.White),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { }
                    .wrapContentSize()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = episode.title)
                    Spacer(modifier = Modifier.heightIn(8.dp))
                    Text(text = episode.itunes?.subtitle ?: "")
                }
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