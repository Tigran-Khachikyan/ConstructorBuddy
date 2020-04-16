package com.calcprojects.constructorbuddy.ui.shapes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Shape
import com.calcprojects.constructorbuddy.ui.*
import com.calcprojects.constructorbuddy.ui.calculator.CalculatorFragment
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
    private lateinit var func: (Shape) -> Unit
    private lateinit var adapter: AdapterRecyclerShapes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainViewModel.setState(ActivityViewStates.FULL_SCREEN)
        Log.d("asaswe2w","Shapes: onCreate")



        job = Job()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d("asaswe2w","Shapes: onCreateView")

        return inflater.inflate(R.layout.fragment_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        func = { s: Shape ->
            launch {
                delay(1200)
                val action = ShapesFragmentDirections.actionStartCalculation(s.name)
                view.findNavController().navigate(action)
            }
        }

        adapter = AdapterRecyclerShapes(requireContext(), false, func)

        Log.d("asaswe2w","Shapes: onViewCreated")


        recycler_shape_fr.setHasFixedSize(true)
        recycler_shape_fr.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        recycler_shape_fr.adapter = adapter
    }


    override fun onResume() {
        super.onResume()
        adapter.selectedPosition = null
        adapter.notifyDataSetChanged()
        Log.d("asaswe2w","Shapes: onResume")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("asaswe2w","Shapes: onDestroy")
        job.cancel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("asaswe2w","Shapes: onActivityCreated")


    }

    override fun onStart() {
        super.onStart()
        Log.d("asaswe2w","Shapes: onStart")

    }

    override fun onPause() {
        super.onPause()
        Log.d("asaswe2w","Shapes: onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d("asaswe2w","Shapes: onStop")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("asaswe2w","Shapes: onDestroyView")
    }


}
