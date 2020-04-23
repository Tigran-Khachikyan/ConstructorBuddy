package com.calcprojects.constructorbuddy.ui.settings

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.price.Currency
import com.calcprojects.constructorbuddy.model.units.Unit

class Preferences : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        //units
        val listUnits: ListPreference? = findPreference("key_units")
        val unitEntry =
            Unit.values().map { resources.getString(it.nameRes) }.toTypedArray()
        val unitEntryValues = Unit.values().map { it.name }.toTypedArray()
        listUnits?.apply {
            entries = unitEntry
            entryValues = unitEntryValues
            setDefaultValue(entryValues[0])
        }

        //currencies
        val listRates: ListPreference? = findPreference("key_rates")
        val ratesEntry =
            Currency.values().map { resources.getString(it.nameRes) }.toTypedArray()
        val ratesEntryValues = Currency.values().map { it.name }.toTypedArray()
        listRates?.apply {
            entries = ratesEntry
            entryValues = ratesEntryValues
            setDefaultValue(entryValues[0])
        }

    }

    override fun onNavigateToScreen(preferenceScreen: PreferenceScreen?) {
        super.onNavigateToScreen(preferenceScreen)

        findNavController().navigate(PreferencesDirections.openMaterialSettings())
    }

}