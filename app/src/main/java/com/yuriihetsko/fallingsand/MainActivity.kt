package com.yuriihetsko.fallingsand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yuriihetsko.fallingsand.ui.theme.FallingSandTheme

const val PIXEL = 50f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FallingSandTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var grid = remember { build2DArrayList(5, 5) }
    val nextGrid = remember { build2DArrayList(5, 5) }

    grid[3][3] = 1
    grid[1][2] = 1

    println("GETZ.<top>.FallingSandRoot--> grid=${grid}")

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {


        for (i in 0..5) {
            for (j in 0..5) {
                val calculatedColor = if (grid[i][j] == 0) Color.Black else Color.White
                drawRect(color = calculatedColor, topLeft = Offset(i * PIXEL, j * PIXEL), size = Size(PIXEL, PIXEL))
            }
        }

//        for (i in 0..5) {
//            for (j in 0..5) {
//                val state = grid[i][j]
//                if (state == 1) {
//                    val below = grid[i][j + 1]
//                    if (below == 0) {
//                        nextGrid[i][j] = 0
//                        nextGrid[i][j + 1] = 1
//                    }
//                }
//            }
//        }
//
//        grid = nextGrid
    }
}

fun build2DArrayList(rows: Int, columns: Int): ArrayList<ArrayList<Int>> {
    val arrayList2D = ArrayList<ArrayList<Int>>(rows)

    for (i in 0..rows) {
        val row = ArrayList<Int>(columns)
        for (j in 0..columns) {
            row.add(0)
        }
        arrayList2D.add(row)
    }

    return arrayList2D
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FallingSandTheme {
        MainScreen(Modifier.size(400.dp, 400.dp))
    }
}