package com.calcprojects.constructorbuddy.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.ParentViewState
import com.calcprojects.constructorbuddy.ui.MainViewModel

class SettingsFragment : Fragment() {

    private lateinit var notificationsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainViewModel.setState(ParentViewState.DEFAULT_SHOW_ALL)

        Log.d("hkjg","SETTINGS _ onCreate")

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.d("hkjg","SETTINGS _ onCreateView")

        notificationsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg","SETTINGS _ onActivityCreated")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("hkjg","SETTINGS _ onViewCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg","SETTINGS _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg","SETTINGS _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg","SETTINGS _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg","SETTINGS _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg","SETTINGS _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg","SETTINGS _ onDestroy")
    }
}
