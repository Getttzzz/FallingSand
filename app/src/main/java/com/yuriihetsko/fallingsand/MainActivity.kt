package com.yuriihetsko.fallingsand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuriihetsko.fallingsand.ui.theme.FallingSandTheme

const val PIXEL = 50f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            FallingSandTheme {
                val viewModel = viewModel<MainScreenViewModel>()
                MainScreen(
                    Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.statusBars.asPaddingValues()), viewModel
                )
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel) {
    val grid = viewModel.grid.collectAsState()
    println("GETZ.<top>.MainScreen--> grid.value.text=${grid.value.counter}")
    Column(modifier = modifier) {
        Text(text = "Some Text=${grid.value.counter}", fontSize = 40.sp)
        Canvas(modifier = modifier.background(Color.LightGray)) {
            for (i in 0..5) {
                for (j in 0..5) {
                    val calculatedColor = if (grid.value.gridValue[i][j] == 0) Color.Black else Color.White
                    drawRect(color = calculatedColor, topLeft = Offset(i * PIXEL, j * PIXEL), size = Size(PIXEL, PIXEL))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FallingSandTheme {
        MainScreen(Modifier.size(400.dp, 400.dp), viewModel<MainScreenViewModel>())
    }
}