package com.calcprojects.constructorbuddy.ui.result

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.StateUIActivity
import com.calcprojects.constructorbuddy.ui.ParentViewState
import com.calcprojects.constructorbuddy.ui.MainViewModel

class ResultFragment : Fragment() {


    private lateinit var viewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainViewModel.setState(
            StateUIActivity(
                (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION),
                true,
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            )
        )
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
