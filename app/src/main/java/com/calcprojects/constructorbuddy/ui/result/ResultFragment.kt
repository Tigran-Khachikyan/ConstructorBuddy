package com.calcprojects.constructorbuddy.ui.result

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.ui.*
import com.calcprojects.constructorbuddy.ui.calculator.CalcViewModel
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ResultFragment : Fragment(), CoroutineScope {

    private var modelToSave: Model? = null
    private lateinit var priceSharedPreferences: SharedPreferences
    private lateinit var job: Job
    private lateinit var resultViewModel: ResultViewModel
    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        resultViewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        priceSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelId = arguments?.let { ResultFragmentArgs.fromBundle(it).modelId }

        modelId?.let { id ->
            if (id == DEFAULT_RES_ARG) {
                modelToSave = CalcViewModel.modelCalculated
                modelToSave?.let { initialize(it, false) }
            } else {
                resultViewModel.getModel(id).observe(viewLifecycleOwner, Observer {
                    initialize(it, true)
                })
            }
        }

        btn_save.setOnClickListener {
            modelToSave?.let {
                resultViewModel.save(it)
            }
        }

    }

    private fun initialize(model: Model, fromDB: Boolean) {
        model.run {
            Log.d("asaasfwe", "model is not null")

            fromDB.let { if (it) btn_save.visibility = View.GONE }

            val length =
                resources.getString(R.string.length) + ": " + shape.length?.to2p() + " " + units.distance
            val weight =
                resources.getString(R.string.weight) + ": " + weight.to3p() + " " + units.weight
            tv_res_length_weight.text = if (createdByLength) weight else length
            tv_init_length_weight.text = if (createdByLength) length else weight


            val volume =
                resources.getString(R.string.volume) + " : " + shape.volume?.to2p() + " " + units.volume
            tv_res_total_volume.text = volume

            val material =
                resources.getString(R.string.material) + ": " + resources.getString(material.substance.nameRes)
            tv_init_mat.text = material

            val width =
                shape.width?.let { resources.getString(R.string.width) + " : " + it.to2p() + " " + units.distance }
            tv_init_Width.run { if (width != null) text = width else visibility = View.GONE }


            val height =
                shape.height?.let { resources.getString(R.string.height) + " : " + it.to2p() + " " + units.distance }
            tv_init_height.run { if (height != null) text = height else visibility = View.GONE }

            val diameter =
                shape.diameter?.let { resources.getString(R.string.diameter) + " : " + it.to2p() + " " + units.distance }
            tv_init_diameter.run {
                if (diameter != null) text = diameter else visibility = View.GONE
            }

            val side =
                shape.side?.let { resources.getString(R.string.side) + " : " + it.to2p() + " " + units.distance }
            tv_init_side.run { if (side != null) text = side else visibility = View.GONE }


            val thickness =
                shape.thickness?.let { resources.getString(R.string.thickness) + " : " + it.to2p() + " " + units.distance }
            tv_init_thickness.run {
                if (thickness != null) text = thickness else visibility = View.GONE
            }

            val thickness2 =
                shape.thickness2?.let { resources.getString(R.string.thickness) + "2 : " + it.to2p() + " " + units.distance }
            tv_init_thickness2.run {
                if (thickness2 != null) text = thickness2 else visibility = View.GONE
            }

            iv_shape_res.setImageResource(shape.form.imageRes)
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

    private fun configureActivity() {
        activity?.run {
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        MainViewModel.showBottomActionView(show = true, withAnimation = true)
    }

}
