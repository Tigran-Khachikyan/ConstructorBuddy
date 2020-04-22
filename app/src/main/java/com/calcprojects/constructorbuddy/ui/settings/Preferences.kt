package com.calcprojects.constructorbuddy.ui.settings

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.calcprojects.constructorbuddy.R
import com.calcprojects.constructorbuddy.model.units.Unit

class Preferences : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)

        val listUnits: ListPreference? = findPreference("key_units")
        val unitEntry =
            Unit.values().map { unit -> resources.getString(unit.nameRes) }.toTypedArray()
        val unitEntryValues = Unit.values().map { unit -> unit.name }.toTypedArray()
        listUnits?.apply {
            entries = unitEntry
            entryValues = unitEntryValues
            setDefaultValue(entryValues[0])
        }
    }
}