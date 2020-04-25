package com.calcprojects.constructorbuddy.ui.splash

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.StateUIActivity
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
        MainViewModel.setState(
            StateUIActivity(
                (View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN),
                false,
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            )
        )

        job = Job()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ghstd", "onCreateView()")

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("ghstd", "onViewCreated(): navController: ${navController.hashCode()}")


    }


    override fun onStart() {
        super.onStart()
        Log.d("ghstd", "onStart(): navController: ${navController.hashCode()}")

    }

    override fun onResume() {
        super.onResume()
        Log.d("ghstd", "onResume(): navController: ${navController.hashCode()}")

        launch {
            delay(1500)
            Log.d("ghstd", "after DELAY(): navController: ${navController.hashCode()}")
            try {
                navController = findNavController(this@SplashFragment)
                navController?.navigate(SplashFragmentDirections.actionFinishSplash())
            } catch (ex: Exception) {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("ghstd", "onPause(): navController: ${navController.hashCode()}")

    }


    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        Log.d("ghstd", "onDestroyView(): navController: ${navController.hashCode()}")

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

}
