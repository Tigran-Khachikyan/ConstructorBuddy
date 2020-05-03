package com.calcprojects.constructorbuddy.ui.saved

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.AdapterRecyclerSaved
import com.calcprojects.constructorbuddy.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment() {

    private var actionMode: ActionMode? = null
    private lateinit var savedViewModel: SavedViewModel
    private lateinit var adapterRecyclerSaved: AdapterRecyclerSaved

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        savedViewModel = ViewModelProvider(this).get(SavedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val funcOpenModel: (Int) -> Unit = {
            Log.d("asaasfwe", "id: $it")
            val action = SavedFragmentDirections.actionOpenSaved(it)
            view.findNavController().navigate(action)
        }


        adapterRecyclerSaved =
            AdapterRecyclerSaved(activity, null, appBar_saved, funcOpenModel)
        recycler_view_saved.setHasFixedSize(true)
        recycler_view_saved.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        recycler_view_saved.adapter = adapterRecyclerSaved

        savedViewModel.getSavedModels().observe(viewLifecycleOwner, Observer {
            adapterRecyclerSaved.models = it
            adapterRecyclerSaved.notifyDataSetChanged()
        })
    }


    override fun onResume() {
        super.onResume()
        configureActivity()
    }

    private fun configureActivity() {
        activity?.run {
            window.statusBarColor = resources.getColor(R.color.colorSecondaryDark)
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        MainViewModel.showBottomActionView(true)
    }

}
