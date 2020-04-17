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
import com.calcprojects.constructorbuddy.model.StateUIActivity
import com.calcprojects.constructorbuddy.ui.ParentViewState.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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
            setActivityViewConfigurations(it)
        })
    }

    private fun setActivityViewConfigurations(state: StateUIActivity) {
        state.run {
            nav_view.visibility = if (bottomNavViewVisibility) View.VISIBLE else View.GONE
            systemUiVisibility?.let { sv -> window.decorView.systemUiVisibility = sv }
            requestedOrientation?.let { ro -> this@MainActivity.requestedOrientation = ro }
            Log.d("kasasja", "STATE: $state")
        }
    }

}
