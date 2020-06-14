package com.calcprojects.constructorbuddy.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.calcprojects.constructorbuddy.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.nav_view)
        bottomNavView.setupWithNavController(findNavController(R.id.nav_host_fragment))
        PreferenceManager.setDefaultValues(this, R.xml.preference, false)
    }

    fun showDialogRemoveBanking(
        view: View,
        selectedModelsIds: List<Int>,
        func: (List<Int>) -> Unit
    ) {

        val context = view.context
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle(getString(R.string.alert_dialog_remove_title))
            setIcon(R.drawable.ic_warning)
            setMessage(getString(R.string.alert_dialog_remove_message))
            setPositiveButton(getString(R.string.alert_dialog_remove_Yes)) { _, _ ->
                func(selectedModelsIds)
            }
            setNegativeButton(getString(R.string.alert_dialog_remove_Cancel)) { _, _ -> }
        }
        val alertDialog = builder.create()
        // alertDialog.setCustomView()
        alertDialog.show()
    }

    fun showSnackBar(text: String, view: View) {
        val snackBar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
        val sbView: View = snackBar.view
        //sbView.setBackgroundColor(view.context.resources.getColor(R.color.PortPrimaryDarkVery))
        snackBar.show()
    }

}
