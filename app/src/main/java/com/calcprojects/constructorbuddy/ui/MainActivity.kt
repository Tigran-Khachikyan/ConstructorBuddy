package com.calcprojects.constructorbuddy.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.calcprojects.constructorbuddy.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

    override fun onResume() {
        super.onResume()

        mainViewModel.getState().observe(this, Observer {
            nav_view?.run {
                if (!it.second)
                    visibility = if (it.first) View.VISIBLE else View.GONE  //without animation
                else {
                    if (it.first) {
                        alpha = 0f
                        visibility = View.VISIBLE
                        animate().apply { duration = SCREEN_DELAY_TIME }.alpha(1f).start()
                    } else {
                        animate().apply { duration = SCREEN_DELAY_TIME }.alpha(0f).withEndAction {
                            visibility = View.GONE
                        }.start()
                    }
                }
            }


        })
    }
}
