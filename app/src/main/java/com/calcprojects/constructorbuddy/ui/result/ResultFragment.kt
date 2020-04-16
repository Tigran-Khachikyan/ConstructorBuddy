package com.calcprojects.constructorbuddy.ui.result

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.ActivityViewStates
import com.calcprojects.constructorbuddy.ui.MainViewModel

class ResultFragment : Fragment() {


    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainViewModel.setState(ActivityViewStates.HIDDEN_NAVIGATION_BAR)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
