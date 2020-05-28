package com.calcprojects.constructorbuddy.ui.shapes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.ui.AdapterRecyclerShapes
import com.calcprojects.constructorbuddy.ui.ConfigFragment
import com.calcprojects.constructorbuddy.ui.SCREEN_DELAY_TIME
import kotlinx.android.synthetic.main.fragment_shapes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class ShapesFragment : ConfigFragment() {

    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setScreenConfigurations()
        recycler_shape_fr.initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        job?.cancel()
    }

    override fun setScreenConfigurations() {
        setScreenOrientationVertical(false)
        setSystemVisibilityFullScreen(false)
        setBottomNavViewVisible(false)
    }

    private fun RecyclerView.initialize() {

        val func = { form: Form ->
            job = CoroutineScope(Main).launch {
                startCalculation(form)
            }
        }
        setHasFixedSize(true)
        adapter = AdapterRecyclerShapes(requireContext(), false, func)
    }

    private suspend fun startCalculation(form: Form) {

        delay(SCREEN_DELAY_TIME)
        try {
            val navController = NavHostFragment.findNavController(this)
            val action = ShapesFragmentDirections.actionStartCalculation(form.name)
            navController.navigate(action)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
