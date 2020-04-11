package com.calcprojects.constructorbuddy.ui.home.calculator

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calcprojects.constructorbuddy.R


class CalcFragment : Fragment() {

    private lateinit var viewModel: CalcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("hkjg","CALC _ onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("hkjg","CALC _ onCreateView")
        return inflater.inflate(R.layout.fragment_calc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg","CALC _ onActivityCreated")

        viewModel = ViewModelProviders.of(this).get(CalcViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("hkjg","CALC _ onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg","CALC _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg","CALC _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg","CALC _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg","CALC _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg","CALC _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg","CALC _ onDestroy")
    }

}
