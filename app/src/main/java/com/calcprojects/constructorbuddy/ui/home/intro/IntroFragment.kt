package com.calcprojects.constructorbuddy.ui.home.intro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.home.shapes.ShapesFragment
import kotlinx.android.synthetic.main.fragment_intro.*

/**
 * A simple [Fragment] subclass.
 */
class IntroFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("hkjg","INTRO _ onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("hkjg","INTRO _ onCreateView")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg","INTRO _ onActivityCreated")
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        Log.d("hkjg","INTRO _ onAttachFragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("hkjg","INTRO _ onViewCreated")


        btn_start.setOnClickListener {
            activity?.run {
                this.supportFragmentManager.beginTransaction().add(R.id.frame, ShapesFragment())
                    .addToBackStack(null).commit()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg","INTRO _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg","INTRO _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg","INTRO _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg","INTRO _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg","INTRO _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg","INTRO _ onDestroy")
    }

}
