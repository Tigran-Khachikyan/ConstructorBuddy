package com.calcprojects.constructorbuddy.ui.splash

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.MainViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureActivity()
        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()

        launch {
            delay(1500)
            try {
                navController = findNavController(this@SplashFragment)
                navController?.navigate(SplashFragmentDirections.actionFinishSplash())
            } catch (ex: Exception) {
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()

        //switch startDestination from splash to home
        navController?.let {
            resetStartDestinationToHomeFragment(it)
        }
    }

    private fun resetStartDestinationToHomeFragment(navController: NavController) {
        val graph = navController.navInflater.inflate(R.navigation.mobile_navigation)
        graph.startDestination = R.id.toHome
        navController.graph = graph
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun configureActivity() {
        activity?.run {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        MainViewModel.showBottomActionView(false)
    }

}
