package com.calcprojects.constructorbuddy.ui.splash

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.SPlASH_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.ScreenConfigurations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment(),
    ScreenConfigurations {

    override val hostActivity: Activity?
        get() = activity

    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setScreenConfigurations(
            orientationVertical = true,
            fullScreenMode = true,
            bottomNavViewVisible = false,
            bottomNavViewAnim = false
        )
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()

        job = CoroutineScope(Main).launch {
            delay(SPlASH_DELAY_TIME)
            try {
                val navController = findNavController(this@SplashFragment)
                navController.navigate(SplashFragmentDirections.actionFinishSplash())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()

        resetStartDestinationToHomeFragment()
    }

    private fun resetStartDestinationToHomeFragment() {

        try {
            val navController = NavHostFragment.findNavController(this)
            val graph = navController.navInflater.inflate(R.navigation.mobile_navigation)
            graph.startDestination = R.id.toHome
            navController.graph = graph
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}
