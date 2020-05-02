package com.calcprojects.constructorbuddy.ui.shapes

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.figures.Form
import com.calcprojects.constructorbuddy.ui.*
import kotlinx.android.synthetic.main.fragment_shapes.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class ShapesFragment : Fragment(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var func: (Form) -> Unit
    private lateinit var adapter: AdapterRecyclerShapes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        configureActivity()
        return inflater.inflate(R.layout.fragment_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        job = Job()
        func = { s: Form ->
            launch {
                startCalculation(s, view.findNavController())
            }
        }

        adapter = AdapterRecyclerShapes(requireContext(), false, func)

        recycler_shape_fr.setHasFixedSize(true)
        recycler_shape_fr.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        recycler_shape_fr.adapter = adapter
    }

    private suspend fun startCalculation(shape: Form, navController: NavController) {
        delay(1200)
        val action = ShapesFragmentDirections.actionStartCalculation(shape.name)
        navController.navigate(action)
    }


    override fun onResume() {
        super.onResume()

        adapter.selectedPosition = null
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
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
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        MainViewModel.showBottomActionView(false)
    }
}
