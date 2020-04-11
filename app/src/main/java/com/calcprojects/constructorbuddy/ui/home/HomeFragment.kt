package com.calcprojects.constructorbuddy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.home.intro.IntroFragment
import com.calcprojects.constructorbuddy.ui.home.shapes.ShapesFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("hkjg","HOME _ onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        Log.d("hkjg","HOME _ onCreateView")

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("hkjg","HOME _ onViewCreated")


        activity?.run {
            this.supportFragmentManager.beginTransaction().add(R.id.frame, IntroFragment())
                .commit()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg","HOME _ onActivityCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg","HOME _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg","HOME _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg","HOME _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg","HOME _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg","HOME _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg","HOME _ onDestroy")
    }
}
