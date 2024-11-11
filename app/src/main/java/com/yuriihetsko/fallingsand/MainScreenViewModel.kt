package com.yuriihetsko.fallingsand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.random.Random

class MainScreenViewModel : ViewModel() {

    data class GridState(val gridValue: ArrayList<ArrayList<Int>>, val counter: Int)

    private val _grid = MutableStateFlow(GridState(build2DArrayList(COLS, ROWS), 0))
    val grid = _grid.asStateFlow()

    init {
        _grid.value.gridValue[2][0] = 1

        viewModelScope.launch {
            while (true) {
                delay(UPDATE_DELAY)

                var oldCounter = _grid.value.counter
                val grid = _grid.value.gridValue
                val nextGrid = build2DArrayList(COLS, ROWS)

                // [0,0,0,0,0]
                // [0,0,0,0,0]
                // [0,0,0,0,0]
                // [0,0,0,0,0]
                // [0,0,1,0,0]

                for (i in 0 until COLS) {
                    for (j in 0 until ROWS) {
                        val state = grid[i][j]

                        if (state == 1) {

                            val below = if (j < ROWS - 1) grid[i][j + 1] else -1 //-1 is to set 1 for current ij

                            val dir = if (Random.nextBoolean()) 1 else -1

                            val belowA = if (i + dir >= 0 && i + dir <= COLS - 1 && j < ROWS - 1) {
                                grid[i + dir][j + 1]
                            } else -1

                            val belowB = if (i - dir >= 0 && i - dir <= COLS - 1 && j < ROWS - 1) {
                                grid[i - dir][j + 1]
                            } else -1


                            if (j == ROWS - 1) {
                                nextGrid[i][j] = 1
                            } else if (below == 0) {
                                nextGrid[i][j + 1] = 1
                            } else if (belowA == 0) {
                                nextGrid[i + dir][j + 1] = 1
                            } else if (belowB == 0) {
                                nextGrid[i - dir][j + 1] = 1
                            } else {
                                nextGrid[i][j] = 1
                            }
                        }
                    }
                }

                _grid.value = GridState(nextGrid, ++oldCounter)
            }
        }
    }

    fun setClickPosition(xParam: Float, yParam: Float) {
        val xTemp = (xParam - PIXEL_SIZE / 2).coerceAtLeast(0f)
        val yTemp = (yParam - PIXEL_SIZE / 2).coerceAtLeast(0f)

        val xRound = round(xTemp / PIXEL_SIZE).toInt()
        val yRound = round(yTemp / PIXEL_SIZE).toInt()

        val x = if (xRound <= 0) 0 else if (xRound >= COLS) COLS - 1 else xRound
        val y = if (yRound <= 0) 0 else if (yRound >= ROWS) ROWS - 1 else yRound

        println("GETZ.MainScreenViewModel.setClickPosition--> x=$x y=$y")

        var oldCounter = _grid.value.counter
        val grid = _grid.value.gridValue
        grid[x][y] = 1
        _grid.value = GridState(grid, ++oldCounter)
    }

    companion object {
        const val UPDATE_DELAY = 30L
        const val COLS = 100
        const val ROWS = 150
        const val PIXEL_SIZE = 10f
    }

}