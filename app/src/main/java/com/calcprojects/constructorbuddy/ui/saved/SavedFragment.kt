package com.calcprojects.constructorbuddy.ui.saved

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.ui.AdapterRecyclerSaved
import com.calcprojects.constructorbuddy.ui.PROGRESS_SHOW_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.SPlASH_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.ScreenConfigurations
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedFragment : Fragment(), ScreenConfigurations {

    private lateinit var savedViewModel: SavedViewModel
    private lateinit var adapterRecyclerSaved: AdapterRecyclerSaved
    private var job: Job? = null

    override val hostActivity: Activity?
        get() = activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        savedViewModel = ViewModelProvider(this).get(SavedViewModel::class.java)
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val funcOpenModel: (Int) -> Unit = {
            val action = SavedFragmentDirections.actionOpenSaved(it)
            view.findNavController().navigate(action)
        }

        val funcRemoveModels: (List<Int>) -> Unit = { savedViewModel.removeModel(it) }

        activity?.let {
            adapterRecyclerSaved =
                AdapterRecyclerSaved(it, null, appBar_saved, funcOpenModel, funcRemoveModels)
        }

        recycler_view_saved.setHasFixedSize(true)
        recycler_view_saved.adapter = adapterRecyclerSaved

        savedViewModel.getSavedModels().observe(viewLifecycleOwner, Observer {

            job = CoroutineScope(Main).launch {
                showProgressBar(true)
                delay(PROGRESS_SHOW_DELAY_TIME)
                adapterRecyclerSaved.models = it
                adapterRecyclerSaved.notifyDataSetChanged()
                showProgressBar(false)
            }
        })
    }

    private fun showProgressBar(show: Boolean) {
        progressBarLayRefresh.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        setScreenConfigurations(
            orientationVertical = true,
            fullScreenMode = false, bottomNavViewVisible = true, bottomNavViewAnim = true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        job?.cancel()
    }

}
