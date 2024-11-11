package com.yuriihetsko.fallingsand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {

    data class GridState(val gridValue: ArrayList<ArrayList<Int>>, val counter: Int)

    private val _grid = MutableStateFlow(GridState(build2DArrayList(5, 5), 0))
    val grid = _grid.asStateFlow()

    init {
        _grid.value.gridValue[1][1] = 1

        viewModelScope.launch {
            while (true) {
            delay(1000)

            var oldCounter = _grid.value.counter

            val grid = _grid.value.gridValue
            val nextGrid = build2DArrayList(5, 5)


            for (i in 0..4) {
                for (j in 0..4) {
                    val state = grid[i][j]
                    println("GETZ.MainScreenViewModel.--> i=$i j=$j state=$state")

                    if (state == 1) {

                        val below = grid[i][j + 1]
                        println("GETZ.MainScreenViewModel.--> i=$i j=$j below=$below")
                        if (below == 0 && j < 5 - 1) {
                            nextGrid[i][j] = 0
                            nextGrid[i][j + 1] = 1
                        } else {
                            nextGrid[i][j] = 1
                        }
                    }
                }


            }

            val incremented = ++oldCounter
            _grid.value = GridState(nextGrid, incremented)
        }


        }
    }


}