package com.yuriihetsko.fallingsand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainScreenViewModel : ViewModel() {

    data class GridState(val gridValue: ArrayList<ArrayList<Int>>)

    private val _grid = MutableStateFlow(GridState(build2DArrayList(5, 5)))
    val grid = _grid.asStateFlow()

    init {
        _grid.value.gridValue[3][3] = 1
        _grid.value.gridValue[1][2] = 1
        _grid.value.gridValue[4][4] = 1

        viewModelScope.launch {
            while (true) {
                delay(1000)

//                val randomI = Random(System.currentTimeMillis()).nextInt(0, 6)
//                val randomJ = Random(System.currentTimeMillis()).nextInt(0, 6)
//                println("GETZ.MainScreenViewModel.--> randomI=$randomI randomJ=$randomJ")

                val wtf = _grid.value.gridValue
//                wtf[randomI][randomJ] = 1

                wtf[0][0] = 1
                wtf[1][1] = 1

                _grid.value = GridState(wtf)
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