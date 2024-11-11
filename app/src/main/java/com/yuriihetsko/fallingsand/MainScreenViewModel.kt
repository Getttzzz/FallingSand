package com.yuriihetsko.fallingsand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.round

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


                for (i in 0 until COLS) {
                    for (j in 0 until ROWS) {
                        val state = grid[i][j]

                        if (state == 1) {

                            val below = grid[i][j + 1]

                            if (below == 0 && j < ROWS - 1) {
                                nextGrid[i][j + 1] = 1
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

    fun setClickPosition(x: Float, y: Float) {
        val xTemp = (x - 25).coerceAtLeast(0f)
        val yTemp = (y - 25).coerceAtLeast(0f)

        val xCoord = round(xTemp / 50)
        val yCoord = round(yTemp / 50)

        println("GETZ.MainScreenViewModel.setClickPosition--> xCoord=$xCoord yCoord=$yCoord")
    }

    companion object {
        const val UPDATE_DELAY = 500L
        const val COLS = 5
        const val ROWS = 10
    }

}