package com.calcprojects.constructorbuddy.ui.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.MainViewModel
import com.calcprojects.constructorbuddy.ui.SCREEN_DELAY_TIME
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class HomeFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_start.setOnClickListener {
            try {
                view.findNavController().navigate(HomeFragmentDirections.actionGetStarted())
            } catch (ex: Exception) {
                Log.d("exx", "${ex.message}")
            }
        }
    }

    override fun onResume() {
        super.onResume()

        job = Job()
        launch {
            delay(SCREEN_DELAY_TIME)
            configureActivity()
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun configureActivity(){
        activity?.run {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        MainViewModel.showBottomActionView(true)
    }
}
