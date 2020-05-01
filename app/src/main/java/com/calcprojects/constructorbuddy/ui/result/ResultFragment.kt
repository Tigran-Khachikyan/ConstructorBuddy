package com.calcprojects.constructorbuddy.ui.result

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.StateUIActivity
import com.calcprojects.constructorbuddy.ui.MainViewModel
import com.calcprojects.constructorbuddy.ui.SCREEN_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.calculator.CalcViewModel
import com.calcprojects.constructorbuddy.ui.to2p
import kotlinx.android.synthetic.main.fragment_result.*
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = CalcViewModel.modelCalculated
        val byLength = CalcViewModel.byLength

        model?.run {
            val length =
                resources.getString(R.string.length) + ": " + shape.length?.to2p() + " " + units.density
            val weight =
                resources.getString(R.string.weight) + ": " + weight.to2p() + " " + units.weight
            byLength?.let {
                tv_res_length_weight.text = if (it) weight else length
                tv_init_length_weight.text = if (it) length else weight
            }

            val volume =
                resources.getString(R.string.volume) + " : " + shape.volume?.to2p() + " " + units.volume
            tv_res_total_volume.text = volume
        }

    }

    override fun onResume() {
        super.onResume()
        job = Job()
        launch {
            delay(SCREEN_DELAY_TIME)
            MainViewModel.setState(state)
        }
    }

    override fun onPause() {
        super.onPause()

        job.cancel()
    }


}
