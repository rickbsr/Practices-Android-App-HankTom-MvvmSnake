package com.rick.mvvm_snake

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rick.mvvm_snake.viewModel.Direction
import com.rick.mvvm_snake.viewModel.GameState
import com.rick.mvvm_snake.viewModel.SnakeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SnakeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(SnakeViewModel::class.java)
        viewModel.snakeLiveData.observe(this, Observer {
            v_gameView.snakeBody = it
            v_gameView.invalidate()
        })

        viewModel.bonusPositionLiveData.observe(this, {
            v_gameView.updateBonus(it)
        })

        viewModel.scoreLiveData.observe(this, {
            tv_score.text = it.toString()
        })

        viewModel.gameStateLiveData.observe(this, {
            if (it == GameState.GAME_OVER) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.main_alert_dialog_title))
                    .setMessage(getString(R.string.main_alert_dialog_msg_over))
                    .setPositiveButton(getString(R.string.main_alert_dialog_btn_replay)) { _, _ ->
                        viewModel.start()
                    }
                    .setNeutralButton(getString(R.string.main_alert_dialog_btn_cancel)) { _, _ ->
                        this.finish()
                    }
                    .show()
            }
        })

        viewModel.start()

        iv_left.setOnClickListener { viewModel.move(Direction.LEFT) }
        iv_right.setOnClickListener { viewModel.move(Direction.RIGHT) }
        iv_up.setOnClickListener { viewModel.move(Direction.UP) }
        iv_down.setOnClickListener { viewModel.move(Direction.DOWN) }
    }
}