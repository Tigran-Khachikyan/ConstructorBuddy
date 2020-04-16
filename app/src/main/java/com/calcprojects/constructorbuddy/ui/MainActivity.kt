package com.calcprojects.constructorbuddy.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.ActivityViewStates.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.destination_home,
                R.id.destination_saved,
                R.id.destination_settings
            )
        )
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getState().observe(this, Observer {
            Log.d("asaswe2w","observer: STATE: $it")

            when (it!!) {
                DEFAULT_SHOW_ALL -> {
                    setFullScreenModeOff()
                    nav_view?.setVisibility(true)
                }
                FULL_SCREEN -> {
                    setFullScreenModeOn()
                    nav_view?.setVisibility(false)
                }
                HIDDEN_NAVIGATION_BAR -> {
                    setFullScreenModeOff()
                    nav_view?.setVisibility(false)
                }
            }
        })
    }


    private fun View.setVisibility(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun setFullScreenModeOn() {
        /*  val a = this.window.decorView.systemUiVisibility
          Log.d("hkjg", "SHAPES _ AAAAAAAAA: $a")*/
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun setFullScreenModeOff() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_LAYOUT_FLAGS


        // window.decorView.systemUiVisibility = View.SYST
    }
}
