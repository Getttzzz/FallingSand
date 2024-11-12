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

    data class GridState(
        val gridValue: ArrayList<ArrayList<Int>>,
        val counter: Int = 0,
        val hueValue: Int = 144
    )

    data class SettingState(
        val grainSize: Float = GRAIN_SIZE,
        val updateSpeed: Long = UPDATE_SPEED,
        val colorChangingSpeed: Float = COLOR_CHANGING_SPEED,
    )

    private val _grid = MutableStateFlow(GridState(build2DArrayList(COLS, ROWS)))
    val grid = _grid.asStateFlow()

    private val _settingsState = MutableStateFlow(SettingState())
    val settingState = _settingsState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(_settingsState.value.updateSpeed)

                var oldCounter = _grid.value.counter
                val hueValue = _grid.value.hueValue
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

                        if (state > 0) {

                            val below = if (j < ROWS - 1) grid[i][j + 1] else -1 //-1 is to set 1 for current ij

                            val dir = if (Random.nextBoolean()) 1 else -1

                            val belowA = if (i + dir >= 0 && i + dir <= COLS - 1 && j < ROWS - 1) {
                                grid[i + dir][j + 1]
                            } else -1

                            val belowB = if (i - dir >= 0 && i - dir <= COLS - 1 && j < ROWS - 1) {
                                grid[i - dir][j + 1]
                            } else -1


                            if (j == ROWS - 1) {
                                nextGrid[i][j] = grid[i][j]
                            } else if (below == 0) {
                                nextGrid[i][j + 1] = grid[i][j]
                            } else if (belowA == 0) {
                                nextGrid[i + dir][j + 1] = grid[i][j]
                            } else if (belowB == 0) {
                                nextGrid[i - dir][j + 1] = grid[i][j]
                            } else {
                                nextGrid[i][j] = grid[i][j]
                            }
                        }
                    }
                }

                _grid.value = GridState(nextGrid, ++oldCounter, hueValue)
            }
        }
    }

    fun setClickPosition(xParam: Float, yParam: Float) {
        val grainSize = _settingsState.value.grainSize
        val xTemp = (xParam - grainSize / 2).coerceAtLeast(0f)
        val yTemp = (yParam - grainSize / 2).coerceAtLeast(0f)

        val xRound = round(xTemp / grainSize).toInt()
        val yRound = round(yTemp / grainSize).toInt()

        val x = if (xRound <= 0) 0 else if (xRound >= COLS) COLS - 1 else xRound
        val y = if (yRound <= 0) 0 else if (yRound >= ROWS) ROWS - 1 else yRound

        dropBunchOfParticles(x, y)
    }

    private fun dropBunchOfParticles(x: Int, y: Int) {
        val oldCounter = _grid.value.counter
        val grid = _grid.value.gridValue
        var hueValue = _grid.value.hueValue
        val updatedHueValue = (hueValue + _settingsState.value.colorChangingSpeed).toInt()

        if (x > 0 && y > 0 && x < COLS - 1 && y < ROWS - 1) {
            // [0,0,0]
            // [0,0,0]
            // [0,0,0]
            grid[x - 1][y - 1] = hueValue
            grid[x - 1][y] = hueValue
            grid[x - 1][y + 1] = hueValue

            grid[x][y - 1] = hueValue
            grid[x][y] = hueValue
            grid[x][y + 1] = hueValue

            grid[x + 1][y - 1] = hueValue
            grid[x + 1][y] = hueValue
            grid[x + 1][y + 1] = hueValue
        }

        _grid.value = GridState(grid, oldCounter, updatedHueValue)
    }

    companion object {
        const val GRAIN_SIZE = 20f

        const val UPDATE_SPEED = 3L

        const val COLOR_CHANGING_SPEED = 1f

        const val COLS = 50
        const val ROWS = 108
    }

}