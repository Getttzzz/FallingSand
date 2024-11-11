package com.yuriihetsko.fallingsand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    data class GridState(val gridValue: ArrayList<ArrayList<Int>>, val counter: Int)

    private val _grid = MutableStateFlow(GridState(build2DArrayList(ROWS, COLS), 0))
    val grid = _grid.asStateFlow()

    init {
        _grid.value.gridValue[2][0] = 1

        viewModelScope.launch {
            while (true) {
                delay(UPDATE_DELAY)

                var oldCounter = _grid.value.counter
                val grid = _grid.value.gridValue
                val nextGrid = build2DArrayList(ROWS, COLS)


                for (i in 0 until ROWS) {
                    for (j in 0 until COLS) {
                        val state = grid[i][j]

                        if (state == 1) {

                            val below = grid[i][j + 1]

                            if (below == 0 && j < COLS - 1) {
                                nextGrid[i][j] = 0
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

    companion object {
        const val UPDATE_DELAY = 500L
        const val ROWS = 5
        const val COLS = 5
    }

}