package com.yuriihetsko.fallingsand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yuriihetsko.fallingsand.ui.theme.FallingSandTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FallingSandTheme {
                val viewModel = viewModel<MainScreenViewModel>()

                /**
                 * It is better to use Scaffold with "innerPaddings" and apply it to the root layout
                 * in order to handle paddings from top (systemBar), from bottom (navigation),
                 * or even camera padding when the phone is rotated.
                 *
                 * For LazyColumn use parameter contentPadding "contentPadding = innerPadding".
                 * For any other root container use just modifier "modifier = Modifier.padding(innerPaddings)".
                 * */
                Scaffold(
                    /**
                     * And for camera padding use this parameter "contentWindowInsets = WindowInsets.safeDrawing".
                     * or "safeContent".
                     * */
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPaddings ->
                    MainScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(innerPaddings),
                        viewModel = viewModel
                    )
                }

            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel) {
    val grid by viewModel.grid.collectAsState()
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { newSize ->
                    if (!grid.isCanvasSizeCalculated) {
                        // This block will run only the first time the Canvas is drawn
                        viewModel.endlessDrawing(newSize.width, newSize.height)
                    }
                }
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.setClickPosition(change.position.x, change.position.y)
                    }
                }
        ) {
            if (!grid.isCanvasSizeCalculated) return@Canvas
            for (i in 0 until grid.cols) {
                for (j in 0 until grid.rows) {
                    val currValue = grid.gridValue?.get(i)?.get(j)!!
                    val calculatedHSBColor = if (currValue > 0) {
                        HSBColor(currValue.toFloat(), MainScreenViewModel.SATURATION, MainScreenViewModel.BRIGHTNESS)
                    } else {
                        HSBColor(0f, 0f, 0f)
                    }
                    drawRect(
                        color = calculatedHSBColor.toColor(),
                        topLeft = Offset(i * MainScreenViewModel.GRAIN_SIZE, j * MainScreenViewModel.GRAIN_SIZE),
                        size = Size(MainScreenViewModel.GRAIN_SIZE, MainScreenViewModel.GRAIN_SIZE)
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