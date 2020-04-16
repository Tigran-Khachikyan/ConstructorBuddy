package com.calcprojects.constructorbuddy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.ActivityViewStates
import com.calcprojects.constructorbuddy.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        MainViewModel.setState(ActivityViewStates.DEFAULT_SHOW_ALL)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_start.setOnClickListener {
            view.findNavController().navigate(HomeFragmentDirections.actionGetStarted())
        }
    }

    override fun onResume() {
        super.onResume()

    }
}
