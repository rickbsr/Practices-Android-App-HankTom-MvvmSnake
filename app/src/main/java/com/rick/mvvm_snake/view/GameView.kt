package com.rick.mvvm_snake.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.rick.mvvm_snake.const.GAME_VIEW_SIZE
import com.rick.mvvm_snake.viewModel.Position

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paintBonus = Paint().apply { color = Color.RED }
    private val paint: Paint = Paint().apply { color = Color.BLUE }
    var snakeBody: List<Position>? = null
    var bonus: Position? = null
    var dimHeight = 0
    var dimWight = 0
    var gap = 5

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.run {
            bonus?.apply {
                drawRect(
                    (x * dimWight + gap).toFloat(),
                    (y * dimHeight + gap).toFloat(),
                    ((x + 1) * dimWight - gap).toFloat(),
                    ((y + 1) * dimHeight - gap).toFloat(),
                    paintBonus
                )
            }
            snakeBody?.forEach {
                drawRect(
                    (it.x * dimWight + gap).toFloat(),
                    (it.y * dimHeight + gap).toFloat(),
                    ((it.x + 1) * dimWight - gap).toFloat(),
                    ((it.y + 1) * dimHeight - gap).toFloat(),
                    paint
                )
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        dimHeight = height / GAME_VIEW_SIZE
        dimWight = width / GAME_VIEW_SIZE
    }

    fun updateBonus(pos: Position?) {
        bonus = pos
        invalidate()
    }
}