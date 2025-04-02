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
        val gridValue: ArrayList<ArrayList<Int>>? = null,
        val hueValue: Int = 144,

        val isCanvasSizeCalculated: Boolean = false,
        val cols: Int = -1,
        val rows: Int = -1,

        val shouldUpdateNextFrame: Boolean = true,
    )

    private val _grid = MutableStateFlow(GridState())
    val grid = _grid.asStateFlow()

    fun endlessDrawing(width: Int, height: Int) {
        val cols = (width / GRAIN_SIZE).toInt()
        val rows = (height / GRAIN_SIZE).toInt()

        viewModelScope.launch {
            while (true) {

                if (_grid.value.shouldUpdateNextFrame) {
                    redraw(cols, rows)
                }

                delay(UPDATE_SPEED)
            }
        }
    }

    private fun redraw(cols: Int, rows: Int) {
        val hueValue = _grid.value.hueValue
        val grid = _grid.value.gridValue ?: build2DArrayList(cols, rows)
        val nextGrid = build2DArrayList(cols, rows)

        // [0,0,0,0,0]
        // [0,0,0,0,0]
        // [0,0,0,0,0]
        // [0,0,0,0,0]
        // [0,0,1,0,0]

        for (i in 0 until cols) {
            for (j in 0 until rows) {
                val state = grid[i][j]

                if (state > 0) {

                    val below = if (j < rows - 1) grid[i][j + 1] else -1 //-1 is to set 1 for current ij

                    val dir = if (Random.nextBoolean()) 1 else -1

                    val belowA = if (i + dir >= 0 && i + dir <= cols - 1 && j < rows - 1) {
                        grid[i + dir][j + 1]
                    } else -1

                    val belowB = if (i - dir >= 0 && i - dir <= cols - 1 && j < rows - 1) {
                        grid[i - dir][j + 1]
                    } else -1


                    if (j == rows - 1) {
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

        val oldGridHash = _grid.value.gridValue.hashCode()
        val newGridHash = nextGrid.hashCode()
        val shouldUpdateNextFrame = oldGridHash != newGridHash

        _grid.value = GridState(
            shouldUpdateNextFrame = shouldUpdateNextFrame,
            gridValue = nextGrid,
            hueValue = hueValue,
            cols = cols,
            rows = rows,
            isCanvasSizeCalculated = true
        )
    }

    fun setClickPosition(xParam: Float, yParam: Float) {
        val cols = _grid.value.cols
        val rows = _grid.value.rows
        val xTemp = (xParam - GRAIN_SIZE / 2).coerceAtLeast(0f)
        val yTemp = (yParam - GRAIN_SIZE / 2).coerceAtLeast(0f)

        val xRound = round(xTemp / GRAIN_SIZE).toInt()
        val yRound = round(yTemp / GRAIN_SIZE).toInt()

        val x = if (xRound <= 0) 0 else if (xRound >= cols) cols - 1 else xRound
        val y = if (yRound <= 0) 0 else if (yRound >= rows) rows - 1 else yRound

        dropBunchOfParticles(x, y)
    }

    private fun dropBunchOfParticles(x: Int, y: Int) {
        val cols = _grid.value.cols
        val rows = _grid.value.rows
        val grid = _grid.value.gridValue ?: error("Shouldn't be null")
        val hueValue = _grid.value.hueValue

        if (x > 0 && y > 0 && x < cols - 1 && y < rows - 1) {
            for (i in x - 1..x + 1) {
                for (j in y - 1..y + 1) {
                    grid[i][j] = hueValue
                }
            }
        }

        //update color for next drop of sand
        val updatedHueValue = (hueValue + COLOR_CHANGING_SPEED).toInt()

        _grid.value = _grid.value.copy(
            gridValue = grid,
            hueValue = updatedHueValue,
            shouldUpdateNextFrame = true
        )
    }

    companion object {
        const val GRAIN_SIZE = 25f
        const val UPDATE_SPEED = 15L
        const val COLOR_CHANGING_SPEED = 1f

        const val SATURATION = 0.7f
        const val BRIGHTNESS = 1f
    }

}