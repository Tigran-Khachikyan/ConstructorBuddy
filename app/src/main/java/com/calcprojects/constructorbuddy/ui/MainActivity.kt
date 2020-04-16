package com.calcprojects.constructorbuddy.ui

import android.os.Bundle
import android.util.Log
import android.view.ActionProvider
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.ParentViewState.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mainViewModel.getState().observe(this, Observer {

            it.systemUiVisibility?.let { sv -> window.decorView.systemUiVisibility = sv }
            it.requestedOrientation?.let { ro -> requestedOrientation = ro }
            nav_view.visibility = if (it.bottomNavViewVisibility) View.VISIBLE else View.GONE

            Log.d("kasasja", "STATE: $it")

        })


        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.destination_home,
                R.id.destination_saved,
                R.id.destination_settings
            )
        )
        nav_view.setupWithNavController(navController)


    }


    override fun onResume() {
        super.onResume()
    }


    private fun setFullScreenModeOn() {
        /*  val a = this.window.decorView.systemUiVisibility
          Log.d("hkjg", "SHAPES _ AAAAAAAAA: $a")*/
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun setFullScreenModeOff() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
}
