package com.calcprojects.constructorbuddy.ui.saved

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import com.calcprojects.constructorbuddy.R
import kotlinx.android.synthetic.main.fragment_saved.*


class SavedFragment : Fragment() {

    private var actionMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("hkjg", "SAVED _ onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        btnClick.setOnLongClickListener {
            return@setOnLongClickListener if (actionMode != null) false else {
                actionMode = (activity as AppCompatActivity).startSupportActionMode(callback)
                true
            }
        }
    }


    private val callback = object : ActionMode.Callback {

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
                    // Handle share icon press
                    mode?.finish()
                    true
                }
                R.id.delete -> {
                    // Handle delete icon press
                    mode?.finish()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
    }

}
