package com.calcprojects.constructorbuddy.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.MainActivity
import com.calcprojects.constructorbuddy.ui.SCREEN_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.ScreenConfigurations
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class HomeFragment : Fragment(),
    ScreenConfigurations {

    override val hostActivity: Activity?
        get() = activity

    private lateinit var job: Job


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        btn_start.initListener()
    }

    override fun onStart() {
        super.onStart()

        job = CoroutineScope(Main).launch {
            delay(SCREEN_DELAY_TIME)
            setScreenConfigurations(
                orientationVertical = true,
                fullScreenMode = false,
                bottomNavViewVisible = true,
                bottomNavViewAnim = false
            )
        }
    }

    override fun onStop() {
        super.onStop()

        job.cancel()
    }

    private fun Button.initListener() {

        setOnClickListener {
            try {
                val navController = NavHostFragment.findNavController(this@HomeFragment)
                navController.navigate(HomeFragmentDirections.actionGetStarted())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}
