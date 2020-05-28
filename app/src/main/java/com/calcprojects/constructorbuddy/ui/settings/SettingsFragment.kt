package com.calcprojects.constructorbuddy.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.calcprojects.constructorbuddy.R

class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d("hkjg", "SETTINGS _ onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("hkjg", "SETTINGS _ onCreateView")

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg", "SETTINGS _ onActivityCreated")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("hkjg", "SETTINGS _ onViewCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg", "SETTINGS _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg", "SETTINGS _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg", "SETTINGS _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg", "SETTINGS _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg", "SETTINGS _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg", "SETTINGS _ onDestroy")
    }
}
