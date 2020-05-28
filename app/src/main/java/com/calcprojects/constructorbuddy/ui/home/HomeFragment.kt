package com.calcprojects.constructorbuddy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.ConfigFragment
import com.calcprojects.constructorbuddy.ui.SCREEN_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.splash.SplashFragmentDirections
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class HomeFragment : ConfigFragment() {

    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_start.setOnClickListener {

            try {
                val navController = NavHostFragment.findNavController(this)
                navController.navigate(HomeFragmentDirections.actionGetStarted())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }

    override fun onStart() {
        super.onStart()

        job = CoroutineScope(Main).launch {
            delay(SCREEN_DELAY_TIME)
            setScreenConfigurations()
        }
    }

    override fun onStop() {
        super.onStop()

        job.cancel()
    }

    override fun setScreenConfigurations() {

        setBottomNavViewVisible(visible = true)
        setSystemVisibilityFullScreen(false)
        setScreenOrientationVertical(false)
    }
}
