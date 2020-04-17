package com.calcprojects.constructorbuddy.ui.result

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.StateUIActivity
import com.calcprojects.constructorbuddy.ui.MainViewModel
import com.calcprojects.constructorbuddy.ui.SCREEN_DELAY_TIME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ResultFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    private val state: StateUIActivity by lazy {
        StateUIActivity(
            (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION),
            true,
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        )
    }
    override val coroutineContext: CoroutineContext
        get() = Main + job
    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d("hhas", "RESULT: onCreateView()")

        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("hhas", "RESULT: onCreate()")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("hhas", "RESULT: onViewCreated()")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hhas", "RESULT: onStart()")

    }

    override fun onStop() {
        super.onStop()
        Log.d("hhas", "RESULT: onStop()")

    }

    override fun onResume() {
        super.onResume()
        Log.d("hhas", "RESULT: onResume()")
        job = Job()
        launch {
            delay(SCREEN_DELAY_TIME)
            MainViewModel.setState(state)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("hhas", "RESULT: onPause()")

        job.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hhas", "RESULT: onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hhas", "RESULT: onDestroy()")

    }


}
