package com.calcprojects.constructorbuddy.ui.saved

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
import com.calcprojects.constructorbuddy.ui.ActivityViewStates
import com.calcprojects.constructorbuddy.ui.MainViewModel

class SavedFragment : Fragment() {

    private lateinit var dashboardViewModel: SavedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainViewModel.setState(ActivityViewStates.DEFAULT_SHOW_ALL)

        Log.d("hkjg","SAVED _ onCreate")

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        Log.d("hkjg","SAVED _ onCreateView")

        dashboardViewModel =
                ViewModelProviders.of(this).get(SavedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_saved, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("hkjg","SAVED _ onActivityCreated")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("hkjg","SAVED _ onViewCreated")

    }

    override fun onStart() {
        super.onStart()
        Log.d("hkjg","SAVED _ onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("hkjg","SAVED _ onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("hkjg","SAVED _ onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("hkjg","SAVED _ onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("hkjg","SAVED _ onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("hkjg","SAVED _ onDestroy")
    }
}
