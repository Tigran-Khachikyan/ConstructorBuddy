package com.calcprojects.constructorbuddy.ui.home.shapes

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Shape
import com.calcprojects.constructorbuddy.ui.AdapterRecyclerShapes
import com.calcprojects.constructorbuddy.ui.MATERIAL_KEY
import com.calcprojects.constructorbuddy.ui.SHAPE_KEY
import com.calcprojects.constructorbuddy.ui.home.calculator.CalcFragment
import kotlinx.android.synthetic.main.fragment_shapes.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ShapesFragment : Fragment(), CoroutineScope {

    private lateinit var viewModel: ShapesViewModel

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private lateinit var func: (Shape) -> Unit
    private lateinit var adapter: AdapterRecyclerShapes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()


        Log.d("hkjg","SHAPES _ onCreate")

        func = { s: Shape ->
            val newFragment = CalcFragment()
            newFragment.arguments = bundleOf(SHAPE_KEY to s.name)
            activity?.run {
                launch {
                    delay(1400)
                    supportFragmentManager.beginTransaction().add(R.id.frame, newFragment)
                        .commit()
                }
            }
        }
        adapter = AdapterRecyclerShapes(requireContext(), func)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg","SHAPES _ onActivityCreated")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d("hkjg","SHAPES _ onCreateView")

        viewModel = ViewModelProvider(this).get(ShapesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_shapes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("hkjg","SHAPES _ onViewCreated")


        recycler_shape_fr.setHasFixedSize(true)
        recycler_shape_fr.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        recycler_shape_fr.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        Log.d("hkjg","SHAPES _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg","SHAPES _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg","SHAPES _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg","SHAPES _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg","SHAPES _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg","SHAPES _ onDestroy")
    }


}
