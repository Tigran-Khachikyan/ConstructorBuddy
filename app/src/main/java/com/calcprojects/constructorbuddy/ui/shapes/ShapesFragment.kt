package com.calcprojects.constructorbuddy.ui.shapes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Shape
import com.calcprojects.constructorbuddy.ui.AdapterRecyclerShapes
import com.calcprojects.constructorbuddy.ui.MATERIAL_KEY
import com.calcprojects.constructorbuddy.ui.SHAPE_KEY
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

        activity?.run {

            val a = this.window.decorView.systemUiVisibility
            Log.d("hkjg", "SHAPES _ AAAAAAAAA: $a")

            this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }

        Log.d("hkjg", "SHAPES _ onCreate")


        job = Job()

        func = { s: Shape ->
            val newFragment = CalculatorFragment()
            newFragment.arguments = bundleOf(SHAPE_KEY to s.name)
            activity?.run {
                launch {
                    delay(1200)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_calculation, newFragment)
                        .commit()
                }
            }
        }
        adapter = AdapterRecyclerShapes(requireContext(), false, func)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d("hkjg", "SHAPES _ onCreateView")

        return inflater.inflate(R.layout.fragment_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("hkjg", "SHAPES _ onViewCreated")


        recycler_shape_fr.setHasFixedSize(true)
        recycler_shape_fr.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        recycler_shape_fr.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.selectedPosition = null
        adapter.notifyDataSetChanged()
        Log.d("hkjg", "SHAPES _ onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg", "SHAPES _ onDestroy")
        activity?.run { window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE }
        job.cancel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg", "SHAPES _ onActivityCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg", "SHAPES _ onStart")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg", "SHAPES _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg", "SHAPES _ onDestroyView")
    }


}
