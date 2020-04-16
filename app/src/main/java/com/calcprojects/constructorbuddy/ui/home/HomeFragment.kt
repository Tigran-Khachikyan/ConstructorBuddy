package com.calcprojects.constructorbuddy.ui.home

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.StateUIActivity
import com.calcprojects.constructorbuddy.ui.ParentViewState
import com.calcprojects.constructorbuddy.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        MainViewModel.setState(
            StateUIActivity(
                (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION),
                true,
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            )
        )
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
