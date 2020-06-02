package com.calcprojects.constructorbuddy.ui.saved

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.Model
import com.calcprojects.constructorbuddy.ui.PROGRESS_SHOW_DELAY_TIME
import com.calcprojects.constructorbuddy.ui.ScreenConfigurations
import com.calcprojects.constructorbuddy.ui.SendModel
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_saved.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class SavedFragment : Fragment(), ScreenConfigurations, SendModel {

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

        recycler_view_saved.initialize()

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
            orientationVertical = true, fullScreenMode = false,
            bottomNavViewVisible = true, bottomNavViewAnim = true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        job?.cancel()
    }

    /////////////////////////////////////////////////////////

    private fun RecyclerView.initialize() {

        setHasFixedSize(true)
        adapterRecyclerSaved = AdapterRecyclerSaved(
            requireContext(),
            null,
            object : OnHolderClickListener {

                private var actionMode: ActionMode? = null
                private val selectedModels: ArrayList<Model> = arrayListOf()
                val callback = object : ActionMode.Callback {

                    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        mode?.menuInflater?.inflate(R.menu.menu_contextual, menu)
                        return true
                    }

                    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                        return false
                    }

                    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                        return when (item?.itemId) {
                            R.id.share -> {
                                if (selectedModels.isNotEmpty())
                                    shareModels(selectedModels)
                                mode?.finish()
                                true
                            }
                            R.id.delete -> {
                                if (selectedModels.isNotEmpty()) {
                                    val selectedModelsIds = selectedModels.map { model -> model.id }
                                    savedViewModel.removeModels(selectedModelsIds)
                                }
                                mode?.finish()
                                true
                            }
                            else -> false
                        }
                    }

                    override fun onDestroyActionMode(mode: ActionMode?) {
                        selectedModels.clear()
                        actionMode = null
                        adapterRecyclerSaved.setSelectedList(null)
                        setScreenConfigurations(
                            orientationVertical = true, fullScreenMode = false,
                            bottomNavViewVisible = true, bottomNavViewAnim = true
                        )
                        collapsingLayout.visibility = View.VISIBLE
                    }
                }

                override fun onHolderClick(model: Model) {

                    if (actionMode != null) {
                        if (selectedModels.contains(model))
                            selectedModels.remove(model)
                        else selectedModels.add(model)
                        adapterRecyclerSaved.setSelectedList(selectedModels)
                    } else {
                        try {
                            val action = SavedFragmentDirections.actionOpenSaved(model.id)
                            findNavController().navigate(action)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }

                override fun onHolderLongClick(model: Model): Boolean {

                    return if (actionMode != null) false
                    else {
                        selectedModels.add(model)
                        adapterRecyclerSaved.setSelectedList(selectedModels)
                        actionMode = activity?.run {
                            (this as AppCompatActivity).startSupportActionMode(callback)
                        }
                        setScreenConfigurations(
                            orientationVertical = true, fullScreenMode = false,
                            bottomNavViewVisible = false, bottomNavViewAnim = false
                        )
                        collapsingLayout.visibility = View.GONE
                        true
                    }
                }
            }
        )
        adapter = adapterRecyclerSaved
    }
}
