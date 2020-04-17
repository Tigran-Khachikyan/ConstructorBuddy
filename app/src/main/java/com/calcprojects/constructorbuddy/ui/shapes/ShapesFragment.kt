package com.calcprojects.constructorbuddy.ui.shapes

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Shape
import com.calcprojects.constructorbuddy.model.StateUIActivity
import com.calcprojects.constructorbuddy.ui.*
import kotlinx.android.synthetic.main.fragment_shapes.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class ShapesFragment : Fragment(), CoroutineScope {

    private val state: StateUIActivity by lazy {
        StateUIActivity(
            (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN),
            false,
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        )
    }


    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var func: (Shape) -> Unit
    private lateinit var adapter: AdapterRecyclerShapes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("asaswe2w", "Shapes: onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        Log.d("asaswe2w", "Shapes: onCreateView")

        return inflater.inflate(R.layout.fragment_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        job = Job()
        MainViewModel.setState(state)
        func = { s: Shape ->
            launch {
                startCalculation(s, view.findNavController())
            }
        }

        adapter = AdapterRecyclerShapes(requireContext(), false, func)

        Log.d("asaswe2w", "Shapes: onViewCreated")


        recycler_shape_fr.setHasFixedSize(true)
        recycler_shape_fr.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        recycler_shape_fr.adapter = adapter
    }

    private suspend fun startCalculation(shape: Shape, navController: NavController) {
        delay(1200)
        val action = ShapesFragmentDirections.actionStartCalculation(shape.name)
        navController.navigate(action)
    }


    override fun onResume() {
        super.onResume()

        adapter.selectedPosition = null
        adapter.notifyDataSetChanged()
        Log.d("asaswe2w", "Shapes: onResume")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("asaswe2w", "Shapes: onDestroy")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("asaswe2w", "Shapes: onActivityCreated")


    }

    override fun onStart() {
        super.onStart()
        Log.d("asaswe2w", "Shapes: onStart")

    }

    override fun onPause() {
        super.onPause()
        Log.d("asaswe2w", "Shapes: onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d("asaswe2w", "Shapes: onStop")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        Log.d("asaswe2w", "Shapes: onDestroyView")
    }


}
