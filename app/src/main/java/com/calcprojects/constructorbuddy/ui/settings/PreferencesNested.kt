package com.calcprojects.constructorbuddy.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.units.Unit

class PreferencesNested : PreferenceFragmentCompat() {

    private val args: PreferencesNestedArgs by navArgs()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference, args.rootKey)
        preferenceScreen.dependency = ""


    }


    /*   override fun onNavigateToScreen(preferenceScreen: PreferenceScreen?) {
           super.onNavigateToScreen(preferenceScreen)

           findNavController().navigate(
               PreferencesDirections.changeRoot(preferenceScreen!!.key)
           )
       }*/

}