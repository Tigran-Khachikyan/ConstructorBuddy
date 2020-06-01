package com.calcprojects.constructorbuddy.ui.result

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.ui.*
import kotlinx.android.synthetic.main.fragment_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultFragment : Fragment(), ScreenConfigurations, SendModel {

    private var model: Model? = null
    private lateinit var job: Job
    private var resultViewModel: ResultViewModel? = null

    override val hostActivity: Activity?
        get() = activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        resultViewModel = activity?.let { ViewModelProvider(it).get(ResultViewModel::class.java) }
        return inflater.inflate(R.layout.fragment_result, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelId = arguments?.let { ResultFragmentArgs.fromBundle(it).modelId }
        modelId?.let { id ->
            if (id == DEFAULT_RES_ARG) { // not from db

                job = CoroutineScope(Main).launch {
                    delay(SCREEN_DELAY_TIME)
                    setScreenConfigurations(
                        orientationVertical = true,
                        fullScreenMode = false,
                        bottomNavViewVisible = true,
                        bottomNavViewAnim = true
                    )
                }
                //coord_result.setPadding()

                resultViewModel?.getCalculatedModel()?.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        initializeShowingViews(it)
                        initSaving(it, false)
                        initSharing(it)
                    }
                })
            } else {

                job = CoroutineScope(Main).launch {
                    delay(SCREEN_DELAY_TIME)
                    setScreenConfigurations(
                        orientationVertical = true,
                        fullScreenMode = false,
                        bottomNavViewVisible = false,
                        bottomNavViewAnim = false
                    )
                }

                resultViewModel?.getModel(id)?.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        initializeShowingViews(it)
                        initSaving(it, true)
                        initSharing(it)
                    }
                })
            }
        }
    }


   /* override fun onDestroy() {
        super.onDestroy()

        job.cancel()
    }*/

    override fun onDestroyView() {
        super.onDestroyView()

        job.cancel()
    }

    private fun initializeShowingViews(model: Model) {

        model.run {
            val length =
                getString(R.string.length) + ": " + shape.length?.to2p() + " " + units.distance
            val weight = getString(R.string.weight) + ": " + weight.to3p() + " " + units.weight
            tv_res_length_weight.text = if (createdByLength) weight else length
            tv_init_length_weight.text = if (createdByLength) length else weight


            val volume =
                getString(R.string.volume) + " : " + shape.volume?.to2p() + " " + units.volume
            tv_res_total_volume.text = volume

            val price =
                getString(R.string.price) + " : " + price?.value?.to2p() + " " + price?.base?.name
            tv_res_price.let {
                if (this.price != null) it.text = price else it.visibility = View.GONE
            }

            val material =
                getString(R.string.material) + ": " + getString(material.substance.nameRes)
            tv_init_mat.text = material

            val width =
                shape.width?.let { getString(R.string.width) + " : " + it.to2p() + " " + units.distance }
            tv_init_Width.run { if (width != null) text = width else visibility = View.GONE }


            val height =
                shape.height?.let { getString(R.string.height) + " : " + it.to2p() + " " + units.distance }
            tv_init_height.run { if (height != null) text = height else visibility = View.GONE }

            val diameter =
                shape.diameter?.let { getString(R.string.diameter) + " : " + it.to2p() + " " + units.distance }
            tv_init_diameter.run {
                if (diameter != null) text = diameter else visibility = View.GONE
            }

            val side =
                shape.side?.let { getString(R.string.side) + " : " + it.to2p() + " " + units.distance }
            tv_init_side.run { if (side != null) text = side else visibility = View.GONE }


            val thickness =
                shape.thickness?.let { getString(R.string.thickness) + " : " + it.to2p() + " " + units.distance }
            tv_init_thickness.run {
                if (thickness != null) text = thickness else visibility = View.GONE
            }

            val thickness2 =
                shape.thickness2?.let { getString(R.string.thickness) + "2 : " + it.to2p() + " " + units.distance }
            tv_init_thickness2.run {
                if (thickness2 != null) text = thickness2 else visibility = View.GONE
            }

            iv_shape_res.setImageResource(shape.form.imageRes)

        }

    }

    private fun initSharing(model: Model) {
        topAppBar_res_fragment.setOnMenuItemClickListener {
            if (it.itemId == R.id.share)
                shareModels(arrayListOf(model))
            return@setOnMenuItemClickListener false
        }
    }

    private fun initSaving(model: Model, fromDB: Boolean) {

        if (fromDB) btn_save.visibility = View.GONE
        btn_save.setOnClickListener {
            resultViewModel?.save(model)
        }
    }

}
