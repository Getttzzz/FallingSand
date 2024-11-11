package com.yuriihetsko.fallingsand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuriihetsko.fallingsand.MainScreenViewModel.Companion.PIXEL_SIZE
import com.yuriihetsko.fallingsand.ui.theme.FallingSandTheme


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
    val grid by viewModel.grid.collectAsState()

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        viewModel.setClickPosition(change.position.x, change.position.y)
                    }
                }
        ) {
            for (i in 0 until MainScreenViewModel.COLS) {
                for (j in 0 until MainScreenViewModel.ROWS) {

                    val calculatedColor = if (grid.gridValue[i][j] == 0) Color.Black else Color.White

                    drawRect(
                        color = calculatedColor,
                        topLeft = Offset(i * PIXEL_SIZE, j * PIXEL_SIZE),
                        size = Size(PIXEL_SIZE, PIXEL_SIZE)
                    )
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