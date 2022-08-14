package com.rick.mvvm_snake.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rick.mvvm_snake.const.*
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

class SnakeViewModel : ViewModel() {
    private val body = mutableListOf<Position>()
    private val size = GAME_VIEW_SIZE
    private var score = 0
    private var bonus: Position? = null
    private var direction = Direction.LEFT

    var snakeLiveData = MutableLiveData<List<Position>>()
    var bonusPositionLiveData = MutableLiveData<Position>()
    var gameStateLiveData = MutableLiveData<GameState>()
    var scoreLiveData = MutableLiveData<Int>()

    fun move(dir: Direction) {
        direction = dir
    }

    fun start() {
        direction = Direction.LEFT

        body.clear()
        for (x in GAME_DEFAULT_VALUE_X_START..GAME_DEFAULT_VALUE_X_END) {
            body.add(Position(x, GAME_DEFAULT_VALUE_Y))
        }

        bonus = nextBonus().also {
            bonusPositionLiveData.value = it
        }

        fixedRateTimer("timer", true, GAME_TIME_DELAY, GAME_TIME_DELAY) {
            val pos = body.first().copy().apply {
                when (direction) {
                    Direction.LEFT -> x--
                    Direction.RIGHT -> x++
                    Direction.UP -> y--
                    Direction.DOWN -> y++
                }

                if (body.contains(this) || x < 0 || x >= size || y < 0 || y >= size) {
                    cancel()
                    gameStateLiveData.postValue(GameState.GAME_OVER)
                }
            }
            body.add(0, pos)
            if (pos != bonus) {
                body.removeLast()
            } else {
                bonus = nextBonus().also {
                    bonusPositionLiveData.postValue(it)
                }
                score++
                scoreLiveData.postValue(score)
            }
            snakeLiveData.postValue(body)
        }
    }

    private fun nextBonus(): Position {
        var pos: Position
        do {
            pos = Position(Random.nextInt(size), Random.nextInt(size))
            Log.d("SnakeViewModel", "nextBonus: $pos")
        } while (body.contains(pos))

        return pos
    }
}

data class Position(var x: Int, var y: Int)

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

enum class GameState {
    GAME_OVER
}